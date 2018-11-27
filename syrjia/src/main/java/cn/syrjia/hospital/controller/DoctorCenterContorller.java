package cn.syrjia.hospital.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.hospital.entity.Knowledge;
import cn.syrjia.hospital.service.DoctorService;
import cn.syrjia.hospital.service.KnowledgeService;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Controller
@RequestMapping("doctorCenter")
public class DoctorCenterContorller {

	@Resource(name = "knowledgeCircleService")
	KnowledgeService knowledgeService;
	
	@Resource(name = "doctorService")
	DoctorService doctorService;
	
	/**
	 * 查询知识圈列表
	 * @param request
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	@RequestMapping("/queryKnowledgeList")
	@ResponseBody
	public Map<String,Object> queryKnowledgeList(HttpServletRequest request,Knowledge knowledge,String memberId,String type,Integer page,Integer row){
		if(StringUtil.isEmpty(memberId)){
			memberId=request.getParameter("memberId").toString();
		}
		type="3";
		//查询文章列表
		List<Map<String, Object>> knowledgelist = knowledgeService.queryKnowledgeList(memberId,knowledge,type,page,row);
		
		Map<String,Object> data = new HashMap<>();
		data.put("knowledgelist", knowledgelist);
		if(null==knowledgelist){
			return Util.resultMap(configCode.code_1015, data);
		}
		return Util.resultMap(configCode.code_1001, data);
	}
	
	/**
	 * 置顶
	 * @param request
	 * @param id
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/updateKnowTop")
	@ResponseBody
	public Map<String,Object> updateKnowTop(HttpServletRequest request,String id,String memberId,Integer top){
		if(StringUtil.isEmpty(memberId)){
			memberId=request.getParameter("memberId").toString();
		}
		//更新置顶
		return knowledgeService.updateKnowTop(id,memberId,top);
		 
	}
	
	/**
	 * 删除文章
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteKnowState")
	@ResponseBody
	public Map<String,Object> deleteKnowState(HttpServletRequest request,String id,String memberId){
		if(StringUtil.isEmpty(memberId)){
			memberId=request.getParameter("memberId").toString();
		}
		return knowledgeService.deleteKnowState(id,memberId);
	}
	
	/**
	 * 移动端添加文章
	 * @param request
	 * @param knowledge
	 * @return
	 */
	@RequestMapping("/addKnowArticle")
	@ResponseBody
	public Map<String,Object> addKnowArticle(HttpServletRequest request,Knowledge knowledge){
		
		return knowledgeService.addKnowArticle(knowledge);
	}
	
	/**
	 * 获取文章详情
	 * @param request
	 * @param doctorId
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryKnowArticle")
	@ResponseBody
	public Map<String,Object> queryKnowArticle(HttpServletRequest request,String id,String doctorId){
		return knowledgeService.queryKnowArticle(id,doctorId);
	}
	
	/**
	 * 编辑
	 * @param request
	 * @param knowledge
	 * @return
	 */
	@RequestMapping("/editKnowArticle")
	@ResponseBody
	public Map<String,Object> editKnowArticle(HttpServletRequest request,Knowledge knowledge){
		return knowledgeService.editKnowArticle(knowledge);
	}
	
	/**
	 * 查询商品
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/queryGoods")
	@ResponseBody
	public Map<String,Object> queryGoods(HttpServletRequest request,String name){
		return knowledgeService.queryGoods(name);
	}
}
