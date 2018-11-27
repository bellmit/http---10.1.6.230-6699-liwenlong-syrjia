package cn.syrjia.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.ScoreGoodsDao;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.ScoreConsume;
import cn.syrjia.entity.ScoreGoods;
import cn.syrjia.weixin.util.StringUtils;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("scoreGoodsDao")
public class ScoreGoodsDaoImpl extends BaseDaoImpl implements ScoreGoodsDao {

	private Logger logger = LogManager.getLogger(ScoreGoodsDaoImpl.class);

	/**
	 * 订单号
	 */
	public synchronized String orderNo() {
		String orderNo = null;
		try {
			orderNo = jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con)
						throws SQLException {
					CallableStatement cs = con
							.prepareCall("{call t_orderNo(?)}");
					cs.registerOutParameter(1, SqlTypeValue.TYPE_UNKNOWN);// 注册输出参数的类型
					return cs;
				}
			}, new CallableStatementCallback<String>() {

				public String doInCallableStatement(CallableStatement call)
						throws SQLException, DataAccessException {
					call.execute();
					return call.getString(1);// 获取输出参数的值
				}
			});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return orderNo;
	}
	
	/**
	 * 查询所有的商品记录
	 */
	@Override
	public List<Map<String, Object>> queryAllScoreGoods(ScoreGoods scoreGoods,
			Integer page, Integer row) {
		String sql = "SELECT g.*,gt.`name` goodsTypeName,gi.`name` identificationName,s.name supplierName,(SELECT price FROM t_score_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1'))activityPrice,(SELECT ga.id FROM t_score_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1')) activityId FROM t_score_goods g INNER JOIN t_score_goods_type gt ON gt.id=g.goodsTypeId INNER JOIN t_score_goods_identification gi ON gi.id=g.identificationId INNER JOIN t_supplier s ON s.id=g.supplierId WHERE g.state=1 AND gt.state=1 AND gi.state=1 AND s.state=1";
		if (!StringUtil.isEmpty(scoreGoods.getIsRecommend())) {
			sql += " AND g.isRecommend=" + scoreGoods.getIsRecommend();
		}
		sql += "  ORDER BY g.rank ";
		//分页
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//查询
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 通过用户id查询
	 */
	@Override
	public String queryScoreByUserId(String scoreConsumeId) {
		String sql = "SELECT IFNULL(SUM(consumeScore),0)integral FROM t_score_consume WHERE userId= ?";
		try {
			//查询
			return jdbcTemplate.queryForObject(sql,
					new Object[] {scoreConsumeId}, String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过ScoreConsume实体查询
	 */
	@Override
	public List<Map<String, Object>> queryScoreRecord(
			ScoreConsume scoreConsume, Integer page, Integer row) {
		String sql = "select * from (SELECT sum(sc.consumeScore) integral,GROUP_CONCAT(IF(sc.orderNo=1,'签到',IFNULL(g.name,sg.name)))goodsName,FROM_UNIXTIME(sc.createtime,'%Y-%m-%d %H:%i:%s') createTime  FROM t_score_consume sc INNER JOIN t_order o ON sc.orderNo=o.orderNo INNER JOIN t_order_detail od ON od.orderNo=o.orderNo LEFT JOIN t_goods g ON od.goodsId=g.id LEFT JOIN t_score_goods sg ON od.goodsId = sg.id where userid=? GROUP BY sc.orderNo UNION ALL SELECT sc.consumeScore integral,'签到' goodsName,FROM_UNIXTIME(sc.createtime,'%Y-%m-%d %H:%i:%s')createTime FROM t_score_consume sc WHERE userid=? AND sc.orderNo = '1') f ORDER BY f.createtime DESC  LIMIT "
				+ (page - 1) * row + "," + row;
		try {
			//执行查询
			return jdbcTemplate.queryForList(sql,
					new Object[] { scoreConsume.getUserid(),scoreConsume.getUserid() });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过id查询商品
	 */
	@Override
	public Map<String, Object> queryScoreGoodsById(String scoreGoodsId) {
		String sql = "SELECT g.*,gt.`name` goodsTypeName,gi.`name` identificationName,s.name supplierName,(SELECT price FROM t_score_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1'))activityPrice,(SELECT ga.id FROM t_score_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1')) activityId FROM t_score_goods g INNER JOIN t_score_goods_type gt ON gt.id=g.goodsTypeId INNER JOIN t_score_goods_identification gi ON gi.id=g.identificationId INNER JOIN t_supplier s ON s.id=g.supplierId WHERE g.state=1 AND gt.state=1 AND gi.state=1 AND s.state=1 and g.id=?";
		try {
			return jdbcTemplate.queryForMap(sql,
					new Object[] {scoreGoodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 分页查询订单
	 */
	@Override
	public List<Map<String, Object>> queryScoreOrderList(String memeberId,
			Integer page, Integer row) {
		String sql = " SELECT go.*,gi.name as giftName ,gi.picture FROM t_gift_order go  INNER JOIN t_gift gi ON gi.id= go.giftId "
				+ " WHERE go.userId=? AND go.state=1 ORDER BY go.createTime desc ";
		sql += " limit " + (page - 1) * row + "," + row;
		try {
			return jdbcTemplate.queryForList(sql, new Object[] { memeberId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}


	/**
	 * 通过订单号查询
	 */
	@Override
	public Map<String, Object> queryScoreOrderByOrderNo(String orderNo,
			String memberId, String scoreGoodsId) {
		String sql="select o.phone,od.goodsId,o.orderNo,o.orderPrice,o.detailedAddress,o.province,o.city,o.area,o.consignee,od.accessAddress,od.validityTime,sg.description,sg.remark,sg.picture,o.receiptsPrice from t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_score_goods sg ON sg.id=od.goodsId WHERE o.orderNo=?";
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			map = jdbcTemplate.queryForMap(sql,new Object[]{orderNo});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}


	/**
	 * 查询订单
	 */
	@Override
	public List<Map<String, Object>> queryScoreOrder(Order order, Integer page,
			Integer row) {
		String sql="select o.orderNo,sg.`name` goodsName,sg.picture,o.orderStatus,sg.isShipping from t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_score_goods sg ON sg.id=od.goodsId WHERE o.orderType=2 and o.memberId=? order by o.createTime desc";
		if(!StringUtils.isEmpty(page)&&!StringUtils.isEmpty(row)){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list = jdbcTemplate.queryForList(sql,new Object[]{order.getMemberId()});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}


	/**
	 * 查询商品图片
	 */
	@Override
	public List<Map<String, Object>> queryScoreGoodsImg(String scoreGoodsId) {
		String sql="SELECT sg.picture img FROM t_score_goods sg where sg.id=? UNION ALL  SELECT p.picPathUrl img FROM t_piclib p INNER JOIN t_score_goods sg ON sg.id=p.goodId where sg.id=?";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql,new Object[]{scoreGoodsId,scoreGoodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

}
