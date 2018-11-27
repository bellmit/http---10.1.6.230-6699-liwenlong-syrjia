package cn.syrjia.common.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.config.configCode;
import cn.syrjia.util.Util;
import cn.syrjia.util.logistics.LogisiticsUtil;
import cn.syrjia.wxPay.wxPay.util.StringUtil;


/**
 * service抽象类
 * @author liwenlong
 *
 * 2016-6-16
 */
@Service("baseService")
public class  BaseServiceImpl implements BaseServiceInterface{
	
	
	@Resource(name="baseDao")
	BaseDaoInterface baseDao;

	/**
	 * 根据实体查询
	 * @param entity 实体
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> query(T entity,boolean...isChild){
		return baseDao.query(entity,isChild);
	}
	
	
	/**
	 * 根据实体查询单个
	 * @param entity 实体
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> T queryByEntity(T entity,boolean...isChild){
		return baseDao.queryByEntity(entity,isChild);
	}
	
	/**
	 * 根据id查询单个
	 * @param entity 实体
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> T queryById(Class<T> t ,Object id,boolean...isChild){
		return baseDao.queryById(t, id,isChild);
	}
	
	/**
	 * 分页
	 * @param entity 实体
	 * @return
	 */
	@SuppressWarnings({ "hiding", "unchecked" })
	public <T> Map<String,Object> queryListByPage(T entity,T... i){
		return baseDao.queryListByPage(entity, i);
	}
	

	
	
	/**
	 * 修改
	 */
	@SuppressWarnings("hiding")
	@Transactional(rollbackFor=Exception.class)
	public <T> Integer updateEntity(T entity){
		return baseDao.updateEntity(entity);
	}
	
	/**
	 * 删除 根据参数
	 */
	@SuppressWarnings({ "hiding", "unchecked" })
	@Transactional(rollbackFor=Exception.class)
	public <T> Map<String,Object> deleteEntity(T entity,T... t){
		return null;
	}
	
	/**
	 * 删除
	 */
	@SuppressWarnings("hiding")
	@Transactional(rollbackFor=Exception.class)
	public <T> Integer deleteEntity(Class<T> entity,Object id){
		return baseDao.deleteEntity(entity, id);
	}
	
	
	/**
	 * 增加
	 */
	@SuppressWarnings("hiding")
	@Transactional(rollbackFor=Exception.class)
	public <T> Object addEntity(T entity){
		return baseDao.addEntity(entity);
	}
	
	@SuppressWarnings("hiding")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public <T> Object addEntity(List<T> entitys) {
		return baseDao.addEntity(entitys);
	}
	
	/**
	 * 增加
	 */
	@SuppressWarnings("hiding")
	@Transactional(rollbackFor=Exception.class)
	public <T> Object addEntityUUID(List<T> entitys){
		return baseDao.addEntityUUID(entitys);
	}
	
	/**
	 * 增加
	 */
	@SuppressWarnings("hiding")
	@Transactional(rollbackFor=Exception.class)
	public <T> Object addEntityUUID(T entity){
		return baseDao.addEntityUUID(entity);
	}
	
	
	@SuppressWarnings("hiding")
	@Override
	public <T> Integer exist(Class<T> t, Map<String, Object> map) {
		return baseDao.exist(t, map);
	}
	
	@SuppressWarnings("hiding")
	@Override
	public <T> Integer existId(Class<T> t, Map<String, Object> map) {
		return baseDao.existId(t, map);
	}
	
	
	@SuppressWarnings("hiding")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public <T> Integer batchUpdate(List<T> entitys) {
		return baseDao.batchUpdate(entitys);
	}
	
	
	@SuppressWarnings("hiding")
	@Override
	public <T> Integer deleteEntityList(Class<T> entity, List<Object> ids) {
		return baseDao.deleteEntityList(entity, ids);
	}


