package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.hospital.entity.IllnessOrDiscomfortClass;

public interface DiagnoseDao extends BaseDaoInterface {

	/**查询我的医生**/
	public abstract List<Map<String, Object>> queryMyDoctor(Integer page, Integer row, String openId);

	/**根据关键字搜索医生***/
	public abstract List<Map<String, Object>> searchDoctor(Integer page, Integer row, String keyword);

	/**查询前几个常用不适**/
	public abstract List<IllnessOrDiscomfortClass> getTopIllClass();

	/**获取所有省份**/
	public abstract List<Map<String, Object>> queryAllProviceNames();

	/**根据省份获取下级城市**/
	public abstract List<Map<String, Object>> queryCitiesByProvice(String proviceId);

	/**获取用过的科室**/
	public abstract List<Map<String, Object>> queryUsedDepartments();

	/**医生筛选**/
	public abstract List<Map<String, Object>> filterDoctor(Integer page, Integer row, String cityId, String deptId,
			String postionId, String keyword, String sortType);

	
	/** 获取医生信息**/
	Map<String, Object> getDoctorInfo(String doctorId);
	
}
