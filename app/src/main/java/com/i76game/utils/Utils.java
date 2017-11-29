package com.i76game.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2017/10/20.
 */

public class Utils {

    /**
     * get请求方法拼接
     */
    public static String getCompUrlFromParams(String url, Map<String, String> params) {
        String url1 = "";
//        if (params != null) {
//            params.put(Constants.AGENT, Constants.agent);
//            params.put(Constants.FROM, Constants.from);
//            params.put(Constants.APP_ID, Constants.appid);
//            params.put(Constants.CLIENT_ID, Constants.clientid);
//        }
        if (null != url && !"".equals(url) && null != params
                && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for (Map.Entry<String, String> entity : params.entrySet()) {
                sb.append(entity.getKey()).append("=")
                        .append(entity.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url1 = url + sb.toString();
            return url1;
        }
        return url;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void showShare(final Context context, String text, String imageUrl, String imagePath, String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享测试");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.shouyoucun.cn/index.php/Web/Substation/index/gameid/" + url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        if (imagePath.equals("")) {
            oks.setImageUrl(imageUrl);
        } else {
            oks.setImagePath(imagePath);
        }
        // url仅在微信（包括好友和朋友圈）中使用
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        oks.setUrl(url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("aaa");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
//            }
//        });
        // 启动分享GUI
        oks.show(context);
    }

    public static SpannableStringBuilder setStyle(String str, String bstr, String fstr) {
        int bstart = str.indexOf(bstr);
        int bend = bstart + bstr.length();
        int fstart = str.indexOf(fstr);
        int fend = fstart + fstr.length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.BLACK), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.BLACK), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    public static double change(double a) {
        return a * Math.PI / 180;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.equals(null) || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
