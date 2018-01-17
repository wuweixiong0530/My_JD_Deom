package adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.CartBean;
import bean.CountPriceBean;
import deom.jingdong.wwx.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import presenter.CartPresenter;
import utils.Api;
import utils.OkHttp3Util;

/**
 * Created by Administrator on 2018/1/12,0012.
 */

public class CartAdapter extends BaseExpandableListAdapter {
    private String uid;
    private Handler handler;
    private RelativeLayout relative_progress;
    private CartPresenter cartPresenter;
    private Context context;
    private CartBean cartBean;
    private int childIndex;
    private int allIndex;

    public CartAdapter(Context context, CartBean cartBean, Handler handler, RelativeLayout relative_progress, CartPresenter cartPresenter, String uid) {
        this.context = context;
        this.cartBean = cartBean;
        this.handler = handler;
        this.relative_progress = relative_progress;
        this.cartPresenter = cartPresenter;
        this.uid = uid;
    }

    @Override
    public int getGroupCount() {
        return cartBean.getData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return cartBean.getData().get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cartBean.getData().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cartBean.getData().get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        final GroupHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.group_layout, null);
            holder = new GroupHolder();

            holder.checkBox = view.findViewById(R.id.group_check);
            holder.textView = view.findViewById(R.id.group_text);

            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }

        final CartBean.DataBean dataBean = cartBean.getData().get(groupPosition);
        //赋值
        holder.textView.setText(dataBean.getSellerName());
        holder.checkBox.setChecked(dataBean.isGroupChecked());

        //商家的点击事件
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示progress
                relative_progress.setVisibility(View.VISIBLE);

                //根据商家的状态holder.checkbox.ischecked(),改变下面所有子条目的状态,,,10,,改变十次,更新完成一个之后再去执行下一个...递归
                childIndex = 0;
                updateAllChildInGroup(dataBean, holder.checkBox.isChecked());

            }
        });

        return view;
    }

    /**
     * 根据商家状态使用递归改变所有的子条目
     *
     * @param dataBean
     * @param checked
     */
    private void updateAllChildInGroup(final CartBean.DataBean dataBean, final boolean checked) {

        CartBean.DataBean.ListBean listBean = dataBean.getList().get(childIndex);

        Map<String, String> params = new HashMap<>();

        //?uid=71&sellerid=1&pid=1&selected=0&num=10
        params.put("uid", uid);
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));

        params.put("selected", String.valueOf(checked ? 1 : 0));
        params.put("num", String.valueOf(listBean.getNum()));

        OkHttp3Util.doPost(Api.UpDateCart_API, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //更新成功一条
                if (response.isSuccessful()) {
                    //索引增加
                    childIndex++;
                    if (childIndex < dataBean.getList().size()) {
                        //再去更新下一条
                        updateAllChildInGroup(dataBean, checked);

                    } else {//全都更新完成了....重新查询购物车
                        cartPresenter.getData(Api.SelectCart_API + uid);
                    }
                }

            }
        });

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.child_layout, null);
            holder = new ChildHolder();

            holder.checkBox = view.findViewById(R.id.child_check);
            holder.text_title = view.findViewById(R.id.child_title);
            holder.imageView = view.findViewById(R.id.child_image);
            holder.text_price = view.findViewById(R.id.child_price);
            holder.text_jian = view.findViewById(R.id.text_jian);
            holder.text_num = view.findViewById(R.id.text_num);
            holder.text_add = view.findViewById(R.id.text_add);
            holder.text_delete = view.findViewById(R.id.text_delete);

            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }

        final CartBean.DataBean.ListBean listBean = cartBean.getData().get(groupPosition).getList().get(childPosition);

        //赋值
        holder.text_title.setText(listBean.getTitle());
        holder.text_price.setText("¥" + listBean.getBargainPrice());

        String[] strings = listBean.getImages().split("\\|");
        Glide.with(context).load(strings[0]).into(holder.imageView);

        holder.checkBox.setChecked(listBean.getSelected() == 0 ? false : true);//根据0,1进行设置是否选中
        //setText()我们使用一定是设置字符串
        holder.text_num.setText(listBean.getNum() + "");

        //点击事件
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //此时需要显示进度条
                relative_progress.setVisibility(View.VISIBLE);
                //更新购物车,,,,需要改变是否选中,,,如果现在显示的是0,改成1;;;1--->0

                Map<String, String> params = new HashMap<>();

                //?uid=71&sellerid=1&pid=1&selected=0&num=10
                params.put("uid", uid);
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));

                params.put("selected", String.valueOf(listBean.getSelected() == 0 ? 1 : 0));
                params.put("num", String.valueOf(listBean.getNum()));

                OkHttp3Util.doPost(Api.UpDateCart_API, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //更新成功之后,,,,再次查询购物车的数据进行展示
                        if (response.isSuccessful()) {
                            cartPresenter.getData(Api.SelectCart_API+uid);
                        }
                    }
                });

            }
        });

        //加号
        holder.text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //此时需要显示进度条
                relative_progress.setVisibility(View.VISIBLE);
                //更新购物车,,,,需要改变是数量,,,,需要加1
                Map<String, String> params = new HashMap<>();
                //?uid=71&sellerid=1&pid=1&selected=0&num=10
                params.put("uid", uid);
                Log.i("shipei--",uid);
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()));
                params.put("num", String.valueOf(listBean.getNum() + 1));
