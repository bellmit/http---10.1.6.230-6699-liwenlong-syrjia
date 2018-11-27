package cn.syrjia.hospital.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.configCode;
import cn.syrjia.hospital.dao.ReplyDao;
import cn.syrjia.hospital.entity.Reply;
import cn.syrjia.hospital.service.ReplyService;
import cn.syrjia.util.Util;

@Service("replyService")
public class ReplyServiceImpl  extends BaseServiceImpl implements ReplyService {
	
	@Resource(name = "replyDao")
	public ReplyDao replyDao;

	@Override
	public Integer addReply(Reply reply) {
		// TODO Auto-generated method stub
		return replyDao.replyDao(reply);
	}

	@Override
	public List<Map<String, Object>> queryReplyDetail(String id) {
		// TODO Auto-generated method stub
		return replyDao.queryReplyDetail(id);
	}

	@Override
	public Map<String,Object> queryUnreadNum(String memberId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = replyDao.queryUnreadNum(memberId);
		return Util.resultMap(configCode.code_1001, map.get("num").toString());
	}

	/**
	 * 查询回复列表
	 */
	@Override
	public List<Map<String, Object>> queryMessageList(String memberId,Integer page,Integer row) {
		// TODO Auto-generated method stub
		return replyDao.queryMessageList(memberId,page,row);
	}

	@Override
	public Integer addEvaReply(Reply reply) {
		// TODO Auto-generated method stub
		return replyDao.addEvaReply(reply);
	}

	@Override
	public Map<String, Object> queryMessageByGrankId(String grandId) {
		// TODO Auto-generated method stub
		return replyDao.queryMessageByGrankId(grandId);
	}


	
}
