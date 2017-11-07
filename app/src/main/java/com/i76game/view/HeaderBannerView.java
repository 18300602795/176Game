package com.i76game.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.i76game.R;

import java.util.List;


public class HeaderBannerView extends AbsHeaderView<List<String>> {
    public LinearLayout ll_banner;
    public LinearLayout infomation_ll;
    public ImageView home_rv_icon;
    public TextView game_content_name;
    public TextView game_content_download_count;
    public TextView game_content_type;
    public TextView game_content_versions;
    public TextView game_content_language;

    public HeaderBannerView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(List<String> list, ListView listView) {
        View view = mInflate.inflate(R.layout.header_banner_layout, listView, false);
        ll_banner = (LinearLayout) view.findViewById(R.id.ll_banner);
        infomation_ll = (LinearLayout) view.findViewById(R.id.infomation_ll);
        home_rv_icon = (ImageView) view.findViewById(R.id.home_rv_icon);
        game_content_name = (TextView) view.findViewById(R.id.game_content_name);
        game_content_download_count = (TextView) view.findViewById(R.id.game_content_download_count);
        game_content_type = (TextView) view.findViewById(R.id.game_content_type);
        game_content_versions = (TextView) view.findViewById(R.id.game_content_versions);
        game_content_language = (TextView) view.findViewById(R.id.game_content_language);
        listView.addHeaderView(view);
    }
}
