package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.StatisticsDao;
import cn.syrjia.service.StatisticsService;

@Service("statisticsService")
public class StatisticsServiceImpl extends BaseServiceImpl implements StatisticsService{

	@Resource(name = "statisticsDao")
	StatisticsDao statisticsDao;
	
	/**
	 * 添加统计
	 */
	@Override
	public Integer addStatistics(List<Map<String, Object>> list, String openId,
			String memberId) {
		return statisticsDao.addStatistics(list, openId, memberId);
	}

}
