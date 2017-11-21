package com.i76game.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.activity.CustomerServiceActivity;
import com.i76game.activity.DownloadActivity;
import com.i76game.activity.EarnActivity;
import com.i76game.activity.FenLeiActivity;
import com.i76game.activity.GameListActivity;
import com.i76game.activity.InformationActivity;
import com.i76game.activity.InviteActivity;
import com.i76game.activity.SearchActivity;
import com.i76game.adapter.GameListAdapter2;
import com.i76game.adapter.mvViewPagerAdapter;
import com.i76game.bean.HomeRVBean;
import com.i76game.bean.LunboImgViewBean;
import com.i76game.download.DownloadAPKManager;
import com.i76game.download.DownloadService;
import com.i76game.inter.Imylistener;
import com.i76game.model.FilterData;
import com.i76game.utils.ColorUtil;
import com.i76game.utils.DensityUtil;
import com.i76game.utils.DimensionUtil;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.ModelUtil;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.i76game.view.FilterView_home;
import com.i76game.view.HeaderBannerView_home;
import com.i76game.view.HeaderBannerView_home2;
import com.i76game.view.HeaderFilterView_home;
import com.i76game.view.LoadDialog;
import com.i76game.view.SmoothListView;
import com.i76game.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.i76game.R.id.main_img_mine;


/**
 * Created by Administrator on 2017/5/8.
 */

public class HomeFragment2 extends Fragment implements View.OnClickListener, SmoothListView.ISmoothListViewListener {
    private GameListAdapter2 mAdapter;
    private SmoothListView smoothListView;
    private List<HomeRVBean.DataBean.GameListBean> mGameListBean = new ArrayList<>();
    private ArrayMap<String, String> mMap;
    private int currentPage = 1;
    private mvViewPagerAdapter mvViewPageradapter;// 轮播图适配器
    private List<View> mDots = new ArrayList<>();// 存放圆点视图的集合
    private List<LunboImgViewBean.DataBean.ListBean> listbean;// 装载着轮播图的
    private VpSwipeRefreshLayout home_refresh;
    private RelativeLayout mSearchLayout;//搜索
    private ImageView mineImage;
    private Imylistener imylisterer;

    FilterView_home realFilterView;
    LinearLayout rlBar;
    private DownloadAPKManager mDownloadAPKManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (home_refresh != null) {
                home_refresh.setRefreshing(false);
            }
        }
    };

    public void setListener(Imylistener imylisterer) {
        this.imylisterer = imylisterer;
    }

    private int mScreenHeight; // 屏幕高度

    private List<String> bannerList = new ArrayList<>(); // 广告数据
//    private List<TravelingEntity> travelingList = new ArrayList<>(); // ListView数据

    private HeaderBannerView_home headerBannerView; // 广告视图
    private HeaderBannerView_home2 headerBannerView2; // 广告视图
    private HeaderFilterView_home headerFilterView; // 分类筛选视图
    private FilterData filterData; // 筛选数据

    private int titleViewHeight = 65; // 标题栏的高度

    private View itemHeaderBannerView; // 从ListView获取的广告子View
    private int bannerViewHeight; // 广告视图的高度
    private int bannerViewTopMargin; // 广告视图距离顶部的距离

    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private int filterViewPosition = 4; // 筛选视图的位置
    private int filterViewTopMargin; // 筛选视图距离顶部的距离
    private boolean isScrollIdle = true; // ListView是否在滑动
    private boolean isStickyTop = false; // 是否吸附在顶部
    private boolean isSmooth = false; // 没有吸附的前提下，是否在滑动
    private int filterPosition = -1; // 点击FilterView的位置：分类(0)、排序(1)、筛选(2)

    public void initData() {
        // 设置广告数据
        headerBannerView = new HeaderBannerView_home(getActivity());
        headerBannerView.fillView(bannerList, smoothListView);

        // 设置广告数据
        headerBannerView2 = new HeaderBannerView_home2(getActivity());
        headerBannerView2.fillView(bannerList, smoothListView);


        // 设置假FilterView数据
        headerFilterView = new HeaderFilterView_home(getActivity());
        headerFilterView.fillView(new Object(), smoothListView);

        // 设置真FilterView数据
//        realFilterView.setFilterData(mActivity, filterData);
        realFilterView.setVisibility(View.GONE);

        filterViewPosition = smoothListView.getHeaderViewsCount() - 1;
        initListener();

        mDownloadAPKManager = DownloadService.getDownloadManager(MyApplication.getContextObject());
        mAdapter = new GameListAdapter2(getActivity(), mGameListBean);
        smoothListView.setAdapter(mAdapter);
        getDataLunboImgView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LogUtils.i("透明导航栏");
            rlBar.setPadding(0, Utils.dip2px(getActivity(), 10), 0, 0);
        }
        headerBannerView2.game_ll.setOnClickListener(this);
        headerBannerView2.fenlei_ll.setOnClickListener(this);
        headerBannerView2.bangdan_ll.setOnClickListener(this);
        headerBannerView2.earn_ll.setOnClickListener(this);
        headerBannerView2.server_ll.setOnClickListener(this);
        headerBannerView2.activity_ll.setOnClickListener(this);
        headerBannerView2.share_ll.setOnClickListener(this);
    }

    private void initListener() {

        // (假的ListView头部展示的)筛选视图点击
        headerFilterView.getFilterView().setOnFilterClickListener(new FilterView_home.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                isSmooth = true;
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(getActivity(), titleViewHeight));
            }
        });

        // (真正的)筛选视图点击
        realFilterView.setOnFilterClickListener(new FilterView_home.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                realFilterView.show(position);
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(getActivity(), titleViewHeight));
            }
        });
        smoothListView.setLoadMoreEnable(true);
        smoothListView.setSmoothListViewListener(this);
        smoothListView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
            @Override
            public void onSmoothScrolling(View view) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (isScrollIdle && bannerViewTopMargin < 0) return;

                // 获取广告头部View、自身的高度、距离顶部的高度
                if (itemHeaderBannerView == null) {
                    LogUtils.i("1111111");
                    itemHeaderBannerView = smoothListView.getChildAt(1);
                }
                if (itemHeaderBannerView != null) {
                    bannerViewTopMargin = DensityUtil.px2dip(getActivity(), itemHeaderBannerView.getTop());
                    bannerViewHeight = DensityUtil.px2dip(getActivity(), itemHeaderBannerView.getHeight());
                }

                // 获取筛选View、距离顶部的高度
                if (itemHeaderFilterView == null) {
                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderFilterView != null) {
                    filterViewTopMargin = DensityUtil.px2dip(getActivity(), itemHeaderFilterView.getTop());
                }
