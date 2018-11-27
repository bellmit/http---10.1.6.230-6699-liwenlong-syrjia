package cn.syrjia.callCenter.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sf.json.JSONObject;
import cn.syrjia.util.HttpReuqest;
import cn.syrjia.util.RequestHandler;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

public class SendCallCenterUtil {

	public static String sendCallCenterData(Map<String, Object> params,
			String requestUrl) {
		String result;
		try {
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			Integer timestamp = Util.queryNowTime();
			params.put("timestamp", timestamp);

			if (!StringUtil.isEmpty(requestUrl)
					&& requestUrl.indexOf("CustomersBase") > -1) {
				if (StringUtil.isEmpty(params.get("photo"))) {
					params.put("photo", params.get("headicon"));
				}
				//params.put("createTime", params.get("createtime"));
				params.put("creater", params.get("realname"));
				params.remove("signId");
				params.remove("loginTime");
				params.remove("randomStr");
				params.remove("usertypecode");
				params.remove("detype");
				params.remove("loginname");
				params.remove("password");
				params.remove("openid");
				params.remove("createuserid");
				params.remove("headicon");
				params.remove("realname");
				params.remove("idcard");
				params.remove("address");
				params.remove("addWay");
				params.remove("version");
				//params.remove("createtime");
				params.remove("token");
				params.remove("QQnumber");
				params.remove("integration");
				params.remove("age");
				params.remove("demodel");
			} else if (!StringUtil.isEmpty(requestUrl)
					&& requestUrl.indexOf("CustomersDoctor") > -1) {
				params.put("creater", "");
				params.put("docIsOn", params.get("applyStatus"));
				params.put("departName", params.get("departNames"));
				params.put("illClassName", params.get("illClassNames"));
				params.remove("docPositionId");
				params.remove("cfQrCodeUrl");
				params.remove("departNames");
				params.remove("illClassNames");
				params.remove("infirmaryId");
				params.remove("applyStatus");
				params.remove("type");
				params.put("docNotice", "");
				params.put("isDefault", 0);
			}

			for (String key : params.keySet()) {
				if (!"timestamp".equals(key)
						&& !StringUtil.isEmpty(params.get(key))) {
					packageParams.put(key, params.get(key).toString());
				}
			}

			packageParams.put("key", CallCenterConfig.callCenterKey);
			packageParams.put("timestamp", params.get("timestamp").toString());

			RequestHandler reqHandler = new RequestHandler(null, null);
			reqHandler.init(null, null, CallCenterConfig.callCenterKey);

			String sign = reqHandler.createSign(packageParams);

			params.put("sign", sign);
			System.out.println(JSONObject.fromMap(params));
			result = HttpReuqest.sendCallCenterPost(
					CallCenterConfig.callCenterUrl + requestUrl,
					JSONObject.fromMap(params));
			System.out.println(result);
			return result;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
