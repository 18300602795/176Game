package com.i76game.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/15.
 */

public class SignActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar sign_toolbar;
    private TextView date_tv, num_tv, name_tv;
    private TextView sign_btn;
    private ImageView sign_iv1, sign_iv2, sign_iv3, sign_iv4, sign_iv5, sign_iv6, sign_iv7;
    private ImageView icon_iv;
    private LinearLayout demo_ll, invite_ll, photo_ll, info_ll, share_ll;

    @Override
    protected int setLayoutResID() {
        return R.layout.sign_activity;
    }

    @Override
    public void initView() {
        setToolbar("每日签到", R.id.sign_toolbar);
        sign_toolbar = (Toolbar) findViewById(R.id.sign_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sign_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        date_tv = (TextView) findViewById(R.id.date_tv);
        num_tv = (TextView) findViewById(R.id.num_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        sign_btn = (TextView) findViewById(R.id.sign_btn);

        sign_iv1 = (ImageView) findViewById(R.id.sign_iv1);
        sign_iv2 = (ImageView) findViewById(R.id.sign_iv2);
        sign_iv3 = (ImageView) findViewById(R.id.sign_iv3);
        sign_iv4 = (ImageView) findViewById(R.id.sign_iv4);
        sign_iv5 = (ImageView) findViewById(R.id.sign_iv5);
        sign_iv6 = (ImageView) findViewById(R.id.sign_iv6);
        sign_iv7 = (ImageView) findViewById(R.id.sign_iv7);
        icon_iv = (ImageView) findViewById(R.id.icon_iv);

        demo_ll = (LinearLayout) findViewById(R.id.demo_ll);
        invite_ll = (LinearLayout) findViewById(R.id.invite_ll);
        photo_ll = (LinearLayout) findViewById(R.id.photo_ll);
        info_ll = (LinearLayout) findViewById(R.id.info_ll);
        share_ll = (LinearLayout) findViewById(R.id.share_ll);

        demo_ll.setOnClickListener(this);
        invite_ll.setOnClickListener(this);
        photo_ll.setOnClickListener(this);
        info_ll.setOnClickListener(this);
        share_ll.setOnClickListener(this);
        sign_btn.setOnClickListener(this);
        num_tv.setText(Utils.setStyle(num_tv.getText().toString(), "连续签到", "天"));
        name_tv.setText(Utils.setStyle(name_tv.getText().toString(), "试玩", ""));
    }


    @Override
    public void initData() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        date_tv.setText(format.format(date));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.demo_ll:
                break;
            case R.id.invite_ll:
                startActivity(new Intent(this, InviteActivity.class));
                break;
            case R.id.photo_ll:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.info_ll:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.share_ll:
                startActivity(new Intent(this, InviteActivity.class));
                break;
            case R.id.sign_btn:
                sign_iv1.setImageResource(R.mipmap.ic_check_box_checked);
                num_tv.setText("连续签到1天");
                num_tv.setText(Utils.setStyle(num_tv.getText().toString(), "连续签到", "天"));
                sign_btn.setBackgroundResource(R.drawable.sign_gray_bg);
                sign_btn.setText("已签到");
                break;
        }
    }
}
