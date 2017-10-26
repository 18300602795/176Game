package com.i76game.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.GetTypeUtils;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.Global;

import java.util.List;

/**
 * 所有游戏列表的适配器
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.HomeRVHolder> {

    private Activity mContext;
    private List<HomeRVBean.DataBean.GameListBean> mBeanList;

    public GameListAdapter(Activity context, List<HomeRVBean.DataBean.GameListBean> beanList) {
        this.mContext = context;
        this.mBeanList = beanList;
    }

    public void setData(List<HomeRVBean.DataBean.GameListBean> beanList){
        mBeanList.clear();
        mBeanList=beanList;
    }
    public void addData(List<HomeRVBean.DataBean.GameListBean> beanList){
        mBeanList.addAll(beanList);
    }

    @Override
    public GameListAdapter.HomeRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_rv_layout, parent, false);
        return new GameListAdapter.HomeRVHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameListAdapter.HomeRVHolder holder, final int position) {
        final HomeRVBean.DataBean.GameListBean gameListBean = mBeanList.get(position);
        holder.mGameName.setText(gameListBean.getGamename());
        GlideUtil.loadImage(gameListBean.getIcon(), holder.mIcon, R.mipmap.load_icon);
        if (!TextUtils.isEmpty(gameListBean.getType())){
            String[] types = GetTypeUtils.getType(gameListBean.getType());

            if (TextUtils.isEmpty(types[1])) {
                holder.mType3.setText(types[0]);
                holder.mType4.setVisibility(View.GONE);
            } else {
                holder.mType3.setText(types[0]);
                holder.mType4.setVisibility(View.VISIBLE);
                holder.mType4.setText(types[1]);
            }
        }
        holder.mSize.setText(gameListBean.getSize());
        holder.mSize.setText(gameListBean.getSize());
        holder.mSize.setText(gameListBean.getSize());
        holder.mSize.setText(gameListBean.getSize());

        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startContentActivity(gameListBean, holder);
            }
        });
    }

    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(HomeRVBean.DataBean.GameListBean gameListBean, HomeRVHolder holder) {
        Intent intent=new Intent(mContext,GameContentActivity.class);
        intent.putExtra(Global.GAME_ID,gameListBean.getGameid());
        holder.mIcon.getDrawingCache(true);
        Global.drawable=holder.mIcon.getDrawable();
        //如果5.0以上使用
        if (android.os.Build.VERSION.SDK_INT > 20) {
            mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    mContext, holder.mIcon,
                    mContext.getResources().getString(R.string.share_img)).toBundle());
        } else {
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mBeanList == null ? 0 : mBeanList.size();
    }


    class HomeRVHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mGameName;
        TextView mSize;
        TextView mType1;
        TextView mType2;
        TextView mType3;
        TextView mType4;
        Button mGameContent;
        LinearLayout mItemLayout;

        public HomeRVHolder(View itemView) {
            super(itemView);
            mItemLayout= (LinearLayout) itemView.findViewById(R.id.home_rv_layout);
            mIcon = (ImageView) itemView.findViewById(R.id.home_rv_icon);
            mGameName = (TextView) itemView.findViewById(R.id.home_rv_game_name);
            mSize = (TextView) itemView.findViewById(R.id.home_rv_game_size);
            mType1 = (TextView) itemView.findViewById(R.id.home_rv_game_type1);
            mType2 = (TextView) itemView.findViewById(R.id.home_rv_game_type2);
            mType3 = (TextView) itemView.findViewById(R.id.home_rv_game_type3);
            mType4 = (TextView) itemView.findViewById(R.id.home_rv_game_type4);
            mGameContent = (Button) itemView.findViewById(R.id.home_rv_game_content);
        }
    }
}
