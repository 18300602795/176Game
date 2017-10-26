package com.i76game.download;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.utils.ApkUtils;
import com.i76game.utils.SharePrefUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:10
 */
public class DownloadAPKManager {

    private List<DownloadInfo> downloadInfoList;

    private int maxDownloadThread = 10;

    private Context mContext;
    private DbUtils db;

    /*package*/
    DownloadAPKManager(Context appContext) {
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class,
                new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
        try {
            downloadInfoList = db.findAll(Selector.from(DownloadInfo.class));
        } catch (DbException e) {

        }
        if (downloadInfoList == null) {
            downloadInfoList = new ArrayList<>();
        }
    }

    public int getDownloadInfoListCount() {
        return downloadInfoList.size();
    }

    public DownloadInfo getDownloadInfo(int index) {
        return downloadInfoList.get(index);
    }

    /**
     * 根据url地址获得downloadinfo
     * @param url
     * @return
     */
    public DownloadInfo getDownloadInfo(String url){
        for (DownloadInfo downinfo :downloadInfoList) {
            if(downinfo.getDownloadUrl().equals(url)){
                return downinfo;
            }
        }
        return null;
    }


    /**
     * 根据filename名字获得downloadinfo
     */
    public DownloadInfo getDownloadfilename(String filename){
        for (DownloadInfo downinfo :downloadInfoList) {
            if(downinfo.getDownloadUrl().equals(filename)){
                return downinfo;
            }
        }
        return null;
    }

    /**
     * 通过应用id获取对应的下载信息
     * @param appId 应用id
     * @return
     */
    public DownloadInfo getDownloadInfoByAppId(String appId) {
    	for (DownloadInfo downloadInfo : downloadInfoList) {
			if (appId.equals(downloadInfo.getAppId())) {
				return downloadInfo;
			}
		}
    	return null;
    }


    /**
     * 根据游戏包名返回一个downloadInfo
     * @param packagename
     * @return
     */
    public DownloadInfo getDownloadInfoByPackagename(String packagename){
        for (DownloadInfo downinfo : downloadInfoList) {
            if(downinfo.getBaoming().equals(packagename)){
                return downinfo;
            }
        }
        return null;
    }
    public DownloadInfo getDownloadInfoByPname(String packageName){
        for (DownloadInfo downinfo : downloadInfoList) {
            if(downinfo.getPackageName().equals(packageName)){
                return downinfo;
            }
        }
        return null;
    }




    private Toast toast = null;
    /** 不会一直重复重复重复重复的提醒了 */
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    public void addNewDownload(String appId, String url, String fileName, String target,
                               boolean autoResume, boolean autoRename,String imageurl, String baoming,
                               final RequestCallBack<File> callback) throws DbException {

        final DownloadInfo downloadInfo = new DownloadInfo();
        /**
         * 自己添加的
         */
        downloadInfo.setImageurl(imageurl);//头像下载地址
        downloadInfo.setBaoming(baoming);//游戏包名
        downloadInfo.setTitle(fileName);

        downloadInfo.setAppId(appId);


        String agent=SharePrefUtil.getString(mContext, SharePrefUtil.KEY.AGENT,"");
        if (!agent.equals("")){
            //先去掉最后一个斜杠后面的字符
            int lastOne=url.lastIndexOf("/");
            url=url.substring(0, lastOne);
            //去掉倒数第二斜杠后面的字符
            int lastTwo=url.lastIndexOf("/");
            String gameID=url.substring(lastTwo+1);
            url=url.substring(0, lastTwo);
            //拿到最后下划线后面的渠道id
            int i=agent.lastIndexOf("_");
            String agentID=agent.substring(i+1);
            //拼接成新的
            url=url+"/"+gameID+"/"+gameID+"_"+agentID+".apk";
        }else{
//            url=url+"abc";
        }

        downloadInfo.setDownloadUrl(url);
        Log.e("-----------", "addNewDownload: "+url);

        downloadInfo.setAutoRename(autoRename);
        downloadInfo.setAutoResume(autoResume);
        downloadInfo.setFileName(fileName);
        downloadInfo.setFileSavePath(target);
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(url, target, autoResume, autoRename, new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        downloadInfoList.add(downloadInfo);
        db.saveBindingId(downloadInfo);
    }

    public void resumeDownload(int index, final RequestCallBack<File> callback) throws DbException {
        final DownloadInfo downloadInfo = downloadInfoList.get(index);
        resumeDownload(downloadInfo, callback);
    }

    public void resumeDownload(DownloadInfo downloadInfo, final RequestCallBack<File> callback) throws DbException {
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(
                downloadInfo.getDownloadUrl(),
                downloadInfo.getFileSavePath(),
                downloadInfo.isAutoResume(),
                downloadInfo.isAutoRename(),
                new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        db.saveOrUpdate(downloadInfo);
    }

    public void removeDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        removeDownload(downloadInfo);
    }

    public void removeDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        }
        downloadInfoList.remove(downloadInfo);
        db.delete(downloadInfo);
        
        // 把apk文件删除
        String apkFilePath = downloadInfo.getFileSavePath();
        File apkFile = new File(apkFilePath);
        if (apkFile.exists()) {
        	apkFile.delete();
		}
    }

    public void stopDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    public void stopDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        } else {
            downloadInfo.setState(HttpHandler.State.CANCELLED);
        }
        db.saveOrUpdate(downloadInfo);
    }

    public void stopAllDownload() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null && !handler.isCancelled()) {
                handler.cancel();
            } else {
                downloadInfo.setState(HttpHandler.State.CANCELLED);
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public void backupDownloadInfoList() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    public void setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
    }

    public class ManagerCallBack extends RequestCallBack<File> {
        private DownloadInfo downloadInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack() {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
        }

        private ManagerCallBack(DownloadInfo downloadInfo, RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public Object getUserTag() {
            if (baseCallBack == null) return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null) return;
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public void onStart() {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {

            }
            if (baseCallBack != null) {
                baseCallBack.onStart();
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled() {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {

            }
            if (baseCallBack != null) {
                baseCallBack.onCancelled();
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            downloadInfo.setFileLength(total);
            downloadInfo.setProgress(current);
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {

            }
            if (baseCallBack != null) {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                String packageName= ApkUtils.getPackageNameByApkFile(MyApplication.getContextObject(),downloadInfo.getFileSavePath());
                downloadInfo.setPackageName(packageName);
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {

            }
            if (baseCallBack != null) {
                baseCallBack.onSuccess(responseInfo);
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {

            }
            if (baseCallBack != null) {
                baseCallBack.onFailure(error, msg);
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    public void setInstallSuccess(DownloadInfo downloadInfo){
        downloadInfo.setIsInstallSuccess(1);
        try {
            db.saveOrUpdate(downloadInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.e("hongliang","更新安装完成"+downloadInfo.getFileName());
    }

    private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue) {
            if (fieldStringValue == null) return null;
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType() {
            return ColumnDbType.INTEGER;
        }
    }
}
