package cn.syrjia.hospital.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.HealthProDao;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("healthProDao")
public class HealthProDaoImpl extends BaseDaoImpl implements HealthProDao {

	private Logger logger = LogManager.getLogger(KnowledgeDaoImpl.class);

	/**
	 * 药品名录查询使用
	 */
	@Override
	public List<Map<String, Object>> queryHealthProducts(String yztId, String name, String type, Integer page,
			Integer row) {
		String sql = "SELECT thp.id,thp.healthProName name,dm.otherName,thp.healthProType type,thp.pinyinCode,thpc.typeName,thu.healthProUtilName unit,FORMAT(thp.price,4) price"
				+ " FROM  t_health_products thp inner JOIN t_health_products_class thpc ON thp.healthProType=thpc.id"
				+ " INNER JOIN t_drug_master dm on dm.id = thp.masterId "
				+ " inner JOIN t_healthpro_util thu ON thp.healthProUtil=thu.id where 1=1 AND thp.state=1 AND dm.state=1 ";
		// 名称
		if (!StringUtil.isEmpty(name)) {
			sql += " and (thp.pinyinCode like '%" + name + "%' or dm.otherName like '%" + name
					+ "%' or thp.healthProName like '%" + name + "%' )";
		}
		// 类型
		if (!StringUtil.isEmpty(type)) {
			sql += " AND thpc.id='" + type + "'";
		} else {
			sql += " AND thpc.id='1'";
		}
		if (!StringUtil.isEmpty(yztId)) {
			sql += " and thp.yztId='" + yztId + "' ";
		}
		sql += "  ORDER BY  thp.pinyinCode ASC";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 申请增加药味
	 */
	@Override
	public String applyAddDrug(String doctorId, String content) {
		if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(content)) {
			String id = Util.getUUID();
			String sql = "insert into t_apply_drug_record(id,content,state,applyUserId,createTime) values(?,?,?,?,?);";
			jdbcTemplate.update(sql, new Object[] { id, content, 2, doctorId, Util.queryNowTime() });
			return id;
		}
		return null;
	}

}
