package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Push;
import cn.syrjia.util.Pager;

public interface PushDao extends BaseDaoInterface{

	/**
	 * 获取消息推送信息
	 * @Description: TODO
	 * @param @param mmPush
	 * @param @param pager
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-22
	 */
	public abstract List<Map<String, Object>> queryPushByPager(Push mmPush,Pager pager);
	/**
	 * 获取记录数
	 * @Description: TODO
	 * @param @param mmPush
	 * @param @return   
	 * @return Integer  
	 * @throws
	 * @author 王昭阳
	 * @date 2017-6-22
	 */
	public abstract Integer queryPushCount(Push mmPush);
}
