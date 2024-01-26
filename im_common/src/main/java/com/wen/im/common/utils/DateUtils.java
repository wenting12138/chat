package com.wen.im.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /***
     *  功能描述：日期转换cron表达式
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
    /***
     * convert Date to cron ,eg.  "0 07 10 15 1 ? 2016"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(java.util.Date  date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    /**
     * 计算 日期 date1 与 date2 之间的所相差的秒数 ,
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差秒
     */
    public static Long secondSubtraction(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }
        return secondSubtraction(date1.getTime(), date2.getTime());
    }
    public static Long secondSubtraction(Long calendar1, Long calendar2) {
        try {
            double time = (calendar1 - calendar2)/1000;
            return Math.round(time);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

        /**
         * 获取当前日期, 默认格式为yyyy-MM-dd
         *
         * @return String
         */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static List<String> genareteGapDateList(String startDay, String endDay) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        Date start = format.parse(startDay);
        Date end = format.parse(endDay);
        List<String> list = new ArrayList<>();
        list.add(startDay);
        while (end.compareTo(start) > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.DATE,1);
            start = cal.getTime();
            String item = format.format(start);
            list.add(item);
        }
        return list;
    }

    public static List<String> genareteGapDateList(Date start, Date end) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        List<String> list = new ArrayList<>();
        list.add(format.format(start));
        while (end.compareTo(start) > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.DATE,1);
            start = cal.getTime();
            String item = format.format(start);
            list.add(item);
        }
        return list;
    }

    public static String genareteDate(String startDay, int i) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        Date start = format.parse(startDay);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.add(Calendar.DATE, i);
        start = cal.getTime();
        return format.format(start);
    }


    public static boolean isBetweenTime(Date now, Date expireStartTime, Date expireStopTime) {

        //如果参数日期等于此日期，则值为0；如果此日期早于日期参数，则值小于0；如果此日期在日期参数之后，则该值大于0
        if (expireStartTime == null && expireStopTime == null) {
            return false;
        }

        if (expireStartTime == null && now.compareTo(expireStopTime) > 0) {
            // 没有开始时间 && 现在时间 > 结束时间    过期了
            return false;
        }else if (expireStartTime == null && now.compareTo(expireStopTime) <= 0){
            // 没有开始时间 && 现在时间 <= 结束时间
            return true;
        }

        if (expireStopTime == null && now.compareTo(expireStartTime) > 0) {
            // 没有结束时间 && 现在时间 > 开始时间
            return true;
        } else if (expireStopTime == null && now.compareTo(expireStartTime) <= 0){
            return false;
        }

        if (expireStartTime != null && expireStopTime != null) {
            // 如果 开始时间 > 结束时间  交换
            if (expireStartTime.compareTo(expireStopTime) > 0) {
                Date temp = new Date();
                temp = expireStartTime;
                expireStartTime = expireStopTime;
                expireStopTime = temp;
            }
            int i = now.compareTo(expireStartTime);
            int j = now.compareTo(expireStopTime);

            // 现在时间 >= 开始时间   现在时间 <= 结束时间
            if (i >= 0 && j <= 0) {
                return true;
            }
        }
        return false;
    }



    public static void main(String[] args) throws ParseException {

        List<String> list = genareteGapDateList("2022-09-01", "2022-11-02");
        System.out.println(list);
    }
}
