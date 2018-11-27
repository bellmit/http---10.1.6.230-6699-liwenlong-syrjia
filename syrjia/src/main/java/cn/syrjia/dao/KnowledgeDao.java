package cn.syrjia.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Knowledge;

public interface KnowledgeDao extends BaseDaoInterface {

	/**
	 * 通过ID查询文章
	 * @param knowledgeId
	 * @return
	 */
	Map<String,Object> queryKnowledgeById(String knowledgeId);
		
	/**
	 * 查询文章列表
	 * @param knowledge
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> queryKnowledges(Knowledge knowledge,Integer page,Integer row);

	/**
	 * 文章列表
	 * @param openId
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeList(String openId,Knowledge knowledge,String type,
			Integer page, Integer row);

	/**
	 * 查询类别列表
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
	 * 查询文章详情
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
	 * 通过名字查询文章列表
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListByName(String name,
			Integer page, Integer row);

	/**
	 * 查询收藏的文章
	 * @param openid
	 * @param id
	 * @return
	 */
	Integer knowledgeCollect(String openid, String id);

	/**
	 * 通过ID查询文章
	 * @param id
	 * @return
	 */
	Map<String, Object> selectKnowledgeById(String id);
}
