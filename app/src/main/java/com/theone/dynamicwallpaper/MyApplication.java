package com.theone.dynamicwallpaper;

import android.app.Application;
import android.content.SharedPreferences;

import org.litepal.LitePal;


/**
 * @author The one
 * @date 2018/5/28 0028
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class MyApplication extends Application {
    private static MyApplication application = null;
    private static String ShareName = " THEONE";
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        LitePal.initialize(this);
        application = this;
    }

    public static MyApplication getInstance() {
        return application;
    }

    public static SharedPreferences getSharedPreferences(){
        // 这里保存的视频路径是在服务里获取，所以这里一定要用：Context.MODE_MULTI_PROCESS
        SharedPreferences sharedPreferences = getInstance().getSharedPreferences(ShareName,application.MODE_MULTI_PROCESS);
        return sharedPreferences;
    }
}
