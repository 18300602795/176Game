package com.i76game.utils;

import java.util.Map;

/**
 * Created by Administrator on 2017/10/26.
 */

public class StringUtils {

    public static String lgoin_state = "玩家未登录";
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
}
