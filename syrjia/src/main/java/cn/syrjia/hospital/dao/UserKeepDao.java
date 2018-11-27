package cn.syrjia.hospital.dao;

import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.hospital.entity.UserKeep;

public interface UserKeepDao extends BaseDaoInterface {
	
	/**
	 * 根据openid 查询该医生是否已收藏
	 * @param keep
	 * @return
	 */
	public abstract Map<String,Object> queryDoctorId(UserKeep keep);

	/**
	 * 根据条件删除收藏
	 * @param keep
	 * @return
	 */
	public abstract Object deleteUserKeep(UserKeep keep);
	
	
	
}
