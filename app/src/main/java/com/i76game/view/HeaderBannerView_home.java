package com.i76game.view;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.i76game.R;

import java.util.List;


public class HeaderBannerView_home extends AbsHeaderView<List<String>> {
    public ViewPager mvViewPager;// 轮播图
    public LinearLayout ll_dots; // 存储圆点的容器
    public HeaderBannerView_home(Activity context) {
        super(context);
    }

    @Override
    protected void getView(List<String> list, ListView listView) {
        View view = mInflate.inflate(R.layout.header_banner_layout_home, listView, false);
        mvViewPager = (ViewPager) view.findViewById(R.id.vp_started_viewpager);
        ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);
        listView.addHeaderView(view);
    }
}
