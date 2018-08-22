package com.theone.dynamicwallpaper.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author The one
 * @date 2018/5/30 0030
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class GlideUtil {
    public static  RequestOptions options = new RequestOptions()
//            .placeholder(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    public static void load(Context context,
                            String url,
                            ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
