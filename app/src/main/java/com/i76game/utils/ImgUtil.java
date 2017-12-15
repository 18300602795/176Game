package com.i76game.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.i76game.MyApplication;
import com.i76game.R;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 2016/8/17.
 */
public class ImgUtil {

    private ImgUtil() {

    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new GlideBitmapUtil(appContext);
        }
        return bitmapUtils;
    }

    private static RequestManager mManager;

    public static void loadImage(String url, int icon_horizontal, ImageView imageView, android.app.Fragment fragment) {
        if (mManager == null) {
            mManager = Glide.with(MyApplication.getContextObject());
        }
        mManager.load(url).placeholder(icon_horizontal).into(imageView);
    }

    public static void loadImage(String url, ImageView imageView) {
        if (mManager == null) {
            mManager = Glide.with(MyApplication.getContextObject());
        }
        mManager.load(url).placeholder(R.mipmap.ic_launcher).into(imageView);
    }

    public static void loadImage(Context context, String url, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).into(iv);
    }
    public static void loadImage( String url, int emptyImg, ImageView iv,Fragment context) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).into(iv);
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}