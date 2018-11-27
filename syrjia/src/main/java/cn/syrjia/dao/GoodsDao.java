package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Goods;
import cn.syrjia.entity.GoodsType;

public interface GoodsDao  extends BaseDaoInterface {

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
	Map<String,Object> queryGoodsPricenumByGoodsId(String goodsId);
	
	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsForList(String goodsId);
	
	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsSpecificationsTypeByGoodsId(String goodsId);
	
	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsSpecificationsByGoodsId(String goodsId);
	
	/**
	 * 查询关注id
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	String queryFollowId(String doctorId,String patientId);
	
	/**
	 * 查询分成比例
	 * @param dividedTypeId
	 * @param dividedObject
	 * @return
	 */
	Double queryDivided(Integer dividedTypeId,Integer dividedObject);
	
	/**
	 * 查询默认分成比例
	 * @param doctorId
	 * @return
	 */
	Map<String,Object> queryCommission(String doctorId);
	
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
	Map<String, Object> queryGoods(Goods goods,String serviceId,String brandId,String lowPrice,String highPrice,String order,Integer page,Integer row);
	
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
	Map<String, Object> querySupplierByGoods(String goodsId);
	
	/**
	 * 查询商品积分（已弃用）
	 * @param goodsId
	 * @return
	 */
	Map<String, Object> queryIdentificationByGoods(String goodsId);
	
	/**
	 * 根据商品类型查询商品
	 * @param goodsTypeId
	 * @return
	 */
	List<Map<String, Object>> queryGoodsByGoodsTypeId(String goodsTypeId);
	
	/**
	 * 查询商品图片
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> queryGoodsImg(String goodsId);
	
	/**
	 * 查询商品活动
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> queryActivity(String goodsId);
	
	/**
	 * 查询商品活动详情
	 * @param activityId
	 * @param type
	 * @return
	 */
	List<Map<String, Object>> queryActivityDetail(String activityId,Integer type);
	
	/**
	 * 查询商品标签
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> queryPromise(String goodsId);
	
	/**
	 * 查询商品库存
	 * @param goodsId
	 * @return
	 */
	Integer queryGoodsStock(String goodsId);
	
	/**
	 * 添加常用搜素
	 * @param memberId
	 * @param name
	 * @return
	 */
	Integer addCommonSearch(String memberId,String name);
	
	/**
	 * 根基城市商品和数量查询邮费
	 * @param city
	 * @param goodsId
	 * @param buyCount
	 * @return
	 */
	String queryPostage(String city,String goodsId,Integer buyCount);
	
	/**
	 * 根据城市id和购物车id查询邮费信息
	 * @param city
	 * @param ids
	 * @return
	 */
	String queryPostageByCartIds(String city,String[] ids);
	
	/**
	 * 根据城市id和订单号查询邮费
	 * @param city
	 * @param orderNo
	 * @return
	 */
	String queryPostageByGoodsIds(String city,String orderNo);
	
	/**
	 * 根据城市id和主订单号查询邮费
	 * @param city
	 * @param orderNo
	 * @return
	 */
	String queryPostageByMainOrderNo(String city,String orderNo);
	
	/**
	 * 根据城市id查询城市名
	 * @param city
	 * @return
	 */
	String queryCityIdByCity(String city);
	
	/**
	 * 更新商品库存
	 * @param goodsId
	 * @param count
	 * @return
	 */
	Integer updateGoodsStock(String goodsId,Integer count);
	
	/**
	 * 查询商品主图
	 * @return
	 */
	List<Map<String,Object>> queryGoodsIndexImg();
	
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
	 * 查询历史搜索数量
	 * @param memberId
	 * @param type
	 * @return
	 */
	Integer queryHistorySearchCount(String memberId,Integer type);
	
	/**
	 * 根据名称删除历史搜索
	 * @param memberId
	 * @param type
	 * @param name
	 * @return
	 */
	Integer deleteHistorySearchByName(String memberId,Integer type,String name);
	
	/**
	 * 删除超过十条的数据
	 * @param memberId
	 * @param type
	 * @param num
	 * @return
	 */
	Integer deleteHistorySearchLast(String memberId,Integer type,Integer num);
	
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
	 * 根据商品id查询商品信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> queryGoodsByGoodsIds(String[] ids);
	
	/**
	 * 根据类型id查询商品
	 * @param typeId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsTypeByTypeId(String typeId);
	
	/**
	 * 更新订单价格
	 * @param orderNo
	 * @param orderPrice
	 * @param receiptsPrice
	 * @param goodsPrice
	 * @param postage
	 * @return
	 */
	Integer updateOrderPrice(String orderNo,Double orderPrice,Double receiptsPrice,Double goodsPrice,Double postage);
}
