package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;

public interface StatisticsService extends BaseServiceInterface{

	/**
	 * 添加统计
	 * @param list
	 * @param openId
	 * @param memberId
	 * @return
	 */
	Integer addStatistics(List<Map<String, Object>> list,String openId,String memberId);
}
