package com.i76game.adapter;

/**
 * Created by Administrator on 2017/11/14.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.i76game.R;
import com.i76game.activity.GameInfoActivity;
import com.i76game.bean.LunboImgViewBean;
import com.i76game.utils.Global;
import com.i76game.utils.ImageCache;
import com.i76game.utils.ImgUtil;

import java.util.List;

/**
 * head1图片轮播适配器
 */
public class mvViewPagerAdapter extends PagerAdapter {
    private List<LunboImgViewBean.DataBean.ListBean> listbean;// 装载着轮播图的信息
    private Context context;
    private Fragment fragment;

    public mvViewPagerAdapter(List<LunboImgViewBean.DataBean.ListBean> listbean, Context context, Fragment fragment) {
        this.listbean = listbean;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return listbean == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container,
                                  final int position) {
        ImageView img = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                -1, -1);
        img.setLayoutParams(layoutParams);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        final LunboImgViewBean.DataBean.ListBean listBean = listbean.get(position % listbean.size());
        if (listBean.getImage() == null) {
            ImageCache.scaleLoad(img, R.mipmap.banner,
                    container.getContext());
        } else {
            ImgUtil.loadImage(listBean.getImage()
                    , R.mipmap.icon_horizontal,img, fragment);
        }
        // 获取gameid 如果是0直接跳转链接，不是0跳转到游戏详情
        // 对轮播图点击监听
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = listBean.getDes();
                if (des.equals("")) {
                    return;
                }
                enterIntoGameDetails(Integer.parseInt(listBean.getDes()));
            }
        });
        container.addView(img);
        return img;
    }
    /**
     * 进入首页列表的详情
     *
     * @param gameID 游戏的id
     */
    public void enterIntoGameDetails(int gameID) {
        Intent intent3 = new Intent(context, GameInfoActivity.class);
        Global.drawable = null;
        intent3.putExtra(Global.GAME_ID, gameID);
        context.startActivity(intent3);
        ((Activity) context).overridePendingTransition(R.anim.act_in,
                R.anim.act_out);
    }

}
