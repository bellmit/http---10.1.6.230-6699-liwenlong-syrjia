package cn.syrjia.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.TcodeDao;
import cn.syrjia.service.TcodeService;
import cn.syrjia.util.BatchPublishSMSMessageDemo;
import cn.syrjia.util.SMSTemplateIdUtil;
import cn.syrjia.util.qcloudsms.QCloudSmsUtil;

@Service("tcodeService")
public class TcodeServiceImpl extends BaseServiceImpl implements TcodeService {

	@Resource(name = "tcodeDao")
	TcodeDao tcodeDao;

	/**
	 * 保存验证码信息 并发送
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String,Object> saveCode(String phone,String code,Integer type,String ip){
		Map<String,Object> m = new HashMap<String, Object>();
		try {
			String oldCode = code;
			String[] params = {code};
			Boolean flag = QCloudSmsUtil.sendSmsByTemId(phone, SMSTemplateIdUtil.sendCode_SMS, params);
			//code = "{\"authcode\":\"" + code + "\"}";
			//Boolean flag = BatchPublishSMSMessageDemo.opera(phone, code,SMSTemplateIdUtil.sendCode_SMS);
			//保存验证码信息
			int i = tcodeDao.saveCode(phone, oldCode, type,ip);
			m.put("state", i);
			m.put("result", flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * 根据手机号和类型获取验证码信息
	 */
	public Map<String, Object> getCodeByPhoneAndType(String phone, Integer type) {
		return tcodeDao.getCodeByPhoneAndType(phone, type);
	}

	/**
	 * 根据手机号和类型获取验证码信息
	 */
	@Override
	public int getPhoneNumberToday(String phone, Integer type,String ip) {
		return tcodeDao.getPhoneNumberToday(phone, type, ip);
	}

}
