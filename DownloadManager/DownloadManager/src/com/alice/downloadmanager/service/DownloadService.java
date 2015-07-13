package com.alice.downloadmanager.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 15-7-13.
 */

/**
 * 下载
 * 只有一个线程下载。
 */
public class DownloadService extends Service implements Runnable{
    private Thread thread;
    private boolean running;//代表线程是否继续执行。
    private Queue<DownloadTask> tasks;//下载的队列
    private HttpURLConnection con;
    private DownloadTask currentTask;


    @Override
    public void onCreate() {
        super.onCreate();
        thread = new Thread(this);
        tasks = new LinkedBlockingQueue<>();
        thread.start();
    }

    @Override
    public void onDestroy() {
        running = false;
        thread = null;
        tasks.clear();
        tasks = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadController();
    }

    /**
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String downloadUrl = intent.getStringExtra("downloadUrl");
        if (downloadUrl != null){
            // TODO 提交下载任务
            DownloadTask task = new DownloadTask();
            task.url = downloadUrl;
            tasks.add(task);//将下载任务添加到队列中
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        running = true;
        while (running){

            currentTask = tasks.poll();
            if (currentTask != null) {
                //TODO 进行下载
                String url = currentTask.getUrl();
                URL u = null;
                InputStream in = null;
                HttpURLConnection con = null;
                try {
                    u = new URL(url);
                    con = (HttpURLConnection) u.openConnection();

                    con.setRequestMethod("GET");
                    con.setReadTimeout(5000);
                    con.connect();
                    if (con.getResponseCode() == 200){
                        int length = con.getContentLength();
                        currentTask.setTotal(length);
                         in= con.getInputStream();
                        //文件的操作
                        FileOutputStream fos = null;

                        File directory = Environment.getExternalStorageDirectory();
                        File fileDir = new File(directory, "Adownload");
                        if (!fileDir.exists()){
                            fileDir.mkdir();
                        }
                        int index = url.lastIndexOf('.');
                        String suffix = url.substring(index);
                        if (in != null) {

                            File target = new File(fileDir, "dl-"+System.currentTimeMillis()+suffix);
                            fos = new FileOutputStream(target);
                            byte[] buf = new byte[1024];
                            int len;
                            while (true){
                                len = in.read(buf);
                                if (len == -1){
                                    break;
                                }
                                currentTask.current += len;
                                fos.write(buf, 0, len);
                                Thread.sleep(10);
                            }
                            buf = null;
                            fos.close();
                            in.close();
                        }
                    }



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                }

            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class DownloadController extends Binder{

        public void requestDownload(String url){
            if (url!= null){
                DownloadTask task = new DownloadTask();
                task.url = url;
                tasks.add(task);
            }
        }

        public DownloadTask getCurrentTask(){
            return currentTask;
        }

    }

}
