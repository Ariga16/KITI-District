package com.dacasa.sdakitidistrict.Models;

import com.google.firebase.database.ServerValue;

public class Post {


    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private String userPhoto;
    private String username;
    private Object timeStamp;
    private int book;
    private int chapter;
    private int from_verse;
    private int to_verse;


    public Post(String title, String description, String picture, String userId, String userPhoto,String username, int book, int chapter, int from_verse, int to_verse) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.username = username;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.book = book;
        this.chapter = chapter;
        this.from_verse = from_verse;
        this.to_verse = to_verse;

    }

    // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUsername() {
        return username;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    // add verse

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



    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}

