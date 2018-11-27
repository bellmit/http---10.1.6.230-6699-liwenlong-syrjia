package cn.syrjia.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 2016/06/24
 * 
 * @author renruizhi 工具类
 */
public class Util2{
	Logger log = LogManager.getLogger(Util.class);
	public  static String getIp(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = null;
            // ipAddress = this.getRequest().getRemoteAddr();
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0
                    || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0
                    || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0
                    || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")
                        || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                                                                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "Exception";
        }
        return ipAddress;
    }
	/**
	 * 比较时间是否小于当前时间
	 * @param time
	 * @return
	 */
	public static boolean compareDate(String time){
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		String times[]=time.split(":");
		calendar1.set(Calendar.HOUR_OF_DAY,Integer.parseInt(times[0]));
		calendar1.set(Calendar.MINUTE,Integer.parseInt(times[1]));
		if(calendar.getTimeInMillis() > calendar1.getTimeInMillis()){
			return true;
		}
		return false;
	}
	
	public static String transFormation(int num){
		String returnNum="";
		switch (num) {
		case 1:
			returnNum= "一";
			break;
		case 2:
			returnNum= "二";
			break;
		case 3:
			returnNum= "三";
			break;
		case 4:
			returnNum= "四";
			break;
		case 5:
			returnNum= "五";
			break;
		case 6:
			returnNum= "六";
			break;
		case 7:
			returnNum= "七";
			break;
		case 8:
			returnNum= "八";
			break;
		case 9:
			returnNum= "九";
			break;
		case 10:
			returnNum= "十";
			break;
		default:
			returnNum= num+"";
			break;
		}
		return returnNum;
	}
	
	public static String months(int num){
		String returnNum="";
		switch (num) {
		case 1:
			returnNum= "January";
			break;
		case 2:
			returnNum= "February";
			break;
		case 3:
			returnNum= "March";
			break;
		case 4:
			returnNum= "April";
			break;
		case 5:
			returnNum= "May";
			break;
		case 6:
			returnNum= "June";
			break;
		case 7:
			returnNum= "July";
			break;
		case 8:
			returnNum= "August";
			break;
		case 9:
			returnNum= "September";
			break;
		case 10:
			returnNum= "October ";
			break;
		case 11:
			returnNum= "November";
			break;
		case 12:
			returnNum= "December";
			break;
		}
		return returnNum;
	}
	
	public static int listLayoutNum(int layoutId){
		int returnNum=0;
		switch (layoutId) {
		case 55://布局1
			returnNum= 3;
			break;
		case 56://布局2
			returnNum= 3;
			break;
		case 57://布局3
			returnNum= 4;
			break;
		
		}
		return returnNum;
	}
	/**
	 * 工作台类型返回
	 * @param id
	 * @return
	 */
	public static int appMsgType(int id){
		int returnNum=0;
		switch (id) {
		case 103://订餐
			returnNum= 1;
			break;
		case 104://现场报修
			returnNum= 2;
			break;
		
		}
		return returnNum;
	}
	
	/**
	 * 判断是否是餐厅管理人员来显示工作台订餐提醒
	 * @param 角色 id
	 * @return
	 */
	public static boolean isMealManager(int id){
		boolean flag = true; 
		switch (id) {
		case 34://餐厅经营人员
			flag = false;
			break;
		case 22://移动管理员
			flag = false;
			break;
		
		}
		return flag;
	}
	
	/**
	 * 判断是否能查看所有表单以及菜单列表数据
	 * @param 角色 id
	 * @return
	 */
	public static boolean isSupperManager(int id){
		boolean flag = true; 
		switch (id) {
		case 22://移动管理员
			flag = false;
			break;
		
		}
		return flag;
	}
	
	/**
	 * 根据日期筛选列表id值进行sql筛选
	 * @param id
	 * @return
	 */
	public static String getSqlByDateListId(String fieldName ,int id){
		String today = DateTime.getDate();
		String returnSql="";
		switch (id) {
		/*case 60://最近更新  
			returnSql= " createTime ";
			break;*/
		case 61://今日
			returnSql= " DATE_FORMAT("+fieldName+",'%Y-%m-%d') = '"+today+"'";
			break;
		case 62://昨日
			returnSql= " DATE_FORMAT("+fieldName+",'%Y-%m-%d') = '"+DateTime.getDate(-1)+"'";
			break;
		case 63://本周
			returnSql= " DATE_FORMAT("+fieldName+",'%Y-%m-%d') >='"+DateTime.getFirstDateForWeek()+"' and DATE_FORMAT("+fieldName+",'%Y-%m-%d') <= '"+DateTime.getLastDateForWeek()+"'";
			break;
		case 64://本月
			returnSql= " DATE_FORMAT("+fieldName+",'%Y-%m-%d') >='"+DateTime.getFirstDateForMonth()+"' and DATE_FORMAT("+fieldName+",'%Y-%m-%d') <= '"+DateTime.getLastDateForMonth()+"'";
			break;
		}
		return returnSql;
	}
	
	/**
	 * 根据日期筛选列表id值进行sql筛选
	 * @param id
	 * @return
	 */
	public static String getSqlForYear(String fieldName){
		int year = DateTime.getYear();
		String returnSql= " DATE_FORMAT("+fieldName+",'%Y') = '"+year+"'";
		return returnSql;
	}
	/**
	 * object转json
	 * @param obj
	 * @return
	 */
	public static String ObjectToString(Object obj){
		if(null==obj||"".equals(obj)){
			return null;
		}
		if(obj instanceof List||obj instanceof Set){
			JSONArray jsonArray= JSONArray.fromObject(obj);
			return jsonArray.toString();
		}else{
			JSONObject jsonObject= JSONObject.fromObject(obj);
			return jsonObject.toString();
		}
		
	}
	
	/**
	 * String转时间
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date StringToDate(String str) throws ParseException{
		 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy年MM月dd日");
		 Date date= sdf.parse(str.toString());
		 return date;
	}
	
	/**
	 * String转时间
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static String StringToDateString(String str) throws ParseException{
		 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy年MM月dd日");
		 SimpleDateFormat sdf1 =   new SimpleDateFormat("yyyy-MM-dd");
		 Date date= sdf.parse(str.toString());
		 return sdf1.format(date);
	}
	
	/**
	 * 判断是否为Null
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String ...str){
		for(String s:str){
			if(null==s||"".equals(s)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断价格是否相等
	 * @param str
	 * @param str1
	 * @return
	 */
	public static boolean isEquals(String str,String str1){
		BigDecimal b=new BigDecimal(str).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal b1=new BigDecimal(str1).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		if(b.equals(b1)){
			return true;
		}
		return false;
	}
	
	public static boolean isEquals(String total,String unitPrice,Integer num){
		BigDecimal t=new BigDecimal(total).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal unit=new BigDecimal(unitPrice).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal n=new BigDecimal(num).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		if(t.equals(unit.multiply(n).setScale(2,BigDecimal.ROUND_HALF_DOWN))){
			return true;
		}
		return false;
	}
	
	public static String total(String unitPrice,Integer num){
		BigDecimal unit=new BigDecimal(unitPrice).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal n=new BigDecimal(num).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		return unit.multiply(n).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
		
	}
	
	public static String bigAdd(String unitPrice,String num){
		BigDecimal unit=new BigDecimal(unitPrice).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal n=new BigDecimal(num).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		return unit.add(n).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
		
	}
	
	
	
	
	public static String listToString(List<?> list){
		String str="";
		for(Object obj:list){
			str+=obj.toString();
		}
		return str;
	}
	
	public static int toInt(int[] i){
		int total=0;
		for(int count:i){
			total+=count;
		}
		return total;
	}
	
	public static Map<String, Object> upload(HttpServletRequest request,MultipartFile file,String directoryName,int x){
		
		String path = request.getSession().getServletContext()
				.getRealPath("uploadFile");
		
		String dateUrl = String.valueOf(DateTime.getYear()) + "/"
				+ DateTime.getDate() + "/";
		
		String datePath = String.valueOf(DateTime.getYear()) + File.separator
				+ DateTime.getDate() ;
	
		String url = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath()
				+ "/uploadFile" + "/" + directoryName + "/" + dateUrl;
		
		String fileName = file.getOriginalFilename();
		
		Long name = System.currentTimeMillis()+x;
		//System.out.println("================="+name);
		String addr = name.toString()
				+ fileName.substring(fileName.lastIndexOf("."));
		File targetFile = new File(path + File.separator + directoryName
				 + File.separator + datePath, addr);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			file.transferTo(targetFile);
			System.out.println(targetFile);
			i = 1;
		} catch (Exception e) {
			i = 0 ;
			e.printStackTrace();
		} 
		map.put("i", i);
		map.put("addr", addr);
		map.put("url", url);
		return map;
	}
	public static  Class<?> ReflectByEntityName(String name){
		 Class<?> entity =null;
		try {
			entity = Class.forName(Constants.ENTITY_PACKAGE+name);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}
	/**  
     * 对象转数组  
     * @param obj  
     * @return  
     */  
    public static byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
    }  
    /**
     * 生成唯一的编号
     * @return
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    
}
