package com.i76game.bean;

/**
 * Created by Administrator on 2017/12/7.
 */

public class SignInfoBean {
    private long create_time;
    private int sign_in_days;
    private long sign_in_time;
    private int integral;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getSign_in_days() {
        return sign_in_days;
    }

    public void setSign_in_days(int sign_in_days) {
        this.sign_in_days = sign_in_days;
    }

    public long getSign_in_time() {
        return sign_in_time;
    }

    public void setSign_in_time(long sign_in_time) {
        this.sign_in_time = sign_in_time;
    }
}
