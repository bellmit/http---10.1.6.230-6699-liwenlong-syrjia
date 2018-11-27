package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.KnowledgeDao;
import cn.syrjia.entity.Knowledge;
import cn.syrjia.entity.KnowledgeReply;
import cn.syrjia.service.KnowledgeService;

@Service("knowledgeService")
public class KnowledgeServiceImpl  extends BaseServiceImpl implements KnowledgeService {
	
	@Resource(name = "knowledgeDao")
	public KnowledgeDao knowledgeDao;

	

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
	 * 通过ID查询分类详情
	 */
	@Override
	public Map<String, Object> queryCircleDetail(String id) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryCircleDetail(id);
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
	 * 通过名称查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListByName(String name,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		return knowledgeDao.queryKnowledgeListByName(name,page,row);
	}

	/**
	 * 收藏文章
	 */
	@Override
	//@Transactional(rollbackFor=Exception.class)
	public Integer knowledgeCollect(String openid, String id) {
		// TODO Auto-generated method stub
		return knowledgeDao.knowledgeCollect(openid,id);
	}

	/**
	 * 点赞
	 */
	@Override
	public Integer knowledgePraise(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询知识圈信息
	 */
	@Override
	public Map<String, Object> queryKnowledgeById(String knowledgeId,
			Object memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 知识列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledges(Knowledge knowledge,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 添加评论
	 */
	@Override
	public Map<String, Object> addKnowledgeReply(KnowledgeReply knowledgeReply) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
