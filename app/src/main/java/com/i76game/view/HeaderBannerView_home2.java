package com.i76game.view;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.i76game.R;

import java.util.List;


public class HeaderBannerView_home2 extends AbsHeaderView<List<String>> {
    public LinearLayout game_ll, fenlei_ll, bangdan_ll, earn_ll, server_ll, activity_ll, share_ll;
    public HeaderBannerView_home2(Activity context) {
        super(context);
    }

    @Override
    protected void getView(List<String> list, ListView listView) {
        View view = mInflate.inflate(R.layout.header_banner_layout_home2, listView, false);
        game_ll = (LinearLayout) view.findViewById(R.id.game_ll);
        fenlei_ll = (LinearLayout) view.findViewById(R.id.fenlei_ll);
        bangdan_ll = (LinearLayout) view.findViewById(R.id.bangdan_ll);
        earn_ll = (LinearLayout) view.findViewById(R.id.earn_ll);
        server_ll = (LinearLayout) view.findViewById(R.id.server_ll);
        activity_ll = (LinearLayout) view.findViewById(R.id.activity_ll);
        share_ll = (LinearLayout) view.findViewById(R.id.share_ll);
        listView.addHeaderView(view);
    }
}
