package cn.syrjia.alipay;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.common.impl.BaseController;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.GoodsDao;
import cn.syrjia.dao.GoodsOrderDao;
import cn.syrjia.dao.GoodsShopCartDao;
import cn.syrjia.entity.AlipayResult;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.PatientData;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.dao.DoctorOrderDao;
import cn.syrjia.hospital.entity.MyEvaBanner;
import cn.syrjia.hospital.service.AppDoctorService;
import cn.syrjia.service.ImService;
import cn.syrjia.service.OrderService;
import cn.syrjia.service.PushService;
import cn.syrjia.util.GetSig;
import cn.syrjia.util.JsonUtil;
import cn.syrjia.util.Producer;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.util.sendModelMsgUtil;
import cn.syrjia.util.alipay.config.AlipayConfig;
import cn.syrjia.util.alipay.config.ReturnBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;

/**
 * 阿里支付controller ClassName: AliPayController
 * 
 * @Description: TODO
 */
@Controller
@RequestMapping("/alipay")
public class AliPayController extends BaseController {

	@Resource(name = "orderService")
	OrderService orderService;

	@Resource(name = "goodsOrderDao")
	GoodsOrderDao goodsOrderDao;

	@Resource(name = "pushService")
	PushService pushService;

	@Resource(name = "imService")
	ImService imService;

	@Resource(name = "goodsShopCartDao")
	GoodsShopCartDao goodsShopCartDao;

	@Resource(name = "goodsDao")
	GoodsDao goodsDao;

	@Resource(name = "appDoctorService")
	AppDoctorService appDoctorService;

	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;

	@Resource(name = "doctorOrderDao")
	DoctorOrderDao doctorOrderDao;

	@Resource(name = "doctorDao")
	DoctorDao doctorDao;

	/**
	 * 支付宝支付
	 * @param request
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/alipayWxPay")
	@ResponseBody
	public Map<String, Object> alipayWxPay(HttpServletRequest request,
			String orderNo) throws Exception {
		if (StringUtil.isEmpty(orderNo)) {
			return Util.resultMap(configCode.code_1029, null);
		}
		//根据订单号查询订单信息
		Map<String, Object> order = orderService.queryOrderByOrderNo(orderNo);
		if (order == null) {
			return Util.resultMap(configCode.code_1034, null);
		} else {
			Integer payStatus = Integer.valueOf(order.get("paymentStatus")
					.toString());
			Integer state = Integer.valueOf(order.get("state").toString());
			Integer orderType = Integer.valueOf(order.get("orderType")
					.toString());
			if (payStatus == 2 || payStatus == 5) {
				return Util.resultMap(configCode.code_1024, null);
			} else if (state == 0) {
				return Util.resultMap(configCode.code_1111, null);
			} else if (payStatus == 6) {
				return Util.resultMap(configCode.code_1083, null);
			}

			if (orderType == 4 || orderType == 5 || orderType == 7
					|| orderType == 9) {
				Integer count = doctorOrderDao.checkNoFinishOrderByPatientId(
						order.get("patientId").toString(), order
								.get("doctorId").toString(), orderType);
				if (count > 0) {
					return Util.resultMap(configCode.code_1072, null);
				}
				Map<String, Object> dayMap = doctorOrderDao
						.getDoctorOrderDayCount(order.get("doctorId")
								.toString(), orderType);

				Map<String, Object> docMap = doctorDao.queryDocotrById(order
						.get("doctorId").toString(), order.get("memberId")
						.toString());
				if (docMap == null) {
					return Util.resultMap(configCode.code_1032, null);
				}
				Integer dayOrderCount = 0;

				if (dayMap != null) {
					dayOrderCount = Integer.valueOf(dayMap.get("dayOrderCount")
							.toString());
				}
				Integer isOnlineTwGh = Integer.valueOf(docMap.get(
						"isOnlineTwGh").toString());
				Integer isOnlinePhoneGh = Integer.valueOf(docMap.get(
						"isOnlinePhoneGh").toString());
				Integer isOnlineTwZx = Integer.valueOf(docMap.get(
						"isOnlineTwZx").toString());
				Integer isOnlinePhoneZx = Integer.valueOf(docMap.get(
						"isOnlinePhoneZx").toString());
				if (orderType == 4 || orderType == 5) {
					if (isOnlineTwGh == 0) {
						return Util.resultMap(configCode.code_1057, null);
					} else {
						if (!StringUtil.isEmpty(docMap
								.get("acceptTwOrderCount"))) {
							Integer acceptTwOrderCount = Integer.valueOf(docMap
									.get("acceptTwOrderCount").toString());
							if (dayOrderCount - acceptTwOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					}
				} else if (orderType == 6) {
					if (isOnlineTwZx == 0) {
						return Util.resultMap(configCode.code_1054, null);
					} else {
						if (!StringUtil.isEmpty(docMap
								.get("acceptTwZxOrderCount"))) {
							Integer acceptTwZxOrderCount = Integer
									.valueOf(docMap.get("acceptTwZxOrderCount")
											.toString());
							if (dayOrderCount - acceptTwZxOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					}
				} else if (orderType == 7 || orderType == 9) {
					if (isOnlinePhoneGh == 0) {
						return Util.resultMap(configCode.code_1055, null);
					} else {
						if (!StringUtil.isEmpty(docMap
								.get("acceptPhoneOrderCount"))) {
							Integer acceptPhoneOrderCount = Integer
									.valueOf(docMap
											.get("acceptPhoneOrderCount")
											.toString());
							if (dayOrderCount - acceptPhoneOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					}
				} else if (orderType == 8) {
					if (isOnlinePhoneZx == 0) {
						return Util.resultMap(configCode.code_1056, null);
					} else {
						if (!StringUtil.isEmpty(docMap
								.get("acceptPhoneZxOrderCount"))) {
							Integer acceptPhoneZxOrderCount = Integer
									.valueOf(docMap.get(
											"acceptPhoneZxOrderCount")
											.toString());
							if (dayOrderCount - acceptPhoneZxOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					}
				}
			}

			Map<String, Object> recordOrder = orderService
					.queryRecordOrderByMainOrderNo(orderNo);

			if (null != recordOrder
					&& Integer.valueOf(recordOrder.get("state").toString())!=1) {
				return Util.resultMap(configCode.code_1105, null);
			}

			// 商户订单号，商户网站订单系统中唯一订单号，必填
			String out_trade_no = new String(orderNo.getBytes("ISO-8859-1"),
					"UTF-8");
			// 订单名称，必填
			String subject = "上医仁家-商品购买";
			System.out.println(subject);
			Map<String, Object> yfMap = getPositage(order);
			if (Integer.valueOf(yfMap.get("respCode").toString()) != 1001) {
				return yfMap;
			}
			// order.setReceiptsPrice(Double.valueOf(yfMap.get("data").toString()));
			// 付款金额，必填
			// order.setReceiptsPrice(0.01);
			String total_amount = new String(order.get("receiptsPrice")
					.toString().getBytes("ISO-8859-1"), "UTF-8");
			// 商品描述，可空
			String body = new String("上医仁家".getBytes("ISO-8859-1"), "UTF-8");
			// 超时时间 可空
			String timeout_express = "30m";
			// 销售产品码 必填
			String product_code = "QUICK_WAP_WAY";
			/**********************/
			// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
			// 调用RSA签名方式
			AlipayClient client = new DefaultAlipayClient(AlipayConfig.aliPay,
					AlipayConfig.appId, AlipayConfig.private_key,
					AlipayConfig.FORMAT, AlipayConfig.input_charset,
					AlipayConfig.ali_public_key, AlipayConfig.sign_type);
			AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

