package com.alice.loadersmscontactstest;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Administrator on 15-7-4.
 * Project: LoaderSmsContactsTest
 * User: Alice
 * Data: 15-7-4
 */
public class ContactDetailActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ContactDetailActivity";
    public static final String NAME = "name";
    public static final String PHONE1 = "phone1";
    public static final String PHONE2 = "phone2";
    public static final String EMAIL = "email";

    private TextView txtName;
    private TextView txtPhone1;
    private TextView txtPhone2;
    private TextView txtEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);


        txtName = (TextView) findViewById(R.id.txt_name);
        txtPhone1 = (TextView) findViewById(R.id.txt_phone1);
        txtPhone2 = (TextView) findViewById(R.id.txt_phone2);
        txtEmail = (TextView) findViewById(R.id.txt_email);


        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        Log.e(TAG, "ContactDetailActivity 返回id " + id);
        LoaderManager manager = getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("uri", ContactsContract.Data.CONTENT_URI.toString());
        manager.initLoader(103, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (args != null) {
            int idRawContact = args.getInt("id");//raw_contact_id表的id
            String uri = args.getString("uri");
            loader = new CursorLoader(this, Uri.parse(uri),
                    new String[]{ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1},
                    ContactsContract.Data.RAW_CONTACT_ID + "=?",
                    new String[]{String.valueOf(idRawContact)},
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "onLoadFinished cursor getCount() " + data.getCount());
        while (data.moveToNext()) {
            int index = data.getColumnIndex(ContactsContract.Data.MIMETYPE);
            Log.e(TAG, "ContactsContract.Data.MIMETYPE " + index);
            if (index != -1) {
                String mimeTypeStr = data.getString(index);
                Log.e(TAG, "mimeTypeStr " + mimeTypeStr);
                if (mimeTypeStr != null) {
                    index = data.getColumnIndex(ContactsContract.Data.DATA1);
                    String data1 = data.getString(index);
                    Log.e(TAG, "data1 " + data1);
                    switch (mimeTypeStr) {
                        case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                            txtName.setText(data1);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                            if (txtPhone1.getText().toString().equals("")) {
                                txtPhone1.setText(data1);
                            } else if (txtPhone2.getText().toString().equals("")) {
                                txtPhone2.setText(data1);
                            }

                            break;
                        case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                            txtEmail.setText(data1);
                            break;
                    }
                }
            }
        }
//        刷新界面
        txtName.invalidate();
        txtPhone1.invalidate();
        txtPhone2.invalidate();
        txtEmail.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}