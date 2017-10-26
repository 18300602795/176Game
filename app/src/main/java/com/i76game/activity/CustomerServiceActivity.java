package com.i76game.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.i76game.R;

/**
 * 客服界面
 */

public class CustomerServiceActivity  extends  BaseActivity implements View.OnClickListener{
    private TextView mQQText;
    private TextView mPhoneText;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_customer_service;
    }

    @Override
    public void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        Button connection= (Button) findViewById(R.id.customer_service_btn_connection);
        connection.setOnClickListener(this);
        Button phone= (Button) findViewById(R.id.customer_service_btn_phone);
        phone.setOnClickListener(this);

        mQQText = (TextView) findViewById(R.id.customer_service_text_connection);
        mPhoneText = (TextView) findViewById(R.id.customer_service_text_phone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.customer_service_btn_connection:
                String url="mqqwpa://im/chat?chat_type=wpa&uin="+mQQText.getText().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;

            case R.id.customer_service_btn_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+
                        mPhoneText.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}