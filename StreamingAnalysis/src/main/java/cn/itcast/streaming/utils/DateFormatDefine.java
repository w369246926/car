package cn.itcast.streaming.utils;

/**
 * 定义时间日期格式的枚举类
 * 定义常量：
 * 1：yyyy-MM-dd HH:mm:ss
 * 2：yyyyMMdd
 */
public enum  DateFormatDefine {
    //定义时间格式的常量
    DATE_TIME_FORMAT("yyyy-MM-dd HH:mm:ss"),
    //定义日期格式的常量
    DATE_FORMAT("yyyyMMdd");

    //定义变量接收常量的参数
    private String format;

    /**
     * 定义构造方法，目的是给常量进行赋值
     * @param format
     */
    DateFormatDefine(String format){
        this.format = format;
    }

    /**
     * 返回常量定义的参数
     * @return
     */
    public String getFormat(){ return  this.format; }
}
