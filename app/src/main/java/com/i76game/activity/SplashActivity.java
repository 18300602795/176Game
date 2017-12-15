package com.i76game.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.utils.ChannelUtil;
import com.i76game.utils.Global;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.SharePrefUtil;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {
    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        setContentView(R.layout.activity_splash);
        String cachePath = MyApplication.apkdownload_path + "CacheTable";
        getUserMessage();
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
//        String channel = ChannelUtil.getChannel(this, ChannelUtil.AGENT_FILE);
//        if (!StringUtils.isEmpty(channel)) {
//            channel = ChannelUtil.getChannel(this, ChannelUtil.AGENT_FILE2);
//            if (!channel.equals("")){
//                SharePrefUtil.saveString(this,SharePrefUtil.KEY.AGENT,channel);
//                Log.e("--------------", "onCreate: "+channel);
//            }
//            Log.e("66666666666", "onCreate: "+channel);
//        }

        String channel = ChannelUtil.getChannel(this,
                ChannelUtil.AGENT_FILE);
        if (!StringUtils.isEmpty(channel)) {
            SharePrefUtil.saveString(this, SharePrefUtil.KEY.AGENT, channel);
            LogUtils.e("channel: " + channel);
        }
        LogUtils.e("channel: 空的" + channel);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    /**
     * 获取用户信息
     */
    public void getUserMessage() {
        LogUtils.i("获取用户信息：" + Global.MONEY_URL);
        OkHttpUtil.getdata(Global.MONEY_URL, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                LogUtils.i("获取用户信息：" + res);
                parseJson(res);
            }
        });
    }

    /**
     * 登录状态
     *
     * @param res 成功返回的json数据
     */
    private void parseJson(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.getInt("code");
            if (code == 403) {
                String msg = jsonObject.getString("msg");
                if (msg.equals(com.i76game.utils.StringUtils.lgoin_state)) {
                    SharePrefUtil.delete(this);
                    SharePrefUtil.saveBoolean(this, SharePrefUtil.KEY.FIRST_LOGIN, true);
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }
    }
}
