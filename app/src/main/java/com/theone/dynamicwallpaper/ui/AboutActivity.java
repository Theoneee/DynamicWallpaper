package com.theone.dynamicwallpaper.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.base.BaseActivity;
import com.theone.dynamicwallpaper.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author The one
 * @date 2018/5/29 0029
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);
        title.setText("关于");
        tvVersion.setText(getVersionName());
    }

    private String getVersionName()   {
        String version  ="";
        try {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
         version = "Ver: " + packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    private int clickNum = 1;
    private int max = 8;
    long lastTime = 0;
    @OnClick({R.id.iv_back, R.id.iv_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_icon:
                bingo();
                break;
        }
    }

    private void bingo(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 2000) {
            clickNum++;
            if(clickNum>4){
                if(clickNum == max){
                    MemorialActivity.starThisActivity(AboutActivity.this);
                }else{
                    int num = max - clickNum;
                    ToastUtils.showToast(AboutActivity.this,"再点击"+num+"次即可进入纪念日");
                }

            }
        } else {
            lastTime = currentTime;
            clickNum = 1;
        }
    }
}
