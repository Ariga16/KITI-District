package com.dacasa.sdakitidistrict.POJOS;

public class Verse implements Comparable<Verse> {

    private int book,chapter,verse_no;
    String verse;

    public Verse() {
    }

    public Verse(int book, int chapter, int verse_no, String verse) {
        this.book = book;
        this.chapter = chapter;
        this.verse_no = verse_no;
        this.verse = verse;
    }

    public int getVerse_no() {
        return verse_no;
    }

    public void setVerse_no(int verse_no) {
        this.verse_no = verse_no;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }



    @Override
    public int compareTo(Verse verse) {
        int compareVerse = verse.getVerse_no();
        return this.verse_no - compareVerse;
    }

    @Override
    public String toString() {
        return "Verse{" +
                "book=" + book +
                ", chapter=" + chapter +
                ", verse_no=" + verse_no +
                ", verse='" + verse + '\'' +
                '}';
    }


}

