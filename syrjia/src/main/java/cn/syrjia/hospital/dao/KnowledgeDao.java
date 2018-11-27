package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.hospital.entity.Knowledge;
import cn.syrjia.hospital.entity.Reply;

public interface KnowledgeDao extends BaseDaoInterface {
	
	/**
	 * 通过Id查询文章
	 * @param knowledgeId
	 * @return
	 */
	Map<String,Object> queryKnowledgeById(String knowledgeId);
		
	/**
	 * 查询文章
	 * @param knowledge
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> queryKnowledges(Knowledge knowledge,Integer page,Integer row);

	/**
	 * 查询文章列表
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
	 * 知识圈分类列表
	 * @param openid
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryClassList(String openid,Knowledge knowledge, String type, Integer page, Integer row);

	/**
	 * 查询返回前台的详情数据
	 * @param id
	 * @param memberId
	 * @return
	 */
	Map<String, Object> queryCircleDetail(String id,String memberId);

	/**
	 * 通过ID查询商品详情
	 * @param id
	 * @return
	 */
	Map<String, Object> queryGoodsById(String id);

	/**
	 * 通过名称查询文章列表
	 * @param id
	 * @param doctorId
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListByName(String id,String doctorId,String name,
			Integer page, Integer row);

	/**
	 * 收藏文章
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

	/**
	 * 收藏文章
	 * @param openid
	 * @param id
	 * @return
	 */
	Integer selectKnowledgeCollect(String openid, String id);
	
	/**
	 * app端查询收藏文章
	 * @param openid
	 * @param id
	 * @return
	 */
	Map<String,Object> selectKnowledgeCollectApp(String openid, String id);

	/**
	 * 取消收藏
	 * @param memberId
	 * @param goodsId
	 * @return
	 */
	Integer deleteKnowledgeCollect(String memberId,String goodsId);

	/**
	 * 搜索框匹配
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeTitleAndLabel(String name);

	/**
	 * 通过名称搜索列表
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> querySearchByName(String name);

	/**
	 * 常用搜索
	 * @return
	 */
	List<Map<String, Object>> queryCommonSearch();

	/**
	 * 增加文章阅读数
	 * @param num
	 * @param id
	 * @return
	 */
	Integer updateReadNum(Integer num, String id);
	
	/**
	 * 取消关注
	 * @param memberId
	 * @param doctorId
	 * @return
	 */
	Integer quertFollow(String memberId, String doctorId);

	/**
	 * 取消关注
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer cancelFollow(String id,String memberId);
	/**
	 * 关注
	 * @param id
	 * @param openid
	 * @return
	 */
	Integer addFollow(String id, String openid);

	/**
	 * 获取医生ID和名称
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryDoctorList(String name);

	/**
	 * 查询文章分类
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeType();

	/**
	 * 商品搜索
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryGoodByName(String name);

	/**
	 * 病症标签查询
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryIllnessByName(String name);

	/**
	 * 添加文章
	 * @param knowledge
	 * @return
	 */
	Integer addKonwledgeContent(Knowledge knowledge);

	/**
	 * 轮播图列表
	 * @return
	 */
	List<Map<String, Object>> queryBanner();

	/**
	 * 保存回复内容
	 * @param reply
	 * @return
	 */
	Integer addReplyContent(Reply reply);

	/**
	 * 添加评论
	 * @param evaluate
	 * @return
	 */
	Integer addEvaContent(Evaluate evaluate);

	/**
	 * 医生端(我的文章)
	 * @param srId
	 * @param name
	 * @param doctorId
	 * @param row
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> queryDoctorArticle(String srId,String name,String doctorId,Integer row,Integer page);

	/**
	 * 医生收藏的文章
	 * @param name
	 * @param memberId
	 * @param row
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> queryDoctorCollectArticle(String name,String memberId,
			Integer row, Integer page);

	/**
	 * 收藏商品
	 * @param name
	 * @param memberId
	 * @param row
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> queryDoctorCollectGoods(String name,
			String memberId, Integer row, Integer page);

	/**
	 * 查询置顶的文章
	 * @param memberId
	 * @return
	 */
	Map<String,Object> queryKnowTop(String memberId);

	/**
	 * 置顶和取消置顶
	 * @param id
	 * @param top
	 * @return
	 */
	Integer updateKnowTop(String id,Integer top);

	/**
	 * 删除文章
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer deleteKnowState(String id,String memberId);

	/**
	 * 添加文章
	 * @param knowledge
	 * @return
	 */
	Integer addKnowArticle(Knowledge knowledge);
	
	/**
	 * 添加标签
	 * @param knowledge
	 * @return
	 */
	Integer addKnowLabel(Knowledge knowledge);
	
	/**
	 * 删除标签
	 * @param knowledge
	 * @return
	 */
	Integer deleteKnowLabel(Knowledge knowledge);

	/**
	 * 标记成已读
	 * @param memberId
	 * @return
	 */
	Integer updateUnreadMess(String memberId);

	/**
	 * 获取评论内容
	 * @param messId
	 * @return
	 */
	Map<String, Object> queryEvaContent(String messId);

