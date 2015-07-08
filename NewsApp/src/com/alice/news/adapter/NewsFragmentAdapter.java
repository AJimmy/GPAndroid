package com.alice.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */

//自定义FragmentPagerAdapter的子类，用于加载到ViewPager
public class NewsFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public NewsFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (fragments != null) {
            ret = fragments.size();
        }
        return ret;
    }
}
