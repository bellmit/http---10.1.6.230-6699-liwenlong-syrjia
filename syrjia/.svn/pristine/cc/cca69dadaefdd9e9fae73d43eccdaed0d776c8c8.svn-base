package cn.syrjia.wxPay.wxPay.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.jdom.JDOMException;

import cn.syrjia.weixin.util.CommonUtil;



public class WxAppPayUtil {

	@SuppressWarnings("unchecked")
	public static SortedMap<Object,Object>  sendPay(String orderNo,HttpServletRequest request,String total,boolean isUser,int locktimes){
		String reuqestXML=PayCommonUtil.getRequestXml(setOrder(orderNo, request, total,isUser,locktimes));
		
		String result=null;
		try {
			result = CommonUtil.httpsRequest(WeiXinConfig.UNIFIED_ORDER_URL,"POST", new String(reuqestXML.getBytes(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		try {
			Map<String,Object> map=XMLUtil.doXMLParse(result);
			if(null==map.get("result_code")||"FAIL".equals(map.get("return_code"))){
				SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
				parameters.put("return_code",map.get("return_code"));
				parameters.put("return_msg",map.get("return_msg"));
				return parameters;
			}
			String result_code=map.get("result_code").toString();
			if("SUCCESS".equals(result_code)){
				SortedMap<Object,Object> parameters=setResult(map.get("prepay_id").toString());
				return parameters;
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	private static SortedMap<Object,Object> setOrder(String orderNo,HttpServletRequest request,String total,boolean isUser,Integer lockTimes){
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		parameters.put("appid",WeiXinConfig.sub_appid);
		parameters.put("body", "syrjia");
		parameters.put("mch_id", WeiXinConfig.sub_mch_id);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("notify_url",WeiXinConfig.app_notify_url);
		parameters.put("out_trade_no",orderNo);
		parameters.put("spbill_create_ip",Util.getIp(request));
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
		int endPayTime = 300000;
		long nowTimes = System.currentTimeMillis();
		String strTime = df1.format(new Date(nowTimes));
		if(lockTimes!=null&&lockTimes!=0){
			endPayTime = lockTimes*58*1000;
		}
		String strexpire = df1.format(new Date((new Date(nowTimes).getTime()) + endPayTime));
		parameters.put("time_expire", strexpire);
		parameters.put("time_start", strTime);
		parameters.put("total_fee",conversion(new BigDecimal(total).multiply(new BigDecimal(100)).toString()));
		parameters.put("trade_type","APP");
		parameters.put("sign",PayCommonUtil.createSign("UTF-8", parameters));
		return parameters;
	}
	
	public static void main(String[] args) {
		String[] x = NumberFormat.getInstance().format(1198.0).split(",");
		for (int i = 0; i < x.length; i++) {
			System.out.println("1111:==="+x[i]);
		}
		
		
	}
	private static SortedMap<Object,Object> setResult(String partnerid){
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		parameters.put("appid",WeiXinConfig.sub_appid);
		parameters.put("prepayid", partnerid);
		parameters.put("partnerid", WeiXinConfig.sub_mch_id);
		parameters.put("noncestr", PayCommonUtil.CreateNoncestr());
		parameters.put("package","Sign=WXPay");
		parameters.put("timestamp",System.currentTimeMillis() / 1000);
		parameters.put("sign",PayCommonUtil.createSign("UTF-8", parameters));
		return parameters;
	}
	
	public static String conversion(String str){
		 NumberFormat nf = new DecimalFormat("#.##");
		return nf.format(Double.parseDouble(str));
	}
	
	
}
