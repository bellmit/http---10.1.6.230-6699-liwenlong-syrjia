package cn.syrjia.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.KeepDao;
import cn.syrjia.entity.Keep;
import cn.syrjia.util.Util;

@Repository("keepDao")
public class KeepDaoImpl extends BaseDaoImpl implements KeepDao{
	
	// 日志
	private Logger logger = LogManager.getLogger(KeepDaoImpl.class);

	/**
	 * 查询关注
	 */
	@Override
	public List<Map<String, Object>> queryKeep(Keep keep,Integer page,Integer row) {
		String sql="SELECT uk.id keepId,g.*,gt.`name` goodsTypeName,gi.`name` identificationName,s.name supplierName,(SELECT price FROM t_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1'))activityPrice,(SELECT ga.id FROM t_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1')) activityId FROM t_goods g INNER JOIN t_goods_type gt ON gt.id=g.goodsTypeId INNER JOIN t_goods_identification gi ON gi.id=g.identificationId INNER JOIN t_supplier s ON s.id=g.supplierId INNER JOIN t_user_keep uk ON uk.goodsId=g.id WHERE g.state=1 AND gt.state=1 AND gi.state=1 AND s.state=1 AND uk.memberId=? order BY uk.createTime desc ";
		if(null!=page&&null!=row){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行sql
			list = jdbcTemplate.queryForList(sql,new Object[]{keep.getMemberId()});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 添加
	 */
	@Override
	public Integer addKeep(Keep keep) {
		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public Integer deleteKeep(Keep keep) {
		String sql="delete from t_user_keep where goodsId=? and memberId=?";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{keep.getGoodsId(),keep.getMemberId()});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}
	
	/**
	 * 批量删除
	 */
	@Override
	public Integer deleteKeeps(final String[] keeps) {
		String sql="delete from t_user_keep where id=?";
		int[] i={0};
		try {
			//批量更新
			i = jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1,keeps[i]);
				}
				@Override
				public int getBatchSize() {
					return keeps.length;
				}
			});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return Util.toInt(i);
	}

	/**
	 * 实体查询
	 */
	@Override
	public String hasKeep(Keep keep) {
		String sql="select id from t_user_keep where goodsId=? and memberId=? limit 0,1";
		String id="";
		try {
			//执行查询
			id = jdbcTemplate.queryForObject(sql,new Object[]{keep.getGoodsId(),keep.getMemberId()},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return id;
	}

}
