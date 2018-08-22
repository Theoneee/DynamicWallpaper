package com.theone.dynamicwallpaper.ui;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.adapter.TabFragmentAdapter;
import com.theone.dynamicwallpaper.base.BaseActivity;
import com.theone.dynamicwallpaper.util.QMUIDialogUtils;
import com.theone.dynamicwallpaper.util.ToastUtils;
import com.theone.dynamicwallpaper.util.VideoUtil;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class MainActivity extends BaseActivity {

    public static final int REQUEST_LIVE_PAPER = 777;
    public static final int REQUEST_UPDATE_DURATION = 888;
    public static final int REQUEST_UPDATE_SORT = 999;
    public static final String SERCIVE_1 = "com.theone.dynamicwallpaper.service.VideoLiveWallpaper";
    public static final String SERCIVE_2 = "com.theone.dynamicwallpaper.service.VideoLiveWallpaper2";

    @BindView(R.id.top_bar)
    QMUITopBar topBar;
    @BindView(R.id.tab_list)
    SmartTabLayout tabList;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_no_permission)
    TextView tvTips;

    private QMUITipDialog loadingDialog;
    private QMUITipDialog deleteDialog;
    private QMUITipDialog addDialog;
    private QMUITipDialog removeDialog;

    private String[] TYPES = {"全部", "历史", "喜欢"};
    private List<VideoFragment> fragments = new ArrayList<>();
    private VideoFragment videoFragment1, videoFragment2, videoFragment3;


    private int CURRENT_FRAGMENT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RequestPermission();
    }

    private void RequestPermission() {
        //动态申请内存存储权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Connector.getDatabase();
                            initView();
                            showContent();
                            getVideos(true);
                        } else {
                            showEmptyView(getResources().getString(R.string.no_permission));
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initView() {
        topBar.addRightImageButton(R.drawable.mz_titlebar_ic_setting_dark, 100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, 100);
            }
        });
//        topBar.addLeftImageButton(R.drawable.local_ic_sort, 200).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showTypeChoose();
//            }
//        });
        loadingDialog = QMUIDialogUtils.LoadingTipsDialog(this, "加载中");
        deleteDialog = QMUIDialogUtils.SuccessTipsDialog(this, "删除成功");
        addDialog = QMUIDialogUtils.SuccessTipsDialog(this, "添加成功");
        removeDialog = QMUIDialogUtils.SuccessTipsDialog(this, "移除成功");

//        CURRENT_SORT = SharedPreferencesUtil.getInstance().getInt("sort",0);
    }

    public void getVideos(final boolean isInit) {
        VideoUtil.getInstance().initData(this, new VideoUtil.OnFindVideoFinishListener() {
            @Override
            public void onFinish() {
                if (isInit)
                    initTabs();
            }
        });
    }

    private void initTabs() {
        videoFragment1 = VideoFragment.newInstance(0);
        videoFragment2 = VideoFragment.newInstance(1);
        videoFragment3 = VideoFragment.newInstance(2);
        fragments.add(videoFragment1);
        fragments.add(videoFragment2);
        fragments.add(videoFragment3);
        viewpager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), fragments, TYPES));
        tabList.setViewPager(viewpager);
        tabList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CURRENT_FRAGMENT = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showEmptyView(String tips) {
        tvTips.setText(tips);
        ViewVisible(tvTips);
        ViewGone(viewpager);
    }

    private void showContent() {
        ViewVisible(viewpager);
        ViewGone(tvTips);
    }

    private void ViewGone(View view) {
        if (view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    private void ViewVisible(View view) {
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.VISIBLE);
    }


    public void fresh() {
        videoFragment1.initData();
        videoFragment2.initData();
        videoFragment3.initData();
    }
    public void freshLike() {
        videoFragment3.initData();
    }

    public void freshHistory() {
        videoFragment2.initData();
    }

    public void showDeleteDialog() {
        showTipsDialog(deleteDialog);
    }

    public void showRemoveDialog() {
        showTipsDialog(removeDialog);
    }

    public void showAddLikeDialog() {
        showTipsDialog(addDialog);
    }

    private void showTipsDialog(final QMUITipDialog dialog) {
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIVE_PAPER) {
            if (isLiveWallpaperChanged()) {
                // 记录到数据库,更新当前的Service
                fragments.get(CURRENT_FRAGMENT).onSeted();
                videoFragment2.initData();
                //退回到桌面
                goHome(MainActivity.this);
            }
        } else if (resultCode == REQUEST_UPDATE_DURATION) {
            freshData();
        }else if(resultCode ==  REQUEST_UPDATE_SORT){
            videoFragment1.initData();
        }
    }

    /**
     * 更换条件后重新筛选
     */
    public void freshData(){
        VideoUtil.getInstance().initData(this, new VideoUtil.OnFindVideoFinishListener() {
            @Override
            public void onFinish() {
                fresh();
            }
        });
    }

    /**
     * 判断是否点击了更换了动态壁纸
     *
     * @return ture 关闭应用 false 只是返回到了选择界面
     */
    public boolean isLiveWallpaperChanged() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);// 得到壁纸管理器
        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();// 如果系统使用的壁纸是动态壁纸话则返回该动态壁纸的信息,否则会返回null
        if (wallpaperInfo != null) { // 如果是动态壁纸,则得到该动态壁纸的包名,并与想知道的动态壁纸包名做比较
            String currentLiveWallpaperPackageName = wallpaperInfo.getPackageName();
            String currentSerciceName = wallpaperInfo.getServiceName();
            if (currentLiveWallpaperPackageName.equals(getPackageName()) && fragments.get(CURRENT_FRAGMENT).getCurrentServices().equals(currentSerciceName)) {
                return true;
            }
        }
        return false;
    }

    public static void goHome(Activity activity) {
        Intent intent = new Intent();
        // 为Intent设置Action、Category属性
        intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
        intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
        activity.startActivity(intent);
    }

    long lastTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime < 2000) {
                finish();
            } else {
                lastTime = currentTime;
                ToastUtils.showToast(this, "再点一次退出");
            }
        }
        return false;
    }
}
