package com.alice.loadersmscontactstest;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Administrator on 15-7-4.
 * Project: LoaderSmsContactsTest
 * User: Alice
 * Data: 15-7-4
 */
public class DatabaseLoader extends AsyncTaskLoader<Cursor> {
    private String uriStr;

    public void setUriStr(String uriStr) {
        this.uriStr = uriStr;
    }

    public DatabaseLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;
        Uri uri = Uri.parse(uriStr);
        ContentResolver resolver = getContext().getContentResolver();
        if (resolver != null) {
            cursor = resolver.query(uri, null, null, null, "date desc");
        }

        return cursor;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
