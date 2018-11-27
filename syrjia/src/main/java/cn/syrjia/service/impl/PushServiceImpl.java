package cn.syrjia.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.entity.SendMode;
import cn.syrjia.service.PushService;
import cn.syrjia.util.BatchPublishSMSMessageDemo;
import cn.syrjia.util.SMSTemplateIdUtil;
import cn.syrjia.util.SendModel;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Service("pushService")
public class PushServiceImpl extends BaseServiceImpl implements PushService {

	private static Logger logger = LogManager.getLogger(PushServiceImpl.class);

	@Resource(name = "orderDao")
	OrderDao orderDao;
	
	@Resource(name = "doctorDao")
	DoctorDao doctorDao;
	
	/**
	 * 查询医生设置
	 */
	@Override
	public Map<String,Object> queryDoctorSet(String doctorId){
		return doctorDao.queryDocotrSetById(doctorId);
	}

	/**
	 *  app推送加入消息列表
	 * @param sendMap
	 */
	private void addPush(Map<String, Object> sendMap) {
		if (null != sendMap) {
			SendMode push = new SendMode();
			//推送消息内容
			push.setTitle(sendMap.get("title") == null ? null : sendMap.get(
					"title").toString());
			push.setContent(sendMap.get("content") == null ? null : sendMap
					.get("content").toString());
			push.setUserId(sendMap.get("userId") == null ? null : sendMap.get(
					"userId").toString());
			push.setType(sendMap.get("type") == null ? null : sendMap.get(
					"type").toString());
			push.setData(sendMap.get("data") == null ? null : sendMap.get(
					"data").toString());
			push.setToken(sendMap.get("token") == null ? null : sendMap.get(
					"token").toString());
			push.setType(sendMap.get("ostype") == null ? null : sendMap.get(
					"ostype").toString());
			push.setModel(sendMap.get("model") == null ? null : sendMap.get(
					"model").toString());
			push.setVersion(sendMap.get("version") == null ? null : sendMap.get(
					"version").toString());
			push.setRelevantUserId(sendMap.get("relevantUserId") == null ? null : sendMap.get(
					"relevantUserId").toString());
			push.setCreationTime(Util.queryNowTime()+"");
			//添加
			orderDao.addEntityUUID(push);
		}
	}

