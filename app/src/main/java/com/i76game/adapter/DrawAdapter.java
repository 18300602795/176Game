package com.i76game.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.bean.Prize;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class DrawAdapter extends BaseAdapter{
    private List<Prize> beens;
    private Context context;

    public DrawAdapter(List<Prize> beens, Context context) {
        this.beens = beens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return beens.size();
    }

    @Override
    public Object getItem(int i) {
        return beens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    class ViewHolder{
        public TextView draw_name;
        public ImageView draw_icon;
        public LinearLayout draw_bg;
    }

}
