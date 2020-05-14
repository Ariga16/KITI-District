package com.dacasa.sdakitidistrict.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dacasa.sdakitidistrict.Adapters.BibleVerseAdapter;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.Commoners.P;
import com.dacasa.sdakitidistrict.POJOS.Chapter;
import com.dacasa.sdakitidistrict.POJOS.Verse;
import com.dacasa.sdakitidistrict.R;

import java.util.ArrayList;
import java.util.List;


public class VerseListing extends Fragment {

    RecyclerView recycler;
    BibleVerseAdapter verseAdapter;
    Chapter chapter;
    int verse_from,verse_to;
    Bible bible;
    SharedPreferences preferences;
    ChapterInetractor chapterInetractor;

    public VerseListing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bible = new Bible(getActivity());
        Bundle args = getArguments();
        chapter = P.chapterFromBundle(args);
        verse_from = args.getInt("from",0);
        verse_to = args.getInt("to",bible.getVerses(chapter).size());
        chapterInetractor = (ChapterInetractor)getActivity();
        View v = inflater.inflate(R.layout.fragment_verse_listing, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        recycler = (RecyclerView)v.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        verseAdapter = new BibleVerseAdapter(getActivity());
        recycler.setAdapter(verseAdapter);

        switchMode(preferences.getBoolean("nightMode",false));
        reLoadData(false);

        return v;
    }

    public void reLoadData(boolean clear){
        if (clear)verseAdapter.clearVerses();
        verseAdapter.addAllVerses(bible.getVerses(chapter));

        if (verse_from >0){
            if (verse_to>0)verseAdapter.highlight(verse_from,verse_to);
            recycler.smoothScrollToPosition(verse_from);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        verseAdapter.clearSelection();
    }

    public void verseLongClicked(Verse verse) {
        verseAdapter.toggleSelection(verse);
        chapterInetractor.onVerseSelectionChange(verseAdapter.getSelection());
    }

    public void verseClicked(Verse verse) {
        verseAdapter.toggleSelection(verse);
        chapterInetractor.onVerseSelectionChange(verseAdapter.getSelection());
    }

    public void clearSelection(){
        verseAdapter.clearSelection();
    }


    public List<Verse> getSelection(){
        return verseAdapter.getSelection();
    }


    public List<Verse> getVerses(){
        return verseAdapter.getVerses();
    }


    public boolean switchMode(boolean nightMode){
        if (verseAdapter == null)return false;
        verseAdapter.toggleNightMode(nightMode);
        if (nightMode){
            recycler.setBackgroundColor(getResources().getColor(R.color.night_mode));
        }else {
            recycler.setBackgroundColor(Color.WHITE);
        }
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Verse> filterList = filter(verseAdapter.getVerses(), newText);
        if(newText != null || newText != ""){
            if (newText.length()>0){
                verseAdapter.animateTo(filterList,newText);
                recycler.scrollToPosition(0);
            }else {
                verseAdapter.noQuery();
                verseAdapter.reload();
            }
        }else {
            verseAdapter.noQuery();
        }
        return true;
    }


    private List<Verse> filter(List<Verse> list, String query){
        query = query.toLowerCase();
        final List<Verse> filterlist = new ArrayList<>();
        for(Verse book: list){
            String text = book.getVerse().toLowerCase();
            if(text.contains(query) && !filterlist.contains(book)){
                filterlist.add(book);
            }
        }
        return filterlist;
    }



    public interface ChapterInetractor{
        void onVerseSelectionChange(List<Verse> selection);
    }




}
