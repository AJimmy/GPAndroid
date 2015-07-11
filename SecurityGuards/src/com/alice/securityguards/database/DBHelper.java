package com.alice.securityguards.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 15-7-10.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-10
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "blacklist.db";
    public static final String CREATE_TABLE_CONTACTS = "CREATE TABLE black_contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, raw_contacts_id INTEGER NOT NULL, display_name TEXT, phone TEXT, phone2 TEXT)";
    public static final String CREATE_TABLE_SMS = "CREATE TABLE black_sms (_id INTEGER PRIMARY KEY AUTOINCREMENT, phone TEXT, display_name TEXT, time long, body TEXT)";
    public static final String TABLE_CONTACTS = "black_contacts";
    public static final String TABLE_SMS = "black_sms";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_SMS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
