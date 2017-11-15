package com.i76game.utils.httpUtil;

import com.i76game.utils.Global;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/4.
 */

public class HttpManager {
    private static Retrofit mRetrofit;
    private HttpManager (){
        initRetrofit();
    }
    private static HttpManager mHttpManager;
    private static HttpManager getInstent(){
        if (mHttpManager==null){
            synchronized (HttpManager.class){
                if (mHttpManager==null){
                    mHttpManager=new HttpManager();
                }
            }
        }
        return new HttpManager();
    }
    private void initRetrofit(){
        mRetrofit = new Retrofit.Builder()
                //设置baseUrl
                .baseUrl(Global.Hot_GAME_URL)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public HttpApi createApiService(){
        return mRetrofit.create(HttpApi.class);
    }
    public <T> void toSubscribe(Observable<T> o) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
//            SubscriberManager.getInstance().addSubscription(context, );

    }


    public void i(){
        Map<String,String> map=new HashMap<>();
        HttpManager instent = HttpManager.getInstent();
        instent.toSubscribe(instent.createApiService().HotService(map));
    }
}
