package com.i76game.activity;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.adapter.GameListAdapter;
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/16.
 */

public class CrackListActivity extends BaseActivity {
    private XRecyclerView mRecyclerView;
    private List<HomeRVBean.DataBean.GameListBean> mGameList;
    private GameListAdapter mAdapter;
    private View layoutNoData;
    private LinearLayout loading_ll;
    private Toolbar crack_toolbar;
    private int mPageIndex = 1;//加载更多的页数
    private Map<String, String> mMap = new HashMap<>();

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_carck_list;
    }

    @Override
    public void initView() {
        layoutNoData = findViewById(R.id.layout_noData);
        crack_toolbar = (Toolbar) findViewById(R.id.crack_toolbar);
        setToolbar("破解版", R.id.crack_toolbar);
        mRecyclerView = (XRecyclerView) findViewById(R.id.game_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            crack_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
    }

    @Override
    public void initData() {
        addMapItem();
        layoutNoData.setVisibility(View.GONE);
        mGameList = new ArrayList<>();
        mAdapter = new GameListAdapter(CrackListActivity.this, mGameList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mMap.put("page", mPageIndex + "");
                request(mMap);
            }
        });
        request(mMap);
    }

    //请求破解和变态版的map
    private void addMapItem() {
        layoutNoData.setVisibility(View.GONE);
        mMap.clear();
        mMap.put("agent", Global.agent);
        mMap.put("category", "1");
        mMap.put("offset", MyApplication.num);
        mMap.put("appid", Global.appid);
        mMap.put("clientid", Global.clientid);
        mMap.put("classify", "1");
        mMap.put("from", Global.from);
        mMap.put("page", "1");
    }

    private void request(Map<String, String> map) {
        if (mPageIndex == 1) {
            loading_ll.setVisibility(View.VISIBLE);
        }
        LogUtils.i("" + Utils.getCompUrlFromParams(Global.Hot_GAME_URL + "game/list", map));
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
                        mRecyclerView.refreshComplete();
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            if (mPageIndex == 1) {
                                mGameList = homeRVBean.getData().getGame_list();
                                mAdapter.setData(mGameList);
                            } else {
                                mAdapter.addData(homeRVBean.getData().getGame_list());
                            }
                            mAdapter.notifyDataSetChanged();
                            if (homeRVBean.getData().getGame_list().size() < Integer.valueOf(MyApplication.num)){
                                mRecyclerView.setNoMore(true);
                            }else {
                                mRecyclerView.setNoMore(false);
                            }
                        } else {
                            if (mPageIndex == 1){
                                mRecyclerView.setNoMore(true);
                                showToast("暂无数据哦", Toast.LENGTH_SHORT);
                            }else {
                                mRecyclerView.setNoMore(true);
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
        if (mAdapter.getDateList().size() == 0) {
            layoutNoData.setVisibility(View.VISIBLE);
        }
        mRecyclerView.loadMoreComplete();
//        mRecyclerView.refreshComplete();
        //刷新加载过一次后加1
        mPageIndex++;
    }
}
