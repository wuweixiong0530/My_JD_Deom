package presenter;

import bean.XiangQingBean;
import model.XiangQingModel;
import view.IXQView;

/**
 * Created by Administrator on 2018/1/10,0010.
 */

public class XiangQingPresenter implements IXQPre {
    private IXQView ixqView;
    private final XiangQingModel xiangQingModel;

    public XiangQingPresenter(IXQView ixqView){
        this.ixqView = ixqView;
        xiangQingModel = new XiangQingModel(this);
    }

    public void getData(String url){
        xiangQingModel.getData(url);
    }

    @Override
    public void onSuccess(XiangQingBean xiangQingBean) {
        ixqView.onSuccess(xiangQingBean);
    }
}
