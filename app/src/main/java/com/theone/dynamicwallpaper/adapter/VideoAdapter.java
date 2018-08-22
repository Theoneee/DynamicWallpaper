package com.theone.dynamicwallpaper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.base.EntityAdapter;
import com.theone.dynamicwallpaper.bean.Video;
import com.theone.dynamicwallpaper.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends EntityAdapter<Video, VideoAdapter.ViewHolder> {

    public VideoAdapter(Context context, List<Video> videos) {
        super(context,videos);
    }
    public VideoAdapter(Context context) {
        super(context);
    }
    @Override
    public int getLayoutRes() {
        return R.layout.item_image;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position, View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, Video data) {
        if (data != null) {
            GlideUtil.load(context,data.getThumbPath(),holder.ivImage);
            holder.tvDuration.setText(data.getDuration());
        }

    }

    public class ViewHolder extends EntityAdapter.BaseHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_duration)
        TextView tvDuration;

        public ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }

}
