package com.i76game.utils;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/10/20.
 */

public class Utils {

    /**
     * get请求方法拼接
     */
    public static String getCompUrlFromParams(String url, Map<String, String> params) {
        String url1 = "";
//        if (params != null) {
//            params.put(Constants.AGENT, Constants.agent);
//            params.put(Constants.FROM, Constants.from);
//            params.put(Constants.APP_ID, Constants.appid);
//            params.put(Constants.CLIENT_ID, Constants.clientid);
//        }
        if (null != url && !"".equals(url) && null != params
                && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for (Map.Entry<String, String> entity : params.entrySet()) {
                sb.append(entity.getKey()).append("=")
                        .append(entity.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url1 = url + sb.toString();
            return url1;
        }
        return url;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
