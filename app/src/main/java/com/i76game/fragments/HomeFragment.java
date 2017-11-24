package com.i76game.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.activity.GameListActivity;
import com.i76game.adapter.GameListAdapter;
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.RetrofitUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/8.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Activity mActivity;
    private GameListAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private List<HomeRVBean.DataBean.GameListBean> mGameListBean = new ArrayList<>();
    private ArrayMap<String, String> mMap;
    private int currentPage=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        mActivity = getActivity();
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.home_rv);
        mAdapter = new GameListAdapter(mActivity, mGameListBean);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.home_head_layout, container, false);
        LinearLayout headGameList = (LinearLayout) headView.findViewById(R.id.head_game_list);
        headGameList.setOnClickListener(this);
        mRecyclerView.addHeaderView(headView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        refreshListener();

        mMap = new ArrayMap<>();
        mMap.put("agent", "");
        mMap.put("category", "2");
        mMap.put("offset", "30");
        mMap.put("appid", "100");
        mMap.put("clientid", "49");
        mMap.put("classify", "1");
        mMap.put("from", "3");
        mMap.put("page", String.valueOf(currentPage));
        request(mMap);

        return view;
    }

    //下拉刷新
    private void refreshListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                request(mMap);
            }

            @Override
            public void onLoadMore() {
                currentPage += 1;
                mMap.put("page", String.valueOf(currentPage));
                request(mMap);
            }
        });
    }

//    private LoadDialog mLoadDialog;

    private void request(Map<String, String> map) {
//        if (mLoadDialog == null) {
//            mLoadDialog = new LoadDialog(mActivity, true, "100倍加速中");
//            mLoadDialog.show();
//        }
        RetrofitUtil.getInstance()
                .create(HttpServer.HotService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeRVBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HomeRVBean homeRVBean) {
                        try{
                            LogUtils.i("msg：" + homeRVBean.getMsg());
                            LogUtils.i("code：" + homeRVBean.getCode());
                        }catch (Exception e){
                            LogUtils.e(e.toString());
                        }
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            mGameListBean = homeRVBean.getData().getGame_list();
                            mAdapter.addData(mGameListBean);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if (currentPage == 1){
                                showToast("暂无数据哦", Toast.LENGTH_SHORT);
                            }else {
                                showToast("没有更多数据哦", Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        hideDialog();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_game_list:
                startActivity(new Intent(mActivity, GameListActivity.class));
                break;
        }
    }

    /**
     * 隐藏对话框
     */
    private void hideDialog() {
//        if (mLoadDialog != null) {
//            mLoadDialog.dismiss();
//        }
        mRecyclerView.refreshComplete();
    }

    private Toast toast = null;
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
