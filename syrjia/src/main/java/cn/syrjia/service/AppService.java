package cn.syrjia.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Advertising;

public interface AppService extends BaseServiceInterface{

	/**
	 * 查询广告
	 * @param request
	 * @param advertising
	 * @return
	 */
	Map<String,Object> queryAdvertising(Advertising advertising);
	
	/**
	 * 查询token
	 * @param _token
	 * @return
	 */
	Map<String,Object> queryMemberToken(String _token);
	
	/**
	 * 获取token
	 * @param memberId
	 * @param oldToken
	 * @param phone
	 * @return
	 */
	Map<String,Object> getToken(String memberId, String oldToken,String phone);
	
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
	 * 下载app
	 * @param port
	 * @return
	 */
	ResponseEntity<byte[]> downApp(Integer port);
	
	/**
	 * 添加token
	 * @param memberId
	 * @param _token
	 * @return
	 */
	Integer addMemberToken(String memberId,String _token);
	
	/**
	 * 根据用户id查询token信息
	 * @param memberId
	 * @return
	 */
	String queryTokenByMemberId(String memberId);
}
