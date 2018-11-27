package cn.syrjia.dao;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.syrjia.common.BaseDaoInterface;

public interface MemberDao extends BaseDaoInterface{
	
	/**
	 * 注销 清空token
	 * @Description: TODO
	 * @param @param userid
	 * @param @return   
	 * @date 2017-6-20
	 */
	public Integer logout(String userid);
	
	/**
	 * 根据微信openid获取微信用户
	 * @Description: TODO
	 * @param @param Openid
	 * @param @return   
	 * @return Map<String,Object>  
	 */
	public Map<String, Object> getWeixinByOpenid(String Openid);
	
	/**
	 * 根据登录名查找用户
	 * @Description: TODO
	 * @param @param loginname
	 * @param @return   
	 * @return Map<String,Object>  
	 */
	public Map<String, Object> queryByLoginname(String loginname,String memberId);
	
	/**
	 * 更新用户生日
	 * @param userId
	 * @param birthday
	 * @return
	 */
	public Integer updateUserBirthday(String userId,String birthday);
	
	/**
	 * 用户开关
	 * @param request
	 * @param userid
	 * @param state
	 * @return
	 */
	public Map<String,Object> userClockOn(HttpServletRequest request,String userid,Integer state);

	/**
	 * 更新密码
	 * @param password
	 * @param loginName
	 * @return
	 */
	public abstract Integer updatePassWord(String password,String loginName);
	
	/**
	 * 通过openid查询
	 * @param openid
	 * @return
	 */
	public abstract Integer clearByOpenid(String openid);
	
	/**
	 * 通过openid获取用户
	 * @param openid
	 * @return
	 */
	public abstract Map<String,Object> getMemberByOpenid(String openid);
	
	/**
	 * 修改联系方式
	 * @Description: TODO
	 * @param @param phone
	 * @param @param loginName
	 * @param @return   
	 * @return Integer  
	 * @throws
	 * @date 2018-3-23
	 */
	public abstract Integer updatePhone(String phone, String loginName,String memberId);
	
	/**
	 * 根据用户ID查询用户信息
	 * @param phone
	 * @return
	 */
	public abstract Map<String,Object> queryMemberById(String memberId);
	
	/**
	 * 检验手机号是否存在
	 * @param phone
	 * @return
	 */
	public abstract Integer checkMemberPhone(String phone);
	
	/**
	 * 检验用户是否绑定安全手机号
	 * @param phone
	 * @return
	 */
	public abstract Integer checkMemberIsBindPhone(String memberId);
	
	/**
	 * 通过id查询member
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryMemberOne(String id);
}

	
	
	
	
