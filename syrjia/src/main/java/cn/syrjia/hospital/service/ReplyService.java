package cn.syrjia.hospital.service;

import java.util.List;
import java.util.Map;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.hospital.entity.Reply;

public interface ReplyService extends BaseServiceInterface{
	/**
	 * 保存当前评论
	 * @param reply
	 * @return
	 */
	Integer addReply(Reply reply);

	/**
	 * 查询评论详情
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> queryReplyDetail(String id);

	/**
	 * 查询未读消息数
	 * @param openid
	 * @return
	 */
	Map<String,Object> queryUnreadNum(String openid);

	/**
	 * 查询消息列表
	 * @param memberId
	 * @param page
	 * @param row
	 * @return
	 */
	List<Map<String, Object>> queryMessageList(String memberId,Integer page,Integer row);

	/**
	 * 添加评论
	 * @param reply
	 * @return
	 */
	Integer addEvaReply(Reply reply);
	
	/**
	 * 通过grandId查询评论消息
	 * @param grandId
	 * @return
	 */
	Map<String,Object> queryMessageByGrankId(String grandId);
}
