package com.i76game.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.i76game.GameCountAdapter;
import com.i76game.R;
import com.i76game.activity.ImagePagerActivity;
import com.i76game.adapter.RecommendAdapter;
import com.i76game.bean.GameContentBean;
import com.i76game.utils.LogUtils;
import com.i76game.view.TextViewExpandableAnimation;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/6.
 */

public class GameDetailFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private TextViewExpandableAnimation mExpandableAnimation, welfare_game, welfare_recharge;
    private GameContentBean.DataBean mData;
    private GameCountAdapter mAdapter;
    private GridView recommend_gv;
    private RecommendAdapter gvAdapter;

    public GameDetailFragment(GameContentBean.DataBean mData) {
        this.mData = mData;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, null);
        initView();
        initDate();
        return view;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.game_content_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mExpandableAnimation = (TextViewExpandableAnimation) view.findViewById(R.id.expand_text_view);
        welfare_game = (TextViewExpandableAnimation) view.findViewById(R.id.welfare_game);
        welfare_recharge = (TextViewExpandableAnimation) view.findViewById(R.id.welfare_recharge);
        recommend_gv = (GridView) view.findViewById(R.id.recommend_gv);
        gvAdapter = new RecommendAdapter(getActivity());
        recommend_gv.setAdapter(gvAdapter);
        mAdapter = new GameCountAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDate() {
        if (mData != null) {
            LogUtils.i("game_id" + mData.getGameid());
            mExpandableAnimation.setText(mData.getDisc());
            welfare_game.setText(mData.getDisc());
            welfare_recharge.setText(mData.getDisc());
            final ArrayList<String> imageList = (ArrayList<String>) mData.getImage();
            mAdapter.setImageUrls(imageList);
            mAdapter.notifyDataSetChanged();
            mAdapter.setOnItemClickListener(new GameCountAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
                    intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageList);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gvAdapter.notifyDataSetChanged();
    }
}
