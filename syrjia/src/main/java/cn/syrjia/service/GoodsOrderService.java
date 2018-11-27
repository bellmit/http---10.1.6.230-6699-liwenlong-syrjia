package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.vo.GoodsOrderDetail;

public interface GoodsOrderService extends BaseServiceInterface {

	
	/**
	 * 生成订单号
	 * @return
	 */
	abstract String orderNo();
	
	/**
	 * 添加订单
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String, Object> addGoodsOrder(Order order,
			String shippingAddressId, String[] shopCarts);
	
	/**
	 * 添加商品订单 直接购买
	 * @param goodsId
	 * @param shippingAddressId
	 * @param buyCount
	 * @param priceNumId
	 * @param memberId
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	abstract Map<String,Object> addGoodsOrderByGoods(String goodsId,String shippingAddressId,String buyCount,String priceNumId,String memberId,String doctorId,String patientId);
	
	/**
	 * 添加商品订单 开方选择百宝箱
	 * @param goods
	 * @param memberId
	 * @param sourceOrderNo
	 * @param doctorId
	 * @return
	 */
	abstract Map<String,Object> addGoodsOrderByGoodsList(List<Map<String,Object>> goods,String memberId,String sourceOrderNo,String doctorId);

	/**
	 * 查询所有订单
	 * 
	 * @param openId
	 *            openId
	 * @param order
	 *            订单实体
	 * @param row
	 *            行数
	 * @param page
	 *            页数
	 * @pdOid edb9daf2-27ec-4de2-81c0-9aa609289d82
	 */
	Map<String, Object> queryOrderList(String memberId, Order order,
			Integer row, Integer page);
	
	/**
	 * 根据订单号查询订单信息
	 * @param memberId
	 * @param order
	 * @return
	 */
	Map<String, Object> queryOrderByOrderNo(String memberId, Order order);
	
	/**
	 * 根据订单号查询订单详情
	 * @param memberId
	 * @param order
	 * @return
	 */
	Map<String, Object> queryOrderDetailByOrderNo(String memberId, Order order);
	/**
	 * 查询订单详情
	 * 
	 * @param order
	 *            订单实体
	 * @pdOid edb9daf2-27ec-4de2-81c0-9aa609289d82
	 */
	Map<String, Object> queryOrderForDetail(Order order);
	
	/**
	 * 查询服务订单
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	Map<String, Object> queryOrderForService(Order order,Integer page,Integer row);
	
	/**
	 * 查询商品订单详情
	 * @param goodsOrderDetail
	 * @param memberId
	 * @return
	 */
	Map<String, Object> queryOrderForServiceById(GoodsOrderDetail goodsOrderDetail,String memberId);

	/**
	 * 订单退款
	 * 
	 * @param openId
	 * @param orderNo
	 *            订单号
	 * @pdOid 90c260c7-95c6-4137-9de4-49407930fdc3
	 */
	Integer refundOrder(String openId, String orderNo, String refundMsg);

	/**
	 * 查询物流信息
	 * 
	 * @param openId
	 *            openId
	 * @param orderNo
	 *            订单号
	 * @pdOid 8e5b0181-ec2e-4746-b743-7d6bbb5d5506
	 */
	Map<String, Object> queryLogistics(String openId, String orderNo);
	
	/**
	 * 查询订单有几个分销商
	 * 
	 * @param orderNo
	 *            订单号
	 * @pdOid 8e5b0181-ec2e-4746-b743-7d6bbb5d5506
	 */
	List<Map<String, Object>> querySupplierByOrderNo(String orderNo);
}
