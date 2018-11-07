package com.theone.dynamicwallpaper.ui;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.adapter.DownloadAdapter;
import com.theone.dynamicwallpaper.base.BaseActivity;
import com.theone.dynamicwallpaper.bean.Download;
import com.theone.dynamicwallpaper.downdload.AnalyzerTask;
import com.theone.dynamicwallpaper.downdload.model.Video;
import com.theone.dynamicwallpaper.service.DownloadService;
import com.theone.dynamicwallpaper.util.DateUtil;
import com.theone.dynamicwallpaper.util.GlideUtil;
import com.theone.dynamicwallpaper.util.LogUtils;
import com.theone.dynamicwallpaper.util.QMUIDialogUtils;
import com.theone.dynamicwallpaper.util.ToastUtils;
import com.theone.dynamicwallpaper.view.FreshDownloadView;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author The one
 * @date 2018/7/20 0020
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class DownloadActivity extends BaseActivity implements AnalyzerTask.AnalyzeListener {


    private static final String TAG = "DownloadActivity";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.des)
    TextView des;
    @BindView(R.id.progress)
    FreshDownloadView freshDownloadView;
    @BindView(R.id.download_ing)
    TextView downloadIng;
    @BindView(R.id.download_done)
    TextView downloadDone;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.download_layout)
    RelativeLayout downloadLayout;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.empty_view)
    QMUIEmptyView emptyView;

    private QMUITipDialog loading, error;
    private DownloadAdapter downloadAdapter;
    private List<Download> downloads;

    private Download download;
    private IntentFilter filter;
    private boolean isDowning = false;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        initView();
