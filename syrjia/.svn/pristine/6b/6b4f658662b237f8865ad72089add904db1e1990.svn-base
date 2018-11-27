package cn.syrjia.wxPay.wxPay.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import cn.syrjia.util.TemplateIdUtil;

public class WeiXinConfig {
	
	/**
	 * 获取微信access_token 请求url
	 */
	public static String tokenUri = "https://api.weixin.qq.com/cgi-bin/token";
	
	/**
	 * 查询微信订单
	 */
	public static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	/**
	 * 关闭订单
	 */
	public static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";

	public static String number32() {

		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());

		// 八位随机数
		String strRandom = TenpayUtil.buildRandom(8) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom + "fasdfasdf";

	}
	
	/**
	 * 获取本机IP
	 */
	private static String localIp(){
		String ip = null;
		@SuppressWarnings("rawtypes")
		Enumeration allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while(allNetInterfaces.hasMoreElements()){
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
				for(InterfaceAddress add:InterfaceAddress){
					InetAddress Ip = add.getAddress();
					if(Ip!=null&&Ip instanceof Inet4Address){
						ip = Ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取本机Ip失败:异常信息："+e.getMessage());
		}
		return ip;
	}
	
	//测试公众号
	/**
	 * 微信APPID
	 */
	//public static String appId = "wx7d283f7813d3ebcb";

	/**
	 * 微信SECRET
	 */
	//public static String appSecret = "99077fe6dce0f8dd77019a98adcbf16b";
	
	/**
	 * 微信商户号
	 */
	//public static String mch_id = "1493982282";
	
	/**
	 * 子商户应用ID
	 */
	//public static String sub_appid = "wxaf93dc17ed1afe7d";
	
	/**
	 * 子商户号
	 */
	//public static String sub_mch_id = "1502539321";
	
	//public static String sub_appSecret = "929ef686bf093d6192e09978b3f18fe8";
	
	//public static String app_trade_type = "APP";
	
	/**
	 * 用户端实际ip
	 */
	//public static String spbill_create_ip = "";
	
	  /* 证书路径,注意应该填写绝对路径（仅退款、撤销订单时需要）
		*/
	//public static String SSLCERT_PATH = "cert/apiclient_cert.p12";
	
	// appkey
	//public static String app_key = "HKYguanli01HKYguanli01HKYguanli0";
	
	// key
	//public static String key = "qe221qeqweq3464sfwrww2312e3dwee1";
	// 支付类型 JSAPI-公众号支付，NATIVE--原生扫码支付、APP--app支付、MICROPAY--刷卡支付
	//public static String trade_type = "JSAPI";
	// 授权方式
	//public static String grantType = "authorization_code";
	
	//public static String  UNIFIED_ORDER_URL= "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/////////  
	// 购物
	//public static String notify_url = "https://"+TemplateIdUtil.serverName+"/syrjia/wx/notifyWeixin.action";
	//public static String app_notify_url = "https://"+TemplateIdUtil.serverName+"/syrjia/wx/notifyAppWeixin.action";

	//public static String scope = "snsapi_userinfo";
	//服务器生成订单号机器IP
	//public static String SPBILL_CREATE_IP = localIp();
	// 微信退款申请接口
	//public final static String REFUND_APPLAY_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	//正式公众号
	
	/**
	 * 微信APPID
	 */
	public static String appId = "wx00a647491b70a8fa";

	/**
	 * 微信SECRET
	 */
	public static String appSecret = "0643863d6e80d27c67c3b93364d3fc63";
	
	/**
	 * 微信商户号
	 */
	public static String mch_id = "1398438202";
	
	/**
	 * 子商户应用ID
	 */
	public static String sub_appid = "wxaf93dc17ed1afe7d";
	
	/**
	 * 子商户号
	 */
	public static String sub_mch_id = "1502539321";
	
	public static String sub_appSecret = "929ef686bf093d6192e09978b3f18fe8";
	
	public static String app_trade_type = "APP";
	
	/**
	 * 用户端实际ip
	 */
	public static String spbill_create_ip = "";
	
	  /* 证书路径,注意应该填写绝对路径（仅退款、撤销订单时需要）
		*/
	public static String SSLCERT_PATH = "cert/apiclient_cert.p12";
	
	// appkey
	public static String app_key = "HKYguanli01HKYguanli01HKYguanli0";
	
	// key
	public static String key = "tjhongkangyunsyrjia2016101315409";
	// 支付类型 JSAPI-公众号支付，NATIVE--原生扫码支付、APP--app支付、MICROPAY--刷卡支付
	public static String trade_type = "JSAPI";
	// 授权方式
	public static String grantType = "authorization_code";
	
	public static String  UNIFIED_ORDER_URL= "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/////////  
	// 购物
	public static String notify_url = "https://"+TemplateIdUtil.serverName+"/syrjia/wx/notifyWeixin.action";
	public static String app_notify_url = "https://"+TemplateIdUtil.serverName+"/syrjia/wx/notifyAppWeixin.action";

	public static String scope = "snsapi_userinfo";
	//服务器生成订单号机器IP
	public static String SPBILL_CREATE_IP = localIp();
	// 微信退款申请接口
	public final static String REFUND_APPLAY_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
}
