package cn.syrjia.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.OrderDao;
import cn.syrjia.entity.Order;
import cn.syrjia.hospital.entity.OrderDetailServer;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDaoImpl implements OrderDao {
	// 日志
	private Logger logger = LogManager.getLogger(OrderDaoImpl.class);

	@Resource(name = "jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	/**
	 * 支付页面查询订单号、订单价格
	 * @param orderNo
	 * @param memberId
	 * @return
	 */
	@Override
	public Map<String, Object> queryPayOrderDetail(Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(order.getOrderNo())) {
			//拼接sql
			String sql = "(SELECT o.state,o.paymentStatus,o.orderNo,o.receiptsPrice price,o.city,GROUP_CONCAT(g.id) goodsId,o.orderType,o.doctorId,o.patientId,o.memberId from t_order o INNER JOIN t_order_detail od ON o.orderNo=od.orderNo INNER JOIN t_goods g ON g.id=od.goodsId INNER JOIN t_supplier s on s.id=g.supplierId WHERE g.state=1 and s.state=1 and o.orderNo='"
					+ order.getOrderNo()
					//+ "' and o.memberId='"
					//+ order.getMemberId()
					+ "' group by o.orderNo) "
					+ " UNION "
					+ " (SELECT o.state,o.paymentStatus,o.orderNo,o.receiptsPrice price,o.city,g.doctorId goodsId,o.orderType,o.doctorId,o.patientId,o.memberId from t_order o INNER JOIN t_order_detail_server od ON o.orderNo=od.orderNo INNER JOIN t_doctor g ON g.doctorId=od.doctorId  WHERE  g.docIsOn='1' and g.docStatus='10' and o.orderNo='"
					+ order.getOrderNo()
					//+ "' and o.memberId='"
					//+ order.getMemberId()
					+ "' )"
					+ " UNION "
					+ " (SELECT o.state,o.paymentStatus,o.orderNo,o.receiptsPrice price,o.city,GROUP_CONCAT(g.id) goodsId,o.orderType,o.doctorId,o.patientId,o.memberId from t_order o INNER JOIN t_order_detail od ON o.orderNo=od.orderNo INNER JOIN t_eva_banner g ON g.id=od.goodsId WHERE g.state=1 and o.orderNo='"
					+ order.getOrderNo()
					//+ "' and o.memberId='"
					//+ order.getMemberId()
					+ "' group by o.orderNo) " +
					"UNION(SELECT o.state,o.paymentStatus,o.orderNo,o.receiptsPrice price,o.city,null goodsId,o.orderType,o.doctorId,o.patientId,o.memberId FROM t_order o WHERE o.orderType in(10,13,14,15,16,17,18,19,20) AND o.orderNo ='"
					+ order.getOrderNo()
					+"' GROUP BY o.orderNo)";
			try {
				//执行sql
				map = jdbcTemplate.queryForMap(sql);
			} catch (DataAccessException e) {
				logger.warn(e);
				return null;
			}
		}
		return map;
	}

	/**
	 * 生成订单号
	 */
	public synchronized String orderNo() {
		String orderNo = null;
		try {
			orderNo = jdbcTemplate.execute(new CallableStatementCreator() {
				//执行存储过程
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
	 * 医生详情判断是否有未完成同类型订单使用
	 * @return
	 */
	@Override
	public Integer queryOrderCountByOrderType(Order order) {
		Integer count = 0;
		try {
			//拼接sql
			String sql = "SELECT count(1) from t_order where state=1 and paymentStatus in(2,3) and memberId=? ";
			if (!StringUtil.isEmpty(order.getMemberId())) {
				if (order.getOrderType() != null) {
					sql += " and orderType= " + order.getOrderType();
				}
				//执行sql
				count = super.queryBysqlCount(sql,
						new Object[] { order.getMemberId() });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	/**
	 * 修改订单状态
	 * @param paystatus
	 * @param orderStatus
	 * @param tradeNo
	 * @param payway
	 * @param orderNo
	 * @param receiptsPrice
	 * @return
	 */
	@Override
	public Integer updateOrderStatus(Integer paystatus, Integer orderStatus,
			String tradeNo, Integer payway, String orderNo,Double receiptsPrice,String followId,Integer orderWay,String yztId) {
		Integer i = 0;
		try {
			//拼接sql
			String sql = " UPDATE t_order SET paymentStatus =? ";
			if (paystatus != null) {
				if (orderStatus != null) {
					sql += ",orderStatus=" + orderStatus;
				}
				if (!StringUtil.isEmpty(tradeNo)) {
					sql += ",tradeNo='" + tradeNo + "'";
				}
				if (!StringUtil.isEmpty(payway)) {
					sql += ",payway=" + payway;
				}
				if (!StringUtil.isEmpty(orderWay)) {
					sql += ",orderWay=" + orderWay;
				}
				if (!StringUtil.isEmpty(followId)) {
					sql += ",followId='"+followId+"'";
				}
				if (!StringUtil.isEmpty(yztId)) {
					sql += ",yztId='"+yztId+"'";
				}
				if (receiptsPrice != null) {
					sql += ",receiptsPrice=" + receiptsPrice + " ";
				}
				if (paystatus == 2||paystatus == 5) {
					sql += ",payTime=UNIX_TIMESTAMP() ";
				}
				sql += " where paymentStatus=1 and orderNo=? ";
				if (!StringUtil.isEmpty(orderNo)) {
					System.out.println(sql);
					//执行sql
					i = super.update(sql, new Object[] { paystatus, orderNo });
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return i;
	}
	
	/**
	 * 更新调理方状态
	 * @param recordId
	 * @return
	 */
	@Override
	public Integer updateRecordStatus(String recordId) {
		String sql="update t_recipe_record set state=2 where recordId=(SELECT recordId FROM t_order WHERE recordId IS NOT NULL AND (orderNo=? OR mainOrderNo=?))";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{recordId,recordId});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 添加支付信息
	 * 
	 * @Description: TODO
	 * @param @param orderNo
	 * @param @param total_amount
	 * @param @param buyer_id
	 * @param @param trade_no
	 * @param @param trade_status
	 * @param @return
	 * @return Integer
	 */
	@Override
	public Integer addPayment(String orderNo, String total_amount,
			String buyer_id, String trade_no, String trade_status) {
		String sql = "INSERT INTO t_order_pay_record VALUES(?,?,?,?,?)";
		int i = 0;
		try {
			//执行插入
			i = super.update(sql, new Object[] { orderNo, total_amount,
					buyer_id, trade_no, trade_status });
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 获取产品类推送参数
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsDetail(String orderNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//拼接sql
			String sql = "SELECT o.orderType,GROUP_CONCAT(DISTINCT(g.`name`)) remarks,o.orderNo,o.orderPrice,o.receiptsPrice,m.openid,m.token,m.loginname,m.realname, "
					+ " m.demodel,m.detype from t_order o "
					+ " INNER JOIN t_order_detail od on od.orderNo = o.orderNo "
					+ " INNER JOIN t_goods g on g.id = od.goodsId "
					+ " INNER JOIN t_member m on m.id=o.memberId "
					+ " where o.paymentStatus=2 and o.orderNo=?  ";
			if (!StringUtil.isEmpty(orderNo)) {
				//执行查询
				map = super.queryBysqlMap(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}

	/**
	 * 查询商品供应商
	 * @param orderNo
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsSupplier(String orderNo) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//拼接sql
			String sql = "SELECT -1 orderType,GROUP_CONCAT(DISTINCT(g.`name`)) remarks,o.orderNo,o.orderPrice,wu.openid,m.token,m.loginname,"
					+ " m.demodel,m.detype from t_order o "
					+ " INNER JOIN t_order_detail od on od.orderNo = o.orderNo "
					+ " INNER JOIN t_goods g on g.id = od.goodsId "
					+ " INNER JOIN t_supplier s ON s.id=g.supplierId"
					+ " INNER JOIN t_user u ON s.userId=u.id"
					+ " INNER JOIN t_weixin_user wu ON wu.openId=u.openId"
					+ " LEFT JOIN t_member m ON m.openid=wu.openId"
					+ " where o.paymentStatus=2 and o.orderNo=? GROUP BY s.id ";
			if (!StringUtil.isEmpty(orderNo)) {
				//执行查询
				list = jdbcTemplate.queryForList(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询订单列表
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryOrder(Order order, Integer page,
			Integer row) {
		//拼接sql
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (SELECT max(e.id) evaluateId,o.orderStatus,o.memberId,o.orderNo,o.createTime,o.orderPrice,o.receiptsPrice,m.realname,o.paymentStatus,o.orderType,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN IF(g.isShipping=0,'已付款',IF(o.orderStatus=1,'待发货','已发货')) WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '交易完成' WHEN 6 THEN '订单作废'  WHEN 8 THEN '部分退款' WHEN 9 THEN IF(g.isShipping=0,'已付款',IF(o.orderStatus=1,'待发货','已发货')) END payment,GROUP_CONCAT(CONCAT_WS(';',g.id,g.`name`,g.picture,od.goodsNum,od.goodsNum*od.goodsOriginalPrice,od.goodsTotal)) goods,COUNT(1) num FROM t_order o INNER JOIN t_member m ON m.id=o.memberId INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_goods g ON g.id=od.goodsId LEFT JOIN t_evaluate e ON e.orderDetailId=od.id where o.orderType <> 2 and o.state=1 GROUP BY o.orderNo ");
		sql.append(" UNION ALL ");
		sql.append("SELECT e.id evaluateId,t_order_a.orderStatus,t_order_a.memberId,t_order_a.orderNo,t_order_a.createTime,t_order_a.orderPrice,t_order_a.receiptsPrice,t_member_a.realname,t_order_a.paymentStatus,t_order_a.orderType,CASE t_order_a.paymentStatus WHEN 0 THEN '待确认' WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '交易完成' WHEN 6 THEN '订单作废' WHEN 8 THEN '部分退款' WHEN 9 THEN '已付款' END payment,GROUP_CONCAT(CONCAT_WS(';',i.infirmaryId,CONCAT_WS(' ',i.infirmaryName,d.departName,p.`name`),IFNULL(i.imageUrl,' '),1,0,t_order_a.receiptsPrice)) goods,COUNT(1) num  ");
		sql.append("FROM t_order AS t_order_a INNER JOIN t_member AS t_member_a ON t_order_a.memberId = t_member_a.id  ");
		sql.append("INNER JOIN t_medica_orderl_detail AS t_medica_orderl_detail_a ON t_order_a.orderNo = t_medica_orderl_detail_a.orderNo ");
		sql.append("INNER JOIN t_infirmary i on i.infirmaryId=t_medica_orderl_detail_a.infirmaryId  ");
		sql.append("Left JOIN t_department d on d.departId = t_medica_orderl_detail_a.departId ");
		sql.append("LEFT JOIN t_evaluate e ON e.orderNo=t_order_a.orderNo ");
		/* sql.append("Left JOIN t_depart_type dd on dd.id = d.departType  "); */
		sql.append("LEFT JOIN t_position p on p.id=t_medica_orderl_detail_a.positionId ");
		/*
		 * sql.append(
		 * "LEFT JOIN t_illness_class ill on ill.illClassId = t_medica_orderl_detail_a.illClassId"
		 * );
		 */
		sql.append(" where t_order_a.orderType <> 2 and t_order_a.state=1 GROUP BY t_order_a.orderNo) f where f.memberId=? ");
		if (!StringUtil.isEmpty(order.getPaymentStatus())) {
			if (order.getPaymentStatus() > 10) {
				sql.append(" AND (f.paymentStatus="
						+ order.getPaymentStatus().toString().substring(0, 1)
						+ " or paymentStatus="
						+ order.getPaymentStatus().toString().substring(1, 2)
						+ ")");
			} else {
				sql.append(" AND f.paymentStatus=" + order.getPaymentStatus());
			}
		}

		if (!StringUtil.isEmpty(order.getRsrvStr1())) {
			sql.append(" AND f.evaluateId is null");
		}

		if (!StringUtil.isEmpty(order.getOrderStatus())) {
			sql.append(" AND f.orderStatus=" + order.getOrderStatus());
		}
		sql.append(" ORDER BY f.createTime DESC");
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql.append(" limit " + (page - 1) * row + "," + row);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql.toString(),
					order.getMemberId());
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询退款订单详情
	 * @param request
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryRefundOrderDetail(String orderNo,
			String memberId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//拼接sql
			String sql = "SELECT o.orderNo,o.paymentStatus,o.payTime, o.orderType, o.type, o.receiptsPrice price, o.createTime, GROUP_CONCAT(IFNULL( g.`name`,CONCAT_WS(' ',i.infirmaryName,d.departName,p.`name`)) SEPARATOR ',') goodsName, "
					+ " GROUP_CONCAT(IFNULL(i.imageUrl,CONCAT_WS(';',g.picture))) picUrl,tmod.ghDate,tmod.bookDate,o.memberId "
					+ " FROM t_order o "
					+ " LEFT JOIN t_order_detail od ON o.orderNo = od.orderNo "
					+ " LEFT JOIN t_goods g ON g.id = od.goodsId "
					+ " LEFT JOIN t_medica_orderl_detail tmod ON tmod.orderNo = o.orderNo "
					+ " LEFT JOIN t_infirmary i ON i.infirmaryId = tmod.infirmaryId "
					+ " LEFT JOIN t_department d ON d.departId = tmod.departId "
					+ " LEFT JOIN t_position p ON p.id = tmod.positionId "
					+ " WHERE o.paymentStatus = 2 AND o.orderNo =? AND o.memberId =?  ";
			if (!StringUtil.isEmpty(orderNo) && !StringUtil.isEmpty(memberId)) {
				//执行查询
				map = super.queryBysqlMap(sql,
						new Object[] { orderNo, memberId });
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}

	/**
	 * 根据订单号查询订单信息
	 * @param order
	 * @return
	 */
	@Override
	public Map<String, Object> queryOrderByOrderNo(Order order) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (SELECT o.payTime,o.phone,o.consignee,o.detailedAddress,o.province,o.city,o.area,max(e.id) evaluateId,o.orderStatus,o.memberId,o.orderNo,o.createTime,o.orderPrice,o.receiptsPrice,m.realname,o.paymentStatus,o.orderType,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN IF(g.isShipping=0,'已付款',IF(o.orderStatus=1,'待发货','已发货')) WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '交易完成' WHEN 6 THEN '订单作废'  WHEN 8 THEN '部分退款' WHEN 9 THEN IF(g.isShipping=0,'已付款',IF(o.orderStatus=1,'待发货','已发货')) END payment,GROUP_CONCAT(CONCAT_WS(';',g.id,g.`name`,g.picture,od.goodsNum,od.goodsNum*od.goodsOriginalPrice,od.goodsTotal,IFNULL(od.accessAddress,' '),o.paymentStatus,o.orderStatus,IFNULL(od.validityTime,' '),g.description,od.id)) goods,COUNT(1) num FROM t_order o INNER JOIN t_member m ON m.id=o.memberId INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_goods g ON g.id=od.goodsId LEFT JOIN t_evaluate e ON e.orderDetailId=od.id where o.orderType <> 2 and o.state=1 GROUP BY o.orderNo) f where f.memberId=? and f.orderNo=?");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql.toString(), order.getMemberId(),
					order.getOrderNo());
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}
	
	/**
     * 更新发货时间
     * @param orderNo
     * @return
     */
	@Override
	public Integer updateDeliveryTime(String orderNo) {
		String sql="update t_logistics set deliveryTime=? where orderNo=?";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{Util.queryNowTime(),orderNo});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}
	
	/**
	 * 根据订单号查询订单详情
	 * @param orderNo
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryOrderDetailByOrderNo(String orderNo) {
		String sql="SELECT * from t_order_detail where orderNo=?";
		List<Map<String,Object>> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForList(sql,new  Object[]{orderNo});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询退款进度
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryRefundProgress(String orderNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT r.*,from_unixtime(r.operatime) refundTime,from_unixtime(r.createtime) applyTime,oc.`name` typeName from t_refundapply_record r "
					+ " INNER JOIN t_order o on o.orderNo = r.orderNo "
					+ " INNER JOIN t_order_class oc on oc.id = o.orderType "
					+ " where r.orderNo=? ORDER BY r.createtime DESC LIMIT 0,1 ";
			if (!StringUtil.isEmpty(orderNo)) {
				//执行查询
				map = super.queryBysqlMap(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> queryNoFinishOrders() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT GROUP_CONCAT(DISTINCT(m.`name`)) userName,GROUP_CONCAT(DISTINCT(m.orderNo)) orderNos,count(1) noFinishCount,m.ghEndTime endDate,o.orderType,'预约挂号' as typeName,FROM_UNIXTIME(m.createTime,'%Y-%m-%d %H:%i:%s') createTime from t_order o "
					+ " INNER JOIN t_medica_orderl_detail m on o.orderNo = m.orderNo "
					+ " INNER JOIN t_member mm on mm.id = o.memberId  "
					+ " where o.paymentStatus in(2,5) and o.orderType =4 and (m.ghDate is null or m.ghDate ='')  "
					+ " and m.ghEndTime=date_add(curdate(), interval 1 day) "
					+ " UNION  "
					+ " SELECT GROUP_CONCAT(DISTINCT(m.`name`)) userName,GROUP_CONCAT(DISTINCT(m.orderNo)) orderNos,count(1) noFinishCount,DATE_ADD(FROM_UNIXTIME(m.createTime,'%Y-%m-%d'), interval 7 day) endDate,o.orderType,'预约挂号' as typeName,FROM_UNIXTIME(m.createTime,'%Y-%m-%d %H:%i:%s') createTime from t_order o "
					+ " INNER JOIN t_medica_orderl_detail m on o.orderNo = m.orderNo "
					+ " INNER JOIN t_member mm on mm.id = o.memberId  "
					+ " where o.paymentStatus in(2,5) and o.orderType =6 and o.orderStatus=1 "
					+ " and FROM_UNIXTIME(m.createTime,'%Y-%m-%d')=DATE_SUB(curdate(), interval 7 day) "
					+ " UNION  "
					+ " SELECT GROUP_CONCAT(DISTINCT(m.`name`)) userName,GROUP_CONCAT(DISTINCT(m.orderNo)) orderNos,count(1) noFinishCount,DATE_ADD(FROM_UNIXTIME(m.createTime,'%Y-%m-%d'), interval 5 day) endDate,o.orderType,'预约挂号' as typeName,FROM_UNIXTIME(m.createTime,'%Y-%m-%d %H:%i:%s') createTime from t_order o "
					+ " INNER JOIN t_medica_orderl_detail m on o.orderNo = m.orderNo "
					+ " INNER JOIN t_member mm on mm.id = o.memberId  "
					+ " where o.paymentStatus in(2,5) and o.orderType =7 and (m.bookDate is null or m.bookDate ='') "
					+ " and FROM_UNIXTIME(m.createTime,'%Y-%m-%d')=DATE_SUB(curdate(), interval 5 day) ";
			//执行查询
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.warn(e);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> queryNoFinishHosOrder() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = " SELECT m.*,mm.openid,mm.realname,mm.token,mm.demodel,mm.detype,o.memberId,o.receiptsPrice,o.orderType,'预约挂号' as typeName,FROM_UNIXTIME(m.createTime,'%Y-%m-%d %H:%i:%s') applyTime from t_order o "
					+ " INNER JOIN t_medica_orderl_detail m on o.orderNo = m.orderNo "
					+ " INNER JOIN t_member mm on mm.id = o.memberId  "
					+ " where o.paymentStatus =2 and o.orderType =7 and (m.bookDate is null or m.bookDate ='')  "
					+ " and FROM_UNIXTIME(m.createTime,'%Y-%m-%d')=DATE_SUB(curdate(), interval 6 day)";
			//执行查询
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.warn(e);
		}
		return list;
	}
	
	
	 /**
     * 查询订单列表 包含所有类型订单
     * @param memberId
     * @param order
     * @param row
     * @param page
     * @return
     */
	@Override
	public List<Map<String, Object>> queryAllOrderList(String memberId,
			Order order, Integer row, Integer page) {
		//拼接sql
		String sql="SELECT f.*,m.realname,m.headicon,pd.name patientName FROM (SELECT 1 recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o.state,o.patientId,o.doctorId,o.orderNo mainOrderNo,o.paymentStatus,o.receiptsPrice,o.orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,e.id evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL goodsIds,o.city,ifnull(o.postage,0)postage,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName,IFNULL(d.docPosition,' '),ifnull(o.receiptsPrice,0),st.name)SEPARATOR '&')goods,NULL type,2 orderTypes FROM t_order o INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo INNER JOIN t_doctor d ON d.doctorId=ods.doctorId inner join t_server_type st on st.id=o.orderType LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo WHERE o.state<>2 and o.orderType not in(21,22) and o.memberId=? AND o.mainOrderNo IS NULL ";
				if (null != order.getPaymentStatus()) {
					//如果PaymentStatus大于100的查询条件
					if (order.getPaymentStatus() > 100) {
						sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
								+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
								+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
								+ ")";
						//如果PaymentStatus大于10的查询条件
					} else if (order.getPaymentStatus() > 10) {
						sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
								+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
								+ ")";
						//如果PaymentStatus等于5的查询条件
					} else if(order.getPaymentStatus()==5){
						sql += " and o.paymentStatus=5 and e.id is null";
					}else{
						sql += " and o.paymentStatus="+order.getPaymentStatus();
					}
				}
				if (!StringUtil.isEmpty(order.getOrderStatus())) {
					//如果OrderStatus大于100的查询条件
					if (order.getOrderStatus() > 100) {
						sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
								+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
								+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
								+ ") and o.orderType not in(4,5,6,7,8,9,21,22)";
						//如果OrderStatus大于10的查询条件
					} else if (order.getOrderStatus() > 10) {
						sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
								+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
								+ ") and o.orderType not in(4,5,6,7,8,9,21,22) ";
					}else{
						sql += " and o.orderStatus="+ order.getOrderStatus();
					}
				}
				//拼接sql
				 sql+=" GROUP BY o.orderNo UNION"
				 +" SELECT rr.state recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o.state,o.patientId,o.doctorId,o.orderNo mainOrderNo,o.paymentStatus,(o.receiptsPrice-ifnull(o.postage,0)) receiptsPrice,(o.orderPrice-ifnull(o.postage,0)) orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,null evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,null goodsId,o.city,ifnull(o.postage,0)postage,GROUP_CONCAT("
				 +" CONCAT_WS(',',IFNULL(d.doctorId,' '),ifnull(d.docUrl, ' '),IFNULL(d.docName,' '),IFNULL(d.docPosition, ' '),rr.price,'调理方案')SEPARATOR '&')goods,"
				 +" NULL type,4 orderTypes FROM"
				 +" t_order o INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId LEFT JOIN t_doctor d ON d.doctorId=o.doctorId LEFT JOIN t_patient_data pd ON pd.id=o.patientId LEFT JOIN t_evaluate e ON e.orderNo = o.orderNo "
				 +" WHERE o.memberId =? and o.state<>2 and o.receiptsPrice>0  AND o.mainOrderNo IS NULL and rr.isSendUser = 1 ";
				 if (null != order.getPaymentStatus()) {
					 //如果PaymentStatu大于100的查询条件
						if (order.getPaymentStatus() > 100) {
							sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
									+ ")";
						 //如果PaymentStatu大于10的查询条件
						} else if (order.getPaymentStatus() > 10) {
							sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ ")";
							//如果PaymentStatu等于5的查询条件
						} else if(order.getPaymentStatus()==5){
							sql += " and o.paymentStatus=5 and e.id is null and o.orderType in(1,4,5,6,7,8,9,21,22) ";
						}else{
							sql += " and o.paymentStatus="+order.getPaymentStatus();
						}
					}
					if (!StringUtil.isEmpty(order.getOrderStatus())) {
						//如果OrderStatus大于100的查询条件
						if (order.getOrderStatus() > 100) {
							sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
									+ ") and o.orderType not in(4,5,6,7,8,9,21,22) and o.state<>0";
							//如果OrderStatus大于10的查询条件
						} else if (order.getOrderStatus() > 10) {
							sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ ") and o.orderType not in(4,5,6,7,8,9,21,22) o.state<>0 ";
						}else{
							sql += " and o.orderStatus="+ order.getOrderStatus();
						}
					}
				 sql+=" GROUP BY o.orderNo,o.recordId "
				 +" UNION"
				 +" SELECT 1 recordState,f.isCustomer,f.consignee,f.supplierId,f.memberId,f.orderType,f.state,f.patientId,f.doctorId,f.orderNo mainOrderNo,f.paymentStatus,(f.receiptsPrice-ifnull(f.postage,0)) receiptsPrice,(f.orderPrice-ifnull(f.postage,0)) orderPrice,f.orderNo,f.time,f.evalId,f.orderStatus,f.payTime,f.payState,GROUP_CONCAT(f.goodsId)goodsIds,f.city,ifnull(f.postage,0)postage,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f.name,f.price,f.goodsNum,f.picture,f.specifications,f.supplierId) separator '&') goods,NULL type,1 orderTypes FROM (SELECT o.isCustomer,o.consignee,o.orderType,o.state,o.patientId,o.doctorId,o.paymentStatus,GROUP_CONCAT(gds.name ORDER BY gds.rank separator ' ') specifications,g.name,s.id supplierId,g.id goodsId,od.goodsNum,e.id evalId,o.city,o.postage,od.goodsOriginalPrice price,IF(gp.picture IS NULL or ''=gp.picture or 'null'=gp.picture,g.picture,gp.picture) picture,o.memberId,o.receiptsPrice, o.orderPrice, o.type,o.orderNo, FROM_UNIXTIME( o.createTime, '%Y-%m-%d %H:%i' )time, o.orderStatus, FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime, CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState FROM t_order o INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId LEFT JOIN t_supplier s on s.id=g.supplierId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id AND gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE o.state<>2 AND o.orderType=1 AND o.mainOrderNo IS NULL GROUP BY o.orderNo,gp.id) f WHERE f.memberId=? ";
				 if (null != order.getPaymentStatus()) {
					 	//如果PaymentStatus大于100的查询条件
						if (order.getPaymentStatus() > 100) {
							sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
									+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(2)
									+ ")";
							//如果PaymentStatus大于10的查询条件
						} else if (order.getPaymentStatus() > 10) {
							sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
									+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ ")";
							//如果PaymentStatus等于5的查询条件
						} else if(order.getPaymentStatus()==5){
							sql += " and f.paymentStatus=5 and f.evalId is null";
						}else{
							sql += " and f.paymentStatus="+order.getPaymentStatus();
						}
					}
					if (!StringUtil.isEmpty(order.getOrderStatus())) {
						//如果OrderStatus大于100的查询条件
						if (order.getOrderStatus() > 100) {
							sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ " or f.orderStatus="+order.getOrderStatus().toString().substring(2)
									+ ") and f.orderType not in(4,5,6,7,8,9,21,22)";
							//如果OrderStatus大于10的查询条件
						} else if (order.getOrderStatus() > 10) {
							sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ ") and f.orderType not in(4,5,6,7,8,9,21,22) ";
						}else{
							sql += " and f.orderStatus="+ order.getOrderStatus();
						}
					}
				 sql+=" GROUP BY f.orderNo UNION("
				 +" SELECT GROUP_CONCAT(IFNULL(g.recordState,' ') SEPARATOR '|||') recordState,GROUP_CONCAT(IFNULL(g.isCustomer,' ') SEPARATOR '|||') isCustomer,GROUP_CONCAT(IFNULL(g.consignee,' ') SEPARATOR '|||') consignee,g.supplierId,g.memberId,GROUP_CONCAT(IFNULL(g.orderType,' ') SEPARATOR '|||') orderType,g.state,g.patientId,g.doctorId,g.mainOrderNo,GROUP_CONCAT(IFNULL(g.paymentStatus,' ') SEPARATOR '|||') paymentStatus,GROUP_CONCAT(IFNULL(g.receiptsPrice,' ') SEPARATOR '|||') receiptsPrice,GROUP_CONCAT(IFNULL(g.orderPrice,' ') SEPARATOR '|||') orderPrice,GROUP_CONCAT(IFNULL(g.orderNo,' ') SEPARATOR '|||') orderNo,GROUP_CONCAT(IFNULL(g.time,' ') SEPARATOR '|||') time,"
				 +" GROUP_CONCAT(IFNULL(g.evalId,' ') SEPARATOR '|||') evalId,GROUP_CONCAT(IFNULL(g.orderStatus,' ') SEPARATOR '|||') orderStatus,GROUP_CONCAT(IFNULL(g.payTime,' ') SEPARATOR '|||') payTime,GROUP_CONCAT(IFNULL(g.payState,' ') SEPARATOR '|||') payState,GROUP_CONCAT(IFNULL(g.goodsIds,' ') SEPARATOR '|||') goodsIds,GROUP_CONCAT(IFNULL(g.city,' ') SEPARATOR '|||') city,GROUP_CONCAT(IFNULL(g.postage,0) SEPARATOR '|||') postage,GROUP_CONCAT(IFNULL(g.goods,' ') SEPARATOR '|||') goods,GROUP_CONCAT(IFNULL(g.type,' ') SEPARATOR '|||') type,"
				 +" 3 orderTypes FROM ("
				 +" SELECT rr.state recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o1.state,o1.patientId,o1.doctorId,o1.orderNo mainOrderNo,o.paymentStatus,(o.receiptsPrice - ifnull(o.postage,0)) receiptsPrice,(o.orderPrice - ifnull(o.postage,0))orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,-1 evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL goodsIds,o.city,o.postage,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName,IFNULL(d.docPosition,' '),rr.price,'调理方案')SEPARATOR '&')goods,3 type FROM t_order o1 INNER JOIN t_order o ON o.mainOrderNo=o1.orderNo INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId LEFT JOIN t_doctor d ON d.doctorId=rr.doctorId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo WHERE o.memberId=? and o.state<>2 and o.receiptsPrice>0 and rr.isSendUser=1 ";
				 if (null != order.getPaymentStatus()) {
					//如果PaymentStatus大于100的查询条件	
					 if (order.getPaymentStatus() > 100) {
							sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
									+ ")";
							//如果PaymentStatus大于10的查询条件	
						} else if (order.getPaymentStatus() > 10) {
							sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ ")";
							//如果PaymentStatus等于5的查询条件	
						} else if(order.getPaymentStatus()==5){
							sql += " and o.paymentStatus=5 and e.id is null and o.orderType in(1,4,5,6,7,8,9,21,22) ";
						}else{
							sql += " and o.paymentStatus="+order.getPaymentStatus();
						}
					}
					if (!StringUtil.isEmpty(order.getOrderStatus())) {
						//如果OrderStatus大于100的查询条件	
						if (order.getOrderStatus() > 100) {
							sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
									+ ") and o.orderType not in(4,5,6,7,8,9,21,22) and o.state<>0 ";
							//如果OrderStatus大于10的查询条件
						} else if (order.getOrderStatus() > 10) {
							sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ ") and o.orderType not in(4,5,6,7,8,9,21,22) and o.state<>0 ";
						}else{
							sql += " and o.orderStatus="+ order.getOrderStatus();
						}
					}
				 sql+=" GROUP BY o.orderNo "
				 +" UNION"
				 +" SELECT 1 recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o1.state,o1.patientId,o1.doctorId,o1.orderNo mainOrderNo,o.paymentStatus,o.receiptsPrice,o.orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,e.id evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL goodsIds,o.city,o.postage,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName,IFNULL(d.docPosition,''),ifnull(o.receiptsPrice,0),st.name)SEPARATOR '&')goods,2 type FROM t_order o1 INNER JOIN t_order o ON o.mainOrderNo=o1.orderNo INNER JOIN t_order o2 on o2.mainOrderNo=o1.orderNo and o2.recordId is not null inner join t_recipe_record rr on rr.recordId=o2.recordId INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo INNER JOIN t_doctor d ON d.doctorId=ods.doctorId inner join t_server_type st on st.id=o.orderType LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo WHERE o.memberId=? AND o.receiptsPrice>0 and o.state<>2 and o.orderType not in(21,22) and rr.isSendUser=1 ";
				 if (null != order.getPaymentStatus()) {
					 	//如果PaymentStatus大于100的查询条件
						if (order.getPaymentStatus() > 100) {
							sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
									+ ")";
							//如果PaymentStatus大于10的查询条件
						} else if (order.getPaymentStatus() > 10) {
							sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
									+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ ")";
							//如果PaymentStatus等于5的查询条件
						} else if(order.getPaymentStatus()==5){
							sql += " and o.paymentStatus=5 and e.id is null and rr.state<>0 ";
						}else{
							sql += " and o.paymentStatus="+order.getPaymentStatus();
						}
					}
					if (!StringUtil.isEmpty(order.getOrderStatus())) {
						//如果OrderStatus大于100的查询条件
						if (order.getOrderStatus() > 100) {
							sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
									+ ") and o.orderType not in(4,5,6,7,8,9,21,22) and o.state<>0";
							//如果OrderStatus大于10的查询条件
						} else if (order.getOrderStatus() > 10) {
							sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ ") and o.orderType not in(4,5,6,7,8,9,21,22) and o.state<>0 ";
						}else{
							sql += " and o.orderStatus="+ order.getOrderStatus();
						}
					}
				 sql+=" GROUP BY o.orderNo"
				 +" UNION"
				 +" SELECT 1 recordState,f.isCustomer,f.consignee,f.supplierId,f.memberId,f.orderType,f.state,f.patientId,f.doctorId,f.mainOrderNo,f.paymentStatus,(f.receiptsPrice-ifnull(f.postage,0)) receiptsPrice,(f.orderPrice-ifnull(f.postage,0)) orderPrice,f.orderNo,f.time,f.evalId,f.orderStatus,f.payTime,f.payState,GROUP_CONCAT(f.goodsId)goodsIds,f.city,f.postage,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f.name,f.price,f.goodsNum,f.picture,f.specifications,f.supplierId) separator '&') goods,1 type FROM (SELECT o.isCustomer,o.consignee,o.orderType,o.state,o.patientId,o.doctorId,o1.orderNo mainOrderNo,o.paymentStatus,GROUP_CONCAT(gds.name ORDER BY gds.rank separator ' ') specifications,g.name,s.id supplierId,g.id goodsId,od.goodsNum,e.id evalId,o.city,o.postage,od.goodsOriginalPrice price,IF(gp.picture IS NULL or ''=gp.picture or 'null'=gp.picture,g.picture,gp.picture) picture,o.memberId,o.receiptsPrice, o.orderPrice, o.type,o.orderNo, FROM_UNIXTIME( o.createTime, '%Y-%m-%d %H:%i' )time, o.orderStatus, FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime, CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState FROM t_order o1 INNER JOIN  t_order o ON o1.orderNo=o.mainOrderNo INNER JOIN t_order o2 on o2.mainOrderNo=o1.orderNo and o2.recordId is not null inner join t_recipe_record rr on rr.recordId=o2.recordId INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId LEFT JOIN t_supplier s on s.id=g.supplierId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id AND gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE o.state=1 AND o.orderType=1 and  o1.state<>2 and rr.isSendUser=1  AND o1.mainOrderNo IS NULL GROUP BY o.orderNo,gp.id) f WHERE f.memberId=? ";
				 if (null != order.getPaymentStatus()) {
					//如果PaymentStatus大于100的查询条件
						if (order.getPaymentStatus() > 100) {
							sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
									+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(2)
									+ ")";
						//如果PaymentStatus大于10的查询条件
						} else if (order.getPaymentStatus() > 10) {
							sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
									+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
									+ ")";
						//如果PaymentStatus等于5的查询条件
						} else if(order.getPaymentStatus()==5){
							sql += " and f.paymentStatus=5 and f.evalId is null";
						}else{
							sql += " and f.paymentStatus="+order.getPaymentStatus();
						}
					}
					if (!StringUtil.isEmpty(order.getOrderStatus())) {
						//如果OrderStatus大于100的查询条件
						if (order.getOrderStatus() > 100) {
							sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ " or f.orderStatus="+order.getOrderStatus().toString().substring(2)
									+ ") and f.orderType not in(4,5,6,7,8,9,21,22) and f.state<>0";
						//如果OrderStatus大于10的查询条件
						} else if (order.getOrderStatus() > 10) {
							sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
									+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
									+ ") and f.orderType not in(4,5,6,7,8,9,21,22) and f.state<>0 ";
						}else{
							sql += " and f.orderStatus="+ order.getOrderStatus();
						}
					}
				 sql+=" GROUP BY f.orderNo"
				 +" ) g GROUP BY g.mainOrderNo"
				 +" )) f inner join t_member m on m.id=f.memberId left join t_patient_data pd on pd.id=f.patientId left join t_logistics l on l.orderNo=f.orderNo where f.state<>2 ";
		/*if (null != order.getPaymentStatus()) {
			if (order.getPaymentStatus() > 100) {
				sql += " and (locate("+order.getPaymentStatus().toString().substring(0, 1)+",f.paymentStatus)>0 "
						+ " or c("+order.getPaymentStatus().toString().substring(1, 2)+",f.paymentStatus)>0 "
						+ " or locate("+order.getPaymentStatus().toString().substring(2)+",f.paymentStatus)>0 "
						+ ")";
			} else if (order.getPaymentStatus() > 10) {
				sql += " and (locate("+ order.getPaymentStatus().toString().substring(0, 1)+",f.paymentStatus)>0 "
						+ " or locate("+order.getPaymentStatus().toString().substring(1, 2)+",f.paymentStatus)>0 "
						+ ")";
			} else if(order.getPaymentStatus()==5){
				sql += " and locate(" + order.getPaymentStatus()+",f.paymentStatus)>0 and (f.evalId is null or locate(' ',f.evalId)>0) and (f.orderTypes=1 or f.orderTypes=2 or f.orderTypes=3)";
			}else{
				sql += " and locate("+ order.getPaymentStatus()+",f.paymentStatus)>0 ";
			}
		}
		if (!StringUtil.isEmpty(order.getOrderStatus())) {
			if (order.getOrderStatus() > 100) {
				sql += " and (locate("+order.getOrderStatus().toString().substring(0, 1)+",f.orderStatus)>0 "
						+ " or locate("+order.getOrderStatus().toString().substring(1, 2)+",f.orderStatus)>0 "
						+ " or locate("+order.getOrderStatus().toString().substring(2)+",f.orderStatus)>0 "
						+ ") and f.orderType not in(4,5,6,7,8,9,21,22)";
			} else if (order.getOrderStatus() > 10) {
				sql += " and (locate("+ order.getOrderStatus().toString().substring(0, 1)+",f.orderStatus)>0 "
						+ " or locate("+order.getOrderStatus().toString().substring(1, 2)+",f.orderStatus)>0 "
						+ ") and f.orderType not in(4,5,6,7,8,9,21,22) ";
			}else{
				sql += " and locate("+ order.getOrderStatus()+",f.orderStatus)>0 ";
			}
		}*/
		//判断orderNo是否为空
		if (!StringUtil.isEmpty(order.getOrderNo())) {
			sql += " and (f.mainOrderNo like '%" + order.getOrderNo()+"%' or locate('"+order.getOrderNo()+"',f.orderNo)>0 or f.goods like '%" + order.getOrderNo()+"%') ";
		}
		sql += " GROUP BY f.orderNo ORDER BY f.time desc";

		if (null != row && null != page) {
			sql += " limit " + row * (page - 1) + "," + row;
		}
		
		logger.info(sql);

		List<Map<String, Object>> list=null;
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql,new Object[]{memberId,memberId,memberId,memberId,memberId,memberId});
			System.out.println(sql);
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 查询订单数
	 * @param memberId
	 * @param order
	 * @return
	 */
	@Override
	public Integer queryAllOrderListNum(String memberId,Order order) {
		String sql="select count(1) from (SELECT f.* FROM (select 1 recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o.state,o.patientId,o.doctorId,o.orderNo mainOrderNo,o.paymentStatus,o.receiptsPrice,o.orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,e.id evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL goodsIds,o.city,ifnull(o.postage,0)postage,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName,IFNULL(d.docPosition,' '),ods.payPrice,st.name)SEPARATOR '&')goods,NULL type,2 orderTypes FROM t_order o INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo INNER JOIN t_doctor d ON d.doctorId=ods.doctorId inner join t_server_type st on st.id=o.orderType LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo WHERE o.memberId=? and o.orderType not in(21,22) AND o.mainOrderNo IS NULL ";
		//拼接sql
		if (null != order.getPaymentStatus()) {
			//如果PaymentStatus大于100的查询条件
			if (order.getPaymentStatus() > 100) {
				sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
						+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
						+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
						+ ")";
			//如果PaymentStatus大于10的查询条件
			} else if (order.getPaymentStatus() > 10) {
				sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
						+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
						+ ")";
				//如果PaymentStatus等于5的查询条件
			} else if(order.getPaymentStatus()==5){
				sql += " and o.paymentStatus=5 and e.id is null";
			}else{
				sql += " and o.paymentStatus="+order.getPaymentStatus();
			}
		}
		if (!StringUtil.isEmpty(order.getOrderStatus())) {
			//如果OrderStatus大于100的查询条件
			if (order.getOrderStatus() > 100) {
				sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
						+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
						+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
						+ ") and o.orderType not in(4,5,6,7,8,9,21,22)";
				//如果OrderStatus大于10的查询条件
			} else if (order.getOrderStatus() > 10) {
				sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
						+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
						+ ") and o.orderType not in(4,5,6,7,8,9,21,22) ";
			}else{
				sql += " and o.orderStatus="+ order.getOrderStatus();
			}
		}
		 sql+=" GROUP BY o.orderNo UNION"
		 +" SELECT rr.state recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o.state,o.patientId,o.doctorId,o.orderNo mainOrderNo,o.paymentStatus,(o.receiptsPrice-ifnull(o.postage,0)) receiptsPrice,(o.orderPrice-ifnull(o.postage,0)) orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,null evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,null goodsId,o.city,ifnull(o.postage,0)postage,GROUP_CONCAT("
		 +" CONCAT_WS(',',IFNULL(d.doctorId,' '),ifnull(d.docUrl, ' '),IFNULL(d.docName,' '),IFNULL(d.docPosition, ' '),rr.price,'调理方案')SEPARATOR '&')goods,"
		 +" NULL type,4 orderTypes FROM"
		 +" t_order o INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId LEFT JOIN t_doctor d ON d.doctorId=o.doctorId LEFT JOIN t_patient_data pd ON pd.id=o.patientId LEFT JOIN t_evaluate e ON e.orderNo = o.orderNo "
		 +" WHERE o.memberId =? and o.state<>2 and o.receiptsPrice>0  AND o.mainOrderNo IS NULL and rr.isSendUser = 1 ";
		 if (null != order.getPaymentStatus()) {
			 	//如果PaymentStatus大于100的查询条件
				if (order.getPaymentStatus() > 100) {
					sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
							+ ")";
				//如果PaymentStatus大于10的查询条件
				} else if (order.getPaymentStatus() > 10) {
					sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ ")";
				//如果PaymentStatus等于5的查询条件
				} else if(order.getPaymentStatus()==5){
					sql += " and o.paymentStatus=5 and e.id is null and o.orderType in(1,4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and o.paymentStatus="+order.getPaymentStatus();
				}
			}
			if (!StringUtil.isEmpty(order.getOrderStatus())) {
				//如果OrderStatus大于100的查询条件
				if (order.getOrderStatus() > 100) {
					sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
							+ ") and o.orderType not in(4,5,6,7,8,9,21,22)";
					//如果OrderStatus大于10的查询条件
				} else if (order.getOrderStatus() > 10) {
					sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ ") and o.orderType not in(4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and o.orderStatus="+ order.getOrderStatus();
				}
			}
		 sql+=" GROUP BY o.orderNo,o.recordId "
		 +" UNION"
		 +" SELECT 1 recordState,f.isCustomer,f.consignee,f.supplierId,f.memberId,f.orderType,f.state,f.patientId,f.doctorId,f.orderNo mainOrderNo,f.paymentStatus,(f.receiptsPrice-ifnull(f.postage,0)) receiptsPrice,(f.orderPrice-ifnull(f.postage,0)) orderPrice,f.orderNo,f.time,f.evalId,f.orderStatus,f.payTime,f.payState,GROUP_CONCAT(f.goodsId)goodsIds,f.city,ifnull(f.postage,0)postage,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f.name,f.price,f.goodsNum,f.picture,f.specifications,f.supplierId) separator '&') goods,NULL type,1 orderTypes FROM (SELECT o.isCustomer,o.consignee,o.orderType,o.state,o.patientId,o.doctorId,o.paymentStatus,GROUP_CONCAT(gds.name ORDER BY gds.rank separator ' ') specifications,g.name,s.id supplierId,g.id goodsId,od.goodsNum,e.id evalId,o.city,o.postage,od.goodsOriginalPrice price,IF(gp.picture IS NULL or ''=gp.picture or 'null'=gp.picture,g.picture,gp.picture) picture,o.memberId,o.receiptsPrice, o.orderPrice, o.type,o.orderNo, FROM_UNIXTIME( o.createTime, '%Y-%m-%d %H:%i' )time, o.orderStatus, FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime, CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState FROM t_order o INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId LEFT JOIN t_supplier s on s.id=g.supplierId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id AND gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE o.state<>2 AND o.orderType=1 AND o.mainOrderNo IS NULL GROUP BY o.orderNo,gp.id) f WHERE f.memberId=? ";
		 if (null != order.getPaymentStatus()) {
			 	//如果PaymentStatus大于100的查询条件
				if (order.getPaymentStatus() > 100) {
					sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
							+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(2)
							+ ")";
				//如果PaymentStatus大于10的查询条件
				} else if (order.getPaymentStatus() > 10) {
					sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
							+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ ")";
				//如果PaymentStatus等于5的查询条件
				} else if(order.getPaymentStatus()==5){
					sql += " and f.paymentStatus=5 and f.evalId is null";
				}else{
					sql += " and f.paymentStatus="+order.getPaymentStatus();
				}
			}
			if (!StringUtil.isEmpty(order.getOrderStatus())) {
				//如果OrderStatus大于100的查询条件
				if (order.getOrderStatus() > 100) {
					sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ " or f.orderStatus="+order.getOrderStatus().toString().substring(2)
							+ ") and f.orderType not in(4,5,6,7,8,9,21,22)";
				//如果OrderStatus大于10的查询条件
				} else if (order.getOrderStatus() > 10) {
					sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ ") and f.orderType not in(4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and f.orderStatus="+ order.getOrderStatus();
				}
			}
		 sql+=" GROUP BY f.orderNo UNION("
		 +" SELECT GROUP_CONCAT(IFNULL(g.recordState,' ') SEPARATOR '|||') recordState,GROUP_CONCAT(IFNULL(g.isCustomer,' ') SEPARATOR '|||') isCustomer,GROUP_CONCAT(IFNULL(g.consignee,' ') SEPARATOR '|||') consignee,g.supplierId,g.memberId,GROUP_CONCAT(IFNULL(g.orderType,' ') SEPARATOR '|||') orderType,g.state,g.patientId,g.doctorId,g.mainOrderNo,GROUP_CONCAT(IFNULL(g.paymentStatus,' ') SEPARATOR '|||') paymentStatus,GROUP_CONCAT(IFNULL(g.receiptsPrice,' ') SEPARATOR '|||') receiptsPrice,GROUP_CONCAT(IFNULL(g.orderPrice,' ') SEPARATOR '|||') orderPrice,GROUP_CONCAT(IFNULL(g.orderNo,' ') SEPARATOR '|||') orderNo,GROUP_CONCAT(IFNULL(g.time,' ') SEPARATOR '|||') time,"
		 +" GROUP_CONCAT(IFNULL(g.evalId,' ') SEPARATOR '|||') evalId,GROUP_CONCAT(IFNULL(g.orderStatus,' ') SEPARATOR '|||') orderStatus,GROUP_CONCAT(IFNULL(g.payTime,' ') SEPARATOR '|||') payTime,GROUP_CONCAT(IFNULL(g.payState,' ') SEPARATOR '|||') payState,GROUP_CONCAT(IFNULL(g.goodsIds,' ') SEPARATOR '|||') goodsIds,GROUP_CONCAT(IFNULL(g.city,' ') SEPARATOR '|||') city,GROUP_CONCAT(IFNULL(g.postage,0) SEPARATOR '|||') postage,GROUP_CONCAT(IFNULL(g.goods,' ') SEPARATOR '|||') goods,GROUP_CONCAT(IFNULL(g.type,' ') SEPARATOR '|||') type,"
		 +" 3 orderTypes FROM ("
		 +" SELECT rr.state recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o1.state,o1.patientId,o1.doctorId,o1.orderNo mainOrderNo,o.paymentStatus,(o.receiptsPrice - ifnull(o.postage,0)) receiptsPrice,(o.orderPrice - ifnull(o.postage,0))orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,-1 evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL goodsIds,o.city,o.postage,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName,IFNULL(d.docPosition,' '),rr.price,'调理方案')SEPARATOR '&')goods,3 type FROM t_order o1 INNER JOIN t_order o ON o.mainOrderNo=o1.orderNo INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId LEFT JOIN t_doctor d ON d.doctorId=rr.doctorId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo WHERE o.memberId=? and o.state<>2 and o.receiptsPrice>0 and rr.isSendUser=1 ";
		 if (null != order.getPaymentStatus()) {
			 	//如果PaymentStatus大于100的查询条件
				if (order.getPaymentStatus() > 100) {
					sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
							+ ")";
				//如果PaymentStatus大于10的查询条件
				} else if (order.getPaymentStatus() > 10) {
					sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ ")";
				//如果PaymentStatus等于5的查询条件
				} else if(order.getPaymentStatus()==5){
					sql += " and o.paymentStatus=5 and e.id is null and o.orderType in(1,4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and o.paymentStatus="+order.getPaymentStatus();
				}
			}
			if (!StringUtil.isEmpty(order.getOrderStatus())) {
				//如果OrderStatus大于100的查询条件
				if (order.getOrderStatus() > 100) {
					sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
							+ ") and o.orderType not in(4,5,6,7,8,9,21,22)";
				//如果OrderStatus大于10的查询条件
				} else if (order.getOrderStatus() > 10) {
					sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ ") and o.orderType not in(4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and o.orderStatus="+ order.getOrderStatus();
				}
			}
		 sql+=" GROUP BY o.orderNo "
		 +" UNION"
		 +" SELECT 1 recordState,o.isCustomer,o.consignee,'' supplierId,o.memberId,o.orderType,o1.state,o1.patientId,o1.doctorId,o1.orderNo mainOrderNo,o.paymentStatus,o.receiptsPrice,o.orderPrice,o.orderNo,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,e.id evalId,o.orderStatus,FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL goodsIds,o.city,o.postage,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName,IFNULL(d.docPosition,''),ifnull(o.receiptsPrice,0),st.name)SEPARATOR '&')goods,2 type FROM t_order o1 INNER JOIN t_order o ON o.mainOrderNo=o1.orderNo INNER JOIN t_order o2 on o2.mainOrderNo=o1.orderNo and o2.recordId is not null inner join t_recipe_record rr on rr.recordId=o2.recordId INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo INNER JOIN t_doctor d ON d.doctorId=ods.doctorId inner join t_server_type st on st.id=o.orderType LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo WHERE o.memberId=? AND o.receiptsPrice>0 and o.state<>2 and o.orderType not in(21,22) and rr.isSendUser=1 ";
		 if (null != order.getPaymentStatus()) {
			 	//如果PaymentStatus大于100的查询条件
				if (order.getPaymentStatus() > 100) {
					sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(2)
							+ ")";
				//如果PaymentStatus大于10的查询条件
				} else if (order.getPaymentStatus() > 10) {
					sql += " and (o.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
							+ " or o.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ ")";
				//如果PaymentStatus等于5的查询条件
				} else if(order.getPaymentStatus()==5){
					sql += " and o.paymentStatus=5 and e.id is null and rr.state<>0 ";
				}else{
					sql += " and o.paymentStatus="+order.getPaymentStatus();
				}
			}
			if (!StringUtil.isEmpty(order.getOrderStatus())) {
				//如果OrderStatus大于100的查询条件
				if (order.getOrderStatus() > 100) {
					sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(2)
							+ ") and o.orderType not in(4,5,6,7,8,9,21,22)";
				//如果OrderStatus大于10的查询条件
				} else if (order.getOrderStatus() > 10) {
					sql += " and (o.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or o.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ ") and o.orderType not in(4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and o.orderStatus="+ order.getOrderStatus();
				}
			}
		 sql+=" GROUP BY o.orderNo"
		 +" UNION"
		 +" SELECT 1 recordState,f.isCustomer,f.consignee,f.supplierId,f.memberId,f.orderType,f.state,f.patientId,f.doctorId,f.mainOrderNo,f.paymentStatus,(f.receiptsPrice-ifnull(f.postage,0)) receiptsPrice,(f.orderPrice-ifnull(f.postage,0)) orderPrice,f.orderNo,f.time,f.evalId,f.orderStatus,f.payTime,f.payState,GROUP_CONCAT(f.goodsId)goodsIds,f.city,f.postage,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f.name,f.price,f.goodsNum,f.picture,f.specifications,f.supplierId) separator '&') goods,1 type FROM (SELECT o.isCustomer,o.consignee,o.orderType,o.state,o.patientId,o.doctorId,o1.orderNo mainOrderNo,o.paymentStatus,GROUP_CONCAT(gds.name ORDER BY gds.rank separator ' ') specifications,g.name,s.id supplierId,g.id goodsId,od.goodsNum,e.id evalId,o.city,o.postage,od.goodsOriginalPrice price,IF(gp.picture IS NULL or ''=gp.picture or 'null'=gp.picture,g.picture,gp.picture) picture,o.memberId,o.receiptsPrice, o.orderPrice, o.type,o.orderNo, FROM_UNIXTIME( o.createTime, '%Y-%m-%d %H:%i' )time, o.orderStatus, FROM_UNIXTIME(o.payTime, '%Y-%m-%d %H:%i')payTime, CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState FROM t_order o1 INNER JOIN  t_order o ON o1.orderNo=o.mainOrderNo INNER JOIN t_order o2 on o2.mainOrderNo=o1.orderNo and o2.recordId is not null inner join t_recipe_record rr on rr.recordId=o2.recordId INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId LEFT JOIN t_supplier s on s.id=g.supplierId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id AND gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE o.state=1 AND o.orderType=1 and  o1.state<>2 and rr.isSendUser=1  AND o1.mainOrderNo IS NULL GROUP BY o.orderNo,gp.id) f WHERE f.memberId=? ";
		 if (null != order.getPaymentStatus()) {
			//如果PaymentStatus大于100的查询条件	
			 if (order.getPaymentStatus() > 100) {
					sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0, 1)
							+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(2)
							+ ")";
				//如果PaymentStatus大于10的查询条件
				} else if (order.getPaymentStatus() > 10) {
					sql += " and (f.paymentStatus="+order.getPaymentStatus().toString().substring(0,1)
							+ " or f.paymentStatus="+order.getPaymentStatus().toString().substring(1,2)
							+ ")";
				//如果PaymentStatus等于5的查询条件
				} else if(order.getPaymentStatus()==5){
					sql += " and f.paymentStatus=5 and f.evalId is null";
				}else{
					sql += " and f.paymentStatus="+order.getPaymentStatus();
				}
			}
			if (!StringUtil.isEmpty(order.getOrderStatus())) {
				//如果OrderStatus大于100的查询条件
				if (order.getOrderStatus() > 100) {
					sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ " or f.orderStatus="+order.getOrderStatus().toString().substring(2)
							+ ") and f.orderType not in(4,5,6,7,8,9,21,22)";
				//如果OrderStatus大于10的查询条件
				} else if (order.getOrderStatus() > 10) {
					sql += " and (f.orderStatus="+order.getOrderStatus().toString().substring(0,1)
							+ " or f.orderStatus="+order.getOrderStatus().toString().substring(1,2)
							+ ") and f.orderType not in(4,5,6,7,8,9,21,22) ";
				}else{
					sql += " and f.orderStatus="+ order.getOrderStatus();
				}
			}
		 sql+=" GROUP BY f.orderNo"
				 +" ) g GROUP BY g.mainOrderNo"
				 +" )) f inner join t_member m on m.id=f.memberId left join t_patient_data pd on pd.id=f.patientId left join t_logistics l on l.orderNo=f.orderNo where f.state<>2 ";
		
		sql += " GROUP BY f.orderNo ORDER BY f.time desc) g";

		Integer i =0;
		try {
			//执行查询
			i = jdbcTemplate.queryForObject(sql,new Object[]{memberId,memberId,memberId,memberId,memberId,memberId},Integer.class);
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 支付完成修改有效期
	 * @param orderDetailServer
	 * @return
	 */
	@Override
	public Integer updateOrderDetailServer(OrderDetailServer orderDetailServer) {
		Integer i = 0;
		try {
			String sql = "UPDATE t_order_detail_server set validityTime=? where orderNo=? and validityTime is null ";
			if (!StringUtil.isEmpty(orderDetailServer.getOrderNo())) {
				//执行更新
				i = super.update(sql,
						new Object[] { orderDetailServer.getValidityTime(),
								orderDetailServer.getOrderNo() });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 根据订单号查询该订单是否支付
	 * @param orderNo
	 * @return
	 */
	@Override
	public Integer queryPayResultByOrderNo(String orderNo) {
		Integer count = 0;
		try {
			String sql = "SELECT count(1) from t_order_pay_record where orderNo=? ";
			if (!StringUtil.isEmpty(orderNo)) {
				//执行查询
				count = super.queryBysqlCount(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return count;
	}

	/**
	 * 插入订单
	 * @param order
	 * @return
	 */
	@Override
	public Object insertOrder(Order order) {
		Object i = null;
		try {
			//拼接sql
			String sql = "INSERT INTO t_order(orderNo,orderPrice,receiptsPrice,goodsPrice,orderStatus,paymentStatus,orderNote,memberId,orderType,createTime,sourceOrderNo,patientId,recordId,mainOrderNo,doctorId,freight) values ";
			if (order != null) {
				sql += " ('" + order.getOrderNo() + "',"
						+ order.getOrderPrice() + ","
						+ order.getReceiptsPrice() + ","
						+ order.getGoodsPrice() + ",1,1,'"
						+ order.getOrderNote() + "','" + order.getMemberId()
						+ "','" + order.getOrderType() + "',"
						+ Util.queryNowTime() + ",'" + order.getSourceOrderNo()
						+ "','" + order.getPatientId() + "','"
						+ order.getRecordId() + "','" + order.getMainOrderNo()
						+ "','" + order.getDoctorId() + "',"
						+ order.getFreight() + ") ";
				//执行插入
				i = jdbcTemplate.update(sql);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return i;
	}

	/**
	 * 根据主订单号查询子订单号
	 * @param mainOrderNo
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryChildOrderNos(String mainOrderNo) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			if(!StringUtil.isEmpty(mainOrderNo)){
				String sql = "SELECT o.*,p.`name` patientName,p.phone patientPhone,d.docName,d.docPhone from t_order o left JOIN t_patient_data p on p.id = o.patientId LEFT JOIN t_doctor d on d.doctorId = o.doctorId where o.mainOrderNo='"+mainOrderNo+"' ";
				//执行查询
				list = jdbcTemplate.queryForList(sql);
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return list;
	}

	/**
	 * 查询医患关系是否存在
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	@Override
	public Integer queryDoctorPatientRelationship(String doctorId,
			String patientId) {
		Integer count = 0;
		try {
			if(!StringUtil.isEmpty(doctorId)&&!StringUtil.isEmpty(patientId)){
				String sql = "SELECT count(1) from t_doctor_patient where doctorId='"+doctorId+"' and patientId='"+patientId+"' ";
				//执行查询
				count = super.queryBysqlCount(sql, null);
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return count;
	}

	/**
	 * 插入医患关系
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	@Override
	public Object insertDoctorPatientRelationship(String doctorId,String memberId,
			String patientId) {
		Object id = null;
		try {
			String sql = "INSERT INTO t_doctor_patient(doctorId,memberId,patientId) VALUES('"+doctorId+"','"+memberId+"','"+patientId+"')";
			//执行插入
			id = jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.info(e);
		}
		return id;
	}

	/**
	 * 查询导入人员
	 * @param openId
	 * @return
	 */
	@Override
	public Map<String, Object> queryFollowIdByOpenId(String openId) {
		String sql="select fh.followId,d.docName,d.docPhone,sr.name srName,sr.phone,IFNULL(d.customYztId,d.defaultYztId) yztId from t_follow_history fh inner join t_doctor d on d.doctorId=fh.openId inner join t_sales_represent sr on sr.srId=fh.followId where fh.openId=? ORDER BY fh.followTime DESC LIMIT 0,1 ";
		Map<String, Object> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{openId});
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}

	/**
	 * 查询要发送呼叫中心数据
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> querySendCallCenterData(String orderNo) {
		Map<String, Object> map=null;
		try {
			//拼接sql
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
				//执行查询
				map = jdbcTemplate.queryForMap(sql,new Object[]{orderNo});
			}
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}
	
	/**
     * 根据主订单号查询药品订单
     * @param mainOrderNo
     * @return
     */
	@Override
	public Map<String, Object> queryRecordOrderByMainOrderNo(String mainOrderNo) {
		String sql="select rr.state,o.state orderState from t_order o inner join t_recipe_record rr on rr.recordId=o.recordId where (o.mainOrderNo=? or o.orderNo=?)";
		Map<String, Object> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{mainOrderNo,mainOrderNo});
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}
	
	/**
	 * 查询订单过期时间
	 * @param mainOrderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryVisitTimeyOrderNo(String orderNo) {
		String sql="select rr.visitTime from t_order o inner join t_recipe_record rr on o.recordId=rr.recordId where o.orderNo=?";
		Map<String, Object> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{orderNo});
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}
	
	/**
	 * 更新抄方订单
	 * @param patientId
	 * @param doctorId
	 * @param orderNo
	 * @return
	 */
	@Override
	public Integer updateCFOrderNo(String patientId,String doctorId,String orderNo) {
		String sql="update t_order set paymentStatus=5,orderStatus=5 where patientId=? and doctorId=? and orderNo<>? and orderType in (4,5,6,7,8,9,21,22) and paymentStatus = 2 ";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{patientId,doctorId,orderNo});
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return i;
	}
	
	 /**
     * 查询订单详情
     * @param orderNo
     * @return
     */
	@Override
	public Map<String, Object> queryOrderDetail(String orderNo) {
		String sql="SELECT e.id evalId,o.state,IFNULL(rr.state,1) recordState,o.createTime,m.realname,m.headicon,o.memberId,o.orderType,1 num,o.postage,st.name serverName,o.consignee,o.phone consigneePhone,o.province,o.city,o.area,o.detailedAddress,o.mainOrderNo,o.orderNo,o.receiptsPrice,o.orderPrice,d.docName,d.docUrl photo,d.docPosition,o.paymentStatus,o.orderStatus,e.id,o.isCustomer,pd.name,pd.phone,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,CASE o1.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END mainPayState FROM t_order o LEFT JOIN t_order o1 ON o1.orderNo=o.mainOrderNo LEFT JOIN t_order o2 ON o2.mainOrderNo=o1.orderNo and o2.recordId is not null LEFT JOIN t_recipe_record rr ON rr.recordId=o2.recordId INNER JOIN t_server_type st ON st.id=o.orderType INNER JOIN t_doctor d ON d.doctorId=o.doctorId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo LEFT JOIN t_patient_data pd ON pd.id=o.patientId inner join t_member m on m.id=o.memberId where o.orderNo=?" +
				  " UNION "
				  +" SELECT 1 evalId,o.state,rr.state recordState,o.createTime,m.realname,m.headicon,o.memberId,o.orderType,count(1) num,o.postage,'' serverName,o.consignee,o.phone consigneePhone,o.province,o.city,o.area,o.detailedAddress,o.mainOrderNo,o.orderNo,o.receiptsPrice,o.orderPrice,d.docName,d.docUrl photo,d.docPosition,o.paymentStatus,o.orderStatus,e.id,o.isCustomer,pd.name,pd.phone,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i')time,CASE o.orderStatus WHEN 0 THEN '待配药' WHEN 1 THEN '待发货' WHEN 2 THEN '配送中' WHEN 5 THEN '订单完成'  WHEN 6 THEN '已取消' END payState,CASE o1.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END mainPayState FROM t_order o INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId inner join t_conditioning_record cr on cr.recordId=rr.recordId LEFT JOIN t_order o1 ON o1.orderNo=o.mainOrderNo LEFT JOIN t_doctor d ON d.doctorId=o.doctorId LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo LEFT JOIN t_patient_data pd ON pd.id=o.patientId inner join t_member m on m.id=o.memberId where (o.orderNo=? or o.mainOrderNo=?) group by o.orderNo";
		Map<String, Object> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{orderNo,orderNo,orderNo});
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}
	
	/**
	 * 添加日志
	 * @param request
	 * @param modular
	 * @param details
	 * @param orderNo
	 * @param operaStr
	 * @param openId
	 * @return
	 */
	@Override
	public Object addCreateLog(HttpServletRequest request,String modular,String details,String orderNo,Integer operaStr,String openId) {
		if(StringUtil.isEmpty(orderNo)){
			orderNo = "";
		}
		String ip = "";
		if(request!=null){
			ip = Util.getIp(request);
		}
		String sql = "INSERT INTO t_operalog(orderNo,modular,details,createUser,createTime,createType,ip) VALUES('"+orderNo+"','"+modular+"','"+details+"','"+openId+"',"+Util.queryNowTime()+","+operaStr+",'"+ip+"') ";
		//执行插入
		Object i = jdbcTemplate.update(sql);
		return i;
	}

	/**
	 * 根据订单号查询订单信息
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryOrderByOrderNo(String orderNo) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * from t_order where orderNo=? ";
			if(!StringUtil.isEmpty(orderNo)){
				//执行插入
				map = super.queryBysqlMap(sql, new Object[]{orderNo});
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return map;
	}

	/**
	 * 查询患者 信息
	 * @param patientId
	 * @return
	 */
	@Override
	public Map<String, Object> queryPatientById(String patientId) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * from t_patient_data where id=? ";
			if(!StringUtil.isEmpty(patientId)){
				//执行查询
				map = super.queryBysqlMap(sql, new Object[]{patientId});
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return map;
	}

	/**
	 * 查询锦旗信息
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> queryMyEvaBannerById(String id) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * from t_my_eva_banner where id=? ";
			if(!StringUtil.isEmpty(id)){
				//执行查询
				map = super.queryBysqlMap(sql, new Object[]{id});
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return map;
	}
	
	/**
	 * 查询服务订单 
	 * @param orderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryServerOrder(String orderNo) {
		//拼接sql
		String sql = "SELECT o.receiptsPrice, pd. NAME patientName, pd.phone, IF (0 = pd.sex, '男', '女') sex, pd.age, ifnull(d.docName, '上医') docName,o.createTime, "
					+" (SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departName "
					+" FROM t_order o "
					+" INNER JOIN t_patient_data pd ON pd.id = o.patientId "
					+" INNER JOIN t_doctor d ON d.doctorId = o.doctorId "
					+" WHERE o.orderNo =?";
		Map<String, Object> map = null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql, new Object[] { orderNo });
		} catch (Exception e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询导流人员
	 */
	@Override
	public Map<String, Object> queryFollowByOpenId(String openId) {
		String sql = "SELECT followId from t_follow_history where openId=? ORDER BY followTime DESC LIMIT 0,1";
		Map<String, Object> map = null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql, new Object[] { openId });
		} catch (Exception e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 查询退款新信息
	 */
	@Override
	public Integer queryRefundApply(String orderNo, String detailId) {
		Integer i = 0;
		try {
			//拼接sql
			String sql = "SELECT count(1) from t_refundapply_record where `status`=0 and orderNo=? ";
			if (!StringUtil.isEmpty(detailId)) {
				sql += " and detailId='" + detailId + "' ";
			}
			//执行查询
			i = super.queryBysqlCount(sql, new Object[] { orderNo });
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return i;
	}
	
	/**
	 * 更新订单信息
	 */
	@Override
	public Integer updateOrder(Order order) {
		Integer i = 0;
		try {
			//拼接sql
			String sql = "UPDATE t_order set paymentStatus=? ";
			if (!StringUtil.isEmpty(order.getOrderNo())) {
				if (order.getOrderStatus() != null) {
					sql += " ,orderStatus='" + order.getOrderStatus() + "' ";
				}
				if (!StringUtil.isEmpty(order.getRefundNote())) {
					sql += " ,refundNote='" + order.getRefundNote() + "' ";
				}
				if (order.getState() != null) {
					sql += " ,state=" + order.getState() + " ";
				}
				sql += "  where orderNo=? ";
				if (order.getPaymentStatus() != null
						&& order.getPaymentStatus() == 6) {
					sql += " and paymentStatus in(0,1) ";
				}
				if (order.getState() != null && order.getState() == 2) {
					sql += " and paymentStatus in(4,5,6,7,9) ";
				}
				//执行更新
				i = super.update(sql, new Object[] { order.getPaymentStatus(),
						order.getOrderNo() });
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return i;
	}
	
	/**
	 * 查询退款推送内容
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> queryApplyRecordById(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT a.*,m.openid,m.loginname,m.token,m.realname name,m.token,m.demodel,m.detype from t_refundapply_record a "
					+ " INNER JOIN t_order o on o.orderNo = a.orderNo "
					+ " INNER JOIN t_member m on m.id= o.memberId "
					+ " where a.`status`=0 and a.id=?  ";
			if (id != null) {
				//执行查询
				map = super.queryBysqlMap(sql, new Object[] { id });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
	/**
	 * 获取审批退款最底层审核人信息
	 * @return
	 */
	@Override
	public Map<String, Object> getMinStreamId() {
		Map<String, Object> map = null;
		try {
			String sql = " select * from t_audit_stream where FIND_IN_SET(id, queryChildrenAuditInfo(0)) ";
			//执行查询
			map = super.queryBysqlMap(sql, null);
		} catch (Exception e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询审核人信息
	 * @param userids
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryAuditUsers(String[] userids,
			Integer menuId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (menuId != null) {
				//拼接sql
				String sql = " SELECT dd.* from (SELECT u.* from t_user u "
						+ " INNER JOIN t_user_role ur on ur.user_id = u.id "
						+ " INNER JOIN t_role_menu r on r.roleId = ur.role_id and r.menuId='"
						+ menuId + "') dd " + " where dd.id in (  ";
				if (userids.length > 0) {
					for (int i = 0; i < userids.length; i++) {
						if (i == userids.length - 1) {
							sql += "'" + userids[i] + "'";
						} else {
							sql += "'" + userids[i] + "'" + ",";
						}
					}
				}
				sql += " ) and dd.account<>'admin' GROUP BY dd.id ";
				//执行查询
				list = super.queryBysqlList(sql, null);
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return list;
	}
	
	/**
	 * 获取所有拥有确认权限的用户
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryApplyRoleIds(Integer orderType) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//拼接sql
			String sql = "SELECT u.* from t_user u "
					+ " INNER JOIN t_user_role ur on ur.user_id = u.id "
					+ " INNER JOIN t_role_menu r on r.roleId = ur.role_id ";
			/*if (orderType != null) {
				if (orderType == 4) {// 挂号
					sql += " and r.menuId in(43,73) ";
				} else if (orderType == 5) {// 陪诊
					sql += " and r.menuId=23 ";
				} else if (orderType == 6) {// 会诊
					sql += " and r.menuId=19 ";
				} else if (orderType == 7) {// 住院
					sql += " and r.menuId=76 ";
				} else if (orderType == -1) {// 住院
					sql += " and r.menuId=97 ";
				}
			}*/
			sql += " and r.menuId=216 ";
			sql += " where u.state=1 and u.account<>'admin' GROUP BY u.id ";
			//执行查询
			list = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 根据医生ID查询医生之前是否开过单
	 * @param doctorId
	 * @return
	 */
	@Override
	public Integer queryRecordCountByDoctorId(String doctorId,String orderNo) {
		Integer count = 0;
		try {
			String sql = "SELECT count(0) from t_order o left JOIN t_recipe_record r on r.orderNo =o.orderNo where o.orderType in (10,13,14,15,16,17) and o.paymentStatus in (2,3,5,8,9) and o.doctorId=? and o.orderNo<>?";
			if(!StringUtil.isEmpty(doctorId)&&!StringUtil.isEmpty(orderNo)){
				//执行查询
				count = super.queryBysqlCount(sql, new Object[]{doctorId,orderNo});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	/**
	 * 根据月份查询开单总额
	 * @return
	 */
	@Override
	public Map<String, Object> queryTotalMoney(String doctorId,String months,String date,String firstDate) {
		Map<String,Object> map = null;
		try {
			//拼接sql
			String sql = " SELECT SUM(o.receiptsPrice) receiptsPrice from t_order o left JOIN t_recipe_record r on r.orderNo =o.orderNo where o.orderType in (10,13,14,15,16,17) and o.paymentStatus in(2,3,5,9) and o.doctorId=?  ";
			if(!StringUtil.isEmpty(doctorId)){
				 if(!StringUtil.isEmpty(months)){
					 sql +=" and FROM_UNIXTIME(payTime,'%Y-%m')='"+months+"' ";
				 }
				 if(!StringUtil.isEmpty(date)&&!StringUtil.isEmpty(firstDate)){
					 sql +=" and FROM_UNIXTIME(payTime,'%Y-%m-%d')>='"+firstDate+"' and FROM_UNIXTIME(payTime,'%Y-%m-%d')<='"+date+"' ";
				 }
				 System.out.println(sql);
				 //查询sql
				 map = super.queryBysqlMap(sql, new Object[]{doctorId});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}

	/**
	 * 查询本月是否已发送短信
	 * @param month
	 * @param phone
	 * @return
	 */
	@Override
	public Integer queryIsSendByPhone(String month, String phone) {
		Integer count = 0;
		try {
			String sql = "SELECT count(0) from t_send_sms_record where sendMonth=? and sendPhone=? ";
			if(!StringUtil.isEmpty(month)&&!StringUtil.isEmpty(phone)){
				//查询sql
				count = super.queryBysqlCount(sql, new Object[]{month,phone});
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	/**
	 * 插入发送记录
	 * @param month
	 * @param sendDate
	 * @param phone
	 * @param type
	 * @return
	 */
	@Override
	public Object insertSendSmsRecord(String month, String sendDate,
			String phone, Integer type,String orderNo) {
		Object i = 0;
		try {
			String sql = "INSERT INTO t_send_sms_record(sendDate,sendPhone,type,sendMonth,orderNo) values('"+sendDate+"','"+phone+"',"+type+",'"+month+"','"+orderNo+"') ";
			//执行插入
			i = jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 根据主订单号查询药品订单
	 * @param mainOrderNo
	 * @return
	 */
	@Override
	public Map<String, Object> queryRecordOrderSmsByMainOrderNo(
			String mainOrderNo) {
		String sql="select o.* from t_order o inner join t_recipe_record rr on rr.recordId=o.recordId where (o.mainOrderNo=? or o.orderNo=?)";
		Map<String, Object> map=null;
		try {
			//执行查询
			map = jdbcTemplate.queryForMap(sql,new Object[]{mainOrderNo,mainOrderNo});
		} catch (DataAccessException e) {
			logger.info(e);
		}
		return map;
	}
}
