package com.theone.dynamicwallpaper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.IResponseDownloadHandler;
import com.theone.dynamicwallpaper.bean.Download;

import java.io.File;

public class DownloadService extends Service {

    public static final String DOWNLOAD = "download";
    public static final String DOWNLOAD_OK = "download_ok";
    public static final String DOWNLOAD_ERROR = "download_error";
    public static final String DOWNLOAD_ERROR_MSG = "download_error_msg";
    public static final String UPDATE_PROGRESS = "update_progress";
    public static final String UPDATE_PROGRESS_CURRENT = "update_progress_current";
    public static final String UPDATE_PROGRESS_TOTAL = "update_progress_total";

    private Download download;

    public DownloadService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         download = intent.getParcelableExtra(DOWNLOAD);
        startDown();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDown(){
        new OkDroid().download().url(download.getUrl())
                .tag(download.getUrl())
                .filePath(download.getLocal())
                .enqueue(new IResponseDownloadHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        Intent intent = new Intent();
                        intent.setAction(DOWNLOAD_OK);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onProgress(long progress, long total) {
                        Intent intent = new Intent();
                        intent.setAction(UPDATE_PROGRESS);
                        intent.putExtra(UPDATE_PROGRESS_CURRENT,progress);
                        intent.putExtra(UPDATE_PROGRESS_TOTAL,total);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onFailed(String errMsg) {
                        Intent intent = new Intent();
                        intent.setAction(DOWNLOAD_ERROR);
                        intent.putExtra(DOWNLOAD_ERROR_MSG,errMsg);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
