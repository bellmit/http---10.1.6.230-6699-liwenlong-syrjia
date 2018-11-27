package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.GoodsDao;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Goods;
import cn.syrjia.entity.GoodsType;
import cn.syrjia.service.GoodsService;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Service("goodsService")
public class GoodsServiceImpl  extends BaseServiceImpl implements GoodsService{
	
	@Resource(name = "goodsDao")
	public GoodsDao goodsDao;

	/**
	 * 根据id查询商品
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsById(String goodsId,Object memberId) {
		//根据id查询商品
		Map<String, Object> map=goodsDao.queryGoodsById(goodsId,memberId);
		//根基商品id查询规格参数
		List<Map<String, Object>> specifications=goodsDao.queryGoodsSpecificationsByGoodsId(goodsId);
		//根基商品id查询规格参数
		Map<String, Object> pricenum=goodsDao.queryGoodsPricenumByGoodsId(goodsId);
		//查询商品图片
		List<Map<String, Object>> banner=goodsDao.queryGoodsImg(goodsId);
		//查询商品活动
		List<Map<String, Object>> activitys=goodsDao.queryActivity(goodsId);
		//循环插入查询商品活动详情
		for(Map<String, Object> activity:activitys){
			List<Map<String, Object>> activityDetail=goodsDao.queryActivityDetail(activity.get("id").toString(),Integer.parseInt(activity.get("type").toString()));
			activity.put("activityDetail", activityDetail);
		}
		//查询商品标签
		List<Map<String, Object>> promise=goodsDao.queryPromise(goodsId);
		map.put("specifications", specifications);
		map.put("pricenum",pricenum);
		map.put("banner", banner);
		map.put("activity", activitys);
		map.put("promise", promise);
		return map;
	}
	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsForList(String goodsId) {
		return goodsDao.queryGoodsForList(goodsId);
	}

	/**
	 * 查询商品信息
	 * @param goods
	 * @param serviceId
	 * @param brandId
	 * @param lowPrice
	 * @param highPrice
	 * @param order
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoods(Goods goods,String memberId,String serviceId,String brandId,String lowPrice,String highPrice,String order, Integer page,
			Integer row) {
		if(!StringUtil.isEmpty(goods.getName())){
			//goodsDao.addCommonSearch(memberId, goods.getName());
		}
		return goodsDao.queryGoods(goods,serviceId,brandId,lowPrice,highPrice,order, page, row);
	}

	/**
	 * 查询商品库存
	 * @param goodsId
	 * @return
	 */
	@Override
	public Integer queryGoodsStock(String goodsId) {
		return goodsDao.queryGoodsStock(goodsId);
	}

	/**
	 * 查询商品类型
	 * @param goodsType
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsType(GoodsType goodsType,
			Integer page, Integer row) {
		return goodsDao.queryGoodsType(goodsType, page, row);
	}
	
	/**
	 * 查询商城首页菜单
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryMallMenu(Integer page, Integer row) {
		return goodsDao.queryMallMenu(page, row);
	}

	/**
	 * 查询商品类型
	 * @param goodsType
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsTypeAndGoods(
			GoodsType goodsType, Integer page, Integer row) {
		//查询商品类型
		List<Map<String, Object>> list=goodsDao.queryGoodsType(goodsType,null,null);
		if(null!=list){
			//循环插入商品
			for(Map<String, Object> map:list){
				List<Map<String, Object>> goods=goodsDao.queryGoodsByGoodsTypeId(map.get("id").toString());
				map.put("goods",goods);
			}
		}
		return list;
	}

	/**
	 * 查询商品评论
	 * @param evaluate
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsEvaluate(Evaluate evaluate,
			Integer page, Integer row) {
		return goodsDao.queryGoodsEvaluate(evaluate, page, row);
	}
	
	/**
	 * 根据商品id查询分销商
	 * @param goodsId
	 * @return
	 */
	@Override
	public Map<String, Object> querySupplierByGoods(String goodsId) {
		return goodsDao.querySupplierByGoods(goodsId);
	}
	
	/**
	 * 查询商品积分（已弃用）
	 * @param goodsId
	 * @return
	 */
	@Override
	public Map<String, Object> queryIdentificationByGoods(String goodsId) {
		return goodsDao.queryIdentificationByGoods(goodsId);
	}
	/**
	 * 查询商品图片
	 * @param goodsId
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsImg(String goodsId) {
		List<Map<String, Object>> list=goodsDao.queryGoodsImg(goodsId);
		if(null==list){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 查询商品主图
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsIndexImg() {
		List<Map<String, Object>> list=goodsDao.queryGoodsIndexImg();
		if(null==list){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 根据类型id查询商品
	 * @param typeId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsTypeByTypeId(String typeId) {
		return goodsDao.queryGoodsTypeByTypeId(typeId);
	}
	
	/**
	 * 查询服务类型
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryService() {
		return goodsDao.queryService();
	}
	
	/**
	 * 查询品牌
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryBrand() {
		return goodsDao.queryBrand();
	}
	
	/**
	 * 根据名称查询商品
	 * @param name
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsByName(String name) {
		return goodsDao.queryGoodsByName(name);
	}
	
	/**
	 * 查询常用搜索
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryCommonSearch() {
		return goodsDao.queryCommonSearch();
	}
	
	/**
	 * 查询历史搜索
	 * @param memberId
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryHistorySearch(String memberId,Integer type) {
		return goodsDao.queryHistorySearch(memberId,type);
	}
	
	/**
	 * 添加历史搜索
	 * @param memberId
	 * @param name
	 * @param type
	 * @return
	 */
	@Override
	public Integer addHistorySearch(String memberId,String name,Integer type) {
		goodsDao.deleteHistorySearchByName(memberId, type, name);
		Integer count=goodsDao.queryHistorySearchCount(memberId, type);
		if(count>=10){
			//删除超过十条的数据
			goodsDao.deleteHistorySearchLast(memberId, type,count-9);
		}
		//添加历史搜索
		return goodsDao.addHistorySearch(memberId,name,type);
	}
	
	/**
	 * 清空历史搜素
	 * @param memberId
	 * @param type
	 * @return
	 */
	@Override
	public Integer deleteHistorySearch(String memberId,Integer type) {
		return goodsDao.deleteHistorySearch(memberId,type);
	}
	
	/**
	 * 查询平台活动
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryMallActivity() {
		return goodsDao.queryMallActivity();
	}
	
	/**
	 * 查询平台活动详情
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> queryMallActivityDetail(String id) {
		//查询平台活动详情
		Map<String, Object> activity=goodsDao.queryMallActivityDetail(id);
		if(null!=activity.get("goodsId")){
			//根据商品id查询商品信息
			List<Map<String, Object>> goods=goodsDao.queryGoodsByGoodsIds(activity.get("goodsId").toString().split(","));
			activity.put("goods",goods);
		}
		return Util.resultMap(configCode.code_1001, activity);
	}
	
	/**
	 * 查询号码
	 */
	@Override
	public String queryPostage(String city, String goodsId) {
		return "0";//goodsDao.queryPostage(city, goodsId);
	}

}
