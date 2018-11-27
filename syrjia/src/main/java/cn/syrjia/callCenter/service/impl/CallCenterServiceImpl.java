package cn.syrjia.callCenter.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.syrjia.callCenter.dao.CallCenterDao;
import cn.syrjia.callCenter.service.CallCenterService;
import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.service.PushService;
import cn.syrjia.util.GetSig;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import cn.syrjia.util.sendModelMsgUtil;
import net.sf.json.JSONObject;

@Service("callCenterService")
public class CallCenterServiceImpl extends BaseServiceImpl implements
		CallCenterService {

	@Resource(name = "callCenterDao")
	CallCenterDao callCenterDao;
	
	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;
	
	@Resource(name = "pushService")
	PushService pushService;
	
	/**
	 * 电话咨询、问诊、复诊呼叫中心回调修改电话拨打时间及时长
	 */
	@Override
	public Map<String,Object> updateOrderTime(Integer yyPhoneTime,String orderNo, String startTime,
			Integer talkTime, Integer validityTime) {
		Integer i = callCenterDao.updateOrderTime(yyPhoneTime,orderNo, startTime, talkTime, validityTime);
		if(i>0){
			Map<String,Object> order = appDoctorDao.queryOneOrder(orderNo);
			if(order!=null&&!StringUtil.isEmpty(order.get("memberId"))&&!StringUtil.isEmpty(yyPhoneTime)){
				String serverName = "电话调理/电话咨询";
				if(Integer.valueOf(order.get("orderType").toString())==7){
					serverName = "电话调理";
				}else if(Integer.valueOf(order.get("orderType").toString())==8){
					serverName = "电话咨询";
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("msgtype", "text");
				params.put("toUser", order.get("memberId"));
				params.put("url", "");
				params.put("content", "您购买的"+serverName+"服务将在"+Util.isNowDayStr(yyPhoneTime)+"开始，请提前预留此段时间并注意接听来电，服务开始前1小时内无法进行时间的更改。");
				String jsonString = sendModelMsgUtil.sendCustomMsg(params, callCenterDao);
				System.out.println(jsonString);
				
				pushService.doctlservice(order.get("doctorId").toString(),order.get("patientId").toString(),order.get("patientName").toString(), serverName, Util.isNowDayStr(yyPhoneTime));
			}
			return Util.resultMap(configCode.code_1001, i);
		}else{
			return Util.resultMap(configCode.code_1014, i);
		}
	}

	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 */
	@Override
	public  Map<String,Object> updateRecordTime(String id, String startTime, String talkTime,String fromId,String toId,Integer type,String fromPhone,String toPhone,String orderNo,Integer state) {
		Integer i = null;
		if(StringUtil.isEmpty(type)){
			type = 0;
		}
		if(StringUtil.isEmpty(id)){
			if(!StringUtil.isEmpty(orderNo)&&!StringUtil.isEmpty(state)&&state==3){
				callCenterDao.updateOrderTime(null, orderNo, startTime, Integer.valueOf(talkTime), null);
			}
			i = callCenterDao.addRecordTime(startTime, talkTime,fromId,toId,type,fromPhone,toPhone,orderNo,state);
		}else{
			i = callCenterDao.updateRecordTime(id, startTime, talkTime);
		}
		if(i>0){
			if(type==1||type==2){
				Map<String,Object> lastOrder=appDoctorDao.queryLastOrderNo(toId,fromId);
				if(!StringUtil.isEmpty(state)&&state==3){
					//通话已结束，通话时常XX秒
					JSONObject json=new JSONObject();
					json.put("msgType",28);
					json.put("serverOrderNo",lastOrder.get("orderNo").toString());
					json.put("content","通话已结束，通话时长"+talkTime+"秒");
					GetSig.sendMsgSync(null,fromId,toId, json);
					appDoctorDao.updatePhoneAdvisory(lastOrder.get("orderNo").toString());
				}else{
					//呼叫失败
					JSONObject json=new JSONObject();
					json.put("msgType",29);
					json.put("serverOrderNo",lastOrder.get("orderNo").toString());
					json.put("content","呼叫失败");
					GetSig.sendMsgSync(null,fromId,toId, json);
				}
			}
			return Util.resultMap(configCode.code_1001, i);
		}else{
			return Util.resultMap(configCode.code_1014, i);
		}
	}
}
