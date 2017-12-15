package com.i76game.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.i76game.R;
import com.i76game.utils.Utils;
import com.i76game.view.TipDialog;

/**
 * Created by Administrator on 2017/11/15.
 */

public class EarnActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar earn_toolbar;
    private ImageView earn_iv;
    private LinearLayout sign_ll, attention_ll, invite_ll, active_ll, demo_ll, play_ll;
    private TipDialog tipDialog;
    @Override
    protected int setLayoutResID() {
        return R.layout.earn_activity;
    }

    @Override
    public void initView() {
        setToolbar("我要赚钱", R.id.earn_toolbar);
        earn_toolbar = (Toolbar) findViewById(R.id.earn_toolbar);
        tipDialog = new TipDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            earn_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        earn_iv = (ImageView) findViewById(R.id.earn_iv);
        sign_ll = (LinearLayout) findViewById(R.id.sign_ll);
        sign_ll.setOnClickListener(this);
        attention_ll = (LinearLayout) findViewById(R.id.attention_ll);
        attention_ll.setOnClickListener(this);
        invite_ll = (LinearLayout) findViewById(R.id.invite_ll);
        invite_ll.setOnClickListener(this);
        active_ll = (LinearLayout) findViewById(R.id.active_ll);
        active_ll.setOnClickListener(this);
        demo_ll = (LinearLayout) findViewById(R.id.demo_ll);
        demo_ll.setOnClickListener(this);
        play_ll = (LinearLayout) findViewById(R.id.play_ll);
        play_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_ll:
                startActivity(new Intent(this, SignActivity.class));
                break;
            case R.id.attention_ll:
                startActivity(new Intent(this, WeAccountsActivity.class));
                break;
            case R.id.invite_ll:
                startActivity(new Intent(this, InviteActivity.class));
                break;
            case R.id.active_ll:
                Intent intent = new Intent();
                intent.setClass(this, InformationActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("title", "最新活动");
                startActivity(intent);
                break;
            case R.id.demo_ll:
                tipDialog.show();
                tipDialog.setTip("该功能正在开发中，敬请期待");
                tipDialog.setOnCallbackLister(new TipDialog.ClickListenerInterface() {
                    @Override
                    public void click(int id) {
                        switch (id) {
                            case R.id.cancel_btn:
                                tipDialog.cancel();
                                break;
                            case R.id.confirm_btn:
                                tipDialog.cancel();
                                break;
                        }
                    }
                });
                break;
            case R.id.play_ll:
                break;
        }
    }
}
