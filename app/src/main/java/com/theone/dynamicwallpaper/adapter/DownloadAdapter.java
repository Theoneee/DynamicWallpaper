package com.theone.dynamicwallpaper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.base.EntityAdapter;
import com.theone.dynamicwallpaper.bean.Download;
import com.theone.dynamicwallpaper.util.DateUtil;
import com.theone.dynamicwallpaper.util.GlideUtil;
import com.theone.dynamicwallpaper.util.LogUtils;
import com.theone.dynamicwallpaper.view.FreshDownloadView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadAdapter extends EntityAdapter<Download, DownloadAdapter.ViewHolder> {

    private String TIME;
    private String TIME_JUST ;
    private  SimpleDateFormat just;

    public DownloadAdapter(Context context, List<Download> datas) {
        super(context, datas);
    }

    public DownloadAdapter(Context context) {
        super(context);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         just = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        TIME = formatter.format(curDate);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_download;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position, View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, Download data) {
        if (data != null) {
            GlideUtil.load(context, data.getIcon(), holder.icon);
            holder.name.setText(data.getTitle());
            holder.des.setText(data.getContent());
            holder.time.setText(getTime(data.getTime()));
        }
    }

    public void setJustTime(){
        TIME_JUST = DateUtil.getCurrentTime();
    }

    private String getTime(String time){
        LogUtils.showLog(time+TIME_JUST+TIME);
        if(null == time || time.isEmpty())
            return "";
        if(null == TIME_JUST)
            setJustTime();
         if(time.startsWith(TIME_JUST)){
            time = "刚刚";
        }else if(time.startsWith(TIME)){
            int index = time.indexOf(" ");
            time = time.substring(index,time.length()-3);
        }else{
            time = TIME;
        }
        return time;
    }

    public class ViewHolder extends EntityAdapter.BaseHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.progress)
        FreshDownloadView progress;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.des)
        TextView des;
        @BindView(R.id.tv_time)
        TextView time;

        public ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }
}
