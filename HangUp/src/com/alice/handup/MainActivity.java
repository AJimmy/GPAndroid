package com.alice.handup;

import android.app.Activity;
import android.os.Bundle;

/**
 * 1. 挂断电话； AIDL(Android源码内部的) + 反射
 2. 关于广播接收者的了解； READ_PHONE_STATE
 */
public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
