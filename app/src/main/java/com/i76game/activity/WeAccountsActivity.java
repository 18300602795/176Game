package com.i76game.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.i76game.R;
import com.i76game.utils.EncodingUtils;
import com.i76game.utils.ImgUtil;
import com.i76game.utils.ToastUtils;
import com.i76game.utils.Utils;

/**
 * Created by Administrator on 2017/11/15.
 */

public class WeAccountsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar we_toolbar;
    private ImageView qr_iv;
    private TextView accept_btn, save_btn;
    private EditText code_et;
    private Bitmap qrCode;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    @Override
    protected int setLayoutResID() {
        return R.layout.weaccounts_activity;
    }

    @Override
    public void initView() {
        setToolbar("关注微信公众号", R.id.we_toolbar);
        we_toolbar = (Toolbar) findViewById(R.id.we_toolbar);
        qr_iv = (ImageView) findViewById(R.id.qr_iv);
        accept_btn = (TextView) findViewById(R.id.accept_btn);
        save_btn = (TextView) findViewById(R.id.save_btn);
        code_et = (EditText) findViewById(R.id.code_et);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            we_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        String input = "http://down.shouyoucun.cn/sdkgame/syc_60123/76bt.apk";
        qrCode = EncodingUtils.createQRCode(input, 100, 100,
                BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
        save_btn.setOnClickListener(this);
        accept_btn.setOnClickListener(this);
        qr_iv.setImageBitmap(qrCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                autoObtainStoragePermission();
                break;
            case R.id.accept_btn:
                break;
        }
    }
    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            if (ImgUtil.saveImageToGallery(this, qrCode)){
                Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ImgUtil.saveImageToGallery(this, qrCode)){
                        Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

}
