package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Goods;
import cn.syrjia.entity.GoodsType;

public interface GoodsService extends BaseServiceInterface{

	/**
	 * 根据id查询商品
	 * @param goodsIds
	 * @return
	 */
	Map<String,Object> queryGoodsById(String goodsId,Object memberId);
	
	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsForList(String goodsId);
	
	/**
	 * 查询商品库存
	 * @param goodsId
	 * @return
	 */
	Integer queryGoodsStock(String goodsId);
	
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
	Map<String, Object> queryGoods(Goods goods,String memberId,String serviceId,String brandId,String lowPrice,String highPrice,String order,Integer page,Integer row);
	
	/**
	 * 查询商品类型
	 * @param goodsType
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> queryGoodsType(GoodsType goodsType,Integer page,Integer row);
	
	/**
	 * 查询商城首页菜单
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> queryMallMenu(Integer page,Integer row);
	
	/**
	 * 查询商品类型
	 * @param goodsType
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> queryGoodsTypeAndGoods(GoodsType goodsType,Integer page,Integer row);
	
	/**
	 * 查询商品评论
	 * @param evaluate
	 * @param page
	 * @param row
	 * @return
	 */
	Map<String, Object> queryGoodsEvaluate(Evaluate evaluate,Integer page,Integer row);
	
	/**
	 * 根据商品id查询分销商
	 * @param goodsId
	 * @return
	 */
	Map<String,Object> querySupplierByGoods(String goodsId);
	
	/**
	 * 查询商品积分（已弃用）
	 * @param goodsId
	 * @return
	 */
	Map<String,Object> queryIdentificationByGoods(String goodsId);
	
	/**
	 * 查询商品图片
	 * @param goodsId
	 * @return
	 */
	Map<String,Object> queryGoodsImg(String goodsId);
	
	/**
	 * 根据类型id查询商品
	 * @param typeId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsTypeByTypeId(String typeId);
	
	/**
	 * 查询服务类型
	 * @return
	 */
	List<Map<String,Object>> queryService();
	
	/**
	 * 查询品牌
	 * @return
	 */
	List<Map<String,Object>> queryBrand();
	/**
	 * 根据名称查询商品
	 * @param name
	 * @return
	 */
	List<Map<String,Object>> queryGoodsByName(String name);
	
	/**
	 * 查询常用搜索
	 * @return
	 */
	List<Map<String,Object>> queryCommonSearch();
	
	/**
	 * 查询历史搜索
	 * @param memberId
	 * @param type
	 * @return
	 */
	List<Map<String,Object>> queryHistorySearch(String memberId,Integer type);
	
	/**
	 * 添加历史搜索
	 * @param memberId
	 * @param name
	 * @param type
	 * @return
	 */
	Integer addHistorySearch(String memberId,String name,Integer type);
	
	/**
	 * 清空历史搜素
	 * @param memberId
	 * @param type
	 * @return
	 */
	Integer deleteHistorySearch(String memberId,Integer type);
	
	/**
	 * 查询平台活动
	 * @return
	 */
	List<Map<String,Object>> queryMallActivity();
	
	/**
	 * 查询平台活动详情
	 * @param id
	 * @return
	 */
	Map<String,Object> queryMallActivityDetail(String id);
	
	/**
	 * 查询号码
	 */
	String queryPostage(String city,String goodsId);
	
	/**
	 * 查询商品主图
	 * @return
	 */
	Map<String,Object> queryGoodsIndexImg();
}
