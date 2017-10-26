package com.i76game.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.i76game.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 开服表
 */

public class ServerFragment2 extends Fragment implements View.OnClickListener{


    private Button mTodayBtn;
    private Button mTomorrowBtn;
    private Button mYesterdayBtn;
    private android.app.FragmentManager fm;
    private TodayFragment todayFragment;
    private YesterdayFragment yesterdayFragment;
    private TomorrowFragment tomorrowFragment;
    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.server_fragment,null);
        initView(view);
        showFragment(1);
        mTodayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
        return view;
    }

    private void initView(View view) {
        mTodayBtn = (Button) view.findViewById(R.id.table_today);
        mTomorrowBtn = (Button) view.findViewById(R.id.table_tomorrow);
        mYesterdayBtn = (Button) view.findViewById(R.id.table_yesterday);
        mTodayBtn.setOnClickListener(this);
        mTomorrowBtn.setOnClickListener(this);
        mYesterdayBtn.setOnClickListener(this);
        fm = getActivity().getFragmentManager();
        fragments = new ArrayList<>();
    }
    private void showFragment(int type) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideAll(transaction);
        switch (type) {
            case 1:
                if (todayFragment == null) {
                    todayFragment = new TodayFragment();
                    transaction.add(R.id.content_fl, todayFragment);
                    fragments.add(todayFragment);
                }
                Log.e("222", "todayFragment：" + todayFragment.today_pager);
                transaction.show(todayFragment);
                break;
            case 2:
                if (tomorrowFragment == null) {
                    tomorrowFragment = new TomorrowFragment();
                    transaction.add(R.id.content_fl, tomorrowFragment);
                    fragments.add(tomorrowFragment);
                }
                Log.e("222", "tomorrowFragment：" + tomorrowFragment.tomorrow_pager);
                transaction.show(tomorrowFragment);
                break;
            case 3:
                if (yesterdayFragment == null) {
                    yesterdayFragment = new YesterdayFragment();
                    transaction.add(R.id.content_fl, yesterdayFragment);
                    fragments.add(yesterdayFragment);
                }
                Log.e("222", "yesterdayFragment：" + yesterdayFragment.yesterday_pager);
                transaction.show(yesterdayFragment);
                break;
        }
        transaction.commit();
    }
    private  void  hideAll(FragmentTransaction transcation){
        for (Fragment fragment: fragments) {
            transcation.hide(fragment);
//            transcation.commit();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_today:
                resetView();
                showFragment(1);
                mTodayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
            case R.id.table_tomorrow:
                resetView();
                showFragment(2);
                mTomorrowBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
            case R.id.table_yesterday:
                resetView();
                showFragment(3);
                mYesterdayBtn.setBackgroundResource(R.drawable.server_btn_gb_pregress);
                break;
        }
    }

    /**
     * 重置所有button
     */
    private void resetView(){
        mTodayBtn.setBackgroundResource(R.drawable.server_btn_bg);
        mTomorrowBtn.setBackgroundResource(R.drawable.server_btn_bg);
        mYesterdayBtn.setBackgroundResource(R.drawable.server_btn_bg);
    }

}
