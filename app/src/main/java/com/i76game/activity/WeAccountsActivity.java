package com.i76game.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.i76game.R;
import com.i76game.utils.EncodingUtils;
import com.i76game.utils.Utils;

/**
 * Created by Administrator on 2017/11/15.
 */

public class WeAccountsActivity extends BaseActivity {
    private Toolbar we_toolbar;
    private ImageView qr_iv;
    @Override
    protected int setLayoutResID() {
        return R.layout.weaccounts_activity;
    }

    @Override
    public void initView() {
        setToolbar("关注微信公众号", R.id.we_toolbar);
        we_toolbar = (Toolbar) findViewById(R.id.we_toolbar);
        qr_iv = (ImageView) findViewById(R.id.qr_iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            we_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        String input = "http://down.shouyoucun.cn/sdkgame/syc_60123/76bt.apk";
        Bitmap qrCode = EncodingUtils.createQRCode(input, 100, 100,
                BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
        qr_iv.setImageBitmap(qrCode);
    }
}
