package presenter;

import bean.LoginBean;
import model.LoginModel;
import view.ILoingView;

/**
 * Created by Administrator on 2018/1/5,0005.
 */

public class LoginPresenter implements ILoingPre {
    private ILoingView iLoginView;
    private LoginModel loginModel;

    public LoginPresenter(ILoingView iLoginView){
        this.iLoginView = iLoginView;
        loginModel = new LoginModel(this);
    }
    public void getData(String url,String mobile,String password){
        loginModel.getData(url,mobile,password);
    }
    @Override
    public void onSuccess(LoginBean loginBean) {
        iLoginView.onSuccess(loginBean);
    }
}
