package cn.syrjia.wxPay.wxPay.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.jdom.JDOMException;

import cn.syrjia.weixin.util.CommonUtil;
import cn.syrjia.weixin.util.PayCommonUtil;
import cn.syrjia.weixin.util.RequestHandler;
import cn.syrjia.weixin.util.TenpayUtil;
import cn.syrjia.weixin.util.XMLUtil;

public class WeiXinUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> cancleWxOrder(String orderNo) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String msg = "fail";
		try {
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
			Map<String, String> map = XMLUtil.doXMLParse(result);
			String returnCode = map.get("return_code");
			String resultCode = map.get("result_code");
			String errcode = map.get("err_code");
			if (null != errcode && errcode.equalsIgnoreCase("ORDERNOTEXIST")) {
				returnMap.put("msg", "Success");
				return returnMap;
			}
			if (returnCode.equalsIgnoreCase("SUCCESS")
					&& resultCode.equalsIgnoreCase("SUCCESS")) {
				// TODO 关闭订单成功的操作
				String closeResult = CommonUtil.httpsRequest(
						WeiXinConfig.CLOSE_ORDER_URL, "POST", reuqestXml);
				Map<String, String> closeMap = XMLUtil.doXMLParse(closeResult);
				if (closeMap.get("return_code").equalsIgnoreCase("SUCCESS")
						&& closeMap.get("return_code").equalsIgnoreCase(
								"SUCCESS")) {
					msg = "Success";
					System.out.println("订单关闭成功");
				}
				System.out.println("订单查询成功");
			} else {
				System.out.println(map.get("return_msg"));
			}
		} catch (JDOMException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		returnMap.put("msg", msg);
		return returnMap;
	}

	public static void main(String[] args) {
		cancleWxOrder("K20170630090608");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> refund(HttpServletRequest request,
			String orderNo, Double refundPrice, Double orderPrice,String addType) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String out_refund_no = orderNo;// 退款单号
			String out_trade_no = orderNo;// 订单号
			String price = Integer.parseInt(TenpayUtil.getMoney(orderPrice
					.toString())) + "";
			String total_fee = price;// 总金额
			String refund_fee = Integer.parseInt(TenpayUtil
					.getMoney(refundPrice.toString())) + "";
			;// 退款金额
			String nonce_str = WeiXinConfig.number32();// 随机字符串
			String appid = WeiXinConfig.appId; // 微信公众号apid
			String appsecret = WeiXinConfig.appSecret; // 微信公众号appsecret
			String mch_id = WeiXinConfig.mch_id; // 微信商户id
			@SuppressWarnings("unused")
			String sub_mch_id = WeiXinConfig.sub_mch_id;// 微信支付分配的子商户号
			String op_user_id = WeiXinConfig.mch_id;// 就是MCHID
			String partnerkey = WeiXinConfig.key;// 商户平台上的那个KEY
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			RequestHandler reqHandler = new RequestHandler(null, null);
			if("4".equals(addType)){
				packageParams.put("appid", WeiXinConfig.sub_appid);
				packageParams.put("mch_id", WeiXinConfig.sub_mch_id);
				reqHandler.init(WeiXinConfig.sub_appid, WeiXinConfig.sub_appSecret, WeiXinConfig.app_key);
			}else{
				packageParams.put("appid", appid);
				packageParams.put("mch_id", mch_id);
				reqHandler.init(appid, appsecret, partnerkey);
			}
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("out_trade_no", out_trade_no);
			packageParams.put("out_refund_no", out_refund_no+System.currentTimeMillis());
			packageParams.put("refund_fee", refund_fee);
			packageParams.put("total_fee", total_fee);
			packageParams.put("op_user_id", op_user_id);

			String sign = reqHandler.createSign(packageParams);
			packageParams.put("sign", sign);
			String createOrderURL = WeiXinConfig.REFUND_APPLAY_URL;
			String result = ClientCustomSSL.doRefund(request, createOrderURL,
					PayCommonUtil.getRequestXml(packageParams),addType);
			map = XMLUtil.doXMLParse(result);
			System.out.println(map+"退款返回结果");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
				// sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

}
