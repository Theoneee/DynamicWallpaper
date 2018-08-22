package com.theone.dynamicwallpaper.util;

import com.theone.dynamicwallpaper.bean.Video;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author The one
 * @date 2018/6/28 0028
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class DataCache {
    public static DataCache dataCache;

    public static DataCache getInstance() {
        if (dataCache == null)
            dataCache = new DataCache();
        return dataCache;
    }

    public List<Video> videos;
    public List<Video> history;
    public List<Video> like;

    public List<Video> getVideos() {
        if (null != videos && videos.size() != 0) {
            if (SharedPreferencesUtil.getInstance().getInt("sort", 0) == 0) {
                // 时间排序
                Collections.sort(videos, comp);
            } else {
                // 大小排序
                Collections.sort(videos, comp1);
            }
        }
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Video> getHistory() {
        if (null != history && history.size() > 0)
            Collections.sort(history, comp2);
        return history;
    }

    public void setHistory(List<Video> history) {
        this.history = history;
    }

    public List<Video> getLike() {
        if (null != like && like.size() > 0)
            Collections.sort(like, comp3);
        return like;
    }

    public void setLike(List<Video> like) {
        this.like = like;
    }

    Comparator comp = new Comparator() {
        public int compare(Object o1, Object o2) {
            Video p1 = (Video) o1;
            Video p2 = (Video) o2;
            long time1 = p1.getAddTime();
            long time2 = p2.getAddTime();
            if (time1 < time2)
                return 1;
            else if (time1 == time2)
                return 0;
            else if (time1 > time2)
                return -1;
            return 0;
        }
    };

    Comparator comp1 = new Comparator() {
        public int compare(Object o1, Object o2) {
            Video p1 = (Video) o1;
            Video p2 = (Video) o2;
            long time1 = p1.getSize();
            long time2 = p2.getSize();
            if (time1 < time2)
                return 1;
            else if (time1 == time2)
                return 0;
            else if (time1 > time2)
                return -1;
            return 0;
        }
    };

    Comparator comp2 = new Comparator() {
        public int compare(Object o1, Object o2) {
            Video p1 = (Video) o1;
            Video p2 = (Video) o2;
            long time1 = p1.getHistoryTime();
            long time2 = p2.getHistoryTime();
            if (time1 < time2)
                return 1;
            else if (time1 == time2)
                return 0;
            else if (time1 > time2)
                return -1;
            return 0;
        }
    };

    Comparator comp3 = new Comparator() {
        public int compare(Object o1, Object o2) {
            Video p1 = (Video) o1;
            Video p2 = (Video) o2;
            long time1 = p1.getLikeTime();
            long time2 = p2.getLikeTime();
            if (time1 < time2)
                return 1;
            else if (time1 == time2)
                return 0;
            else if (time1 > time2)
                return -1;
            return 0;
        }
    };
}
