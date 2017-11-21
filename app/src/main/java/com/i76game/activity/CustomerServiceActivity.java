package com.i76game.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.utils.Utils;

/**
 * 客服界面
 */

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {
    private TextView mQQText;
    private TextView mPhoneText;
    private ImageView back_return;
    private RelativeLayout title_rl;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_customer_service;
    }

    @Override
    public void initView() {
        title_rl = (RelativeLayout) findViewById(R.id.title_rl);
        back_return = (ImageView) findViewById(R.id.back_return);
        back_return.setOnClickListener(this);
        Button connection = (Button) findViewById(R.id.customer_service_btn_connection);
        connection.setOnClickListener(this);
        Button join = (Button) findViewById(R.id.customer_service_btn_join);
        join.setOnClickListener(this);
        Button phone = (Button) findViewById(R.id.customer_service_btn_phone);
        phone.setOnClickListener(this);

        mQQText = (TextView) findViewById(R.id.customer_service_text_connection);
        mPhoneText = (TextView) findViewById(R.id.customer_service_text_phone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            title_rl.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_service_btn_connection:
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + mQQText.getText().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.customer_service_btn_join:
                String url2 = "mqqwpa://im/chat?chat_type=wpa&uin=" + mQQText.getText().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url2)));
                break;

            case R.id.customer_service_btn_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +
                        mPhoneText.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.back_return:
                finish();
                break;
        }
    }
}
