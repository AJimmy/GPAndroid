package com.alice.news.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.alice.news.adapter.SubNewsAdapter;
import com.alice.news.entity.News;
import com.alice.news.tool.HttpTools;
import com.alice.news.tool.ParserJson;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 15-6-27.
 * Project: MyAssistance
 * User: Alice
 * Data: 15-6-27
 */
public class NewsAsyncTask extends AsyncTask<String, Void, List<News>> {
    private Context mContext;
    private SubNewsAdapter adapter;
//    private ProgressDialog dialog;

    public NewsAsyncTask(Context mContext, SubNewsAdapter adapter) {
        this.adapter = adapter;
        this.mContext = mContext;
//        dialog = new ProgressDialog(mContext);
//        dialog.setMessage("正在刷新...");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        dialog.show();
    }

    @Override
    protected List<News> doInBackground(String... params) {
        try {
            byte[] arr = HttpTools.getHttpBytes(params[0]);
            if (arr != null) {
                return ParserJson.parserJson(arr);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<News> newses) {
        super.onPostExecute(newses);
//        dialog.dismiss();
        adapter.addList(newses);
        adapter.notifyDataSetChanged();
    }
}
