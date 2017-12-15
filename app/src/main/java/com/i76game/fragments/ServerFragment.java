package com.i76game.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.utils.LogUtils;
import com.i76game.utils.Utils;

import java.util.ArrayList;

/**
 * 开服表
 */

public class ServerFragment extends BaseFragment {


    private ArrayList<Fragment> fragments;
    private ViewPager server_pager;
    private LinearLayout fragment_ll;
    private TextView fragment_title;
    private TabLayout tabs;
    private RegisterAdapter pagerAdapter;
    private String[] mTitles = {"今日开服", "即将开服", "已开新服"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.server_fragment, null);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        initView(view);
        LogUtils.i("item：" + MyApplication.item);
        return view;
    }


    private void initView(View view) {
        fragment_title = (TextView) view.findViewById(R.id.fragment_title);
        fragment_title.setText("开服表");
        fragment_ll = (LinearLayout) view.findViewById(R.id.fragment_ll);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragment_ll.setPadding(0, Utils.dip2px(getActivity(), 10), 0, 0);
            setTranslucentStatus(true);
        }
        server_pager = (ViewPager) view.findViewById(R.id.server_pager);
        server_pager.setOffscreenPageLimit(3);
        fragments = new ArrayList<>();
        addFragment();
        tabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabs.setupWithViewPager(server_pager);//将TabLayout和ViewPager关联起来。
        pagerAdapter = new RegisterAdapter(getFragmentManager(), fragments, mTitles);
        server_pager.setAdapter(pagerAdapter);
        server_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (((BaseFragment) (pagerAdapter.getItem(position))).isShow)
                    ((BaseFragment) (pagerAdapter.getItem(position))).initDate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void initDate() {
        isShow = false;
        LogUtils.i("server---开始加载数据");
        if (((BaseFragment) (pagerAdapter.getItem(0))).isShow)
            ((BaseFragment) (pagerAdapter.getItem(0))).initDate();
    }

    private void addFragment() {
        TodayFragment todayFragment = new TodayFragment();
        TomorrowFragment tomorrowFragment = new TomorrowFragment();
        YesterdayFragment yesterdayFragment = new YesterdayFragment();
        fragments.add(todayFragment);
        fragments.add(tomorrowFragment);
        fragments.add(yesterdayFragment);
    }


}
