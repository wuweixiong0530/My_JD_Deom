package deom.jingdong.wwx.activity.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CartAdapter;
import adapter.TuiJianAdapter;
import bean.CartBean;
import bean.CountPriceBean;
import bean.CreatDingDanBean;
import bean.LunBoBean;
import deom.jingdong.wwx.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.CartPresenter;
import presenter.MiaoShaPresenter;
import utils.Api;
import utils.CartExpanableListview;
import utils.OkHttp3Util;
import view.ICartView;
import view.IMiaoShaView;

public class GouWuCheActivity extends AppCompatActivity  implements ICartView, IMiaoShaView {

    private CartExpanableListview expand_listView;
    private RelativeLayout relative_progress;
    private CheckBox check_all;
    private TextView text_total;
    private TextView text_buy;
    private CartPresenter cartPresenter;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String uid;
    private CartAdapter cartAdapter;
    private CountPriceBean countPriceBean;
    private int count;
    private String priceString;
    private CartBean cartBean;
    private RecyclerView weinituijian;
    private List<LunBoBean.TuijianBean.ListBean> tuijianList;
    private TuiJianAdapter tuiJianAdapter;
    private MiaoShaPresenter miaoShaPresenter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                CountPriceBean countPriceBean = (CountPriceBean) msg.obj;
                Log.i("hand", countPriceBean.getPriceString());
                //设置价格和数量
                text_total.setText("合计:¥" + countPriceBean.getPriceString());
                text_buy.setText("去结算(" + countPriceBean.getCount() + ")");
                //得到价钱和数量
                priceString = countPriceBean.getPriceString();
                count = countPriceBean.getCount();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gou_wu_che);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        findView();
        getUidFromShared();
        cartPresenter = new CartPresenter(this);
    }

    /**
     * 得到存入到SD卡的UID
     */
    private void getUidFromShared() {
        sharedPreferences = getSharedPreferences("logins", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private void findView() {
        check_all = findViewById(R.id.check_all);

        expand_listView = findViewById(R.id.expand_listView);
        relative_progress = findViewById(R.id.relative_progress);

        text_total = findViewById(R.id.text_total);
        text_buy = findViewById(R.id.text_buy);

        weinituijian = findViewById(R.id.weinituijian);

        //check_all.setOnClickListener(this);
        text_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("prices", priceString);
                //通过判断价钱是否一样进行跳转
                double price = 0;
                for (int i = 0; i < cartBean.getData().size(); i++) {
                    List<CartBean.DataBean.ListBean> listBeans = cartBean.getData().get(i).getList();
                    for (int j = 0; j < listBeans.size(); j++) {

                        if (listBeans.get(j).getSelected() == 1) {
                            price += listBeans.get(j).getBargainPrice() * listBeans.get(j).getNum();
                        }
                    }
                }

                //double高精度,,,计算的时候可能会出现一串数字...保留两位
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                priceString = decimalFormat.format(price);
                tiaoZhuanDingDan();
            }
        });
    }

    private void tiaoZhuanDingDan() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("price", priceString);
        OkHttp3Util.doPost(Api.CreateDingDan_API, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String string = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CreatDingDanBean creatDingDanBean = new Gson().fromJson(string, CreatDingDanBean.class);
                            if (creatDingDanBean.getCode().equals("0")) {
                                Toast.makeText(GouWuCheActivity.this, creatDingDanBean.getMsg(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(GouWuCheActivity.this, DingDanActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(GouWuCheActivity.this, creatDingDanBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        miaoShaPresenter = new MiaoShaPresenter(this);
        miaoShaPresenter.getData(Api.LUNBO_API);

        //去掉默认指示器
        expand_listView.setGroupIndicator(null);
        //progressBar显示
        relative_progress.setVisibility(View.VISIBLE);
        uid = sharedPreferences.getString("uid", "");
        Log.i("gouwu的UID::", uid);
        cartPresenter.getData(Api.SelectCart_API + uid);
        check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartAdapter.setAllChildsChecked(check_all.isChecked());

            }
        });


    }

    /**
     * 重写的购物车的方法
     *
     * @param cartBean
     */
    @Override
    public void onSuccess(final CartBean cartBean) {
        this.cartBean = cartBean;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取数据成功...隐藏
                relative_progress.setVisibility(View.GONE);
//                Log.i("gouwulist",cartBean.toString());
                if (cartBean != null) {
                    for (int i = 0; i < cartBean.getData().size(); i++) {
                        //当前组中所有孩子的数据
                        List<CartBean.DataBean.ListBean> listBeans = cartBean.getData().get(i).getList();
                        //设置组的初始选中状态,,,,根据所有孩子的状态
                        cartBean.getData().get(i).setGroupChecked(isAllChildInGroupChecked(listBeans));
                    }

                    //2.根据所有商家选中的状态,改变全选的状态
                    check_all.setChecked(isAllGroupChecked(cartBean));

                    cartAdapter = new CartAdapter(GouWuCheActivity.this, cartBean, handler, relative_progress, cartPresenter, uid);
                    expand_listView.setAdapter(cartAdapter);

                    //展开所有的组...expanableListView.expandGroup()
                    for (int i = 0; i < cartBean.getData().size(); i++) {
                        expand_listView.expandGroup(i);
                    }

                    //3.计算总价和商品的数量
                    cartAdapter.sendPriceAndCount();
                } else {
                    Toast.makeText(GouWuCheActivity.this, "购物车是空的", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /**
     * 所有的商家是否选中
     *
     * @param cartBean
     * @return
     */
    private boolean isAllGroupChecked(CartBean cartBean) {

        for (int i = 0; i < cartBean.getData().size(); i++) {
            //只要有一个组未选中 返回false
            if (!cartBean.getData().get(i).isGroupChecked()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 当前组中所有的孩子是否选中
     *
     * @param listBeans 当前组中所有的孩子的数据
     * @return
     */
    private boolean isAllChildInGroupChecked(List<CartBean.DataBean.ListBean> listBeans) {

        for (int i = 0; i < listBeans.size(); i++) {
            if (listBeans.get(i).getSelected() == 0) {
                return false;
            }
        }

        return true;
    }

//    //点击全选
//    @Override
//    public void onClick(View v) {
//        switch (view.getId()) {
//            case R.id.check_all:
//                Log.d("---------","qqqqqqqqqq");
//                //根据全选的状态更新所有商品的状态...check_all.isChecked() true...1;;;;false---0
//                break;
//            default:
//                break;
//
//        }
//    }

    /**
     * 为你推荐
     * @param lunBoBean
     */
    @Override
    public void miaoShaOnSuccess(final LunBoBean lunBoBean) {
       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tuijianList = lunBoBean.getTuijian().getList();
                tuiJianAdapter = new TuiJianAdapter(GouWuCheActivity.this, tuijianList);
                weinituijian.setAdapter(tuiJianAdapter);
                weinituijian.setLayoutManager(new GridLayoutManager(GouWuCheActivity.this,2, OrientationHelper.VERTICAL,false));
                tuiJianAdapter.setOnItemClick(new TuiJianAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(GouWuCheActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cartPresenter != null) {
            cartPresenter.destory();
        }
    }

}
