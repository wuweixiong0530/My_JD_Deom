package deom.jingdong.wwx.activity.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunfusheng.marqueeview.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import bean.LoginBean;
import deom.jingdong.wwx.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import presenter.LoginPresenter;
import utils.Api;
import view.ILoingView;

public class GeRenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button tuichu;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private Button fanhui;
    private ImageView touxiang;
    private TextView name;
    private TextView nicheng;

    //上传头像需要用到
    private static final int CHOOSE_PICTURE=0;
    private static final int TAKE_PICTURE=1;
    private static final int CROP_SMALL_PICTURE=2;
    private static Uri tempUri;

    private String mobile;
    private String password;
    private String nn;
    private String token;
    private String uid;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_ge_ren);
        findView();

        sharedPreferences = getSharedPreferences("logins",MODE_PRIVATE);
        edit = sharedPreferences.edit();

        mobile = sharedPreferences.getString("mobile", "");
        password = sharedPreferences.getString("password", "");
        token = sharedPreferences.getString("token", "");
        nn = sharedPreferences.getString("mobile","");
        uid = sharedPreferences.getString("uid",uid);
        Log.i("geren:", "username" + nn);
        Log.i("geren:", "mobile" + mobile);
        Log.i("geren:", "password" + password);
        sharedPreferences = getSharedPreferences("logins",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        edit.putString("icon", String.valueOf(file));
        edit.putBoolean("isLogin", true);
        edit.commit();
    }

    private void findView() {
        touxiang = findViewById(R.id.geren_touxiang);
        name = findViewById(R.id.geren_name);
        nicheng = findViewById(R.id.geren_nicheng);
        tuichu = findViewById(R.id.geren_tuichu);
        fanhui = findViewById(R.id.geren_fanhui);
        touxiang.setOnClickListener(this);
        tuichu.setOnClickListener(this);
        fanhui.setOnClickListener(this);

//        String icon = sharedPreferences.getString("icon", "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.geren_touxiang://头像
                showChoosePicDialog();
                break;
            case R.id.geren_name:
                break;
            case R.id.geren_nicheng:
                break;
            case R.id.geren_tuichu://退出
                edit.clear();
                edit.putString("username","登录/注册＞");
                edit.commit();
                finish();
                break;
            case R.id.geren_fanhui://返回
                finish();
                break;
        }
    }


    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items={"选择本地照片","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case CHOOSE_PICTURE://选择本地照片
                        Intent openAlbumIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(openAlbumIntent,CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        Intent openCameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                        //指定照片保存路径sd卡，image.jpg为一个临时文件，每次拍照后这个图片都会被
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
                        startActivityForResult(openCameraIntent,TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PICTURE:
                startPhotoZoom(tempUri);
                break;
            case CHOOSE_PICTURE:
                startPhotoZoom(data.getData());
                break;
            case CROP_SMALL_PICTURE:
                if(data!=null){
                    setImageToView(data);
                }
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    protected  void startPhotoZoom(Uri uri){
        if(uri==null){
            Log.i("tag","The uri is not exist");
        }
        tempUri=uri;
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置裁剪
        intent.putExtra("crop","true");
        //aspectX aspectY是宽高的比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //outputX outputY是裁剪图片的宽高
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     * @param data
     */
    protected void setImageToView(Intent data){
        Bundle extras=data.getExtras();
        if(extras!=null){
            Bitmap photo=extras.getParcelable("data");
            touxiang.setImageBitmap(photo);



            saveImage(photo);
            File file=new File(getCacheDir()+"/aa.jpg");
            OkHttpClient ck=new OkHttpClient();
            MultipartBody.Builder mb=new MultipartBody.Builder().setType(MultipartBody.FORM);
            mb.addFormDataPart("mobile",mobile);
            mb.addFormDataPart("file",file.getName(), RequestBody.create(MediaType.parse("image/*"),file));
            Request r=new Request.Builder().url(Api.HEAD_API).post(mb.build()).build();
            ck.newCall(r).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GeRenActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GeRenActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }
    /**
     * 读取的图片存在一个路径里
     * @param photo
     */
    private void saveImage(Bitmap photo) {
        file = new File(getCacheDir()+"/aa.jpg");
        try{
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.JPEG,100,bos);//压缩
            bos.flush();
            bos.close();
        }catch (Exception e){

        }
    }

    public void resBtn(View view) {
        Intent intent = new Intent(GeRenActivity.this,ResActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(nn);
        nicheng.setText(token);

    }
}
