package com.theone.dynamicwallpaper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.view.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author The one
 * @date 2018/7/24 0024
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.loading_view)
    LoadingView loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        loadingView.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView.stop();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
