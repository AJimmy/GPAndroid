package com.alice.securityguards.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.alice.securityguards.database.DBHelper;
import org.apache.http.client.utils.URIUtils;

/**
 * Created by Administrator on 15-7-11.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-11
 */

//TODO 自定义数据表格的ContentProvider
public class BlackContentProvider extends ContentProvider {
    public static final String URI = "content://com.alice.smsguards/";
    public static final String AUTHORITY = "com.alice.smsguards";
    public static final String CONTACTS_URI = URI+DBHelper.TABLE_CONTACTS;
    public static final String SMS_URI = URI+DBHelper.TABLE_SMS;

    public static final int CONTACTS_CODE = 301;
    public static final int SMS_CODE = 302;

    private static final UriMatcher matcher = new UriMatcher(0);
    private DBHelper dbHelper;

    static {
        matcher.addURI(AUTHORITY, DBHelper.TABLE_CONTACTS, CONTACTS_CODE);
        matcher.addURI(AUTHORITY, DBHelper.TABLE_SMS, SMS_CODE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (matcher.match(uri)) {
            case CONTACTS_CODE:
                cursor = database.query(DBHelper.TABLE_CONTACTS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SMS_CODE:
                cursor = database.query(DBHelper.TABLE_SMS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri); //使Loader自动更新数据
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        Uri uriNew = null;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (matcher.match(uri)) {
            case CONTACTS_CODE:
                id = database.insert(DBHelper.TABLE_CONTACTS, null, values);
                break;
            case SMS_CODE:
                id = database.insert(DBHelper.TABLE_SMS, null, values);
                break;
        }
        if (id > 0) {
            uriNew = Uri.withAppendedPath(uri, String.valueOf(id));
        }
        getContext().getContentResolver().notifyChange(uriNew, null);
        return uriNew;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int ret = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (matcher.match(uri)){
            case CONTACTS_CODE:
                ret = database.delete(DBHelper.TABLE_CONTACTS, selection, selectionArgs);
                break;
            case SMS_CODE:
                ret = database.delete(DBHelper.TABLE_SMS, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int ret = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (matcher.match(uri)){
            case CONTACTS_CODE:
                ret = database.update(DBHelper.TABLE_CONTACTS, values, selection, selectionArgs);
                break;
            case SMS_CODE:
                ret = database.update(DBHelper.TABLE_SMS, values, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }
}
