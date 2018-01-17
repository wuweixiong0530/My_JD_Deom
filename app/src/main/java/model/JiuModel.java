package model;

import com.google.gson.Gson;

import java.io.IOException;

import bean.FenLeiLeftBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.IJiuPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class JiuModel {
    IJiuPre iJiuPre;
    public JiuModel(IJiuPre iJiuPre){
        this.iJiuPre = iJiuPre;
    }
    //获取数据的方法
    public void getData(String url){
        OkHttp3Util.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    Gson gson = new Gson();
                    FenLeiLeftBean fenLeiBean = gson.fromJson(string, FenLeiLeftBean.class);
                    iJiuPre.onSuccess(fenLeiBean);
                }
            }
        });
    }
}