	@SuppressWarnings("hiding")
	@Override
	public <T> List<T> query(Class<T> t, Map<String, Object> map,boolean...isChild) {
		return baseDao.query(t, map,isChild);
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T queryOne(Class<T> t, Map<String, Object> map,boolean...isChild) {
		return baseDao.queryOne(t, map,isChild);
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <T> Integer queryListByPageNum(T entity, T... i) {
		return baseDao.queryListByPageNum(entity, i);
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> Map<String, Object> queryListByPage(Class<T> t,
			Map<String, Object> param,boolean...isChild) {
		return baseDao.queryListByPage(t, param,isChild);
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> Integer queryListByPageNum(Class<T> entity,
			Map<String, Object> param) {
		return baseDao.queryListByPageNum(entity, param);
	}

	@SuppressWarnings("hiding")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public <T> Integer updateOrAdd(T entity) {
		return baseDao.updateOrAdd(entity);
	}


	@Override
	public Integer queryBysqlCount(String sql, Object[] obj) {
		return baseDao.queryBysqlCount(sql, obj);
	}


	@Override
	public List<Map<String, Object>> queryBysqlList(String sql, Object[] obj) {
		return baseDao.queryBysqlList(sql, obj);
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> List<T> queryBysqlListBean(Class<T> t, String sql, Object[] obj) {
		return baseDao.queryBysqlListBean(t, sql, obj);
	}


	@Override
	public Map<String, Object> queryBysqlMap(String sql, Object[] obj) {
		return baseDao.queryBysqlMap(sql, obj);
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public Integer update(String sql, Object[] obj) {
		return baseDao.update(sql, obj);
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public Integer delete(String sql, Object[] obj) {
		return baseDao.delete(sql, obj);
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public Integer deleteBatch(String sql, List<Object> list) {
		return baseDao.deleteBatch(sql, list);
	}


	@Override
	public Map<String, Object> getTicket() {
		return baseDao.getTicket();
	}
	
	@Override
	public List<Map<String, Object>> queryProvinces() {
		return baseDao.queryProvinces();
	}
	
	@Override
	public List<Map<String, Object>> queryCitys(String provinceId) {
		return baseDao.queryCitys(provinceId);
	}
	
	@Override
	public List<Map<String, Object>> queryAreas(String cityId) {
		return baseDao.queryAreas(cityId);
	}


	@Override
	public Map<String, Object> getSysSet() {
		return baseDao.getSysSet();
	}
	
	@Override
	public Map<String, Object> queryLogistics(String id, String orderNo) {
		if(StringUtil.isEmpty(orderNo)){
			return Util.resultMap(configCode.code_1029, null);
		}
		Map<String,Object> logisticsMap = baseDao.queryLogisiticsByOrderNo(orderNo);
		if(logisticsMap==null){
			return Util.resultMap(configCode.code_1101, null);
		}
		if(!"SF".equals(logisticsMap.get("logisticsCode"))){
			return Util.resultMap(configCode.code_1101, null);
		}
		Map<String,Object> logisiticsDetail = LogisiticsUtil.querySFLogisitics(logisticsMap.get("logisticsNo").toString());
/*		if(logisiticsDetail==null){
			return Util.resultMap(configCode.code_1101, null);
		}
		if(Integer.valueOf(logisiticsDetail.get("respCode").toString())!=1001){
			return logisiticsDetail;
		}*/
		logisticsMap.put("logisiticsList", logisiticsDetail.get("data"));
		return Util.resultMap(configCode.code_1001, logisticsMap);
	}
	
	@Override
	public Map<String, Object> queryLoginLogByUserId(String userId) {
		return baseDao.queryLoginLogByUserId(userId);
	}
	
	@Override
	public Map<String, Object> queryOpenIdByPatientId(String patientId) {
		return baseDao.queryOpenIdByPatientId(patientId);
	}


	@Override
	public Map<String, Object> querySysSet() {
		return Util.resultMap(configCode.code_1001, baseDao.querySysSet());
	}


	@Override
	public Integer updateCodeState(String phone, Integer type, String code) {
		return baseDao.updateCodeState(phone, type, code);
	}


	@Override
	public Map<String, Object> queryWxUserByOpenId(String openId) {
		return baseDao.queryWxUserByOpenId(openId);
	}


	@Override
	public Map<String, Object> queryMembersById(String openId) {
		return baseDao.queryMembersById(openId);
	}


	@Override
	public Map<String, Object> queryPatientsById(String id) {
		return baseDao.queryPatientsById(id);
	}


	@Override
	public Integer insertPhoneVersion(String content, String id) {
		return baseDao.insertPhoneVersion(content, id);
	}


	@Override
	public Map<String, Object> querySalesOne(String id) {
		return baseDao.querySalesOne(id);
	}
}
