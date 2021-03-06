package com.i76game.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.adapter.InformationAdapter;
import com.i76game.bean.InformationRVBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.i76game.view.CustomLinearLayoutManager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/8.
 */

public class MessageFragment2 extends BaseFragment{
    private List<InformationRVBean.DataBean.NewsListBean> mInformationList;
    private InformationAdapter mAdapter;
    private View view;
    public XRecyclerView recyclerView;
    private int currentPage = 1;
    public String app_id = "";
    private View layoutNoData;
    private LinearLayout loading_ll;
    public boolean isDate;
    public CustomLinearLayoutManager linearLayoutManager;
    public String post_type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_fragment, null);
        initView();
        return view;
    }

    public void initView() {
        recyclerView = (XRecyclerView) view.findViewById(R.id.information_rv);
        linearLayoutManager = new CustomLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mInformationList = new ArrayList<>();
        layoutNoData = view.findViewById(R.id.layout_noData);
        loading_ll = (LinearLayout) view.findViewById(R.id.loading_ll);
        if (isDate) {
            initDate();
        }

    }



    @Override
    public void initDate() {
        LogUtils.i("msg---开始加载数据");
        recyclerView.setVisibility(View.GONE);
        isShow = false;
        mAdapter = new InformationAdapter(mInformationList, getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        getDate();
        layoutNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage = 1;
                loading_ll.setVisibility(View.VISIBLE);
                getDate();
            }
        });

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getDate();
            }

            @Override
            public void onLoadMore() {
                currentPage += 1;
                getDate();
            }
        });
    }

    private void getDate() {
        layoutNoData.setVisibility(View.GONE);
        ArrayMap<String, String> map = new ArrayMap<>();
//        map.put("appid", Global.appid);
        if (!Utils.isEmpty(app_id)) {
            map.put("gameid", app_id);
        }
        map.put("appid", "100");
        map.put("post_type", post_type);
        map.put("clientid", Global.clientid);
        map.put("agent", "");
        map.put("from", "3");
        map.put("page", String.valueOf(currentPage));
        map.put("offset", MyApplication.num);
        map.put("catalog", "0");
        LogUtils.i("" + Utils.getCompUrlFromParams(Global.Hot_GAME_URL + "news/list", map));
        RetrofitUtil.getInstance().create(HttpServer.InformationService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InformationRVBean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull InformationRVBean informationRVBean) {
                        recyclerView.refreshComplete();
                        try {
                            LogUtils.i("msg：" + informationRVBean.getMsg());
                            LogUtils.i("code：" + informationRVBean.getCode());
                        } catch (Exception e) {
                            LogUtils.e(e.toString());
                        }
                        if (informationRVBean != null && informationRVBean.getCode() == 200 && informationRVBean.getData().getNews_list().size() > 0) {
                            mInformationList = informationRVBean.getData().getNews_list();
                            mAdapter.addDate(mInformationList);
                            if (mInformationList.size() < Integer.valueOf(MyApplication.num)){
                                recyclerView.setNoMore(true);
                            }else {
                                recyclerView.setNoMore(false);
                            }
                        } else {
                            if (currentPage == 1) {
                                recyclerView.setNoMore(true);
                                showToast("暂无数据哦", Toast.LENGTH_SHORT);
                            } else {
                                recyclerView.setNoMore(true);
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

    /**
     * 隐藏对话框
     */
    private void hideDialog() {
        LogUtils.i("隐藏dialog");
//        layoutNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        loading_ll.setVisibility(View.GONE);
//        recyclerView.refreshComplete();
        if (mAdapter.getDateList().size() == 0) {
            LogUtils.i("没有数据");
            layoutNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
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
