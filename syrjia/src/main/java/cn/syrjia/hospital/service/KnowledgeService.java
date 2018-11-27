package cn.syrjia.hospital.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.hospital.entity.Knowledge;
import cn.syrjia.hospital.entity.Reply;
import cn.syrjia.hospital.entity.UserShare;

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
	 * 知识圈分类列表
	 * @param openid
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryClassList(String openid,Knowledge knowledge,String type, Integer page, Integer row);

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
	 * 点赞文章
	 * @param id
	 * @return
	 */
	Integer knowledgePraise(String id);

	/**
	 * 搜索框匹配
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeTitleAndLabel(String name);

	/**
	 * 通过名字搜索文章
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
	Integer updateReadNum(Integer num,String id);
	/**
	 * 取消关注
	 * @param openid
	 * @param doctorId
	 * @return
	 */
	Integer quertFollow(String openid, String doctorId);

	/**
	 * 取消关注
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer cancelFollow(String id,String memberId);
	/**
	 * 关注,
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer addFollow(String id, String memberId);

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
	 * 更新置顶
	 * @param id
	 * @param memberId
	 * @param top
	 * @return
	 */
	Map<String,Object> updateKnowTop(String id,String memberId,Integer top);

	/**
	 * 删除文章
	 * @param id
	 * @param memberId
	 * @return
	 */
	Map<String,Object> deleteKnowState(String id,String memberId);

	/**
	 * 添加文章
	 * @param knowledge
	 * @return
	 */
	Map<String,Object> addKnowArticle(Knowledge knowledge);

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
	 * 文章详情里的评论列表
	 * @param id
	 * @param page
	 * @param row
	 * @param memberId
	 * @return
	 */
	List<Map<String, Object>> queryEvaList(String id, Integer page, Integer row,String memberId);

	/**
	 * 文章里的回复列表
	 * @param knowId
	 * @param memberId
	 * @return
	 */
	List<Map<String, Object>> queryEvaListContent(String knowId,String memberId);

	/**
	 * 查询收藏的文章
	 * @param memberId
	 * @param string
	 * @return
	 */
	Map<String,Object> queryknowledgeCollect(String memberId, String string);
	
	/**
	 * 查询文章操作记录
	 * @param id
	 * @return
	 */
	public abstract Map<String,Object> queryKonwOperaRecord(String id,String doctorId);

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
	 * 文章点赞
	 * @param id
	 * @param memberId
	 * @return
	 */
	Integer knowledgePraise(String id, String memberId);

	/**
	 * 通过Id查询文章
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
	Map<String, Object> editKnowArticle(Knowledge knowledge);

	/**
	 * 通过名字查询商品
	 * @param name
	 * @return
	 */
	Map<String, Object> queryGoods(String name);

	/**
	 * 查询关注的文章
	 * @param memberId
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListKeep(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row);

	/**
	 * 获取关注列表
	 * @param memberId
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListFoll(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row);

	/**
	 * 获取热门列表
	 * @param memberId
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListHot(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row);

	/**
	 * 获取收藏列表
	 * @param memberId
	 * @param knowledge
	 * @param type
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryKnowledgeListColl(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row);
	
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
	 * 批量发布文章
	 * @param memberId
	 * @param knowIds
	 * @return
	 */
	Integer sendKnowledge(String memberId,String knowIds);
	
	/**
	 * 发布文章里面的分类列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String,Object>> querySendClass(String memberId,Integer page,Integer row);
	
	/**
	 * 查询科室，病症，症型
	 * @return
	 */
	public abstract Map<String,Object> queryKnowSearchBasicData();
	
	/**
	 * 标记为已读
	 * @param list
	 * @return
	 */
	Integer markRead(List<Map<String,Object>> list);
	
	/**
	 * 保存分享
	 * @param userShare
	 * @return
	 */
	public abstract Map<String,Object> addUserShare(UserShare userShare);
}
