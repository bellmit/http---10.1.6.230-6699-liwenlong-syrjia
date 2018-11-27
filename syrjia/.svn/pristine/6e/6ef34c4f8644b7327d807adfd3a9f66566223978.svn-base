package cn.syrjia.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.AnswerDao;
import cn.syrjia.entity.Answer;

/** 疑难答疑dao实现
 * 
 * @pdOid 19c6b439-48fc-4cc5-b390-5fe1b817ad43 */
@Repository("answerDao")
public class AnswerDaoImpl extends BaseDaoImpl implements AnswerDao {

	// 日志
	private Logger logger = LogManager.getLogger(AnswerDaoImpl.class);
	
	 /** 查询疑难答疑列表
	    * 
	    * @param answer 疑难答疑实体
	    * @param row 行数
	    * @param page 页数
	    * @pdOid c443b07e-24da-4528-a7c5-0b8b59978fdd */
	@Override
	public List<Map<String,Object>> queryAnswerList(String answerType, Integer row, Integer page) {
		String sql="select id,title,answer from t_answer where 1=1 and state= 1 and answerType=? order by rank desc";
		try {
			return this.jdbcTemplate.queryForList(sql,new Object[]{answerType});
		} catch (DataAccessException e) {
			logger.warn(e);
			throw e;
		}
	}

	/**
   	 * 疑难答疑类型列表
   	 */
	@Override
	public List<Map<String, Object>> queryAnswerTypeList(Integer answerClass) {
		String sql="select at.id,at.name from t_answer_type at where at.state=1 GROUP BY at.id order by at.rank desc";
		List<Map<String, Object>> list=null;
		try {
			//list = jdbcTemplate.queryForList(sql,new Object[]{null==answerClass?2:answerClass});
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return list;
	}
}