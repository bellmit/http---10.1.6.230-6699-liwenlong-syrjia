package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;

public interface ScoreConsumeDao extends BaseDaoInterface {
	
	/**
	 * 查询今天是否已签到
	 * @param userId
	 * @return
	 */
	abstract Integer queryTodayIsClick(String userId);

	/**
	 * 查询本月签到列表
	 * @param userId
	 * @return
	 */
	abstract List<Map<String,Object>> queryClicks(String userId);
}
