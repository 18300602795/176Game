package com.i76game.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.utils.Global;

import javax.security.auth.login.LoginException;

/**
 * Created by Administrator on 2017/5/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResID());
        initView();
        initData();
    }

    protected abstract int setLayoutResID();

    /**
     * 初始化UI
     */
    public abstract void initView(
    );

    public void initData() {

    }

    private Toast toast = null;
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    public void setToolbar(String title, int id, View.OnClickListener listener){
        Toolbar toolbar = (Toolbar) findViewById(id);
        // 主标题,默认为app_label的名字
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        //侧边栏的按钮
        toolbar.setNavigationIcon(R.mipmap.ic_actionbar_back);
        //取代原本的actionbar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(listener);
    }

    public void setToolbar(String title, int id){
        Toolbar toolbar = (Toolbar) findViewById(id);
        // 主标题,默认为app_label的名字
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        //侧边栏的按钮
        toolbar.setNavigationIcon(R.mipmap.ic_actionbar_back);
        //取代原本的actionbar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 权限检查
     */
    public boolean hasPermission(String...permissions){
        if (Build.VERSION.SDK_INT<23){
            return true;
        }

        for (String permission:permissions){
            if (ContextCompat.checkSelfPermission(this,permission)!=
                    PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     */
    public void requestPermission(int code , String... permissions){
        ActivityCompat.requestPermissions(this,permissions,code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Global.WRITE_READ_EXTERNAL_CODE:
                writePermission();
                break;
        }
    }

    public void writePermission(){

    }

    /**
     * 设置状态栏透明
     * @param on
     */
    @TargetApi(19)
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
}
