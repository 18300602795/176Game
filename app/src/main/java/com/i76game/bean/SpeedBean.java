package com.i76game.bean;

/**
 * Created by Administrator on 2017/5/25.
 */

public class SpeedBean {
    private long currentSize;
    private long currentTime;

    public SpeedBean(long currentSize, long currentTime) {
        this.currentSize = currentSize;
        this.currentTime = currentTime;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
