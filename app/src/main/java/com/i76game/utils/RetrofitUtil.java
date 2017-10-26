package com.i76game.utils;

import com.i76game.bean.HomeRVBean;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/23.
 */

public class RetrofitUtil {

    private static RetrofitUtil mInstance;
    private final  Retrofit mRetrofit;

    public static RetrofitUtil getInstance(){
        if (mInstance == null){
            synchronized (RetrofitUtil.class){
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    private RetrofitUtil(){
        //设置baseUrl
        mRetrofit = new Retrofit.Builder()
                //设置baseUrl
                .baseUrl(Global.Hot_GAME_URL)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T  create(Class<T> c) {
        return mRetrofit.create(c);
    }
//    public <T> Observable<T> getObservable(Observable<T> observable){
//        observable
//    }
}
