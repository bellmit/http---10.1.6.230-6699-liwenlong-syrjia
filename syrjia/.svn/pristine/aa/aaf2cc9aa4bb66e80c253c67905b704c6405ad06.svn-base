package cn.syrjia.callCenter.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import cn.syrjia.callCenter.dao.CallCenterDao;
import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Repository("callCenterDao")
public class CallCenterDaoImpl extends BaseDaoImpl implements CallCenterDao {

	// 日志
	private Logger logger = LogManager.getLogger(CallCenterDaoImpl.class);

	/**
	 * 呼叫中心回调修改电话拨打时间及时长
	 * 
	 * @param order
	 * @return
	 */
	@Override
	public Integer updateOrderTime(Integer yyPhoneTime,String orderNo, String startTime,
			Integer talkTime, Integer validityTime) {
		Integer i = 0;
		try {
			if (!StringUtil.isEmpty(orderNo)
					&& (!StringUtils.isEmpty(startTime)
							|| !StringUtil.isEmpty(talkTime) || !StringUtil
								.isEmpty(validityTime))|| !StringUtil
								.isEmpty(yyPhoneTime)) {
				String sql = "UPDATE t_order_detail_server set orderNo='"
						+ orderNo + "' ";
				if (!StringUtil.isEmpty(yyPhoneTime)) {
					sql += " ,yyPhoneTime=" + yyPhoneTime ;
				}
				if (!StringUtils.isEmpty(startTime)) {
					sql += " ,startPhoneTime='" + startTime + "' ";
				}
				if (!StringUtil.isEmpty(talkTime)) {
					sql += " ,talkTime=" + talkTime;
				}
				/*if (!StringUtil.isEmpty(validityTime)) {
					sql += " ,validityTime=" + validityTime;
				}*/
				sql += " where orderNo='" + orderNo + "' ";
				i = jdbcTemplate.update(sql);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 * 
	 * @param order
	 * @return
	 */
	@Override
	public Integer updateRecordTime(String id, String startTime, String talkTime) {
		Integer i = 0;
		try {
			if (!StringUtil.isEmpty(id)
					&& (!StringUtils.isEmpty(startTime) || !StringUtils
							.isEmpty(talkTime))) {
				String sql = "UPDATE t_phone_record set id='" + id + "' ";
				if (!StringUtils.isEmpty(startTime)) {
					sql += " ,startPhoneTime='" + startTime + "' ";
				}
				if (!StringUtils.isEmpty(talkTime)) {
					sql += " ,talkTime='" + talkTime + "' ";
				}
				sql += " where id='" + id + "' ";
				i = jdbcTemplate.update(sql);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 */
	@Override
	public Integer addRecordTime(String startTime, String talkTime,
			String fromId, String toId, Integer type,String fromPhone,String toPhone,String orderNo,Integer state) {
		Integer i = 0;
		try {
			String sql = "INSERT INTO t_phone_record (t_phone_record.id,t_phone_record.createTime,t_phone_record.startPhoneTime,t_phone_record.talkTime,t_phone_record.fromPhone, "
					+ " t_phone_record.toPhone,t_phone_record.fromId,t_phone_record.toId,t_phone_record.type,t_phone_record.orderNo,t_phone_record.state) VALUES('"+Util.getUUID()+"',"+Util.queryNowTime()+",'"+startTime+"',"+talkTime+",'"+fromPhone+"','"+toPhone+"','"+fromId+"','"+toId+"',"+type+",'"+orderNo+"',"+state+") ";
			i = jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}
}
