package deom.jingdong.wwx.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dash.zxinglibrary.activity.CaptureFragment;
import com.dash.zxinglibrary.activity.CodeUtils;

import deom.jingdong.wwx.R;

public class CustomCaptrueActivity extends AppCompatActivity {


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        //解析成功的回调
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);

            resultIntent.putExtras(bundle);

            setResult(RESULT_OK, resultIntent);
            finish();
        }

        //解析失败的回调
        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };
    private boolean flag;
    private LinearLayout xiangce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_captrue);

        xiangce = findViewById(R.id.xiangce);

        //使用扫描的fragment替换frameLayout布局....captureActivity真正扫描的操作交给fragment
        //扫描成功或者失败的监听应该设置给fragment
        CaptureFragment captureFragment = new CaptureFragment();

        //设置自定义的...扫描布局
        //给扫描的fragment定制一个页面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        //设置监听
        captureFragment.setAnalyzeCallback(analyzeCallback);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container,captureFragment).commit();

        flag = false;

    }


    public void xiangce(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1002);
    }
}
