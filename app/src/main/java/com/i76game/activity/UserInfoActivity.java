package com.i76game.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i76game.R;
import com.i76game.utils.LogUtils;
import com.i76game.utils.PhotoUtils;
import com.i76game.utils.ToastUtils;
import com.i76game.utils.Utils;
import com.i76game.view.CircleImageView;
import com.i76game.view.PhotoDialog;

import java.io.File;

/**
 * Created by Administrator on 2017/11/16.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar info_toolbar;
    private LinearLayout photo_ll, num_ll, nick_ll, qq_ll, wechat_ll, password_ll;
    private TextView num_tv, nick_tv, qq_tv, wechat_tv;
    private TextView exit_btn;
    private CircleImageView photo_iv;
    private PhotoDialog dialog;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;


    @Override
    protected int setLayoutResID() {
        return R.layout.userinfo_activity;
    }

    @Override
    public void initView() {
        setToolbar("个人中心", R.id.info_toolbar);
        info_toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            info_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        dialog = new PhotoDialog(this);
        photo_ll = (LinearLayout) findViewById(R.id.photo_ll);
        num_ll = (LinearLayout) findViewById(R.id.num_ll);
        nick_ll = (LinearLayout) findViewById(R.id.nick_ll);
        qq_ll = (LinearLayout) findViewById(R.id.qq_ll);
        wechat_ll = (LinearLayout) findViewById(R.id.wechat_ll);
        password_ll = (LinearLayout) findViewById(R.id.password_ll);

        num_tv = (TextView) findViewById(R.id.num_tv);
        nick_tv = (TextView) findViewById(R.id.nick_tv);
        qq_tv = (TextView) findViewById(R.id.qq_tv);
        wechat_tv = (TextView) findViewById(R.id.wechat_tv);
        exit_btn = (TextView) findViewById(R.id.exit_btn);

        photo_iv = (CircleImageView) findViewById(R.id.photo_iv);

        photo_ll.setOnClickListener(this);
        num_ll.setOnClickListener(this);
        nick_ll.setOnClickListener(this);
        qq_ll.setOnClickListener(this);
        wechat_ll.setOnClickListener(this);
        password_ll.setOnClickListener(this);

        exit_btn.setOnClickListener(this);
        num_tv.setText(setNum(num_tv.getText().toString()));
    }

    private String setNum(String num) {
        if (num.length() == 11) {
            String num1 = num.substring(0, 3);
            String num2 = num.substring(num.length() - 5, num.length() - 1);
            num = num1 + "****" + num2;
        }
        return num;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo_ll:
                dialog.show();
                dialog.setOnCallbackLister(new PhotoDialog.ClickListenerInterface() {
                    @Override
                    public void click(int id) {
                        dialog.cancel();
                        switch (id) {
                            case R.id.take_btn:
                                try {
                                    autoObtainCameraPermission();
                                } catch (Exception e) {
                                    LogUtils.e(e.toString());
                                    ToastUtils.showShort(UserInfoActivity.this, "您没有打开拍照权限");
                                }

                                break;
                            case R.id.photo_btn:
                                try {
                                    autoObtainStoragePermission();
                                } catch (Exception e) {
                                    LogUtils.e(e.toString());
                                    ToastUtils.showShort(UserInfoActivity.this, "您没有打开读取SD卡的权限");
                                }
                                break;
                        }
                    }
                });
                break;
            case R.id.num_ll:
                break;
            case R.id.nick_ll:
                Intent intent = new Intent(this, EditNickActivity.class);
                intent.putExtra("nick", nick_tv.getText().toString());
                startActivityForResult(intent, 101);
                break;
            case R.id.qq_ll:
                break;
            case R.id.wechat_ll:
                break;
            case R.id.password_ll:
                startActivity(new Intent(this, EditWorldActivity.class));
                break;
            case R.id.exit_btn:
                break;

        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.i76game", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.i76game", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 100;
    private static final int OUTPUT_Y = 100;

    @Override
    //照相以后返回的照片
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 102 && requestCode == 101) {
            String nick = data.getStringExtra("nick");
            nick_tv.setText(nick);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    LogUtils.i("访问到相册");
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        LogUtils.i("cropImageUri：" + cropImageUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                newUri = FileProvider.getUriForFile(this, "com.i76game", new File(newUri.getPath()));
                            } catch (Exception e) {
                                LogUtils.e(e.toString());
                            }
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    LogUtils.i("开始显示图片");
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
                default:
            }
        }

    }

    private void showImages(Bitmap bitmap) {
        photo_iv.setImageBitmap(bitmap);
    }
}
