package adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bean.FenLeiRightBean;
import deom.jingdong.wwx.R;

/**
 * Created by Administrator on 2018/1/9,0009.
 */

public class FLRightAdapter extends RecyclerView.Adapter<FLRightAdapter.ViewHolder>{
    private Context context;
    private List<FenLeiRightBean.DataBean> rightList = new ArrayList<>();

    public FLRightAdapter(Context context, List<FenLeiRightBean.DataBean> rightList) {
        this.context = context;
        this.rightList = rightList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.right_adapter_item,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(rightList.get(position).getName());
        //子分类的数据
        List<FenLeiRightBean.DataBean.ListBean> ziList = rightList.get(position).getList();
        if (ziList != null){
            ZiRightAdapter ziRightAdapter = new ZiRightAdapter(context,ziList);
            holder.recyclerView.setAdapter(ziRightAdapter);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
            //条目的点击事件
            ziRightAdapter.setOnItemClickListner(new ZiRightAdapter.OnItemClicklistener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return rightList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rightholder_text);
            recyclerView = itemView.findViewById(R.id.rightholder_recycleView);
        }
    }
}
