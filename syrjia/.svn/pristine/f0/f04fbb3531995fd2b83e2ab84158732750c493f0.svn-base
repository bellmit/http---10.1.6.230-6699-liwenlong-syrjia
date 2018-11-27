package cn.syrjia.hospital.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vdurmont.emoji.EmojiParser;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.hospital.dao.KnowledgeDao;
import cn.syrjia.hospital.entity.Knowledge;
import cn.syrjia.hospital.entity.Reply;
import cn.syrjia.hospital.entity.UserShare;
import cn.syrjia.hospital.service.KnowledgeService;
import cn.syrjia.service.PushService;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Service("knowledgeCircleService")
public class KnowledgeServiceImpl  extends BaseServiceImpl implements KnowledgeService {
	
	@Resource(name = "knowledgeCircleDao")
	public KnowledgeDao knowledgeDao;
	
	@Resource(name = "pushService")
	public PushService pushService;


	/**
	 * 知识圈列表(患者端)
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeList(String openId,Knowledge knowledge,String type,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryKnowledgeList(openId,knowledge, type,page, row);
	}

	/**
	 * 知识圈分类列表
	 */
	@Override
	public List<Map<String, Object>> queryClassList(String openid,
			Knowledge knowledge, String type, Integer page, Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryClassList(openid,knowledge, type,page, row);
	}

