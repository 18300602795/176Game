package com.i76game.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class UserGiftCodeBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":4,"gift_list":[{"giftid":15,"gameid":1,"giftname":"yuyuyuyu","total":1,"remain":0,"content":"yuyuyuyu","icon":"http://demo.1tsdk.com/upload/20160905/57cd4461bf416.jpg","starttime":"2016-09-07 20:55:00","enttime":"2016-09-10 20:55:00","scope":"","code":"yuyuyuyu"}]}
     */

    private int code;
    private String msg;
    /**
     * count : 4
     * gift_list : [{"giftid":15,"gameid":1,"giftname":"yuyuyuyu","total":1,"remain":0,"content":"yuyuyuyu","icon":"http://demo.1tsdk.com/upload/20160905/57cd4461bf416.jpg","starttime":"2016-09-07 20:55:00","enttime":"2016-09-10 20:55:00","scope":"","code":"yuyuyuyu"}]
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
         * giftid : 15
         * gameid : 1
         * giftname : yuyuyuyu
         * total : 1
         * remain : 0
         * content : yuyuyuyu
         * icon : http://demo.1tsdk.com/upload/20160905/57cd4461bf416.jpg
         * starttime : 2016-09-07 20:55:00
         * enttime : 2016-09-10 20:55:00
         * scope :
         * code : yuyuyuyu
         */

        private List<GiftListBean> gift_list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GiftListBean> getGift_list() {
            return gift_list;
        }

        public void setGift_list(List<GiftListBean> gift_list) {
            this.gift_list = gift_list;
        }

        public static class GiftListBean {
            private int giftid;
            private int gameid;
            private String giftname;
            private int total;
            private int remain;
            private String content;
            private String icon;
            private String starttime;
            private String enttime;
            private String scope;
            private String code;

            public int getGiftid() {
                return giftid;
            }

            public void setGiftid(int giftid) {
                this.giftid = giftid;
            }

            public int getGameid() {
                return gameid;
            }

            public void setGameid(int gameid) {
                this.gameid = gameid;
            }

            public String getGiftname() {
                return giftname;
            }

            public void setGiftname(String giftname) {
                this.giftname = giftname;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getRemain() {
                return remain;
            }

            public void setRemain(int remain) {
                this.remain = remain;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEnttime() {
                return enttime;
            }

            public void setEnttime(String enttime) {
                this.enttime = enttime;
            }

            public String getScope() {
                return scope;
            }

            public void setScope(String scope) {
                this.scope = scope;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            @Override
            public String toString() {
                return "GiftListBean{" +
                        "giftid=" + giftid +
                        ", gameid=" + gameid +
                        ", giftname='" + giftname + '\'' +
                        ", total=" + total +
                        ", remain=" + remain +
                        ", content='" + content + '\'' +
                        ", icon='" + icon + '\'' +
                        ", starttime='" + starttime + '\'' +
                        ", enttime='" + enttime + '\'' +
                        ", scope='" + scope + '\'' +
                        ", code='" + code + '\'' +
                        '}';
            }
        }
    }
}
