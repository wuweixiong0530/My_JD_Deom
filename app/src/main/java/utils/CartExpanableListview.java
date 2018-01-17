package utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by wwx on 2018/1/12,0012.
 *
 * 创建自定义二级购物车列表
 */

public class CartExpanableListview extends ExpandableListView {
    public CartExpanableListview(Context context) {
        super(context);
    }

    public CartExpanableListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CartExpanableListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写测量的方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }
}
