package com.theone.dynamicwallpaper.service;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.ui.MemorialActivity;
import com.theone.dynamicwallpaper.util.SharedPreferencesUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author The one
 * @date 2018/7/4 0004
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class Memorial extends AppWidgetProvider {
    public static final String TAG = "Memorial";
    public static final String INTENT = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final Intent SERVICE_INTENT =
            new Intent(INTENT);

    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate: ");
        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
        updateAppWidget(context, appWidgetManager, idsSet);
    }

    // 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用
    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        Log.e(TAG, "onAppWidgetOptionsChanged");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
                newOptions);
    }

    // widget被删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted(): appWidgetIds.length=" + appWidgetIds.length);
        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        super.onDeleted(context, appWidgetIds);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Set set) {
        Log.e(TAG, "updateAppWidget: ");
        // widget 的id
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();
        while (it.hasNext()) {
            appID = ((Integer) it.next()).intValue();
            // 获取 example_appwidget.xml 对应的RemoteViews
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
            if (null == sharedPreferencesUtil)
                sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
            String start = sharedPreferencesUtil.getString(MemorialActivity.TIME,"2016-7-2");
            String des = sharedPreferencesUtil.getString(MemorialActivity.DES, "已相恋");
            views.setTextViewText(R.id.appwidget_text, getDays(start));
            views.setTextViewText(R.id.tv_des, des);
            String icon1 = sharedPreferencesUtil.getString(MemorialActivity.ICON1);
            String icon2 = sharedPreferencesUtil.getString(MemorialActivity.ICON2);
            try {
                if (!icon1.isEmpty())
                    views.setImageViewBitmap(R.id.icon1, getCircleBitmap(icon1));
                if (!icon2.isEmpty())
                    views.setImageViewBitmap(R.id.icon2, getCircleBitmap(icon2));
            } catch (FileNotFoundException e) {
            }
            // 更新 widget
            appWidgetManager.updateAppWidget(appID, views);
        }
    }

    // 第一个widget被创建时调用
    @Override
    public void onEnabled(Context context) {
        Log.e(TAG, "onEnabled");
        // 在第一个 widget 被创建时，开启服务
        SERVICE_INTENT.setPackage("com.theone.dynamicwallpaper");
        context.startService(SERVICE_INTENT);
        super.onEnabled(context);
    }

    // 最后一个widget被删除时调用
    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled");
        // 在最后一个 widget 被删除时，终止服务
        SERVICE_INTENT.setPackage("com.theone.dynamicwallpaper");
        context.stopService(SERVICE_INTENT);
        super.onDisabled(context);
    }

    SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.e(TAG, "onReceive: " + action);
        if (action.equals(INTENT))
            updateAppWidget(context, AppWidgetManager.getInstance(context), idsSet);
        super.onReceive(context, intent);
    }

    String getDays(String start) {
        String days = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(start);
            Date d2 = new Date(System.currentTimeMillis());
            GregorianCalendar cal1 = new GregorianCalendar();
            GregorianCalendar cal2 = new GregorianCalendar();
            cal1.setTime(d1);
            cal2.setTime(d2);
            int dayCount = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24));//从间隔毫秒变成间隔天数
            days = dayCount + "";
        } catch (Exception e) {
        }
        return days;
    }

    /**
     * 把图片裁剪成圆形
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getCircleBitmap(String path) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        if (bitmap == null) {
            return null;
        }
        try {
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(circleBitmap);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            float roundPx = 0.0f;
            roundPx = bitmap.getWidth();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return circleBitmap;
        } catch (Exception e) {
            return bitmap;
        }
    }

}
