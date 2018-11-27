package cn.syrjia.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.EvaluateDao;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.util.StringUtil;

@Repository("evaluateDao")
public class EvaluateDaoImpl extends BaseDaoImpl implements EvaluateDao {

	// 日志
	private Logger logger = LogManager.getLogger(EvaluateDaoImpl.class);

	/**
	 * 查询评价标签
	 */
	@Override
	public List<Map<String, Object>> queryEvalabels(Integer type) {
		String sql = "select evaLabelId id,evaLabelName name from t_evalabel where type=? and state=1 ";
		List<Map<String, Object>> list = null;
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql, new Object[] { type });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询评价列表
	 * @param request
	 * @param evaluate
	 * @param memberId
	 * @param page
	 * @param row
	 * @param level
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryEvaluateList(Evaluate evaluate,
			Integer level, Integer page, Integer row) {
		//拼接sql
		String sql = "SELECT * FROM (select e.`explain`,e.id,e.state,e.goodsId,FROM_UNIXTIME(e.createTime,'%Y-%m-%d %H:%i') time,e.evaluate_note,e.evaluateLevel,IFNULL(m.photo,d.docUrl) photo,IFNULL(m.realname,d.docName) realName,(SELECT GROUP_CONCAT(p.picPathUrl) FROM t_piclib p WHERE p.goodId=e.id) pic,(SELECT GROUP_CONCAT(ee.evalableName) FROM t_evaluate_evalabel ee WHERE ee.evaluateId=e.id)evalName,GROUP_CONCAT(gds.NAME separator ' ') specifications from t_evaluate e LEFT join t_member m on m.id=e.memberId LEFT JOIN t_doctor d ON d.doctorId=e.memberId INNER JOIN t_order o ON o.orderNo=e.orderNo INNER JOIN t_order_detail od ON od.id=e.orderDetailId INNER JOIN t_goods g ON g.id=od.goodsId INNER JOIN t_goods_pricenum gp ON gp.id = od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id = gd.specificationsId where e.state=1  GROUP BY e.id "
				+ " UNION ALL"
				+ " select e.`explain`,e.id,e.state,e.goodsId,FROM_UNIXTIME(e.createTime,'%Y-%m-%d %H:%i') time,e.evaluate_note,e.evaluateLevel,IFNULL(m.photo,d.docUrl) photo,IFNULL(m.realname,d.docName) realName,(SELECT GROUP_CONCAT(p.picPathUrl) FROM t_piclib p WHERE p.goodId=e.id) pic,(SELECT GROUP_CONCAT(ee.evalableName) FROM t_evaluate_evalabel ee WHERE ee.evaluateId=e.id)evalName,GROUP_CONCAT(gds.NAME separator ' ') specifications from t_evaluate e LEFT join t_member m on m.id=e.memberId LEFT JOIN t_doctor d ON d.doctorId=e.memberId  INNER JOIN t_order o ON o.orderNo=e.orderNo INNER JOIN t_order_detail od ON od.id=e.orderDetailId INNER JOIN t_goods g ON g.id=od.goodsId INNER JOIN t_goods_pricenum gp ON gp.id = od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id = gd.specificationsId where e.state=2  AND e.memberId=? GROUP BY e.id) f WHERE f.id IS NOT NULL ";
		if (!StringUtil.isEmpty(evaluate.getGoodsId())) {
			sql += " AND f.goodsId='" + evaluate.getGoodsId() + "' ";
		}
		//级别
		if (!StringUtil.isEmpty(level)) {
			if (level == 1) {
				sql += " AND f.evaluateLevel>=4 ";
			} else if (level == 2) {
				sql += " AND f.evaluateLevel>=2 AND f.evaluateLevel<=3 ";
			} else if (level == 3) {
				sql += " AND f.evaluateLevel<=1 ";
			}
		}
		//分组，排序
		sql += "  GROUP BY f.id order by f.time desc ";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (null != page && null != row) {
				sql += " limit " + (page - 1) * row + "," + row;
			}
			//执行查询
			list = jdbcTemplate.queryForList(sql,
					new Object[] {
							evaluate.getMemberId() });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 改為好評
	 */
	@Override
	public Object updateHigh(Evaluate eva) {
		Object i = 0;
		String sql = "UPDATE t_evaluate set evaluateLevel='1' where id=?";
		if (eva.getId() != null) {
			//执行更新
			i = super.update(sql, new Object[] { eva.getId() });
		}
		return i;
	}

	/**
	 * 添加频率图片
	 * @param evaluateId
	 * @param picIds
	 * @return
	 */
	@Override
	public Integer updateEvaluatePic(final String evaluateId,
			final String[] picIds) {
		String sql = "update t_piclib set goodId=? where picId=?";
		//执行更新
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setString(1, evaluateId);
				ps.setString(2, picIds[i]);
			}

			@Override
			public int getBatchSize() {
				return picIds.length;
			}
		});
		return null;
	}

	/**
	 * 查询好评率
	 * @param evaluate
	 * @param evaluateLevel
	 * @return
	 */
	@Override
	public String queryEvaluateRate(Evaluate evaluate, Integer evaluateLevel) {
		String sql = "SELECT  ifnull(CONCAT(round(SUM(IF(e.evaluateLevel>=?,1,0))/ COUNT(0)*100), '%'),'100%') rate  FROM t_evaluate e inner join t_order o on o.orderNo=e.orderNo WHERE e.state=1 and goodsId=?";
		String rate = "100%";
		try {
			//执行查询
			rate = jdbcTemplate.queryForObject(sql, new Object[] {
					evaluateLevel, evaluate.getGoodsId() }, String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return rate;
	}

	/**
	 * 查询差评 中评 好评数
	 * @param evaluate
	 * @return
	 */
	@Override
	public Map<String, Object> queryEvaluateNum(Evaluate evaluate) {
		//拼接sql
		String sql = "SELECT  ifnull(CONCAT(round(SUM(IF(e.evaluateLevel>=4,1,0))/ COUNT(0)*100), '%'),'100%') num,'rate' type  FROM t_evaluate e inner join t_order o on o.orderNo=e.orderNo  WHERE e.state=1 and goodsId=? "
				+ " UNION "
				+ " SELECT if(0=COUNT(1),null,COUNT(1)) num,'hp' type FROM t_evaluate e inner join t_order o on o.orderNo=e.orderNo WHERE e.state=1 AND e.evaluateLevel>=4 and goodsId=? "
				+ " UNION  "
				+ " SELECT if(0=COUNT(1),null,COUNT(1)) num,'zp' type FROM t_evaluate e inner join t_order o on o.orderNo=e.orderNo  WHERE e.state=1 AND e.evaluateLevel>=2 AND e.evaluateLevel<=3 and goodsId=? "
				+ " UNION "
				+ " SELECT if(0=COUNT(1),null,COUNT(1)) num,'qb' type FROM t_evaluate e inner join t_order o on o.orderNo=e.orderNo  WHERE e.state=1 and goodsId=? "
				+ " UNION "
				+ " SELECT if(0=COUNT(1),null,COUNT(1)) num,'cp' type FROM t_evaluate e inner join t_order o on o.orderNo=e.orderNo  WHERE e.state=1 AND e.evaluateLevel<=1 and goodsId=?";
		final Map<String, Object> map = new HashMap<String, Object>();
		try {
			//执行查询
			jdbcTemplate.query(sql,
					new Object[] {
							evaluate.getGoodsId(), evaluate.getGoodsId(), evaluate.getGoodsId(),
							evaluate.getGoodsId(),evaluate.getGoodsId() }, new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs)
								throws SQLException {
							map.put(rs.getString("type"), rs.getString("num"));
						}
					});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询订单状态
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryOrderState(String orderNo) {
		String sql = "select paymentStatus,orderStatus from t_order where orderNo=?";
		Map<String, Object> map = null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql, new Object[] { orderNo });
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询是否已评价
	 * @param orderNo
	 * @return
	 */
	@Override
	public Integer queryEvaluateByOrderNo(String orderNo) {
		String sql = "select count(1) from t_evaluate where orderNo=?";
		Integer i = 0;
		try {
			//执行查询
			i = jdbcTemplate.queryForObject(sql, new Object[] { orderNo },
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

}
