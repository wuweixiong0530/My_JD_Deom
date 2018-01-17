package presenter;

import bean.FenLeiRightBean;
import model.FenLeiRightModel;
import view.IFLRightView;

/**
 * Created by Administrator on 2018/1/9,0009.
 */

public class FenLeiRightPresenter implements IFLRightPre {
    private IFLRightView iflRightView;
    private FenLeiRightModel fenLeiRightModel;

    public FenLeiRightPresenter(IFLRightView iflRightView){
        this.iflRightView = iflRightView;
        fenLeiRightModel = new FenLeiRightModel(this);
    }

    public void getData(String url){
        fenLeiRightModel.getData(url);
    }

    @Override
    public void onSuccess(FenLeiRightBean fenLeiRightBean) {
        iflRightView.onSuccess(fenLeiRightBean);
    }
}
