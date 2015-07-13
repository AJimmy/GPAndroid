package com.alice.handup.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 15-7-13.
 *
 */
public class HangUpReceiver extends BroadcastReceiver {
    private static final String TAG = "HangUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, "action "+action);
        if (action.equals("android.intent.action.PHONE_STATE")){
            try {
                Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
                IBinder iBinder = (IBinder) method.invoke(null, new Object[]{context.TELEPHONY_SERVICE});
                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                iTelephony.endCall();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}