//?uid=71&sellerid=1&pid=1&selected=0&num=10
                OkHttp3Util.doPost(Api.UpDateCart_API, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //更新成功之后,,,,再次查询购物车的数据进行展示
                        if (response.isSuccessful()) {
                            cartPresenter.getData(Api.SelectCart_API+uid);
                        }
                    }
                });

            }
        });
        //减号
        holder.text_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = listBean.getNum();

                if (num == 1) {
                    return;
                }

                //此时需要显示进度条
                relative_progress.setVisibility(View.VISIBLE);
                //更新购物车,,,,需要改变是数量,,,,需要加1

                Map<String, String> params = new HashMap<>();

                //?uid=71&sellerid=1&pid=1&selected=0&num=10
                params.put("uid", uid);
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()));

                params.put("num", String.valueOf(num - 1));

                OkHttp3Util.doPost(Api.UpDateCart_API, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //更新成功之后,,,,再次查询购物车的数据进行展示
                        if (response.isSuccessful()) {
                            cartPresenter.getData(Api.SelectCart_API+uid);
                        }
                    }
                });
            }
        });
        //删除
        holder.text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示进度条
                relative_progress.setVisibility(View.VISIBLE);
                //调用删除的接口
                Map<String, String> params = new HashMap<>();

                //uid=72&pid=1
                params.put("uid", uid);
                params.put("pid", String.valueOf(listBean.getPid()));

                OkHttp3Util.doPost(Api.DeleteCart_API, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            //再次请求购物车的数据
                                cartPresenter.getData(Api.SelectCart_API+uid);

                        }
                    }
                });

            }
        });


        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    /**
     * 计算总价和数量,,,并且发送给activity进行显示
     */
    public void sendPriceAndCount() {

        double price = 0;
        int count = 0;

        for (int i = 0; i < cartBean.getData().size(); i++) {
            List<CartBean.DataBean.ListBean> listBeans = cartBean.getData().get(i).getList();
            for (int j = 0; j < listBeans.size(); j++) {

                if (listBeans.get(j).getSelected() == 1) {
                    price += listBeans.get(j).getBargainPrice() * listBeans.get(j).getNum();
                    count += listBeans.get(j).getNum();
                }
            }
        }

        //double高精度,,,计算的时候可能会出现一串数字...保留两位
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String priceString = decimalFormat.format(price);
        CountPriceBean countPriceBean = new CountPriceBean(priceString, count);

        //发送到主页面进行显示
        Message msg = Message.obtain();

        msg.what = 0;
        msg.obj = countPriceBean;
        handler.sendMessage(msg);
    }

    /**
     * 根据全选的状态更新所有商品的状态
     *
     * @param checked
     */
    public void setAllChildsChecked(boolean checked) {

        //创建一个大的结合,,,存放所有商品的数据
        List<CartBean.DataBean.ListBean> allList = new ArrayList<>();
        for (int i = 0; i < cartBean.getData().size(); i++) {
            List<CartBean.DataBean.ListBean> listBeans = cartBean.getData().get(i).getList();
            for (int j = 0; j < listBeans.size(); j++) {
                allList.add(listBeans.get(j));
            }
        }

        //显示progress
        relative_progress.setVisibility(View.VISIBLE);

        //递归更新....
        allIndex = 0;
        updateAllChecked(allList, checked);
    }

    /**
     * 更新所有的商品
     *
     * @param allList
     * @param checked
     */
    private void updateAllChecked(final List<CartBean.DataBean.ListBean> allList, final boolean checked) {

        CartBean.DataBean.ListBean listBean = allList.get(allIndex);
        Map<String, String> params = new HashMap<>();

        //?uid=71&sellerid=1&pid=1&selected=0&num=10
        params.put("uid", uid);
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));

        params.put("selected", String.valueOf(checked ? 1 : 0));
        params.put("num", String.valueOf(listBean.getNum()));

        OkHttp3Util.doPost(Api.UpDateCart_API, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //更新一条成功
                if (response.isSuccessful()) {
                    allIndex++;

                    if (allIndex < allList.size()) {
                        //继续更新下一条
                        updateAllChecked(allList, checked);
                    } else {
                        //重新查询
                        cartPresenter.getData(Api.SelectCart_API+uid);
                    }

                }
            }
        });
    }


    private class GroupHolder {
        CheckBox checkBox;
        TextView textView;
    }

    private class ChildHolder {
        CheckBox checkBox;
        ImageView imageView;
        TextView text_title;
        TextView text_price;
        TextView text_num;
        TextView text_jian;
        TextView text_add;
        TextView text_delete;
    }
}
