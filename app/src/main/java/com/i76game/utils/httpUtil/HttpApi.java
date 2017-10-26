package com.i76game.utils.httpUtil;

import com.i76game.bean.GameContentBean;
import com.i76game.bean.GiftBean;
import com.i76game.bean.HomeRVBean;
import com.i76game.bean.InformationRVBean;
import com.i76game.bean.NewVersionBean;
import com.i76game.bean.TypeBean;
import com.i76game.bean.UserGiftCodeBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/7/4.
 */

public interface HttpApi {

    /**
     * 游戏详情
     */
    @GET("game/detail")
    Observable<GameContentBean> GameContentServer(@QueryMap Map<String, String> queryMap);


    /**
     * 主页面热门
     */
    @GET("game/list")
    Call<HomeRVBean> HomeService(@QueryMap Map<String, String> queryMap);

    /**
     * 类型
     */
    @GET("game/type_list")
    Observable<TypeBean> TypeService(@QueryMap Map<String, String> queryMap);


    /**
     * 主页面热门
     */
    @GET("game/list")
    Observable<HomeRVBean> HotService(@QueryMap Map<String, String> queryMap);


    /**
     * 礼包中心
     */
    @GET("gift/list")
    Observable<GiftBean> GiftService(@QueryMap Map<String, String> queryMap);


    /**
     * 搜索
     */
    @GET("search/list")
    Observable<HomeRVBean> SearchService(@QueryMap Map<String, String> queryMap);


    /**
     * 已领取礼包
     */
    @GET("user/gift/list")
    Observable<UserGiftCodeBean> UserGiftService(@QueryMap Map<String, String> queryMap);

    /**
     * 检查更新
     */
    @GET("system/has_new_version")
    Observable<NewVersionBean> NewVersionService(@QueryMap Map<String, String> queryMap);


    /**
     * 资讯中心
     */
    @GET("news/list")
    Observable<InformationRVBean> InformationService(@QueryMap Map<String, String> queryMap);
}
