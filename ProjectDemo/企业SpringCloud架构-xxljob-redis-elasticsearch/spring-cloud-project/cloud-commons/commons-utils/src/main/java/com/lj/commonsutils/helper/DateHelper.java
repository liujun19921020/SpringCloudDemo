package com.lj.commonsutils.helper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期 帮助API
 */
public class DateHelper {
    private static Logger logger = LoggerFactory.getLogger(DateHelper.class);

    /**
     * 日期格式 时分秒
     */
    public static final String HH_MM_SS = "HHmmss";

    /**
     * 日期格式 年-月-日 时:分:秒
     */
    public static final String DATA_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式 年-月-日
     */
    public static final String DATA_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式 年/月
     */
    public static final String DATAYYYYMM = "yyyy/MM";

    /**
     * 日期格式 年-月
     */
    public static final String DEFAAULT_DATAYYMM = "yyyy-MM";


    /**
     * 日期格式 年/月/日
     */
    public static final String DATAYYYYMMDD = "yyyy/MM/dd";
    /**
     * 日期格式 年
     */
    public static final String YEAR_FORMAT = "yyyy";

    /**
     *  默认时间格式 年-月-日 时:分:秒 毫秒
     */
    public static final String DEFUALT_TIMESTAMPE_FORMAT = "yyyy-MM-dd hh:mm:ss sss";

