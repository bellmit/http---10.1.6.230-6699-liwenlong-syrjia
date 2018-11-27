package cn.syrjia.hospital.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.hospital.dao.KnowledgeDao;
import cn.syrjia.hospital.entity.Knowledge;
import cn.syrjia.hospital.entity.Reply;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("knowledgeCircleDao")
public class KnowledgeDaoImpl extends BaseDaoImpl implements KnowledgeDao {

	// 日志
	private Logger logger = LogManager.getLogger(KnowledgeDaoImpl.class);

	/**
	 * 通过ID查询文章
	 */
	@Override
	public Map<String, Object> queryKnowledgeById(String knowledgeId) {
		String sql = "SELECT k.id,k.name,k.remark,k.riskPath,k.imageUrl,k.videoPath,k.state,k.rank,k.describes,k.operationTime,k.createTime,k.isVideo,(select count(1) from t_knowledge_reply where knowledgeId=k.id and state=2) replyNum,(select count(distinct memberId) from t_knowledge_member where knowledgeId=k.id) readNum FROM t_knowledge k WHERE k.state=1 AND k.id=?";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { knowledgeId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledges(Knowledge knowledge,
			Integer page, Integer row) {
		String sql = "SELECT k.id,k.name,k.remark,k.riskPath,k.imageUrl,k.isVideo,k.videoPath,k.state,k.rank,k.describes,k.operationTime,k.createTime,(select count(1) from t_knowledge_reply where knowledgeId=k.id and state=2) replyNum,(select count(distinct memberId) from t_knowledge_member where knowledgeId=k.id) readNum FROM t_knowledge k  WHERE k.state=1";
		/*
		 * if(!StringUtil.isEmpty(knowledge.getName())){
		 * sql+=" and name like '"+knowledge.getName()+"%'"; }
		 * if(!StringUtil.isEmpty(knowledge.getIsVideo())){
		 * sql+=" and isVideo = "+knowledge.getIsVideo(); }
		 */
		sql += "  ORDER BY createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeList(String memberId,
			Knowledge knowledge, String type, Integer page, Integer row) {
		String sql = " SELECT t.*,"
				+ " CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp,IF(followId is null ,1,2) follow,IF(praiseId is null ,2,1) praise,IF(collectId is null ,1,2) collect"
				+ " FROM "
				+ " (SELECT tk.id,tk.doctorId,tk.typeId,tk.title,fnStripTags(tk.content) content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,"
				+ " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId,tuk.type keepType,FROM_UNIXTIME(tk.createTime) AS times,"
				+ " (select count(*) from t_konw_eva_reply tker where tker.knowId=tk.id and tker.pid='0' and (tker.state = 1 OR tker.replyId='"
				+ memberId
				+ "')) as replyNum,"
				+ " (select count(1) from t_user_share tus where tus.goodsId = tk.id and tus.type=2) as shareNum,"
				+ " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id and (tker.state = 1 OR tker.replyId='"
				+ memberId
				+ "')) as plNum,"
				+ " ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum,"
				+ " (select id from t_user_keep where type=3 and goodsId=tk.id and memberId='"
				+ memberId
				+ "') followId,"
				+ " (select id from t_user_praise where type=2 and goodsId=tk.id and memberId='"
				+ memberId
				+ "') praiseId,"
				+ " (select id from t_user_keep where memberId='"
				+ memberId
				+ "' and goodsId=tk.id and type=2) collectId "
				+ " FROM "
				+ " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ "	LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId  AND tuk.type=2"
				+ "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ "	LEFT JOIN t_position p ON p.id = td.docPositionId )t WHERE 1=1 and t.state=1 ";
		//类型
		if (type != null && type.equals("1")) {
			sql += " AND t.memberId='" + memberId + "'";
		}
		//如果等于2
		if (type != null && type.equals("2")) {
			sql += " and t.isHot=1 ";
		}
		//3
		if (type != null && type.equals("3")) {
			sql += " and t.memberId='" + memberId + "' and t.keepType= 2 ";
		}
		/*
		 * if(!StringUtil.isEmpty(knowledge.getName())){
		 * sql+=" and name like '"+knowledge.getName()+"%'"; }
		 * if(!StringUtil.isEmpty(knowledge.getIsVideo())){
		 * sql+=" and isVideo = "+knowledge.getIsVideo(); }
		 */
		if (type != null && type.equals("1")) {
			sql += " group by t.doctorId ORDER BY t.createTime DESC ";
		} else {
			sql += " group by t.id ORDER BY t.createTime DESC ";
		}
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询类别列表
	 */
	@Override
	public List<Map<String, Object>> queryClassList(String openid,
			Knowledge knowledge, String type, Integer page, Integer row) {
		// TODO Auto-generated method stub
		String sql = "SELECT tkt.id,tkt.typeName,tkt.imgUrl,tkt.state,tkt.createTime,tkt.updateTime , (SELECT COUNT(*) FROM t_konwledge  k INNER JOIN t_doctor d on k.doctorId=d.doctorId  WHERE k.typeId=tkt.id  and k.state =1 ) num FROM t_knowledge_type tkt WHERE state=1 ";

		/*
		 * if(!StringUtil.isEmpty(knowledge.getName())){
		 * sql+=" and name like '"+knowledge.getName()+"%'"; }
		 * if(!StringUtil.isEmpty(knowledge.getIsVideo())){
		 * sql+=" and isVideo = "+knowledge.getIsVideo(); }
		 */
		sql += "  ORDER BY tkt.sort DESC,tkt.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询文章详情
	 */
	@Override
	public Map<String, Object> queryCircleDetail(String id, String memberId) {
		// String sql =
		// "select '"+memberId+"' as flag,t.*,IF(t.followId IS NULL,1,2) AS follow,IF(t.praiseId IS NULL,1,2) praise,IF(t.collectId IS NULL,1,2) collect,"
		String sql = "select '"
				+ memberId
				+ "' as flag,t.*"
				// +
				// "(SELECT  count(*) FROM 	t_konw_eva_reply tker1 WHERE 	1 = 1 AND tker1.pid = '0' AND (	tker1.state = 1 OR tker1.replyId='"+memberId+"') AND tker1.knowId='"+id+"' ) replyNum"
				+ " from (SELECT  tk.labelIds,tk.id,tk.doctorId,tk.typeId,tk.title,tk.content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,"
				+ "  tk.publisher,tk.sendId,tk.updateTime,FROM_UNIXTIME(tk.createTime,'%Y-%m-%d %H:%i') AS times,tk.voiceUrl,tk.videoUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,tk.original,"
				+ "  i.infirmaryName AS hosName,td.docName,td.qrCodeUrl,td.docUrl,p.name AS docPosition,"
				// "(SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  "
				// +
				// " UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1') t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
				// +
				// "  ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum,"
				+ "  (tk.actualPointNum+tk.pointNum) as pointNum,"
				// +
				// " ((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId = tk.id AND TYPE = 2 )+tk.collectNum) collectNum,"
				+ " (tk.actualCollectNum+tk.collectNum) collectNum,"
				// +
				// " (select count(1) from t_user_share tus where tus.goodsId = tk.id and tus.type=2) as shareNum,"
				+ " tk.actualShareNum as shareNum"
				// +
				// " (select COUNT(1) from t_user_keep where type=3 and goodsId=tk.doctorId and memberId='"+memberId+"' GROUP BY goodsId) followId,"
				// +
				// " (select COUNT(1) from t_user_praise where type=2 and goodsId=tk.id and memberId='"+memberId+"' GROUP BY goodsId) praiseId,"
				// +
				// " (select COUNT(1) from t_user_keep where memberId='"+memberId+"' and goodsId=tk.id and type=2 GROUP BY goodsId) collectId "
				+",		 (SELECT	group_concat( ill.illClassName ) FROM t_illness_class ill WHERE	ill.illClassId IN ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = td.doctorId )	) illClassName"
				+ " FROM "
				+ " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId=td.doctorId "
				+ " LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId"
				+ " LEFT JOIN t_position p ON p.id = td.docPositionId where tk.id=?)t ";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过id查询商品
	 */
	@Override
	public Map<String, Object> queryGoodsById(String id) {
		// TODO Auto-generated method stub
		String sql = "SELECT g.id,g.picture,g.remark,g.name,NULL keepId,g.description"
				+ " ,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock"
				+ " FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id inner join t_supplier s on s.id=g.supplierId and s.state=1 WHERE  g.id =? and  g.state=1 ";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过名字查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListByName(String id,
			String doctorId, String name, Integer page, Integer row) {
		// TODO Auto-generated method stub
		String sql = " SELECT t.*,"
				+ " CASE num WHEN num=1 THEN DATE_FORMAT(times,\'%Y-%m-%d %H:%i\') ELSE DATE_FORMAT(times,\'%H:%i\') END timesapp"
				+ " FROM "
				+ " (SELECT tk.id,tk.doctorId,tk.typeId,tk.title,fnStripTags(tk.content) content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,"
				+ " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId ,FROM_UNIXTIME(tk.createTime) AS times,"
				+ " (select count(*) from t_konw_eva_reply tker where tker.knowId=tk.id and tker.pid='0' and (tker.state = 1 OR tker.replyId='"
				+ doctorId
				+ "')) as replyNum,"
				+ " (select count(1) from t_user_share tus where tus.goodsId = tk.id and type=2) as shareNum ,"
				+ "(SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id and (tker.state = 1 OR tker.replyId='"
				+ doctorId
				+ "')) as plNum,"
				+ "((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum"
				+ " FROM "
				+ " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ "	LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId"
				+ "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ "	LEFT JOIN t_position p ON p.id = td.docPositionId )t WHERE 1=1";
		//名称
		if (!StringUtil.isEmpty(name)) {
			sql += " and (t.docName like '%" + name + "%' or t.hosName like '%"
					+ name + "%' or t.labelNames like '%" + name + "%')";
		}
		//医生id
		if (!StringUtil.isEmpty(doctorId)) {
			sql += " and t.doctorId = '" + doctorId + "%'";
		}
		//id
		if (!StringUtil.isEmpty(id)) {
			sql += " and t.typeId ='" + id + "'";
		}
		sql += "  ORDER BY t.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 收藏文章
	 */
	@Override
	public Integer knowledgeCollect(String openid, String id) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO t_user_keep (id,memberId,goodsId,createTime,TYPE) VALUES (?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { Util.getUUID(), openid,
					id, Util.queryNowTime(), 2 });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询文章通过ID
	 */
	@Override
	public Map<String, Object> selectKnowledgeById(String id) {
		// TODO Auto-generated method stub
		String sql = "SELECT id,doctorId,typeId,title,fnStripTags(content) content,isHot,sort,pointNum,createTime,state,readNum,labelNames,goodsIds,publisher,sendId "
				+ " FROM t_konwledge WHERE id=?";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 查询收藏表的数量
	 */
	@Override
	public Integer selectKnowledgeCollect(String memberId, String id) {
		// TODO Auto-generated method stub
		String sql = "select count(0) from t_user_keep where memberId=? and goodsId=? and type=?";
		try {
			if (!StringUtil.isEmpty(memberId) && !StringUtil.isEmpty(id))
				return super.queryBysqlCount(sql, new Object[] { memberId, id,
						2 });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return 0;
	}

	/**
	 * app端的查询收藏的文章
	 * 
	 */
	@Override
	public Map<String, Object> selectKnowledgeCollectApp(String memberId,
			String id) {
		// TODO Auto-generated method stub
		String sql = "select id,memberId,goodsId,createTime,type from t_user_keep where memberId=? and goodsId=? and type=2";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { memberId, id });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 删除收藏的文章
	 */
	@Override
	public Integer deleteKnowledgeCollect(String memberId, String goodsId) {
		// TODO Auto-generated method stub
		String sql = "delete from t_user_keep where type=? and goodsId=? and memberId=?";
		Integer i = 0;
		try {
			if (!StringUtil.isEmpty(memberId) && !StringUtil.isEmpty(goodsId))
				i = jdbcTemplate.update(sql, new Object[] { 2, goodsId,
						memberId });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询文章通过标题和标签
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeTitleAndLabel(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT tdc.doctorId AS doctorId,tdc.docName AS docName ,tdc.docUrl AS docUrl,i.infirmaryName AS hosName ,p.name AS docPosition,1 TYPE"
				+ " FROM "
				+ " t_doctor tdc"
				+ " INNER JOIN t_infirmary i ON i.infirmaryId = tdc.infirmaryId"
				+ " INNER JOIN t_position p ON p.id = tdc.docPositionId"
				+ " WHERE tdc.docName LIKE '%"
				+ name
				+ "%'   UNION  "
				+ " SELECT "
				+ " NULL AS doctorId,tk.title AS docName ,NULL AS docUrl,NULL AS hosName ,NULL AS docPosition,2 TYPE"
				+ " FROM "
				+ " t_konwledge tk"
				+ " WHERE tk.title LIKE '%"
				+ name
				+ "%'  UNION"
				+ " SELECT "
				+ " NULL AS doctorId,tl.evaLabelName AS docName,NULL  AS docUrl,NULL  AS hosName,NULL AS docPosition,2 TYPE"
				+ " FROM "
				+ " t_evalabel tl WHERE tl.type=3 AND tl.evaLabelName LIKE '%"
				+ name
				+ "%'   UNION"
				+ " SELECT "
				+ " NULL AS doctorId,td.name AS docName, NULL AS docUrl,NULL AS hosName,NULL AS docPosition,2 TYPE"
				+ " FROM "
				+ " t_diagnostic td WHERE td.name LIKE '%"
				+ name
				+ "%' ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 通过名字搜索功能
	 */
	@Override
	public List<Map<String, Object>> querySearchByName(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT TIMESTAMPDIFF( HOUR,t.times,NOW() ) AS xiaoshi,t.* FROM (SELECT "
				+ " tk.id,tk.doctorId,tk.typeId,tk.title,fnStripTags(tk.content) content,tk.isHot,tk.sort,tk.pointNum,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,"
				+ " td.docUrl,td.hosName,td.docName,td.docPosition,(SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2) replyNum,FROM_UNIXTIME(tk.createTime) AS times ,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId "
				+ " FROM "
				+ " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ " LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId)t where 1=1";

		/*
		 * if(!StringUtil.isEmpty(knowledge.getName())){
		 * sql+=" and name like '"+knowledge.getName()+"%'"; }
		 * if(!StringUtil.isEmpty(knowledge.getIsVideo())){
		 * sql+=" and isVideo = "+knowledge.getIsVideo(); }
		 */
		sql += "  ORDER BY t.createTime DESC ";
		/*
		 * if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
		 * sql+=" limit "+(page-1)*row+","+row; }
		 */
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询搜索的名字
	 */
	@Override
	public List<Map<String, Object>> queryCommonSearch() {
		String sql = "SELECT name FROM t_common_search where type=2 group by name order by sum(clickCount) desc";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 更新阅读数
	 */
	@Override
	public Integer updateReadNum(Integer num, String id) {
		// TODO Auto-generated method stub
		String sql = "update t_konwledge set readNum=? where id=?  ";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { num, id });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询关注表的个数
	 */
	@Override
	public Integer quertFollow(String doctorId, String memberId) {
		// TODO Auto-generated method stub

		String sql = "select count(*) from t_user_keep where 1=1 and type=3 and goodsId=? and memberId=?";
		try {
			return super.queryBysqlCount(sql,
					new Object[] { doctorId, memberId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 取消关注文章
	 */
	@Override
	public Integer cancelFollow(String id, String memberId) {
		// TODO Auto-generated method stub
		String sql = "delete from t_user_keep where type=3 and memberId=? and goodsId=?";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { memberId, id });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 添加关注
	 */
	@Override
	public Integer addFollow(String id, String memberId) {
		// TODO Auto-generated method stub
		String sql = "insert into t_user_keep (id,memberId,goodsId,createTime,type) VALUES (?,?,?,?,?) ";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { Util.getUUID(),
					memberId, id, Util.queryNowTime(), 3 });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询医生列表
	 */
	@Override
	public List<Map<String, Object>> queryDoctorList(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT " + " doctorId,docName " + " FROM "
				+ " t_doctor where docIsOn=1 and docName like '%" + name + "%'";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);

		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeType() {
		// TODO Auto-generated method stub
		//String sql = "SELECT tkt.id,tkt.typeName,tkt.imgUrl,tkt.state,tkt.createTime,tkt.updateTime , (SELECT COUNT(*) FROM t_konwledge  k INNER JOIN t_doctor d on k.doctorId=d.doctorId  WHERE k.typeId=tkt.id  and k.state =1 ) num FROM t_knowledge_type tkt WHERE state=1 "
		//		+ " ORDER BY tkt.sort DESC,tkt.createTime DESC ";
		
		String sql = "SELECT tkt1.id,tkt1.typeName,tkt1.imgUrl,tkt1.state, tkt1.createTime, tkt1.updateTime, IFNULL(t.num, 0) num "
		            +"FROM t_knowledge_type tkt1 LEFT JOIN ( SELECT tkt.id, COUNT(*) num FROM t_knowledge_type tkt LEFT JOIN "
				    +"t_konwledge k ON ( tkt.id = k.typeId AND k.state = 1 ) INNER JOIN t_doctor d ON k.doctorId = d.doctorId "
		            +"WHERE tkt.state = 1 GROUP BY tkt.id ) t ON tkt1.id = t.id "
				    +"WHERE tkt1.state = 1 ORDER BY tkt1.sort DESC, tkt1.createTime DESC";
		
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);

		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 通过名称查询商品
	 */
	@Override
	public List<Map<String, Object>> queryGoodByName(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT * " + "FROM " + "t_goods " + "WHERE NAME LIKE '%"
				+ name + "%'";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);

		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 通过名字查询文章标签，证型，病症
	 */
	@Override
	public List<Map<String, Object>> queryIllnessByName(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT id,name,null abbreviations,1 type FROM  t_diagnostic where state = 1 "
				+ " UNION  SELECT  illClassId id,illClassName name,illClassShort abbreviations,2 type  FROM t_illness_class   WHERE illClassStatus=10 AND illClassIsOn = '1'  "
				+ " UNION  SELECT  evaLabelId id,evaLabelName name,null abbreviations,3 type  FROM t_evalabel   WHERE type=3 and state=1 ";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);

		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 添加文章
	 */
	@Override
	public Integer addKonwledgeContent(Knowledge knowledge) {
		// TODO Auto-generated method stub
		String sql = "insert into t_konwledge(id,doctorId,typeId,title,content,isHot,sort,pointNum,createTime,state,readNum,labelNames,goodsIds,sendId,publisher) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate
					.update(sql,
							new Object[] { Util.getUUID(),
									knowledge.getDoctorId(),
									knowledge.getTypeId(),
									knowledge.getTitle(),
									knowledge.getContent(),
									knowledge.getIsHot(), knowledge.getSort(),
									0, Util.queryNowTime(),
									knowledge.getState(), 0,
									knowledge.getLabelNames(),
									knowledge.getGoodsIds(),
									knowledge.getSendId(), "2" });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询启用的轮播图
	 */
	@Override
	public List<Map<String, Object>> queryBanner() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM t_banner WHERE state=1 AND TYPE=5 ";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);

		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 添加回复评论内容
	 */
	@Override
	public Integer addReplyContent(Reply reply) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO t_konw_eva_reply (id,knowId,replyId,returnId,pid,content,type,createTime,sign_,state,grankId) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(
					sql,
					new Object[] { Util.getUUID(), reply.getKnowId(),
							reply.getReplyId(), reply.getReturnId(),
							reply.getPid(), reply.getContent(), 1,
							Util.queryNowTime(), 1, reply.getState(),
							reply.getGrankId() });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 添加评论内容
	 */
	@Override
	public Integer addEvaContent(Evaluate evaluate) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO t_evaluate (id,goodsId,memberId,evaluate_note,createTime,type,state) VALUES (?,?,?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql,
					new Object[] { Util.getUUID(), evaluate.getGoodsId(),
							evaluate.getMemberId(),
							evaluate.getEvaluate_note(), Util.queryNowTime(),
							3, evaluate.getState() });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询医生的文章
	 */
	@Override
	public List<Map<String, Object>> queryDoctorArticle(String srId,
			String name, String doctorId, Integer row, Integer page) {
		// TODO Auto-generated method stub
		doctorId = StringUtil.isEmpty(srId) ? doctorId : srId;
		String sql = " SELECT t.labelIds,t.id,t.doctorId,t.typeId,t.title,t.content,t.isHot,t.sort,t.createTime,t.state,t.readNum,t.goodsIds,t.publisher,t.sendId,t.type,t.voiceUrl,t.videoUrl,t.signUrl,t.imgUrl,t.videoShowUrl,t.top,t.hosName,t.docName,t.docUrl,t.docPosition,t.collectNum,t.num, t.shareNum,t.pointNum,"
				+ " CASE num "
				+ " WHEN num=1 THEN FROM_UNIXTIME(t.createTime,'%Y-%m-%d %H:%i')"
				+ " ELSE FROM_UNIXTIME(t.createTime,'%H:%i') END timesapp "
				// +
				// " END timesapp,IF(t.followId<=0,1,2) follow,IF(t.praiseId<=0,1,2) praise,IF(t.collectId<=0,1,2) collect"
				+ " FROM "
				+ " (SELECT tk.labelIds,tk.id,tk.doctorId,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,"
				+ " tk.type,tk.voiceUrl,tk.videoUrl,tk.signUrl,case tk.type when 4 then tk.imgUrl when 2 then tk.videoShowUrl else 'https://mobile.syrjia.com/syrjia/img/collectlogo.png' end imgUrl,tk.videoShowUrl,tk.top,"
				+ " i.infirmaryName AS hosName,td.docName,td.docUrl,p.name AS docPosition,"
				// " (SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 "
				// +
				// " UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1') t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
				// +
				// " ((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,"
				+ " (tk.actualCollectNum+tk.collectNum) collectNum,"
				// +
				// " (select count(1) from t_user_keep where type=2 and goodsId=tk.id and memberId=?) followId,"
				// +
				// " (select count(1) from t_user_praise where type=2 and goodsId=tk.id and memberId=?) praiseId,"
				// +
				// " (select count(1) from t_user_keep where memberId=? and goodsId=tk.id and type=2) collectId,"
				+ " UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				// + " tuk.id AS tid,tuk.memberId,tuk.goodsId ,"
				// +
				// " (SELECT COUNT(*) FROM t_konw_eva_reply tker WHERE tker.knowId=tk.id AND tker.pid='0' AND (tker.state = 1 OR tker.replyId='"+doctorId+"')) AS replyNum,"
				+ " (SELECT COUNT(1) FROM t_user_share tus WHERE tus.goodsId = tk.id AND TYPE=2) AS shareNum ,"
				// +
				// " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id AND (tker.state = 1 OR tker.replyId='"+doctorId+"')) AS plNum,"
				// +
				// " ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum"
				+ " tk.actualPointNum+tk.pointNum as pointNum"
				+ " FROM "
				+ " t_konwledge tk inner JOIN t_doctor td ON tk.doctorId = td.doctorId"
				// +
				// " LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId AND tuk.memberId=?"
				+ " left JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId"
				+ " left JOIN t_position p ON p.id = td.docPositionId where 1=1 ";

		//助理id
		if (!StringUtil.isEmpty(srId)) {
			sql += " and ((tk.state in (1,4,5) and tk.sendId='"
					+ srId
					+ "')  or (tk.state=1 and tk.doctorId in (SELECT f.openId from t_follow_history f where f.followId='"
					+ srId + "'))) ";
		//医生id
		} else if (!StringUtil.isEmpty(doctorId)) {
			sql += " and tk.state in (1,4,5) and tk.doctorId='" + doctorId + "' ";
		} else {
			sql += " and tk.state=1 ";
		}
		//名称
		if (!StringUtil.isEmpty(name)) {
			sql += " and tk.title like'%" + name + "%'";
		}
		/*
		 * if(!StringUtil.isEmpty(knowledge.getName())){
		 * sql+=" and name like '"+knowledge.getName()+"%'"; }
		 * if(!StringUtil.isEmpty(knowledge.getIsVideo())){
		 * sql+=" and isVideo = "+knowledge.getIsVideo(); }
		 */
		sql += ") t  ORDER BY t.top asc,t.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询医生收藏的文章
	 */
	@Override
	public List<Map<String, Object>> queryDoctorCollectArticle(String name,
			String memberId, Integer row, Integer page) {
		// TODO Auto-generated method stub
		String sql = " SELECT t.*,case type when 4 then t.imgUrl when 2 then t.videoShowUrl else 'https://mobile.syrjia.com/syrjia/img/collectlogo.png' end docUrl,"
				+ " CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp"
				+ " FROM "
				+ " (SELECT tk.id,tk.doctorId,tk.type,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,tk.pointNum,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,"
				+ " i.infirmaryName AS hosName,td.docName,p.name AS docPosition,(SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2) replyNum,UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId ,FROM_UNIXTIME(tk.createTime) AS times,tuk.type as keepType,tk.imgUrl,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.videoShowUrl"
				+ " FROM "
				+ " t_konwledge tk INNER JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ "	INNER JOIN t_user_keep tuk ON tk.id =tuk.goodsId"
				+ "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ "	LEFT JOIN t_position p ON p.id = td.docPositionId ";
		//登录人id
		if (!StringUtil.isEmpty(memberId)) {
			sql += " where tuk.memberId='" + memberId + "'";
		}
		sql += " group by tk.id)t WHERE 1=1 and t.keepType = 2 and t.state=1 ";
		//名称
		if (!StringUtil.isEmpty(name)) {
			sql += " and t.title like'%" + name + "%'";
		}
		/*
		 * if(!StringUtil.isEmpty(knowledge.getName())){
		 * sql+=" and name like '"+knowledge.getName()+"%'"; }
		 * if(!StringUtil.isEmpty(knowledge.getIsVideo())){
		 * sql+=" and isVideo = "+knowledge.getIsVideo(); }
		 */
		sql += "  ORDER BY t.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询医生收藏的商品
	 */
	@Override
	public List<Map<String, Object>> queryDoctorCollectGoods(String name,
			String memberId, Integer row, Integer page) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT g.id,g.picture,g.remark,g.name,NULL keepId,g.description"
					+ " ,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock"
					+ " FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id"
					+ " INNER JOIN t_user_keep tuk ON gp.goodsId=tuk.goodsId WHERE tuk.type=1 and tuk.memberId=?";
			if (!StringUtil.isEmpty(name)) {
				sql += " and g.name LIKE '%" + name + "%'";
			}
			sql += " GROUP BY gp.goodsId";
			if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
				sql += " limit " + (page - 1) * row + "," + row;
			}
			// System.out.println(sql);
			listMaps = jdbcTemplate
					.queryForList(sql, new Object[] { memberId });
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}

		return listMaps;
	}

	/**
	 * 查询置顶的文章
	 */
	@Override
	public Map<String, Object> queryKnowTop(String memberId) {
		// TODO Auto-generated method stub
		String sql = "SELECT id FROM t_konwledge WHERE doctorId=?  AND top=0 and state=1";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { memberId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 置顶和取消置顶
	 */
	@Override
	public Integer updateKnowTop(String id, Integer top) {
		// TODO Auto-generated method stub
		Integer i = 0;
		if (top != null) {
			String sql = "UPDATE t_konwledge SET top=" + top + " WHERE id=? ";
			try {
				i = jdbcTemplate.update(sql, new Object[] { id });
			} catch (DataAccessException e) {
				logger.error(e);
				throw e;
			}
		}
		return i;
	}

	/**
	 * 删除文章
	 */
	@Override
	public Integer deleteKnowState(String id, String memberId) {
		// TODO Auto-generated method stub
		String sql = "UPDATE t_konwledge SET state=3 WHERE id=? ";
		String sqlrecord = " insert into t_knowledge_opera_record (knowId,operaId,operaNote,operaTime,state) values (?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { id });
			i = jdbcTemplate.update(sqlrecord, new Object[] { id, memberId,
					"删除", Util.queryNowTime(), 1 });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 添加文章
	 */
	@Override
	public Integer addKnowArticle(Knowledge knowledge) {
		String sql = "insert into t_konwledge(id,doctorId,typeId,title,content,isHot,sort,pointNum,createTime,state,readNum,goodsIds,sendId,publisher,voiceUrl,videoUrl,signUrl,imgUrl,videoShowUrl,top,type,original,labelIds,sendTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlrecord = " insert into t_knowledge_opera_record (knowId,operaId,operaNote,operaTime,state) values (?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(
					sql,
					new Object[] { knowledge.getId(), knowledge.getDoctorId(),
							knowledge.getTypeId(), knowledge.getTitle(),
							knowledge.getContent(), 0, 0, 0,
							Util.queryNowTime(), knowledge.getState(), 0,
							knowledge.getGoodsIds(), knowledge.getSendId(),
							"2", knowledge.getVoiceUrl(),
							knowledge.getVideoUrl(), knowledge.getSignUrl(),
							knowledge.getImgUrl(), knowledge.getVideoShowUrl(),
							1, knowledge.getType(), knowledge.getOriginal(),
							knowledge.getLabelIds(), knowledge.getSendTime() });
			/*
			 * i = jdbcTemplate.update( sqlrecord, new Object[] { uuId,
			 * knowledge.getDoctorId(), "新增", Util.queryNowTime(), 1 });
			 */
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 改为已读状态
	 */
	@Override
	public Integer updateUnreadMess(String memberId) {
		// TODO Auto-generated method stub
		String sql = " update t_konw_eva_reply set sign_=2 where id in (select t.id from (SELECT	kr.id from t_konw_eva_reply kr INNER JOIN t_konwledge k ON kr.knowId = k.id WHERE kr.state = 1 AND k.state = 1 AND kr.id NOT IN (SELECT pid FROM t_konw_eva_reply where state=1 or state=4) and kr.returnId =?) t )";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { memberId });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 通过ID查询评论内容
	 */
	@Override
	public Map<String, Object> queryEvaContent(String messId) {
		// TODO Auto-generated method stub
		String sql = "select * from t_konw_eva_reply where id=?";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { messId });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 点赞
	 */
	@Override
	public Integer addPraise(String id, String memberId) {
		// TODO Auto-generated method stub
		String sql = " insert into t_user_praise (id,memberId,goodsId,createTime,type) value(?,?,?,?,?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { Util.getUUID(),
					memberId, id, Util.queryNowTime(), 2 });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 取消点赞
	 */
	@Override
	public Integer cancelPraise(String id, String memberId) {
		// TODO Auto-generated method stub
		String sql = " delete from t_user_praise where goodsId=? and memberId=? and type=2";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { id, memberId });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 查询点赞
	 */
	@Override
	public Integer quertPraise(String id, String memberId) {
		// TODO Auto-generated method stub

		String sql = "select count(1) from t_user_praise where 1=1 and type=2 and goodsId=? and memberId=?";
		try {
			return super.queryBysqlCount(sql, new Object[] { id, memberId });
		} catch (DataAccessException e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 查询评论列表
	 */
	@Override
	public List<Map<String, Object>> queryEvaList(String id, Integer page,
			Integer row, String memberId) {
		String sql = "SELECT tker.*,FROM_UNIXTIME(tker.createTime,'%Y-%m-%d %H:%i') AS times,IFNULL(IFNULL( (CASE WHEN char_length(tm.realname)=1 THEN tm.realname WHEN char_length(tm.realname)=2 THEN CONCAT(left(tm.realname, 1),'**') WHEN char_length(tm.realname)>2 "
				+ "THEN CONCAT(left(tm.realname,1),'**',right(tm.realname,1)) else NULL end),(CASE WHEN char_length( td.docName)=1 THEN  td.docName WHEN char_length( td.docName)=2 THEN CONCAT(left( td.docName, 1),'**') WHEN char_length( td.docName)>2 THEN CONCAT(left( td.docName,1),'**',right( td.docName,1)) "
				+ "else NULL end)),(CASE WHEN ts.showState = 1 and char_length(ts.NAME)=1 THEN ts.NAME WHEN ts.showState = 1 and char_length(ts.NAME)=2 THEN CONCAT(left(ts. NAME, 1),'**') WHEN ts.showState = 1 and char_length(ts.NAME)>2 THEN CONCAT(left(ts. NAME,1),'**',right(ts. NAME,1)) WHEN ts.showState = 0 "
				+ " THEN ts. NAME else NULL END )) realname,"
				+ "IFNULL(IFNULL(tm.headicon, td.docUrl),ts.imgUrl) AS headicon,(CASE WHEN tm.realname IS NOT NULL THEN 1 WHEN td.docName IS NOT NULL THEN 2 WHEN ts. NAME IS NOT NULL THEN 3 else 0 end) flag "
				+ " FROM t_konw_eva_reply tker LEFT JOIN t_member tm ON tker.replyId=tm.id LEFT JOIN t_doctor td ON tker.replyId=td.doctorId LEFT JOIN t_sales_represent ts ON tker.replyId = ts.srid WHERE 1=1 AND tker.pid='0'   and (tker.state=1 or (tker.replyId='"
				+ memberId + "' and tker.state<>3))";

		sql += " and tker.knowId ='" + id + "'";

		sql += " ORDER BY  tker.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询回复内容
	 */
	@Override
	public List<Map<String, Object>> queryEvaListContent(String knowId,
			String memberId) {
		// TODO Auto-generated method stub
		String sql = "SELECT tker.id,FROM_UNIXTIME(tker.createTime,'%Y-%m-%d %H:%i') AS times,IFNULL(IFNULL( (CASE WHEN char_length(tm.realname)=1 THEN tm.realname "
				+ " WHEN char_length(tm.realname)=2 THEN CONCAT(left(tm.realname, 1),'**') WHEN char_length(tm.realname)>2 THEN CONCAT(left(tm.realname,1),'**',right(tm.realname,1)) "
				+ " else NULL end),(CASE WHEN char_length( td.docName)=1 THEN  td.docName WHEN char_length( td.docName)=2 THEN CONCAT(left( td.docName, 1),'**') WHEN char_length( td.docName)>2  "
				+ " THEN CONCAT(left( td.docName,1),'**',right( td.docName,1)) else NULL end)),(CASE WHEN ts.showState = 1 and char_length(ts.NAME)=1 THEN ts.NAME WHEN ts.showState = 1 and char_length(ts.NAME)=2 "
				+ " THEN CONCAT(left(ts. NAME, 1),'**') WHEN ts.showState = 1 and char_length(ts.NAME)>2 THEN CONCAT(left(ts. NAME,1),'**',right(ts. NAME,1)) 	WHEN ts.showState = 0 THEN ts. NAME else NULL END )) AS plname,"
				+ " IFNULL(IFNULL( (CASE WHEN char_length(tm1.realname)=1 THEN tm1.realname WHEN char_length(tm1.realname)=2 THEN CONCAT(left(tm1.realname, 1),'**') WHEN char_length(tm1.realname)>2 THEN "
				+ " CONCAT(left(tm1.realname,1),'**',right(tm1.realname,1)) else NULL end),(CASE WHEN char_length( td1.docName)=1 THEN  td1.docName WHEN char_length( td1.docName)=2 THEN CONCAT(left( td1.docName, 1),'**') "
				+ " WHEN char_length( td1.docName)>2 THEN CONCAT(left( td1.docName,1),'**',right( td1.docName,1)) else NULL end)),(CASE WHEN ts1.showState = 1 and char_length(ts1.NAME)=1 THEN ts1.NAME WHEN ts1.showState = 1 "
				+ " and char_length(ts1.NAME)=2 THEN CONCAT(left(ts1. NAME, 1),'**') WHEN ts1.showState = 1 and char_length(ts1.NAME)>2 THEN CONCAT(left(ts1. NAME,1),'**',right(ts1. NAME,1)) 	WHEN ts1.showState = 0 THEN ts1. NAME else NULL END )) realname,"
				+ " tker.content AS plcontent,IFNULL(td.docName, 0) AS plPos"
				+ " FROM	t_konw_eva_reply tker "
				+ " LEFT JOIN t_member tm1 ON tker.returnId = tm1.id"
				+ " LEFT JOIN t_doctor td1 ON tker.returnId = td1.doctorId"
				+ " LEFT JOIN t_sales_represent ts1 ON tker.returnId = ts1.srId"
				+ " LEFT JOIN t_member tm ON tker.replyId = tm.id"
				+ " LEFT JOIN t_doctor td ON tker.replyId = td.doctorId"
				+ " LEFT JOIN t_sales_represent ts ON tker.replyId = ts.srId"
				+ " WHERE 1=1 and tker.pid <>'0' and (tker.state=1 OR (tker.replyId='"
				+ memberId
				+ "' and tker.state<>3 ))  and tker.grankId ='"
				+ knowId + "'";
		sql += " ORDER BY  tker.createTime ";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询修改记录
	 */
	@Override
	public List<Map<String, Object>> queryKonwOperaRecord(String id,
			String doctorId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT CONCAT(ddd.operaName,' ',ddd.operaRole,' ',ddd.operaDate,'进行',ddd.operaRecord) knowOpera from (SELECT IFNULL(htUser.realName,IFNULL(sr.srName,doc.docName)) operaName,IFNULL(htUser.operaRole,IFNULL(sr.operaRole,doc.operaRole)) operaRole,FROM_UNIXTIME(kr.operaTime,'%Y年%m月%d日') operaDate,kr.operaNote operaRecord from t_knowledge_opera_record kr "
					+ " INNER JOIN t_konwledge k on k.id = kr.knowId "
					+ " INNER JOIN t_doctor d on d.doctorId = k.doctorId "
					+ " LEFT JOIN (SELECT u.id,u.realName,GROUP_CONCAT(r.`name`) operaRole from t_user u inner JOIN t_user_role ur on ur.user_id = u.id inner JOIN t_role r on r.id = ur.role_id where u.state=1) htUser on htUser.id = kr.operaId "
					+ " LEFT JOIN (SELECT s.srId,s.`name` srName,'助理' operaRole from t_sales_represent s where s.state=0 ) sr on sr.srId = kr.operaId "
					+ " LEFT JOIN (SELECT d.doctorId,d.docName,'作者' operaRole from t_doctor d where d.docStatus='10' ) doc on doc.doctorId = kr.operaId "
					+ " WHERE d.docStatus='10'  and kr.state=1 and kr.knowId=? and d.doctorId=?) ddd ";
			if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(doctorId)) {
				list = super.queryBysqlList(sql, new Object[] { id, doctorId });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return list;
	}

	/**
	 * 搜索的文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeSearch(String id,
			String memberId, String doctorId, String name, Integer page,
			Integer row) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//搜索出来的文章
			String sql = " SELECT t.typesearch, t.id, t.doctorId, t.typeId, t.title, t.content, t.isHot, t.sort, t.pointNum, t.createTime, t.state, "
					+ " t.readNum, t.goodsIds, t.publisher, t.sendId, t.voiceUrl, t.videoUrl, t.signUrl, t.imgUrl, t.videoShowUrl, t.top, t.type, "
					+ " t.labelNames, t.docUrl, t.hosName, t.docName, t.docPosition, t.collectNum,t.shareNum, t.timesapp, "
					+ " t.replyNum,t.replyNum plNum,t.followId,t.praiseId,t.collectId,t.collectTime,t.follow,t.praise,t.collect "
					+ " FROM (SELECT t.typesearch, t.id, t.doctorId, t.typeId, t.title, t.content, t.isHot, t.sort, t.pointNum, t.createTime, t.state, "
					+ " t.readNum, t.goodsIds, t.publisher, t.sendId, t.voiceUrl, t.videoUrl, t.signUrl, t.imgUrl, t.videoShowUrl, t.top, t.type, "
					+ " t.labelNames, t.docUrl, t.hosName, t.docName, t.docPosition, t.collectNum,t.shareNum,CASE WHEN UNIX_TIMESTAMP(NOW()) - t.createTime > 24 * 60 * 60 THEN FROM_UNIXTIME( "
					+ " t.createTime,'%Y-%m-%d %H:%i') ELSE FROM_UNIXTIME(t.createTime, '%H:%i') END timesapp, "
					+" t.replyNum,t.replyNum plNum,t.followNum followId,t.praiseNum praiseId,t.num collectId,t.collectCreateTime collectTime, "
					+" CASE WHEN t.followNum=0 THEN 1 else 2 END follow,CASE WHEN t.praiseNum=0 THEN 1 else 2 END praise,CASE WHEN t.num=0 THEN 1 else 2 END collect "
					+" FROM(SELECT 1 typesearch,tk.id, tk.doctorId, tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,(tk.actualPointNum + tk.pointNum) AS pointNum, "
					+ " tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type, "
					+ " labels.labelNames,td.docUrl,i.infirmaryName AS hosName,td.docName,p.`name` AS docPosition,(tk.actualCollectNum + tk.collectNum) collectNum,tk.actualShareNum AS shareNum, "
					+" IFNULL(reply.replyNum,0) replyNum,ifnull(follow.followNum,0) followNum,ifnull(praise.praiseNum,0) praiseNum,ifnull(collect.num,0) num,ifnull(collect.createTime,'') collectCreateTime "
					+ " FROM t_konwledge tk INNER JOIN t_doctor td ON tk.doctorId = td.doctorId LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
					+ " LEFT JOIN t_position p ON p.id = td.docPositionId LEFT JOIN (SELECT GROUP_CONCAT(CONCAT_WS( ',',el.evaLabelName,d.`name`,ic.illClassName)) labelNames,kl.knowId from t_know_label kl "
					+ " LEFT JOIN t_evalabel el FORCE INDEX (PRIMARY) ON el.evaLabelId = kl.labelId AND el.state = 1 AND el.type = 3 "
					+ " LEFT JOIN t_diagnostic d ON d.id = kl.labelId AND d.state = 1 LEFT JOIN t_illness_class ic ON ic.illClassId = kl.labelId "
					+ " AND illClassStatus = '10' AND illClassIsOn = '1' GROUP BY kl.knowId) labels on labels.knowId= tk.id"
					+ " LEFT JOIN (SELECT COUNT(1) replyNum,tker.knowId FROM t_konw_eva_reply tker WHERE (tker.state = 1 OR tker.replyId = '"+memberId+"') AND tker.pid = '0' GROUP BY tker.knowId) reply on reply.knowId = tk.id "
					+" LEFT JOIN (SELECT count(1) followNum,goodsId FROM t_user_keep WHERE type = 3 AND memberId = '"+memberId+"' GROUP BY goodsId) follow on follow.goodsId = tk.id "
					+" LEFT JOIN (SELECT count(1) praiseNum,goodsId FROM t_user_praise WHERE type = 2 AND memberId = '"+memberId+"' GROUP BY goodsId) praise on praise.goodsId = tk.id "
					+" LEFT JOIN (SELECT count(1) num,createTime,goodsId FROM t_user_keep WHERE memberId = '"+memberId+"' and type=2 GROUP BY goodsId) collect on collect.goodsId = tk.id "
					+" WHERE tk.state = 1 ) t "
					+ " WHERE 1 = 1 ";
			//名称
			if (!StringUtil.isEmpty(name)) {
				sql += " and (t.docName like '%" + name
						+ "%' or t.hosName like '%" + name
						+ "%' or t.labelNames like '%" + name
						+ "%' or t.title  like '%" + name + "%')";
			}
			if (!StringUtil.isEmpty(id)) {
				sql += " and t.typeId ='" + id + "'";
			}
	//热门文章
	sql += " UNION SELECT t.typesearch, t.id, t.doctorId, t.typeId, t.title, t.content, t.isHot, t.sort, t.pointNum, t.createTime, t.state, "
			+ " t.readNum, t.goodsIds, t.publisher, t.sendId, t.voiceUrl, t.videoUrl, t.signUrl, t.imgUrl, t.videoShowUrl, t.top, t.type,  "
			+ " t.labelNames, t.docUrl, t.hosName, t.docName, t.docPosition, t.collectNum,t.shareNum,CASE WHEN UNIX_TIMESTAMP(NOW()) - t.createTime > 24 * 60 * 60 THEN FROM_UNIXTIME( "
			+ " t.createTime,'%Y-%m-%d %H:%i') ELSE FROM_UNIXTIME(t.createTime, '%H:%i') END timesapp, "
			+" t.replyNum,t.replyNum plNum,t.followNum followId,t.praiseNum praiseId,t.num collectId,t.collectCreateTime collectTime, "
			+" CASE WHEN t.followNum=0 THEN 1 else 2 END follow,CASE WHEN t.praiseNum=0 THEN 1 else 2 END praise,CASE WHEN t.num=0 THEN 1 else 2 END collect "
			+ " FROM (SELECT 2 typesearch, tk.id,tk.doctorId,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,tk.actualPointNum + tk.pointNum AS pointNum,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId, "
			+ " tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,labels.labelNames,td.docUrl,i.infirmaryName AS hosName,td.docName,p.`name` AS docPosition,(tk.actualCollectNum + tk.collectNum) collectNum,tk.actualShareNum AS shareNum, "
			+ " IFNULL(reply.replyNum,0) replyNum,ifnull(follow.followNum,0) followNum,ifnull(praise.praiseNum,0) praiseNum,ifnull(collect.num,0) num,ifnull(collect.createTime,'') collectCreateTime "
			+ " FROM t_konwledge tk INNER JOIN t_doctor td ON tk.doctorId = td.doctorId LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
			+ " LEFT JOIN t_position p ON p.id = td.docPositionId LEFT JOIN (SELECT GROUP_CONCAT(CONCAT_WS(',',el.evaLabelName,d.`name`,ic.illClassName)) labelNames,kl.knowId from t_know_label kl "
			+ " LEFT JOIN t_evalabel el FORCE INDEX (PRIMARY) ON el.evaLabelId = kl.labelId AND el.state = 1 AND el.type = 3 LEFT JOIN t_diagnostic d ON d.id = kl.labelId AND d.state = 1 "
			+ " LEFT JOIN t_illness_class ic ON ic.illClassId = kl.labelId AND illClassStatus = '10' AND illClassIsOn = '1' GROUP BY kl.knowId) labels on labels.knowId= tk.id "
			+ " LEFT JOIN (SELECT COUNT(1) replyNum,tker.knowId FROM t_konw_eva_reply tker WHERE (tker.state = 1 OR tker.replyId = '"+memberId+"') AND tker.pid = '0' GROUP BY tker.knowId) reply on reply.knowId = tk.id "
			+" LEFT JOIN (SELECT count(1) followNum,goodsId FROM t_user_keep WHERE type = 3 AND memberId = '"+memberId+"' GROUP BY goodsId) follow on follow.goodsId = tk.id "
			+" LEFT JOIN (SELECT count(1) praiseNum,goodsId FROM t_user_praise WHERE type = 2 AND memberId = '"+memberId+"' GROUP BY goodsId) praise on praise.goodsId = tk.id "
			+" LEFT JOIN (SELECT count(1) num,createTime,goodsId FROM t_user_keep WHERE memberId = '"+memberId+"' and type=2 GROUP BY goodsId) collect on collect.goodsId = tk.id "
			+ " WHERE tk.isHot = 1 AND tk.state = 1) t where 1=1 ";
		/*if (!StringUtil.isEmpty(name)) {
			sql += " and t.docName not like '%" + name
					+ "%' and t.hosName not like '%" + name
					+ "%' and t.labelNames not like '%" + name
					+ "%' and t.title not  like '%" + name + "%' ";
		}
		if (!StringUtil.isEmpty(id)) {
			sql += " and t.typeId <>'" + id + "'";
		}*/
			sql+= " ) t ORDER BY t.typesearch ASC,t.createTime DESC ";
			if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
				sql += " limit " + (page - 1) * row + "," + row;
			}
			System.out.println(sql);
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.warn(e);
		}
		return list;
	}

	/**
	 * 通过名称查询文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeSearchName(String id,
			String memberId, String doctorId, String name, Integer page,
			Integer row) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/*
			 * String sql = " SELECT * FROM ( "+
			 * " SELECT t.*,CASE WHEN num=1 THEN FROM_UNIXTIME(t.createTime,'%Y-%m-%d %H:%i') ELSE FROM_UNIXTIME(t.createTime,'%H:%i') END timesapp"
			 * + //
			 * " IF(t.followId IS NULL,1,2) AS follow,IF(t.praiseId IS NULL,1,2) praise,IF(t.collectId IS NULL,1,2) collect "
			 * +
			 * "  FROM (SELECT  1 typesearch,tk.id,tk.doctorId,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,"
			 * //+
			 * "((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id AND tup.type=2)+tk.pointNum) AS pointNum,"
			 * + +" tk.actualPointNum+tk.pointNum AS pointNum,"+
			 * "  tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,"
			 * + " tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type," +
			 * "(SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  "
			 * +
			 * " UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1' ) t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
			 * +
			 * " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,"
			 * + //
			 * " ((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,"
			 * + " (tk.actualCollectNum+tk.collectNum) collectNum," +
			 * "UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"+ //
			 * " (SELECT COUNT(*) FROM t_konw_eva_reply tker WHERE tker.knowId=tk.id AND tker.pid='0' AND (tker.state = 1 OR tker.replyId='"
			 * +memberId+"')) AS replyNum," + //
			 * "(SELECT COUNT(1) FROM t_user_share tus WHERE tus.goodsId = tk.id AND TYPE=2) AS shareNum ,"
			 * + "tk.actualShareNum AS shareNum"+ //
			 * " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id AND (tker.state=1 or tker.replyId='"
			 * +memberId+"') and tker.pid='0') AS plNum," + //
			 * "(SELECT COUNT(1) FROM t_user_keep WHERE TYPE=3 AND goodsId=tk.doctorId AND memberId='"
			 * +memberId+"' GROUP BY goodsId) followId,"+ //
			 * " (SELECT COUNT(1) FROM t_user_praise WHERE TYPE=2 AND goodsId=tk.id AND memberId='"
			 * +memberId+"' GROUP BY goodsId) praiseId," +
			 * //"(SELECT COUNT(1) FROM t_user_keep WHERE memberId='"+memberId+
			 * "' AND goodsId=tk.id AND TYPE=2 GROUP BY goodsId) collectId"+
			 * " FROM "+
			 * " t_konwledge tk inner JOIN t_doctor td ON tk.doctorId = td.doctorId"
			 * + " LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId" +
			 * " LEFT JOIN t_position p ON p.id = td.docPositionId )t WHERE 1=1 and t.state=1  "
			 * ; if (!StringUtil.isEmpty(name)) { sql +=
			 * " and (t.docName like '%" + name + "%' or t.hosName like '%" +
			 * name + "%' or t.labelNames like '%" + name +
			 * "%' or t.title  like '%" + name + "%')"; } if
			 * (!StringUtil.isEmpty(id)) { sql += " and t.typeId ='" + id + "'";
			 * }
			 * 
			 * sql+=" UNION"+
			 * "  SELECT t.*,CASE WHEN num=1 THEN FROM_UNIXTIME(t.createTime,'%Y-%m-%d %H:%i') ELSE FROM_UNIXTIME(t.createTime,'%H:%i') END timesapp"
			 * + //
			 * " IF(t.followId IS NULL,1,2) AS follow,IF(t.praiseId IS NULL,1,2) praise,IF(t.collectId IS NULL,1,2) collect"
			 * +
			 * "  FROM (SELECT  2 typesearch,tk.id,tk.doctorId,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,"
			 * + //
			 * "((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id AND tup.type=2)+tk.pointNum) AS pointNum,"
			 * + " tk.actualPointNum+tk.pointNum AS pointNum,"+
			 * "  tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,"
			 * + "  tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type," +
			 * "(SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  "
			 * +
			 * " UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1' ) t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
			 * +
			 * " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,"
			 * + //
			 * "((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,"
			 * + " (tk.actualCollectNum+tk.collectNum) collectNum," +
			 * " UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num," + //
			 * "(SELECT COUNT(*) FROM t_konw_eva_reply tker WHERE tker.knowId=tk.id AND tker.pid='0' AND (tker.state = 1 OR tker.replyId='"
			 * +memberId+"')) AS replyNum,"+ //
			 * "  (SELECT COUNT(1) FROM t_user_share tus WHERE tus.goodsId = tk.id AND TYPE=2) AS shareNum ,"
			 * + " tk.actualShareNum AS shareNum"+ //
			 * "  (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id AND (tker.state=1 or tker.replyId='"
			 * +memberId+"') and tker.pid='0') AS plNum,"+ //
			 * " (SELECT COUNT(1) FROM t_user_keep WHERE TYPE=3 AND goodsId=tk.doctorId AND memberId='"
			 * +memberId+"' GROUP BY goodsId) followId,"+ //
			 * "  (SELECT COUNT(1) FROM t_user_praise WHERE TYPE=2 AND goodsId=tk.id AND memberId='"
			 * +memberId+"' GROUP BY goodsId) praiseId,"+
			 * //" (SELECT COUNT(1) FROM t_user_keep WHERE memberId='"+memberId+
			 * "' AND goodsId=tk.id AND TYPE=2 GROUP BY goodsId) collectId"+
			 * "  FROM "+
			 * "  t_konwledge tk INNER JOIN t_doctor td ON tk.doctorId = td.doctorId"
			 * + "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId"+
			 * "	LEFT JOIN t_position p ON p.id = td.docPositionId )t"+
			 * "	 WHERE 1=1 AND t.isHot=1)t where t.state=1"; sql +=
			 * " ORDER BY t.typesearch asc, t.createTime DESC ";
			 */

			String sql = "SELECT * FROM (SELECT t.*, CASE WHEN num = 1 THEN FROM_UNIXTIME( t.createTime, '%Y-%m-%d %H:%i' ) ELSE FROM_UNIXTIME(t.createTime, '%H:%i') END timesapp FROM (SELECT 1 typesearch, tk.id, tk.doctorId, tk.typeId, tk.title, tk.content content, tk.isHot, tk.sort, tk.actualPointNum + tk.pointNum AS pointNum, tk.createTime, tk.state, tk.readNum, tk.goodsIds, tk.publisher, tk.sendId, tk.voiceUrl, tk.videoUrl, tk.signUrl, tk.imgUrl, tk.videoShowUrl, tk.top, tk.type, GROUP_CONCAT(CONCAT_WS(',',el.evaLabelName,d.name,ic.illClassName))labelNames, td.docUrl, i.infirmaryName AS hosName, td.docName, p. NAME AS docPosition, ( tk.actualCollectNum + tk.collectNum )collectNum, UNIX_TIMESTAMP(NOW())- tk.createTime > 24 * 60 * 60 AS num, tk.actualShareNum AS shareNum FROM t_konwledge tk INNER JOIN t_doctor td ON tk.doctorId = td.doctorId LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId LEFT JOIN t_position p ON p.id = td.docPositionId LEFT JOIN t_know_label kl ON kl.knowId=tk.id LEFT JOIN t_evalabel el force index(PRIMARY) ON el.evaLabelId=kl.labelId AND el.state=1 AND el.type=3 LEFT JOIN t_diagnostic d ON d.id=kl.labelId AND d.state=1 LEFT JOIN t_illness_class ic ON ic.illClassId=kl.labelId AND illClassStatus = '10' AND illClassIsOn='1' WHERE tk.state=1 GROUP BY tk.id )t WHERE 1 = 1 ";
			//名称
			if (!StringUtil.isEmpty(name)) {
				sql += " and (t.docName like '%" + name
						+ "%' or t.hosName like '%" + name
						+ "%' or t.labelNames like '%" + name
						+ "%' or t.title  like '%" + name + "%')";
			}
			//id
			if (!StringUtil.isEmpty(id)) {
				sql += " and t.typeId ='" + id + "'";
			}
			// sql+=" UNION SELECT t.*, CASE WHEN num = 1 THEN FROM_UNIXTIME( t.createTime, '%Y-%m-%d %H:%i' ) ELSE FROM_UNIXTIME(t.createTime, '%H:%i') END timesapp FROM (SELECT 2 typesearch, tk.id, tk.doctorId, tk.typeId, tk.title, tk.content content, tk.isHot, tk.sort, tk.actualPointNum + tk.pointNum AS pointNum, tk.createTime, tk.state, tk.readNum, tk.goodsIds, tk.publisher, tk.sendId, tk.voiceUrl, tk.videoUrl, tk.signUrl, tk.imgUrl, tk.videoShowUrl, tk.top, tk.type, GROUP_CONCAT(CONCAT_WS(',',el.evaLabelName,d.name,ic.illClassName))labelNames, td.docUrl, i.infirmaryName AS hosName, td.docName, p. NAME AS docPosition, ( tk.actualCollectNum + tk.collectNum )collectNum, UNIX_TIMESTAMP(NOW())- tk.createTime > 24 * 60 * 60 AS num, tk.actualShareNum AS shareNum FROM t_konwledge tk INNER JOIN t_doctor td ON tk.doctorId = td.doctorId LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId LEFT JOIN t_position p ON p.id = td.docPositionId LEFT JOIN t_know_label kl ON kl.knowId=tk.id LEFT JOIN t_evalabel el force index(PRIMARY) ON el.evaLabelId=kl.labelId AND el.state=1 AND el.type=3 LEFT JOIN t_diagnostic d ON d.id=kl.labelId AND d.state=1 LEFT JOIN t_illness_class ic ON ic.illClassId=kl.labelId AND illClassStatus = '10' AND illClassIsOn='1' WHERE tk.isHot = 1 AND tk.state=1 GROUP BY tk.id )t )t ORDER BY t.typesearch ASC, t.createTime DESC ";
			sql += ")t ORDER BY t.createTime DESC ";
			if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
				sql += " limit " + (page - 1) * row + "," + row;
			}
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.warn(e);
		}
		return list;
	}
	//取消置顶
	@Override
	public Integer updateKnowUnTop(String id) {
		// TODO Auto-generated method stub
		String sql = "UPDATE t_konwledge SET top=1 WHERE doctorId=? ";
		Integer i = 0;
		try {
			i = jdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 通过Id查询文章详情
	 */
	@Override
	public Map<String, Object> queryKnowArticle(String id, String doctorId) {
		// TODO Auto-generated method stub
		String sql = " select id,doctorId,typeId,title,content,isHot,goodsIds,voiceUrl,videoUrl,imgUrl,videoShowUrl,type,original,signUrl, "
				+ " (SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME	FROM t_evalabel el WHERE type = 3 AND state = 1"
				+ " UNION SELECT id,NAME FROM t_diagnostic WHERE	state = 1 "
				+ " UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1' ) t WHERE FIND_IN_SET(t.id, labelIds)) labelNames,"
				+ " (SELECT GROUP_CONCAT(t.id) FROM (SELECT evaLabelId id,evaLabelName NAME	FROM t_evalabel el WHERE type = 3 AND state = 1"
				+ " UNION SELECT id,NAME FROM t_diagnostic WHERE	state = 1 "
				+ " UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1' ) t WHERE FIND_IN_SET(t.id, labelIds)) labelIds"
				+ " from t_konwledge where id=?";
		try {
			return jdbcTemplate.queryForMap(sql, new Object[] { id });
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 编辑文章
	 */
	@Override
	public Integer editKnowArticle(Knowledge knowledge) {
		// TODO Auto-generated method stub
		String sql = "update t_konwledge set title=?,content=?,labelIds=?,goodsIds=?,state=?  where id=? and doctorId=? ";
		String sqlrecord = " insert into t_knowledge_opera_record (knowId,operaId,operaNote,operaTime,state) values (?,?,?,?,?)";
		try {
			int i = jdbcTemplate.update(sql,
					new Object[] { knowledge.getTitle(),
							knowledge.getContent(), knowledge.getLabelIds(),
							knowledge.getGoodsIds(), knowledge.getState(),
							knowledge.getId(), knowledge.getDoctorId() });
			jdbcTemplate.update(sqlrecord, new Object[] { knowledge.getId(),
					knowledge.getSendId(), "编辑", Util.queryNowTime(), 1 });
			return i;
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过名字查询商品
	 */
	@Override
	public List<Map<String, Object>> queryGoods(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT g.id,g.picture,g.name,"
				+ " IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice"
				+ " FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id "
				+ "WHERE NAME LIKE '%" + name + "%'";

		sql += " GROUP BY g.id";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);

		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return list;
	}

	/**
	 * 查询收藏的文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListKeep(String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询关注的文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListFoll(String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		String sql = " SELECT t.*,"
				// +
				// " CASE WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp,IF(t.followId IS NULL,1,2) AS follow,IF(t.praiseId IS NULL,1,2) praise,IF(t.collectId IS NULL,1,2) collect "
				+ " CASE WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp "
				+ " FROM "
				+ " (SELECT tk.labelIds,tuk.createTime follTime,tk.id,tk.doctorId,tk.typeId,tk.title,fnStripTags(tk.content) content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,"
				+ " tk.videoShowUrl,tk.top,tk.type,"
				// +" (SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 "
				// +
				// " UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1' ) t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
				+ " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,"
				// +" ((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,"
				+ " (tk.actualCollectNum+tk.collectNum) collectNum,"
				+ " UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId,tuk.type keepType,FROM_UNIXTIME(tk.createTime) AS times,"
				// +
				// " (select count(1) from t_konw_eva_reply tker where tker.knowId=tk.id and tker.pid='0' and (tker.state = 1 OR tker.replyId='"+memberId+"')) as replyNum,"
				// +
				// " (select count(1) from t_user_share tus where tus.goodsId = tk.id and tus.type=2) as shareNum,"
				+ " tk.actualShareNum as shareNum,"
				// +
				// " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id and (tker.state=1 or tker.replyId='"+memberId+"') and tker.pid='0') as plNum,"
				// +
				// " ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum,"
				+ " (tk.actualPointNum+tk.pointNum) as pointNum"
				// +
				// " (select COUNT(1) from t_user_keep where type=3 and goodsId=tk.doctorId and memberId='"+memberId+"' group by goodsId) followId,"
				// +
				// " (select COUNT(1) from t_user_praise where type=2 and goodsId=tk.id and memberId='"+memberId+"' group by goodsId) praiseId,"
				// +
				// " (select COUNT(1) from t_user_keep where memberId='"+memberId+"' and goodsId=tk.id group by goodsId) collectId "
				+ " FROM "
				+ " t_konwledge tk inner JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ "	LEFT JOIN t_user_keep tuk ON td.doctorId  =tuk.goodsId "
				+ "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ "	LEFT JOIN t_position p ON p.id = td.docPositionId  where tuk.memberId='"
				+ memberId + "' and tk.state=1)t  ";

		sql += " ORDER BY t.follTime desc,t.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询热门文章
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListHot(String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		/*
		 * String sql = " SELECT t.*," +
		 * " CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp,IF(t.followId IS NULL,1,2) AS follow,IF(t.praiseId IS NULL,1,2) praise,IF(t.collectId IS NULL,1,2) collect "
		 * + " FROM " +
		 * " (SELECT tk.id,tk.doctorId,tk.typeId,tk.title,fnStripTags(tk.content) content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,"
		 * +
		 * " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
		 * +
		 * " FROM_UNIXTIME(tk.createTime) AS times,(SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  "
		 * +
		 * " UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1') t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
		 * +
		 * " (select count(*) from t_konw_eva_reply tker where tker.knowId=tk.id and tker.pid='0' and (tker.state = 1 OR tker.replyId='"
		 * +memberId+"')) as replyNum," +
		 * " (select count(1) from t_user_share tus where tus.goodsId = tk.id and tus.type=2) as shareNum,"
		 * +
		 * " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id and (tker.state=1 or tker.replyId='"
		 * +memberId+"') and tker.pid='0') as plNum," +
		 * " ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum,"
		 * +
		 * " (select count(1) from t_user_keep where type=3 and goodsId=tk.doctorId and memberId='"
		 * +memberId+"' group by goodsId) followId," +
		 * " (select count(1) from t_user_praise where type=2 and goodsId=tk.id and memberId='"
		 * +memberId+"' group by goodsId) praiseId," +
		 * " (select count(1) from t_user_keep where memberId='"
		 * +memberId+"' and goodsId=tk.id  group by goodsId) collectId " +
		 * " FROM " +
		 * " t_konwledge tk inner JOIN t_doctor td ON tk.doctorId = td.doctorId"
		 * + "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId " +
		 * "	LEFT JOIN t_position p ON p.id = td.docPositionId GROUP BY tk.id)t WHERE 1=1 and t.state=1 and t.isHot=1  "
		 * ;
		 * 
		 * sql +=
		 * " group by t.id ORDER BY t.sort desc,t.pointNum DESC,t.collectNum desc,t.shareNum desc,convert(t.title using gbk) ASC "
		 * ;
		 */
		String sql = "SELECT t.*,CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp FROM "
				+ " (SELECT tk.labelIds,tk.id,tk.doctorId,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,"
				+ " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,FROM_UNIXTIME(tk.createTime) AS times,tk.collectNum+tk.actualCollectNum collectNum,tk.pointNum+tk.actualPointNum pointNum,tk.actualShareNum shareNum"
				+ " FROM "
				+ " t_konwledge tk inner JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ " LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ " LEFT JOIN t_position p ON p.id = td.docPositionId  WHERE 1=1 and tk.state=1 and tk.isHot=1)t "
				+ " group by t.id ORDER BY t.sort desc,t.pointNum DESC,t.collectNum desc,t.shareNum desc,convert(t.title using gbk) ASC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询收藏的文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListColl(String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		String sql = " SELECT t.*,"
				// +
				// " CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp,IF(t.followId IS NULL,1,2) AS follow,IF(t.praiseId IS NULL,1,2) praise,IF(t.collectId IS NULL,1,2) collect "
				+ " CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp "
				+ " FROM "
				+ " (SELECT tk.labelIds,tk.sendTime,tk.id,tk.doctorId,tk.typeId,tk.title,tk.content content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,"
				+ " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,"
				// +" ((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,"
				+ " (tk.actualCollectNum+tk.collectNum) collectNum,"
				+ " UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId,tuk.type keepType,FROM_UNIXTIME(tk.createTime) AS times,"
				// +" (SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 "
				// +
				// " UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1') t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
				// +
				// " (select count(*) from t_konw_eva_reply tker where tker.knowId=tk.id and tker.pid='0' and (tker.state = 1 OR tker.replyId='"+memberId+"')) as replyNum,"
				// +
				// " (select count(1) from t_user_share tus where tus.goodsId = tk.id and tus.type=2) as shareNum,"
				+ " tk.actualShareNum as shareNum,"
				// +
				// " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id and (tker.state=1 or tker.replyId='"+memberId+"') and tker.pid='0') as plNum,"
				// +
				// " ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum,"
				+ " (tk.actualPointNum+tk.pointNum) as pointNum,"
				// +
				// " (select COUNT(1) from t_user_keep where type=3 and goodsId=tk.doctorId and memberId='"+memberId+"' GROUP BY goodsId) followId,"
				// +
				// " (select COUNT(1) from t_user_praise where type=2 and goodsId=tk.id and memberId='"+memberId+"' GROUP BY goodsId) praiseId,"
				// +
				// " (select COUNT(1) from t_user_keep where memberId='"+memberId+"' and goodsId=tk.id GROUP BY goodsId) collectId,"
				+ " (select createTime from t_user_keep where memberId='"
				+ memberId
				+ "' and goodsId=tk.id GROUP BY goodsId) collectTime "
				+ " FROM "
				+ " t_konwledge tk inner JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ "	LEFT JOIN t_user_keep tuk ON tk.id  =tuk.goodsId "
				+ "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ "	LEFT JOIN t_position p ON p.id = td.docPositionId where tuk.memberId='"
				+ memberId + "' GROUP BY tk.id)t WHERE 1=1 and t.state=1 ";
		//分组、排序
		sql += " group by t.id ORDER BY t.collectTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 通过ID查询商品
	 */
	@Override
	public List<Map<String, Object>> queryGoodsByIds(String goodsId) {
		String sql = "SELECT s.isProprietary,g.id,g.picture,g.remark,g.name,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id inner join t_supplier s on s.id=g.supplierId where g.id in ( ";
		if (!StringUtil.isEmpty(goodsId)) {
			if (goodsId.indexOf(",") > -1) {
				String[] ids = goodsId.split(",");
				for (int i = 0; i < ids.length; i++) {
					if (i == ids.length - 1) {
						sql += "'" + ids[i] + "'";
					} else {
						sql += "'" + ids[i] + "',";
					}
				}
			} else {
				sql += "'" + goodsId + "'";
			}
		}
		sql += " ) and g.state=1 and s.state=1 GROUP BY g.id ";
		try {
			return jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 查询所有的文章
	 */
	@Override
	public Map<String, Object> queryAllKnowledge(String typeId, Integer type,
			String labelId, String title, String memberId, Integer page,
			Integer row) {
		// TODO Auto-generated method stub
		String sql = "SELECT "
				+ "tk.id,"
				+ "tk.title,"
				+ "tk.createTime,"
				+ "fnStripTags(tk.content) content,"
				+ "(SELECT GROUP_CONCAT(t. NAME) "
				+ "FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 "
				+ "	UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1   "
				+ "	UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1') t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
				+ "tk.sendNum," + "(" + "(" + "SELECT " + "count(1) " + "FROM "
				+ "t_konwledge " + "WHERE " + "flag = tk.flag " + ") - 1 "
				+ ") sendedNum " + "FROM " + "t_konwledge tk " + "WHERE "
				+ "tk.state = 1 " + "AND  "
				+ "(tk.doctorId IS NULL OR tk.doctorId='')"
				+ " AND tk.flag not in (" + "SELECT " + "flag " + "FROM "
				+ "t_konwledge " + "WHERE " + "doctorId = '" + memberId + "' "
				+ "AND flag = tk.id " + ") ";
		//类型id
		if (!StringUtil.isEmpty(typeId)) {
			sql += " AND tk.typeId ='" + typeId + "' ";
		}
		//类型
		if (type != null) {
			sql += " AND tk.type = " + type;
		}
		//标签id
		if (!StringUtil.isEmpty(labelId)) {
			sql += " AND FIND_IN_SET('" + labelId + "',tk.labelIds) ";
		}
		//标题
		if (!StringUtil.isEmpty(title)) {
			sql += " AND tk.title like '%" + title + "%' ";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer count = jdbcTemplate.queryForObject(
					"select count(1) from (" + sql + ") t", Integer.class);
			sql += "  ORDER BY tk.createTime DESC ";
			if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
				sql += " limit " + (page - 1) * row + "," + row;
			}
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			map.put("count", count);
			map.put("list", list);
		} catch (DataAccessException e) {
			logger.error(e);
		}

		return map;
	}

	/**
	 * 批量发布文章
	 */
	@Override
	public Integer sendKnowledge(String memberId, String ids) {
		// TODO Auto-generated method stub
		Integer i = 0;
		String sql = "insert into t_konwledge(id,doctorId,typeId,title,content,isHot,sort,pointNum,createTime,state,readNum,labelNames,goodsIds,sendId,publisher,voiceUrl,videoUrl,imgUrl,videoShowUrl,type,original,sendNum,flag,sendTime,signUrl,departmentIds,rels,labelIds) "
				+ " SELECT uuid() id,'"
				+ memberId
				+ "' doctorId,typeId,title,content,isHot,sort,pointNum,createTime,state,readNum,labelNames,goodsIds,sendId,publisher,voiceUrl,videoUrl,imgUrl,videoShowUrl,type,original,sendNum,flag,"
				+ Util.queryNowTime()
				+ " sendTime,signUrl,departmentIds,rels,labelIds FROM t_konwledge WHERE id in ("
				+ ids + ")";
		String upSql = "update t_konwledge set id = REPLACE(id,'-','')";
		try {
			i = jdbcTemplate.update(sql);
			jdbcTemplate.update(upSql);
		} catch (DataAccessException e) {
			logger.error(e);
			i = 0;
			throw e;
		}
		return i;
	}

	/**
	 * 查询文章列表
	 */
	@Override
	public List<Map<String, Object>> querySendClass(String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stu
		String sql = "SELECT " + "kt.id, " + "kt.typeName, " + "kt.imgUrl, "
				+ "kt.createTime, " + "( " + "SELECT " + "count(1) " + "FROM "
				+ "t_konwledge tk " + "WHERE " + "tk.state = 1 " + "AND ( "
				+ "tk.doctorId IS NULL " + "OR tk.doctorId = '' " + ") "
				+ "AND tk.flag NOT IN ( " + "SELECT " + "flag " + "FROM "
				+ "t_konwledge " + "WHERE " + "doctorId = '" + memberId + "' "
				+ "AND flag = tk.id " + ") " + "AND tk.typeId = kt.id "
				+ ") knowNum " + "FROM " + "t_knowledge_type kt " + "WHERE "
				+ "kt.state = 1";
		sql += "  ORDER BY kt.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询搜索基础数据
	 */
	@Override
	public List<Map<String, Object>> queryKnowSearchBasicData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = " select * from (SELECT t.id id,t.`name` name,'knowClass' _sign from t_knowledge_class t where 1=1 ORDER BY t.id ASC) e "
					+ " UNION select * from (SELECT d.departId id,d.departName name,'depart' _sign from t_department d where d.state=1 order by d.departSort desc,d.departShort ASC) a "
					/*
					 * +
					 * " UNION select * from (SELECT ill.illClassId id,ill.illClassName name,'illClass' _sign "
					 * +
					 * " from t_illness_class ill INNER JOIN t_department dd on dd.departId=ill.departId where ill.illClassIsOn='1' and ill.illClassStatus='10' ORDER BY ill.isSort ASC,ill.illClassShort ASC) b "
					 */
					+ " UNION select * from (SELECT i.id id,i.`name` name,'diagnostic' _sign from t_diagnostic i where i.flag=1 ORDER BY i.pinyinCode ASC ) c "
					+ " UNION select * from (SELECT t.id id,t.typeName name,'knowType' _sign from t_knowledge_type t where t.state=1 ORDER BY t.createTime DESC) d ";
			list = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 标记为已读
	 */
	@Override
	public Integer markRead(String replyIds) {
		// TODO Auto-generated method stub
		int i = 0;
		String sql = "UPDATE t_konw_eva_reply SET sign_=2 WHERE id in ("
				+ replyIds + ")";
		try {
			i = jdbcTemplate.update(sql);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
			i = 0;
		}
		return i;
	}

	/**
	 * 查询病症标签
	 */
	@Override
	public List<Map<String, Object>> illLabels() {
		// TODO Auto-generated method stub
		String sql = "select illClassId,labelName from t_ill_label where state=1";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询病症
	 */
	@Override
	public List<Map<String, Object>> illnessClasses() {
		// TODO Auto-generated method stub
		String sql = " select illClassId,illClassName,departId from t_illness_class where illClassStatus='10' and illClassIsOn='1'";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		return list;
	}

	/**
	 * 添加文章标签
	 */
	@Override
	public Integer addKnowLabel(Knowledge knowledge) {
		// TODO Auto-generated method stub
		String[] labels = knowledge.getLabelIds().split(",");
		int i = 0;
		if (labels.length > 0) {
			String sql = "insert INTO t_know_label (id,knowId,labelId,createTime) values ";
			for (int j = 0; j < labels.length; j++) {
				sql += "('" + Util.getUUID() + "','" + knowledge.getId()
						+ "','" + labels[j] + "'," + Util.queryNowTime() + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			try {
				i = jdbcTemplate.update(sql);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return i;
	}

	/**
	 * 删除文章标签
	 */
	@Override
	public Integer deleteKnowLabel(Knowledge knowledge) {
		// TODO Auto-generated method stub
		int i = 0;
		String sql = " delete from t_know_label where knowId = '"
				+ knowledge.getId() + "'";
		try {
			i = jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 查询文章详情
	 */
	@Override
	public Map<String, Object> queryKnowledgeInfo(String knowledgeId,
			String lables, String memberId) {
		final Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT ifnull(count(1),0) num,1 type FROM t_konw_eva_reply tker WHERE tker.knowId = ? AND tker.pid = '0' AND (tker.state = 1 OR tker.replyId = ?)"
				+ " union"
				+ " SELECT ifnull(count(1),0) num,2 type  FROM t_user_share tus WHERE tus.goodsId = ? AND tus.type = 2"
				+ " union"
				+ " SELECT ifnull(count(1),0) num,3 type  FROM t_konw_eva_reply tker WHERE tker.knowId = ? AND (tker.state = 1 OR tker.replyId = ?) AND tker.pid = '0'"
				+ " union"
				+ " SELECT ifnull(count(1),0) num,4 type  FROM t_user_praise tup WHERE tup.goodsId = ? AND tup.type = 2"
				+ " union"
				+ " SELECT ifnull(count(1),0) num,5 type  FROM t_user_keep WHERE type = 3 AND goodsId = ? AND memberId = ? GROUP BY goodsId"
				+ " union"
				+ " SELECT ifnull(count(1),0) num,6 type  FROM t_user_praise WHERE type = 2 AND goodsId = ? AND memberId = ? GROUP BY goodsId"
				+ " union"
				+ " SELECT ifnull(count(1),0) num,7 type  FROM t_user_keep WHERE goodsId = ? AND memberId = ?  GROUP BY goodsId"
				+ " union"
				+ " SELECT COUNT(1) num,8 type  FROM t_user_keep tuk WHERE tuk.goodsId = ? AND TYPE = 2"
				+ " union"
				+ " SELECT GROUP_CONCAT(t.NAME) num,9 type  FROM (SELECT evaLabelId AS id, evaLabelName AS NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id, NAME FROM t_diagnostic WHERE state = 1 UNION SELECT illClassId AS id, illClassName AS NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn = '1') t WHERE FIND_IN_SET(t.id,?)";
		try {
			jdbcTemplate.query(sql, new Object[] { knowledgeId, memberId,
					knowledgeId, knowledgeId, memberId, knowledgeId,
					knowledgeId, memberId, knowledgeId, memberId, knowledgeId,
					memberId, knowledgeId, lables }, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Integer type = rs.getInt("type");
					Object value = rs.getObject("num");
					if (type == 1) {
						map.put("replyNum", value);
					} else if (type == 2) {
						map.put("shareNum", value);
					} else if (type == 3) {
						map.put("plNum", value);
					} else if (type == 4) {
						map.put("pointNum", value);
					} else if (type == 5) {
						map.put("follow", value);
					} else if (type == 6) {
						map.put("praise", value);
					} else if (type == 7) {
						map.put("collect", value);
					} else if (type == 8) {
						map.put("collectNum", value);
					} else if (type == 9) {
						map.put("labelNames", value);
					}
				}
			});
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询收藏数
	 */
	@Override
	public Integer queryCollectNum(String knowledgeId) {
		String sql = "SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId = ? AND TYPE = 2";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { knowledgeId },
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 通过ID查询标签名称
	 */
	@Override
	public String queryLabelNames(String lables) {
		String sql = "SELECT GROUP_CONCAT(t.NAME) FROM (SELECT evaLabelId AS id, evaLabelName AS NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id, NAME FROM t_diagnostic WHERE state = 1 UNION SELECT illClassId AS id, illClassName AS NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn = '1') t WHERE FIND_IN_SET(t.id,?)";
		String lableNames = null;
		try {
			lableNames = jdbcTemplate.queryForObject(sql,
					new Object[] { lables }, String.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return lableNames;
	}

	/**
	 * 查询评论数
	 */
	@Override
	public Integer queryReplyNum(String knowledgeId, String memberId) {
		String sql = "SELECT count(1) FROM t_konw_eva_reply tker WHERE tker.knowId = ? AND tker.pid = '0' AND (tker.state = 1 OR tker.replyId = ?)";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { knowledgeId,
					memberId }, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;

	}

	/**
	 * 查询分享数
	 */
	@Override
	public Integer queryShareNum(String knowledgeId) {
		String sql = "SELECT count(1) FROM t_user_share tus WHERE tus.goodsId = ? AND tus.type = 2";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { knowledgeId },
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 查询评论数
	 */
	@Override
	public Integer queryPlNum(String knowledgeId, String memberId) {
		String sql = "SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = ? AND (tker.state = 1 OR tker.replyId = ?) AND tker.pid = '0'";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { knowledgeId,
					memberId }, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}
	/**
	 * 查询点赞数
	 */
	@Override
	public Integer queryPointNum(String knowledgeId) {
		String sql = "SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = ? AND tup.type = 2";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { knowledgeId },
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}
	/**
	 * 查询是否关注
	 */
	@Override
	public Integer queryFollow(String doctorId, String memberId) {
		String sql = "SELECT count(1) FROM t_user_keep WHERE type = 3 AND goodsId = ? AND memberId = ? GROUP BY goodsId";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { doctorId,
					memberId }, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 查询点赞数
	 */
	@Override
	public Integer querypPraiseId(String knowledgeId, String memberId) {
		String sql = "SELECT count(1) FROM t_user_praise WHERE type = 2 AND goodsId = ? AND memberId = ? GROUP BY goodsId";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { knowledgeId,
					memberId }, Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 查询是否收藏
	 */
	@Override
	public Map<String, Object> querypCollectId(String knowledgeId,
			String memberId) {
		String sql = "SELECT ifnull(count(1),0) num,ifnull(createTime,'') FROM t_user_keep WHERE goodsId = ? AND memberId = ? and type=2 GROUP BY goodsId";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = jdbcTemplate.queryForMap(sql, new Object[] { knowledgeId,
					memberId });
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return map;
	}

}