//                if (filterViewTopMargin <= titleViewHeight || firstVisibleItem > filterViewPosition) {
//                    LogUtils.i("吸附到顶部");
//                    isStickyTop = true; // 吸附在顶部
//                    realFilterView.setVisibility(View.VISIBLE);
//                } else {
//                    LogUtils.i("没有吸附在顶部");
                    isStickyTop = false; // 没有吸附在顶部
                    realFilterView.setVisibility(View.GONE);
//                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    realFilterView.show(filterPosition);
                }

                // 处理标题栏颜色渐变
                handleTitleBarColorEvaluate();
            }
        });
    }


    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (bannerViewTopMargin > 0) {
            fraction = 1f - bannerViewTopMargin * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            rlBar.setAlpha(fraction);
            return;
        }

        float space = Math.abs(bannerViewTopMargin) * 1f;
        fraction = space / (bannerViewHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        rlBar.setAlpha(1f);
        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;
            rlBar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
        } else {
            rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getActivity(), fraction, R.color.transparent2, R.color.blue));
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        currentPage += 1;
        mMap.put("page", String.valueOf(currentPage));
        request(mMap);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment2, null);
        smoothListView = (SmoothListView) view.findViewById(R.id.home_st);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_head_layout, container, false);
        home_refresh = (VpSwipeRefreshLayout) view.findViewById(R.id.home_refresh);
        LinearLayout headGameList = (LinearLayout) headView.findViewById(R.id.head_game_list);
        headGameList.setOnClickListener(this);
//        smoothListView.addHeaderView(headView);
        realFilterView = (FilterView_home) view.findViewById(R.id.real_filterView);
        rlBar = (LinearLayout) view.findViewById(R.id.rl_bar);
        mScreenHeight = DensityUtil.getWindowHeight(getActivity());
        // 筛选数据
        filterData = new FilterData();
        filterData.setCategory(ModelUtil.getCategoryData());
        filterData.setSorts(ModelUtil.getSortData());
        filterData.setFilters(ModelUtil.getFilterData());
//         广告数据
        bannerList = ModelUtil.getBannerData();
        mineImage = (ImageView) view.findViewById(main_img_mine);
        mineImage.setOnClickListener(this);
        mSearchLayout = (RelativeLayout) view.findViewById(R.id.main_search_layout);
        mSearchLayout.setOnClickListener(this);
        ImageView downloadBtn = (ImageView) view.findViewById(R.id.main_download);
        downloadBtn.setOnClickListener(this);
//         频道数据
//        channelList = ModelUtil.getChannelData();
//         运营数据
//        operationList = ModelUtil.getOperationData();
//         ListView数据
        initData();
