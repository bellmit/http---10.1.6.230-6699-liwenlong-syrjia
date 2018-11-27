package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.ShopCart;

public interface GoodsShopCartDao extends BaseDaoInterface{

	/**
	 * 添加
	 * @param goodsId
	 * @param num
	 * @param memberId
	 * @return
	 */
	abstract Integer addGoodsShopCart(String goodsId,Integer num,String memberId);
	
	/**
	 * 根据商品和规格参数查询购物车信息
	 * @param goodsId
	 * @param priceNumId
	 * @param memberId
	 * @return
	 */
	abstract Map<String,Object> queryShopCartByGoodsIdAndPriceNumId(String goodsId,String priceNumId,String memberId);
	
	/**
	 * 根据商品id查询购物车信息
	 * @param goodsId
	 * @param memberId
	 * @return
	 */
	abstract Map<String,Object> queryShopCartByGoodsId(String goodsId,String memberId);
	
	/**
	 * 根据商品和规格参数查询商品信息
	 * @param goodsId
	 * @param priceNumId
	 * @param memberId
	 * @return
	 */
	abstract Map<String,Object> queryGoodsByGoodsIdAndPriceNumId(String goodsId,String priceNumId,String memberId);
	
	/**
	 * 根据会员查询已购买数量
	 * @param memberId
	 * @return
	 */
	abstract Integer queryGoodsNumByMemberId(String memberId);
	
	/**
	 * 添加购物车信息
	 * @param id
	 * @param buyCount
	 * @return
	 */
	abstract Integer addGoodsShopCartById(String id,Integer buyCount);
	
	/**
	 * 删除购物车
	 * @param shopCarts
	 * @return
	 */
	abstract Integer deleteGoodsShopCart(String[] shopCarts);
	
	/**
	 * 更新购物车信息
	 * @param shopCart
	 * @return
	 */
	abstract Integer updateGoodsShopCartBuyCount(ShopCart shopCart);
	
	/**
	 * 根据购物车id查询商品信息
	 * @param shopCartId
	 * @return
	 */
	abstract Map<String,Object> queryGoodsByShopCartId(String shopCartId);
	
	/**
	 * 根据商品和规格参数id查询商品信息
	 * @param goodsId
	 * @param priceNumId
	 * @return
	 */
	abstract List<Map<String,Object>> queryGoodsById(String goodsId,String priceNumId);
	
	/**
	 * 根据购物车信息和供应商信息 查询购物车
	 * @param shopCart
	 * @param supplierId
	 * @return
	 */
	abstract List<Map<String,Object>> queryShopCart(ShopCart shopCart,String supplierId);
	
	/**
	 * 根据购物车信息查询供应商信息
	 * @param shopCart
	 * @return
	 */
	abstract List<Map<String,Object>> queryShopCartSupplier(ShopCart shopCart);
	
	/**
	 * 根据购物车信息查询购物车数量
	 * @param shopCart
	 * @return
	 */
	abstract Integer queryShopCartNum(ShopCart shopCart);
	
}
