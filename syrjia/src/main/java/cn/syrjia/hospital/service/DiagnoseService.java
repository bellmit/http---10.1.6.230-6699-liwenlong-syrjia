package cn.syrjia.hospital.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.hospital.entity.IllnessOrDiscomfortClass;

public interface DiagnoseService extends BaseServiceInterface {

	/**查询我的医生*/
	public abstract List<Map<String, Object>> queryMyDoctor( Integer page, Integer row, String openId);

	/**根据关键词搜索医生**/
	public abstract List<Map<String, Object>> searchDoctor(Integer page, Integer row, String keyword);

	/**获取前几个常用不适***/
	public abstract List<IllnessOrDiscomfortClass> getTopIllClass();

	/**获取所有省份**/
	public abstract List<Map<String, Object>> queryAllProviceNames();

	/**根据省份id获取城市**/
	public abstract List<Map<String, Object>> queryCitiesByProvice(String proviceId);

	/**获取用过的科室**/
	public abstract List<Map<String, Object>> queryUsedDepartments();

	/**筛选医生**/
	public abstract List<Map<String, Object>> filterDoctor(Integer page, Integer row, String cityId, String deptId,
			String postionId, String keyword, String sortType);
	
	/** 获取医生信息 **/
	Map<String, Object> getDoctorInfo(String doctorId);

}
