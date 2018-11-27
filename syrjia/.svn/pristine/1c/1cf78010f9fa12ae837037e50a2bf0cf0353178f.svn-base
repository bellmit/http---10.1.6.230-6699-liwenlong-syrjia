package cn.syrjia.util.logistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import cn.syrjia.config.configCode;
import cn.syrjia.util.JsonUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

public class LogisiticsUtil {

	/**
	 * 请求地址
	 */
	public static String reqURL = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";

	/**
	 * 客户编码
	 */
	private static String clientCode = "0106071311"; // 客户编码

	private static String checkword = "nOk6klNyR3xkhCcQJvukhjjjyOlmYats"; // 客户校验码

	@SuppressWarnings("static-access")
	public static Map<String, Object> querySFLogisitics(String sfLogisiticsNo) {
		//sfLogisiticsNo = "272054388962";
		if (StringUtil.isEmpty(sfLogisiticsNo)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		// 医珍堂测试账号
		String mailno = sfLogisiticsNo; // 顺丰运单号
		String reqXml = "<Request service='RouteService' lang='zh-CN'>"
				+ "<Head>"
				+ clientCode
				+ "</Head>"
				+ "<Body>"
				+ "<RouteRequest tracking_type='1' method_type='1' tracking_number='"
				+ mailno + "'/>" + "</Body></Request>";

		CallExpressServiceTools client = CallExpressServiceTools.getInstance();
		System.out.println("请求报文：" + reqXml);
		String respXml = client.callSfExpressServiceByCSIM(reqURL, reqXml,
				clientCode, checkword);
		List<Map<String, Object>> logisiticsList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isEmpty(respXml)) {
			return Util.resultMap(configCode.code_1101, null);
		}
		System.out.println("--------------------------------------");
		System.out.println("返回报文: " + respXml);
		System.out.println("--------------------------------------");
		try {
			String result = JsonUtil.xml2jsonString(respXml).toString(4);
			JSONObject ResponseJson = JSONObject.fromObject(result);
			if (respXml.indexOf("Route") == -1) {
				return Util.resultMap(configCode.code_1101, null);
			}
			JSONObject BodyJson = JSONObject.fromObject(ResponseJson
					.getJSONObject("Response"));
			Map<String, Object> BodyMap = JsonUtil.jsonToMap(BodyJson);
			JSONObject RouteResponseJson = JSONObject.fromObject(BodyMap
					.get("Body"));
			Map<String, Object> map1 = JsonUtil.jsonToMap(RouteResponseJson);
			JSONObject RouteJson = JSONObject.fromObject(map1
					.get("RouteResponse"));
			logisiticsList = JsonUtil.parseJSON2List(RouteJson
					.getString("Route"));
			Collections.reverse(logisiticsList);
		} catch (Exception e) {
			e.printStackTrace();
			return Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, logisiticsList);
	}

	public static void main(String[] args) {
		querySFLogisitics(null);
	}
}
