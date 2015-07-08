package com.alice.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.alice.news.R;
import com.alice.news.adapter.SubNewsAdapter;
import com.alice.news.entity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class SubNewsFragment extends Fragment {

    public SubNewsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_news, container, false);
        TextView text = (TextView) view.findViewById(R.id.sub_news_text);
        if (text != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String value = bundle.getString("key");
                text.setText(value);
            }
        }

        ListView listView = (ListView) view.findViewById(R.id.sub_news_list);
        List<News> newsList = new ArrayList<>();
        SubNewsAdapter adapter = new SubNewsAdapter(getActivity(), newsList);
        listView.setAdapter(adapter);

        //TODO 连接网络，获取并解析JSON,添加List,adapter.notifyDateSetChanged()
        //TODO 使用Loader.
        //ListView 分页显示

        return view;
    }
}