package cn.syrjia.callCenter.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;

public interface CallCenterService extends BaseServiceInterface {

	/**
	 * 电话咨询、问诊、复诊呼叫中心回调修改电话拨打时间及时长
	 * @param order
	 * @return
	 */
	public abstract Map<String,Object> updateOrderTime(Integer yyPhoneTime,String orderNo,String startTime,Integer talkTime,Integer validityTime);
	
	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 * @param order
	 * @return
	 */
	public abstract Map<String,Object> updateRecordTime(String id,String startTime,String talkTime,String fromId,String toId,Integer type,String fromPhone,String toPhone,String orderNo,Integer state);
}
