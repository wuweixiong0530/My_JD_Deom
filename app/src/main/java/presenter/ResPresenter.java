package presenter;

import bean.ResBean;
import model.ResModel;
import view.IResView;

/**
 * Created by Administrator on 2018/1/5,0005.
 */

public class ResPresenter implements IResPre {
    private IResView iResView;
    private ResModel resModel;

    public ResPresenter(IResView iResView){
        this.iResView = iResView;
        resModel = new ResModel(this);
    }
    public void getData(String url,String mobile,String password){
        resModel.getData(url,mobile,password);
    }
    @Override
    public void onSuccess(ResBean resBean) {
        iResView.onSuccess(resBean);
    }
}
