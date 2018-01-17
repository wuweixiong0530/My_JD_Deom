package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bean.ResBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.IResPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/5,0005.
 */

public class ResModel {
    private IResPre iResPre;

    public ResModel(IResPre iResPre){
        this.iResPre = iResPre;
    }

    public void getData(String url, String mobile, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("password",password);
        map.put("source","android");
        OkHttp3Util.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    ResBean resBean = new Gson().fromJson(string, ResBean.class);
                    iResPre.onSuccess(resBean);
                }
            }
        });
    }
}
