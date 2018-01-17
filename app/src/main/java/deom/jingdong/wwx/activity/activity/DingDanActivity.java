package deom.jingdong.wwx.activity.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import deom.jingdong.wwx.R;
import fragment.DingDanFragment;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;

public class DingDanActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupWindow popupMenu;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> list;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_ding_dan);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        imageView = findViewById(R.id.image);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);

        list = new ArrayList<>();

        list.add("待付款");
        list.add("已完成");
        list.add("已取消");

        viewPager.setOffscreenPageLimit(list.size());
        //1.给viewPager设置适配器
        /**
         * 管理者对象有几种????
         * getSupportFragmentManager()...activity管理他身上的fragment的时候使用
         * getChildFragmentManager()...fragment嵌套的时候,,,管理孩子需要使用这个管理者
         * getFragmentManager()....孩子里面还有fragment的话就使用这个
         */
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            //2.重写这个方法getPageTitle,,,得到当前页面的标题
            @Override
            public CharSequence getPageTitle(int position) {

                return list.get(position);
            }

            @Override
            public Fragment getItem(int position) {
                DingDanFragment fragment = new DingDanFragment();

                //应该要做的是传值,,,,去fragment里面获取,,,获取到值之后,,,在进行url路径的拼接
                Bundle bundle = new Bundle();
                bundle.putString("name",list.get(position));
                fragment.setArguments(bundle);

                return fragment;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });

        //3.将tabLayout和viewPager关联起来
        tabLayout.setupWithViewPager(viewPager);

        //图片的点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
    }

    private void showPopWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(DingDanActivity.this).inflate(R.layout.popwindow_item, null);
        popupMenu = new PopupWindow(contentView);
        popupMenu.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置各个控件的点击响应
        TextView tv1 = contentView.findViewById(R.id.pop_dai);
        TextView tv2 = contentView.findViewById(R.id.pop_wancheng);
        TextView tv3 = contentView.findViewById(R.id.pop_quxiao);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        popupMenu.showAsDropDown(imageView);

    }


    /**
     * 点击popwindow里的内容
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_dai:
                viewPager.setCurrentItem(0);
                popupMenu.dismiss();
                break;
            case R.id.pop_wancheng:
                viewPager.setCurrentItem(1);
                popupMenu.dismiss();
                break;
            case R.id.pop_quxiao:
                viewPager.setCurrentItem(2);
                popupMenu.dismiss();
                break;
            default:
                break;
        }
    }

}
