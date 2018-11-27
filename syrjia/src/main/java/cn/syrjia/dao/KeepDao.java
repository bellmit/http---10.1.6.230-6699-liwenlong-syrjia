package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Keep;

public interface KeepDao extends BaseDaoInterface{

	/**
	 * 查询keep
	 * @param keep
	 * @param page
	 * @param row
	 * @return
	 */
	abstract List<Map<String,Object>> queryKeep(Keep keep,Integer page,Integer row);
	
	/**
	 * 添加
	 * @param keep
	 * @return
	 */
	abstract Integer addKeep(Keep keep);
	
	/**
	 * 删除
	 * @param keep
	 * @return
	 */
	abstract Integer deleteKeep(Keep keep);
	
	/**
	 * 批量删除
	 * @param keeps
	 * @return
	 */
	abstract Integer deleteKeeps(String[] keeps);
	
	/**
	 * 实体查询
	 * @param keep
	 * @return
	 */
	abstract String hasKeep(Keep keep);
}