	/**
	 * 用户预约申请提交
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public String yyApplySuccess(Map<String, Object> map) {
		if (map != null) {

			String param = "{\"time\":\""
					+ Util.timeStamp2Date(map.get("createtime").toString())
					+ "\",\"typeName\":\"" + map.get("typeName") + "\"}";
			try {
				BatchPublishSMSMessageDemo.opera(map.get("phone").toString(),
						param, SMSTemplateIdUtil.yyApplySuccess_SMS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 微信推送
			if (map.get("openid") != null
					&& !StringUtils.isEmpty(map.get("openid").toString())) {
				SendModel.yyApplySuccess_Weixin(map, orderDao);
			}

			if (map.get("token") != null
					&& !StringUtils.isEmpty(map.get("token").toString())) {
				// app推送
				Map<String, Object> sendMap = SendModel.yyApplySuccess_App(map);// app推送消息
				// app推送加入消息列表
				addPush(sendMap);
			}
		}
		return "success";
	}

	/**
	 * 退款申请通知
	 */
	@Override
	public void sendApplyRefundMsg(Map<String, Object> map) {
		try {
			if (map != null) {
				//拼接通知内容
				if ("1".equals(map.get("sendMember"))) {
					String param = "{\"name\":\""
							+ map.get("name")
							+ "\",\"orderNo\":\""
							+ map.get("orderNo")
							+ "\",\"price\":\""
							+ map.get("money")
							+ "\",\"applyTime\":\""
							+ Util.timeStamp2Date(map.get("createtime")
									.toString()) + "\",\"applyReason\":\""
							+ map.get("applyReason").toString() + "\"}";
					BatchPublishSMSMessageDemo.opera(map.get("loginname")
							.toString(), param,
							SMSTemplateIdUtil.refund_USER_SMS);
				} else {
					//拼接通知内容
					String param = "{\"name\":\""
							+ map.get("operaName")
							+ "\",\"orderNo\":\""
							+ map.get("orderNo")
							+ "\",\"applyName\":\""
							+ map.get("name")
							+ "\",\"price\":\""
							+ map.get("money")
							+ "\",\"applyTime\":\""
							+ Util.timeStamp2Date(map.get("createtime")
									.toString()) + "\",\"applyReason\":\""
							+ map.get("applyReason").toString() + "\"}";
					BatchPublishSMSMessageDemo.opera(map.get("loginname")
							.toString(), param, SMSTemplateIdUtil.refund_SMS);
				}

				// 微信推送
				if (map.get("openid") != null
						&& !StringUtils.isEmpty(map.get("openid").toString())) {
					SendModel.ApplyRefundMoney_Weixin(map, orderDao);
				}

				if (map.get("token") != null
						&& !StringUtil.isEmpty(map.get("token"))) {
					// app推送
					Map<String, Object> sendMap = SendModel
							.ApplyRefundMoney_App(map);// app推送消息
					addPush(sendMap);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 订单支付成功通知
	 */
	@Override
	public void sendPayOrderSuccessMsg(Map<String, Object> map) {
		try {
			if (map != null) {
				String param = "{\"name\":\"" + map.get("realname")
						+ "\",\"orderNo\":\"" + map.get("orderNo")
						+ "\",\"price\":\"" + map.get("receiptsPrice")
						+ "\",\"goodDetails\":\"" + map.get("remarks") + "\"}";
				/*BatchPublishSMSMessageDemo.opera(map.get("loginname")
						.toString(), param, SMSTemplateIdUtil.paySuccess_SMS);*/
				// 微信推送
				if (map.get("openid") != null
						&& !StringUtils.isEmpty(map.get("openid").toString())) {
					SendModel.PayOrderSuccess_Weixin(map, orderDao);
				}

				if (map.get("token") != null
						&& !StringUtils.isEmpty(map.get("token").toString())) {
					// app推送
					Map<String, Object> sendMap = SendModel
							.PayOrderSuccess_App(map);// app推送消息
					addPush(sendMap);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 服务待处理通知
	 */
	@Override
	public void serverNoFinish(Map<String, Object> map) {
		try {
			if (map != null) {
				String param = "{\"name\":\"" + map.get("operaName")
						+ "\",\"count\":\"" + map.get("noFinishCount")
						+ "\",\"typeName\":\"" + map.get("typeName")
						+ "\",\"time\":\"" + map.get("createTime")
						+ "\",\"endDate\":\"" + map.get("endDate")
						+ "\",\"orderNos\":\"" + map.get("orderNos") + "\"}";
			/*	BatchPublishSMSMessageDemo.opera(map.get("loginname")
						.toString(), param,
						SMSTemplateIdUtil.serverNoFinish_SMS);*/
				// 微信推送
				if (map.get("openid") != null
						&& !StringUtils.isEmpty(map.get("openid").toString())) {
					SendModel.PayOrderSuccess_Weixin(map, orderDao);
				}

				if (map.get("token") != null
						&& !StringUtils.isEmpty(map.get("token").toString())) {
					// app推送
					Map<String, Object> sendMap = SendModel
							.PayOrderSuccess_App(map);// app推送消息
					addPush(sendMap);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 医生成功绑定医生助理
	 */
	@Override
	public String bindsuc(String srId,String doctroId,String docName) {
		try {
				/*String param = "{\"name\":\"" + map.get("realname")
						+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
						+ "\",\"departName\":\"" + map.get("departName")
						+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
								: map.get("positionName") + "\",\"ghDate\":\""
								+ map.get("ghDate") + "\"}";
				BatchPublishSMSMessageDemo.opera(map.get("loginname")
						.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
				if (!StringUtils.isEmpty(doctroId)) {
					// app推送
					Map<String, Object> sendMap = SendModel.bindsuc_app(srId, doctroId, docName,this);
						// app推送加入消息列表
						//addPush(sendMap);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 认证推送
	 */
	@Override
	public String authassist(String srId, String doctroId, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.authassist_app(srId, doctroId, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}

	/**
	 * 助理调理服务通知
	 */
	@Override
	public String paysuczxtl(String srId, String doctroId, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.paysuczxtl_app(srId, doctroId, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	/**
	 * 医生调理服务通知
	 */
	@Override
	public String docpaysuczxtl(String doctroId,String patientId,String patientName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docpaysuczxtl_app(doctroId,patientId,patientName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生电话调理服务订单
	 */
	@Override
	public String docpaysucdhtl(String doctroId,String patientId,String patientName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docpaysucdhtl_app(doctroId,patientId,patientName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生图文咨询服务
	 */
	@Override
	public String docpaysuctwzx(String doctroId,String patientId,String patientName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docpaysuctwzx_app(doctroId,patientId,patientName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生电话咨询服务
	 */
	@Override
	public String docpaysucdhzx(String doctroId,String patientId,String patientName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docpaysucdhzx_app(doctroId,patientId,patientName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生 填写完问症状描述
	 */
	@Override
	public String docfinishzzms(String doctroId,String patientId,String patientName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docfinishzzms_app(doctroId,patientId,patientName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生 填写完问问诊单
	 */
	@Override
	public String docfinishwzd(String doctroId,String patientId,String patientName,String name) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docfinishwzd_app(doctroId,patientId,patientName,name,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生 评论
	 */
	@Override
	public String docreceivepj(String doctroId,String orderNo) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docreceivepj_app(doctroId,orderNo,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 支付推送
	 */
	@Override
	public String paysucdhtl(String srId, String doctroId, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.paysucdhtl_app(srId, doctroId, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	/**
	 * 支付推送
	 */
	@Override
	public String paysuctwzx(String srId, String doctroId, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.paysuctwzx_app(srId, doctroId, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	/**
	 * 电话咨询服务推送
	 */
	@Override
	public String paysucdhzx(String srId, String doctroId, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.paysucdhzx_app(srId, doctroId, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	/**
	 * 调理订单推送
	 */
	@Override
	public String unpayedorder(String srId, String doctroId,String orderNo,String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.unpayedorder_app(srId, doctroId,orderNo, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 调理订单推送
	 */
	@Override
	public String docprescorder(String srId, String doctroId,String orderNo, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctroId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docprescorder_app(srId, doctroId,orderNo, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * docdh推送
	 */
	@Override
	public String docdhservice(String srId, String doctorId, String docName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctorId)) {
				// app推送
				//Map<String, Object> sendMap = SendModel.docprescorder_app(srId, doctorId,orderNo, docName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 调理订单推送
	 */
	@Override
	public String doctwzxservice(String srId,String realName,String docName,String orderNo,String doctorId) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(srId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.doctwzxservice_app(srId, realName,docName,orderNo,doctorId,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 调理订单推送
	 */
	@Override
	public String doctwtlservice(String srId,String realName,String docName,String orderNo,String doctorId) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(srId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.doctwtlservice_app(srId, realName,docName,orderNo,doctorId,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 推送
	 */
	@Override
	public String docunreplytwtl(String srId,String realName,String docName,String orderNo,String doctorId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 推送
	 */
	@Override
	public String docunreplytwzx(String srId,String realName,String docName,String orderNo,String doctorId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 支付通知
	 */
	@Override
	public String docpaytlsuc(String doctorId,String realName,String orderNo) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctorId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docpaytlsuc_app(doctorId,realName,orderNo,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医生助理推送
	 */
	@Override
	public String docbindsuc(String srId,String doctorId, String srName) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctorId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docbindsuc_app(srId,doctorId,srName,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 抄方申请通知
	 */
	@Override
	public String docCf(String doctorId,String patientId) {
		try {
			/*String param = "{\"name\":\"" + map.get("realname")
					+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
					+ "\",\"departName\":\"" + map.get("departName")
					+ "\",\"positionName\":\"" + map.get("positionName") == null ? "未填写"
							: map.get("positionName") + "\",\"ghDate\":\""
							+ map.get("ghDate") + "\"}";
			BatchPublishSMSMessageDemo.opera(map.get("loginname")
					.toString(), param, SMSTemplateIdUtil.ghYqrSuccess_SMS);*/
			if (!StringUtils.isEmpty(doctorId)) {
				// app推送
				Map<String, Object> sendMap = SendModel.docCf_app(doctorId,patientId,this);
				// app推送加入消息列表
				//addPush(sendMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 评论发送给医生
	 */
	@Override
	public String docmcomment(String doctorId) {
		try {
			/*Map<String, Object> sendMap = new HashMap<String,Object>();
				String name = map.get("name")+" "+map.get("noticeTopDetail");
				String param = "{\"name\":\"" + name
						+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
						+ "\",\"departName\":\"" + map.get("departName")
						+ "\",\"positionName\":\"" + map.get("positionName")
						+ "\",\"hzTime\":\"" + map.get("ghDate") + "\"}";
				SMSMessage.opera(map.get("loginname").toString(), param,
						SMSTemplateIdUtil.jzAlert_SMS);
			 */
			/*// 微信推送
				if (map.get("openid") != null
						&& !StringUtils.isEmpty(map.get("openid").toString())) {
					sendMap = SendModel.JzAlertNotice_Weixin(map, memberDao);
				}*/
			SendModel.docmcomment_app(doctorId,this);
		} catch (Exception e) {
			logger.error(e);
		}
		return "success";
	}
	
	/**
	 * 评论发送给助理
	 */
	@Override
	public String zlcommentreply(String doctorId) {
		try {
			/*Map<String, Object> sendMap = new HashMap<String,Object>();
				String name = map.get("name")+" "+map.get("noticeTopDetail");
				String param = "{\"name\":\"" + name
						+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
						+ "\",\"departName\":\"" + map.get("departName")
						+ "\",\"positionName\":\"" + map.get("positionName")
						+ "\",\"hzTime\":\"" + map.get("ghDate") + "\"}";
				SMSMessage.opera(map.get("loginname").toString(), param,
						SMSTemplateIdUtil.jzAlert_SMS);
			 */
			/*// 微信推送
				if (map.get("openid") != null
						&& !StringUtils.isEmpty(map.get("openid").toString())) {
					sendMap = SendModel.JzAlertNotice_Weixin(map, memberDao);
				}*/
			SendModel.zlcommentreply_app(doctorId,this);
		} catch (Exception e) {
			logger.error(e);
		}
		return "success";
	}
	
	/**
	 * 购买调理电话通知
	 */
	@Override
	public String doctlservice(String doctorId,String patientId,String patientName,String date,String serverName){
		try {
			/*Map<String, Object> sendMap = new HashMap<String,Object>();
				String name = map.get("name")+" "+map.get("noticeTopDetail");
				String param = "{\"name\":\"" + name
						+ "\",\"infirmaryName\":\"" + map.get("infirmaryName")
						+ "\",\"departName\":\"" + map.get("departName")
						+ "\",\"positionName\":\"" + map.get("positionName")
						+ "\",\"hzTime\":\"" + map.get("ghDate") + "\"}";
				SMSMessage.opera(map.get("loginname").toString(), param,
						SMSTemplateIdUtil.jzAlert_SMS);
			 */
			/*// 微信推送
				if (map.get("openid") != null
						&& !StringUtils.isEmpty(map.get("openid").toString())) {
					sendMap = SendModel.JzAlertNotice_Weixin(map, memberDao);
				}*/
			SendModel.doctlservice_app(doctorId,patientId,patientName,date,serverName,this);
		} catch (Exception e) {
			logger.error(e);
		}
		return "success";
	}
	
}
