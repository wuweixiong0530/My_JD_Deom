package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bean.CartBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.ICartPre;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/12,0012.
 */

public class CartModel {
    private ICartPre iCartPre;

    public CartModel(ICartPre  iCartPre){
        this.iCartPre = iCartPre;
    }

    public void GetData(String url){
        OkHttp3Util.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    CartBean cartBean = new Gson().fromJson(string, CartBean.class);
                    iCartPre.onSuccess(cartBean);
                }
            }
        });
    }
}
