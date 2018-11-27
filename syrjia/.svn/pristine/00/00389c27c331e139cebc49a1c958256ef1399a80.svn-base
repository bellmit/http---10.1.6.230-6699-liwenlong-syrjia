package cn.syrjia.wxPay.wxPayReceive.controller;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.service.AppDoctorService;
import cn.syrjia.service.ImService;
import cn.syrjia.service.OrderService;
import cn.syrjia.service.PushService;
import cn.syrjia.util.Notify;
import cn.syrjia.util.Util;
import cn.syrjia.util.sendModelMsgUtil;
import cn.syrjia.weixin.util.CommonUtil;
import cn.syrjia.weixin.util.PayCommonUtil;
import cn.syrjia.weixin.util.RequestHandler;
import cn.syrjia.weixin.util.XMLUtil;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;
import cn.syrjia.wxPay.wxPay.util.WeiXinUtil;
import cn.syrjia.wxPay.wxPayReceive.bean.WXPayHttpReciBean;
import cn.syrjia.wxPay.wxPayReceive.service.WxPayReciService;

/**
 * 
 * @author liwenlong
 * 
 */
@Controller
@RequestMapping("/wx")
public class WXPayReceiveController {
	private Log logger = LogFactory.getLog(WXPayReceiveController.class);
	@Resource(name = "wxPayReciService")
	WxPayReciService wxPayReciService;

	@Resource(name = "orderService")
	OrderService orderService;
	
	@Resource(name = "pushService")
	PushService pushService;
	
	@Resource(name = "imService")
	ImService imService;
	
	@Resource(name = "appDoctorService")
	AppDoctorService appDoctorService;
	
	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;
	
