package presenter;

import bean.DingDanBean;
import model.DingDanModel;
import view.IDingDanView;

/**
 * Created by Administrator on 2018/1/15,0015.
 */

public class DingDanPresenter implements IDingDanPre {

    private IDingDanView iDingDanView;
    private DingDanModel dingDanModel;

    public DingDanPresenter(IDingDanView iDingDanView){
        this.iDingDanView = iDingDanView;
        dingDanModel = new DingDanModel(this);
    }

    public void getData(String url,String uid){
        dingDanModel.getData(url,uid);
    }

    @Override
    public void onSuccess(DingDanBean dingDanBean) {
        iDingDanView.onSuccess(dingDanBean);
    }
}
