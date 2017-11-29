package com.i76game.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.activity.GameInfoActivity;
import com.i76game.bean.GameContentBean;
import com.i76game.download.DownloadAPKManager;
import com.i76game.download.DownloadService;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.Global;

import java.util.List;


/**
 * Created by Administrator on 2017/11/7.
 */

public class RecommendAdapter extends BaseAdapter {
    private Activity activity;
    private List<GameContentBean.GameListBean> gameListBeen;
    private DownloadAPKManager mDownloadAPKManager;
    private boolean isFirst = true;

    public RecommendAdapter(Activity activity, List<GameContentBean.GameListBean> gameListBeen) {
        this.activity = activity;
        this.gameListBeen = gameListBeen;
        mDownloadAPKManager = DownloadService.getDownloadManager(MyApplication.getContextObject());
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return gameListBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.recommend_item, null);
            holder.item_ll = (LinearLayout) view.findViewById(R.id.item_ll);
            holder.item_icon = (ImageView) view.findViewById(R.id.item_icon);
            holder.item_name = (TextView) view.findViewById(R.id.item_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.item_icon.setTransitionName("");
        }
        holder.item_name.setText(gameListBeen.get(i).getGamename());
        GlideUtil.loadImage(gameListBeen.get(i).getIcon(), holder.item_icon, R.mipmap.ic_launcher);
        holder.item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startContentActivity(holder, gameListBeen.get(i));
            }
        });
        return view;
    }


    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(ViewHolder holder, GameContentBean.GameListBean gameListBean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.item_icon.setTransitionName(activity.getResources().getString(R.string.share_img));
        }
        Intent intent = new Intent(activity, GameInfoActivity.class);
        intent.putExtra(Global.GAME_ID, gameListBean.getGameid());
        holder.item_icon.getDrawingCache(true);
        Global.drawable = holder.item_icon.getDrawable();
        //如果5.0以上使用
        if (Build.VERSION.SDK_INT > 20) {
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    activity, holder.item_icon,
                    activity.getResources().getString(R.string.share_img)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    class ViewHolder {
        LinearLayout item_ll;
        ImageView item_icon;
        TextView item_name;
    }

}
