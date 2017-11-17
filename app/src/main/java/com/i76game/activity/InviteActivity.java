package com.i76game.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.utils.EncodingUtils;
import com.i76game.utils.Utils;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/11/15.
 */

public class InviteActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar invite_toolbar;
    private ImageView qr_iv;
    private LinearLayout wechat_ll, moments_ll, weibo_ll, qq_ll, qzone_ll, copy_ll;
    private String url = "http://down.shouyoucun.cn/sdkgame/syc_60123/76bt.apk";

    @Override
    protected int setLayoutResID() {
        return R.layout.invite_activity;
    }

    @Override
    public void initView() {
        setToolbar("邀请好友", R.id.invite_toolbar);
        invite_toolbar = (Toolbar) findViewById(R.id.invite_toolbar);
        qr_iv = (ImageView) findViewById(R.id.qr_iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            invite_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        wechat_ll = (LinearLayout) findViewById(R.id.wechat_ll);
        moments_ll = (LinearLayout) findViewById(R.id.moments_ll);
        weibo_ll = (LinearLayout) findViewById(R.id.weibo_ll);
        qq_ll = (LinearLayout) findViewById(R.id.qq_ll);
        qzone_ll = (LinearLayout) findViewById(R.id.qzone_ll);
        copy_ll = (LinearLayout) findViewById(R.id.copy_ll);
        wechat_ll.setOnClickListener(this);
        moments_ll.setOnClickListener(this);
        weibo_ll.setOnClickListener(this);
        qq_ll.setOnClickListener(this);
        qzone_ll.setOnClickListener(this);
        copy_ll.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setQr(url);
    }

    private void setQr(String str) {
        Bitmap qrCode = EncodingUtils.createQRCode(str, 100, 100,
                BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
        qr_iv.setImageBitmap(qrCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_ll:
                share(Wechat.NAME);
                break;
            case R.id.moments_ll:
                share(WechatMoments.NAME);
                break;
            case R.id.weibo_ll:
                share(SinaWeibo.NAME);
                break;
            case R.id.qq_ll:
                share(QQ.NAME);
                break;
            case R.id.qzone_ll:
                share(QZone.NAME);
                break;
            case R.id.copy_ll:
                ClipboardManager copy = (ClipboardManager) InviteActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(url);
                Toast.makeText(this, "链接复制成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void share(String name) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.setTitleUrl(url);
        oks.setUrl(url);
        oks.setSiteUrl(url);
        oks.setText("text");
        oks.setTitle("标题");
        oks.setPlatform(name);
        oks.show(this);
    }
}
