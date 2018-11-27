package cn.syrjia.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.common.impl.BaseController;
import cn.syrjia.config.configCode;
import cn.syrjia.entity.Keep;
import cn.syrjia.service.KeepService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Controller
@RequestMapping("/keep")
public class KeepController extends BaseController {

	@Resource(name = "keepService")
	KeepService keepService;

	/**
	 * 查询关注列表
	 * @param request
	 * @param keep
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryKeep")
	@ResponseBody
	public Map<String, Object> queryKeep(HttpServletRequest request,Keep keep,Integer page,Integer row){
		String memberId = GetOpenId.getMemberId(request);
		keep.setMemberId(memberId);
		return keepService.queryKeep(keep, page, row);
	}
	
	/**
	 * 关注
	 * @param request
	 * @param keep
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/addKeep")
	@ResponseBody
	public Map<String, Object> addKeep(HttpServletRequest request,Keep keep,String memberId){
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//非空判断
		if(StringUtil.isEmpty(memberId)){
			return Util.resultMap(configCode.code_1074, null);	
		} 
		keep.setMemberId(memberId.toString());
		//执行新增
		return keepService.addKeep(keep);
	}
	
	/**
	 * 取消关注
	 * @param request
	 * @param keep
	 * @return
	 */
	@RequestMapping("/deleteKeep")
	@ResponseBody
	public Map<String, Object> deleteKeep(HttpServletRequest request,Keep keep){
		String memberId = GetOpenId.getMemberId(request);
		keep.setMemberId(memberId);
		//删除
		return keepService.deleteKeep(keep);
	}
	
	/**
	 * 批量取消关注
	 * @param request
	 * @param keep
	 * @return
	 */
	@RequestMapping("/deleteKeeps")
	@ResponseBody
	public Map<String, Object> deleteKeeps(HttpServletRequest request,@RequestParam(value="keeps[]")String[] keeps){
		return keepService.deleteKeeps(keeps);
	}
	
	/**
	 * 查询是否关注
	 * @param request
	 * @param keep
	 * @return
	 */
	@RequestMapping("/hasKeep")
	@ResponseBody
	public Map<String, Object> hasKeep(HttpServletRequest request,Keep keep){
		String memberId = GetOpenId.getMemberId(request);
		keep.setMemberId(memberId);
		return keepService.hasKeep(keep);
	}
}
