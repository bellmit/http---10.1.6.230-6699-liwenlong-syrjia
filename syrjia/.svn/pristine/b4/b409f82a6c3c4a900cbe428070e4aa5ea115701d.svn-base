package cn.syrjia.callCenter.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.callCenter.service.CallCenterService;

@Controller
@RequestMapping("/callCenter")
public class CallCenterController {

	@Resource(name = "callCenterService")
	CallCenterService callCenterService;
	
	/**
	 * 电话咨询、问诊、复诊呼叫中心回调修改订单电话拨打时间及时长
	 * @param order
	 * @return
	 */
	@RequestMapping("/updateOrderTime")
	@ResponseBody
	public Map<String,Object> updateOrderTime(HttpServletRequest request,Integer yyPhoneTime,String orderNo,String startTime,Integer talkTime,Integer validityTime){
		return callCenterService.updateOrderTime(yyPhoneTime,orderNo, startTime, talkTime, validityTime);
	}
	
	/**
	 * 医患交流页面中点击电话功能呼叫中心回调修改电话拨打时间及时长
	 * @param order
	 * @return
	 */
	@RequestMapping("/updateRecordTime")
	@ResponseBody
	public Map<String,Object> updateRecordTime(HttpServletRequest request,String id,String startTime,String talkTime,String fromId,String toId,Integer type,String fromPhone,String toPhone,String orderNo,Integer state){
		return callCenterService.updateRecordTime(id, startTime, talkTime,fromId,toId,type,fromPhone,toPhone,orderNo,state);
	}
}
