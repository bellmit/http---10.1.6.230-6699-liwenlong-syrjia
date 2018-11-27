package cn.syrjia.util.alipay.config;

import java.util.Map;

import cn.syrjia.util.JsonUtil;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

public class ReturnBody {
	/**
	 * 支付宝支付
	 * 
	 * @param feeName
	 * @param orderNo
	 * @param total
	 * @return
	 */
	public static String responseBody(String feeName, String orderNo,
			String total) {
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", AlipayConfig.appId,
				AlipayConfig.private_key, "json", "utf-8",
				AlipayConfig.public_key, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setTimeoutExpress("10m");
		model.setSubject(feeName);
		model.setOutTradeNo(orderNo); // 订单id
		model.setTotalAmount(total); // 钱数
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(AlipayConfig.app_notify_url);
		try {
			AlipayTradeAppPayResponse response = alipayClient
					.sdkExecute(request);
			//调用成功，则处理业务逻辑
			if(response.isSuccess()){
				return response.getBody();// 就是orderString 可以直接给客户端请求，无需再做处理。
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝退款
	 * 
	 * @param orderNo
	 * @param total
	 * @return
	 */
	public static Map<String, Object> refund(String orderNo, String total) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", AlipayConfig.appId,
				AlipayConfig.private_key, "json", "utf-8",
				AlipayConfig.ali_public_key, "RSA2"); // 获得初始化的AlipayClient
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();// 创建API对应的request类
		request.setBizContent("{" + "\"out_trade_no\":\"" + orderNo + "\","
				+ "\"refund_amount\":\"" + total + "\"" + "}");// 设置业务参数
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			// 将返回的信息转换成map
			return JsonUtil.jsonToMap(JsonUtil.jsonToMap(response.getBody())
					.get("alipay_trade_refund_response"));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 支付宝部分退款
	 * 
	 * @param orderNo
	 * @param total
	 * @return
	 */
	public static Map<String, Object> refundPart(String orderNo, String total,String out_request_no) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", AlipayConfig.appId,
				AlipayConfig.private_key, "json", "utf-8",
				AlipayConfig.ali_public_key, "RSA2"); // 获得初始化的AlipayClient
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();// 创建API对应的request类
		request.setBizContent("{" + "\"out_trade_no\":\"" + orderNo + "\","
				+ "\"refund_amount\":\"" + total + "\"," 
				+ "\"out_request_no\":\"" + out_request_no + "\"" + "}");// 设置业务参数
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			// 将返回的信息转换成map
			return JsonUtil.jsonToMap(JsonUtil.jsonToMap(response.getBody())
					.get("alipay_trade_refund_response"));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝取消订单
	 * 
	 * @Description: TODO
	 * @param @param orderNo
	 * @param @param total
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-8
	 */
	public static Map<String, Object> cancel(String orderNo) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", AlipayConfig.appId,
				AlipayConfig.private_key, "json", "utf-8",
				AlipayConfig.ali_public_key, "RSA2"); // 获得初始化的AlipayClient
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();// 创建API对应的request类
		request.setBizContent("{" + "\"out_trade_no\":\"" + orderNo + "\"}");// 设置业务参数
		try {
			AlipayTradeCancelResponse responseCancle = alipayClient
					.execute(request);
			System.out.println(JsonUtil.jsonToMap(responseCancle.getBody())
					.get("alipay_trade_cancel_response"));
			// 将返回的信息转换成map
			return JsonUtil.jsonToMap(JsonUtil.jsonToMap(
					responseCancle.getBody()).get(
					"alipay_trade_cancel_response"));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> close(String orderNo) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", AlipayConfig.appId,
				AlipayConfig.private_key, "json", "utf-8",
				AlipayConfig.ali_public_key, "RSA2"); // 获得初始化的AlipayClient
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizContent("{" + "\"out_trade_no\":\"" + orderNo + "\"}");// 设置业务参数
		try {
			AlipayTradeCloseResponse response = alipayClient.execute(request);
			System.out.println(JsonUtil.jsonToMap(response.getBody()).get(
					"alipay_trade_close_response"));
			// 将返回的信息转换成map
			return JsonUtil.jsonToMap(JsonUtil.jsonToMap(response.getBody())
					.get("alipay_trade_close_response"));
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 订单创建【app支付时不用该接口，请忽略】
	 * @Description: TODO
	 * @param @param feeName
	 * @param @param orderNo
	 * @param @param total
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-9
	 */
	public static String createPay(String feeName, String orderNo,
			String total){
		try {
			System.out.println("+++++++++++++创建订单开始++++++++++++++");
			// 实例化客户端
			AlipayClient alipayClient = new DefaultAlipayClient(
					"https://openapi.alipay.com/gateway.do", AlipayConfig.appId,
					AlipayConfig.private_key, "json", "utf-8",
					AlipayConfig.public_key, "RSA2");
			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.create
			AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
			// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
			AlipayTradeCreateModel model = new AlipayTradeCreateModel();
			model.setSubject(feeName);
			model.setOutTradeNo(orderNo); // 订单id
			model.setTotalAmount(total); // 钱数
			request.setBizModel(model);
			AlipayTradeCreateResponse response = alipayClient.execute(request);
			System.out.println(JsonUtil.jsonToMap(JsonUtil.jsonToMap(response.getBody())
					.get("alipay_trade_create_response")));
			String code = (String) JsonUtil.jsonToMap(JsonUtil.jsonToMap(response.getBody())
					.get("alipay_trade_create_response")).get("code");
			if(null!=code&&"10000".equals(code)){
				System.out.println("订单创建成功");
				return aliPay(alipayClient, feeName, orderNo, total);//调用支付宝支付接口
			}else{
				System.out.println("订单创建失败");
				return "";
			}
			/*if(response.isSuccess()){
				System.out.println("订单创建成功");
				return aliPay(alipayClient, feeName, orderNo, total);//调用支付宝支付接口
			} else {
				System.out.println("订单创建失败");
				return "";
			}*/
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	/**
	 * 订单支付
	 * @Description: TODO
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-9
	 */
	private static String aliPay(AlipayClient alipayClient,String feeName, String orderNo,
			String total){
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setSubject(feeName);
		model.setOutTradeNo(orderNo); // 订单id
		model.setTotalAmount(total); // 钱数
		request.setBizModel(model);
		request.setNotifyUrl(AlipayConfig.notify_url);
		try {
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			System.out.println("支付宝支付接口调用成功");
			return response.getBody();// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("支付宝支付接口调用失败");
			return "";
		}
	}

	/**
	 *  支付宝部分退款
	 * @param orderNo  
	 * @param requestNo
	 * @param refundReason
	 * @param total
	 * @return
	 */
	public static Map<String, Object> sectionRefund(String orderNo,String requestNo,String refundReason,String total){
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConfig.appId, AlipayConfig.private_key, "json",  "utf-8", AlipayConfig.ali_public_key, "RSA2"); //获得初始化的AlipayClient
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
		request.setBizContent("{" +
		"\"out_trade_no\":\""+orderNo+"\"," +
		"\"refund_reason\":\""+requestNo+"\"," +
		"\"out_request_no\":\""+refundReason+"\"," +
		"\"refund_amount\":\""+total+"\"" +
		"}");//设置业务参数refund_reason out_request_no 

		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			//将返回的信息转换成map
			return JsonUtil.jsonToMap(JsonUtil.jsonToMap(response.getBody()).get("alipay_trade_refund_response"));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
}
