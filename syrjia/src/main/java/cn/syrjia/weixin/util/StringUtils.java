package cn.syrjia.weixin.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils  {

	//默认的空值
	public static final String EMPTY = "";
	
	/**
	 * 检查字符串是否为空
	 * @Title: isEmpty 
	 * @param str
	 * @return boolean
	 * 
	 * @time: Nov 27, 2011 1:27:34 PM
	 */
	public static boolean isEmpty(Object str) {
		if (str == null) {
			return true;
		} else if (str.toString().length() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 如果数据null或者""则替换为new_str
	 * @Title: getNotNullStringValue 
	 * @param str
	 * @param new_str
	 * @return String
	 * 
	 * @time: Nov 27, 2011 1:40:53 PM
	 */
	public static String getNotNullStringValue(Object str, String new_str) {
		if (isEmpty(str)) {
			return new_str;
		}
		return str.toString();
	}
	
	/**
	 * 判断整形数据null替换new_str
	 * @Title: getNotNullIntValue 
	 * @param str
	 * @param new_str
	 * @return int
	 * 
	 * @time: Nov 27, 2011 1:42:05 PM
	 */
	public static int getNotNullIntValue(Object str, int new_str) {
		if (isEmpty(str)) {
			return new_str;
		}
		int n_str = 0;
		try {
			n_str = Integer.parseInt(str.toString());
		} catch (NumberFormatException e) {
			n_str = new_str;
		}
		return n_str;
	}
	
	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : str.toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 截取字符串 并补充后缀
	 * @Title: getLimitLengthString 
	 * @param str
	 * @param len
	 * @param houzhui
	 * @return String
	 * @time: Nov 27, 2011 1:35:38 PM
	 */
	public static String abbr(String str, int len, String suffix) {
		if (isEmpty(str)) {
			return "";
		}
		try{
			int counterOfDoubleByte = 0;
			byte[] b = str.getBytes("GBK"); 
			if(b.length <= len){
				return str; 
			}
			for(int i = 0; i < len; i++){
				if(b[i] < 0){
					counterOfDoubleByte++; 
				}
			}
			if(counterOfDoubleByte % 2 == 0){
				return new String(b, 0, len, "GBK") + suffix; 
			}else{
				return new String(b, 0, len - 1, "GBK") + suffix; 
			}
		}catch(Exception e){
			return ""; 
		} 
	}
	
	
	/**
	 * 中文转换成unicode码
	 * @param cnStr
	 * @return
	 */
	public static String encodeUnicode(String cnStr) {
		String s1 = "";
		for (int i = 0; i < cnStr.length(); i++) {
			String hexB = Integer.toHexString(cnStr.charAt(i) & 0xffff);
			if (hexB.length() <= 2) {      
                hexB = "00" + hexB;      
            } 
			s1 += "\\u" + hexB;
		}
		return s1;
	}

	/**
	 * unicode码 转换成 中文
	 * @param unicodeStr
	 * @return
	 */
	public static String decodeUnicode(String unicodeStr) {
		int n = unicodeStr.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = unicodeStr.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * URL编码转换
	 * @param str
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String URIEncoding(String str,String encoding) throws UnsupportedEncodingException {
		return URLDecoder.decode(str, encoding);
	}
	
	/**
	 * 获取字符串css信息
	 * @Title: getStr_CssStyle 
	 * @param str
	 * @return String
	 * @time: Nov 27, 2011 1:44:34 PM
	 */
	public static String getCssStyle(String str) {
		String return_str = "";
		if (!StringUtils.isEmpty(str)) {
			String pattern = "style=('(.*?)')";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			if (m.find()) {
				return_str = m.group(2);
			}
		}
		return return_str;
	}

	
	/**
	 * HTML字符串转换
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeHTML(String str) {
		if (isEmpty(str)) {
			return "";
		}
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	/**
	 * 还原HTML字符串数据
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeHTML(String str) {
		if (isEmpty(str)) {
			return "";
		}
		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&quot;", "\"");
		return str;
	}
	
	/**
	 * Html转换为TextArea文本:编辑时拿来做转换
	 * @param str
	 * @return
	 */
	public static String Html2Text(String str) {
		if (str == null) {
			return "";
		}else if (str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<br />", "\n");
		str = str.replaceAll("<br />", "\r");
		return str;
	}
	
	/**
	 * TextArea文本转换为Html:写入数据库时使用
	 * @param str
	 * @return
	 */
	public static String Text2Html(String str) {
		if (str == null) {
			return "";
		}else if (str.length() == 0) {
			return "";
		}
		str = str.replaceAll("\n", "<br />");
		str = str.replaceAll("\r", "<br />");
		return str;
	}
	
	/**
	 * 字符串非法字符检测
	 * 
	 * @Title: checkStringRule
	 * @param str
	 * @return boolean
	 * @time: Nov 27, 2011 1:45:18 PM
	 */
	public static boolean checkStringRule(String str) {
		if (isEmpty(str)) {
			return true;
		}
		boolean b = true;
		if ((str.indexOf("`") > -1) || (str.indexOf("~") > -1)
				|| (str.indexOf("!") > -1) || (str.indexOf("#") > -1)
				|| (str.indexOf("$") > -1) || (str.indexOf("%") > -1)
				|| (str.indexOf("^") > -1) || (str.indexOf("&") > -1)
				|| (str.indexOf("*") > -1) || (str.indexOf("(") > -1)
				|| (str.indexOf(")") > -1) || (str.indexOf("=") > -1)
				|| (str.indexOf("+") > -1) || (str.indexOf("|") > -1)
				|| (str.indexOf("\\") > -1) || (str.indexOf(";") > -1)
				|| (str.indexOf(":") > -1) || (str.indexOf("'") > -1)
				|| (str.indexOf("\"") > -1) || (str.indexOf("<") > -1)
				|| (str.indexOf(",") > -1) || (str.indexOf(">") > -1)
				|| (str.indexOf("?") > -1) || (str.indexOf("　") > -1)) {
			b = false;
		}
		return b;
	}

	

	/**
	 * 获取字符串css信息
	 * @Title: getStr_CssStyle 
	 * @param str
	 * @return String
	 * @time: Nov 27, 2011 1:44:34 PM
	 */
	public static String moneyformat(Double Accounts,Double Balance) {
		
		if( StringUtils.isEmpty(Accounts) ){
			Accounts=0.00;
		}
		
		if( StringUtils.isEmpty(Balance) ){
			Balance=0.00;
		}
		
		DecimalFormat df = new DecimalFormat("##0.00");
		
		Double money = (Accounts*100+Balance*100)/100;
		
		return df.format(money);
	}


}