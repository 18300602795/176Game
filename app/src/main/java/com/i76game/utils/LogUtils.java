package com.i76game.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/10/26.
 */

public class LogUtils {
    private static boolean isLog = true;
    private static String TAG = "333";
    private static String URL_TAG = "222";
    public static void e(String msg) {
        if (isLog)
            Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (isLog)
            Log.i(TAG, msg);
    }

    public static void eUrl(String msg) {
        if (isLog)
            Log.e(URL_TAG, msg);
    }

    public static void iUrl(String msg) {
        if (isLog)
            Log.i(URL_TAG, msg);
    }

    public static void d(String msg) {
        if (isLog)
            Log.d(TAG, msg);
    }

    public static void w(String msg) {
        if (isLog)
            Log.w(TAG, msg);
    }

    public static void v(String msg) {
        if (isLog)
            Log.v(TAG, msg);
    }


}
