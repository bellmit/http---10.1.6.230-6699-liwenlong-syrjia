package cn.syrjia.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.KnowledgeDao;
import cn.syrjia.entity.Knowledge;
import cn.syrjia.util.Util;
import cn.syrjia.wxPay.wxPay.util.StringUtil;

@Repository("knowledgeDao")
public class KnowledgeDaoImpl extends BaseDaoImpl implements KnowledgeDao{
	
	// 日志
	private Logger logger = LogManager.getLogger(KnowledgeDaoImpl.class);

	/**
	 * 通过ID查询文章
	 */
	@Override
	public Map<String, Object> queryKnowledgeById(String knowledgeId) {
		String sql="SELECT k.id,k.name,k.remark,k.riskPath,k.imageUrl,k.videoPath,k.state,k.rank,k.describes,k.operationTime,k.createTime,k.isVideo,(select count(1) from t_knowledge_reply where knowledgeId=k.id and state=2) replyNum,(select count(distinct memberId) from t_knowledge_member where knowledgeId=k.id) readNum FROM t_knowledge k WHERE k.state=1 AND k.id=?";
		try {
			//执行查询
			return jdbcTemplate.queryForMap(sql,new Object[]{knowledgeId});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledges(Knowledge knowledge,Integer page,Integer row) {
		String sql="SELECT k.id,k.name,k.remark,k.riskPath,k.imageUrl,k.isVideo,k.videoPath,k.state,k.rank,k.describes,k.operationTime,k.createTime,(select count(1) from t_knowledge_reply where knowledgeId=k.id and state=2) replyNum,(select count(distinct memberId) from t_knowledge_member where knowledgeId=k.id) readNum FROM t_knowledge k  WHERE k.state=1";
		//拼接sql，名称
		if(!StringUtil.isEmpty(knowledge.getName())){
			sql+=" and name like '"+knowledge.getName()+"%'";
		}
		//是否视频
		if(!StringUtil.isEmpty(knowledge.getIsVideo())){
			sql+=" and isVideo = "+knowledge.getIsVideo();
		}
		//排序
		sql+="  ORDER BY createTime DESC ";
		//分页
		if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeList(String openId,Knowledge knowledge,String type,
			Integer page, Integer row) {
		//拼接sql
		String sql="SELECT TIMESTAMPDIFF( HOUR,t.times,NOW() ) AS xiaoshi,t.* FROM (SELECT "+
					 " tk.id,tk.doctorId,tk.typeId,tk.title,tk.content,tk.isHot,tk.sort,tk.pointNum,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,"+
					 " td.docUrl,td.hosName,td.docName,td.docPosition,(SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.id=tk.id AND TYPE=2) replyNum,FROM_UNIXTIME(tk.createTime) AS times "+
					" FROM "+
					 " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"+
					 " LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId)t where 1=1";
		//类型判断
		if(type.equals("1")){
			sql +="";
		}
		if(type.equals("2")){
			sql +=" and t.isHot=1 ";
		}
		if(type.equals("3")){
			sql +=" and tuk.memberId="+openId;
		}
		//名称
		if(!StringUtil.isEmpty(knowledge.getName())){
			sql+=" and name like '"+knowledge.getName()+"%'";
		}
		//是否视频
		if(!StringUtil.isEmpty(knowledge.getIsVideo())){
			sql+=" and isVideo = "+knowledge.getIsVideo();
		}
		//排序
		sql+="  ORDER BY t.createTime DESC ";
		//分页
		if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行查询
			list = jdbcTemplate.queryForList(sql);
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
		String sql="SELECT id,typeName,imgUrl,state,createTime,updateTime , (SELECT COUNT(*) FROM t_konwledge  WHERE typeId=id) num FROM t_knowledge_type ";
		//名称
		if(!StringUtil.isEmpty(knowledge.getName())){
			sql+=" and name like '"+knowledge.getName()+"%'";
		}
		//是否视频
		if(!StringUtil.isEmpty(knowledge.getIsVideo())){
			sql+=" and isVideo = "+knowledge.getIsVideo();
		}
		//排序
		sql+="  ORDER BY createTime DESC ";
		//分页
		if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行sql
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
	public Map<String, Object> queryCircleDetail(String id) {
		// TODO Auto-generated method stub
		String sql="SELECT "+
					"  tk.id,tk.doctorId,tk.typeId,tk.title,tk.content,tk.isHot,tk.sort,tk.pointNum,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,"+
					"  tk.publisher,tk.sendId,tk.updateTime,"+
					"  td.hosName,td.docName,td.docUrl,td.docPosition"+
					",		 (SELECT	group_concat( ill.illClassName ) FROM t_illness_class ill WHERE	ill.illClassId IN ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = td.doctorId )	) illClassName"+
					" FROM "+
					 " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId=td.doctorId where tk.id=?";
		try {
			//执行sql
			return jdbcTemplate.queryForMap(sql,new Object[]{id});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过ID查询商品
	 */
	@Override
	public Map<String, Object> queryGoodsById(String id) {
		// TODO Auto-generated method stub
		String sql="SELECT g.id,g.picture,g.remark,g.name,NULL keepId,g.description"+
					" ,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock"+ 
					" FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id WHERE  g.id =?";
		try {
			//执行sql
			return jdbcTemplate.queryForMap(sql,new Object[]{id});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * 通过名字查询文章列表
	 */
	@Override
	public List<Map<String, Object>> queryKnowledgeListByName(String name,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		String sql="SELECT "+
				 " tk.id,tk.doctorId,tk.typeId,tk.title,tk.content,tk.isHot,tk.sort,tk.pointNum,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,"+
				 " td.docUrl,td.hosName,td.docName,td.docPosition,(SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.id=tk.id AND TYPE=2) replyNum "+
				 " FROM "+
				 " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"+
				 " LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId where 1=1 ";	
		//名称
		if(!StringUtil.isEmpty(name)){
			sql+=" and (td.docName like '"+name+"%' or td.hosName like '%"+name+"'%)";
		}
		//排序
		sql+="  ORDER BY tk.createTime DESC ";
		if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
			sql+=" limit "+(page-1)*row+","+row;
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			//执行sql
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}

	/**
	 * 查询收藏的文章
	 */
	@Override
	public Integer knowledgeCollect(String openid, String id) {
		// TODO Auto-generated method stub
		String sql="INSERT INTO t_user_keep (id,memberId,goodsId,createTime,TYPE) VALUES (?,?,?,?,?)";
		Integer i=0;
		try {
			//执行更新
			i = jdbcTemplate.update(sql,new Object[]{Util.getUUID(),openid,id,Util.queryNowTime(),2});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return i;
	}

	/**
	 * 通过ID查询文章
	 */
	@Override
	public Map<String, Object> selectKnowledgeById(String id) {
		// TODO Auto-generated method stub
		String sql="SELECT id,doctorId,typeId,title,content,isHot,sort,pointNum,createTime,state,readNum,labelNames,goodsIds,publisher,sendId "+
					" FROM t_konwledge WHERE id=？";
	try {
		//执行查询
		return jdbcTemplate.queryForMap(sql,new Object[]{id});
	} catch (DataAccessException e) {
		logger.warn(e);
		return null;
	}
	}
}
