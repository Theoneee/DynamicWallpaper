package com.theone.dynamicwallpaper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author The one
 * @date 2018/6/27 0027
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class DateUtil {


//    yyyy：年
//    MM：月
//    dd：日
//    hh：1~12小时制(1-12)
//    HH：24小时制(0-23)
//    mm：分
//    ss：秒
//    S：毫秒
//    E：星期几
//    D：一年中的第几天
//    F：一月中的第几个星期(会把这个月总共过的天数除以7)
//    w：一年中的第几个星期
//    W：一月中的第几星期(会根据实际情况来算)
//    a：上下午标识
//    k：和HH差不多，表示一天24小时制(1-24)。
//    K：和hh差不多，表示一天12小时制(0-11)。
//    z：表示时区

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
              {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
             {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        }catch (ParseException e){

        }
        return date;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
              {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String getCurrentTime(){
      return   longToString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
    }

}
