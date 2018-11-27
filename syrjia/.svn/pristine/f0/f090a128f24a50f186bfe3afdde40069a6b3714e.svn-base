package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Order;
import cn.syrjia.entity.ScoreConsume;
import cn.syrjia.entity.ScoreGoods;

public interface ScoreGoodsDao extends BaseDaoInterface{
	
	/**
	 * 订单号
	 * @return
	 */
	abstract String orderNo();

	/**
	 * 查询所有的商品记录
	 * @param scoreGoods
	 * @param page
	 * @param row
	 * @return
	 */
	abstract List<Map<String, Object>> queryAllScoreGoods(ScoreGoods scoreGoods,Integer page,Integer row);
	
	/**
	 * 通过id查询商品
	 * @param scoreGoodsId
	 * @return
	 */
	abstract Map<String, Object> queryScoreGoodsById(String scoreGoodsId);
	
	/**
	 * 通过用户id查询
	 * @param scoreConsumeId
	 * @return
	 */
	abstract String queryScoreByUserId(String scoreConsumeId);
	
	/**
	 * 通过ScoreConsume实体查询
	 * @param scoreConsume
	 * @param page
	 * @param row
	 * @return
	 */
	abstract List<Map<String,Object>> queryScoreRecord(ScoreConsume scoreConsume,Integer page,Integer row);
	
	/**
	 * 分页查询订单
	 * @param memeberId
	 * @param page
	 * @param row
	 * @return
	 */
	abstract List<Map<String,Object>> queryScoreOrderList(String memeberId,Integer page,Integer row);
	
	/**
	 * 通过订单号查询
	 * @param orderNo
	 * @param memberId
	 * @param scoreGoodsId
	 * @return
	 */
	abstract Map<String, Object> queryScoreOrderByOrderNo(String orderNo,String memberId,String scoreGoodsId);
	
	/**
	 * 查询订单
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	abstract List<Map<String, Object>> queryScoreOrder(Order order,Integer page,Integer row);
	
	/**
	 * 查询商品图片
	 * @param scoreGoodsId
	 * @return
	 */
	abstract List<Map<String, Object>> queryScoreGoodsImg(String scoreGoodsId);
}