	/**
	 * 点赞
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer addPraise(String id, String memberId);
	
	/**
	 * 取消点赞
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer cancelPraise(String id,String memberId);
	/**
	 * 文章点赞
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer quertPraise(String id, String memberId);

	/**
	 * 文章详情里的评论列表
	 * @param id
	 * @param page
	 * @param row
	 * @param memberId
	 * @return
	 */
	List<Map<String, Object>> queryEvaList(String id, Integer page, Integer row,String memberId);

	/**
	 * 查询评论回复内容
	 * @param knowId
	 * @param memberId
	 * @return
	 */
	List<Map<String, Object>> queryEvaListContent(String knowId,String memberId);
	
	/**
	 * 查询文章操作记录
	 * @param id
	 * @return
	 */
	public abstract List<Map<String,Object>> queryKonwOperaRecord(String id,String doctorId);

	/**
	 * 通过名称查询知识圈列表
	 * @param id
	 * @param memberId
	 * @param doctorId
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeSearch(String id,String memberId, String doctorId,
			String name, Integer page, Integer row);
	
	/**
	 * 通过名称查询知识圈列表
	 * @param id
	 * @param memberId
	 * @param doctorId
	 * @param name
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeSearchName(String id,String memberId, String doctorId,
			String name, Integer page, Integer row);
	
	/**
	 * 取消置顶
	 * @param id
	 * @return
	 */
	Integer updateKnowUnTop(String id);

	/**
	 * 查询文章详情
	 * @param id
	 * @param doctorId
	 * @return
	 */
	Map<String, Object> queryKnowArticle(String id, String doctorId);
	
	/**
	 * 编辑文章
	 * @param knowledge
	 * @return
	 */
	Integer editKnowArticle(Knowledge knowledge);
	
	/**
	 * 查询商品列表
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryGoods(String name);

	/**
	 * 查询关注的列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListKeep(String memberId,
			Integer page, Integer row);

	/**
	 * 获取关注列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListFoll(String memberId,
			Integer page, Integer row);

	/**
	 * 获取热门列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListHot(String memberId,
			Integer page, Integer row);

	/**
	 * 获取收藏列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListColl(String memberId,
			Integer page, Integer row);
	
	/**
	 * 通过ID查询商品
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> queryGoodsByIds(String goodsId);
	
	/**
	 * 查询可以发布的文章的列表
	 * @param typeId
	 * @param type
	 * @param labelId
	 * @param title
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	Map<String,Object> queryAllKnowledge(String typeId,Integer type,String labelId,String title,String memberId,Integer page,Integer row);
	
	/**
	 * 发送文章
	 * @param memberId
	 * @param ids
	 * @return
	 */
	Integer sendKnowledge( String memberId,String ids);
	
	/**
	 * 发布文章里面的分类列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> querySendClass(String memberId,Integer page,Integer row);
	
	/**
	 * 查询科室，病症，症型
	 * @return
	 */
	public abstract List<Map<String,Object>> queryKnowSearchBasicData();
	
	/**
	 * 标为已读
	 * @param replyIds
	 * @return
	 */
	Integer markRead(String replyIds);
	
	/**
	 * 病症标签
	 * @return
	 */
	List<Map<String,Object>> illLabels();
	
	/**
	 * 查询病症
	 * @return
	 */
	List<Map<String,Object>> illnessClasses();
	
	/**
	 * 
	 * @param knowledgeId
	 * @param lables
	 * @param memberId
	 * @return
	 */
	Map<String,Object> queryKnowledgeInfo(String knowledgeId,String lables,String memberId);
	
	/**
	 * 查询收藏数
	 * @param knowledgeId
	 * @return
	 */
	Integer queryCollectNum(String knowledgeId);
	
	/**
	 * 通过ID查询标签名称
	 * @param lables
	 * @return
	 */
	String queryLabelNames(String lables);
	
	/**
	 * 查询评论数
	 * @param knowledgeId
	 * @param memberId
	 * @return
	 */
	Integer queryReplyNum(String knowledgeId,String memberId);
	
	/**
	 * 查询分享数
	 * @param knowledgeId
	 * @return
	 */
	Integer queryShareNum(String knowledgeId);
	
	/**
	 * 查询评论数
	 * @param knowledgeId
	 * @param memberId
	 * @return
	 */
	Integer queryPlNum(String knowledgeId,String memberId);
	
	/**
	 * 查询点赞数
	 * @param knowledgeId
	 * @return
	 */
	Integer queryPointNum(String knowledgeId);
	
	/**
	 * 查询是否关注
	 * @param doctorId
	 * @param memberId
	 * @return
	 */
	Integer queryFollow(String doctorId,String memberId);
	
	/**
	 * 查询是否点赞
	 * @param knowledgeId
	 * @param memberId
	 * @return
	 */
	Integer querypPraiseId(String knowledgeId,String memberId);
	
	/**
	 * 查询是否收藏
	 * @param knowledgeId
	 * @param memberId
	 * @return
	 */
	Map<String,Object> querypCollectId(String knowledgeId,String memberId);
}
