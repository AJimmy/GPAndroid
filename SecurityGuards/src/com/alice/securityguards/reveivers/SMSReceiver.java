package com.alice.securityguards.reveivers;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.alice.securityguards.database.DBHelper;
import com.alice.securityguards.providers.BlackContentProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 15-7-11.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-11
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");

        String action = intent.getAction();
        if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.e(TAG, bundle.toString());

//短息中pdu代表短信内部的数据结构
                Object[] objects = (Object[]) bundle.get("pdus");

                if (objects != null) {
                    for (Object pdu : objects) {
                        Log.e(TAG, "pdus " + pdu.toString());//打印字节数组
                        //每个pud都是一个字节数组
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                        String phone = sms.getOriginatingAddress();//发送短信的手机号码
                        String body = sms.getDisplayMessageBody(); //短信内容
                        long time = sms.getTimestampMillis();
//                        Date date = new Date(sms.getTimestampMillis());
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String time = dateFormat.format(date);  //得到发送时间
                        Log.e(TAG, TAG + "-->phone:" + phone);
                        Log.e(TAG, TAG + "-->body:" + body);
//                        Log.i(TAG, TAG+"-->time:"+time);

                        //TODO 判断是否在黑名单
                        DBHelper helper = new DBHelper(context);
                        SQLiteDatabase database = helper.getWritableDatabase();
                        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, "phone = ?", new String[]{phone}, null, null, null);

                        Log.e(TAG, "count " + cursor.getCount());
                        if (cursor.getCount() > 0) {
                            Log.e(TAG, "拦截，终止广播");
                            abortBroadcast(); //终止此条广播
                            //将拦截的短信存入数据库
                            ContentValues values = new ContentValues();
                            while (cursor.moveToNext()){
                                int index = cursor.getColumnIndex("display_name");
                                if (index != -1){
                                    String display_name = cursor.getString(index);
                                    values.put("display_name", display_name);
                                }
                            }
                            values.put("phone", phone);
                            values.put("time", time);
                            values.put("body", body);

//                            long insertId = database.insert(DBHelper.TABLE_SMS, null, values);
                            ContentResolver resolver = context.getContentResolver();
                            Uri uri = resolver.insert(Uri.parse(BlackContentProvider.SMS_URI), values);
                            long insertId = ContentUris.parseId(uri);
                            if (insertId > 0){
                                Log.e(TAG, "短信被拦截，短信已成功存入骚扰短信数据库");
                            }
                        }
                    }
                }
            }
        }
    }
}
