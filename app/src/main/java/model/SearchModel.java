package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bean.SearchBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.ISearchPre;
import utils.OkHttp3Util;
 /**
 * Created by Administrator on 2018/1/5,0005.
 */

public class SearchModel {
    private ISearchPre iSearchPre;

    public SearchModel(ISearchPre iSearchPre){
        this.iSearchPre = iSearchPre;
    }

    public void getData(String url,String keywords){
        Map<String,String> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("page","1");
        map.put("source","android");
        OkHttp3Util.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    SearchBean dataDataBean = new Gson().fromJson(string, SearchBean.class);
                    iSearchPre.onSuccess(dataDataBean);
                }
            }
        });
    }
}
