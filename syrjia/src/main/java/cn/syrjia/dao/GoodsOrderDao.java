package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.vo.GoodsOrderDetail;

public interface GoodsOrderDao extends BaseDaoInterface {

	/**
	 * 生成订单号
	 */
	abstract String orderNo();

	/**
	 * 查询订单列表
	 * @param memberId
	 * @param order
	 * @param row
	 * @param page
	 * @return
	 */
	abstract List<Map<String, Object>> queryOrderList(String memberId,
			Order order, Integer row, Integer page);
	
	/**
	 * 根据订单号查询订单信息
	 * @param memberId
	 * @param order
	 * @return
	 */
	abstract Map<String, Object> queryOrderByOrderNo(String memberId,Order order);
	
	/**
	 * 根据用户id查询已购买数量
	 * @param memberId
	 * @param activityId
	 * @param goodsPriceNumId
	 * @param orderNo
	 * @return
	 */
	abstract Integer queryBuyConnt(String memberId,String activityId,String goodsPriceNumId,String orderNo);

	/**
	 * 根据订单号查询订单信息
	 * @param memberId
	 * @param order
	 * @return
	 */
	abstract List<Map<String, Object>> queryOrderDetailByOrderNo(String memberId,Order order);
	
	/**
	 * 减去库存
	 * @param priceNumId
	 * @param buyCount
	 * @return
	 */
	abstract Integer restocking(String priceNumId,Integer buyCount);

	/**
	 * 订单退款
	 * @param openId
	 * @param orderNo
	 * @param refundMsg
	 * @return
	 */
	abstract Integer refundOrder(String openId, String orderNo, String refundMsg);

	/**
	 * 根据订单号查询订单详情
	 * @param orderNo
	 * @return
	 */
	abstract Map<String, Object> queryOrderInfo(String orderNo);
	
	/**
	 * 根据主订单号删除订单信息
	 * @param orderNo
	 * @return
	 */
	abstract Integer deleteOrderByMainOrder(String orderNo);
	
	/**
	 * 查询订单详情
	 * 
	 * @param order
	 *            订单实体
	 * @pdOid edb9daf2-27ec-4de2-81c0-9aa609289d82
	 */
	abstract Map<String, Object> queryOrderForDetail(Order order);
	
	/**
	 * 根据订单号查询供应商信息
	 */
	abstract List<Map<String,Object>> querySupplierByOrderNo(String orderNo);
	
	/**
	 * 查询服务订单
	 */
	abstract List<Map<String,Object>> queryOrderForService(Order order, Integer page, Integer row);
	
	/**
	 * 查询服务订单详情
	 */
	Map<String, Object> queryOrderForServiceById(GoodsOrderDetail goodsOrderDetail,String memberId);

	/**
	 * 查询呼叫中心数据
	 * @param orderNo
	 * @return
	 */
	public abstract Map<String,Object> querySendCallCenterData(String orderNo);
}
