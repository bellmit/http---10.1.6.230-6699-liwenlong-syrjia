package cn.syrjia.hospital.dao;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.hospital.entity.Reply;

public interface ReplyDao extends BaseDaoInterface {
	/**
	 * 保存当前评论
	 * @param reply
	 * @return
	 */
	Integer replyDao(Reply reply);

	/**
	 * 查询评论详情
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> queryReplyDetail(String id);

	/**
	 * 查询未读消息数
	 * @param memberId
	 * @return
	 */
	Map<String,Object> queryUnreadNum(String memberId);

	/**
	 * 查询回复列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryMessageList(String memberId,Integer page,Integer row);

	Integer addEvaReply(Reply reply);
	
	/**
	 * 通过grandId查询评论消息
	 * @param grandId
	 * @return
	 */
	Map<String,Object> queryMessageByGrankId(String grandId);

	
}
