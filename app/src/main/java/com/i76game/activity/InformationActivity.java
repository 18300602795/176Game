package com.i76game.activity;

import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.adapter.MessageAdapter;
import com.i76game.bean.InformationRVBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/13.
 */

public class InformationActivity extends BaseActivity {

    private List<InformationRVBean.DataBean.NewsListBean> mInformationList;
    private MessageAdapter mAdapter;
    private String type, title;
    private XRecyclerView recyclerView;
    private int currentPage=1;
    private Toolbar information_toolbar;
    private View layoutNoData;
    private LinearLayout loading_ll;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_information;
    }

    @Override
    public void initView() {
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        setToolbar(title, R.id.information_toolbar);
        layoutNoData = findViewById(R.id.layout_noData);
        loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
        information_toolbar = (Toolbar) findViewById(R.id.information_toolbar);
        recyclerView = (XRecyclerView) findViewById(R.id.information_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInformationList = new ArrayList<>();
        mAdapter = new MessageAdapter(mInformationList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            information_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
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
        map.put("appid", Global.appid);
        map.put("clientid", Global.clientid);
        map.put("agent", "");
        map.put("from", "3");
        map.put("page", String.valueOf(currentPage));
        map.put("offset", MyApplication.num);
        map.put("catalog", "0");
        map.put("post_type", type);
        LogUtils.i("" + currentPage);
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
                        try{
                            LogUtils.i("msg：" + informationRVBean.getMsg());
                            LogUtils.i("code：" + informationRVBean.getCode());
                        }catch (Exception e){
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
                            if (currentPage == 1){
                                recyclerView.setNoMore(true);
                                showToast("暂无数据哦", Toast.LENGTH_SHORT);
                            }else {
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
        loading_ll.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
//        recyclerView.refreshComplete();
        if (mAdapter.getDateList().size() == 0) {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
}
