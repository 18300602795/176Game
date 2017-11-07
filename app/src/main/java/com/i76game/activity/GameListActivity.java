package com.i76game.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.bean.HomeRVBean;
import com.i76game.bean.TypeBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.RetrofitUtil;
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
 * Created by Administrator on 2017/5/16.
 */

public class GameListActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout mTabLayout;
    private PopupWindow mPopupWindow;
    private String mTitles[] = {"热门", "游戏福利", "类型"};
    private String mTypes[];
    private List<String> mTypeId = new ArrayList<>();
    private XRecyclerView mRecyclerView;
    private TextView mTvBt;
    private TextView mTvBreak;

    private GridView mPopupGrid;
    private List<HomeRVBean.DataBean.GameListBean> mGameList;
    private GameListAdapter mAdapter;

    private Map<String, String> mMap = new HashMap<>();
    private View mPopupView1, mPopupView2;
    private LoadDialog mLoadDialog;
    private int mPageIndex = 1;//加载更多的页数
    private View mLastView;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_game_list;
    }

    @Override
    public void initView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.game_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTabLayout = (TabLayout) findViewById(R.id.game_list_table_layout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTitles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab().setText(mTitles[i]);
            tab.setTag(mTitles[i]);
            mTabLayout.addTab(tab, i);
        }

        LayoutInflater inflater = LayoutInflater.from(GameListActivity.this);
        //福利游戏
        mPopupView1 = inflater.inflate(R.layout.game_list_popup1, null);
        mTvBt = (TextView) mPopupView1.findViewById(R.id.popup1_bt);
        mTvBreak = (TextView) mPopupView1.findViewById(R.id.popup1_break);
        mTvBt.setOnClickListener(this);
        mTvBreak.setOnClickListener(this);
        //分类
        mPopupView2 = inflater.inflate(R.layout.game_list_popup2, null);

        mPopupGrid = (GridView) mPopupView2.findViewById(R.id.popup2_grid_view);
        mPopupGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPageIndex = 1;
                resetPopup1();
                if (mLastView != null) {
                    mLastView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                mLastView = view;
                request(addTypeMap(mTypeId.get(position)));
                isShow(mPopupView2);
                view.setBackgroundColor(Color.parseColor("#bebec4"));

            }
        });
        ImageView back = (ImageView) findViewById(R.id.game_list_back);
        back.setOnClickListener(this);
        RelativeLayout searchLayout = (RelativeLayout) findViewById(R.id.game_list_search_layout);
        searchLayout.setOnClickListener(this);
        tabListener();
    }

    private void tabListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() == mTitles[0]) {
                    mPageIndex = 1;
                    resetPopup1();
                    if (mLastView != null) {
                        mLastView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    request(addHotMap());
                } else if (tab.getTag() == mTitles[1]) {
                    openWindow(mPopupView1);
                } else if (tab.getTag() == mTitles[2]) {
                    openWindow(mPopupView2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getTag() == mTitles[1]) {
                    //如果popupwindow出来，再点击不会触发点击事件。保险一点还是加个判断
                    isShow(mPopupView1);
                } else if (tab.getTag() == mTitles[2]) {
                    isShow(mPopupView2);
                }
            }
        });
    }


    private void isShow(View view) {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            openWindow(view);
        }
    }

    @Override
    public void initData() {
        mGameList = new ArrayList<>();
        mAdapter = new GameListAdapter(GameListActivity.this, mGameList);
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
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("clientid", Global.clientid);
        map.put("appid", Global.appid);
        map.put("agent", Global.agent);
        map.put("from", Global.from);

        request(addHotMap());
        RetrofitUtil.getInstance()
                .create(HttpServer.TypeService.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TypeBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull TypeBean typeBean) {
                        if (typeBean.getData() != null && typeBean.getCode() == 200) {
                            mTypes = new String[typeBean.getData().size()];
                            for (int i = 0; i < typeBean.getData().size(); i++) {
                                TypeBean.DataBean dataBean = typeBean.getData().get(i);
                                mTypes[i] = dataBean.getTypename();
                                mTypeId.add(dataBean.getTypeid() + "");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mPopupGrid.setAdapter(new ArrayAdapter<String>(GameListActivity.this
                                , android.R.layout.simple_list_item_1, mTypes));
                    }
                });

    }

    /**
     * 初始化popupwindow的参数
     *
     * @param view
     */
    private void openWindow(View view) {
        View popupOut = view.findViewById(R.id.popup_out);
        mPopupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAsDropDown(mTabLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_list_back:
                finish();
                break;

            case R.id.game_list_search_layout:
                startActivity(new Intent(this, SearchActivity.class));
                break;

            //变态版
            case R.id.popup1_bt:
                mPageIndex = 1;
                mTvBreak.setTextColor(getResources().getColor(R.color.main_item_text_color));
                mTvBt.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                request(addMapItem("2"));
                if (mLastView != null) {
                    mLastView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                isShow(mPopupView1);
                break;
            //破解版
            case R.id.popup1_break:
                mPageIndex = 1;
                mTvBreak.setTextColor(getResources().getColor(R.color.main_item_text_color_p));
                mTvBt.setTextColor(getResources().getColor(R.color.main_item_text_color));
                request(addMapItem("1"));
                if (mLastView != null) {
                    mLastView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                isShow(mPopupView1);
                break;
        }
    }

    //请求破解和变态版的map
    private Map<String, String> addMapItem(String category) {
        mMap.clear();
        mMap.put("agent", Global.agent);
        mMap.put("category", category);
        mMap.put("offset", "30");
        mMap.put("appid", Global.appid);
        mMap.put("clientid", Global.clientid);
        mMap.put("classify", "1");
        mMap.put("from", Global.from);
        mMap.put("page", "1");
        return mMap;
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

    //热门游戏的map
    private Map<String, String> addHotMap() {
        mMap.clear();
        mMap.put("agent", Global.agent);
        mMap.put("category", "2");
        mMap.put("offset", "30");
        mMap.put("hot", "1");
        mMap.put("appid", Global.appid);
        mMap.put("clientid", Global.clientid);
        mMap.put("from", Global.from);
        mMap.put("page", "1");
        return mMap;
    }

    private void resetPopup1() {
        mTvBreak.setTextColor(getResources().getColor(R.color.main_item_text_color));
        mTvBt.setTextColor(getResources().getColor(R.color.main_item_text_color));
    }


    private void request(Map<String, String> map) {
        if (mLoadDialog == null) {
            mLoadDialog = new LoadDialog(this, true, "100倍加速中");
        }
        if (mPageIndex == 1) {
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
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            if (mPageIndex == 1) {
                                mGameList = homeRVBean.getData().getGame_list();
                                mAdapter.setData(mGameList);
                            } else {
                                mAdapter.addData(homeRVBean.getData().getGame_list());
                            }

                            mAdapter.notifyDataSetChanged();
                        } else {
                            if (mPageIndex == 1){
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
        mRecyclerView.loadMoreComplete();
        mRecyclerView.refreshComplete();
        //刷新加载过一次后加1
        mPageIndex++;
    }

}