//        getData(getIntent());
        initFilter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        registerReceiver(mReceiver, filter);
    }

    private void initFilter() {
        filter = new IntentFilter();
        filter.addAction(DownloadService.DOWNLOAD_OK);
        filter.addAction(DownloadService.DOWNLOAD_ERROR);
        filter.addAction(DownloadService.DOWNLOAD_ERROR_MSG);
        filter.addAction(DownloadService.UPDATE_PROGRESS);
        filter.addAction(DownloadService.UPDATE_PROGRESS_CURRENT);
        filter.addAction(DownloadService.UPDATE_PROGRESS_TOTAL);

    }

    /**
     * 接收Service发送的进度数据
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DownloadService.DOWNLOAD_OK:
                    // 下载完成
                    showDownView(false, null);
                    isDowning = false;
                    break;
                case DownloadService.DOWNLOAD_ERROR:
                    // 下载失败
                    freshDownloadView.showDownloadError();
                    String error = intent.getStringExtra(DownloadService.DOWNLOAD_ERROR_MSG);
                    showError(error);
                    break;
                case DownloadService.UPDATE_PROGRESS:
                    // 更新进度
                    long total = intent.getLongExtra(DownloadService.UPDATE_PROGRESS_TOTAL, 0);
                    long progress = intent.getLongExtra(DownloadService.UPDATE_PROGRESS_CURRENT, 0);
                    freshDownloadView.upDateProgress(progress, total);
                    isDowning = true;
                    break;
            }
        }
    };


    private void initView() {
        title.setText("下载");
        loading = QMUIDialogUtils.LoadingTipsDialog(this, "解析中");
        emptyView.setTitleText("暂无下载");
        downloads = new ArrayList<>();
        downloadAdapter = new DownloadAdapter(this);
        listView.setAdapter(downloadAdapter);
        downloads.addAll(updateDownloads());
        if (null != downloads && downloads.size() > 0) {
            downloadDone.setVisibility(View.VISIBLE);
            downloadAdapter.addHeadData(downloads, true);
            emptyView.setVisibility(View.GONE);
        } else {
            downloadDone.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        downloadLayout.setVisibility(View.GONE);
        tvTime.setVisibility(View.INVISIBLE);
    }

    private List<Download> updateDownloads() {
        List<Download> list = LitePal.findAll(Download.class);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Download download = list.get(i);
                if (!new File(download.getLocal()).exists()) {
                    list.remove(i);
                    LitePal.delete(Download.class, download.getId());
                }
            }
        }
        return list;
    }

    private boolean isDownload(String id) {
        boolean isDown = false;
        for (Download download : downloads) {
            if (download.getUrl().equals(id)) {
                isDown = true;
                break;
            }
        }
        return isDown;
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        getData(intent);
//    }
//
//    private void getData(Intent intent) {
//        if (isDowning) {
//            ToastUtils.showToast(this, "等待当前视频下载结束");
//            return;
//        }
//        Recv recv = new Recv(intent);
//        if (recv.isActionSend()) {
//            loading.show();
//            AnalyzerTask analyzerTask = new AnalyzerTask(this, this);
//            analyzerTask.execute(recv.getContent());
//        }
//    }

    ClipboardManager clipboard;

    private void getData() {
        String content = "";
        // 获取系统剪贴板
        if (null == clipboard)
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 获取剪贴板的剪贴数据集
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            // 从数据集中获取（粘贴）第一条文本数据
            content = clipData.getItemAt(0).getText().toString();
            Log.e(TAG, "getData:  content = "+content );
        }
        if (isDowning) {
            ToastUtils.showToast(this, "等待当前视频下载结束");
            return;
        }
        loading.show();
        AnalyzerTask analyzerTask = new AnalyzerTask(this, this);
        analyzerTask.execute(content);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void showDownView(boolean show, Video video) {

        downloadIng.setVisibility(show ? View.VISIBLE : View.GONE);
        downloadLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        freshDownloadView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            download = null;
            download = new Download();
            freshDownloadView.startDownload();
            GlideUtil.load(this, video.getUser().getAvatarThumbUrl(), icon);
            name.setText(video.getUser().getNickname());
            des.setText(video.getTitle());
            download.setIcon(video.getUser().getAvatarThumbUrl());
            download.setUrl(video.getUrl());
            download.setTitle(video.getUser().getNickname());
            download.setContent(video.getTitle());
            download.setLocal("DouYin-" + video.getId() + ".mp4");
            startDown();
        } else {
            downloadDone.setVisibility(View.VISIBLE);

            freshDownloadView.showDownloadOk();
            download.setDownload(true);
            download.setTime(DateUtil.getCurrentTime());
            download.save();
            downloads.add(download);
            downloadAdapter.setJustTime();
            downloadAdapter.addHeadData(downloads, true);
            File file = new File(download.getLocal());
            MimeTypeMap mtm = MimeTypeMap.getSingleton();
            MediaScannerConnection.scanFile(DownloadActivity.this,
                    new String[]{file.toString()},
                    new String[]{mtm.getMimeTypeFromExtension(file.toString().substring(file.toString().lastIndexOf(".") + 1))},
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(final String path, final Uri uri) {
                            LogUtils.showLog("刷新完毕");
                        }
                    });
        }
    }

    private void startDown() {
        Intent intent = new Intent(this, DownloadService.class);
        //携带额外数据
        intent.putExtra(DownloadService.DOWNLOAD, download);
        //发送数据给service
        startService(intent);

    }

    @Override
    public void onAnalyzed(final Video video) {
        loading.dismiss();
        if (emptyView.getVisibility() == View.VISIBLE)
            emptyView.setVisibility(View.GONE);
        if (isDownload(video.getUrl())) {
            ToastUtils.showToast(DownloadActivity.this, "该视频已下载");
            return;
        } else {
            showDownView(true, video);
        }
    }

    @Override
    public void onAnalyzeCanceled() {

    }

    @Override
    public void onAnalyzeError(Exception e) {
        showError(e.toString());
    }

    private void showError(String str) {
        error = QMUIDialogUtils.showTipsDialog(this, QMUITipDialog.Builder.ICON_TYPE_FAIL, str);
        error.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                error.dismiss();
                error = null;
            }
        }, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