			// 封装请求支付信息
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			model.setOutTradeNo(out_trade_no);
			model.setSubject(subject);
			model.setTotalAmount(total_amount);
			model.setBody(body);
			model.setTimeoutExpress(timeout_express);
			model.setProductCode(product_code);
			alipay_request.setBizModel(model);
			// 设置异步通知地址
			alipay_request.setNotifyUrl(AlipayConfig.notify_url);
			/*
			 * // 设置同步地址 alipay_request.setReturnUrl(AlipayConfig.return_url);
			 */

			// form表单生产
			String form = "";
			try {
				// 调用SDK生成表单
				form = client.pageExecute(alipay_request).getBody();
				response.setContentType("text/html;charset="
						+ AlipayConfig.input_charset);
				response.getWriter().write(form);// 直接将完整的表单html输出到页面
				response.getWriter().flush();
				response.getWriter().close();
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 调用支付宝APP接口
	 * 
	 * @return
	 */
	@RequestMapping("/alipayAppPay")
	@ResponseBody
	public Map<String, Object> alipayAppPay(HttpServletRequest request,
			String orderNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtil.isEmpty(orderNo)) {
			resultMap.put("respCode", configCode.code_1029);
			resultMap.put("respMsg", configCode.codeDesc(configCode.code_1029));
		} else {
			Map<String, Object> order = orderService
					.queryOrderByOrderNo(orderNo);
			if (order == null) {
				resultMap.put("respCode", configCode.code_1071);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1071));
			} else if (Integer.valueOf(order.get("state").toString()) == 0) {
				return Util.resultMap(configCode.code_1111, null);
			} else if (Integer.valueOf(order.get("paymentStatus").toString()) == 1) {
				if (Integer.valueOf(order.get("orderType").toString()) == 4
						|| Integer.valueOf(order.get("orderType").toString()) == 5
						|| Integer.valueOf(order.get("orderType").toString()) == 7
						|| Integer.valueOf(order.get("orderType").toString()) == 9) {
					Integer orderType = Integer.valueOf(order.get("orderType")
							.toString());
					Integer count = doctorOrderDao
							.checkNoFinishOrderByPatientId(
									order.get("patientId").toString(), order
											.get("doctorId").toString(),
									orderType);
					if (count > 0) {
						return Util.resultMap(configCode.code_1072, null);
					}
					Map<String, Object> dayMap = doctorOrderDao
							.getDoctorOrderDayCount(order.get("doctorId")
									.toString(), orderType);

					Map<String, Object> docMap = doctorDao.queryDocotrById(
							order.get("doctorId").toString(),
							order.get("memberId").toString());
					if (docMap == null) {
						return Util.resultMap(configCode.code_1032, null);
					}
					Integer dayOrderCount = 0;

					if (dayMap != null) {
						dayOrderCount = Integer.valueOf(dayMap.get(
								"dayOrderCount").toString());
					}
					Integer isOnlineTwGh = Integer.valueOf(docMap.get(
							"isOnlineTwGh").toString());
					Integer isOnlinePhoneGh = Integer.valueOf(docMap.get(
							"isOnlinePhoneGh").toString());
					Integer isOnlineTwZx = Integer.valueOf(docMap.get(
							"isOnlineTwZx").toString());
					Integer isOnlinePhoneZx = Integer.valueOf(docMap.get(
							"isOnlinePhoneZx").toString());
					if (orderType == 4 || orderType == 5) {
						if (isOnlineTwGh == 0) {
							return Util.resultMap(configCode.code_1057, null);
						} else {
							Integer acceptTwOrderCount = Integer.valueOf(docMap
									.get("acceptTwOrderCount").toString());
							if (dayOrderCount - acceptTwOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					} else if (orderType == 6) {
						if (isOnlineTwZx == 0) {
							return Util.resultMap(configCode.code_1054, null);
						} else {
							Integer acceptTwZxOrderCount = Integer
									.valueOf(docMap.get("acceptTwZxOrderCount")
											.toString());
							if (dayOrderCount - acceptTwZxOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					} else if (orderType == 7 || orderType == 9) {
						if (isOnlinePhoneGh == 0) {
							return Util.resultMap(configCode.code_1055, null);
						} else {
							Integer acceptPhoneOrderCount = Integer
									.valueOf(docMap
											.get("acceptPhoneOrderCount")
											.toString());
							if (dayOrderCount - acceptPhoneOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					} else if (orderType == 8) {
						if (isOnlinePhoneZx == 0) {
							return Util.resultMap(configCode.code_1056, null);
						} else {
							Integer acceptPhoneZxOrderCount = Integer
									.valueOf(docMap.get(
											"acceptPhoneZxOrderCount")
											.toString());
							if (dayOrderCount - acceptPhoneZxOrderCount >= 0) {
								return Util.resultMap(configCode.code_1116,
										null);
							}
						}
					}
				}

				Map<String, Object> yfMap = getPositage(order);
				if (Integer.valueOf(yfMap.get("respCode").toString()) != 1001) {
					return yfMap;
				}
				// order.setReceiptsPrice(Double.valueOf(yfMap.get("data").toString()));
				// order.setReceiptsPrice(0.01);
				String alipayinfo = ReturnBody.responseBody("上医仁家", orderNo,
						order.get("receiptsPrice").toString());
				resultMap.put("respCode", configCode.code_1001);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1001));
				resultMap.put("payInfo", alipayinfo);
			} else {
				resultMap.put("respCode", configCode.code_1083);
				resultMap.put("respMsg",
						configCode.codeDesc(configCode.code_1083));
			}
		}
		return resultMap;
	}

	/**
	 * 支付宝支付成功返回（异步）
	 * 
	 * @Description: TODO
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "paySuccess")
	@ResponseBody
	public void paySuccess(HttpServletRequest request) throws Exception {
		Map<String, Object> order = null;
		try {
			// 获取支付宝返回参数
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 订单交易金额
			String total_amount = request.getParameter("total_amount");
			// 商户订单号
			String out_trade_no = new String(request.getParameter(
					"out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no")
					.getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter(
					"trade_status").getBytes("ISO-8859-1"), "UTF-8");
			// 操作方seller_id
			String seller_id = request.getParameter("seller_id");
			// 商户app_id
			String app_id = request.getParameter("app_id");
			// 流水id
			String buyer_id = request.getParameter("buyer_id");
			System.out.println("+---------支付宝返回结果验证开始----------+");
			PrintWriter out = response.getWriter();
			// 第一步 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params,
					AlipayConfig.ali_public_key, "utf-8", "RSA2");
			if (signVerified) {// 验签成功
				// 第二步 验证订单号和订单金额
				order = orderService.queryOrderByOrderNo(out_trade_no);
				System.out.println("订单编号：【" + out_trade_no + "】；订单金额：【"
						+ total_amount + "】");
				if (null != order) {
					int orderTypeCode = Integer.valueOf(order.get("orderType")
							.toString());
					int payStatus = Integer.valueOf(order.get("paymentStatus")
							.toString());
					System.out.println(payStatus + "***********payStatus");
					// 第三步校验操作方 seller_id 即 partnerid
					System.out.println(seller_id
							+ "第三步校验操作方 seller_id 即 partnerid"
							+ (!StringUtils.isEmpty(seller_id) && seller_id
									.equals(AlipayConfig.partner)));
					if (!StringUtils.isEmpty(seller_id)
							&& seller_id.equals(AlipayConfig.partner)) {
						// 第四步校验商户 app_id
						System.out.println("第四步校验商户 app_id"
								+ (!StringUtils.isEmpty(app_id) && app_id
										.equals(AlipayConfig.appId)));
						if (!StringUtils.isEmpty(app_id)
								&& app_id.equals(AlipayConfig.appId)) {
							// 第五步 支付成功时执行订单逻辑操作
							System.out.println(trade_status);
							System.out.println(!StringUtils
									.isEmpty(trade_status.trim())
									&& trade_status
											.equals(AlipayConfig.tradeSuccess));
							AlipayResult alipayResult = new AlipayResult();
							if (!StringUtils.isEmpty(trade_status.trim())
									&& trade_status
											.equals(AlipayConfig.tradeSuccess)) {
								System.out
										.println("*********支付结果验证通过，执行订单逻辑处理*********");
								alipayResult.setApp_id(app_id);
								alipayResult.setBuyer_id(buyer_id);
								alipayResult.setNotify_time(params
										.get("notify_time"));
								alipayResult.setNotify_id(params
										.get("notify_id"));
								alipayResult.setNotify_type(params
										.get("notify_type"));
								alipayResult.setOut_trade_no(out_trade_no);
								alipayResult.setTrade_status(trade_status);
								alipayResult.setSeller_id(seller_id);
								alipayResult.setTrade_no(trade_no);
								alipayResult.setTotal_amount(total_amount);
								alipayResult.setBuyer_pay_amount(params
										.get("buyer_pay_amount"));
								alipayResult.setBuyer_logon_id(params
										.get("buyer_logon_id"));
								alipayResult.setSign(params.get("sign"));
								alipayResult.setSign_type(params
										.get("sign_type"));
								if (payStatus == 2 || payStatus == 5) {
									System.out.println("***订单已支付或已完成****");
									ReturnBody.cancel(out_trade_no);
								} else {
									Integer payCnt = orderService
											.queryPayResultByOrderNo(out_trade_no);
									System.out.println("***订单未支付****");
									if (payCnt <= 0) {
										int status = 2;
										orderService.addEntity(alipayResult);
										// 改变订单状态
										Integer updateOk = orderService.updateState(request, 1,
												status, out_trade_no,
												orderTypeCode, null, trade_no,1,
												1);// 更改订单及详情的状态
										if(updateOk>0){
											// 添加支付信息
											System.out.println("支付成功了。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
											orderService.paySuccessPush(request,order);
										}
										// 添加支付信息
										orderService.addPayment(out_trade_no,
												total_amount.toString(), order
														.get("memberId")
														.toString(), trade_no,
												trade_status);
									}
								}
							} else if (!StringUtils.isBlank(trade_status)
									&& trade_status
											.equals(AlipayConfig.tradeFail)) {
								String out_request_no = order.get("orderNo")
										.toString()
										+ System.currentTimeMillis();
								Map<String, Object> map = ReturnBody
										.refundPart(out_trade_no, total_amount,
												out_request_no);
								int status = 0;
								if (null != map
										&& "Success".equals(map.get("msg"))) {
									status = 3;
								}
								orderService.updateState(request, 0, status,
										out_trade_no, orderTypeCode, null,
										trade_no, 1,1);// 更改订单及详情的状态
							} else {
								System.out.println("---支付失败---");
							}
						} else {
							System.out.println("---app_id信息不匹配---");
						}
					} else {
						System.out.println("---seller_id信息不匹配---");
					}
				} else {
					System.out.println("---订单编号和金额不匹配订单信息---");
				}
				out.print("success");
			} else {
				System.out.println("---验签失败---");
				out.print("failure");
			}
			System.out.println("+----------支付宝返回结果验证结束---------+");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("支付宝信息验证失败，错误信息：" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 支付宝支付成功返回（异步）
	 * 
	 * @Description: TODO
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "appPaySuccess")
	@ResponseBody
	public void appPaySuccess(HttpServletRequest request) throws Exception {
		Map<String, Object> order = null;
		try {
			// 获取支付宝返回参数
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 订单交易金额
			String total_amount = request.getParameter("total_amount");
			// 商户订单号
			String out_trade_no = new String(request.getParameter(
					"out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no")
					.getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter(
					"trade_status").getBytes("ISO-8859-1"), "UTF-8");
			// 操作方seller_id
			String seller_id = request.getParameter("seller_id");
			// 商户app_id
			String app_id = request.getParameter("app_id");
			// 流水id
			String buyer_id = request.getParameter("buyer_id");
			System.out.println("+---------支付宝返回结果验证开始----------+");
			PrintWriter out = response.getWriter();
			// 第一步 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params,
					AlipayConfig.ali_public_key, "utf-8", "RSA2");
			if (signVerified) {// 验签成功
				// 第二步 验证订单号和订单金额
				order = orderService.queryOrderByOrderNo(out_trade_no);
				System.out.println("订单编号：【" + out_trade_no + "】；订单金额：【"
						+ total_amount + "】");
				if (null != order) {
					int orderTypeCode = Integer.valueOf(order.get("orderType")
							.toString());
					int payStatus = Integer.valueOf(order.get("paymentStatus")
							.toString());
					System.out.println(payStatus + "***********payStatus");
					// 第三步校验操作方 seller_id 即 partnerid
					System.out.println(seller_id
							+ "第三步校验操作方 seller_id 即 partnerid"
							+ (!StringUtils.isEmpty(seller_id) && seller_id
									.equals(AlipayConfig.partner)));
					if (!StringUtils.isEmpty(seller_id)
							&& seller_id.equals(AlipayConfig.partner)) {
						// 第四步校验商户 app_id
						System.out.println("第四步校验商户 app_id"
								+ (!StringUtils.isEmpty(app_id) && app_id
										.equals(AlipayConfig.appId)));
						if (!StringUtils.isEmpty(app_id)
								&& app_id.equals(AlipayConfig.appId)) {
							// 第五步 支付成功时执行订单逻辑操作
							System.out.println(trade_status);
							System.out.println(!StringUtils
									.isEmpty(trade_status.trim())
									&& trade_status
											.equals(AlipayConfig.tradeSuccess));
							AlipayResult alipayResult = new AlipayResult();
							if (!StringUtils.isEmpty(trade_status.trim())
									&& trade_status
											.equals(AlipayConfig.tradeSuccess)) {
								System.out
										.println("*********支付结果验证通过，执行订单逻辑处理*********");
								alipayResult.setApp_id(app_id);
								alipayResult.setBuyer_id(buyer_id);
								alipayResult.setNotify_time(params
										.get("notify_time"));
								alipayResult.setNotify_id(params
										.get("notify_id"));
								alipayResult.setNotify_type(params
										.get("notify_type"));
								alipayResult.setOut_trade_no(out_trade_no);
								alipayResult.setTrade_status(trade_status);
								alipayResult.setSeller_id(seller_id);
								alipayResult.setTrade_no(trade_no);
								alipayResult.setTotal_amount(total_amount);
								alipayResult.setBuyer_pay_amount(params
										.get("buyer_pay_amount"));
								alipayResult.setBuyer_logon_id(params
										.get("buyer_logon_id"));
								alipayResult.setSign(params.get("sign"));
								alipayResult.setSign_type(params
										.get("sign_type"));
								if (payStatus == 2 || payStatus == 5) {
									System.out.println("***订单已支付或已完成****");
									ReturnBody.cancel(out_trade_no);
								} else {
									Integer payCnt = orderService
											.queryPayResultByOrderNo(out_trade_no);
									System.out.println("***订单未支付****");
									if (payCnt <= 0) {
										int status = 2;
										orderService.addEntity(alipayResult);
										// 改变订单状态
										Integer updateOk = orderService.updateState(request, 1,
												status, out_trade_no,
												orderTypeCode, null, trade_no,2,
												1);// 更改订单及详情的状态
										if(updateOk>0){
											// 添加支付信息
											System.out.println("支付成功了。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
											orderService.paySuccessPush(request,order);
										}
										// 添加支付信息
										orderService.addPayment(out_trade_no,
												total_amount.toString(), order
														.get("memberId")
														.toString(), trade_no,
												trade_status);
									}
								}
							} else if (!StringUtils.isBlank(trade_status)
									&& trade_status
											.equals(AlipayConfig.tradeFail)) {
								String out_request_no = order.get("orderNo")
										.toString()
										+ System.currentTimeMillis();
								Map<String, Object> map = ReturnBody
										.refundPart(out_trade_no, total_amount,
												out_request_no);
								int status = 0;
								if (null != map
										&& "Success".equals(map.get("msg"))) {
									status = 3;
								}
								orderService.updateState(request, 0, status,
										out_trade_no, orderTypeCode, null,
										trade_no, 2,1);// 更改订单及详情的状态
							} else {
								System.out.println("---支付失败---");
							}
						} else {
							System.out.println("---app_id信息不匹配---");
						}
					} else {
						System.out.println("---seller_id信息不匹配---");
					}
				} else {
					System.out.println("---订单编号和金额不匹配订单信息---");
				}
				out.print("success");
			} else {
				System.out.println("---验签失败---");
				out.print("failure");
			}
			System.out.println("+----------支付宝返回结果验证结束---------+");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("支付宝信息验证失败，错误信息：" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 支付宝支付成功返回（同步）
	 * 
	 * @Description: TODO
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "returnPaySuccess")
	@ResponseBody
	public void returnPaySuccess(HttpServletRequest request) throws Exception {
		try {
			// 获取支付宝返回参数
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 订单交易金额
			String total_amount = request.getParameter("total_amount");
			// 商户订单号
			String out_trade_no = new String(request.getParameter(
					"out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no")
					.getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter(
					"trade_status").getBytes("ISO-8859-1"), "UTF-8");
			// 操作方seller_id
			String seller_id = request.getParameter("seller_id");
			// 商户app_id
			String app_id = request.getParameter("app_id");
			// 买家支付宝用户号
			String buyer_id = request.getParameter("buyer_id");
			System.out.println("+---------支付宝返回结果验证开始----------+");
			PrintWriter out = response.getWriter();
			// 第一步 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params,
					AlipayConfig.ali_public_key, "utf-8", "RSA2");
			if (signVerified) {// 验签成功
				// 第二步 验证订单号和订单金额
				Order order = orderService.queryById(Order.class, out_trade_no);
				System.out.println("订单编号：【" + out_trade_no + "】；订单金额：【"
						+ total_amount + "】");
				if (null != order) {
					int orderTypeCode = order.getOrderType();
					int payStatus = order.getPaymentStatus();
					System.out.println(payStatus + "***********payStatus");
					// 第三步校验操作方 seller_id 即 partnerid
					System.out.println(seller_id
							+ "第三步校验操作方 seller_id 即 partnerid"
							+ (!StringUtils.isEmpty(seller_id) && seller_id
									.equals(AlipayConfig.partner)));
					if (!StringUtils.isEmpty(seller_id)
							&& seller_id.equals(AlipayConfig.partner)) {
						// 第四步校验商户 app_id
						System.out.println("第四步校验商户 app_id"
								+ (!StringUtils.isEmpty(app_id) && app_id
										.equals(AlipayConfig.appId)));
						if (!StringUtils.isEmpty(app_id)
								&& app_id.equals(AlipayConfig.appId)) {
							// 第五步 支付成功时执行订单逻辑操作
							System.out.println(trade_status);
							System.out.println(!StringUtils
									.isEmpty(trade_status.trim())
									&& trade_status
											.equals(AlipayConfig.tradeSuccess));
							AlipayResult alipayResult = new AlipayResult();
							if (!StringUtils.isEmpty(trade_status.trim())
									&& trade_status
											.equals(AlipayConfig.tradeSuccess)) {
								alipayResult.setApp_id(app_id);
								alipayResult.setBuyer_id(buyer_id);
								alipayResult.setNotify_time(params
										.get("notify_time"));
								alipayResult.setNotify_id(params
										.get("notify_id"));
								alipayResult.setNotify_type(params
										.get("notify_type"));
								alipayResult.setOut_trade_no(out_trade_no);
								alipayResult.setTrade_status(trade_status);
								alipayResult.setSeller_id(seller_id);
								alipayResult.setTrade_no(trade_no);
								alipayResult.setTotal_amount(total_amount);
								alipayResult.setBuyer_pay_amount(params
										.get("buyer_pay_amount"));
								alipayResult.setBuyer_logon_id(params
										.get("buyer_logon_id"));
								alipayResult.setSign(params.get("sign"));
								alipayResult.setSign_type(params
										.get("sign_type"));
								System.out
										.println("*********支付结果验证通过，执行订单逻辑处理*********");
								if (payStatus == 2 || payStatus == 5) {
									System.out.println("***订单已支付或已完成****");
									ReturnBody.cancel(out_trade_no);
								} else {
									Integer payCnt = orderService
											.queryPayResultByOrderNo(out_trade_no);
									System.out.println("***订单未支付****");
									if (payCnt == 0) {
										int status = 2;
										orderService.addEntity(alipayResult);
										// 改变订单状态
										orderService.updateState(request, 1,
												status, out_trade_no,
												orderTypeCode, null, trade_no,
												1);// 更改订单及详情的状态
										// 添加支付信息
										orderService.addPayment(out_trade_no,
												total_amount.toString(),
												order.getMemberId(), trade_no,
												trade_status);

										Map<String, Object> follow = orderService
												.queryFollowIdByOpenId(order
														.getDoctorId());

										if (order.getOrderType() == 10
												|| order.getOrderType() == 13
												|| order.getOrderType() == 14
												|| order.getOrderType() == 15
												|| order.getOrderType() == 16
												|| order.getOrderType() == 17
												|| order.getOrderType() == 18
												|| order.getOrderType() == 19
												|| order.getOrderType() == 20) {
											JSONObject json = new JSONObject();
											json.put("msgType", 13);
											json.put("orderNo",
													order.getOrderNo());
											json.put("serverOrderNo",
													order.getOrderNo());
											json.put("content",
													order.getReceiptsPrice());
											GetSig.sendMsg(request,
													order.getPatientId(),
													order.getDoctorId(), 1,
													json);
											if (null != follow
													&& follow.size() > 0) {
												pushService.docprescorder(
														follow.get("followId")
																.toString(),
														order.getDoctorId(),
														order.getOrderNo(),
														follow.get("docName")
																.toString());
											}
											pushService.docpaytlsuc(
													order.getDoctorId(), null,
													order.getOrderNo());

											sendModelMsgUtil
													.sendOrder(
															order.getPatientId(),
															order.getOrderNo(),
															order.getReceiptsPrice()
																	+ "",
															"调理方",
															goodsDao,
															"im/inquiry.html?identifier="
																	+ order.getPatientId()
																	+ "&selToID="
																	+ order.getDoctorId());
										}

										if (order.getOrderType() == 1) {
											sendModelMsgUtil
													.sendOrder(
															order.getPatientId(),
															order.getOrderNo(),
															order.getReceiptsPrice()
																	+ "",
															"商城商品",
															goodsDao,
															"order/order_detail.html?orderNo="
																	+ order.getOrderNo());
										}

										PatientData patientData = imService
												.queryById(PatientData.class,
														order.getPatientId());

										if (order.getOrderType() == 4) {
											if (!StringUtil.isEmpty(order
													.getRsrvStr1())
													&& order.getRsrvStr1()
															.equals("1")) {
												JSONObject json = new JSONObject();
												json.put("msgType", 16);
												json.put("orderNo",
														order.getOrderNo());
												json.put("serverOrderNo",
														order.getOrderNo());
												json.put("content", order
														.getReceiptsPrice());
												GetSig.sendMsg(request,
														order.getPatientId(),
														order.getDoctorId(), 0,
														json);
											} else {
												String testId = appDoctorService
														.queryDefultSpecial(
																order.getDoctorId(),
																1);

												GetSig.sendMsg(request,
														order.getPatientId(),
														order.getDoctorId(),
														order.getOrderNo(),
														patientData.getName(),
														order.getOrderType());

												if (!StringUtil.isEmpty(testId)) {
													Map<String, Object> spect = imService
															.queryLastOrderNo(
																	order.getPatientId(),
																	order.getDoctorId(),
																	testId);
													if (spect.get("respCode")
															.toString()
															.equals("1001")) {
														JSONObject json = new JSONObject();
														json.put("msgType", 8);
														json.put(
																"orderNo",
																order.getOrderNo());
														json.put(
																"dataId",
																JsonUtil.jsonToMap(
																		spect.get("data"))
																		.get("testId"));
														json.put(
																"serverOrderNo",
																order.getOrderNo());
														GetSig.sendMsg(
																request,
																order.getDoctorId(),
																order.getPatientId(),
																1, json);
													}
												}
											}
										} else {

											GetSig.sendMsg(request,
													order.getPatientId(),
													order.getDoctorId(),
													order.getOrderNo(),
													patientData.getName(),
													order.getOrderType());
										}

										if (order.getOrderType() == 4) {
											Map<String, Object> doctor = appDoctorDao
													.queryOneDoctor(order
															.getDoctorId());
											if (null != doctor
													&& doctor.size() > 0) {
												sendModelMsgUtil
														.sendBuySuccess(
																order.getPatientId(),
																"图文调理",
																doctor.get(
																		"docName")
																		.toString(),
																doctor.get(
																		"infirmaryName")
																		.toString(),
																patientData
																		.getName(),
																patientData
																		.getSex() == 0 ? "男"
																		: "女",
																goodsDao,
																"im/inquiry.html?identifier="
																		+ order.getPatientId()
																		+ "&selToID="
																		+ order.getDoctorId());
											}
										} else if (order.getOrderType() == 7) {
											Map<String, Object> doctor = appDoctorDao
													.queryOneDoctor(order
															.getDoctorId());
											if (null != doctor
													&& doctor.size() > 0) {
												sendModelMsgUtil
														.sendBuySuccess(
																order.getPatientId(),
																"电话调理",
																doctor.get(
																		"docName")
																		.toString(),
																doctor.get(
																		"infirmaryName")
																		.toString(),
																patientData
																		.getName(),
																patientData
																		.getSex() == 0 ? "男"
																		: "女",
																goodsDao,
																"im/inquiry.html?identifier="
																		+ order.getPatientId()
																		+ "&selToID="
																		+ order.getDoctorId());
											}
										} else if (order.getOrderType() == 6) {
											Map<String, Object> doctor = appDoctorDao
													.queryOneDoctor(order
															.getDoctorId());
											if (null != doctor
													&& doctor.size() > 0) {
												sendModelMsgUtil
														.sendBuySuccess(
																order.getPatientId(),
																"图文咨询",
																doctor.get(
																		"docName")
																		.toString(),
																doctor.get(
																		"infirmaryName")
																		.toString(),
																patientData
																		.getName(),
																patientData
																		.getSex() == 0 ? "男"
																		: "女",
																goodsDao,
																"im/inquiry.html?identifier="
																		+ order.getPatientId()
																		+ "&selToID="
																		+ order.getDoctorId());
											}
										} else if (order.getOrderType() == 8) {
											Map<String, Object> doctor = appDoctorDao
													.queryOneDoctor(order
															.getDoctorId());
											if (null != doctor
													&& doctor.size() > 0) {
												sendModelMsgUtil
														.sendBuySuccess(
																order.getPatientId(),
																"电话咨询",
																doctor.get(
																		"docName")
																		.toString(),
																doctor.get(
																		"infirmaryName")
																		.toString(),
																patientData
																		.getName(),
																patientData
																		.getSex() == 0 ? "男"
																		: "女",
																goodsDao,
																"im/inquiry.html?identifier="
																		+ order.getPatientId()
																		+ "&selToID="
																		+ order.getDoctorId());
											}
										}

										if (null != follow && follow.size() > 0) {
											if (order.getOrderType() == 4) {
												pushService.paysuczxtl(follow
														.get("followId")
														.toString(), order
														.getDoctorId(), follow
														.get("docName")
														.toString());
											} else if (order.getOrderType() == 7) {
												pushService.paysucdhtl(follow
														.get("followId")
														.toString(), order
														.getDoctorId(), follow
														.get("docName")
														.toString());
											} else if (order.getOrderType() == 6) {
												pushService.paysuctwzx(follow
														.get("followId")
														.toString(), order
														.getDoctorId(), follow
														.get("docName")
														.toString());
											} else if (order.getOrderType() == 8) {
												pushService.paysucdhzx(follow
														.get("followId")
														.toString(), order
														.getDoctorId(), follow
														.get("docName")
														.toString());
											}
										}

										if (order.getOrderType() == 4) {
											pushService.docpaysuczxtl(
													order.getDoctorId(),
													order.getPatientId(), null);
										} else if (order.getOrderType() == 6) {
											pushService.docpaysuctwzx(
													order.getDoctorId(),
													order.getPatientId(), null);
										} else if (order.getOrderType() == 7) {
											pushService.docpaysucdhtl(
													order.getDoctorId(),
													order.getPatientId(), null);
										} else if (order.getOrderType() == 8) {
											pushService.docpaysucdhzx(
													order.getDoctorId(),
													order.getPatientId(), null);
										}

										if (order.getOrderType() == 12) {// 锦旗
											List<Map<String, Object>> orderDetails = orderService
													.queryOrderDetailByOrderNo(order
															.getOrderNo());
											if (null != orderDetails
													&& orderDetails.size() > 0) {
												for (Map<String, Object> orderDetail : orderDetails) {
													Map<String,Object> myEva = orderService.queryMyEvaBannerById(orderDetail
																			.get("goodsId")
																			.toString());
													Integer count = Integer
															.parseInt(orderDetail
																	.get("goodsNum")
																	.toString());
													for (int i = 0; i < count; i++) {
														MyEvaBanner myEvaBanner = new MyEvaBanner();
														myEvaBanner
																.setCreateTime(Util
																		.queryNowTime());
														myEvaBanner
																.setMemberId(order
																		.getMemberId());
														myEvaBanner
																.setOrderNo(order
																		.getOrderNo());
														myEvaBanner
																.setPrice(Double
																		.parseDouble(orderDetail
																				.get("goodsPrice")
																				.toString()));
														myEvaBanner
																.setType(Integer.valueOf(myEva.get("type").toString()));
														orderService
																.addEntityUUID(myEvaBanner);
													}
												}
											}
										}

										Producer.producer(order.getOrderNo());// 结算分成

									}
								}
							} else if (!StringUtils.isBlank(trade_status)
									&& trade_status
											.equals(AlipayConfig.tradeFail)) {
								String out_request_no = order.getOrderNo()
										+ System.currentTimeMillis();
								Map<String, Object> map = ReturnBody
										.refundPart(out_trade_no, total_amount,
												out_request_no);
								int status = 0;
								if (null != map
										&& "Success".equals(map.get("msg"))) {
									status = 3;
								}
								orderService.updateState(request, 0, status,
										out_trade_no, orderTypeCode, null,
										trade_no,1);// 更改订单及详情的状态
							} else {
								System.out.println("---支付失败---");
							}
						} else {
							System.out.println("---app_id信息不匹配---");
						}
					} else {
						System.out.println("---seller_id信息不匹配---");
					}
				} else {
					System.out.println("---订单编号和金额不匹配订单信息---");
				}
				out.print("success");
			} else {
				System.out.println("---验签失败---");
				out.print("failure");
			}
			System.out.println("+----------支付宝返回结果验证结束---------+");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("支付宝信息验证失败");
			e.printStackTrace();
		}
	}

	private Map<String, Object> getPositage(Map<String, Object> order) {
		Double payPrice = Double.valueOf(order.get("receiptsPrice").toString());
		if (Integer.valueOf(order.get("orderType").toString()) == 1) {
			GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
			goodsOrderDetail.setOrderNo(order.get("orderNo").toString());
			List<GoodsOrderDetail> goodsOrderDetails = orderService
					.query(goodsOrderDetail);
			List<String> goodsId = new ArrayList<String>();

			Double originalTotal = 0.0;

			Map<String, Object> platformActivity = new HashMap<String, Object>();

			for (GoodsOrderDetail detail : goodsOrderDetails) {
				Map<String, Object> goods = goodsShopCartDao
						.queryGoodsByGoodsIdAndPriceNumId(detail.getGoodsId(),
								detail.getGoodsPriceNumId(), null);
				if (null == goods || StringUtil.isEmpty(goods.get("id"))) {
					return Util.resultMap(configCode.code_1067, null);
				}

				if (Double.parseDouble(goods.get("price").toString()) != detail
						.getGoodsOriginalPrice()) {
					return Util.resultMap(configCode.code_1082, null);
				}

				Double goodsOriginalTotal = detail.getGoodsOriginalPrice()
						* detail.getGoodsNum();

				Double goodsPrice = detail.getGoodsOriginalPrice()
						* detail.getGoodsNum();

				List<Map<String, Object>> activitys = goodsDao
						.queryActivity(goods.get("id").toString());

				for (Map<String, Object> activity : activitys) {
					List<Map<String, Object>> activityDetails = goodsDao
							.queryActivityDetail(activity.get("id").toString(),
									Integer.parseInt(activity.get("type")
											.toString()));
					if (activity.get("type").toString().equals("1")) {

						Integer buyCounted = goodsOrderDao.queryBuyConnt(order
								.get("memberId").toString(), activity.get("id")
								.toString(),
								goods.get("priceNumId").toString(),
								order.get("orderNo").toString());
						if (buyCounted >= Integer.parseInt(activity.get(
								"activityNum").toString())
								|| detail.getGoodsNum() > Integer
										.parseInt(activity.get("activityNum")
												.toString())) {
							return Util.resultMap(configCode.code_1079, null);
						}
						for (Map<String, Object> activityDetail : activityDetails) {
							if (Util.multiply(
									detail.getGoodsOriginalPrice() + "",
									detail.getGoodsNum() + "").doubleValue() >= Double
									.parseDouble(activityDetail.get(
											"activityPrice").toString())) {
								if (activity.get("activityType").toString()
										.equals("1")) {
									goodsPrice = Util.multiply(
											goodsOriginalTotal + "",
											activityDetail.get("activityFold")
													.toString()).doubleValue();
								} else if (activity.get("activityType")
										.toString().equals("2")) {
									goodsPrice = Util.subtract(
											goodsOriginalTotal + "",
											activityDetail.get("activityFold")
													.toString()).doubleValue();
								}
								break;
							}
						}

					} else {
						if (null != platformActivity.get(activity.get("id")
								.toString())) {
							@SuppressWarnings("unchecked")
							Map<String, Object> activityDetail = (Map<String, Object>) platformActivity
									.get("id");
							activityDetail.put("price", Util.add(
									platformActivity.get("price").toString(),
									goodsPrice + ""));
							activityDetail.put("goodsOriginalTotal", Util.add(
									platformActivity.get("goodsOriginalTotal")
											.toString(), goodsPrice + ""));
						} else {
							Map<String, Object> activityDetail = new HashMap<String, Object>();
							activityDetail.put("price", goodsPrice);
							activityDetail.put("activityType",
									activity.get("activityType").toString());
							activityDetail.put("goodsOriginalTotal",
									goodsOriginalTotal);
							activityDetail.put("activityDetail",
									activityDetails);
							platformActivity.put(activity.get("id").toString(),
									activityDetail);

						}
					}

				}
				goodsId.add(detail.getGoodsId());
				originalTotal = Util.add(originalTotal + "", goodsPrice + "");

			}

			for (Map.Entry<String, Object> entry : platformActivity.entrySet()) {
				// String key = entry.getKey().toString();
				@SuppressWarnings("unchecked")
				Map<String, Object> activityDetail = (Map<String, Object>) entry
						.getValue();
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> activityDetails = (List<Map<String, Object>>) activityDetail
						.get("activityDetail");
				for (Map<String, Object> activity : activityDetails) {
					if (Double.parseDouble(activityDetail.get(
							"goodsOriginalTotal").toString()) >= Double
							.parseDouble(activity.get("activityPrice")
									.toString())) {
						if (activityDetail.get("activityType").toString()
								.equals("1")) {
							System.out.println(Double.parseDouble(activity.get(
									"activityFold").toString()) / 10);
							originalTotal = originalTotal
									- Util.subtract(
											activityDetail.get("price")
													.toString(),
											Util.multiply(
													activityDetail.get("price")
															.toString(),
													Double.parseDouble(activity
															.get("activityFold")
															.toString())
															/ 10 + "")
													.toString()).doubleValue();
						} else if (activityDetail.get("activityType")
								.toString().equals("2")) {
							originalTotal = originalTotal
									- Util.subtract(
											activityDetail.get("price")
													.toString(),
											Util.subtract(
													activityDetail.get("price")
															.toString(),
													activity.get("activityFold")
															.toString())
													.toString()).doubleValue();
						}
					}
				}
			}

			if (!Util.isEquals(originalTotal,
					Double.valueOf(order.get("receiptsPrice").toString())
							- Double.valueOf(order.get("postage").toString()))) {
				return Util.resultMap(configCode.code_1082, null);
			}
			/*
			 * String cityId=goodsDao.queryCityIdByCity(order.getCity()); String
			 * postage
			 * =goodsDao.queryPostageByGoodsIds(cityId,order.getOrderNo());
			 * payPrice = Util.add(payPrice+"",postage).doubleValue();
			 */
		}
		return Util.resultMap(configCode.code_1001, payPrice);
	}
}
