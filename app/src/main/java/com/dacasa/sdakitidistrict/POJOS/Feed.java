package com.dacasa.sdakitidistrict.POJOS;

public class Feed {

    private String id,message,title,subtitle;
    private int book,chapter,from_verse,to_verse;
    private long date;
    private int type;
    private String image_url;
    private boolean toParish;

    public Feed() {
    }



    public Feed(String id, String message, String title,String subtitle, int book, int chapter, int from_verse, int to_verse, long date, int type) {
        this.id = id;
        this.subtitle = subtitle;
        this.message = message;
        this.title = title;
        this.book = book;
        this.chapter = chapter;
        this.from_verse = from_verse;
        this.to_verse = to_verse;
        this.date = date;
        this.type = type;
    }

    public Feed(String id, String message, String title,String subtitle, int book, int chapter, int from_verse, int to_verse, long date, int type, String image_url, boolean toParish) {
        this.id = id;
        this.message = message;
        this.title = title;
        this.book = book;
        this.chapter = chapter;
        this.from_verse = from_verse;
        this.to_verse = to_verse;
        this.date = date;
        this.subtitle = subtitle;
        this.type = type;
        this.image_url = image_url;
        this.toParish = toParish;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getFrom_verse() {
        return from_verse;
    }

    public void setFrom_verse(int from_verse) {
        this.from_verse = from_verse;
    }

    public int getTo_verse() {
        return to_verse;
    }

    public void setTo_verse(int to_verse) {
        this.to_verse = to_verse;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isToParish() {
        return toParish;
    }

    public void setToParish(boolean toParish) {
        this.toParish = toParish;
    }



}
