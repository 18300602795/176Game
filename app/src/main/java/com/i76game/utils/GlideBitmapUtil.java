package com.i76game.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.i76game.MyApplication;
import com.i76game.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;

/**
 * Created by liu hong liang on 2016/9/14.
 */
public class GlideBitmapUtil<T> extends BitmapUtils {
    public GlideBitmapUtil(Context context) {
        super(context);
    }

    public GlideBitmapUtil(Context context, String diskCachePath) {
        super(context, diskCachePath);
    }

    public GlideBitmapUtil(Context context, String diskCachePath, int memoryCacheSize) {
        super(context, diskCachePath, memoryCacheSize);
    }

    public GlideBitmapUtil(Context context, String diskCachePath, int memoryCacheSize, int diskCacheSize) {
        super(context, diskCachePath, memoryCacheSize, diskCacheSize);
    }

    public GlideBitmapUtil(Context context, String diskCachePath, float memoryCachePercent) {
        super(context, diskCachePath, memoryCachePercent);
    }

    public GlideBitmapUtil(Context context, String diskCachePath, float memoryCachePercent, int diskCacheSize) {
        super(context, diskCachePath, memoryCachePercent, diskCacheSize);
    }
    public <T extends View> void display(T container, String uri) {
        if(container instanceof ImageView){
            display((ImageView)container,uri);
        }else{
            this.display(container, uri, (BitmapDisplayConfig)null, (BitmapLoadCallBack)null);
        }
    }
    public <T extends View> void display(ImageView container, String uri) {
        Glide.with(MyApplication.getContextObject()).load(uri)
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fitCenter().into(container);

    }
}
