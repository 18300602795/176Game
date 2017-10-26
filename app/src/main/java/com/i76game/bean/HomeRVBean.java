package com.i76game.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class HomeRVBean {
    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":5,"game_list":[{"gameid":1,"icon":null,"gamename":"demo","type":1,"runtime":1467733945,"category":2,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""},{"gameid":2,"icon":"http://www.etsdk
     * .com1466323415.png","gamename":"我叫MT","type":2,"runtime":1466066755,"category":1,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""},{"gameid":3,"icon":"http://www.etsdk.com1466323391.jpg","gamename":"天龙八部",
     * "type":1,"runtime":1466068349,"category":1,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"http://gdl.25pp.com/wm/12/12/p_3002_UC_2.00.003_V3.5.3.1_2010071003_Release_614892_21234790a9c7.apk?cc=868334420&amp;
     * vh=4c2ad7efb9c857580c2b032d763b064c&amp;sf=193976200","oneword":"《天龙八部》原版人马打造，金庸正版授权的一款3D武侠MMORPG手游。","size":"178.3M"},{"gameid":4,"icon":null,"gamename":"管理员测试","type":3,"runtime":1466085979,"category":1,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,
     * "sharecnt":0,"downlink":null,"oneword":null,"size":null},{"gameid":5,"icon":null,"gamename":"神武赵子龙","type":4,"runtime":1467295022,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"http://sdk.huosdk
     * .com/download/sdkgame/swzzl_5/swzzl_5.apk","oneword":"2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！","size":""},{"gameid":6,"icon":null,"gamename":"野猪冰冻流","type":5,"runtime":1466320006,"category":2,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,
     * "downlink":null,"oneword":null,"size":null},{"gameid":7,"icon":null,"gamename":"斗罗大陆","type":6,"runtime":0,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":null,"oneword":null,"size":null},{"gameid":8,"icon":null,
     * "gamename":"再来一瓶","type":7,"runtime":1466359483,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""},{"gameid":9,"icon":null,"gamename":"无边战火","type":2,"runtime":0,"category":2,"hot":2,
     * "downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""}]}
     */

    private int code;
    private String msg;
    /**
     * count : 5
     * game_list : [{"gameid":1,"icon":null,"gamename":"demo","type":1,"runtime":1467733945,"category":2,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""},{"gameid":2,"icon":"http://www.etsdk.com1466323415.png",
     * "gamename":"我叫MT","type":2,"runtime":1466066755,"category":1,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""},{"gameid":3,"icon":"http://www.etsdk.com1466323391.jpg","gamename":"天龙八部","type":1,
     * "runtime":1466068349,"category":1,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"http://gdl.25pp.com/wm/12/12/p_3002_UC_2.00.003_V3.5.3.1_2010071003_Release_614892_21234790a9c7.apk?cc=868334420&amp;
     * vh=4c2ad7efb9c857580c2b032d763b064c&amp;sf=193976200","oneword":"《天龙八部》原版人马打造，金庸正版授权的一款3D武侠MMORPG手游。","size":"178.3M"},{"gameid":4,"icon":null,"gamename":"管理员测试","type":3,"runtime":1466085979,"category":1,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,
     * "sharecnt":0,"downlink":null,"oneword":null,"size":null},{"gameid":5,"icon":null,"gamename":"神武赵子龙","type":4,"runtime":1467295022,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"http://sdk.huosdk
     * .com/download/sdkgame/swzzl_5/swzzl_5.apk","oneword":"2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！","size":""},{"gameid":6,"icon":null,"gamename":"野猪冰冻流","type":5,"runtime":1466320006,"category":2,"hot":1,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,
     * "downlink":null,"oneword":null,"size":null},{"gameid":7,"icon":null,"gamename":"斗罗大陆","type":6,"runtime":0,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":null,"oneword":null,"size":null},{"gameid":8,"icon":null,
     * "gamename":"再来一瓶","type":7,"runtime":1466359483,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""},{"gameid":9,"icon":null,"gamename":"无边战火","type":2,"runtime":0,"category":2,"hot":2,
     * "downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""}]
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        /**
         * gameid : 1
         * icon : null
         * gamename : demo
         * type : 1
         * runtime : 1467733945
         * category : 2
         * hot : 1
         * downcnt : 0
         * score : 0
         * distype : 0
         * discount : null
         * rebate : null
         * likecnt : 0
         * sharecnt : 0
         * downlink :
         * oneword :
         * size :
         */

        private List<GameListBean> game_list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GameListBean> getGame_list() {
            return game_list;
        }

        public void setGame_list(List<GameListBean> game_list) {
            this.game_list = game_list;
        }

        public static class GameListBean {
            private int gameid;
            private String icon;
            private String gamename;
            private String type;
            private long runtime;
            private long category;
            private long hot;
            private long downcnt;
            private long score;
            private long distype;
            private long likecnt;
            private long sharecnt;
            private Object discount;
            private Object rebate;

            private String downlink;
            private String oneword;
            private String size;

            public int getGameid() {
                return gameid;
            }

            public void setGameid(int gameid) {
                this.gameid = gameid;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getGamename() {
                return gamename;
            }

            public void setGamename(String gamename) {
                this.gamename = gamename;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public long getRuntime() {
                return runtime;
            }

            public void setRuntime(long runtime) {
                this.runtime = runtime;
            }

            public long getCategory() {
                return category;
            }

            public void setCategory(long category) {
                this.category = category;
            }

            public long getHot() {
                return hot;
            }

            public void setHot(long hot) {
                this.hot = hot;
            }

            public long getDowncnt() {
                return downcnt;
            }

            public void setDowncnt(long downcnt) {
                this.downcnt = downcnt;
            }

            public long getScore() {
                return score;
            }

            public void setScore(long score) {
                this.score = score;
            }

            public long getDistype() {
                return distype;
            }

            public void setDistype(long distype) {
                this.distype = distype;
            }

            public long getLikecnt() {
                return likecnt;
            }

            public void setLikecnt(long likecnt) {
                this.likecnt = likecnt;
            }

            public long getSharecnt() {
                return sharecnt;
            }

            public void setSharecnt(long sharecnt) {
                this.sharecnt = sharecnt;
            }

            public Object getDiscount() {
                return discount;
            }

            public void setDiscount(Object discount) {
                this.discount = discount;
            }

            public Object getRebate() {
                return rebate;
            }

            public void setRebate(Object rebate) {
                this.rebate = rebate;
            }

            public String getDownlink() {
                return downlink;
            }

            public void setDownlink(String downlink) {
                this.downlink = downlink;
            }

            public String getOneword() {
                return oneword;
            }

            public void setOneword(String oneword) {
                this.oneword = oneword;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }
    }

}
