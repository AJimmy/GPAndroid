package com.alice.securityguards;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import com.alice.securityguards.adapter.SecurityFragmentAdapter;
import com.alice.securityguards.fragment.BlackListFragment;
import com.alice.securityguards.fragment.BlackSmsFragment;
import com.alice.securityguards.fragment.ContactsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全卫⼠：
 * 功能划分：短信拦截（终⽌短信），⿊名单；从联系⼈添加；
 * 1）监测短信，当短信收到， 检测发件⼈，从数据库查找⿊名单的列表，匹配发
 * 件⼈是否在⿊名单中，在的话，终⽌⼲播，并且删除信息；（有序⼲播优先级
 * 和终⽌⼲播）
 * 2）可以通过联系⼈列表进⾏⿊名单的添加；
 * 3）可以在⿊名单进⾏编辑、删除操作。
 * 界⾯需求：
 * 1）采⽤ViewPager显⽰ ：拦截短信列表、联系⼈列表、⿊名单列表三个部分；
 * 2）拦截短信列表与⿊名单列表采⽤数据库存储
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private RadioGroup tabBar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pager = (ViewPager) findViewById(R.id.view_pager);
        tabBar = (RadioGroup) findViewById(R.id.tab_bar);

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new BlackSmsFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new BlackListFragment());

        SecurityFragmentAdapter adapter = new SecurityFragmentAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
        tabBar.setOnCheckedChangeListener(this);
        tabBar.check(R.id.tab_sms);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int currentId = 0;
        switch (checkedId) {
            case R.id.tab_sms:
                currentId = 0;
                break;
            case R.id.tab_contacts:
                currentId = 1;
                break;
            case R.id.tab_blacklist:
                currentId = 2;
                break;
        }
        pager.setCurrentItem(currentId);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int tabId = R.id.tab_sms;
        switch (position) {
            case 0:
                tabId = R.id.tab_sms;
                break;
            case 1:
                tabId = R.id.tab_contacts;
                break;
            case 2:
                tabId = R.id.tab_blacklist;
                break;
        }
        tabBar.check(tabId);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
