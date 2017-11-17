package com.i76game.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.adapter.MainPagerAdapter;
import com.i76game.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 开服表
 */

public class ServerFragment2 extends Fragment implements View.OnClickListener {


    private Button mTodayBtn;
    private Button mTomorrowBtn;
    private Button mYesterdayBtn;
    private List<Fragment> fragments;
    private ViewPager server_pager;
    private MainPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.server_fragment, null);
        initView(view);
        LogUtils.i("item：" + MyApplication.item);
        mTodayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
        return view;
    }


    private void initView(View view) {
        mTodayBtn = (Button) view.findViewById(R.id.table_today);
        mTomorrowBtn = (Button) view.findViewById(R.id.table_tomorrow);
        mYesterdayBtn = (Button) view.findViewById(R.id.table_yesterday);
        server_pager = (ViewPager) view.findViewById(R.id.server_pager);
        mTodayBtn.setOnClickListener(this);
        mTomorrowBtn.setOnClickListener(this);
        mYesterdayBtn.setOnClickListener(this);
        fragments = new ArrayList<>();
        addFragment();
        pagerAdapter = new MainPagerAdapter(getFragmentManager(), fragments);
        server_pager.setAdapter(pagerAdapter);
        server_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addFragment() {
        TodayFragment todayFragment = new TodayFragment();
        TomorrowFragment tomorrowFragment = new TomorrowFragment();
        YesterdayFragment yesterdayFragment = new YesterdayFragment();
        fragments.add(todayFragment);
        fragments.add(tomorrowFragment);
        fragments.add(yesterdayFragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_today:
                resetView(0);
                server_pager.setCurrentItem(0);

                break;
            case R.id.table_tomorrow:
                resetView(1);
                server_pager.setCurrentItem(1);

                break;
            case R.id.table_yesterday:
                resetView(2);
                server_pager.setCurrentItem(2);
                break;
        }
    }

    /**
     * 重置所有button
     */
    private void resetView(int position) {
        mTodayBtn.setBackgroundResource(R.drawable.server_btn_bg);
        mTomorrowBtn.setBackgroundResource(R.drawable.server_btn_bg);
        mYesterdayBtn.setBackgroundResource(R.drawable.server_btn_bg);
        switch (position) {
            case 0:
                mTodayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
            case 1:
                mTomorrowBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
            case 2:
                mYesterdayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
        }
    }
}
