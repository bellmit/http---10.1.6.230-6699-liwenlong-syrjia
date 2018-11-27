package cn.syrjia.util;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class SessionUtil {

	public static boolean hasOpenId(HttpServletRequest request){
		try {
			String openId=StringUtil.isEmpty(request.getSession().getAttribute("openid"))?null:request.getSession().getAttribute("openid").toString();
			if(null==openId||"".equals(openId.trim())){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String getOpenId(HttpServletRequest request){
		try {
			String openId=StringUtil.isEmpty(request.getSession().getAttribute("openid"))?null:request.getSession().getAttribute("openid").toString();
			if(StringUtil.isEmpty(openId)){
				
				return null;
			}else{
				return openId;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
}
