package bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15,0015.
 */

public class DingDanBean {

    /**
     * msg : 请求成功
     * code : 0
     * data : [{"createtime":"2017-12-21T10:53:38","orderid":4954,"price":22.9,"status":2,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T10:53:40","orderid":4955,"price":22.9,"status":2,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T10:57:44","orderid":4962,"price":246.88,"status":1,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:03:57","orderid":4972,"price":223.98,"status":2,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:16:06","orderid":4994,"price":223.98,"status":0,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:16:14","orderid":4995,"price":223.98,"status":0,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:16:16","orderid":4996,"price":223.98,"status":0,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:18:20","orderid":4999,"price":223.98,"status":0,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:18:23","orderid":5000,"price":223.98,"status":0,"title":"订单测试标题2845","uid":2845},{"createtime":"2017-12-21T11:34:15","orderid":5029,"price":223.98,"status":2,"title":"订单测试标题2845","uid":2845}]
     * page : 1
     */

    private String msg;
    private String code;
    private String page;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createtime : 2017-12-21T10:53:38
         * orderid : 4954
         * price : 22.9
         * status : 2
         * title : 订单测试标题2845
         * uid : 2845
         */

        private String createtime;
        private int orderid;
        private double price;
        private int status;
        private String title;
        private int uid;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
