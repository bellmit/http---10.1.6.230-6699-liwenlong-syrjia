package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Order;
import cn.syrjia.hospital.entity.OrderDetailServer;

public interface OrderDao extends BaseDaoInterface {
	
	/**
	 * 支付页面查询订单号、订单价格
	 * @param orderNo
	 * @param memberId
	 * @return
	 */
	public abstract Map<String,Object> queryPayOrderDetail(Order order);
	
	/**
	 * 根据订单号查询该订单是否支付
	 * @param orderNo
	 * @return
	 */
	public abstract Integer queryPayResultByOrderNo(String orderNo);

	/**
	 * 获取订单号
	 * @return
	 */
	public abstract String orderNo();
	
	/**
	 * 医生详情判断是否有未完成同类型订单使用
	 * @return
	 */
	public abstract Integer queryOrderCountByOrderType(Order order);
	
	/**
	 * 插入订单
	 * @param order
	 * @return
	 */
	public abstract Object insertOrder(Order order);
	
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
	public abstract Integer updateOrderStatus(Integer paystatus,Integer orderStatus,String tradeNo,Integer payway,String orderNo,Double receiptsPrice,String followId,Integer orderWay,String yztId);
	
	/**
	 * 更新调理方状态
	 * @param recordId
	 * @return
	 */
	public abstract Integer updateRecordStatus(String recordId);

	/**
	 * 根据主订单号查询子订单号
	 * @param mainOrderNo
	 * @return
	 */
	public abstract List<Map<String,Object>> queryChildOrderNos(String mainOrderNo);
	
	/**
	 * 插入医患关系
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Object insertDoctorPatientRelationship(String doctorId,String memberId,String patientId);
	
	/**
	 * 查询医患关系是否存在
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	public abstract Integer queryDoctorPatientRelationship(String doctorId,String patientId);
	
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
	public Integer addPayment(String orderNo, String total_amount,
			String buyer_id, String trade_no, String trade_status);
	
	/**
	 * 获取产品类推送参数
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryGoodsDetail(String orderNo);
	
	/**
	 * 查询商品供应商
	 * @param orderNo
	 * @return
	 */
	public List<Map<String, Object>> queryGoodsSupplier(String orderNo);
	
	/**
	 * 查询订单列表
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	abstract List<Map<String,Object>> queryOrder(Order order,Integer page,Integer row);
	
	/**
	 * 查询退款订单详情
	 * @param request
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRefundOrderDetail(String orderNo,String memberId);
	
	/**
	 * 根据订单号查询订单信息
	 * @param order
	 * @return
	 */
	abstract Map<String,Object> queryOrderByOrderNo(Order order);
	
	/**
	 * 更新发货时间
	 * @param orderNo
	 * @return
	 */
	abstract Integer updateDeliveryTime(String orderNo);
	
	/**
	 * 根据订单号查询订单详情
	 * @param orderNo
	 * @return
	 */
	abstract List<Map<String,Object>> queryOrderDetailByOrderNo(String orderNo);
	
	/**
	 * 查询退款进度
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRefundProgress(String orderNo);
	
	/**
	 * 查询未完成订单
	 * @return
	 */
	public abstract List<Map<String,Object>> queryNoFinishOrders();
	
	/**
	 * 查询未完成的历史订单
	 * @return
	 */
	public abstract List<Map<String,Object>> queryNoFinishHosOrder();

	 /**
     * 查询订单列表 包含所有类型订单
     * @param memberId
     * @param order
     * @param row
     * @param page
     * @return
     */
	public abstract List<Map<String, Object>> queryAllOrderList(String memberId,
			Order order, Integer row, Integer page);
	
	/**
	 * 查询订单数
	 * @param memberId
	 * @param order
	 * @return
	 */
	public abstract Integer queryAllOrderListNum(String memberId,Order order);
	
