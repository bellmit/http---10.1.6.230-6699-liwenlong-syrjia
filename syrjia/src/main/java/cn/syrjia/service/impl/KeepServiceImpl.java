package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.KeepDao;
import cn.syrjia.entity.Keep;
import cn.syrjia.service.KeepService;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Service("keepService")
public class KeepServiceImpl extends BaseServiceImpl implements KeepService{

	@Resource(name = "keepDao")
	KeepDao keepDao;
	
	/**
	 * 查询keep
	 */
	@Override
	public Map<String, Object> queryKeep(Keep keep, Integer page,
			Integer row) {
		//分页查询
		List<Map<String,Object>> list=keepDao.queryKeep(keep, page, row);
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list); 
	}

	//添加 
	@Override
	public Map<String, Object> addKeep(Keep keep) {
		String id=keepDao.hasKeep(keep);
		Object obj=null;
		//非空判断
		if(StringUtil.isEmpty(id)){
			keep.setCreateTime(Util.queryNowTime());
			if(keep.getType()==null){
				keep.setType(1);
			}
			//执行添加
			obj=keepDao.addEntityUUID(keep);
			if(null==obj){
				return Util.resultMap(configCode.code_1015, obj);
			}
		}else{
			obj=keepDao.deleteKeep(keep);
		}
		return Util.resultMap(configCode.code_1001,obj); 
	}

	/**
	 * 删除
	 */
	@Override
	public Map<String, Object> deleteKeep(Keep keep) {
		Integer i=keepDao.deleteKeep(keep);
		if(null==i){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,null); 
	}
	
	/**
	 * 批量删除
	 */
	@Override
	public Map<String, Object> deleteKeeps(String[] keeps) {
		Integer i=keepDao.deleteKeeps(keeps);
		if(null==i){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,null); 
	}

	/**
	 * 通过实体查询
	 */
	@Override
	public Map<String, Object> hasKeep(Keep keep) {
		String id=keepDao.hasKeep(keep);
		if(null==id){
			return Util.resultMap(configCode.code_1015, id);
		}
		return Util.resultMap(configCode.code_1001, id); 
	}

}
