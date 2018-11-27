package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;


public interface ClinicDao extends BaseDaoInterface{

	/**
	 * 根据医馆名称查询主键
	 * @param name
	 * @return
	 */
	public Object queryIdByName(String name);
	/**
	 * 获取医馆下科室数量和医生数量
	 * @param hosId
	 * @return
	 */
	public Map<String,Object> queryDepNumAndDocNum(String hosId);
	/**
	 * 查询医馆下的科室列表
	 * @param hosId
	 * @return
	 */
	public List<Map<String,Object>> queryDepList(String hosId);
}
