package cn.syrjia.hospital.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.DoctorDao;
import cn.syrjia.hospital.entity.Department;
import cn.syrjia.hospital.entity.Doctor;
import cn.syrjia.util.StringUtil;

@Repository("doctorDao")
public class DoctorDaoImpl extends BaseDaoImpl implements DoctorDao {

	// 日志
	private Logger logger = LogManager.getLogger(DoctorDaoImpl.class);

	/**
	 * 条件查询医生列表
	 */
	@Override
	public List<Map<String, Object>> queryDoctorList(
			HttpServletRequest request, Doctor doctor, Integer page,
			Integer row, String memberId, String searchSort, String illClassId,
			String area,List<Map<String,Object>> lockDoctorIds,String city,String province) {
		List<Map<String, Object>> doclist = new ArrayList<Map<String, Object>>();
		try {
			// 查询医生所在部门sql，页面暂时不用
			String departSql = ",(SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames ";
			String sql = "SELECT d.doctorId,d.infirmaryId,d.departId,d.infirDepartId,d.docPosition,d.docUrl,d.docLocalUrl,d.docName,d.docSex,d.docPhone,d.docSignature,d.docDesc,d.docStatus,d.docIsOn,d.isRecommended,d.createTime, "
						+" d.docAbstract,d.docNotice,d.isLocalDoc,d.openid,d.qrCodeUrl,d.localQrCodeUrl,d.isOnLine,d.docPositionId,d.idCardNo,d.isDefault,d.askCount, "
						+" d.isAccpetAsk,d.nameShort,d.isSort,d.auditSuccessTime, "
						+" d.doctorSign,d.isOld,d.isDownLoadApp,IFNULL(dss.isHide,ds.isHide) isHide,ddpp.blackCount isBlack,infir.infirmaryName,tt.`name` positionName, ifnull(docEva.evaCnt,0) evaCount, IFNULL(num.buyNum,0) payCount,GROUP_CONCAT(CONCAT_WS(';',if(IFNULL(dss.isOnlineTwGh,ds.isOnlineTwGh)=1,ifnull(IFNULL(ceil(dss.fisrtTwGhMoney),ceil(ds.fisrtTwGhMoney)), ' '),' '),if(IFNULL(dss.isOnlinePhoneGh,ds.isOnlinePhoneGh)=1,ifnull(IFNULL(ceil(dss.fisrtPhoneGhMoney),ceil(ds.fisrtPhoneGhMoney)), ' '),' '),if(IFNULL(dss.isOnlineTwZx,ds.isOnlineTwZx)=1,ifnull(IFNULL(ceil(dss.twZxMoney),ceil(ds.twZxMoney)), ' '),' '),if(IFNULL(dss.isOnlinePhoneZx,ds.isOnlinePhoneZx)=1,ifnull(IFNULL(ceil(dss.phoneZxMoney),ceil(ds.phoneZxMoney)), ' '),' '),1,1,IFNULL(dss.twZxCount,ds.twZxCount),IFNULL(dss.phoneZxCount,ds.phoneZxCount))) moneys, "
						+" if(IFNULL(dss.isOnlineTwGh,ds.isOnlineTwGh)=1,IFNULL(IFNULL(ceil(dss.fisrtTwGhMoney),ceil(ds.fisrtTwGhMoney)),' '),if(IFNULL(dss.isOnlinePhoneGh,ds.isOnlinePhoneGh)=1,IFNULL(IFNULL(ceil(dss.fisrtPhoneGhMoney),ceil(ds.fisrtPhoneGhMoney)),' '),if(IFNULL(dss.isOnlineTwZx,ds.isOnlineTwZx)=1,IFNULL(IFNULL(ceil(dss.twZxMoney),ceil(ds.twZxMoney)),' '),if(IFNULL(dss.isOnlinePhoneZx,ds.isOnlinePhoneZx)=1,IFNULL(IFNULL(ceil(dss.phoneZxMoney),ceil(ds.phoneZxMoney)), 0),' ')))) docMoney, "
						+" ROUND(IFNULL(doCGoodCnt.evaGoodCnt,0)/IFNULL(docEva.evaCnt,1)*100,1) goodEvaRate, ";
			//病症id
			if (!StringUtil.isEmpty(illClassId)) {// 1、科室为空 病症不为空 2、科室、病症都不为空
													// 以最底层（病症为准）
				sql += " (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId ='"
						+ illClassId + "') illClassNames ";
				//科室id
			} else if (!StringUtil.isEmpty(doctor.getDepartId())) {// 病症为空 科室不为空
				sql += " (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.departId ='"
						+ doctor.getDepartId() + "') illClassNames ";
				departSql = ",(SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId ='"
						+ doctor.getDepartId() + "') departNames ";
			} else {// 病症、科室都为空
				sql += " (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId in(SELECT miu.illClassId from t_middle_util miu where miu.doctorId=d.doctorId)) illClassNames ";
			}
			sql += departSql+" FROM t_doctor d  "
					+ " INNER JOIN t_infirmary infir on infir.infirmaryId =d.infirmaryId "
					+ " INNER JOIN t_position tt on tt.id =d.docPositionId "
					+ " INNER JOIN t_doctor_set ds ON ds.doctorId = '-1' "
					+ " LEFT JOIN t_doctor_set dss on dss.doctorId = d.doctorId "
					+ " LEFT JOIN (SELECT count(1) blackCount,dp.doctorId from t_doctor_patient dp where  dp.memberId='"+memberId+"' and dp.isBlack=1 ) ddpp on ddpp.doctorId=d.doctorId "
					+ " left join (SELECT COUNT(1) buyNum,os.doctorId FROM t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo WHERE o.state = 1 AND o.paymentStatus in(2,5) and o.orderType in(SELECT s.id from t_server_type s) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaCnt,e.goodsId FROM t_evaluate e WHERE e.state=1 GROUP BY e.goodsId ) docEva on docEva.goodsId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaGoodCnt,e1.goodsId FROM t_evaluate e1 WHERE e1.state=1 and e1.evaluateLevel>=2  GROUP BY e1.goodsId ) doCGoodCnt on doCGoodCnt.goodsId = d.doctorId "
					+ " where d.docIsOn='1' and d.docStatus='10' ";
			//省区域
//			if("全部".equals(city)){
//				//增加搜索全部省级 20181119
//				sql += " and infir.countyName in (select t.area from t_areas t where t.cityid in(select c.cityid from t_cities c where c.provinceid =(select p.provinceid from t_provinces p where p.province = '"+province+"'))) ";
//			} 
			if (StringUtil.isEmpty(area)&&!StringUtil.isEmpty(province)) {
				//增加搜索全部省级 20181119
				sql += " and infir.countyName in (select t.area from t_areas t where t.cityid in(select c.cityid from t_cities c where c.provinceid =(select p.provinceid from t_provinces p where p.province = '"+province+"'))) ";
			}
			
			//县区域
			if (!StringUtil.isEmpty(area)) {
				if("全部".equals(area)){
					//增加搜索全部市级 20181022
					sql += " and infir.countyName in (select t.area from t_areas t where t.cityid=(select c.cityid from t_cities c where c.city='"+city+"'))";
				} else {
					sql += " and infir.countyName ='" + area + "'";
				}
			}
			//病症id
			if (!StringUtil.isEmpty(illClassId)) {
				sql += " and d.doctorId in(SELECT miu.doctorId from t_middle_util miu where miu.illClassId='"
						+ illClassId + "') ";
			}
			//
			if (!StringUtil.isEmpty(doctor.getIsRecommended())) {
				sql += " and d.isRecommended='" + doctor.getIsRecommended()
						+ "'";
			}
			//科室
			if (!StringUtil.isEmpty(doctor.getDepartId())) {
				sql += " and d.doctorId in(SELECT ddu.doctorId from t_doc_depart_util ddu where ddu.departId='"
						+ doctor.getDepartId() + "') ";
			}
			/**
			 * 医院
			 */
			if (!StringUtil.isEmpty(doctor.getInfirmaryId())) {
				sql += " and d.infirmaryId='" + doctor.getInfirmaryId() + "'";
			}

			//职位
			if (!StringUtil.isEmpty(doctor.getDocPositionId())
					&& !"-1".equals(doctor.getDocPositionId())) {
				sql += " and d.docPositionId = '" + doctor.getDocPositionId()
						+ "'";
			}
			
			sql += " GROUP BY d.doctorId ";
			if (!StringUtil.isEmpty(searchSort)) {
				if ("hplSort".equals(searchSort)) {// 好评度
					sql += " order by goodEvaRate desc,payCount desc,nameShort asc ";
				} else if ("jzlSort".equals(searchSort)) {// 接单数
					sql += " order by payCount desc,goodEvaRate desc,nameShort asc ";
				} else if ("wzfHighSort".equals(searchSort)) {// 问诊费由高到低
					sql += " order by docMoney desc,payCount desc,nameShort asc ";
				} else if ("wzfLowSort".equals(searchSort)) {// 问诊费由低到高
					sql += " order by docMoney asc,payCount desc,nameShort asc ";
				} else {
					sql += " order by d.isSort desc,goodEvaRate desc,nameShort asc ";
				}
			} else {
				sql += " order by d.isSort desc,goodEvaRate desc,payCount desc,nameShort asc ";
			}
			String finialSql = "SELECT doc.doctorId,doc.infirmaryId,doc.departId,doc.infirDepartId,doc.docPosition,doc.docUrl,doc.docLocalUrl,doc.docName,doc.docSex,doc.docPhone,doc.docSignature,doc.docDesc,doc.docStatus,doc.docIsOn,doc.isRecommended,doc.createTime, "
							+" doc.docAbstract,doc.docNotice,doc.isLocalDoc,doc.openid,doc.qrCodeUrl,doc.localQrCodeUrl,doc.isOnLine,doc.docPositionId,doc.idCardNo,doc.isDefault,doc.askCount, "
							+" doc.isAccpetAsk,doc.nameShort,doc.isSort,doc.auditSuccessTime,doc.doctorSign,doc.isOld,doc.isDownLoadApp,doc.isHide,doc.isBlack,doc.infirmaryName,doc.positionName, "
							+" doc.evaCount,doc.payCount,doc.moneys,doc.docMoney,doc.goodEvaRate,doc.illClassNames,doc.departNames from ("
							+ sql
							+ ") doc where "
							+ "  IF(IFNULL(doc.isBlack,0)=0,1=1,find_in_set(doc.doctorId,(SELECT GROUP_CONCAT(dpp.doctorId) from t_doctor_patient dpp where dpp.isBlack=1 and dpp.memberId='"
					+ memberId + "'))=0) "
					+ " and (IF(doc.isHide=0,1=1,find_in_set('"
					+ memberId
					+ "',(SELECT GROUP_CONCAT(DISTINCT(dp.memberId)) from t_doctor_patient dp where dp.doctorId=doc.doctorId ))) or IF(doc.isHide=0,1=1,find_in_set('"
					+ memberId
					+ "',(SELECT GROUP_CONCAT(DISTINCT(dp.memberId)) from t_scan_keep dp where dp.keepId=doc.doctorId ))) ) ";
			//是否上锁
			if (StringUtil.isEmpty(doctor.getIsRecommended())&&!StringUtil.isEmpty(lockDoctorIds)&&lockDoctorIds.size()>0) {
				finialSql += " and doc.doctorId in (";
					for(int i=0;i<lockDoctorIds.size();i++){
						if(i==lockDoctorIds.size()-1){
							finialSql += "'"+lockDoctorIds.get(i).get("id")+"'";
						}else{
							finialSql += "'"+lockDoctorIds.get(i).get("id")+"',";
						}
					}
				finialSql+=")";
			}
			//获取医生名称
			if (!StringUtil.isEmpty(doctor.getDocName())) {
				// or doc.positionName like '%" + doctor.getDocName() + "%'
				finialSql += " and (doc.illClassNames like '%" + doctor.getDocName() + "%' or doc.docName like '%" + doctor.getDocName() + "%' or  doc.departNames like '%" + doctor.getDocName() + "%' )  ";
				
			}
			System.out.println(finialSql);
			finialSql += " LIMIT " + (page - 1) * row + "," + row;
			System.out.println(finialSql);
			doclist = jdbcTemplate.queryForList(finialSql);
		} catch (NumberFormatException e) {
			logger.info(e + "异常信息");
		}
		return doclist;
	}

