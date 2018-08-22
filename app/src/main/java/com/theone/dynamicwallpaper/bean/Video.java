package com.theone.dynamicwallpaper.bean;


import org.litepal.crud.LitePalSupport;

public class Video extends LitePalSupport {

   private String path ;
   private long size;
   private String duration;
   private int id;
   private String thumbPath;
   private long addTime;
    /**
     * 是否显示
     */
   private  boolean isShow = true;
    /**
     * 是否为喜欢
     */
   private boolean isLike = false;
    /**
     * 是否使用过
     */
   private boolean isHistory = false;
    /**
     * 添加喜欢的时间
     */
   private long likeTime ;
    /**
     * 添加到历史的时间
     */
   private long historyTime;

    public Video() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }

    public long getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(long likeTime) {
        this.likeTime = likeTime;
    }

    public long getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(long historyTime) {
        this.historyTime = historyTime;
    }
}

