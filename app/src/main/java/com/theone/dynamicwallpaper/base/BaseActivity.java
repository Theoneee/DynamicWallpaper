package com.theone.dynamicwallpaper.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

/**
 * @author The one
 * @date 2018/7/26 0026
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class BaseActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        getWindow().setStatusBarColor(Color.WHITE);
    }

}