    /**
     * 日期格式化
     */
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_YYYY_MM_DD);

    /**
     * 24小时时间正则表达式
     */
    public static final String MATCH_TIME_24 = "(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]";

    /**
     * 日期正则表达式
     */
    public static final String VALATE_YYYYMMDD = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";



    /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     *
     * @param time
     * @return
     */
    public static Integer StringToTimestamp(String time) {

        int times = 0;
        try {
            times = (int) ((Timestamp.valueOf(time).getTime()) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (times == 0) {
           logger.info("String转10位时间戳失败");
        }
        return times;
    }

    /**
     * 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
     * @param time
     * @return
     */
    public static String timestampToString(Integer time){
        long temp = (long)time*1000;
        Timestamp ts = new Timestamp(temp);
        String tsStr = "";
        DateFormat dateFormat = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS);
        try {
            tsStr = dateFormat.format(ts);
            logger.info(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    /**
     * 10位时间戳转Date
     * @param time
     * @return
     */
    public static Date TimestampToDate(Integer time){
        long temp = (long)time*1000;
        Timestamp ts = new Timestamp(temp);
        Date date = new Date();
        try {
            date = ts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date类型转换为10位时间戳
     * @param time
     * @return
     */
    public static Integer DateToTimestamp(Date time){
        Timestamp ts = new Timestamp(time.getTime());
        return (int) ((ts.getTime())/1000);
    }


    /**
     * 获取年月日数据
     * @param time
     * @return
     */
    public static String getDataYyyyMmDd(String time){
        String result = "";
        try{
            if(StringHelper.isNotBlank(time)){
                time = time.replaceAll("-","/");
            }
            SimpleDateFormat sdf = new SimpleDateFormat(DATAYYYYMMDD);
            result = sdf.format(formatDate2(time));
        }catch (Exception e){
            e.printStackTrace();
        }
       return  result;
    }

    /**
     * 获取年月数据
     * @param time
     * @return
     */
    public static String getDataYyyyMm(String time){
        String result = "";
        try{
            if(StringHelper.isNotBlank(time)){
                time = time.replaceAll("-","/");
            }
            SimpleDateFormat sdf = new SimpleDateFormat(DATAYYYYMM);
            result = sdf.format(formatDate2(time));
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * 日期转换
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date formatDate1(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat(DATA_YYYY_MM_DD);
        return df.parse(date);
    }

    /**
     * 日期转换
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date formatDate2(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DATAYYYYMMDD);
        return df.parse(date);
    }


    /**
     * 日期转换
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date formatDate3(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS);
        return df.parse(date);
    }

    /**
     * 日期校正
     * @param data
     * @return
     */
    public static boolean validate(String data){
        boolean flags = true;
        if(StringHelper.isNotBlank(data)){
            data = data.replaceAll("/","-");
        }
        SimpleDateFormat df = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS);
        try {
            df.setLenient(false);
            df.parse(data);
        } catch (Exception exception) {
            flags = false;
        }
        return flags;
    }

    /**
     * 日期比较
     * @param startDate
     * @param endDate
     * @return
     */
    public static int compareDate(String startDate, String endDate) {
        SimpleDateFormat df = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS);
        try {
            Date dt1 = df.parse(startDate);
            Date dt2 = df.parse(endDate);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 当天年月日
     * @return
     */
    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
       return simpleDateFormat.format(c.getTime());
    }

    /**
     * 当天开始时间
     * @return
     */
    public static String getTodayStartDate(){
        return getCurrentDate()+" 00:00:00";
    }

    /**
     * 当天结束时间
     * @return
     */
    public static String getTodayEndDate(){
        return getCurrentDate()+" 23:59:59";
    }

    /**
     * 取得指定格式的时间格式字符串
     * @return 现在时间
     */
    public static String getDateStringByFormat(String format) {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(today);
    }

    /**
     * 将传入的时间转换为指定时间格式字符串.
     * @param time 传入时间
     * @param dateFormat 时间格式
     * @return 时间字符串
     */
    public static String formatTimeStamp(Timestamp time, String dateFormat) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }

    /**
     * 取得现在时间.
     * @return 现在时间
     */
    public static Timestamp getCurrentSqlTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }



    /**
     * 转日期
     * @param lo
     * @return
     */
    public static String longToStrDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS);
        return sd.format(date);
    }

    /**
     * 转日期
     * @param lo
     * @return
     */
    public static Date longToDate(long lo){
        return new Date(lo);
    }



    /**
     * 取得今天日期字符串.
     * @return 今天日期字符串
     */
    public static String getCurrentDay() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_YYYY_MM_DD);
        return sdf.format(today);
    }

    /**
     * 将传入的时间转换为默认时间格式字符串.
     * @param time 传入时间
     * @return 时间字符串
     */
    public static String formatTimeStampDefualt(Timestamp time) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFUALT_TIMESTAMPE_FORMAT);
        return sdf.format(time);
    }

    /**
     * 将传入日期转换为默认格式字符串.
     * @param date 传入日期
     * @return 日期字符串
     */
    public static String formatDateDefault(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_YYYY_MM_DD);
        return sdf.format(date);
    }

    /**
     * 功能描述: 根据指定的格式化规则返回当前日期
     *
     * @param formater
     */
    public static String getCurrentDate(String formater) {
        SimpleDateFormat format = new SimpleDateFormat(formater);
        return format.format(new Date());
    }

    /**
     * 将传入日期转换为指定格式字符串.
     *
     * @param date 传入日期
     * @param dateFormat 时间格式
     * @return 日期字符串
     */
    public static String formatDateByDateFormate(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 取得当前年 .
     * @return 当前年
     */
    public static String getCurrentYear() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_FORMAT);
        return sdf.format(today);
    }

    /**
     * 计算距今天指定天数的日期
     * @param days
     * @return
     */
    public static String getDateAfterDays(int days) {
        Calendar date = Calendar.getInstance();// today
        date.add(Calendar.DATE, days);
        SimpleDateFormat simpleDate = new SimpleDateFormat(DATA_YYYY_MM_DD);
        return simpleDate.format(date.getTime());
    }

    /**
     * 在指定的日期的前几天或后几天
     * @param source 源日期(yyyy-MM-dd)
     * @param days 指定的天数,正负皆可
     * @return
     * @throws ParseException
     */
    public static String addDays(String source, int days) {
        Date date = null;
        try {
            date = formatDate1(source);
        } catch (ParseException e) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_YYYY_MM_DD);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 24小时时间校验
     * @param time
     * @return
     */
     public static boolean isValidate24(String time){
         Pattern p = Pattern.compile(MATCH_TIME_24);
         return p.matcher(time).matches();
     }

    /**
     * 日期校验
     * @param date
     * @return
     */
    public static boolean isDate(String date){
        Pattern pat = Pattern.compile(VALATE_YYYYMMDD);
        Matcher mat = pat.matcher(date);
        boolean dateType = mat.matches();
        return dateType;
    }


}
