package com.alice.downloadmanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alice.downloadmanager.Tools.HttpTools;
import com.alice.downloadmanager.services.DownloadService;

import java.io.IOException;

/**
 * 下载管理器：
 * 1. 一个输入框，可以输入网址进行下载
 * 2. 可以实现一个隐式意图的Activity，把ACTION_VIEW实现，支持http网址的访问；（意图类型）
 * 3. 当意图Activity收到请求后，显示网址在输入框，点击“下载”按钮。
 * 4. 给服务发送消息，开始下载
 * 5. 还有一个Activity可以查看当前的下载进度。
 */
public class MainActivity extends Activity implements Runnable, ServiceConnection {

    private static final String TAG = "MainActivity";
    private EditText editUrl;
    private ProgressBar progressBar;
    private TextView txtProgressState;
    private DownloadService.Controller controller;
    private int maxSize = 0;
    private int currentSize = 0;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HttpTools.STATE_CONNECTING:
                    txtProgressState.setText("正在连接到网络");
                    break;
                case HttpTools.STATE_CONNECT_SUCCESS:
                    txtProgressState.setText("连接成功");
                    break;
                case HttpTools.STATE_CONNECT_FAIL:
                    txtProgressState.setText("连接失败，下载失败");
                    break;
                case HttpTools.STATE_READ_FAIL:
                    txtProgressState.setText("连接成功，下载失败");
                    break;
                case HttpTools.STATE_READING:
                    txtProgressState.setText("正在下载");
                    progressBar.setMax(maxSize);
                    progressBar.setProgress((Integer) msg.obj);
                    break;
                case HttpTools.STATE_READ_SUCCESS:
                    txtProgressState.setText("下载完成");
                    break;
            }

            return true;
        }
    });

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        editUrl = (EditText) findViewById(R.id.edit_url);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        txtProgressState = (TextView) findViewById(R.id.txt_progress_state);
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && controller == null){
            editUrl.setText(data.toString());
        }
    }

    /**
     * TODO 按钮点击响应时间，开启服务，开始下载
     *
     * @param view
     */
    public void btnStartDownloadService(View view) {
        if (controller == null) {
            Intent intent = new Intent(this, DownloadService.class);
            bindService(intent, this, BIND_AUTO_CREATE);
        }
    }


    //线程执行，每隔1秒，获取一次下载进度
    @Override
    public void run() {
        while (true) {
            currentSize = controller.getCurrentProgress();
            maxSize = controller.getFileSize();
            Message message = handler.obtainMessage();
            message.what = HttpTools.STATE_READING;
            message.obj = currentSize;
            message.arg1 = maxSize;
            handler.sendMessage(message);
//            Log.d(TAG, maxSize +"--------"+currentSize);
            if (maxSize != 0 && currentSize == maxSize) {
                message = handler.obtainMessage();
                message.what = HttpTools.STATE_READ_SUCCESS;
                message.obj = currentSize;
                handler.sendMessage(message);
                controller.resetHttpToolsFile();
                unbindService(this);
                break;//下载完成，线程结束
            }else if ( controller.getHttpState() == HttpTools.STATE_READ_FAIL){
                message = handler.obtainMessage();
                message.what = HttpTools.STATE_READ_FAIL;
                handler.sendMessage(message);
                controller.resetHttpToolsFile();
                unbindService(this);
                break;
            }else if( controller.getHttpState() == HttpTools.STATE_CONNECT_FAIL){
                message = handler.obtainMessage();
                message.what = HttpTools.STATE_CONNECT_FAIL;
                handler.sendMessage(message);
                controller.resetHttpToolsFile();
                unbindService(this);
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //bindService成功后回调该方法
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        controller = (DownloadService.Controller) service;
        String url = editUrl.getText().toString();
//        url = "http://img0.bdstatic.com/img/image/imglogo-r.png";

        if (controller != null) {
            try {
                controller.startDownload(url);
                new Thread(this).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //解除绑定服务后回调该方法
    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected");
        controller = null;
    }
}
