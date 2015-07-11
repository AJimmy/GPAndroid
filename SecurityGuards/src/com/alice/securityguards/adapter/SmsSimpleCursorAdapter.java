package com.alice.securityguards.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.alice.securityguards.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 15-7-4.
 * Project: LoaderSmsContactsTest
 * User: Alice
 * Data: 15-7-4
 */
public class SmsSimpleCursorAdapter extends SimpleCursorAdapter {
    private static final String TAG = "SmsSimpleCursorAdapter";
    private Context mContext;
    private Cursor cursor;

    public SmsSimpleCursorAdapter(Context mContext, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(mContext, layout, c, from, to, flags);
        this.mContext = mContext;
        this.cursor = c;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //TODO 电话号码的数据查找需要在Loder中吗？
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.txtTime = (TextView) view.findViewById(R.id.item_black_sms_time);
            view.setTag(holder);
        }

        String dataStr = cursor.getString(cursor.getColumnIndex("time"));
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(Long.parseLong(dataStr));
        holder.txtTime.setText(format.format(date));
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

    class ViewHolder {
        TextView txtTime;
    }
}
