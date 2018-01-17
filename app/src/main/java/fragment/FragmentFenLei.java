package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapter.FLLeftAdapter;
import bean.FenLeiLeftBean;

import bean.FenLeiRightBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.CustomCaptrueActivity;
import presenter.JiuPresenter;
import resou.ReSouActivity;
import utils.Api;
import view.IFLRightView;
import view.IJiuView;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class FragmentFenLei extends Fragment implements IJiuView,AdapterView.OnItemClickListener {
    private static int totalHeight = 0;//ListView的高度
    public static int mPostion;
    private ListView listView;
    private FrameLayout frameLayout;
    private List<FenLeiLeftBean.DataBean> leftList;
    private FLLeftAdapter leftAdapter;
    private JiuPresenter jiuPresenter;
    private final int REQUEST_CODE = 1001;
    private View view;
    private FenLeiRightFragment fenLeiRightFragment;
    private RelativeLayout xiaoxi;
    private TextView tv_sousuo;
    private RelativeLayout saoyisao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentfenlei_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        jiuPresenter = new JiuPresenter(this);
        jiuPresenter.getData(Api.FENLEI_API);
    }

    private void findView() {
        listView = view.findViewById(R.id.left_listView);
        frameLayout = view.findViewById(R.id.left_frameLayout);
        saoyisao = view.findViewById(R.id.saoyisao);
        tv_sousuo = view.findViewById(R.id.tv_sousuo);
        xiaoxi = view.findViewById(R.id.xiaoxi);
        //搜索框跳转
        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReSouActivity.class);
                startActivity(intent);
            }
        });
        //点击二维码
        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CustomCaptrueActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        //点击消息
        xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"开发中",Toast.LENGTH_SHORT).show();
            }
        });
        //点击事件
        listView.setOnItemClickListener(this);
    }

    /**
     * 重写的方法
     * @param fenLeiBean
     */
    @Override
    public void jiuSuccess(final FenLeiLeftBean fenLeiBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leftList = fenLeiBean.getData();
                leftAdapter = new FLLeftAdapter(getActivity(),leftList);
                listView.setAdapter(leftAdapter);
            }
        });

    }

    /**
     * 点击listview的时间
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //拿到当前位置
        Log.i("-------","点击了"+mPostion);
        mPostion=position;
        //点击listViewitem回滚居中
        totalHeight = listView.getMeasuredHeight()-120;
        listView.smoothScrollToPositionFromTop(mPostion,totalHeight/2,50);
        leftAdapter.notifyDataSetChanged();
        //替换布局
        fenLeiRightFragment = FenLeiRightFragment.getFenRightFragment(mPostion);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.left_frameLayout, fenLeiRightFragment).commit();

    }

}
