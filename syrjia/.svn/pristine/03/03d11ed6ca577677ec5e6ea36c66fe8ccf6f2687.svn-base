package cn.syrjia.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

@SuppressWarnings("unchecked")
public class WeekDayUtil {
	/**
	* 输入一个日期的时间段，以及相应的星期数，获得这些星期的日期
	*/
	private static Map weekNumberMap = new HashMap();
	static {
		weekNumberMap.put(0,1);
		weekNumberMap.put(1,2);
		weekNumberMap.put(2,3);
		weekNumberMap.put(3,4);
		weekNumberMap.put(4,5);
		weekNumberMap.put(5,6);
		weekNumberMap.put(6,7);
	}


	public static List<String> getDates(String dateFrom, String dateEnd, List weekDays) throws Exception {
		long time;
		long perDayMilSec = 24L * 60 * 60 * 1000;
		List dateList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 需要查询的星期系数
		String strWeekNumber = weekForNum(weekDays);
		try {
			dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
			while (true) {
				time = sdf.parse(dateFrom).getTime();
				time = time + perDayMilSec;
				Date date = new Date(time);
				dateFrom = sdf.format(date);
				if (dateFrom.compareTo(dateEnd) <= 0) {
					// 查询的某一时间的星期系数
					Integer weekDay = dayForWeek(date);
					// 判断当期日期的星期系数是否是需要查询的
				if (strWeekNumber.contains(weekDay.toString())) {
					dateList.add(dateFrom);
					}
				} else {
					break;
				}
			}
		} catch (ParseException e) {
		e.printStackTrace();
		}
		return dateList;
		}


	// 等到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
	public static Integer dayForWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}


	/**
	* 得到对应星期的系数 0：1，星期
	*/
	public static String weekForNum(List<Integer> weekDays) {
		// 返回结果为组合的星期系数
		String weekNumber = "";
		for (Integer weekDay : weekDays) {
		weekNumber = weekNumber + "" + getWeekNum(weekDay).toString();
		}
		return weekNumber;
	}


	// 将星期转换为对应的系数 0,星期日; 1,星期一; 2....
	public static Integer getWeekNum(int strWeek) {
		return (Integer) weekNumberMap.get(strWeek);
	}
	//将数字转换成星期几的字符串
	public static String numToWeek(int num){
		String weekString = "";
		 switch(num){ 
		  case 0: 
			  weekString = "星期日"; 
		   break;
		  case 1: 
			  weekString = "星期一"; 
		   break; 
		  case 2: 
			  weekString = "星期二";
		   break; 
		  case 3: 
			  weekString = "星期三"; 
		   break; 
		  case 4: 
			  weekString = "星期四";
		   break; 
		  case 5: 
			  weekString = "星期五";
		   break; 
		  case 6: 
			  weekString = "星期六";
		   break; 
		  } 
		 return weekString;
	}
}
