package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import adapter.FLRightAdapter;
import bean.FenLeiRightBean;
import deom.jingdong.wwx.R;
import presenter.FenLeiRightPresenter;
import utils.Api;
import utils.OkHttp3Util;
import view.IFLRightView;

/**
 * Created by Administrator on 2018/1/9,0009.
 */

public class FenLeiRightFragment extends Fragment implements IFLRightView{

    private View view;
    private RecyclerView recyclerView;
    private List<FenLeiRightBean.DataBean> rightLeft;
    private FLRightAdapter rightAdapter;
    private int cid;
    private FenLeiRightPresenter fenLeiRightPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fenlei_right_layout,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();
        fenLeiRightPresenter = new FenLeiRightPresenter(this);

        cid = getArguments().getInt("cid");

        fenLeiRightPresenter.getData(Api.ZIFENLEI_API+cid);
    }

    private void findView() {
        recyclerView = view.findViewById(R.id.right_recycleView);
    }

    /**
     * 得到传过来的cid，  fragment之间传值是Bundel
     * @param position
     * @return
     */
    public static FenLeiRightFragment getFenRightFragment(int position){
        FenLeiRightFragment fenLeiRightFragment = new FenLeiRightFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("cid",position);
        fenLeiRightFragment.setArguments(bundle);

        return fenLeiRightFragment;
    }

    /**
     * 分类右边的数据
     * @param fenLeiRightBean
     */
    @Override
    public void onSuccess(final FenLeiRightBean fenLeiRightBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rightLeft = fenLeiRightBean.getData();
                rightAdapter = new FLRightAdapter(getActivity(),rightLeft);
                recyclerView.setAdapter(rightAdapter);
                Log.i("------","111111");
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                if (rightLeft.equals("")){
                    Toast.makeText(getActivity(),"没有数据",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
