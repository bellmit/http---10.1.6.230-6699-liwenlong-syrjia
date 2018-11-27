package cn.syrjia.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.GoodsShopCartDao;
import cn.syrjia.entity.ShopCart;
import cn.syrjia.util.Util;
import cn.syrjia.weixin.util.StringUtils;

@Repository("goodsShopCartDao")
public class GoodsShopCartDaoImpl extends BaseDaoImpl implements GoodsShopCartDao{
	
	// 日志
	private Logger logger = LogManager.getLogger(GoodsShopCartDaoImpl.class);

	/**
	 * 添加
	 */
	@Override
	public Integer addGoodsShopCart(String goodsId, Integer num, String memberId) {
		
		return null;
	}

	/**
	 * 根据商品和规格参数查询购物车信息
	 * @param goodsId
	 * @param priceNumId
	 * @param memberId
	 * @return
	 */
	@Override
	public Map<String,Object> queryShopCartByGoodsIdAndPriceNumId(String goodsId,String priceNumId, String memberId) {
		String sql="select id,buyCount from t_shopcart where goodsId=? and priceNumId=? and memberId=?";
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			//执行sql
			map = jdbcTemplate.queryForMap(sql,new Object[]{goodsId,priceNumId,memberId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 根据商品id查询购物车信息
	 * @param goodsId
	 * @param memberId
	 * @return
	 */
	@Override
	public Map<String,Object> queryShopCartByGoodsId(String goodsId,String memberId) {
		String sql="select IFNULL(sum(buyCount),0) buyCount from t_shopcart where goodsId=? and memberId=?";
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			//执行sql
			map = jdbcTemplate.queryForMap(sql,new Object[]{goodsId,memberId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 添加购物车信息
	 * @param id
	 * @param buyCount
	 * @return
	 */
	@Override
	public Integer addGoodsShopCartById(String id, Integer buyCount) {
		String sql="update t_shopcart set buyCount=buyCount+? where id=?";
		Integer i=0;
		try {
			//执行sql
			i = jdbcTemplate.update(sql,new Object[]{buyCount,id});
		} catch (DataAccessException e) {
			logger.error(e);
			return null;
		}
		return i;
	}

	/**
	 * 删除购物车
	 * @param shopCarts
	 * @return
	 */
	@Override
	public Integer deleteGoodsShopCart(final String[] shopCarts) {
		String sql="delete from t_shopcart where id=?";
		int[] i={0};
		try {
			//执行sql
			i=jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1,shopCarts[i]);
				}
				@Override
				public int getBatchSize() {
					return shopCarts.length;
				}
			});
		} catch (DataAccessException e) {
			logger.error(e);
			return 0;
		}
		return Util.toInt(i);
	}

	/**
	 * 更新购物车信息
	 * @param shopCart
	 * @return
	 */
	@Override
	public Integer updateGoodsShopCartBuyCount(ShopCart shopCart) {
		String sql="update t_shopcart set buyCount=? where id=? and memberId=?";
		Integer i=0;
		try {
			//执行sql
			i = jdbcTemplate.update(sql,new Object[]{shopCart.getBuyCount(),shopCart.getId(),shopCart.getMemberId()});
		} catch (DataAccessException e) {
			logger.error(e);
			return null;
		}
		return i;
	}

	/**
	 * 根据购物车id查询商品信息
	 * @param shopCartId
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsByShopCartId(String shopCartId) {
		String sql="SELECT g.id,sc.buyCount,gp.price,gp.stock,sc.priceNumId FROM t_shopcart sc INNER JOIN t_goods g ON g.id=sc.goodsId INNER JOIN t_goods_pricenum gp ON gp.id=sc.priceNumId INNER JOIN t_supplier s ON s.id=g.supplierId WHERE g.state=1 AND s.state=1 AND gp.state=1 AND sc.id=?";
		Map<String, Object> map=null;
		try {
			//执行sql
			map = jdbcTemplate.queryForMap(sql,new Object[]{shopCartId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}
	
	/**
	 * 根据商品和规格参数id查询商品信息
	 * @param goodsId
	 * @param priceNumId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryGoodsById(String goodsId,String priceNumId) {
		String sql="SELECT g.description,IF(gp.picture IS NULL OR gp.picture = 'null' OR gp.picture = '',g.picture,gp.picture) picture,g.name,GROUP_CONCAT(gds.name order by gds.rank) specifications,NULL activityPrice,gp.price,gp.stock,g.id goodsId,gp.id pricenumId FROM t_goods g 	LEFT JOIN t_goods_pricenum gp ON gp.goodsId=g.id INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId inner join t_supplier s on s.id=g.supplierId where g.id=? AND gp.id=? and g.state=1 and s.state=1 ";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行sql
			list = jdbcTemplate.queryForList(sql,new Object[]{goodsId,priceNumId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 根据购物车信息和供应商信息 查询购物车
	 * @param shopCart
	 * @param supplierId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryShopCart(ShopCart shopCart,String supplierId) {
		String sql="SELECT g.description,sc.id,IF(gp.picture IS NULL OR gp.picture = 'null' OR gp.picture = '',g.picture,gp.picture) picture,g.name,GROUP_CONCAT(gds.name) specifications,NULL activityPrice,gp.price,gp.stock,sc.buyCount,g.id goodsId,gp.id pricenumId FROM t_shopcart sc INNER JOIN t_goods g ON g.id=sc.goodsId LEFT JOIN t_goods_pricenum gp ON gp.id=sc.priceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId inner join t_supplier s on s.id=g.supplierId where sc.memberId=? and g.state=1 and s.state=1  ";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		//拼接查询条件
		if(!StringUtils.isEmpty(shopCart.getId())){
			sql+=" AND sc.id in ("+Util.ArrayToString(shopCart.getId().split(","))+")";
		}
		if(!StringUtils.isEmpty(supplierId)){
			sql+=" AND s.id='"+supplierId+"'";
		}
		sql+=" group by sc.id";
		try {
			//执行sql
			list = jdbcTemplate.queryForList(sql,new Object[]{shopCart.getMemberId()});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 根据购物车信息查询供应商信息
	 * @param shopCart
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryShopCartSupplier(ShopCart shopCart) {
		String sql="SELECT s.id,s.name,s.isProprietary FROM t_shopcart sc INNER JOIN t_goods g ON g.id=sc.goodsId  inner join t_supplier s on s.id=g.supplierId where sc.memberId=? and g.state=1 and s.state=1  group by g.supplierId ";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行sql
			list = jdbcTemplate.queryForList(sql,new Object[]{shopCart.getMemberId()});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 根据购物车信息查询购物车数量
	 * @param shopCart
	 * @return
	 */
	@Override
	public Integer queryShopCartNum(ShopCart shopCart) {
		String sql="select count(1) from t_shopcart where memberId=?";
		Integer i=0;
		try {
			//执行sql
			i = jdbcTemplate.queryForObject(sql,new Object[]{shopCart.getMemberId()},Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return i;
	}

	/**
	 * 根据商品和规格参数查询商品信息
	 * @param goodsId
	 * @param priceNumId
	 * @param memberId
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsByGoodsIdAndPriceNumId(String goodsId,
			String priceNumId, String memberId) {
		String sql="SELECT g.id,gp.stock,gp.price,g.city,gp.id priceNumId FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id INNER JOIN t_supplier s ON s.id=g.supplierId WHERE goodsId=? AND gp.id=? AND g.state=1 and s.state=1 ";
		Map<String, Object> map=null;
		try {
			//执行sql
			map = jdbcTemplate.queryForMap(sql,new Object[]{goodsId,priceNumId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 根据会员查询已购买数量
	 * @param memberId
	 * @return
	 */
	@Override
	public Integer queryGoodsNumByMemberId(String memberId) {
		String sql="select count(1) from t_shopcart where memberId=?";
		Integer num=0;
		try {
			//执行sql
			num = jdbcTemplate.queryForObject(sql,new Object[]{memberId},Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return num;
	}
}
