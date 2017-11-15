package com.i76game.utils;

import com.i76game.bean.GameContentBean;
import com.i76game.bean.GiftBean;
import com.i76game.bean.HomeRVBean;
import com.i76game.bean.InformationRVBean;
import com.i76game.bean.LunboImgViewBean;
import com.i76game.bean.NewVersionBean;
import com.i76game.bean.TypeBean;
import com.i76game.bean.UserGiftCodeBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/5/17.
 */

public interface HttpServer {

    /**
     * 游戏详情
     */
    interface GameContentServer {
        @GET("game/detail")
        Observable<GameContentBean> listResponse(@QueryMap Map<String,String> queryMap);
    }

    /**
     * 游戏详情
     */
    interface SlideShow {
        @GET("slide/list")
        Observable<LunboImgViewBean> listResponse(@QueryMap Map<String,String> queryMap);
    }

    /**
     * 主页面热门
     */
    interface HomeService {
        @GET("game/list")
        Call<HomeRVBean> listResponse(@QueryMap Map<String,String> queryMap);
    }

    /**
     * 类型
     */
    interface TypeService{
        @GET("game/type_list")
        Observable<TypeBean> listResponse(@QueryMap Map<String,String> queryMap);
    }


    /**
     * 主页面热门
     */
    interface HotService {
        @GET("game/list")
        Observable<HomeRVBean> listResponse(@QueryMap Map<String,String> queryMap);
    }


    /**
     * 礼包中心
     */
    interface GiftService {
        @GET("gift/list")
        Observable<GiftBean> listResponse(@QueryMap Map<String,String> queryMap);
    }


    /**
     * 搜索
     */
    interface SearchService {
        @GET("search/list")
        Observable<HomeRVBean> listResponse(@QueryMap Map<String,String> queryMap);
    }


    /**
     * 已领取礼包
     */
    interface UserGiftService{
        @GET("user/gift/list")
        Observable<UserGiftCodeBean> listResponse(@QueryMap Map<String,String> queryMap);
    }

    /**
     * 检查更新
     */
    interface NewVersionService{
        @GET("system/has_new_version")
        Observable<NewVersionBean> listResponse(@QueryMap Map<String,String> queryMap);
    }


    /**
     * 资讯中心
     */
    interface InformationService{
        @GET("news/list")
        Observable<InformationRVBean> listResponse(@QueryMap Map<String,String> queryMap);
    }
}
