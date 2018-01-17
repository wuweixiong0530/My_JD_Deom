package resou;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.SearchActivity;

public class ReSouActivity extends AppCompatActivity {
    private String mNames[] = {
            "洗衣机","IT","第二",
            "电冰箱","水果","第二第一",
            "电脑","苹果","viewgroup",
            "电磁炉","第二","第二"};
    private ReSouView reSouView;
    private EditText name;
    private Dao dao;
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private List<String> sel;
    private Button btn;
    List<String> a=new ArrayList<>();
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_sou);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //找控件
        name = findViewById(R.id.et_sou);
        back = findViewById(R.id.btn_back);
        //热搜
        initChildViews();
        lv =  findViewById(R.id.lv);
        btn =  findViewById(R.id.btn);
        dao = new Dao(ReSouActivity.this);
        sel = dao.sel();
        adapter = new ArrayAdapter<>(ReSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sel);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(android.R.id.text1);
                String n = textView.getText().toString();
                name.setText(n);
                Toast.makeText(ReSouActivity.this, n, Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int d, long l) {
                AlertDialog.Builder ab=new AlertDialog.Builder(ReSouActivity.this);
                ab.setTitle("是否删除");
                Log.d("aaa",sel.get(d).toString());
                ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int delyi = dao.delyi(sel.get(d).toString());

                        if (delyi==1){
                            zhanshi();
                        }
                    }
                });

                ab.setNegativeButton("取消",null);
                ab.show();
                return true;
            }
        });
        if (sel.size()>0){
            btn.setVisibility(View.VISIBLE);
        }else if(sel.size()==0)
        {
            btn.setVisibility(View.INVISIBLE);
        }
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void zhanshi() {
        List<String> sel4 = dao.sel();
        ArrayAdapter<String> ada = new ArrayAdapter<>(ReSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sel4);
        lv.setAdapter(ada);
    }

    private void initChildViews() {
        // TODO Auto-generated method stub
        reSouView = findViewById(R.id.reSouView);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for(int i = 0; i < mNames.length; i ++){
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.WHITE);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textshape));
            reSouView.addView(view,lp);
        }
    }
    //点击搜索
    public void add(View view) {
        String n = name.getText().toString();
        int i = dao.insertJson(n);
        int typeClassText = InputType.TYPE_CLASS_TEXT;
        btn.setVisibility(View.VISIBLE);
        List<String> sel3 = dao.sel();
        a.add(0,n);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(ReSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, a);
        lv.setAdapter(adapter3);
        Intent intent = new Intent(ReSouActivity.this, SearchActivity.class);
        intent.putExtra("name",n);
        startActivity(intent);
    }

    public void delall(View view) {
        dao.del();
        List<String> sel2 = dao.sel();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(ReSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sel2);

        lv.setAdapter(adapter2);

        Toast.makeText(ReSouActivity.this,"清除成功",Toast.LENGTH_LONG).show();

        btn.setVisibility(View.INVISIBLE);

    }
}
