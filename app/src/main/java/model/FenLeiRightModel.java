package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bean.FenLeiRightBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.IFLRightPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/9,0009.
 */

public class FenLeiRightModel {
    private IFLRightPre iflRightPre;

    public FenLeiRightModel(IFLRightPre iflRightPre){
        this.iflRightPre = iflRightPre;
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
                    FenLeiRightBean fenLeiRightBean = new Gson().fromJson(string, FenLeiRightBean.class);
                    iflRightPre.onSuccess(fenLeiRightBean);
                }
            }
        });
    }
}