//        travelingList = ModelUtil.getTravelingData();
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
        home_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                mMap.put("page", String.valueOf(currentPage));
                request(mMap);
                getDataLunboImgView();

            }
        });
        return view;
    }

    private LoadDialog mLoadDialog;

    private void request(Map<String, String> map) {
        if (mLoadDialog == null) {
            mLoadDialog = new LoadDialog(getActivity(), true, "100倍加速中");
//            mLoadDialog.show();
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
                        smoothListView.stopLoadMore();
                        handler.sendMessage(new Message());
                        try {
                            LogUtils.i("msg：" + homeRVBean.getMsg());
                            LogUtils.i("code：" + homeRVBean.getCode());
                        } catch (Exception e) {
                            LogUtils.e(e.toString());
                        }
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            mGameListBean = homeRVBean.getData().getGame_list();
                            mAdapter.addData(mGameListBean);
                            mAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(getActivity(), GameListActivity.class));
                break;
            case R.id.main_img_mine:
                imylisterer.Onclick();
                break;
            case R.id.game_ll:
                startActivity(new Intent(getActivity(), GameListActivity.class));
                break;
            case R.id.fenlei_ll:
                startActivity(new Intent(getActivity(), FenLeiActivity.class));
                break;
            case R.id.bangdan_ll:
//                startActivity(new Intent(getActivity(), GameListActivity.class));
                break;
            case R.id.earn_ll:
                startActivity(new Intent(getActivity(), EarnActivity.class));
                break;
            case R.id.server_ll:
                startActivity(new Intent(getActivity(), CustomerServiceActivity.class));
                break;
            case R.id.activity_ll:
                Intent intent = new Intent();
                intent.setClass(getActivity(), InformationActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("title", "最新活动");
                startActivity(intent);
                break;
            case R.id.share_ll:
//                startActivity(new Intent(getActivity(), GameListActivity.class));
                startActivity(new Intent(getActivity(), InviteActivity.class));
//                Utils.showShare(getActivity(), "欢迎下载176Game", "", "", "http://down.shouyoucun.cn/sdkgame/syc_60123/syc_60123.apk");
                break;
            case R.id.main_download:
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                break;

            case R.id.main_search_layout:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    /**
     * 隐藏对话框
     */
    private void hideDialog() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
        if (currentPage <= 1) {
//                                showToast("暂无数据哦", Toast.LENGTH_SHORT);
            smoothListView.mFooterView.setState(3);
        } else {
            smoothListView.mFooterView.setState(4);
//                                showToast("没有更多数据哦", Toast.LENGTH_SHORT);
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

    Handler abc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = headerBannerView.mvViewPager.getCurrentItem();
            headerBannerView.mvViewPager.setCurrentItem(currentItem + 1);
        }
    };

    /**
     * 开始轮播图片
     */
    private void startScrollView() {
        if (mvViewPageradapter == null) {
            mvViewPageradapter = new mvViewPagerAdapter(listbean, getActivity(), HomeFragment2.this);
            headerBannerView.mvViewPager.setAdapter(mvViewPageradapter);
            headerBannerView.mvViewPager.setCurrentItem(10000 * listbean.size());
        } else {
            mvViewPageradapter.notifyDataSetChanged();
        }
        // 实现轮播效果
        abc.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 显示广告条
     */
    private void showBanner() {
        // 创建ViewPager对应的点
        headerBannerView.ll_dots.removeAllViews();
        mDots.clear();
        for (int i = 0; i < listbean.size(); i++) {
            View dot = new View(getActivity());
            int size = DimensionUtil.dip2px(getActivity(), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    size, size);
            params.leftMargin = size;
            if (i == 0) {
                dot.setBackgroundResource(R.mipmap.dot_focus);// 默认选择第1个点
            } else {
                dot.setBackgroundResource(R.mipmap.dot_normal);
            }
            dot.setLayoutParams(params);
            headerBannerView.ll_dots.addView(dot);
            mDots.add(dot);
        }

    }

    /***
     * 试图去隐藏轮播图 AD(广告)
     */
    private void tryToHintAd() {
        if (headerBannerView.mvViewPager != null) {
            headerBannerView.mvViewPager.setVisibility(View.GONE);
        }
    }

    /***
     * 试图去显示轮播图 AD(广告)
     */
    private void tryToShowAD() {
        if (headerBannerView.mvViewPager != null) {
            headerBannerView.mvViewPager.setVisibility(View.VISIBLE);
        }
    }

    private void getDataLunboImgView() {
        Map<String, String> map = new HashMap<>();
        map.put("hot", 1 + "");
        map.put("cnt", 3 + "");
        map.put("appid", "100");
        map.put("page", 1 + "");
        map.put("agent", "");
        map.put("category", "2");
        map.put("offset", "30");
        map.put("clientid", "49");
        map.put("classify", "1");
        map.put("from", "3");
        LogUtils.i("" + Utils.getCompUrlFromParams(Global.Hot_GAME_URL + "slide/list", map));
        RetrofitUtil.getInstance()
                .create(HttpServer.SlideShow.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LunboImgViewBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LunboImgViewBean homeRVBean) {
                        try {
                            LogUtils.i("msg：" + homeRVBean.getMsg());
                            LogUtils.i("code：" + homeRVBean.getCode());
                        } catch (Exception e) {
                            LogUtils.e(e.toString());
                        }
                        LogUtils.i("轮播图信息");
                        if (homeRVBean != null && homeRVBean.getCode() == 200) {
                            listbean = homeRVBean.getData().getList();
                            LogUtils.i("显示轮播图");
                            tryToShowAD();
                            showBanner();
                            startScrollView();
                        } else {
                            LogUtils.i("隐藏轮播图");
                            tryToHintAd();
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
}

