package cn.syrjia.hospital.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.ReplyDao;
import cn.syrjia.hospital.entity.Reply;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("replyDao")
public class ReplyDaoImpl extends BaseDaoImpl implements ReplyDao{
	
	// 日志
	private Logger logger = LogManager.getLogger(ReplyDaoImpl.class);

	/**
	 * 保存当前评论
	 */
	@Override
	public Integer replyDao(Reply reply) {
		// TODO Auto-generated method stub
		String sql="INSERT INTO t_konw_eva_reply (id,knowId,replyId,returnId,pid,content,type,createTime,sign_,grankId,state) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		Integer i=0;
		String id = Util.getUUID();
		try {
			i = jdbcTemplate.update(sql,new Object[]{id,reply.getKnowId(),reply.getReplyId(),reply.getReturnId(),reply.getPid(),reply.getContent(),reply.getType(),Util.queryNowTime(),1,id,reply.getState()});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询评论详情
	 */
	@Override
	public List<Map<String, Object>> queryReplyDetail(String id) {
		/*String sql="SELECT tker1.*,FROM_UNIXTIME(tker1.createTime) AS times,tker1.id AS oneId,tm1.realname,tm1.headicon,GROUP_CONCAT(CONCAT_WS('^$', IF(td.doctorId IS NULL,IF(tker2.id IS NULL,NULL,0),IF(tker2.id IS NULL,NULL,1)),tker2.id,tker2.knowId,tker2.content,tm2.realname,td.docName,tker2.createTime,tm1.realname,tker1.id,tker1.knowId,tker2.grankId,tker2.returnId) SEPARATOR '&#') AS pllist"+
					" FROM t_konw_eva_reply tker1 LEFT JOIN t_konw_eva_reply  tker2 ON tker1.id = tker2.grankId "+
					" LEFT JOIN t_member tm1 ON tker1.replyId=tm1.id"+
					" LEFT JOIN t_member tm2 ON tker2.returnId=tm2.id "+
					" LEFT JOIN t_doctor td ON tker2.replyId=td.doctorId WHERE 1=1 AND tker1.pid=0";*/
		 String sql="SELECT tker1.*,FROM_UNIXTIME(tker1.createTime,'%Y-%m-%d %H:%i') AS times,tm.realname,tm.headicon"+
				 	" FROM t_konw_eva_reply tker1 LEFT JOIN t_member tm ON tker1.replyId=tm.id WHERE 1=1 AND tker1.pid='0'";
		sql += " and tker1.knowId ='"+id+"'";
		/*if(!StringUtil.isEmpty(knowledge.getName())){
			sql+=" and name like '"+knowledge.getName()+"%'";
		}
		if(!StringUtil.isEmpty(knowledge.getIsVideo())){
			sql+=" and isVideo = "+knowledge.getIsVideo();
		}*/
		sql+=" ORDER BY  tker1.createTime DESC ";
		
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询未读消息数
	 */
	@Override
	public Map<String,Object> queryUnreadNum(String memberId) {
		// TODO Auto-generated method stub
		String sql=" SELECT count(*) num FROM t_konw_eva_reply kr INNER JOIN t_konwledge k ON kr.knowId = k.id WHERE kr.state = 1 AND k.state = 1 AND kr.sign_=1 ";
		//登录人id 
		if(!StringUtil.isEmpty(memberId)){
			   sql+=" and kr.returnId = '"+memberId+"' ";
		   }
		try {
			return jdbcTemplate.queryForMap(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 查询回复列表
	 */
	@Override
	public List<Map<String, Object>> queryMessageList(String memberId,Integer page,Integer row) {
		// TODO Auto-generated method stub
 		String sql = "SELECT ktd.docUrl,ktd.docName knowDocName,kp.name AS knowDocPosition,ki.infirmaryName AS knowHosName,p.name AS docPosition,i.infirmaryName AS hosName,k.title,kr.id,kr.knowId,kr.content plContent,IFNULL(IFNULL((CASE WHEN char_length(tm.realname)=1 THEN tm.realname  WHEN char_length(tm.realname)=2 THEN CONCAT(left(tm.realname, 1),'**')  WHEN char_length(tm.realname)>2 THEN CONCAT(left(tm.realname,1),'**',right(tm.realname,1)) " +
 				    " else NULL end),(CASE WHEN char_length( td.docName)=1 THEN  td.docName WHEN char_length( td.docName)=2 THEN CONCAT(left( td.docName, 1),'**') WHEN char_length( td.docName)>2   THEN CONCAT(left( td.docName,1),'**',right( td.docName,1)) else NULL end)),(CASE WHEN tsr.showState = 1 and char_length(tsr.NAME)=1 " +
 				    " THEN tsr.NAME WHEN tsr.showState = 1 and char_length(tsr.NAME)=2  THEN CONCAT(left(tsr. NAME, 1),'**') WHEN tsr.showState = 1 and char_length(tsr.NAME)>2  THEN CONCAT(left(tsr. NAME,1),'**',right(tsr. NAME,1)) 	WHEN tsr.showState = 0 THEN tsr. NAME else NULL END )) realName,IFNULL(IFNULL((CASE WHEN char_length(m.realname)=1 " +
 				    " THEN m.realname  WHEN char_length(m.realname)=2 THEN CONCAT(left(m.realname, 1),'**') " +
 					" WHEN char_length(m.realname)>2 THEN CONCAT(left(m.realname,1),'**',right(m.realname,1))  else NULL end), (CASE WHEN char_length( d.docName)=1 THEN  d.docName WHEN char_length( d.docName)=2 THEN CONCAT(left( d.docName, 1),'**') WHEN char_length( d.docName)>2  " +
 					" THEN CONCAT(left( d.docName,1),'**',right( d.docName,1)) else NULL end)),(CASE WHEN sr.showState = 1 and char_length(sr.NAME)=1 THEN sr.NAME WHEN sr.showState = 1 and char_length(sr.NAME)=2  THEN CONCAT(left(sr. NAME, 1),'**') WHEN sr.showState = 1 and char_length(sr.NAME)>2 " +
 					" THEN CONCAT(left(sr. NAME,1),'**',right(sr. NAME,1)) 	WHEN sr.showState = 0 THEN sr. NAME else NULL END )) plName  ,"
				   + "IFNULL(IFNULL(m.headicon, d.docUrl),sr.imgUrl) headicon,(CASE WHEN m.realname IS NOT NULL THEN 1 WHEN d.docName IS NOT NULL THEN 2 WHEN sr. NAME IS NOT NULL THEN 3 else 0 end) flag,FROM_UNIXTIME(kr.createTime,'%Y-%m-%d %H:%i') times,fnStripTags(k.content) content,k.imgUrl,k.videoShowUrl,k.type	FROM t_konw_eva_reply kr "
				   +" INNER JOIN t_konwledge k ON kr.knowId = k.id "
				   +" LEFT JOIN t_member m ON kr.replyId = m.id "
				   +" LEFT JOIN t_doctor d ON kr.replyId = d.doctorId  "
				   +" LEFT JOIN t_position p ON p.id = d.docPositionId "
				   +" LEFT JOIN t_infirmary i ON i.infirmaryId = d.infirmaryId "
				   +" LEFT JOIN t_sales_represent sr ON kr.replyId = sr.srId "
				   +" LEFT JOIN t_member tm ON kr.returnId = tm.id "
				   +" LEFT JOIN t_doctor td ON kr.returnId = td.doctorId "
				   +" LEFT JOIN t_doctor ktd ON k.doctorId = ktd.doctorId "
				   +" LEFT JOIN t_position kp ON kp.id = ktd.docPositionId "
				   +" LEFT JOIN t_infirmary ki ON ki.infirmaryId = ktd.infirmaryId "
				   +" LEFT JOIN t_sales_represent tsr ON kr.returnId = tsr.srId "
				   +" where kr.state=1 and k.state=1 ";
 				   //登录人id
				   if(!StringUtil.isEmpty(memberId)){
					   sql+=" and kr.returnId = '"+memberId+"' ";
				   }
				   sql+=" ORDER BY kr.createTime DESC ";
			if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
				sql+=" limit "+(page-1)*row+","+row;
			}
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			try {
				list = jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				logger.warn(e);
				return null;
			}
			return list;
	}

	/**
	 * 添加评论
	 */
	@Override
	public Integer addEvaReply(Reply reply) {
		// TODO Auto-generated method stub
		String sql="INSERT INTO t_konw_eva_reply (id,knowId,replyId,returnId,pid,content,type,createTime,sign_,grankId) VALUES (?,?,?,?,?,?,?,?,?,?)";
		String id=Util.getUUID();
		Integer i=0;
		try {
			i = jdbcTemplate.update(sql,new Object[]{id,reply.getKnowId(),reply.getReplyId(),reply.getReturnId(),reply.getPid(),reply.getContent(),reply.getType(),Util.queryNowTime(),1,id});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 通过grandId查询评论消息
	 */
	@Override
	public Map<String, Object> queryMessageByGrankId(String grandId) {
		String sql = "SELECT tker1.grankId,tker1.content AS plcontent,td.docName as plname,IFNULL(tm.realname,td.docName) AS realname FROM "+
					"t_konw_eva_reply tker1 LEFT JOIN t_member tm ON tker1.returnId = tm.id LEFT JOIN t_doctor td ON tker1.replyId = td.doctorId "+
					"WHERE	tker1.grankId = ? ORDER BY 	tker1.createTime DESC LIMIT 1";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { grandId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}
	
	
}
