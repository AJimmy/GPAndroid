package com.alice.downloadmanager.service;

/**
 * Created by Administrator on 15-7-13.
 */
public class DownloadTask {
    public long current;
    public long total;
    public String url;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
