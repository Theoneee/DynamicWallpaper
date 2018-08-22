package com.theone.dynamicwallpaper.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.theone.dynamicwallpaper.bean.Video;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class VideoUtil {

    public static VideoUtil videoUtil;

    public static VideoUtil getInstance(){
        if(null == videoUtil)
            videoUtil = new VideoUtil();
        return videoUtil;
    }

    public  void initData(Context context,OnFindVideoFinishListener listner)  {
        DurationUtils durationUtils = new DurationUtils();
        SharedPreferencesUtil sp = SharedPreferencesUtil.getInstance();
        String max = sp.getString("max","1min");
        String min = sp.getString("min","5s");
        long MIN = durationUtils.String2Long(min);
        long MAX  = durationUtils.String2Long(max);

        ArrayList vList = new ArrayList<Video>();
        String[] mediaColumns = new String[]{MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DURATION,MediaStore.Video.Thumbnails.DATA,MediaStore.MediaColumns.DATE_ADDED};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    if(time>=MIN&&time<=MAX){
                        Video info = new Video();
                        info.setPath(cursor.getString(cursor
                                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                        info.setSize(time);
                        info.setDuration(durationUtils.stringForTime((int) time));
                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                        info.setThumbPath(videoPath);
                        String addtime = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
                        info.setAddTime(Long.parseLong(addtime));
                        vList.add(info);
                }
            } while (cursor.moveToNext());
        }
        if(vList.size()>0) {
            SplitVideos(vList);
        }
        listner.onFinish();
    }

    /**
     * 将本地扫描的视频进行拆分（全部、历史、喜欢）
     */
    public void SplitVideos(ArrayList<Video> videos){
        List<Video> all = new ArrayList<>();
        List<Video> history = new ArrayList<>();
        List<Video> like = new ArrayList<>();
        // 循环判断视频类型，并加入到对应的数据里面
        for(Video video:videos){
            // 判断数据库里是否存在此条视频信息（ 只有经过操作过后才加入到数据库 )
            List<Video> list = LitePal.where("path = ?", video.getPath()).find(Video.class);
            if(list.size()>0){
                // 存在，则判断用户是否标记移除显示列表
                Video video1 = list.get(0);
                if(video1.isShow()){
                    // 首先加入到全部里
                    all.add(video1);
                    // 判断是否是历史
                    if(video1.isHistory()){
                        history.add(video1);
                    }
                    //判断是否是喜欢
                    if(video1.isLike()){
                        like.add(video1);
                    }
                }
            }else{
                // 数据库里没有当前视频信息则直接加入到全部里
                all.add(video);
            }
        }
        // 将处理过后的数据保存到缓存用户页面数据显示（ 用户操作时，直接更改缓存里的数据和更改数据库，显示时直接用缓存的)
        DataCache dataCache = DataCache.getInstance();
        dataCache.setVideos(all);
        dataCache.setHistory(history);
        dataCache.setLike(like);
    }

    public interface OnFindVideoFinishListener{
        void onFinish();
    }
}
