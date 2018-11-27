package cn.syrjia.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.syrjia.interceptor.HtmlVerifyHandlerFilter;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;
import cn.syrjia.wxPay.wxPay.util.http.HttpRequest;
import net.sf.json.JSONObject;

public class GetOpenId {

	// 日志
	private static Logger logger = LogManager.getLogger(GetOpenId.class);

	public static String getMemberId(HttpServletRequest request) {
		String openid = StringUtil.isEmpty(request.getSession().getAttribute(
				"openid")) ? null : request.getSession().getAttribute("openid")
				.toString();
		Object memberId = null;
		if (!StringUtil.isEmpty(openid)) {
			try {
				Object obj = RedisUtil.getVal(openid);
				if (!StringUtil.isEmpty(obj)) {// 如果已有toeken则把旧token返回
					JSONObject json = JSONObject.fromObject(obj);
					memberId = json.has("memberId") ? json.get("memberId")
							: null;
				}
			} catch (Exception e) {
				System.out.println("getMemberId方法异常：" + e);
			}
			if (!StringUtil.isEmpty(memberId)) {
				return memberId + "";
			}
		}
		return "";
	}

	public static String getOpenId(HttpRequest requests,
			HttpServletRequest request, HttpServletResponse response,
			String code) {
		String openid = StringUtil.isEmpty(request.getSession().getAttribute(
				"openid")) ? null : request.getSession().getAttribute("openid")
				.toString();
		try {
			String strReci = "https://api.weixin.qq.com/sns/oauth2/access_token";
			String param = "appid=" + WeiXinConfig.appId + "&secret="
					+ WeiXinConfig.appSecret + "&code=" + code + "&grant_type="
					+ WeiXinConfig.grantType; //
			@SuppressWarnings("static-access")
			String result = requests.sendGet(strReci, param);
			JSONObject jsonObject = JSONObject.fromObject(result);
			// request.getSession().setAttribute("accessToken", jsonObject);、
			if (jsonObject.has("openid")) {
				openid = jsonObject.getString("openid");
				Cookie cookie1 = new Cookie("openid", openid);
				cookie1.setMaxAge(60 * 60 * 24 * 30);
				cookie1.setPath("/");
				// cookie 保存30天
				response.addCookie(cookie1); //
				request.getSession().setAttribute("openid", openid);
				request.getSession().setAttribute("memberId", openid);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return openid;
	}

	public static String getOpenId(HttpRequest requests,
			HttpServletRequest request, String code,
			HttpServletResponse response) {
		/*
		 * String openid =
		 * StringUtil.isEmpty(request.getSession().getAttribute("openid"
		 * ))?null:request.getSession().getAttribute("openid").toString();
		 */// request.getSession().setAttribute("accessToken", jsonObject);
		String openid = "";
		try {
			if (StringUtil.isEmpty(openid)) {
				String strReci = "https://api.weixin.qq.com/sns/oauth2/access_token";
				String param = "appid=" + WeiXinConfig.appId + "&secret="
						+ WeiXinConfig.appSecret + "&code=" + code
						+ "&grant_type=" + WeiXinConfig.grantType; //
				@SuppressWarnings("static-access")
				String result = requests.sendGet(strReci, param);
				System.out.println(result + "获取的结果result");
				logger.info("getOpenId获取OPEND获取的结果result：" + result);
				JSONObject jsonObject = JSONObject.fromObject(result);
				logger.info("getOpenId获取OPEND获取的结果转换为jsonObject：" + jsonObject);
				if (jsonObject.has("openid")) {
					openid = jsonObject.getString("openid");
					logger.info("getOpenId获取OPEND获取的结果openid：" + openid);
					System.out.println(openid + "getOpenId为空，重新获取");
					Object obj = RedisUtil.getVal(openid);
					Object memberId = null;
					if (!StringUtil.isEmpty(obj)) {// 如果已有toeken则把旧token返回
						JSONObject json = JSONObject.fromObject(obj);
						memberId = json.has("memberId") ? json.get("memberId")
								: null;
						logger.info("getOpenId获取RedisUtil.getVal获取的结果memberId：" + memberId);
					}
					if (StringUtil.isEmpty(memberId)) {
						try {
							JSONObject json = new JSONObject();
							json.set("memberId", openid);
							RedisUtil.setVal(openid, 60 * 60 * 24 * 365,
									json.toString());
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					/*
					 * Cookie cookie1 = new Cookie("openid", openid);
					 * cookie1.setMaxAge(60 * 60 * 24 * 30);
					 * cookie1.setPath("/"); // cookie 保存30天
					 * response.addCookie(cookie1); //
					 */}
				System.out.println(openid + "getOpenId为空，重新获取");
				request.getSession().setAttribute("openid", openid);
				// request.getSession().setAttribute("memberId", openid);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return openid;
	}
}
