package com.tecsun.card.common.clarencezeroutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haixia.shi
 * @description 日期操作工具类
 * @date 2017年12月7日
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    // 设置基础时间格式
    public static SimpleDateFormat dateFormat    = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat dateFormat2   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat YMDHM         = new SimpleDateFormat("yyyyMMddHHmm");
    public static SimpleDateFormat YMDHMS        = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat YMDHMS_WITH_CHN        = new SimpleDateFormat("yyyy年 MM月 dd日 HH时 mm分 ss秒");
    public static SimpleDateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");

    // 常用的格式
    public static final String Y_M_D       = "yyyy-MM-dd";
    public static final String Y_M_D_H_M   = "yyyy-MM-dd HH:mm";
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD         = "yyyyMMdd";
    public static final String ymd         = "yyyy/MM/dd";
    public static final String ymd_H_M     = "yyyy/MM/dd HH:mm";
    public static final String ymd_H_M_S   = "yyyy/MM/dd HH:mm:ss";

    public static void main(String[] args) {
        String s = "2018";
        System.out.println(isValidateyyyymmdd(s));
        // System.out.println(getYYYYMMDDFormatDateStr());
        // System.out.println(getNowYMDHMS());
        // String s1   = "20180917";
        // Date   date = DateUtils.getDateByString(s1, YMD);
        // System.out.println(date.getTime());
    }


    public static String getYYYYMMDDFormatDateStr() {
        Date now = new Date();
        return YYYYMMDD.format(now);
    }

    /**
     * @return
     * @Description 获取当前日期并以 yyyyMMddHHmm 格式返回 精确到分
     * @params
     * @author 0214
     * @createTime 2018-09-17 10:19
     * @updateTime
     */
    public static String getNowYMDHM() {
        Date now = new Date();
        return YMDHM.format(now);
    }

    public static String getNowYMDHMS() {
        Date now = new Date();
        return YMDHMS.format(now);
    }
    public static String getNowYMDHMSWithCHN() {
        Date now = new Date();
        return YMDHMS_WITH_CHN.format(now);
    }

    /**
     * @return
     * @Description 格式化日期为自定义格式
     * @params
     * @author 0214
     * @createTime 2018-09-17 10:22
     * @updateTime
     */
    public static String dateFormat(String timeStr, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = "yyyy/MM/dd HH";
        }
        Date             date = null;
        String           time = null;
        SimpleDateFormat sdf  = new SimpleDateFormat(YMD);
        try {
            date = sdf.parse(timeStr);
            time = dateFormatYMD.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static Date getDateByString(String timeStr, String pattern) {
        Date             date = null;
        SimpleDateFormat sdf  = new SimpleDateFormat(pattern);
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前的日期
     *
     * @return 以字符串格式返回当前日期
     */
    public static String todayDate1() {
        // 获取今日日期
        Date date = new Date();
        // 日期格式化为字符串
        String endTime = dateFormat.format(date);

        return endTime;
    }

    public static String todayDatemm() {
        Date date = new Date();
        return dateFormat2.format(date);
    }


    /**
     * 格式化方法(只能格式化数据为【yyyy-MM-dd、yyyy-MM-dd HH:mm:ss】)
     *
     * @param format
     * @return 以字符串格式返回格式化后的日期
     */
    public static String newDateFormat1(String selectDate, String format) {
        if (format == null || "".equals(format)) {
            format = "yyyy-MM-dd";
        }

        System.out.println(format);

        SimpleDateFormat sdf  = new SimpleDateFormat(format);
        Date             date = null;
        String           time = "";
        try {
            date = sdf.parse(selectDate);
            time = dateFormatYMD.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info("时间格式有误");
            return null;
        }

        return time;
    }

    /**
     * 格式化方法(日期的格式化只包括【yyyy-MM-dd、yyyy-MM-dd HH:mm:ss】)
     *
     * @return 以字符串格式返回格式化后的日期
     */
    public static String newDateFormat2(String selectDate, String dateFormat, String stringFormat) {
        if (dateFormat == null || "".equals(dateFormat)) {
            dateFormat = "yyyy-MM-dd";
        }
        if (stringFormat == null || "".equals(stringFormat)) {
            stringFormat = "yyyy-MM-dd";
        }

        SimpleDateFormat sdf  = new SimpleDateFormat(dateFormat);
        Date             date = null;
        String           time = "";
        try {
            date = sdf.parse(selectDate);
            sdf = new SimpleDateFormat(stringFormat);
            time = sdf.format(date);
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }

        return time;
    }

    /**
     * 判断日期是否符合“YYYY-MM-DD”格式
     *
     * @param selectDate 查询日期
     * @return 是返回true，不是返回false
     */
    public static boolean isValidDate(String selectDate) {
        try {
            Date date = dateFormat.parse(selectDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidateyyyymmdd(String selectDate) {
        try {
            YYYYMMDD.setLenient(false);
            Date date = YYYYMMDD.parse(selectDate);
            System.out.println(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查日期是否合法
     *
     * @param expDate
     * @return
     */
    public static boolean checkExpDate(String expDate) {
        Date date = null;
        try {
            date = dateFormat.parse(expDate);
            int year  = Integer.parseInt(expDate.substring(0, 4));
            int month = Integer.parseInt(expDate.substring(5, 7));
            int day   = Integer.parseInt(expDate.substring(8, 10));
            if (month > 12 || month < 1) {
                return false;
            }

            int[] monthLengths = new int[]{0, 31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            if (isLeapYear(year)) {
                monthLengths[2] = 29;
            } else {
                monthLengths[2] = 28;
            }

            int monthLength = monthLengths[month];
            if (day < 1 || day > monthLength) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.info("日期格式有误！");
            return false;
        }

    }

    /**
     * 判断是否为闰年
     *
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }

    /**
     * 返回查询日期年份
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getYear(String selectDate) {
        Date    date = null;
        Integer year = null;
        try {
            date = dateFormat.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return year;

    }

    /**
     * 返回查询日期的月
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getMonth(String selectDate) {
        Date    date  = null;
        Integer month = null;
        try {
            date = dateFormat.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = cal.get(Calendar.MONTH) + 1;

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return month;
    }

    /**
     * 返回查询日期的日
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getDay(String selectDate) {
        Date    date = null;
        Integer day  = null;
        try {
            date = dateFormat.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            day = cal.get(Calendar.DAY_OF_MONTH);

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return day;
    }

    /**
     * 返回查询日期的小时
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getHour(String selectDate) {
        Date    date = null;
        Integer hour = null;
        try {
            date = dateFormat2.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            hour = cal.get(Calendar.HOUR_OF_DAY);

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return hour;
    }

    /**
     * 返回查询日期的分钟
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getMinute(String selectDate) {
        Date    date   = null;
        Integer minute = null;
        try {
            date = dateFormat2.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            minute = cal.get(Calendar.MINUTE);

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return minute;
    }

    /**
     * 返回查询日期的秒
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getSecond2(String selectDate) {
        Date    date   = null;
        Integer second = null;
        try {
            date = dateFormat2.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            second = cal.get(Calendar.SECOND);

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return second;
    }

    /**
     * 返回查询日期的毫秒
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Long getMillis(String selectDate) {
        Date date   = null;
        Long millis = null;
        try {
            date = dateFormat2.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            millis = cal.getTimeInMillis();

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }
        return millis;
    }

    /**
     * 返回查询日期的星期 （1：星期一，2:星期二 ... 6:星期六 0:星期日）
     *
     * @param selectDate 查询日期
     * @return
     */
    public static Integer getChinaWeek(String selectDate) {
        Date    date = null;
        Integer week = null;
        try {
            date = dateFormat.parse(selectDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            week = cal.get(Calendar.DAY_OF_WEEK) - 1;

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }

        return week;
    }


    /**
     * 获取当前的日期2
     *
     * @return 以字符串格式返回当前日期
     */
    public static String todayDate2() {
        // String endTime =
        // LocalDate.now().format(DateTimeFormatter.ofPattern(Y_M_D));

        // 设置时间格式
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Y_M_D);
//
//         获取今日日期
//        LocalDate date = LocalDate.now();
        // 日期格式化为字符串
//        String endTime = dtf.format(date);

//        return endTime;
        return null;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param selectDate 查询时间
     * @return 返回一个格式化后的时间
     */
    public static String getSpecifiedDayBefore(String selectDate) {
        Calendar c    = Calendar.getInstance();
        Date     date = null;
        try {
            date = dateFormat.parse(selectDate);
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = dateFormat.format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param selectDate 查询时间
     * @return 返回一个格式化后的时间
     */
    public static String getSpecifiedDayAfter(String selectDate) {
        Calendar c    = Calendar.getInstance();
        Date     date = null;
        try {
            date = dateFormat.parse(selectDate);
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayBefore = dateFormat.format(c.getTime());
        return dayBefore;
    }

    /**
     * 获取昨天的日期1
     *
     * @return 以字符串格式返回昨天日期
     */
    public static String yesterdayDate1() {
        // 获取当前日期
        Date     date     = new Date();
        Calendar calendar = Calendar.getInstance();
        // 获取前一天日期
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        // 日期格式化为字符串
        String endTime = dateFormat.format(date);

        return endTime;
    }

    /**
     * 获取昨天的日期2
     *
     * @return 以字符串格式返回昨天日期
     */
//    public static String yesterdayDate2() {
//        // String endTime =
//        // LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // 设置时间格式
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Y_M_D);
//
//        // 获取昨天日期
//        LocalDate date = LocalDate.now().minusDays(1);
//        // 日期格式化为字符串
//        String endTime = dtf.format(date);
//
//        return endTime;
//    }

    /**
     * 获取查询日期前/后n天的日期
     *
     * @param selectDate
     * @param number
     * @return
     */
    public static String dateToNumber(String selectDate, int number) {
        Date date = new Date();

        String nowDay = "";

        try {
            Calendar cal = Calendar.getInstance();

            // 字符串转换为日期格式
            date = dateFormat.parse(selectDate);
            cal.setTime(date);
            // 减去30天
            cal.add(Calendar.DAY_OF_MONTH, number);
            date = cal.getTime();
            nowDay = dateFormat.format(date);
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }
        return nowDay;
    }

    /**
     * 获得当前月份
     *
     * @return
     */
    public static String getCurrMonth() {
        Calendar cal    = Calendar.getInstance();
        int      year   = cal.get(Calendar.YEAR);
        int      month  = cal.get(Calendar.MONTH) + 1;
        String   months = "";
        if (month < 10) {
            months = "0" + month;
        } else {
            months = month + "";
        }
        String currentMonth = year + "-" + months;
        return currentMonth;
    }

    /**
     * 获得当前月的第一天的日期
     *
     * @return
     */
    public static String getCurrMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = dateFormat.format(cal.getTime());
        return firstday;
    }

    /**
     * 获得当前月的最后一天的日期
     *
     * @return
     */
    public static String getCurrMonthLastDay() {
        Calendar cal = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        String lastday = dateFormat.format(cal.getTime());
        return lastday;
    }

    /**
     * 获取查询日期所在月的第一天
     *
     * @param selectDate 查询日期
     * @return
     */
    public static String getFirstDay(String selectDate) {
        Date   date      = null;
        String day_first = "";
        try {
            // 字符串转日期
            date = dateFormat.parse(selectDate);
            GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
            gcLast.setTime(date);
            // 获取上个月第一天
            gcLast.set(Calendar.DAY_OF_MONTH, 1);
            // 格式化日期
            day_first = dateFormat.format(gcLast.getTime());

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }

        return day_first;
    }

    /**
     * 获取查询日期所在月的最后一天
     *
     * @param selectDate 查询日期
     * @return
     */
    public static String getLastDay(String selectDate) {
        Date   date     = null;
        String day_last = "";
        try {
            // 字符串转日期
            date = dateFormat.parse(selectDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // 加一个月
            calendar.add(Calendar.MONTH, 1);
            // 设置为该月第一天
            calendar.set(Calendar.DATE, 1);
            // 再减一天即为上个月最后一天
            calendar.add(Calendar.DATE, -1);
            // 格式化日期
            day_last = dateFormat.format(calendar.getTime());

        } catch (Exception e) {
            logger.info("时间格式有误");
            return null;
        }

        return day_last;
    }

    /**
     * 获取从开始时间到当前时间经历的天数(包括开始时间和结束时间)
     *
     * @param startTime 开始时间
     * @return 返回int类型数值
     */
    public static int todayBetween(String startTime) {

        // 获取今日日期
        Date date = new Date();
        // 字符串格式化为日期
        Calendar cal   = Calendar.getInstance();
        long     time1 = 0;
        long     time2 = 0;
        try {
            cal.setTime(dateFormat.parse(startTime));
            // 获取开始时间的时间戳
            time1 = cal.getTimeInMillis();
            cal.setTime(date);
            // 获取结束时间的时间戳
            time2 = cal.getTimeInMillis();
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return -1;
        }
        // 获取时间差的天数加一天
        long between_days = (time2 - time1) / (1000 * 3600 * 24) + 1;

        // 将天数转换为int返回
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取从开始时间到结束时间经历的天数(包括开始时间和结束时间)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回int类型数值
     */
    public static int daysBetween(String startTime, String endTime) {
        // 字符串格式化为日期
        Calendar cal   = Calendar.getInstance();
        long     time1 = 0;
        long     time2 = 0;
        try {
            cal.setTime(dateFormat.parse(startTime));
            // 获取开始时间的时间戳
            time1 = cal.getTimeInMillis();
            cal.setTime(dateFormat.parse(endTime));
            // 获取结束时间的时间戳
            time2 = cal.getTimeInMillis();
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return -1;
        }
        // 获取时间差的天数加一天
        long between_days = (time2 - time1) / (1000 * 3600 * 24) + 1;

        // 将天数转换为int返回
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取两个日期之间的所有日期(不包括结束时间)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回int类型数值
     */
    public static List<String> GetDates(String startTime, String endTime) {
        Date         d1    = new Date();
        Date         d2    = new Date();
        List<String> dates = new ArrayList<String>();
        try {
            Calendar cal = Calendar.getInstance();
            d1 = dateFormat.parse(startTime);
            d2 = dateFormat.parse(endTime);
            // 如果开始时间小于结束时间则执行
            while (d1.before(d2)) {
                // 循环讲数据插入到list
                dates.add(dateFormat.format(d1));
                cal.setTime(d1);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                d1 = cal.getTime();
            }
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }

        return dates;
    }

    /**
     * 获取两个日期之间的所有日期(包括开始时间和结束时间)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回String类型的集合
     */
    public static List<String> GetDates2(String startTime, String endTime) {
        Date         d1    = new Date();
        Date         d2    = new Date();
        List<String> dates = new ArrayList<String>();
        try {
            Calendar cal = Calendar.getInstance();
            d1 = dateFormat.parse(startTime);
            d2 = dateFormat.parse(endTime);
            // 结束时间加一天
            cal.setTime(d2);
            cal.add(Calendar.DATE, +1);
            d2 = cal.getTime();
            // 如果开始时间小于结束时间则执行
            while (d1.before(d2)) {
                // 循环讲数据插入到list
                dates.add(dateFormat.format(d1));
                cal.setTime(d1);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                d1 = cal.getTime();
            }
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }

        return dates;
    }

    /**
     * 获取今日小时数（包括当前小时）
     *
     * @return 返回int类型数值
     */
    public static List<Integer> hours() {
        Calendar      cal     = Calendar.getInstance();
        Integer       curHour = cal.get(Calendar.HOUR_OF_DAY);
        List<Integer> hours   = new ArrayList<Integer>();
        for (int i = 0; i <= curHour; i++) {
            hours.add(i);
        }
        return hours;
    }

    /**
     * 判断传入的是不是当前日期
     *
     * @param date 查询时间
     * @return 是返回true，不是返回false
     */
    public static boolean currentDate(String date) {
        // 当前时间
        Date now = new Date();
        // 获取今天的日期
        String nowDay = dateFormat.format(now);

        return date.equals(nowDay);
    }

    /**
     * 获取当前日期30天前的日期
     *
     * @return 返回一个格式化后的时间
     */
    public static String currentDateTo30() {
        Calendar theCa = Calendar.getInstance();
        // 获取当前日期
        theCa.setTime(new Date());
        // 获取30天前的日期
        theCa.add(theCa.DATE, -30);
        Date   date   = theCa.getTime();
        String nowDay = dateFormat.format(date);
        return nowDay;
    }

    /**
     * 获取指定日期30天前的日期
     *
     * @return 返回一个格式化后的时间
     */
    public static String dateTo30(String selectDate) {
        Date date = new Date();

        String nowDay = "";

        try {
            Calendar cal = Calendar.getInstance();

            // 字符串转换为日期格式
            date = dateFormat.parse(selectDate);
            cal.setTime(date);
            // 减去30天
            cal.add(Calendar.DATE, -30);
            date = cal.getTime();
            nowDay = dateFormat.format(date);
        } catch (ParseException e) {
            logger.info("时间格式有误");
            return null;
        }
        return nowDay;
    }

    /**
     * 判断传入日期是否在当前时间到30天前的日期段内
     *
     * @param selectDate 查询时间
     * @return 是返回true，不是返回false
     */
    public static boolean atDateInterval(String selectDate) {
        boolean result = false;
        try {
            Date date     = new Date();
            Date dateTo30 = dateFormat.parse(currentDateTo30());
            Date sdate    = dateFormat.parse(selectDate);
            if (sdate.after(dateTo30) && sdate.before(date)) {
                result = true;
            }
        } catch (ParseException e1) {
            logger.info("时间格式有误");
            return false;
        }
        return result;
    }

    /**
     * 判断传入的日期是不是大于当前日期
     *
     * @param selectDate 查询时间
     * @return 是返回true，不是返回false
     */
    public static boolean afterToday(String selectDate) {
        boolean result = false;
        try {
            Date date  = new Date();
            Date sdate = dateFormat.parse(selectDate);
            if (sdate.after(date)) {
                result = true;
            }
        } catch (ParseException e1) {
            logger.info("时间格式有误");
            return false;
        }
        return result;
    }

    /**
     * 判断传入的两个日期的大小(仅限不相等的两个日期比较)
     *
     * @return 是返回true，不是返回false
     */
    public static boolean dateCompare(String startDate, String endDate) {
        boolean result = false;
        try {
            Date date  = dateFormat.parse(endDate);
            Date sdate = dateFormat.parse(startDate);
            if (date.after(sdate)) {
                result = true;
            }
        } catch (ParseException e1) {
            logger.info("时间格式有误");
            return false;
        }
        return result;
    }

    /**
     * 判断传入的两个日期是否相等
     *
     * @return 是返回true，不是返回false
     */
    public static boolean dateEquals(String startDate, String endDate) {
        boolean result = false;
        try {
            Date date  = dateFormat.parse(endDate);
            Date sdate = dateFormat.parse(startDate);
            if (date.equals(sdate)) {
                result = true;
            }
        } catch (ParseException e1) {
            logger.info("时间格式有误");
            return false;
        }
        return result;
    }

    /**
     * 时间检查(是否符合格式--开始时间是否小于结束时间--结束时间是否小于当前时间--开始时间是否大于系统上线时间【系统上线时间在配置文件修改】)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 正确返回true，错误返回false
     */
    public static boolean isRight(String startTime, String endTime, String systemDate) {
        boolean result = false;
        int     days   = -1;
        result = isValidDate(startTime);
        if (result) {
            result = isValidDate(endTime);
            if (result) {
                days = daysBetween(startTime, endTime);
                if (days > 0) {
                    result = afterToday(endTime);
                    if (!result) {
                        days = daysBetween(systemDate, startTime);
                        if (days > 0) {
                            result = true;
                        } else {
                            result = false;
                        }
                    } else {
                        result = false;
                    }
                } else {
                    result = false;
                }
            }
        }

        return result;
    }
}
