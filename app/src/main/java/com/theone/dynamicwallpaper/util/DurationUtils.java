package com.theone.dynamicwallpaper.util;

import java.util.Formatter;
import java.util.Locale;

public class DurationUtils {
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;


    public DurationUtils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    public  long String2Long(String str) {
        long time = 0;
        switch (str) {
            case "0s":
                break;
            case "5s":
                time = 5000;
                break;
            case "10s":
                time = 10000;
                break;
            case "15s":
                time = 15000;
                break;
            case "20s":
                time = 20000;
                break;
            case "1min":
                time = 60000;
                break;
            case "2min":
                time = 120000;
                break;
            case "3min":
                time = 180000;
                break;
            case "4min":
                time = 2400000;
                break;
            case "不限":
                time = Long.MAX_VALUE;
                break;
        }
        return time;
    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     *
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public String Time2String(int time) {
        String duration = "";
        String str = stringForTime(time);
        int len = str.length();
        if (str.startsWith("0")) {
            if (str.startsWith("00:")) {
                if (str.startsWith("00:0")) {
                    duration = StringSub(str, len - 1, len);
                } else {
                    duration = StringSub(str, len - 2, len);
                }
            } else {
                String min = str.substring(1, 2);
                duration = min + "min" + StringSub(str, len - 2, len);
            }

        } else {
            String min = str.substring(0, 2);
            duration = min + "min" + StringSub(str, len - 2, len);
        }

        return duration;
    }

    public String StringSub(String str, int start, int end) {
        return str.substring(start, end) + "s";
    }

    public long Time2Long(String time) {
        int hours = 0;
        int minuts = 0;
        int seconds = 0;
        String[] times = time.split(":");
        if (times.length == 3) {
            hours = String2Int(times[0]) * 3600;
            minuts = String2Int(times[1]) * 60;
            seconds = String2Int(times[2]);
        } else {
            minuts = String2Int(times[0]) * 60;
            seconds = String2Int(times[1]);
        }
        seconds = hours + minuts + seconds;
        return seconds * 1000;
    }

    private int String2Int(String str) {
        return Integer.parseInt(str);
    }
}
