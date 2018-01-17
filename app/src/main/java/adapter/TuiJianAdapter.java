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

import bean.LunBoBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.SearchActivity;

/**
 * Created by Administrator on 2017/12/27,0027.
 */

public class TuiJianAdapter extends RecyclerView.Adapter<TuiJianAdapter.ViewHolder>{
    private Context context;
    private List<LunBoBean.TuijianBean.ListBean> list;
    private OnItemClickListener onItemClickListener;

    public TuiJianAdapter(Context context, List<LunBoBean.TuijianBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TuiJianAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.tuijian_item_layout,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TuiJianAdapter.ViewHolder holder, final int position) {
        String string = list.get(position).getImages().split("\\|")[0]+"";
        Glide.with(context).load(string).into(holder.iv);
        holder.tv1.setText(list.get(position).getTitle());
        holder.tv2.setText("￥"+list.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "笔记本";
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("name",name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv1,tv2;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.tuijian_iv);
            tv1 = itemView.findViewById(R.id.tuijian_tv1);
            tv2 = itemView.findViewById(R.id.tuijian_tv2);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
