package cn.syrjia.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTime {
	/**
	 * 获取当前的日期yyyy-MM-dd
	 */
	public static String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	/**
	 * 获取当前的日期yyyyMMdd
	 */
	public static String getDateYMD() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public static String getDate(String date, Integer day, Integer month) {
		org.joda.time.DateTime dt = org.joda.time.DateTime.parse(date);
		org.joda.time.DateTime dt2 = dt;
		if (null != day) {
			dt2 = dt2.plusDays(day);
		}
		if (null != month) {
			dt2 = dt2.plusMonths(month);
		}
		return dt2.toString("yyyy-MM-dd");
	}

	/**
	 * 获取当前的时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获取当前的年份yyyy
	 */
	public static int getYear() {
		Calendar a = Calendar.getInstance();
		return a.get(Calendar.YEAR);
	}

	/**
	 * 字符串转日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				format = new SimpleDateFormat("yyyy.MM.dd");
				date = format.parse(str);
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
				format = new SimpleDateFormat("yyyy年MM月dd");
				try {
					date = format.parse(str);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}
		return date;
	}

	/**
	 * 字符串转时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date StrToTime(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date StrToDate(String str, String formatter) {

		SimpleDateFormat format = new SimpleDateFormat(formatter);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String DateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}

	/**
	 * 时间转字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String TimeToStr(Date time) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(time);
		return str;
	}

	/**
	 * 获取当前的日期加xxx天后yyyy-MM-dd
	 */
	public static String getDate(int x) {
		org.joda.time.DateTime dt = org.joda.time.DateTime.now();
		org.joda.time.DateTime dt2 = dt.plusDays(x);

		return dt2.toString("yyyy-MM-dd");
	}

	/**
	 * 获取当天日期加X月后的日期
	 * 
	 * @Description: TODO
	 * @param @param x
	 * @param @return
	 * @return String
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-7
	 */
	public static String getDateMonth(int x) {
		org.joda.time.DateTime dt = org.joda.time.DateTime.now();
		org.joda.time.DateTime dt2 = dt.plusMonths(x);

		return dt2.toString("yyyy-MM-dd");
	}

	/**
	 * 获取当前的日期加xxx天前yyyy-MM-dd
	 */
	public static String getDateBefore(int x) {
		org.joda.time.DateTime dt = org.joda.time.DateTime.now();
		org.joda.time.DateTime dt2 = dt.minusDays(x);

		return dt2.toString("yyyy-MM-dd");
	}

	/**
	 * 获取当前月第一天
	 */
	public static String getFirstDateForMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		return first;
	}
	
	/**
	 * 获取上个月
	 */
	public static String getSyMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String first = format.format(c.getTime());
		return first;
	}
	

	/**
	 * 获取本月
	 */
	public static String getNowMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		String first = format.format(c.getTime());
		return first;
	}

	/**
	 * 传入时间 yyyy-MM-dd 获取月
	 */
	public static int getMonthForStr(String str) {
		Calendar now = Calendar.getInstance();
		// 获得一个Date对象
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 设置当前时间
		now.setTime(date);
		// 从日期中取得当前的月
		int month = now.get(Calendar.MONTH);
		return month + 1;
	}
	
	/**
	 * 时间戳转当前时间
	 * 
	 * @param s
	 * @return
	 */
	public static String stampToTime(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s) * 1000;
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 获取当前月
	 */
	public static int getMonth() {
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH);
		return month + 1;
	}

	/**
	 * 获取当前小时数
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return int
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-14
	 */
	public static int getHour() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/**
	 * 获取当前天
	 */
	public static int getDay() {
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static void main(String[] args) {

		System.out.println(getDay());
	}

	/**
	 * 获取当前月最后一天
	 */
	public static String getLastDateForMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH,
				ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());

		return last;
	}

	/**
	 * 获取当前月最后一天
	 */
	public static Date getLastDateForMonthByDate() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH,
				ca.getActualMaximum(Calendar.DAY_OF_MONTH));

		return ca.getTime();
	}

	/**
	 * 获取当前月第一天
	 */
	public static Date getFirstDateForMonthByDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天

		return c.getTime();
	}

	/**
	 * 获取本周第一天
	 */
	public static String getFirstDateForWeek() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		String first = format.format(c.getTime());
		return first;
	}

	/**
	 * 获取本周最后一天
	 */
	public static String getLastDateForWeek() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		ca.add(Calendar.WEEK_OF_YEAR, 1);
		String last = format.format(ca.getTime());

		return last;
	}

	/**
	 * 获取当前的时间加xxx分后yyyy-MM-dd HH:mm:ss
	 */
	public static String getTime(int x) {
		org.joda.time.DateTime dt = org.joda.time.DateTime.now();
		org.joda.time.DateTime dt2 = dt.plusMinutes(x);
		return dt2.toString("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 验证码时长比较
	 */
	public static long codeTimeCompare(String codeTime) {
		Date now = new Date();
		if(codeTime.indexOf("-")>0||codeTime.indexOf(":")>0){
			Date cTime = StrToTime(codeTime);
			long diff = now.getTime() - cTime.getTime();
			return  diff / (1000);
		}else{
			return  (now.getTime() / (1000))-Integer.valueOf(codeTime);
		}

	}

	/**
	 * 两个时间戳获取相差小时数
	 */
	public static int getHoursByTwoTime(long begin, long end) {

		int minutes = (int) ((end - begin) / (1000 * 60));
		int hours = minutes / 60;
		int diff = minutes % 60;
		if (diff > 0) {
			hours += 1;
		}

		return hours;
	}

	/**
	 * 会议室时长比较
	 */
	public static long meetingTimeCompare(String beginTime, String endTime) {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		Date date2 = null;
		try {
			date = format.parse(beginTime);
			date2 = format.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long diff = date2.getTime() - date.getTime();
		return diff;
	}

	/**
	 * 获取时间戳
	 */
	public static long getTimestamp(String time) {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = date.getTime();
		return diff;
	}

	/**
	 * 获取当前时间的HH：mm
	 */
	public static String getTimestamp() {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		String hh = null;
		try {
			hh = format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hh;
	}

	/**
	 * 签到5分钟限制(返回分钟数)
	 */
	public static long signInTimeCompare(String beginTime, String endTime) {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		Date date2 = null;
		try {
			date = format.parse(beginTime);
			date2 = format.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = date2.getTime() - date.getTime();
		return diff / 60000;
	}

	/**
	 * 提交订单剩余时间，返回秒数
	 * 
	 * @Description: TODO
	 * @param @param time
	 * @param @return
	 * @return long
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-3
	 */
	public static long orderInTime(Timestamp time) {
		Timestamp systime = new Timestamp(System.currentTimeMillis());
		long old = (systime.getTime() - time.getTime()) / 1000;// 已过的时间
		if (old > 600) {
			return 0;
		} else {
			return 600 - old;
		}
	}

	public static Integer orderInTime(Integer time, int locktimes) {
		int systime = (int)(System.currentTimeMillis()/1000);
		Integer old = systime - time;// 已过的时间
		if (old > locktimes) {
			return 0;
		} else {
			return locktimes - old;
		}
	}

	/**
	 * 获取当前小时
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return int
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-5
	 */
	public static int getTodayHour() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/**
	 * 判断当前时间是否是预订前24小时
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Boolean
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-6
	 */
	public static Boolean compare24h(String date, String time) {
		org.joda.time.DateTime dt = org.joda.time.DateTime.now();
		org.joda.time.DateTime dt2 = dt.plusHours(24);
		String nowtime = dt2.toString("yyyy-MM-dd HH:mm:ss");
		System.out.println(nowtime);
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(5, 7)),
				Integer.parseInt(date.substring(8)), Integer.parseInt(time), 0);
		Calendar calnow = Calendar.getInstance();
		calnow.set(Integer.parseInt(nowtime.substring(0, 4)),
				Integer.parseInt(nowtime.substring(5, 7)),
				Integer.parseInt(nowtime.substring(8, 10)),
				Integer.parseInt(nowtime.substring(11, 13)), 0);
		int flag = cal.compareTo(calnow);
		if (flag <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是否是当前时间之前的时间
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-14
	 */
	public static boolean compareParkTime(String date, int i) {
		String nowString = getDate();
		java.util.Date date1 = java.sql.Date.valueOf(date);
		java.util.Date date2 = java.sql.Date.valueOf(nowString);
		int hour = getHour();
		if (date1.before(date2) || (date.equals(nowString) && i <= hour)) {
			return true;
		}
		return false;
	}

	/**
	 * 将数字转换为小时数
	 * 
	 * @Description: TODO
	 * @param @param num
	 * @param @return
	 * @return String
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-16
	 */
	public static String switchNumToHour(String num) {
		int t = Integer.parseInt(num);
		if (t < 10) {
			return "0" + num + ":00";
		} else {
			return num + ":00";
		}
	}

	/**
	 * 将数字转换为小时区间
	 * 
	 * @Description: TODO
	 * @param @param num
	 * @param @return
	 * @return String
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-22
	 */
	public static String numToHour(String num) {
		return switchNumToHour(num) + "-"
				+ switchNumToHour(String.valueOf(Integer.parseInt(num) + 1));
	}

	public static boolean compareTime(String beginTime,String endTime) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		// 获取Calendar实例
		Calendar currentTime = Calendar.getInstance();
		Calendar compareTime = Calendar.getInstance();
		try {
			// 把字符串转成日期类型
			currentTime.setTime(df.parse(beginTime));
			compareTime.setTime(df.parse(endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 利用Calendar的方法比较大小
		if (currentTime.compareTo(compareTime) > 0) {
			return true;//System.out.println("前者时间小");
		} else {
			return false;//System.out.println("后边时间大");
		}
		/*// 转成数字后比较大小
		int startTime = Integer.parseInt("201406");
		int endTime = Integer.parseInt("201506");
		System.out.println(endTime - startTime);*/
	}
	/**
	 * 比较当前时间和参数时间相差时间返回  时间戳
	 * @param time
	 * @return
	 */
	public static Integer CompareNowByStampReturn(Integer time){
		long dd1 = System.currentTimeMillis()/1000;
		Integer str = Integer.parseInt(String.valueOf(time-dd1)); 
		return str;
	}
	
	public static Boolean isBelong(String start,String end) {

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
		Date now = null;
		Date beginTime = null;
		Date endTime = null;
		try {
			now = df.parse(df.format(new Date()));
			beginTime = df.parse(start);
			endTime = df.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Boolean flag = belongCalendar(now, beginTime, endTime);
		return flag;
	}

	/**
	 * 判断时间是否在时间段内
	 * 
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
}
