package cn.syrjia.wxPay.wxPay.dao.impl;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.entity.WXPayBean;
import cn.syrjia.util.GetWxOrderno;
import cn.syrjia.util.RequestHandler;
import cn.syrjia.util.Sha1Util;
import cn.syrjia.util.TenpayUtil;
import cn.syrjia.wxPay.wxPay.dao.WxPayDao;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author liwenlong
 * 
 */
@Repository("wxPayDao")
public class WxPayDaoImpl extends BaseDaoImpl implements WxPayDao {

	/**
	 * @descriptions:jsApi接口
	 * @copyright：
	 */
	private Logger logger = LogManager.getLogger(WxPayDaoImpl.class);

	@Override
	public Map<String,Object> getPackage(String openid, WXPayBean jsApiBean) {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		try {
			// 商户订单号
			String out_trade_no = jsApiBean.getOutTradeNo();
			String nonce_str = getNonceStr();
			//String attach = "attach";
			SortedMap<String, String> packageParams = new TreeMap<String, String>();

			String goods_tag = jsApiBean.getGoodsTag();
			if (goods_tag == null || "".equals(goods_tag)) {
				goods_tag = "无";
			}
			packageParams.put("appid", jsApiBean.getAppId());
			packageParams.put("attach", jsApiBean.getAttach());
			packageParams.put("body", jsApiBean.getBody());
			packageParams.put("goods_tag", goods_tag);// jsApiBean.getGoodsTag());
			packageParams.put("mch_id", jsApiBean.getMchId());
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("notify_url", jsApiBean.getNotifyUrl());
			packageParams.put("openid", openid);
			packageParams.put("out_trade_no", jsApiBean.getOutTradeNo());
			packageParams.put("spbill_create_ip", WeiXinConfig.SPBILL_CREATE_IP);
			int endPayTime = 30*60000;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
			long nowTimes = System.currentTimeMillis();
			String strTime = sdf.format(new Date(Long.valueOf(nowTimes)));  
			/*if(jsApiBean.getLockTime()!=null&&jsApiBean.getLockTime()!=0){
				endPayTime = jsApiBean.getLockTime()*1000;
			}*/
			String strexpire = sdf.format(new Date(Long.valueOf(nowTimes+endPayTime))); 
			packageParams.put("time_start", strTime);
			packageParams.put("time_expire", strexpire);
			// 这里写的金额为1 分到时修改
			packageParams.put("total_fee", jsApiBean.getTotalFee() + "");
			packageParams.put("trade_type", jsApiBean.getTradeType());
			RequestHandler reqHandler = new RequestHandler(null, null);
			reqHandler.init(WeiXinConfig.appId, WeiXinConfig.appSecret,
					WeiXinConfig.key);

			String sign = reqHandler.createSign(packageParams);
			logger.info(sign+"-----sign");


			String xml = "<xml>" + "<appid><![CDATA["
					+ WeiXinConfig.appId
					+ "]]></appid>"
					+ "<attach><![CDATA["
					+ jsApiBean.getAttach()
					+ "]]></attach>"
					+ "<body><![CDATA["
					+ jsApiBean.getBody()
					+ "]]></body>"
					+ "<goods_tag><![CDATA["
					+ goods_tag
					+ "]]></goods_tag>"// jsApiBean.getGoodsTag()
					+ "<mch_id><![CDATA[" + jsApiBean.getMchId() + "]]></mch_id>"
					+ "<nonce_str><![CDATA[" + nonce_str + "]]></nonce_str>"
					+ "<notify_url><![CDATA[" + jsApiBean.getNotifyUrl()
					+ "]]></notify_url>" + "<openid><![CDATA[" + openid
					+ "]]></openid>"
					+ "<out_trade_no><![CDATA["
					+ out_trade_no
					+ "]]></out_trade_no>"
					+ "<sign><![CDATA["
					+ sign
					+ "]]></sign>"
					+ "<spbill_create_ip><![CDATA["
					+ WeiXinConfig.SPBILL_CREATE_IP
					+ "]]></spbill_create_ip>"// + jsApiBean.getSpbillCreateIp() +
												// "]]></spbill_create_ip>"
					+ "<time_expire><![CDATA[" + strexpire + "]]></time_expire>"
					+ "<time_start><![CDATA[" + strTime + "]]></time_start>"

					+ "<total_fee><![CDATA[" + jsApiBean.getTotalFee()
					+ "]]></total_fee>" + "<trade_type><![CDATA["
					+ jsApiBean.getTradeType() + "]]></trade_type>" + "</xml>";

			String prepay_id = "";
			String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
			logger.info(prepay_id+"-----prepay_id");
			// 获取prepay_id后，拼接最后请求支付所需要的package

			SortedMap<String, String> finalpackage = new TreeMap<String, String>();
			// 要签名
			String timestamp = Sha1Util.getTimeStamp();
			String packages = "prepay_id=" + prepay_id;
			finalpackage.put("appId", WeiXinConfig.appId);
			finalpackage.put("timeStamp", timestamp);
			finalpackage.put("nonceStr", nonce_str);
			finalpackage.put("package", packages);
			finalpackage.put("signType", "MD5");

			String finalsign = reqHandler.createSign(finalpackage);
			logger.info(finalsign+"--------------finalsign");
			finalpackage.put("paySign", finalsign);

			String finaPackage = "{\"appId\":\"" + WeiXinConfig.appId
					+ "\",\"timeStamp\":\"" + timestamp + "\",\"nonceStr\":\""
					+ nonce_str + "\",\"package\":\"" + packages
					+ "\",\"signType\" : \"MD5" + "\",\"paySign\":\"" + finalsign
					+ "\"}";
			returnMap.put("finaPackage", finaPackage);
			returnMap.put("prepayPack", packages);
		} catch (Exception e) {
			System.out.println(e);
		}
		return returnMap;
	}

	/**
	 * @descriptions:获得随机字符串
	 * @copyright：
	 */
	private String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(8) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom + "fasdfasdf";
	}

	@Override
	public Map<String, Object> queryUserOpenId() {
		String sql="select openid from h_weixin_user";
		final Map<String,Object> map=new HashMap<String, Object>();
		super.jdbcTemplate.query(sql,new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				map.put(rs.getString(1),"");
			}
		});
		return map;
	}
}
