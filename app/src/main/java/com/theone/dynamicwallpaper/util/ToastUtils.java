package com.theone.dynamicwallpaper.util;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
    private static Toast mToast;
    public static void showToast(Context context, CharSequence msg){
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
