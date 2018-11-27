package cn.syrjia.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.ShopCart;

public interface GoodsShopCartService extends BaseServiceInterface{

	/**
	 * 添加购物车
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> addGoodsShopCart(ShopCart shopCart);
	
	/**
	 * 添加购物车 立即支付时使用
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> addGoodsShopCartForOrder(ShopCart shopCart);
	
	/**
	 * 删除购物车
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> deleteGoodsShopCart(String[] shopCarts);
	
	/**
	 * 更新购物车商品数量
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> updateGoodsShopCartBuyCount(ShopCart shopCart);
	
	/**
	 * 查询购物车商品
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryShopCart(ShopCart shopCart);
	
	/**
	 * 查询购物车商品
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryShopCartById(ShopCart shopCart);
	
	/**
	 * 查询购物车商品
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryGoodsById(String goodsId,String priceNumId,String buyCount);

	/**
	 * 查询购物车商品数量
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryShopCartNum(ShopCart shopCart);
	
	/**
	 * 查询购物车邮费
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryPostageByCartIds(String city,String[] ids);
	
	/**
	 * 查询购物车邮费
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryPostageByCityName(String city,String goodsId,Integer buyCount);
	

	/**
	 * 查询购物车邮费
	 * @param goodsIds
	 * @return
	 */
	abstract Map<String,Object> queryPostageByMainOrderNo(String city,String orderNo);
	
	/**
	 * 更新订单价格
	 * @param orderNo
	 * @param orderPrice
	 * @param receiptsPrice
	 * @param goodsPrice
	 * @param postage
	 * @return
	 */
	abstract Integer updateOrderPrice(String orderNo,Double orderPrice,Double receiptsPrice,Double goodsPrice,Double postage);
	
}
