package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.ScoreConsumeDao;
import cn.syrjia.entity.ScoreConsume;
import cn.syrjia.entity.SysSet;
import cn.syrjia.service.ScoreConsumeService;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.Util;

@Service("scoreConsumeService")
public class ScoreConsumeServiceImpl extends BaseServiceImpl implements ScoreConsumeService{

	@Resource(name = "scoreConsumeDao")
	ScoreConsumeDao scoreConsumeDao;
	/**
	 * 查询今天是否已签到
	 */
	@Override
	public Map<String, Object> queryTodayIsClick(String userId) {
		Integer i=scoreConsumeDao.queryTodayIsClick(userId);
		if(null==i){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,i);
	}

	/**
	 * 查询本月签到列表
	 */
	@Override
	public Map<String, Object> queryClicks(String userId) {
		//查询
		List<Map<String, Object>> list=scoreConsumeDao.queryClicks(userId);
		if(null==list){
			return Util.resultMap(configCode.code_1015,list);
		}
		return Util.resultMap(configCode.code_1001,list);
	}

	/**
	 * 签到
	 */
	@Override
	public Map<String, Object> click(ScoreConsume scoreConsume) {
		//查询今天是否已签到
		Integer i=scoreConsumeDao.queryTodayIsClick(scoreConsume.getUserid());
		if(null!=i&&i>0){
			return Util.resultMap(configCode.code_1072,null);
		}
		SysSet sysSet=null==RedisUtil.getVal("sysSet")?null:(SysSet)RedisUtil.getVal("sysSet");
		//赋值
		scoreConsume.setOrderNo("1");
		scoreConsume.setCreatetime(Util.queryNowTime());
		scoreConsume.setConsumeScore(null==sysSet?10: (double)sysSet.getClickScore());
		//执行添加
		Object obj=scoreConsumeDao.addEntity(scoreConsume);
		if(null==obj){
			return Util.resultMap(configCode.code_1015,obj);
		}
		return Util.resultMap(configCode.code_1001,scoreConsume.getConsumeScore());
	}
}
