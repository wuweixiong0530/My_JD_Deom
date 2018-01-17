package deom.jingdong.wwx.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.XiangQingBean;
import deom.jingdong.wwx.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.XiangQingPresenter;
import utils.Api;
import utils.OkHttp3Util;
import view.IXQView;

public class XiangQingActivity extends AppCompatActivity implements IXQView {

    private String pid;
    private XBanner xBanner;
    private TextView tv2;
    private TextView tv1;
    private TextView tv3;
    private XiangQingPresenter xiangQingPresenter;
    private List<String> imageList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_xiang_qing);

        findView();

        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        Log.i("xiangqingpid", pid);
        xiangQingPresenter = new XiangQingPresenter(this);
        xiangQingPresenter.getData(Api.XIANGQING_API + pid);
    }

    private void findView() {
        xBanner = findViewById(R.id.xq_xbanner);
        tv1 = findViewById(R.id.xq_tv1);
        tv2 = findViewById(R.id.xq_tv2);
        tv3 = findViewById(R.id.xq_tv3);
    }

    //点击返回
    public void xq_back(View view) {
        finish();
    }

    /**
     * 重写的方法
     *
     * @param xiangQingBean
     */
    @Override
    public void onSuccess(final XiangQingBean xiangQingBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageList = new ArrayList<>();

                String images = xiangQingBean.getData().getImages();
                String[] splits = images.split("\\|");//得到图片并分割
                //图片循环添加进集合
                for (String string : splits) {
                    imageList.add(string);
                }
                xBanner.setData(imageList, null);
                xBanner.setPoinstPosition(XBanner.BOTTOM);
                xBanner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, Object model, View view, int position) {
                        Glide.with(XiangQingActivity.this).load(imageList.get(position)).into((ImageView) view);
                    }
                });
                //点击xBanner
                xBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                    @Override
                    public void onItemClick(XBanner banner, int position) {
                        Intent intent = new Intent(XiangQingActivity.this,ImageActivity.class);
                        intent.putExtra("position",imageList.get(position));
                        startActivity(intent);
                    }
                });

                tv1.setText(xiangQingBean.getData().getTitle());
                tv2.setText("原价" + xiangQingBean.getData().getBargainPrice());
                tv3.setText("现价" + xiangQingBean.getData().getPrice());
                tv2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

            }
        });
    }

    /**
     * 加入购物车
     *
     * @param view
     */
    public void btn_addCard(View view) {
        sharedPreferences = getSharedPreferences("logins", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        String uid = sharedPreferences.getString("uid", "");
        Log.i("xiangqing", "uid+" + uid);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);//先传一个固定的id  看看对不对
        map.put("pid", pid);
        OkHttp3Util.doPost(Api.AddCart_API, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                String string = response.body().string();
                                JSONObject jsonObject = new JSONObject(string);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {//如果加入购物车成功，
                                    Toast.makeText(XiangQingActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(XiangQingActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });
    }
    //购物车
    public void btn_Card(View view) {
       Intent intent = new Intent(XiangQingActivity.this,GouWuCheActivity.class);
       startActivity(intent);
    }
}
