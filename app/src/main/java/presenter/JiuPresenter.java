package presenter;

import bean.FenLeiLeftBean;
import model.JiuModel;
import view.IJiuView;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class JiuPresenter implements IJiuPre {
    private IJiuView iShouyeView;
    private JiuModel jiuModel;
    public JiuPresenter(IJiuView iShouyeView){
        this.iShouyeView = iShouyeView;
        jiuModel = new JiuModel(this);
    }

    //获取数据
    public void getData(String url){
        jiuModel.getData(url);
    }

    @Override
    public void onSuccess(FenLeiLeftBean fenLeiBean) {
        iShouyeView.jiuSuccess(fenLeiBean);
    }

    @Override
    public void onFail() {

    }
}
