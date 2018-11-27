package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;

/**
 * 患者信息dao接口
 * 
 * @pdOid b602396f-9ec1-4be2-aea3-8a116e9833af
 */
public interface PatientDataDao extends BaseDaoInterface {

	/**
	 * 查询患者信息
	 * @param userId
	 * @return
	 */
	public abstract List<Map<String, Object>> queryPatientList(String userId);

	/**
	 * 删除患者信息（假删）
	 */
	public abstract Integer deletePatient(String id);

	/**
	 * 修改默认患者
	 * @param userId
	 * @return
	 */
	public abstract Integer updatePatientIsDefault(String userId);

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
	public abstract List<Map<String, Object>> queryPatientNexusList();
	
	/**
	 * 设置就默认就诊人
	 */
	public abstract Integer defPatient(String id);
	
	/**
	 *查询已有就诊人个数
	 * @param memberId
	 * @return
	 */
	public abstract Integer queryPatientCount(String memberId,Integer state);
	
	/**
	 * 查询同步呼叫中心数据
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> querySendCallCenterData(String id);
}