package com.platform.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前日期字符串（yyyy-MM-dd）
     * @Title: getDate
     * @Description: TODO  
     * @param: @return      
     * @return: String
     * @author: sunshine  
     * @throws
     */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	/**
	 * 获取当前日期字符串（yyyy-MM-dd HH:mm:ss）
	 * @Title: getDateTime
	 * @Description: TODO  
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String getDateTime() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}
	
	
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
    /**
     * 格式化日期(默认yyyy-MM-dd)
     * @Title: formatDate
     * @Description: TODO  
     * @param: @param date
     * @param: @param pattern
     * @param: @return      
     * @return: String
     * @author: sunshine  
     * @throws
     */
	public static String formatDate(Date date, String... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0]);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 *格式化日期(yyyy-MM-dd HH:mm:ss)
	 * @Title: formatDateTime
	 * @Description: TODO  
	 * @param: @param date
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
    /**
     * 字符串转化为日期
     * @Title: parseDate
     * @Description: TODO  
     * @param: @param str
     * @param: @return      
     * @return: Date
     * @author: sunshine  
     * @throws
     */
	public static Date parseDate(Object str) {
		if (str==null || StringUtils.isEmpty(str.toString())){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	 /**
	  * 是否闰年
	  * @param year
	  * @return
      */
    public boolean isLeapYear(int year) {
        boolean leap;
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    	leap = true;
                    }
                else {
                	leap = false;
                }
            }
            else {
            	leap = true;
            }
        }
        else {
        	leap = false;
        }
        return leap;
    }
    /**
     * 根据身份证号码计算生日
     * @param: @param identity
     */
    public static Date parseToBirthday(String identity){
    	try {
        	String birthday = identity.substring(6,14);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		return sdf.parse(birthday);
		} catch (Exception e) {
			return null;
		}
    }
    /**
     * 根据身份证号码计算年龄
     * @param: @param identity

     */
    public static Integer parseToAge(String identity){
    	try {
    	Date birthDay=parseToBirthday(identity);
    	Calendar cal = Calendar.getInstance();
    	if (cal.before(birthDay)) {
                throw new IllegalArgumentException("出生时间大于当前时间!");
          }
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            
            cal.setTime(birthDay);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH)+1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                } else {
                    age--;
                }
            }
            if(age<0){
            	return null;
            }
            return age;
		} catch (Exception e) {
			return null;
		}
    }
    
    /**
     * 获取本周周一(以星期一开始)
     */
    public static Date getMondayOfCurrentWeek(){
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);//本周第一天，以星期日开始
    	return c.getTime();
    }
    
    /**
     * 获取本周周日(以星期一开始)
     */
    public static Date getSundayOfCurrentWeek(){
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
    	c.add(Calendar.DAY_OF_WEEK,1);
    	return c.getTime();
    }
    
    /**
     * 获取本月第一天
     * @Title: getDayOfCurrentMonth
     */
    public static Date getFirstDayOfCurrentMonth(){
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_MONTH,1);
    	return c.getTime();
    }
    
    /**
     * 获取本月最后一天
     */
    public static Date getLastDayOfCurrentMonth(){
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.MONTH, 1);
    	c.set(Calendar.DAY_OF_MONTH, 1);
    	c.add(Calendar.DAY_OF_MONTH, -1);
    	return c.getTime();
    }
    
    /**
     *根据末次月经时间计算预产期
     *@param lmp 末次月经
     */
    public static String YCQ(Date lmp){
    	Calendar  cal = Calendar.getInstance();
    	cal.setTime(lmp);
    	if(cal.get(Calendar.MONTH) > 3){
    		cal.add(Calendar.MONTH,-3);
    		cal.add(Calendar.YEAR,1);
    	}else{
    		cal.add(Calendar.MONTH,9);
    	}
    	cal.add(Calendar.DAY_OF_MONTH,7);
    	return formatDate(cal.getTime(), "yyyy-MM-dd");
    }
    
    /**
     *根据预产期推算末次月经时间
     *@param edc
     */
    public static String MCYJ(Date edc){
    	Calendar  cal = Calendar.getInstance();
    	cal.setTime(edc);
    	if(cal.get(Calendar.MONTH) < 10){
    		cal.add(Calendar.MONTH,3);
    		cal.add(Calendar.YEAR,-1);
    	}else{
    		cal.add(Calendar.MONTH,-9);
    	}
    	cal.add(Calendar.DAY_OF_MONTH,-7);
    	return formatDate(cal.getTime(), "yyyy-MM-dd");
    }
    
    /**
     * 根据日期字符串计算星期几
     * @param: @param dateStr
     */
    public static String dateTOWeekday(String dateStr){
        Calendar calendar = Calendar.getInstance();
		Date date = parseDate(dateStr);
        calendar.setTime(date);
        int number = calendar.get(Calendar.DAY_OF_WEEK)-1;
        String[] str = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    	return str[number];
    }
    /**
     * 根据出生日期计算成长时间
     * @Title: getYearMonthDay
     * @Description: TODO  
     * @param: @param birthday
     * @param: @return      
     * @return: String[]
     * @author: sunshine  
     * @throws
     */
    public static int[] getYearMonthDay(Date oldBirthday){
		Calendar birthday = Calendar.getInstance();
		birthday.setTime(oldBirthday);
		Calendar now = Calendar.getInstance();

		int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
		int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
		int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

		if(day<0){
			 month -= 1;
			 now.add(Calendar.MONTH, -1);
			 day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		if(month<0){
			month = (month+12)%12;
			year--;
		}
		if(year<0){
			day=0;
			month=0;
			year=0;
		}
     return new int[]{year,month,day};
    }
}
