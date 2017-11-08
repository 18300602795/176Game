package com.i76game.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.GameCountAdapter;
import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.bean.GameContentBean;
import com.i76game.download.DownloadAPKManager;
import com.i76game.download.DownloadInfo;
import com.i76game.download.DownloadService;
import com.i76game.utils.ApkUtils;
import com.i76game.utils.GetTypeUtils;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.RetrofitUtil;
import com.i76game.utils.Utils;
import com.i76game.view.TextViewExpandableAnimation;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 游戏详情页
 */
public class GameContentActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mGameIcon;
    private TextView mGameName;
    private TextView mGameType;
    private TextView mGameDownCnt;
    private TextView mGameVerName;
    private TextView mGameSize;
    private TextViewExpandableAnimation mExpandableAnimation;
    private RecyclerView mRecyclerView;
    private LinearLayout linearLayout;

    private ProgressBar mDownloadProgress;
    private GameContentBean.DataBean mData;
    private GameCountAdapter mAdapter;

    private ScrollView mScrollView;
    private RelativeLayout mDownloadLayout;

    @Override
    public void initData() {
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
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_game_content;
    }

    @Override
    public void initView() {
        setToolbar("游戏详情", R.id.game_content_toolbar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT > 20) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.game_content_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new GameCountAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mGameIcon = (ImageView) findViewById(R.id.home_rv_icon);
        if (Global.drawable != null) {
            mGameIcon.setImageDrawable(Global.drawable);
        }
        mGameName = (TextView) findViewById(R.id.game_content_name);
        mGameType = (TextView) findViewById(R.id.game_content_type);
        mGameDownCnt = (TextView) findViewById(R.id.game_content_download_count);
        mGameVerName = (TextView) findViewById(R.id.game_content_versions);
        mGameSize = (TextView) findViewById(R.id.game_content_size);

        mExpandableAnimation = (TextViewExpandableAnimation)
                findViewById(R.id.expand_text_view);
        mDownloadLayout = (RelativeLayout) findViewById(R.id.game_content_download_layout);
        mDownloadLayout.setOnClickListener(this);
        mDownloadProgress = (ProgressBar) findViewById(R.id.game_content_download_progress);
        mScrollView = (ScrollView) findViewById(R.id.game_content_scroll_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            linearLayout.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
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
                            refreshUI();
                            refreshUi();
                        } else {
                            Toast.makeText(GameContentActivity.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
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
        mGameName.setText(mData.getGamename());

        String type = GetTypeUtils.getContentType(mData.getType());
        mGameType.setText("类型：" + type);
        mGameDownCnt.setText("下载：" + mData.getDowncnt() + "");
        mGameVerName.setText("版本：" + mData.getVername());
        mGameSize.setText("下载(" + mData.getSize() + ")");

        mExpandableAnimation.setText(mData.getDisc());
        final ArrayList<String> imageList = (ArrayList<String>) mData.getImage();
        mAdapter.setImageUrls(imageList);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new GameCountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(GameContentActivity.this, ImagePagerActivity.class);
                intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageList);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }
        });
    }


    private DownloadAPKManager mDownloadAPKManager;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_content_download_layout:
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    writePermission();
                } else {
                    requestPermission(Global.WRITE_READ_EXTERNAL_CODE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
        }
    }

    @Override
    public void writePermission() {
        try {
            if (mData != null) {
                String target = MyApplication.apkdownload_path + mGameName.getText() + ".apk";
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
                        ApkUtils.install(downloadInfo);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "下载地址错误", Toast.LENGTH_SHORT).show();
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

    //开场动画
    private void createAnim(final ArrayMap<String, String> map) {
        mScrollView.setAlpha(0);
        mScrollView.setTranslationY(100);
        mDownloadLayout.setScaleX(0);

        httpRequest(map);
        mScrollView.animate()
                .setDuration(800)
                .translationY(0)
                .alpha(1);
        mDownloadLayout.animate().setDuration(800).scaleX(1);
    }

    @Override
    protected void onDestroy() {
        Global.drawable = null;
        if (Build.VERSION.SDK_INT > 20) {
            finishAfterTransition();
        }
        super.onDestroy();
    }
}
