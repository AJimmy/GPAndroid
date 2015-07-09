package com.alice.news.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import com.alice.news.fragment.SubNewsFragment;
import com.alice.news.tool.HttpTools;
import android.os.Handler;

import java.io.IOException;

/**
 * Created by Administrator on 15-7-9.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-9
 */
public class NewsImageThread extends Thread {
    private String path;
    private Handler handler;

    @Override
    public void run() {
        super.run();
        byte[] arr = new byte[0];
        try {
            arr = HttpTools.getHttpBytes(path);

            Bitmap bit = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            if (bit != null) {
                Message message = handler.obtainMessage();
                message.what = SubNewsFragment.HANDLE_IMAGE;
                message.obj = bit;
                handler.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
