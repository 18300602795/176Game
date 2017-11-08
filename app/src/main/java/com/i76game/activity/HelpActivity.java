package com.i76game.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.i76game.R;
import com.i76game.utils.Global;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.Utils;
import com.i76game.view.LoadDialog;
import com.i76game.view.PowerWebView;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/27.
 */

public class HelpActivity extends BaseActivity {

    private Toolbar mToolbar;
    private String mLunbotuUrl;
    private PowerWebView mWebView;
    private WebSettings mWebSettings;
    HashMap<String, String> mHeaderMap = new HashMap<>();
    LoadDialog mLoadDialog;
    public static final String TYPE_TO_HELP="type_to_help";
    public static final String URL_TO_HELP="URL_to_help";

    public static final String HELP_1_CUSTOMER_SERVICE="help_1_customer_service";
    public static final String HELP_2_MODIFY_PASSWORD="help_2_modify_password";
    public static final String HELP_3_E_MAIL="help_3_e_mail";
    public static final String HELP_4_PHONE="help_4_phone";
    public static final String HELP_5_WALLET="help_5_wallet";
    public static final String HELP_6_RECHARGE="help_6_recharge";
    public static final String HELP_7_CONSUME="help_7_consume";
    public static final String HELP_8_FORGET_PASSWORD="help_8_forget_password";

    public static final String HELP_9_URL="help_9_url";
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_help;
    }

    @Override
    public void initView() {

        mWebView = (PowerWebView) findViewById(R.id.help_web_view);
        mToolbar = (Toolbar) findViewById(R.id.help_toolbar);
        mToolbar.setTitle("安全中心");
        mToolbar.setTitleTextColor(Color.WHITE);
        //侧边栏的按钮
        mToolbar.setNavigationIcon(R.mipmap.ic_actionbar_back);
        //取代原本的actionbar
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String type=getIntent().getStringExtra(TYPE_TO_HELP);
        setTitle(type);
        mLunbotuUrl = getIntent().getStringExtra(URL_TO_HELP);
        getWebViewData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mToolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
    }

    private String url;
    public void setTitle(String type) {
        String clientid = Global.clientid;
        String appid = Global.appid;
        String agent = Global.agent;
        String from = Global.from;
        switch (type) {
            case HELP_1_CUSTOMER_SERVICE:
//                mToolbar.setTitle("客服中心");
                url = Global.BASE_URL+"float.php/Mobile/Help/index";
                break;
            case HELP_2_MODIFY_PASSWORD:
//                mToolbar.setTitle("密码修改");
                url = Global.BASE_URL+"float.php/Mobile/Password/uppwd";
                break;
            case HELP_3_E_MAIL:
//                mToolbar.setTitle("密保邮箱");
                url = Global.BASE_URL+"float.php/Mobile/Security/email";
                break;
            case HELP_4_PHONE:
//                mToolbar.setTitle("密保手机");
                url = Global.BASE_URL+"float.php/Mobile/Security/mobile";
                break;
            case HELP_5_WALLET:
//                mToolbar.setTitle("钱包");
                url = Global.BASE_URL+"float.php/Mobile/Wallet/charge";
                break;
            case HELP_6_RECHARGE:
//                mToolbar.setTitle("充值记录");
                url = Global.BASE_URL+"float.php/Mobile/Wallet/charge_detail";
                break;
            case HELP_7_CONSUME:
//                mToolbar.setTitle("消费记录");
                url = Global.BASE_URL+"float.php/Mobile/Wallet/pay_detail";
                break;

            case HELP_8_FORGET_PASSWORD:
//                mToolbar.setTitle("忘记密码");
                url = Global.BASE_URL+"float.php/Mobile/Forgetpwd/index";
                break;

            case HELP_9_URL:
                url = mLunbotuUrl;
                mToolbar.setVisibility(View.GONE);
                break;
//            case HELP_9_URL:
//                url = mLunbotuUrl;
//                mToolbar.setVisibility(View.GONE);
//                break;
        }
        url = url + "?clientid=" + clientid + "&appid=" + appid + "&agent="
                + agent + "&from=" + from;
    }

    private void getWebViewData(){
        // WebView加载web资源
        mWebSettings = mWebView.getSettings();
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        // 设置WebView属性，能够执行Javascript脚本
        mWebSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        mWebSettings.setAllowFileAccess(true);
        // 设置支持缩放
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放

        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // //支持通过JS打开新窗口

        mWebView.requestFocusFromTouch();

        mWebSettings.setSupportZoom(true); // 支持缩放

        mHeaderMap.put("timestamp", OkHttpUtil.Gettimestamp() + "");
        String token = OkHttpUtil.gethstoken();
        mHeaderMap.put("hs-token", token + "");
//        Log.e("helpActivity","token="+token);
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mLoadDialog==null){
                    mLoadDialog = new LoadDialog(HelpActivity.this,true,"100倍加速中");
                }
                mLoadDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(mLoadDialog!=null){
                    mLoadDialog.dismiss();
                }

            }
        });
        mWebView.clearCache(true);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(url,mHeaderMap);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();// goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        return false;
    }

    @Override
    protected void onDestroy() {
        mWebView.setVisibility(View.GONE);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true);
    }
}
