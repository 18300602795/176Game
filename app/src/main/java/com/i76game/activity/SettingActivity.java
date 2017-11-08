package com.i76game.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.utils.SharePrefUtil;
import com.i76game.utils.Utils;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("设置中心");
        //侧边栏的按钮
        toolbar.setNavigationIcon(R.mipmap.ic_actionbar_back);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.setting_up_layout);
        relativeLayout.setOnClickListener(this);
        Button outLogin= (Button) findViewById(R.id.setting_out_login);
        outLogin.setOnClickListener(this);
        TextView versionText= (TextView) findViewById(R.id.setting_text_version);
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionText.setText("v"+version);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_out_login:
                SharePrefUtil.delete(this);
                SharePrefUtil.saveBoolean(this, SharePrefUtil.KEY.FIRST_LOGIN, true);
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.setting_up_layout:

                break;
        }
    }
}
