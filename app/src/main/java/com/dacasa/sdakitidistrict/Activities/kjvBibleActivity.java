package com.dacasa.sdakitidistrict.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dacasa.sdakitidistrict.Adapters.BibleBooksAdapter;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.Commoners.P;
import com.dacasa.sdakitidistrict.POJOS.Book;
import com.dacasa.sdakitidistrict.POJOS.Chapter;
import com.dacasa.sdakitidistrict.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.pitt.library.fresh.FreshDownloadView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class kjvBibleActivity extends AppCompatActivity implements BibleBooksAdapter.BookListener {

    View downloader,download_prompt,downloading_view,quick_nav,go,cover;
    FreshDownloadView progressBar;
    TextView percent_download,progress;
    ImageButton download_button;
    RecyclerView recycler;
    Bible bible;
    StorageReference storage;
    BibleBooksAdapter bibleBooksAdapter;
    ArrayAdapter<String> booksAdapter,chaptersAdapter,verseAdapter;
    DatabaseReference database;
    FileDownloadTask downloadTask;
    Spinner books,chapters,verses;
    private  boolean downloading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kjv_bible);
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sda-kiti-district-9a0cf.appspot.com");
        database = FirebaseDatabase.getInstance().getReference();
        bible = new Bible(this);
        initUI();
    }

    public void initUI(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("The Holy Bible");

        downloader = findViewById(R.id.downloader);
        progressBar = findViewById(R.id.progressBar);
        percent_download = (TextView)findViewById(R.id.percentage_download);
        progress = (TextView)findViewById(R.id.progress);
        download_button = (ImageButton)findViewById(R.id.download_bible);
        download_prompt = findViewById(R.id.download_prompt);
        downloading_view = findViewById(R.id.downloading_view);
        quick_nav = findViewById(R.id.quick_nav);
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBible();
            }
        });

        books = (Spinner)findViewById(R.id.book);
        chapters = (Spinner)findViewById(R.id.chapter);
        verses = (Spinner)findViewById(R.id.verse);

        if (P.bibleAvailable()){
            populateRecycler();
            downloader.setVisibility(View.GONE);
            quick_nav.setVisibility(View.VISIBLE);
        }else {
            downloader.setVisibility(View.VISIBLE);
            quick_nav.setVisibility(View.GONE);
        }

        go = findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),BibleView.class);
                intent.putExtra("book",books.getSelectedItemPosition());
                intent.putExtra("chapter",chapters.getSelectedItemPosition()+1);
                intent.putExtra("verse_from",verses.getSelectedItemPosition());
                startActivity(intent);
            }
        });

        cover = findViewById(R.id.cover);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cover.animate().translationX(-700).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        cover.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }
        },2000);
    }

    public void populateRecycler(){
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        bibleBooksAdapter = new BibleBooksAdapter(this,R.layout.row_book);
        recycler.setAdapter(bibleBooksAdapter);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        booksAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        booksAdapter.add("Book");
        books.setAdapter(booksAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] books = new String[67];
                for (int i = 1; i<67;i++){
                    String name = bible.bookName(i);
                    Book book = new Book(i,0,name);
                    books[i-1] = name;
                    bibleBooksAdapter.addBook(book);
                    mainHandler.sendEmptyMessage(0);
                }
                Message message = new Message();
                message.getData().putStringArray("books", books);
                mainHandler.sendMessage(message);
            }
        }).start();

        final ArrayAdapter<String> chaptersAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        chaptersAdapter.add("Chapter");
        chapters.setAdapter(chaptersAdapter);

        final ArrayAdapter<String> versesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        versesAdapter.add("Verse");
        verses.setAdapter(versesAdapter);

        books.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chaptersAdapter.clear();
                //+1 chapter (adds the last Chapter)
                int chapters = bible.bookChapters(i);
                if (chapters<1)chaptersAdapter.add("Chapter");
                for (int a = 1; a < chapters; a++) {
                    chaptersAdapter.add("Ch " + a);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        chapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int a, long l) {
                List verses = bible.getVerses(new Chapter(books.getSelectedItemPosition(),a+1));
                versesAdapter.clear();
                if (verses.isEmpty())versesAdapter.add("Verse");
                for (int i = 1; i<verses.size();i++){
                    versesAdapter.add("Vs "+i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String[]books = msg.getData().getStringArray("books");
            if (books == null){
                bibleBooksAdapter.notifyDataSetChanged();
            }else {
                booksAdapter.addAll(books);
            }
        }
    };

    @Override
    public void bookClicked(Book book) {
        Intent intent = new Intent(this,BibleView.class);
        intent.putExtra("book",book.getIndex());
        startActivity(intent);
    }
    public void downloadBible(){
        progressBar.reset();
        downloading_view.setVisibility(View.VISIBLE);
        download_prompt.setVisibility(View.GONE);
        downloading = true;
        File dest = new File(P.bible_path);
        dest.mkdirs();
        File to_file = null;
        to_file = new File(P.bible_path+"/bible.db");
        if (to_file.exists())to_file.delete();
        downloadTask = storage.child("Bible").child("bible.db").getFile(to_file);
        downloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                progressBar.showDownloadOk();
                populateRecycler();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        P.exitReveal(downloader,"center");
                        quick_nav.setVisibility(View.VISIBLE);
                        downloading = false;
                    }
                },2000);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(final Exception e) {
                progressBar.showDownloadError();
                progress.setVisibility(View.VISIBLE);
                progress.setText("Download Failed");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        downloading_view.setVisibility(View.GONE);
                        download_prompt.setVisibility(View.VISIBLE);
                        Log.e("FAILURE IN DOWNLOAD",e.getMessage());
                        downloading = false;
                        progress.setText(null);
                        progress.setVisibility(View.GONE);
                    }
                },2000);
                e.printStackTrace();
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                DecimalFormat df = new DecimalFormat("####0.00");
                long total = taskSnapshot.getTotalByteCount();
                long transferred = taskSnapshot.getBytesTransferred();
                percent_download.setText(df.format(((P.bytesToDouble(transferred) / P.bytesToDouble(total)) * 100)) + "%");
                progress.setText((P.bytesToMB(transferred) + "/" + P.bytesToMB(total) + " MB"));
                progressBar.upDateProgress((float)((P.bytesToDouble(transferred) / P.bytesToDouble(total))));
            }
        });
    }
    private int backPresses = 0;
    @Override
    public void onBackPressed() {
        backPresses++;
        if (downloading && backPresses <2){
            Toast.makeText(kjvBibleActivity.this, "Exiting will cancel the download", Toast.LENGTH_LONG).show();
            return;
        }
        if (downloading){
            downloadTask.cancel();
            Toast.makeText(kjvBibleActivity.this, "Download cancelled", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bible, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setGravity(Gravity.TOP);
        searchView.setQueryHint("Search book..");
        return true;
    }


    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            List<Book> filterList = filter(bibleBooksAdapter.getBooks(), newText);
            if(newText != null || newText != ""){
                if (newText.length()>0){
                    bibleBooksAdapter.animateTo(filterList);
                    recycler.scrollToPosition(0);
                }else {
                    bibleBooksAdapter.reload();
                }
            }
            return true;
        }
    };


    private List<Book> filter(List<Book> list, String query){
        query = query.toLowerCase();
        final List<Book> filterlist = new ArrayList<>();
        for(Book book: list){
            String text = book.getName().toLowerCase();
            if(text.contains(query) && !filterlist.contains(book)){
                filterlist.add(book);
            }
        }
        return filterlist;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
