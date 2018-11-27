package cn.syrjia.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Knowledge;
import cn.syrjia.entity.KnowledgeReply;

public interface KnowledgeService extends BaseServiceInterface{

	/**
	 * 查询文章列表
	 * @param openId
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> queryKnowledgeList(String openId,Knowledge knowledge,String type, Integer page,Integer row);

	/**
	 * 查询分类列表
	 * @param openid
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryClassList(String openid,
			Knowledge knowledge, String type, Integer page, Integer row);

	/**
	 * 通过ID查询文章详情
	 * @param id
	 * @return
	 */
	Map<String, Object> queryCircleDetail(String id);

	/**
	 * 通过ID查询商品
	 * @param id
	 * @return
	 */
	Map<String, Object> queryGoodsById(String id);

	/**
	 * 通过名称查询文章列表
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListByName(String name,
			Integer page, Integer row);

	/**
	 * 收藏文章
	 * @param openid
	 * @param id
	 * @return
	 */
	Integer knowledgeCollect(String openid, String id);

	/**
	 * 点赞
	 * @param id
	 * @return
	 */
	Integer knowledgePraise(String id);

	/**
	 * 查询知识圈信息
	 * @param knowledgeId
	 * @param memberId
	 * @return
	 */
	Map<String, Object> queryKnowledgeById(String knowledgeId, Object memberId);

	/**
	 * 知识列表
	 * @param knowledge
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledges(Knowledge knowledge,
			Integer page, Integer row);

	/**
	 * 添加评论
	 * @param knowledgeReply
	 * @return
	 */
	Map<String, Object> addKnowledgeReply(KnowledgeReply knowledgeReply);
}
