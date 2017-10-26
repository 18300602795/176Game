package com.i76game.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.ClipboardManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.i76game.activity.RechargeActivity;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AndroidJSInterfaceForWeb {


    private static final String TAG = AndroidJSInterfaceForWeb.class
            .getSimpleName();
    public Activity ctx;
    public double money;
    public String orderNo;

    /**
     * 接口说明 这个接口是关闭web所依附的那个actvity
     * */
    @JavascriptInterface
    public void closeSelfWindow() {
        ctx.finish();
    }

    /**
     * 接口说明 这个接口是关闭web所依附的那个actvity
     * */
    @JavascriptInterface
    public void closeSelfWindow(String toast) {
        ctx.finish();
        if (!StringUtils.isEmpty(toast))
            Toast.makeText(ctx, "asdfasdf" + toast, Toast.LENGTH_SHORT).show();
    }

    /***
     * 这个接口之前就有，为了方便，我不改变接口名字 ：是复制礼包码的接口
     * */
    @JavascriptInterface
    public void goToGift(String code) {

        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager mClipboard = (android.content.ClipboardManager) ctx
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("simple text", code);
            mClipboard.setPrimaryClip(clip);
            Toast.makeText(ctx, "复制成功，请尽快使用", Toast.LENGTH_LONG).show();
        } else {
            ClipboardManager Mclipboard = (ClipboardManager) ctx
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            Mclipboard.setText(code);

            Toast.makeText(ctx, "复制成功，请尽快使用", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * @param payway
     *            支付方式，见说明
     * @param params
     *            参数(一个json字符串，为什么这么定义，因为每个支付都有不同的参数，我需要一个json字符串，用json传参)
     * @throws Exception
     *             会抛出异常，这个要求代码使用者，不要习惯传参数，没有初始化成员变量（想了想还是捕捉回来好一点）
     * @说明：
     *      这个函数目前支持的支付有：ptbpay,gamepay,alipay,spay,unionpay,payeco,heepay,shengpay
     *      ,shengpayh5
     *
     * */
    @JavascriptInterface
    public void callNativePay(String payway, String params) {
        Log.e("callNativePay", "" + params);
        if (ctx == null) {
            try {
                throw new Exception("ctx==null");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Log.e("callNativePay", "ctx is not null");
        if (StringUtils.isEmpty(params) || StringUtils.isEmpty(payway)) {
            Toast.makeText(
                    ctx,
                    "服务端出现了问题:====>"
                            + "call native method[callNativePay],params is null",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!payway.equals("ptbpay") && !payway.equals("gamepay")
                && !payway.equals("alipay") && !payway.equals("spay")
                && !payway.equals("unionpay") && !payway.equals("payeco")
                && !payway.equals("heepay") && !payway.equals("shengpay")
                && !payway.equals("shengpayh5")) {
            return;
        }
        Log.e("callNativePay", "params is ok");
        PayInterface parser = null;
        PayBean bean = null;
        Intent intent = null;
        if (payway.equals("ptbpay")) {// 平台币
            return;
        }
        if (payway.equals("gamepay")) {// 游戏币

            return;
        }
        if (payway.equals("alipay")) {// 支付宝
            Log.e("callNativePay", "call alipay");
            parser = new AlipayJsonParser();
            bean = (PayBean) parser.parseObj(params);
            intent = new Intent(ctx, AlipayActivityForWap.class);
            intent.putExtra("params", bean);
            ctx.startActivityForResult(intent,
                    RechargeActivity.REQUEST_CODE);
            // 模式一：
            // orderid
            // productname
            // productdesc
            // amount
            // notify_url 回调地址
            // DEFAULT_PARTNER 合作者id(native)
            // DEFAULT_SELLER 账号(native)

            // 模式二：
            // token 签名之后的名字
            return;
        }
        if (payway.equals("spay")) {// 威富通微信支付
//            parser =(PayInterface) new SpayJsonParser();
//            bean = (PayBean) parser.parseObj(params);
//            intent = new Intent(ctx, WFTPayActivityForWap.class);
//            intent.putExtra("params", bean);
//            ctx.startActivityForResult(intent,
//                    ChargeActivityForWap.REQUEST_CODE);
            return;
        }
        if (payway.equals("unionpay")) {// 银行卡（纯银联）
            // mode
            // token_id
            // orderid
            return;
        }
        if (payway.equals("payeco")) {// 银行卡 易联支付
            // mode
            // token (uppayReq)
            // orderid
            return;
        }
        if (payway.equals("heepay")) {// 汇付宝微信支付(30)
            // heepayagentid
            // orderid
            // tokenid
            return;
        }
        if (payway.equals("heepayali")) {// 汇付宝支付宝(22)
            // heepayagentid
            // orderid
            // tokenid
            return;
        }
        if (payway.equals("shnegpay")) {// 盛付通收银台
            // orderJson (需要urlencode)
            // orderid
            // mode
            return;
        }
        if (payway.equals("shnegpayh5")) {// shengpay H5 是卡类支付收银台
            // orderJson (需要urlencode)
            // orderid
            return;
        }
        return;
    }

}
