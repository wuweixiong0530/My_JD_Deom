package deom.jingdong.wwx.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapter.GridAdapter;
import adapter.LinearAdapter;
import bean.SearchBean;
import deom.jingdong.wwx.R;
import presenter.SearchPresenter;
import utils.Api;
import view.ISearchView;

public class SearchActivity extends AppCompatActivity implements ISearchView {

    private TextView edit_search;
    private CheckBox checkbox;
    private RecyclerView recyclerView;
    private SearchPresenter searchPresenter;
    private String keywords;
    private List<SearchBean.DataBean> list;
    private LinearAdapter adapter;
    private GridAdapter gridadapter;
    boolean flag = false;
    private String name;
    private TextView back;
    private String pscid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_search);
        findView();
        searchPresenter = new SearchPresenter(this);

        //得到传过来的数据并显示搜索的内容
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
//        name = intent.getStringExtra("name");
        edit_search.setText(name);

        keywords = edit_search.getText().toString();

        if (keywords == null){
            Toast.makeText(SearchActivity.this,"请输入搜索的内容",Toast.LENGTH_SHORT).show();
        }else {
            searchPresenter.getData(Api.SEARCH_API,keywords);
        }
    }

    private void findView() {
        back = findViewById(R.id.search_back);
        edit_search = findViewById(R.id.edit_search);
        checkbox = findViewById(R.id.checkbox);
        recyclerView = findViewById(R.id.reycleView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 重新的方法
     * @param dataDataBean
     */
    @Override
    public void onSuccess(final SearchBean dataDataBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list = dataDataBean.getData();
                if (dataDataBean.getCode().equals("0")){
                    setAdapter(list);
                    Toast.makeText(SearchActivity.this,dataDataBean.getMsg(),Toast.LENGTH_SHORT).show();
                    checkbox.setChecked(flag);//默认未点击
                    checkbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag){
                                //设置适配器
                                setAdapter(list);
                                checkbox.setChecked(false);
                                flag = checkbox.isChecked();
                            }else {
                                gridadapter = new GridAdapter(SearchActivity.this,list);
                                recyclerView.setAdapter(gridadapter);
                                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                                checkbox.setChecked(true);
                                flag = checkbox.isChecked();
                                //给适配器设置点击事件
                                gridadapter.setOnItemClick(new LinearAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Toast.makeText(SearchActivity.this,"点击"+position,Toast.LENGTH_SHORT).show();

                                    }

                                });
                            }
                        }
                    });
                }else {
                    Toast.makeText(SearchActivity.this,"输入有误",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setAdapter(List<SearchBean.DataBean> list) {
        adapter = new LinearAdapter(SearchActivity.this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL,false));
        //给适配器设置点击事件
        adapter.setOnItemClick(new LinearAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

        });
    }


}