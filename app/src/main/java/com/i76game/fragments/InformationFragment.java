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

import com.i76game.R;
import com.i76game.utils.LogUtils;
import com.i76game.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/17.
 */

public class InformationFragment extends BaseFragment {
    private View view;
    private TabLayout tabs;
    private ViewPager inf_pager;
    private RegisterAdapter pagerAdapter;
    private String[] mTitles = {"资讯", "攻略"};
    private ArrayList<Fragment> fragments;
    private LinearLayout fragment_ll;
    private TextView fragment_title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information, null);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        inf_pager = (ViewPager) view.findViewById(R.id.inf_pager);
        fragment_title = (TextView) view.findViewById(R.id.fragment_title);
        fragment_title.setText("我的消息");
        fragment_ll = (LinearLayout) view.findViewById(R.id.fragment_ll);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragment_ll.setPadding(0, Utils.dip2px(getActivity(), 10), 0, 0);
            setTranslucentStatus(true);
        }
        addFragments();
        pagerAdapter = new RegisterAdapter(getFragmentManager(), fragments, mTitles);
        tabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabs.setupWithViewPager(inf_pager);//将TabLayout和ViewPager关联起来。
        inf_pager.setAdapter(pagerAdapter);
        inf_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        return view;
    }

    @Override
    public void initDate() {
        LogUtils.i("info---开始加载数据");
        isShow = false;
        if (((BaseFragment) (pagerAdapter.getItem(0))).isShow)
            ((BaseFragment) (pagerAdapter.getItem(0))).initDate();
    }

    private void addFragments() {
        fragments = new ArrayList<>();
        MessageFragment2 messageFragment = new MessageFragment2();
        MessageFragment2 messageFragment2 = new MessageFragment2();
        fragments.add(messageFragment);
        fragments.add(messageFragment2);
    }
}
