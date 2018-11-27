package cn.syrjia.wxPay.wxPay.service.impl;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.entity.WXPayBean;
import cn.syrjia.service.OrderService;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.weixin.util.TenpayUtil;
import cn.syrjia.wxPay.wxPay.dao.WxPayDao;
import cn.syrjia.wxPay.wxPay.service.WxPayService;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Liwenlong
 * 
 */
@SuppressWarnings({ "unused", "deprecation" })
@Service("wxPayService")
public class WxPayServiceImpl extends BaseServiceImpl implements WxPayService {

	@Resource(name = "wxPayDao")
	WxPayDao wxPayDao;
	
	@Resource(name = "orderService")
	OrderService orderService;

	private Logger logger = LogManager.getLogger(WxPayServiceImpl.class);

	// ---必须参数
	// 商品描述根据情况修改
	private String body = "ShangYiJia";

	String grantType = "authorization_code";
	private WXPayBean jsApiBean;

	/**
	 * @descriptions:jsApi接口
	 * @copyright：
	 */

	@Override
	public Map<String, Object> getPackage(String openid, WXPayBean jsApiBean) {
		Map<String, Object> map = wxPayDao.getPackage(openid, jsApiBean);
		try {
			// wxPayDao.addEntity(jsApiBean);
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> queryUserOpenId() {
		return wxPayDao.queryUserOpenId();
	}

	/**
	 * 调用jsApi接口 微信支付
	 * 
	 * @return
	 */
	public Map<String, Object> jsApi(HttpServletRequest request, String openId,
			String orderNo, Double pay) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 用户id
		logger.info("第一次openId" + openId);
		Double orderPrice = 0.0;
		String finaPackage = "";
		String notifyUrl = "";
		logger.info("------------jsApi start------------------------");
		logger.info("orderNo--------------------" + orderNo);
		orderPrice = pay;
		notifyUrl = WeiXinConfig.notify_url;
		logger.info(openId + "调之前有没有");
		returnMap = getFinalPackage(orderNo, openId, orderPrice, notifyUrl);
		System.out.println(finaPackage + "finaPackage");

		/*
		 * if(yjOrderPrice==0.0){ yjOrderPrice = orderPrice; }
		 * request.setAttribute("isSign", isSign);
		 * request.setAttribute("finaPackage", finaPackage);
		 * request.setAttribute("orderNo",
		 * (docOrderNo==null||"".equals(docOrderNo))?orderNo:docOrderNo);
		 * request.setAttribute("orderPrice", orderPrice);
		 * request.setAttribute("yjOrderPrice", yjOrderPrice);
		 * request.setAttribute("serverId",serverId);
		 */
		return returnMap;
	}

	/**
	 * 发起支付公共方法
	 * 
	 * @param orderNo
	 * @param openId
	 * @param orderPrice
	 * @return
	 */
	private Map<String, Object> getFinalPackage(String orderNo, String openId,
			Double orderPrice, String notifyUrl) {
		if (StringUtil.isEmpty(openId)) {
			openId = "";
		}
		logger.info(openId + "0000000000000000有没有");
		jsApiBean = new WXPayBean();
		jsApiBean.setWxid(Util.getUUID());
		jsApiBean.setAppId(WeiXinConfig.appId);
		jsApiBean.setMchId(WeiXinConfig.mch_id);
		jsApiBean.setDeviceInfo("");
		jsApiBean.setNonceStr(WeiXinConfig.number32());
		jsApiBean.setBody(body);
		jsApiBean.setAttach("attach");
		jsApiBean.setOutTradeNo(orderNo);
		logger.info("orderPrice:::::::::::"
				+ Integer.parseInt(TenpayUtil.getMoney(orderPrice.toString())));
		jsApiBean.setTotalFee(Integer.parseInt(TenpayUtil.getMoney(orderPrice
				.toString())));
		jsApiBean.setSpbillCreateIp(WeiXinConfig.SPBILL_CREATE_IP);
		jsApiBean.setTimeStart((int) (System.currentTimeMillis() / 1000) + "");
		jsApiBean.setNotifyUrl(notifyUrl);
		jsApiBean.setTradeType(WeiXinConfig.trade_type);
		logger.info(openId + "weixin---------------------openid");
		jsApiBean.setOpenid(openId);
		jsApiBean.setOperdate((int) (System.currentTimeMillis() / 1000));
		jsApiBean.setSign("-1");
		return wxPayDao.getPackage(openId, jsApiBean);
	}

	/**
	 * @descriptions:获得随机字符串
	 * @copyright：
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(8) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom + "fasdfasdf";
	}

	/**
	 * 元转换成分
	 * 
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if (amount == null) {
			return "";
		} // 金额转化为分为单位
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
																// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "") + "00");
		}
		return amLong.toString();
	}
}