	/**
	 * 查询医生职称
	 */
	@Override
	public List<Map<String, Object>> queryPositions() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT t_position.id,t_position.`name`,t_position.remark,t_position.sort,t_position.createTime,t_position.state,t_position.nameShort from t_position t_position where t_position.state=1 ORDER BY t_position.sort DESC ";
			list = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 查询医生科室信息
	 */
	@Override
	public List<Map<String, Object>> queryDeparts() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT departId,departName,departShort,departSort,departType,iconUrl,createTime,state from t_department where state=1 ORDER BY departSort DESC ";
			list = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 根据医生ID查询医生详情信息
	 */
	@Override
	public Map<String, Object> queryDocotrById(String doctorId, String memberId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//医生id
			if (!StringUtil.isEmpty(doctorId)) {
				String sql = "select dd.evaCnt,dd.docId,dd.docName,dd.isAccpetAsk,dd.docUrl,dd.qrCodeUrl,dd.copyBarCode,dd.docSignature,dd.docAbstract, " 
						+" dd.id,dd.doctorId,dd.isHide,dd.isLockPatient,dd.isDisturb,dd.disturbStartTime,dd.disturbEndTime, "
						+" dd.isOnlineTwGh,dd.fisrtTwGhMoney,dd.repeatTwGhMoney, "
						+" dd.acceptTwOrderCount,dd.isOnlinePhoneGh,dd.fisrtPhoneGhMoney,dd.phoneGhServerTime,dd.repeatPhoneGhMoney, "
						+" dd.acceptPhoneOrderCount,dd.isOnlineTwZx,dd.twZxMoney,dd.twZxCount,dd.twZqZxCount, "
						+" dd.twZhZxCount,dd.twZxTime,dd.acceptTwZxOrderCount, "
						+" dd.isOnlinePhoneZx,dd.phoneZxMoney,dd.phoneZxCount,dd.phoneZxTime,dd.isSecret,dd.secretPassword,dd.isGesture, "
						+" dd.gesturePassword,dd.isHideGram,dd.isSystem,dd.acceptPhoneZxOrderCount,dd.onServerTalkTime, "
						+" dd.onServerTalkCount,dd.allTalkTime,dd.allTalkCount, "
						+" dd.infirmaryName,dd.isBlack,dd.positionName,dd.buyNum,dd.keepId,dd.xhCount,dd.jqCount,dd.goodEvaRate,dd.doctorNotice,dd.keepCount,dd.illClassNames,dd.evalNum,dd.goodevalNum,dd.konwNum "
						+ " from(SELECT d.doctorId docId,d.docName,d.isAccpetAsk,d.docUrl,d.qrCodeUrl,d.copyBarCode,d.docSignature,d.docAbstract, "
						+" IFNULL(ds.id,dss.id) id,IFNULL(ds.doctorId,dss.doctorId) doctorId,ifnull(ds.isHide,dss.isHide)isHide,IFNULL(ds.isLockPatient,dss.isLockPatient)isLockPatient, "
						+" IFNULL(ds.isDisturb,dss.isDisturb) isDisturb,IFNULL(ds.disturbStartTime,dss.disturbStartTime) disturbStartTime,IFNULL(ds.disturbEndTime,dss.disturbEndTime) disturbEndTime, "
						+" IFNULL(ds.isOnlineTwGh,dss.isOnlineTwGh) isOnlineTwGh,IFNULL(ds.fisrtTwGhMoney,dss.fisrtTwGhMoney) fisrtTwGhMoney, "
						+" IFNULL(ds.repeatTwGhMoney,dss.repeatTwGhMoney) repeatTwGhMoney,IFNULL(ds.acceptTwOrderCount,dss.acceptTwOrderCount) acceptTwOrderCount,IFNULL(ds.isOnlinePhoneGh,dss.isOnlinePhoneGh) isOnlinePhoneGh,IFNULL(ds.fisrtPhoneGhMoney,dss.fisrtPhoneGhMoney) fisrtPhoneGhMoney,IFNULL(ds.phoneGhServerTime,dss.phoneGhServerTime)phoneGhServerTime,IFNULL(ds.repeatPhoneGhMoney,dss.repeatPhoneGhMoney) repeatPhoneGhMoney, "
						+" IFNULL(ds.acceptPhoneOrderCount,dss.acceptPhoneOrderCount) acceptPhoneOrderCount,IFNULL(ds.isOnlineTwZx,dss.isOnlineTwZx)isOnlineTwZx,IFNULL(ds.twZxMoney,dss.twZxMoney)twZxMoney,IFNULL(ds.twZxCount,dss.twZxCount)twZxCount,IFNULL(ds.twZqZxCount,dss.twZqZxCount)twZqZxCount,IFNULL(ds.twZhZxCount,dss.twZhZxCount) twZhZxCount,IFNULL(ds.twZxTime,dss.twZxTime)twZxTime,IFNULL(ds.acceptTwZxOrderCount,dss.acceptTwZxOrderCount)acceptTwZxOrderCount,IFNULL(ds.isOnlinePhoneZx,dss.isOnlinePhoneZx)isOnlinePhoneZx, "
						+" IFNULL(ds.phoneZxMoney,dss.phoneZxMoney)phoneZxMoney,IFNULL(ds.phoneZxCount,dss.phoneZxCount)phoneZxCount,IFNULL(ds.phoneZxTime,dss.phoneZxTime)phoneZxTime,IFNULL(ds.isSecret,dss.isSecret)isSecret,IFNULL(ds.secretPassword,dss.secretPassword)secretPassword, "
						+" IFNULL(ds.isGesture,dss.isGesture)isGesture,IFNULL(ds.gesturePassword,dss.gesturePassword)gesturePassword,IFNULL(ds.isHideGram,dss.isHideGram)isHideGram,IFNULL(ds.isSystem,dss.isSystem)isSystem, "
						+" IFNULL(ds.acceptPhoneZxOrderCount,dss.acceptPhoneZxOrderCount)acceptPhoneZxOrderCount,IFNULL(ds.onServerTalkTime,dss.onServerTalkTime)onServerTalkTime,IFNULL(ds.onServerTalkCount,dss.onServerTalkCount)onServerTalkCount,IFNULL(ds.allTalkTime,dss.allTalkTime)allTalkTime,IFNULL(ds.allTalkCount,dss.allTalkCount) allTalkCount, "
						+" i.infirmaryName,ddpp.blackCount isBlack,p.`name` as positionName,ifnull(orders.buyNum,0) buyNum,guk.id keepId,ifnull(docEva.evaCnt,0) evaCnt,ifnull(xhCnt.xhCount,0) xhCount,ifnull(jqCnt.jqCount,0) jqCount,ROUND(IFNULL(doCGoodCnt.evaGoodCnt,0)/IFNULL(docEva.evaCnt,1)*100,1) goodEvaRate,notice.content doctorNotice,IFNULL(uk.keepCount,0) keepCount, "
						+ " (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId in(SELECT miu.illClassId from t_middle_util miu where miu.doctorId=d.doctorId)) illClassNames, "
						+ " (SELECT count(1) FROM t_evaluate e WHERE e.goodsId=d.doctorId) evalNum,"
						+ " (SELECT count(1) FROM t_evaluate e WHERE e.goodsId=d.doctorId and e.evaluateLevel>=2) goodevalNum,"
						+ " (SELECT count(1) FROM t_konwledge k WHERE k.doctorId=d.doctorId and k.state=1) konwNum "
						+ " from t_doctor d "
						+ " INNER JOIN t_infirmary i ON i.infirmaryId = d.infirmaryId "
						+ " INNER JOIN t_position p on p.id = d.docPositionId "
						+ " INNER JOIN t_doctor_set dss on dss.doctorId = '-1' "
						+"  LEFT JOIN t_doctor_set ds on ds.doctorId = d.doctorId "
						+ " left join (SELECT count(1) blackCount, dp.doctorId FROM t_doctor_patient dp WHERE dp.memberId = '"+memberId+"' AND dp.isBlack = 1 ) ddpp ON ddpp.doctorId = d.doctorId "
						+ " LEFT JOIN (select dn.content,dn.doctorId from t_doctor_notice dn where dn.state=1 ORDER BY dn.auditTime DESC LIMIT 0,1) notice on notice.doctorId = d.doctorId  "
						+ " left join (SELECT COUNT(1) buyNum,os.doctorId FROM t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo WHERE o.state = 1 AND o.paymentStatus in(2,3,5) and o.orderType in(4,5,6,7,8,11,9,21,22) GROUP BY os.doctorId) orders ON orders.doctorId = d.doctorId "
						+ " left join (SELECT COUNT(1) evaCnt,e.goodsId FROM t_evaluate e WHERE e.state=1 ";
					//id		
					if(!StringUtil.isEmpty(memberId)){
							sql +=" OR (e.state !=3 && e.memberId = '"+memberId+"' ";
						}
						sql +=" ) GROUP BY e.goodsId ) docEva on docEva.goodsId = d.doctorId  "
						+ " left join (SELECT COUNT(1) evaGoodCnt,e1.goodsId FROM t_evaluate e1 WHERE e1.state=1 and e1.evaluateLevel>=2  GROUP BY e1.goodsId ) doCGoodCnt on doCGoodCnt.goodsId = d.doctorId "
						+ " left join (SELECT COUNT(1) xhCount,e.goodsId FROM t_evaluate e INNER JOIN t_my_eva_banner m on m.evaId=e.id WHERE m.type=1 and m.state=2 GROUP BY e.goodsId) xhCnt on xhCnt.goodsId = d.doctorId "
						+ " left join (SELECT COUNT(1) jqCount,e.goodsId FROM t_evaluate e INNER JOIN t_my_eva_banner m on m.evaId=e.id WHERE m.type=2 and m.state=2 GROUP BY e.goodsId) jqCnt on jqCnt.goodsId = d.doctorId "
						+ " LEFT JOIN (SELECT count(1) keepCount,k.goodsId from t_user_keep k where k.type=3 GROUP BY k.goodsId) uk on uk.goodsId=d.doctorId "
						+ " LEFT JOIN (SELECT k.id,k.goodsId from t_user_keep k where k.type=3 and k.memberId='"
						+ memberId
						+ "') guk on guk.goodsId=d.doctorId "
						+ " where " +
						" d.docIsOn='1' and d.docStatus='10' and" +
						" d.doctorId='"+doctorId+"' "
						+ " and (IF(IFNULL(ds.isHide,dss.isHide)=0,1=1,find_in_set('"
						+ memberId
						+ "',(SELECT GROUP_CONCAT(DISTINCT(dp.memberId)) from t_doctor_patient dp where dp.doctorId=d.doctorId ))) or IF(IFNULL(ds.isHide,dss.isHide)=0,1=1,find_in_set('"
						+ memberId
						+ "',(SELECT GROUP_CONCAT(DISTINCT(dp.memberId)) from t_scan_keep dp where dp.keepId=d.doctorId ))) ) " 
						+")dd  "
						+" where IF(IFNULL(dd.isBlack,0)=0,1=1,find_in_set(dd.doctorId,(SELECT GROUP_CONCAT(dpp.doctorId) from t_doctor_patient dpp where dpp.isBlack=1 and dpp.memberId='"
						+ memberId
						+ "'))=0)";
				System.out.println(sql);
				map = jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}
	
	/**
	 * 查询医生名片
	 */
	@Override
	public Map<String, Object> queryDoctorByCard(String doctorId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!StringUtil.isEmpty(doctorId)) {
				/*String sql =  "select dd.* "
						+ " from(SELECT d.doctorId docId,d.docName,d.isAccpetAsk,d.docUrl,d.qrCodeUrl,d.copyBarCode,d.docSignature,d.docAbstract,"
						+" IFNULL(ds.id,dss.id) id,IFNULL(ds.doctorId,dss.doctorId) doctorId,ifnull(ds.isHide,dss.isHide)isHide,IFNULL(ds.isLockPatient,dss.isLockPatient)isLockPatient, "
						+" IFNULL(ds.isDisturb,dss.isDisturb) isDisturb,IFNULL(ds.disturbStartTime,dss.disturbStartTime) disturbStartTime,IFNULL(ds.disturbEndTime,dss.disturbEndTime) disturbEndTime, "
						+" IFNULL(ds.isOnlineTwGh,dss.isOnlineTwGh) isOnlineTwGh,IFNULL(ds.fisrtTwGhMoney,dss.fisrtTwGhMoney) fisrtTwGhMoney, "
						+" IFNULL(ds.repeatTwGhMoney,dss.repeatTwGhMoney) repeatTwGhMoney,IFNULL(ds.acceptTwOrderCount,dss.acceptTwOrderCount) acceptTwOrderCount,IFNULL(ds.isOnlinePhoneGh,dss.isOnlinePhoneGh) isOnlinePhoneGh,IFNULL(ds.fisrtPhoneGhMoney,dss.fisrtPhoneGhMoney) fisrtPhoneGhMoney,IFNULL(ds.phoneGhServerTime,dss.phoneGhServerTime)phoneGhServerTime,IFNULL(ds.repeatPhoneGhMoney,dss.repeatPhoneGhMoney) repeatPhoneGhMoney, "
						+" IFNULL(ds.acceptPhoneOrderCount,dss.acceptPhoneOrderCount) acceptPhoneOrderCount,IFNULL(ds.isOnlineTwZx,dss.isOnlineTwZx)isOnlineTwZx,IFNULL(ds.twZxMoney,dss.twZxMoney)twZxMoney,IFNULL(ds.twZxCount,dss.twZxCount)twZxCount,IFNULL(ds.twZqZxCount,dss.twZqZxCount)twZqZxCount,IFNULL(ds.twZhZxCount,dss.twZhZxCount) twZhZxCount,IFNULL(ds.twZxTime,dss.twZxTime)twZxTime,IFNULL(ds.acceptTwZxOrderCount,dss.acceptTwZxOrderCount)acceptTwZxOrderCount,IFNULL(ds.isOnlinePhoneZx,dss.isOnlinePhoneZx)isOnlinePhoneZx, "
						+" IFNULL(ds.phoneZxMoney,dss.phoneZxMoney)phoneZxMoney,IFNULL(ds.phoneZxCount,dss.phoneZxCount)phoneZxCount,IFNULL(ds.phoneZxTime,dss.phoneZxTime)phoneZxTime,IFNULL(ds.isSecret,dss.isSecret)isSecret,IFNULL(ds.secretPassword,dss.secretPassword)secretPassword, "
						+" IFNULL(ds.isGesture,dss.isGesture)isGesture,IFNULL(ds.gesturePassword,dss.gesturePassword)gesturePassword,IFNULL(ds.isHideGram,dss.isHideGram)isHideGram,IFNULL(ds.isSystem,dss.isSystem)isSystem, "
						+" IFNULL(ds.acceptPhoneZxOrderCount,dss.acceptPhoneZxOrderCount)acceptPhoneZxOrderCount,IFNULL(ds.onServerTalkTime,dss.onServerTalkTime)onServerTalkTime,IFNULL(ds.onServerTalkCount,dss.onServerTalkCount)onServerTalkCount,IFNULL(ds.allTalkTime,dss.allTalkTime)allTalkTime,IFNULL(ds.allTalkCount,dss.allTalkCount) allTalkCount, "
						+ " i.infirmaryName,p.`name` as positionName,ifnull(orders.buyNum,0) buyNum,ifnull(docEva.evaCnt,0) evaCnt,ifnull(xhCnt.xhCount,0) xhCount,ifnull(jqCnt.jqCount,0) jqCount,ROUND(IFNULL(doCGoodCnt.evaGoodCnt,0)/IFNULL(docEva.evaCnt,1)*100,1) goodEvaRate,notice.content doctorNotice,IFNULL(uk.keepCount,0) keepCount, "
						+ " (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId in(SELECT miu.illClassId from t_middle_util miu where miu.doctorId=d.doctorId)) illClassNames, "
						+ " (SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames, "
						+ " (SELECT count(1) FROM t_evaluate e WHERE e.goodsId=d.doctorId) evalNum,"
						+ " (SELECT count(1) FROM t_evaluate e WHERE e.goodsId=d.doctorId and e.evaluateLevel>3) goodsEvalNum,"
						+ " (SELECT count(1) FROM t_konwledge k WHERE k.doctorId=d.doctorId and k.state=1) konwNum "
						+ " from t_doctor d "
						+ " INNER JOIN t_infirmary i ON i.infirmaryId = d.infirmaryId "
						+ " INNER JOIN t_position p on p.id = d.docPositionId "
						+ " INNER JOIN t_doctor_set dss on dss.doctorId = '-1' "
						+ " LEFT JOIN t_doctor_set ds on ds.doctorId = d.doctorId "
						+ " LEFT JOIN (select dn.content,dn.doctorId from t_doctor_notice dn where dn.state=1 ORDER BY dn.auditTime DESC LIMIT 0,1) notice on notice.doctorId = d.doctorId  "
						+ " left join (SELECT COUNT(1) buyNum,os.doctorId FROM t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo WHERE o.state = 1 AND o.paymentStatus in(2,5) and o.orderType in(4,5,6,7,8,11,9) GROUP BY os.doctorId) orders ON orders.doctorId = d.doctorId "
						+ " left join (SELECT COUNT(1) evaCnt,e.goodsId FROM t_evaluate e WHERE e.state=1 GROUP BY e.goodsId ) docEva on docEva.goodsId = d.doctorId "
						+ " left join (SELECT COUNT(1) evaGoodCnt,e1.goodsId FROM t_evaluate e1 WHERE e1.state=1 and e1.evaluateLevel>=2  GROUP BY e1.goodsId ) doCGoodCnt on doCGoodCnt.goodsId = d.doctorId "
						+ " left join (SELECT COUNT(1) xhCount,e.goodsId FROM t_evaluate e INNER JOIN t_my_eva_banner m on m.evaId=e.id WHERE e.state=1 and m.type=1 and m.state=2 GROUP BY e.goodsId) xhCnt on xhCnt.goodsId = d.doctorId "
						+ " left join (SELECT COUNT(1) jqCount,e.goodsId FROM t_evaluate e INNER JOIN t_my_eva_banner m on m.evaId=e.id WHERE e.state=1 and m.type=2 and m.state=2 GROUP BY e.goodsId) jqCnt on jqCnt.goodsId = d.doctorId "
						+ " LEFT JOIN (SELECT count(1) keepCount,k.goodsId from t_user_keep k where k.type=3 GROUP BY k.goodsId) uk on uk.goodsId=d.doctorId "
						+ " where d.docIsOn='1' and d.docStatus='10' and d.doctorId='"+doctorId+"')dd ";*/
				String sql = "SELECT d.doctorId docId,d.docName, d.isAccpetAsk,d.docUrl,d.qrCodeUrl,d.copyBarCode,d.docSignature,d.docAbstract,i.infirmaryName,p.name positionName, "
						+" (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId IN ( SELECT miu.illClassId FROM t_middle_util miu	WHERE miu.doctorId = d.doctorId )) illClassNames, "
						+" (SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId IN (SELECT ddu.departId FROM t_doc_depart_util ddu WHERE ddu.doctorId = d.doctorId)) departNames "
						+" FROM t_doctor d " 
						+" INNER JOIN t_infirmary i ON i.infirmaryId = d.infirmaryId "
						+" INNER JOIN t_position p ON p.id = d.docPositionId "
						+" WHERE d.docIsOn = '1' AND d.docStatus = '10' AND d.doctorId = '"+doctorId+"' ";
				System.out.println(sql);
				map = jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 根据医生ID查询医生线上调理信息
	 */
	@Override
	public Map<String, Object> queryDocotrSetById(String doctorId) {
		Map<String, Object> setMap = null;
		try {
			String sql = "select s.id,s.doctorId, s.isHide,s.isLockPatient,s.isDisturb,s.disturbStartTime,s.disturbEndTime, "
						+" s.isOnlineTwGh,s.fisrtTwGhMoney,s.repeatTwGhMoney, "
						+" s.acceptTwOrderCount,s.isOnlinePhoneGh,s.fisrtPhoneGhMoney,s.phoneGhServerTime,s.repeatPhoneGhMoney, " 
						+" s.acceptPhoneOrderCount,s.isOnlineTwZx,s.twZxMoney,s.twZxCount,s.twZqZxCount, "
						+" s.twZhZxCount,s.twZxTime,s.acceptTwZxOrderCount, "
						+"s.isOnlinePhoneZx,s.phoneZxMoney,s.phoneZxCount,s.phoneZxTime,s.isSecret,s.secretPassword,s.isGesture, "
						+" s.gesturePassword,s.isHideGram,s.isSystem,s.acceptPhoneZxOrderCount,s.onServerTalkTime, "
						+"s.onServerTalkCount,s.allTalkTime,s.allTalkCount from t_doctor_set s where s.doctorId=? ";
			if (!StringUtil.isEmpty(doctorId)) {
				setMap = super.queryBysqlMap(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return setMap;
	}

	/**
	 * 根据医生ID查询医生线上调理信息(默认)
	 */
	@Override
	public Map<String, Object> queryDocotrSetDefault() {
		Map<String, Object> setMap = null;
		try {
			String sql = "select s.id,s.doctorId, s.isHide,s.isLockPatient,s.isDisturb,s.disturbStartTime,s.disturbEndTime, "
					+" s.isOnlineTwGh,s.fisrtTwGhMoney,s.repeatTwGhMoney, "
					+" s.acceptTwOrderCount,s.isOnlinePhoneGh,s.fisrtPhoneGhMoney,s.phoneGhServerTime,s.repeatPhoneGhMoney, " 
					+" s.acceptPhoneOrderCount,s.isOnlineTwZx,s.twZxMoney,s.twZxCount,s.twZqZxCount, "
					+" s.twZhZxCount,s.twZxTime,s.acceptTwZxOrderCount, "
					+"s.isOnlinePhoneZx,s.phoneZxMoney,s.phoneZxCount,s.phoneZxTime,s.isSecret,s.secretPassword,s.isGesture, "
					+" s.gesturePassword,s.isHideGram,s.isSystem,s.acceptPhoneZxOrderCount,s.onServerTalkTime, "
					+"s.onServerTalkCount,s.allTalkTime,s.allTalkCount from t_doctor_set s where s.id='1' and s.doctorId='-1' ";
			setMap = super.queryBysqlMap(sql, null);
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return setMap;
	}

	/**
	 * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
	 */
	@Override
	public List<Map<String, Object>> queryDoctorEvaList(String doctorId,
			Integer row, Integer page, String _sign, String memberId) {
		List<Map<String, Object>> evalist = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT e.*,FROM_UNIXTIME(e.createTime,'%Y-%m-%d') evaDate,s.name serverName,pd.name realname,if(pd.nexus='本人',m.photo,'') memberUrl,if(pd.sex=0,'男','女')sex,"
					+ " (SELECT GROUP_CONCAT(ee.evalableName) from t_evaluate_evalabel ee where ee.evaluateId=e.id ) labelNames "
					+ " ,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i') wzDate,ifnull(os.symptomDescribe,'') diagonsticName from t_evaluate e "
					+ " INNER JOIN t_order o on o.orderNo = e.orderNo "
					+ " INNER JOIN t_server_type s on s.id = o.orderType "
					+ " INNER JOIN t_doctor d on d.doctorId = e.goodsId "
					+ " INNER JOIN t_member m on m.id = e.memberId "
					+ " INNER JOIN t_patient_data pd on pd.id = o.patientId "
					+ " left join t_order_symptom os on os.orderNo = o.orderNo ";
			//医生id
			if (!StringUtil.isEmpty(doctorId)) {
				sql += "where e.id is not null and (e.state =1 or (e.state!=3 &&e.memberId='"
						+ memberId + "')) and e.goodsId='" + doctorId + "' ";
				if (!StringUtil.isEmpty(_sign)) {
					if ("good".equals(_sign)) {
						sql += " and e.evaluateLevel >=4  ";
					} else if ("middle".equals(_sign)) {
						sql += " and e.evaluateLevel >=2 and e.evaluateLevel <=3 ";
					} else if ("low".equals(_sign)) {
						sql += " and e.evaluateLevel =1  ";
					}
				}
				sql += " GROUP BY e.id  order by e.createTime DESC  ";
				if (page != null && row != null) {
					sql += " limit " + (page - 1) * row + "," + row;
				} else {
					sql += " limit 0,3";
				}
				evalist = jdbcTemplate.queryForList(sql);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return evalist;
	}

	/**
	 * 根据医生ID查询医生文章信息
	 */
	@Override
	public List<Map<String, Object>> queryDoctorArticleList(String memberId,String doctorId,
			Integer row, Integer page) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT t.* FROM (SELECT tk.labelIds,tk.id,tk.videoShowUrl,tk.doctorId,tk.title,tk.content AS content,tk.state,tk.readNum,"+
						//" (SELECT GROUP_CONCAT(t. NAME) FROM (SELECT evaLabelId id,evaLabelName NAME FROM t_evalabel el WHERE type = 3 AND state = 1 UNION SELECT id,NAME FROM t_diagnostic WHERE state = 1  " + 
						//" UNION SELECT illClassId id,illClassName NAME FROM t_illness_class WHERE illClassStatus = '10' AND illClassIsOn='1') t WHERE FIND_IN_SET(t.id, tk.labelIds) ) labelNames,"+
						//" (SELECT count(1) FROM t_user_keep WHERE TYPE=3 AND goodsId=tk.id AND memberId='"+memberId+"') followId,"+
						//" (SELECT count(1) FROM t_user_praise WHERE TYPE=2 AND goodsId=tk.id AND memberId='"+memberId+"') praiseId,"+
						//" (SELECT count(1) FROM t_user_keep WHERE memberId='"+memberId+"' AND goodsId=tk.id AND TYPE=2) collectId,"+
						" tk.createTime,tk.voiceUrl,tk.videoUrl,tk.imgUrl,tk.type,"+
						" FROM_UNIXTIME(tk.createTime) AS times,"+
						//" (SELECT COUNT(1) FROM t_konw_eva_reply tker WHERE tker.knowId = tk.id AND  pid='0' and (tker.state = 1 OR tker.replyId='"+memberId+"')) AS plNum,"+
						" (tk.actualPointNum+tk.pointNum) AS pointNum,"+
						" (tk.actualCollectNum+tk.collectNum) as collectNum, "+
						" tk.actualShareNum as shareNum"+
						" FROM"+
						" t_konwledge tk LEFT JOIN t_doctor td ON tk.doctorId = td.doctorId)t"+
						 " WHERE t.state=1 AND t.doctorId='"+doctorId+"' ORDER BY t.times DESC  ";
					 if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
							sql += " limit " + (page - 1) * row + "," + row;
						}
					 list = jdbcTemplate.queryForList(sql,new Object[]{});
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 根据医生ID查询医生评价统计信息
	 */
	@Override
	public Map<String, Object> queryDoctorEvaCensus(String doctorId,String memberId) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT IFNULL(docEva.evaCnt,0) totalEvaCnt,IFNULL(docHaoCnt.evaHaoCnt,0) goodEvaCnt,IFNULL(doCMiddelCnt.evaMiddleCnt,0) middleEvaCnt, "
					+ " IFNULL(doCLowCnt.evaLowCnt,0) lowEvaCnt,ROUND(IFNULL(doCGoodCnt.evaGoodCnt,0)/IFNULL(docEva.evaCnt,1)*100,1) goodEvaRate from t_doctor d "
					+ " left join (SELECT COUNT(1) evaCnt,e.goodsId FROM t_evaluate e WHERE (e.state in(1,4) or ((e.state=2 or e.state=5) and e.memberId='"
						+ memberId + "')) GROUP BY e.goodsId ) docEva on docEva.goodsId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaGoodCnt,e1.goodsId FROM t_evaluate e1 WHERE (e1.state in(1,4) or ((e1.state=2 or e1.state=5) and e1.memberId='"
						+ memberId + "')) and e1.evaluateLevel>=2  GROUP BY e1.goodsId ) doCGoodCnt on doCGoodCnt.goodsId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaHaoCnt,e1.goodsId FROM t_evaluate e1 WHERE (e1.state in(1,4) or ((e1.state=2 or e1.state=5) and e1.memberId='"
						+ memberId + "')) and e1.evaluateLevel in(4,5)  GROUP BY e1.goodsId ) docHaoCnt on docHaoCnt.goodsId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaMiddleCnt,e1.goodsId FROM t_evaluate e1 WHERE (e1.state in(1,4) or ((e1.state=2 or e1.state=5) and e1.memberId='"
						+ memberId + "')) and e1.evaluateLevel in(2,3) GROUP BY e1.goodsId ) doCMiddelCnt on doCMiddelCnt.goodsId = d.doctorId "
					+ " left join (SELECT COUNT(1) evaLowCnt,e1.goodsId FROM t_evaluate e1 WHERE (e1.state in(1,4) or ((e1.state=2 or e1.state=5) and e1.memberId='"
						+ memberId + "')) and e1.evaluateLevel =1 GROUP BY e1.goodsId ) doCLowCnt on doCLowCnt.goodsId = d.doctorId "
					+ " where d.docIsOn='1' and d.docStatus='10' and d.doctorId=? ";
			if (!StringUtil.isEmpty(doctorId)) {
				map = super.queryBysqlMap(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 分页查询我的医生页面列表
	 */
	@Override
	public Map<String, Object> queryMyDoctorList(String memberId, Integer page,
			Integer row) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!StringUtil.isEmpty(memberId)) {
				String jzSql = "SELECT o.orderNo,d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount, "
						+ " GROUP_CONCAT(CONCAT_WS(';',ifnull(ds.fisrtTwGhMoney, ' '),ifnull(ds.fisrtPhoneGhMoney, ' '),ifnull(ds.twZxMoney, ' '),ifnull(ds.phoneZxMoney, ' '),1,1,ds.twZxCount,ds.phoneZxCount)) moneys, "
						+ " ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate, "
						+ " (SELECT group_concat(ill.illClassName)	FROM t_illness_class ill WHERE ill.illClassId in ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId  ) ) illClassNames, "
						+ " 1 as type "
						+ " from t_order o "
						+ " INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo "
						+ " INNER JOIN t_doctor d on d.doctorId = os.doctorId  "
						+ " INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
						+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
						+ " INNER JOIN t_doctor_set ds ON if((SELECT count(0) from t_doctor_set s where s.doctorId=d.doctorId)>0, ds.doctorId=d.doctorId,ds.doctorId='-1') "
						+ " INNER JOIN t_server_type s on s.id = o.orderType "
						+ " INNER JOIN t_patient_data p on p.id = o.patientId "
						+ " LEFT JOIN (SELECT COUNT(1) buyNum, os.doctorId FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo WHERE o.state = 1 AND o.paymentStatus IN (2, 5) "
						+ " AND o.orderType IN (select s.id from t_server_type s where s.state=1) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaGoodCnt ,e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = d.doctorId "
						+ " where o.state = 1 AND o.paymentStatus in(2,5) and o.memberId='"
						+ memberId
						+ "' GROUP BY d.doctorId ORDER BY o.createTime DESC ";
				String gzSql = "SELECT null orderNo,d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount, "
						+" GROUP_CONCAT(CONCAT_WS(';',IF(IFNULL(dss.isOnlineTwGh,ds.isOnlineTwGh)=1,ifnull(IFNULL(ceil(dss.fisrtTwGhMoney),ceil(ds.fisrtTwGhMoney)), ' '),' '),if(IFNULL(dss.isOnlinePhoneGh,ds.isOnlinePhoneGh)=1,ifnull(IFNULL(ceil(dss.fisrtPhoneGhMoney),ceil(ds.fisrtPhoneGhMoney)), ' '),' '), "
						+" if(IFNULL(dss.isOnlineTwZx,ds.isOnlineTwZx)=1,ifnull(IFNULL(ceil(dss.twZxMoney),ceil(ds.twZxMoney)), ' '),' '),if(IFNULL(dss.isOnlinePhoneZx,ds.isOnlinePhoneZx)=1,ifnull(IFNULL(ceil(dss.phoneZxMoney),ceil(ds.phoneZxMoney)), ' '),' '),1,1,IFNULL(dss.twZxCount,ds.twZxCount),IFNULL(dss.phoneZxCount,ds.phoneZxCount))) moneys, "
						+ " ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate, "
						+ " (SELECT group_concat(ill.illClassName)	FROM t_illness_class ill WHERE ill.illClassId in ( SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId  ) ) illClassNames, "
						+ " 2 as type FROM t_doctor d INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
						+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
						+" INNER JOIN t_doctor_set ds ON ds.id=1 and ds.doctorId='-1' "
						+" LEFT JOIN t_doctor_set dss on dss.doctorId = d.doctorId "
						+ " INNER JOIN t_user_keep uk on uk.goodsId = d.doctorId "
						+ " LEFT JOIN (SELECT COUNT(1) buyNum, os.doctorId FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo WHERE o.state = 1 AND o.paymentStatus IN (2, 5) "
						+ " AND o.orderType IN (select s.id from t_server_type s where s.state=1) GROUP BY os.doctorId) num ON num.doctorId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = d.doctorId "
						+ " LEFT JOIN ( SELECT COUNT(1) evaGoodCnt ,e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = d.doctorId "
						+ " WHERE d.docIsOn = '1' AND d.docStatus = '10' AND infir.state = 1 and uk.type=3 and uk.memberId='"
						+ memberId
						+ "'  GROUP BY d.doctorId ORDER BY uk.createTime DESC ";

				String sql = "select dd.* from ((" + jzSql + ") " + " UNION "
						+ " (" + gzSql + ")) dd limit " + (page - 1) * row
						+ "," + row;

				int jzCount = super.queryBysqlCount("select count(1) from ("
						+ jzSql + ") jz", null);
				int gzCount = super.queryBysqlCount("select count(1) from ("
						+ gzSql + ") gz", null);
				List<Map<String, Object>> list = super
						.queryBysqlList(sql, null);
				map.put("data", list);
				map.put("jzCount", jzCount);
				map.put("gzCount", gzCount);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 根据搜索关键字搜索医生匹配列表
	 */
	@Override
	public List<Map<String, Object>> queryDoctorByKey(String searchkey) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//搜索值
			if (!StringUtil.isEmpty(searchkey)) {
				String sql = "SELECT d.doctorId id,d.docName name,1 as type from t_doctor d "
						+ " where d.docIsOn='1' and d.docStatus='10' and d.docName LIKE '%"
						+ searchkey
						+ "%' "
						+ " UNION "
						+ " SELECT i.illClassId id,i.illClassName name ,2 as type from t_illness_class i "
						+ " where i.illClassIsOn='1' and i.illClassStatus='10' and i.illClassName LIKE '%"
						+ searchkey
						+ "%' "
						+ " UNION "
						+ " (SELECT d.departId id,d.departName name ,3 as type from t_department d "
						+ " where d.state=1 and d.departName LIKE '% "
						+ searchkey + "%' ORDER BY d.departSort DESC)";
				list = super.queryBysqlList(sql, null);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 查询上级ID
	 */
	@Override
	public Map<String, Object> queryFollowId(String doctorId) {
		Map<String, Object> map = null;
		try {
			String sql = " SELECT followId from t_follow_history where openId=?";
			if (!StringUtil.isEmpty(doctorId)) {
				map = super.queryBysqlMap(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}

	/**
	 * 查询医生坐诊信息
	 */
	@Override
	public List<Map<String, Object>> queryDoctorZzList(String doctroId,
			String zzDate) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (StringUtil.isEmpty(zzDate)) {
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				zzDate = sdf.format(d);
			}
			String sql = "SELECT CONCAT_WS('@',dd.czTime,dd.zzData) zzData from (SELECT dz.zzState, GROUP_CONCAT(CONCAT_WS(';',dz.id,ifnull(dz.price,0),dz.isHis,if(dz.isHis=1,dz.num,0),i.infirmaryName,CONCAT(i.provieceName,i.cityName,i.countyName,i.address))) zzData,CASE dz.zzState WHEN 0 THEN '上午出诊' WHEN 3 THEN '全天出诊' WHEN 1 THEN '下午出诊' ELSE '晚上出诊' END czTime "
					+ " from t_doctor_zz_data dz "
					+ " INNER JOIN t_infirmary i on i.infirmaryId = dz.infirmaryId "
					+ " where dz.state=1 and dz.doctorId=? and dz.zzDate=? GROUP BY dz.zzState ORDER BY zzState ASC ) dd";
			if (!StringUtil.isEmpty(doctroId)) {
				list = super.queryBysqlList(sql, new Object[] { doctroId,
						zzDate });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询是否有坐诊信息
	 */
	@Override
	public List<Map<String, Object>> queryZzDataCountByDocId(String doctorId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT CONCAT_WS('@',dd.tdate,dd.tday,dd.datas) dayData from (SELECT y.tdate,y.tday,if(MAX(d.isHis)=1 or max(d.isYzt)=1,1,IF(MAX(d.isHis)=0,0,' ')) datas "
					+ " from t_year_date y "
					+ " left JOIN t_doctor_zz_data d on d.zzDate=y.tdate and d.state=1 and d.doctorId=? "
					+ " where y.tdate>=DATE_FORMAT(NOW(),'%Y-%m-%d') and y.tdate<=DATE_SUB(CURDATE(), INTERVAL -13 DAY) GROUP BY y.tdate order by y.tdate asc ) dd ";
			if (!StringUtil.isEmpty(doctorId)) {
				list = super.queryBysqlList(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询从今天起7天内坐诊状态
	 */
	@Override
	public List<Map<String, Object>> querySevenZzStatus(String doctorId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT case when ((max(dz.isHis)=1 and (dz.num-IFNULL(xxData.xxCount,0))>0) or max(dz.isYzt)=1) THEN '可预约' when (max(dz.isHis)=1 and (dz.num-IFNULL(xxData.xxCount,0))<=0) THEN '约满' WHEN max(dz.isHis)=0 THEN '出诊' ELSE '休' END maxHis from t_year_date y "
						+" left JOIN t_doctor_zz_data dz on y.tdate = dz.zzDate and dz.doctorId=? and dz.state=1 "
						+" LEFT JOIN (SELECT count(1) xxCount,os.doctorId,os.xxGhDate,os.xxGhTime from t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo where o.orderType=11 and o.paymentStatus in(2,5)) xxData on xxData.doctorId=dz.doctorId and xxData.xxGhDate=dz.zzDate and xxData.xxGhTime=dz.zzState "
						+" where y.tdate>=DATE_FORMAT(NOW(),'%Y-%m-%d') and y.tdate<=DATE_SUB(CURDATE(), INTERVAL -6 DAY) "
						+" GROUP BY y.tdate ORDER BY y.tdate ASC ";
			if (!StringUtil.isEmpty(doctorId)) {
				list = super.queryBysqlList(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 查询疾病及不时分类
	 */
	@Override
	public List<Map<String,Object>> queryIllClass(Integer num) {
		List<Map<String,Object>> secondList = new ArrayList<Map<String,Object>>();
		try {
			if (num == null) {
				num = 7;
			}
			String sql = "SELECT c.illClassId,c.illClassName,c.illClassShort,c.departId,c.illClassDesc,c.illClassStatus,c.illClassDesc,c.createDate,c.illClassIsOn,c.illClassUrl,c.illClassLocalUrl,c.illClassShowUrl,c.illClassShowLocalUrl,c.isSort from t_illness_class c where c.illClassIsOn = '1' and c.illClassStatus = '10' ORDER BY c.isSort DESC LIMIT 0,"
					+ num;
			secondList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.info(e);
		}
		return secondList;
	}

	/**
	 * 查询全部病症
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Department> queryDepartments() {
		String sql = "SELECT d.*,GROUP_CONCAT(CONCAT_WS(';',ic.illClassName,ic.illClassId) ORDER BY ic.isSort DESC) illClass FROM t_department d INNER JOIN t_illness_class ic ON ic.departId=d.departId where d.state=1 and ic.illClassStatus='10' and ic.illClassIsOn='1' GROUP BY d.departId order by d.departSort desc,d.createTime desc ";
		List<Department> departments = new ArrayList<Department>();
		try {
			departments = jdbcTemplate.query(sql, new BeanPropertyRowMapper(
					Department.class));
		} catch (DataAccessException e) {
			logger.warn(e);
			return null;
		}
		return departments;
	}

	/**
	 * 查询患者病历本列表
	 */
	@Override
	public List<Map<String, Object>> queryMedicalRecords(Integer orderType,
			String patientId, String memberId, Integer page, Integer row) {
		// 还差关联调理方订单进行症状、状态判断
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (!StringUtil.isEmpty(memberId)) {
				String sql = "SELECT o.*,FROM_UNIXTIME(o.createTime) AS times ,p.`name` patientName,d.docName,i.infirmaryName,p1.`name` positionName,s.`name` serverName , "
						+ " IFNULL(CASE WHEN o1.paymentStatus=1 THEN '待支付' WHEN (o1.paymentStatus=2 and o1.orderStatus=1) THEN '待发货' WHEN (o1.paymentStatus=2 and o1.orderStatus=2) THEN '已发货' WHEN (o1.paymentStatus=5 and o1.orderStatus=5) THEN '已完成' WHEN o1.paymentStatus=6 THEN '已结束' ELSE null end, "
						+ " CASE o.orderStatus WHEN 5 THEN '已完成' ELSE '进行中' end) recordStatus,os.symptomDescribe diagonsticName "
						+ " from t_order o "
						+ " INNER JOIN t_order_detail_server ods on ods.orderNo=o.orderNo "
						+ " INNER JOIN t_doctor d on d.doctorId = ods.doctorId "
						+ " INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
						+ " INNER JOIN t_server_type s on s.id = o.orderType "
						+ " INNER JOIN t_position p1 on p1.id = d.docPositionId "
						+ " INNER JOIN t_patient_data p on p.id = o.patientId "
						+ " left join t_order o1 on o1.sourceOrderNo = o.orderNo"
						+ " left join t_order_symptom os on os.orderNo = o.orderNo"
						+ " where o.paymentStatus in(2,5) and o.orderType in (4,5,6,7,8,9,21,22) and o.memberId=? ";
				if (orderType != null) {
					sql += " and o.orderType=" + orderType;
				}
				//患者id
				if (!StringUtil.isEmpty(patientId)) {
					sql += " and p.id='" + patientId + "' ";
				}
				sql += " order by o.payTime desc limit " + (page - 1) * row
						+ "," + row;
				list = super.queryBysqlList(sql, new Object[] { memberId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 查询问诊类型列表
	 */
	@Override
	public List<Map<String, Object>> queryServerTypes() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT * from t_server_type where state=1 ORDER BY id asc";
			list = super.queryBysqlList(sql, null);
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 根据会员查询就诊人列表
	 */
	@Override
	public List<Map<String, Object>> queryPatients(String memberId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//id
			if (!StringUtil.isEmpty(memberId)) {
				String sql = "SELECT * from t_patient_data where memberId='"
						+ memberId + "' ORDER BY createTime DESC ";
				list = super.queryBysqlList(sql, null);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 查询开启锁定患者医生ID集合
	 */
	@Override
	public Map<String, Object> queryLockPatientDoctor(String memberId) {
		Map<String, Object> lockDoctors = null;
		try {
			if(!StringUtil.isEmpty(memberId)){
				String sql = " SELECT dd.lockDoctorId from(SELECT GROUP_CONCAT(dp.followId) lockDoctorId,IFNULL(dss.isLockPatient,ds.isLockPatient) isLockPatient FROM t_follow_history dp "
							+" inner join t_doctor d on d.doctorId=dp.followId "
							+" INNER JOIN t_doctor_set ds ON ds.id='1' and ds.doctorId='-1' "
							+" LEFT JOIN t_doctor_set dss on dss.doctorId = dp.followId "
							+" WHERE d.docStatus = '10' AND dp.openId = '"+memberId+"') dd where dd.isLockPatient=1 ";
				lockDoctors = jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return lockDoctors;
	}

	/**
	 * 获取医生钱包标识
	 */
	@Override
	public String getDoctorSign() {
		String doctorSign = null;
		try {
			//执行存储过程
			doctorSign = jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con)
						throws SQLException {
					CallableStatement cs = con
							.prepareCall("{call t_doctor_sign(?)}");
					cs.registerOutParameter(1, SqlTypeValue.TYPE_UNKNOWN);// 注册输出参数的类型
					return cs;
				}
			}, new CallableStatementCallback<String>() {

				public String doInCallableStatement(CallableStatement call)
						throws SQLException, DataAccessException {
					call.execute();
					return call.getString(1);// 获取输出参数的值
				}
			});
		} catch (DataAccessException e) {
			logger.error(e);
			throw e;
		}
		return doctorSign;
	}

	/**
	 * 根据评价ID查询锦旗
	 */
	@Override
	public Map<String, Object> queryJqImgs(String evaId) {
		Map<String, Object> map = null;
		try {
			if(!StringUtil.isEmpty(evaId)){
				String sql = " SELECT GROUP_CONCAT(dd.imgdata) imgdata  from (SELECT CONCAT_WS(';',count(b.id), b.imgUrl,b.name) imgdata,me.evaId FROM t_my_eva_banner me "
							+" INNER JOIN t_eva_banner b ON b.id = me.evaBannerId WHERE me.state = 2 and me.evaId='"+evaId+"' GROUP BY b.id )dd ";
				map = jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return map;
	}

	/**
	 * 查询就诊记录
	 */
	@Override
	public List<Map<String, Object>> queryJsRecords(String orderNo,
			String doctorId,String memberId) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			if(!StringUtil.isEmpty(doctorId)&&!StringUtil.isEmpty(memberId)){
				String sql = "SELECT o.orderNo,FROM_UNIXTIME(ifnull(o.payTime, o.createTime),'%Y-%m-%d') createTime,ifnull(p.name, ' ') patientName, s.`name` serverName, o.orderStatus,o.paymentStatus,o.doctorId,o.patientId "
						+" from t_order o INNER JOIN t_server_type s on s.id = o.orderType INNER JOIN t_patient_data p ON p.id = o.patientId "
						+" where o.state = 1 AND o.paymentStatus IN (2, 5) and o.doctorId='"+doctorId+"' and o.memberId='"+memberId+"' order by o.payTime desc ";
				list = jdbcTemplate.queryForList(sql);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 根据用户ID查询绑定关系和就诊关系医生集合
	 */
	@Override
	public List<Map<String, Object>> queryScanAndJsIds(String memberId,String doctorId) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			if(!StringUtil.isEmpty(memberId)){
				String sql = "SELECT DISTINCT(dd.id) id from (SELECT a.* from(SELECT DISTINCT(dp.doctorId) id from t_doctor_patient dp  where dp.memberId='"+memberId+"' ORDER BY dp.createTime DESC)a "
						+" UNION "
						+" SELECT b.* from(SELECT DISTINCT(dp.keepId) id from t_scan_keep dp  where dp.type=3 and dp.memberId='"+memberId+"' ORDER BY dp.createTime DESC) b "
						+" UNION "
						+" SELECT c.* from(SELECT DISTINCT(d.doctorId) id from t_doctor d  where d.docStatus='10' and d.docIsOn='1' and d.isRecommended=1 ORDER BY d.createTime DESC)c )dd ";
				if(!StringUtil.isEmpty(doctorId)){
					sql +=" where dd.id ='"+doctorId+"' ";
				}
				list = jdbcTemplate.queryForList(sql);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 分页查询我的医生页面列表
	 */
	@Override
	public Map<String, Object> queryMyDoctorListWx(String memberId, Integer page,
			Integer row) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!StringUtil.isEmpty(memberId)) {
				String jzSql = "SELECT d.isAccpetAsk,o.orderNo,d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName,0 evaCount,0 payCount, "
						+ " null moneys,null goodEvaRate, null illClassNames, "
						+ " 1 as type "
						+ " from t_order o "
						+ " INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo "
						+ " INNER JOIN t_doctor d on d.doctorId = os.doctorId  "
						+ " INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId  "
						+ " INNER JOIN t_position tt ON tt.id = d.docPositionId "
						+ " INNER JOIN t_server_type s on s.id = o.orderType "
						+ " INNER JOIN t_patient_data p on p.id = o.patientId "
						+ " where o.state = 1 AND o.paymentStatus in(2,5) and o.memberId='"
						+ memberId
						+ "' GROUP BY d.doctorId ORDER BY o.createTime DESC ";
				String gzSql = "SELECT dd.isAccpetAsk,dd.orderNo,dd.doctorId,dd.docUrl,dd.docName,dd.infirmaryName,dd.positionName,ifnull(docEva.evaCnt, 0) evaCount,IFNULL(num.buyNum, 0) payCount,dd.moneys, "
							+" ROUND(IFNULL(doCGoodCnt.evaGoodCnt, 0) / IFNULL(docEva.evaCnt, 1) * 100,1) goodEvaRate,dd.illClassNames,dd.type "
							+" from (SELECT NULL orderNo,d.isAccpetAsk,d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName, "
							+" GROUP_CONCAT(CONCAT_WS(';',IF(IFNULL(dss.isOnlineTwGh,ds.isOnlineTwGh)=1,ifnull(IFNULL(ceil(dss.fisrtTwGhMoney),ceil(ds.fisrtTwGhMoney)), ' '),' '),if(IFNULL(dss.isOnlinePhoneGh,ds.isOnlinePhoneGh)=1,ifnull(IFNULL(ceil(dss.fisrtPhoneGhMoney),ceil(ds.fisrtPhoneGhMoney)), ' '),' '), "
							+" if(IFNULL(dss.isOnlineTwZx,ds.isOnlineTwZx)=1,ifnull(IFNULL(ceil(dss.twZxMoney),ceil(ds.twZxMoney)), ' '),' '),if(IFNULL(dss.isOnlinePhoneZx,ds.isOnlinePhoneZx)=1,ifnull(IFNULL(ceil(dss.phoneZxMoney),ceil(ds.phoneZxMoney)), ' '),' '),1,1,IFNULL(dss.twZxCount,ds.twZxCount),IFNULL(dss.phoneZxCount,ds.phoneZxCount))) moneys, "
							+" (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId IN (SELECT miu.illClassId FROM t_middle_util miu WHERE miu.doctorId = d.doctorId)) illClassNames, 2 AS type "
							+" FROM t_doctor d "
							+" INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId "
							+" INNER JOIN t_position tt ON tt.id = d.docPositionId "
							+" INNER JOIN t_user_keep uk ON uk.goodsId = d.doctorId "
							+" INNER JOIN t_doctor_set ds ON ds.id=1 and ds.doctorId='-1' "
							+" LEFT JOIN t_doctor_set dss on dss.doctorId = d.doctorId "
							+" WHERE d.docIsOn = '1' AND d.docStatus = '10' AND uk.type = 3 AND uk.memberId = '"+memberId+"' "
							+" GROUP BY d.doctorId "
							+" ORDER BY uk.createTime DESC ) dd "
							+" LEFT JOIN (SELECT COUNT(1) buyNum, o.doctorId FROM t_order o WHERE o.state = 1 AND o.paymentStatus IN (2,3, 5) AND o.orderType IN (SELECT "
							+" s.id FROM t_server_type s WHERE s.state = 1) GROUP BY o.doctorId) num ON num.doctorId = dd.doctorId "
							+" LEFT JOIN (SELECT COUNT(1) evaCnt, e.goodsId FROM t_evaluate e WHERE e.state = 1 GROUP BY e.goodsId ) docEva ON docEva.goodsId = dd.doctorId "
							+" LEFT JOIN (SELECT COUNT(1) evaGoodCnt, e1.goodsId FROM t_evaluate e1 WHERE e1.state = 1 AND e1.evaluateLevel >= 2 GROUP BY e1.goodsId ) doCGoodCnt ON doCGoodCnt.goodsId = dd.doctorId ";

				String gzCountSql = "SELECT NULL orderNo,d.doctorId,d.docUrl,d.docName,infir.infirmaryName,tt.`name` positionName, 2 AS type "
						+" FROM t_doctor d "
						+" INNER JOIN t_infirmary infir ON infir.infirmaryId = d.infirmaryId "
						+" INNER JOIN t_position tt ON tt.id = d.docPositionId "
						+" INNER JOIN t_user_keep uk ON uk.goodsId = d.doctorId "
						+" WHERE d.docIsOn = '1' AND d.docStatus = '10' AND uk.type = 3 AND uk.memberId = '"+memberId+"' "
						+" GROUP BY d.doctorId "
						+" ORDER BY uk.createTime DESC ";
				
				String sql = "select dd.* from ((" + jzSql + ") " + " UNION "
						+ " (" + gzSql + ")) dd limit " + (page - 1) * row
						+ "," + row;
				System.out.println(sql);
				int jzCount = super.queryBysqlCount("select count(1) from ("
						+ jzSql + ") jz", null);
				int gzCount = super.queryBysqlCount("select count(1) from ("
						+ gzCountSql + ") gz", null);
				List<Map<String, Object>> list = super
						.queryBysqlList(sql, null);
				map.put("data", list);
				map.put("jzCount", jzCount);
				map.put("gzCount", gzCount);
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 查询病症详情
	 */
	@Override
	public Map<String, Object> queryIllClassById(String id) {
		Map<String,Object> map = null;
		try {
			String sql = "SELECT * from t_illness_class where illClassId=? ";
			if(!StringUtil.isEmpty(id)){
				map = jdbcTemplate.queryForMap(sql,new Object[]{id});
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}
	
}
