package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.hospital.entity.Doctor;
import cn.syrjia.hospital.entity.SendMsgRecord;
import cn.syrjia.hospital.entity.Symptom;

public interface HospitalDao extends BaseDaoInterface {

	/**
	 * 根据医生ID查询医生接单数
	 * 
	 * @param docId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryOrderTaskByDocId(
			String docId, Integer page, Integer row);

	/**
	 * 查询用户详情
	 * @param openId
	 * @return
	 */
	public List<Map<String, Object>> queryUserInfoToSymtom(String openId);

	/**
	 * 查询该用户是否扫该医生进入
	 * 
	 * @param openId
	 * @return
	 */
	public abstract Map<String, Object> isScanThisDoc(String openId);

	/**
	 * 设置
	 * @param doc
	 * @return
	 */
	public abstract Integer onOrEndDoctor(Doctor doc);

	/**
	 * 通过订单号删除
	 * @param orderNo
	 * @return
	 */
	public abstract Integer delSymptomByOrderNo(String orderNo);

	/**
	 * 查询发送记录
	 * @param smr
	 * @return
	 */
	public abstract Map<String,Object> querySendRecord(SendMsgRecord smr);
	
	/**
	 * 更新医生订单
	 * @param orderNo
	 * @param sign
	 * @return
	 */
	public abstract Integer updateDocOrderSign(String orderNo, String sign);

	/**
	 * 添加症状描述
	 * 
	 * @param symptoms
	 * @return
	 */
	public abstract Object addSymptom(List<Symptom> symptoms);

}
