package cn.syrjia.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Keep;

public interface KeepService extends BaseServiceInterface{

	/**
	 * 查询keep
	 * @param keep
	 * @param page
	 * @param row
	 * @return
	 */
	abstract Map<String, Object> queryKeep(Keep keep,Integer page,Integer row);
	
	/**
	 * 添加keep
	 * @param keep
	 * @return
	 */
	abstract Map<String, Object> addKeep(Keep keep);
	
	/**
	 * 删除
	 * @param keep
	 * @return
	 */
	abstract Map<String, Object> deleteKeep(Keep keep);
	
	/**
	 * 批量删除
	 * @param keeps
	 * @return
	 */
	abstract Map<String, Object> deleteKeeps(String[] keeps);
	
	/**
	 * 通过实体查询
	 * @param keep
	 * @return
	 */
	abstract Map<String, Object> hasKeep(Keep keep);
}