	/**
	 * 查询文章详情
	 */
	@Override
	public Map<String, Object> queryCircleDetail(String id,String memberId) {
		//查询文章详情
		Map<String, Object> map=knowledgeDao.queryCircleDetail(id,memberId);
		String labelNames=null;
		//查询文章的标签
		if(!StringUtil.isEmpty(map.get("labelIds"))){
			labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
		}
		/*Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
		
		map.put("collectNum",info.get("collectNum").toString());
		if(!StringUtil.isEmpty(info.get("labelNames"))){
			map.put("labelNames",info.get("labelNames").toString());
		}else{
			map.put("labelNames",null);
		}
		map.put("replyNum",info.get("replyNum").toString());
		map.put("shareNum",info.get("shareNum").toString());
		map.put("plNum",info.get("plNum").toString());
		map.put("pointNum",info.get("pointNum").toString());
		map.put("followId",info.get("follow").toString());
		map.put("praiseId",info.get("praise").toString());
		map.put("collectId",info.get("collect").toString());
		
		
		if(info.get("follow").toString().equals(0)){
			map.put("follow",1);
		}else{
			map.put("follow",2);
		}
		
		if(info.get("praise").toString().equals(0)){
			map.put("praise",1);
		}else{
			map.put("praise",2);
		}
		
		if(info.get("collect").toString().equals(0)){
			map.put("collect",1);
		}else{
			map.put("collect",2);
		}*/
		//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
		//查询文章评论数
		Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),memberId);
		//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
		//查询文章评论数
		Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),memberId);
		//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
		//查询是否关注
		Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),memberId);
		//查询是否点赞
		Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),memberId);
		//查询是否收藏
		Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),memberId);
		//map.put("collectNum",collectNum);
		map.put("labelNames",labelNames);
		map.put("replyNum",replyNum);
		//map.put("shareNum",shareNum);
		map.put("plNum",plNum);
		//map.put("pointNum",pointNum);
		map.put("followId",follow);
		map.put("praiseId",praiseId);
		map.put("collectId",collect.get("num"));
		map.put("collectTime",collect.get("createTime"));
		//2为已关注，1为未关注
		if(follow==0){
			map.put("follow",1);
		}else{
			map.put("follow",2);
		}
		//2为已点赞，1为未点赞
		if(praiseId==0){
			map.put("praise",1);
		}else{
			map.put("praise",2);
		}
		//2为已收藏，1为未收藏
		if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
			map.put("collect",1);
		}else{
			map.put("collect",2);
		}
		return  map;
	}

	/**
	 * 通过ID查询商品
	 */
	@Override
	public Map<String, Object> queryGoodsById(String id) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryGoodsById(id);
	}

	/**
	 * 通过名字查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListByName(String id,String doctorId,String name,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryKnowledgeListByName(id,doctorId,name,page,row);
	}

	/**
	 * 收藏文章
	 */
	@Override
	//@Transactional(rollbackFor=Exception.class)
	public Integer knowledgeCollect(String openid, String id) {
		// TODO Auto-generated method stub
		if(!StringUtil.isEmpty(openid)){
			Integer count = knowledgeDao.selectKnowledgeCollect(openid,id);
			if(count!=null&&count>0){//取消收藏
				knowledgeDao.deleteKnowledgeCollect(openid,id);
				return 2;
			}else{//已收藏
				knowledgeDao.knowledgeCollect(openid,id);
				return 1;
			}
		}
		return 0;//参数错误
	}

	/**
	 * app查询收藏的文章
	 */
	public Map<String,Object> queryknowledgeCollect(String openid, String id) {
		return knowledgeDao.selectKnowledgeCollectApp(openid,id);
	}
	
	/**
	 * 点赞
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Integer knowledgePraise(String id) {
		if(!StringUtil.isEmpty(id)){;
			String sql = "update t_konwledge set pointNum=pointNum+1 where id=? ";
			knowledgeDao.update(sql,new Object[] {id});
			return 1;
		}else{
			return 0;
		}
	}

	/**
	 * 通过标题和标签查询文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeTitleAndLabel(String name) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryKnowledgeTitleAndLabel(name);
	}

	/**
	 * 通过名称查询搜索列表
	 */
	@Override
	public List<Map<String, Object>> querySearchByName(String name) {
		// TODO Auto-generated method stub
		return knowledgeDao.querySearchByName(name);
	}
	
	/**
	 * 查询搜索记录
	 */
	@Override
	public List<Map<String, Object>> queryCommonSearch() {
		// TODO Auto-generated method stub
		return knowledgeDao.queryCommonSearch();
	}

	/**
	 * 更新阅读数
	 */
	@Override
	public Integer updateReadNum(Integer num, String id) {
		// TODO Auto-generated method stub
		return knowledgeDao.updateReadNum(num,id);
	}

	/**
	 * 查询关注的文章
	 */
	@Override
	public Integer quertFollow(String memberId, String doctorId) {
		// TODO Auto-generated method stub
		return knowledgeDao.quertFollow(memberId,doctorId);
	}

	/**
	 * 取消关注
	 */
	@Override
	public Integer cancelFollow(String id,String memberId) {
		// TODO Auto-generated method stub
		return knowledgeDao.cancelFollow(id,memberId);
	}

	/**
	 * 关注
	 */
	@Override
	public Integer addFollow(String id, String memberId) {
		// TODO Auto-generated method stub
		return knowledgeDao.addFollow(id,memberId);
	}

	/**
	 * 查询医生列表
	 */
	@Override
	public List<Map<String, Object>> queryDoctorList(String name) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryDoctorList(name);
	}

	/**
	 * 查询文章类别
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeType() {
		// TODO Auto-generated method stub
		return knowledgeDao.queryKnowledgeType();
	}

	/**
	 * 通过名称查询商品
	 */
	@Override
	public List<Map<String, Object>> queryGoodByName(String name) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryGoodByName(name);
	}

	/**
	 * 通过名称查询病症
	 */
	@Override
	public List<Map<String, Object>> queryIllnessByName(String name) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryIllnessByName(name);
	}

	/**
	 * 添加文章
	 */
	@Override
	public Integer addKonwledgeContent(Knowledge knowledge) {
		// TODO Auto-generated method stub
		//查询系统设置判断是否设置了快速审核
		Map<String,Object> sysMap = knowledgeDao.getSysSet();
		Integer doctorIssueArticleAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("doctorIssueArticleAutoAudit"))?0:Integer.valueOf(sysMap.get("doctorIssueArticleAutoAudit").toString());
		Integer salesIssueArticleAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("salesIssueArticleAutoAudit"))?0:Integer.valueOf(sysMap.get("salesIssueArticleAutoAudit").toString());
		//等于1说明设置自动审核
		if(!StringUtil.isEmpty(knowledge.getSendId())&&knowledge.getDoctorId().equals(knowledge.getSendId())&&doctorIssueArticleAutoAudit==1){
			knowledge.setState(1);
			knowledge.setSendTime(Util.queryNowTime());
		}else if(!StringUtil.isEmpty(knowledge.getSendId())&&!knowledge.getDoctorId().equals(knowledge.getSendId())&&salesIssueArticleAutoAudit==1){
			knowledge.setState(1);
			knowledge.setSendTime(Util.queryNowTime());
		}else {
			//未设置自动审核
			knowledge.setState(4);
		}
		//执行保存
		return knowledgeDao.addKonwledgeContent(knowledge);
	}

	/**
	 * 查询banner
	 */
	@Override
	public List<Map<String, Object>> queryBanner() {
		// TODO Auto-generated method stub
		return knowledgeDao.queryBanner();
	}

	/**
	 * 添加评论的回复
	 */
	@Override
	public Integer addReplyContent(Reply reply) {
		//获取系统设置信息
		Map<String,Object> sysMap = knowledgeDao.getSysSet();
		Integer articleCommentAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("articleCommentAutoAudit"))?0:Integer.valueOf(sysMap.get("articleCommentAutoAudit").toString());
		if(articleCommentAutoAudit==1){
			reply.setState(1);
		}else{
			reply.setState(4);
		}
		return knowledgeDao.addReplyContent(reply);
	}
	
	/**
	 * 添加评论
	 */
	@Override
	public Integer addEvaContent(Evaluate evaluate) {
		// TODO Auto-generated method stub
		//获取系统设置信息
				Map<String,Object> sysMap = knowledgeDao.getSysSet();
				Integer articleCommentAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("articleCommentAutoAudit"))?0:Integer.valueOf(sysMap.get("articleCommentAutoAudit").toString());
				if(articleCommentAutoAudit==1){
					evaluate.setState(1);
				}else{
					evaluate.setState(2);
				}
		return knowledgeDao.addEvaContent(evaluate);
	}

	/**
	 * 查询医生的文章
	 */
	@Override
	public List<Map<String, Object>> queryDoctorArticle(String srId,String name,String doctorId,Integer row,Integer page) {
		//查询列表基础数据
		List<Map<String, Object>> list=knowledgeDao.queryDoctorArticle(srId,name,doctorId,row,page);
		 if(!StringUtil.isEmpty(srId)){
			 doctorId = srId;
		 }
		 for(Map<String,Object> map:list){
				String labelNames=null;
				//通过标签ID查询标签
				if(!StringUtil.isEmpty(map.get("labelIds"))){
					labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
				}
				/*Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
				
				map.put("collectNum",info.get("collectNum").toString());
				if(!StringUtil.isEmpty(info.get("labelNames"))){
					map.put("labelNames",info.get("labelNames").toString());
				}else{
					map.put("labelNames",null);
				}
				map.put("replyNum",info.get("replyNum").toString());
				map.put("shareNum",info.get("shareNum").toString());
				map.put("plNum",info.get("plNum").toString());
				map.put("pointNum",info.get("pointNum").toString());
				map.put("followId",info.get("follow").toString());
				map.put("praiseId",info.get("praise").toString());
				map.put("collectId",info.get("collect").toString());
				
				
				if(info.get("follow").toString().equals(0)){
					map.put("follow",1);
				}else{
					map.put("follow",2);
				}
				
				if(info.get("praise").toString().equals(0)){
					map.put("praise",1);
				}else{
					map.put("praise",2);
				}
				
				if(info.get("collect").toString().equals(0)){
					map.put("collect",1);
				}else{
					map.put("collect",2);
				}*/
				//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
				//查询评论数
				Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),doctorId);
				//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
				//查询评论数
				Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),doctorId);
				//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
				//查询关注
				Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),doctorId);
				//查询点赞
				Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),doctorId);
				//查询收藏
				Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),doctorId);
				//map.put("collectNum",collectNum);
				map.put("labelNames",labelNames);
				map.put("replyNum",replyNum);
				//map.put("shareNum",shareNum);
				map.put("plNum",plNum);
				//map.put("pointNum",pointNum);
				map.put("followId",follow);
				map.put("praiseId",praiseId);
				map.put("collectId",collect.get("num"));
				map.put("collectTime",collect.get("createTime"));
				//2为已关注，1未关注
				if(follow==0){
					map.put("follow",1);
				}else{
					map.put("follow",2);
				}
				//2为已点赞，1未点赞
				if(praiseId==0){
					map.put("praise",1);
				}else{
					map.put("praise",2);
				}
				//2已收藏，1未收藏
				if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
					map.put("collect",1);
				}else{
					map.put("collect",2);
				}
			}
			return list;
	}

	/**
	 * 查询医生收藏的文章
	 */
	@Override
	public List<Map<String, Object>> queryDoctorCollectArticle(String name,String memberId,
			Integer row, Integer page) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryDoctorCollectArticle(name,memberId,row,page);
	}

	/**
	 * 查询医生收藏的商品
	 */
	@Override
	public List<Map<String, Object>> queryDoctorCollectGoods(String name,
			String memberId, Integer row, Integer page) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryDoctorCollectGoods(name,memberId,row,page);
	}

	/**
	 * 置顶和取消置顶
	 */
	@Override
	public Map<String,Object> updateKnowTop(String id,String memberId,Integer top) {
			Integer i=knowledgeDao.updateKnowUnTop(memberId);
			i=knowledgeDao.updateKnowTop(id,top);
			if(i==0){
				return Util.resultMap(configCode.code_1088,i);
			}else{
				return Util.resultMap(configCode.code_1001,i);
			}
	}

	/**
	 * 删除文章
	 */
	@Override
	public Map<String,Object> deleteKnowState(String id,String memberId) {
		// TODO Auto-generated method stub
		Integer i=knowledgeDao.deleteKnowState(id,memberId);
		if(i==0){
			return Util.resultMap(configCode.code_1088,null);  
		}else{
			return Util.resultMap(configCode.code_1001,i);  
		}
	}

	/**
	 * 添加文章
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String,Object> addKnowArticle(Knowledge knowledge) {
		String uuId = Util.getUUID();
		knowledge.setId(uuId);
		//标题和内容为必填
		if(StringUtil.isEmpty(knowledge.getTitle())||StringUtil.isEmpty(knowledge.getContent())){
			return Util.resultMap(configCode.code_1029,null);  
		}
		//判断是否已经设置快速审核
		Map<String,Object> sysMap = knowledgeDao.getSysSet();
		Integer doctorIssueArticleAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("doctorIssueArticleAutoAudit"))?0:Integer.valueOf(sysMap.get("doctorIssueArticleAutoAudit").toString());
		Integer salesIssueArticleAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("salesIssueArticleAutoAudit"))?0:Integer.valueOf(sysMap.get("salesIssueArticleAutoAudit").toString());
		//等于1说明设置了快速审核
		if(!StringUtil.isEmpty(knowledge.getSendId())&&knowledge.getDoctorId().equals(knowledge.getSendId())&&doctorIssueArticleAutoAudit==1){
			knowledge.setState(1);
			knowledge.setSendTime(Util.queryNowTime());
		}else if(!StringUtil.isEmpty(knowledge.getSendId())&&!knowledge.getDoctorId().equals(knowledge.getSendId())&&salesIssueArticleAutoAudit==1){
			knowledge.setState(1);
			knowledge.setSendTime(Util.queryNowTime());
		}else {
			//未设置快速审核
			knowledge.setState(4);
		}
		//去掉标签符号
		knowledge.setContent(EmojiParser.removeAllEmojis(knowledge.getContent()));
		knowledge.setTitle(EmojiParser.removeAllEmojis(knowledge.getTitle()));
		Integer i = knowledgeDao.addKnowLabel(knowledge);
		//保存文章，并返回前台是否设置了快速审核
		if(i>0){
			i= knowledgeDao.addKnowArticle(knowledge);
			Map<String,Object> m=new HashMap<String, Object>();
			m.put("result",i);
			m.put("doctorIssueArticleAutoAudit",doctorIssueArticleAutoAudit);
			m.put("salesIssueArticleAutoAudit",salesIssueArticleAutoAudit);
			return Util.resultMap(configCode.code_1001,m);   
		}else{
			return Util.resultMap(configCode.code_1014,i); 
		}
	}

	/**
	 * 标记成已读
	 */
	@Override
	public Integer updateUnreadMess(String memberId) {
		// TODO Auto-generated method stub
		return knowledgeDao.updateUnreadMess(memberId);
	}

	/**
	 * 查询评论内容
	 */
	@Override
	public Map<String, Object> queryEvaContent(String messId) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryEvaContent(messId);
	}

	/**
	 * 点赞
	 */
	@Override
	public Integer addPraise(String id, String memberId) {
		// TODO Auto-generated method stub
		return knowledgeDao.addPraise(id,memberId);
	}

	/**
	 * 查询评论列表
	 */
	@Override
	public List<Map<String, Object>> queryEvaList(String id,Integer page, Integer row,String memberId) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryEvaList(id,page,row,memberId);
	}

	/**
	 * 查询评论回复内容
	 */
	@Override
	public List<Map<String, Object>> queryEvaListContent(String knowId,String memberId) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryEvaListContent(knowId,memberId);
	}
	
	/**
	 * 查询文章操作记录
	 */
	@Override
	public Map<String, Object> queryKonwOperaRecord(String id,String doctorId) {
		if(StringUtil.isEmpty(id)||StringUtil.isEmpty(doctorId)){
			return Util.resultMap(configCode.code_1029, null);
		}else{
			//查询操作记录
			List<Map<String,Object>> list = knowledgeDao.queryKonwOperaRecord(id,doctorId);
			if(list == null){
				return Util.resultMap(configCode.code_1011, null);
			}else{
				return Util.resultMap(configCode.code_1001, list);
			}
		}
	}

	/**
	 * 查询文章搜索列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeSearch(String id,String memberId,
			String doctorId, String name, Integer page, Integer row) {
		Integer s =Util.queryNowTime();
		//通过名称查询知识圈列表
		List<Map<String, Object>> list=knowledgeDao.queryKnowledgeSearch(id,memberId,doctorId,name,page,row);
		/*for(Map<String,Object> map:list){
			Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),memberId);
			Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),memberId);
			Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),memberId);
			Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),memberId);
			Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),memberId);
			map.put("replyNum",replyNum);
			map.put("plNum",plNum);
			map.put("followId",follow);
			map.put("praiseId",praiseId);
			map.put("collectId",collect.get("num"));
			map.put("collectTime",collect.get("createTime"));
			if(follow==0){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			if(praiseId==0){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}
		}*/
		return list;
	}
	
	/**
	 * 通过名称搜索文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeSearchName(String id,String memberId,
			String doctorId, String name, Integer page, Integer row) {
		//文章列表
		List<Map<String, Object>> list=knowledgeDao.queryKnowledgeSearch(id,memberId,doctorId,name,page,row);
		for(Map<String,Object> map:list){
			/*String labelNames=null;
			if(!StringUtil.isEmpty(map.get("labelIds"))){
				labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
			}
			Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
			
			map.put("collectNum",info.get("collectNum").toString());
			if(!StringUtil.isEmpty(info.get("labelNames"))){
				map.put("labelNames",info.get("labelNames").toString());
			}else{
				map.put("labelNames",null);
			}
			map.put("replyNum",info.get("replyNum").toString());
			map.put("shareNum",info.get("shareNum").toString());
			map.put("plNum",info.get("plNum").toString());
			map.put("pointNum",info.get("pointNum").toString());
			map.put("followId",info.get("follow").toString());
			map.put("praiseId",info.get("praise").toString());
			map.put("collectId",info.get("collect").toString());
			
			
			if(info.get("follow").toString().equals(0)){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			
			if(info.get("praise").toString().equals(0)){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			
			if(info.get("collect").toString().equals(0)){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}*/
			//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
			//评论数
			Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),memberId);
			//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
			//评论数
			Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),memberId);
			//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
			//是否关注
			Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),memberId);
			Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),memberId);
			Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),memberId);
			//map.put("collectNum",collectNum);
			//map.put("labelNames",labelNames);
			map.put("replyNum",replyNum);
			//map.put("shareNum",shareNum);
			map.put("plNum",plNum);
			//map.put("pointNum",pointNum);
			map.put("followId",follow);
			map.put("praiseId",praiseId);
			map.put("collectId",collect.get("num"));
			map.put("collectTime",collect.get("createTime"));
			if(follow==0){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			if(praiseId==0){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}
		}
		return list;
	}

	/**
	 * 点赞
	 */
	@Override
	public Integer knowledgePraise(String id, String memberId) {
		// TODO Auto-generated method stub
		Integer count = knowledgeDao.quertPraise(id, memberId);
		if(count!=null&&count>0){
			//取消点赞
			knowledgeDao.cancelPraise(id,memberId);
			return 2;
			
		}else{
			//点赞
			knowledgeDao.addPraise(id,memberId);
			return 1;
		}
	}

	/**
	 * 查询文章详情
	 */
	@Override
	public Map<String, Object> queryKnowArticle(String id, String doctorId) {
		// TODO Auto-generated method stub
		//id和doctorId不能为空
		if(StringUtil.isEmpty(id)||StringUtil.isEmpty(doctorId)){
			return Util.resultMap(configCode.code_1029, null);
		}else{
			//查询文章
			Map<String,Object> knowledge = knowledgeDao.queryKnowArticle(id,doctorId);
			if(knowledge == null){
				return Util.resultMap(configCode.code_1011, null);
			}else{
				//如果文章不为空，增加商品
				if(!StringUtil.isEmpty(knowledge.get("goodsIds"))){
					List<Map<String, Object>> list = knowledgeDao.queryGoodsByIds(knowledge.get("goodsIds").toString());
					knowledge.put("goodslist", list);
				}
				return Util.resultMap(configCode.code_1001, knowledge);
			}
		}
	}

	/**
	 * 编辑文章
	 */
	@Override
	public Map<String, Object> editKnowArticle(Knowledge knowledge) {
		// TODO Auto-generated method stub
		//判断id和doctorId不能为空
		if(StringUtil.isEmpty(knowledge.getId())||StringUtil.isEmpty(knowledge.getDoctorId())){
			return Util.resultMap(configCode.code_1029, null);
		}else{
			//查询系统设置判断是否设置了自动审核
			Map<String,Object> sysMap = knowledgeDao.getSysSet();
			Integer doctorIssueArticleAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("doctorIssueArticleAutoAudit"))?0:Integer.valueOf(sysMap.get("doctorIssueArticleAutoAudit").toString());
			Integer salesIssueArticleAutoAudit = sysMap==null||StringUtil.isEmpty(sysMap.get("salesIssueArticleAutoAudit"))?0:Integer.valueOf(sysMap.get("salesIssueArticleAutoAudit").toString());
			//等于1是设置了自动审核
			if(!StringUtil.isEmpty(knowledge.getSendId())&&knowledge.getDoctorId().equals(knowledge.getSendId())&&doctorIssueArticleAutoAudit==1){
				knowledge.setState(1);
			}else if(!StringUtil.isEmpty(knowledge.getSendId())&&!knowledge.getDoctorId().equals(knowledge.getSendId())&&salesIssueArticleAutoAudit==1){
				knowledge.setState(1);
			}else {
				//没有设置自动审核
				knowledge.setState(4);
			}
			//去表情
			knowledge.setContent(EmojiParser.removeAllEmojis(knowledge.getContent()));
			knowledge.setTitle(EmojiParser.removeAllEmojis(knowledge.getTitle()));
			//先删除标签
			Integer i = knowledgeDao.deleteKnowLabel(knowledge);
			if(i>0){
				//添加标签
				i = knowledgeDao.addKnowLabel(knowledge);
				if(i>0){
					//编辑文章
					i = knowledgeDao.editKnowArticle(knowledge);
					if(i>0){
						//返回前台数据，包括是否设置了快速审核
						Map<String,Object> m=new HashMap<String, Object>();
						m.put("result",i);
						m.put("doctorIssueArticleAutoAudit",doctorIssueArticleAutoAudit);
						m.put("salesIssueArticleAutoAudit",salesIssueArticleAutoAudit);
						return Util.resultMap(configCode.code_1001, m);
					}else{
						return Util.resultMap(configCode.code_1014, null);
					}
				}else{
					return Util.resultMap(configCode.code_1014, null);
				}
			}else{
				return Util.resultMap(configCode.code_1014, null);
			}
		}
		
		
	}

	/**
	 * 查询商品列表
	 */
	@Override
	public Map<String, Object> queryGoods(String name) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = knowledgeDao.queryGoods(name);
		if(list == null){
			return Util.resultMap(configCode.code_1011, null);
		}else{
			return Util.resultMap(configCode.code_1001, list);
		}
	}

	/**
	 * 查询关注的文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListKeep(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryKnowledgeListKeep(memberId,page,row);
		
	}

	/**
	 * 查询关注的文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListFoll(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row) {
		//查询关注的列表
		List<Map<String, Object>> list=knowledgeDao.queryKnowledgeListFoll(memberId,page,row);
		for(Map<String,Object> map:list){
			String labelNames=null;
			//给查询每列数据的标签名
			if(!StringUtil.isEmpty(map.get("labelIds"))){
				labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
			}
			/*Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
			
			map.put("collectNum",info.get("collectNum").toString());
			if(!StringUtil.isEmpty(info.get("labelNames"))){
				map.put("labelNames",info.get("labelNames").toString());
			}else{
				map.put("labelNames",null);
			}
			map.put("replyNum",info.get("replyNum").toString());
			map.put("shareNum",info.get("shareNum").toString());
			map.put("plNum",info.get("plNum").toString());
			map.put("pointNum",info.get("pointNum").toString());
			map.put("followId",info.get("follow").toString());
			map.put("praiseId",info.get("praise").toString());
			map.put("collectId",info.get("collect").toString());
			
			
			if(info.get("follow").toString().equals(0)){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			
			if(info.get("praise").toString().equals(0)){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			
			if(info.get("collect").toString().equals(0)){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}*/
			//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
			//查询每列数据的评论数
			Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),memberId);
			//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
			//查询每列数据的评论数
			Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),memberId);
			//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
			//查询是否关注
			Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),memberId);
			//查询是否点赞
			Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),memberId);
			//查询是否收藏
			Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),memberId);
			//map.put("collectNum",collectNum);
			map.put("labelNames",labelNames);
			map.put("replyNum",replyNum);
			//map.put("shareNum",shareNum);
			map.put("plNum",plNum);
			//map.put("pointNum",pointNum);
			map.put("followId",follow);
			map.put("praiseId",praiseId);
			map.put("collectId",collect.get("num"));
			map.put("collectTime",collect.get("createTime"));
			//2为已关注，1为未关注
			if(follow==0){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			//2为已点赞，1为未点赞
			if(praiseId==0){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			//2为已收藏，1为未收藏
			if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}
		}
		return list;
	}

	/**
	 *查询热门文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListHot(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row) {
		//查询热门列表
		List<Map<String, Object>> list=knowledgeDao.queryKnowledgeListHot(memberId,page,row);
		for(Map<String,Object> map:list){
			String labelNames=null;
			//查询每列数据的标签
			if(!StringUtil.isEmpty(map.get("labelIds"))){
				labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
			}
			/*Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
			
			map.put("collectNum",info.get("collectNum").toString());
			if(!StringUtil.isEmpty(info.get("labelNames"))){
				map.put("labelNames",info.get("labelNames").toString());
			}else{
				map.put("labelNames",null);
			}
			map.put("replyNum",info.get("replyNum").toString());
			map.put("shareNum",info.get("shareNum").toString());
			map.put("plNum",info.get("plNum").toString());
			map.put("pointNum",info.get("pointNum").toString());
			map.put("followId",info.get("follow").toString());
			map.put("praiseId",info.get("praise").toString());
			map.put("collectId",info.get("collect").toString());
			
			
			if(info.get("follow").toString().equals(0)){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			
			if(info.get("praise").toString().equals(0)){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			
			if(info.get("collect").toString().equals(0)){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}*/
			//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
			//查询每列数据的评论数
			Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),memberId);
			//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
			//查询每列数据的评论数
			Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),memberId);
			//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
			//查询是否关注
			Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),memberId);
			//查询是否点赞
			Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),memberId);
			//查询是否收藏
			Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),memberId);
			//map.put("collectNum",collectNum);
			map.put("labelNames",labelNames);
			map.put("replyNum",replyNum);
			//map.put("shareNum",shareNum);
			map.put("plNum",plNum);
			//map.put("pointNum",pointNum);
			map.put("followId",follow);
			map.put("praiseId",praiseId);
			map.put("collectId",collect.get("num"));
			map.put("collectTime",collect.get("createTime"));
			//2为已关注，1为未关注
			if(follow==0){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			//2为已点赞，1为未点赞
			if(praiseId==0){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			//2为已收藏，1为未收藏
			if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}
		}
		return list;
	}

	/**
	 * 获取收藏列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListColl(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row) {
		//查询收藏列表
		List<Map<String, Object>> list=knowledgeDao.queryKnowledgeListColl(memberId,page,row);
		for(Map<String,Object> map:list){
			String labelNames=null;
			//为每列数据添加标签
			if(!StringUtil.isEmpty(map.get("labelIds"))){
				labelNames=knowledgeDao.queryLabelNames(map.get("labelIds").toString());
			}
			/*Map<String,Object> info=knowledgeDao.queryKnowledgeInfo(map.get("id").toString(),StringUtil.isEmpty(map.get("labelIds"))?"":map.get("labelIds").toString(), memberId);
			
			map.put("collectNum",info.get("collectNum").toString());
			if(!StringUtil.isEmpty(info.get("labelNames"))){
				map.put("labelNames",info.get("labelNames").toString());
			}else{
				map.put("labelNames",null);
			}
			map.put("replyNum",info.get("replyNum").toString());
			map.put("shareNum",info.get("shareNum").toString());
			map.put("plNum",info.get("plNum").toString());
			map.put("pointNum",info.get("pointNum").toString());
			map.put("followId",info.get("follow").toString());
			map.put("praiseId",info.get("praise").toString());
			map.put("collectId",info.get("collect").toString());
			
			
			if(info.get("follow").toString().equals(0)){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			
			if(info.get("praise").toString().equals(0)){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			
			if(info.get("collect").toString().equals(0)){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}*/
			//Integer collectNum=knowledgeDao.queryCollectNum(map.get("id").toString());
			//查询每列数据的评论数
			Integer replyNum=knowledgeDao.queryReplyNum(map.get("id").toString(),memberId);
			//Integer shareNum=knowledgeDao.queryShareNum(map.get("id").toString());
			//查询每列数据的评论数
			Integer plNum=knowledgeDao.queryPlNum(map.get("id").toString(),memberId);
			//Integer pointNum=knowledgeDao.queryPointNum(map.get("id").toString());
			//查询是否关注
			Integer follow=knowledgeDao.queryFollow(map.get("doctorId").toString(),memberId);
			//查询是否点赞
			Integer praiseId=knowledgeDao.querypPraiseId(map.get("id").toString(),memberId);
			//查询是否收藏
			Map<String,Object> collect=knowledgeDao.querypCollectId(map.get("id").toString(),memberId);
			//map.put("collectNum",collectNum);
			map.put("labelNames",labelNames);
			map.put("replyNum",replyNum);
			//map.put("shareNum",shareNum);
			map.put("plNum",plNum);
			//map.put("pointNum",pointNum);
			map.put("followId",follow);
			map.put("praiseId",praiseId);
			map.put("collectId",collect.get("num"));
			map.put("collectTime",collect.get("createTime"));
			//2为已关注，1为未关注
			if(follow==0){
				map.put("follow",1);
			}else{
				map.put("follow",2);
			}
			//2为已点赞，1为未点赞
			if(praiseId==0){
				map.put("praise",1);
			}else{
				map.put("praise",2);
			}
			//2为已收藏，1为未收藏
			if(collect.size()==0||StringUtil.isEmpty(collect.get("num"))||Integer.parseInt(collect.get("num").toString())==0){
				map.put("collect",1);
			}else{
				map.put("collect",2);
			}
		}
		return list;
	}


	/**
	 * 查询所有的文章
	 */
	@Override
	public Map<String, Object> queryAllKnowledge(String typeId,Integer type,String labelId,String title,String memberId,Integer page, Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryAllKnowledge(typeId,type,labelId,title,memberId,page, row);
	}

	/**
	 * 发送文章
	 */
	@Override
	public Integer sendKnowledge( String memberId,String knowIds) {
		String[] knowId = knowIds.split(",");
		String ids = "";
		//拼接文章字符串
		if(knowId!=null&&knowId.length>0){
			for (int i = 0; i < knowId.length; i++) {
				ids+="'"+knowId[i].trim()+"',";
			}
		}
		ids = ids.substring(0, ids.length()-1);
		return knowledgeDao.sendKnowledge(memberId, ids);
	}
	
	/**
	 * 查询类别
	 */
	@Override
	public List<Map<String, Object>> querySendClass(String memberId, Integer page,
			Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.querySendClass(memberId, page, row);
	}
	
	/**
	 * 查询标签基础数据
	 */
	@Override
	public Map<String, Object> queryKnowSearchBasicData() {
		//查询基础数据
		List<Map<String,Object>> list = knowledgeDao.queryKnowSearchBasicData();
		//查询病症
		List<Map<String, Object>> illnessClasses = knowledgeDao.illnessClasses();
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> departList = new LinkedList<Map<String,Object>>();
		List<Map<String,Object>> knowClassList = new LinkedList<Map<String,Object>>();
		List<Map<String,Object>> diagnosticList = new LinkedList<Map<String,Object>>();
		List<Map<String,Object>> knowTypeList = new LinkedList<Map<String,Object>>();
		String _sign = "";
		String departId = "";
		//判断查询出来的结束是否为空
		if(list==null){
			return Util.resultMap(configCode.code_1011, null);
		}else{
			//循环拼接数据
			for (int i = list.size() - 1; i >= 0; i--) {
				_sign = (String) list.get(i).get("_sign");
				//判断是否为科室
				if("depart".equals(_sign)){
					departId = (String) list.get(i).get("id");
					List<Map<String, Object>> illLabelList = new LinkedList<Map<String,Object>>();
					for (int j = 0; j < illnessClasses.size(); j++) {
						if(departId!=null&&departId.equals(illnessClasses.get(j).get("departId"))){
							Map<String,Object> illLabelMap = new HashMap<String, Object>();
							illLabelMap.put("id", illnessClasses.get(j).get("illClassId"));
							illLabelMap.put("NAME", illnessClasses.get(j).get("illClassName"));
							illLabelList.add(illLabelMap);
						}
					}
					//添加病症
					if(illLabelList!=null&&illLabelList.size()>0){
						list.get(i).put("illLabelList", illLabelList);
						departList.add(list.get(i));
					}
				}else if("knowClass".equals(_sign)){
					//文章类别添加
					knowClassList.add(list.get(i));
				}else if("diagnostic".equals(_sign)){
					//证型添加
					diagnosticList.add(list.get(i));
				}else if("knowType".equals(_sign)){
					//文章分类添加
					knowTypeList.add(list.get(i));
				}
			}
		}
		map.put("departList", departList);
		map.put("knowClassList", knowClassList);
		map.put("diagnosticList", diagnosticList);
		map.put("knowTypeList", knowTypeList);
		return Util.resultMap(configCode.code_1001, map);
	}

	/**
	 * 标记为已读数据
	 */
	@Override
	public Integer markRead(List<Map<String,Object>> list) {
		// TODO Auto-generated method stub
		String replyIds = "";
		for (int i = 0; i < list.size(); i++) {
			replyIds += "'"+list.get(i).get("id")+"',";
		}
		//截取字符串
		if(replyIds.length()>0){
			replyIds = replyIds.substring(0, replyIds.length()-1);
		}
		return knowledgeDao.markRead(replyIds);
	}

	/**
	 * 保存分享
	 */
	@Override
	public Map<String, Object> addUserShare(UserShare userShare) {
		if(!StringUtil.isEmpty(userShare.getGoodsId())){
			userShare.setCreateTime(Util.queryNowTime());
			userShare.setType(2);
			userShare.setMemberId("1");
			//保存分享
			knowledgeDao.addEntityUUID(userShare);
		}
		return Util.resultMap(configCode.code_1001, 1);
	}
}
