package cn.syrjia.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.AppDao;
import cn.syrjia.entity.Advertising;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.service.AppService;
import cn.syrjia.util.RedisUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Service("appService")
public class AppServiceImpl extends BaseServiceImpl implements AppService{

	@Resource(name="appDao")
	AppDao appDao;
	
	@Resource(name = "appDoctorDao")
	AppDoctorDao appDoctorDao;
	
	/**
	 * 获取token
	 * @param request
	 * @param phone
	 * @param memberId
	 * @param oldToken
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> getToken(String memberId, String oldToken,String phone) {
		synchronized(this){
			
			if(StringUtil.isEmpty(memberId)){//没有id时为登录时获取token 用手机号
				memberId=phone;
			}
			
			Map<String, Object> doctor =null;// appDao.queryDoctorById(memberId);

			/*if (null == doctor
					|| StringUtil.isEmpty(doctor.get("doctorId").toString())) {
				return Util.resultMap(configCode.code_1002, null);
			}*/

			String token = appDao.queryTokenByMemberId(memberId);//根据id或手机号查询是否已有token信息
			if (!StringUtil.isEmpty(token)) {//如果已有toeken
				Object obj = RedisUtil.getVal(token);
				if (!StringUtil.isEmpty(obj)) {//如果已有toeken则把旧token返回
					JSONObject json = JSONObject.fromObject(obj);
					doctor = new HashMap<String, Object>();
					doctor.put("_token", token);
					doctor.put("_encry_token", json.get("_encry_token"));
					doctor.put("memberId",memberId.equals(phone)?null:json.has("doctorId")?json.get("doctorId"):json.get("srId"));
					return Util.resultMap(configCode.code_1001, doctor);
				}
			}

			//生成新token
			String _token = Util.getUUID();
			String _encry_token = Util.getUUID();
			Integer i = appDoctorDao.addMemberToken(memberId, _token);//token入库

			if (i == 0) {//如果入库失败
				return Util.resultMap(configCode.code_1051, null);
			}
			if (!StringUtil.isEmpty(oldToken)) {//如果有旧token则删除旧token
				RedisUtil.deleteKey(oldToken);
			}
			JSONObject json = new JSONObject();
			json.set("doctorId", memberId);
			json.set("_encry_token", _encry_token);
			RedisUtil.setVal(_token, 60 * 60 * 2, json.toString());//设置token超时时间2个小时
			doctor = new HashMap<String, Object>();
			doctor.put("_token", _token);
			doctor.put("_encry_token", _encry_token);
			doctor.put("memberId",memberId.equals(phone)?null:json.has("doctorId")?json.get("doctorId"):json.get("srId"));
			return Util.resultMap(configCode.code_1001, doctor);
		}
	}

	/**
	 * 查询广告
	 * @param request
	 * @param advertising
	 * @return
	 */
	@Override
	public Map<String, Object> queryAdvertising(Advertising advertising) {
		if(null==advertising.getPort()||0==advertising.getPort()){
			advertising.setPort(1);
		}
		return appDao.queryAdvertising(advertising);
	}

	/**
	 * 根据手机号查询医生信息
	 * @param loginName
	 * @return
	 */
	@Override
	public Integer queryUserByLoginName(String loginName) {
		return appDao.queryUserByLoginName(loginName);
	}

	/**
	 * 根据手机号查询医生信息
	 */
	@Override
	public String queryDoctorByLoginName(String loginName) {
		return appDao.queryDoctorByLoginName(loginName);
	}

	/**
	 * 根据医生id查询医生信息
	 */
	@Override
	public Map<String,Object> queryDoctorById(String userId) {
		return appDao.queryDoctorById(userId);
	}
	
	/**
	 * 查询app最新安装包
	 */
	@Override
	public Map<String, Object> queryAppByLast(Integer port,Integer phoneType,String version) {
		Map<String, Object> map=appDao.queryAppByLast(port,phoneType,version);
		return Util.resultMap(configCode.code_1001,map);
	}
	
	/**
	 * 下载app
	 */
	@Override
	public ResponseEntity<byte[]> downApp(Integer port) {
		//创建实体
		HttpHeaders headers = new HttpHeaders();
        try {
        	//查询app最新物理地址
        	Map<String, Object> map=appDao.queryAppByLastAddr(port);
        	if(null==map||map.size()==0){
        		return null;
        	}
			File file= new File(map.get("riskPath").toString());
			//赋值
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", new String(("上医仁家"+(port==1?"医生端":"助理端")).getBytes("utf-8"), "ISO8859-1"));
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
			                                  headers, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;	
    }

	/**
	 * 查询token
	 */
	@Override
	public Map<String,Object> queryMemberToken(String _token) {
		return appDao.queryMemberToken(_token);
	}

	/**
	 * 添加token
	 */
	@Override
	public Integer addMemberToken(String memberId, String _token) {
		return appDao.addMemberToken(memberId, _token);
	}
	
	/**
	 * 根据用户id查询token信息
	 */
	@Override
	public String queryTokenByMemberId(String memberId) {
		return appDao.queryTokenByMemberId(memberId);
	}
}
