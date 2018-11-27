package cn.syrjia.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.syrjia.service.AppService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.SessionUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;
import cn.syrjia.wxPay.wxPay.util.http.HttpRequest;
import net.sf.json.JSONObject;

public class HtmlVerifyHandlerFilter implements Filter {

	// 日志
	private Logger logger = LogManager.getLogger(HtmlVerifyHandlerFilter.class);
	
	private AppService appService;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest requests, ServletResponse responses,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) requests;
		HttpServletResponse response = ((HttpServletResponse) responses);
		String openId = SessionUtil.getOpenId(request);
		String code = request.getParameter("code");
		System.out.println("hmtl拦截器"+openId+"&$$$$$code"+code+"@@@@@@@@@");
		logger.info("hmtl拦截器获取的openId"+openId);
		logger.info("hmtl拦截器获取的code"+code);
		if (StringUtil.isEmpty(openId) && !StringUtil.isEmpty(code)) {
			request.getSession().setAttribute("code", code);
			HttpRequest httpRequests = new HttpRequest();
			openId = GetOpenId.getOpenId(httpRequests, request, code,response);
			logger.info("hmtl拦截器获取的请求后在微信获取的openId："+openId);
		}
		boolean flag=request.getRequestURL().indexOf("activity/activity")>0
				||request.getRequestURL().indexOf("goods/commodity_details")>0
				//||request.getRequestURL().indexOf("knowledge/knowledge_circle")>0
				||request.getRequestURL().indexOf("myself/doctor_card")>0
				||request.getRequestURL().indexOf("register/aid_card")>0
				||request.getRequestURL().indexOf("hospital/doctor_detail")>0
				||request.getRequestURL().indexOf("hospital/line_below.html")>0
				//||request.getRequestURL().indexOf("hospital/look_scheme")>0
				//||request.getRequestURL().indexOf("knowledge/article_detail")>0
				||request.getRequestURL().indexOf("goods/store_index")>0
				||request.getRequestURL().indexOf("order/queryPayOrderDetail")>0
				||request.getRequestURL().indexOf("wx/wxJsApiPay")>0
				||request.getRequestURL().indexOf("pay/pay")>0
				||request.getRequestURL().indexOf("myself/user_agreement")>0
				||request.getRequestURL().indexOf("myself/bill")>0
				||request.getRequestURL().indexOf("settle_accounts")>0
				||request.getRequestURL().indexOf("bill_detail.html")>0
				||request.getRequestURL().indexOf("logistics/logistics")>0
				||request.getRequestURL().indexOf("register/download_assistant")>0
				||request.getRequestURL().indexOf("register/download_doctor")>0
				//||request.getRequestURL().indexOf("goods/commodity_details")>0
				||request.getRequestURL().indexOf("question/faq")>0
				||request.getRequestURL().indexOf("serverOnline/service")>0
				||request.getRequestURL().indexOf("myself/binding_card")>0
				||request.getRequestURL().indexOf("myself/history_card")>0
				||request.getRequestURL().indexOf("im/be_careful")>0
				||request.getRequestURL().indexOf("myself/doctor_card")>0
				||request.getRequestURL().indexOf("sales/article_detail")>0
				||request.getRequestURL().indexOf("leadPhone/leadPhone")>0
				||request.getRequestURL().indexOf("toAPP/toAPP")>0
				||request.getRequestURL().indexOf("register/doctor_register_v2")>0
				||request.getRequestURL().indexOf("register/downloadGuide")>0;
		String ua = ((HttpServletRequest) request).getHeader("user-agent")  
		        .toLowerCase();
		openId = "o92LXv9cdADQ8xfcDNDlfycbGIhg";
		request.getSession().setAttribute("openid",openId);
		/*JSONObject json = new JSONObject();
		json.set("memberId", openId);
		RedisUtil.setVal(openId, 60 * 60 * 24*365,json.toString());
		Object obj = RedisUtil.getVal(openId);
		if (!StringUtil.isEmpty(obj)) {//如果已有toeken则把旧token返回
			json = JSONObject.fromObject(obj);
			System.out.println("RedisUtil.getVal(openid);"+ (json.has("memberId")?json.get("memberId"):null));
		}*/
		//request.getSession().setAttribute("memberId",openId);
		if(flag&&request.getRequestURL().indexOf("pay/pay")==-1){
			chain.doFilter(requests, responses);
		}else{
			if (StringUtil.isEmpty(openId)&&(ua.indexOf("micromessenger") > 0||!flag)) {
				logger.info("openid为空");
				@SuppressWarnings("unchecked")
				Map<String, String[]> params = request.getParameterMap();
				String queryString = "";
				for (String key : params.keySet()) {
					String[] values = params.get(key);
					for (int i = 0; i < values.length; i++) {
						String value = values[i];
						queryString += key + "=" + value + "&";
					}
				}
				// 去掉最后一个空格
				if (StringUtil.isEmpty(queryString.trim())) {
					queryString = "";
				} else {
					queryString = "?"
							+ queryString.substring(0, queryString.length() - 1);
				}
				
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ WeiXinConfig.appId
						+ "&redirect_uri="
						+ request.getRequestURL().toString().replace("http","https")
						+ queryString
						+ "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
				((HttpServletResponse) response).sendRedirect(url);
			}else{
				/*Cookie cookie = new Cookie("openid",openId);
				cookie.setMaxAge(60 * 60 * 24 * 30);
				cookie.setPath("/");
				response.addCookie(cookie);*/
				chain.doFilter(requests, responses);
			}
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		 ServletContext sc = fConfig.getServletContext(); 
	     XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
	     if(cxt != null && cxt.getBean("appService") != null && appService == null)
	    	 appService = (AppService) cxt.getBean("appService");      
	}

}
