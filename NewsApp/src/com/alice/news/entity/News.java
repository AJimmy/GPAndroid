package com.alice.news.entity;

/**
 * Created by Administrator on 15-6-27.
 * Project: MyAssistance
 * User: Alice
 * Data: 15-6-27
 */
public class News {
    private String cover;
    private String subject;
    private String summary;
    private String changed;

    public News(String summary, String subject, String cover, String changed) {
        this.summary = summary;
        this.subject = subject;
        this.cover = cover;
        this.changed = changed;
    }

    public String getChanged() {
        return changed;
    }

    public void setChanged(String changed) {
        this.changed = changed;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
