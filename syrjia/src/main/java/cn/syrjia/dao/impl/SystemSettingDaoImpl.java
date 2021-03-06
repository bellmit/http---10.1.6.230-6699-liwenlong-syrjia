package cn.syrjia.dao.impl;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.SystemSettingDao;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("systemSettingDao")
public class SystemSettingDaoImpl extends BaseDaoImpl implements SystemSettingDao {

	private static final Logger logger = LogManager.getLogger(SystemSettingDaoImpl.class);

	/**
	 * 通过键查询值
	 *
	 * @param key
	 *            键
	 * @return 值。键不存在或键存在值不存在返回null
	 */
	@Override
	public String getValueByKey(String key, String yztId) {
		String value = null;
		try {
			String sql = "select `value` from t_system_setting where `key`=?";
			if (!StringUtils.isEmpty(yztId)) {
				sql += " and uuid='" + yztId + "' ";
			}
			value = jdbcTemplate.queryForObject(sql, new Object[] { key }, String.class);
		} catch (Exception e) {
			System.out.println(e);
		}

		return StringUtils.isBlank(value) ? null : value;
	}

	/**
	 * 通过key更新value
	 */
	@Override
	public int updateValueByKey(String key, String value) {
		if (StringUtils.isBlank(key)) {
			logger.warn("系统设置中：更新系统设置时，key为空。");
			return 0;
		}

		String sql = "update t_system_setting set `value`=? where `key`=?";
		return super.update(sql, new Object[] { value, key });
	}

}
