package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.GoodsDao;
import cn.syrjia.dao.GoodsShopCartDao;
import cn.syrjia.entity.ShopCart;
import cn.syrjia.service.GoodsShopCartService;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Service("goodsShopCartService")
public class GoodsShopCartServiceImpl extends BaseServiceImpl implements GoodsShopCartService{

	
	@Resource(name = "goodsShopCartDao")
	GoodsShopCartDao goodsShopCartDao;
	
	@Resource(name = "goodsDao")
	GoodsDao goodsDao;
	
	/**
	 * 添加购物车
	 * @param goodsIds
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String,Object> addGoodsShopCart(ShopCart shopCart) {
		//必要参数判断
		if(null==shopCart||StringUtil.isEmpty(shopCart.getBuyCount())||shopCart.getBuyCount()==0||StringUtil.isEmpty(shopCart.getMemberId())||StringUtil.isEmpty(shopCart.getGoodsId())||StringUtil.isEmpty(shopCart.getPriceNumId())){
			return Util.resultMap(configCode.code_1068,null);
		}
		//根据会员查询已购买数量
		Integer goodsNum=goodsShopCartDao.queryGoodsNumByMemberId(shopCart.getPriceNumId());
		if(null!=goodsNum&&goodsNum>=50){//如果购物车数量大于50
			return Util.resultMap(configCode.code_1085,null);
		}
		
		Map<String,Object> goods=goodsShopCartDao.queryGoodsByGoodsIdAndPriceNumId(shopCart.getGoodsId(),shopCart.getPriceNumId(),shopCart.getMemberId());
		if(null==goods||goods.size()==0){//如果商品不存在
			return Util.resultMap(configCode.code_1067, goods);
		}
		//判断stock不能为空
		if(null!=goods.get("stock")&&Integer.parseInt(goods.get("stock").toString())<shopCart.getBuyCount()){//如果商品数量超过库存
			return Util.resultMap(configCode.code_1021, goods);
		}
		//根据商品和规格参数查询购物车信息
		Map<String,Object> oldShopCart=goodsShopCartDao.queryShopCartByGoodsIdAndPriceNumId(shopCart.getGoodsId(),shopCart.getPriceNumId(),shopCart.getMemberId());
		Object obj=null;
		Integer num=0;
		List<Map<String,Object>> goodsActivity=goodsDao.queryActivity(shopCart.getGoodsId());
		for(Map<String,Object> map:goodsActivity){//查询限量
			if(!StringUtil.isEmpty(map.get("activityNum"))){
				num=Integer.parseInt(map.get("activityNum").toString());
			}
		}
		//根据商品id查询购物车信息
		Map<String,Object> buyCount=goodsShopCartDao.queryShopCartByGoodsId(shopCart.getGoodsId(),shopCart.getMemberId());
		if(num!=0&&num<Integer.parseInt(buyCount.get("buyCount").toString())+shopCart.getBuyCount()){//如果购买数量超过限量
			return Util.resultMap(configCode.code_1079, goods);
		}
		//如果此商品购物车已有
		if(null==oldShopCart||StringUtil.isEmpty(oldShopCart.get("id"))){
			shopCart.setCreateTime(Util.queryNowTime());
			obj=goodsShopCartDao.addEntityUUID(shopCart);
		}else{
			//如果超过库存
			if(null!=goods.get("stock")&&Integer.parseInt(goods.get("stock").toString())<Integer.parseInt(oldShopCart.get("buyCount").toString())+shopCart.getBuyCount()){
				return Util.resultMap(configCode.code_1021, goods);
			}
			//添加购物车信息
			obj=goodsShopCartDao.addGoodsShopCartById(oldShopCart.get("id").toString(),shopCart.getBuyCount());
		}
		if(null==obj){
			return Util.resultMap(configCode.code_1015,obj);
		}
		//根据购物车信息查询购物车数量
		Integer count=goodsShopCartDao.queryShopCartNum(shopCart);
		return Util.resultMap(configCode.code_1001,count);
	}
	
	/**
	 * 添加购物车 立即支付时使用
	 * @param goodsIds
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String,Object> addGoodsShopCartForOrder(ShopCart shopCart) {
		//判断shopCart是否为空
		if(null==shopCart||StringUtil.isEmpty(shopCart.getBuyCount())||StringUtil.isEmpty(shopCart.getMemberId())||StringUtil.isEmpty(shopCart.getGoodsId())){
			return Util.resultMap(configCode.code_1068,null);
		}
		//根据id查询商品
		Map<String,Object> goods=goodsDao.queryGoodsById(shopCart.getGoodsId(),shopCart.getMemberId());
		if(null==goods||goods.size()==0){
			return Util.resultMap(configCode.code_1067, goods);
		}
		//判断stock是否为空
		if(null!=goods.get("stock")&&Integer.parseInt(goods.get("stock").toString())<shopCart.getBuyCount()){
			return Util.resultMap(configCode.code_1021, goods);
		}
		shopCart.setCreateTime(Util.queryNowTime());
		//根据实体保存信息
		Object obj=goodsShopCartDao.addEntityUUID(shopCart);
		if(null==obj){
			return Util.resultMap(configCode.code_1015,obj);
		}
		return Util.resultMap(configCode.code_1001, obj);
	}

	/**
	 * 删除购物车
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> deleteGoodsShopCart(String[] shopCarts) {
		// 删除购物车
		Integer i=goodsShopCartDao.deleteGoodsShopCart(shopCarts);
		if(null==i||i==0){
			return Util.resultMap(configCode.code_1066,i);
		}
		return Util.resultMap(configCode.code_1001,i);
	}

	/**
	 * 更新购物车商品数量
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> updateGoodsShopCartBuyCount(ShopCart shopCart) {
		Integer i=0;
		if(shopCart.getBuyCount()==0){
			//删除购物车
			i=goodsShopCartDao.deleteGoodsShopCart(new String[]{shopCart.getId()});
		}else{
			//根据商品和规格参数查询商品信息
			Map<String,Object> goods=goodsShopCartDao.queryGoodsByGoodsIdAndPriceNumId(shopCart.getGoodsId(),shopCart.getPriceNumId(),shopCart.getMemberId());
			if(null==goods||goods.size()==0){
				return Util.resultMap(configCode.code_1067, goods);
			}
			//判断stock是否为空
			if(null!=goods.get("stock")&&Integer.parseInt(goods.get("stock").toString())<shopCart.getBuyCount()){
				return Util.resultMap(configCode.code_1021, goods);
			}
			//根据商品和规格参数查询购物车信息
			Map<String,Object> oldShopCart=goodsShopCartDao.queryShopCartByGoodsIdAndPriceNumId(shopCart.getGoodsId(),shopCart.getPriceNumId(),shopCart.getMemberId());
			Integer num=0;
			//查询商品活动
			List<Map<String,Object>> goodsActivity=goodsDao.queryActivity(shopCart.getGoodsId());
			for(Map<String,Object> map:goodsActivity){
				if(!StringUtil.isEmpty(map.get("activityNum"))){
					num=Integer.parseInt(map.get("activityNum").toString());
				}
			}
			//根据商品id查询购物车信息
			Map<String,Object> buyCount=goodsShopCartDao.queryShopCartByGoodsId(shopCart.getGoodsId(),shopCart.getMemberId());
			if(num!=0&&num<Integer.parseInt(buyCount.get("buyCount").toString())+shopCart.getBuyCount()){
				return Util.resultMap(configCode.code_1079, goods);
			}
			//判断stock是否为空
			if(null!=goods.get("stock")&&Integer.parseInt(goods.get("stock").toString())<Integer.parseInt(oldShopCart.get("buyCount").toString())+shopCart.getBuyCount()){
				return Util.resultMap(configCode.code_1021, goods);
			}
			//添加购物车信息
			i=goodsShopCartDao.addGoodsShopCartById(oldShopCart.get("id").toString(),shopCart.getBuyCount());
			//i=goodsShopCartDao.updateGoodsShopCartBuyCount(shopCart);
		}
		if(i==0){
			return Util.resultMap(configCode.code_1066,i);
		}
		return Util.resultMap(configCode.code_1001,i);
	}

	/**
	 * 查询购物车商品
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryShopCart(ShopCart shopCart) {
		//判断shopCart是否为空
		if(null==shopCart||StringUtil.isEmpty(shopCart.getMemberId())){
			return Util.resultMap(configCode.code_1002,null);
		}
		//根据购物车信息查询供应商信息
		List<Map<String,Object>> supplier=goodsShopCartDao.queryShopCartSupplier(shopCart);
		//如果supplier为空
		if(null==supplier){
			return Util.resultMap(configCode.code_1015,null);
		}
		for(Map<String,Object> map:supplier){
			//根据购物车信息和供应商信息 查询购物车
			List<Map<String,Object>> goods=goodsShopCartDao.queryShopCart(shopCart,map.get("id").toString());
			for(Map<String,Object> m:goods){
				//查询商品活动
				List<Map<String,Object>> activitys=goodsDao.queryActivity(m.get("goodsId").toString());
				for(Map<String, Object> activity:activitys){
					//查询商品活动详情
					List<Map<String, Object>> activityDetail=goodsDao.queryActivityDetail(activity.get("id").toString(),Integer.parseInt(activity.get("type").toString()));
					activity.put("activityDetail", activityDetail);
				}
				m.put("activity", activitys);
			}
			map.put("goods", goods);
		}
		return Util.resultMap(configCode.code_1001,supplier);
	}
	
	/**
	 * 查询购物车商品
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryShopCartById(ShopCart shopCart) {
		//判断shopCart是否为空
		if(null==shopCart||StringUtil.isEmpty(shopCart.getMemberId())){
			return Util.resultMap(configCode.code_1002,null);
		}
		//根据购物车信息和供应商信息 查询购物车
		List<Map<String,Object>> goods=goodsShopCartDao.queryShopCart(shopCart,null);
		for(Map<String,Object> m:goods){
			//查询商品活动
			List<Map<String,Object>> activitys=goodsDao.queryActivity(m.get("goodsId").toString());
			for(Map<String, Object> activity:activitys){
				//查询商品活动详情
				List<Map<String, Object>> activityDetail=goodsDao.queryActivityDetail(activity.get("id").toString(),Integer.parseInt(activity.get("type").toString()));
				activity.put("activityDetail", activityDetail);
			}
			m.put("activity", activitys);
		}
		return Util.resultMap(configCode.code_1001,goods);
	}
	
	/**
	 * 查询购物车商品
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsById(String goodsId,String priceNumId,String buyCount) {
		if(StringUtil.isEmpty(goodsId)||StringUtil.isEmpty(priceNumId)){
			return Util.resultMap(configCode.code_1002,null);
		}
		//根据商品和规格参数id查询商品信息
		List<Map<String,Object>> goods=goodsShopCartDao.queryGoodsById(goodsId,priceNumId);
		for(Map<String,Object> m:goods){
			//查询商品活动
			List<Map<String,Object>> activitys=goodsDao.queryActivity(m.get("goodsId").toString());
			for(Map<String, Object> activity:activitys){
				//查询商品活动详情
				List<Map<String, Object>> activityDetail=goodsDao.queryActivityDetail(activity.get("id").toString(),Integer.parseInt(activity.get("type").toString()));
				activity.put("activityDetail", activityDetail);
			}
			m.put("buyCount",buyCount);
			m.put("activity", activitys);
		}
		return Util.resultMap(configCode.code_1001,goods);
	}

	/**
	 * 查询购物车商品数量
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryShopCartNum(ShopCart shopCart) {
		//判断shopCart是否为空
		if(null==shopCart||StringUtil.isEmpty(shopCart.getMemberId())){
			return Util.resultMap(configCode.code_1002,null);
		}
		//根据购物车信息查询购物车数量
		Integer i=goodsShopCartDao.queryShopCartNum(shopCart);
		if(null==i){
			return Util.resultMap(configCode.code_1015,i);
		}
		return Util.resultMap(configCode.code_1001,i);
	}

	/**
	 * 查询购物车邮费
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryPostageByCartIds(String city,String[] ids) {
		//根据城市id查询城市名
		String cityId=goodsDao.queryCityIdByCity(city);
		//判断cityId是否为空
		if(StringUtil.isEmpty(cityId)){
			return Util.resultMap(configCode.code_1069,null);
		}
		//根据城市id和购物车id查询邮费信息
		String startFee=goodsDao.queryPostageByCartIds(cityId, ids);
		if(StringUtil.isEmpty(startFee)){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,startFee);
	}

	/**
	 * 查询购物车邮费
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryPostageByCityName(String city, String goodsId,Integer buyCount) {
		//根据城市id查询城市名
		String cityId=goodsDao.queryCityIdByCity(city);
		//判断cityId是否为空
		if(StringUtil.isEmpty(cityId)){
			return Util.resultMap(configCode.code_1069,null);
		}
		//根基城市商品和数量查询邮费
		String startFee=goodsDao.queryPostage(cityId, goodsId,buyCount);
		if(StringUtil.isEmpty(startFee)){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,startFee);
	}
	
	/**
	 * 查询购物车邮费
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryPostageByMainOrderNo(String city,
			String orderNo) {
		//根据城市id查询城市名
		String cityId=goodsDao.queryCityIdByCity(city);
		if(StringUtil.isEmpty(cityId)){
			return Util.resultMap(configCode.code_1069,null);
		}
		//根据城市id和主订单号查询邮费
		String startFee=goodsDao.queryPostageByMainOrderNo(cityId,orderNo);
		//判断startFee是否为空
		if(StringUtil.isEmpty(startFee)){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,startFee);
	}

	/**
	 * 更新订单价格
	 * @param orderNo
	 * @param orderPrice
	 * @param receiptsPrice
	 * @param goodsPrice
	 * @param postage
	 * @return
	 */
	@Override
	public Integer updateOrderPrice(String orderNo,
			Double orderPrice, Double receiptsPrice, Double goodsPrice,Double postage) {
		return goodsDao.updateOrderPrice(orderNo, orderPrice, receiptsPrice, goodsPrice,postage);
	}
}
