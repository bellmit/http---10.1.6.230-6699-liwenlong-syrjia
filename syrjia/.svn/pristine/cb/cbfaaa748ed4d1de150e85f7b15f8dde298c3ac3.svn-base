package cn.syrjia.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.PatientDataDao;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

/**
 * 患者信息dao实现
 * 
 * @pdOid 672fc366-869e-4c91-b4b8-72b9f0e8e2b3
 */
@Repository("patientDataDao")
public class PatientDataDaoDaoImpl extends BaseDaoImpl implements
		PatientDataDao {

	// 日志
	private Logger logger = LogManager.getLogger(PatientDataDaoDaoImpl.class);

	/**
	 * 查询患者信息
	 */
	@Override
	public List<Map<String, Object>> queryPatientList(String userId) {
		String sql = "select * from t_patient_data where state=1 and memberId=? order by isDefaultPer desc,createTime desc";
		List<Map<String, Object>> addresss = new ArrayList<Map<String, Object>>();
		try {
			//执行查询
			addresss = jdbcTemplate.queryForList(sql, new Object[] { userId });
		} catch (DataAccessException e) {
			logger.warn(e);
			throw e;
		}
		return addresss;
	}

	/**
	 * 删除患者信息（假删）
	 */
	@Override
	public Integer deletePatient(String id) {
		String sql = "update t_patient_data set state=2 where id=?";
		Integer i = 0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 修改默认患者
	 */
	@Override
	public Integer updatePatientIsDefault(String userId) {
		String sql = "update t_patient_data set isDefaultPer=0 where memberId=?";
		Integer i = 0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql, new Object[] { userId });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 获取默认患者信息
	 */
	@Override
	public Map<String, Object> queryPatientByDefault(String userId) {
		String sql = "select * from t_patient_data where state=1 and isDefaultPer=1 and memberId=?";
		try {
			//执行查询
			return jdbcTemplate.queryForMap(sql, new Object[] { userId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 获取默认患者关系列表
	 */
	@Override
	public List<Map<String, Object>> queryPatientNexusList() {
		String sql = "select nexusName text from t_patient_nexus where state=1 order by createTime desc ";
		List<Map<String, Object>> addresss = new ArrayList<Map<String, Object>>();
		try {
			//执行查询
			addresss = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			throw e;
		}
		return addresss;
	}
	
	/**
	 * 设置就默认就诊人
	 */
	@Override
	public Integer defPatient(String id) {
		String idSql = " SELECT id FROM t_patient_data tpd1 WHERE tpd1.memberId IN (SELECT tpd2.memberId FROM t_patient_data tpd2 WHERE tpd2.id='"+id+"') AND tpd1.isDefaultPer='1'  ";
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		try {
			//执行查询
			listMaps = jdbcTemplate.queryForList(idSql);
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		//拼接更新sql
		String sql = "update t_patient_data set isDefaultPer=1 where id=?";
		if(listMaps!=null &&listMaps.size()>0){
			String ids = "";
			for(int i=0; i<listMaps.size(); i++){
				if(listMaps.get(i).get("id")!=null){
					ids+="'"+listMaps.get(i).get("id")+"',";
				}
			}
			if(!StringUtils.isEmpty(ids)){
				sql +=";update t_patient_data set isDefaultPer=0 where id in ("+ids.substring(0, ids.length()-1)+")";
			}
		}
		Integer i = 0;
		try {
			//更新
			i = jdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询已有就诊人个数
	 */
	@Override
	public Integer queryPatientCount(String memberId,Integer state) {
		Integer i = 0;
		try {
			String sql = "SELECT count(1) from t_patient_data p WHERE 1=1 ";
			if(!StringUtil.isEmpty(state)){
				sql +=" and p.state=1  ";
			}
			sql +=" and p.memberId=? ";
			if(!StringUtils.isEmpty(memberId)){
				//执行查询
				i = super.queryBysqlCount(sql, new Object[]{memberId});
			}
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询同步呼叫中心数据
	 */
	@Override
	public Map<String, Object> querySendCallCenterData(String id) {
		Map<String, Object> map = null;
		try {
			if(!StringUtils.isEmpty(id)){
				String sql = "SELECT p.id,p.memberId,p.`name`,p.phone,p.nexus,p.state,p.age,p.isDefaultPer,p.createTime "
						+" from t_patient_data p where p.id='"+id+"'";
				//执行查询
				map = jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return map;
	}
	
}