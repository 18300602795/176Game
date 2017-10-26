package com.i76game.bean;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ServerBean {
    private String mWeek;
    private String mGameName;
    private String mGameArea;
    private String mData;
    private int mGameId;
    private String mIconUrl;

    public int getGameId() {
        return mGameId;
    }

    public void setGameId(int gameId) {
        mGameId = gameId;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public String getWeek() {
        return mWeek;
    }

    public void setWeek(String week) {
        mWeek = week;
    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String gameName) {
        mGameName = gameName;
    }

    public String getGameArea() {
        return mGameArea;
    }

    public void setGameArea(String gameArea) {
        mGameArea = gameArea;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }
}
