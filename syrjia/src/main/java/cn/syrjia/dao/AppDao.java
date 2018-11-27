package cn.syrjia.dao;

import java.util.Map;
import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Advertising;

public interface AppDao extends BaseDaoInterface{

	/**
	 * 查询广告
	 * @param request
	 * @param advertising
	 * @return
	 */
	Map<String,Object> queryAdvertising(Advertising advertising);
	
	/**
	 * 获取token
	 * @param request
	 * @param phone
	 * @param memberId
	 * @param oldToken
	 * @return
	 */
	Map<String,Object> queryMemberToken(String _token);
	
	/**
	 * 添加token
	 */
	Integer addMemberToken(String memberId,String _token);
	
	/**
	 * 根据手机号查询医生信息
	 * @param loginName
	 * @return
	 */
	Integer queryUserByLoginName(String loginName);
	
	/**
	 * 根据手机号查询医生信息
	 * @param loginName
	 * @return
	 */
	String queryDoctorByLoginName(String loginName);
	
	/**
	 * 根据医生id查询医生信息
	 * @param loginName
	 * @return
	 */
	Map<String,Object> queryDoctorById(String userId);
	
	/**
	 * 查询app最新安装包
	 * @param port
	 * @return
	 */
	Map<String,Object> queryAppByLast(Integer port,Integer phoneType,String version);
	
	/**
	 * 查询app最新物理地址
	 * @param port
	 * @return
	 */
	Map<String,Object> queryAppByLastAddr(Integer port);
	
	/**
	 * 根据用户id查询token信息
	 * @param memberId
	 * @return
	 */
	String queryTokenByMemberId(String memberId);
}
