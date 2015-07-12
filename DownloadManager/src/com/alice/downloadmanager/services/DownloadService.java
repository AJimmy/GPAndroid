package com.alice.downloadmanager.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import com.alice.downloadmanager.Tools.HttpTools;

import java.io.*;

/**
 * Created by Administrator on 15-7-12.
 */
public class DownloadService extends Service {

    private static final String TAG = "Service";
    private int i = 1;
    @Override
    public IBinder onBind(Intent intent) {
        return new Controller();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    //定义自己的方法
    public class Controller extends Binder {
        HttpTools httpTools;

        public void startDownload(final String url) throws IOException {
            //开始下载
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpTools = new HttpTools();
                    try {
                        byte[] bytes = httpTools.getHttpBytes(url);
                        // TODO bytes为网络下载文件的总大小，将该数组保存到本地目录下
                        if (bytes != null && bytes.length > 0) {
                            writeFile(url, bytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public void pauseDownload() {
            //暂停下载

        }

        public void stopDownload() {
            //停止下载
        }

        public int getHttpState() {
            return httpTools.getState();
        }

        public int getFileSize() {
            //下载文件的总大小
            return httpTools.getSize();
        }

        public int getCurrentProgress() {
            //当前下载的进度
            return httpTools.getCurrentSize();
        }

        public void resetHttpToolsFile() {
            httpTools.setCurrentSize(0);
            httpTools.setSize(0);
        }

        private void writeFile(String url, byte[] bytes) throws IOException {
            File directory = Environment.getExternalStorageDirectory();
            File subDir = new File(directory, "Adownload");
            if (!subDir.exists()){
             subDir.mkdir();
            }
            int index = url.lastIndexOf('.');
            Log.e(TAG, "index " + index);
//            String name = httpTools.getFileName();
            String name = ""+(i++) +url.substring(index);
            Log.e(TAG, "---"+name);
            File file = new File(subDir, name);
            {
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(bytes, 0, bytes.length);
                bos.close();
            }
        }
    }


}
