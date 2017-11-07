package com.i76game.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.activity.InformationContentActivity;
import com.i76game.bean.InformationRVBean;
import com.i76game.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class InformationAdapter extends RecyclerView.Adapter<InformationHolder> {
    private Activity mActivity;
    private List<InformationRVBean.DataBean.NewsListBean> mInformationList;

    public InformationAdapter(List<InformationRVBean.DataBean.NewsListBean> mInformationList, Activity mActivity) {
        this.mActivity = mActivity;
        this.mInformationList = mInformationList;
    }

    public void addDate(List<InformationRVBean.DataBean.NewsListBean> mInformationList) {
        if (this.mInformationList == null)
            mInformationList = new ArrayList<>();
        this.mInformationList.addAll(mInformationList);
        notifyDataSetChanged();
    }


    @Override
    public InformationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity)
                .inflate(R.layout.information_rv_layout, parent, false);
        return new InformationHolder(view);
    }

    @Override
    public void onBindViewHolder(InformationHolder holder, int position) {
        final InformationRVBean.DataBean.NewsListBean informationBean = mInformationList.get(position);
        GlideUtil.loadImage(mActivity, informationBean.getImg(), holder.mImageView,
                R.mipmap.icon_horizontal);

        holder.mTitleText.setText(informationBean.getTitle());
        holder.mTimeText.setText(informationBean.getPudate());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = informationBean.getId();
                Intent intent = new Intent(mActivity, InformationContentActivity.class);
                intent.putExtra("id", id);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInformationList == null ? 0 : mInformationList.size();
    }
}


class InformationHolder extends RecyclerView.ViewHolder {
    TextView mTimeText;
    ImageView mImageView;
    TextView mTitleText;

    public InformationHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.information_rv_image);
        mTitleText = (TextView) itemView.findViewById(R.id.information_rv_title);
        mTimeText = (TextView) itemView.findViewById(R.id.information_rv_time);
    }
}
