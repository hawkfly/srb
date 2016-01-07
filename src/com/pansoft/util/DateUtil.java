package com.pansoft.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
    static SimpleDateFormat formattime = new SimpleDateFormat("yyyyMMddHHmmss"); 

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getToday(){
		Calendar cal = Calendar.getInstance();
	    String today = format.format(cal.getTime());
        return today;
	}
	/**
	 * 当周起始
	 * @return
	 */
	public static String getStartWeekDate(){
		Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
 	    String WeekOfFirst = format.format(currentDate.getTime());
 	    return WeekOfFirst;
	}
	/**
	 * 当月起始
	 * @return
	 */
	public static String getStartMonthDate(){
		Calendar c = Calendar.getInstance();   
	    c.add(Calendar.MONTH, 0);
	    c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
	    String MonthOfFirst = format.format(c.getTime());
        return MonthOfFirst;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		Calendar cal = Calendar.getInstance();
	    String currentTime = formattime.format(cal.getTime());
        return currentTime;
	}
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	private static SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	/**
	 * 获取当前年月日
	 * @return
	 */
	public static String yyyyMMdd(){
		return yyyyMMdd.format(new Date());
	}
	/**
	 * 获取当前年月
	 * @return
	 */
	public static String yyyyMM(){
		return yyyyMM.format(new Date());
	}
	/**
	 * 获取当前年份
	 * @return
	 */
	public static String yyyy(){
		return yyyy.format(new Date());
	}
	/**
	 * 获取当前年份的一月一号
	 * @return
	 */
	public static String yyyy0101(){
		return yyyy.format(new Date())+"0101";
	}
	/**
	 * 获取当前月的第一天
	 * @return
	 */
	public static String yyyyMM01(){
		return yyyyMM.format(new Date())+"01";
	}
	/**
	 * 获取当前月最后一天
	 * @return
	 */
	public static String yyyyMM31(){
		int monthDay[] = {31,28,31,30,31,30,31,31,30,31,30,31};
		String yyyymm = yyyyMM.format(new Date());
		int month = Integer.parseInt(yyyymm.substring(4, 6));
		int year = Integer.parseInt(yyyy.format(new Date()));
		if((year%4==0 && year%100 != 0)|| year%400==0 ){
			monthDay[1]++;
		}
		
		return yyyymm+monthDay[month];
	}
	/**
	 * 获取当前日期
	 * @param 月份的增减
	 * @return
	 */
	public static String yyyyMMdd(int monthZJ){
		Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(Calendar.MONTH, monthZJ);
        return yyyyMMdd.format(calender.getTime());
	}
}
