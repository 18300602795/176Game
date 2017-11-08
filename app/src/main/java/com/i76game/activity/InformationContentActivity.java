package com.i76game.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.i76game.R;
import com.i76game.utils.Global;
import com.i76game.utils.Utils;
import com.i76game.view.LoadDialog;
import com.i76game.view.PowerWebView;

/**
 * Created by Administrator on 2017/6/14.
 */

public class InformationContentActivity extends BaseActivity {
    private LoadDialog mLoadDialog;
    private PowerWebView mWebView;
    private String mPath;
    private ProgressBar progressBar1;
    private Toolbar information_content_toolbar;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_information_content;
    }

    @Override
    public void initView() {
        setToolbar("精彩资讯", R.id.information_content_toolbar);
        information_content_toolbar = (Toolbar) findViewById(R.id.information_content_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            information_content_toolbar.setPadding(0, Utils.dip2px(this, 10), 0, 0);
            setTranslucentStatus(true);
        }
        mWebView = (PowerWebView) findViewById(R.id.information_content_webview);
        int id = getIntent().getIntExtra("id",0);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mPath= Global.INFORMATION_CONTENT+id+"?"+Global.clientid+"&"+Global.appid+"&"+Global.agent;

    }

    @Override
    public void initData() {

        WebSettings webSettings = mWebView.getSettings();
//        webView.setDownloadListener(new MyWebViewDownLoadListener());
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放

        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // //支持通过JS打开新窗口

        mWebView.requestFocusFromTouch();

        webSettings.setSupportZoom(true);  //支持缩放

        mWebView.loadUrl(mPath);

//        Log.e("m====",""+mPath);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mLoadDialog==null){
                    mLoadDialog = new LoadDialog(InformationContentActivity.this,true,"100倍加速中");
                }
//                mLoadDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadDialog.dismiss();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar1.setProgress(newProgress);//设置进度值
                }
            }
        });

    }
}
