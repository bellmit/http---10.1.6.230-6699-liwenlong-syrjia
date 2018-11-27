package cn.syrjia.common;

import java.util.List;
import java.util.Map;

import cn.syrjia.entity.Log;


/**
 * 
 * @author liwenlong
 * 日志
 */
public interface LogServiceInterface {

	
	/**
	 * 添加日志
	 * @param log
	 * @return
	 */
	public abstract Object insertLog(Log log);
	
	
	/**
	 * 查询日志
	 * @return
	 */
	public abstract Map<String, Object> searchLog(Log log,Integer page,Integer row,Map<String,List<String>> setMap);
}
