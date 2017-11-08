package com.i76game.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.i76game.R;
import com.i76game.utils.Utils;

/**
 * 充值明细
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar order_toolbar;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_order;
    }

    @Override
    public void initView() {
        setToolbar("货币明细",R.id.order_toolbar);
        order_toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            order_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        LinearLayout rechargeLayout= (LinearLayout) findViewById(R.id.order_recharge);
        LinearLayout consumeLayout= (LinearLayout) findViewById(R.id.order_consume);
        rechargeLayout.setOnClickListener(this);
        consumeLayout.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case  R.id.order_recharge:
                EnterHelpActivity(intent, HelpActivity.HELP_6_RECHARGE);
                break;
            case  R.id.order_consume:
                EnterHelpActivity(intent, HelpActivity.HELP_7_CONSUME);
                break;
        }
    }


    private void EnterHelpActivity(Intent intent, String type) {
        intent.setClass(this, HelpActivity.class);
        intent.putExtra(HelpActivity.TYPE_TO_HELP, type);
        startActivity(intent);
    }
}
