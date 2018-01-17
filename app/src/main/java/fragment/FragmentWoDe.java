package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapter.TuiJianAdapter;
import bean.LunBoBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.DingDanActivity;
import deom.jingdong.wwx.activity.activity.GeRenActivity;
import deom.jingdong.wwx.activity.activity.LoginActivity;
import presenter.MiaoShaPresenter;
import utils.Api;
import view.IMiaoShaView;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class FragmentWoDe extends Fragment implements View.OnClickListener, IMiaoShaView {

    private View view;
    private ImageView imageView;
    private TextView tv_login;
    private RecyclerView tuijian;
    private List<LunBoBean.TuijianBean.ListBean> tuijianList;
    private TuiJianAdapter tuiJianAdapter;
    private String tv;
    private String username;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView iv_dingdan;
    private ImageView shezhi;
    private MiaoShaPresenter miaoShaPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentwode_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        sharedPreferences = getActivity().getSharedPreferences("logins",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



    }

    private void findView() {
        imageView = view.findViewById(R.id.wode_touxiang);
        tv_login = view.findViewById(R.id.wode_login);
        tuijian = view.findViewById(R.id.rlv_tuijian);
        iv_dingdan = view.findViewById(R.id.iv_dingdan);
        shezhi = view.findViewById(R.id.shezhi);

        tv_login.setOnClickListener(this);
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiaoDing();
            }
        });
    }

    private void tiaoDing() {
        Intent intent = new Intent(getActivity(),DingDanActivity.class);
        getActivity().startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();

        miaoShaPresenter = new MiaoShaPresenter(this);
        miaoShaPresenter.getData(Api.LUNBO_API);

        username = sharedPreferences.getString("username", this.username);
        tv_login.setText(username);

    }

    /**
     * 点击名字的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wode_login:   //点击登录
                tv = tv_login.getText().toString();
                if (tv.equals("登录/注册＞")){
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(),GeRenActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    /**
     * 重写推荐的方法
     * @param lunBoBean
     */
    @Override
    public void miaoShaOnSuccess(final LunBoBean lunBoBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tuijianList = lunBoBean.getTuijian().getList();
                tuiJianAdapter = new TuiJianAdapter(getActivity(),tuijianList);
                tuijian.setAdapter(tuiJianAdapter);
                tuijian.setLayoutManager(new GridLayoutManager(getActivity(),2, OrientationHelper.VERTICAL,false));
                tuiJianAdapter.setOnItemClick(new TuiJianAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(),"点击"+position,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
