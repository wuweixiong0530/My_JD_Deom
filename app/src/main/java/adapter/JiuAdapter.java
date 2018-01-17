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

import bean.FenLeiLeftBean;
import deom.jingdong.wwx.R;
import deom.jingdong.wwx.activity.activity.SearchActivity;

/**
 * Created by Administrator on 2018/1/2,0002.
 */

public class JiuAdapter extends RecyclerView.Adapter<JiuAdapter.ViewHolder>{
    private List<FenLeiLeftBean.DataBean> list;
    private Context context;

    public JiuAdapter(Context context, List<FenLeiLeftBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public JiuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.jiu_item,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(JiuAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).getName());
        //图片分割
        String string = list.get(position).getIcon().split("\\|")[0]+"";
        Glide.with(context).load(string).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

}
