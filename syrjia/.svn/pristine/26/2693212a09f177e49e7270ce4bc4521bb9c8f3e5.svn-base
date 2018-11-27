package cn.syrjia.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 2016/05/16
 * @author liwenlong
 * md5加密
 */
public class Md5Encoder {
	
	/**
	 *MD5加密
	 * @param str 需要加密的字符串
	 * @return
	 */
	public static String getMd5(String str){
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		 md5.setEncodeHashAsBase64(false); 
		 return md5.encodePassword(str,null);
	}
}
