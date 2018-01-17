package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bean.FenLeiLeftBean;
import deom.jingdong.wwx.R;
import fragment.FragmentFenLei;

/**
 * Created by Administrator on 2018/1/8,0008.
 */

public class FLLeftAdapter extends BaseAdapter{
    private List<FenLeiLeftBean.DataBean> leftList;
    private Context context;
    private int defItem;

    public FLLeftAdapter(Context context, List<FenLeiLeftBean.DataBean> leftList) {
        this.context = context;
        this.leftList = leftList;
    }

    @Override
    public int getCount() {
        return leftList.size();
    }

    @Override
    public Object getItem(int position) {
        return leftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.fl_left_item,null);
        TextView tv_fenlei=view.findViewById(R.id.left_text);
        tv_fenlei.setText(leftList.get(position).getName());

        //点击条目时背景变化
        if(position == FragmentFenLei.mPostion) {
            tv_fenlei.setTextColor(Color.RED);
            view.setBackgroundColor(Color.parseColor("#f1f5f5"));
        }
        else {
            tv_fenlei.setTextColor(Color.parseColor("#000000"));
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return view;
    }

}
