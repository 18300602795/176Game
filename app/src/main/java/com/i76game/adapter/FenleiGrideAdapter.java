package com.i76game.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.ImgUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class FenleiGrideAdapter extends BaseAdapter{
    private List<HomeRVBean.DataBean.GameListBean> mGameList;
    private Context context;

    public FenleiGrideAdapter(List<HomeRVBean.DataBean.GameListBean> mGameList, Context context) {
        this.mGameList = mGameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mGameList == null ? 0 : mGameList.size();
    }

    @Override
    public Object getItem(int i) {
        return mGameList == null ? null : mGameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.fenlei_gride_item, null);
            holder.grid_icon = (ImageView) view.findViewById(R.id.grid_icon);
            holder.grid_name = (TextView) view.findViewById(R.id.grid_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.grid_name.setText(mGameList.get(i).getGamename());
//        ImgUtil.loadImage(mGameList.get(i).getIcon() + "", holder.grid_icon);
        ImgUtil.loadImage(context, mGameList.get(i).getIcon(), R.mipmap.load_icon, holder.grid_icon);
        return view;
    }

    class ViewHolder{
        ImageView grid_icon;
        TextView grid_name;
    }

}
