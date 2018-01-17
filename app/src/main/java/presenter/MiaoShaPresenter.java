package presenter;

import bean.LunBoBean;
import model.MiaoShaModel;
import view.IMiaoShaView;

/**
 * Created by Administrator on 2018/1/3,0003.
 */

public class MiaoShaPresenter implements IMiaoShaPre {

    private IMiaoShaView iMiaoShaView;
    private final MiaoShaModel miaoShaModel;

    public MiaoShaPresenter(IMiaoShaView iMiaoShaView){
        this.iMiaoShaView = iMiaoShaView;
        miaoShaModel = new MiaoShaModel(this);
    }

    public void getData(String url){
        miaoShaModel.getData(url);
    }

    @Override
    public void miaoShaOnSuccess(LunBoBean lunBoBean) {
        iMiaoShaView.miaoShaOnSuccess(lunBoBean);
    }
}
