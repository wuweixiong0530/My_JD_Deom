package deom.jingdong.wwx.activity.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import deom.jingdong.wwx.R;
import fragment.FragmentFaXian;
import fragment.FragmentFenLei;
import fragment.FragmentGouWu;
import fragment.FragmentShouYe;
import fragment.FragmentWoDe;

public class HomeActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //找控件
        findView();
        //初始化数据
        initData();
    }

    private void initData() {
        //默认显示首页的fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentShouYe()).commit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_01:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentShouYe()).commit();
                        break;
                    case R.id.radioButton_02:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentFenLei()).commit();
                        break;
                    case R.id.radioButton_03:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentFaXian()).commit();
                        break;
                    case R.id.radioButton_04:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentGouWu()).commit();
                        break;
                    case R.id.radioButton_05:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentWoDe()).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void findView() {
        radioGroup = findViewById(R.id.radioGroup);
        //默认选中
        radioGroup.check(R.id.radioButton_01);
    }
}
