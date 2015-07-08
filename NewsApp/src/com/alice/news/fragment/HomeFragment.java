package com.alice.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.alice.news.R;
import com.alice.news.adapter.NewsFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private RadioGroup tabHome;
    private ViewPager pagerHome;

    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        tabHome = (RadioGroup) view.findViewById(R.id.home_tab_bar);
        pagerHome = (ViewPager) view.findViewById(R.id.home_pager);
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            SubNewsFragment fragment = new SubNewsFragment();
            Bundle args = new Bundle();
            args.putString("key", "news " + i);
            fragment.setArguments(args);
            fragmentList.add(fragment);
        }
        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getChildFragmentManager(), fragmentList);
        pagerHome.setAdapter(adapter);

        tabHome.setOnCheckedChangeListener(this);
        pagerHome.addOnPageChangeListener(this);

        tabHome.check(0);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int currentId = 0;
        switch (checkedId) {
            case R.id.tab_home_hot:
                currentId = 0;
                break;
            case R.id.tab_home_beijing:
                currentId = 1;
                break;
            case R.id.tab_home_entertainment:
                currentId = 2;
                break;
            case R.id.tab_home_sport:
                currentId = 3;
                break;
        }
        pagerHome.setCurrentItem(currentId);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int checkId = R.id.tab_home_hot;
        switch (position) {
            case 0:
                checkId = R.id.tab_home_hot;
                break;
            case 1:
                checkId = R.id.tab_home_beijing;
                break;
            case 2:
                checkId = R.id.tab_home_entertainment;
                break;
            case 3:
                checkId = R.id.tab_home_sport;
                break;
        }
        tabHome.check(checkId);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}