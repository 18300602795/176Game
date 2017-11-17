package com.i76game.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.adapter.DetailListAdapter;
import com.i76game.bean.GameContentBean;
import com.i76game.download.DownloadAPKManager;
import com.i76game.download.DownloadInfo;
import com.i76game.download.DownloadService;
import com.i76game.model.FilterData;
import com.i76game.model.TravelingEntity;
import com.i76game.utils.ApkUtils;
import com.i76game.utils.DensityUtil;
import com.i76game.utils.GetTypeUtils;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.ImgUtil;
import com.i76game.utils.ModelUtil;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.i76game.view.FilterView;
import com.i76game.view.HeaderBannerView;
import com.i76game.view.HeaderFilterView;
import com.i76game.view.SmoothListView;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 游戏详情页
 */
public class GameInfoActivity extends FragmentActivity implements SmoothListView.ISmoothListViewListener, View.OnClickListener {

    SmoothListView smoothListView;
    FilterView realFilterView;
    LinearLayout rlBar;
    TextView game_title;
    private DownloadAPKManager mDownloadAPKManager;
    private RelativeLayout mDownloadLayout;
    private GameContentBean.DataBean mData;
    private ProgressBar mDownloadProgress;
    private TextView mGameSize;
    private ImageView home_rv_icon;
    private LinearLayout ll_banner;
    private ImageView back_return;
    private ImageView share_iv;
    private ImageView download_iv;
    private boolean isTop = false;
    private boolean isBack = false;

    private Context mContext;
    private Activity mActivity;
    private int mScreenHeight; // 屏幕高度

    private List<String> bannerList = new ArrayList<>(); // 广告数据
    private List<TravelingEntity> travelingList = new ArrayList<>(); // ListView数据

    private HeaderBannerView headerBannerView; // 广告视图
    private HeaderFilterView headerFilterView; // 分类筛选视图
    private FilterData filterData; // 筛选数据
    private DetailListAdapter mAdapter;

    private int titleViewHeight = 65; // 标题栏的高度

    private View itemHeaderBannerView; // 从ListView获取的广告子View
    private int bannerViewHeight = 130; // 广告视图的高度
    private int bannerViewTopMargin; // 广告视图距离顶部的距离

    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private int filterViewPosition = 4; // 筛选视图的位置
    private int filterViewTopMargin; // 筛选视图距离顶部的距离
    private boolean isScrollIdle = true; // ListView是否在滑动
    private boolean isStickyTop = false; // 是否吸附在顶部
    private boolean isSmooth = false; // 没有吸附的前提下，是否在滑动
    private int filterPosition = -1; // 点击FilterView的位置：分类(0)、排序(1)、筛选(2)
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        initView();
        initData();
    }


    public void initView() {
        mContext = this;
        mActivity = this;
        smoothListView = (SmoothListView) findViewById(R.id.listView);
        realFilterView = (FilterView) findViewById(R.id.real_filterView);
        rlBar = (LinearLayout) findViewById(R.id.rl_bar);
        game_title = (TextView) findViewById(R.id.game_content_name);
        game_title.setVisibility(View.GONE);
        mScreenHeight = DensityUtil.getWindowHeight(this);
        share_iv = (ImageView) findViewById(R.id.share_iv);
        share_iv.setOnClickListener(this);
        download_iv = (ImageView) findViewById(R.id.download_iv);
        download_iv.setOnClickListener(this);
        // 筛选数据
        filterData = new FilterData();
        filterData.setCategory(ModelUtil.getCategoryData());
        filterData.setSorts(ModelUtil.getSortData());
        filterData.setFilters(ModelUtil.getFilterData());
        // 广告数据
        bannerList = ModelUtil.getBannerData();
        // 频道数据
//        channelList = ModelUtil.getChannelData();
        // 运营数据
//        operationList = ModelUtil.getOperationData();
        // ListView数据
        travelingList = ModelUtil.getTravelingData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rlBar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mDownloadLayout = (RelativeLayout) findViewById(R.id.game_content_download_layout);
        mDownloadLayout.setOnClickListener(this);
        mGameSize = (TextView) findViewById(R.id.game_content_size);
        mDownloadProgress = (ProgressBar) findViewById(R.id.game_content_download_progress);
        home_rv_icon = (ImageView) findViewById(R.id.home_rv_icon1);
        ll_banner = (LinearLayout) findViewById(R.id.ll_banner);
        back_return = (ImageView) findViewById(R.id.back_return);
    }

    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void initData() {
        // 设置广告数据
        headerBannerView = new HeaderBannerView(this);
        headerBannerView.fillView(bannerList, smoothListView);


        // 设置假FilterView数据
        headerFilterView = new HeaderFilterView(this);
        headerFilterView.fillView(new Object(), smoothListView);

        // 设置真FilterView数据
//        realFilterView.setFilterData(mActivity, filterData);
        realFilterView.setVisibility(View.GONE);
        realFilterView.detail_ll.setOnClickListener(this);
        realFilterView.strategy_ll.setOnClickListener(this);
        realFilterView.activity_ll.setOnClickListener(this);
        realFilterView.gift_ll.setOnClickListener(this);

        realFilterView.detail_tv.setTextColor(getResources().getColor(R.color.blue));

        headerFilterView.fakeFilterView.detail_ll.setOnClickListener(this);
        headerFilterView.fakeFilterView.strategy_ll.setOnClickListener(this);
        headerFilterView.fakeFilterView.activity_ll.setOnClickListener(this);
        headerFilterView.fakeFilterView.gift_ll.setOnClickListener(this);

        headerFilterView.fakeFilterView.detail_tv.setTextColor(getResources().getColor(R.color.blue));
        filterViewPosition = smoothListView.getHeaderViewsCount() - 1;
        initListener();

        mDownloadAPKManager = DownloadService.getDownloadManager(MyApplication.getContextObject());
        Intent intent = getIntent();
        int gameId = intent.getIntExtra(Global.GAME_ID, 0);
        final ArrayMap<String, String> map = new ArrayMap<>();
        map.put("gameid", gameId + "");
        map.put("clientid", "49");
        map.put("appid", "100");
        map.put("agent", "");
        map.put("from", "3");
        createAnim(map);
        if (Global.drawable != null) {
            home_rv_icon.setImageDrawable(Global.drawable);
            headerBannerView.home_rv_icon.setImageDrawable(Global.drawable);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_banner.setVisibility(View.GONE);
            }
        }, 100);
