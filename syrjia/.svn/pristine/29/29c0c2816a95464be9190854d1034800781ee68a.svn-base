package cn.syrjia.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;

@Controller
public class SessionVerifyHandlerInterceptor extends HandlerInterceptorAdapter {

	@Resource(name = "baseService")
	BaseServiceInterface baseServiceInterface;

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		try {
//			Object obj = request.getSession().getAttribute("openid");
//			if (null == obj) {
//				super.postHandle(request, response, handler, modelAndView);
//			} else {
//				WeiXinUser user = new WeiXinUser();
//				user.setOpenid(obj.toString());
//				user.setState(0);
//				WeiXinUser weixinUser = baseServiceInterface
//						.queryByEntity(user);
//				if (null == weixinUser
//						|| StringUtil.isEmpty(weixinUser.getOpenid())) {
//					response.sendRedirect(
//							"https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzIxODU2MzgzMQ==&scene=124#wechat_redirect");
//				} else {
//					super.postHandle(request, response, handler, modelAndView);
//				}
//			}
//		} catch (Exception e) {
			super.postHandle(request, response, handler, modelAndView);
//		}
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		try {
			Object obj = request.getSession().getAttribute("openid");
			if (-1 != request.getRequestURL()
					.indexOf("centerIndex/centerIndex")) {
				return true;
			}
			if (null == obj || "".equals(obj)) {
				response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WeiXinConfig.appId+"&redirect_uri="
						+ request.getRequestURL()
						+ "?"
						+ request.getQueryString()
						+ "&response_type=code&scope=snsapi_base&state=1#wechat_redirect");
			}
			return true;
		} catch (Exception e) {
			try {
				response.sendRedirect(request.getContextPath()
						+ "/login.action");
				return false;
			} catch (Exception e1) {
				return false;
			}
		}
	}
}
