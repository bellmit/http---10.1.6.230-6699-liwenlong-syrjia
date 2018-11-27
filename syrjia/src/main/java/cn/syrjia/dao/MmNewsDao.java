package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.util.Pager;

public interface MmNewsDao extends BaseDaoInterface{

	/**
	 * 分页查询列表信息
	 * @Description: TODO
	 * @param @param pager
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-21
	 */
	public abstract List<Map<String, Object>> queryByPager(Pager pager,Integer userid,Integer newstype);
	/**
	 * 分页查询总数
	 * @Description: TODO
	 * @param @return   
	 * @return Integer  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-21
	 */
	public abstract Integer queryByPagerCount(Integer userid,Integer newstype);
	/**
	 * 根据新闻id查询风采详情
	 * @Description: TODO
	 * @param @param actid
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-21
	 */
	public abstract List<Map<String, Object>> queryByActid(Integer actid);
}
