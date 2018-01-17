package utils;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class Api {
    //上传头像
    public static final String HEAD_API = "https://www.zhaoapi.cn/file/upload";
    //登录  请求参数  mobile  password
    public static final String LOGIN_API = "https://www.zhaoapi.cn/user/login";
    //注册  请求参数  mobile  password
    public static final String RES_API = "https://www.zhaoapi.cn/user/reg";
    //上传头像  请求参数 uid  file(文件)   路径中的file改为文件路径
    public static final String TOUXIANG_API = "https://www.zhaoapi.cn/file/upload";
    //首页广告（轮播图+京东秒杀+最底部的为你推荐）
    public static final String LUNBO_API = "https://www.zhaoapi.cn/ad/getAd";
    //商品分类接口（此接口用于首页九宫格，和底部页签分类页）
    public static final String FENLEI_API = "https://www.zhaoapi.cn/product/getCatagory";
    //商品子分类接口   请求参数 cid  分类右边
    public static final String ZIFENLEI_API = "https://www.zhaoapi.cn/product/getProductCatagory?cid=";
    //商品详情   请求参数 pid
    public static final String XIANGQING_API = "https://www.zhaoapi.cn/product/getProductDetail?pid=";
    //当前子分类下的商品列表（分页）   请求参数 pscid   page
    public static final String LIEBIAO_API = "https://www.zhaoapi.cn/product/getProducts";
    //根据关键词搜索商品   请求参数 keywords=笔记本   page
    public static final String SOUSUO_API = "https://www.zhaoapi.cn/product/searchProducts";
    //修改昵称   请求参数 uid   nickname
    public static final String XIUGAI_API = "https://www.zhaoapi.cn/user/updateNickName";
    //添加购物车   请求参数 Uid   Pid
    public static final String AddCart_API = "https://www.zhaoapi.cn/product/addCart";
    //查询购物车   请求参数 Uid
    public static final String SelectCart_API = "https://www.zhaoapi.cn/product/getCarts?uid=";
    //更新购物车   请求参数 uid=71 sellerid pid selected num   uid=71&sellerid=1&pid=1&selected=0&num=10
    public static final String UpDateCart_API = " https://www.zhaoapi.cn/product/updateCarts";
    //搜索商品
    public  static final String SEARCH_API = "https://www.zhaoapi.cn/product/searchProducts";
    //删除购物车
    public static final String DeleteCart_API = "https://www.zhaoapi.cn/product/deleteCart";
    //查询订单
    public static final String SelectDingDan_API = "https://www.zhaoapi.cn/product/getOrders?uid=";
    //修改订单
    public static final String UpDateDingDan_API = "https://www.zhaoapi.cn/product/updateOrder";
    //创建订单    ?uid=71&price=99.99
    public static final String CreateDingDan_API = "https://www.zhaoapi.cn/product/createOrder";
}
