package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import bean.SearchBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.XiangQingActivity;


/**
 * Created by Administrator on 2018/1/5,0005.
 */

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.ViewHolder> {

    private Context context;
    private List<SearchBean.DataBean> list;
    private OnItemClickListener onItemClickListener;

    public LinearAdapter(Context context, List<SearchBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public LinearAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.search1_item,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LinearAdapter.ViewHolder holder, final int position) {
        String[] split = list.get(position).getImages().split("\\|");
        Glide.with(context).load(split[0]).into(holder.imageView);
        holder.tv1.setText(list.get(position).getTitle());
        holder.tv2.setText("￥"+list.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid = list.get(position).getPid() + "";

                Intent intent = new Intent(context,XiangQingActivity.class);

                intent.putExtra("pid",pid);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv1,tv2;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }
    //点击的接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    //实现这个接口
    public void setOnItemClick(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
