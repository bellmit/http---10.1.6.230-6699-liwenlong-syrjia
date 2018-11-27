package cn.syrjia.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.ScoreConsume;
import cn.syrjia.entity.ScoreGoods;

public interface ScoreGoodsService extends BaseServiceInterface{

	/**
	 * 查询所有的记录商品
	 * @param scoreGoods
	 * @param page
	 * @param row
	 * @return
	 */
	abstract Map<String,Object> queryAllScoreGoods(ScoreGoods scoreGoods,Integer page,Integer row);
	
	/**
	 * 通过id查询
	 * @param scoreGoods
	 * @return
	 */
	abstract Map<String,Object> queryScoreGoodsById(ScoreGoods scoreGoods);
	
	/**
	 * 通过用户id查询
	 * @param scoreConsume
	 * @return
	 */
	abstract Map<String,Object> queryScoreByUserId(ScoreConsume scoreConsume);
	
	/**
	 * 查询记录
	 * @param scoreConsume
	 * @param page
	 * @param row
	 * @return
	 */
	abstract Map<String,Object> queryScoreRecord(ScoreConsume scoreConsume,Integer page,Integer row);
	
	/**
	 * 查询列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	abstract Map<String,Object> queryScoreOrderList(String memberId,Integer page,Integer row);
	
	/**
	 * 添加
	 * @param request
	 * @param order
	 * @param scoreGoodsId
	 * @param shippingAddressId
	 * @return
	 */
	abstract Map<String, Object> addScoreOrder(HttpServletRequest request,Order order,String scoreGoodsId,String shippingAddressId);
	
	/**
	 * 通过订单查询
	 * @param orderNo
	 * @param memberId
	 * @param scoreGoodsId
	 * @return
	 */
	abstract Map<String, Object> queryScoreOrderByOrderNo(String orderNo,String memberId,String scoreGoodsId);
	
	/**
	 * 查询订单记录
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	abstract Map<String, Object> queryScoreOrder(Order order,Integer page,Integer row);
	
	/**
	 * 查询商品的图片
	 * @param scoreGoodsId
	 * @return
	 */
	abstract Map<String,Object> queryScoreGoodsImg(String scoreGoodsId);
}
