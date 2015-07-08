package com.alice.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alice.news.R;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class OtherFragment extends Fragment {

    public OtherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        TextView text = (TextView) view.findViewById(R.id.text_title);
        if (text != null) {
            Bundle bundle = getArguments();//获取ViewPager传来的参数，并设置当前Fragment的显示
            if (bundle != null) {
                String value = bundle.getString("position");
                text.setText(value);
            }
        }
        return view;
    }

}