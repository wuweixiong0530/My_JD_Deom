package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.DingDanAdapter;
import bean.DingDanBean;
import deom.jingdong.wwx.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.DingDanPresenter;
import utils.Api;
import utils.CommonUtils;
import utils.OkHttp3Util;
import view.IDingDanView;

/**
 * Created by Administrator on 2018/1/15,0015.
 */

public class DingDanFragment extends Fragment{
    private SharedPreferences sharedPreferences;

    DingDanBean dingDanBean = new DingDanBean();
    private View view;
    private ListView listView;
    private String name;
    DingDanAdapter adapter;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                if ("待付款".equals(name)) {
                    //Log.d("---",name);
                    dingdanList.clear();
                    dingdanList.addAll(dingDanBean.getData());
                    if (dingdanList.size() > 0) {
                        setAdapter();
                    } else {
                        Toast.makeText(getActivity(), "没有此类型的商品订单哦", Toast.LENGTH_SHORT).show();
                    }

                }else if ("已完成".equals(name)) {
                        Log.d("----", name);
                        if (dingdanList.size() > 0) {
                            dingdanList.clear();
                            for (int i = 0; i < dingdanList.size(); i++) {
                                if (dingdanList.get(i).getStatus() == 2) {
                                    dingdanList.add(dingdanList.get(i));
                                }

                            }
                            if (dingdanList.size() > 0) {
                                setAdapter();
                            } else {
                                Toast.makeText(getActivity(), "没有此类型的商品订单哦", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            add();
                        }

                }else if ("已取消".equals(name)) {
                    Log.d("----", name);
                    if (dingdanList.size() > 0) {
                        dingdanList.clear();
                        for (int i = 0; i < dingdanList.size(); i++) {
                            if (dingdanList.get(i).getStatus() == 2) {
                                dingdanList.add(dingdanList.get(i));
                            }

                        }
                        if (dingdanList.size() > 0) {
                            setAdapter();
                        } else {
                            Toast.makeText(getActivity(), "没有此类型的商品订单哦", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        add();
                    }

                }
            }else if (msg.what==2){
                add();
                setAdapter();
            }
        }
    };



    private List<DingDanBean.DataBean> dingdanList = new ArrayList<>();
    private DingDanPresenter dingDanPresenter;
    private String uid;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragmentdingdan_layout,null);
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("logins", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        Log.i("dingdna--",uid);
//        dingDanPresenter = new DingDanPresenter(this);
//        dingDanPresenter.getData(Api.SelectDingDan_API,uid);

        name = getArguments().getString("name", "");
        Log.d("nnnn", name);
        if (name.equals("待付款")){
            add();
        }else if (name.equals("已完成")){
            handler.sendEmptyMessage(1);
        }else if (name.equals("已取消")){
            handler.sendEmptyMessage(1);
        }

    }

    private void setAdapter() {
        if (adapter==null){
            adapter = new DingDanAdapter(getActivity(),dingdanList,handler,uid);
            listView.setAdapter(adapter);

        }else
        {
            adapter.notifyDataSetChanged();
        }
    }

    private void add(){
//        Api.SelectDingDan_API+uid+"&"+page
        page++;
        OkHttp3Util.doGet(Api.SelectDingDan_API+uid+"&"+page, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String string = response.body().string();
                if (response.isSuccessful()){

                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson=new Gson();
                            dingDanBean = gson.fromJson(string, DingDanBean.class);
                            Log.i("------da",dingDanBean.toString());
//                            dataBeanList.clear();
                            dingdanList.addAll(dingDanBean.getData());
                            Log.i("------dal",dingdanList.toString());
                            handler.sendEmptyMessage(1);

                        }
                    });

                }

            }
        });
    }
}
