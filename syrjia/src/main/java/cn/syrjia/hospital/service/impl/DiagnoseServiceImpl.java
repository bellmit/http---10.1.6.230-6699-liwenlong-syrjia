package cn.syrjia.hospital.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.hospital.dao.DiagnoseDao;
import cn.syrjia.hospital.entity.IllnessOrDiscomfortClass;
import cn.syrjia.hospital.service.DiagnoseService;

@Service("diagnoseService")
public class DiagnoseServiceImpl extends BaseServiceImpl implements DiagnoseService {

	@Resource(name="diagnoseDao")
	DiagnoseDao diagnoseDao;
	
	/**查询常用疾病****/
	@Override
	public List<IllnessOrDiscomfortClass> getTopIllClass() {
		return diagnoseDao.getTopIllClass();
	}


	@Override
	public List<Map<String, Object>> queryMyDoctor( Integer page, Integer row, String openId) {
		return diagnoseDao.queryMyDoctor( page, row, openId);
	}


	@Override
	public List<Map<String, Object>> searchDoctor(Integer page, Integer row, String keyword) {
		return diagnoseDao.searchDoctor( page, row, keyword);
	}

	
	/**获取所有省份**/
	@Override
	public List<Map<String, Object>> queryAllProviceNames() {
		return diagnoseDao.queryAllProviceNames();
	}

	/**查询某个省份下的城市**/
	@Override
	public List<Map<String, Object>> queryCitiesByProvice(String proviceId) {
		return diagnoseDao.queryCitiesByProvice(proviceId);
	}
	
	@Override
	public List<Map<String, Object>> queryUsedDepartments() {
		
		return diagnoseDao.queryUsedDepartments();
	}

	@Override
	public List<Map<String, Object>> filterDoctor(Integer page, Integer row, String cityId, String deptId,
			String postionId, String keyword, String sortType) {
		
		return diagnoseDao.filterDoctor( page, row, cityId,deptId,postionId,keyword,sortType);
		
	}


	
	/** 获取医生信息 **/
	@Override
	public Map<String, Object> getDoctorInfo(String doctorId) {
		return diagnoseDao.getDoctorInfo(doctorId);
	}

}
