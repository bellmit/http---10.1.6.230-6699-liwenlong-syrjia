package cn.syrjia.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//import cn.hxTravel.entity.Evaluate;
import cn.syrjia.config.configCode;
import cn.syrjia.entity.Knowledge;
import cn.syrjia.entity.KnowledgeReply;
import cn.syrjia.service.KnowledgeService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Controller 
@RequestMapping("/knowledge")
public class KnowledgeController {
	
	@Resource(name = "knowledgeService")
	KnowledgeService knowledgeService;

	/**
	 * 查询知识圈信息
	 * @param request
	 * @param knowledgeId：文章iD
	 * @return
	 */
	@RequestMapping("/queryKnowledgeById")
	@ResponseBody
	public Map<String,Object> queryKnowledgeById(HttpServletRequest request,String knowledgeId){
		//knowledgeId不能为空
		if(StringUtil.isEmpty(knowledgeId)){
			return Util.resultMap(configCode.code_1003,null);
		}
		String memberId = GetOpenId.getMemberId(request);
		//通过Id查询知识圈信息
		Map<String,Object> knowledge=knowledgeService.queryKnowledgeById(knowledgeId,memberId);
		if(null==knowledge){
			return Util.resultMap(configCode.code_1015, knowledge);
		}
		return Util.resultMap(configCode.code_1001, knowledge);
	}
	
	/**
	 * 知识列表
	 * @param knowledge：文章实体
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryKnowledges")
	@ResponseBody
	public Map<String,Object> queryKnowledges(Knowledge knowledge,Integer page,Integer row){
		List<Map<String,Object>> knowledges=knowledgeService.queryKnowledges(knowledge,page,row);
		if(null==knowledges){
			return Util.resultMap(configCode.code_1015, knowledges);
		}
		return Util.resultMap(configCode.code_1001, knowledges);
	}
	
	/**
	 * 知识评价列表
	 * @param knowledge：文章实体
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryKnowledgeReplys")
	@ResponseBody
	public Map<String,Object> queryKnowledgeReplys(KnowledgeReply knowledgeReply,Integer page,Integer row){
		knowledgeReply.setState(2);
		//根据实体查询
		List<KnowledgeReply> knowledges=knowledgeService.query(knowledgeReply);
		//非空判断
		if(null==knowledges){
			return Util.resultMap(configCode.code_1015, knowledges);
		}
		return Util.resultMap(configCode.code_1001, knowledges);
	}
	
	/**
	 * 添加评论
	 * @param knowledge：文章实体
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/addKnowledgeReply")
	@ResponseBody
	public Map<String,Object> addKnowledgeReply(HttpServletRequest request,KnowledgeReply knowledgeReply){
		String memberId = GetOpenId.getMemberId(request);
		//判断memberId不能为空1
		if(StringUtil.isEmpty(memberId)){
			return Util.resultMap(configCode.code_1074,null); 
		}
		knowledgeReply.setMemberId(memberId.toString());
		//执行添加
		return knowledgeService.addKnowledgeReply(knowledgeReply);
	}

}
