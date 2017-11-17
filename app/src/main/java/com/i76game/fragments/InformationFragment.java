package com.i76game.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i76game.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/17.
 */

public class InformationFragment extends Fragment{
    private View view;
    private TabLayout tabs;
    private ViewPager inf_pager;
    private  RegisterAdapter pagerAdapter;
    private String[] mTitles={"资讯", "攻略"};
    private ArrayList<Fragment> fragments;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information, null);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        inf_pager = (ViewPager) view.findViewById(R.id.inf_pager);
        initDate();
        return view;
    }

    private void initDate() {
        addFragments();
        pagerAdapter = new RegisterAdapter(getFragmentManager(), fragments, mTitles);
        tabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabs.setupWithViewPager(inf_pager);//将TabLayout和ViewPager关联起来。
        inf_pager.setAdapter(pagerAdapter);
    }

    private void addFragments() {
        fragments = new ArrayList<>();
        MessageFragment2 messageFragment = new MessageFragment2();
        MessageFragment2 messageFragment2 = new MessageFragment2();
        fragments.add(messageFragment);
        fragments.add(messageFragment2);
    }
}
