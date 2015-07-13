package com.alice.downloadmanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.alice.downloadmanager.service.DownloadService;
import com.alice.downloadmanager.service.DownloadTask;

public class MainActivity extends Activity implements ServiceConnection, Runnable {

    private static final String TAG = "MainActivity";
    private EditText edit;
    private DownloadService.DownloadController controller;
    private ProgressBar progressBar;
    private Thread thread;
    private boolean running;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        edit = (EditText) findViewById(R.id.txt_url);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //启动服务
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);

        Uri data = getIntent().getData();
        if (data != null) {
            edit.setText(data.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //当SingleTop匹配，singleTask匹配，sing
        // startActivity不再创建新的实例，而是调用onNewIntent()更新intent
        //只有这个方法调用之后，新的intent才会剩下。
        setIntent(intent);
        Uri data = intent.getData();
        if (data != null) {
            //TODO 用来下载网络的设置
            edit.setText(data.toString());
        }
    }

    public void btnStart(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.parse("http://www.baidu.com");
        intent.setData(uri);
        startActivity(intent);
    }

    public void btnDownload(View view) {
        String url = edit.getText().toString().trim();
        Log.e(TAG, url);
        if (url.length() > 0 && url.startsWith("http://")) {
            controller.requestDownload(url);
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        controller = (DownloadService.DownloadController) service;
        // TODO 线程的创建，监测下载进度
        thread = new Thread(this);

        thread.start();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        running = false;
        thread = null;
        controller = null;
    }

    @Override
    public void run() {
        running = true;
        try {//try-catch下载while外面
            while (running) {
                if (controller != null) {
                    DownloadTask currentTask = controller.getCurrentTask();
                    if (currentTask != null) {
                        long total = currentTask.getTotal();
                        long current = currentTask.getCurrent();
                        if (total == -1) {
                            progressBar.setIndeterminate(true);//进度条不是确切
                        } else {
                            //进度条可以在子线程中执行
                            progressBar.setIndeterminate(false);
                            progressBar.setMax((int) total);
                            progressBar.setProgress((int) current);
                        }
                    }else {//下载完
                        progressBar.setIndeterminate(false);
                        progressBar.setProgress(0);
                    }

                }


                Thread.sleep(1000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
