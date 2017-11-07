package com.i76game.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.activity.GameInfoActivity;
import com.i76game.utils.Global;

import static com.i76game.R.id.item_icon;

/**
 * Created by Administrator on 2017/11/7.
 */

public class RecommendAdapter extends BaseAdapter {
    private Activity activity;
    public RecommendAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.recommend_item, null);
            holder.item_ll = (LinearLayout) view.findViewById(R.id.item_ll);
            holder.item_bt = (Button) view.findViewById(R.id.item_bt);
            holder.item_icon = (ImageView) view.findViewById(item_icon);
            holder.item_name = (TextView) view.findViewById(R.id.item_name);
            holder.item_num = (TextView) view.findViewById(R.id.item_num);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.item_icon.setTransitionName("");
        }
        holder.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startContentActivity(holder);
            }
        });

        return view;
    }

    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(ViewHolder holder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.item_icon.setTransitionName(activity.getResources().getString(R.string.share_img));
        }
        Intent intent=new Intent(activity,GameInfoActivity.class);
        intent.putExtra(Global.GAME_ID,60358);
        holder.item_icon.getDrawingCache(true);
        Global.drawable=holder.item_icon.getDrawable();
        //如果5.0以上使用
        if (Build.VERSION.SDK_INT > 20) {
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    activity, holder.item_icon,
                    activity.getResources().getString(R.string.share_img)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    class ViewHolder{
        LinearLayout item_ll;
        ImageView item_icon;
        TextView item_name, item_num;
        Button item_bt;
    }

}
