package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Member;
import cn.syrjia.entity.Piclib;
import cn.syrjia.entity.UserAdvise;

public interface  MemberService extends BaseServiceInterface{
	
	/**
	 * 添加用户
	 * @param member
	 * @return
	 */
	public abstract Object addMember(Member member);
	 
	/**
	 * 上传头像
	 * @param image
	 * @param id
	 * @param request
	 * @return
	 */
	public Map<String, Object> insertUserHeadInfo(MultipartFile image, String id,HttpServletRequest request);

	/**
	 * 注销，清空token信息
	 * @Description: TODO
	 * @param @param userid
	 * @param @return   
	 */
	public abstract Integer logout(String userid);
	
	/**
	 * 根据openid查找微信用户信息
	 * @Description: TODO
	 * @param @param Openid
	 * @param @return   
	 * @return Map<String,Object>  
	 */
	public abstract Map<String, Object> getWeixinByOpenid(String Openid);
	/**
	 * 根据登录名获取用户信息
	 * @Description: TODO
	 * @param @param loginname
	 * @param @return   
	 * @return Map<String,Object>  
	 * @date 2017-6-24
	 */
	public abstract Map<String, Object> getByLoginname(String loginname,String memberId);
	
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
	 * @param _sign
	 * @return
	 */
	public abstract Map<String,Object> userClockOn(HttpServletRequest request,String userid,Integer state,String _sign);
	
	/**
	 * 更新用户的登录数据
	 * @param request
	 * @param member
	 * @return
	 */
	public abstract Map<String,Object> updateUserLoginData(HttpServletRequest request,Member member);

	/**
	 * 更新密码
	 * @param password
	 * @param loginName
	 * @return
	 */
	public abstract Integer updatePassWord(String password,String loginName);
	
	/**
	 * 添加用户警告
	 * @param userAdvise
	 * @param piclist
	 * @param request
	 * @return
	 */
	public abstract Object addUserAdvise(UserAdvise userAdvise,List<Piclib> piclist,HttpServletRequest request);

	/**
	 * 通过openid清除
	 * @param openid
	 * @return
	 */
	public abstract Integer clearByOpenid(String openid);
	
	/**
	 * 修改联系方式
	 * @Description: TODO
	 * @param @param password
	 * @param @param loginName   
	 * @return void  
	 * @throws
	 * @date 2018-3-23
	 */
	public abstract Integer updatePhone(HttpServletRequest request,String phone, String loginName,String code,String memberId);
	
	/**
	 * 获取验证码
	 * @param request
	 * @param phone
	 * @param type
	 * @return
	 */
	public abstract Map<String,Object> getPhoneCode(HttpServletRequest request,String phone, Integer type,String memberId);
	

	/**
	 * 检验用户是否绑定安全手机号
	 * @param phone
	 * @return
	 */
	public abstract Map<String,Object> checkMemberIsBindPhone(HttpServletRequest request,String memberId);
	
	/**
	 * 根据openId获取member信息
	 * @param openid
	 * @return
	 */
	public abstract Map<String,Object> getMemberByOpenid(String openid);

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryMemberOne(String id);
	
}
