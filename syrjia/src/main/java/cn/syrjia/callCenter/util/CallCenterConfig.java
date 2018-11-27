package cn.syrjia.callCenter.util;

import cn.syrjia.util.DateTime;
import cn.syrjia.util.Md5Encoder;
public class CallCenterConfig {
	
	/**
	 * 前期测试呼叫中心固定值
	 */
	public static String oldPassword = "POLYLINK_MESSAGE_TOKEN";
	
	public static String calledNumber = "82100750";
	
	public static String callCenterKey = "00d7c0cd6d544f96bb765e7ccbe64d0c";
	
	public static String callCenterUrl = "http://125.35.255.62:8001/";
	
	public static String CustomersAssist = "api/Data/CustomersAssist";
	
	public static String CustomersDoctor="api/Data/CustomersDoctor";
	
	public static String CustomersBase="api/Data/CustomersBase";
	
	public static String CustomersPatient="api/Data/CustomersPatient";
	
	public static String CustomersReceiver = "api/Data/CustomersReceiver";

	public static String Pharmacist = "api/Data/Pharmacist";
	
	public static String PatientRelation="api/Data/PatientRelation";
	
	public static String Order  ="api/Data/Order"; 

	public static String CallResultCallBack="api/Data/CallResultCallBack";
	
	public static String OncKeyCall="api/Data/OncKeyCall";
	
	public static String OutBoundCallOut = "api/Data/OutBoundCallOut";

	public static String PreviewCallOut = "api/Data/PreviewCallOut";
	
	public static String PharmacistByWorkNumAndCustomerForeignId="api/Data/PharmacistByWorkNumAndCustomerForeignId";
	
	public static String UnResponse  ="api/Data/UnResponse"; 

	public static String UnPayOrder="api/Data/UnPayOrder";
	
	public static String getPhoneBasicToken(){
		String oldPassWordMD5 = Md5Encoder.getMd5(oldPassword);
		String nowYMD = DateTime.getDateYMD();
		String token = Md5Encoder.getMd5(oldPassWordMD5 + nowYMD);
		return token;
	}
	
}
