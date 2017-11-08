package com.i76game.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.i76game.R;
import com.i76game.utils.Utils;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AccountSecurityActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar account_security_toolbar;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_account_security;
    }

    @Override
    public void initView() {
        setToolbar("帐号安全",R.id.account_security_toolbar);
        account_security_toolbar = (Toolbar) findViewById(R.id.account_security_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            account_security_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }

        LinearLayout phoneSecurity= (LinearLayout) findViewById(R.id.account_security_phone);
        LinearLayout emailSecurity= (LinearLayout) findViewById(R.id.account_security_e_mail);
        LinearLayout passwordSecurity= (LinearLayout) findViewById(R.id.account_security_change_password);
        phoneSecurity.setOnClickListener(this);
        emailSecurity.setOnClickListener(this);
        passwordSecurity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.account_security_e_mail:
                EnterHelpActivity(intent, HelpActivity.HELP_3_E_MAIL);
                break;
            case R.id.account_security_phone:
                EnterHelpActivity(intent, HelpActivity.HELP_4_PHONE);
                break;
            case R.id.account_security_change_password:
                EnterHelpActivity(intent, HelpActivity.HELP_2_MODIFY_PASSWORD);
                break;
        }
    }

    private void EnterHelpActivity(Intent intent, String type) {
        intent.setClass(this, HelpActivity.class);
        intent.putExtra(HelpActivity.TYPE_TO_HELP, type);
        startActivity(intent);
    }
}
