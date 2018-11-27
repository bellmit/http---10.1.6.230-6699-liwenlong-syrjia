package cn.syrjia.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.ScoreConsume;

public interface ScoreConsumeService extends BaseServiceInterface{

	/**
	 * 查询今天是否已签到
	 * @param userId
	 * @return
	 */
	public abstract Map<String,Object> queryTodayIsClick(String userId);
	
	/**
	 * 查询本月签到列表
	 * @param userId
	 * @return
	 */
	public abstract Map<String,Object> queryClicks(String userId);
	
	/**
	 * 签到
	 * @param scoreConsume
	 * @return
	 */
	public abstract Map<String,Object> click(ScoreConsume scoreConsume);
	
}
