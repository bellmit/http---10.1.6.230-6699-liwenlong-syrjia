package cn.syrjia.hospital.dao.impl;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.PatientData;
import cn.syrjia.entity.ShippingAddress;
import cn.syrjia.hospital.dao.AppDoctorDao;
import cn.syrjia.hospital.entity.*;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Repository("appDoctorDao")
public class APPDoctorDaoImpl extends BaseDaoImpl implements AppDoctorDao {

    // 日志
    private Logger logger = LogManager.getLogger(APPDoctorDaoImpl.class);

    /**
     * 获取订单号
     */
    public synchronized String getOrderNo() {
        String orderNo = null;
        try {
            orderNo = jdbcTemplate.execute(new CallableStatementCreator() {
               //执行存储过程
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
     * 根据医生Id查询患者列表
     */
    @Override
    public Map<String, Object> queryPatientsById(String doctorId, String _sign,
                                                 Integer page, Integer row, String name) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT dp.*,d.docStatus,d.docIsOn,CASE WHEN p.nexus='本人' THEN m.photo ELSE '' end imgUrl,o.payTime,p.`name` patientName,p.phone patientPhone,CASE p.sex WHEN 0 THEN '男' else '女' end patientSex,p.age,p.nameShort, "
                        + " substr(p.nameShort,1,1) initialNum,  "
                        + " (SELECT s.`name` from t_order o INNER JOIN t_server_type s on s.id=o.orderType "
                        + " where o.doctorId=dp.doctorId and o.patientId =dp.patientId ORDER BY payTime desc LIMIT 0,1 ) serverName "
                        + " from t_doctor_patient dp "
                        + " INNER JOIN t_doctor d on d.doctorId = dp.doctorId "
                        + " INNER JOIN t_patient_data p on p.id = dp.patientId "
                        + " INNER JOIN t_member m on m.id = p.memberId  "
                        + " INNER JOIN t_order o on o.patientId = dp.patientId and o.doctorId=dp.doctorId and o.state=1 "
                        + " where dp.doctorId= ? "
                        + " and o.orderType in(select s.id from t_server_type s where s.state=1) and d.docStatus='10' and d.docIsOn='1' ";
                //名称
                if (!StringUtil.isEmpty(name)) {
                    sql += " and (p.name like '%" + name
                            + "%' or dp.remarks like '%" + name + "%') ";
                }
                //判断标识
                if (StringUtil.isEmpty(_sign)) {
                    sql += " GROUP BY dp.patientId  ORDER BY p.nameShort asc ";
                } else {
                    if ("xm".equals(_sign)) {
                        sql += " GROUP BY dp.patientId  ORDER BY p.nameShort asc ";
                    } else if ("time".equals(_sign)) {
                        sql += " GROUP BY dp.patientId  ORDER BY time desc ";
                    } else if ("follow".equals(_sign)) {
                        sql += " and dp.isFollow=1 GROUP BY dp.patientId  ORDER BY p.nameShort asc ";
                    } else if ("black".equals(_sign)) {
                        sql += " and dp.isBlack=1 GROUP BY dp.patientId  ORDER BY p.nameShort asc ";
                    }
                }
                int count = 0;
                if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                    count = super.queryBysqlCount("select count(1) from ("
                            + sql + ") fff", null);
                    sql += " limit " + (page - 1) * row + ", " + row;
                }

                String lastContactTimeSql = "select t.patiend_id, max(t.time) AS time from\n" +
                        "((SELECT\n" +
                        "  m1.from_account AS doctor_id,\n" +
                        "\tm1.to_account AS patiend_id,\n" +
                        "\t(CASE\n" +
                        "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 7 * 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%Y/%m/%d %H:%i' ) \n" +
                        "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%m/%d %H:%i' ) \n" +
                        "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime < 24 * 60 * 60 AND TO_DAYS( FROM_UNIXTIME( m1.msgTime ) ) = TO_DAYS( NOW( ) ) - 1 THEN CONCAT( '昨天 ', FROM_UNIXTIME( m1.msgTime, '%H:%i' ) ) ELSE from_unixtime( m1.msgTime, '%H:%i' ) \n" +
                        "\tEND) as time\n" +
                        "FROM t_msg m1 \n" +
                        "WHERE (m1.from_account = ? ))\n" +
                        "union all\n" +
                        "(SELECT\n" +
                        "\tm1.to_account AS doctor_id,\n" +
                        "  m1.from_account AS patiend_id,\n" +
                        "\t(CASE\n" +
                        "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 7 * 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%Y/%m/%d %H:%i' ) \n" +
                        "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%m/%d %H:%i' ) \n" +
                        "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime < 24 * 60 * 60 AND TO_DAYS( FROM_UNIXTIME( m1.msgTime ) ) = TO_DAYS( NOW( ) ) - 1 THEN CONCAT( '昨天 ', FROM_UNIXTIME( m1.msgTime, '%H:%i' ) ) ELSE from_unixtime( m1.msgTime, '%H:%i' ) \n" +
                        "\tEND) as time\n" +
                        "FROM t_msg m1 \n" +
                        "WHERE (m1.to_account = ?))) t\n" +
                        "group by t.patiend_id";

                sql = "select a.*,b.time from (" + sql + ") a left join (" + lastContactTimeSql + ") b on a.patientId=b.patiend_id";

                int start = Util.queryNowTime();
                List<Map<String, Object>> list = super.queryBysqlList(sql, new Object[]{doctorId, doctorId, doctorId});
                logger.info(Util.queryNowTime() - start);

                if (list != null) {
                    count = list.size();

                    /*String sql2 = "select t.patiend_id, max(t.time) AS time from\n" +
                            "((SELECT\n" +
                            "  m1.from_account AS doctor_id,\n" +
                            "\tm1.to_account AS patiend_id,\n" +
                            "\t(CASE\n" +
                            "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 7 * 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%Y/%m/%d %H:%i' ) \n" +
                            "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%m/%d %H:%i' ) \n" +
                            "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime < 24 * 60 * 60 AND TO_DAYS( FROM_UNIXTIME( m1.msgTime ) ) = TO_DAYS( NOW( ) ) - 1 THEN CONCAT( '昨天 ', FROM_UNIXTIME( m1.msgTime, '%H:%i' ) ) ELSE from_unixtime( m1.msgTime, '%H:%i' ) \n" +
                            "\tEND) as time\n" +
                            "FROM t_msg m1 \n" +
                            "WHERE (m1.from_account = ?))\n" +
                            "union all\n" +
                            "(SELECT\n" +
                            "\tm1.to_account AS doctor_id,\n" +
                            "  m1.from_account AS patiend_id,\n" +
                            "\t(CASE\n" +
                            "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 7 * 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%Y/%m/%d %H:%i' ) \n" +
                            "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime > 24 * 60 * 60 THEN from_unixtime( m1.msgTime, '%m/%d %H:%i' ) \n" +
                            "\t\tWHEN UNIX_TIMESTAMP( now( ) ) - m1.msgTime < 24 * 60 * 60 AND TO_DAYS( FROM_UNIXTIME( m1.msgTime ) ) = TO_DAYS( NOW( ) ) - 1 THEN CONCAT( '昨天 ', FROM_UNIXTIME( m1.msgTime, '%H:%i' ) ) ELSE from_unixtime( m1.msgTime, '%H:%i' ) \n" +
                            "\tEND) as time\n" +
                            "FROM t_msg m1 \n" +
                            "WHERE (m1.to_account = ?))) t\n" +
                            "group by t.patiend_id";
                    List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql2, doctorId, doctorId);
                    Map<String, Object> lastContactMap = new HashMap<>();
                    if (maps != null) {
                        for (Map<String, Object> stringObjectMap : maps) {
                            lastContactMap.put(stringObjectMap.get("patiend_id").toString(), stringObjectMap.get("time"));
                        }
                    }
                    for (Map<String, Object> tmpMap : list) {
                        Object _patientId = tmpMap.get("patientId");
                        tmpMap.put("time", lastContactMap.get(_patientId.toString()));
                    }*/
                }

                map.put("patients", list);
                map.put("total", count);
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }

    /**
     * 根据患者ID查询患者管理信息
     */
    @Override
    public Map<String, Object> queryPatientManageById(String patientId,
                                                      String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT id,patientId,isBlack,remarks,isFollow from t_doctor_patient where patientId=? and doctorId=? ";
            if (!StringUtil.isEmpty(patientId)) {
                map = super.queryBysqlMap(sql, new Object[]{patientId,
                        doctorId});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }

    /**
     * 根据患者ID查询患者信息
     */
    @Override
    public Map<String, Object> queryPatientById(String patientId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT p.id,p.`name`,CASE WHEN p.nexus='本人' THEN m.photo ELSE '' end imgUrl,p.phone, "
                    + " CASE p.sex WHEN 0 THEN '男' else '女' end patientSex,p.age,p.memberId "
                    + " from t_patient_data p "
                    + " INNER JOIN t_member m ON m.id = p.memberId "
                    + " where p.id=? ";
            if (!StringUtil.isEmpty(patientId)) {
                map = super.queryBysqlMap(sql, new Object[]{patientId});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }

    /**
     * 查询是否发过通知
     */
    @Override
    public Map<String, Object> querySendRecord(SendMsgRecord smr) {
        String sql = "SELECT * FROM h_sendmsg_record where openId='"
                + smr.getOpenId() + "' AND docOpenId='" + smr.getDocOpenId()
                + "'AND unitype='" + smr.getUnitype()
                + "' order by sendTime DESC LIMIT 0,1";
        return super.queryBysqlMap(sql, null);
    }

    /**
     * 编辑患者管理中信息（特别关注等）
     */
    @Override
    public Integer editPatientManage(DoctorPatient docPatient) {
        Integer count = 0;
        try {
            String sql = "update t_doctor_patient set isBlack=?,remarks=?,isFollow=? where id=? ";
            if (!StringUtil.isEmpty(docPatient.getId())) {
                count = jdbcTemplate.update(
                        sql,
                        new Object[]{docPatient.getIsBlack(),
                                docPatient.getRemarks(),
                                docPatient.getIsFollow(), docPatient.getId()});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return count;
    }

    /**
     * 查询医生当月是否群发
     */
    @Override
    public Integer queryDoctorSendNotice(String doctorId) {
        // TODO Auto-generated method stub
        String sql = "SELECT COUNT(1) FROM t_doctor_notice WHERE DATE_FORMAT( FROM_UNIXTIME(createtime), '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) and isMass=1 and state <>5 AND doctorId=? ";
        Integer i = 0;
        try {
            i = super.queryBysqlCount(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 分页查询医生公告列表
     */
    @Override
    public List<Map<String, Object>> queryNoticeList(String doctorId,
                                                     Integer state, Integer page, Integer row) {
        // TODO Auto-generated method stub
        String sql = "SELECT "
                + "  id,doctorId,title,content,state,isMass,createWay,failNote,CASE WHEN UNIX_TIMESTAMP(now())-createtime>(24*60*60) THEN from_unixtime(createtime,'%Y-%m-%d %H:%i') ELSE from_unixtime(createtime,'%H:%i') END sendtime"
                + " FROM " + "  t_doctor_notice WHERE doctorId ='" + doctorId
                + "'";
        if (StringUtil.isEmpty(state)) {
            sql += " and state NOT IN (2,3) ";
        } else {
            sql += " and state = " + state;
        }
        sql += "  ORDER BY createtime DESC ";
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
     * 新增公告
     */
    @Override
    public Integer addNotice(DoctorNotice doctorNotice) {
        // TODO Auto-generated method stub
        String sql = "insert into t_doctor_notice (id,doctorId,title,content,state,createtime,auditUserId,auditTime,isMass,createWay) VALUES (?,?,?,?,?,?,?,?,?,?) ";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql,
                    new Object[]{Util.getUUID(), doctorNotice.getDoctorId(),
                            doctorNotice.getTitle(), doctorNotice.getContent(),
                            doctorNotice.getState(), Util.queryNowTime(), null,
                            null, doctorNotice.getIsMass(), "doctor"});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 修改公告信息
     */
    @Override
    public Integer editNotice(DoctorNotice doctorNotice) {
        // TODO Auto-generated method stub
        String sql = "update t_doctor_notice set title=? ,content=?,state=?,isMass=? where id=?  ";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{
                    doctorNotice.getTitle(), doctorNotice.getContent(),
                    doctorNotice.getState(), doctorNotice.getIsMass(),
                    doctorNotice.getId()});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 查询公告详情
     */
    @Override
    public Map<String, Object> queryNoticeDetail(String id) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String sql = "SELECT id,doctorId,title,content,state,isMass,failNote from t_doctor_notice where id=?";
            map = jdbcTemplate.queryForMap(sql, new Object[]{id});
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }

    /**
     * 删除公告信息
     */
    @Override
    public Integer deleteNoticeById(String id) {
        // TODO Auto-generated method stub
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(id)) {
                String sql = "update t_doctor_notice set state=3 where id=?";
                i = jdbcTemplate.update(sql, new Object[]{id});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return i;
    }

    /**
     * 查询公告每天发送数量
     */
    @Override
    public Integer checkNoticeDaySendCount(String doctorId) {
        String sql = "SELECT COUNT(1) FROM t_doctor_notice WHERE DATE_FORMAT( FROM_UNIXTIME(createtime), '%Y%m%d' ) = DATE_FORMAT( CURDATE( ) , '%Y%m%d' ) and state in(1,4) AND doctorId=? ";
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                i = super.queryBysqlCount(sql, new Object[]{doctorId});
            }
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 回复用户评价
     */
    @Override
    public Integer replyMemberEva(Evaluate evaluate) {
        Integer i = 0;
        try {
            String sql = "update t_evaluate set `explain`=?,replyState="
                    + evaluate.getReplyState() + " where id=?";
            if (!StringUtil.isEmpty(evaluate.getId())) {
                i = super.update(sql, new Object[]{evaluate.getExplain(),
                        evaluate.getId()});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 修改评价状态
     */
    @Override
    public Integer updateEvaState(Evaluate evaluate) {
        Integer i = 0;
        try {
            String sql = "update t_evaluate set state=" + evaluate.getState()
                    + " where id=?";
            if (!StringUtil.isEmpty(evaluate.getId())) {
                i = super.update(sql, new Object[]{evaluate.getId()});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 查询医生认证信息基本数据
     */
    @Override
    public List<Map<String, Object>> queryDoctorAuthData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "select * from (SELECT i.infirmaryId id,i.infirmaryName name,'infirmary' _sign,'' departId,'' departName,i.infirmaryNameShort short from t_infirmary i where i.state=1 ORDER BY i.infirmaryNameShort ASC ) a "
                    + " UNION select * from (SELECT t.id id,t.`name` name,'position' _sign,'' departId,'' departName,t.nameShort short from t_position t where t.state=1 ORDER BY t.sort desc,t.nameShort ASC) b "
                    + " UNION select * from (SELECT d.departId id,d.departName name,'depart' _sign,d.departId departId,d.departName departName,d.departShort short from t_department d where d.state=1 order by d.departSort desc,d.departShort ASC) c "
                    + " UNION select * from (SELECT ill.illClassId id,ill.illClassName name,'illClass' _sign,ill.departId departId,dd.departName departName,ill.illClassShort short "
                    + " from t_illness_class ill INNER JOIN t_department dd on dd.departId=ill.departId where ill.illClassIsOn='1' and ill.illClassStatus='10' ORDER BY ill.isSort desc,ill.illClassShort ASC) d ";
            list = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return list;
    }

    /**
     * 根据医生ID获取医生认证信息
     */
    @Override
    public Map<String, Object> queryDoctorApplyData(String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT d.applyId,d.docName, d.departId,d.docPrice,d.docUrl,d.docLocalUrl,d.idCardFaceUrl,d.idCardBackUrl,d.pracEoSUrl,d.pracFourUrl,d.docPhyUrl,d.docPhyFourUrl,d.docProfessUrl,d.docSex,d.docPhone,d.docSignature,d.docDesc,d.docStatus,d.docIsOn, "
                    + " d.createTime,d.docAbstract,d.docNotice,d.createWay,d.recordFailNote,d.idCardNo,d.isAcceptAsk,d.infirDepartId,d.docPositionId,d.basicIsOn,d.picIsOn,d.infirmaryId,d.applyUserId,d.docProfessUrlTwo, "
                    + " d.saleTj,d.recordPicFailNote,dd.docIsOn doctorIsOn,ifnull(dd.docUrl,'https://mobile.syrjia.com/syrjia/img/defaultPhoto.png') imgUrl,ff.followId,i.infirmaryName,p.`name` positionName, "
                    + " (SELECT group_concat(DISTINCT(ddd.departId) ORDER BY departSort DESC) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.applyId)) departNames, "
                    + " (SELECT group_concat(DISTINCT(ddd.departName)) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.applyId)) departName, "
                    + " (SELECT group_concat(DISTINCT(ill.illClassId)) FROM t_illness_class ill WHERE ill.illClassId in(SELECT miu.illClassId from t_middle_util miu where miu.doctorId=d.applyId)) illClassNames,"
                    + "	(SELECT GROUP_CONCAT(DISTINCT(ill.illClassName) SEPARATOR '、') FROM t_illness_class ill WHERE ill.illClassId in(SELECT miu.illClassId from t_middle_util miu where miu.doctorId=d.applyId)) illClassName"
                    + " from t_doctor_apply_record d "
                    + " INNER JOIN t_doctor dd on dd.doctorId = d.applyId "
                    + " left JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
                    + " left JOIN t_position p on p.id = d.docPositionId "
                    + " LEFT JOIN (SELECT f.followId,f.openId from t_follow_history f inner join t_sales_represent ssr on ssr.srId=f.followId ORDER BY f.followTime DESC LIMIT 0,1) ff ON ff.openId = d.applyId "
                    + " where d.docStatus='10' and applyId =? ";
            System.out.println(sql);
            if (!StringUtil.isEmpty(doctorId)) {
                map = super.queryBysqlMap(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }
    
    /**
     * 根据医生ID获取医生个人信息（医馆APP个人中心用）
     */
    @Override
    public Map<String, Object> queryOneDoctor(String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT d.doctorId,dr.docName,fh.followId pid,d.isAccpetAsk,d.idCardNo,d.copyBarCode cfQrCodeUrl,ifnull(docabstr.docAbstract,d.docAbstract)docAbstract,d.docPhone,d.docIsOn,d.docPositionId,d.docSex,d.docSignature,d.docUrl,d.infirmaryId,d.qrCodeUrl,i.infirmaryName,p.`name` positionName,d.isRecommended,d.docDesc,d.createTime,d.customYztId,d.defaultYztId,"
                    + " CASE WHEN dr.docIsOn='0' THEN '待审核' WHEN dr.docIsOn='2' THEN '未通过审核' WHEN dr.docIsOn='-1' THEN '未认证' when dr.docIsOn='1' THEN '已认证'  ELSE '已认证' END applyStatus, "
                    + " dr.docIsOn type, "
                    + " (SELECT group_concat(ddd.departName) FROM t_department ddd WHERE ddd.departId in(SELECT ddu.departId from t_doc_depart_util ddu where ddu.doctorId=d.doctorId)) departNames, "
                    + " (SELECT group_concat(ill.illClassName) FROM t_illness_class ill WHERE ill.illClassId in(SELECT miu.illClassId from t_middle_util miu where miu.doctorId=d.doctorId)) illClassNames "
                    + " from t_doctor d "
                    + " left JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
                    + " left JOIN t_position p on p.id = d.docPositionId "
                    + " left join t_follow_history fh on fh.openId= d.doctorId "
                    + " LEFT JOIN t_doctor_apply_record dr on dr.applyId = d.doctorId  "
                    + "	LEFT JOIN (select dar.doctorId,dar.content docAbstract from t_doctor_abstract_record dar order by dar.auditTime desc limit 0,1) docabstr on  docabstr.doctorId = d.doctorId "
                    + " where d.docStatus='10' and d.doctorId =? ";
            System.out.println(sql);
            if (!StringUtil.isEmpty(doctorId)) {
                map = super.queryBysqlMap(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }

    /**
     * 修改医生头像、手机号
     */
    @Override
    public Integer updateDoctor(String doctorId, String localUrl, String url,
                                String phone) {
        Integer i = 0;
        try {
            String sql = "";
            //地址
            if (!StringUtil.isEmpty(url)) {
                sql = "UPDATE t_doctor set docUrl='" + url + "',docLocalUrl='"
                        + localUrl + "' ";
            }
            //电话
            if (!StringUtil.isEmpty(phone)) {
                sql = "UPDATE t_doctor set docPhone='" + phone + "' ";
            }
            //医生id
            if (!StringUtil.isEmpty(sql) && !StringUtil.isEmpty(doctorId)) {
                sql += " where doctorId='" + doctorId + "' ";
                i = super.update(sql, null);
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return i;
    }

    /**
     * 检验医生手机号是否存在（修改手机号验证用）
     */
    @Override
    public Integer checkDoctorPhone(String docPhone, String doctorId) {
        Integer count = 0;
        try {
            String sql = "SELECT count(1) from t_doctor d where d.docStatus<>'30' ";
            //医生电话
            if (!StringUtil.isEmpty(docPhone)) {
                sql += " and d.docPhone='" + docPhone + "' ";
                //医生id
                if (!StringUtil.isEmpty(doctorId)) {
                    sql += " and d.doctorId='" + doctorId + "' ";
                }
                count = super.queryBysqlCount(sql, null);
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return count;
    }

    /**
     * 检验医生认证时医生名称是否重复
     */
    @Override
    public Integer checkApplyDocName(String docName, String doctorId) {
        Integer count = 0;
        try {
            String sql = "SELECT count(1) from t_doctor d where d.docStatus<>'30' ";
            //医生名称
            if (!StringUtil.isEmpty(docName)) {
                sql += " and d.docName='" + docName + "' ";
                //医生id
                if (!StringUtil.isEmpty(doctorId)) {
                    sql += " and d.doctorId<>'" + doctorId + "' ";
                }
                count = super.queryBysqlCount(sql, null);
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return count;
    }

    /**
     * 通过手机号查询医生信息
     */
    @Override
    public String queryDoctorByPhone(String phone) {
        String sql = "select doctorId from t_doctor where docPhone=? and docStatus='10' ";
        String doctorId = null;
        try {
            doctorId = jdbcTemplate.queryForObject(sql, new Object[]{phone},
                    String.class);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return doctorId;
    }

    /**
     * 登录没有医生及注册
     */
    @Override
    public String loginAddDoctor(String docPhone, String docName,
                                 String doctorSign, Integer isDownLoadApp, Integer downTime,String defaultYztId,String saleId) {
        String doctorId = Util.getUUID();
        String sql = "insert into t_doctor(doctorId,docPhone,docStatus,isLocalDoc,docName,createTime,docUrl,isOld,isDownLoadApp,downTime,defaultYztId,salesId) values(?,?,?,?,?,?,?,?,?,?,?,?) ";
        try {
            jdbcTemplate.update(sql, new Object[]{doctorId, docPhone, 10, 1,
                    docName, Util.queryNowTime(),
                    "https://mobile.syrjia.com/syrjia/img/defaultPhoto.png", 0,
                    isDownLoadApp, downTime,defaultYztId,saleId});
        } catch (DataAccessException e) {
            logger.error(e);
            return null;
        }
        return doctorId;
    }

    /**
     * 批量增加坐诊信息
     */
    @Override
    public Object insertDoctorZzData(DoctorZzData zzData, List<String> dates) {
        String sql = "insert into t_doctor_zz_data(id,doctorId,zzDate,zzState,infirmaryId,price,num,createTime,repeatId,repeatWay,isYzt) values ";
        Object ok = null;
        try {
            if (dates != null && dates.size() > 0) {
                for (int i = 0; i < dates.size(); i++) {
                    if (i == dates.size() - 1) {
                        sql += " ('" + Util.getUUID() + "','"
                                + zzData.getDoctorId() + "','" + dates.get(i)
                                + "'," + zzData.getZzState() + ",'"
                                + zzData.getInfirmaryId() + "',"
                                + zzData.getPrice() + "," + zzData.getNum()
                                + "," + Util.queryNowTime() + ",'"
                                + zzData.getRepeatId() + "',"
                                + zzData.getRepeatWay() + ","
                                + zzData.getIsYzt() + ") ";
                    } else {
                        sql += " ('" + Util.getUUID() + "','"
                                + zzData.getDoctorId() + "','" + dates.get(i)
                                + "'," + zzData.getZzState() + ",'"
                                + zzData.getInfirmaryId() + "',"
                                + zzData.getPrice() + "," + zzData.getNum()
                                + "," + Util.queryNowTime() + ",'"
                                + zzData.getRepeatId() + "',"
                                + zzData.getRepeatWay() + ","
                                + zzData.getIsYzt() + "), ";
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
     * 根据repeatId,重复方式删除
     */
    @Override
    public Integer deleteZzDataByRepeatId(String repeatId, String checkDate) {
        Integer i = 0;
        try {
            String sql = "DELETE from t_doctor_zz_data where repeatId=? and zzDate>? ";
            if (!StringUtil.isEmpty(repeatId) && !StringUtil.isEmpty(checkDate)) {
                i = super.delete(sql, new Object[]{repeatId, checkDate});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 验证医生手势密码、数字密码是否正确
     */
    @Override
    public Integer checkDoctorPassword(DoctorSet doctorSet) {
        Integer i = 0;
        try {
            String sql = "SELECT COUNT(0) from t_doctor_set where doctorId=? ";
            if (!StringUtil.isEmpty(doctorSet.getDoctorId())) {
                if (!StringUtil.isEmpty(doctorSet.getGesturePassword())) {
                    sql += "and gesturePassword='"
                            + doctorSet.getGesturePassword() + "' ";
                }
                if (!StringUtil.isEmpty(doctorSet.getSecretPassword())) {
                    sql += " and secretPassword='"
                            + doctorSet.getSecretPassword() + "' ";
                }
                i = super.queryBysqlCount(sql,
                        new Object[]{doctorSet.getDoctorId()});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 根据医生ID查询医生评价信息（详情页面与评价列表共用）
     */
    @Override
    public List<Map<String, Object>> queryDoctorEvaList(String doctorId,
                                                        Integer row, Integer page, String _sign) {
        // 还不完善，还差症状描述
        List<Map<String, Object>> evalist = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT e.*,s.name serverName,pd.name realname,if(pd.nexus='本人',m.photo,'') memberUrl,if(pd.sex=0,'男','女')sex,"
                    + " (SELECT GROUP_CONCAT(ee.evalableName) from t_evaluate_evalabel ee where ee.evaluateId=e.id ) labelNames "
                    + " ,FROM_UNIXTIME(e.createTime,'%Y-%m-%d %H:%i') wzDate,ifnull(os.symptomDescribe,'无') diagonsticName from t_evaluate e "
                    + " INNER JOIN t_order o on o.orderNo = e.orderNo "
                    + " INNER JOIN t_server_type s on s.id = o.orderType "
                    + " INNER JOIN t_doctor d on d.doctorId = e.goodsId "
                    + " INNER JOIN t_member m on m.id = e.memberId "
                    + " INNER JOIN t_patient_data pd on pd.id = o.patientId "
                    + " left join t_order_symptom os on os.orderNo = o.orderNo";
            //医生id
            if (!StringUtil.isEmpty(doctorId)) {
                sql += " where e.id is not null and e.state in (1,4) and e.goodsId='"
                        + doctorId + "' ";
                //标识
                if (!StringUtil.isEmpty(_sign)) {
                    if ("good".equals(_sign)) {
                        sql += " and e.evaluateLevel >=4  ";
                    } else if ("middle".equals(_sign)) {
                        sql += " and e.evaluateLevel >=2 and e.evaluateLevel <=3 ";
                    } else if ("low".equals(_sign)) {
                        sql += " and e.evaluateLevel =1  ";
                    } else if ("noReply".equals(_sign)) {
                        sql += " and e.`explain` is null  ";
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
     * 根据医生ID查询医生评价统计信息
     */
    @Override
    public Map<String, Object> queryDoctorEvaCensus(String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT ifnull(docNoCnt.evaNoCnt,0) noReplyCnt,IFNULL(docEva.evaCnt,0) totalEvaCnt,IFNULL(docHaoCnt.evaHaoCnt,0) goodEvaCnt,IFNULL(doCMiddelCnt.evaMiddleCnt,0) middleEvaCnt, "
                    + " IFNULL(doCLowCnt.evaLowCnt,0) lowEvaCnt,ROUND(IFNULL(doCGoodCnt.evaGoodCnt,0)/IFNULL(docEva.evaCnt,1)*100,1) goodEvaRate FROM t_doctor d "
                    + " left join (SELECT COUNT(1) evaCnt,e.goodsId FROM t_evaluate e INNER JOIN t_order o ON o.orderNo = e.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e.goodsId  INNER JOIN t_member m ON m.id = e.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  WHERE e.state in (1,4) GROUP BY e.goodsId ) docEva on docEva.goodsId = d.doctorId "
                    + " left join (SELECT COUNT(1) evaGoodCnt,e1.goodsId FROM t_evaluate e1 INNER JOIN t_order o ON o.orderNo = e1.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e1.goodsId  INNER JOIN t_member m ON m.id = e1.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  WHERE e1.state in (1,4) and e1.evaluateLevel>=2  GROUP BY e1.goodsId ) doCGoodCnt on doCGoodCnt.goodsId = d.doctorId "
                    + " left join (SELECT COUNT(1) evaHaoCnt,e1.goodsId FROM t_evaluate e1 INNER JOIN t_order o ON o.orderNo = e1.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e1.goodsId  INNER JOIN t_member m ON m.id = e1.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  WHERE e1.state in (1,4) and e1.evaluateLevel in(4,5)  GROUP BY e1.goodsId ) docHaoCnt on docHaoCnt.goodsId = d.doctorId "
                    + " left join (SELECT COUNT(1) evaMiddleCnt,e1.goodsId FROM t_evaluate e1 INNER JOIN t_order o ON o.orderNo = e1.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e1.goodsId  INNER JOIN t_member m ON m.id = e1.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  WHERE e1.state in (1,4) and e1.evaluateLevel in(2,3) GROUP BY e1.goodsId ) doCMiddelCnt on doCMiddelCnt.goodsId = d.doctorId "
                    + " left join (SELECT COUNT(1) evaLowCnt,e1.goodsId FROM t_evaluate e1 INNER JOIN t_order o ON o.orderNo = e1.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e1.goodsId  INNER JOIN t_member m ON m.id = e1.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  WHERE e1.state in (1,4) and e1.evaluateLevel =1 GROUP BY e1.goodsId ) doCLowCnt on doCLowCnt.goodsId = d.doctorId "
                    + " left join (SELECT COUNT(1) evaNoCnt,e1.goodsId FROM t_evaluate e1 INNER JOIN t_order o ON o.orderNo = e1.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e1.goodsId  INNER JOIN t_member m ON m.id = e1.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  WHERE  e1.state in (1,4) and (e1.`explain` is null or e1.`explain`='') GROUP BY e1.goodsId ) docNoCnt on docNoCnt.goodsId = d.doctorId "
                    + " where d.docStatus='10' AND d.docIsOn='1' and d.doctorId=? ";
            //医生id
            if (!StringUtil.isEmpty(doctorId)) {
                System.out.println(sql);
                map = super.queryBysqlMap(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.info(e + "异常信息");
        }
        return map;
    }

    /**
     * 修改状态
     */
    @Override
    public Integer updateChannelState(String id) {
        Integer i = 0;
        String channelSql = " update t_channel set state=2 where id=? ";
        try {
            if (!StringUtil.isEmpty(id)) {
                i = super.update(channelSql, new Object[]{id});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 删除医生疾病分类关联
     */
    @Override
    public Integer delMiddleByDocId(String id) {
        Integer i = 0;
        String sql = " DELETE f.* from t_middle_util f where f.type='1' and f.doctorId=? ";
        try {
            if (!StringUtil.isEmpty(id)) {
                i = super.delete(sql, new Object[]{id});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 根据医生ID删除医生部门关联
     */
    @Override
    public Integer delDocAndDepartUtil(String docId, String departId) {
        Integer i = 0;
        try {
            String sql = "DELETE FROM t_doc_depart_util where 1=1 ";
            if (!StringUtils.isEmpty(departId) || !StringUtils.isEmpty(docId)) {
                //医生id
            	if (!StringUtils.isEmpty(docId)) {
                    sql += " and doctorId='" + docId + "' ";
                }
            	//部门id
                if (!StringUtils.isEmpty(departId)) {
                    sql += " and departId='" + departId + "' ";
                }
                i = super.delete(sql, null);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 检验医生自己设置是否存在
     */
    @Override
    public String checkDocSetById(String doctorId) {
        String id = null;
        try {
            String sql = "SELECT id from t_doctor_set where doctorId=? ";
            if (!StringUtil.isEmpty(doctorId)) {
                id = jdbcTemplate.queryForObject(sql,
                        new Object[]{doctorId}, String.class);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return id;
    }

    /**
     * 查询医生有没有待审核的简介
     */
    @Override
    public Integer queryAbstractRecordByDoctorId(String doctorId) {
        Integer count = null;
        try {
            String sql = "SELECT count(1) from t_doctor_abstract_record where doctorId=? and state=0";
            if (!StringUtil.isEmpty(doctorId)) {
                count = jdbcTemplate.queryForObject(sql,
                        new Object[]{doctorId}, Integer.class);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return count;
    }

    /**
     * 根据医生ID查询医生14天出诊状态
     */
    @Override
    public List<Map<String, Object>> queryFourTeenZzStatus(String doctorId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT date_format(replace(y.tdate,'-',''),'%Y年%c月%e日') showDate,y.tdate,y.tday,case when ((max(dz.isHis)=1 and (dz.num-IFNULL(xxData.xxCount,0))>0) or max(dz.isYzt)=1) THEN '可预约' when (max(dz.isHis)=1 and (dz.num-IFNULL(xxData.xxCount,0))<=0) THEN '约满' WHEN max(dz.isHis)=0 THEN '出诊' ELSE '休' END zzStaus,case when ((max(dz.isHis)=1 and (dz.num-IFNULL(xxData.xxCount,0))>0) or max(dz.isYzt)=1) THEN 'kyy' when (max(dz.isHis)=1 and (dz.num-IFNULL(xxData.xxCount,0))<=0) THEN 'ym' WHEN max(dz.isHis)=0 THEN 'cz' ELSE 'x' END zzStausSign from t_year_date y  "
                    + "	left JOIN t_doctor_zz_data dz on y.tdate = dz.zzDate and dz.doctorId=? and dz.state=1"
                    + " LEFT JOIN (SELECT count(1) xxCount,os.doctorId,os.xxGhDate from t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo where o.orderType=11 and o.paymentStatus in(2,5)) xxData on xxData.doctorId=dz.doctorId and xxData.xxGhDate=dz.zzDate "
                    + "	where y.tdate>=DATE_FORMAT(NOW(),'%Y-%m-%d') and y.tdate<=DATE_SUB(CURDATE(), INTERVAL -13 DAY) "
                    + "	GROUP BY y.tdate ORDER BY y.tdate ASC";
            if (!StringUtil.isEmpty(doctorId)) {
                list = super.queryBysqlList(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 查询一周日期
     */
    @Override
    public List<Map<String, Object>> queryWeekData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT CASE dd.wekcount WHEN 1 THEN '一' WHEN 2 THEN '二' WHEN 3 THEN '三' WHEN 4 THEN '四' WHEN 5 THEN '五' WHEN 6 THEN '六' ELSE '日' end weeks "
                    + " from (SELECT date_format(y.tdate,'%w') wekcount from t_year_date y "
                    + " where y.tdate>=DATE_FORMAT(NOW(),'%Y-%m-%d') and y.tdate<=DATE_SUB(CURDATE(), INTERVAL -6 DAY) "
                    + " GROUP BY y.tdate ORDER BY y.tdate ASC ) dd";
            list = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 删除坐诊信息
     */
    @Override
    public Integer updateDoctroZzState(String id) {
        Integer i = 0;
        try {
            String sql = "UPDATE t_doctor_zz_data set state=3 where id=? ";
            if (!StringUtil.isEmpty(id)) {
                i = super.update(sql, new Object[]{id});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 获取医院列表
     */
    @Override
    public List<Map<String, Object>> queryInfirmaryList(String name,
                                                        Integer page, Integer row) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT i.infirmaryId,i.infirmaryName,i.infirmaryNameShort,i.latitude,i.longitude,i.imageUrl from t_infirmary i where i.state=1 ";
            //医院
            if (!StringUtil.isEmpty(name)) {
                sql += " and (i.infirmaryName like '%" + name
                        + "%' or i.infirmaryNameShort like '%" + name + "%') ";
            }
            sql += " ORDER BY i.infirmaryNameShort ASC ";
            if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                sql += " limit " + (page - 1) * row + " , " + row;
            }
            list = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 根据坐诊ID查询坐诊信息
     */
    @Override
    public Map<String, Object> queryDoctorZzDataById(String id) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT d.*,i.infirmaryName,(SELECT GROUP_CONCAT(DISTINCT(dd.zzDate)) from t_doctor_zz_data dd where dd.repeatId=d.id and dd.state=1 and dd.repeatWay=4 ) dates  from t_doctor_zz_data d "
                    + " INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
                    + " where d.id=? and d.state=1 ";
            if (!StringUtil.isEmpty(id)) {
                map = super.queryBysqlMap(sql, new Object[]{id});
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
                                                       String zzDate, Integer state) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (StringUtil.isEmpty(zzDate)) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                zzDate = sdf.format(d);
            }
            String sql = "SELECT dz.isHis,dz.zzState,dz.id,dz.price,if(dz.isHis=1,dz.num,0) totalNum,(if(dz.isHis=1,dz.num,0) -IFNULL(xxData.xxCount,0)) sjNum,i.infirmaryName,CONCAT(i.provieceName,i.cityName,i.countyName,i.address) address "
                    + " from t_doctor_zz_data dz "
                    + " INNER JOIN t_infirmary i on i.infirmaryId = dz.infirmaryId "
                    + " LEFT JOIN (SELECT count(1) xxCount,os.doctorId,os.xxGhDate,os.xxGhTime from t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo where o.orderType=11 and o.paymentStatus in(2,5)) xxData on xxData.doctorId=dz.doctorId and xxData.xxGhDate=dz.zzDate and xxData.xxGhTime=dz.zzState "
                    + " where dz.state=1 and dz.doctorId=? and dz.zzDate=? and dz.zzState=? ORDER BY dz.createTime deSC ";
            if (!StringUtil.isEmpty(doctroId) && state != null) {
                list = super.queryBysqlList(sql, new Object[]{doctroId,
                        zzDate, state});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 查询医生坐诊时间列表
     */
    @Override
    public List<Map<String, Object>> queryDoctorZzTimes(String doctroId,
                                                        String zzDate) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (StringUtil.isEmpty(zzDate)) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                zzDate = sdf.format(d);
            }
            String sql = "SELECT dz.zzState, CASE dz.zzState WHEN 0 THEN '上午出诊' WHEN 3 THEN '全天出诊' WHEN 1 THEN '下午出诊' ELSE '晚上出诊' END czTime "
                    + " from t_doctor_zz_data dz "
                    + " where dz.state=1 and dz.doctorId=? and dz.zzDate=? GROUP BY dz.zzState ORDER BY zzState ASC";
            if (!StringUtil.isEmpty(doctroId)) {
                list = super.queryBysqlList(sql, new Object[]{doctroId,
                        zzDate});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 根据医生id查询问诊/复诊模板
     */
    @Override
    public Map<String, Object> querySpecialTest(String doctroId) {
        String sql = "select f.id,f.testName,f.type,f.isDoctor,IF(f.isDefault=0,2,1) isDefault,f.createTime from (SELECT st.id,st.testName,st.type,st.isDoctor,(SELECT COUNT(1) FROM t_doctor_special ds WHERE ds.specialId=st.id AND doctorId=?)isDefault,st.createTime FROM t_special_test st where state=1 AND (createUserId=? OR isDoctor=2))f ORDER BY f.isDefault DESC,f.createTime";
        final List<Map<String, Object>> interrogation = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> visit = new ArrayList<Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            jdbcTemplate.query(sql, new Object[]{doctroId, doctroId},
                    new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs)
                                throws SQLException {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("Id", rs.getString("id"));
                            map.put("testName", rs.getString("testName"));
                            map.put("isDoctor", rs.getInt("isDoctor"));
                            map.put("isDefault", rs.getInt("isDefault"));
                            if (rs.getInt("type") == 1) {
                                interrogation.add(map);
                            } else {
                                visit.add(map);
                            }
                        }
                    });
            result.put("interrogation", interrogation);
            result.put("visit", visit);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return result;
    }

    /**
     * 查询名著
     */
    @Override
    public List<Map<String, Object>> querySpecialTestClassic(String name,
                                                             Integer page, Integer row) {
        String sql = "select dc.id,dc.name,dc.provenance,dc.author,dc.attending,GROUP_CONCAT(CONCAT_WS('',dm.`name`,'(',ROUND(dcd.weight),'克',')') ORDER BY dcd.optionNum SEPARATOR '  ') drug from t_doctor_conditioning dc INNER JOIN t_doctor_conditioning_detail dcd ON dcd.conditioningId=dc.id INNER JOIN t_drug_master dm ON dm.id=dcd.drugId where dc.state=1 and dc.type=2 ";
       //名称
        if (!StringUtil.isEmpty(name)) {
            sql += " and dc.name like '%" + name + "%' ";
        }
        //分组分页
        sql += " GROUP BY dc.id limit " + (page - 1) * row + "," + row;
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过医生id查询
     */
    @Override
    public List<Map<String, Object>> querySpecialTestByDoctorId(
            String doctorId, Integer type, String testId) {
        String sql = "SELECT id Id,testName,isDoctor,isDefault FROM t_special_test where state=1 AND createUserId=? and type=?";
        //id
        if (!StringUtil.isEmpty(testId)) {
            sql += " and id <> '" + testId + "'";
        }
        //排序id
        sql += " ORDER BY isDefault,createTime";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = jdbcTemplate.queryForList(sql,
                    new Object[]{doctorId, type});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过id查询
     */
    @Override
    public Map<String, Object> querySpecialTestById(String testId) {
        String sql = "SELECT id,testName,isDoctor,isTongue,isSurface,isOther,otherName,type FROM t_special_test where state=1 AND id=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 通过id查询调理方
     */
    @Override
    public Map<String, Object> queryConditioningById(String conditioningId) {
        String sql = "SELECT id,name,isDoctor FROM t_doctor_conditioning where state=1 AND id=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate
                    .queryForMap(sql, new Object[]{conditioningId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据医生id查询调理方/经典方
     */
    @Override
    public Map<String, Object> queryConditioning(String doctroId) {
        String sql = "SELECT id,name,type,isDoctor,2 isDefault FROM t_doctor_conditioning where state=1 AND (createUserId=? OR isDoctor=2) ORDER BY createTime";
        final List<Map<String, Object>> commonly = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> classic = new ArrayList<Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            jdbcTemplate.query(sql, new Object[]{doctroId},
                    new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs)
                                throws SQLException {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("Id", rs.getString("id"));
                            map.put("testName", rs.getString("name"));
                            map.put("isDoctor", rs.getInt("isDoctor"));
                            map.put("isDefault", rs.getInt("isDefault"));
                            if (rs.getInt("type") == 1) {
                                commonly.add(map);
                            } else {
                                classic.add(map);
                            }
                        }
                    });
            result.put("interrogation", commonly);// 常用
            result.put("visit", classic);// 经典
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return result;
    }

    /**
     * 通过医生id查询调理方
     */
    @Override
    public List<Map<String, Object>> queryConditioningByDoctorId(
            String doctorId, String name, Integer type, String conditioningId,
            Integer page, Integer row) {
        String sql = "SELECT * FROM (SELECT dc.id Id, dc.name,FROM_UNIXTIME(dc.createTime,'%Y-%m-%d %h:%i') time,dc.provenance,dc.author,dc.attending,GROUP_CONCAT(CONCAT_WS('',dm.`name`,dcd.weight,'克') SEPARATOR ' ') drugName,0 agentType FROM t_doctor_conditioning dc inner join t_doctor_conditioning_detail dcd on dcd.conditioningId=dc.id inner join t_drug_master dm on dm.id=dcd.drugId where dm.state=1 and dc.state=1 ";
        //创建用户id
        if (type == 1) {
            sql += " AND dc.createUserId='" + doctorId + "'";
        }
        //名称
        if (!StringUtil.isEmpty(name)) {
            sql += " and dc.name like'%" + name + "%'";
        }
        //调理方id
        if (!StringUtil.isEmpty(conditioningId)) {
            sql += " and dc.id <>  '" + conditioningId + "'";
        }
        //类型、分组
        sql += " and dc.type=? group by dc.id ORDER BY dc.createTime desc) f WHERE f.Id IS NOT NULL ";
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
            sql += " limit " + (page - 1) * row + "," + row;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{type});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过医生id查询历史方
     */
    @Override
    public List<Map<String, Object>> queryHistoryConditioningByDoctorId(
            String doctroId, String name, Integer type, String conditioningId,
            String patientId, Integer page, Integer row) {
        String sql = "SELECT cr.id,cr.recipeName AS name,'' time,'' provenance,'' author,'' attending,cr.agentType FROM t_recipe_record rr INNER JOIN t_conditioning_record cr on cr.recordId=rr.recordId INNER JOIN t_drug_record dr ON dr.conditionId=cr.id INNER JOIN t_health_products hp ON hp.id=dr.drugId INNER JOIN t_drug_master dm ON dm.id=hp.masterId inner join t_order o on o.recordId=rr.recordId WHERE rr.doctorId=? ";
        //患者id
        if (!StringUtil.isEmpty(patientId)) {
            sql += " and o.patientId='" + patientId + "'";
        }
        //分组、排序
        sql += " group by cr.id ORDER BY cr.createTime desc";

        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
            sql += " limit " + (page - 1) * row + "," + row;
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{doctroId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过id查询调理方详情
     */
    @Override
    public List<Map<String, Object>> queryConditioningDetailById(String id) {
        String sql = "SELECT dcd.drugId, dcd.weight, dm.`name` FROM t_doctor_conditioning_detail dcd INNER JOIN t_drug_master dm ON dm.id = dcd.drugId WHERE dm.state = 1 AND dcd.conditioningId =? ORDER BY optionNum ASC";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{id});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过id查询历史方
     */
    @Override
    public List<Map<String, Object>> queryHistoryConditioningDetailById(
            String id) {
        String sql = "SELECT dm.`name`,dr.drugDose weight,dm.id drugId,dr.util FROM t_conditioning_record cr INNER JOIN t_drug_record dr ON dr.conditionId=cr.id INNER JOIN t_health_products hp ON hp.id=dr.drugId INNER JOIN t_drug_master dm ON dm.id=hp.masterId WHERE dm.state = 1  and cr.id=? order by dr.drugOrder ";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{id});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 根据查询问诊/复诊模板id查询模板详情
     */
    @Override
    public List<Map<String, Object>> querySpecialTestDetail(String testId) {
        String sql = "SELECT id,qid,titleName,optionType,isMandatory FROM t_special_test_title WHERE specialTestId=? order by qid asc";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查最新的id
     */
    @Override
    public Integer querySpecialMaxQid(String testId) {
        String sql = "select ifnull(max(qid),0) from t_special_test_title where specialTestId=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.queryForObject(sql, new Object[]{testId},
                    Integer.class);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询标题
     */
    @Override
    public Map<String, Object> querySpecialTestTitle(String titleId) {
        String sql = "select * from t_special_test_title where id=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, new Object[]{titleId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询题目
     */
    @Override
    public List<Map<String, Object>> querySpecialTestDetailOption(
            String detailId) {
        String sql = "SELECT id,optionNum,optionName FROM t_special_test_title_options WHERE titleId=? order by optionNum";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{detailId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> queryConditioningDetail(String detailId) {
        String sql = "SELECT hp.id drugId,optionNum,hp.name drugName,dcd.weight FROM t_doctor_conditioning_detail dcd inner join t_drug_master hp on hp.id=dcd.drugId WHERE conditioningId=? order by optionNum asc";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{detailId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 删除
     */
    @Override
    public Integer deleteSpecialTest(String testId) {
        String sql = "update t_special_test set state=3 where id=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 删除调理方
     */
    @Override
    public Integer deleteConditioning(String conditioningId) {
        String sql = "update t_doctor_conditioning set state=3 where id=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{conditioningId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 删除调理方详情
     */
    @Override
    public Integer deleteConditioningDetail(String conditioningId) {
        String sql = "delete from t_doctor_conditioning_detail where conditioningId=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{conditioningId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 删除标题
     */
    @Override
    public Integer deleteSpecialTestTitle(String testId) {
        String sql = "delete from t_special_test_title where specialTestId=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 添加默认的值
     */
    @Override
    public Integer addSpecialTestDefault(String testId, String doctorId,
                                         Integer type) {
        String sql = "insert into t_doctor_special(id,doctorId,specialId,type,createTime) values(?,?,?,?,?)";
        Integer i = 0;
        try {
        	//更新
            i = jdbcTemplate.update(sql, new Object[]{Util.getUUID(),
                    doctorId, testId, type, Util.queryNowTime()});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 删除默认值
     */
    public Integer deleteSpecialTestDefault(String testId, String doctorId,
                                            Integer type) {
        String sql = "select specialId from t_doctor_special where doctorId=? and type=?";
        String specialId = null;
        try {
            specialId = jdbcTemplate.queryForObject(sql, new Object[]{
                    doctorId, type}, String.class);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        Integer i = 0;
        if (StringUtils.isBlank(specialId)) { // 没有默认
            i = this.addSpecialTestDefault(testId, doctorId, type);
        } else { // 有默认
            if (!specialId.equalsIgnoreCase(testId)) {
                String sql2 = "delete from t_doctor_special where doctorId=? and type=?";
                i = jdbcTemplate.update(sql2, new Object[]{doctorId, type});
                i = this.addSpecialTestDefault(testId, doctorId, type);
            } else {
                String sql2 = "delete from t_doctor_special where doctorId=? and type=?";
                i = jdbcTemplate.update(sql2, new Object[]{doctorId, type});
            }
        }

        return i;
    }

    /**
     * 更新调理方默认值
     */
    @Override
    public Integer updateConditioningDefault(String conditioningId,
                                             String doctorId) {
        String sql = "update t_doctor_conditioning set isDefault=? where createUserId=?";
        if (!StringUtil.isEmpty(conditioningId)) {
            sql += " and id='" + conditioningId + "'";
        }
        Integer i = 0;
        try {
        	//更新
            i = jdbcTemplate.update(sql,
                    new Object[]{StringUtil.isEmpty(conditioningId) ? 2 : 1,
                            doctorId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 查询患者病历档案
     */
    @Override
    public List<Map<String, Object>> queryPatientServerRecords(String doctorId,
                                                               String patientId, Integer page, Integer row, String year) {
        List<Map<String, Object>> serverRecords = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT o.orderNo,FROM_UNIXTIME(o.payTime,'%Y/%m') ghDate,FROM_UNIXTIME(o.payTime,'%d') serverDay,s.`name` serverName,case when FROM_UNIXTIME(o.payTime,'%Y')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y') THEN 1 ELSE 0 END redType from t_order o "
                    + " INNER JOIN t_patient_data p on p.id = o.patientId "
                    + " INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo "
                    + " INNER JOIN t_server_type s on s.id = o.orderType "
                    + " where o.state=1 and o.paymentStatus in(2,5) and o.orderType in(4,5,6,7,8,9) and o.patientId=? and os.doctorId=?  ";
            //年
            if (!StringUtil.isEmpty(year)) {
                sql += " and FROM_UNIXTIME(o.payTime,'%Y')='" + year + "' ";
            }
            sql += " order by o.payTime desc ";
            if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                sql += " limit " + (page - 1) * row + " , " + row;
            }
            //医生id、患者id
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                serverRecords = super.queryBysqlList(sql, new Object[]{
                        patientId, doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return serverRecords;
    }

    /**
     * 查询症型列表
     */
    @Override
    public List<Map<String, Object>> queryDiagnosticList(Diagnostic diagnostic) {
        List<Map<String, Object>> diagnosList = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT d.id Id,d.`name`,d.`code`,d.pinyinCode from t_diagnostic d where d.state=1 ";
            diagnosList = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.warn(e);
        }
        return diagnosList;
    }

    /**
     * 查询药品列表(开方页面用)
     */
    @Override
    public List<Map<String, Object>> queryDrugList(String name,String yztId) {
        List<Map<String, Object>> drugList = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT h.id Id,h.healthProName name,h.masterId,h.healthProType type,dm.otherName,h.maxWeight,h.pinyinCode,hc.typeName,hu.healthProUtilName util,FORMAT(h.price,4) price, "
                    + " (SELECT GROUP_CONCAT(DISTINCT(c.conflictProId)) from t_confict_healthpro c where c.healthProId=h.masterId) conflictProIds,h.describes "
                    + " from t_health_products h "
                    + " INNER JOIN t_health_products_class hc on hc.id = h.healthProType "
                    + " INNER JOIN t_healthpro_util hu on hu.id = h.healthProUtil "
                    + " INNER JOIN t_drug_master dm on dm.id = h.masterId "
                    + " where h.state=1 and dm.state=1 ";
            //名称
            if (!StringUtil.isEmpty(name)) {
                sql += " and (h.healthProName like '%" + name
                        + "%' or h.pinyinCode like '%" + name + "%') ";
            }
            if(!StringUtil.isEmpty(yztId)){
            	sql += " and h.yztId='"+yztId+"' ";
            }
            sql += " group by h.id ORDER BY h.pinyinCode ASC ";
            drugList = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.warn(e);
        }
        return drugList;
    }

    /**
     * 查询复诊时间列表
     */
    @Override
    public List<Map<String, Object>> queryRepeatTimes() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT id Id,`name`,days from t_visit_time where state=1 ORDER BY createTime DESC ";
            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询处方模版处主药品列表
     */
    @Override
    public List<Map<String, Object>> queryDrugMasterList() {
        List<Map<String, Object>> drugList = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT dm.id Id,dm.`name`,dm.nameShort,dm.otherName,(SELECT GROUP_CONCAT(DISTINCT(c.conflictProId)) from t_confict_healthpro c where c.healthProId=dm.id) conflictProIds from t_drug_master dm where dm.state=1 ORDER BY dm.nameShort ASC ";
            drugList = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.warn(e);
        }
        return drugList;
    }

    /**
     * 查询主药
     */
    @Override
    public List<Map<String, Object>> queryDrugMasters(String yztId,String name,
                                                      Integer page, Integer row) {
        List<Map<String, Object>> drugList = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT dm.id Id,dm.`name`,ifnull(dm.nameShort,'') nameShort,ifnull(dm.otherName,'') otherName from t_drug_master dm where dm.state=1";
           //名称
            if (!StringUtil.isEmpty(name)) {
                sql += " and dm.name like '" + name + "%'";
            }
            if(!StringUtil.isEmpty(yztId)){
            	sql +=" and dm.yztId='"+yztId+"' ";
            }
            //排序
            sql += " ORDER BY dm.nameShort ASC ";
            if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                sql += " limit " + (page - 1) * row + "," + row;
            }
            drugList = super.queryBysqlList(sql, null);
        } catch (Exception e) {
            logger.warn(e);
        }
        return drugList;
    }

    /**
     * 根据医生ID、患者ID查询调理方订单
     */
    @Override
    public List<Map<String, Object>> queryConditionsByPatientId(
            String doctorId, String patientId, String wzDate, Integer page,
            Integer row) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT o.orderNo,p.id patientId,p.`name` patientName,CASE p.sex WHEN 0 THEN '男' ELSE '女' END patientSex,p.age,rr.otherDia diagnostics,s.`name` serverName,o1.recordId, "
                    + " CASE WHEN UNIX_TIMESTAMP(now())-o1.payTime>(30*24*60*60) THEN from_unixtime(o1.payTime,'%y/%m/%d') ELSE from_unixtime(o1.payTime,'%m/%d') END time "
                    + " from t_order o "
                    + " INNER JOIN t_server_type s on s.id = o.orderType "
                    + " INNER JOIN t_order o1 on o1.sourceOrderNo = o.orderNo "
                    + " INNER JOIN t_patient_data p on p.id = o1.patientId "
                    + " INNER JOIN t_recipe_record rr on rr.recordId = o1.recordId "
                    + " where rr.state in (0,1,2) and o.paymentStatus in (2,5) and o1.paymentStatus in (2,5) and o1.patientId =? and rr.doctorId=? ";
            //时间
            if (!StringUtil.isEmpty(wzDate)) {
                sql += " and from_unixtime(o1.payTime,'%Y-%m-%d')='" + wzDate
                        + "' ";
            }
            sql += " ORDER BY o1.payTime DESC ";
            if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                sql += " limit " + (page - 1) * row + " , " + row;
            }
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                list = super.queryBysqlList(sql, new Object[]{patientId,
                        doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询患者病历图像(问诊单ID)
     */
    @Override
    public List<Map<String, Object>> queryPatientImg(String doctorId,
                                                     String patientId, String wzDate, Integer page, Integer row) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT s.`name` serverName, GROUP_CONCAT(DISTINCT(sth.id)) ids, "
                    + "CASE WHEN UNIX_TIMESTAMP(now())-sth.createTime>(30*24*60*60) THEN from_unixtime(sth.createTime,'%Y/%m/%d') ELSE from_unixtime(sth.createTime,'%m/%d') END time "
                    + " from t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo "
                    + " INNER JOIN t_server_type s on s.id = o.orderType "
                    + " INNER JOIN t_doctor d on d.doctorId = o.doctorId "
                    + " INNER JOIN t_patient_data p on p.id = o.patientId "
                    + " INNER JOIN t_special_test_history sth on sth.patientId=o.patientId and sth.orderNo=o.orderNo "
                    + " where o.paymentStatus in (2,5) and o.patientId =? and os.doctorId=? ";
            //时间
            if (!StringUtil.isEmpty(wzDate)) {
                sql += " and from_unixtime(sth.createTime,'%Y-%m-%d')='"
                        + wzDate + "' ";
            }
            //分组，排序
            sql += " GROUP BY time,o.orderType ORDER BY sth.createTime DESC ";
            if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                sql += " limit " + (page - 1) * row + " , " + row;
            }
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                list = super.queryBysqlList(sql, new Object[]{patientId,
                        doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询患者病历图像
     */
    @Override
    public List<Map<String, Object>> queryPatientCaseImgs(String ids) {
        List<Map<String, Object>> imgs = new ArrayList<Map<String, Object>>();
        try {
            if (!StringUtil.isEmpty(ids)) {
                String sql = "SELECT pic.picPathUrl from t_piclib pic where pic.goodId in (";
                if (ids.indexOf(",") > -1) {
                    String[] idss = ids.split(",");
                    for (int i = 0; i < idss.length; i++) {
                        if (i == idss.length - 1) {
                            sql += "'" + idss[i] + "'";
                        } else {
                            sql += "'" + idss[i] + "',";
                        }
                    }
                } else {
                    sql += "'" + ids + "'";
                }
                sql += " ) ";
                imgs = jdbcTemplate.queryForList(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return imgs;
    }

    /**
     * 根据调理记录查询调理方案、药品记录
     */
    @Override
    public List<Map<String, Object>> queryConditionsByRecordId(String recordId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
        	//记录id
            if (!StringUtil.isEmpty(recordId)) {
                String sql = "SELECT GROUP_CONCAT(drr.drugName,drr.drugDose,drr.util) drugList,c.price conditionPrice,c.jgPrice,c.outOrIn userMethod,"
                        + " CASE c.agentType WHEN 1 THEN '颗粒' WHEN 2 THEN '饮片' else '膏方' end agentName,c.dose,c.useCount,IFNULL(c.waring,'') waring,IFNULL(c.taboo,'') taboo, "
                        + " CASE c.jgServerType WHEN 1 THEN '打粉' WHEN 3 THEN '膏方' else '无' end jgServerName "
                        + " from t_conditioning_record c "
                        + " INNER JOIN t_drug_record drr on drr.conditionId = c.id ";
                sql += " where c.recordId='" + recordId + "' GROUP BY c.id ";
                list = jdbcTemplate.queryForList(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询所有问诊日期(同一天在一个页面展示)
     */
    @Override
    public List<Map<String, Object>> queryWzDates(String doctorId,
                                                  String patientId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT from_unixtime(o.payTime,'%Y-%m-%d') time  "
                    + " from t_order o INNER JOIN t_order_detail_server os on os.orderNo=o.orderNo "
                    + " where o.paymentStatus in (2,5) and o.patientId =? and os.doctorId=? GROUP BY time ORDER BY time DESC ";
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                list = super.queryBysqlList(sql, new Object[]{patientId,
                        doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 根据药品ID集合获取药品列表
     */
    @Override
    public Map<String, Object> queryDrugById(String id) {
        Map<String, Object> drug = null;
        try {
            String sql = "SELECT h.id,h.healthProName drugName,h.maxWeight,u.healthProUtilName util,FORMAT(h.price,4) drugPrice,h.masterId from t_health_products h "
                    + " INNER JOIN t_healthpro_util u on u.id = h.healthProUtil "
                    + " where h.state=1 and h.id=? ";
            if (!StringUtil.isEmpty(id)) {
                drug = super.queryBysqlMap(sql, new Object[]{id});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return drug;
    }

    /**
     * 批量插入药品记录信息
     */
    @Override
    public Object insertDrugRecords(List<Map<String, Object>> list) {
        String sql = "INSERT INTO t_drug_record(id,conditionId,drugName,drugDose,drugPrice,util,drugId,maxWeight,isExcess,drugOrder,waringType)  values ";
        Object ok = null;
        try {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        sql += " ('" + Util.getUUID() + "','"
                                + list.get(i).get("conditionId") + "','"
                                + list.get(i).get("drugName") + "',"
                                + list.get(i).get("drugDose") + ","
                                + list.get(i).get("drugPrice") + ",'"
                                + list.get(i).get("util") + "','"
                                + list.get(i).get("drugId") + "',"
                                + list.get(i).get("maxWeight") + ","
                                + list.get(i).get("isExcess") + ","
                                + list.get(i).get("drugOrder") + ","
                                + list.get(i).get("waringType") + ") ";
                    } else {
                        sql += " ('" + Util.getUUID() + "','"
                                + list.get(i).get("conditionId") + "','"
                                + list.get(i).get("drugName") + "',"
                                + list.get(i).get("drugDose") + ","
                                + list.get(i).get("drugPrice") + ",'"
                                + list.get(i).get("util") + "','"
                                + list.get(i).get("drugId") + "',"
                                + list.get(i).get("maxWeight") + ","
                                + list.get(i).get("isExcess") + ","
                                + list.get(i).get("drugOrder") + ","
                                + list.get(i).get("waringType") + "), ";
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
     * 查询服务类型年
     */
    @Override
    public List<Map<String, Object>> queryServerYears(String doctorId,
                                                      String patientId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT FROM_UNIXTIME(o.payTime,'%Y') tlYear from t_order o "
                    + " INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo "
                    + " where o.state=1 and o.paymentStatus in(2,5) and o.orderType in(4,5,6,7,8,9) and o.patientId=? and os.doctorId=? GROUP BY tlYear order by tlYear desc ";
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                list = super.queryBysqlList(sql, new Object[]{patientId,
                        doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询常用语
     */
    @Override
    public List<Map<String, Object>> queryPhrase(String doctorId) {
        String sql = "select id,content from t_phrase where state = 1 and doctorId=? order by createTime";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 添加常用语
     */
    @Override
    public Integer addPhrase(String id, String doctorId, String content) {
        String sql = "insert into t_phrase(id,doctorId,content,state,createTime) values(?,?,?,?,?)";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{id, doctorId, content,
                    1, Util.queryNowTime()});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 更新短语
     */
    @Override
    public Integer updatePhrase(String id, String doctorId, String content) {
        String sql = "update t_phrase set content=? where id=? and doctorId=?";
        Integer i = 0;
        try {
            i = jdbcTemplate
                    .update(sql, new Object[]{content, id, doctorId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 删除常用语
     */
    @Override
    public Integer deletePhrase(String doctorId, String phraseId) {
        String sql = "delete from t_phrase where doctorId=? and id=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{doctorId, phraseId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 根据医生ID条件、分页查询就诊列表
     */
    @Override
    public List<Map<String, Object>> queryJzList(String doctorId, Integer page,
                                                 Integer row, Integer type) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT dd.orderstate,dd.orderStatus,dd.imgUrl,dd.age,dd.doctorId,dd.patientId,dd.patientName,dd.phone,dd.sex, (SELECT s.`name` FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dd.doctorId AND o.patientId = dd.patientId ORDER BY payTime DESC LIMIT 0,1)serverName, "
                        + " (SELECT s.id FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dd.doctorId AND o.patientId = dd.patientId ORDER BY payTime DESC LIMIT 0,1)serverId "
                        + " from (SELECT d.doctorId,"
                        + " CASE WHEN pd.nexus = '本人' THEN m.photo ELSE '' END imgUrl,pd.`name` patientName,pd.id patientId,pd.age,pd.phone,IF(0 = pd.sex, '男', '女') sex, "
                        + " (SELECT CASE WHEN (drugOrder.paymentStatus=2 or drugOrder.paymentStatus=3 or drugOrder.paymentStatus=5) THEN '已支付' WHEN drugOrder.paymentStatus=1 THEN '未支付' ELSE '' end orderState from t_order o INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo inner JOIN t_order drugOrder on drugOrder.sourceOrderNo = o.orderNo INNER JOIN t_recipe_record rr on rr.orderNo=drugOrder.orderNo where rr.isSendUser=1 and o.paymentStatus IN (2,3,5) and os.doctorId=dp.doctorId and o.patientId= dp.patientId ORDER BY drugOrder.createTime DESC LIMIT 0,1) orderStatus, "
                        + "(SELECT CASE WHEN (drugOrder.paymentStatus=2 or drugOrder.paymentStatus=3 or drugOrder.paymentStatus=5) THEN 'yzf' WHEN drugOrder.paymentStatus=1 THEN 'wzf' ELSE '' end orderState from t_order o INNER JOIN t_order_detail_server os on os.orderNo = o.orderNo inner JOIN t_order drugOrder on drugOrder.sourceOrderNo = o.orderNo INNER JOIN t_recipe_record rr on rr.orderNo=drugOrder.orderNo where rr.isSendUser=1 and o.paymentStatus IN (2,3,5) and os.doctorId=dp.doctorId and o.patientId= dp.patientId ORDER BY drugOrder.createTime DESC LIMIT 0,1) orderstate "
                        + " FROM t_doctor_patient dp "
                        + " INNER JOIN t_doctor d ON dp.doctorId=d.doctorId "
                        + " INNER JOIN t_patient_data pd ON pd.id=dp.patientId "
                        + " INNER JOIN t_member m ON m.id=pd.memberId  "
                        + " WHERE d.docStatus='10' and d.docIsOn='1' and d.doctorId='"
                        + doctorId + "' )dd where dd.orderStatus!='' ";
                // type 1-全部 2-已付款
                if (!StringUtil.isEmpty(type) && type != 1) {
                    sql += " and dd.orderstate='yzf' ";
                }
                // sql +="  ORDER BY msgTime DESC ";
                if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                    sql += " limit " + (page - 1) * row + " , " + row;
                }
                System.out.println(sql);
                list = jdbcTemplate.queryForList(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询常用语以有数量
     */
    @Override
    public Integer queryPhraseCount(String doctorId) {
        String sql = "select count(1) from t_phrase where doctorId=? and state <> 3";
        Integer i = 0;
        try {
            i = jdbcTemplate.queryForObject(sql, new Object[]{doctorId},
                    Integer.class);
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 百宝箱我的商品选择（开药时用）
     */
    @Override
    public List<Map<String, Object>> queryMyGoods(String doctorId, String name,
                                                  Integer page, Integer row) {
        String sql = "SELECT s.isProprietary,g.id,g.picture,g.name,g.description,IF(MIN(gp.price)=MAX(gp.price),MIN(gp.price),CONCAT_WS('-',MIN(gp.price),MAX(gp.price))) originalPrice,NULL activityPrice,SUM(gp.stock) stock FROM t_goods g INNER JOIN t_goods_pricenum gp ON gp.goodsId=g.id INNER JOIN t_user_keep uk ON uk.goodsId=g.id inner join t_supplier s on s.id=g.supplierId where uk.memberId=?  and g.state=1 and s.state=1 ";
        //名称
        if (!StringUtil.isEmpty(name)) {
            sql += " and g.name like '%" + name + "%'";
        }
        //分组、排序
        sql += " GROUP BY g.id order by uk.createTime desc";
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
            sql += " limit " + (page - 1) * row + "," + row;
        }
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 点击助理提交修改状态
     */
    @Override
    public Integer updateApplyRecordState(DoctorApplyRecord applyRecord) {
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(applyRecord.getApplyId())) {
                String sql = "UPDATE t_doctor_apply_record set saleTj=1 where applyId='"
                        + applyRecord.getApplyId() + "' ";
                i = jdbcTemplate.update(sql);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 根据医生ID查询助理是否存在
     */
    @Override
    public Map<String, Object> checkDoctorFollowId(String doctorId) {
        Map<String, Object> map = null;
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT IFNULL(f.followId,'') followId,d.docName from t_follow_history f inner join t_doctor d on d.doctorId=f.openId where f.openId='"
                        + doctorId + "' ";
                map = jdbcTemplate.queryForMap(sql);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return map;
    }

    /**
     * 查询最后一次服务订单
     */
    @Override
    public Map<String, Object> queryLastOrderNo(String patientId,
                                                String doctorId) {
        Map<String, Object> map = null;
        String sql = "SELECT orders.serverId,orders.serverName,p.age,IF(0 = p.sex, '男', '女')sex,p.id patientId,orders.orderNo,p.memberId,p.`name` patientName,p.phone,CASE WHEN p.nexus = '本人' THEN m.photo ELSE '' END imgUrl,0 type FROM t_patient_data p inner join t_member m on m.id=p.memberId "
                + " LEFT JOIN (SELECT o.orderNo,o.memberId,o.patientId,st.id serverId,st.name serverName from t_order o INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo inner join t_server_type st on st.id=o.orderType where ods.doctorId=? and o.patientId=? and (o.paymentStatus=2 or o.paymentStatus=5) and o.orderType in(4,7,22) ORDER BY o.payTime DESC LIMIT 0,1) orders on orders.patientId=p.id "
                + " WHERE p.id=?";
        try {
            map = super.queryBysqlMap(sql, new Object[]{doctorId, patientId,
                    patientId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询订单记录
     */
    @Override
    public Map<String, Object> queryRecordOrder(String orderNo) {
        String sql = "SELECT rr.remarks,rr.jgBasicPrice,rr.jgBasicDose,o.mainOrderNo,o.postage,o.orderNo,rr.orderNo receipeOrderNo,o.orderPrice,o.createTime,o.receiptsPrice,rr.visitTime,o.goodsPrice,o.orderStatus,o.paymentStatus,o.orderType,rr.otherDia,rr.price,rr.wzPrice,rr.doctorId,o.recordId,o.state orderState,rr.state,pmm.realName operaName FROM t_order o INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId LEFT JOIN(SELECT u.realName,pm.orderNo from t_photo_medical_record pm INNER JOIN t_user u on u.id = pm.operaUserId where pm.state=1) pmm on pmm.orderNo = o.orderNo or pmm.orderNo = o.mainOrderNo  WHERE (o.mainOrderNo=? or o.orderNo=?) and (o.orderType=10 OR o.orderType=13 OR o.orderType=14 OR o.orderType=15 OR o.orderType=16 OR o.orderType=17 OR o.orderType=18 OR o.orderType=19) ";
        System.out.println(sql);
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql,
                    new Object[]{orderNo, orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询调理方记录
     */
    @Override
    public List<Map<String, Object>> queryConditioningRecord(String recordId) {
        String sql = "SELECT id,recipeName,visitTime,taboo,waring,price,dose,useCount,useCount,outOrIn,agentType,jgPrice,isHideGram,drugPrice,jgServerType FROM t_conditioning_record WHERE recordId=? ORDER BY conditionOrder asc ";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{recordId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询药品记录
     */
    @Override
    public List<Map<String, Object>> queryDrugRecord(String recordId) {
        String sql = "SELECT drugId id,hp.masterId,drugName,drugDose,drugPrice,util FROM t_drug_record dr inner join t_health_products hp on hp.id=dr.drugId WHERE dr.conditionId=? order by dr.drugOrder asc ";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{recordId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过订单号查询商品
     */
    @Override
    public List<Map<String, Object>> queryGoodsByOrderNo(String orderNo) {
        String sql = "SELECT g.description,IF(gp.picture IS NULL OR gp.picture = 'null' OR gp.picture = '',g.picture,gp.picture) picture,g.name,GROUP_CONCAT(gds.name SEPARATOR ' ') specifications,NULL activityPrice,gp.price,gp.stock,od.goodsNum buyCount,g.id goodsId,gp.id priceNumId,IF(od.doctorId is null,2,1) isDoctor,1 `check`,s.isProprietary FROM t_order o INNER JOIN t_order_detail od ON od.orderNo=o.orderNo INNER JOIN t_goods g ON g.id=od.goodsId LEFT JOIN t_goods_pricenum gp ON gp.id=od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId=gp.id INNER JOIN t_goods_details_specifications gds ON gds.id=gd.specificationsId inner join t_supplier s on s.id=g.supplierId where o.mainOrderNo=? and o.orderType=1 and g.state=1 and s.state=1 GROUP BY gp.id";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 通过药品记录查询商品
     */
    @Override
    public Map<String, Object> queryGoodsByDrugRecord(String orderNo) {
        String sql = "SELECT receiptsPrice  FROM t_order o where o.mainOrderNo=? and o.orderType=1";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, new Object[]{orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询订单价格
     */
    @Override
    public Map<String, Object> queryRecordOrderPrice(String orderNo) {
        String sql = "SELECT orderPrice,receiptsPrice,goodsPrice,doctorId,memberId,patientId,ifnull(postage,0) postage,mainOrderNo,sourceOrderNo FROM t_order WHERE orderNo=?";
        Map<String, Object> price = null;
        try {
            price = jdbcTemplate.queryForMap(sql, new Object[]{orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return price;
    }

    /**
     * 更新订单价格记录
     */
    @Override
    public Integer updateRecordOrderPrice(String orderNo, Double orderPrice,
                                          Double receiptsPrice, Double goodsPrice) {
        String sql = "update t_order set orderPrice=?,receiptsPrice=?,goodsPrice=? where orderNo=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{orderPrice,
                    receiptsPrice, goodsPrice, orderNo,});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 更新订单号记录
     */
    @Override
    public Integer updateRecordOrdeNo(String orderNo, String patientId,
                                      ShippingAddress shippingAddress) {
        String sql = "update t_order set consignee=?,phone=?,province=?,city=?,area=?,detailedAddress=?,patientId=?,memberId=? where orderNo=? or mainOrderNo=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(
                    sql,
                    new Object[]{shippingAddress.getConsignee(),
                            shippingAddress.getPhone(),
                            shippingAddress.getProvince(),
                            shippingAddress.getCity(),
                            shippingAddress.getArea(),
                            shippingAddress.getDetailedAddress(), patientId,
                            shippingAddress.getMemberId(), orderNo, orderNo});
            if (i > 0) {
                String sql1 = "update t_recipe_record rr,t_order o set rr.isIdentifyingPeople =1 where rr.orderNo=o.orderNo and (o.orderNo=? or o.mainOrderNo=?) ";
                jdbcTemplate.update(sql1, new Object[]{orderNo, orderNo});
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 通过用户id更新订单记录
     */
    @Override
    public Integer updateRecordOrdeNoMemberId(String orderNo, String patientId,
                                              String memberId) {
        String sql = "update t_order set patientId=?,memberId=? where orderNo=?";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{patientId, memberId,
                    orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询药品价格记录
     */
    @Override
    public Map<String, Object> queryDrugRecordPrice(String orderNo,
                                                    Integer orderType) {
        String sql = "SELECT orderPrice-ifnull(postage,0) orderPrice,receiptsPrice-ifnull(postage,0) receiptsPrice,goodsPrice-ifnull(postage,0) goodsPrice,orderNo,postage FROM t_order WHERE mainOrderNo=? AND orderType=?";
        Map<String, Object> price = null;
        try {
            price = jdbcTemplate.queryForMap(sql, new Object[]{orderNo,
                    orderType});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return price;
    }

    /**
     * 查询药的记录价格通过主订单
     */
    @Override
    public Map<String, Object> queryDrugRecordPriceByMainOrderNo(String orderNo) {
        String sql = "SELECT o.orderPrice,o.receiptsPrice,o.goodsPrice,o.orderNo,o.postage,rr.state FROM t_order o INNER JOIN t_recipe_record rr on rr.orderNo=o.orderNo WHERE (o.mainOrderNo=? or o.orderNo=?) AND (o.orderType=10 OR o.orderType=13 OR o.orderType=14 OR o.orderType=15 OR o.orderType=16 OR o.orderType=17 OR o.orderType=18 OR o.orderType=19)";
        Map<String, Object> price = null;
        try {
            price = jdbcTemplate.queryForMap(sql, new Object[]{orderNo,
                    orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return price;
    }

    /**
     * 通过主订单查询服务类型
     */
    @Override
    public Integer queryServerTypeByMainOrderNo(String orderNo) {
        String sql = "SELECT orderType FROM t_order o WHERE (mainOrderNo=? or orderNo=?) and orderType=13";
        Integer count = null;
        try {
            count = jdbcTemplate.queryForObject(sql, new Object[]{orderNo,
                    orderNo}, Integer.class);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 查询最后一个订单
     */
    @Override
    public Map<String, Object> queryLastOrde(String patientId, String doctorId) {
        String sql = "(SELECT dp.isBlack,o.mainOrderNo,os1.id symptomId,s.id AS serverId, s.name AS serverName, d.docName, o.payTime, os.syZxCount , o.memberId, o.paymentStatus, o.orderNo , IF(ds.fisrtTwGhMoney - o.receiptsPrice < 0, 0, ds.fisrtTwGhMoney - o.receiptsPrice) AS payment , ds.twZxMoney, ds.fisrtTwGhMoney, ds.isOnlineTwGh, ds.isOnlineTwZx, d.isAccpetAsk , ds.twZxCount,ds.twZhZxCount, os.zxCount, p.age , IF(0 = p.sex, '男', '女') AS sex , p.id AS patientId, p.`name` AS patientName, p.phone , CASE  WHEN p.nexus = '本人' THEN m.photo ELSE '' END AS imgUrl,os.validityTime,0 AS type FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType INNER JOIN t_doctor d ON d.doctorId = os.doctorId INNER JOIN (SELECT s.* from t_doctor_set s) ds on if((SELECT count(0) from t_doctor_set s where s.doctorId=d.doctorId)>0, ds.doctorId=d.doctorId,ds.doctorId='-1') INNER JOIN t_patient_data p ON p.id = o.patientId INNER JOIN t_doctor_patient dp on dp.doctorId=os.doctorId and dp.patientId=o.patientId INNER JOIN t_member m ON m.id = p.memberId left join t_order_symptom os1 on os1.orderNo=o.orderNo WHERE o.patientId = ? AND os.doctorId = ?  AND (o.paymentStatus=2 or o.paymentStatus=5) ORDER BY payTime DESC LIMIT 0, 1)"
                + " UNION "
                + " (SELECT null isBlack,null mainOrderNo,'' symptomId,'' serverId,'' serverName, d.docName, '' payTime,'' syZxCount ,'' memberId, '' paymentStatus, '' orderNo ,'' payment ,'' twZxMoney, '' fisrtTwGhMoney,'' isOnlineTwGh,'' isOnlineTwZx,'' isAccpetAsk , '' twZxCount,'' twZhZxCount,'' zxCount, '' age , IF(0 = sr.sex, '男', '女') AS sex , sr.srId AS patientId, sr.`name` AS patientName, sr.phone ,sr.imgUrl,' ' validityTime, 1 AS type FROM t_sales_represent sr INNER JOIN t_follow_history fh ON fh.followId=sr.srId INNER JOIN t_doctor d ON d.doctorId=fh.openId WHERE fh.followId= ? AND fh.openId = ?)";
        Map<String, Object> order = null;
        try {
            order = jdbcTemplate.queryForMap(sql, new Object[]{patientId,
                    doctorId, patientId, doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return order;
    }

    /**
     * 更新发送模板
     */
    @Override
    public Integer updateSendMode(String userId) {
        String sql = "update t_send_model set `read`=2 where userId=?";
        try {
            return jdbcTemplate.update(sql, new Object[]{userId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
    }

    /**
     * 查询未读通知数
     */
    @Override
    public Integer querySendModeByUnRead(String userId, String type) {
        String sql = "select count(1) from t_send_model where userId=? and `read`=1 and isShow=1";
        if (!StringUtil.isEmpty(type)) {
            sql += " AND type='" + type + "'";
        }
        Integer i = 0;
        try {
            i = jdbcTemplate.queryForObject(sql, new Object[]{userId},
                    Integer.class);
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 查询最后一个发送的模板
     */
    @Override
    public Map<String, Object> querySendModeByLast(String userId, String type) {
        String sql = "select ifnull(title,' ') lastMsg,CASE WHEN UNIX_TIMESTAMP(now())- creationTime >(7 * 24 * 60 * 60)THEN from_unixtime(creationTime,'%Y/%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- creationTime >(24 * 60 * 60)THEN from_unixtime(creationTime, '%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- creationTime <(24 * 60 * 60)&&TO_DAYS(FROM_UNIXTIME(creationTime))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(creationTime,'%H:%i'))ELSE from_unixtime(creationTime, '%H:%i') END lastTime from t_send_model where userId=? and isShow=1";
        if (!StringUtil.isEmpty(type)) {
            sql += " AND type='" + type + "'";
        }
        sql += " order by creationTime desc limit 0,1";
        Map<String, Object> i = null;
        try {
            i = jdbcTemplate.queryForMap(sql, new Object[]{userId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 更新发送的模板
     */
    @Override
    public Integer updateSendMode(final String userId, final String[] models) {
        String sql = "update t_send_model set `read`=2 where userId=? and id=?";
        int[] i = {0};
        try {
            i = jdbcTemplate.batchUpdate(sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                            ps.setString(1, userId);
                            ps.setString(2, models[i]);
                        }

                        @Override
                        public int getBatchSize() {
                            return models.length;
                        }
                    });
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return Util.toInt(i);
    }

    /**
     * 查询默认的特价商品
     */
    @Override
    public String queryDefultSpecial(String doctorId, Integer type) {
        String sql = "select st.id from t_special_test st inner join t_doctor_special ds on ds.specialId=st.id where ds.doctorId=? and st.type=? and st.state=1 limit 0,1";
        String id = null;
        try {
            id = jdbcTemplate.queryForObject(sql,
                    new Object[]{doctorId, type}, String.class);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return id;
    }

    /**
     * 更新最后一个订单
     */
    @Override
    public Integer updateOrderByEnd(String orderNo) {
        Integer i = 0;
        try {
            // String sql =
            // "UPDATE t_order set paymentStatus=5,orderStatus=5,endTime=UNIX_TIMESTAMP() where orderNo=? ";
            String sql = "UPDATE t_order set paymentStatus=5,orderStatus=5 where orderNo=? ";
            i = jdbcTemplate.update(sql, new Object[]{orderNo});
        } catch (Exception e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询推送消息纪录
     */
    @Override
    public List<Map<String, Object>> querySendModePage(String userId,
                                                       Integer type, Integer page, Integer row) {
        String sql = "SELECT * from (SELECT sm.title,IF(locate('userName:',sm.content)>0,REPLACE(sm.content,'userName:',relevantum.realName),sm.content)content,FROM_UNIXTIME(sm.creationTime,'%Y-%m-%d %H:%i:%s') date,sm.type,sm.data,sm.read FROM t_send_model sm INNER join t_doctor d on sm.userId = d.doctorId left  join t_member relevantum on sm.relevantUserId = relevantum.id  WHERE sm.userId = ? and sm.isShow=1 "
                + " UNION"
                + " SELECT sm.title,IF(locate('userName:',sm.content)>0,REPLACE(sm.content,'userName:',relevantum.docName),sm.content)content,FROM_UNIXTIME(sm.creationTime,'%Y-%m-%d %H:%i:%s') date,sm.type,sm.data,sm.read FROM t_send_model sm INNER JOIN t_sales_represent sr on sm.userId = sr.srId LEFT JOIN t_doctor relevantum on sm.relevantUserId = relevantum.doctorId  WHERE sm.userId = ? and sm.isShow=1)f where 1=1 ";
        if (!StringUtil.isEmpty(type)) {
            sql += " and f.type=" + type;
        }
        sql += " order by f.date desc ";
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
            sql += " limit " + (page - 1) * row + "," + row;
        }
        try {
            return jdbcTemplate.queryForList(sql,
                    new Object[]{userId, userId});
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    /**
     * 查询总结算
     */
    @Override
    public Map<String, Object> querySettlementTotal(String doctorId,
                                                    Integer settlementType, String startTime, String endTime,
                                                    String checkDoctorId) {
        String sql = "select f.*,if(d.doctorId is null,2,1) member from(SELECT ifnull(SUM(s.settlementPrice),0) price,1 type,s.settlementMember FROM t_order o INNER JOIN t_settlement s ON s.orderNo=o.orderNo WHERE TO_DAYS(NOW())=TO_DAYS(FROM_UNIXTIME(o.payTime)) AND s.settlementMember=?"
                + " UNION"
                + " SELECT ifnull(SUM(s.settlementPrice),0) price,2 type,s.settlementMember FROM t_order o INNER JOIN t_settlement s ON s.orderNo=o.orderNo WHERE DATE_FORMAT(now(),'%Y-%m')=DATE_FORMAT(FROM_UNIXTIME(o.payTime),'%Y-%m') AND s.settlementMember=?"
                + " UNION"
                + " SELECT ifnull(SUM(s.settlementPrice),0) price,3 type,s.settlementMember FROM t_order o INNER JOIN t_settlement s ON s.orderNo=o.orderNo WHERE s.settlementMember=? ";

        //开始时间
        if (!StringUtil.isEmpty(startTime)) {
            sql += " AND FROM_UNIXTIME(o.payTime,'%Y-%m-%d')>='" + startTime
                    + "'";
        }
        //结束时间
        if (!StringUtil.isEmpty(endTime)) {
            sql += " AND FROM_UNIXTIME(o.payTime,'%Y-%m-%d')<='" + endTime
                    + "'";
        }
        //医生id
        if (!StringUtil.isEmpty(checkDoctorId)) {
            sql += " AND s.settlementMember='" + checkDoctorId + "' ";
        }
        //结算
        if (!StringUtil.isEmpty(settlementType)) {
            sql += " AND s.type=" + settlementType;
        }
        sql += " ) f left join t_doctor d on d.doctorId=f.settlementMember left join t_sales_represent sr on sr.srId=f.settlementMember";
        final Map<String, Object> settlementTotal = new HashMap<String, Object>();
        try {
            jdbcTemplate.query(sql,
                    new Object[]{doctorId, doctorId, doctorId},
                    new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs)
                                throws SQLException {
                            Integer type = rs.getInt("type");
                            if (type == 1) {
                                settlementTotal.put("today",
                                        rs.getDouble("price"));
                            } else if (type == 2) {
                                settlementTotal.put("month",
                                        rs.getDouble("price"));
                            } else {
                                settlementTotal.put("all",
                                        rs.getDouble("price"));
                            }
                            settlementTotal.put("type", rs.getInt("member"));
                        }
                    });
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return settlementTotal;
    }

    /**
     * 查询结算
     */
    @Override
    public List<Map<String, Object>> querySettlement(String doctorId,
                                                     Integer settlementType, String startTime, String endTime,
                                                     String checkDoctorId, Integer page, Integer row) {
        String sql = "SELECT f.* , CASE f.settlementType WHEN 1 THEN '商品引流收益' WHEN 2 THEN '商品推广收益' WHEN 3 THEN '调理服务收益 ' WHEN 4 THEN ' 调理引流收益' WHEN 5 THEN ' 图文调理收益 ' WHEN 7 THEN ' 图文咨询收益 ' WHEN 8 THEN ' 电话调理收益 ' WHEN 9 THEN ' 电话咨询收益 ' WHEN 11 THEN ' 锦旗收益 ' END AS settlementTypeName, FROM_UNIXTIME(f.payTime, '%Y-%m') AS date , FROM_UNIXTIME(f.payTime, '%Y.%m.%d ') AS time , ( SELECT SUM(ifnull(s.settlementPrice, 0)) FROM t_settlement s INNER JOIN t_order o ON o.orderNo = s.orderNo WHERE s.settlementMember = ? AND FROM_UNIXTIME(o.payTime, '%Y-%m') = FROM_UNIXTIME(f.payTime, '%Y-%m') ";
        //开始时间
        if (!StringUtil.isEmpty(startTime)) {
            sql += " AND FROM_UNIXTIME(o.payTime,'%Y-%m-%d')>='" + startTime
                    + "'";
        }
        //结束时间
        if (!StringUtil.isEmpty(endTime)) {
            sql += " AND FROM_UNIXTIME(o.payTime,'%Y-%m-%d')<='" + endTime
                    + "'";
        }
        //医生id
        if (!StringUtil.isEmpty(checkDoctorId)) {
            sql += " AND o.doctorId='" + checkDoctorId + "' ";
        }
        //结算
        if (!StringUtil.isEmpty(settlementType)) {
            sql += " AND s.type=" + settlementType;
        }

        sql += " ) AS monthPrice FROM ( SELECT s.id settlementId, IFNULL(SUM(ifnull(s.settlementPrice, 0)), 0) AS settlementPrice ,pd.`name` AS realName, s.type AS settlementType, o.patientId, o.doctorId, d.docName , o.mainOrderNo, o.receiptsPrice, o.orderNo, o.payTime , CONCAT_WS(',', d.doctorId, ifnull(d.docUrl, ' '), d.docName, IFNULL(d.docPosition, ' '), ods.payPrice, st.NAME) AS goods , CASE o.paymentStatus WHEN 1 THEN ' 待付款 ' WHEN 2 THEN ' 已付款 ' WHEN 3 THEN ' 退款中 ' WHEN 4 THEN ' 已退款 ' WHEN 5 THEN ' 订单完成 ' WHEN 6 THEN ' 已取消 ' WHEN 7 THEN ' 已付款 ' END AS payState, NULL AS type, 2 AS orderTypes FROM t_order o INNER JOIN t_order_detail_server ods ON ods.orderNo = o.orderNo INNER JOIN t_doctor d ON d.doctorId = ods.doctorId INNER JOIN t_server_type st ON st.id = o.orderType INNER JOIN t_settlement s ON s.orderNo = o.orderNo INNER JOIN t_patient_data pd ON pd.id=o.patientId WHERE s.settlementMember = ? GROUP BY s.id " +
                "UNION " +
                "SELECT s.id settlementId, IFNULL(SUM(ifnull(s.settlementPrice, 0)), 0) AS settlementPrice ,pd.`name` AS realName,s.type AS settlementType, o.patientId, o.doctorId, d.docName , o.mainOrderNo, o.receiptsPrice, o.orderNo, o.payTime , CONCAT_WS(',', d.doctorId, ifnull(d.docUrl, ' '), d.docName, IFNULL(d.docPosition, ' '), rr.price) AS goods , CASE o.paymentStatus WHEN 1 THEN ' 待付款 ' WHEN 2 THEN ' 已付款 ' WHEN 3 THEN ' 退款中 ' WHEN 4 THEN ' 已退款 ' WHEN 5 THEN ' 订单完成 ' WHEN 6 THEN ' 已取消 ' WHEN 7 THEN ' 已付款 ' END AS payState, NULL AS type, 4 AS orderTypes FROM t_order o INNER JOIN t_recipe_record rr ON rr.recordId = o.recordId INNER JOIN t_doctor d ON d.doctorId = o.doctorId INNER JOIN t_patient_data pd ON pd.id = o.patientId INNER JOIN t_settlement s ON s.orderNo = o.orderNo WHERE s.settlementMember = ? GROUP BY s.id " +
                "UNION " +
                "SELECT s.id settlementId, IFNULL(SUM(ifnull(s.settlementPrice,0)),0) AS settlementPrice,pd.`name` AS realName,s.type AS settlementType,o.patientId,o.doctorId,d.docName,o.mainOrderNo,o.receiptsPrice,o.orderNo,o.payTime,CONCAT_WS(',',d.doctorId,ifnull(d.docUrl,' '),d.docName, IFNULL( d.docPosition, ' ' ), o.orderPrice) AS goods,CASE o.paymentStatus WHEN 1 THEN ' 待付款 ' WHEN 2 THEN ' 已付款 ' WHEN 3 THEN ' 退款中 ' WHEN 4 THEN ' 已退款 ' WHEN 5 THEN ' 订单完成 ' WHEN 6 THEN ' 已取消 ' WHEN 7 THEN ' 已付款 ' END AS payState,NULL AS type,12 AS orderTypes FROM t_order o INNER JOIN t_doctor d ON d.doctorId=o.doctorId INNER JOIN t_patient_data pd ON pd.id=o.patientId INNER JOIN t_settlement s ON s.orderNo=o.orderNo WHERE s.settlementMember=? AND s.type=11 GROUP BY s.id " +
                "UNION " +
                "SELECT s.id settlementId, s.settlementPrice,pd.`name` AS realName,s.type AS settlementType, f1.patientId, f1.doctorId , f1.docName, f1.mainOrderNo, f1.receiptsPrice, f1.orderNo, f1.payTime , f1.goods, f1.payState, f1.type, f1.orderTypes FROM ( SELECT f.patientId, f.doctorId, f.docName, f.mainOrderNo, f.receiptsPrice , f.orderNo, f.payTime, f.memberId, f.gpId , GROUP_CONCAT(CONCAT_WS(',', f.goodsId, f.NAME, f.price, f.goodsNum, f.picture, f.specifications) SEPARATOR '&') AS goods , CASE f.paymentStatus WHEN 1 THEN ' 待付款 ' WHEN 2 THEN ' 已付款 ' WHEN 3 THEN ' 退款中 ' WHEN 4 THEN ' 已退款 ' WHEN 5 THEN ' 订单完成 ' WHEN 6 THEN ' 已取消 ' WHEN 7 THEN ' 已付款 ' END AS payState, NULL AS type, 1 AS orderTypes FROM ( SELECT o.paymentStatus, o.mainOrderNo, o.patientId, o.doctorId, d.docName , gp.id AS gpId, o.payTime, GROUP_CONCAT(gds.NAME ORDER BY gds.rank SEPARATOR ' ') AS specifications, g.NAME , g.id AS goodsId, od.goodsNum, od.goodsOriginalPrice AS price , IF((gp.picture IS NULL OR '' = gp.picture OR 'NULL' = gp.picture), g.picture, gp.picture) AS picture , o.memberId, o.receiptsPrice, o.type, o.orderNo FROM t_order o INNER JOIN t_order_detail od ON o.orderNo = od.orderNo INNER JOIN t_goods g ON g.id = od.goodsId INNER JOIN t_goods_pricenum gp ON gp.goodsId = g.id AND gp.id = od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId = gp.id INNER JOIN t_goods_details_specifications gds ON gds.id = gd.specificationsId INNER JOIN t_doctor d ON d.doctorId = o.doctorId WHERE o.state = 1 AND o.orderType = 1 GROUP BY o.orderNo, gp.id ) f GROUP BY f.orderNo ) f1 INNER JOIN t_settlement s ON s.orderNo = f1.orderNo INNER JOIN t_member m ON m.id = f1.memberId INNER JOIN t_patient_data pd ON pd.id=f1.patientId WHERE 1 = 1 AND s.settlementMember = ? GROUP BY s.id ) f WHERE 1=1 ";
        //开始时间
        if (!StringUtil.isEmpty(startTime)) {
            sql += " AND FROM_UNIXTIME(f.payTime,'%Y-%m-%d')>='" + startTime
                    + "'";
        }
        //结束时间
        if (!StringUtil.isEmpty(endTime)) {
            sql += " AND FROM_UNIXTIME(f.payTime,'%Y-%m-%d')<='" + endTime
                    + "'";
        }
        //医生id
        if (!StringUtil.isEmpty(checkDoctorId)) {
            sql += " AND f.doctorId='" + checkDoctorId + "' ";
        }

        //结算
        if (!StringUtil.isEmpty(settlementType)) {
            sql += " AND f.settlementType=" + settlementType;
        }
        sql += " order by f.payTime desc ";
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
            sql += " limit " + (page - 1) * row + "," + row;
        }
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId,
                    doctorId, doctorId,doctorId, doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询订单结算详情
     */
    @Override
    public List<Map<String, Object>> querySettlementOrderDetail(String orderNo) {
        String sql = "SELECT f.* FROM(SELECT IFNULL(sum(ifnull(s.settlementPrice, 0)),0)settlementPrice,o.patientId, o.doctorId, o.orderNo mainOrderNo, o.receiptsPrice,o.orderNo,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl, ' '),d.docName,IFNULL(d.docPosition, ' '),ods.payPrice,st. NAME)SEPARATOR '&'"
                + " )goods,CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL type,2 orderTypes FROM t_order o INNER JOIN t_order_detail_server ods ON ods.orderNo = o.orderNo"
                + " INNER JOIN t_doctor d ON d.doctorId = ods.doctorId INNER JOIN t_server_type st ON st.id = o.orderType inner join t_settlement s on s.orderNo=o.orderNo WHERE o.orderNo =?  GROUP BY o.orderNo"
                + " UNION"
                + " SELECT IFNULL(sum(ifnull(s.settlementPrice, 0)),0)settlementPrice,o.patientId, o.doctorId, o.orderNo mainOrderNo, o.receiptsPrice, o.orderNo,GROUP_CONCAT(CONCAT_WS(',',d.doctorId,ifnull(d.docUrl, ' '),d.docName,IFNULL(d.docPosition, ' '),rr.price)SEPARATOR '&')goods,"
                + " CASE o.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL type, 4 orderTypes FROM t_order o INNER JOIN t_recipe_record rr ON rr.recordId=o.recordId INNER JOIN t_doctor d ON d.doctorId=o.doctorId INNER JOIN t_patient_data pd ON pd.id=o.patientId inner join t_settlement s on s.orderNo=o.orderNo "
                + " WHERE o.orderNo =? GROUP BY o.orderNo"
                + " UNION"
                + " SELECT IFNULL(sum(ifnull(s.settlementPrice, 0)),0)settlementPrice,f.patientId, f.doctorId, f.orderNo mainOrderNo, f.receiptsPrice, f.orderNo,GROUP_CONCAT(CONCAT_WS(',',f.goodsId,f. NAME,f.price,f.goodsNum,f.picture,f.specifications)SEPARATOR '&')goods,CASE f.paymentStatus WHEN 1 THEN '待付款' WHEN 2 THEN '已付款' WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '订单完成' WHEN 6 THEN '已取消' WHEN 7 THEN '已付款' END payState,NULL type,1 orderTypes"
                + " FROM(SELECT o.paymentStatus,o.patientId,o.doctorId,GROUP_CONCAT(gds. NAME ORDER BY gds.rank SEPARATOR ' ')specifications,g. NAME,g.id goodsId,od.goodsNum,od.goodsOriginalPrice price,"
                + " IF(gp.picture IS NULL OR '' = gp.picture OR 'null' = gp.picture, g.picture, gp.picture)picture,o.memberId,o.receiptsPrice,o.type,o.orderNo FROM t_order o"
                + " INNER JOIN t_order_detail od ON o.orderNo = od.orderNo LEFT JOIN t_goods g ON g.id = od.goodsId INNER JOIN t_goods_pricenum gp ON gp.goodsId = g.id AND gp.id = od.goodsPriceNumId INNER JOIN t_goods_details gd ON gd.goodsPriceNumId = gp.id INNER JOIN t_goods_details_specifications gds ON gds.id = gd.specificationsId"
                + " WHERE o.state = 1 AND o.orderType = 1 GROUP BY o.orderNo, gp.id)f inner join t_settlement s on s.orderNo=f.orderNo WHERE f.orderNo =? GROUP BY f.orderNo"
                + " WHERE 1 = 1";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{orderNo,
                    orderNo, orderNo, orderNo, orderNo, orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 修改是否接单
     */
    @Override
    public Integer updateDoctorisAccpetAsk(String doctorId, Integer isAccpetAsk) {
        Integer i = 0;
        try {
            String sql = "UPDATE t_doctor set isAccpetAsk=? where doctorId=? ";
            if (!StringUtil.isEmpty(doctorId)
                    && !StringUtil.isEmpty(isAccpetAsk)) {
                i = super.update(sql, new Object[]{isAccpetAsk, doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询医生接单接口是否开启
     */
    @Override
    public Integer queryDocIsAccOn(String doctorId) {
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT isAccpetAsk from t_doctor where doctorId='"
                        + doctorId + "'";
                i = jdbcTemplate.queryForObject(sql, Integer.class);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 删除调理方案
     */
    @Override
    public Integer deleteConditionById(String id) {
        String sql = "DELETE from t_conditioning_record where recordId=?";
        Integer count = 0;
        try {
            if (!StringUtils.isEmpty(id)) {
                count = super.delete(sql, new Object[]{id});
                String sql1 = "INSERT INTO t_operalog(modular,details,createTime) VALUES('删除调理方功能','删除了调理记录ID为："
                        + id + "'," + Util.queryNowTime() + ") ";
                jdbcTemplate.update(sql1);
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 修改调理单获取订单信息接口（orderNo-调理单订单号）
     */
    @Override
    public Map<String, Object> queyrDetailByOrderNo(String orderNo,
                                                    String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT o.orderNo,o.mainOrderNo,wzOrder.orderNo wzOrderNo,o.paymentStatus,o.state,d.doctorId,d.docName,d.docPhone,FROM_UNIXTIME(o.createTime,'%Y-%m-%d %H:%i') time,o.orderType, "
                    + " p.id patientId,p.phone,p.`name` patientName,CASE WHEN p.sex=0 THEN '男' ELSE '女' end patientSex,p.age, "
                    + " rr.recordId,rr.isIdentifyingPeople,ifnull(rr.price,0.0) recipePrice,ifnull(rr.wzPrice,0.0) wzPrice,rr.visitTime,wzOrder.isOldServer,o.city,o.postage from t_order o "
                    + " inner JOIN t_doctor d on d.doctorId = o.doctorId "
                    + " inner JOIN t_recipe_record rr on rr.orderNo = o.orderNo "
                    + " LEFT JOIN t_patient_data p on p.id = o.patientId "
                    + " LEFT JOIN t_order wzOrder on wzOrder.orderNo = o.sourceOrderNo "
                    + " where o.orderNo=? and o.doctorId=? ";
            //订单号
            if (!StringUtils.isEmpty(orderNo) && !StringUtil.isEmpty(doctorId)) {
                map = super.queryBysqlMap(sql,
                        new Object[]{orderNo, doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据订单号修改服务订单详情内容
     */
    @Override
    public Integer updateOrderDetailServer(OrderDetailServer detailServer) {
        String sql = "UPDATE t_order_detail_server s set s.payPrice=?,s.doctorPrice=? where s.orderNo=?";
        Integer i = 0;
        try {
            if (!StringUtils.isEmpty(detailServer.getOrderNo())) {
                i = super.update(
                        sql,
                        new Object[]{detailServer.getPayPrice(),
                                detailServer.getDoctorPrice(),
                                detailServer.getOrderNo()});
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 修改调理记录状态改为修改状态
     */
    @Override
    public Integer updateRecipeRecordState(String recordId, Integer state) {
        if (StringUtil.isEmpty(state)) {
            state = 1;
        }
        String sql = "UPDATE t_recipe_record set state=" + state
                + " WHERE recordId=?";
        Integer i = 0;
        try {
        	//记录id
            if (!StringUtils.isEmpty(recordId)) {
                i = super.update(sql, new Object[]{recordId});
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询作废理由列表
     */
    @Override
    public List<Map<String, Object>> queryDeleteReasons() {
        String sql = "SELECT id,content from t_reason where state=1 and type=1";
        List<Map<String, Object>> reasons = new ArrayList<Map<String, Object>>();
        try {
            reasons = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return reasons;
    }

    /**
     * 作废调理方
     */
    @Override
    public Integer cancleRecipeState(String orderNo, String cannelNote,
                                     Integer isServer) {
        String sql = "UPDATE t_order set state=0,paymentStatus=6,deleteReason=? WHERE paymentStatus=1 and (orderNo=? or mainOrderNo=?) ";
        Integer i = 0;
        try {
        	//订单号
            if (!StringUtils.isEmpty(orderNo)
                    && !StringUtil.isEmpty(cannelNote)) {
            	//是否服务
                if (!StringUtil.isEmpty(isServer)) {
                    sql += " and orderType in(22,21) ";
                }
                i = super.update(sql, new Object[]{cannelNote, orderNo,
                        orderNo});
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 通过记录id查询处方订单
     */
    @Override
    public Map<String, Object> queryRecipeOrderByRecordId(String recordId) {
        String sql = "select o.state,o.paymentStatus,rr.isIdentifyingPeople from t_order o inner join t_recipe_record rr on rr.recordId=o.recordId where rr.recordId=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, new Object[]{recordId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询我的粉丝列表接口
     */
    @Override
    public Map<String, Object> queryFollowMembers(String doctorId,
                                                  Integer page, Integer row, String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        int total = 0;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT dd.imgUrl,dd.patientSex,dd.patientName,dd.age,dd.subscribeTime,dd.phone,pp.id patientId,dd.memberId,dd.createTime "
                        + " FROM(SELECT IFNULL(m.photo, m.headicon) imgUrl, CASE WHEN m.sex = 1 THEN '男' WHEN m.sex = 2 THEN '女' ELSE '未知' END patientSex, "
                        + " m.realname patientName, IFNULL(m.age, 0) age, FROM_UNIXTIME(m.createtime, '%Y-%m-%d') subscribeTime, m.phone, m.id memberId,f.followTime createTime "
                        + " FROM t_follow_history f INNER JOIN t_member m ON m.id = f.openId WHERE f.followId = '"
                        + doctorId
                        + "' "
                        + " UNION SELECT IFNULL(m.photo, m.headicon) imgUrl, CASE WHEN m.sex = 1 THEN '男' WHEN m.sex = 2 THEN '女' ELSE '未知' END patientSex, "
                        + " m.realname patientName, IFNULL(m.age, 0) age, FROM_UNIXTIME(uk.createTime, '%Y-%m-%d') subscribeTime, m.phone, m.id memberId, uk.createTime "
                        + " FROM t_user_keep uk INNER JOIN t_member m ON m.id = uk.memberId WHERE uk.type = 3 AND uk.goodsId = '"
                        + doctorId
                        + "' "
                        + " UNION SELECT IFNULL(m.photo, m.headicon) imgUrl, CASE WHEN m.sex = 1 THEN '男' WHEN m.sex = 2 THEN '女' ELSE '未知' END patientSex, "
                        + " m.realname patientName,IFNULL(m.age, 0) age,FROM_UNIXTIME(uk.createTime, '%Y-%m-%d') subscribeTime,m.phone,m.id memberId,uk.createTime "
                        + " FROM t_scan_keep uk INNER JOIN t_member m ON m.id = uk.memberId WHERE uk.keepId = '"
                        + doctorId
                        + "' ) dd  LEFT JOIN ( "
                        + " SELECT pd.id, pd.memberId FROM t_patient_data pd WHERE pd.isDefaultPer = 1 AND pd.state = 1 ) pp ON pp.memberId = dd.memberId where 1=1  ";
                //名称
                if (!StringUtil.isEmpty(name)) {
                    sql += " and dd.patientName like '%" + name + "%' ";
                }
                
                String countsql = " SELECT dd.memberId FROM (SELECT m.realname patientName,m.id memberId FROM t_follow_history f INNER JOIN t_member m ON m.id = f.openId "
                        + " WHERE f.followId = '"
                        + doctorId
                        + "' "
                        + " UNION SELECT m.realname patientName, m.id memberId FROM t_user_keep uk INNER JOIN t_member m ON m.id = uk.memberId WHERE "
                        + " uk.type = 3 AND uk.goodsId = '"
                        + doctorId
                        + "' UNION SELECT m.realname patientName, m.id memberId FROM "
                        + " t_scan_keep uk INNER JOIN t_member m ON m.id = uk.memberId WHERE uk.keepId = '"
                        + doctorId + "') dd WHERE 1 = 1 ";
                //名称
                if (!StringUtil.isEmpty(name)) {
                    countsql += " and dd.patientName like '%" + name + "%' ";
                }
                countsql += " GROUP BY dd.memberId  ";
                System.out.println(sql);
                //查询总数
                total = super.queryBysqlCount("SELECT count(0) from("
                        + countsql + ")f", null);
                sql += " GROUP BY dd.memberId ORDER BY dd.createTime DESC ";
                if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
                    sql += " limit " + (page - 1) * row + " , " + row;
                }
                list = jdbcTemplate.queryForList(sql);
            }
            map.put("total", total);
            map.put("list", list);
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询医生4种服务是否均以关闭
     */
    @Override
    public Integer queryDoctorSetIsClose(String doctorId) {
        Integer count = 0;
        try {
            String sql = "SELECT count(1) FROM t_doctor_set where isOnlinePhoneGh=0 and isOnlinePhoneZx=0 and isOnlineTwGh=0 and isOnlineTwZx =0 and doctorId=? ";
            if (!StringUtil.isEmpty(doctorId)) {
                count = super.queryBysqlCount(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 查询未认证个人简介数量
     */
    @Override
    public Integer queryAbstractIsNotSuccess(String doctorId) {
        Integer count = 0;
        try {
            String sql = "SELECT count(1) from t_doctor_abstract_record where doctorId=? and state=0 ";
            if (!StringUtil.isEmpty(doctorId)) {
                count = super.queryBysqlCount(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 查询最后一条医生简介记录
     */
    @Override
    public Map<String, Object> queryLastAbstract(String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT content docAbstract,remark,state from t_doctor_abstract_record where doctorId=? ORDER BY createTime DESC LIMIT 0,1 ";
            if (!StringUtil.isEmpty(doctorId)) {
                map = super.queryBysqlMap(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据手机号查询是否存在申请记录 
     */
    @Override
    public Integer deleteApplyRecrodByDoctorId(String docPhone) {
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(docPhone)) {
                String sql = "delete from t_doctor_apply_record where docPhone='"
                        + docPhone + "' ";
                i = jdbcTemplate.update(sql);
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 修改医生简介信息
     */
    @Override
    public Integer updateDocAbstract(String doctorId, String content) {
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "update t_doctor set docAbstract='" + content
                        + "' where doctorId='" + doctorId + "' ";
                i = jdbcTemplate.update(sql);
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询每天拨打次数
     */
    @Override
    public Integer queryPhoneRecordCount(String fromId, String toId,
                                         Integer type) {
        Integer i = 0;
        try {
            if (!StringUtil.isEmpty(fromId) && !StringUtil.isEmpty(toId)) {
                String sql = "SELECT count(1) from t_phone_record where FROM_UNIXTIME(createTime,'%Y-%m-%d')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y-%m-%d') and state=3 and fromId='"
                        + fromId + "' ";
                if (!StringUtil.isEmpty(toId)) {
                    sql += " and toId='" + toId + "' ";
                }
                if (!StringUtil.isEmpty(type)) {
                    sql += " and type=" + type;
                }
                i = super.queryBysqlCount(sql, null);
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询单个医院信息
     */
    @Override
    public Map<String, Object> queryOneInfirmary(String infirmaryId) {
        Map<String, Object> map = null;
        try {
            if (!StringUtil.isEmpty(infirmaryId)) {
                String sql = "SELECT * from t_infirmary i WHERE i.state=1 and i.infirmaryId='"
                        + infirmaryId + "' ";
                map = jdbcTemplate.queryForMap(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询是否有重复坐诊信息
     */
    @Override
    public Integer queryIsCfZzData(DoctorZzData zzData) {
        Integer i = 0;
        try {
        	//医生id
            if (!StringUtil.isEmpty(zzData.getDoctorId())
                    && !StringUtil.isEmpty(zzData.getInfirmaryId())) {
                String sql = "SELECT count(1) from t_doctor_zz_data d where d.state=1 and d.doctorId='"
                        + zzData.getDoctorId()
                        + "' and d.infirmaryId='"
                        + zzData.getInfirmaryId()
                        + "' and d.zzDate='"
                        + zzData.getZzDate()
                        + "' and d.zzState="
                        + zzData.getZzState() + " ";
                //id
                if (!StringUtil.isEmpty(zzData.getId())) {
                    sql += " and d.id<>'" + zzData.getId() + "' ";
                }
                i = super.queryBysqlCount(sql, null);
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 根据订单号查询订单
     */
    @Override
    public Map<String, Object> queryOneOrder(String orderNo) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT o.*,d.docName,d.docPhone,q.qrCodeUrl,pd.name c from t_order o "
                    + " INNER JOIN t_doctor d on d.doctorId = o.doctorId "
                    + " left JOIN t_patient_data pd on pd.id = o.patientId "
                    + " LEFT JOIN t_qrCode q on q.orderNo = o.orderNo "
                    + " where o.orderNo=? ";
            if (!StringUtil.isEmpty(orderNo)) {
                map = super.queryBysqlMap(sql, new Object[]{orderNo});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据订单号查询二维码
     */
    @Override
    public Map<String, Object> queryOrderQrCode(String orderNo) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT q.qrCodeUrl,o.doctorId,d.docName from t_order o left JOIN t_qrcode q on o.orderNo=q.orderNo LEFT JOIN t_doctor d on d.doctorId=o.doctorId  where o.orderNo=?  ORDER BY q.createTime DESC limit 0,1 ";
            if (!StringUtil.isEmpty(orderNo)) {
                map = super.queryBysqlMap(sql, new Object[]{orderNo});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 插入订单号查询二维码
     */
    @Override
    public Object insertOrderQrCode(String orderNo, String qrCodeUrl) {
        Object i = null;
        try {
            if (!StringUtil.isEmpty(orderNo) && !StringUtil.isEmpty(qrCodeUrl)) {
                String sql = "INSERT INTO t_qrcode(orderNo,qrCodeUrl) VALUES('"
                        + orderNo + "','" + qrCodeUrl + "')";
                i = jdbcTemplate.update(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询医患最后一个交流服务类型
     */
    @Override
    public Map<String, Object> queryLastSeverName(String doctorId,
                                                  String patientId) {
        Map<String, Object> map = null;
        try {
            if (!StringUtil.isEmpty(patientId) && !StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT s.`name` from t_order o INNER JOIN t_server_type s on s.id=o.orderType where o.doctorId='"
                        + doctorId
                        + "' and o.patientId = '"
                        + patientId
                        + "' ORDER BY payTime desc LIMIT 0,1 ";
                map = jdbcTemplate.queryForMap(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询历史银行卡信息
     */
    @Override
    public List<Map<String, Object>> queryBankHistorys(String doctorId,
                                                       Integer page, Integer row) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT bb.id,bb.bankName,bb.bankCode,b.`name`,b.id type from t_bind_bank bb "
                    + " INNER JOIN t_bank b on b.id = bb.bankId "
                    + " where b.state=1 and bb.doctorId=? ORDER BY bb.bankTime DESC";
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId});
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询医生最新银行卡号
     */
    @Override
    public Map<String, Object> queryLastBankByDoctorId(String doctorId) {
        Map<String, Object> map = null;
        try {
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT bb.id,bb.bankName,bb.bankCode,b.`name`,b.id type from t_bind_bank bb "
                        + " INNER JOIN t_bank b on b.id = bb.bankId "
                        + " where b.state=1 and bb.doctorId='"
                        + doctorId
                        + "' " + " ORDER BY bb.bankTime DESC limit 0,1 ";
                map = jdbcTemplate.queryForMap(sql);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询银行字典列表
     */
    @Override
    public List<Map<String, Object>> queryBankList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT id value,name text from t_bank where state=1  ORDER BY createTime DESC ";
            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询订单详情服务
     */
    @Override
    public Map<String, Object> queryOrderDetailServer(String orderNo) {
        String sql = "select validityTime from t_order_detail_server where orderNo=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, new Object[]{orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 添加调理单记录
     */
    @Override
    public Integer addConditionRecords(
            final List<ConditionRecord> conditionRecords) {
        String sql = "insert into t_conditioning_record (id,recipeName,visitTime,taboo,waring,price,createTime,dose,useCount,unitPrice,outOrIn,agentType,recordId,waringType,jgPrice,isHideGram,drugPrice,jgServerType,conditionOrder) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int i[] = {0};
        try {
            i = jdbcTemplate.batchUpdate(sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                            ps.setString(1, conditionRecords.get(i).getId());
                            ps.setString(2, conditionRecords.get(i)
                                    .getRecipeName());
                            ps.setObject(3, conditionRecords.get(i)
                                    .getVisitTime());
                            ps.setString(4, conditionRecords.get(i).getTaboo());
                            ps.setString(5, conditionRecords.get(i).getWaring());
                            ps.setDouble(6, conditionRecords.get(i).getPrice());
                            ps.setInt(7, conditionRecords.get(i)
                                    .getCreateTime());
                            ps.setInt(8, conditionRecords.get(i).getDose());
                            ps.setInt(9, conditionRecords.get(i).getUseCount());
                            ps.setDouble(10, conditionRecords.get(i)
                                    .getUnitPrice());
                            ps.setString(11, conditionRecords.get(i)
                                    .getOutOrIn());
                            ps.setInt(12, conditionRecords.get(i)
                                    .getAgentType());
                            ps.setString(13, conditionRecords.get(i)
                                    .getRecordId());
                            ps.setObject(14, conditionRecords.get(i)
                                    .getWaringType());
                            ps.setDouble(15, conditionRecords.get(i)
                                    .getJgPrice());
                            ps.setInt(16, conditionRecords.get(i)
                                    .getIsHideGram());
                            ps.setDouble(17, conditionRecords.get(i)
                                    .getDrugPrice());
                            ps.setInt(18, conditionRecords.get(i)
                                    .getJgServerType());
                            ps.setInt(19, conditionRecords.get(i)
                                    .getConditionOrder());
                        }

                        @Override
                        public int getBatchSize() {
                            return conditionRecords.size();
                        }
                    });
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return Util.toInt(i);
    }

    /**
     * 查询医生信息根据ID
     */
    @Override
    public Map<String, Object> queryDocById(String doctorId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT * from t_doctor where doctorId=? ";
            if (!StringUtil.isEmpty(doctorId)) {
                map = jdbcTemplate.queryForMap(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return map;
    }

    /**
     * 查询单个医生
     */
    @Override
    public Map<String, Object> queryPushDoctor(String doctorId) {
        String sql = "SELECT d.docName,d.docPhone,d.doctorId,i.infirmaryName,p.`name` positionName from t_doctor d INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId INNER JOIN t_position p on p.id = d.docPositionId where d.doctorId=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询首页统计信息
     */
    @Override
    public Map<String, Object> queryIndexCount(String doctorId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Integer patientCount = 0;
            Integer cfCount = 0;
            Integer evaCount = 0;
            //医生id
            if (!StringUtil.isEmpty(doctorId)) {
                String sql = "SELECT MAX(ddd.patientCount) patientCount,MAX(ddd.evaCount) evaCount,MAX(ddd.cfCount) cfCount from(SELECT count(0) patientCount,0 evaCount,0 cfCount from(SELECT dp.* FROM t_doctor_patient dp  INNER JOIN t_doctor d ON d.doctorId = dp.doctorId  INNER JOIN t_patient_data p ON p.id = dp.patientId  INNER JOIN t_member m ON m.id = p.memberId  INNER JOIN t_order o ON o.patientId = dp.patientId  AND o.doctorId = dp.doctorId  AND o.state = 1  WHERE dp.doctorId = ? and d.docStatus = '10' AND d.docIsOn = '1'  AND o.orderType IN (SELECT s.id FROM t_server_type s WHERE s.state = 1 ) GROUP BY dp.patientId ) dd "
                        + " UNION "
                        + " SELECT 0 patientCount,count(0) evaCount,0 cfCount from (SELECT s. NAME serverName,pd. NAME realname  FROM t_evaluate e  INNER JOIN t_order o ON o.orderNo = e.orderNo  INNER JOIN t_server_type s ON s.id = o.orderType  INNER JOIN t_doctor d ON d.doctorId = e.goodsId  INNER JOIN t_member m ON m.id = e.memberId  INNER JOIN t_patient_data pd ON pd.id = o.patientId  LEFT JOIN t_order_symptom os ON os.orderNo = o.orderNo  WHERE e.id IS NOT NULL  AND e.state IN (1, 4)  AND e.goodsId = ? and d.docStatus = '10' AND d.docIsOn = '1'  GROUP BY e.id ) dd "
                        + " UNION "
                        + " SELECT 0 patientCount, 0 evaCount,count(0) cfCount FROM ( "
                        + " SELECT dp.doctorId,(SELECT CASE WHEN (drugOrder.paymentStatus = 2 OR drugOrder.paymentStatus = 3 OR drugOrder.paymentStatus = 5 ) THEN 'yzf' WHEN drugOrder.paymentStatus = 1 THEN "
                        + " 'wzf' ELSE '' END orderState FROM t_order o "
                        + " INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo "
                        + " INNER JOIN t_order drugOrder ON drugOrder.sourceOrderNo = o.orderNo "
                        + " INNER JOIN t_recipe_record rr on rr.orderNo=drugOrder.orderNo "
                        + " WHERE rr.isSendUser=1 and o.paymentStatus IN (2,3,5) "
                        + " AND os.doctorId = dp.doctorId "
                        + " AND o.patientId = dp.patientId "
                        + " ORDER BY drugOrder.createTime DESC "
                        + " LIMIT 0, 1) orderstate "
                        + " FROM t_doctor_patient dp "
                        + " INNER JOIN t_doctor d ON dp.doctorId = d.doctorId "
                        + " INNER JOIN t_patient_data pd ON pd.id = dp.patientId "
                        + " INNER JOIN t_member m ON m.id = pd.memberId "
                        + " WHERE d.docStatus = '10' AND d.docIsOn = '1'  AND d.doctorId = ?  ) dd "
                        + " WHERE dd.orderstate != '' ) ddd ";
                System.out.println(sql);
                map = super.queryBysqlMap(sql, new Object[]{doctorId,
                        doctorId, doctorId});
            }
            if (map == null) {
                map = new HashMap<String, Object>();
                map.put("patientCount", patientCount);
                map.put("cfCount", cfCount);
                map.put("evaCount", evaCount);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return map;
    }

    /**
     * 查询患者通过医生id
     */
    @Override
    public List<Map<String, Object>> queryPatientsByDoctorId(String doctorId) {
        String sql = "select pd.id,d.docName,i.infirmaryName from t_doctor_patient dp inner join t_patient_data pd on pd.id=dp.patientId inner join t_doctor d on d.doctorId=dp.doctorId inner join t_infirmary i on d.infirmaryId=i.infirmaryId where dp.doctorId=? and pd.state=1 group by pd.memberId";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 通过助理id查询关联
     */
    @Override
    public List<Map<String, Object>> queryFollowBySrId(String srId) {
        String sql = "SELECT d.doctorId value,d.docName text FROM t_doctor d "
                + " INNER JOIN t_infirmary i on i.infirmaryId = d.infirmaryId "
                + " INNER JOIN t_position p on p.id = d.docPositionId "
                + " where d.doctorId in (SELECT f.openId FROM t_follow_history f where f.followId=?) ORDER BY d.createTime DESC  ";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, new Object[]{srId});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 通过url查询订单
     */
    @Override
    public Map<String, Object> queryOrderQrUrl(String orderNo) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT qrCodeUrl from t_qrcode where orderNo=? LIMIT 0,1";
            if (!StringUtil.isEmpty(orderNo)) {
                map = super.queryBysqlMap(sql, new Object[]{orderNo});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return map;
    }

    /**
     * 修改医生是否已下载app
     */
    @Override
    public Integer updateDoctorDownloadState(String doctorId) {
        Integer i = 0;
        try {
            String sql = "UPDATE t_doctor set isDownLoadApp=1,downTime=UNIX_TIMESTAMP() where isDownLoadApp=0 and doctorId=? ";
            if (!StringUtil.isEmpty(doctorId)) {
                i = jdbcTemplate.update(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 查询十八反十九畏集合
     */
    @Override
    public Map<String, Object> queryConfict() {
        String sql = "select id,healthProId,conflictProId from t_confict_healthpro where 1 =1 ";
        final Map<String, Object> map = new HashMap<String, Object>();
        try {

            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put(rs.getString("healthProId") + "_"
                            + rs.getString("conflictProId"), rs.getString("id"));
                }
            });
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询最后一次交流信息
     */
    @Override
    public Map<String, Object> queryLastChatMap(String doctorId,
                                                String patientId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT CASE m1.msgType WHEN 1 THEN mc.content WHEN 2 THEN '[图片]' WHEN 3 THEN '[语音]' WHEN 5 THEN '[地址]' WHEN 6 THEN '[症状描述填写]' WHEN 7 THEN '[症状描述填写完成]' WHEN 8 THEN '[问诊单填写]' WHEN 9 THEN '[问诊单填写完成]' WHEN 10 THEN '[复诊单填写]' WHEN 11 THEN '[复诊单填写完成]' WHEN 12 THEN '[调理方案]' WHEN 13 THEN '[调理方案已付款]' WHEN 14 THEN '[赠送提问]' WHEN 15 THEN '[补缴挂号费]' WHEN 16 THEN '[挂号费已补缴]' WHEN 17 THEN '[结束问诊]' WHEN 18 THEN '[消息撤回]' WHEN 19 THEN '[文章推荐]' WHEN 20 THEN '[商品推荐]' WHEN 21 THEN '[坐诊信息]' WHEN 22 THEN '[帮忙认证]' WHEN 23 THEN '[服务购买]' WHEN 24 THEN '[问诊将于10分钟后结束]' WHEN 25 THEN '[已到复诊时间]' WHEN 26 THEN '[调理方案作废]' WHEN 27 THEN '[用药说明]' WHEN 28 THEN '[接通电话成功]' WHEN 29 THEN '[接通电话失败]' WHEN 30 THEN '[新增就诊人抄方]' ELSE '您有一条新消息' END  lastMsg, "
                    + " CASE WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(7 * 24 * 60 * 60)THEN from_unixtime(m1.msgTime,'%Y/%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(24 * 60 * 60)THEN from_unixtime(m1.msgTime, '%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- m1.msgTime <(24 * 60 * 60)&&TO_DAYS(FROM_UNIXTIME(m1.msgTime))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(m1.msgTime,'%H:%i'))ELSE from_unixtime(m1.msgTime, '%H:%i') END lastTime, "
                    + " m1.msgTime "
                    + " FROM t_msg m1 INNER JOIN t_msg_content mc ON mc.msgId=m1.id "
                    + " WHERE (m1.from_account=? AND m1.to_account=?) OR (m1.from_account=? AND m1.to_account=?) ORDER BY m1.msgTime DESC LIMIT 0,1 ";
            //医生id，患者id
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                map = jdbcTemplate.queryForMap(sql, new Object[]{doctorId,
                        patientId, patientId, doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询最后一次服务
     */
    @Override
    public Map<String, Object> queryLastServerMap(String doctorId,
                                                  String patientId) {
        Map<String, Object> map = null;
        try {
            String sql = "SELECT s.`name` serverName,s.id serverId "
                    + " FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo "
                    + " INNER JOIN t_server_type s ON s.id = o.orderType "
                    + " WHERE os.doctorId = ? AND o.patientId = ? ORDER BY payTime DESC LIMIT 0,1 ";
            //医生id，患者id
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(patientId)) {
                map = jdbcTemplate.queryForMap(sql, new Object[]{doctorId,
                        patientId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据手机号查询关联医生数量
     */
    @Override
    public Integer queryDocCountByPhone(String phone) {
        Integer count = 0;
        try {
            String sql = "SELECT count(0) from t_doctor where docPhone=? and docStatus='10' ";
            if (!StringUtil.isEmpty(phone)) {
                count = super.queryBysqlCount(sql, new Object[]{phone});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 查询是否存在申请记录
     */
    @Override
    public Integer queryDocApply(String doctorId) {
        Integer count = 0;
        try {
            String sql = "SELECT count(0) from t_doctor_apply_record where docStatus='10' and applyId=? ";
            if (!StringUtil.isEmpty(doctorId)) {
                count = super.queryBysqlCount(sql, new Object[]{doctorId});
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 电话咨询完成
     */
    @Override
    public Integer updatePhoneAdvisory(String orderNo) {
        String sql = "update t_order set orderStatus=5,paymentStatus=5 where orderNo=? and orderType=8";
        Integer i = 0;
        try {
            i = jdbcTemplate.update(sql, new Object[]{orderNo});
        } catch (DataAccessException e) {
            logger.error(e);
        }
        return i;
    }

    /**
     * 根据OPENID查询此人是否已有导入人
     */
    @Override
    public Integer queryFollowCountById(String openid) {
        Integer count = 0;
        try {
            String sql = "SELECT count(0) from t_follow_history where openId=? ";
            if (!StringUtil.isEmpty(openid)) {
                count = super.queryBysqlCount(sql, new Object[]{openid});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return count;
    }

    /**
     * 查询收货地址
     */
    @Override
    public ShippingAddress queryShippingAddress(String id) {
        String sql = "select * from t_shipping_address where id=?";
        ShippingAddress shippingAddress = null;
        try {
            shippingAddress = jdbcTemplate.queryForObject(sql,
                    new Object[]{id}, new BeanPropertyRowMapper<>(
                            ShippingAddress.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return shippingAddress;
    }

    /**
     * 查询患者信息
     */
    @Override
    public PatientData queryPatientData(String id) {
        String sql = "select * from t_patient_data where id=?";
        PatientData patientData = null;
        try {
            patientData = jdbcTemplate.queryForObject(sql, new Object[]{id},
                    new BeanPropertyRowMapper<>(PatientData.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return patientData;
    }

    /**
     * 修改订单主订单号
     */
    @Override
    public Integer updateOrderMainOrderNo(String orderNo) {
        Integer count = 0;
        try {
            String sql = "UPDATE t_order set mainOrderNo='' where orderNo=? and mainOrderNo is not NULL ";
            if (!StringUtil.isEmpty(orderNo)) {
                count = super.update(sql, new Object[]{orderNo});
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    /**
     * 查询银行卡信息单个
     */
    @Override
    public Map<String, Object> queryBankById(String id) {
        Map<String, Object> map = null;
        try {
            String sql = "select id,bankId,bankName,bankCode,bankTime,doctorId from t_bind_bank where id=? ";
            if (!StringUtil.isEmpty(id)) {
                map = super.queryBysqlMap(sql, new Object[]{id});
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return map;
    }

    /**
     * 查询所有未使用锦旗订单号
     */
    @Override
    public List<Map<String, Object>> queryEvaBannerOrders(String memberId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "SELECT m.orderNo FROM t_my_eva_banner m "
                    + " INNER JOIN t_eva_banner eb on eb.id = m.evaBannerId "
                    + " where eb.state=1 and m.state=1 and m.memberId=? GROUP BY m.orderNo";
            if (!StringUtil.isEmpty(memberId)) {
                list = super.queryBysqlList(sql, new Object[]{memberId});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * 查询是否已存在未支付补缴挂号费订单
     */
    @Override
    public Integer queryIsHasBjGhPrice(String doctorId, String memberId,
                                       String patientId) {
        Integer count = 0;
        try {
            String sql = "SELECT count(1) from t_order where doctorId=? and memberId=? and patientId=? and orderType=4 and rsrvStr1='1' and paymentStatus=1 ";
            if (!StringUtil.isEmpty(doctorId) && !StringUtil.isEmpty(memberId) && !StringUtil.isEmpty(patientId)) {
                count = super.queryBysqlCount(sql, new Object[]{doctorId, memberId, patientId});
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return count;
    }

    /**
     * 根据药品ID集合字符串获取药品列表
     */
    public List<Map<String, Object>> queryDrugListByIds(String ids) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            String sql = "SELECT h.id,h.healthProName drugName,h.maxWeight,u.healthProUtilName util,FORMAT(h.price,4) drugPrice,h.state,h.masterId from t_health_products h "
                    + " INNER JOIN t_healthpro_util u on u.id = h.healthProUtil "
                    + " where h.id IN (" + ids + ")";

            list = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.warn(e);
        }
        return list;
    }
    
    /**
     * 根据老服务订单号删除订单
     */
    @Override
	public Integer deleteOldWzOrder(String orderNo) {
		Integer i = 0;
		try {
			String sql = "delete from t_order where orderNo=? ";
			if(!StringUtils.isEmpty(orderNo)){
				i = jdbcTemplate.update(sql,new Object[]{orderNo});
			}
		} catch (Exception e) {
			logger.error(e);
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
	 * 拍照开方插入图片（多张）
	 * @param imgArr
	 * @param id
	 * @return
	 */
	@Override
	public Integer addOrderPhoto(String[] imgArr, String id) {
		Integer map = 0;
		try {
			String sql = "insert into t_order_photo(id,extractid,img,lpos)values";
			for(int i=0;i<imgArr.length;i++){
				if(i==imgArr.length-1){sql+="('"+Util.getUUID()+"','"+id+"','"+imgArr[i]+"',"+i+")";}else{
				sql+="('"+Util.getUUID()+"','"+id+"','"+imgArr[i]+"',"+i+"),";}
			}
			if(!StringUtil.isEmpty(id)&&!StringUtil.isEmpty(imgArr)){
				map = jdbcTemplate.update(sql);
			
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}
}
