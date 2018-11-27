package cn.syrjia.hospital.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.Member;
import cn.syrjia.hospital.dao.KnowledgeDao;
import cn.syrjia.hospital.entity.Doctor;
import cn.syrjia.hospital.entity.Knowledge;
import cn.syrjia.hospital.entity.Reply;
import cn.syrjia.hospital.entity.UserShare;
import cn.syrjia.hospital.service.KnowledgeService;
import cn.syrjia.hospital.service.ReplyService;
import cn.syrjia.service.MemberService;
import cn.syrjia.service.PushService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Controller 
@RequestMapping("/knowledgeCircle")
public class KnowledgeCircleController {
	
	@Resource(name = "knowledgeCircleService")
	KnowledgeService knowledgeService;
	
	@Resource(name = "replyService")
	ReplyService replyService;
	
	@Resource(name = "pushService")
	PushService pushService;
	
	@Resource(name = "memberService")
	MemberService memberService;
	
	@Resource(name = "knowledgeCircleDao")
	public KnowledgeDao knowledgeDao;

	
	
	/**
	 * 知识圈列表
	 * @param knowledge:文章实体，接受参数
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryKnowledgeList")
	@ResponseBody
	public Map<String,Object> queryKnowledgeList(HttpServletRequest request,String type,Knowledge knowledge,Integer page,Integer row,String memberId){
		if(StringUtil.isEmpty(memberId)){
			//session里面获取memerId
			memberId = GetOpenId.getMemberId(request);
		}
		//判断如果type为空默认是关注列表
		if(type==null){
			type="1";
		}
		//知识圈文章
		List<Map<String, Object>> knowledgelist = new ArrayList<>();
		if(type.equals("1")){
			//获取关注列表
			knowledgelist = knowledgeService.queryKnowledgeListFoll(memberId,knowledge,type,page,row);
		}
		if(type.equals("2")){
			//获取热门列表
			knowledgelist = knowledgeService.queryKnowledgeListHot(memberId,knowledge,type,page,row);
		}
		if(type.equals("3")){
			//获取收藏列表
			knowledgelist = knowledgeService.queryKnowledgeListColl(memberId,knowledge,type,page,row);
		}
		
		Map<String,Object> data = new HashMap<>();
		data.put("knowledgelist", knowledgelist);
		//如果为空，返回服务器异常，请重试
		if(null==knowledgelist){
			return Util.resultMap(configCode.code_1015, data);
		}
		/*for(int i=0;i<knowledgelist.size();i++){
			Map<String, Object> follow = knowledgeService.quertFollow(knowledgelist.get(i).get("doctorId").toString(),memberId);
			Map<String, Object> praise = knowledgeService.quertPraise(knowledgelist.get(i).get("id").toString(), memberId);
			Map<String, Object> collect = knowledgeService.queryknowledgeCollect(memberId, knowledgelist.get(i).get("id").toString());
			if(follow!=null){
				knowledgelist.get(i).put("follow", "2");
				knowledgelist.get(i).put("followId", follow.get("id"));
			}else{
				knowledgelist.get(i).put("follow", "1");
				knowledgelist.get(i).put("followId", "");
			}
			
			if(praise!=null){
				knowledgelist.get(i).put("praise", "2");
				knowledgelist.get(i).put("praiseId", praise.get("id"));
			}else{
				knowledgelist.get(i).put("praise", "1");
				knowledgelist.get(i).put("praiseId", "");
			}
			if(collect!=null){
				knowledgelist.get(i).put("collect", "2");
				knowledgelist.get(i).put("collectId", collect.get("id"));
			}else{
				knowledgelist.get(i).put("collect", "1");
				knowledgelist.get(i).put("collectId", "");
			}
		}	*/
		return Util.resultMap(configCode.code_1001, data);
	}
	
	/**
	 * 知识圈分类列表
	 * @param request
	 * @param knowledge：文章实体
	 * @param type：类型
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryClassList")
	@ResponseBody
	public Map<String,Object> queryClassList(HttpServletRequest request,Knowledge knowledge,String type,Integer page,Integer row,String memberId){
		if(StringUtil.isEmpty(memberId)){
			//session里面获取memerId
			memberId = GetOpenId.getMemberId(request);
		}
		//知识圈分类列表
		List<Map<String, Object>> list = knowledgeService.queryClassList(memberId, knowledge,type,page,row);
		
		//如果为空，返回服务器异常，请重试
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 查看文章详情
	 * @param request
	 * @param id：文章ID
	 * @param memberId：
	 * @return
	 */
	@RequestMapping("/queryCircleDetail")
	@ResponseBody
	public Map<String,Object> queryCircleDetail(HttpServletRequest request,String id,String memberId,Integer type){
		if(StringUtil.isEmpty(memberId)){
			//session里面获取memerId
			memberId = GetOpenId.getMemberId(request);
		}
		//通过文章ID查询文章详情
		//查询返回前台的详情数据
		Map<String,Object> map = knowledgeService.queryCircleDetail(id,memberId);
		//type==1别的接口调用，不等于1才是详情接口
		if(type==null&&map!=null){
			//增加文章阅读数
			knowledgeService.updateReadNum((Integer) map.get("ReadNum")+1,id);
		}
		
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		//添加商品
		if(map.get("goodsIds")!=null&&!map.get("goodsIds").equals("")){
			String[] goodId = map.get("goodsIds").toString().split(",");
			for(int i=0;i<goodId.length;i++){
				if(!goodId[i].equals("")){
					//通过ID查询商品详情
					Map<String,Object> map1 = knowledgeService.queryGoodsById(goodId[i]);
					if(map1!=null&&map1.get("id")!=null&&!"".equals(map1.get("id"))){
						list.add(map1);
					}
				}
			}
			map.put("goods", list);
		}else{
			map.put("goods", list);
		}
		System.out.println(6);
		return Util.resultMap(configCode.code_1001, map);
	}
	
	/**
	 * 通过名称查询知识圈列表
	 * @param request
	 * @param id：文章ID
	 * @param name：查询参数
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryKnowledgeListByName")
	@ResponseBody
	public Map<String,Object> queryKnowledgeListByName(HttpServletRequest request,String memberId,String doctorId,String id,String name,Integer page,Integer row){
		Integer s =Util.queryNowTime();
		//List<Map<String, Object>> list = knowledgeService.queryKnowledgeListByName(id,doctorId,name,page,row);
		//如果memberId为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//通过名称查询知识圈列表
		List<Map<String, Object>> list = knowledgeService.queryKnowledgeSearch(id,memberId, doctorId, name, page, row);
		
		//如果为空，返回服务器异常，请重试
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		System.out.println(Util.queryNowTime()-s);
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 通过名称查询知识圈列表
	 * @param request
	 * @param id：文章ID
	 * @param name：查询参数
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryKnowledgeListBySearch")
	@ResponseBody
	public Map<String,Object> queryKnowledgeListBySearch(HttpServletRequest request,String memberId,String doctorId,String id,String name,Integer page,Integer row){
		//List<Map<String, Object>> list = knowledgeService.queryKnowledgeListByName(id,doctorId,name,page,row);
		//判断memberId,如果为空就从session里面取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//通过名称查询知识圈列表
		List<Map<String, Object>> list = knowledgeService.queryKnowledgeSearchName(id,memberId, doctorId, name, page, row);
		
		//如果为空，返回服务器异常，请重试
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		/*for(Map<String,Object> item:list){
			Map<String, Object> follow = knowledgeService.quertFollow(item.get("doctorId").toString(),memberId);
			Map<String, Object> praise = knowledgeService.quertPraise(item.get("id").toString(), memberId);
			Map<String, Object> collect = knowledgeService.queryknowledgeCollect(memberId, item.get("id").toString());
			if(follow!=null){
				item.put("follow", "2");
				item.put("followId", follow.get("id"));
			}else{
				item.put("follow", "1");
				item.put("followId","");
			}
			if(praise!=null){
				item.put("praise", "2");
				item.put("praiseId", praise.get("id"));
			}else{
				item.put("praise", "1");
				item.put("praise","");
			}
			if(collect!=null){
				item.put("collect", "2");
				item.put("collectId", collect.get("id"));
			}else{
				item.put("collect", "1");
				item.put("collectId","");
			}
		}*/
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 收藏文章
	 * @param request
	 * @param id:文章ID
	 * @param memberId：收藏人ID
	 * @param type：类型
	 * @return
	 */
	@RequestMapping("/knowledgeCollect")
	@ResponseBody
	public Map<String,Object> knowledgeCollect(HttpServletRequest request,String id,String memberId ,Integer type){
		//判断memberId,如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//收藏文章
		Integer i = knowledgeService.knowledgeCollect(memberId,id);
		//更新状态失败，就返回失败
		if(i==0){
			return Util.resultMap(configCode.code_1088, i);
		}
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 文章点赞
	 * @param request
	 * @param memberId：点赞人ID
	 * @param id：文章ID
	 * @param num 返回的参数：1取消点赞 2点赞
	 * @return
	 */
	@RequestMapping("/knowledgePraise")
	@ResponseBody
	public Map<String,Object> knowledgePraise(HttpServletRequest request,String memberId,String id){
		//如果memberId为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//判断当前状态进行更新
		Integer i = knowledgeService.knowledgePraise(id,memberId);
		//如果为零返回失败
		if(i==0){
			return Util.resultMap(configCode.code_1088, i);
		}
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 搜索框匹配
	 * @param request
	 * @param name：查询参数
	 * @return
	 */
	@RequestMapping("/queryKnowledgeTitleAndLabel")
	@ResponseBody
	public Map<String,Object> queryKnowledgeTitleAndLabel(HttpServletRequest request,String name){
		//String openid = SessionUtil.getOpenId(request);
		
		//搜索框匹配
		List<Map<String, Object>> list = knowledgeService.queryKnowledgeTitleAndLabel(name);
		
		//如果为空，返回服务器异常，请重试
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 常用搜索
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryCommonSearch")
	@ResponseBody
	public Map<String,Object> queryCommonSearch(HttpServletRequest request){
		
		//常用搜索
		List<Map<String, Object>> list = knowledgeService.queryCommonSearch();
		
		//如果为空，返回服务器异常，请重试
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 *查询评论详情
	 * @param request
	 * @param id：评论iD
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryReplyDetail")
	@ResponseBody
	public Map<String,Object> queryReplyDetail(HttpServletRequest request,String id,Integer page,Integer row){
		//String openid = SessionUtil.getOpenId(request);
		
		//查询评论详情
		List<Map<String, Object>> list = replyService.queryReplyDetail(id);
		
		//如果为空，返回服务器异常，请重试
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 添加评论(第一次添加)
	 * @param request
	 * @param reply：评论的实体
	 * @return
	 */
	@RequestMapping("/addEvaReply")
	@ResponseBody
	public Map<String,Object> addEvaReply(HttpServletRequest request,Reply reply,String memberId,String knowId){
		
		//如果memberId为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			//session里面获取memerId
			memberId = GetOpenId.getMemberId(request);
		}
		//通过ID查询实体
		Map<String,Object> member = memberService.getMemberByOpenid(memberId);
		//判断禁言
		if(member!=null){
			Integer state = (Integer)member.get("state");
			//state=3说明已被禁言
			if(state==4){
				return Util.resultMap(configCode.code_1125, 0);
			}
		}
		//通过id查询实体
		Map<String, Object> map=knowledgeDao.queryCircleDetail(knowId,memberId);
		//获取系统设置信息判断是否已经设置自动审核通过功能
		Map<String,Object> sysMap = knowledgeService.getSysSet();
		Integer articleCommentAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("articleCommentAutoAudit"))?0:Integer.valueOf(sysMap.get("articleCommentAutoAudit").toString());
		//等于1说明是自动审核通过
		if(articleCommentAutoAudit==1){
			reply.setState(1);
			if(!StringUtil.isEmpty(reply.getReturnId())){
				//通过回复ID查询当前医生
				Doctor doctor=knowledgeService.queryById(Doctor.class,reply.getReturnId());
				//通知当前医生或助理
				if(null!=doctor&&!StringUtil.isEmpty(doctor.getDoctorId())){
					pushService.docmcomment(reply.getReturnId());
				}else{
					pushService.zlcommentreply(reply.getReturnId());
				}
				
			}
		}else{
			//自动审核不通过设置为4，待审核状态
			reply.setState(4);
		}
		reply.setPid("0");
		reply.setType(1);
		reply.setGrankId("0");
		reply.setReplyId(memberId);
		reply.setReturnId((String)map.get("doctorId"));
		//保存当前评论
		Integer i = replyService.addReply(reply);
		//判断是否保存成功
		if(i==0){
			return Util.resultMap(configCode.code_1088, i);
		}
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 关注,取消关注
	 * @param request
	 * @param id：文章ID
	 * @param num：数量
	 * @param memberId：关注人ID
	 * @return
	 */
	@RequestMapping("/updateFollow")
	@ResponseBody
	public Map<String,Object> updateFollow(HttpServletRequest request,String id,String num,String memberId){
		//判断memberId，如果没有就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		Integer i = 0;
		//查询当前用户是否关注
		Integer count = knowledgeService.quertFollow(id, memberId);
		if(count!=null&&count>0){
			//取消关注
			knowledgeService.cancelFollow(id, memberId);
			i=2;
		}else{
			//关注
			knowledgeService.addFollow(id,memberId);
			i=1;
			
		}
		//判断是否操作成功
		if(i==0){
			return Util.resultMap(configCode.code_1088, i);
		}
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 保存分享
	 * @param userShare：分享的实体，接受参数
	 * @return
	 */
	@RequestMapping("/addUserShare")
	@ResponseBody
	public Map<String,Object> addUserShare(UserShare userShare){
		return knowledgeService.addUserShare(userShare);
	}
	
	/**
	 * 查询回复列表
	 * @param request
	 * @param memberId:回复人ID
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryMessageList")
	@ResponseBody
	public Map<String,Object> queryMessageList(HttpServletRequest request,String memberId,Integer page,Integer row){
		if(StringUtil.isEmpty(memberId)){
			//通过session获取memberId
			memberId = GetOpenId.getMemberId(request);
		}
		//如果memberId为空，返回未“获取到登录信息，请先登录”
		if(memberId==null||"".equals(memberId)){
			return Util.resultMap(configCode.code_1018, null);
		} 
		//查询消息列表
		List<Map<String, Object>> list = replyService.queryMessageList(memberId,page,row);
		knowledgeService.markRead(list);
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 获取医生ID和名称
	 * @param request
	 * @param name：搜索条件
	 * @return
	 */
	@RequestMapping("/queryDoctorList")
	@ResponseBody
	public Map<String,Object> queryDoctorList(HttpServletRequest request,String name){
		//获取医生ID和名称
		List<Map<String, Object>> list = knowledgeService.queryDoctorList(name);
		//判断是否为空
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 *查询文章分类
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryKnowledgeType")
	@ResponseBody
	public Map<String,Object> queryKnowledgeType(HttpServletRequest request){
		//查询文章分类
		List<Map<String, Object>> list = knowledgeService.queryKnowledgeType();
		//判断查询内容是否为空
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 商品搜索
	 * @param request
	 * @param name：查询的搜索条件
	 * @return
	 */
	@RequestMapping("/queryGoodByName")
	@ResponseBody
	public Map<String,Object> queryGoodByName(HttpServletRequest request,String name){
		//商品搜索
		List<Map<String, Object>> list = knowledgeService.queryGoodByName(name);
		
		//判断搜索内容是否为空
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 病症标签查询
	 * @param request
	 * @param name：搜索条件
	 * @return
	 */
	@RequestMapping("/queryIllnessByName")
	@ResponseBody
	public Map<String,Object> queryIllnessByName(HttpServletRequest request,String name){
		// 病症标签查询
		List<Map<String, Object>> list = knowledgeService.queryIllnessByName(name);
		//判断查询内容是否为空
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 添加文章
	 * @param knowledge：文章实体
	 * @return
	 */
	@RequestMapping("/addKonwledgeContent")
	@ResponseBody
	public Integer addKonwledgeContent(Knowledge knowledge) {
		return knowledgeService.addKonwledgeContent(knowledge);
	}
	
	/**
	 * 点击人名回复
	 * @param request
	 * @param content：回复的内容
	 * @param returnId：回复人ID
	 * @param memberId：
	 * @param messId：原评论的ID
	 * @return
	 */
	@RequestMapping("/addReplyContent")
	@ResponseBody
	public Map<String,Object> addReplyContent(HttpServletRequest request,String content,String returnId,String memberId,String messId) {
		if(StringUtil.isEmpty(memberId)){
			//通过session获取memerId
			memberId = GetOpenId.getMemberId(request);
		}
		//通过ID查询member
		Member member = knowledgeService.queryById(Member.class, memberId);
		//如果是4被禁言不能回复
		if(member!=null){
			Integer state = member.getState();
			if(state==4){
				return Util.resultMap(configCode.code_1125, 0);
			}
		}
		//获取评论内容
		Map<String,Object> knowEva = knowledgeService.queryEvaContent(messId);
		if(knowEva.get("replyId").toString().equals(memberId)){
			return Util.resultMap(configCode.code_1107, "无法回复自己的评论!");
		}
		Reply reply = new Reply();
		//获取系统设置信息判断是否设置快速审核功能
		Map<String,Object> sysMap = knowledgeService.getSysSet();
		Integer isAutoReplyKnowledgeEva = sysMap==null||StringUtil.isEmpty(sysMap.get("isAutoReplyKnowledgeEva"))?0:Integer.valueOf(sysMap.get("isAutoReplyKnowledgeEva").toString());
		//等于1说明设置快速审核功能
		if(isAutoReplyKnowledgeEva==1){
			if(!StringUtil.isEmpty(reply.getReturnId())){
				//通过医生ID查询医生
				Doctor doctor=knowledgeService.queryById(Doctor.class,reply.getReturnId());
				//给助理或者医生发送通知
				if(null!=doctor&&!StringUtil.isEmpty(doctor.getDoctorId())){
					pushService.docmcomment(reply.getReturnId());
				}else{
					pushService.zlcommentreply(reply.getReturnId());
				}
				
			}
			reply.setState(1);
		}else{
			//没有设置快速审核，4为待审核
			reply.setState(4);
		}
		reply.setContent(content);
		reply.setGrankId(knowEva.get("grankId").toString());
		reply.setKnowId(knowEva.get("knowId").toString());
		reply.setPid(knowEva.get("id").toString());
		reply.setReturnId(knowEva.get("replyId").toString());
		reply.setReplyId(memberId);
		//保存回复内容
		Integer i = knowledgeService.addReplyContent(reply);
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 添加评论
	 * @param request
	 * @param evaluate：评价实体
	 * @param memberId：评论人iD
	 * @return
	 */
	@RequestMapping("/addEvaContent")
	@ResponseBody
	public Map<String,Object> addEvaContent(HttpServletRequest request,Evaluate evaluate,String memberId){
		if(StringUtil.isEmpty(memberId)){
			//通过session获取memberID
			memberId = GetOpenId.getMemberId(request);
		}
		//获取系统设置，判断是否设置了快速审核功能
		Map<String,Object> sysMap = knowledgeService.getSysSet();
		Integer articleCommentAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("articleCommentAutoAudit"))?0:Integer.valueOf(sysMap.get("articleCommentAutoAudit").toString());
		//等于1说明设置了快速审核功能
		if(articleCommentAutoAudit==1){
			evaluate.setState(1);
		}else{
			evaluate.setState(2);
		}
		evaluate.setMemberId(memberId);
		Integer i = knowledgeService.addEvaContent(evaluate);
		//判断保存是否成功
		if(i==0){
			return Util.resultMap(configCode.code_1088, i);
		}
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 医生端(我的文章)
	 * @param request
	 * @param doctorId：医生ID
	 * @param row：一页几行
	 * @param page：第几页
	 * @return
	 */
	@RequestMapping("/queryDoctorArticle")
	@ResponseBody
	public Map<String,Object> queryDoctorArticle(HttpServletRequest request,String srId,String doctorId,Integer row,Integer page,String name){
		//医生ID和助理ID不能同时为空
		if(StringUtil.isEmpty(doctorId)&&StringUtil.isEmpty(srId)){
			return Util.resultMap(configCode.code_1029, null);
		}
		List<Map<String, Object>> knowledgelist = knowledgeService.queryDoctorArticle(srId,name,doctorId,row,page);
		if(null==knowledgelist){
			return Util.resultMap(configCode.code_1015, knowledgelist);
		}
		return Util.resultMap(configCode.code_1001, knowledgelist);
	}
	
	/**
	 * 医生收藏的文章
	 * @param request
	 * @param memberId：当前登录人
	 * @param row：一页几行
	 * @param page：第几页
	 * @return
	 */
	@RequestMapping("/queryDoctorCollectArticle")
	@ResponseBody
	public Map<String,Object> queryDoctorCollectArticle(HttpServletRequest request,String memberId,Integer row,Integer page,String name){
		//判断memberId是否为空，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//查询医生收藏的文章
		List<Map<String, Object>> list = knowledgeService.queryDoctorCollectArticle(name,memberId,row,page);
		//判断查询内容是否为空
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 收藏商品
	 * @param request
	 * @param memberId：收藏人ID
	 * @param row：一页几行
	 * @param page：第几页
	 * @return
	 */
	@RequestMapping("/queryDoctorCollectGoods")
	@ResponseBody
	public Map<String,Object> queryDoctorCollectGoods(HttpServletRequest request,String memberId,Integer row,Integer page,String name){
		//判断memberId是否为空，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//查询收藏商品
		List<Map<String, Object>> list = knowledgeService.queryDoctorCollectGoods(name,memberId,row,page);
		//判断查询内容是否为空
		if(null==list){
			return Util.resultMap(configCode.code_1015, list);
		}
		return Util.resultMap(configCode.code_1001, list);
	}
	
	/**
	 * 标记成已读
	 * @param request
	 * @param memberId：当前登录人ID
	 * @return
	 */
	@RequestMapping("/updateUnreadMess")
	@ResponseBody
	public Map<String,Object> updateUnreadMess(HttpServletRequest request,String memberId){
		//判断memberId是否为空，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//执行保存
		Integer i = knowledgeService.updateUnreadMess(memberId);
		return Util.resultMap(configCode.code_1001, i);
		
	}
	
	/**
	 * 文章详情里的评论列表
	 * @param request
	 * @param id：文章ID
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryEvaList")
	@ResponseBody
	public Map<String,Object> queryEvaList(HttpServletRequest request,String id,Integer page,Integer row,String memberId){
		//判断memberId是否为空，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//文章详情里的评论列表
		List<Map<String,Object>> onelist =  knowledgeService.queryEvaList(id,page,row,memberId);
		//循环添加每条评论的回复
		for(Map<String,Object> item:onelist){
			List<Map<String,Object>> towlist = knowledgeService.queryEvaListContent(item.get("grankId").toString(),memberId);
			item.put("plcontent", towlist);
		}
		//返回前台内容
		return Util.resultMap(configCode.code_1001, onelist);
	}
	
	/**
	 * 查询单条评论
	 * @param request
	 * @param id:当前评论的ID
	 * @param page：第几页
	 * @param row:一页几行
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/queryEva")
	@ResponseBody
	public Map<String,Object> queryEva(HttpServletRequest request,String id,Integer page,Integer row,String memberId){
		//判断memberId如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//memberId = "16673ae4561546a58cf2b1cf106ac30d";
		//查询评论列表
		List<Map<String,Object>> onelist =  knowledgeService.queryEvaList(id,page,row,memberId);
		for(Map<String,Object> item:onelist){
			item.put("plcontent", new ArrayList<Map<String,Object>>());
		}
		return Util.resultMap(configCode.code_1001, onelist);
	}
	
	
	/**
	 * 查询文章操作记录
	 * @param id：文章的ID
	 * @param doctorId：医生的iD
	 * @return
	 */
	@RequestMapping("/queryKonwOperaRecord")
	@ResponseBody
	public Map<String,Object> queryKonwOperaRecord(String id,String doctorId){
		return knowledgeService.queryKonwOperaRecord(id,doctorId);
	}
	
	/**
	 * 查询未读消息数
	 * @param request
	 * @param memberId：当前登录人ID
	 * @return
	 */
	@RequestMapping("/queryUnreadNum")
	@ResponseBody
	public Map<String,Object> queryUnreadNum(HttpServletRequest request,String memberId){
		//判断memberId，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		//执行查询
		return replyService.queryUnreadNum(memberId);
	}
	
	//List<Map<String, Object>> classlist = knowledgeService.queryClassList(memberId, knowledge,type,page,row);
	//Integer unread =  replyService.queryUnreadNum(memberId);
	/**
	 * 轮播图
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryBanner")
	@ResponseBody
	public Map<String,Object> queryBanner(HttpServletRequest request){
		//查询banner列表
		List<Map<String, Object>> bannerlist = knowledgeService.queryBanner();
		//判断查询结果是否为空
		if(bannerlist==null){
			return Util.resultMap(configCode.code_1088, bannerlist);
		}
		return Util.resultMap(configCode.code_1001, bannerlist);
	}
	
	/**
	 * 解析网站链接
	 * @param request
	 * @param src:网站的路径
	 * @return
	 */
	@RequestMapping("/queryArticleByHref")
	@ResponseBody
	public Map<String,Object> queryArticleByHref(HttpServletRequest request,String src){
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			//获取网站的document
			Document doc = Jsoup.connect(src).get();
			//获取title，创建content
			String title=doc.title(),content;
			Elements ele =doc.getElementsByClass("mod-detail-infro wbwr");
			content=ele.text();
			//如果内容为空就通过id为js_content获取
			if(StringUtil.isEmpty(content)){
				if(null!=doc.getElementById("js_content")){
					content=doc.getElementById("js_content").text();
				}
			}
			//如果内容为空就通过class为article获取
			if(StringUtil.isEmpty(content)){
				if(null!=doc.getElementsByClass("article-main")){
					content=doc.getElementsByClass("article-main").text();
				}
			}
			//如果内容为空就通过body获取
			if(StringUtil.isEmpty(content)){
				if(null!=doc.getElementsByTag("body")){
					content=doc.getElementsByTag("body").text();
				}
			}
			map.put("title",title);
			map.put("content",content);
		} catch (IOException e) {}
		//判断是否解析成功
		if(map.size()==0){
			return Util.resultMap(configCode.code_1102,null);
		}else{
			return Util.resultMap(configCode.code_1001, map);
		}
	}
	
	/**
	 * 通过grandId查询评论消息
	 * @param grandId：评论表的grandId字段
	 * @return
	 */
	@RequestMapping("/queryMessageByGrankId")
	@ResponseBody
	public Map<String,Object> queryMessageByGrankId(String grandId){
		return replyService.queryMessageByGrankId(grandId);
	}
	
	/**
	 * 查询可以发布的文章的列表
	 * @param typeId:文章类别的ID
	 * @param type：分类的iD
	 * @param labelId:标签的ID
	 * @param title：标题
	 * @param memberId：当前登录人的ID
	 * @param page：第几页
	 * @param row：一页几行
	 * @return
	 */
	@RequestMapping("/queryAllKnowledge")
	@ResponseBody
	public Map<String, Object> queryAllKnowledge(String typeId,Integer type,String labelId,String title,String memberId,Integer page,Integer row){
		//判断memberId是否为空，如果空的话就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			return  Util.resultMap(configCode.code_1029, null);
		}
		//查询可以发布的文章的列表
		Map<String, Object> map = knowledgeService.queryAllKnowledge(typeId,type,labelId,title, memberId,page, row);
		//判断查询内容是否为空
		if(map==null){
			return  Util.resultMap(configCode.code_1015, null);
		}
		return Util.resultMap(configCode.code_1001, map);
	}
	
	/**
	 * 发布文章里面的分类列表（按照知识圈分类做的，目前需求不是这样的，该接口暂时没用）
	 * @return
	 */
	@RequestMapping("querySendClass")
	@ResponseBody
	public Map<String,Object> querySendClass(String memberId,Integer page,Integer row){
		//判断memberId是否为空，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			return  Util.resultMap(configCode.code_1015, null);
		}
		Map<String, Object> map = new HashMap<String,Object>();
		
		//发布文章里面的分类列表（按照知识圈分类做的，目前需求不是这样的，该接口暂时没用）
		List<Map<String, Object>> classList = knowledgeService.querySendClass(memberId, page, row);
		map.put("classList", classList);
		return Util.resultMap(configCode.code_1001, map);
	}
	
	/**
	 * 发送文章
	 * @param memberId：发布人的ID
	 * @param knowId：文章的ID
	 * @return
	 */
	@RequestMapping("/sendKnowledge")
	@ResponseBody
	public Map<String, Object> sendKnowledge(HttpServletRequest request,String memberId,String knowIds){
		//判断memberID是否为空，如果为空就从session里面获取
		if(StringUtil.isEmpty(memberId)){
			memberId = GetOpenId.getMemberId(request);
		}
		if(memberId==null){
			return Util.resultMap(configCode.code_1088, null);
		}
		/**
		 * 发送文章
		 */
		int i = knowledgeService.sendKnowledge(memberId, knowIds);
		return Util.resultMap(configCode.code_1001, i);
	}
	
	/**
	 * 查询科室，病症，症型
	 * @return
	 */
	@RequestMapping("/queryKnowSearchBasicData")
	@ResponseBody
	public Map<String,Object> queryKnowSearchBasicData(){
		return knowledgeService.queryKnowSearchBasicData();
	}
	
}
