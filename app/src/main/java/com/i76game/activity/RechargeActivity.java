package com.i76game.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.i76game.R;
import com.i76game.pay.AndroidJSInterfaceForWeb;
import com.i76game.pay.OnPaymentListener;
import com.i76game.pay.PaymentCallbackInfo;
import com.i76game.pay.PaymentErrorMsg;
import com.i76game.utils.Global;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.Utils;
import com.i76game.view.LoadDialog;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/31.
 */

public class RechargeActivity extends BaseActivity {

    private static final String TAG = "ChargeActivityForWap";

    private String url, title;
    private double amount;
    private String toast;
    HashMap<String, String> header = new HashMap<>();

    public static OnPaymentListener paymentListener;// 充值接口监听
    public boolean isPaySus = false;// 支付jar包的回执
    public static final int REQUEST_CODE = 200;
    private WebView mWebView;
    private LoadDialog mLoadDialog;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        Toolbar toolbar = (Toolbar) findViewById(R.id.recharge_toolbar);
        // 主标题,默认为app_label的名字
        toolbar.setTitle("支付中");
        toolbar.setTitleTextColor(Color.WHITE);
        //侧边栏的按钮
        toolbar.setNavigationIcon(R.mipmap.ic_actionbar_back);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResult();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mWebView = (WebView) findViewById(R.id.recharge_web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("wpa")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        AndroidJSInterfaceForWeb js = new AndroidJSInterfaceForWeb();
        js.ctx = this;
        mWebView.addJavascriptInterface(js, "android");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mLoadDialog==null){
                    mLoadDialog = new LoadDialog(RechargeActivity.this,true,"100倍加速中");
                }
                mLoadDialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("wpa")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadDialog.dismiss();
                webviewCompat(mWebView);

            }

        });
        mWebView.setWebChromeClient(new WebChromeClient());
        webviewCompat(mWebView);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        header.put("timestamp", OkHttpUtil.Gettimestamp() + "");
        String token = OkHttpUtil.gethstoken();
        header.put("hs-token", token + "");
        String clientid = Global.clientid;
        String appid = Global.appid;
        String agent = Global.agent;
        String from = Global.from;
        url = url + "?clientid=" + clientid + "&appid=" + appid + "&agent="
                + agent + "&from=" + from;
        LogUtils.iUrl("充值页面网页："  + url);
        mWebView.loadUrl(url, header);
    }


    /**
     * 一些版本特性操作，需要适配、
     *
     * @date 6/3
     * @param mWebView
     * @reason 在微蓝项目的时候遇到了 返回键 之后 wv显示错误信息
     * */
    private void webviewCompat(WebView mWebView) {
        if (OkHttpUtil.isNetWorkConneted(mWebView.getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            handleResult();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                amount = data.getDoubleExtra("amount", 0);
                boolean sus = data.getBooleanExtra("result", false);
                toast = data.getStringExtra("attach");
                isPaySus = sus;
            }
            handleResult();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handleResult() {
        if (paymentListener == null) {
            return;
        }
        if (isPaySus) {
            PaymentCallbackInfo payInfo = new PaymentCallbackInfo();
            payInfo.msg = toast == null ? "支付成功" : toast;
            payInfo.money = amount;
            paymentListener.paymentSuccess(payInfo);
            finish();
        } else {
            PaymentErrorMsg errorMsg = new PaymentErrorMsg();
            errorMsg.code = -1;
            errorMsg.money = amount;
            errorMsg.msg = toast == null ? "支付失败" : toast;
            paymentListener.paymentError(errorMsg);
            finish();
        }
    }
}
