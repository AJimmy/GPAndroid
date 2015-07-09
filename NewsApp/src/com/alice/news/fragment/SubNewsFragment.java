package com.alice.news.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import com.alice.news.R;
import com.alice.news.adapter.SubNewsAdapter;
import com.alice.news.asynctask.NewsAsyncTask;
import com.alice.news.asynctask.NewsThread;
import com.alice.news.entity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class SubNewsFragment extends Fragment implements AbsListView.OnScrollListener {
    public static final String NEWPATH = "http://litchiapi.jstv.com/api/GetFeeds?column=0&PageSize=20&pageIndex=";
    public static final String NEWPATHSUB = "&val=100511D3BE5301280E0992C73A9DEC41";
    public static final String NEWSDIE = "http://litchiapi.jstv.com/";
    public static final int HANDLE_ADAPTER= 1 ;
    public static final int HANDLE_IMAGE= 1 ;
    private int pageIndex ;
    private boolean isBottom = false;
    private SubNewsAdapter adapter;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_ADAPTER:
                    adapter.notifyDataSetChanged();
                    break;
            }
            return true;
        }
    });
    private View view;
    private String path;

    public SubNewsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sub_news, container, false);
            TextView text = (TextView) view.findViewById(R.id.sub_news_text);
            path = null;

            if (text != null) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    path = bundle.getString("page");
                    text.setText(path);
                }
            }

            ListView listView = (ListView) view.findViewById(R.id.sub_news_list);
            List<News> newsList = new ArrayList<>();
            adapter = new SubNewsAdapter(getActivity(), newsList);
            listView.setAdapter(adapter);

            //TODO 连接网络，获取并解析JSON,添加List,adapter.notifyDateSetChanged()
            //TODO 使用Loader.
            //ListView 分页显示
            pageIndex = Integer.parseInt(path) + 1;
//        new NewsThread(handler, adapter, NEWPATH + path + NEWPATHSUB).start();
            listView.setOnScrollListener(this);
        }

        new NewsAsyncTask(getActivity(), adapter).execute(NEWPATH + path + NEWPATHSUB);
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && isBottom){
            Toast.makeText(getActivity(), "正在加载...", Toast.LENGTH_SHORT).show();
            new NewsAsyncTask(getActivity(), adapter).execute(NEWPATH + pageIndex++ + NEWPATHSUB);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isBottom = (firstVisibleItem + visibleItemCount)==totalItemCount;
    }
}