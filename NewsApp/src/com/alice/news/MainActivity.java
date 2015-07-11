package com.alice.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioGroup;
import com.alice.news.adapter.NewsFragmentAdapter;
import com.alice.news.fragment.HomeFragment;
import com.alice.news.fragment.OtherFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    private RadioGroup tabBar;
    private ViewPager viewPager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.e(TAG, "onCreate ---------------------------");

        viewPager = (ViewPager) findViewById(R.id.news_pager);
        tabBar = (RadioGroup) findViewById(R.id.news_tab_bar);
        //ViewPager 使用的List<Fragment>集合填充
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        for (int i = 1; i < 5; i++) {
            OtherFragment fragment = new OtherFragment();
            Bundle args = new Bundle();
            args.putString("position", "news " + i);
            fragment.setArguments(args);
            fragmentList.add(fragment);
        }
        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(this);//推荐使用addOnPageChangeListener（）,setOnPageChangeListener()已经过时
        tabBar.setOnCheckedChangeListener(this);
        tabBar.check(R.id.tab_home);//启动默认选中项为第0项

    }

    //RadioGroup中点击，ViewPager动态变化
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int currentId = 0;
        switch (checkedId) {
            case R.id.tab_home://新闻
                currentId = 0;
                break;
            case R.id.tab_read://阅读
                currentId = 1;
                break;
            case R.id.tab_video://视听
                currentId = 2;
                break;
            case R.id.tab_discover://发现
                currentId = 3;
                break;
            case R.id.tab_personal://我
                currentId = 4;
                break;
        }
        viewPager.setCurrentItem(currentId);
    }

    //ViewPager滑动式，频繁调用该方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 页面切换完成，回调该方法
     *
     * @param position 当前显示第几页
     */
    @Override
    public void onPageSelected(int position) {
        int checkId = R.id.tab_home;
        switch (position) {
            case 0:
                checkId = R.id.tab_home;
                break;
            case 1:
                checkId = R.id.tab_read;
                break;
            case 2:
                checkId = R.id.tab_video;
                break;
            case 3:
                checkId = R.id.tab_discover;
                break;
            case 4:
                checkId = R.id.tab_personal;
                break;
        }
        tabBar.check(checkId);
    }

    /**
     * 页面的切换
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
