package com.dash.zxinglibrary.activity;

import android.content.Context;
import android.util.DisplayMetrics;

import com.dash.zxinglibrary.DisplayUtil;


/**
 * created by wwx
 */

public class ZXingLibrary {

    public static void initDisplayOpinion(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(context, dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(context, dm.heightPixels);
    }
}
