package com.i76game;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.util.ArrayMap;

import com.i76game.bean.UserInfoBean;
import com.i76game.utils.Global;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.SharePrefUtil;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MyApplication extends Application {
    private static Context mContext;
    public static String apkdownload_path="";
    public static int time = 0;
    public static String gameID = "syc_60123";
    public static String agentID = "399";
    public static int item;
    public static String num = "10";
    public static String agent = "241";
    public static UserInfoBean userInfoBean;
    @Override
    public void onCreate(){
        //获取Context
        super.onCreate();
        item = 1;
        mContext = getApplicationContext();
        MobSDK.init(mContext, "223f8bf400810", "9138658894f1356bb66dea019c3daa16");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        apkdownload_path= Environment.getExternalStorageDirectory().getPath()+ File.separator+this.getPackageName();
        File file=new File(apkdownload_path);
        if(!file.exists()){
            file.mkdirs();
        }
        apkdownload_path+=File.separator;

        getData();
    }

    //返回
    public static Context getContextObject(){
        return mContext;
    }

    public void getData() {
        ArrayMap<String, String> map = new ArrayMap<>();
        OkHttpUtil.postFormEncodingdata(Global.GET_SERVER_TIME, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                try {
                    JSONObject josnObject=new JSONObject(res);
                    String code=josnObject.getString("code");
                    if (code.equals("200")){
                        String data=josnObject.getString("data");
                        josnObject=new JSONObject(data);
                        int timeJson=josnObject.getInt("time");

                        int i = SharePrefUtil.saveInt(MyApplication.this,SharePrefUtil.KEY.TIMESTAMP,
                                timeJson-(int)(System.currentTimeMillis()/1000));
                        time = i*2;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

}
