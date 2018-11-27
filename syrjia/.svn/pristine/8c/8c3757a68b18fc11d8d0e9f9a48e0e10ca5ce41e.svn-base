package cn.syrjia.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.RefundApplyRecord;

public interface OrderService extends BaseServiceInterface {
	
	/**
	 * 支付页面查询订单号、订单价格
	 * @param orderNo
	 * @param memberId
	 * @return
	 * 2018-04-07
	 */
	public abstract Map<String,Object> queryPayOrderDetail(HttpServletRequest request,Order order);

	/**
	 * 根据订单号查询该订单是否支付
	 * @param orderNo
	 * @return
	 */
	public abstract Integer queryPayResultByOrderNo(String orderNo);
	
	/**
	 * 生成订单号
	 * @return
	 */
	public abstract String orderNo();

	/**
	 * 修改订单状态
	 * 
	 * @Description: TODO
	 * @param @param paystatus 支付状态 0-等待付款1-已付款2-退款中 3-已退款（交易关闭） 4-交易成功 5-订单作废'
	 * @param @param orderNo 订单编号
	 * @param @param ordertype 订单类型 默认1 1-挂号 2-陪诊 3-会诊 4-住院 5-手术 6-服务类 7-产品类
	 *        8-积分类 9-混合
	 * @param @return
	 * @return Integer
	 */
	public Integer updateState(HttpServletRequest request, Integer orderStatus,
			Integer paystatus, String orderNo, Integer ordertype,
			Integer refundId, String tradeNo,Integer orderWay, Integer... payWay);

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
	 * 查询订单列表
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	abstract Map<String,Object> queryOrder(Order order,Integer page,Integer row);
	
	/**
	 * 查询退款订单详情
	 * @param request
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRefundOrderDetail(HttpServletRequest request,String orderNo);

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
	 * 查询系统设置
	 * @param orderType
	 * @return
	 */
	public abstract Integer getLockTimes(Integer orderType);
	
	/**
	 * 查询未完成订单
	 * @return
	 */
	public abstract List<Map<String,Object>> queryNoFinishOrders();
	
	/**
	 * 订单
	 * @return
	 */
	public abstract List<Map<String,Object>> operaHosOrder();
	
	
	 /**
     * 查询订单列表 包含所有类型订单
     * @param memberId
     * @param order
     * @param row
     * @param page
     * @return
     */
	public abstract Map<String, Object> queryAllOrderList(String memberId,
			Order order, Integer row, Integer page);
	
	/**
	 * 查询导入人员
	 * @param openId
	 * @return
	 */
	public abstract Map<String,Object> queryFollowIdByOpenId(String openId);
	
    /**
     * 根据主订单号查询药品订单
     * @param mainOrderNo
     * @return
     */
	public abstract Map<String,Object> queryRecordOrderByMainOrderNo(String mainOrderNo);
	
	/**
	 * 根据订单号查询药品订单
	 * @param mainOrderNo
	 * @return
	 */
	public abstract Map<String,Object> queryRecordOrderByOrderNo(String mainOrderNo);
	
    /**
     * 查询订单详情
     * @param orderNo
     * @return
     */
	public abstract Map<String,Object> queryOrderDetail(String orderNo);
	
	/**
	 * 根据订单号查询订单信息
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> queryOrderByOrderNo(String orderNo);
	
	/**
	 * 查询锦旗信息
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryMyEvaBannerById(String id);
	
	/**
	 * 添加支付成功信息
	 * @param request
	 * @param order
	 * @return
	 */
	public abstract Integer paySuccessPush(HttpServletRequest request,Map<String,Object> order);
	
	/**
	 * 更新子订单状态
	 * @param request
	 * @param orderNo
	 * @return
	 */
	public abstract Integer updateChildState(HttpServletRequest request,String orderNo);
	
	/**
	 * 用户申请退款
	 * @param record
	 * @return
	 */
	public abstract Map<String,Object> applyRefundMoney(RefundApplyRecord record);
	
	/**
	 * 同步数据到his
	 * @return
	 */
	public abstract Integer tbGhMoneyToHis();
}
