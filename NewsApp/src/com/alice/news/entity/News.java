package com.alice.news.entity;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class News {
    private Bitmap bitmap;
    private String title;
    private String time;
    private String content;

    public News(String content, Bitmap bitmap, String title, String time) {
        this.content = content;
        this.bitmap = bitmap;
        this.title = title;
        this.time = time;
    }

    public News() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
