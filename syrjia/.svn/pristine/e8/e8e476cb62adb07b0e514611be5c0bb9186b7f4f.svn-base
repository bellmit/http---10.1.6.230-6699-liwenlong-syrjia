package cn.syrjia.hospital.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.hospital.dao.HospitalDao;
import cn.syrjia.hospital.entity.Doctor;
import cn.syrjia.hospital.entity.SendMsgRecord;
import cn.syrjia.hospital.entity.Symptom;
import cn.syrjia.util.StringUtil;

@Repository("hospitalDao")
public class HospitalDaoImpl extends BaseDaoImpl implements HospitalDao {

	// 日志
	private Logger logger = LogManager.getLogger(HospitalDaoImpl.class);

	/**
	 * 根据医生ID查询医生接单数
	 */
	@Override
	public List<Map<String, Object>> queryOrderTaskByDocId(String docId,
			Integer page, Integer row) {
		List<Map<String, Object>> orderTaskList = new ArrayList<Map<String, Object>>();
		String sql = " select d.*,u.headimgurl,u.nickname from h_doctor_order d "
				+ " INNER JOIN h_weixin_user u on u.openid = d.openid "
				+ " WHERE d.status=10 and (d.paymentStatus='2' or d.paymentStatus='5') and d.doctorId=? "
				+ " order by d.orderDate desc LIMIT "
				+ (page - 1)
				* row
				+ ","
				+ row;
		try {
			//医生id
			if (!StringUtil.isEmpty(docId))
				orderTaskList = super.queryBysqlList(sql,
						new Object[] { docId });
		} catch (Exception e) {
			System.out.println(e);
		}
		return orderTaskList;
	}

	/**
	 * 查询用户详情
	 */
	@Override
	public List<Map<String, Object>> queryUserInfoToSymtom(String openId) {
		String sql = "select dos.age,dos.`name`,dos.sex FROM h_doctor_order_symptom dos INNER JOIN h_doctor_order o ON o.orderNo=dos.orderNo"
				+ " WHERE o.openid=? AND dos.type in (6,7,8) ORDER BY dos.createTime DESC LIMIT 0,3";
		return super.queryBysqlList(sql, new Object[] { openId });
	}

	/**
	 * 查询该用户是否扫该医生进入
	 */
	@Override
	public Map<String, Object> isScanThisDoc(String openId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "select f.followId,d.isProtected,d.isLockUser from h_follow_history f inner join h_doctor d on f.followId=d.doctorId and f.type='0' and "
					+ " d.docStatus='10' and d.docIsOn='1' where f.openId='"
					+ openId + "' and d.isLockUser=1";
			List<Map<String, Object>> maplist = super.queryBysqlList(sql, null);
			if (maplist.size() > 0) {
				map = maplist.get(0);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}

	/**
	 * 设置
	 */
	@Override
	public Integer onOrEndDoctor(Doctor doc) {
		String docId = doc.getDoctorId();
		Integer i = 0;
		String sql = "update h_doctor set docIsOn=? where doctorId=?";
		try {
			if (!StringUtils.isEmpty(docId)) {
				i = super.update(sql, new Object[] { doc.getDocIsOn(), docId });
			}
		} catch (DataAccessException e) {
			logger.warn(e);
		}
		return i;
	}

	/**
	 * 通过订单号删除
	 */
	@Override
	public Integer delSymptomByOrderNo(String orderNo) {
		String sql = "DELETE FROM h_doctor_order_symptom WHERE orderNo=?";
		return super.delete(sql, new Object[] { orderNo });
	}

	/**
	 * 更新医生订单
	 */
	@Override
	public Integer updateDocOrderSign(String orderNo, String sign) {
		if (!StringUtils.isEmpty(orderNo)) {
			String sql = "update h_doctor_order set rsrvStr2=?  where orderNo=?";
			return super.update(sql, new Object[] { sign, orderNo });
		}
		return 0;
	}

	/**
	 * 添加症状描述
	 */
	@Override
	public Object addSymptom(final List<Symptom> symptoms) {
		int[] obj = { 0 };
		String sql = "insert into h_doctor_order_symptom (`id`, `orderNo`, `content`, `visitAddr`, `physicsAddr`, `voiceAddr`, `type`, `createTime`, `name`, `age`, `sex`, `voiceLen`)  "
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
		if (symptoms != null && symptoms.size() > 0) {
			try {
				//批量更新
				obj = super.jdbcTemplate.batchUpdate(sql,
						new BatchPreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								if (symptoms.get(i) != null
										&& symptoms.get(i).getType() != null) {
									ps.setString(1,
											StringUtils.isEmpty(symptoms.get(i)
													.getId()) ? null : symptoms
													.get(i).getId());
									ps.setString(2, StringUtils
											.isEmpty(symptoms.get(i)
													.getOrderNo()) ? null
											: symptoms.get(i).getOrderNo());
									ps.setString(3, StringUtils
											.isEmpty(symptoms.get(i)
													.getContent()) ? null
											: symptoms.get(i).getContent());
									ps.setString(4, StringUtils
											.isEmpty(symptoms.get(i)
													.getVisitAddr()) ? null
											: symptoms.get(i).getVisitAddr());
									ps.setString(5, StringUtils
											.isEmpty(symptoms.get(i)
													.getPhysicsAddr()) ? null
											: symptoms.get(i).getPhysicsAddr());
									ps.setString(6, StringUtils
											.isEmpty(symptoms.get(i)
													.getVoiceAddr()) ? null
											: symptoms.get(i).getVoiceAddr());
									ps.setInt(7, null == symptoms.get(i)
											.getType() ? null : symptoms.get(i)
											.getType());
									ps.setInt(8, null == symptoms.get(i)
											.getCreateTime() ? null : symptoms
											.get(i).getCreateTime());
									ps.setString(9,
											StringUtils.isEmpty(symptoms.get(i)
													.getName()) ? null
													: symptoms.get(i).getName());
									ps.setInt(10, null == symptoms.get(i)
											.getAge() ? 0 : symptoms.get(i)
											.getAge());
									ps.setInt(11, null == symptoms.get(i)
											.getSex() ? 0 : symptoms.get(i)
											.getSex());
									ps.setString(12, StringUtils
											.isEmpty(symptoms.get(i)
													.getVoiceLen()) ? null
											: symptoms.get(i).getVoiceLen());
								}
							}

							@Override
							public int getBatchSize() {
								return symptoms.size();
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 查询发送记录
	 */
	@Override
	public Map<String, Object> querySendRecord(SendMsgRecord smr) {
		String sql = "SELECT * FROM t_sendmsg_record where openId='"
				+ smr.getOpenId() + "' AND docOpenId='" + smr.getDocOpenId()
				+ "'AND unitype='" + smr.getUnitype()
				+ "' order by sendTime DESC LIMIT 0,1";
		return super.queryBysqlMap(sql, null);
	}

}
