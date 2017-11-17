package com.i76game.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.text.TextUtils;
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
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.GetTypeUtils;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.Global;

import java.util.List;

/**
 * 所有游戏列表的适配器
 */

public class GameListAdapter2 extends BaseAdapter {

    private Activity mContext;
    private List<HomeRVBean.DataBean.GameListBean> mBeanList;

    public GameListAdapter2(Activity context, List<HomeRVBean.DataBean.GameListBean> beanList) {
        this.mContext = context;
        this.mBeanList = beanList;
    }

    public void setData(List<HomeRVBean.DataBean.GameListBean> beanList) {
        mBeanList.clear();
        mBeanList = beanList;
    }

    public void addData(List<HomeRVBean.DataBean.GameListBean> beanList) {
        mBeanList.addAll(beanList);
    }

    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(HomeRVBean.DataBean.GameListBean gameListBean, HomeRVHolder holder) {
        Intent intent = new Intent(mContext, GameInfoActivity.class);
        intent.putExtra(Global.GAME_ID, gameListBean.getGameid());
        holder.mIcon.getDrawingCache(true);
        Global.drawable = holder.mIcon.getDrawable();
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
    public int getCount() {
        return mBeanList == null ? 0 : mBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final HomeRVHolder holder;
        if (view == null) {
            holder = new HomeRVHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.home_rv_layout, null);
            holder.mItemLayout = (LinearLayout) view.findViewById(R.id.home_rv_layout);
            holder.mIcon = (ImageView) view.findViewById(R.id.home_rv_icon);
            holder.mGameName = (TextView) view.findViewById(R.id.home_rv_game_name);
            holder.mSize = (TextView) view.findViewById(R.id.home_rv_game_size);
            holder.mType1 = (TextView) view.findViewById(R.id.home_rv_game_type1);
            holder.mType2 = (TextView) view.findViewById(R.id.home_rv_game_type2);
            holder.mType3 = (TextView) view.findViewById(R.id.home_rv_game_type3);
            holder.mType4 = (TextView) view.findViewById(R.id.home_rv_game_type4);
            holder.mGameContent = (Button) view.findViewById(R.id.home_rv_game_content);
            view.setTag(holder);
        } else {
            holder = (HomeRVHolder) view.getTag();
        }

        final HomeRVBean.DataBean.GameListBean gameListBean = mBeanList.get(i);
        holder.mGameName.setText(gameListBean.getGamename());
        GlideUtil.loadImage(gameListBean.getIcon(), holder.mIcon, R.mipmap.load_icon);
        if (!TextUtils.isEmpty(gameListBean.getType())) {
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
        return view;
    }

    class HomeRVHolder {

        ImageView mIcon;
        TextView mGameName;
        TextView mSize;
        TextView mType1;
        TextView mType2;
        TextView mType3;
        TextView mType4;
        Button mGameContent;
        LinearLayout mItemLayout;
    }
}
