package cn.syrjia.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.common.impl.BaseController;
import cn.syrjia.config.configCode;
import cn.syrjia.hospital.entity.UserKeep;
import cn.syrjia.service.CenterCollectService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Controller
@RequestMapping("/centerCollect")
public class CenterCollectController extends BaseController {
	
	@Resource(name = "centerCollectService")
	CenterCollectService centerCollectservice;
	
	
	/**
	 * 查询关注医生
	 * @Description: TODO
	 * @param @param request
	 * @param @param keep
	 * @param @param page
	 * @param @param row
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @date 2018-3-26
	 */
	@RequestMapping("/queryUserKeep")
	@ResponseBody
	public List<Map<String,Object>> queryUserKeep(HttpServletRequest request,
			UserKeep keep, Integer page, Integer row,String memberId) {
		List<Map<String,Object>> doclist = new ArrayList<Map<String,Object>>();
		//非空判断
		if(!StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		if(!StringUtil.isEmpty(memberId)){
			/*Men doc = new Doctor();
			doc.setOpenid(openid);*/
			//关注的医生
			doclist = centerCollectservice.queryKeepDoctor(null, memberId, page, row, null);
		}
		return doclist;
	}
	
	/*
	 * 查询关注的文章
	 */
	@RequestMapping("/queryKeepKonwledge")
	@ResponseBody
	public Map<String,Object> queryKeepKonwledge(HttpServletRequest request,
			UserKeep keep, Integer page, Integer row,String memberId) {
		List<Map<String,Object>> doclist = new ArrayList<Map<String,Object>>();
		//非空判断
		if(!StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		if(!StringUtil.isEmpty(memberId)){
			//我收藏的文章
			doclist = centerCollectservice.queryKeepKonwledge(null, memberId, page, row, null);
		}
		if(doclist!=null && doclist.size()>0){
			return Util.resultMap(configCode.code_1001, doclist);
		}else{
			return Util.resultMap(configCode.code_1005, doclist);
		}
	}
	
	/**
	 * 收藏文章中关注医生
	 * @Description: TODO
	 * @param @param request
	 * @param @param doctorId
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @date 2018-3-27
	 */
	@RequestMapping("/addKeepDoc")
	@ResponseBody
	public Map<String,Object> addKeepDoc(HttpServletRequest request,String doctorId,String memberId) {
		if(!StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//查询关注的医生
		return centerCollectservice.addKeepDoc(memberId, doctorId);
	}

	/**
	 * 查询就诊医生
	 */
	@RequestMapping("/queryDoctorJZ")
	@ResponseBody
	public Map<String,Object> queryDoctorJZ(HttpServletRequest request,String name,String doctorId, Integer page, Integer row,String memberId) {
		//关注的医生
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		 List<Map<String,Object>> doclist = new ArrayList<Map<String,Object>>();
		 //查询就诊医生
		 doclist = centerCollectservice.queryDoctorJZ( name,memberId, page, row, null);
		 if(null==doclist){
				return Util.resultMap(configCode.code_1015, doclist);
			}
			return Util.resultMap(configCode.code_1001, doclist);
	}
	
	/**
	 * 查看关注医生
	 * @param request
	 * @param doctorId
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryDoctorGZ")
	@ResponseBody
	public Map<String,Object> queryDoctorGZ(HttpServletRequest request,String name,String doctorId, Integer page, Integer row,String memberId) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		List<Map<String,Object>> doclist = new ArrayList<Map<String,Object>>();
		//查询关注的医生
		doclist = centerCollectservice.queryDoctorGZ(name, memberId, page, row, null);
		if(null==doclist){
			return Util.resultMap(configCode.code_1015, doclist);
		}
		return Util.resultMap(configCode.code_1001, doclist);
	}
	
	/**
	 * 查询收藏的文章
	 * @param request
	 * @param memberId
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryCollectArticle")
	@ResponseBody
	public Map<String,Object> queryCollectArticle(HttpServletRequest request,String memberId,String name, Integer page, Integer row) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		List<Map<String,Object>> articlelist = new ArrayList<Map<String,Object>>();
		//查询医生的文章
		articlelist = centerCollectservice.queryCollectArticle(name, memberId, page, row);
		if(null==articlelist){
			return Util.resultMap(configCode.code_1015, articlelist);
		}
		return Util.resultMap(configCode.code_1001, articlelist);
	}
	
	/**
	 * 查询收藏的商品
	 * @param request
	 * @param memberId
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryCollectGoods")
	@ResponseBody
	public Map<String,Object> queryCollectGoods(HttpServletRequest request,String memberId,String name, Integer page, Integer row) {
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		List<Map<String,Object>> goodslist = new ArrayList<Map<String,Object>>();
		//查询收藏的商品
		goodslist = centerCollectservice.queryCollectGoods(name, memberId, page, row);
		if(null==goodslist){
			return Util.resultMap(configCode.code_1015, goodslist);
		}
		return Util.resultMap(configCode.code_1001, goodslist);
	}
	
	/**
	 * 根据id查询用户
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryMemberById")
	@ResponseBody
	public Map<String,Object> queryMemberById(HttpServletRequest request) {
		String memberId = GetOpenId.getMemberId(request);
		//根据id查询用户
		Map<String,Object> memberInfo = centerCollectservice.queryMemberById(memberId);
		if(null==memberInfo){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001, memberInfo);
	}
}
