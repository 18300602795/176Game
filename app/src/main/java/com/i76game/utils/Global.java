package com.i76game.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/5/9.
 */

public class Global {
    public static final String GAME_ID="game_id";

    public static final String BASE_URL = "http://www.shouyoucun.cn/";
    public static final String SERVER_URL = BASE_URL+"sdk/open.php";
    public static final String Hot_GAME_URL = BASE_URL+"api/public/index.php/v1/";


    public static final String MONEY_URL=Hot_GAME_URL+"user/wallet/get_balance?clientid=49&appid=100&agent=&from=3";
    /**
     * 用户详情
     * */
    public static final String INFO_URL=Hot_GAME_URL+"user/get_user_info";

    /**
     * 用户登陆
     * */
    public static final String LOGIN_URL=Hot_GAME_URL+"user/login";

    /**
     * 验证码
     * */
    public static final String SMS_CODE_SEND=Hot_GAME_URL+"smscode/send";
    /**
     * 领取礼包
     * */
    public static final String GET_GIFT=Hot_GAME_URL+"user/gift/add";

    /**
     * 获取服务器时间
     */
    public static final String GET_SERVER_TIME=Hot_GAME_URL+"system/get_server_time";

    /**
     * 用户注册
     * */
    public static final String USER_ADD=Hot_GAME_URL+"user/add";

    /**
     * 资讯详情页
     */
    public static final String INFORMATION_CONTENT=Hot_GAME_URL+"news/webdetail/";

    /**
     * 签到信息
     */
    public static final String SIGN_MESSAGE=Hot_GAME_URL+"user/wallet/sign";

    /**
     * 签到
     */
    public static final String UP_SIGN_MESSAGE=Hot_GAME_URL+"user/wallet/up_sign";
    /**
     * 加密解密的 key
     */
    public static String appkey="62da94cb04d85549024f698e2022a736";
    //公共参数
    public static final String APP_ID="appid";
    public static final String CLIENT_ID="clientid";
    public static final String FROM="from";
    public static final String AGENT="agent";

    public static String appid="100";
    public static String agent="";
    public static String clientid="49";
    public static String from="3";


    public static Drawable drawable;


    public static final String DEFAULT_PARTNER = "2088512857502955";
    public static final String DEFAULT_SELLER = "yulangen@163.com";
    public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEA" +
            "AoGBAJVKXNeSIZZwftadNILX6henNWO6tbmuwuqvZGeEFrq0/igO2w4hCTFxZxwVuU1YBG3WBZC" +
            "gJC4uB9xNkI5GWZq+99xft7Z8DBR1kvQWHazAutI4APaZWr9o7lR431Potc4mcPqlJjOpQ78Y84" +
            "v04T/pjddImnavtO0oQCHS9FQ1AgMBAAECgYEAjSdTYyuzADf7ZVYcST53AshBLbtiiV8Ywqb1Kv" +
            "70MrJgwGVTbyeDNrF/iUls/BkljuhWOKmBq/wvyHe6HxubKcebula1vXyM2F/XXsZBz7xijTZby7" +
            "Ng9ynhyIiRk6DMQyLYeuBYCzqIwOBgC/ZdPgFlpjvWtCQkIsce4E5860ECQQDR0/eoPbVea5DGpZ" +
            "2MNC6ao5aeOcC6ZjvQKuPVZw8/QiCB4Huu3GymbjLbVDDjxi5dTGajTa4uT+mbQmlNckNlAkEAtiQx+s" +
            "v9j8I7E2xw6Sd1iciU4T4S3Gt7kwI37yKh6d1K8r5r3Q3cqlDNHn9B/Nx39ScDYalXp1tGzd4KKw0IkQ" +
            "JBALjrQr59q8KLr6qxuFgggNX9x7aoFSbxBFOgCOxAiFpqj7WGPdpMHmDKi31qBDAoryHAPFA9HH1qJem" +
            "TrLP5OFUCQEANdIIVEKNok6vd+8sSdFQy9KBNWwamybtGEXOIQ1Zh2wSIkJgZUtuWNLwf1o3c3laZFKhY" +
            "jBmvkLO+/OZs2/ECQHZiXNNNxK7MQzTinv/RH5FXnYJ0S6s0tVh30aTODzgorqkmdF6p5UHHlLuD2UjOMPfjbzfL18nsyS0tcKWKvhk=";


    /**
     * 权限常量
     */
    public static final int WRITE_READ_EXTERNAL_CODE=0x01;
}
