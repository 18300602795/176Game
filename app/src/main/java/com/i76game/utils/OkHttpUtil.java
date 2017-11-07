package com.i76game.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.util.ArrayMap;

import com.google.gson.JsonObject;
import com.i76game.MyApplication;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/5/22.
 */
public class OkHttpUtil {

    public static void getdata(String url, boolean shouldCache,
                               Callback callback) {
        Request.Builder builder = new Request.Builder();
        if (!shouldCache) {
            builder.cacheControl(new CacheControl.Builder().maxAge(0,
                    TimeUnit.SECONDS).build());
        }
        Request request = builder.header("timestamp", Gettimestamp() + "")
                .addHeader("hs-token", gethstoken()).url(url)
                .build();

        makeRequest(request, callback);
    }


    public static void postFormEncodingdata(String url, boolean shouldCache,
                                            ArrayMap<String, String> params, Callback callBack) {

        FormBody.Builder fb = new FormBody.Builder();
        if (params != null) {
            params.put(Global.AGENT, Global.agent);
            params.put(Global.FROM, Global.from);
            params.put(Global.APP_ID, Global.appid);
            params.put(Global.CLIENT_ID, Global.clientid);
        }
        for (Map.Entry e : params.entrySet()) {
            fb.add((String) e.getKey(), (String) e.getValue());
        }
        RequestBody formBody = fb.build();
//        formBody.toString();
        Request.Builder builder = new Request.Builder();
        if (!shouldCache) {
            builder.cacheControl(new CacheControl.Builder().maxAge(0,
                    TimeUnit.SECONDS).build());
        }
        Request request = builder.header("timestamp", Gettimestamp() + "")
                .addHeader("hs-token", gethstoken()).url(url).post(formBody).build();

        makeRequest(request, callBack);
    }

//    public static void postBackMessage(String url, ArrayMap<String, String> params, Callback callBack) {
//        FormBody.Builder fb = new FormBody.Builder();
//        if (params != null) {
//            params.put("--", "");
//        }
//        for (Map.Entry entity : params.entrySet()) {
//            fb.add((String) entity.getValue(),(String) entity.getKey());
//        }
//        RequestBody requestBody=fb.build();
//
//    }


    public static long Gettimestamp() {
        return getcurrent() + MyApplication.time;
    }

    /**
     * 获取当前时间戳，从1970.1.1到现在的秒数
     */
    private static long getcurrent() {
        long i = System.currentTimeMillis() / 1000;
        return i;
    }

    private static OkHttpClient mOkHttpClient;
    private static final int TIME_OUT = 5; // 请求超时 5 秒
    private static Cache cache; // 缓存
    private static final int cacheSize = 10 * 1024 * 1024; // 缓存大小10 MB

    private static void init() {
        File cacheDir = Environment.getDownloadCacheDirectory();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = Environment.getExternalStorageDirectory();
        }
        cache = new Cache(cacheDir, cacheSize);

        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .cache(cache).build();
    }

    private static void makeRequest(Request request, Callback callBack) {
        if (mOkHttpClient == null) {
            OkHttpUtil.init();
        }
        mOkHttpClient.newCall(request).enqueue(callBack);
    }


    public static String gethstoken() {
        String username = SharePrefUtil.getString(MyApplication.getContextObject(),
                SharePrefUtil.KEY.IDENTIFIER, "");
//        Logger.msg("username", username);

        String password = SharePrefUtil.getString(MyApplication.getContextObject(),
                SharePrefUtil.KEY.ACCESSTOKEN, "");
//        Logger.msg("password", username);

        String usernameEcode = AuthCodeUtil.authcodeEncode(username,
                Global.appkey);
//        Logger.msg("usernameEcode", username);

        String passwordEcode = AuthCodeUtil.authcodeEncode(password,
                Global.appkey);
//        Logger.msg("passwordEcode", passwordEcode);

        // JsonArray array = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        String dest = "";
        if (usernameEcode != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(usernameEcode);
            usernameEcode = m.replaceAll("");
        }
        if (passwordEcode != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(passwordEcode);
            passwordEcode = m.replaceAll("");
        }
        jsonObject.addProperty("identify", usernameEcode);
        jsonObject.addProperty("accesstoken", passwordEcode);
        String jsonEncode = jsonObject.toString();
        String aaa = AuthCodeUtil.authcodeEncode(jsonEncode, Global.appkey);
        String str1 = aaa.replaceAll("\n", "");
        return str1;
    }


    /**
     * 网络是否是连接状态
     *
     * @return true表示可能，false网络不可用
     */
    public static boolean isNetWorkConneted(Context ctx) {
        ConnectivityManager cmr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = cmr.getActiveNetworkInfo();
        return null != networkinfo && networkinfo.isConnectedOrConnecting();
    }
}
