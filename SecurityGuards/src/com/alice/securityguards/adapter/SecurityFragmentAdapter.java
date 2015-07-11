package com.alice.securityguards.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 15-7-10.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-10
 */
public class SecurityFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    public SecurityFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (fragmentList != null) {
            ret = fragmentList.size();
        }
        return ret;
    }
}
