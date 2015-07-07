package com.alice.loadersmscontactstest;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by Administrator on 15-7-7.
 * Project: LoaderSmsContactsTest
 * User: Alice
 * Data: 15-7-7
 */
public class SmsQuaryNameAsyncLoader extends AsyncTaskLoader<String> {
    private static final String TAG = "SmsQuaryNameAsyncLoader";
    private Context mContext;
    private String address;

    public SmsQuaryNameAsyncLoader(Context mContext, String address) {
        super(mContext);
        this.mContext = mContext;
        this.address = address;
    }

    @Override
    public String loadInBackground() {
        return getContactName(address);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public String getContactName(String address) {
        String name = null;
        if (address.startsWith("+86")) {
            address = address.substring(3);
//            Log.e(TAG, address);
        }
        String address2 = "";
        if (address.length() == 11) {//手机号码为11位，进行该转换
            address2 = address.substring(0, 3) + " " + address.substring(3, 7) + " " + address.substring(7);
        }
//数据库中的电话存储方式18310416993 或者183 10416993
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                ContactsContract.Data.DATA1 + " = ? or " + ContactsContract.Data.DATA1 + " like ?",
                new String[]{address, address2}, null);
        Log.e(TAG, "cursor " + cursor.getCount());
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
            if (index != -1) {
                String rawContactsId = cursor.getString(index);
                Cursor cursor2 = resolver.query(uri, new String[]{ContactsContract.Data.DATA1},
                        ContactsContract.Data.MIMETYPE + "=? and " + ContactsContract.Data.RAW_CONTACT_ID + "=?",
                        new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, rawContactsId}, null);
                Log.e(TAG, "cursor2 " + cursor2.getCount());
                while (cursor2.moveToNext()) {
                    index = cursor2.getColumnIndex(ContactsContract.Data.DATA1);
                    if (index != -1) {
                        name = cursor2.getString(index);
                        Log.e(TAG, name);
                        break;
                    }
                }
                cursor2.close();
                if (name != null) {
                    break;
                }
            }
        }
        cursor.close();
        if (name == null) {
            name = address;
        }
        return name;
    }
}