	@RequestMapping("/updateOrderState")
	@ResponseBody
	public Integer updateOrderState(HttpServletRequest request){
		String orderNo = "TW-U20180901194815";
		RequestHandler reqHandler = new RequestHandler(null, null);
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", WeiXinConfig.appId);
		parameters.put("mch_id", WeiXinConfig.mch_id);
		parameters.put("out_trade_no", orderNo);// 商户订单号，要唯一
		parameters.put("nonce_str", WeiXinConfig.number32());
		reqHandler.init(WeiXinConfig.appId, WeiXinConfig.appSecret,
				WeiXinConfig.key);
		String sign = reqHandler.createSign(parameters);// 签名
		parameters.put("sign", sign);
		String reuqestXml = PayCommonUtil.getRequestXml(parameters);

		String result = CommonUtil.httpsRequest(
				WeiXinConfig.CHECK_ORDER_URL, "POST", reuqestXml);
		System.out.println(result);
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> m = XMLUtil.doXMLParse(result);
			System.out.println(m);
			WXPayHttpReciBean weixinPayRec = new WXPayHttpReciBean();
			if (m.get("return_code").equals("SUCCESS")) {
				weixinPayRec.setAppId(m.get("appid").toString());
				weixinPayRec.setMchId(m.get("mch_id").toString());
				weixinPayRec.setNonceStr(m.get("nonce_str").toString());
				weixinPayRec.setSign(m.get("sign").toString());
				weixinPayRec.setResultCode(m.get("result_code").toString());
				System.out.println("result_code:::::::::::::::::::::::"
						+ m.get("result_code"));
				if (m.get("result_code").equals("SUCCESS")) {
					weixinPayRec.setOpenId(m.get("openid").toString());
					weixinPayRec.setIsSubscribe(m.get("is_subscribe")
							.toString());
					weixinPayRec.setTradeType(m.get("trade_type").toString());
					weixinPayRec.setBankType(m.get("bank_type").toString());
					System.out
							.println("------------------getMoney---------------");
					weixinPayRec.setTotalFee(Integer.parseInt(m
							.get("total_fee").toString()));
					System.out
							.println("------------------getMoney----end-----------");
					weixinPayRec.setTransactionId((m.get("transaction_id")
							.toString()));
					weixinPayRec
							.setOutTradeNo(m.get("out_trade_no").toString());
					System.out.println("---------m.get(time_end)--------"
							+ m.get("time_end"));
					weixinPayRec.setTimeEnd(m.get("time_end").toString());
					weixinPayRec.setPayType("购物");
				}
			}
			if ("SUCCESS".equals(weixinPayRec.getResultCode())) { // 支付成功
				// 获得订单，
				System.out.println("----m.get(out_trade_no)----------"
						+ m.get("out_trade_no"));
				System.out.println("----m.get(out_trade_no)----------"
						+ m.get("out_trade_no"));
				// 改变订单状态
				// updateOrderStatus(request, weixinPayRec,2);
				Integer count = wxPayReciService.queryCount(m.get(
						"out_trade_no").toString());
				if (count <= 0) {
					weixinPayRec.setWxreId(Util.getUUID());
					wxPayReciService.addEntity(weixinPayRec);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("msgtype", "text");
					params.put("url", "");
					params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
					params.put("content", "订单："+m.get("out_trade_no").toString()+"回调");
					sendModelMsgUtil.sendCustomMsg(params, appDoctorDao);
					// 改变订单状态
					updateOrderStatus(request, weixinPayRec, 2,1);
					// response.getWriter().write(resXml);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Map<String,Object> map = orderService.queryBysqlMap("SELECT o.orderNo,o.orderType,w.transactionId from t_order o  INNER JOIN t_wxpay_reci w on w.outTradeNo=o.orderNo WHERE o.orderNo='"+orderNo+"'  LIMIT 0,1", null);
		if(map!=null){
			System.out.println(map);
			orderService.updateState(request, 1, 2, map.get("orderNo").toString(), Integer.valueOf(map.get("orderType").toString()), null, map.get("transactionId").toString(), 2);
		}*/
		/*Map<String,Object> order = orderService.queryOrderByOrderNo("TW-I20180827111323");
		orderService.paySuccessPush(request,order);*/
		return 1;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/notifyWeixin")
	@ResponseBody
	public String notifyWeixin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out
				.println("-------------------notifyWeixin start-------------------------");
		System.out
				.println("-------------------notifyWeixin start-------------------------");
		String inputLine;
		String notityXml = "";
		String resXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			System.out.println("notityXml-------------------" + notityXml);
			request.getReader().close();

			System.out.println("接收到的报文：::::::::::::" + notityXml);
			System.out.println("接收到的报文：::::::::::::" + notityXml);
			Notify no = new Notify();
			Map m = no.parseXmlToList2(notityXml);
			System.out.println("------------no.parseXmlToList2(notityXml)::::"
					+ m);
			System.out.println("return_code:::::::::::::::::::::::"
					+ m.get("return_code"));
			WXPayHttpReciBean weixinPayRec = new WXPayHttpReciBean();
			if (m.get("return_code").equals("SUCCESS")) {
				weixinPayRec.setAppId(m.get("appid").toString());
				weixinPayRec.setMchId(m.get("mch_id").toString());
				weixinPayRec.setNonceStr(m.get("nonce_str").toString());
				weixinPayRec.setSign(m.get("sign").toString());
				weixinPayRec.setResultCode(m.get("result_code").toString());
				System.out.println("result_code:::::::::::::::::::::::"
						+ m.get("result_code"));
				if (m.get("result_code").equals("SUCCESS")) {
					weixinPayRec.setOpenId(m.get("openid").toString());
					weixinPayRec.setIsSubscribe(m.get("is_subscribe")
							.toString());
					weixinPayRec.setTradeType(m.get("trade_type").toString());
					weixinPayRec.setBankType(m.get("bank_type").toString());
					System.out
							.println("------------------getMoney---------------");
					weixinPayRec.setTotalFee(Integer.parseInt(m
							.get("total_fee").toString()));
					System.out
							.println("------------------getMoney----end-----------");
					weixinPayRec.setTransactionId((m.get("transaction_id")
							.toString()));
					weixinPayRec
							.setOutTradeNo(m.get("out_trade_no").toString());
					System.out.println("---------m.get(time_end)--------"
							+ m.get("time_end"));
					weixinPayRec.setTimeEnd(m.get("time_end").toString());
					weixinPayRec.setPayType("购物");
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("appId", WeiXinConfig.appId);
			map.put("mchId", WeiXinConfig.mch_id);
			map.put("transactionId", m.get("transaction_id").toString());
			map.put("outTradeNo", m.get("out_trade_no").toString());
			map.put("nonceStr", WeiXinConfig.number32());
			map.put("sign", m.get("sign").toString());
			// 将订单查询参数加入数据库存档

			// 判断支付是否成功
			System.out.println("-----weixinPayRec.getResultCode()------------"
					+ weixinPayRec.getResultCode());
			if ("SUCCESS".equals(weixinPayRec.getResultCode())) { // 支付成功
				// 获得订单，
				System.out.println("----m.get(out_trade_no)----------"
						+ m.get("out_trade_no"));
				System.out.println("----m.get(out_trade_no)----------"
						+ m.get("out_trade_no"));
				resXml = "<xml>"
						+ "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				// 改变订单状态
				// updateOrderStatus(request, weixinPayRec,2);
				Integer count = wxPayReciService.queryCount(m.get(
						"out_trade_no").toString());
				if (count <= 0) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("msgtype", "text");
					params.put("url", "");
					params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
					params.put("content", "订单："+m.get("out_trade_no").toString()+"回调");
					sendModelMsgUtil.sendCustomMsg(params, appDoctorDao);
					// 改变订单状态
					updateOrderStatus(request, weixinPayRec, 2,1);
					// response.getWriter().write(resXml);
					weixinPayRec.setWxreId(Util.getUUID());
					wxPayReciService.addEntity(weixinPayRec);
				}
			} else {
				System.out.println("------------------订单查询接口--------------");
				weixinPayRec.setResultCode("NOPAY");
				updateOrderStatus(request, weixinPayRec, 2,null);

				resXml = "<xml>"
						+ "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>"
						+ "</xml> ";
			}

			BufferedOutputStream outs = new BufferedOutputStream(
					response.getOutputStream());
			outs.write(resXml.getBytes());
			outs.flush();
			outs.close();
		} catch (Exception e) {
			logger.debug("--------------Exception e  Exception e-"
					+ e.getLocalizedMessage());
			logger.debug("--------Exception e  Exception---" + e.getMessage());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("msgtype", "text");
			params.put("url", "");
			params.put("toUser", "o92LXv1ZIHTjleF3MYPFlAiV7Bas");
			params.put("content", "异常信息："+e);
			sendModelMsgUtil.sendCustomMsg(params, appDoctorDao);
			e.printStackTrace();
		}
		logger.debug("-------------------notifyWeixin end-------------------------");

		return "success";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/notifyAppWeixin")
	@ResponseBody
	public String notifyAppWeixin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out
				.println("-------------------notifyWeixin start-------------------------");
		System.out
				.println("-------------------notifyWeixin start-------------------------");
		String inputLine;
		String notityXml = "";
		String resXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			System.out.println("notityXml-------------------" + notityXml);
			request.getReader().close();

			System.out.println("接收到的报文：::::::::::::" + notityXml);
			System.out.println("接收到的报文：::::::::::::" + notityXml);
			Notify no = new Notify();
			Map m = no.parseXmlToList2(notityXml);
			System.out.println("------------no.parseXmlToList2(notityXml)::::"
					+ m);
			System.out.println("return_code:::::::::::::::::::::::"
					+ m.get("return_code"));
			WXPayHttpReciBean weixinPayRec = new WXPayHttpReciBean();
			if (m.get("return_code").equals("SUCCESS")) {
				weixinPayRec.setAppId(m.get("appid").toString());
				weixinPayRec.setMchId(m.get("mch_id").toString());
				weixinPayRec.setNonceStr(m.get("nonce_str").toString());
				weixinPayRec.setSign(m.get("sign").toString());
				weixinPayRec.setResultCode(m.get("result_code").toString());
				System.out.println("result_code:::::::::::::::::::::::"
						+ m.get("result_code"));
				if (m.get("result_code").equals("SUCCESS")) {
					weixinPayRec.setOpenId(m.get("openid").toString());
					weixinPayRec.setIsSubscribe(m.get("is_subscribe")
							.toString());
					weixinPayRec.setTradeType(m.get("trade_type").toString());
					weixinPayRec.setBankType(m.get("bank_type").toString());
					System.out
							.println("------------------getMoney---------------");
					weixinPayRec.setTotalFee(Integer.parseInt(m
							.get("total_fee").toString()));
					System.out
							.println("------------------getMoney----end-----------");
					weixinPayRec.setTransactionId((m.get("transaction_id")
							.toString()));
					weixinPayRec
							.setOutTradeNo(m.get("out_trade_no").toString());
					System.out.println("---------m.get(time_end)--------"
							+ m.get("time_end"));
					weixinPayRec.setTimeEnd(m.get("time_end").toString());
					weixinPayRec.setPayType("购物");
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("appId", WeiXinConfig.appId);
			map.put("mchId", WeiXinConfig.mch_id);
			map.put("transactionId", m.get("transaction_id").toString());
			map.put("outTradeNo", m.get("out_trade_no").toString());
			map.put("nonceStr", WeiXinConfig.number32());
			map.put("sign", m.get("sign").toString());
			// 将订单查询参数加入数据库存档

			// 判断支付是否成功
			System.out.println("-----weixinPayRec.getResultCode()------------"
					+ weixinPayRec.getResultCode());
			if ("SUCCESS".equals(weixinPayRec.getResultCode())) { // 支付成功
				// 获得订单，
				System.out.println("----m.get(out_trade_no)----------"
						+ m.get("out_trade_no"));
				System.out.println("----m.get(out_trade_no)----------"
						+ m.get("out_trade_no"));
				resXml = "<xml>"
						+ "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				/*
				 * WXPayHttpReciBean recibean = new WXPayHttpReciBean();
				 * recibean.setOutTradeNo(m.get("out_trade_no").toString());
				 * WXPayHttpReciBean bean = wxPayReciService
				 * .queryByEntity(recibean);
				 */
				Integer count = wxPayReciService.queryCount(m.get(
						"out_trade_no").toString());
				Integer payCnt = orderService.queryPayResultByOrderNo(m.get(
						"out_trade_no").toString());
				if (count == 0&&payCnt==0) {
					
					// 改变订单状态
					updateOrderStatus(request, weixinPayRec, 4,2);
					weixinPayRec.setWxreId(Util.getUUID());
					// response.getWriter().write(resXml);
					wxPayReciService.addEntity(weixinPayRec);
				}
			} else {
				System.out.println("------------------订单查询接口--------------");
				weixinPayRec.setResultCode("NOPAY");
				updateOrderStatus(request, weixinPayRec, 4,null);

				resXml = "<xml>"
						+ "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>"
						+ "</xml> ";
			}

			BufferedOutputStream outs = new BufferedOutputStream(
					response.getOutputStream());
			outs.write(resXml.getBytes());
			outs.flush();
			outs.close();
		} catch (Exception e) {
			logger.debug("--------------Exception e  Exception e-"
					+ e.getLocalizedMessage());
			logger.debug("--------Exception e  Exception---" + e.getMessage());
			e.printStackTrace();
		}
		logger.debug("-------------------notifyWeixin end-------------------------");

		return "success";

	}

	public String updateOrderStatus(HttpServletRequest request,
			WXPayHttpReciBean recibean, Integer payWay,Integer orderWay) {
		String resultState = recibean.getResultCode();
		System.out.println(resultState + "---resultState");
		Integer returnPayStatus = 0;

		// * 1-等待付款 2-已付款 3-退款中 4-已退款（交易关闭） 5-交易成功 6-取消

		// SUCCESS 支付成功
		// REFUND 转入退款
		// NOTPAY 未支付
		// CLOSED 已关闭
		// REVOKED 已撤销
		// USERPAYING 用户支付中
		// NOPAY 未支付(超时)
		// PAYERROR 支付失败（如银行返回问题）
		System.out.println(recibean.getOutTradeNo());
		try {
			Map<String,Object> order = orderService.queryOrderByOrderNo(recibean.getOutTradeNo());
			if (order != null) {
				int orderTypeCode = Integer.valueOf(order.get("orderType").toString());
				if ("SUCCESS".equals(resultState)) {
					System.out.println("订单号：+===================="
							+ recibean.getOutTradeNo() + "订单价格============"
							+ Double.valueOf(fenToYuan(recibean.getTotalFee())));
					int payStatus = Integer.valueOf(order.get("paymentStatus").toString());
					if (payStatus == 2 || payStatus == 5) {
						System.out.println("***订单已支付或已完成****");
						WeiXinUtil.refund(request, recibean.getOutTradeNo(),
								Double.valueOf(fenToYuan(recibean.getTotalFee())),
								Double.valueOf(fenToYuan(recibean.getTotalFee())),
								payWay + "");
					} else {
						int status = 2;
						Integer updateOk = orderService.updateState(request, 1, status,
								recibean.getOutTradeNo(), orderTypeCode, null,
								recibean.getTransactionId(),orderWay, payWay);// 更改订单及详情的状态
						if(updateOk>0){
							// 添加支付信息
							System.out.println("支付成功了。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
							orderService.paySuccessPush(request,order);
						}
						orderService.addPayment(recibean.getOutTradeNo(), recibean
								.getTotalFee().toString(), order.get("memberId").toString(),
								recibean.getTransactionId(), resultState);
						return "success";
					}
				} else if (resultState == "CLOSED" || resultState == "REVOKED"
						|| resultState == "ORDERCLOSED") {
					Map<String, Object> map = WeiXinUtil.refund(request,
							recibean.getOutTradeNo(),
							Double.valueOf(fenToYuan(recibean.getTotalFee())),
							Double.valueOf(fenToYuan(recibean.getTotalFee())),
							payWay + "");
					if (null != map && "SUCCESS".equals(map.get("result_code"))) {
						returnPayStatus = 4;
					} else {
						returnPayStatus = 2;
					}
					orderService.updateState(request, 1, returnPayStatus,
							recibean.getOutTradeNo(), orderTypeCode, null,
							recibean.getTransactionId(),orderWay, payWay);// 更改订单及详情的状态
				}
			} else {
				System.out
						.println("------------------------11111111111------------------");
			}
		} catch (Exception e) {
			System.out
			.println("------------------------微信回调异常------------------"+e);
		}
		return null;
	}

	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	/**
	 * 
	 * 功能描述：金额字符串转换：单位分转成单元
	 * 
	 * @param str
	 *            传入需要转换的金额字符串
	 * @return 转换后的金额字符串
	 */
	public static String fenToYuan(Object o) {
		if (o == null)
			return "0.00";
		String s = o.toString();
		int len = -1;
		StringBuilder sb = new StringBuilder();
		if (s != null && s.trim().length() > 0 && !s.equalsIgnoreCase("null")) {
			s = removeZero(s);
			if (s != null && s.trim().length() > 0
					&& !s.equalsIgnoreCase("null")) {
				len = s.length();
				int tmp = s.indexOf("-");
				if (tmp >= 0) {
					if (len == 2) {
						sb.append("-0.0").append(s.substring(1));
					} else if (len == 3) {
						sb.append("-0.").append(s.substring(1));
					} else {
						sb.append(s.substring(0, len - 2)).append(".")
								.append(s.substring(len - 2));
					}
				} else {
					if (len == 1) {
						sb.append("0.0").append(s);
					} else if (len == 2) {
						sb.append("0.").append(s);
					} else {
						sb.append(s.substring(0, len - 2)).append(".")
								.append(s.substring(len - 2));
					}
				}
			} else {
				sb.append("0.00");
			}
		} else {
			sb.append("0.00");
		}
		return sb.toString();
	}

	/**
	 * 
	 * 功能描述：去除字符串首部为"0"字符
	 * 
	 * @param str
	 *            传入需要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String removeZero(String str) {
		char ch;
		String result = "";
		if (str != null && str.trim().length() > 0
				&& !str.trim().equalsIgnoreCase("null")) {
			try {
				for (int i = 0; i < str.length(); i++) {
					ch = str.charAt(i);
					if (ch != '0') {
						result = str.substring(i);
						break;
					}
				}
			} catch (Exception e) {
				result = "";
			}
		} else {
			result = "";
		}
		return result;
	}
	
}
