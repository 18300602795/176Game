package com.i76game.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.i76game.MyApplication;
import com.i76game.R;
import com.i76game.utils.ChannelUtil;
import com.i76game.utils.SharePrefUtil;
import com.ta.utdid2.android.utils.StringUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

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
            Log.e("--------------", "onCreate: " + channel);
        }

        Log.e("--------------", "onCreate: 空的" + channel);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
