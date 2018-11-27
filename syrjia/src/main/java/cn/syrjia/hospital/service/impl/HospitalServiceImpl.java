package cn.syrjia.hospital.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.Config;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.SendTempMsg;
import cn.syrjia.entity.TemplateData;
import cn.syrjia.entity.WeiXinUser;
import cn.syrjia.hospital.dao.CouponDao;
import cn.syrjia.hospital.dao.HospitalDao;
import cn.syrjia.hospital.entity.Doctor;
import cn.syrjia.hospital.entity.DoctorReply;
import cn.syrjia.hospital.entity.Hospital;
import cn.syrjia.hospital.entity.SendMsgRecord;
import cn.syrjia.hospital.entity.Symptom;
import cn.syrjia.hospital.service.HospitalService;
import cn.syrjia.util.AmrToMp3;
import cn.syrjia.util.DateTime;
import cn.syrjia.util.DownloadImgByMediaId;
import cn.syrjia.util.SendTempUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.TemplateIdUtil;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.WeiXinConfig;

@Service("hospitalService")
public class HospitalServiceImpl extends BaseServiceImpl implements
		HospitalService {

	@Resource(name = "hospitalDao")
	HospitalDao hospitalDao;

	@Resource(name = "orderDao")
	OrderDao orderDao;

	@Resource(name = "couponDao")
	CouponDao couponDao;

	@Resource(name = "config")
	Config config;


	@SuppressWarnings("unused")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Object addSymptom(HttpServletRequest request,
			List<Symptom> symptoms, String orderNo, String test) {
		Object obj = null;
		String pathName = "";
		String id="";
		try {
			Map<String, String> map = new HashMap<String, String>();
			Integer age = 0;
			Integer sex = 0;
			int i=0;
			for (Symptom symptom : symptoms) {
				if (symptom.getType() != null) {
					if (symptom.getType() == 4 && null != symptom.getVoiceAddr()
							&& !"".equals(symptom.getVoiceAddr().trim())) {
						pathName = "symptom/voice";
						map = DownloadImgByMediaId.downloadMedia(
								symptom.getVoiceAddr(), pathName, request,
								hospitalDao, config.getImgIp(), "voice");
						if (map.size() > 0) {
							AmrToMp3.changeToMp3(
									map.get("riskPath").toString(),
									map.get("riskPath").toString()
											.replace(".amr", ".mp3"));
							symptom.setVoiceAddr(map.get("fwPath").toString()
									.replace(".amr", ".mp3"));
							String time = AmrToMp3.getMp3Time(map.get("riskPath")
									.toString().replace(".amr", ".mp3"));
							symptom.setVoiceLen(time);
						}
					} else if (symptom.getType() == 7) {
						age = symptom.getAge();
					} else if (symptom.getType() == 8) {
						if (symptom.getSex() == 0) {
							sex = 2;
						} else {
							sex = symptom.getSex();
						}
					}
					obj = null;
					symptom.setOrderNo(orderNo);
					symptom.setCreateTime((int) (System.currentTimeMillis() / 1000));
					obj = hospitalDao.addEntityUUID(symptom);
					//id=Util.getUUID();
					//symptom.setId(id);
					
					if(symptom==null || symptom.getType()==null){
						symptoms.remove(i);
					}
					i++;
				}
			}
			//obj=hospitalDao.addSymptom(symptoms);
			hospitalDao.updateDocOrderSign(orderNo, "0");

			WeiXinUser w = new WeiXinUser();
//			w.setAge(age);
			w.setSex(sex);
//			w.setOpenid(openid);
			hospitalDao.updateEntity(w);
			if (!StringUtil.isEmpty(test)) {
//				setSymptom(obj, test, hospitalDao);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return obj;
	}

	@SuppressWarnings("unused")
	@Override
	public Integer updateSymptom(HttpServletRequest request,
			List<Symptom> symptomList) {
		String pathName = "";
		Integer i = 0;
		Map<String, String> map = new HashMap<String, String>();
		for (Symptom symptom : symptomList) {
			if (!StringUtils.isEmpty(symptom.getId())) {
				if (!StringUtils.isEmpty(symptom.getOldPhysicsAddr())) {
					if (!symptom.getOldPhysicsAddr().equals(
							symptom.getPhysicsAddr())) {
						Util.deleteFile(symptom.getOldPhysicsAddr());
					}
				}
				symptom.setOldPhysicsAddr(null);
				i = hospitalDao.updateEntity(symptom);
			} else {
				hospitalDao.addEntityUUID(symptom);
			}
		}
		return i;
	}

	@Override
	public DoctorReply addDoctorReply(DoctorReply doctorReply) {
		Object obj = hospitalDao.addEntityUUID(doctorReply);
		DoctorReply reply = hospitalDao.queryById(DoctorReply.class, obj);
		return reply;
	}

/*	public Object setSymptom(Object o, String test, HospitalDao hospitalDao) {
		List<Map<String, Object>> list = JsonUtil.parseJSON2List(test);
		Object obj = null;
		for (Map<String, Object> map : list) {
			List<Map<String, Object>> specialTestOptions = JsonUtil
					.parseJSON2List(map.get("specialTitleOptions").toString());
			Map<String, Object> specialTest = JsonUtil.jsonToMap(map
					.get("specialTest"));

			SubjectHistoryTitle subjectHistoryTitle = new SubjectHistoryTitle();
			subjectHistoryTitle.setQid(Integer.parseInt(specialTest.get("qid")
					.toString()));
			subjectHistoryTitle.setOptionType(Integer.parseInt(specialTest.get(
					"optionType").toString()));
			subjectHistoryTitle.setTitleName(specialTest.get("titleName")
					.toString());
			subjectHistoryTitle.setSymptomId(o.toString());

			obj = hospitalDao.addEntityUUID(subjectHistoryTitle);

			for (Map<String, Object> options : specialTestOptions) {

				SubjectHistoryTitleOptions SubjectHistoryTitleOptions = new SubjectHistoryTitleOptions();

				SubjectHistoryTitleOptions.setOptionName(options.get(
						"optionName").toString());

				SubjectHistoryTitleOptions.setOptionNum(options
						.get("optionNum").toString());

				SubjectHistoryTitleOptions.setTitleId(obj.toString());

				Object obj1 = hospitalDao
						.addEntityUUID(SubjectHistoryTitleOptions);

				List<Map<String, Object>> symptomsOptions = JsonUtil
						.parseJSON2List(options.get("symptomsOptions"));
				for (Map<String, Object> symptomsOption : symptomsOptions) {
					HistorySymptomsOption symptoms = new HistorySymptomsOption();
					symptoms.setOptionName(symptomsOption.get("optionName")
							.toString());
					symptoms.setOptionNum(Integer.parseInt(symptomsOption.get(
							"optionNum").toString()));
					symptoms.setTitleId(obj1.toString());
					hospitalDao.addEntityUUID(symptoms);
				}
			}
		}

		return obj;
	}*/

	@Override
	public Map<String, Object> sendMsgToDoc(HttpServletRequest request,
			String docId, String openId, Integer isDoc,
			Map<String, Object> vistedMap) {
/*		Doctor d = hospitalDao.queryById(Doctor.class, docId, false);
		WeiXinUser docUser = hospitalDao.queryById(WeiXinUser.class,
				d.getOpenid(), false);// 此处的到会员的信息
		WeiXinUser user = hospitalDao
				.queryById(WeiXinUser.class, openId, false);// 此处的到会员的信息
		StringBuffer sb = new StringBuffer();
		StringBuffer sbToDoc = new StringBuffer();
		StringBuffer sbHistory = new StringBuffer();
		StringBuffer sbToDocHistory = new StringBuffer();
		//String url = "http://60.205.56.203:3000/";
		// 原聊天 请求地址
		// String historyUrl = "http://60.205.56.203:3000/history";
		// 现聊天请求地址
		String historyUrl = "toChatHistory.action";
		String docPhoto = null == docUser ? "" : docUser.getHeadimgurl();
		String userPhoto = null == user ? "" : user.getHeadimgurl();
		String orderNo = "";
		Integer visitDate = 0;
		String isVisitStatus = "";
		System.out.println(vistedMap == null);
		if (vistedMap != null && vistedMap.size() > 0) {
			visitDate = Integer.parseInt(vistedMap.get("orderDate").toString());
			visitDate = DateTime.CompareNowByStampReturn(visitDate + 172800);
			isVisitStatus = vistedMap.get("isVisitStatus").toString();
			orderNo = vistedMap.get("orderNo").toString();
			System.out.println(orderNo + "*********");
		}

		// 会员进入聊天
		
		 * sb.append("memId=").append(openId).append("&memLogo=").append(userPhoto
		 * ).append("&memName=").append(userName)
		 * .append("&docId=").append(docId
		 * ).append("&docLogo=").append(docPhoto).
		 * append("&docName=").append(docName).append("&isDoc=0")
		 * .append("&ticket="
		 * ).append(jsapi_ticket).append("&acToken=").append(acToken);
		 
		sbHistory.append("memId=").append(openId).append("&memLogo=")
				.append(userPhoto).append("&docId=").append(docId)
				.append("&docLogo=").append(docPhoto).append("&isDoc=")
				.append(isDoc).append("&orderNo=").append(orderNo);
		sb.append("memId=").append(openId).append("&memLogo=")
				.append(userPhoto).append("&docId=").append(docId)
				.append("&docLogo=").append(docPhoto).append("&isDoc=")
				.append(isDoc).append("&visitDate=").append(visitDate)
				.append("&isVisitStatus=").append(isVisitStatus)
				.append("&orderNo=").append(orderNo);

		// 医生进入聊天的url
		
		 * sbToDoc.append("memId=").append(openId).append("&memLogo=").append(
		 * userPhoto).append("&memName=").append(userName)
		 * .append("&docId=").append
		 * (docId).append("&docLogo=").append(docPhoto).
		 * append("&docName=").append(docName).append("&isDoc=1")
		 * .append("&ticket="
		 * ).append(jsapi_ticket).append("&acToken=").append(acToken);
		 
		sbToDocHistory.append("memId=").append(openId).append("&memLogo=")
				.append(userPhoto).append("&docId=").append(docId)
				.append("&docLogo=").append(docPhoto).append("&isDoc=1")
				.append("&orderNo=").append(orderNo);
		sbToDoc.append("memId=").append(openId).append("&memLogo=")
				.append(userPhoto).append("&docId=").append(docId)
				.append("&docLogo=").append(docPhoto).append("&isDoc=1")
				.append("&visitDate=").append(visitDate)
				.append("&isVisitStatus=").append(isVisitStatus)
				.append("&orderNo=").append(orderNo);

		String result = url + "?" + sb.toString();
		historyUrl = historyUrl + "?" + sbHistory.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("historyUrl", historyUrl);
		return map;*/
		
		return null;
	}

	@Override
	public List<Map<String, String>> returnMap(HttpServletRequest request,
			String[] serverIds) {
		List<Map<String, String>> map = new ArrayList<Map<String, String>>();
		Map<String, String> resultMap = new HashMap<String, String>();
		for (String s : serverIds) {
			resultMap = DownloadImgByMediaId.downloadMedia('"' + s + '"',
					"msgPhoto", request, hospitalDao, config.getImgIp());
			map.add(resultMap);
		}
		return map;
	}

	@Override
	public Map<String, Object> sendCoutom(HttpServletRequest request,
			String fromUser, String toUser, Integer isDoc, String orderNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sendUrl = "";

		String jsonString = "error";
		String docId = "";
		Map<String, Object> sendMsgMap = new HashMap<String, Object>();
		SendMsgRecord smr = null;
		// 发送客服消息数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("msgtype", "text");
		params.put("content", "您有新问诊回复");
		Doctor doc = null;
		System.out.println(fromUser + "+fromUser");
		System.out.println(toUser + "+toUser");
		if (!StringUtil.isEmpty(fromUser) && !StringUtil.isEmpty(toUser)) {
			if (isDoc == 0) {// 医生在线，给会员发
				sendUrl = request.getScheme() + "://" + request.getServerName()
						+ "/" + "ShangYiJia/toVisitDetailAuth.action";
				doc = hospitalDao.queryById(Doctor.class, fromUser);
				if (doc != null) {
					docId = doc.getDoctorId();
					String state = docId + "," + orderNo + ",isChat";

					SendMsgRecord sm = new SendMsgRecord(toUser, docId, 0);
					sendMsgMap = hospitalDao.querySendRecord(sm);
					params.put("toUser", toUser);
					smr = new SendMsgRecord(toUser, docId,
							Integer.parseInt(System.currentTimeMillis() / 1000
									+ ""), 0);// 插入记录表

					sendUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
							+ WeiXinConfig.appId
							+ "&redirect_uri="
							+ sendUrl
							+ "&response_type=code&scope=snsapi_base&state="
							+ state + "#wechat_redirect";
					System.out.println(sendUrl + "==sendUrl");
					// 发送客服消息数据
					params.put("content", "您有新问诊回复");
					params.put("url", sendUrl);
				}
			} else {// 会员在线，给医生发
				sendUrl = request.getScheme() + "://" + request.getServerName()
						+ "/" + "ShangYiJia/toUserInfoAuth.action";
				doc = hospitalDao.queryById(Doctor.class, toUser);
				if (doc != null) {
					docId = toUser;
					String state = fromUser + "," + docId + "," + orderNo
							+ ",isChat";
					;

					SendMsgRecord sm = new SendMsgRecord(fromUser, docId, 1);
					sendMsgMap = hospitalDao.querySendRecord(sm);
					params.put("toUser", doc.getOpenid());
					smr = new SendMsgRecord(fromUser, docId,
							Integer.parseInt(System.currentTimeMillis() / 1000
									+ ""), 1);// 插入记录表

					sendUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
							+ WeiXinConfig.appId
							+ "&redirect_uri="
							+ sendUrl
							+ "&response_type=code&scope=snsapi_base&state="
							+ state + "#wechat_redirect";
					System.out.println(sendUrl + "==sendUrl");

					// 发送客服消息数据
					params.put("content", "有新患者向您咨询");
					params.put("url", sendUrl);
				}
			}
		}
		if (doc != null) {
			Integer nowTime = (int) (System.currentTimeMillis() / 1000);
//			Integer jgTime = config.getChatSendTime() * 60;
			Integer jgTime = 5 * 60;
			if (sendMsgMap == null
					|| sendMsgMap.size() == 0
					|| (sendMsgMap != null
							&& sendMsgMap.get("sendTime") != null
							&& !StringUtil.isEmpty(sendMsgMap.get("sendTime")
									.toString()) && nowTime > (Integer
							.valueOf(sendMsgMap.get("sendTime").toString()) + jgTime))) {// 无记录发送模板
				jsonString = SendTempUtil.sendCustomMsg(params, hospitalDao);
				if ("0".equals(jsonString)) {
					hospitalDao.addEntity(smr);
				}
			}
		}
		map.put("resultCode", jsonString);
		return map;
	}

	@Override
	public List<Map<String, Object>> queryOrderTaskByDocId(String docId,
			Integer page, Integer row) {
		return hospitalDao.queryOrderTaskByDocId(docId, page, row);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> queryUserInfoToSymtom(String openId) {
		List<Map<String, Object>> list = hospitalDao
				.queryUserInfoToSymtom(openId);
		Map<String, Object> userMap = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				Iterator ite = map.entrySet().iterator();
				while (ite.hasNext()) {
					Entry entry = (Entry) ite.next();
					if (entry.getValue() != null
							&& !StringUtils.isEmpty(entry.getValue())) {
						userMap.put(entry.getKey().toString(), entry.getValue());
					}
				}
			}
		} else {
			WeiXinUser user = hospitalDao.queryById(WeiXinUser.class, openId,
					false);
			if (user != null) {
				/*userMap.put("name", user.getRealname());
				userMap.put("age", user.getAge());*/
				userMap.put("sex", user.getSex());
			}
		}
		return userMap;
	}

	/**
	 * 挂号订单生成成功通知
	 * 
	 * @param order
	 */
	@SuppressWarnings("unused")
	private void sendToUserAddOrderSuccess(Order order) {
		try {

			SendTempMsg sendTempMsg = new SendTempMsg();
			sendTempMsg.setTouser(order.getMemberId());
//			sendTempMsg.setTemplate_id(TemplateIdUtil.docOrderAddSuccess);
			Map<String, TemplateData> map = new HashMap<String, TemplateData>();
			List<TemplateData> templateDatat = new ArrayList<>();
			TemplateData templateData = new TemplateData();
			TemplateData templateData1 = new TemplateData();
			TemplateData templateData2 = new TemplateData();
			TemplateData templateData3 = new TemplateData();
			TemplateData templateData4 = new TemplateData();
			templateData.setValue("您的订单已成功提交，请确认支付");
			templateData1.setValue(DateTime.getTime());

			map.put("frist", templateData);
			map.put("keyword1", templateData1);

			templateData2.setValue(order.getOrderNo());
			map.put("keyword2", templateData2);

			templateData3.setValue(order.getOrderPrice()+"");
			map.put("keyword3", templateData3);
			
			templateData4.setValue("详情");

			templateDatat.add(templateData);
			templateDatat.add(templateData1);
			templateDatat.add(templateData2);
			templateDatat.add(templateData3);
			templateDatat.add(templateData4);

			sendTempMsg.setDatalist(templateDatat);
			String url = "http://" + TemplateIdUtil.serverName + "/"
					+ "ShangYiJia/toWxPayAuth.action";
			String state = order.getOrderNo();
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
					+ WeiXinConfig.appId + "&redirect_uri=" + url
					+ "&response_type=code&scope=snsapi_base&state=" + state
					+ "#wechat_redirect";
			sendTempMsg.setUrl(url);
			SendTempUtil.sendMsg(sendTempMsg, orderDao);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public List<Hospital> queryHospital(String proviceName, Integer page,
			Integer row) {
		// TODO Auto-generated method stub
		return null;
	}

}