	/**
	 * 支付完成修改有效期
	 * @param orderDetailServer
	 * @return
	 */
	public abstract Integer updateOrderDetailServer(OrderDetailServer orderDetailServer);
	
	/**
	 * 查询导入人员
	 * @param openId
	 * @return
	 */
	public abstract Map<String,Object> queryFollowIdByOpenId(String openId);
	
	/**
	 * 查询要发送呼叫中心数据
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> querySendCallCenterData(String orderNo);
	
	/**
     * 根据主订单号查询药品订单
     * @param mainOrderNo
     * @return
     */
	public abstract Map<String, Object> queryRecordOrderByMainOrderNo(String mainOrderNo);
	
	/**
	 * 查询订单过期时间
	 * @param mainOrderNo
	 * @return
	 */
	public abstract Map<String, Object> queryVisitTimeyOrderNo(String mainOrderNo);
	
	/**
	 * 更新抄方订单
	 * @param patientId
	 * @param doctorId
	 * @param orderNo
	 * @return
	 */
	public abstract Integer updateCFOrderNo(String patientId,String doctorId,String orderNo);
	
	 /**
     * 查询订单详情
     * @param orderNo
     * @return
     */
	public abstract Map<String,Object> queryOrderDetail(String orderNo);
	
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
	public abstract Object addCreateLog(HttpServletRequest request,String modular,String details,String orderNo,Integer operaStr,String openId);

	/**
	 * 根据订单号查询订单信息
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOrderByOrderNo(String orderNo);
	
	/**
	 * 查询患者 信息
	 * @param patientId
	 * @return
	 */
	public abstract Map<String,Object> queryPatientById(String patientId);
	
	/**
	 * 查询锦旗信息
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryMyEvaBannerById(String id);
	
	/**
	 * 查询服务订单 
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryServerOrder(String orderNo);
	
	/**
	 * 根据openId查询导流人
	 * @param openId
	 * @return
	 */
	public abstract Map<String,Object> queryFollowByOpenId(String openId);
	
	/**
	 * 查询退款信息是否存在
	 * @param orderNo
	 * @param detailId
	 * @return
	 */
	public abstract Integer queryRefundApply(String orderNo, String detailId);
	
	/**
	 * 修改订单状态
	 * @param order
	 * @return
	 */
	public abstract Integer updateOrder(Order order);
	
	/**
	 * 查询退款推送内容
	 * @param id
	 * @return
	 */
	public abstract Map<String, Object> queryApplyRecordById(Integer id);
	
	/**
	 * 获取审批退款最底层审核人信息
	 * @return
	 */
	public abstract Map<String,Object> getMinStreamId();
	
	/**
	 * 查询审核人信息
	 * @param userids
	 * @return
	 */
	public abstract List<Map<String,Object>> queryAuditUsers(String[] userids,Integer menuId);

	/**
	 * 获取所有拥有确认权限的用户
	 * @return
	 */
	public abstract List<Map<String,Object>> queryApplyRoleIds(Integer orderType);
	
	/**
	 * 根据医生ID查询医生之前是否开过单
	 * @param doctorId
	 * @return
	 */
	public abstract Integer queryRecordCountByDoctorId(String doctorId,String orderNo);
	
	/**
	 * 根据月份查询开单总额
	 * @return
	 */
	public abstract Map<String,Object> queryTotalMoney(String doctorId,String months,String date,String firstDate);
	
	/**
	 * 查询本月是否已发送短信
	 * @param month
	 * @param phone
	 * @return
	 */
	public abstract Integer queryIsSendByPhone(String month,String phone);
	
	/**
	 * 插入发送记录
	 * @param month
	 * @param sendDate
	 * @param phone
	 * @param type
	 * @return
	 */
	public abstract Object insertSendSmsRecord(String month,String sendDate,String phone,Integer type,String orderNo);

	/**
	 * 根据主订单号查询药品订单
	 * @param mainOrderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRecordOrderSmsByMainOrderNo(String mainOrderNo);
}
