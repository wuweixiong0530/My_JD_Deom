package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bean.XiangQingBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.IXQPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/10,0010.
 */

public class XiangQingModel {
    private IXQPre ixqPre;

    public XiangQingModel(IXQPre ixqPre){
        this.ixqPre = ixqPre;
    }
    public void getData(String url){
        OkHttp3Util.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    XiangQingBean xiangQingBean = new Gson().fromJson(string, XiangQingBean.class);
                    ixqPre.onSuccess(xiangQingBean);
                }
            }
        });
    }
}
