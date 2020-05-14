package com.dacasa.sdakitidistrict.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content,uid,uname,uimg;
    private Object timestamp;


    public Comment(String content, String uid,String uimg, String uname) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.uimg = uimg;
        this.timestamp = ServerValue.TIMESTAMP;

    }

    public Comment(String content, String uid, String uname,String uimg, Object timestamp) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.uimg = uimg;
        this.timestamp = timestamp;
    }

    public Comment() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getUname() {
        return uname;
    }

    public String getUimg() {
        return uimg;
    }
    public void setUimg(String uimg) {
        this.uimg = uimg;
    }


    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
