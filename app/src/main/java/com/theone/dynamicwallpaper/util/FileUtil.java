package com.theone.dynamicwallpaper.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author The one
 * @date 2018/6/28 0028
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class FileUtil {
    /**
     * 删除已存储的文件
     */
    public static void deleteFile(Context context,String fileName) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        String where = MediaStore.Video.Media.DATA + "='" +fileName + "'";
        //删除视频
        mContentResolver.delete(uri, where, null);
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }
            } catch (Exception e) {
            return false;
        }
        return true;
    }
}
