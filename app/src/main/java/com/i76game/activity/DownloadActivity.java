package com.i76game.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.bean.SpeedBean;
import com.i76game.download.DownStateChange;
import com.i76game.download.DownloadAPKManager;
import com.i76game.download.DownloadInfo;
import com.i76game.download.DownloadService;
import com.i76game.utils.ApkUtils;
import com.i76game.utils.GlideUtil;
import com.i76game.utils.SharePrefUtil;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载界面
 */
public class DownloadActivity extends BaseActivity {
    private ListView mListView;
    private DownloadAPKManager mDownloadAPKManager;
    private MyDownloadListAdapter mDownloadListAdapter;
    private boolean mIsDelete=false;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_download;
    }

    @Override
    public void initView() {
        mListView= (ListView) findViewById(R.id.download_rv);
        setToolbar("我的下载",R.id.download_toolbar);

        final IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        registerReceiver(receiver_download, filter);
        mDownloadAPKManager = DownloadService.getDownloadManager(MyApplication.getContextObject());
        getUnInstallApk();
        mDownloadListAdapter = new MyDownloadListAdapter();
        mListView.setAdapter(mDownloadListAdapter);
        TextView manager= (TextView) findViewById(R.id.download_manager);
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsDelete){
                    mIsDelete=true;
                }else{
                    mIsDelete=false;
                }
                mDownloadListAdapter.notifyDataSetChanged();
            }
        });

    }
    private List<DownloadInfo> unInstallList;


    public void getUnInstallApk() {
        unInstallList = new ArrayList<>();
        if (mDownloadAPKManager == null) return;
        for (int i = 0; i < mDownloadAPKManager.getDownloadInfoListCount(); i++) {
            DownloadInfo downloadInfo = mDownloadAPKManager.getDownloadInfo(i);
            if (downloadInfo != null) {
                String packageNameByApkFile = ApkUtils.getPackageNameByApkFile(
                        MyApplication.getContextObject(), downloadInfo.getFileSavePath());
                    downloadInfo.setPackageName(packageNameByApkFile);
                    unInstallList.add(downloadInfo);
            }
        }
    }

        /**
         * 注册广播接收者
         *
         * @return
         */
    private BroadcastReceiver receiver_download = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getStringExtra("androidurl");

            if (unInstallList != null) {
                DownloadInfo downloadinfo = getDownloadInfoByPackagename(packageName);
                if (null != downloadinfo) {
                    try {
                        mDownloadListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    /**
     * 根据游戏包名返回一个downloadInfo
     * @param packagename
     * @return
     */
    public DownloadInfo getDownloadInfoByPackagename(String packagename){
        for (DownloadInfo downinfo : unInstallList) {
            if(downinfo.getBaoming().equals(packagename)){
                return downinfo;
            }
        }
        return null;
    }

    private class DownloadRequestCallBack extends RequestCallBack<File> {

        @SuppressWarnings("unchecked")
        private void refreshListItem() {
            if (userTag == null) return;
            WeakReference<DownloadRVHolder> tag = (WeakReference<DownloadRVHolder>) userTag;
            DownloadRVHolder holder = tag.get();
            if (holder != null) {
                holder.refreshs();
            }
        }


        @Override
        public void onStart() {
            refreshListItem();
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            refreshListItem();
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            refreshListItem();
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            refreshListItem();
        }

        @Override
        public void onCancelled() {
            refreshListItem();
        }
    }

    /**
     * 下载列队适配器
     */
    private class MyDownloadListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyDownloadListAdapter() {
            inflater = LayoutInflater.from(DownloadActivity.this);
        }

        @Override
        public int getCount() {
            return unInstallList.size();
        }

        @Override
        public Object getItem(int position) {
            return unInstallList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DownloadRVHolder vh;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.download_rv_layout, parent,false);
                vh = new DownloadRVHolder(unInstallList.get(position), convertView);
                convertView.setTag(vh);
            } else {
                vh = (DownloadRVHolder) convertView.getTag();
                vh.update(unInstallList.get(position));
            }

            GlideUtil.loadImage(unInstallList.get(position).getImageurl(),vh.mImageIcon,
                    R.mipmap.load_icon);
            HttpHandler<File> handler = unInstallList.get(position).getHandler();
            if (handler != null) {
                RequestCallBack callBack = handler.getRequestCallBack();
                if (callBack instanceof  DownloadAPKManager.ManagerCallBack) {
                    DownloadAPKManager.ManagerCallBack managerCallBack = (DownloadAPKManager.ManagerCallBack) callBack;
                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
                }
                callBack.setUserTag(new WeakReference<DownloadRVHolder>(vh));
            }

            if (mIsDelete){
                vh.mBtnDelete.setVisibility(View.VISIBLE);
                vh.mBtnState.setVisibility(View.GONE);
            }else{
                vh.mBtnDelete.setVisibility(View.GONE);
                vh.mBtnState.setVisibility(View.VISIBLE);
            }
            vh.refresh();
            boolean mark = convertView.getTag(R.string.view_tag) == null ? false : (Boolean) convertView.getTag(R.string.view_tag);
//            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DimensionUtil.dip2px(ctx, mark ? 132 : 80)));
            convertView.setTag(R.string.view_tag, mark);
            return convertView;
        }
    }


    class DownloadRVHolder implements View.OnClickListener{
        TextView mTextProgress;
        TextView mTextState;
        ProgressBar mProgressBar;
        Button mBtnState;
        ImageView mImageIcon;
        TextView mTextName;
        Button mBtnDelete;
        private DownloadInfo mDownloadInfo;
        public DownloadRVHolder(DownloadInfo downloadInfo,View itemView) {
            this.mDownloadInfo = downloadInfo;
            mBtnDelete= (Button) itemView.findViewById(R.id.download_rv_btn_delete);
            mTextName= (TextView) itemView.findViewById(R.id.download_rv_game_name);
            mBtnState= (Button) itemView.findViewById(R.id.download_rv_btn_state);
            mImageIcon= (ImageView) itemView.findViewById(R.id.download_rv_icon);
            mTextProgress= (TextView) itemView.findViewById(R.id.download_rv_text_progress);
            mTextState= (TextView) itemView.findViewById(R.id.download_rv_text_state);
            mProgressBar= (ProgressBar) itemView.findViewById(R.id.download_rv_progress);
        }
        /**
         * 刷新下载进度
         */
        public void refresh() {
            //Logger.msg(TAG, "在刷新下载进度吗？");
            HttpHandler.State state = mDownloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    mBtnState.setText("暂停");
                    String speed=ApkUtils.getFormatSize(mDownloadInfo.getProgress() - mDownloadInfo.getProgress()) + "/s";
                    if(speed.equals(".00/s")){
                        mTextState.setText("0/s");
                    }else{
                        mTextState.setText(speed);
                    }
                    break;
                case CANCELLED:
                    mBtnState.setText("继续");
                    mTextState.setText("暂停中");
                    break;
                case SUCCESS:
                    mDownloadInfo.isDownloadComplete = true;
                    mBtnState.setText("安装");//执行安装
                    mTextState.setText("已完成");
                    break;
                case FAILURE:
                    mBtnState.setText("重试");
                    mTextState.setText("0/s");
                    break;
                default:
                    break;
            }
            if (mDownloadInfo.isDownloadComplete&&mDownloadInfo.getIsInstallSuccess()==1){
                mBtnState.setText("打开");
            }

            mBtnDelete.setOnClickListener(this);
            mBtnState.setOnClickListener(this);
            mTextName.setText(mDownloadInfo.getFileName());
            long fileLength = mDownloadInfo.getFileLength();
            if(fileLength<=0){
                mTextProgress.setText("未知大小");//设置文件大小
            }else{
                //设置文件大小
                Log.e("=======", ""+ mDownloadInfo.getFileLength());
                mTextProgress.setText(ApkUtils.getFormatSize(mDownloadInfo.getFileLength()));
            }
            if (mDownloadInfo.getFileLength() > 0) {
                mProgressBar.setProgress((int) (mDownloadInfo.getProgress() * 100 / mDownloadInfo.getFileLength()));
                mDownloadInfo.setProgress(mDownloadInfo.getProgress());
            } else {
                mProgressBar.setProgress(0);
            }
        }


        public void refreshs() {
            HttpHandler.State state = mDownloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    mBtnState.setText("暂停");
                    break;
                case CANCELLED:
                    mBtnState.setText("继续");
                    mTextState.setText("暂停中");
                    break;
                case SUCCESS:
                    mDownloadInfo.isDownloadComplete = true;
                    mBtnState.setText("安装");//执行安装
                    mTextState.setText("已完成");
                    EventBus.getDefault().post(new DownStateChange(Integer.parseInt(mDownloadInfo.getAppId())));
                    ApkUtils.install(mDownloadInfo);
                    break;
                case FAILURE:
                    mBtnState.setText("重试");
                    mTextState.setText("0/s");
                    EventBus.getDefault().post(new DownStateChange(Integer.parseInt(mDownloadInfo.getAppId())));
                    break;
                default:
                    break;
            }
            if (mDownloadInfo.isDownloadComplete&&mDownloadInfo.getIsInstallSuccess()==1){
                mBtnState.setText("打开");
            }
            mTextName.setText(mDownloadInfo.getFileName());
            mBtnDelete.setOnClickListener(this);
            mBtnState.setOnClickListener(this);
            long fileLength = mDownloadInfo.getFileLength();
            if(fileLength<=0){
                mTextProgress.setText("未知大小");//设置文件大小
            }else{
                //设置文件大小
                mTextProgress.setText(ApkUtils.getFormatSize(mDownloadInfo.getFileLength()));
            }
            if(mTextState.getTag()!=null){
                SpeedBean tag = (SpeedBean) mTextState.getTag();
                long chaTime = (System.currentTimeMillis() - tag.getCurrentTime())/1000;
                if(chaTime>0){
                    mTextState.setText(ApkUtils.getFormatSize((mDownloadInfo.getProgress()
                            - tag.getCurrentSize())/chaTime) + "/s");
                }
            }
            mTextState.setTag(new SpeedBean(mDownloadInfo.getProgress(),System.currentTimeMillis()));
            if (mDownloadInfo.getFileLength() > 0) {
                mProgressBar.setProgress((int)
                        (mDownloadInfo.getProgress() * 100 / mDownloadInfo.getFileLength()));
                mDownloadInfo.setProgress(mDownloadInfo.getProgress());
            } else {
                mProgressBar.setProgress(0);
            }
        }

        public void update(DownloadInfo downloadinfo) {
            this.mDownloadInfo = downloadinfo;
            refresh();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.download_rv_btn_state:
                    HttpHandler.State state = mDownloadInfo.getState();
                    switch (state) {
                        case WAITING:
                        case STARTED:
                        case LOADING:
                            try {
                                mDownloadAPKManager.stopDownload(mDownloadInfo);
                                ((TextView) v).setText("继续");
                            } catch (DbException e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                            break;
                        case CANCELLED:
                        case FAILURE:
                            try {
                                mDownloadAPKManager.resumeDownload(mDownloadInfo, new DownloadRequestCallBack());
                                ((TextView) v).setText("暂停");
                            } catch (DbException e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                            mDownloadListAdapter.notifyDataSetChanged();
                            break;
                        case SUCCESS:
                            ApkUtils.install(mDownloadInfo);
                            break;
                        default:
                            break;
                    }
                    break;

                case R.id.download_rv_btn_delete:
                    SharePrefUtil.saveBoolean(MyApplication.getContextObject(),SharePrefUtil.KEY.ISDELETE,false);
                    try {
                        ApkUtils.deleteDownloadApk(MyApplication.getContextObject(), mDownloadInfo.getFileName());//delete file apk from sdcard!
                        mDownloadAPKManager.removeDownload(mDownloadInfo);
                        unInstallList.remove(mDownloadInfo);
                        mDownloadListAdapter.notifyDataSetChanged();
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }

                    break;
                default:
                    break;
            }
            EventBus.getDefault().post(new DownStateChange(Integer.parseInt(mDownloadInfo.getAppId())));
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver_download);
        super.onDestroy();

    }
}
