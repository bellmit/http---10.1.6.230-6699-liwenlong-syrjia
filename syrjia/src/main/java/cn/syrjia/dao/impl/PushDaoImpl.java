package cn.syrjia.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.PushDao;
import cn.syrjia.entity.Push;
import cn.syrjia.util.Pager;
@Repository("pushDao")
public class PushDaoImpl extends BaseDaoImpl implements PushDao{

	/**
	 * 获取消息推送信息
	 */
	@Override
	public List<Map<String, Object>> queryPushByPager(Push mmPush, Pager pager) {
		// TODO Auto-generated method stub
		String sql = "select a.id,a.title,a.comment,a.userid,a.token,a.deleteflag,a.type,DATE_FORMAT(a.createime,'%Y-%m-%d %H:%i:%s') as createime" +
				" from (select * from t_mm_push where deleteflag = 0 and userid = "
		+ mmPush.getUserid()+" order by createime desc) a limit "+pager.getStart()+","+pager.getRow();
		//执行查询
		return this.queryBysqlList(sql, null);
	}

	/**
	 * 获取记录数
	 */
	@Override
	public Integer queryPushCount(Push mmPush) {
		// TODO Auto-generated method stub
		String sqlString = "select count(*) from t_mm_push where deleteflag = 0 and userid = "
				+ mmPush.getUserid();
		return this.queryBysqlCount(sqlString, null);
	}

}
