package com.alice.news.asynctask;

import android.os.Message;
import com.alice.news.adapter.SubNewsAdapter;
import com.alice.news.fragment.SubNewsFragment;
import com.alice.news.tool.HttpTools;
import com.alice.news.tool.ParserJson;
import android.os.Handler;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Administrator on 15-7-9.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-9
 */
public class NewsThread extends Thread {
    private String path;
    private SubNewsAdapter adapter;
    private Handler handler;

    public NewsThread(Handler handler, SubNewsAdapter adapter, String path) {
        this.handler = handler;
        this.adapter = adapter;
        this.path = path;
    }

    public NewsThread() {
    }

    @Override
    public void run() {
        super.run();
        try {
            byte[] arr = HttpTools.getHttpBytes(path);
            if (arr != null) {
                adapter.addList(ParserJson.parserJson(arr));
                Message message = handler.obtainMessage();
                message.what = SubNewsFragment.HANDLE_ADAPTER;
                handler.sendMessage(message);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
