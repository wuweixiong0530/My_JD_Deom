package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dash.zxinglibrary.activity.CodeUtils;
import com.dash.zxinglibrary.util.ImageUtil;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;
import com.sunfusheng.marqueeview.MarqueeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.JiuAdapter;
import adapter.MiaoShaAdapter;
import adapter.TuiJianAdapter;
import bean.FenLeiLeftBean;
import bean.LunBoBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.CustomCaptrueActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.JiuPresenter;
import presenter.MiaoShaPresenter;
import resou.ReSouActivity;
import utils.Api;
import utils.ObservableScrollView;
import utils.OkHttp3Util;
import view.IMiaoShaView;
import view.IJiuView;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class FragmentShouYe extends Fragment implements IJiuView, IMiaoShaView{
    //秒杀相关
    private long mHour = 02;
    private long mMin = 22;
    private long mSecond = 22;
    private boolean isRun = true;
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private RecyclerView miaosha;
    private List<LunBoBean.MiaoshaBean.ListBeanX> miaoShaList;
    private MiaoShaAdapter miaoShaAdapter;
    private MiaoShaPresenter miaoShaPresenter;
    //推荐相关
    private RecyclerView tuijian;
    private List<LunBoBean.TuijianBean.ListBean> tuijianList;
    private TuiJianAdapter tuiJianAdapter;

    private View view;
    private XBanner banner;
    private List<String> imageList;
    private JiuPresenter jiuPresenter;
    private List<FenLeiLeftBean.DataBean> jiuList;
    private JiuAdapter jiuAdapter;
    private RecyclerView jiu;
    private List<LunBoBean.DataBean> lunboList;
    private LunBoBean guangGaoBean;
    private MarqueeView marqueeView;

    private RelativeLayout saoyisao;
    private TextView tv_sousuo;
    private RelativeLayout xiaoxi;
    private final int REQUEST_CODE = 1001;
//
//    private PullToRefreshScrollView pull_lv;
//    ILoadingLayout startLabels;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                banner.setData(imageList,null);
                banner.setPoinstPosition(XBanner.RIGHT);
                banner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, Object model, View view, int position) {
                        Glide.with(getActivity()).load(imageList.get(position)).into((ImageView) view);

                    }
                });
                //点击跳转
                banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                    @Override
                    public void onItemClick(XBanner banner, int position) {
                        Toast.makeText(getContext(),"点击了"+position,Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(),WebActivity.class);
////                        //把URL传过去
//                        intent.putExtra("url",lunboList.get(position).getUrl());
//                        startActivity(intent);
                    }
                });
            }
            if (msg.what == 2){
                //适配器
                jiuAdapter = new JiuAdapter(getActivity(),jiuList);
                jiu.setAdapter(jiuAdapter);
                //显示方式
                jiu.setLayoutManager(new GridLayoutManager(getActivity(),2, OrientationHelper.HORIZONTAL,false));
            }
            if (msg.what == 3){
                computeTime();
                if (mHour<10){
                    tvHour.setText("0"+mHour+"");
                }else {
                    tvHour.setText("0"+mHour+"");
                }
                if (mMin<10){
                    tvMinute.setText("0"+mMin+"");
                }else {
                    tvMinute.setText(mMin+"");
                }
                if (mSecond<10){
                    tvSecond.setText("0"+mSecond+"");
                }else {
                    tvSecond.setText(mSecond+"");
                }
            }
            if (msg.what == 4){
                miaoShaAdapter = new MiaoShaAdapter(getActivity(),miaoShaList);
                miaosha.setAdapter(miaoShaAdapter);
                miaosha.setLayoutManager(new LinearLayoutManager(getActivity(),OrientationHelper.HORIZONTAL,false));
                miaoShaAdapter.setOnItemClick(new MiaoShaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(),"点击"+position,Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (msg.what == 5){
                tuiJianAdapter = new TuiJianAdapter(getActivity(),tuijianList);
                tuijian.setAdapter(tuiJianAdapter);
                tuijian.setLayoutManager(new GridLayoutManager(getActivity(),2,OrientationHelper.VERTICAL,false));
                tuiJianAdapter.setOnItemClick(new TuiJianAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(),"点击"+position,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
    private LinearLayout line;
    private int imageHeight=300;
    private ObservableScrollView pull_lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentshouye_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //找控件
        findView();
        //实例化九宫格中间者
        jiuPresenter = new JiuPresenter(this);
        jiuPresenter.getData(Api.FENLEI_API);
        //轮播
        lunBo();
        //跑马灯
        PaoMaDeng();
        //秒杀倒计时
        startRun();
        //实例化秒杀中间者
        miaoShaPresenter = new MiaoShaPresenter(this);
        miaoShaPresenter.getData(Api.LUNBO_API);

        //搜索框在布局最上面
        line.bringToFront();
        //滑动页面搜索框背景渐变
        pull_lv.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {
                    line.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或者美工提供
                } else if (y > 0 && y <= imageHeight) {
                    float scale = (float) y / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明
                    line.setBackgroundColor(Color.argb((int) alpha, 227, 29, 26));
                } else {
                    line.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
                }
            }
        });
    }

    private void lunBo() {
        OkHttp3Util.doGet(Api.LUNBO_API, new Callback() {

            private List<LunBoBean.DataBean> lunboList;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                guangGaoBean = gson.fromJson(string, LunBoBean.class);
                lunboList = guangGaoBean.getData();
                imageList = new ArrayList<>();
                for (LunBoBean.DataBean dataBean : lunboList)
                {
                    String icon1 = dataBean.getIcon();
                    imageList.add(icon1);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }

    private void findView() {
        line = view.findViewById(R.id.line);
        pull_lv = view.findViewById(R.id.pull_lv);
//        pull_lv = view.findViewById(R.id.pull_lv);
//        //2.设置刷新模式
//        pull_lv.setMode(PullToRefreshBase.Mode.BOTH);
//        //3.通过getLoadingLayoutProxy 方法来指定上拉和下拉时显示的状态的区别(也就是设置向下拉的时候头部里面显示的文字)
//        startLabels = pull_lv.getLoadingLayoutProxy(true, false);
//        startLabels.setPullLabel("下拉刷新");
//        startLabels.setRefreshingLabel("正在刷新...");
//        startLabels.setReleaseLabel("放开刷新");
//
//        ILoadingLayout endLabels = pull_lv.getLoadingLayoutProxy(false, true);
//        endLabels.setPullLabel("上拉刷新");
//        endLabels.setRefreshingLabel("正在载入...");
//        endLabels.setReleaseLabel("放开刷新...");

        banner = view.findViewById(R.id.banner);
        jiu = view.findViewById(R.id.jiu);
        marqueeView = view.findViewById(R.id.marqueeView);
        tvHour = view.findViewById(R.id.tv_hour);
        tvMinute = view.findViewById(R.id.tv_minute);
        tvSecond = view.findViewById(R.id.tv_second);
        miaosha = view.findViewById(R.id.rlv_miaosha);
        tuijian = view.findViewById(R.id.rlv_tuijian);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                //拿到传递回来的数据
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    //解析得到的结果
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }else if (requestCode == 1002){

            //uri路径......需要把uri路径转换为绝对路径!!!!!!!!!!!!!!!!!!!file...new file(绝对路径)
            Uri uri = data.getData();

            try {
                //解析图片的方法...ImageUtil.getImageAbsolutePath(this, uri)通过uri路径得到图片在手机中的绝对路径
                CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(getActivity(), uri), new CodeUtils.AnalyzeCallback() {
                    //Bitmap mBitmap 解析的那张图片, String result解析的结果
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void PaoMaDeng() {
        List<String> info = new ArrayList<>();
        info.add("1. 大家好，我叫吴卫雄。");
        info.add("2. 欢迎大家关注我哦！");
        info.add("3. GitHub帐号：sfsheng0322");
        info.add("4. 新浪微博：孙福生微博");
        info.add("5. 个人博客：sunfusheng.com");
        info.add("6. 微信公众号：孙福生");
        marqueeView.startWithList(info);
        // 在代码里设置自己的动画
        marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
    }

    /**
     * 九宫格
     * 重写的view层的方法
     * @param fenLeiBean
     */
    @Override
    public void jiuSuccess(final FenLeiLeftBean fenLeiBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jiuList = fenLeiBean.getData();
                handler.sendEmptyMessage(2);
            }
        });
    }

    /**
     * 秒杀倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 3;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
            }
        }
    }

    /**
     * 重写的秒杀和推荐的方法
     * @param lunBoBean
     */
    @Override
    public void miaoShaOnSuccess(final LunBoBean lunBoBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                miaoShaList = lunBoBean.getMiaosha().getList();
                handler.sendEmptyMessage(4);

                tuijianList = lunBoBean.getTuijian().getList();
                handler.sendEmptyMessage(5);
            }
        });
    }

}
