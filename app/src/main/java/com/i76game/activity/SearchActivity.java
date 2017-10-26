package com.i76game.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;

import com.i76game.R;
import com.i76game.bean.HomeRVBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private List<HomeRVBean.DataBean.GameListBean> mGameList;
    private Map<String, String> mMap;
    private RecyclerView mRecyclerView;
    private GameListAdapter mAdapter;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        SearchView searchView = (SearchView) findViewById(R.id.search_search_view);
        ImageView back = (ImageView) findViewById(R.id.search_back);
        back.setOnClickListener(this);

        mGameList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.search_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GameListAdapter(this, mGameList);
        mRecyclerView.setAdapter(mAdapter);

        mMap = new HashMap<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    return false;
                }
                mMap.put("q", newText);
                requestResult(mMap);
                return true;
            }
        });

        mMap.put("q", "");
        mMap.put("appid", Global.appid);
        mMap.put("agent", Global.agent);
        mMap.put("clientid", "12");
        mMap.put("from", Global.from);
        mMap.put("catalog", "");
        mMap.put("clientid", Global.clientid);
        mMap.put("appid", Global.appid);
        mMap.put("agent", Global.agent);
        mMap.put("from", Global.from);

    }

    private void requestResult(Map<String, String> map) {
        RetrofitUtil.getInstance()
                .create(HttpServer.SearchService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeRVBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HomeRVBean homeRVBean) {
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            mAdapter.setData(homeRVBean.getData().getGame_list());
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                finish();
                break;
//            case R.id.search_text:
//                requestResult(mMap);
//                break;
        }
    }
}
