package cn.syrjia.hospital.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.hospital.entity.Hospital;

public interface ClinicService extends BaseServiceInterface{

	/**
	 * 获取医馆详情
	 * @param id
	 * @return
	 */
	public Hospital queryHosById(String id);
	/**
	 * 查询医诊堂头部信息
	 * @param hosId
	 * @return
	 */
	public Map<String,Object> queryTopMsg(String hosId);
	/**
	 * 查询医馆下包含的科室列表
	 * @param hosId
	 * @return
	 */
	public List<Map<String,Object>> queryDepList(String hosId);
}
