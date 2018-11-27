package cn.syrjia.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;

public interface PushService extends BaseServiceInterface{
	
	/**
	 * 查询医生设置
	 * @param doctorId
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorSet(String doctorId);
	
	/**
	 * 用户预约申请提交
	 * @param map
	 * @return
	 */
	public abstract String yyApplySuccess(Map<String,Object> map);
	
	/**
	 * 退款申请通知
	 * @param map
	 */
	public abstract void sendApplyRefundMsg(Map<String,Object> map);
	
	/**
	 * 订单支付成功通知
	 * @param map
	 */
	public abstract void sendPayOrderSuccessMsg(Map<String,Object> map);
	
	/**
	 * 服务待处理通知
	 * @param map
	 * @return
	 */
	public abstract void serverNoFinish(Map<String,Object> map);
	
	/**
	 * 医生成功绑定医生助理
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String bindsuc(String srId,String doctorId,String docName);
	
	/**
	 * 认证推送
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String authassist(String srId,String doctorId,String docName);
	
	/**
	 * 助理调理服务通知
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String paysuczxtl(String srId,String doctorId,String docName);
	
	/**
	 * 医生调理服务通知
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String docpaysuczxtl(String doctorId,String patientId,String patientName);
	
	/**
	 * 医生电话调理服务订单
	 * @param doctorId
	 * @param patientId
	 * @param patientName
	 * @return
	 */
	public abstract String docpaysucdhtl(String doctorId,String patientId,String patientName);
	
	/**
	 * 医生图文咨询服务
	 * @param doctorId
	 * @param patientId
	 * @param patientName
	 * @return
	 */
	public abstract String docpaysuctwzx(String doctorId,String patientId,String patientName);
	
	/**
	 * 医生电话咨询服务
	 * @param doctorId
	 * @param patientId
	 * @param patientName
	 * @return
	 */
	public abstract String docpaysucdhzx(String doctorId,String patientId,String patientName);
	
	/**
	 * 医生 填写完问症状描述
	 * @param doctorId
	 * @param patientId
	 * @param patientName
	 * @return
	 */
	public abstract String docfinishzzms(String doctorId,String patientId,String patientName);
	
	/**
	 * 医生 填写完问问诊单
	 * @param doctorId
	 * @param patientId
	 * @param patientName
	 * @return
	 */
	public abstract String docfinishwzd(String doctorId,String patientId,String patientName,String name);
	
	/**
	 * 医生 评论
	 * @param doctorId
	 * @param patientId
	 * @param patientName
	 * @return
	 */
	public abstract String docreceivepj(String doctorId,String orderNo);
	
	/**
	 * 评论发送给医生
	 * @param doctorId
	 * @param patientName
	 * @return
	 */
	public abstract String docmcomment(String doctorId);
	
	/**
	 * 评论发送给助理
	 * @param doctorId
	 * @param patientName
	 * @return
	 */
	public abstract String zlcommentreply(String srId);
	
	/**
	 * 购买调理电话通知
	 * @param srId
	 * @return
	 */
	public abstract String doctlservice(String doctorId,String patientId,String patientName,String serverName,String date);
	
	/**
	 * 支付推送
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String paysucdhtl(String srId,String doctorId,String docName);
	
	/**
	 * 支付推送
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String paysuctwzx(String srId,String doctorId,String docName);
	
	/**
	 * 电话咨询服务推送
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String paysucdhzx(String srId,String doctorId,String docName);
	
	/**
	 * docdh推送
	 * @param srId
	 * @param doctorId
	 * @param docName
	 * @return
	 */
	public abstract String docdhservice(String srId,String doctorId,String docName);
	
	/**
	 * 调理订单推送
	 * @param srId
	 * @param realName
	 * @param docName
	 * @param orderNo
	 * @param doctorId
	 * @return
	 */
	public abstract String doctwzxservice(String srId,String realName,String docName,String orderNo,String doctorId);
	
	/**
	 * 调理订单推送
	 * @param srId
	 * @param realName
	 * @param docName
	 * @param orderNo
	 * @param doctorId
	 * @return
	 */
	public abstract String doctwtlservice(String srId,String realName,String docName,String orderNo,String doctorId);
	
	/**
	 * 推送
	 * @param srId
	 * @param realName
	 * @param docName
	 * @param orderNo
	 * @param doctorId
	 * @return
	 */
	public abstract String docunreplytwtl(String srId,String realName,String docName,String orderNo,String doctorId);
	
	/**
	 * 推送
	 * @param srId
	 * @param realName
	 * @param docName
	 * @param orderNo
	 * @param doctorId
	 * @return
	 */
	public abstract String docunreplytwzx(String srId,String realName,String docName,String orderNo,String doctorId);
	
	/**
	 * 调理订单推送
	 * @param srId
	 * @param doctorId
	 * @param orderNo
	 * @param docName
	 * @return
	 */
	public abstract String unpayedorder(String srId,String doctorId,String orderNo,String docName);
	
	/**
	 * 调理订单推送
	 * @param srId
	 * @param doctorId
	 * @param orderNo
	 * @param docName
	 * @return
	 */
	public abstract String docprescorder(String srId,String doctorId,String orderNo,String docName);
	
	/**
	 * 支付通知
	 * @param doctorId
	 * @param realName
	 * @param orderNo
	 * @return
	 */
	public abstract String docpaytlsuc(String doctorId,String realName,String orderNo);
	
	/**
	 * 医生助理推送
	 * @param srId
	 * @param doctorId
	 * @param srName
	 * @return
	 */
	public abstract String docbindsuc(String srId,String doctorId,String srName);
	
	/**
	 * 抄方申请通知
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract String docCf(String doctorId,String patientId);
	
}
