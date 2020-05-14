package com.dacasa.sdakitidistrict.POJOS;

public class Book {

    private int index,chapters;
    String name;

    public Book(int index, int chapters) {
        this.index = index;
        this.chapters = chapters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book(int index, int chapters, String name) {
        this.index = index;
        this.chapters = chapters;
        this.name = name;
    }

    public Book() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

}
