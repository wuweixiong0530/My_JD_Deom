package model;

import com.google.gson.Gson;

import java.io.IOException;

import bean.LunBoBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.IMiaoShaPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/3,0003.
 */

public class MiaoShaModel {
    private IMiaoShaPre iMiaoShaPre;

    public MiaoShaModel(IMiaoShaPre iMiaoShaPre){
        this.iMiaoShaPre = iMiaoShaPre;
    }
    //获取数据
    public void getData(String url){
        OkHttp3Util.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    if (string != null){
                        LunBoBean lunBoBean = new Gson().fromJson(string, LunBoBean.class);
                        iMiaoShaPre.miaoShaOnSuccess(lunBoBean);
                    }
                }
            }
        });
    }
}
