package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

public class MiaoShaAdapter extends RecyclerView.Adapter<MiaoShaAdapter.ViewHolder>{
    private Context context;
    private List<LunBoBean.MiaoshaBean.ListBeanX> list;
    private OnItemClickListener onItemClickListener;

    public MiaoShaAdapter(Context context, List<LunBoBean.MiaoshaBean.ListBeanX> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MiaoShaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.miaosha_item_layout,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MiaoShaAdapter.ViewHolder holder, final int position) {
        String string = list.get(position).getImages().split("\\|")[0]+"";
        Glide.with(context).load(string).into(holder.imageView);
        holder.tv1.setText("￥"+list.get(position).getPrice());
        holder.tv2.setText("￥"+list.get(position).getBargainPrice());
        holder.tv1.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "手机";
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
        ImageView imageView;
        TextView tv1,tv2;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.miaosha_image);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClick(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
