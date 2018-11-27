package cn.syrjia.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.ScoreConsumeDao;
import cn.syrjia.util.StringUtil;

@Repository("scoreConsumeDao")
public class ScoreConsumeDaoImpl extends BaseDaoImpl implements ScoreConsumeDao {

	// 日志
	private Logger logger = LogManager.getLogger(ScoreConsumeDaoImpl.class);

	/**
	 * 查询今天是否已签到
	 */
	@Override
	public Integer queryTodayIsClick(String userId) {
		String sql = "SELECT count(1) from t_score_consume where userid=? and TO_DAYS(FROM_UNIXTIME(createtime))=TO_DAYS(NOW()) and orderNo='1'";
		Integer i = 0;
		try {
			if(!StringUtil.isEmpty(userId)){
				//执行查询
				i = super.queryBysqlCount(sql, new Object[]{userId});
			}
		} catch (DataAccessException e) {
			logger.warn(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询本月签到列表
	 */
	@Override
	public List<Map<String, Object>> queryClicks(String userId) {
		String sql="SELECT FROM_UNIXTIME(createtime,'%d') date,consumeScore from t_score_consume where userid=? and FROM_UNIXTIME(createtime,'%Y%m')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y%m') and orderNo='1'";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql,new Object[]{userId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

}
