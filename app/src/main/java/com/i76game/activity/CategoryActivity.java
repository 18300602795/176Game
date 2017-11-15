package com.i76game.activity;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.adapter.GameListAdapter;
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.i76game.view.LoadDialog;
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
 * Created by Administrator on 2017/11/15.
 */

public class CategoryActivity extends BaseActivity {
    private GameListAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private List<HomeRVBean.DataBean.GameListBean> mGameListBean = new ArrayList<>();
    private int currentPage=1;
    private Toolbar category_toolbar;
    private String name;
    private String id;
    private Map<String, String> mMap = new HashMap<>();
    @Override
    protected int setLayoutResID() {
        return R.layout.category_activity;
    }

    @Override
    public void initView() {
        name = getIntent().getStringExtra("category_name");
        id = getIntent().getStringExtra("category_id");
        setToolbar(name, R.id.category_toolbar);
        category_toolbar = (Toolbar) findViewById(R.id.category_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            category_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mRecyclerView = (XRecyclerView) findViewById(R.id.home_rv);
        mAdapter = new GameListAdapter(this, mGameListBean);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        refreshListener();
        addTypeMap(id);
        request(mMap);
    }

    //请求类型的map
    private Map<String, String> addTypeMap(String typeId) {
        mMap.clear();
        mMap.put("appid", Global.appid);
        mMap.put("clientid", Global.clientid);
        mMap.put("agent", Global.agent);
        mMap.put("from", Global.from);
        mMap.put("page", "1");
        mMap.put("type", typeId);
        mMap.put("offset", "30");
        return mMap;
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

    private LoadDialog mLoadDialog;

    private void request(Map<String, String> map) {
        if (mLoadDialog == null) {
            mLoadDialog = new LoadDialog(this, true, "100倍加速中");
            mLoadDialog.show();
        }
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
    /**
     * 隐藏对话框
     */
    private void hideDialog() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
        mRecyclerView.refreshComplete();
    }

    private Toast toast = null;
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
