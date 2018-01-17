package deom.jingdong.wwx.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bean.LoginBean;
import deom.jingdong.wwx.R;
import fragment.FragmentWoDe;
import presenter.LoginPresenter;
import utils.Api;
import view.ILoingView;

public class LoginActivity extends AppCompatActivity implements ILoingView, View.OnClickListener {

    private EditText phone;
    private EditText pwd;
    private Button btn_login;
    private String password;
    private String mobile;
    private LoginPresenter loginPresenter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button btn_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login);
        findVIew();
        loginPresenter = new LoginPresenter(this);

        sharedPreferences = getSharedPreferences("logins", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void findVIew() {
        phone = findViewById(R.id.edit_phone);
        pwd = findViewById(R.id.edit_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_res = findViewById(R.id.btn_res);

        btn_login.setOnClickListener(this);
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResActivity.class);
                startActivity(intent);
            }
        });
    }

    public void cha(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        mobile = phone.getText().toString();
        password = pwd.getText().toString();
        loginPresenter.getData(Api.LOGIN_API, mobile, password);

    }

    @Override
    public void onSuccess(final LoginBean loginBean) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String code = loginBean.getCode();
                String username = loginBean.getData().getUsername();
                if (code.equals("0")) {
                    Toast.makeText(LoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();
                    editor.putString("username", username);
                    editor.putString("mobile", loginBean.getData().getMobile());
                    editor.putString("uid", loginBean.getData().getUid() + "");
                    editor.putString("token",loginBean.getData().getToken());
                    editor.putString("password", password);
                    editor.putBoolean("isLogin", true);
                    editor.commit();
                    Log.i("login:", "username" + username);
                    Log.i("login:", "mobile" + loginBean.getData().getMobile());
                    Log.i("login:", "uid" + loginBean.getData().getUid() + "");

                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
