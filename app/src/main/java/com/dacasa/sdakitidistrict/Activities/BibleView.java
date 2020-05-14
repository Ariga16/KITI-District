package com.dacasa.sdakitidistrict.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dacasa.sdakitidistrict.Adapters.BibleBooksAdapter;
import com.dacasa.sdakitidistrict.Adapters.BibleVerseAdapter;
import com.dacasa.sdakitidistrict.Adapters.PagerAdapter;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.Commoners.P;
import com.dacasa.sdakitidistrict.Fragments.VerseListing;
import com.dacasa.sdakitidistrict.NoteEditorActivity;
import com.dacasa.sdakitidistrict.Notes;
import com.dacasa.sdakitidistrict.NyimboZaKrist;
import com.dacasa.sdakitidistrict.Nyimbo_Details;
import com.dacasa.sdakitidistrict.POJOS.Book;
import com.dacasa.sdakitidistrict.POJOS.Chapter;
import com.dacasa.sdakitidistrict.POJOS.Verse;
import com.dacasa.sdakitidistrict.R;
import com.dacasa.sdakitidistrict.Setting;
import com.eftimoff.viewpagertransformers.StackTransformer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BibleView extends AppCompatActivity implements BibleBooksAdapter.BookListener, PagerAdapter.PageListener, BibleVerseAdapter.VerseListener
, VerseListing.ChapterInetractor {

    DrawerLayout drawerLayout;
    RecyclerView books_recycler;
    ViewPager viewPager;
    ArrayAdapter<String> chaptersSpinnerAdapter;
    PagerAdapter chapterAdapter;
    BibleBooksAdapter bibleBooksAdapter;
    ProgressDialog progressDialog;
    Spinner chapters_spinner;
    View navigator;
    AppBarLayout appBar;
    MenuItem nightMode;
    private boolean forResult = false;
    List<String> books = new ArrayList<>();
    private int book,chapter,verse_from,verse_to,chapters;
    Bible bible;
    DatabaseReference database;
    public ActionMode actionMode;
    //ActionModeCallback actionModeCallback = new ActionModeCallback();
    SharedPreferences preferences;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_view);

        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        forResult = intent.getBooleanExtra("forResult",false);
        bible = new Bible(this);
        book = intent.getIntExtra("book",1);
        // check
        chapter = intent.getIntExtra("chapter",-1);
        verse_from = intent.getIntExtra("verse_from",0);
        verse_to = intent.getIntExtra("verse_to",0);
        initUI();

        populateChapters();
        loadBooks();

        if (forResult){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BibleView.this, "Select a verse then press done", Toast.LENGTH_SHORT).show();
                }
            },1000);
        }
    }


    public void populateChapters(){
        chapterAdapter.clearAll();
        chaptersSpinnerAdapter.clear();
        chaptersSpinnerAdapter.notifyDataSetChanged();
        chapterAdapter = new PagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(chapterAdapter);
        // Chapter +1 (includes the last chapter)
        chapters = bible.bookChapters(book)+1;
        Log.e("CHAPTER DISCOVERED",chapters+"");
        Log.e("CHAPTER DISCOVERED", book + "," + chapter + "," + verse_from + "," + verse_to);
        if (chapter > 0){
            getSupportActionBar().setTitle(bible.bookName(book));
            //getSupportActionBar().setSubtitle("Chapter " + chapter);//changed from to
            getSupportActionBar().setSubtitle("Chapter 1");
            if (chapter>1){
                VerseListing verseListing = new VerseListing();
                verseListing.setArguments(P.chapterToBundle(new Chapter(book, chapter - 1)));
                chapterAdapter.addFrag(verseListing, (chapter-1)+"");
            }
            VerseListing verseListing = new VerseListing();
            Bundle bundle = P.chapterToBundle(new Chapter(book, chapter));
            bundle.putInt("from",verse_from);
            bundle.putInt("to",verse_to);
            verseListing.setArguments(bundle);
            chapterAdapter.addFrag(verseListing, (chapter) + "");
            VerseListing verseListing2 = new VerseListing();
            Bundle bundle2 = P.chapterToBundle(new Chapter(book, chapter + 1));
            verseListing2.setArguments(bundle2);
            chapterAdapter.addFrag(verseListing2, (chapter + 1) + "");
            chapterAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(chapterAdapter.getCount() == 3 ? 1 : 0);
            chapters_spinner.setVisibility(View.GONE);
            appBar.setExpanded(false,false);
            return;
        }



        for (int a = 1; a < chapters; a++) {
            VerseListing verseListing = new VerseListing();
            verseListing.setArguments(P.chapterToBundle(new Chapter(book, a)));
            chapterAdapter.addFrag(verseListing, (a) + "");
            chaptersSpinnerAdapter.add("Chapter "+a);
        }
        chapterAdapter.notifyDataSetChanged();


        chapters_spinner.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(bible.bookName(book));
        getSupportActionBar().setSubtitle("Chapter 1");

    }




    public void initUI(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        View body = getLayoutInflater().inflate(R.layout.bible_side_nav,navigationView,false);
        navigationView.addHeaderView(body);
        books_recycler = (RecyclerView)body.findViewById(R.id.recycler);
        books_recycler.setHasFixedSize(false);
        bibleBooksAdapter = new BibleBooksAdapter(this,R.layout.row_book_small);
        books_recycler.setAdapter(bibleBooksAdapter);
        books_recycler.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_bars).color(Color.WHITE).sizeDp(20));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        chapterAdapter = new PagerAdapter(getSupportFragmentManager(),this);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(chapterAdapter);
        viewPager.setPageTransformer(true,new StackTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSubtitle("Chapter "+chapterAdapter.getPageTitle(position));
                chapters_spinner.setSelection(position);
                if (actionMode != null){
                    actionMode.finish();
                    try {
                        ((VerseListing)chapterAdapter.getItem(position-1)).clearSelection();
                    }catch (Exception e){}
                    try {
                        ((VerseListing)chapterAdapter.getItem(position+1)).clearSelection();
                    }catch (Exception e){}
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigator = findViewById(R.id.chapter_navigator);
        View move_left = navigator.findViewById(R.id.move_left);
        View move_right = navigator.findViewById(R.id.move_right);
        chapters_spinner = (Spinner)navigator.findViewById(R.id.chapters);
        chaptersSpinnerAdapter = new ArrayAdapter<String>(this,R.layout.chapter_spinner_item);
        chapters_spinner.setAdapter(chaptersSpinnerAdapter);
        chapters_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        move_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() > 0 ? viewPager.getCurrentItem() - 1 : 0);
            }
        });
        move_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() < chapterAdapter.getCount() - 1 ? viewPager.getCurrentItem() + 1 : chapterAdapter.getCount() - 1);
            }
        });
        move_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewPager.setCurrentItem(chapterAdapter.getCount() - 1);
                return true;
            }
        });
        move_left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewPager.setCurrentItem(0);
                return true;
            }
        });

        appBar = (AppBarLayout)findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                navigator.setTranslationY(-(verticalOffset * 2));
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Setting up...");
    }



    @Override
    public void verseLongClicked(Verse verse) {
        if (actionMode == null) {
            //actionMode = startSupportActionMode(actionModeCallback);
        }
        ((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).verseLongClicked(verse);
    }



    @Override
    public void verseClicked(Verse verse) {
        if (actionMode != null){
            ((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).verseClicked(verse);
            return;
        }
    }

    @Override
    public void bookClicked(Book book) {
        this.book = book.getIndex();
        getSupportActionBar().setTitle(bible.bookName(this.book));
        getSupportActionBar().setSubtitle("Chapter 1");
        chapter = -1; verse_from = 0; verse_to = 0;
        populateChapters();
        drawerLayout.closeDrawers();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else {
            super.onBackPressed();
        }
    }

    public void loadBooks(){
//        if (progressDialog.isShowing())progressDialog.dismiss();
//        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i<67;i++){
                    String name = bible.bookName(i);
                    Book book = new Book(i,0,name);
                    bibleBooksAdapter.addBook(book);
                }
                mainHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            bibleBooksAdapter.notifyDataSetChanged();
//            progressDialog.dismiss();
            drawerLayout.scrollTo(0,0);
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bible_view, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setGravity(Gravity.TOP);
        searchView.setQueryHint("Search...");
        nightMode = menu.findItem(R.id.night_mode);
        nightMode.setTitle(preferences.getBoolean("nightMode", false) ? "Normal mode" : "Night Mode");
        return true;
    }



    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return ((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).onQueryTextChange(newText);
        }
    };


    public void highlight(){
        List<Verse> verses = (((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).getSelection());
        if (verses.isEmpty())return;
        Collections.sort(verses);
        int min = verses.get(0).getVerse_no();
        int max = verses.get(verses.size() - 1).getVerse_no();
        if (min>max){
            int a = min;
            min = max;
            max = a;
        }
        if (verses.size()>1)verses = (((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).getVerses()).subList(min,max);
        Verse verse = verses.get(0);
        String key = database.child("highlights").child(user.getUid()).push().getKey();
        String book = bible.bookName(verse.getBook());
        //Note note = new Note(key,verse.getVerse(),book+": Ch "+verse.getChapter()+": v "+(min == max?min:min+" - "+max),
               // verse.getBook(),verse.getChapter(),min,max,System.currentTimeMillis());
        //database.child("highlights").child(user.getUid()).child(key).setValue(note);
    }



    public void addNote(){
        List<Verse> verses = (((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).getSelection());
        if (verses.isEmpty())return;
        Collections.sort(verses);
        int min = verses.get(0).getVerse_no();
        int max = verses.get(verses.size() - 1).getVerse_no();
        if (min>max){
            int a = min;
            min = max;
            max = a;
        }
        Verse v = verses.get(0);
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra("b",v.getBook());
        intent.putExtra("c",v.getChapter());
        intent.putExtra("from",min);
        intent.putExtra("to",max);
        startActivity(intent);
    }

    public void returnSelection(){
        List<Verse> verses = (((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).getSelection());
        if (verses.isEmpty())return;
        Collections.sort(verses);
        int min = verses.get(0).getVerse_no();
        int max = verses.get(verses.size() - 1).getVerse_no();
        if (min>max){
            int a = min;
            min = max;
            max = a;
        }
        Verse v = verses.get(0);
        Intent intent = new Intent();
        intent.putExtra("b",v.getBook());
        intent.putExtra("c",v.getChapter());
        intent.putExtra("from",min);
        intent.putExtra("to",max);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.night_mode) {
            for (Fragment fragment:chapterAdapter.getFragments()){
                ((VerseListing)fragment).switchMode(!preferences.getBoolean("nightMode", false));
            }
            preferences.edit().putBoolean("nightMode",!preferences.getBoolean("nightMode",false)).commit();
            return true;
        }

        if (id == R.id.nyimbo_za_kristo) {

            Intent intent = new Intent(getApplicationContext(), NyimboZaKrist.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void pageCount(int size) {

    }

    @Override
    public void onVerseSelectionChange(List<Verse> selection) {
        Collections.sort(selection);
        try {
            if (selection.size()>1){
                actionMode.setTitle("Verse "+selection.get(0).getVerse_no()+" to "+selection.get(selection.size()-1).getVerse_no());
            }else {
                actionMode.setTitle("Verse "+selection.get(0).getVerse_no());
            }
        }catch (Exception e){}
    }





    public void share(List<Verse> verses){
        StringBuilder text = new StringBuilder();
        Verse verse = verses.get(0);
        Collections.sort(verses);
        int min = verses.get(0).getVerse_no();
        int max = verses.get(verses.size() - 1).getVerse_no();
        if (min>max){
            int a = min;
            min = max;
            max = a;
        }
        if (verses.size()>1) verses = (((VerseListing)chapterAdapter.getItem(viewPager.getCurrentItem())).getVerses()).subList(min,max);
        text.append(bible.bookName(verse.getBook())).append(" Chapter "+verse.getChapter()+"\nVerse "+min+" to "+max+"\n\n");
        for (Verse vers:verses){
            text.append(vers.getVerse()+"\n");
        }
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text.toString());
        startActivity(share);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



}
