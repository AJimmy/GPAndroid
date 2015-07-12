package com.alice.downloadmanager.Tools;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class HttpTools {
    private static final String TAG = "HttpTools";

    public static final int STATE_CONNECTING = 0;//正在连接
    public static final int STATE_CONNECT_FAIL = 1;//连接失败
    public static final int STATE_CONNECT_SUCCESS = 2;//连接成功
    public static final int STATE_READING = 3;//正在读取
    public static final int STATE_READ_SUCCESS = 4;//网络读取结束
    public static final int STATE_READ_FAIL = 5;//网络读取结束

    private int size = 0;
    private int currentSize = 0;
    private int state = STATE_CONNECTING;
    private String fileName = "";

    public void setSize(int size) {
        this.size = size;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getSize() {
        return size;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public int getState() {
        return state;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getHttpBytes(String path) {
        byte[] arr = null;
        URL url = null;
        try {
            state = STATE_CONNECTING;


            url = new URL(path);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoInput(true);
            con.connect();
            if (con.getResponseCode() == 200) {
                state = STATE_CONNECT_SUCCESS;
                size = con.getContentLength();
                fileName = con.getHeaderField("filename");
                Log.e(TAG, "con.getContentLength() " + size + ", " + fileName);
                InputStream in = con.getInputStream();

                int len;
                byte[] b = new byte[64];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while (true) {
                    len = in.read(b);
//                Log.d(TAG, "3-----------------------"+len  +"------"+state);
                    if (len == -1) {
                        break;
                    } else if (len == 0) {
                        Log.d(TAG, "len == 0");
                        state = STATE_READ_FAIL;
                        break;
                    }
                    state = STATE_READING;
                    bos.write(b, 0, len);
                    currentSize += len;//设置已下载大小
                }
                state = STATE_READ_SUCCESS;
                arr = bos.toByteArray();
                con.disconnect();
            } else {
                state = STATE_CONNECT_FAIL;
            }
        } catch (MalformedURLException e) {
            state = STATE_CONNECT_FAIL;
            e.printStackTrace();
        } catch (ProtocolException e) {
            state = STATE_CONNECT_FAIL;
            e.printStackTrace();
        } catch (IOException e) {
            state = STATE_CONNECT_FAIL;
            e.printStackTrace();
        }
        return arr;
    }
}