//        mAdapter = new DetailListAdapter(null, this, 1);
//        smoothListView.setAdapter(mAdapter);
        smoothListView.setLoadMoreEnable(false);
        back_return.setOnClickListener(this);
    }

    private void initListener() {

        // (假的ListView头部展示的)筛选视图点击
        headerFilterView.getFilterView().setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                isSmooth = true;
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, titleViewHeight));
            }
        });

        // (真正的)筛选视图点击
        realFilterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                realFilterView.show(position);
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, titleViewHeight));
            }
        });
        smoothListView.setSmoothListViewListener(this);
        smoothListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
//                        com.i76game.utils.LogUtils.i("抬起");
//                        handleTitleBarColorEvaluate(true);
                        break;
                }
                return false;
            }
        });
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
                    itemHeaderBannerView = smoothListView.getChildAt(1);
                }
                if (itemHeaderBannerView != null) {
                    bannerViewTopMargin = DensityUtil.px2dip(mContext, itemHeaderBannerView.getTop());
                    bannerViewHeight = DensityUtil.px2dip(mContext, itemHeaderBannerView.getHeight());
                }

                // 获取筛选View、距离顶部的高度
                if (itemHeaderFilterView == null) {
                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderFilterView != null) {
                    filterViewTopMargin = DensityUtil.px2dip(mContext, itemHeaderFilterView.getTop());
                }

                if (filterViewTopMargin <= titleViewHeight || firstVisibleItem > filterViewPosition) {
                    isStickyTop = true; // 吸附在顶部
                    realFilterView.setVisibility(View.VISIBLE);
                } else {
                    isStickyTop = false; // 没有吸附在顶部
                    realFilterView.setVisibility(View.GONE);
                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    realFilterView.show(filterPosition);
                }

                if (isTop) {
                    isStickyTop = true;
                }

                if (isBack) {
                    bannerViewTopMargin = 0;
                }

                // 处理标题栏颜色渐变
                handleTitleBarColorEvaluate(false);
            }
        });
    }

    private void showInfo() {
        headerBannerView.game_content_download_count.setVisibility(View.VISIBLE);
        headerBannerView.game_content_language.setVisibility(View.VISIBLE);
        headerBannerView.game_content_type.setVisibility(View.VISIBLE);
        headerBannerView.game_content_versions.setVisibility(View.VISIBLE);
        headerBannerView.home_rv_icon.setVisibility(View.VISIBLE);
    }

    private void hideInfo() {
        headerBannerView.game_content_download_count.setVisibility(View.INVISIBLE);
        headerBannerView.game_content_language.setVisibility(View.INVISIBLE);
        headerBannerView.game_content_type.setVisibility(View.INVISIBLE);
        headerBannerView.game_content_versions.setVisibility(View.INVISIBLE);
        headerBannerView.home_rv_icon.setVisibility(View.INVISIBLE);
    }

    private void close() {
        if (mAdapter != null) {
            if (mAdapter.strategyFragment != null) {
                if (mAdapter.strategyFragment.linearLayoutManager != null) {
                    mAdapter.strategyFragment.linearLayoutManager.setScrollEnabled(false);
                    mAdapter.strategyFragment.recyclerView.setLayoutManager(mAdapter.strategyFragment.linearLayoutManager);
                }
            }
            if (mAdapter.giftFragment != null) {
                if (mAdapter.giftFragment.linearLayoutManager != null) {
                    mAdapter.giftFragment.linearLayoutManager.setScrollEnabled(false);
                    mAdapter.giftFragment.mRecyclerView.setLayoutManager(mAdapter.giftFragment.linearLayoutManager);
                }
            }
        }
    }

    private void open() {
        if (mAdapter != null) {
            if (mAdapter.strategyFragment != null) {
                if (mAdapter.strategyFragment.linearLayoutManager != null) {
                    mAdapter.strategyFragment.linearLayoutManager.setScrollEnabled(true);
                    mAdapter.strategyFragment.recyclerView.setLayoutManager(mAdapter.strategyFragment.linearLayoutManager);
                }
            }
            if (mAdapter.giftFragment != null) {
                if (mAdapter.giftFragment.linearLayoutManager != null) {
                    mAdapter.giftFragment.linearLayoutManager.setScrollEnabled(true);
                    mAdapter.giftFragment.mRecyclerView.setLayoutManager(mAdapter.giftFragment.linearLayoutManager);
                }
            }

        }
    }


    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate(boolean isUp) {
        isTop = false;
        isBack = false;
        float fraction;
        hideInfo();
        game_title.setVisibility(View.VISIBLE);
//        if (isUp) {
//            if (bannerViewTopMargin != 0) {
//                if (bannerViewTopMargin <= -titleViewHeight / 2 && bannerViewTopMargin > -titleViewHeight) {
//                    isTop = true;
//                    com.i76game.utils.LogUtils.i("显示标题栏");
//                    smoothListView.setSelection(1);
//                    smoothListView.scrollTo(0, Utils.dip2px(this, titleViewHeight));
////                    ObjectAnimator.ofFloat(headerBannerView.infomation_ll, "translationY", Utils.dip2px(this, (bannerViewHeight - bannerViewTopMargin) * 1 / 2)).setDuration(300).start();
////                    ObjectAnimator.ofFloat(headerBannerView.infomation_ll, "translationX", Utils.dip2px(this, -(bannerViewHeight - bannerViewTopMargin) * 1 / 2)).setDuration(300).start();
//                    isStickyTop = true;
//                } else if (bannerViewTopMargin > -titleViewHeight / 2) {
//                    isStickyTop = false;
//                    isBack = true;
//                    com.i76game.utils.LogUtils.i("返回");
//                    smoothListView.scrollTo(0, Utils.dip2px(this,  0));
//                    smoothListView.setSelection(1);
//                    bannerViewTopMargin = 0;
//                }
//            }
//        }else {
        ObjectAnimator.ofFloat(headerBannerView.infomation_ll, "translationY", Utils.dip2px(this, -bannerViewTopMargin * 1 / 2)).setDuration(0).start();
        ObjectAnimator.ofFloat(headerBannerView.infomation_ll, "translationX", Utils.dip2px(this, bannerViewTopMargin * 1 / 2)).setDuration(0).start();
//        }
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
//        rlBar.setAlpha(1f);
        if (fraction >= 1f || isStickyTop) {
//            com.i76game.utils.LogUtils.i("标题栏");
            isStickyTop = true;
            rlBar.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
//            rlBar.setBackgroundResource(R.mipmap.bg_game_detail);
            game_title.setVisibility(View.VISIBLE);
            open();
        } else {
            if (bannerViewTopMargin == 0) {
//                com.i76game.utils.LogUtils.i("返回原位");
                showInfo();
                headerBannerView.ll_banner.setBackgroundResource(R.mipmap.bg_game_detail);
                close();
            } else {
                headerBannerView.ll_banner.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            }
            game_title.setVisibility(View.GONE);
            rlBar.setBackgroundColor(mContext.getResources().getColor(R.color.transparent2));
//            rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(mContext, fraction, R.color.transparent, R.color.colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_banner.setVisibility(View.GONE);
            }
        }, 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (realFilterView.isShowing()) {
            realFilterView.resetAllStatus();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    //开场动画
    private void createAnim(final ArrayMap<String, String> map) {
        mDownloadLayout.setScaleX(0);
        httpRequest(map);
        mDownloadLayout.animate().setDuration(800).scaleX(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.game_content_download_layout:
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    writePermission();
                } else {
                    requestPermission(Global.WRITE_READ_EXTERNAL_CODE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.back_return:
                if (Build.VERSION.SDK_INT > 20) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.share_iv:
                Utils.showShare(this, "一起来玩" + mData.getGamename(), mData.getIcon(), "", String.valueOf(mData.getGameid()));
                break;
            case R.id.download_iv:
                startActivity(new Intent(this, DownloadActivity.class));
                break;
            case R.id.detail_ll:
                clear();
                realFilterView.detail_tv.setTextColor(getResources().getColor(R.color.blue));
                headerFilterView.fakeFilterView.detail_tv.setTextColor(getResources().getColor(R.color.blue));
                mAdapter = new DetailListAdapter(mData, this, 1);
                smoothListView.setAdapter(mAdapter);
                break;
            case R.id.strategy_ll:
                clear();
                realFilterView.strategy_tv.setTextColor(getResources().getColor(R.color.blue));
                headerFilterView.fakeFilterView.strategy_tv.setTextColor(getResources().getColor(R.color.blue));
                mAdapter = new DetailListAdapter(mData, this, 2);
                smoothListView.setAdapter(mAdapter);
                break;
            case R.id.activity_ll:
                clear();
                realFilterView.activity_tv.setTextColor(getResources().getColor(R.color.blue));
                headerFilterView.fakeFilterView.activity_tv.setTextColor(getResources().getColor(R.color.blue));
                mAdapter = new DetailListAdapter(mData, this, 3);
                smoothListView.setAdapter(mAdapter);
                break;
            case R.id.gift_ll:
                clear();
                realFilterView.gift_tv.setTextColor(getResources().getColor(R.color.blue));
                headerFilterView.fakeFilterView.gift_tv.setTextColor(getResources().getColor(R.color.blue));
                mAdapter = new DetailListAdapter(mData, this, 4);
                smoothListView.setAdapter(mAdapter);
                break;
        }
    }

    /**
     * 权限检查
     */
    public boolean hasPermission(String...permissions){
        if (Build.VERSION.SDK_INT<23){
            return true;
        }

        for (String permission:permissions){
            if (ContextCompat.checkSelfPermission(this,permission)!=
                    PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     */
    public void requestPermission(int code , String... permissions){
        ActivityCompat.requestPermissions(this,permissions,code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Global.WRITE_READ_EXTERNAL_CODE:
                writePermission();
                break;
        }
    }

    private void clear() {
        realFilterView.clear();
        headerFilterView.fakeFilterView.clear();
    }

    private void httpRequest(ArrayMap<String, String> map) {
        RetrofitUtil.getInstance()
                .create(HttpServer.GameContentServer.class)
                .listResponse(map)
                .subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GameContentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull GameContentBean gameContentBean) {
                        if (gameContentBean != null) {
                            mData = gameContentBean.getData();
                            try {
                                refreshUI();
                                refreshUi();
                            } catch (Exception e) {
                                LogUtils.e(e.toString());
                            }
                        } else {
                            Toast.makeText(GameInfoActivity.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
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


    private void refreshUI() {
        headerBannerView.game_content_name.setText(mData.getGamename());
        game_title.setText(mData.getGamename());

        String type = GetTypeUtils.getContentType(mData.getType());
        headerBannerView.game_content_type.setText("类型：" + type);
        headerBannerView.game_content_download_count.setText("下载：" + mData.getDowncnt() + "");
        headerBannerView.game_content_versions.setText("版本：" + mData.getVername());
        mGameSize.setText("下载(" + mData.getSize() + ")");
        // 设置ListView数据
        mAdapter = new DetailListAdapter(mData, this, 1);
        smoothListView.setAdapter(mAdapter);
        if (mData.getIcon() != null && mData.getIcon() != "") {
            if (Global.drawable == null) {
                ImgUtil.getBitmapUtils(GameInfoActivity.this).display(headerBannerView.home_rv_icon, mData.getIcon() + "");
                ImgUtil.loadImage(mData.getIcon() + "", headerBannerView.home_rv_icon);
                LogUtils.i("图片url：" + mData.getIcon());
            }
        }
//        if (Global.drawable != null) {
//            headerBannerView.home_rv_icon.setImageDrawable(Global.drawable);
//        }
    }

    private void writePermission() {
        try {
            if (mData != null) {
                String target = MyApplication.apkdownload_path + headerBannerView.game_content_name.getText() + ".apk";
                DownloadInfo downloadInfo = mDownloadAPKManager.getDownloadInfoByAppId(mData.getGameid() + "");

                if (downloadInfo == null) {
                    // 如果原来没有下载过，则添加一个新的下载
                    if (mData.getGameid() != null) {
                        try {
                            mDownloadAPKManager.addNewDownload(
                                    mData.getGameid() + "",
                                    mData.getDownlink(),
                                    mData.getGamename(),
                                    target,
                                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                                    mData.getIcon(),  //传入的是图片地址，因为服务器暂时没有，这里先写死、
                                    null,
                                    downloadStateListener);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                    return;

                }
                HttpHandler.State state = downloadInfo.getState();

                switch (state) {
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        try {
                            mDownloadAPKManager.stopDownload(downloadInfo);
                        } catch (DbException e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                        break;
                    case CANCELLED:
                    case FAILURE:
                        try {
                            mDownloadAPKManager.resumeDownload(downloadInfo, downloadStateListener);
                        } catch (DbException e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                        break;
                    case SUCCESS:
                        if (downloadInfo != null) {
                            if (!ApkUtils.install(downloadInfo)) {
                                deleteInfo(downloadInfo);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "下载地址错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteInfo(DownloadInfo downloadInfo) {
        try {
            ApkUtils.deleteDownloadApk(MyApplication.getContextObject(), downloadInfo.getFileName());//delete file apk from sdcard!
            mDownloadAPKManager.removeDownload(downloadInfo);
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }


    private boolean isFirst = true;

    private void refreshUi() {
        if (mData == null) {
            return;
        }

        DownloadInfo downloadInfo = mDownloadAPKManager.getDownloadInfoByAppId(mData.getGameid() + "");
        if (downloadInfo == null) {
            //说明没有下载过
            mGameSize.setText("下载(" + mData.getSize() + ")");
            return;
        }
        HttpHandler.State state = downloadInfo.getState();


        switch (state) {
            case WAITING:
                mGameSize.setText("等待");
                break;
            case STARTED:
            case LOADING:
            case CANCELLED:
                // 如果是开始状态或者正在下载的状态，或者暂停的状态，都需要显示当前的进度
                if (downloadInfo.getFileLength() > 0) {
                    int pesent = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
                    mDownloadProgress.setProgress(pesent);
                    mGameSize.setText(" " + pesent + "%");
                } else {
                    mDownloadProgress.setProgress(0);
                }
                if (state == HttpHandler.State.CANCELLED) {
                    mGameSize.setText("暂停");
                }
                if (isFirst) {
                    if (state == HttpHandler.State.LOADING && downloadInfo.getHandler() != null) {
                        RequestCallBack<File> requestCallBack = downloadInfo.getHandler().getRequestCallBack();
                        if (requestCallBack != null && requestCallBack instanceof DownloadAPKManager.ManagerCallBack) {
                            DownloadAPKManager.ManagerCallBack managerCallBack = (DownloadAPKManager.ManagerCallBack) requestCallBack;
                            managerCallBack.setBaseCallBack(downloadStateListener);
                        }
                    }
                    isFirst = false;
                }

                break;
            case SUCCESS:
                mDownloadProgress.setProgress(100);
                mGameSize.setText("安装");
                break;
            case FAILURE:
                mGameSize.setText("重试");
                break;
            default:
                break;
        }
    }

    private DownloadStateListener downloadStateListener = new DownloadStateListener();

    public class DownloadStateListener extends RequestCallBack<File> {
        /**
         * 开始下载
         */
        @Override
        public void onStart() {
            refreshUi();
        }

        /**
         * 正在下载
         */
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            refreshUi();
        }

        /**
         * 下载成功
         */
        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            refreshUi();
        }

        /**
         * 下载失败
         */
        @Override
        public void onFailure(HttpException e, String s) {
            refreshUi();
        }

        /**
         * 下载取消
         */
        @Override
        public void onCancelled() {
            refreshUi();
        }
    }

}


