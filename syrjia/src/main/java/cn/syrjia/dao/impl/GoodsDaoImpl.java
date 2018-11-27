package cn.syrjia.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.GoodsDao;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Goods;
import cn.syrjia.entity.GoodsType;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("goodsDao")
public class GoodsDaoImpl extends BaseDaoImpl implements GoodsDao{
	
	// 日志
	private Logger logger = LogManager.getLogger(GoodsDaoImpl.class);

	/**
	 * 根据id查询商品
	 * @param goodsIds
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsById(String goodsId,Object memberId) {
		String sql="";
		//判断memberId是否为空
		if(StringUtil.isEmpty(memberId)){
			sql="SELECT s.id supplierId,s.city,s.areas,s.isProprietary,g.id,g.picture,g.remark,g.name,null keepId,g.description,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id inner join t_supplier s on s.id=g.supplierId where g.id=? and g.state=1 and s.state=1 GROUP BY g.id";
		}else{
			sql="SELECT s.id supplierId,s.city,s.areas,s.isProprietary,g.id,g.picture,g.remark,g.name,uk.id keepId,g.description,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id LEFT JOIN t_user_keep uk ON uk.goodsId=g.id AND uk.memberId='"+memberId.toString()+"' inner join t_supplier s on s.id=g.supplierId where g.id=? and g.state=1 and s.state=1 GROUP BY g.id";
		}
		try {
			//查询Map集合
			return jdbcTemplate.queryForMap(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}
	
	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsForList(String goodsId) {
		//拼接sql
		String sql="SELECT gi.id identificationId,g. NAME goodsName,g.isShipping,g.id goodsId,g.price,1 buyCount,g.price * 1 total,IF(a.price IS NULL,NULL,a.price * 1)activityTotal,a.price activityPrice,g.picture,g.description FROM t_goods g LEFT JOIN t_goods_activity a ON a.goodsId = g.id INNER JOIN t_goods_identification gi ON gi.id = g.identificationId WHERE g.id=?";
		try {
			//查询list集合
			return jdbcTemplate.queryForList(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
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
	public Map<String, Object> queryGoods(Goods goods,String serviceId,String brandId,String lowPrice,String highPrice,String order,Integer page,Integer row) {
		String sql="SELECT f.* FROM (SELECT g.isRecommend,g.brandId,CONCAT_WS(',',(SELECT sa.label FROM t_supplier_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=g.id AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime and sa.state=1 ORDER BY sa.updateTime DESC LIMIT 0,1),(SELECT sa.label FROM t_mall_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=g.id AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime and sa.state=1 ORDER BY sa.updateTime DESC LIMIT 0,1))label,(SELECT SUM(od.goodsNum) FROM t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo  WHERE o.paymentStatus <> 1 AND o.paymentStatus <> 6 AND od.goodsId=g.id) purchase,s.isProprietary,g.id,g.picture,g.name,g.createTime,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock,MIN(gp.price) minPrice,MAX(gp.price) maxPrice,g.goodsTypeId FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id inner join t_supplier s on s.id=g.supplierId where g.state=1 and s.state=1 GROUP BY g.id) f WHERE 1=1 ";
		//拼接goodsTypeId查询条件
		if(!StringUtil.isEmpty(goods.getGoodsTypeId())){
			sql+=" AND f.goodsTypeId='"+goods.getGoodsTypeId()+"'";
		}
		//拼接name查询条件
		if(!StringUtil.isEmpty(goods.getName())){
			sql+=" AND f.name like '%"+goods.getName()+"%'";
		}
		//拼接isRecommend查询条件
		if(!StringUtil.isEmpty(goods.getIsRecommend())){
			sql+=" AND f.isRecommend = "+goods.getIsRecommend();
		}
		//拼接serviceId查询条件
		if(!StringUtil.isEmpty(serviceId)){
			String[] serviceIds=serviceId.split(",");
			if(ArrayUtils.contains(serviceIds,"1")){
				sql+=" AND f.label is not null AND  f.label <> '' ";
			}
			if(ArrayUtils.contains(serviceIds,"2")){
				sql+=" AND f.isProprietary = 1 ";
			}
			if(ArrayUtils.contains(serviceIds,"3")){
				sql+=" AND f.stock > 0 ";
			}
		}
		//拼接brandId查询条件
		if(!StringUtil.isEmpty(brandId)){
			String[] brandIds=brandId.split(",");
			for(int i=0;i<brandIds.length;i++){
				if(i==0){
					sql+=" AND (f.brandId = '"+brandIds[i]+"'";
				}else{
					sql+=" or f.brandId = '"+brandIds[i]+"'";
				}
				if(i==brandIds.length-1){
					sql+=") ";
				}
			}
		}
		//拼接maxPrice查询条件
		if(!StringUtil.isEmpty(lowPrice)){
			sql+=" AND f.maxPrice >="+lowPrice;
		}
		//拼接maxPrice查询条件
		if(!StringUtil.isEmpty(highPrice)){
			sql+=" AND f.minPrice <="+highPrice;
		}
		sql+="  GROUP BY f.id ";
		//拼接sql排序
		if(StringUtil.isEmpty(order)){
			sql+="order by f.stock desc,f.createTime desc,f.name asc ";
		}else if(order.equals("pasc")){
			sql+=" order by f.minPrice asc,f.stock desc ";
		}else if(order.equals("pdesc")){
			sql+=" order by f.maxPrice desc,f.stock desc ";
		}else if(order.equals("sasc")){
			//sql+=" order by f.purchase,f.purchase";
			sql+=" order by f.purchase desc,f.stock desc ";
		}else if(order.equals("sdesc")){
			sql+=" order by f.purchase desc,f.stock desc ";
		}
		//执行总数查询
		Integer totla=jdbcTemplate.queryForObject("select count(1) from ("+sql+") g",Integer.class);
		if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
			sql+=" limit "+(page-1)*row+","+row;
		}
		Map<String,Object> result=new HashMap<String,Object>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回list集合
			list = jdbcTemplate.queryForList(sql);
			result.put("data",list);
			result.put("total",totla);
		} catch (DataAccessException e) {
			//异常处理
			logger.warn(e);
			return null;
		}
		return result;
	}


	/**
	 * 查询商品库存
	 * @param goodsId
	 * @return
	 */
	@Override
	public Integer queryGoodsStock(String goodsId) {
		String sql="select stock from t_goods where id=?";
		Integer stock=0;
		try {
			//返回执行结果
			stock = jdbcTemplate.queryForObject(sql,new Object[]{goodsId},Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
			return 0;
		}
		return stock;
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
		String sql="SELECT gt.`name`,gt.id,gt.picture FROM t_goods_type gt WHERE gt.state=1 AND gt.pid IS NULL ORDER BY gt.rank";
		if(null!=page&&null!=row){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回执行结果
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询商城首页菜单
	 * @param page
	 * @param row
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryMallMenu(Integer page, Integer row) {
		String sql="SELECT mm.name,mm.id,mm.picture,am.url,am.iosActivity,am.androidActivity,am.data FROM t_mall_menu mm inner join t_app_model am on am.id=mm.modelId WHERE mm.state=1 ORDER BY mm.rank";
		if(null!=page&&null!=row){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回执行结果
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 根据商品类型查询商品
	 * @param goodsTypeId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsByGoodsTypeId(String goodsTypeId) {
		String sql="SELECT g.name,g.id,g.picture,g.price,(SELECT price FROM t_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1'))activityPrice,(SELECT ga.id FROM t_goods_activity ga WHERE ga.goodsId=g.id AND ga.state=1 AND ga.startTime <UNIX_TIMESTAMP() AND ga.endTime >UNIX_TIMESTAMP() AND IF(ga.num IS NOT NULL AND ga.num <> 0,(SELECT COUNT(1) FROM t_order o INNER JOIN t_order_activity oa ON oa.orderNo = o.orderNo WHERE oa.activityId = ga.id AND o.paymentStatus <> 6 )< ga.num,'1=1')) activityId FROM t_goods g INNER JOIN t_goods_type gt ON gt.id=g.goodsTypeId INNER JOIN t_supplier s ON s.id=g.supplierId WHERE g.state=1 AND gt.state=1 AND s.state=1 AND gt.id=? ORDER BY g.rank";
		
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回执行结果
			list = jdbcTemplate.queryForList(sql,new Object[]{goodsTypeId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
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
		String sql="select e.*,m.photo,m.realname from t_evaluate e inner join t_member m on m.id=e.memberId where e.state=1 ";
		//拼接goodsId查询条件
		if(!StringUtil.isEmpty(evaluate.getGoodsId())){
			sql +=" and e.goodsId='"+evaluate.getGoodsId()+"' ";
		}
		//拼接type查询条件
		if(evaluate.getType()!=null){
			sql +=" and e.type="+evaluate.getType();
		}
		sql +=" ORDER BY e.createTime desc  ";
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			//查询总数
			Integer total=super.queryBysqlCount("select count(1) from ("+sql+") f", null);
			if(null!=page&&null!=row){
				sql+=" limit "+(page-1)*row+","+row;
			}
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//执行查询结果
			list = jdbcTemplate.queryForList(sql);
			map.put("total",total);
			map.put("data",list);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}

	/**
	 * 更新商品库存
	 */
	@Override
	public Integer updateGoodsStock(String goodsId, Integer count) {
		String sql="update t_goods set stock=stock-? where id=?";
		Integer i=0;
		try {
			//返回更新结果
			i = jdbcTemplate.update(sql,new Object[]{count,goodsId});
		} catch (DataAccessException e) {
			logger.error(e);
			return null;
		}
		return i;
	}

	/**
	 * 根据商品id查询分销商
	 * @param goodsId
	 * @return
	 */
	@Override
	public Map<String, Object> querySupplierByGoods(String goodsId) {
		String sql="select g.supplierId from t_goods g inner join t_supplier s on s.id=g.supplierId where g.id=?";
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			//返回执行结果
			map = jdbcTemplate.queryForMap(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}
	
	/**
	 * 查询商品积分（已弃用）
	 * @param goodsId
	 * @return
	 */
	@Override
	public Map<String, Object> queryIdentificationByGoods(String goodsId) {
		String sql="select g.identificationId,g.isIntegral,g.integral,g.validityTime from t_goods g inner join t_goods_identification gi on gi.id=g.identificationId where g.id=?";
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			//返回查询结果
			map = jdbcTemplate.queryForMap(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}

	/**
	 * 查询商品图片
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsImg(String goodsId) {
		String sql="SELECT sg.picture img FROM t_goods sg where sg.id=? UNION ALL SELECT p.picPathUrl img FROM t_piclib p INNER JOIN t_goods sg ON sg.id=p.goodId where sg.id=? ";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回查询结果
			list = jdbcTemplate.queryForList(sql,new Object[]{goodsId,goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询商品活动
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryActivity(String goodsId) {
		/*String sql="SELECT * FROM((SELECT 1 type,sa.id,sa.name,sa.activityNum,sa.activityType,sa.label,(SELECT sas.activityFold FROM t_mall_activity_sale sas WHERE sas.mallActivityId=sa.id AND sas.activityPrice<=199 ORDER BY sas.activityPrice DESC LIMIT 0,1) activityFold FROM t_mall_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=? AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime ORDER BY sa.updateTime DESC LIMIT 0,1) UNION ALL"
					+" (SELECT 2 type,sa.id,sa.name,sa.activityNum,sa.activityType,sa.label,(SELECT sas.activityFold FROM t_supplier_activity_sale sas WHERE sas.supplierActivityId=sa.id AND sas.activityPrice<=199 ORDER BY sas.activityPrice DESC LIMIT 0,1) activityFold FROM t_supplier_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=? AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime ORDER BY sa.updateTime DESC LIMIT 0,1))f WHERE f.activityFold IS NOT NULL";*/
		//拼接sql
		String sql="(SELECT 1 type,sa.activityType,sa.name,sa.label,sa.activityNum,sa.id FROM t_supplier_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=? AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime and sa.state=1 ORDER BY sa.updateTime DESC LIMIT 0,1)"
					+"UNION ALL"
					+"(SELECT 2 type,sa.activityType,sa.name,sa.label,null activityNum,sa.id FROM t_mall_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=? AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime and sa.state=1 ORDER BY sa.updateTime DESC LIMIT 0,1)";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回查询结果
			list = jdbcTemplate.queryForList(sql,new Object[]{goodsId,goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询商品活动详情
	 * @param activityId
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryActivityDetail(String activityId,Integer type) {
		String sql="";
		//根据类型拼接sql
		if(type==1){
			sql="SELECT saa.activityFold,saa.activityPrice FROM t_supplier_activity  sa INNER JOIN t_supplier_activity_sale saa ON saa.supplierActivityId=sa.id where sa.id=? order by saa.activityPrice desc";
		}else{
			sql="SELECT saa.activityFold,saa.activityPrice FROM t_mall_activity  sa INNER JOIN t_mall_activity_sale saa ON saa.mallActivityId=sa.id where sa.id=? order by saa.activityPrice desc";
		}
		final List<Map<String, Object>> list=new LinkedList<Map<String,Object>>();
		try {
			//执行sql
			jdbcTemplate.query(sql,new Object[]{activityId},new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Map<String,Object> price=new HashMap<String, Object>();
					price.put("activityFold",rs.getDouble("activityFold"));
					price.put("activityPrice",rs.getDouble("activityPrice"));
					list.add(price);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询商品标签
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryPromise(String goodsId) {
		String sql="select p.name from t_goods_promise gp inner join t_promise p on p.id=gp.promiseId where gp.goodsId=? and p.state=1";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回查询结果
			list = jdbcTemplate.queryForList(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询商品主图
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsIndexImg() {
		String sql = "SELECT * from t_piclib where goodId='goodsIndex' ORDER BY statusDate DESC ";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 查询服务类型
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryService() {
		String sql = "SELECT 1 id,'促销' name FROM DUAL UNION ALL SELECT 2 id,'自营' name FROM DUAL UNION ALL SELECT 3 id,'有货' name FROM DUAL";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 查询品牌
	 */
	@Override
	public List<Map<String, Object>> queryBrand() {
		String sql = "SELECT id,name FROM t_goods_brand where state=1";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 根据名称查询商品
	 */
	@Override
	public List<Map<String, Object>> queryGoodsByName(String name) {
		String sql = "SELECT id,name FROM t_goods where name like '%"+name+"%' and state=1 order by length(name) asc";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 查询常用搜索
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryCommonSearch() {
		String sql = "SELECT name FROM t_common_search where type=1 and state=1 group by name order by sum(clickCount) desc";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 查询历史搜索
	 * @param memberId
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryHistorySearch(String memberId,
			Integer type) {
		String sql = "SELECT name FROM t_history_search where state=1 and memberId=? and type=? group by name order by createTime desc limit 0,10";
		return jdbcTemplate.queryForList(sql,new Object[]{memberId,type});
	}
	
	/**
	 * 添加历史搜索
	 * @param memberId
	 * @param name
	 * @param type
	 * @return
	 */
	@Override
	public Integer addHistorySearch(String memberId, String name, Integer type) {
		String sql="insert into t_history_search(id,name,type,createTime,memberId) values(REPLACE(UUID(),'-',''),?,?,UNIX_TIMESTAMP(),?)";
		Integer i=0;
		try {
			//返回执行结果
			i = jdbcTemplate.update(sql,new Object[]{name,type,memberId});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}
	
	/**
	 * 清空历史搜素
	 * @param memberId
	 * @param type
	 * @return
	 */
	@Override
	public Integer deleteHistorySearch(String memberId,Integer type) {
		String sql="delete from t_history_search where memberId=? and type=?";
		Integer i=0;
		try {
			//返回执行结果
			i = jdbcTemplate.update(sql,new Object[]{memberId,type});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}
	
	/**
	 * 查询历史搜索数量
	 * @param memberId
	 * @param type
	 * @return
	 */
	@Override
	public Integer queryHistorySearchCount(String memberId, Integer type) {
		String sql="select count(1) from t_history_search where memberId=? and type=?";
		Integer i=0;
		try {
			//返回执行结果
			i = jdbcTemplate.queryForObject(sql,new Object[]{memberId,type},Integer.class);
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}
	
	/**
	 * 删除超过十条的数据
	 * @param memberId
	 * @param type
	 * @param num
	 * @return
	 */
	@Override
	public Integer deleteHistorySearchLast(String memberId, Integer type,Integer num) {
		String sql="delete from t_history_search where memberId=? and type=? order by createTime asc limit "+num;
		Integer i=0;
		try {
			//返回执行结果
			i = jdbcTemplate.update(sql,new Object[]{memberId,type});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}
	
	/**
	 * 根据名称删除历史搜索
	 * @param memberId
	 * @param type
	 * @param name
	 * @return
	 */
	@Override
	public Integer deleteHistorySearchByName(String memberId, Integer type,
			String name) {
		String sql="delete from t_history_search where memberId=? and type=? and name=?";
		Integer i=0;
		try {
			//返回执行结果
			i = jdbcTemplate.update(sql,new Object[]{memberId,type,name});
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return i;
	}
	
	/**
	 * 查询平台活动
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryMallActivity() {
		String sql = "SELECT ma.id,GROUP_CONCAT(picPathUrl) picture FROM t_mall_activity ma inner join t_piclib p on p.goodId=ma.id where ma.state=1 AND UNIX_TIMESTAMP()>=startTime AND UNIX_TIMESTAMP()<endTime group by ma.id order by ma.createTime desc";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 查询平台活动详情
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> queryMallActivityDetail(String id) {
	//	String sql="SELECT ma.id,ma.remark,GROUP_CONCAT(p.picPathUrl)picture,p.picture,GROUP_CONCAT((SELECT g.id FROM t_goods_activity ga INNER JOIN t_goods g ON g.id=ga.goodsId where ga.activityId=ma.id AND g.state=1)) goodsId FROM t_mall_activity ma INNER JOIN t_piclib p ON p.goodId=ma.id where ma.id=?";
		String sql="SELECT ma.id,ma.remark,ma.picture,ma.name,GROUP_CONCAT((SELECT g.id FROM t_goods_activity ga INNER JOIN t_goods g ON g.id=ga.goodsId where ga.activityId=ma.id AND g.state=1)) goodsId FROM t_mall_activity ma where ma.id=?";
		Map<String, Object> map=null;
		try {
			//返回执行结果
			map = jdbcTemplate.queryForMap(sql,new Object[]{id});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 根据商品id查询商品信息
	 * @param ids
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsByGoodsIds(String[] ids) {
		String sql="SELECT CONCAT_WS(',',(SELECT sa.label FROM t_supplier_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=g.id AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime and sa.state=1 ORDER BY sa.updateTime DESC LIMIT 0,1),(SELECT sa.label FROM t_mall_activity sa INNER JOIN t_goods_activity ga ON ga.activityId=sa.id WHERE ga.goodsId=g.id AND UNIX_TIMESTAMP()>=sa.startTime AND UNIX_TIMESTAMP()<sa.endTime and sa.state=1 ORDER BY sa.updateTime DESC LIMIT 0,1))label,s.isProprietary,g.id,g.picture,g.name,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id inner join t_supplier s on s.id=g.supplierId where g.id in ("+Util.ArrayToString(ids)+") and g.state=1 and s.state=1";
		List<Map<String, Object>> list=null;
		try {
			//返回执行结果
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return list;
	}

	/**
	 * 根据类型id查询商品
	 * @param typeId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsTypeByTypeId(String typeId) {
		String sql="SELECT GROUP_CONCAT(CONCAT_WS(',',f.id,f.name,f.picture) separator '&') goods,f.typeName FROM (SELECT id,name,picture,(SELECT group_concat(`name` separator '>') FROM t_goods_type WHERE FIND_IN_SET(id,fn_getParentList(gt.id)) AND pid IS NOT NULL AND id <> gt.id ) typeName  FROM t_goods_type gt WHERE FIND_IN_SET(gt.id,fn_getChildLst(?)) ORDER BY gt.rank,gt.createTime,gt.`name` DESC) f  WHERE f.typeName is NOT NULL GROUP BY f.typeName";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回执行结果
			list=jdbcTemplate.queryForList(sql,new Object[]{typeId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
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
		String sql="update t_order set orderPrice=?,receiptsPrice=?,goodsPrice=?,postage=? where orderNo=?";
		Integer i=0;
		try {
			//返回执行结果
			i = jdbcTemplate.update(sql,new Object[]{orderPrice,receiptsPrice,goodsPrice,postage,orderNo});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsSpecificationsTypeByGoodsId(
			String goodsId) {
		String sql="SELECT gd.specificationsId,gs.`name` FROM t_goods_details gd INNER JOIN t_goods_specifications gs ON gs.id=gd.specificationsId WHERE gd.goodsId=? GROUP BY gd.specificationsId";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回执行结果
			list=jdbcTemplate.queryForList(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryGoodsSpecificationsByGoodsId(String goodsId) {
		String sql="SELECT GROUP_CONCAT(CONCAT_WS(',',gds.id,gds.`name`) ORDER BY gds.rank  separator '@') specificationsDetail,gs.`name` specificationsName FROM t_goods_details_specifications gds INNER JOIN t_goods_specifications gs ON gs.id=gds.specificationsId WHERE gds.goodsId=? GROUP BY gs.id";;
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//返回执行结果
			list=jdbcTemplate.queryForList(sql,new Object[]{goodsId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	/**
	 * 查询分成比例
	 * @param dividedTypeId
	 * @param dividedObject
	 * @return
	 */
	@Override
	public Double queryDivided(Integer dividedTypeId, Integer dividedObject) {
		String sql="SELECT dividedProportion FROM t_divided d WHERE d.dividedObject=? AND d.dividedTypeId=? AND d.state=1";
		Double dividedProportion=0.0;
		try {
			//返回执行结果
			dividedProportion = jdbcTemplate.queryForObject(sql,new Object[]{dividedObject,dividedTypeId},Double.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return dividedProportion;
	}
	
	/**
	 * 查询默认分成比例
	 * @param doctorId
	 * @return
	 */
	@Override
	public Map<String,Object> queryCommission(String doctorId) {
		String sql="select * from t_commission where id=?";
		Map<String, Object> map=null;
		try {
			//返回执行结果
			map = jdbcTemplate.queryForMap(sql,new Object[]{doctorId});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}
	
	/**
	 * 查询关注id
	 * @param doctorId
	 * @param patientId
	 * @return
	 */
	@Override
	public String queryFollowId(String doctorId, String patientId) {
		String sql="select id from t_follow_history where followId=? and openId=?";
		String id=null;
		try {
			//返回执行结果
			id = jdbcTemplate.queryForObject(sql,new Object[]{doctorId,patientId},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return id;
	}

	/**
	 * 根基商品id查询规格参数
	 * @param goodsId
	 * @return
	 */
	@Override
	public Map<String, Object> queryGoodsPricenumByGoodsId(String goodsId) {
		String sql="SELECT GROUP_CONCAT(gds.id ORDER BY gds.rank separator '-') name,gp.stock,gp.price,gp.id,gp.picture,gp.rank FROM t_goods_pricenum gp INNER JOIN t_goods_details gd ON gp.id=gd.goodsPriceNumId INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId WHERE gp.goodsId=? GROUP BY gp.id ORDER BY gp.rank";
		final LinkedHashMap<String, Object> map=new LinkedHashMap<String, Object>();
		try {
			//返回执行结果
			jdbcTemplate.query(sql,new Object[]{goodsId},new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Map<String,Object> price=new HashMap<String, Object>();
					price.put("stock",rs.getInt("stock"));
					price.put("price",rs.getDouble("price"));
					price.put("id",rs.getString("id"));
					price.put("picture",rs.getString("picture"));
					price.put("rank",rs.getString("rank"));
					map.put(rs.getString("name"),price);
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return map;
	}

	/**
	 * 根基城市商品和数量查询邮费
	 * @param city
	 * @param goodsId
	 * @param buyCount
	 * @return
	 */
	@Override
	public String queryPostage(String city, String goodsId,Integer buyCount) {
		String sql="SELECT IF(sft.bearFreight=2,0,IFNULL(sftaf.startFee,sft.startFee)+IF(sftaf.startFee IS NOT NULL,floor((?-1)/sftaf.addStandard)*IFNULL(sftaf.addFee,sft.addFee),floor((?-1)/sft.addStandard)*IFNULL(sftaf.addFee,sft.addFee))) startFee FROM t_goods g INNER JOIN t_supplier_freight_template sft ON sft.id=g.freightTemplateId LEFT JOIN t_supplier_freight_template_area sfta ON sfta.templateId=sft.id AND sfta.cityId=? LEFT JOIN t_supplier_freight_template_area_freight sftaf ON sftaf.id=sfta.freightId  WHERE g.id=?";
		String startFee=null;
		try {
			//返回执行结果
			startFee = jdbcTemplate.queryForObject(sql,new Object[]{null==buyCount?1:buyCount,null==buyCount?1:buyCount,city,goodsId},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return startFee;
	}

	/**
	 * 添加常用搜素
	 * @param memberId
	 * @param name
	 * @return
	 */
	@Override
	public Integer addCommonSearch(String memberId, String name) {
		String sql="insert into t_common_search(id,name,type,createTime,clickCount,memberId) values(?,?,?,?,1,?)";
		jdbcTemplate.update(sql,new Object[]{Util.getUUID(),name,1,Util.queryNowTime(),memberId});
		return null;
	}

	/**
	 * 根据城市id和购物车id查询邮费信息
	 * @param city
	 * @param ids
	 * @return
	 */
	@Override
	public String queryPostageByCartIds(String city,String[] ids) {
		String sql="SELECT SUM(startFee) startFee FROM(SELECT MIN(IF(sft.bearFreight=2,0,IFNULL(sftaf.startFee,sft.startFee)+IF(sftaf.startFee IS NOT NULL,floor((sc.buyCount-1)/sftaf.addStandard)*IFNULL(sftaf.addFee,sft.addFee),floor((sc.buyCount-1)/sft.addStandard)*IFNULL(sftaf.addFee,sft.addFee)))) startFee FROM t_shopcart sc INNER JOIN t_goods g  ON g.id=sc.goodsId INNER JOIN t_supplier_freight_template sft ON sft.id=g.freightTemplateId LEFT JOIN t_supplier_freight_template_area sfta ON sfta.templateId=sft.id AND sfta.cityId=? LEFT JOIN t_supplier_freight_template_area_freight sftaf ON sftaf.id=sfta.freightId  WHERE 1=1 ";
		//拼接sql
		if(null!=ids&&ids.length>0){
			sql+=" AND sc.id in ("+Util.ArrayToString(ids)+")";
		}
		sql+=" GROUP BY g.supplierId) f";
		String startFee=null;
		try {
			//返回执行结果
			startFee = jdbcTemplate.queryForObject(sql,new Object[]{city},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return startFee;
	}
	
	/**
	 * 根据城市id和订单号查询邮费
	 * @param city
	 * @param orderNo
	 * @return
	 */
	@Override
	public String queryPostageByGoodsIds(String city,String orderNo) {
		//拼接sql
		String sql="SELECT SUM(startFee) startFee FROM(SELECT MIN(IF(sft.bearFreight=2,0,IFNULL(sftaf.startFee,sft.startFee)+IF(sftaf.startFee IS NOT NULL,floor((od.goodsNum-1)/sftaf.addStandard)*IFNULL(sftaf.addFee,sft.addFee),floor((od.goodsNum-1)/sft.addStandard)*IFNULL(sftaf.addFee,sft.addFee)))) startFee FROM t_goods g inner join t_order_detail od on od.goodsId=g.id INNER JOIN t_supplier_freight_template sft ON sft.id=g.freightTemplateId LEFT JOIN t_supplier_freight_template_area sfta ON sfta.templateId=sft.id AND sfta.cityId=? LEFT JOIN t_supplier_freight_template_area_freight sftaf ON sftaf.id=sfta.freightId  WHERE 1=1 and od.orderNo=? ";
		sql+=" GROUP BY g.supplierId) f";
		String startFee=null;
		try {
			//返回执行结果
			startFee = jdbcTemplate.queryForObject(sql,new Object[]{city,orderNo},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return startFee;
	}
	
	/**
	 * 根据城市id和主订单号查询邮费
	 * @param city
	 * @param orderNo
	 * @return
	 */
	@Override
	public String queryPostageByMainOrderNo(String city,String orderNo) {
		//拼接sql
		String sql="SELECT ifnull(SUM(startFee),0) startFee FROM(SELECT MIN(IF(sft.bearFreight=2,0,IFNULL(sftaf.startFee,sft.startFee)+IF(sftaf.startFee IS NOT NULL,floor((od.goodsNum-1)/sftaf.addStandard)*IFNULL(sftaf.addFee,sft.addFee),floor((od.goodsNum-1)/sft.addStandard)*IFNULL(sftaf.addFee,sft.addFee)))) startFee FROM t_goods g inner join t_order_detail od on od.goodsId=g.id inner join t_order o on o.orderNo=od.orderNo  INNER JOIN t_supplier_freight_template sft ON sft.id=g.freightTemplateId LEFT JOIN t_supplier_freight_template_area sfta ON sfta.templateId=sft.id AND sfta.cityId=? LEFT JOIN t_supplier_freight_template_area_freight sftaf ON sftaf.id=sfta.freightId  WHERE 1=1 and o.mainOrderNo=? and o.orderType=1 ";
		sql+=" GROUP BY g.supplierId) f";
		String startFee=null;
		try {
			//返回执行结果
			startFee = jdbcTemplate.queryForObject(sql,new Object[]{city,orderNo},String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return startFee;
	}

	/**
	 * 根据城市id查询城市名
	 * @param city
	 * @return
	 */
	@Override
	public String queryCityIdByCity(String city) {
		//拼接sql
		String sql="SELECT cityid from t_cities where city=?";
		String cityId=null;
		try {
			if(!StringUtil.isEmpty(city)){
				//返回执行结果
				cityId = jdbcTemplate.queryForObject(sql,new Object[]{city},String.class);
			}
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return cityId;
	}
}
