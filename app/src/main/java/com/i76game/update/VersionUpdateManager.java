package com.i76game.update;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.i76game.bean.NewVersionBean;
import com.i76game.utils.Global;
import com.i76game.utils.HttpServer;
import com.i76game.utils.LogUtils;
import com.i76game.utils.OkHttpUtil;
import com.i76game.utils.StringUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/5.
 */

public class VersionUpdateManager {
    private Activity mContext;
    private int versionCode;
    private String versionName;

    private VersionUpdateDialog mDialog;

    public VersionUpdateManager(final Activity mContext) {
        this.mContext = mContext;
        getNativeVersion();

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("verid", versionCode + "");
        map.put("vername", versionName + "");
        map.put("clientid", "49");
        map.put("appid", "100");
        map.put("agent", "");
        map.put("from", "3");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        builder.addHeader("hs-token", OkHttpUtil.gethstoken());
                        builder.addHeader("timestamp", OkHttpUtil.Gettimestamp() + "");
                        return chain.proceed(builder.build());
                    }
                }).retryOnConnectionFailure(true).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Global.Hot_GAME_URL)
                .build();

        Observable<NewVersionBean> observable = retrofit.create(
                HttpServer.NewVersionService.class).listResponse(map);
        LogUtils.iUrl(StringUtils.getCompUrlFromParams(Global.Hot_GAME_URL, map));
        observable.subscribeOn(Schedulers.io())// 指定被观察者发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewVersionBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull final NewVersionBean newVersionBean) {
                        int code=newVersionBean.getCode();
                        LogUtils.i("VersionUpdateManager code：" + code);
                        LogUtils.i("VersionUpdateManager msg“" + newVersionBean.getMsg());
                        if (code == 404&&newVersionBean.getData()==null) {
                            return;
                        }
                        LogUtils.i("VersionUpdateManager url：" + newVersionBean.getData().getNewurl());
                        showUpdateDialog(newVersionBean, mContext);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void showUpdateDialog(@NonNull final NewVersionBean newVersionBean, final Activity mContext) {
        if(mDialog==null) {
            mDialog = new VersionUpdateDialog();
            mDialog.showDialog(mContext, true, "有新版本发布了，是否需要下载更新？",
                    new VersionUpdateDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(mContext, VersionUpdateService.class);
                    intent.putExtra("url", newVersionBean.getData().getNewurl());
//                    intent.putExtra("url","http://183.61.13.174/imtt.dd.qq.com/16891/2A76B7A9A8E841F0D8C1E74AD65FCB3F.apk?mkey=57d696be4979a782&f=8f5d&c=0&fsname=com.tencent.mobileqq_6.5.3_398.apk&csr=4d5s&p=.apk");
                    mContext.startService(intent);
                    Toast.makeText(mContext, "开始下载,请在下载完成后确认安装！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void cancel() {
                }
            });

        }
    }


    /**
     * 获取本地版本号
     */

    public void getNativeVersion() {
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;
            versionName = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}