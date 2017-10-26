package com.i76game.download;




import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import java.io.File;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:11
 */
public class DownloadInfo {

    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public DownloadInfo() {
    }

    private long id;
    
    private String appId;
    private String packageName;
    private int isInstallSuccess=0;
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Transient
    private HttpHandler<File> handler;

    private HttpHandler.State state;

    private String downloadUrl;

    private String fileName;

    private String fileSavePath;

    private long progress;
    private long pre_progress=0;//上一次下载的位置

    private long fileLength;

    private boolean autoResume;

    private boolean autoRename;

    public int getIsInstallSuccess() {
        return isInstallSuccess;
    }

    public void setIsInstallSuccess(int isInstallSuccess) {
        this.isInstallSuccess = isInstallSuccess;
    }

    /*************自定义添加参数**************/
    private String imageurl;//游戏图标的下载地址

    private String baoming;//游戏包名
    private String title;//游戏标题
    public boolean isDownloadComplete=false;//是否下载完成 true下载完成 false没有完成

    public boolean isDownloadComplete() {
        return isDownloadComplete;
    }

    public void setDownloadComplete(boolean downloadComplete) {
        isDownloadComplete = downloadComplete;
    }

    public long getPre_progress() {
        return pre_progress;
    }

    public void setPre_progress(long pre_progress) {
        this.pre_progress = pre_progress;
    }
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    public String getBaoming() {
        return baoming;
    }

    public void setBaoming(String baoming) {
        this.baoming = baoming;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HttpHandler<File> getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler<File> handler) {
        this.handler = handler;
    }

    public HttpHandler.State getState() {
        return state;
    }

    public void setState(HttpHandler.State state) {
        this.state = state;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadInfo)) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
