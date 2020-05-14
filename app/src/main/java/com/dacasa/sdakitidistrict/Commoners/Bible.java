package com.dacasa.sdakitidistrict.Commoners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dacasa.sdakitidistrict.POJOS.Chapter;
import com.dacasa.sdakitidistrict.POJOS.Verse;

import java.util.ArrayList;
import java.util.List;


public class Bible extends SQLiteOpenHelper {

    public Bible(Context context) {
        super(context, "bible.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public String bookName(int book_index){
        String name = null;
        SQLiteDatabase db = getDb();
        Cursor c = db.rawQuery("SELECT n FROM key_english WHERE b = "+book_index,null);
        if (c.moveToFirst()){
            name = c.getString(0);
        }
        db.close();
        return name;
    }

    public int bookChapters(int book_index){
        int name = 0;
        SQLiteDatabase db = getDb();
        Cursor c = db.rawQuery("SELECT MAX(c) FROM t_kjv WHERE b = "+book_index,null);
        if (c.moveToFirst()){
            name = c.getInt(0);
        }
        db.close();
        return name;
    }


    public List<Verse> getVerses(Chapter chapter){
        List<Verse> verses = new ArrayList<>();
        SQLiteDatabase db = getDb();
        if (db == null)return verses;
        Cursor cursor = db.rawQuery("SELECT*FROM t_kjv WHERE b = "+chapter.getBook()+" AND c = "+chapter.getChapter()+"",null);
        if (cursor.moveToFirst()){
            do {
                Verse verse = new Verse(
                        chapter.getBook(),
                        chapter.getChapter(),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
                verses.add(verse);
            }while (cursor.moveToNext());
        }else {
            Log.e("FROM DB","Nothing found in db");
        }

        db.close();
        return verses;
    }


    public List<Verse> getVerses(int book,int chapter,int from, int to){
        List<Verse> verses = new ArrayList<>();
        SQLiteDatabase db = getDb();
        if (db == null)return verses;
        Cursor cursor = db.rawQuery("SELECT*FROM t_kjv WHERE b = "+book+" AND c = "+chapter+" AND v BETWEEN "+from+" AND "+to,null);
        if (cursor.moveToFirst()){
            do {
                Verse verse = new Verse(
                        book,
                        chapter,
                        cursor.getInt(3),
                        cursor.getString(4)
                );
                verses.add(verse);
            }while (cursor.moveToNext());
        }else {
            Log.e("FROM DB","Nothing found in db");
        }

        db.close();
        return verses;
    }


    public SQLiteDatabase getDb(){
        if (!com.dacasa.sdakitidistrict.Commoners.P.bibleAvailable())return null;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(com.dacasa.sdakitidistrict.Commoners.P.bible_path+"/bible.db",null,SQLiteDatabase.OPEN_READWRITE);
        return db;
    }





}
