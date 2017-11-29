package com.i76game.bean;

import java.util.List;

/**
 * 游戏详情的实体
 */

public class GameContentBean {

        /**
         * code : 200
         * msg : 请求成功
         * data : {"gameid":2,"icon":"http://demo.1tsdk.com","gamename":"我叫MT","type":"2","runtime":1466066755,"category":1,"hot":1,"downcnt":74,"score":10,"distype":1,"discount":0,"rebate":0,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"FASDF","size":"125M","image":["http://demo.1tsdk
         * .com/upload/20160905/57cd1a2b3429d.jpg","http://demo.1tsdk.com/upload/20160905/57cd1a30a239b.jpg"],"lang":"中文","sys":"",
         * "disc":"2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015
         * 年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！","verid":2,"vername":"5.1","giftcnt":0}
         */

        private int code;
        private List<GameListBean> msg;
        /**
         * gameid : 2
         * icon : http://demo.1tsdk.com
         * gamename : 我叫MT
         * type : 2
         * runtime : 1466066755
         * category : 1
         * hot : 1
         * downcnt : 74
         * score : 10
         * distype : 1
         * discount : 0
         * rebate : 0
         * likecnt : 0
         * sharecnt : 0
         * downlink :
         * oneword : FASDF
         * size : 125M
         * image : ["http://demo.1tsdk.com/upload/20160905/57cd1a2b3429d.jpg","http://demo.1tsdk.com/upload/20160905/57cd1a30a239b.jpg"]
         * lang : 中文
         * sys :
         * disc : 2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！
         * verid : 2
         * vername : 5.1
         * giftcnt : 0
         */

        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<GameListBean> getMsg() {
            return msg;
        }

        public void setMsg(List<GameListBean> msg) {
            this.msg = msg;
        }

        public DataBean getData() {
            return data;
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

        public Integer getGameid() {
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


        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private Integer gameid;
            private String icon;
            private String gamename;
            private String type;
            private long runtime;
            private long category;
            private long hot;
            private long downcnt;
            private long score;
            private long distype;
            private long discount;
            private long rebate;
            private long likecnt;
            private long sharecnt;
            private String downlink;
            private String oneword;
            private String size;
            private String lang;
            private String sys;
            private String disc;
            private long verid;

            @Override
            public String toString() {
                return "DataBean{" +
                        "gameid=" + gameid +
                        ", icon='" + icon + '\'' +
                        ", gamename='" + gamename + '\'' +
                        ", type='" + type + '\'' +
                        ", runtime=" + runtime +
                        ", category=" + category +
                        ", hot=" + hot +
                        ", downcnt=" + downcnt +
                        ", score=" + score +
                        ", distype=" + distype +
                        ", discount=" + discount +
                        ", rebate=" + rebate +
                        ", likecnt=" + likecnt +
                        ", sharecnt=" + sharecnt +
                        ", downlink='" + downlink + '\'' +
                        ", oneword='" + oneword + '\'' +
                        ", size='" + size + '\'' +
                        ", lang='" + lang + '\'' +
                        ", sys='" + sys + '\'' +
                        ", disc='" + disc + '\'' +
                        ", verid=" + verid +
                        ", vername='" + vername + '\'' +
                        ", giftcnt=" + giftcnt +
                        ", image=" + image +
                        '}';
            }

            private String vername;
            private long giftcnt;
            private List<String> image;

            public Integer getGameid() {
                return gameid;
            }

            public void setGameid(Integer gameid) {
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

            public long getDiscount() {
                return discount;
            }

            public void setDiscount(long discount) {
                this.discount = discount;
            }

            public long getRebate() {
                return rebate;
            }

            public void setRebate(long rebate) {
                this.rebate = rebate;
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

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public String getSys() {
                return sys;
            }

            public void setSys(String sys) {
                this.sys = sys;
            }

            public String getDisc() {
                return disc;
            }

            public void setDisc(String disc) {
                this.disc = disc;
            }

            public long getVerid() {
                return verid;
            }

            public void setVerid(long verid) {
                this.verid = verid;
            }

            public String getVername() {
                return vername;
            }

            public void setVername(String vername) {
                this.vername = vername;
            }

            public long getGiftcnt() {
                return giftcnt;
            }

            public void setGiftcnt(long giftcnt) {
                this.giftcnt = giftcnt;
            }

            public List<String> getImage() {
                return image;
            }

            public void setImage(List<String> image) {
                this.image = image;
            }
        }


}
