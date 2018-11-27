package cn.syrjia.common.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.common.LogServiceInterface;
import cn.syrjia.entity.Log;

/**
 * 
 * @author liwenlong
 * 日志
 */
@Service("logService")
public class LogServiceImpl implements LogServiceInterface{
	
	@Resource(name="baseDao")
	BaseDaoInterface baseDao;

	@Override
	public Object insertLog(Log log) {
		return baseDao.addEntity(log);
	}

	@Override
	public Map<String, Object> searchLog(Log log,Integer page,Integer row,Map<String,List<String>> setMap) {
		if("all".equals(log.getModular())){
			log.setModular(null);
		}
		return baseDao.queryListByPage(log, page,row,setMap);
	}

}
