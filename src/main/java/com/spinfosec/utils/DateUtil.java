package com.spinfosec.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil
{

    // 用来全局控制 上一周，本周，下一周的周数变化
    private static int weeks = 0;
    // private static int MaxDate;//一月最大天数
    private static int MaxYear;// 一年最大天数

    public final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public final static String MONTH_FORMAT_PATTERN = "YYYY-MM";

    public final static String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String CUSTOM_FORMAT_PATTERN = "yyyy-MM-dd.HH-mm-ss";

    public static void main(String[] args)
    {
        System.out.println("获取当天日期:" + dateToString(getNowTime(), DATE_FORMAT_PATTERN));
        System.out.println("获取本周一日期:" + dateToString(getMondayOFWeek(), DATE_FORMAT_PATTERN));
        System.out.println("获取本周日的日期~:" + dateToString(getCurrentWeekday(), DATE_FORMAT_PATTERN));
        System.out.println("获取上周一日期:" + dateToString(getPreviousWeekMonday(), DATE_FORMAT_PATTERN));
        System.out.println("获取上周日日期:" + dateToString(getPreviousWeekSunday(), DATE_FORMAT_PATTERN));
        System.out.println("获取下周一日期:" + dateToString(getNextMonday(), DATE_FORMAT_PATTERN));
        System.out.println("获取下周日日期:" + dateToString(getNextSunday(), DATE_FORMAT_PATTERN));
        System.out.println("获得相应周的周六的日期:" + dateToString(getNowTime(), DATE_FORMAT_PATTERN));
        System.out.println("获取本月第一天日期:" + dateToString(getFirstDayOfMonth(), DATE_FORMAT_PATTERN));
        System.out.println("获取本月最后一天日期:" + dateToString(getLastDayOfMonth(), DATE_FORMAT_PATTERN));
        System.out.println("获取上月第一天日期:" + dateToString(getPreviousMonthFirst(), DATE_FORMAT_PATTERN));
        System.out.println("获取上月最后一天的日期:" + dateToString(getPreviousMonthEnd(), DATE_FORMAT_PATTERN));
        System.out.println("获取下月第一天日期:" + dateToString(getNextMonthFirst(), DATE_FORMAT_PATTERN));
        System.out.println("获取下月最后一天日期:" + dateToString(getNextMonthEnd(), DATE_FORMAT_PATTERN));
        System.out.println("获取本年的第一天日期:" + dateToString(getCurrentYearFirst(), DATE_FORMAT_PATTERN));
        System.out.println("获取本年最后一天日期:" + dateToString(getCurrentYearEnd(), DATE_FORMAT_PATTERN));
        System.out.println("获取去年的第一天日期:" + dateToString(getPreviousYearFirst(), DATE_FORMAT_PATTERN));
        System.out.println("获取去年的最后一天日期:" + dateToString(getPreviousYearEnd(), DATE_FORMAT_PATTERN));
        System.out.println("获取明年第一天日期:" + dateToString(getNextYearFirst(), DATE_FORMAT_PATTERN));
        System.out.println("获取明年最后一天日期:" + dateToString(getNextYearEnd(), DATE_FORMAT_PATTERN));
        System.out.println("获取本季度第一天:" + dateToString(getFirstDayOfSeason(), DATE_FORMAT_PATTERN));
        System.out.println("获取本季度最后一天:" + dateToString(getLastDayOfSeason(), DATE_FORMAT_PATTERN));
        System.out.println("获取上季度第一天:" + dateToString(getFirstDayOfPreviousSeason(), DATE_FORMAT_PATTERN));
        System.out.println("获取上季度最后一天:" + dateToString(getLastDayOfPreviousSeason(), DATE_FORMAT_PATTERN));

    }

    /**
     * 获取当前的月份
     * 
     * @return
     */
    public static int getCurrMonth()
    {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 功能: 将日期对象按照某种格式进行转换，返回转换后的字符串
     * 
     * @param date 日期对象
     * @param pattern 转换格式 例：yyyy-MM-dd
     */
    public static String dateToString(Date date, String pattern)
    {
        String strDateTime = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        strDateTime = date == null ? null : formater.format(date);
        return strDateTime;
    }

    /**
     * 功能: 将插入的字符串按格式转换成对应的日期对象
     * 
     * @param str 字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date stringToDate(String str, String pattern)
    {
        Date dateTime = null;
        try
        {
            if (str != null && !str.equals(""))
            {
                SimpleDateFormat formater = new SimpleDateFormat(pattern);
                dateTime = formater.parse(str);
            }
        }
        catch (Exception ex)
        {
        }
        return dateTime;
    }

    /**
     * 功能: 将传入的字符串按yyyy-MM-dd HH:mm:ss格式转换成对应的日期对象
     * 
     * @param str 需要转换的字符串
     * @return Date
     */
    public static Date strToDateTime(String str)
    {
        return stringToDate(str, DATETIME_FORMAT_PATTERN);
    }

    /**
     * 功能: 将传入的字符串转换成对应的Timestamp对象
     * 
     * @param str 待转换的字符串
     * @return Timestamp 转换之后的对象
     * @throws Exception Timestamp
     */
    public static Timestamp stringToDateHMS(String str) throws Exception
    {
        Timestamp time = null;
        time = Timestamp.valueOf(str);
        return time;
    }

    /**
     * 功能: 根据传入的年月日返回相应的日期对象
     * 
     * @param year 年份
     * @param month 月份
     * @param day 天
     * @return Date 日期对象
     */
    public static Date ymdToDate(int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * 得到日期的最大时间
     * 
     * @param date
     * @return
     */
    public static Date getMaxDateOfDay(Date date)
    {
        if (date == null)
        {
            return null;
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, calendar.getActualMaximum(11));
            calendar.set(12, calendar.getActualMaximum(12));
            calendar.set(13, calendar.getActualMaximum(13));
            calendar.set(14, calendar.getActualMaximum(14));
            return calendar.getTime();
        }
    }

    /**
     * 得到日期的最小时间
     * 
     * @param date
     * @return
     */
    public static Date getMinDateOfDay(Date date)
    {
        if (date == null)
        {
            return null;
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, calendar.getActualMinimum(11));
            calendar.set(12, calendar.getActualMinimum(12));
            calendar.set(13, calendar.getActualMinimum(13));
            calendar.set(14, calendar.getActualMinimum(14));
            return calendar.getTime();
        }
    }

    /**
     * 功能：返回传入日期对象（date）之后afterDays天数的日期对象
     * 
     * @param date 日期对象
     * @param afterDays 往后天数
     * @return java.util.Date 返回值
     */
    public static Date getAfterDay(Date date, int afterDays)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, afterDays);
        return cal.getTime();
    }

    /**
     * 功能：返回传入日期对象（date）之前beforeDays天数的日期对象
     * 
     * @param date
     * @param beforeDays
     * @return
     */
    public static Date getBeforeDays(Date date, int beforeDays)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -beforeDays);
        return cal.getTime();
    }

    /**
     * 功能: 返回date1与date2相差的天数
     * 
     * @param date1
     * @param date2
     * @return long
     */
    public static long dateDiff(Date date1, Date date2)
    {
        return ((date1.getTime() - date2.getTime()) / 3600 / 24 / 1000);
    }

    // min
    /**
     * 功能: 返回date1与date2相差的分钟数
     * 
     * @param date1
     * @param date2
     * @return int
     */
    public static int MinDiff(Date date1, Date date2)
    {
        int i = (int) ((date1.getTime() - date2.getTime()) / 1000 / 60);
        return i;
    }

    // second
    /**
     * 功能: 返回date1与date2相差的秒数
     * 
     * @param date1
     * @param date2
     * @return int
     */
    public static int TimeDiff(Date date1, Date date2)
    {
        int i = (int) ((date1.getTime() - date2.getTime()));
        return i;
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     * 
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate)
    {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * 
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    // 计算当月最后一天,返回字符串
    public static Date getLastDayOfMonth()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return lastDate.getTime();
    }

    // 上月第一天
    public static Date getPreviousMonthFirst()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        return lastDate.getTime();
    }

    // 获取当月第一天
    public static Date getFirstDayOfMonth()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        return lastDate.getTime();
    }

    // 获得本周星期日的日期
    public static Date getCurrentWeekday()
    {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        return monday;
    }

    // 获取当天时间
    public static Date getNowTime()
    {
        return new Date();
    }

    // 获得当前日期与本周日相差的天数
    private static int getMondayPlus()
    {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1)
        {
            return 0;
        }
        else
        {
            return 1 - dayOfWeek;
        }
    }

    // 获得本周一的日期
    public static Date getMondayOFWeek()
    {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        return monday;
    }

    // 获得相应周的周六的日期
    public static Date getSaturday()
    {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        return monday;
    }

    // 获得上周星期日的日期
    public static Date getPreviousWeekSunday()
    {
        weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        return monday;
    }

    // 获得上周星期一的日期
    public static Date getPreviousWeekMonday()
    {
        weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        return monday;
    }

    // 获得下周星期一的日期
    public static Date getNextMonday()
    {
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        return monday;
    }

    // 获得下周星期日的日期
    public static Date getNextSunday()
    {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        return monday;
    }

    // public static int getMonthPlus() {
    // Calendar cd = Calendar.getInstance();
    // int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
    // cd.set(Calendar.DATE, 1);//把日期设置为当月第一天
    // cd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
    // MaxDate = cd.get(Calendar.DATE);
    // if (monthOfNumber == 1) {
    // return -MaxDate;
    // } else {
    // return 1 - monthOfNumber;
    // }
    // }

    // 获得上月最后一天的日期
    public static Date getPreviousMonthEnd()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        return lastDate.getTime();
    }

    // 获得下个月第一天的日期
    public static Date getNextMonthFirst()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        return lastDate.getTime();
    }

    // 获得下个月最后一天的日期
    public static Date getNextMonthEnd()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        return lastDate.getTime();
    }

    // 获得明年最后一天的日期
    public static Date getNextYearEnd()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        return lastDate.getTime();
    }

    // 获得明年第一天的日期
    public static Date getNextYearFirst()
    {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        return lastDate.getTime();

    }

    // 获得本年有多少天
    public static int getMaxYear()
    {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private static int getYearPlus()
    {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1)
        {
            return -MaxYear;
        }
        else
        {
            return 1 - yearOfNumber;
        }
    }

    // 获得本年第一天的日期
    public static Date getCurrentYearFirst()
    {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        return yearDay;
    }

    // 获得本年最后一天的日期 *
    public static Date getCurrentYearEnd()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return strToDate(years + "-12-31");
    }

    // 获得上年第一天的日期 *
    public static Date getPreviousYearFirst()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return strToDate(years_value + "-1-1");
    }

    // 获得上年最后一天的日期
    public static Date getPreviousYearEnd()
    {
        weeks--;
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return strToDate(preYearDay);
    }

    /**
     * 获取本季度第一天
     * 
     * @param month
     * @return
     */
    public static Date getFirstDayOfSeason()
    {
        int month = getCurrMonth();
        int array[][] = { {1, 2, 3 }, {4, 5, 6 }, {7, 8, 9 }, {10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3)
        {
            season = 1;
        }
        if (month >= 4 && month <= 6)
        {
            season = 2;
        }
        if (month >= 7 && month <= 9)
        {
            season = 3;
        }
        if (month >= 10 && month <= 12)
        {
            season = 4;
        }
        int start_month = array[season - 1][0];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy"); // 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        int start_days = 1;
        return strToDate(years_value + "-" + start_month + "-" + start_days);
    }

    /**
     * 获取本季度最后一天
     * 
     * @param month
     * @return
     */
    public static Date getLastDayOfSeason()
    {
        int month = getCurrMonth();
        int array[][] = { {1, 2, 3 }, {4, 5, 6 }, {7, 8, 9 }, {10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3)
        {
            season = 1;
        }
        if (month >= 4 && month <= 6)
        {
            season = 2;
        }
        if (month >= 7 && month <= 9)
        {
            season = 3;
        }
        if (month >= 10 && month <= 12)
        {
            season = 4;
        }
        int end_month = array[season - 1][2];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        int end_days = getLastDayOfMonth(years_value, end_month);
        return strToDate(years_value + "-" + end_month + "-" + end_days);
    }

    /**
     * 获取上季度第一天
     * 
     * @param month
     * @return
     */
    public static Date getFirstDayOfPreviousSeason()
    {
        int month = getCurrMonth();
        int array[][] = { {1, 2, 3 }, {4, 5, 6 }, {7, 8, 9 }, {10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3)
        {
            season = 4;
        }
        if (month >= 4 && month <= 6)
        {
            season = 1;
        }
        if (month >= 7 && month <= 9)
        {
            season = 2;
        }
        if (month >= 10 && month <= 12)
        {
            season = 3;
        }
        int start_month = array[season - 1][0];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        if (4 == season)
        {
            years_value = years_value - 1;
        }
        int start_days = 1;
        return strToDate(years_value + "-" + start_month + "-" + start_days);
    }

    /**
     * 获取本季度最后一天
     * 
     * @param month
     * @return
     */
    public static Date getLastDayOfPreviousSeason()
    {
        int month = getCurrMonth();
        int array[][] = { {1, 2, 3 }, {4, 5, 6 }, {7, 8, 9 }, {10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3)
        {
            season = 4;
        }
        if (month >= 4 && month <= 6)
        {
            season = 1;
        }
        if (month >= 7 && month <= 9)
        {
            season = 2;
        }
        if (month >= 10 && month <= 12)
        {
            season = 3;
        }
        int end_month = array[season - 1][2];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        if (4 == season)
        {
            years_value = years_value - 1;
        }
        int end_days = getLastDayOfMonth(years_value, end_month);
        return strToDate(years_value + "-" + end_month + "-" + end_days);
    }

    /**
     * 获取某年某月的最后一天
     * 
     * @param year 年
     * @param month 月
     * @return 最后一天
     */
    private static int getLastDayOfMonth(int year, int month)
    {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
        {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11)
        {
            return 30;
        }
        if (month == 2)
        {
            if (isLeapYear(year))
            {
                return 29;
            }
            else
            {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     * 
     * @param year 年
     * @return
     */
    public static boolean isLeapYear(int year)
    {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 判断时间是否在预定的时间段
     * 
     * @param date
     * @param startHour
     * @param startMinute
     * @param endHour
     * @param endMinute
     * @return
     */
    public static boolean compareTime(Date date, int startHour, int startMinute, int endHour, int endMinute)
    {
        boolean result = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if (startHour < endHour)
        {
            if (hour >= startHour && hour <= endHour)
            {
                result = true;
            }
        }
        else if (startHour == endHour)
        {
            if (minute >= startMinute && minute <= endMinute)
            {
                result = true;
            }
        }
        return result;
    }
}
