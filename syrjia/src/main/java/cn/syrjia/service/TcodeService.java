package cn.syrjia.service;

import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;

public interface TcodeService extends BaseServiceInterface{
	/**
	 * 保存验证码信息 并发送
	 * @param 手机号  验证码  类型
	 * @return 
	 */
	public abstract Map<String,Object> saveCode(String phone,String code,Integer type,String ip);

	/**
	 * 根据手机号和类型获取验证码信息
	 * @param phone 手机号 type 类型
	 * @return
	 */
	public abstract Map<String,Object> getCodeByPhoneAndType(String phone,Integer type);
	
	/**
	 * 查询当天的电话数
	 * @param phone
	 * @param type
	 * @param ip
	 * @return
	 */
	public abstract int getPhoneNumberToday(String phone,Integer type,String ip);
}
