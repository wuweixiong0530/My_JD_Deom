package utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.dash.zxinglibrary.activity.ZXingLibrary;

/**
 * Created by Administrator on 2018/1/8,0008.
 */

public class MyApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainId;
    public static boolean isLoginSuccess;//是否已经登录的状态

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化zxing库
        ZXingLibrary.initDisplayOpinion(this);

        //关于context----http://blog.csdn.net/lmj623565791/article/details/40481055
        context = getApplicationContext();
        //初始化handler
        handler = new Handler();
        //主线程的id
        mainId = Process.myTid();
    }

    /**
     * 一下三个订单用
     */


    /**
     * 对外提供了context
     * @return
     */
    public static Context getAppContext() {
        return context;
    }

    /**
     * 得到全局的handler
     * @return
     */
    public static Handler getAppHanler() {
        return handler;
    }

    /**
     * 获取主线程id
     * @return
     */
    public static int getMainThreadId() {
        return mainId;
    }
}
