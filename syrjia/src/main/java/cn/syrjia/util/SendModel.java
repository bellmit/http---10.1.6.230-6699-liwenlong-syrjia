package cn.syrjia.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.SendTempMsg;
import cn.syrjia.entity.TemplateData;
import cn.syrjia.hospital.entity.SendMode;
import cn.syrjia.service.PushService;
import net.sf.json.JSONObject;

/**
 * 推送消息模板 ClassName: SendModel
 * 
 * @Description: TODO
 */
public class SendModel {
	// app推送type : reserve:预定/rent:租赁/order:订单/notice:场地通知公告（整体推送
	// apns）/news:新闻风采（整体推送apns）/repair:物业报修/membercard:会员卡相关/coach:教练
	// 日志
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SendModel.class);

	/**
	 * app预约申请提交通知
	 * 
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> yyApplySuccess_App(Map<String, Object> map) {
		Map<String, Object> sendMap = new HashMap<>();
		String data = "1";
		String title = "您有一个新的预约待处理";
		StringBuffer content = new StringBuffer();
		content.append("详情信息:" + map.get("name").toString() + "于"
				+ DateTime.getTime() + "提交了" + map.get("typeName") + "申请");
		// content.append("，消费金额"+ order.getOrderprice() +"元，祝您生活愉快！");
		sendMap.put("title", title);
		sendMap.put("content", content.toString());
		sendMap.put("type", "reserve");
		if (!StringUtils.isBlank(map.get("token").toString())) {// 含有token信息，进行推送
			sendMap.put("isall", 0);
			sendMap.put("data", data);
			List<Map<String, Object>> apns = new ArrayList<>();
			Map<String, Object> apn = new HashMap<>();
			apn.put("id", map.get("loginname"));
			apn.put("token", map.get("token"));
			apn.put("ostype", map.get("detype"));
			apn.put("model", map.get("demodel"));
			apns.add(apn);
			sendMap.put("apns", apns);
			String sendResult = HttpSend.HttpPost2(sendMap);
			System.out.println(sendResult);
		}
		return sendMap;
	}

	/**
	 * 微信预约申请成功通知
	 * 
	 * @return Map<String,Object>
	 * @throws
	 */
	public static <T extends BaseDaoInterface> Map<String, Object> yyApplySuccess_Weixin(
			Map<String, Object> map, T t) {
		SendTempMsg sendTempMsg = new SendTempMsg();
		sendTempMsg.setTouser(map.get("openid").toString());
		sendTempMsg.setTemplate_id(TemplateIdUtil.yyApplySuccess);
		List<TemplateData> datalist = new ArrayList<>();
		TemplateData templateData1 = new TemplateData();// title
		TemplateData templateData2 = new TemplateData();// 提交时间
		TemplateData templateData3 = new TemplateData();// 预约类型
		TemplateData templateData4 = new TemplateData();// 详情
		templateData1.setValue("您有一个新的预约待处理");
		templateData2.setValue(DateTime.getTime());
		templateData3.setValue(map.get("typeName").toString());
		templateData4.setValue(map.get("remarks").toString());

		datalist.add(templateData1);
		datalist.add(templateData2);
		datalist.add(templateData3);
		datalist.add(templateData4);
		sendTempMsg.setDatalist(datalist);
		SendTempUtil.sendMsg(sendTempMsg, t);
		return null;
	}

	/**
	 * 微信退款申请通知
	 */
	public static <T extends BaseDaoInterface> Map<String, Object> ApplyRefundMoney_Weixin(
			Map<String, Object> map, T t) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		SendTempMsg sendTempMsg = new SendTempMsg();
		sendTempMsg.setTouser(map.get("openid").toString());
		sendTempMsg.setTemplate_id(TemplateIdUtil.ApplyRefundMoney_Weixin);
		List<TemplateData> templateDatat = new ArrayList<>();

		TemplateData templateData = new TemplateData();
		templateData.setValue(map.get("first").toString());

		TemplateData templateData1 = new TemplateData();
		templateData1.setValue(map.get("orderNo").toString());

		TemplateData templateData2 = new TemplateData();
		templateData2.setValue(map.get("name").toString());

		TemplateData templateData3 = new TemplateData();
		templateData3.setValue(map.get("money").toString() + "元");

		TemplateData templateData4 = new TemplateData();
		templateData4.setValue(DateTime.getTime());

		TemplateData templateData5 = new TemplateData();
		templateData5.setValue(map.get("applyReason").toString());

		TemplateData templateData6 = new TemplateData();
		templateData6.setValue(map.get("noticeRemark").toString());

		templateDatat.add(templateData);
		templateDatat.add(templateData1);
		templateDatat.add(templateData2);
		templateDatat.add(templateData3);
		templateDatat.add(templateData4);
		templateDatat.add(templateData5);
		templateDatat.add(templateData6);
		if("1".equals(map.get("sendMember"))){
			String url = "http://"
					+ TemplateIdUtil.serverName
					+ "/HXTravelInterface/weixin/order/refund.html?orderNo="
					+ map.get("orderNo");
			sendTempMsg.setUrl(url);
		}
		sendTempMsg.setDatalist(templateDatat);
		String returnStatus = SendTempUtil.sendMsg(sendTempMsg, t);
		returnMap.put("returnStatus", returnStatus);
		return returnMap;
	}

	/**
	 * APP退款申请通知
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> ApplyRefundMoney_App(
			Map<String, Object> map) {
		Map<String, Object> sendMap = new HashMap<>();
		String data = map.get("orderNo").toString();
		String title = "很抱歉，您的退款申请未通过审核";
		StringBuffer content = new StringBuffer();
		content.append("退款单号：" + map.get("orderNo") + ",退款金额："
				+ map.get("money") + "，失败原因：" + map.get("refundReason"));
		sendMap.put("title", title);
		sendMap.put("content", content.toString());
		sendMap.put("type", "orderDetail");
		if (!StringUtils.isBlank(map.get("token").toString())) {// 含有token信息，进行推送
			sendMap.put("isall", 0);
			sendMap.put("data", data);
			List<Map<String, Object>> apns = new ArrayList<>();
			Map<String, Object> apn = new HashMap<>();
			apn.put("id", map.get("loginname"));
			apn.put("token", map.get("token"));
			apn.put("ostype", map.get("detype"));
			apn.put("model", map.get("demodel"));
			apns.add(apn);
			sendMap.put("apns", apns);
			String sendResult = HttpSend.HttpPost2(sendMap);
			if ("success".equals(sendResult)) {
				System.out.println("app消息推送成功！To" + map.get("loginname"));
				sendMap.put("sendResult", sendResult);
			}
		}
		return sendMap;
	}

	/**
	 * 微信支付成功通知
	 */
	public static <T extends BaseDaoInterface> Map<String, Object> PayOrderSuccess_Weixin(
			Map<String, Object> map, T t) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		SendTempMsg sendTempMsg = new SendTempMsg();
		sendTempMsg.setTouser(map.get("openid").toString());
		sendTempMsg.setTemplate_id(TemplateIdUtil.PayOrderSuccess_Weixin);
		List<TemplateData> templateDatat = new ArrayList<>();

		TemplateData templateData = new TemplateData();
		templateData.setValue("恭喜您，订单：" + map.get("orderNo") + "支付成功。");

		TemplateData templateData1 = new TemplateData();
		templateData1.setValue(map.get("orderNo").toString());

		TemplateData templateData2 = new TemplateData();
		templateData2.setValue(map.get("remarks").toString());

		TemplateData templateData3 = new TemplateData();
		templateData3.setValue(map.get("receiptsPrice").toString() + "元");

		TemplateData templateData4 = new TemplateData();
		templateData4.setValue("点此查看详情");

		templateDatat.add(templateData);
		templateDatat.add(templateData1);
		templateDatat.add(templateData2);
		templateDatat.add(templateData3);
		templateDatat.add(templateData4);
		// 服务器网址+页面+参数
		Integer orderType = map.get("orderType") == null ? null : Integer
				.valueOf(map.get("orderType").toString());
		String url = "";
		if (orderType != null) {
			if (orderType == 1) {
				url = "http://"
						+ TemplateIdUtil.serverName
						+ "/HXTravelInterface/weixin/order/order_details.html?orderNo="
						+ map.get("orderNo");
			} else if (orderType == -1) {
				url = "";
			} else if (orderType == 4) {
				url = "http://"
						+ TemplateIdUtil.serverName
						+ "/HXTravelInterface/weixin/confirm_gh_detail/confirm_gh_detail.html?orderNo="
						+ map.get("orderNo");
			} else if (orderType == 5) {
				url = "http://"
						+ TemplateIdUtil.serverName
						+ "/HXTravelInterface/weixin/medicalOrder/confirm_escort_orderDetail.html?orderNo="
						+ map.get("orderNo");
			} else if (orderType == 6) {
				url = "http://"
						+ TemplateIdUtil.serverName
						+ "/HXTravelInterface/weixin/medicalOrder/confirm_hz_detail.html?orderNo="
						+ map.get("orderNo");
			} else if (orderType == 7) {
				url = "http://"
						+ TemplateIdUtil.serverName
						+ "/HXTravelInterface/weixin/medicalOrder/confirm_zy_detail.html?orderNo="
						+ map.get("orderNo");
			}
		} else {
			url = "http://" + TemplateIdUtil.serverName
					+ "/HXTravelInterface/weixin/order/orderall.html";
		}
		sendTempMsg.setUrl(url);
		sendTempMsg.setDatalist(templateDatat);
		String returnStatus = SendTempUtil.sendMsg(sendTempMsg, t);
		returnMap.put("returnStatus", returnStatus);
		return returnMap;
	}

	/**
	 * APP支付成功通知
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> PayOrderSuccess_App(
			Map<String, Object> map) {
		Map<String, Object> sendMap = new HashMap<>();
		String data = map.get("orderNo").toString();
		String title = "恭喜您，订单：" + map.get("orderNo") + "支付成功。";
		StringBuffer content = new StringBuffer();
		content.append("订单号：" + map.get("orderNo") + ",支付金额："
				+ map.get("orderPrice") + ",商品详情:" + map.get("remarks"));
		sendMap.put("title", title);
		sendMap.put("content", content.toString());
		sendMap.put("type", "orderDetail");
		if (!StringUtils.isBlank(map.get("token").toString())) {// 含有token信息，进行推送
			sendMap.put("isall", 0);
			sendMap.put("data", data);
			List<Map<String, Object>> apns = new ArrayList<>();
			Map<String, Object> apn = new HashMap<>();
			apn.put("id", map.get("loginname"));
			apn.put("token", map.get("token"));
			apn.put("ostype", map.get("detype"));
			apn.put("model", map.get("demodel"));
			apns.add(apn);
			sendMap.put("apns", apns);
			String sendResult = HttpSend.HttpPost2(sendMap);
			if ("success".equals(sendResult)) {
				System.out.println("app消息推送成功！To" + map.get("loginname"));
				sendMap.put("sendResult", sendResult);
			}
		}
		return sendMap;
	}

	/**
	 * 微信服务未处理通知
	 */
	public static <T extends BaseDaoInterface> Map<String, Object> serverNoFinish_Weixin(
			Map<String, Object> map, T t) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		SendTempMsg sendTempMsg = new SendTempMsg();
		sendTempMsg.setTouser(map.get("openid").toString());
		sendTempMsg.setTemplate_id(TemplateIdUtil.serverNoFinish_Weixin);
		List<TemplateData> templateDatat = new ArrayList<>();

		TemplateData templateData = new TemplateData();
		templateData.setValue("你好，你有服务申请还未处理。");

		TemplateData templateData1 = new TemplateData();
		templateData1.setValue(map.get("userName").toString());

		TemplateData templateData2 = new TemplateData();
		templateData2.setValue(map.get("typeName").toString());

		TemplateData templateData3 = new TemplateData();
		templateData3.setValue(map.get("createTime").toString());

		TemplateData templateData4 = new TemplateData();
		templateData4.setValue(map.get("endDate").toString());

		TemplateData templateData5 = new TemplateData();
		templateData5.setValue("请及时处理");

		templateDatat.add(templateData);
		templateDatat.add(templateData1);
		templateDatat.add(templateData2);
		templateDatat.add(templateData3);
		templateDatat.add(templateData4);
		sendTempMsg.setDatalist(templateDatat);
		String returnStatus = SendTempUtil.sendMsg(sendTempMsg, t);
		returnMap.put("returnStatus", returnStatus);
		return returnMap;
	}

	/**
	 * APP服务未处理通知
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> serverNoFinish_App(Map<String, Object> map) {
		Map<String, Object> sendMap = new HashMap<>();
		String data = "1";
		String title = "您有服务申请未处理，即将到期，请及时处理";
		StringBuffer content = new StringBuffer();
		content.append("患者名称：" + map.get("userName") + ",服务类型："
				+ map.get("typeName") + ",申请日期:" + map.get("createTime")
				+ ",请及时处理");
		sendMap.put("title", title);
		sendMap.put("content", content.toString());
		sendMap.put("type", "orderDetail");
		if (!StringUtils.isBlank(map.get("token").toString())) {// 含有token信息，进行推送
			sendMap.put("isall", 0);
			sendMap.put("data", data);
			List<Map<String, Object>> apns = new ArrayList<>();
			Map<String, Object> apn = new HashMap<>();
			apn.put("id", map.get("loginname"));
			apn.put("token", map.get("token"));
			apn.put("ostype", map.get("detype"));
			apn.put("model", map.get("demodel"));
			apns.add(apn);
			sendMap.put("apns", apns);
			String sendResult = HttpSend.HttpPost2(sendMap);
			if ("success".equals(sendResult)) {
				System.out.println("app消息推送成功！To" + map.get("loginname"));
				sendMap.put("sendResult", sendResult);
			}
		}
		return sendMap;
	}
	
	/**
	 * app挂号日期确认通知
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> ghConfirm_App(Map<String,Object> map){
		Map<String, Object> sendMap = new HashMap<>();
		String data = map.get("orderNo").toString();
		String title = "挂号日期已确认通知";
		StringBuffer content = new StringBuffer();
		content.append("详情信息:您的订单："+map.get("orderNo")+"挂号日期已确认为:"+map.get("ghDate")+"，请保持电话畅通，如有疑问，请联系客服");
		//content.append("，消费金额"+ order.getOrderprice() +"元，祝您生活愉快！");
		sendMap.put("title", title);
		sendMap.put("content", content.toString());
		sendMap.put("type", "reserve");
		if(!StringUtils.isBlank(map.get("token").toString())){//含有token信息，进行推送
			sendMap.put("isall", 0);
			sendMap.put("data", data);
			List<Map<String,Object>> apns = new ArrayList<>();
			Map<String,Object> apn =  new HashMap<>();
			apn.put("id", map.get("loginname"));
			apn.put("token", map.get("token"));
			apn.put("ostype", map.get("detype"));
			apn.put("model", map.get("demodel"));
			apns.add(apn);
			sendMap.put("apns", apns);
			String sendResult = HttpSend.HttpPost2(sendMap);
			System.out.println(sendResult);
		}
		return sendMap;
	}
	
	/**
	 * 微信挂号日期已确认通知
	 * @return Map<String,Object>  
	 * @throws
	 */
	public static <T extends BaseDaoInterface> Map<String, Object> sendToGhConfirm_Weixin(Map<String,Object> map,T t){
		SendTempMsg sendTempMsg = new SendTempMsg();
		sendTempMsg.setTouser(map.get("openid").toString());
		sendTempMsg.setTemplate_id(TemplateIdUtil.ghYqrSuccess);
		List<TemplateData> datalist = new ArrayList<>();
		TemplateData templateData1 = new TemplateData();//title
		TemplateData templateData2 = new TemplateData();//提交时间
		TemplateData templateData3 = new TemplateData();//预约类型
		TemplateData templateData4 = new TemplateData();//详情
		TemplateData templateData5 = new TemplateData();//提交时间
		TemplateData templateData6 = new TemplateData();//预约类型
		TemplateData templateData7 = new TemplateData();//详情
		templateData1.setValue("挂号日期已确认。");
		templateData2.setValue(map.get("name").toString());
		templateData3.setValue(map.get("infirmaryName").toString());
		templateData4.setValue(map.get("departName").toString());
		templateData5.setValue(map.get("positionName") == null ? "未填写" : map.get("departName")+map
				.get("positionName").toString());
		templateData6.setValue(map.get("ghDate").toString());
		templateData7.setValue("点此查看详情");
		datalist.add(templateData1);
		datalist.add(templateData2);
		datalist.add(templateData3);
		datalist.add(templateData4);
		datalist.add(templateData5);
		datalist.add(templateData6);
		datalist.add(templateData7);
		sendTempMsg.setDatalist(datalist);
		String url ="http://"+TemplateIdUtil.serverName+"/HXTravelInterface/weixin/medicalOrder/confirm_gh_detail.html?orderNo="+map.get("orderNo");
		sendTempMsg.setUrl(url);
		SendTempUtil.sendMsg(sendTempMsg,t);
		return null;
	}
	
	
	public static void push(String userId,String relevantUserId,String title,String content,String type,Integer port,Map<String,Object> data,PushService pushService,Integer isShow){
		Map<String,Object> userLog=pushService.queryLoginLogByUserId(userId);
		if(null==userLog||userLog.size()==0||StringUtil.isEmpty(userLog.get("token"))){
			return;
		}
		SendMode sendMode=new SendMode();
		sendMode.setContent(content);
		sendMode.setCreationTime(Util.queryNowTime()+"");
		sendMode.setModel(userLog.get("model").toString());
		sendMode.setOstype(userLog.get("ostype").toString());
		sendMode.setTitle(title);
		sendMode.setToken(userLog.get("token").toString());
		sendMode.setIsShow(isShow);
		sendMode.setVersion(userLog.get("version").toString());
		sendMode.setType(type);
		sendMode.setUserId(userId);
		sendMode.setRelevantUserId(relevantUserId);
		sendMode.setData(JSONObject.fromMap(data).toString());
		Object id=pushService.addEntityUUID(sendMode);
		
		if(userLog.get("type").toString().equals("2")){
			return;
		}
		
		if(!StringUtil.isEmpty(id)){
			if(null==data){
				data=new HashMap<String, Object>();
			}
			
			if(userLog.get("model").toString().toLowerCase().equals("ios")&&!StringUtil.isEmpty(data.get("photo"))){
				data.remove("photo");
			}
			
			data.put("id",id.toString());
			
			/*if(!StringUtil.isEmpty(relevantUserId)){
				UserMember userMember=userMemberService.queryById(UserMember.class,relevantUserId);
				content=content.replace("userName:",userMember.getUserName());
			}*/
			
			if(port==1){
				Map<String,Object> doctorSet=pushService.queryDoctorSet(userId);
				if(null!=doctorSet){
					if(doctorSet.get("isDisturb").toString().equals("1")){
						if(DateTime.isBelong(doctorSet.get("disturbStartTime").toString(), doctorSet.get("disturbEndTime").toString())){
							return;
						}
					}
				}
			}

			Map<String,Object> map=new HashMap<String,Object>();
			Map<String,Object> map1=new HashMap<String,Object>();
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			map1.put("id",userLog.get("loginName"));
			map1.put("ostype",userLog.get("ostype"));
			map1.put("model", userLog.get("model"));
			map1.put("token", userLog.get("token"));
			list.add(map1);
			map.put("isall","1");
			map.put("type",type);
			map.put("data",JSONObject.fromMap(data).toString());
			map.put("title",title);
			map.put("content",content);
			map.put("apns",list);
			if(port==1){
				HttpSend.HttpPost2(map);
			}else if(port==2){
				HttpSend.HttpPost3(map);
			}
		}
	}

	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> bindsuc_app(String srId,String doctorId,String docName,PushService pushService){
		String title = "绑定成功";
		String content = docName+"医生与您绑定成功，已成为您的医生！";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		push(srId,doctorId, title, content,"bindsuc",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> authassist_app(String srId,String doctorId,String docName,PushService pushService){
		String title = "认证提醒";
		String content = docName+"医生未认证，请帮忙提交认证信息";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		push(srId,doctorId, title, content,"authassist",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> paysuczxtl_app(String srId,String doctorId,String docName,PushService pushService){
		String title = "图文调理服务订单";
		String content = docName+"医生收到新的图文调理服务订单";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		push(srId,doctorId, title, content,"paysuczxtl",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docpaysuczxtl_app(String doctorId,String patientId,String patientName,PushService pushService){
		String title = "调理服务通知";
		String content ="您收到新的图文调理服务订单，该服务具有时间限制，请在尽快进行回复。";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,patientId, title, content,"docpaysuczxtl",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docpaysucdhtl_app(String doctorId,String patientId,String patientName,PushService pushService){
		String title = "调理服务通知";
		String content ="您收到新的电话调理服务订单，客服将在30分钟内致电与您确认调理服务时间，请留意您的来电哦。";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,patientId, title, content,"docpaysucdhtl",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docpaysuctwzx_app(String doctorId,String patientId,String patientName,PushService pushService){
		String title = "咨询服务通知";
		String content ="您收到新的图文咨询服务订单，请在24小时内进行回复。";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,patientId, title, content,"docpaysuctwzx",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docpaysucdhzx_app(String doctorId,String patientId,String patientName,PushService pushService){
		String title = "咨询服务通知";
		String content ="您收到新的电话咨询服务订单，客服将在30分钟内致电与您确认咨询服务时间，请留意您的来电哦。";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,patientId, title, content,"docpaysucdhzx",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docfinishzzms_app(String doctorId,String patientId,String patientName,PushService pushService){
		String title = "新消息提醒";
		String content =patientName+"患者已完成症状描述填写前往查看>>";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,patientId, title, content,"docfinishzzms",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docfinishwzd_app(String doctorId,String patientId,String patientName,String name,PushService pushService){
		String title = "新消息提醒";
		String content =patientName+"患者已完成"+name+"填写前往查看>>";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,patientId, title, content,"docfinishwzd",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docreceivepj_app(String doctorId,String orderNo,PushService pushService){
		String title = "收到评价";
		String content ="收到1条评价，前往查看>>";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("orderNo",orderNo);
		push(doctorId,null, title, content,"docreceivepj",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> paysucdhtl_app(String srId,String doctorId,String docName,PushService pushService){
		String title = "电话调理服务订单";
		String content = docName+"医生收到新的电话调理服务订单";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		push(srId,doctorId, title, content,"paysucdhtl",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> paysuctwzx_app(String srId,String doctorId,String docName,PushService pushService){
		String title = "图文咨询服务订单";
		String content = docName+"医生收到新的图文咨询服务订单";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		push(srId,doctorId, title, content,"paysuctwzx",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> paysucdhzx_app(String srId,String doctorId,String docName,PushService pushService){
		String title = "电话咨询服务订单";
		String content = docName+"医生收到新的电话咨询服务订单";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		push(srId,doctorId, title, content,"paysucdhzx",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> unpayedorder_app(String srId,String doctorId,String orderNo,String docName,PushService pushService){
		String title = "调理订单";
		String content = docName+"医生的调理方案仍处于未支付状态，请及时跟进，点击查看详情>>";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		data.put("orderNo",orderNo);
		push(srId,doctorId, title, content,"unpayedorder",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docprescorder_app(String srId,String doctorId,String orderNo,String docName,PushService pushService){
		String title = "调理订单";
		String content = docName+"医生收到新的调理订单，点击查看详情>>";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("docId",doctorId);
		data.put("orderNo",orderNo);
		push(srId,doctorId, title, content,"docprescorder",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> doctwzxservice_app(String srId,String realName,String docName,String orderNo,String doctorId,PushService pushService){
		String title = "调理订单";
		String content = realName+"患者购买了"+docName+"医生的图文咨询，请提醒医生在24小时内与患者及时沟通";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("orderNo",orderNo);
		data.put("docId",doctorId);
		push(srId,null, title, content,"doctwzxservice",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> doctwtlservice_app(String srId,String realName,String docName,String orderNo,String doctorId,PushService pushService){
		String title = "调理订单";
		String content = realName+"患者购买了"+docName+"医生的图文调理，请提醒医生在24小时内与患者及时沟通";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("orderNo",orderNo);
		data.put("docId",doctorId);
		push(srId,null, title, content,"doctwtlservice",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docpaytlsuc_app(String doctorId,String realName,String orderNo,PushService pushService){
		String title = "支付通知";
		String content = "您为患者定制的调理方案已支付，感谢您的耐心服务。前往查看>>";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("orderNo",orderNo);
		push(doctorId,null, title, content,"docpaytlsuc",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docbindsuc_app(String srId,String doctorId, String srName,PushService pushService){
		String title = "医生助理";
		String content = "您好，我是您的专属医生助理"+srName+"，感谢您选择上医仁家平台，如您在使用过程中有任何问题，请随时与我联系。";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("srId",srId);
		push(doctorId,null, title, content,"docbindsuc",1,data,pushService,2);
		return null;
	}

	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docCf_app(String doctorId, String patientId,PushService pushService){
		String title = "抄方申请通知";
		String content = "您收到新的抄方订单，请尽快进行处理";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,null, title, content,"docCf",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> docmcomment_app(String doctorId,PushService pushService){
		String title = "评论回复";
		String content ="收到评论回复,点击查看详情";
		Map<String,Object> data=new HashMap<String, Object>();
		push(doctorId,null, title, content,"docmcomment",1,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> zlcommentreply_app(String doctorId,PushService pushService){
		String title = "评论回复";
		String content ="收到评论回复,点击查看详情";
		Map<String,Object> data=new HashMap<String, Object>();
		push(doctorId,null, title, content,"zlcommentreply",2,data,pushService,2);
		return null;
	}
	
	/**
	 * @Description: TODO
	 * @param @param user
	 * @param @param infoList
	 */
	public static Map<String, Object> doctlservice_app(String doctorId,String patientId,String patientName,String date,String serverName,PushService pushService){
		String title = "调理服务通知";
		String content =patientName+"患者购买的"+serverName+"服务将在"+date+"开始，请您提前预留此段时间并注意接听来电";
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("patientId",patientId);
		push(doctorId,null, title, content,"doctlservice",1,data,pushService,2);
		return null;
	}
}
