package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import bean.DingDanBean;
import deom.jingdong.wwx.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.Api;
import utils.CommonUtils;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/15,0015.
 * 订单的适配器
 */

public class DingDanAdapter extends BaseAdapter {

    private Context context;
    private List<DingDanBean.DataBean> list;
    private Handler handler;
    private String uid;
    private int page = 1;

    public DingDanAdapter(Context context, List<DingDanBean.DataBean> list, Handler handler, String uid) {
        this.context = context;
        this.list = list;
        this.handler = handler;
        this.uid = uid;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.dingdan_item, null);
            holder.titile = view.findViewById(R.id.dingtitile);
            holder.price = view.findViewById(R.id.dingprice);
            holder.time = view.findViewById(R.id.dingtime);
            holder.zt = view.findViewById(R.id.zhuangtai);
            holder.dan = view.findViewById(R.id.dan);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.titile.setText(list.get(i).getTitle());
        holder.price.setText("价格:" + list.get(i).getPrice());
        holder.time.setText("时间：" + list.get(i).getCreatetime());

        Log.d("ffff", list.get(i).getStatus() + "");
        if (list.get(i).getStatus() == 0) {
            holder.zt.setText("待付款");
            holder.zt.setTextColor(Color.RED);
        } else if (list.get(i).getStatus() == 1) {
            holder.zt.setText("已支付");
        } else if (list.get(i).getStatus() == 2) {
            holder.zt.setText("已取消");
        }


        if (list.get(i).getStatus() == 0) {
            holder.dan.setText("取消订单");
            holder.dan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    ab.setTitle("确认取消订单吗？");
                    ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int j) {

                            OkHttp3Util.doGet(Api.UpDateDingDan_API+"?uid="+uid+"&status=2&orderId="+list.get(i).getOrderid(), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        final String string = response.body().string();
                                        CommonUtils.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();

                                                page++;
                                                OkHttp3Util.doGet(Api.SelectDingDan_API+uid+"&page=1", new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {

                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {

                                                        final String string = response.body().string();
                                                        if (response.isSuccessful()) {

                                                            CommonUtils.runOnUIThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Gson gson = new Gson();
                                                                    DingDanBean dingbean = gson.fromJson(string, DingDanBean.class);
                                                                    list.clear();
                                                                    list.addAll(dingbean.getData());
                                                                    holder.zt.setTextColor(Color.GRAY);

                                                                    notifyDataSetChanged();
                                                                    List<DingDanBean.DataBean> data = dingbean.getData();

                                                                    Message message = Message.obtain();
                                                                    message.obj = data;

                                                                    message.what = 2;
                                                                    handler.sendMessage(message);

                                                                }
                                                            });
                                                        }


                                                    }
                                                });

                                            }
                                        });
                                    }


                                }
                            });


                        }
                    });
                    ab.setNegativeButton("否", null);
                    ab.show();
                }
            });


        } else {
            holder.dan.setText("查看订单");
        }

        return view;
    }

    public class ViewHolder {
        Button dan;
        TextView zt;
        TextView time;
        TextView price;
        TextView titile;
    }

}
