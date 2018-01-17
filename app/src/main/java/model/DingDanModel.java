package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bean.DingDanBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.IDingDanPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/15,0015.
 */

public class DingDanModel {
    private IDingDanPre iDingDanPre;

    public DingDanModel(IDingDanPre iDingDanPre){
        this.iDingDanPre = iDingDanPre;
    }

    public void getData(String url,String uid){
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("page","1");
        OkHttp3Util.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    DingDanBean dingDanBean = new Gson().fromJson(string, DingDanBean.class);
                    iDingDanPre.onSuccess(dingDanBean);
                }
            }
        });
    }
}
