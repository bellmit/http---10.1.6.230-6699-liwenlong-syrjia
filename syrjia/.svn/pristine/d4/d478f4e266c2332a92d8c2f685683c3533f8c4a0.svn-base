package cn.syrjia.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.CenterCollectDao;
import cn.syrjia.util.StringUtil;

@Repository("centerCollectDao")
public class CenterCollectDaoImpl extends BaseDaoImpl implements
		CenterCollectDao {

	// 日志
	private Logger logger = LogManager.getLogger(CenterCollectDaoImpl.class);

	/**
	 * 查询关注的医生
	 */
	@Override
	public List<Map<String, Object>> queryKeepDoctor(String searchSort, String memberId,
			Integer page, Integer row,String openId) {
		List<Map<String, Object>> doclist = new ArrayList<Map<String, Object>>();
		try {
			//拼接sql
			String sql = "SELECT d.*,infir.infirmaryName, de.departName,(select GROUP_CONCAT(ff.illClassName) from (SELECT ill.illClassName,miu.doctorId FROM t_middle_util miu INNER JOIN t_illness_class ill ON ill.illClassId=miu.illClassId WHERE  miu.type=1)ff where ff.doctorId=d.doctorId LIMIT 0,2) illClassName, (SELECT COUNT(1) FROM t_doctor_order o "
					+ " WHERE o.doctorId = d.doctorId AND o. STATUS = '10' AND ( o.paymentStatus = '2' OR o.paymentStatus = '5' )) buyNum, "
					+ " (SELECT COUNT(1) evaCnt FROM t_evaluate e WHERE e.state='10' and e.goodsId = d.doctorId ) evaCnt, "
					+ " (SELECT COUNT(1) evaGoodCnt FROM t_evaluate e1 WHERE e1.state='10' and e1.evaluateLevel>5 and e1.goodsId = d.doctorId ) evaGoodCnt, "
					+ " ( SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames "
					+ " FROM t_doctor d left JOIN t_department de on de.departId =d.infirDepartId "
					+ " inner JOIN t_infirmary infir on infir.infirmaryId =d.infirmaryId and infir.state=1 "
					/*+ " left join (SELECT COUNT(1) evaCnt,e.doctorId FROM t_evaluate e WHERE e.evaStaus='10' ) docEva on docEva.doctorId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaGoodCnt,e1.doctorId FROM t_evaluate e1 WHERE e1.evaStaus='10' and e1.evaluateLevel>7 ) doCGoodCnt on doCGoodCnt.doctorId = d.doctorId "*/
					+ " where d.docIsOn='1' and d.docStatus='10' ";
			/*if (!StringUtil.isEmpty(doc.getIsRecommended())) {
				sql += " and d.isRecommended='" + doc.getIsRecommended() + "'";
			}
			if (!StringUtil.isEmpty(doc.getDepartId())) {
				sql += " and d.departId='" + doc.getDepartId() + "'";
			}
			if (!StringUtil.isEmpty(doc.getHospitalId())) {
				sql += " and d.hospitalId='" + doc.getHospitalId() + "'";
			}
			if (!StringUtil.isEmpty(memberId)) {
				sql += " and d.doctorId in(select mu.doctorId from t_middle_util mu where mu.type='4' and mu.systemId='"
						+ doc.getSystemId() + "')";
			}*/
			
			if (!StringUtil.isEmpty(memberId)) {
				sql += " and d.doctorId in(SELECT u.goodsId from t_user_keep u where u.type='3' and u.memberId='"
						+ memberId + "')";
			}
			/*
			if (!StringUtil.isEmpty(doc.getHosName())) {
				sql += " and d.hosName = '" + doc.getHosName() + "'";
			}
			
			if (!StringUtil.isEmpty(doc.getDocPosition())
					&& !"职称".equals(doc.getDocPosition())) {
				sql += " and d.docPosition = '" + doc.getDocPosition() + "'";
			}
			
			if (!StringUtil.isEmpty(doc.getDocPositionId())
					&& !"0".equals(doc.getDocPositionId())) {
				sql += " and d.docPositionId = '" + doc.getDocPositionId() + "'";
			}
			
			if (!StringUtil.isEmpty(doc.getDocName())) {
				sql += " and d.docName like '%" + doc.getDocName() + "%'";
			}*/
			//拼接排序
			if (!StringUtil.isEmpty(searchSort)) {
				if ("0".equals(searchSort)) {// 好评度
					sql += " order by evaGoodCnt desc ";
				} else if ("1".equals(searchSort)) {// 接单数
					sql += " order by buyNum desc ";
				} else if ("2".equals(searchSort)) {// 职称
					sql += " order by titleSort desc ";
				} else if ("3".equals(searchSort)) {// 收费高低
					sql += " order by d.docPrice desc ";
				} else {
					sql += " order by d.createTime ASC ";
				}
			} else {
				sql += " order by buyNum desc ";
			}
//			sql += " LIMIT " + (page - 1) + "," + row;
			System.out.println(sql);
			//执行查询
			doclist = super.queryBysqlList(sql, null);
		} catch (NumberFormatException e) {
			logger.info(e+"异常信息");
		}
		return doclist;
	}

	/**
	 * 我的收藏文章
	 */
	@Override
	public List<Map<String, Object>> queryKeepKonwledge(String searchSort,
			String memberId, Integer page, Integer row, String openId) {
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		memberId="";
		try {
			//拼接sql
			String sql = " SELECT TIMESTAMPDIFF( HOUR,t.times,NOW() ) AS xiaoshi,t.*, tukk.id isKeepDoc,( SELECT COUNT(1) FROM t_user_keep tukp WHERE tukp.goodsId=t.id ) keepNum FROM (SELECT" 
					  +" tk.id,tk.doctorId,tk.typeId,tk.title,tk.content,tk.isHot,tk.sort,tk.pointNum,tk.createTime,tk.state,tk.readNum,tk.labelNames,tk.goodsIds,tk.publisher,tk.sendId,"
					  +" td.docUrl,td.hosName,td.docName,td.docPosition,(SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2) replyNum,FROM_UNIXTIME(tk.createTime) AS times ,"
					  +" tuk.id AS tid,tuk.memberId,tuk.goodsId FROM "
					  +" t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"
					  +" LEFT JOIN t_user_keep tuk ON tk.id =tuk.goodsId)t  LEFT JOIN t_user_keep tukk ON tukk.goodsId = t.doctorId ";
			
			if(!StringUtils.isEmpty(memberId)){
				sql+=" AND tukk.memberId = '"+memberId+"' where t.memberId = '"+memberId+"' ";
			}
			System.out.println(sql);
			//执行查询
			listMaps = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			logger.info(e+"异常信息");
		}
		
		return listMaps;
	}


	/**
	 * 收藏文章中关注医生
	 */
	@Override
	public Map<String, Object> addKeepDoc(String memberId, String doctorId) {
		try {
			String sql = "INSERT INTO t_user_keep (id,memberId,goodsId,createTime,TYPE) VALUES (?,?,?,?,?)";
		} catch (Exception e) {
			logger.info(e+"异常信息");
		}
		return null;
	}

	/**
	 * 获取就诊医生列表
	 */
	@Override
	public List<Map<String, Object>> queryVisDoctor(String memberId) {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(memberId)){
			try {
				String jzSql = " SELECT d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount, "
						+ " GROUP_CONCAT(CONCAT_WS(';',ifnull(ds.fisrtTwGhMoney, ' '),ifnull(ds.fisrtPhoneGhMoney, ' '),ifnull(ds.twZxMoney, ' '),ifnull(ds.phoneZxMoney, ' '),1,1,ds.twZxCount,ds.phoneZxCount)) moneys, "
						+ " ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate, "
						+ " (SELECT group_concat(ill.illClassName)	FROM t_illness_class ill WHERE ill.illClassId in ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId  ) ) illClassNames, "
						+ " GROUP_CONCAT(CONCAT_WS(';',o.orderNo,ifnull(o.createTime, ' '),ifnull(p.name, ' '),s.`name`,o.orderStatus,d.doctorId )) jzLists,1 as type "
						+ " from t_order o "
						+ " INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo "
						+ " INNER JOIN t_doctor d on d.doctorId = os.doctorId  "
						+ " INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
						+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
						+ " INNER JOIN t_doctor_set ds ON ds.doctorId = d.doctorId "
						+ " INNER JOIN t_server_type s on s.id = o.orderType "
						+ " INNER JOIN t_patient_data p on p.id = o.patientId "
						+ " LEFT JOIN (SELECT COUNT(1) buyNum, os.doctorId FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo WHERE o.state = 1 AND o.paymentStatus IN (2, 5) "
						+ " AND o.orderType IN (4, 5, 6, 7, 8, 9) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaGoodCnt ,e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = d.doctorId "
						+ " where o.state = 1 AND o.paymentStatus in(2,5) and o.orderType in(4,5,6,7,8,9) and o.memberId='"
						+ memberId
						+ "' GROUP BY d.doctorId ORDER BY o.createTime DESC ";
			} catch (Exception e) {
				logger.info(e+"异常信息");
			}
		}
		return null;
	}

	/**
	 * 获取关注医生列表
	 */
	@Override
	public List<Map<String, Object>> queryKeepDoctor(String memberId) {
		if(!StringUtils.isEmpty(memberId)){
			try {
				String gzSql = "SELECT d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount, "
						+ " GROUP_CONCAT(CONCAT_WS(';',ifnull(ds.fisrtTwGhMoney, ' '),ifnull(ds.fisrtPhoneGhMoney, ' '),ifnull(ds.twZxMoney, ' '),ifnull(ds.phoneZxMoney, ' '),1,1,ds.twZxCount,ds.phoneZxCount)) moneys, "
						+ " ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate, "
						+ " (SELECT group_concat(ill.illClassName)	FROM t_illness_class ill WHERE ill.illClassId in ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId  ) ) illClassNames, "
						+ " null as jzLists,2 as type FROM t_doctor d INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
						+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
						+ " INNER JOIN t_doctor_set ds ON ds.doctorId = d.doctorId "
						+ " INNER JOIN t_user_keep uk on uk.goodsId = d.doctorId "
						+ " LEFT JOIN (SELECT COUNT(1) buyNum, os.doctorId FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo WHERE o.state = 1 AND o.paymentStatus IN (2, 5) "
						+ " AND o.orderType IN (4, 5, 6, 7, 8, 9) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaGoodCnt ,e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = d.doctorId "
						+ " WHERE d.docIsOn = '1' AND d.docStatus = '10' AND infir.state = 1 and uk.type=3 and uk.memberId='"
						+ memberId
						+ "'  GROUP BY d.doctorId ORDER BY uk.createTime DESC ";
			} catch (Exception e) {
				logger.info(e+"异常信息");
			}
		}
		return null;
	}

	/**
	 * 查询医生GZ
	 */
	@Override
	public List<Map<String, Object>> queryDoctorGZ(String name,String memberId,
			Integer page, Integer row, String openId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		try {
			//拼接sql
			String gzSql = "SELECT d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount, "
					+ " GROUP_CONCAT(CONCAT_WS(';',ifnull(ds.fisrtTwGhMoney, ' '),ifnull(ds.fisrtPhoneGhMoney, ' '),ifnull(ds.twZxMoney, ' '),ifnull(ds.phoneZxMoney, ' '),1,1,ds.twZxCount,ds.phoneZxCount)) moneys, "
					+ " ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate, "
					+ " (SELECT group_concat(ill.illClassName)	FROM t_illness_class ill WHERE ill.illClassId in ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId  ) ) illClassNames, "
					+ " null as jzLists,2 as type FROM t_doctor d INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
					+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
					+ " INNER JOIN t_doctor_set ds ON ds.doctorId = d.doctorId "
					+ " INNER JOIN t_user_keep uk on uk.goodsId = d.doctorId "
					+ " LEFT JOIN (SELECT COUNT(1) buyNum, os.doctorId FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo WHERE o.state = 1 AND o.paymentStatus IN (2, 5) "
					+ " AND o.orderType IN (4, 5, 6, 7, 8, 9) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
					+ " LEFT JOIN ( SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = d.doctorId "
					+ " LEFT JOIN ( SELECT COUNT(1) evaGoodCnt ,e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = d.doctorId "
					+ " WHERE d.docIsOn = '1' AND d.docStatus = '10' AND infir.state = 1 and uk.type=3 and uk.memberId='"
					+ memberId + "'";
			if(!StringUtil.isEmpty(name)){
				gzSql+=" and d.docName like '%"+name+"%'";
			}
			gzSql+=" GROUP BY d.doctorId ORDER BY uk.createTime DESC ";		  
			if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
				gzSql+=" limit "+(page-1)*row+","+row;
			}
			//System.out.println(sql);
			//执行查询
			listMaps = jdbcTemplate.queryForList(gzSql);
		} catch (Exception e) {
			logger.info(e+"异常信息");
		}
		
		return listMaps;
	}
	
	/**
	 * 查询医生JZ
	 */
	public List<Map<String, Object>> queryDoctorJZ(String name,String memberId,
			Integer page, Integer row, String openId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		try {
			//拼接sql
			String jzSql = " SELECT d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount, "
					+ " GROUP_CONCAT(CONCAT_WS(';',ifnull(ds.fisrtTwGhMoney, ' '),ifnull(ds.fisrtPhoneGhMoney, ' '),ifnull(ds.twZxMoney, ' '),ifnull(ds.phoneZxMoney, ' '),1,1,ds.twZxCount,ds.phoneZxCount)) moneys, "
					+ " ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate, "
					+ " (SELECT group_concat(ill.illClassName)	FROM t_illness_class ill WHERE ill.illClassId in ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId  ) ) illClassNames, "
					+ " GROUP_CONCAT(CONCAT_WS(';',o.orderNo,ifnull(o.createTime, ' '),ifnull(p.name, ' '),s.`name`,o.orderStatus,d.doctorId )) jzLists,1 as type "
					+ " from t_order o "
					+ " INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo "
					+ " INNER JOIN t_doctor d on d.doctorId = os.doctorId  "
					+ " INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
					+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
					+ " INNER JOIN t_doctor_set ds ON ds.doctorId = d.doctorId "
					+ " INNER JOIN t_server_type s on s.id = o.orderType "
					+ " INNER JOIN t_patient_data p on p.id = o.patientId "
					+ " LEFT JOIN (SELECT COUNT(1) buyNum, os.doctorId FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo WHERE o.state = 1 AND o.paymentStatus IN (2, 5) "
					+ " AND o.orderType IN (4, 5, 6, 7, 8, 9) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
					+ " LEFT JOIN ( SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = d.doctorId "
					+ " LEFT JOIN ( SELECT COUNT(1) evaGoodCnt ,e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = d.doctorId "
					+ " where o.state = 1 AND o.paymentStatus in(2,5) and o.orderType in(4,5,6,7,8,9) and o.memberId='"
					+ memberId + "'";
			if(!StringUtil.isEmpty(name)){
				jzSql+=" and  d.docName like '%"+name+"%'";
			}
			jzSql+=" GROUP BY d.doctorId ORDER BY o.createTime DESC";
			if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
				jzSql+=" limit "+(page-1)*row+","+row;
			}
			//System.out.println(sql);
			//执行查询
			listMaps = jdbcTemplate.queryForList(jzSql);
		} catch (Exception e) {
			logger.info(e+"异常信息");
		}
		
		return listMaps;
	}
	
	/**
	 * 查询收藏的文章
	 */
	@Override
	public List<Map<String, Object>> queryCollectArticle(String name,String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		//拼接sql
		String sql = " SELECT t.*,"
				+ " CASE num WHEN num=1 THEN DATE_FORMAT(times,'%Y-%m-%d %H:%i') ELSE DATE_FORMAT(times,'%H:%i') END timesapp,IF(followId is null ,1,2) follow,IF(praiseId is null ,2,1) praise,IF(collectId is null ,1,2) collect"
				+ " FROM "
				+ " (SELECT tk.id,tk.doctorId,tk.typeId,tk.title,fnStripTags(tk.content) as content,tk.isHot,tk.sort,tk.createTime,tk.state,tk.readNum,tk.goodsIds,tk.publisher,tk.sendId,tk.voiceUrl,tk.videoUrl,tk.signUrl,tk.imgUrl,tk.videoShowUrl,tk.top,tk.type,"
				+ " td.docUrl,i.infirmaryName AS hosName,td.docName,p.name AS docPosition,((SELECT COUNT(1) FROM t_user_keep tuk WHERE tuk.goodsId=tk.id AND TYPE=2)+tk.collectNum) collectNum,UNIX_TIMESTAMP(NOW())-tk.createTime>24*60*60 AS num,"
				+ " tuk.id AS tid,tuk.memberId,tuk.goodsId,tuk.type keepType,FROM_UNIXTIME(tk.createTime) AS times,(SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 "
				+ " UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  UNION SELECT id,labelName NAME FROM t_ill_label WHERE state = 1 ) t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"
				+ " (select count(*) from t_konw_eva_reply tker where tker.knowId=tk.id and tker.pid=0 and tker.state=1) as replyNum,"
				+ " (select count(1) from t_user_share tus where tus.goodsId = tk.id and tus.type=2) as shareNum," 
				+ " (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id and (tker.state=1 or tker.replyId='"+memberId+"') and tker.pid='0') as plNum," 
				+ " ((SELECT COUNT(1) FROM t_user_praise tup WHERE tup.goodsId = tk.id and tup.type=2)+tk.pointNum) as pointNum,"
				+ " (select id from t_user_keep where type=3 and goodsId=tk.id and memberId='"+memberId+"') followId,"
				+ " (select id from t_user_praise where type=2 and goodsId=tk.id and memberId='"+memberId+"') praiseId,"
				+ " (select id from t_user_keep where memberId='"+memberId+"' and goodsId=tk.id and type=2) collectId "
				+ " FROM "
				+ " t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId"
				+ "	LEFT JOIN t_user_keep tuk ON tk.id  =tuk.goodsId "
				+ "	LEFT JOIN t_infirmary i ON i.infirmaryId = td.infirmaryId "
				+ "	LEFT JOIN t_position p ON p.id = td.docPositionId where tuk.memberId='"+memberId+"' GROUP BY tk.id)t WHERE 1=1 and t.state=1  ";
		if(!StringUtils.isEmpty(name)){
			sql += " and t.title like '%"+name+"%'";
		}
		//排序，分组
		sql += " group by t.id ORDER BY t.createTime DESC ";
		if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
			sql += " limit " + (page - 1) * row + "," + row;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//查询
			list = jdbcTemplate.queryForList(sql,new Object[]{});
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询收藏的商品
	 */
	@Override
	public List<Map<String, Object>> queryCollectGoods(String name,
			String memberId, Integer page, Integer row) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		try {
			//拼接sql
			String sql="SELECT g.id,g.picture,g.remark,g.name,NULL keepId,g.description"+
					" ,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock"+
					" FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id"+
					" INNER JOIN t_user_keep tuk ON gp.goodsId=tuk.goodsId WHERE tuk.type=1 and tuk.memberId=?";
							
			if(!StringUtil.isEmpty(name)){
				sql+=" and g.name LIKE '%"+name+"%'";
			}
			sql+=" GROUP BY gp.goodsId";
			if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(row)){
				sql+=" limit "+(page-1)*row+","+row;
			}
			//System.out.println(sql);
			//执行查询
			listMaps = jdbcTemplate.queryForList(sql,new Object[]{memberId});
		} catch (Exception e) {
			logger.info(e+"异常信息");
		}
		
		return listMaps;
	}
}
