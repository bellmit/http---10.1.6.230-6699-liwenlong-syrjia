package cn.syrjia.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import cn.syrjia.dao.MemberDao;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.StringUtil;

public class MemberIdVerifyHandlerFilter implements Filter {

	// 日志
	private Logger logger = LogManager
			.getLogger(MemberIdVerifyHandlerFilter.class);

	/*
	 * @Resource(name = "memberDao") MemberDao memberDao;
	 */

	private WebApplicationContext wac;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	@Override
	public void doFilter(ServletRequest requests, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) requests;
		HttpServletResponse responses = ((HttpServletResponse) response);
		// 判断 是否是微信浏览器
		String userAgent = request.getHeader("user-agent").toLowerCase();
		System.out.println(userAgent.indexOf("micromessenger") >= 0);
		System.out.println(request.getRequestURL());
		//if (userAgent.indexOf("micromessenger") >=0) {// 是微信客户端
		if (-1 != request.getRequestURL().indexOf("hospital/affirm_purchasing")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/affirm_serveorder")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/case_history")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/casehistory_list")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/clinic_evaluate")
				|| -1 != request.getRequestURL().indexOf("hospital/consult")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/disease_detail")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/doctor_detail")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/evaluate_doctor")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/hospital_index")
				|| -1 != request.getRequestURL().indexOf("hospital/im_history")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/inquiry_detail")
				|| -1 != request.getRequestURL()
						.indexOf("hospital/look_scheme")
				|| -1 != request.getRequestURL()
						.indexOf("hospital/mine_doctor")
				|| -1 != request.getRequestURL()
						.indexOf("hospital/now_inquiry")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/photo_medicine")
				|| -1 != request.getRequestURL().indexOf(
						"hospital/doctor_search")
				|| -1 != request.getRequestURL().indexOf("hospital/to_mind")
				|| -1 != request.getRequestURL().indexOf("goods/shopping_cart")
				|| -1 != request.getRequestURL().indexOf("goods/affirm_order")
				|| -1 != request.getRequestURL()
						.indexOf("myself/affirm_change")
				|| -1 != request.getRequestURL().indexOf("myself/center_index")
				|| -1 != request.getRequestURL().indexOf(
						"myself/change_phonenum")
				|| -1 != request.getRequestURL()
						.indexOf("myself/mine_collect")
				|| -1 != request.getRequestURL().indexOf("myself/verify_phonenum")
/*				|| -1 != request.getRequestURL().indexOf("pay/pay")
*/				|| -1 != request.getRequestURL().indexOf("myself/import_code")
				|| -1 != request.getRequestURL().indexOf("im")
				|| -1 != request.getRequestURL().indexOf("order")
				|| -1 != request.getRequestURL().indexOf("person")
				|| -1 != request.getRequestURL().indexOf("shippingaddress")
				|| -1 != request.getRequestURL().indexOf("knowledge")
				|| -1 != request.getRequestURL().indexOf("evaluate")) {
			Object memberId = null;
			String openid = StringUtil.isEmpty(request.getSession()
					.getAttribute("openid")) ? null : request.getSession()
					.getAttribute("openid").toString();
			if(!StringUtil.isEmpty(openid)){
				try {
					Object obj = RedisUtil.getVal(openid);
					if (!StringUtil.isEmpty(obj)) {//如果已有toeken则把旧token返回
						JSONObject json = JSONObject.fromObject(obj);
						memberId = json.has("memberId")?json.get("memberId"):null;
					}
				} catch (Exception e) {
					logger.info("RedisUtil拦截器获取的memberid={}，异常信息："+e,memberId);
				}
				logger.info("RedisUtil拦截器获取的memberid={}",memberId);
			}
			/*if(StringUtil.isEmpty(memberId)){
				memberId = request.getSession().getAttribute("memberId");
			}*/
			System.out.println("第一次memberId拦截器获取的memberId" + memberId);
			logger.info("第一次memberId拦截器获取的{}",memberId);
			if (StringUtil.isEmpty(memberId)) {
				
				System.out.println("第二次memberId拦截器获取的opendiId" + openid);
				logger.info("第二次memberId拦截器获取的memberId拦截器获取的{}",openid);
				MemberDao memberDao = wac.getBean(MemberDao.class);
				Map<String, Object> memberMap = memberDao
						.queryMemberOne(openid);
				System.out.println(memberMap);
				logger.info("获取到的memberMap={}",memberMap);
				if (StringUtil.isEmpty(openid) || memberMap == null) {
					//String url = "https://mobile.syrjia.com/weixin/myself/center_index.html";
					String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx00a647491b70a8fa&redirect_uri=https://mobile.syrjia.com/weixin/myself/center_index.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
					((HttpServletResponse) response).sendRedirect(url);
					/*
					 * String code =
					 * StringUtil.isEmpty(request.getSession().getAttribute
					 * ("code"
					 * ))?null:request.getSession().getAttribute("code").
					 * toString (); System.out.println(code +
					 * "验签接口获取SESSION中的code"); if (StringUtil.isEmpty(openid)&&
					 * !StringUtil.isEmpty(code)) { HttpRequest httpRequests =
					 * new HttpRequest(); String strReci =
					 * "https://api.weixin.qq.com/sns/oauth2/access_token";
					 * String param = "appid=" + WeiXinConfig.appId + "&secret="
					 * + WeiXinConfig.appSecret + "&code=" + code +
					 * "&grant_type=" + WeiXinConfig.grantType; //
					 * 
					 * @SuppressWarnings("static-access") String result =
					 * httpRequests .sendGet(strReci, param); JSONObject
					 * jsonObject = JSONObject .fromObject(result); if
					 * (jsonObject.has("openid")) { openid =
					 * jsonObject.getString("openid"); Cookie cookie1 = new
					 * Cookie("openid", openid); cookie1.setMaxAge(60 * 60 * 24
					 * * 30); cookie1.setPath("/"); // cookie 保存30天
					 * responses.addCookie(cookie1); // }
					 * System.out.println(openid + "验签接口重新获取openId");
					 * request.getSession().setAttribute("openid", openid);
					 * request.getSession().setAttribute("memberId", openid);
					 * chain.doFilter(requests, response); }else{ String url =
					 * "https://mobile.syrjia.com/weixin/myself/center_index.html"
					 * ; ((HttpServletResponse) response).sendRedirect(url); }
					 */
				} else {
					logger.info("获取到的id={}",memberMap.get("id"));
					JSONObject json = new JSONObject();
					json.set("memberId", openid);
					RedisUtil.setVal(openid, 60 * 60 * 24*365,json.toString());
					/*request.getSession().setAttribute("memberId",
							memberMap.get("id"));
					request.getSession().setAttribute("phone",
							memberMap.get("phone"));
					request.getSession().setAttribute("loginname",
							memberMap.get("realname"));*/
					chain.doFilter(requests, response);
				}
			} else {
				chain.doFilter(requests, response);
			}
		} else {
			chain.doFilter(requests, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		wac = (WebApplicationContext) arg0.getServletContext().getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

}
