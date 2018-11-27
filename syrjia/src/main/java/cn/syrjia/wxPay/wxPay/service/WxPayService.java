package cn.syrjia.wxPay.wxPay.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.WXPayBean;

public interface WxPayService extends BaseServiceInterface{

	/**
	 * @descriptions:jsApi接口
	 * @copyright：
	 */
	public Map<String,Object> getPackage(String openid, WXPayBean jsApiBean);
	
	/**
	 * 查询用户openid
	 * @return
	 */
	public abstract Map<String,Object> queryUserOpenId();
	
}
