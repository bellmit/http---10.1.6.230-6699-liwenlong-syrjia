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
import cn.syrjia.dao.GoodsOrderDao;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("goodsOrderDao")
public class GoodsOrderDaoImpl extends BaseDaoImpl implements GoodsOrderDao {

	// 日志
	private Logger logger = LogManager.getLogger(GoodsOrderDaoImpl.class);

	/**
	 * 生成订单号
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
	 * 订单退款
	 * @param openId
	 * @param orderNo
	 * @param refundMsg
	 * @return
	 */
	@Override
	public Integer refundOrder(String openId, String orderNo, String refundMsg) {
		String sql = "update t_order set paymentStatus=3,refundNote=? where orderNo=?";
		try {
			return jdbcTemplate
					.update(sql, new Object[] { refundMsg, orderNo });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
	}

	/**
	 * 查询订单列表
	 * @param memberId
	 * @param order
	 * @param row
	 * @param page
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryOrderList(String memberId, Order order,
			Integer row, Integer page) {
		String sql = "SELECT f.consignee,f.realname,f.headicon,f.memberId,f.orderType,f.isCustomer,f.paymentStatus,(f.receiptsPrice-f.postage) receiptsPrice,(f.orderPrice-f.postage) orderPrice,f.orderNo,f.time,f.evalId,f.orderStatus,f.payTime,f.payState,GROUP_CONCAT(f.goodsId)goodsIds,f.city,f.postage,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f.name,f.price,f.goodsNum,f.picture,f.specifications) separator '&') goods FROM (SELECT IFNULL(m.realname,d.docName) realname,IFNULL(m.headicon,d.docUrl) headicon,o.orderType,o.consignee,o.isCustomer,o.paymentStatus,GROUP_CONCAT(gds.name ORDER BY gds.rank separator ' ') specifications,g.name,g.id goodsId,od.goodsNum,e.id evalId,o.city,o.postage,od.goodsOriginalPrice price,IF(gp.picture IS NULL or ''=gp.picture or 'null'=gp.picture,g.picture,gp.picture) picture,o.memberId,o.receiptsPrice, o.orderPrice, o.type,o.orderNo, FROM_UNIXTIME( o.createTime, '%Y-%m-%d %H:%i' )time, o.orderStatus, FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime, CASE o.paymentStatus WHEN 1 THEN '待付款' "
					+" WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' "
					+" WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState "
					+" FROM t_order o left join t_doctor d on d.doctorId=o.memberId left join t_member m on m.id=o.memberId INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id AND gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE o.state=1 AND o.orderType=1 AND o.mainOrderNo IS NULL GROUP BY o.orderNo,gp.id) f WHERE f.memberId=? ";
		if (null != order.getPaymentStatus()) {
			if (order.getPaymentStatus() > 100) {
				sql += " and (f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(0, 1)
						+ " or f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(1, 2)
						+ " or f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(2)
						+ ")";
			} else if (order.getPaymentStatus() > 10) {
				sql += " and (f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(0, 1)
						+ " or f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(1, 2)
						+ ")";
			} else if(order.getPaymentStatus()==5){
				sql += " and f.paymentStatus=" + order.getPaymentStatus()+" and f.evalId is null ";
			}else{
				sql += " and f.paymentStatus=" + order.getPaymentStatus();
			}
		}
		if (!StringUtil.isEmpty(order.getOrderStatus())) {
			if (order.getOrderStatus() > 100) {
				sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0, 1)
						+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1, 2)
						+ " or f.orderStatus="+order.getOrderStatus().toString().substring(2)
						+ ")";
			} else if (order.getOrderStatus() > 10) {
				sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0, 1)
						+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1, 2)
						+ ")";
			}else{
				sql += " and f.orderStatus="+ order.getOrderStatus();
			}
		}
		if (!StringUtil.isEmpty(order.getOrderNo())) {
			sql += " and f.orderNo like '%" + order.getOrderNo().toUpperCase()+"%' ";
		}
		sql += " GROUP BY f.orderNo ORDER BY f.time desc";

		if (null != row && null != page) {
			sql += " limit " + row * (page - 1) + "," + row;
		}
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] { memberId });
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 根据订单号查询订单信息
	 * @param memberId
	 * @param order
	 * @return
	 */
	@Override
	public Map<String, Object> queryOrderByOrderNo(String memberId, Order order) {
		String sql = "SELECT f.createTime,f.state,f.realname,f.headicon,f.memberId,f.orderType,f.mainOrderNo,f.isCustomer,f.paymentStatus,f.city,f.province,f.area,f.detailedAddress,f.evalId,f.consignee,f.phone,f.receiptsPrice,f.orderPrice,f.orderNo,f.time,f.orderStatus,f.payTime,f.payState,GROUP_CONCAT(f.goodsId)goodsIds,f.city,f.postage,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f.name,f.price,f.goodsNum,f.picture,f.specifications) separator '&') goods,1 type FROM (SELECT o.createTime,o.state,m.realname,m.headicon,o.orderType,o.mainOrderNo,o.isCustomer,o.paymentStatus,GROUP_CONCAT(gds.name separator ' ') specifications,g.name,g.id goodsId,od.goodsNum,e.id evalId,o.city,o.province,o.area,o.detailedAddress,o.consignee,o.phone,o.postage,od.goodsOriginalPrice price,IF(gp.picture IS NULL or ''=gp.picture or 'null'=gp.picture,g.picture,gp.picture) picture,o.memberId,o.receiptsPrice, o.orderPrice, o.type,o.orderNo, FROM_UNIXTIME( o.createTime, '%Y-%m-%d %H:%i' )time, o.orderStatus, FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime, CASE o.paymentStatus WHEN 1 THEN '待付款'"
				+" WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款'"
				+" WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState"
				+" FROM t_order o left JOIN t_member m on m.id=o.memberId INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id AND gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE o.state<>2 GROUP BY o.orderNo,gp.id) f WHERE f.memberId=? and f.orderNo=? ";
		if (null != order.getPaymentStatus()) {
			if (order.getPaymentStatus() > 100) {
				sql += " and (f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(0, 1)
						+ " or f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(1, 2)
						+ " or f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(2)
						+ ")";
			} else if (order.getPaymentStatus() > 10) {
				sql += " and (f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(0, 1)
						+ " or f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(1, 2)
						+ ")";
			} else {
				sql += " and f.paymentStatus=" + order.getPaymentStatus();
			}
		}
		if (!StringUtil.isEmpty(order.getOrderStatus())) {
			sql += " and f.orderStatus=" + order.getOrderStatus();
		}
		
		if(!StringUtil.isEmpty(order.getOrderStatus())&&StringUtil.isEmpty(order.getPaymentStatus())&&order.getOrderStatus()==5&&order.getPaymentStatus()==5){
			sql += " and f.evalId= is not null ";
		}
		sql+=" order by f.time desc";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql, new Object[] { memberId,order.getOrderNo() });
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 根据用户id查询已购买数量
	 * @param memberId
	 * @param activityId
	 * @param goodsPriceNumId
	 * @param orderNo
	 * @return
	 */
	@Override
	public Integer queryBuyConnt(String memberId, String activityId, String goodsPriceNumId,String orderNo) {
		String sql="SELECT ifnull(SUM(od.goodsNum),0) FROM t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_order_activity oa ON oa.orderDetialId=od.id INNER JOIN t_supplier_activity sa ON sa.id=oa.activityId WHERE o.memberId=? and sa.id=? and o.paymentStatus <> 6 and od.goodsPriceNumId=? ";
		if(!StringUtil.isEmpty(orderNo)){
			sql+=" AND o.orderNo <> '"+orderNo+"'";
		}
		Integer i=0;
		try {
			i = jdbcTemplate.queryForObject(sql,new Object[]{memberId,activityId,goodsPriceNumId},Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}
	
	/**
	 * 根据订单号查询订单信息
	 * @param memberId
	 * @param order
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryOrderDetailByOrderNo(String memberId, Order order) {
		String sql = "SELECT GROUP_CONCAT(gds.name SEPARATOR ' ')specifications,g.name,od.goodsOriginalPrice price,g.id goodsId,od.id orderDetailId,od.goodsNum, "
					+" IF(gp.picture IS NULL OR '' = gp.picture OR 'null' = gp.picture,g.picture,gp.picture)picture,o.memberId,o.type,o.orderNo"
					+" FROM t_order o"
					+" INNER JOIN t_order_detail od ON o.orderNo = od.orderNo"
					+" INNER JOIN t_goods g ON g.id = od.goodsId"
					+" INNER JOIN t_goods_pricenum gp ON gp.goodsId = g.id AND gp.id=od.goodsPriceNumId"
					+" INNER JOIN t_goods_details gd ON gd.goodsPriceNumId = gp.id"
					+" INNER JOIN t_goods_details_specifications gds ON gds.id = gd.specificationsId"
					+" WHERE o.state = 1 AND o.orderNo=? AND o.memberId=? GROUP BY od.goodsPriceNumId";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] {order.getOrderNo(),memberId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return list;
	}
	
	/**
	 * 减去库存
	 * @param priceNumId
	 * @param buyCount
	 * @return
	 */
	@Override
	public Integer restocking(String priceNumId, Integer buyCount) {
		String sql="UPDATE t_goods_pricenum SET stock=stock-? WHERE id=? AND stock-?>=0";
		Integer i=0;
		try {
			i = jdbcTemplate.update(sql,new Object[]{buyCount,priceNumId,buyCount});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 根据订单号查询订单详情
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryOrderInfo(String orderNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select * from t_logistics where orderNo=?";
		try {
			map = jdbcTemplate.queryForMap(sql, new Object[] { orderNo });
		} catch (Exception e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 根据主订单号删除订单信息
	 * @param orderNo
	 * @return
	 */
	@Override
	public Integer deleteOrderByMainOrder(String orderNo) {
		String sql="delete from t_order where orderType=1 and mainOrderNo=?";
		Integer i=0;
		try {
			if(!StringUtil.isEmpty(orderNo)){
				i = jdbcTemplate.update(sql,new Object[]{orderNo});
			}
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 查询订单详情
	 * 
	 * @param order
	 *            订单实体
	 * @pdOid edb9daf2-27ec-4de2-81c0-9aa609289d82
	 */
	@Override
	public Map<String, Object> queryOrderForDetail(Order order) {
		String sql="SELECT o.orderNo,o.receiptsPrice price,o.city,GROUP_CONCAT(g.id) goodsId,o.orderType from t_order o INNER JOIN t_order_detail od ON o.orderNo=od.orderNo INNER JOIN t_goods g ON g.id=od.goodsId INNER JOIN t_supplier s on s.id=g.supplierId WHERE o.paymentStatus=1 and g.state=1 and s.state=1 and o.orderNo=? and o.memberId=? group by o.orderNo";
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			map = jdbcTemplate.queryForMap(sql,new Object[]{order.getOrderNo(),order.getMemberId()});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}

	/**
	 * 根据订单号查询供应商信息
	 */
	@Override
	public List<Map<String, Object>> querySupplierByOrderNo(String orderNo) {
		String sql="SELECT g.supplierId FROM t_order_detail od INNER JOIN t_goods g ON g.id=od.goodsId INNER JOIN t_supplier s ON s.id=g.supplierId where od.orderNo=?  GROUP BY s.id";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list = jdbcTemplate.queryForList(sql,new Object[]{orderNo});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询服务订单
	 */
	@Override
	public List<Map<String, Object>> queryOrderForService(Order order,
			Integer page, Integer row) {
		String sql="SELECT * from (SELECT o.memberId,o.orderType,o.orderNo,od.id detailId,o.orderStatus,o.payTime,od.id,od.paymentStatus,od.validityTime,g.`name`,g.picture FROM t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_goods g ON g.id=od.goodsId WHERE od.type=1 AND o.state=1 AND o.paymentStatus <> 1 AND o.paymentStatus <> 6 UNION ALL SELECT o.memberId,o.orderType,o.orderNo,od.id detailId,o.orderStatus,o.payTime,od.id, od.paymentStatus, od.validityTime, g.`name`, g.picture FROM t_order o INNER JOIN t_order_detail od ON od.orderNo = o.orderNo INNER JOIN t_score_goods g ON g.id = od.goodsId WHERE od.type = 1 AND o.state = 1 AND o.paymentStatus <> 1 AND o.paymentStatus <> 6 ) f where f.memberId=? ";
		
		if(order.getOrderStatus()==1){
			sql+=" AND f.orderStatus=1 AND f.validityTime > UNIX_TIMESTAMP() ";
		}else if(order.getOrderStatus()==5){
			sql+=" AND (f.orderStatus=5 or f.validityTime <= UNIX_TIMESTAMP())";
		}
		sql+="  order by f.payTime desc";
		if (null != row && null != page) {
			sql += " limit " + row * (page - 1) + "," + row;
		}
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] { order.getMemberId() });
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询服务订单详情
	 */
	@Override
	public Map<String, Object> queryOrderForServiceById(
			GoodsOrderDetail goodsOrderDetail, String memberId) {
		String sql="SELECT o.memberId,o.orderType,g.description,od.accessAddress,od.goodsOriginalPrice*od.goodsNum goodsOriginalPrice,od.goodsTotal,od.goodsNum,o.orderNo,od.id detailId,o.orderStatus,o.payTime,od.id,od.paymentStatus,od.validityTime,g.`name`,g.picture,od.validityTime <=UNIX_TIMESTAMP() validity   FROM t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_goods g ON g.id=od.goodsId WHERE od.type=1 AND o.state=1 AND o.paymentStatus <> 1 AND o.paymentStatus <> 6 AND od.id=? AND o.memberId=?";
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			map = jdbcTemplate.queryForMap(sql,new Object[]{goodsOrderDetail.getId(),memberId});
		} catch (DataAccessException e) {
			logger.error(e);
			return null;
		}
		return map;
	}
	
	/**
	 * 查询呼叫中心数据
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> querySendCallCenterData(String orderNo) {
		Map<String, Object> map=null;
		try {
			if(!StringUtil.isEmpty(orderNo)){
				String sql="SELECT ifnull(o.mainOrderNo,o.orderNo) OrderNo,o.orderNo DetailNo,ifnull(o.phone,p.phone) Mobile,o.createTime OrderDate, "
						+" o.orderType OrderType,o.patientId PatientId,p.`name` CustomerName,o.consignee Receiver,'' as ReceiverId,d.docName Doctor,o.doctorId DoctorId, "
						+" s.`name` Assist,s.srId AssistId,o.orderType ServeType,FORMAT(o.receiptsPrice,2) Cash, "
						+" o.orderStatus TrackStatus,d.docPhone, "
						+" o.paymentStatus TradeStatus, "
						+" os.talkLimitTime TalkLength,os.talkTime CallLength,o.createTime CreateTime "
						+" from t_order o  "
						+" LEFT JOIN t_order_detail_server os on os.orderNo = o.orderNo "
						+" LEFT JOIN t_patient_data p on p.id=o.patientId "
						+" LEFT JOIN t_doctor d on d.doctorId = o.doctorId "
						+" LEFT JOIN t_follow_history f on f.openId = o.doctorId "
						+" LEFT JOIN t_sales_represent s on s.srId = f.followId "
						+" where o.orderNo=? ";
				map = jdbcTemplate.queryForMap(sql,new Object[]{orderNo});
			}
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}
}
