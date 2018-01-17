package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import bean.FenLeiRightBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.SearchActivity;

/**
 * Created by Administrator on 2018/1/9,0009.
 */

public class ZiRightAdapter extends RecyclerView.Adapter<ZiRightAdapter.ViewHolder>{
    private Context context;
    private List<FenLeiRightBean.DataBean.ListBean> ziList;
    private OnItemClicklistener onItemClickListner;

    public ZiRightAdapter(Context context, List<FenLeiRightBean.DataBean.ListBean> ziList) {
        this.context = context;
        this.ziList = ziList;
    }

    @Override
    public ZiRightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.zi_right_item,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ZiRightAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(ziList.get(position).getName());
        String[] split = ziList.get(position).getIcon().split("\\|");
        Glide.with(context).load(split[0]).into(holder.imageView);
        //如果设置了回调事件
        if (onItemClickListner != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ziList.get(position).getName();
                    Intent intent=new Intent(context, SearchActivity.class);
                    intent.putExtra("name",name);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ziList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.zi_imageView);
            textView = itemView.findViewById(R.id.zi_textView);
        }
    }
    //自定义点击的接口
    public interface OnItemClicklistener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListner(OnItemClicklistener onItemClickListner){
        this.onItemClickListner = onItemClickListner;
    }
}
