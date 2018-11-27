package cn.syrjia.hospital.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;

public interface HealthProductService extends BaseServiceInterface{

	/**
	 * 药品名录查询使用
	 * @param name
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	public abstract Map<String, Object> queryHealthProducts(String doctorId,String name,String type, Integer page,
			Integer row);
	
	/**
	 * 申请增加药味
	 * @param doctorId
	 * @param content
	 * @return
	 */
	public abstract Map<String,Object> applyAddDrug(String doctorId,String content);

	
}
