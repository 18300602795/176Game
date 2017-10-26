package com.i76game.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */

public class GiftBean {


    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":3,"gift_list":[{"giftid":10,"gameid":1,"giftname":"11111111","total":2,"remain":1,"content":"asasfasfasfasfasffs","icon":"http://www.etsdk.com1466530074.jpg","starttime":"2016-07-12 22:38:00","enttime":"2016-07-28 22:38:00","scope":""},{"giftid":9,"gameid":1,
     * "giftname":"新手礼包","total":9,"remain":6,"content":"擦 ","icon":"http://www.etsdk.com1466530074.jpg","starttime":"2016-06-21 01:25:00","enttime":"2016-06-30 01:25:00","scope":""},{"giftid":8,"gameid":2,"giftname":"礼包1共10个","total":11,"remain":9,"content":"222","icon":"http://www.etsdk
     * .com1466323415.png","starttime":"2016-06-08 00:16:00","enttime":"2016-06-30 00:16:00","scope":""}]}
     */

    private int code;
    private String msg;
    /**
     * count : 3
     * gift_list : [{"giftid":10,"gameid":1,"giftname":"11111111","total":2,"remain":1,"content":"asasfasfasfasfasffs","icon":"http://www.etsdk.com1466530074.jpg","starttime":"2016-07-12 22:38:00","enttime":"2016-07-28 22:38:00","scope":""},{"giftid":9,"gameid":1,"giftname":"新手礼包","total":9,
     * "remain":6,"content":"擦 ","icon":"http://www.etsdk.com1466530074.jpg","starttime":"2016-06-21 01:25:00","enttime":"2016-06-30 01:25:00","scope":""},{"giftid":8,"gameid":2,"giftname":"礼包1共10个","total":11,"remain":9,"content":"222","icon":"http://www.etsdk.com1466323415.png",
     * "starttime":"2016-06-08 00:16:00","enttime":"2016-06-30 00:16:00","scope":""}]
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
         * giftid : 10
         * gameid : 1
         * giftname : 11111111
         * total : 2
         * remain : 1
         * content : asasfasfasfasfasffs
         * icon : http://www.etsdk.com1466530074.jpg
         * starttime : 2016-07-12 22:38:00
         * enttime : 2016-07-28 22:38:00
         * scope :
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
            private long giftid;
            private long gameid;
            private int total;
            private int remain;
            private String giftname;
            private String content;
            private String icon;
            private String starttime;
            private String enttime;
            private String scope;

            public long getGiftid() {
                return giftid;
            }

            public void setGiftid(long giftid) {
                this.giftid = giftid;
            }

            public long getGameid() {
                return gameid;
            }

            public void setGameid(long gameid) {
                this.gameid = gameid;
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

            public String getGiftname() {
                return giftname;
            }

            public void setGiftname(String giftname) {
                this.giftname = giftname;
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
        }
    }
}
