package cn.syrjia.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Advertising;
import cn.syrjia.entity.LoginLog;
import cn.syrjia.service.AppService;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Controller
@RequestMapping("/app")
public class AppController {

	@Resource(name = "appService")
	AppService appService;

	/**
	 * 查询广告
	 * @param request
	 * @param advertising
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAdvertising")
	public Map<String, Object> queryAdvertising(HttpServletRequest request,
			Advertising advertising) {
		Map<String, Object> map = appService.queryAdvertising(advertising);
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 获取token
	 * @param request
	 * @param phone
	 * @param memberId
	 * @param oldToken
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getToken")
	public Map<String, Object> getToken(HttpServletRequest request,String phone,
			String memberId, String oldToken) {
		return appService.getToken(memberId, oldToken,phone);
	}
	
	/**
	 * 查询app是否有更新
	 * @param port
	 * @return
	 */
	@RequestMapping("queryAppByLast")
	@ResponseBody
	public Map<String,Object> queryAppByLast(Integer port,Integer phoneType,String version){
	  return appService.queryAppByLast(port,phoneType,version);
	}
	
	/**
	 * 下载app
	 * @param port
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("downApp")
	@ResponseBody
	public ResponseEntity<byte[]>  downApp(Integer port) throws IOException{
		return appService.downApp(port);
	}
	
	/**
	 * 添加日志
	 * @param request
	 * @param doctorId
	 * @param loginLog
	 * @return
	 */
	@RequestMapping(value = "addUserLoginLog")
	@ResponseBody
	public Map<String, Object> addUserLoginLog(HttpServletRequest request,String doctorId,LoginLog loginLog) {
		//为空判断
		if(StringUtil.isEmpty(doctorId)||StringUtil.isEmpty(loginLog.getToken())){
			return Util.resultMap(configCode.code_1029, null);
		}
		//赋值
		loginLog.setUserId(doctorId);
		loginLog.setCreateTime(Util.queryNowTime());
		//执行新增
		Object obj = appService.addEntityUUID(loginLog);
		if(StringUtil.isEmpty(obj)){
			return Util.resultMap(configCode.code_1014, null);
		}
		return Util.resultMap(configCode.code_1001, obj);
	}
	
	/**
	 * 添加电话版本
	 * @param content
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "insertPhoneVersion")
	@ResponseBody
	public Map<String,Object> insertPhoneVersion(String content,String id){
		appService.insertPhoneVersion(content, id);
		return Util.resultMap(configCode.code_1001, 1);
	}
}
