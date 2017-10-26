package com.i76game.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;
import com.i76game.MyApplication;


/**
 * Created by Administrator on 2017/5/17.
 */

public class GlideUtil {

    //dip转化为px
    public static int dip2px(Context context,float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
        //因为要转化为整形，即使4.9也会转化为4，所以要加0.5
    }

    //sp转化为px
    public  static int sp2px(float sp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    private static RequestManager mManager;
    public static void loadImage( String url, ImageView imageView,
                                  int loadingImage){
        if (mManager==null){
            mManager = Glide.with(MyApplication.getContextObject());
        }
        mManager.load(url).placeholder(loadingImage).into(imageView);
    }
    public static void loadImage(Activity context, String url, ImageView imageView, int loadingImage){
        Glide.with(context).load(url).placeholder(loadingImage).into(imageView);
    }

}
