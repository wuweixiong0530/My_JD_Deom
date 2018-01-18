package presenter;

import bean.CartBean;
import model.CartModel;
import view.ICartView;

/**
 * Created by Administrator on 2018/1/12,0012.
 */

public class CartPresenter implements ICartPre{

    private ICartView iCartView;
    private CartModel cartModel;

    public CartPresenter(ICartView iCartView){
        this.iCartView = iCartView;
        cartModel = new CartModel(this);
    }

    public void getData(String url){
        cartModel.GetData(url);
    }

    @Override
    public void onSuccess(CartBean cartBean) {
        iCartView.onSuccess(cartBean);
    }

    /**
     * 解决内存泄漏
     */
    public void destory() {
        if (iCartView!=null){
            iCartView=null;
        }
    }

}
