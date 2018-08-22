package com.theone.dynamicwallpaper.bean;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.theone.dynamicwallpaper.downdload.model.Video;

import org.litepal.crud.LitePalSupport;

/**
 * @author The one
 * @date 2018/7/23 0023
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class Download extends LitePalSupport implements Parcelable {

    private int id;
    private String title;
    private String content;
    private String icon;
    private boolean isDownload;
    private String url;
    private String local;
    private String time;


    public Download() {
    }

    public Download(Video video) {
        this.title = video.getUser().getNickname();
        this.content = video.getTitle();
        this.icon = video.getUser().getAvatarThumbUrl();
        this.isDownload = false;
        this.url = video.getUrl();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WallPaper/"+local;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Download{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", icon='" + icon + '\'' +
                ", isDownload=" + isDownload +
                ", url='" + url + '\'' +
                ", local='" + local + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.icon);
        dest.writeByte(this.isDownload ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeString(this.local);
        dest.writeString(this.time);
    }

    protected Download(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.icon = in.readString();
        this.isDownload = in.readByte() != 0;
        this.url = in.readString();
        this.local = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel source) {
            return new Download(source);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}
