package cn.syrjia.hospital.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.ClinicDao;

@Repository("clinicDao")
public class ClinicDaoImpl extends BaseDaoImpl implements ClinicDao {

	/**
	 * 根据医馆名称查询主键
	 */
	@Override
	public Object queryIdByName(String name) {
		String sql = "select hospitalId from h_hospital where hospitalName=? LIMIT 0,1";
		return super.jdbcTemplate.queryForObject(sql, new Object[]{name},String.class);
	}

	/**
	 * 获取医馆下科室数量和医生数量
	 */
	@Override
	public Map<String, Object> queryDepNumAndDocNum(String hosId) {
		String sql = "SELECT h.hospitalId,count(d.doctorId) AS docNum,count(DISTINCT m.departId) AS depNum"
					+" FROM h_hospital h left JOIN h_middle_util m ON h.hospitalId = m.hospitalId"
					+" left  JOIN h_doctor d ON m.departId = d.departId AND d.docIsOn = 1 AND d.docStatus='10' AND m.hospitalId=d.hospitalId WHERE m.type='0' and  h.hospitalId =?" 
					+" GROUP BY h.hospitalId";
		Map<String,Object> map =super.queryBysqlMap(sql, new Object[]{hosId});
		return map;
	}

	/**
	 * 查询医馆下的科室列表
	 */
	@Override
	public List<Map<String, Object>> queryDepList(String hosId) {
		String sql = "SELECT h.hospitalId,p.departId,p.departName,count(d.doctorId) as docNum"
					+" FROM h_hospital h LEFT JOIN h_middle_util m ON h.hospitalId = m.hospitalId"
					+" INNER JOIN h_department p ON m.departId=p.departId LEFT JOIN h_doctor d ON p.departId = d.departId AND d.docIsOn = 1 AND d.docStatus = '10' AND m.hospitalId=d.hospitalId"
					+" where h.hospitalId=? GROUP BY departName "; 
		List<Map<String, Object>> list = super.queryBysqlList(sql, new Object[]{hosId});
		return list;
	}

}
