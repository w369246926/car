package cn.itcast.streaming.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理的工具类
 * 需要实现的方法：
 * 1）直接获取当前的时间，格式："yyyy-MM-dd HH:mm:ss"
 * 2）直接获取当前的日期，格式："yyyyMMdd"
 * 3）字符串日期转换方法，传入的参数格式："yyyy-MM-dd HH:mm:ss"，转换成data类型
 * 4）字符串日期转换方法，传入的参数格式："yyyy-MM-dd HH:mm:ss"，返回"yyyyMMdd"类型
 */
public class DateUtil {

    /**
     * 1）直接获取当前的时间，格式："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurrentDateTime(){
        return new SimpleDateFormat(DateFormatDefine.DATE_TIME_FORMAT.getFormat()).format(new Date());
    }

    /**
     * 2）直接获取当前的日期，格式："yyyyMMdd"
     * @return
     */
    public static String getCurrentDate(){
        return new SimpleDateFormat(DateFormatDefine.DATE_FORMAT.getFormat()).format(new Date());
    }

    /**
     * 3）字符串日期转换方法，传入的参数格式："yyyy-MM-dd HH:mm:ss"，转换成data类型
     * @param dateStr
     * @return
     */
    public static Date convertStringToDate(String dateStr){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormatDefine.DATE_TIME_FORMAT.getFormat());
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 4）字符串日期转换方法，传入的参数格式："yyyy-MM-dd HH:mm:ss"，返回"yyyyMMdd"类型
     * @param timeStr
     * @return
     */
    public static String convertStringToDateString(String timeStr){
        String dateStr = null;
        try {
            Date date = new SimpleDateFormat(DateFormatDefine.DATE_TIME_FORMAT.getFormat()).parse(timeStr);
            dateStr = new SimpleDateFormat(DateFormatDefine.DATE_FORMAT.getFormat()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
