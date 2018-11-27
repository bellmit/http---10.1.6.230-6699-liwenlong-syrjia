package cn.syrjia.callCenter.dao;

import cn.syrjia.common.BaseDaoInterface;

public interface CallCenterDao extends BaseDaoInterface {

	/**
	 * 电话咨询、问诊、复诊呼叫中心回调修改订单电话拨打时间及时长
	 * @return
	 */
	public abstract Integer updateOrderTime(Integer yyPhoneTime,String orderNo,String startTime,Integer talkTime,Integer validityTime);
	
	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 * @param order
	 * @return
	 */
	public abstract Integer updateRecordTime(String id,String startTime,String talkTime);
	
	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 * @param order
	 * @return
	 */
	public abstract Integer addRecordTime(String startTime,String talkTime,String fromId,String toId,Integer type,String fromPhone,String toPhone,String orderNo,Integer state);
	
}
