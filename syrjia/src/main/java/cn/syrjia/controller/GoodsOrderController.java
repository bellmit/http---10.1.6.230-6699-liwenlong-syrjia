package cn.syrjia.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.entity.Order;
import cn.syrjia.entity.vo.GoodsOrderDetail;
import cn.syrjia.service.GoodsOrderService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Controller 
@RequestMapping("/goodsOrder")
public class GoodsOrderController {

	@Resource(name = "goodsOrderService")
	GoodsOrderService goodsOrderService;
	
	/**
	 * 添加订单
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/addGoodsOrder")
	@ResponseBody
	public Map<String,Object> addGoodsOrder(HttpServletRequest request,Order order,String shippingAddressId,String shopCarts,String memberId){
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//实体添加memberId
		order.setMemberId(memberId);
		//添加订单
		return goodsOrderService.addGoodsOrder(order, shippingAddressId, shopCarts.split(","));
	}
	
	/**
	 * 添加订单
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/addGoodsOrderByGoods")
	@ResponseBody
	public Map<String,Object> addGoodsOrderByGoods(HttpServletRequest request,String goodsId,String shippingAddressId,String buyCount,String priceNumId,String memberId,String doctorId,String patientId){
		//判断memberid
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//添加商品订单 直接购买
		return goodsOrderService.addGoodsOrderByGoods(goodsId, shippingAddressId, buyCount,priceNumId,memberId,doctorId,patientId);
	}
	
	/**
	 * 查询所有订单
	 * 
	 * @param request
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid 685211c6-1e92-45c0-ab16-e3a4dca0bd2d
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderList")
	public Map<String, Object> queryOrderList(HttpServletRequest request,
			Order order, Integer row, Integer page,String memberId) {
		//判断memberid是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//查询所有的订单
		return goodsOrderService.queryOrderList(memberId, order, row, page);
	}
	
	/**
	 * 查询所有订单
	 * 
	 * @param request
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid 685211c6-1e92-45c0-ab16-e3a4dca0bd2d
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderByOrderNo")
	public Map<String, Object> queryOrderByOrderNo(HttpServletRequest request,Order order,String memberId) {
		//判断memberid是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//根据订单号查询订单信息
		return goodsOrderService.queryOrderByOrderNo(memberId, order);
	}
	
	/**
	 * 查询订单详情 评价时用
	 * 
	 * @param request
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid 685211c6-1e92-45c0-ab16-e3a4dca0bd2d
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderDetailByOrderNo")
	public Map<String, Object> queryOrderDetailByOrderNo(HttpServletRequest request,Order order,String memberId) {
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//根据订单号查询订单详情
		return goodsOrderService.queryOrderDetailByOrderNo(memberId, order);
	}
	
	/**
	 * 查询订单详情 支付时用
	 * 
	 * @param request
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid 685211c6-1e92-45c0-ab16-e3a4dca0bd2d
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderForDetail")
	public Map<String, Object> queryOrderForDetail(HttpServletRequest request,Order order,String memberId) {
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//实体里面添加memberId
		order.setMemberId(memberId);
		//查询订单详情
		return goodsOrderService.queryOrderForDetail(order);
	}
	
	/**
	 * 查询服务条码
	 * 
	 * @param request
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid 685211c6-1e92-45c0-ab16-e3a4dca0bd2d
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderForService")
	public Map<String, Object> queryOrderForService(HttpServletRequest request,Order order,Integer page,Integer row) {
		String memberId = GetOpenId.getMemberId(request);
		order.setMemberId(memberId);
		//查询订单详情
		return goodsOrderService.queryOrderForService(order,page,row);
	}
	
	/**
	 * 查询服务条码 根据订单详情Id
	 * 
	 * @param request
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid 685211c6-1e92-45c0-ab16-e3a4dca0bd2d
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOrderForServiceById")
	public Map<String, Object> queryOrderForServiceById(HttpServletRequest request,GoodsOrderDetail goodsOrderDetail) {
		String memberId = GetOpenId.getMemberId(request);
		return goodsOrderService.queryOrderForServiceById(goodsOrderDetail,memberId);
	}
	
	/**
	 * 订单退款
	 * 
	 * @param request
	 * @param orderNo
	 *            订单号
	 * @pdOid 134497c6-8328-457c-a9d9-93a15fa876ef
	 */
	@ResponseBody
	@RequestMapping(value = "/refundOrder")
	public Integer refundOrder(HttpServletRequest request, String orderNo,
			String refundMsg) {
		String memberId = GetOpenId.getMemberId(request);
		return goodsOrderService.refundOrder(memberId, orderNo, refundMsg);
	}
}
