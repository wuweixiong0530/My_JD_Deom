package deom.jingdong.wwx.activity.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bean.ResBean;
import deom.jingdong.wwx.R;
import presenter.ResPresenter;
import utils.Api;
import view.IResView;

public class ResActivity extends AppCompatActivity implements IResView, View.OnClickListener {

    private EditText phone;
    private EditText pwd;
    private Button btn_res;
    private String mobile;
    private String password;
    private ResPresenter resPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_res);
        finiView();
        resPresenter = new ResPresenter(this);
    }

    private void finiView() {
        phone = findViewById(R.id.edit_phone);
        pwd = findViewById(R.id.edit_pwd);
        btn_res = findViewById(R.id.btn_res);

        btn_res.setOnClickListener(this);
    }

    public void cha(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        mobile = phone.getText().toString();
        password = pwd.getText().toString();
        resPresenter.getData(Api.RES_API,mobile,password);

    }

    @Override
    public void onSuccess(final ResBean resBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String code = resBean.getCode();
                if (code.equals("0")){
                    Toast.makeText(ResActivity.this,resBean.getMsg(),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(ResActivity.this,resBean.getMsg(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
