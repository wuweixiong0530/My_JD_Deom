package presenter;

import bean.SearchBean;
import model.SearchModel;
import view.ISearchView;

/**
 * Created by Administrator on 2018/1/5,0005.
 */

public class SearchPresenter implements ISearchPre {

    private ISearchView iSearchView;
    private final SearchModel searchModel;

    public SearchPresenter(ISearchView iSearchView){
        this.iSearchView = iSearchView;
        searchModel = new SearchModel(this);
    }

    public void getData(String url, String keywords){
      searchModel.getData(url,keywords);
    }
    @Override
    public void onSuccess(SearchBean dataDataBean) {
        iSearchView.onSuccess(dataDataBean);
    }
}
