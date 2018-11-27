package cn.syrjia.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.RefundApplyRecord;
import cn.syrjia.entity.RefundError;
import cn.syrjia.service.OrderService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Resource(name = "orderService")
	OrderService orderService;
	
	/**
	 * 支付页面查询订单号、订单价格
	 * @param orderNo
	 * @param memberId
	 * @return
	 * 2018-04-07
	 */
	@RequestMapping("/queryPayOrderDetail")
	@ResponseBody
	public Map<String,Object> queryPayOrderDetail(HttpServletRequest request,Order order){
		return orderService.queryPayOrderDetail(request, order);
	}
	
	/**
	 * 查询订单
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryOrder")
	@ResponseBody
	public Map<String, Object> queryOrder(HttpServletRequest request,Order order, Integer page, Integer row) {
		String memberId = GetOpenId.getMemberId(request);
		order.setMemberId(memberId);
		return orderService.queryOrder(order, page, row);
	}
	
	/**
	 * 查询订单 根据订单号
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/queryOrderByOrderNo")
	@ResponseBody
	public Map<String, Object> queryOrderByOrderNo(HttpServletRequest request,Order order) {
		String memberId = GetOpenId.getMemberId(request);

		order.setMemberId(memberId);
		return orderService.queryOrderByOrderNo(order);
	}
	
	
	/**
	 * 删除订单
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/deleteOrder")
	@ResponseBody
	public Map<String, Object> deleteOrder(Order order) {
		order.setState(2);
		//通过实体更新
		Integer i=orderService.updateEntity(order);
		if(null==i){
			return Util.resultMap(configCode.code_1015,i);
		}
		if(i<=0){
			return Util.resultMap(configCode.code_1066,i);
		}
		return Util.resultMap(configCode.code_1001,i);
	}
	
	/**
	 * 取消订单
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody
	public Map<String, Object> cancelOrder(Order order) {
		order.setPaymentStatus(6);
		//通过实体更新
		Integer i=orderService.updateEntity(order);
		if(null==i){
			return Util.resultMap(configCode.code_1015,i);
		}
		if(i<=0){
			return Util.resultMap(configCode.code_1066,i);
		}
		return Util.resultMap(configCode.code_1001,i);
	}
	
	/**
	 * 确认收货
	 * 
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/successOrder")
	@ResponseBody
	public Map<String, Object> successOrder(Order order) {
		order.setPaymentStatus(5);
		order.setOrderStatus(5);
		//通过实体更新
		Integer i=orderService.updateEntity(order);
		if(null==i){
			return Util.resultMap(configCode.code_1015,i);
		}
		if(i<=0){
			return Util.resultMap(configCode.code_1066,i);
		}
		//更新发货时间
		orderService.updateDeliveryTime(order.getOrderNo());
		return Util.resultMap(configCode.code_1001,i);
	}
	
	/**
	 * 查询退款订单详情
	 * @param request
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/queryRefundOrderDetail")
	@ResponseBody
	public Map<String, Object> queryRefundOrderDetail(HttpServletRequest request,String orderNo) {
		return orderService.queryRefundOrderDetail(request, orderNo);
	}
	
	/**
	 * 查询退款原因
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryApplyRefundReason")
	@ResponseBody
	public Map<String, Object> queryApplyRefundReason(HttpServletRequest request) {
		RefundError error = new RefundError();
		error.setErrorType("2");
		return Util.resultMap(configCode.code_1001, orderService.query(error));
	}
	
	/**
	 * 查询退款过程
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/queryRefundProgress")
	@ResponseBody
	public Map<String, Object> queryRefundProgress(String orderNo) {
		return orderService.queryRefundProgress(orderNo);
	}
	
	/**
	 * 查询物流信息
	 * 
	 * @param request
	 * @param orderNo
	 *            订单号
	 * @pdOid dd4f713c-0f84-48e3-a7ca-62e1178b83d6
	 */
	@ResponseBody
	@RequestMapping(value = "/queryLogistics")
	public Map<String, Object> queryLogistics(HttpServletRequest request,
			String orderNo) {
		return orderService.queryLogistics(null, orderNo);
	}
	
	/**
	 * 查询物流信息
	 * 
	 * @param request
	 * @param orderNo
	 *            订单号
	 * @pdOid dd4f713c-0f84-48e3-a7ca-62e1178b83d6
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllOrderList")
	public Map<String, Object> queryAllOrderList(HttpServletRequest request,Order order,Integer row,Integer page) {
		String memberId = GetOpenId.getMemberId(request);

		return orderService.queryAllOrderList(memberId, order, row, page);
	}
	
	/**
	 * 查询物流信息
	 * 
	 * @param request
	 * @param orderNo
	 *            订单号
	 * @pdOid dd4f713c-0f84-48e3-a7ca-62e1178b83d6
	 */
	@ResponseBody
	@RequestMapping(value = "/queryRecordOrderByOrderNo")
	public Map<String, Object> queryRecordOrderByOrderNo(HttpServletRequest request,String orderNo) {
		return orderService.queryRecordOrderByOrderNo(orderNo);
	}
	
	/**
	 * 查询订单想去
	 * 
	 * @param request
	 * @param orderNo
	 *            订单号
	 * @pdOid dd4f713c-0f84-48e3-a7ca-62e1178b83d6
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderDetail")
	public Map<String, Object> queryOrderDetail(HttpServletRequest request,String orderNo) {
		return orderService.queryOrderDetail(orderNo);
	}

	/**
	 * 根据订单号查询订单信息
	 * @param request
	 * @param orderNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateChildState")
	public Integer updateChildState(HttpServletRequest request,String orderNo){
		Map<String,Object> order = orderService.queryOrderByOrderNo(orderNo);
		return orderService.paySuccessPush(request, order);
		/*orderService.updateChildState(request, "Main-P20180901213334");*/
		//return 1;
	}
	
	/**
	 * 用户申请退款
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/applyRefundMoney")
	public Map<String,Object> applyRefundMoney(RefundApplyRecord record){
		return orderService.applyRefundMoney(record);
	}
	
	/**
	 * 同步到his数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tbGhMoneyToHis")
	public Integer tbGhMoneyToHis(){
		return orderService.tbGhMoneyToHis();
	}
	
}
