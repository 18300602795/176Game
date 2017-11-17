package com.i76game.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/1116.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        // TODO Auto-generated constructor stub
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return mFragments.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

}
