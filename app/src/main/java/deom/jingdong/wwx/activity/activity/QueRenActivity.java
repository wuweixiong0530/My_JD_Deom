package deom.jingdong.wwx.activity.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import bean.CountPriceBean;
import deom.jingdong.wwx.R;

public class QueRenActivity extends AppCompatActivity {

    private TextView text_shifu;
    private TextView text_liji;
    private String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_que_ren);

        findVIew();
        Intent intent = getIntent();
        order = intent.getStringExtra("order");
        Log.i("queren",order);
        text_shifu.setText("实付款：￥"+order);
    }

    private void findVIew() {
        text_shifu = findViewById(R.id.text_shifu);
        text_liji = findViewById(R.id.text_liji);
    }

}
