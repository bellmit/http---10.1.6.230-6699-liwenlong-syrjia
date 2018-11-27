package cn.syrjia.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.PatientData;

/** 
 * 患者信息service接口
 */
public interface PatientDataService extends BaseServiceInterface {
	
   /** 
    * 添加患者信息
    * @param patient
    * @return
    */
   public abstract Map<String,Object> addPatientData(HttpServletRequest request,PatientData patient);
   
   /** 修改地址信息
    * 
    * @param patient
    * @return
    */
   public abstract Map<String,Object> updatePatientData(PatientData patient);
   
   /**
	 * 查询患者信息
	 * @param userId
	 * @return
	 */
   public abstract Map<String,Object> queryPatientList(String userId);

	/**
	 * 删除患者信息（假删）
	 */
   public abstract Map<String,Object> deletePatient(String id);

	/**
	 * 获取默认患者信息
	 * @param userId
	 * @return
	 */
	public abstract Map<String, Object> queryPatientByDefault(String userId);
	
	/**
	 * 获取默认患者关系列表
	 * @param userId
	 * @return
	 */
	public abstract Map<String,Object> queryPatientNexusList();
	
	/**
	 * 设置默认就诊人
	 */
   public abstract Map<String,Object> defPatient(String id);
   
   /**
	 *查询已有就诊人个数
	 * @param memberId
	 * @return
	 */
	public abstract Map<String,Object> queryPatientCount(HttpServletRequest request,String memberId);

}