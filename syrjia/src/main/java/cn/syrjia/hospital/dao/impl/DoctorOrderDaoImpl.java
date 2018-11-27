package cn.syrjia.hospital.dao.impl;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.DoctorOrderDao;
import cn.syrjia.hospital.entity.MyEvaBanner;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import net.sf.json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("doctorOrderDao")
public class DoctorOrderDaoImpl extends BaseDaoImpl implements DoctorOrderDao {

	// 日志
	private Logger logger = LogManager.getLogger(DoctorOrderDaoImpl.class);

	/**
	 * 获取订单号
	 */
	public synchronized String orderNo() {
		String orderNo = null;
		try {
			orderNo = jdbcTemplate.execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con)
						throws SQLException {
					CallableStatement cs = con
							.prepareCall("{call t_orderNo(?)}");
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
		return orderNo;
	}

	/**
	 * 查询用户购买服务确认页面信息
	 */
	@Override
	public Map<String, Object> queryConfirmDocServerData(Integer orderType,
			String doctorId, String memberId) {
		Map<String, Object> map = null;
		try {
			if (!StringUtil.isEmpty(doctorId) && orderType != null) {
				String sql = "select dd.* from (SELECT d.doctorId docId,ddpp.blackCount,d.docName,d.docUrl,s.`name` serverName,i.infirmaryName,p.`name` positionName,ds.* from t_doctor d "
						+ " INNER JOIN t_doctor_set ds on if((SELECT count(0) from t_doctor_set s where s.doctorId=d.doctorId)>0, ds.doctorId=d.doctorId,ds.doctorId='-1') "
						+ " INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
						+ " INNER JOIN t_position p on p.id = d.docPositionId "
						+ " inner join t_server_type s on s.id="
						+ orderType
						+ " LEFT JOIN (SELECT count(1) blackCount,dp.doctorId from t_doctor_patient dp where  dp.memberId='"
						+ memberId
						+ "' and dp.isBlack=1 ) ddpp on ddpp.doctorId=d.doctorId "
						+ " WHERE d.docIsOn='1' and d.docStatus='10' and d.doctorId=?"
						+ " and (IF(ds.isHide=0,1=1, "
						+ " find_in_set('"
						+ memberId
						+ "',(SELECT GROUP_CONCAT(dp.memberId) from t_doctor_patient dp where dp.doctorId=d.doctorId))) or IF(ds.isHide=0,1=1,find_in_set('"
						+ memberId
						+ "',(SELECT GROUP_CONCAT(DISTINCT(dp.memberId)) from t_scan_keep dp where dp.keepId=d.doctorId ) ))) ";
				if (orderType == 4 || orderType == 5) {// 图文问诊、复诊
					sql += " and ds.isOnlineTwGh=1 ";
				} else if (orderType == 6) {// 图文咨询
					sql += " and ds.isOnlineTwZx=1 ";
				} else if (orderType == 7 || orderType == 9) {// 电话问诊、复诊
					sql += " and ds.isOnlinePhoneGh=1 ";
				} else if (orderType == 8) {// 电话咨询
					sql += " and ds.isOnlinePhoneZx=1 ";
				}
				sql += ") dd where 1=1 and IF(IFNULL(dd.blackCount,0)=0,1=1,find_in_set(dd.doctorId,(SELECT GROUP_CONCAT(dpp.doctorId) from "
						+ " t_doctor_patient dpp where dpp.isBlack=1 and dpp.memberId='"
						+ memberId + "'))=0) ";
				map = super.queryBysqlMap(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 根据服务类型、就诊人ID查询是否存在未完成订单
	 */
	@Override
	public Integer checkNoFinishOrderByPatientId(String patientId,
			String doctorId, Integer orderType) {
		Integer count = 0;
		Map<String, Object> map = null;
		try {
			// String sql =
			// "SELECT count(0) from t_order where patientId=? and doctorId=? and paymentStatus in(2,3) ";
			//
			// if(orderType == 4){
			// sql += " and orderType in(4,7,9) ";
			// }else if (orderType == 5) {// 图文问诊、复诊
			// sql += " and orderType in(4,5) ";
			// } else if (orderType == 7 || orderType == 9) {// 电话问诊、复诊
			// sql += " and orderType in(4,7,9)  ";
			// } else{
			// sql += " and orderType="+orderType;
			// }
			// if (!StringUtil.isEmpty(patientId)) {
			// count = super.queryBysqlCount(sql, new Object[] {
			// patientId,doctorId });
			// }
			String sql = "SELECT paymentStatus,orderType from t_order where patientId=? and doctorId=? and paymentStatus<>1 order by payTime desc limit 0,1";

			if (!StringUtil.isEmpty(patientId)) {
				map = jdbcTemplate.queryForMap(sql, new Object[] { patientId,
						doctorId });
				if ((map.get("paymentStatus").toString().equals("2") || map
						.get("paymentStatus").toString().equals("3"))
						&& ((map.get("orderType").toString().equals("4")
								|| map.get("orderType").toString().equals("7") || map
								.get("orderType").toString().equals("9")))) {
					count = 1;
				}
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return count;
	}

	/**
	 * 根据订单号查询症状描述
	 */
	@Override
	public Map<String, Object> queryOrderSymptomByOrderNo(
			HttpServletRequest request, String orderNo) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT o.paymentStatus,o.patientId orderPatientId,os.*,od.doctorId,i.infirmaryName,CASE pd.sex when 0 THEN '男' ELSE '女' end patientSex,p.`name` positionName,d.docUrl,d.docName,pd.`name` patientName,pd.phone patientPhone,pd.age from t_order o "
					+ " INNER JOIN t_order_detail_server od on od.orderNo = o.orderNo "
					+ " INNER JOIN t_doctor d on d.doctorId = od.doctorId "
					+ " INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
					+ " INNER JOIN t_position p on p.id = d.docPositionId "
					+ " INNER JOIN t_patient_data pd on pd.id=o.patientId "
					+ " LEFT JOIN t_order_symptom os on os.orderNo = o.orderNo "
					+ " where o.orderNo=? ";
			//订单号
			if (!StringUtil.isEmpty(orderNo)) {
				map = super.queryBysqlMap(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 查询同一用户是否有未处理拍方抓药
	 */
	@Override
	public Integer checkPhotoMedicalCount(String memberId) {
		Integer i = 0;
		try {
			String sql = "SELECT count(1) from t_photo_medical_record where state=-1 and memberId=? ";
			if (!StringUtil.isEmpty(memberId)) {
				i = super.queryBysqlCount(sql, new Object[] { memberId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return i;
	}

	/**
	 * 评价页面获取信息
	 */
	@Override
	public Map<String, Object> queryDoctorOrderDetail(String orderNo,
			String memberId) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT e.id evalId,o.orderNo,od.id orderDetailId,d.doctorId,d.docUrl,d.docName,i.infirmaryName,p.`name` positionName "
					+ " FROM t_order o "
					+ " INNER JOIN t_order_detail_server od ON o.orderNo = od.orderNo "
					+ " INNER JOIN t_doctor d on d.doctorId = od.doctorId "
					+ " INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
					+ " INNER JOIN t_position p on p.id = d.docPositionId "
					+ " LEFT JOIN t_evaluate e on e.orderNo = o.orderNo "
					+ " WHERE o.state = 1 AND o.orderNo=? AND o.memberId=? ";
			//订单号，登录人id
			if (!StringUtil.isEmpty(orderNo) && !StringUtil.isEmpty(memberId)) {
				map = super.queryBysqlMap(sql,
						new Object[] { orderNo, memberId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 查询我的锦旗列表
	 */
	@Override
	public List<Map<String, Object>> queryMyEvaBanners(String memberId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT eb.*,IFNULL(myBan.imgCount,0) imgCount from t_eva_banner eb "
					+ " LEFT JOIN (SELECT ifnull(count(me.evaBannerId),0) imgCount,me.evaBannerId FROM t_my_eva_banner me where me.state=1 and me.memberId=? GROUP BY me.evaBannerId) myBan on myBan.evaBannerId=eb.id "
					+ " where eb.state=1 GROUP BY eb.id ORDER BY eb.sort desc ";
			//登录人id
			if (!StringUtil.isEmpty(memberId)) {
				System.out.println(sql);
				list = super.queryBysqlList(sql, new Object[] { memberId });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return list;
	}

	/**
	 * 根据锦旗ID查询锦旗信息
	 */
	@Override
	public Map<String, Object> queryEvaBannerById(String id) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT * from t_eva_banner where id=? ";
			//id
			if (!StringUtil.isEmpty(id)) {
				map = super.queryBysqlMap(sql, new Object[] { id });
			}
		} catch (Exception e) {
			logger.info(e + "异常信息");
		}
		return map;
	}

	/**
	 * 批量增加我的锦旗
	 */
	@Override
	public Object insertMyEvaBanners(MyEvaBanner myEvaBanner,
			List<Map<String, Object>> list) {
		String sql = "insert into t_my_eva_banner(id,memberId,evaBannerId,price,orderNo,state,type,createTime) values";
		Object ok = null;
		try {
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (i == list.size() - 1) {
						if (!StringUtil.isEmpty(list.get(i).get("goodsNum"))
								&& Integer.valueOf(list.get(i).get("goodsNum")
										.toString()) > 2) {
							for (int j = 0; j < Integer.valueOf(list.get(i)
									.get("goodsNum").toString()); j++) {
								if (j == Integer.valueOf(list.get(i)
										.get("goodsNum").toString()) - 1) {
									sql += " ('" + Util.getUUID() + "','"
											+ list.get(i).get("memberId")
											+ "','" + list.get(i).get("id")
											+ "'," + list.get(i).get("price")
											+ ",'" + list.get(i).get("orderNo")
											+ "',1," + list.get(i).get("type")
											+ "," + Util.queryNowTime() + ")";
								} else {
									sql += " ('" + Util.getUUID() + "','"
											+ list.get(i).get("memberId")
											+ "','" + list.get(i).get("id")
											+ "'," + list.get(i).get("price")
											+ ",'" + list.get(i).get("orderNo")
											+ "',1," + list.get(i).get("type")
											+ "," + Util.queryNowTime() + "),";
								}
							}
						} else {
							sql += " ('" + Util.getUUID() + "','"
									+ list.get(i).get("memberId") + "','"
									+ list.get(i).get("id") + "',"
									+ list.get(i).get("price") + ",'"
									+ list.get(i).get("orderNo") + "',1,"
									+ list.get(i).get("type") + ","
									+ Util.queryNowTime() + ")";
						}
					} else {
						if (!StringUtil.isEmpty(list.get(i).get("goodsNum"))
								&& Integer.valueOf(list.get(i).get("goodsNum")
										.toString()) > 2) {
							for (int j = 0; j < Integer.valueOf(list.get(i)
									.get("goodsNum").toString()); j++) {
								sql += " ('" + Util.getUUID() + "','"
										+ list.get(i).get("memberId") + "','"
										+ list.get(i).get("id") + "',"
										+ list.get(i).get("price") + ",'"
										+ list.get(i).get("orderNo") + "',1,"
										+ list.get(i).get("type") + ","
										+ Util.queryNowTime() + "),";
							}
						} else {
							sql += " ('" + Util.getUUID() + "','"
									+ list.get(i).get("memberId") + "','"
									+ list.get(i).get("id") + "',"
									+ list.get(i).get("price") + ",'"
									+ list.get(i).get("orderNo") + "',1,"
									+ list.get(i).get("type") + ","
									+ Util.queryNowTime() + "),";
						}
					}
				}
			}
			ok = jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return ok;
	}

	/**
	 * 根据订单号查询已付款购买锦旗列表
	 */
	@Override
	public List<Map<String, Object>> queryEvaBannerByOrderNo(String orderNo) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "SELECT o.orderNo,e.id,e.price,od.goodsNum,e.type,o.memberId from t_order o "
					+ " INNER JOIN t_order_detail od on od.orderNo = o.orderNo "
					+ " INNER JOIN t_eva_banner e on e.id = od.goodsId "
					+ " where o.orderNo=? ";
			if (!StringUtil.isEmpty(orderNo)) {
				list = super.queryBysqlList(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * 修改锦旗状态
	 */
	@Override
	public Integer updateMyEvaBanState(String memberId,String evaId, String bannerId,
			Integer count) {
		Integer i = 0;
		try {
			if (!StringUtil.isEmpty(bannerId) && !StringUtil.isEmpty(evaId)&&!StringUtil.isEmpty(memberId)) {
				String sql = "UPDATE t_my_eva_banner m,(SELECT GROUP_CONCAT(dd.ids) ids from (SELECT m1.id ids from t_my_eva_banner m1 where m1.evaBannerId='"
						+ bannerId
						+ "' and m1.state=1 and m1.memberId='"+memberId+"' ORDER BY m1.createTime ASC LIMIT 0,"
						+ count
						+ ")dd ) oldBan set m.state=2,m.evaId='"
						+ evaId
						+ "' where m.state=1 and m.memberId='"+memberId+"' and FIND_IN_SET(m.id,oldBan.ids) ";
				i = super.update(sql, null);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}

	/**
	 * 通过订单号查询评论
	 */
	@Override
	public Integer queryEvaByOrderNo(String orderNo) {
		String sql = "select count(1) from t_evaluate where orderNo=?";
		Integer i = 0;
		try {
			i = jdbcTemplate.queryForObject(sql, new Object[] { orderNo },
					Integer.class);
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 批量添加评价标签
	 */
	@Override
	public Object insertEvaLables(JSONArray checkLabels, String evaId) {
		String sql = "insert into t_evaluate_evalabel(id,evaluateId,evalableName) values";
		Object ok = null;
		try {
			if (checkLabels != null && checkLabels.length() > 0) {
				for (int i = 0; i < checkLabels.length(); i++) {
					if (i == checkLabels.length() - 1) {
						sql += " ('" + Util.getUUID() + "','" + evaId + "','"
								+ checkLabels.getString(i) + "')";
					} else {
						sql += " ('" + Util.getUUID() + "','" + evaId + "','"
								+ checkLabels.getString(i) + "'),";
					}

				}
			}
			ok = jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			logger.error(e);
		}
		return ok;
	}

	/**
	 * 查询最新调查
	 */
	@Override
	public List<Map<String, Object>> queryNowInquiry(String memberId,
			Integer page, Integer row) {
		// TODO Auto-generated method stub
		String sql = "select * FROM(select * FROM(SELECT e.id evalId,o.orderNo,o1.orderNo recordOrderNo,o1.mainOrderNo,o.orderType,o.endTime,"
				+ " p.name AS hzname,d.doctorId,o.paymentStatus,os.syZxCount,o.patientId,d.docName AS docName,d.docUrl,i.infirmaryName AS HosName,dp.name AS docPos,"
				+ " IF(os.validityTime IS NULL,NULL,if(os.validityTime -UNIX_TIMESTAMP()<=0,null,CONCAT(floor((os.validityTime -UNIX_TIMESTAMP())/3600),'时',floor((os.validityTime -UNIX_TIMESTAMP())%3600/60),'分'))) sytimes,st.name,"
				+ " CASE WHEN (o.paymentStatus=5 AND e.id is null) THEN '待评价' "
				+ " WHEN (o1.paymentStatus=1 and rr.state=1) THEN '待付款'"
				+ " WHEN (o1.paymentStatus=2 AND o1.orderStatus=0) THEN '待配药'"
				+ " WHEN (o1.paymentStatus=2 AND o1.orderStatus=1) THEN '待发货'"
				+ " WHEN (o1.paymentStatus=2 AND o1.orderStatus=2) THEN '待收货'"
				+ " WHEN (o.paymentStatus=4 ) THEN '已退款'"
				+ " ELSE '问诊中' END wzState,"
				+ " CASE WHEN (o.paymentStatus=5 AND e.id is null) THEN '1'"
				+ " WHEN (o1.paymentStatus=1 and rr.state=1) THEN '2'"
				+ " WHEN (o1.paymentStatus=2 AND o1.orderStatus=0) THEN '3'"
				+ " WHEN (o1.paymentStatus=2 AND o1.orderStatus=1) THEN '4'"
				+ " WHEN (o1.paymentStatus=2 AND o1.orderStatus=2) THEN '5'"
				+ " WHEN (o.paymentStatus=4 ) THEN '7'"
				+ " ELSE '6' END wzType "
				//+ " (SELECT CASE m1.msgType WHEN 1 THEN mc.content WHEN 2 THEN '[图片]' WHEN 3 THEN '[语音]' WHEN 5 THEN '[地址]' WHEN 6 THEN '[症状描述填写]' WHEN 7 THEN '[症状描述填写完成]' WHEN 8 THEN '[问诊单填写]' WHEN 9 THEN '[问诊单填写完成]' WHEN 10 THEN '[复诊单填写]' WHEN 11 THEN '[复诊单填写完成]' WHEN 12 THEN '[调理方案]' WHEN 13 THEN '[调理方案已付款]' WHEN 14 THEN '[赠送提问]' WHEN 15 THEN '[补缴挂号费]' WHEN 16 THEN '[挂号费已补缴]' WHEN 17 THEN '[结束问诊]' WHEN 18 THEN '[消息撤回]' WHEN 19 THEN '[文章推荐]' WHEN 20 THEN '[商品推荐]' WHEN 21 THEN '[坐诊信息]' WHEN 22 THEN '[帮忙认证]' WHEN 23 THEN '[服务购买]' WHEN 24 THEN '[问诊将于10分钟后结束]' WHEN 25 THEN '[已到复诊时间]' WHEN 26 THEN '[调理方案作废]' WHEN 27 THEN '[用药说明]' WHEN 28 THEN '[接通电话成功]' WHEN 29 THEN '[接通电话失败]' WHEN 30 THEN '[新增就诊人抄方]' ELSE '您有一条新消息' END  FROM t_msg m1 INNER JOIN t_msg_content mc ON mc.msgId=m1.id WHERE m1.state =1 and ((m1.from_account=d.doctorId AND m1.to_account=p.id) OR (m1.from_account=p.id AND m1.to_account=d.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastMsg,"
				//+ " (SELECT m1.id  FROM t_msg m1 INNER JOIN t_msg_content mc ON mc.msgId=m1.id WHERE m1.state =1 and ((m1.from_account=d.doctorId AND m1.to_account=p.id) OR (m1.from_account=p.id AND m1.to_account=d.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastMsgId,"
				//+ " (SELECT CASE WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(24 * 60 * 60)THEN from_unixtime(m1.msgTime, '%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(7 * 24 * 60 * 60)THEN from_unixtime(m1.msgTime,'%Y/%m/%d %H:%i') WHEN TO_DAYS(FROM_UNIXTIME(m1.msgTime))=TO_DAYS(NOW())-1 THEN  CONCAT('昨天 ',from_unixtime(m1.msgTime, '%H:%i')) ELSE from_unixtime(m1.msgTime, '%H:%i') END  FROM t_msg m1 WHERE m1.state=1 and ((m1.from_account=d.doctorId AND m1.to_account=p.id) OR (m1.from_account=p.id AND m1.to_account=d.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastTime,"
				//+ " (SELECT m1.msgTime  FROM t_msg m1 WHERE m1.state=1 and ((m1.from_account=d.doctorId AND m1.to_account=p.id) OR (m1.from_account=p.id AND m1.to_account=d.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastMsgTime,"
				//+ " (SELECT if(m1.from_account=p.id,1,2)  FROM t_msg m1 WHERE m1.state=1 and ((m1.from_account=d.doctorId AND m1.to_account=p.id) OR (m1.from_account=p.id AND m1.to_account=d.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastPeople"
				+ " FROM t_order o "
				+ "	INNER JOIN t_order_detail_server os ON os.orderNo=o.orderNo"
				+ "	INNER JOIN t_doctor d ON d.doctorId = os.doctorId"
				+ "	INNER JOIN t_patient_data p ON p.id=o.patientId"
				+ "	INNER JOIN t_infirmary i ON i.infirmaryId=d.infirmaryId"
				+ "	INNER JOIN t_position dp ON dp.id=d.docPositionId"
				+ " INNER JOIN t_server_type st ON st.id=o.orderType"
				+ " LEFT JOIN t_evaluate e ON e.orderNo=o.orderNo"
				+ "	LEFT JOIN t_order o1 ON o1.sourceOrderNo=o.orderNo and o1.state=1 "
				+ "	LEFT JOIN t_recipe_record rr ON rr.orderNo=o1.orderNo and rr.state=1"
				+ "	WHERE o.state=1 and d.docStatus=10 AND o.memberId='" + memberId + "'";

		//sql += " ORDER BY o.payTime DESC) f GROUP BY f.patientId,f.doctorId)e where (e.paymentStatus=2 or (e.paymentStatus=5&&e.syZxCount>0) or (e.paymentStatus=5 and e.endTime is not null and UNIX_TIMESTAMP()-e.endTime<48*60*60)) ORDER BY e.lastMsgTime DESC ";
		sql += " ORDER BY o.payTime DESC,o1.createTime DESC) f GROUP BY f.patientId,f.doctorId)e where ((e.paymentStatus=2 or (e.paymentStatus=5&&e.syZxCount>0) or (e.paymentStatus=5 and e.endTime is not null and UNIX_TIMESTAMP()-e.endTime<48*60*60)))";
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
	 * 查询医生每天已接单数量
	 */
	@Override
	public Map<String, Object> getDoctorOrderDayCount(String doctorId,
			Integer orderType) {
		Map<String, Object> map = null;
		try {
			String sql = "SELECT count(1) dayOrderCount from t_order o WHERE o.paymentStatus in(2,3,5) and o.orderType=? and o.doctorId=? and FROM_UNIXTIME(o.payTime,'%Y-%m-%d')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y-%m-%d') ";
			if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(orderType)) {
				map = super.queryBysqlMap(sql, new Object[] { orderType,
						doctorId });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return map;
	}

	/**
	 * 查询患者是否有未完成抄方订单
	 */
	@Override
	public Integer queryCfOrderByIds(String doctorId, String patientId) {
		Integer count = 0;
		try {
			String sql = "SELECT count(1) from t_order where doctorId=? and patientId=? and state=1 and orderType=22 and paymentStatus in(2,3) ";
			if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
				count = super.queryBysqlCount(sql, new Object[] { doctorId,
						patientId });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return count;
	}
	/**
	 * 新增抄方申请
	 */
	@Override
	public Integer addCopySquare(String wzOrderNo,String img1,String img2,String img3,String snote ,int agentType,int anagraphCount,String patientId)
	{
		int i = 0;
		
		String sql ="INSERT INTO t_order_type22(id,orderNo,img1,img2,img3,created,sex,`name`,agentType,anagraphCount,snote,tel,nexus)"+
		"select '" + Util.getUUID() + "','"+wzOrderNo+"','"+img1+"','"+img2+"','"+img3+"',"+Util.queryNowTime()+",sex,`name`,'"+agentType+"','"+anagraphCount+"','"+snote+"',phone,nexus from t_patient_data where id='"+patientId+"'";
		try{
		i = super.update(sql,null);
		}
		catch(Exception e){
			logger.warn(e);
		}
		return i;
	}
	/**
	 * 查询抄方申请
	 */
	@Override
	public Map<String, Object> lookCfOrder(String orderNo)
	{
		Map<String, Object> obj = null;
		
		String sql =" select * from t_order_type22 where orderNo=?";
	try{
			obj = super.queryBysqlMap(sql,new Object[] { orderNo});
		}
		catch(Exception e){
			logger.warn(e);
		}
		return obj;
	}
	/**
	 * 查询是否添加症状描述
	 */
	@Override
	public Integer querySypCountByOrderNo(String orderNo) {
		Integer count = 0;
		try {
			String sql = "SELECT count(0) from t_order_symptom where orderNo=? ";
			if (!StringUtil.isEmpty(orderNo)) {
				count = super.queryBysqlCount(sql, new Object[] { orderNo });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return count;
	}

	/**
	 * 提交评价锦旗订单与医生挂钩
	 */
	@Override
	public Integer updateJqOrderDoctorId(List<Map<String, Object>> orderNos,
			String doctorId) {
		Integer count = 0;
		try {
			String sql = "UPDATE t_order set doctorId=? where doctorId is NULL and orderNo in( ";
			if (!StringUtil.isEmpty(orderNos)&&!StringUtil.isEmpty(doctorId)) {
				for(int i=0;i<orderNos.size();i++){
					if(i==orderNos.size()-1){
						sql+="'"+orderNos.get(i).get("orderNo")+"'";
					}else{
						sql+="'"+orderNos.get(i).get("orderNo")+"',";
					}
				}
				sql +=")";
				System.out.println(sql);
				count = super.update(sql, new Object[] { doctorId });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return count;
	}

	/**
	 * 提交评价锦旗订单与医生挂钩
	 */
	@Override
	public Integer updateJqOrderDoctorIdPatientId(List<Map<String, Object>> orderNos, String doctorId, String patientId) {
		Integer count = 0;
		try {
			String sql = "UPDATE t_order set doctorId=?, patientId = ? where doctorId is NULL and orderNo in( ";
			if (!StringUtil.isEmpty(orderNos)&&!StringUtil.isEmpty(doctorId)) {
				for(int i=0;i<orderNos.size();i++){
					if(i==orderNos.size()-1){
						sql+="'"+orderNos.get(i).get("orderNo")+"'";
					}else{
						sql+="'"+orderNos.get(i).get("orderNo")+"',";
					}
				}
				sql +=")";
				System.out.println(sql);
				count = super.update(sql, new Object[] { doctorId, patientId });
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return count;
	}
}
