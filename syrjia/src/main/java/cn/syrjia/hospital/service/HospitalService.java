package cn.syrjia.hospital.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.hospital.entity.DoctorReply;
import cn.syrjia.hospital.entity.Hospital;
import cn.syrjia.hospital.entity.Symptom;

public interface HospitalService extends BaseServiceInterface {
	
	/**
	 * 添加问诊人员症状描述
	 * @param symptoms
	 * @return
	 */
	public abstract Object addSymptom(HttpServletRequest request,List<Symptom> symptoms,String orderNo,String test);
	/**
	 * 修改症状描述
	 * @param request
	 * @param symptom
	 * @return
	 */
	public abstract Integer updateSymptom(HttpServletRequest request,List<Symptom> symBean);
	
	
	/**
	 *添加回复
	 */
	public abstract DoctorReply addDoctorReply(DoctorReply doctorReply);
	/**
	 * 会员向医生发送请求
	 * @return
	 */
	public abstract Map<String,Object> sendMsgToDoc(HttpServletRequest request,String docId,String openId,Integer isDoc,Map<String,Object> map);
	/**
	 * 获取医馆头信息
	 * @return
	 */
	public abstract List<Hospital> queryHospital(String proviceName,Integer page,Integer row);
	/**
	 * 下载图片
	 * @return
	 */
	public abstract List<Map<String,String>> returnMap(HttpServletRequest request,String[] serverIds);

	/**
	 * 发送通知
	 * @param request
	 * @param docId
	 * @param openId
	 * @param isDoc
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> sendCoutom(HttpServletRequest request,String fromUser,String toUser,Integer isDoc,String orderNo);
	
	/**
	 * 根据医生ID查询医生接单数
	 * @param docId
	 * @return
	 */
	public abstract List<Map<String,Object>> queryOrderTaskByDocId(String docId,Integer page,Integer row);

	/**
	 * 查询系统设置
	 * @param openId
	 * @return
	 */
	public abstract Map<String,Object> queryUserInfoToSymtom(String openId);
	
}
