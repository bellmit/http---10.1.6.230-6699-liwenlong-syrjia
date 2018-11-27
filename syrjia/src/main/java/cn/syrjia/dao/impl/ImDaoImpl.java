package cn.syrjia.dao.impl;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.dao.ImDao;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 疑难答疑dao实现
 *
 * @pdOid 19c6b439-48fc-4cc5-b390-5fe1b817ad43
 */
@Repository("imDao")
public class ImDaoImpl extends BaseDaoImpl implements ImDao {

    // 日志
    private Logger logger = LogManager.getLogger(ImDaoImpl.class);

    /**
     * 查询患者列表
     *
     * @param request
     * @param doctorId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPatient(String doctorId) {
        //拼接sql
        //String sql = "SELECT f.serverName,f.serverId,f.paymentStatus,f.endTime,f.syZxCount,f.imgUrl,f.patientName,f.patientId,f.doctorId,f.age,f.phone,f.sex,f.type,(SELECT CASE m1.msgType WHEN 1 THEN mc.content WHEN 2 THEN '[图片]' WHEN 3 THEN '[语音]' WHEN 5 THEN '[地址]' WHEN 6 THEN '[症状描述填写]' WHEN 7 THEN '[症状描述填写完成]' WHEN 8 THEN '[问诊单填写]' WHEN 9 THEN '[问诊单填写完成]' WHEN 10 THEN '[复诊单填写]' WHEN 11 THEN '[复诊单填写完成]' WHEN 12 THEN '[调理方案]' WHEN 13 THEN '[调理方案已付款]' WHEN 14 THEN '[赠送提问]' WHEN 15 THEN '[补缴挂号费]' WHEN 16 THEN '[挂号费已补缴]' WHEN 17 THEN '[结束问诊]' WHEN 18 THEN '[消息撤回]' WHEN 19 THEN '[文章推荐]' WHEN 20 THEN '[商品推荐]' WHEN 21 THEN '[坐诊信息]' WHEN 22 THEN '[帮忙认证]' WHEN 23 THEN '[服务购买]' WHEN 24 THEN '[问诊将于10分钟后结束]' WHEN 25 THEN '[已到复诊时间]' WHEN 26 THEN '[调理方案作废]' WHEN 27 THEN '[用药说明]' WHEN 28 THEN '[接通电话成功]' WHEN 29 THEN '[接通电话失败]' WHEN 30 THEN '[新增就诊人抄方]' ELSE '您有一条新消息' END  FROM t_msg m1 INNER JOIN t_msg_content mc ON mc.msgId=m1.id WHERE m1.state =1 and m1.toDel=1 and m1.msgType <> 27 and ((m1.from_account=f.doctorId AND m1.to_account=f.patientId) OR (m1.from_account=f.patientId AND m1.to_account=f.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastMsg,"
        String sql = "SELECT f.serverName,f.serverId,f.paymentStatus,f.endTime,f.syZxCount,f.imgUrl,f.patientName,f.patientId,f.doctorId,f.age,f.phone,f.sex,f.type"
                //+" (SELECT CASE WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(7 * 24 * 60 * 60)THEN from_unixtime(m1.msgTime,'%Y/%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(24 * 60 * 60)THEN from_unixtime(m1.msgTime, '%m/%d %H:%i') WHEN TO_DAYS(FROM_UNIXTIME(m1.msgTime))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(m1.msgTime,'%H:%i'))ELSE from_unixtime(m1.msgTime, '%H:%i') END  FROM t_msg m1 WHERE m1.state=1 and m1.toDel=1 and m1.msgType <> 27 and ((m1.from_account=f.doctorId AND m1.to_account=f.patientId) OR (m1.from_account=f.patientId AND m1.to_account=f.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) lastTime,"
                //+" (SELECT m1.msgTime FROM t_msg m1 WHERE m1.state=1 and m1.toDel=1 and m1.msgType <> 27 and ((m1.from_account=f.doctorId AND m1.to_account=f.patientId) OR (m1.from_account=f.patientId AND m1.to_account=f.doctorId)) ORDER BY m1.msgTime DESC LIMIT 0,1) msgTime "
                + " FROM (SELECT (SELECT s.`name` FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dp.doctorId AND o.patientId = dp.patientId ORDER BY payTime DESC LIMIT 0,1)serverName,"
                + " (SELECT s.id FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dp.doctorId AND o.patientId = dp.patientId ORDER BY payTime DESC LIMIT 0,1)serverId,"
                + " (SELECT o.paymentStatus FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dp.doctorId AND o.patientId = dp.patientId ORDER BY payTime DESC LIMIT 0,1)paymentStatus,"
                + " (SELECT o.endTime FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dp.doctorId AND o.patientId = dp.patientId ORDER BY payTime DESC LIMIT 0,1)endTime,"
                + " (SELECT os.syZxCount FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo = o.orderNo INNER JOIN t_server_type s ON s.id = o.orderType WHERE os.doctorId = dp.doctorId AND o.patientId = dp.patientId ORDER BY payTime DESC LIMIT 0,1)syZxCount,"
                + " CASE WHEN pd.nexus = '本人' THEN m.photo ELSE '' END imgUrl,pd.`name` patientName,pd.id patientId,d.doctorId,pd.age,pd.phone,IF(0 = pd.sex, '男', '女')sex,2 type"
                + " FROM t_doctor_patient dp"
                + " INNER JOIN t_doctor d ON dp.doctorId=d.doctorId"
                + " INNER JOIN t_patient_data pd ON pd.id=dp.patientId"
                + " INNER JOIN t_member m ON m.id=pd.memberId "
                + " WHERE d.doctorId=? AND d.docStatus='10' and d.docIsOn='1' and dp.isDelete=0 "
                //+" UNION"
                //+" SELECT "
                //+" '医生助理' serverName,'' serverId,2 paymentStatus,UNIX_TIMESTAMP()-10 endTime,1 syZxCount,sp.imgUrl,sp.`name`,sp.srId patientId,d.doctorId,null age,sp.phone,IF(0 = sp.sex, '男', '女')sex,1 type FROM t_follow_history fh INNER JOIN t_sales_represent sp ON sp.srId=fh.followId INNER JOIN t_doctor d ON d.doctorId=fh.openId WHERE d.doctorId=?) f WHERE (f.paymentStatus=2 OR f.syZxCount>0 or (f.paymentStatus=5 and f.endTime is not null and UNIX_TIMESTAMP()-f.endTime<48*60*60))  ORDER BY f.type DESC";
                //+" ) f WHERE (f.paymentStatus=2 OR f.syZxCount>0 or (f.paymentStatus=5 and f.endTime is not null and UNIX_TIMESTAMP()-f.endTime<48*60*60))";
                + " ) f WHERE 1=1 ";
        List<Map<String, Object>> list = null;
        try {
            System.out.println(sql);
            //执行查询
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询聊天记录
     *
     * @param request
     * @param date
     * @param doctorId
     * @param patientId
     * @param page
     * @param row
     * @return
     */
    @Override
    public List<Map<String, Object>> queryIms(String data, String doctorId, String patientId, String orderNo, Integer page, Integer row) {
        //拼接sql
        String sql = "select * from (SELECT m.id,m.from_account,m.msgType,mc.content,mc.type,mc.originalUrl,mc.bigPictureUrl,mc.shrinkingMapUrl,mc.width,mc.height,mc.size,mc.`second`,m.msgTime,mc.orderNo,mc.dataId,m.msgSeq,CASE WHEN TO_DAYS(FROM_UNIXTIME(m.msgTime))=TO_DAYS(NOW()) THEN FROM_UNIXTIME(m.msgTime,'%H:%i') WHEN TO_DAYS(FROM_UNIXTIME(m.msgTime))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(m.msgTime,'%H:%i')) WHEN YEARWEEK(FROM_UNIXTIME(m.msgTime))=YEARWEEK(NOW()) THEN CONCAT((CASE DAYOFWEEK(FROM_UNIXTIME(m.msgTime)) WHEN 1 THEN '周日 ' WHEN 2 THEN '周一 ' WHEN 3 THEN '周二 ' WHEN 4 THEN '周三 ' WHEN 5 THEN '周四 ' WHEN 6 THEN '周五 ' WHEN 7 THEN '周六 ' END),FROM_UNIXTIME(m.msgTime,'%H:%i')) ELSE FROM_UNIXTIME(m.msgTime,'%Y年%m月%d日 %H:%i') END time FROM t_msg m INNER JOIN t_msg_content mc ON mc.msgId=m.id WHERE m.state=1 and ((m.from_account=? AND m.to_account=? AND m.fromDel =1 AND syncOtherMachine=1) OR (m.to_account=? AND m.from_account=? AND m.toDel = 1))";
        if (!StringUtil.isEmpty(data)) {
            sql += " AND FROM_UNIXTIME(m.msgTime,'%Y-%m-%d')='" + data + "'";
        }
        if (!StringUtil.isEmpty(orderNo)) {
            sql += " AND mc.serverOrderNo='" + orderNo + "'";
        }
        //分组排序
        sql += " GROUP BY m.id order by m.msgTime desc ";
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(row)) {
            sql += " limit " + (page - 1) * row + "," + row;
        }
        sql += ") f order by f.msgTime asc";
        List<Map<String, Object>> list = null;
        try {
            //拼接sql
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId, patientId, doctorId, patientId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 根据日期查询聊天记录
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryImsDate(String doctorId,
                                                  String patientId) {
        //拼接sql
        String sql = "SELECT * FROM(select f.date,st.name from (select FROM_UNIXTIME(m.msgTime,'%Y-%m-%d') date FROM t_msg m INNER JOIN t_msg_content mc ON mc.msgId=m.id WHERE m.state=1 and ((m.from_account=? AND m.to_account=? AND m.fromDel =1) OR (m.to_account=? AND m.from_account=? AND m.toDel = 1))) f inner join t_order o on FROM_UNIXTIME(o.payTime,'%Y-%m-%d')=f.date inner join t_order_detail_server ods on ods.orderNo=o.orderNo inner join t_server_type st on st.id=o.orderType where 1=1  order by f.date asc) f  group by f.date ";
        List<Map<String, Object>> list = null;
        try {
            //执行sql
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId, patientId, doctorId, patientId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询历史聊天记录
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @param page
     * @param row
     * @return
     */
    @Override
    public List<Map<String, Object>> queryHistoryOrder(String doctorId,
                                                       String patientId, Integer page, Integer row) {
        String sql = "SELECT FROM_UNIXTIME(o.payTime,'%Y-%m-%d') time,st.`name`,o.paymentStatus,o.orderNo FROM t_order o INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo INNER JOIN t_server_type st ON st.id=o.orderType  WHERE o.doctorId=? and o.patientId=? AND o.paymentStatus <> 1 AND o.paymentStatus <> 6 order by o.payTime desc";
        List<Map<String, Object>> list = null;
        try {
            //执行sql
            list = jdbcTemplate.queryForList(sql, new Object[]{doctorId, patientId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 根据医生id查询医生信息
     *
     * @param request
     * @param doctorId
     * @return
     */
    @Override
    public Map<String, Object> queryDoctorById(String doctorId) {
        String sql = "select d.docUrl,d.docName,p.name docPosition,i.infirmaryName,d.doctorId,dset.isOnlineTwGh,dset.fisrtTwGhMoney,dset.isHideGram,dset.isOnlineTwZx,dset.twZxMoney,if(d.docSex=0,'男','女') sex,'' age from t_doctor d inner join (SELECT s.* from t_doctor_set s) dset on if((SELECT count(0) from t_doctor_set s where s.doctorId=d.doctorId)>0, dset.doctorId=d.doctorId,dset.doctorId='-1') left join t_infirmary i on i.infirmaryId=d.infirmaryId left join t_position p on p.id=docPositionId  where d.doctorId=? and d.docStatus=10";
        Map<String, Object> map = null;
        try {
            //执行sql
            map = jdbcTemplate.queryForMap(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据患者id查询患者信息
     *
     * @param request
     * @param patientId
     * @return
     */
    @Override
    public Map<String, Object> queryPatientById(String patientId) {
        String sql = "select m.id memberId,m.photo,pd.name,pd.id,pd.sex,pd.age,pd.phone from t_patient_data pd inner join t_member m on m.id=pd.memberId  where pd.id=? ";
        Map<String, Object> map = null;
        try {
            //执行sql
            map = jdbcTemplate.queryForMap(sql, new Object[]{patientId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据信息id查询音频播放路径
     */
    @Override
    public Map<String, Object> queryMp3BySqe(String id) {
        String sql = "select mc.content from t_msg m inner join t_msg_content mc on mc.msgId=m.id where m.id=?";
        Map<String, Object> map = null;
        try {
            //执行sql
            map = jdbcTemplate.queryForMap(sql, new Object[]{id});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 根据订单号查询症状描述
     *
     * @param request
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, Object> querySymptom(String orderNo) {
        String sql = "select symptomDescribe,otherDescribe,pulse,state from t_order_symptom where orderNo=?";
        Map<String, Object> map = null;
        try {
            //执行sql
            map = jdbcTemplate.queryForMap(sql, new Object[]{orderNo});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询最新订单号
     */
    @Override
    public Map<String, Object> queryLastOrderNo(String patientId,
                                                String doctorId) {
        String sql = "SELECT o.orderNo,o.memberId,o.orderType FROM t_order o  INNER JOIN t_order_detail_server ods ON ods.orderNo=o.orderNo WHERE patientId=? AND ods.doctorId=? AND (o.paymentStatus=2 OR o.paymentStatus=5) ORDER BY o.payTime DESC LIMIT 0,1";
        Map<String, Object> orderNo = null;
        try {
            //执行sql
            orderNo = jdbcTemplate.queryForMap(sql, new Object[]{patientId, doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return orderNo;
    }

    /**
     * 查询特殊证明详情历史记录
     */
    @Override
    public List<Map<String, Object>> querySpecialTestDetailHistory(String testId) {
        String sql = "SELECT id,qid,titleName,optionType,isMandatory FROM t_special_test_title_history WHERE specialTestId=? order by qid asc";
        List<Map<String, Object>> list = null;
        try {
            //执行sql
            list = jdbcTemplate.queryForList(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询特殊证明历史记录
     */
    @Override
    public Map<String, Object> querySpecialTestHistory(String testId) {
        String sql = "select id,testName,otherName,isTongue,isSurface,isOther,type,state from t_special_test_history where id=? ";
        Map<String, Object> map = null;
        try {
            //执行sql
            map = jdbcTemplate.queryForMap(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 删除特殊证明历史记录
     */
    @Override
    public Integer deleteSpecialTestHistory(String testId) {
        String sql = "delete from t_special_test_history where id=?";
        Integer i = 0;
        try {
            //执行sql
            i = jdbcTemplate.update(sql, new Object[]{testId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 查询特殊证明项的历史记录
     */
    @Override
    public List<Map<String, Object>> querySpecialTestOptionHistory(
            String titleId) {
        String sql = "SELECT id,optionNum,optionName,checked FROM t_special_test_title_options_history WHERE titleId=? order by optionNum";
        List<Map<String, Object>> list = null;
        try {
            //执行sql
            list = jdbcTemplate.queryForList(sql, new Object[]{titleId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 查询图片
     */
    @Override
    public List<Map<String, Object>> queryPic(String testId, Integer type) {
        String sql = "SELECT picPathUrl FROM t_piclib WHERE goodId=? and picType=? order by rsrvStr1";
        List<Map<String, Object>> list = null;
        try {
            //执行查询
            list = jdbcTemplate.queryForList(sql, new Object[]{testId, type});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return list;
    }

    /**
     * 添加ZxCount
     */
    @Override
    public Integer addZxCount(String orderNo, Integer count) {
        String sql = "update t_order_detail_server set syZxCount=IF(IFNULL(syZxCount,0)+?<0,0,IFNULL(syZxCount,0)+?),zxCount=IF(IFNULL(zxCount,0)+?<0,0,IFNULL(zxCount,0)+?) where orderNo=?";
        Integer i = 0;
        try {
            //执行sql
            i = jdbcTemplate.update(sql, new Object[]{count, count, count, count, orderNo});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 添加SyZxCount
     */
    @Override
    public Integer addSyZxCount(String orderNo, Integer count) {
        String sql = "update t_order_detail_server set syZxCount=IF(IFNULL(syZxCount,0)+?<0,0,IFNULL(syZxCount,0)+?) where orderNo=?";
        Integer i = 0;
        try {
            //执行sql
            i = jdbcTemplate.update(sql, new Object[]{count, count, orderNo});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 添加ZxCount
     */
    @Override
    public Integer addZxCount(String orderNo, String doctorId, Integer count) {
        String sql = "insert into t_zx_count(id,orderNo,syZxCount,useZxCount,createTime,doctorId) values(?,?,?,0,UNIX_TIMESTAMP(),?)";
        Integer i = 0;
        try {
            //执行sql
            i = jdbcTemplate.update(sql, new Object[]{Util.getUUID(), orderNo, count, doctorId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 更新ZxCount
     */
    @Override
    public Integer updateZxCount(String orderNo, Integer count) {
        String sql = "UPDATE t_zx_count c INNER JOIN (SELECT id FROM t_zx_count WHERE orderNo=? AND syZxCount-useZxCount>0 LIMIT 0,1) c1 ON c1.id=c.id SET useZxCount=useZxCount-?";
        Integer i = 0;
        try {
            //执行更新
            i = jdbcTemplate.update(sql, new Object[]{orderNo, count});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 查询SyZxCount
     */
    @Override
    public Integer querySyZxCount(String orderNo) {
        String sql = "select syZxCount from t_order_detail_server where orderNo=?";
        Integer i = 0;
        try {
            //执行查询
            i = jdbcTemplate.queryForObject(sql, new Object[]{orderNo}, Integer.class);
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 消息撤回
     *
     * @param request
     * @param toAccount
     * @param fromAccount
     * @param msgId
     * @return
     */
    @Override
    public Integer withdrawIm(String msgId, Integer state) {
        String sql = "update t_msg set msgType=? where id=?";
        Integer i = 0;
        try {
            //更新sql
            i = jdbcTemplate.update(sql, new Object[]{state, msgId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 通过id查询消息
     */
    @Override
    public Map<String, Object> queryMsgById(String toAccount, String fromAccount, String msgId) {
        String sql = "select * from  t_msg where id=? ";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //执行查询
            map = jdbcTemplate.queryForMap(sql, new Object[]{msgId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return map;
    }

    /**
     * 删除消息
     *
     * @param request
     * @param msgId
     * @param doctorId
     * @return
     */
    @Override
    public Integer deleteMsg(String msgId, String doctorId) {
        String sql = "update t_msg set fromDel=(IF(?=from_account,2,fromDel)),toDel=(IF(?=to_account,2,toDel)) where id=? ";
        Integer i = 0;
        try {
            //执行删除
            i = jdbcTemplate.update(sql, new Object[]{doctorId, doctorId, msgId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 查询消息内容
     */
    @Override
    public Map<String, Object> queryMsgContent(String msgId) {
        String sql = "select * from  t_msg_content where  msgId=? ";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //执行查询
            map = jdbcTemplate.queryForMap(sql, new Object[]{msgId});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return map;
    }

    /**
     * 删除患者
     *
     * @param request
     * @param doctorId
     * @param patientId
     * @return
     */
    @Override
    public Integer deletePatient(String deleteId, String beDeleted) {
        String sql = "update t_doctor_patient set isDelete=1 where doctorId=? and patientId=? ";
        Integer i = 0;
        try {
            //执行更新
            i = jdbcTemplate.update(sql, new Object[]{deleteId, beDeleted});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    /**
     * 删除医生
     *
     * @param request
     * @param doctorId
     * @param srId
     * @return
     */
    @Override
    public Integer deleteDoctor(String deleteId, String beDeleted) {
        String sql = "update t_follow_history set isDelete=1 where followId=? and openId=? ";
        Integer i = 0;
        try {
            //执行更新
            i = jdbcTemplate.update(sql, new Object[]{deleteId, beDeleted});
        } catch (DataAccessException e) {
            logger.error(e);
            throw e;
        }
        return i;
    }

    @Override
    public void queryLastMsgListByList(List<Map<String, Object>> list) {
        // 修改为数组查询 不通过单一查询
        StringBuffer sbDoctorId = new StringBuffer();
        StringBuffer sbPatientId = new StringBuffer();
        for (Map<String, Object> map : list) {
            sbDoctorId.append("'").append(map.get("doctorId")).append("',");
            sbPatientId.append("'").append(map.get("patientId")).append("',");
        }
        if (sbDoctorId.lastIndexOf(",") > -1 && sbDoctorId.lastIndexOf(",") == sbDoctorId.length() - 1) {
            sbDoctorId.deleteCharAt(sbDoctorId.length() - 1);
        }
        if (sbPatientId.lastIndexOf(",") > -1 && sbPatientId.lastIndexOf(",") == sbPatientId.length() - 1) {
            sbPatientId.deleteCharAt(sbPatientId.length() - 1);
        }
        List<Map<String, Object>> listMap = this.queryLastMsgList(sbDoctorId.toString(), sbPatientId.toString());
        for (Map<String, Object> map : list) {
            String doctorIdStr = (String) map.get("doctorId");
            String patientIdStr = (String) map.get("patientId");
            for (Map<String, Object> map1 : listMap) {
                String fromAccountStr = (String) map1.get("fromAccount");
                String toAccountStr = (String) map1.get("toAccount");
                if ((doctorIdStr.equals(fromAccountStr) && patientIdStr.equals(toAccountStr)) ||
                        (doctorIdStr.equals(toAccountStr) && patientIdStr.equals(fromAccountStr))) {
                    map.put("lastMsg", Util.getValue(map1, "lastMsg", ""));
                    map.put("lastTime", Util.getValue(map1, "lastTime", ""));
                    map.put("msgTime", Util.getValue(map1, "msgTime", ""));
                    break;
                }
            }
            if (null == map.get("msgTime")) {
                map.put("msgTime", 0);
            }
        }
    }

    /**
     * 查询最新消息
     */
    @Override
    public List<Map<String, Object>> queryLastMsgList(String doctorId, String patientId) {
        String sql = "select * from (SELECT m1.from_account as fromAccount, m1.to_account as toAccount, m1.id,m1.msgTime,CASE WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(7 * 24 * 60 * 60)THEN from_unixtime(m1.msgTime,'%Y/%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(24 * 60 * 60)THEN from_unixtime(m1.msgTime, '%m/%d %H:%i') WHEN TO_DAYS(FROM_UNIXTIME(m1.msgTime))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(m1.msgTime,'%H:%i'))ELSE from_unixtime(m1.msgTime, '%H:%i') END lastTime,CASE m1.msgType WHEN 1 THEN mc.content WHEN 2 THEN '[图片]' WHEN 3 THEN '[语音]' WHEN 5 THEN '[地址]' WHEN 6 THEN '[症状描述填写]' WHEN 7 THEN '[症状描述填写完成]' WHEN 8 THEN '[问诊单填写]' WHEN 9 THEN '[问诊单填写完成]' WHEN 10 THEN '[复诊单填写]' WHEN 11 THEN '[复诊单填写完成]' WHEN 12 THEN '[调理方案]' WHEN 13 THEN '[调理方案已付款]' WHEN 14 THEN '[赠送提问]' WHEN 15 THEN '[补缴挂号费]' WHEN 16 THEN '[挂号费已补缴]' WHEN 17 THEN '[结束问诊]' WHEN 18 THEN '[消息撤回]' WHEN 19 THEN '[文章推荐]' WHEN 20 THEN '[商品推荐]' WHEN 21 THEN '[坐诊信息]' WHEN 22 THEN '[帮忙认证]' WHEN 23 THEN '[服务购买]' WHEN 24 THEN '[问诊将于10分钟后结束]' WHEN 25 THEN '[已到复诊时间]' WHEN 26 THEN '[调理方案作废]' WHEN 27 THEN '[用药说明]' WHEN 28 THEN '[接通电话成功]' WHEN 29 THEN '[接通电话失败]' WHEN 30 THEN '[新增就诊人抄方]' ELSE '您有一条新消息' END lastMsg  FROM t_msg m1 INNER JOIN t_msg_content mc ON mc.msgId=m1.id WHERE m1.state =1 and m1.toDel=1 and m1.msgType <> 27 and ((m1.from_account in ( " + patientId + ") AND m1.to_account in ( " + doctorId + ")) OR (m1.from_account in ( " + doctorId + ") AND m1.to_account  in ( " + patientId + "))) ORDER BY m1.msgTime DESC) t group by fromAccount, toAccount order by msgTime desc";
        List<Map<String, Object>> map = null;
        try {
            //执行查询
            map = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询最新消息
     */
    @Override
    public Map<String, Object> queryLastMsg(String doctorId, String patientId) {
        String sql = "SELECT IF (m1.from_account = ?, 1, 2) lastPeople,m1.id,m1.msgTime,CASE WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(7 * 24 * 60 * 60)THEN from_unixtime(m1.msgTime,'%Y/%m/%d %H:%i') WHEN UNIX_TIMESTAMP(now())- m1.msgTime >(24 * 60 * 60)THEN from_unixtime(m1.msgTime, '%m/%d %H:%i') WHEN TO_DAYS(FROM_UNIXTIME(m1.msgTime))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(m1.msgTime,'%H:%i'))ELSE from_unixtime(m1.msgTime, '%H:%i') END lastTime,CASE m1.msgType WHEN 1 THEN mc.content WHEN 2 THEN '[图片]' WHEN 3 THEN '[语音]' WHEN 5 THEN '[地址]' WHEN 6 THEN '[症状描述填写]' WHEN 7 THEN '[症状描述填写完成]' WHEN 8 THEN '[问诊单填写]' WHEN 9 THEN '[问诊单填写完成]' WHEN 10 THEN '[复诊单填写]' WHEN 11 THEN '[复诊单填写完成]' WHEN 12 THEN '[调理方案]' WHEN 13 THEN '[调理方案已付款]' WHEN 14 THEN '[赠送提问]' WHEN 15 THEN '[补缴挂号费]' WHEN 16 THEN '[挂号费已补缴]' WHEN 17 THEN '[结束问诊]' WHEN 18 THEN '[消息撤回]' WHEN 19 THEN '[文章推荐]' WHEN 20 THEN '[商品推荐]' WHEN 21 THEN '[坐诊信息]' WHEN 22 THEN '[帮忙认证]' WHEN 23 THEN '[服务购买]' WHEN 24 THEN '[问诊将于10分钟后结束]' WHEN 25 THEN '[已到复诊时间]' WHEN 26 THEN '[调理方案作废]' WHEN 27 THEN '[用药说明]' WHEN 28 THEN '[接通电话成功]' WHEN 29 THEN '[接通电话失败]' WHEN 30 THEN '[新增就诊人抄方]' ELSE '您有一条新消息' END lastMsg  FROM t_msg m1 INNER JOIN t_msg_content mc ON mc.msgId=m1.id WHERE m1.state =1 and m1.toDel=1 and m1.msgType <> 27 and ((m1.from_account=? AND m1.to_account=?) OR (m1.from_account=? AND m1.to_account=?)) ORDER BY m1.msgTime DESC LIMIT 0,1";
        Map<String, Object> map = null;
        try {
            //执行查询
            map = jdbcTemplate.queryForMap(sql, new Object[]{patientId, doctorId, patientId, patientId, doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询Sr
     */
    @Override
    public Map<String, Object> querySr(String doctorId) {
        String sql = "SELECT '医生助理' serverName,'' serverId,2 paymentStatus,UNIX_TIMESTAMP()-10 endTime,1 syZxCount,sp.imgUrl,sp.`name` patientName,sp.srId patientId,d.doctorId,null age,sp.phone,IF(0 = sp.sex, '男', '女')sex,1 type FROM t_follow_history fh INNER JOIN t_sales_represent sp ON sp.srId=fh.followId INNER JOIN t_doctor d ON d.doctorId=fh.openId WHERE d.doctorId=?";
        Map<String, Object> map = null;
        try {
            //执行查询
            map = jdbcTemplate.queryForMap(sql, new Object[]{doctorId});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 查询Sig
     */
    @Override
    public Map<String, Object> querySig(String identifier) {
        Map<String, Object> map = null;
        try {
            String sql = "select identifier,sig,addTime from t_im_sig where identifier=? ";
            if (!StringUtil.isEmpty(identifier)) {
                //执行查询
                map = jdbcTemplate
                        .queryForMap(sql, new Object[]{identifier});
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return map;
    }

    /**
     * 更新Sig
     */
    @Override
    public Integer updateSig(String identifier, String sig) {
        Integer count = 0;
        try {
            String sql = "update t_im_sig set sig=?,addTime=UNIX_TIMESTAMP()+160*24*60*60 where identifier=? ";
            if (!StringUtil.isEmpty(identifier) && !StringUtil.isEmpty(sig)) {
                //执行更新
                count = jdbcTemplate.update(sql,
                        new Object[]{sig, identifier});
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return count;
    }

    /**
     * 插入Sig
     */
    @Override
    public Object insertSig(String identifier, String sig) {
        Integer count = 0;
        try {
            if (!StringUtil.isEmpty(identifier) && !StringUtil.isEmpty(sig)) {
                String sql = "INSERT INTO t_im_sig(identifier,sig,addTime) VALUES('"
                        + identifier + "','" + sig + "',UNIX_TIMESTAMP()) ";
                //执行更新
                count = jdbcTemplate.update(sql);
            }
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return count;
    }

    @Override
    public List<Map<String, Object>> findPatients(final String doctorId, Integer pageNo) {
        Integer startIndex = (pageNo - 1) * 10;
        String sql = "\n" +
                "SELECT\n" +
                "  g.msg_time as msgTime, g.last_time as lastTime, g.last_msg as lastMsg,\n" +
                "  f.serverName,f.serverId,f.paymentStatus,f.endTime,f.syZxCount,f.imgUrl,f.patientName,f.patientId,f.doctorId,f.age,f.phone,f.sex,f.type\n" +
                "FROM\n" +
                "  (\n" +
                "    SELECT\n" +
                "      (\n" +
                "        SELECT s.`name`\n" +
                "        FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo=o.orderNo INNER JOIN t_server_type s ON s.id=o.orderType\n" +
                "        WHERE os.doctorId=dp.doctorId  AND o.patientId=dp.patientId\n" +
                "        ORDER BY payTime DESC LIMIT 0,1\n" +
                "      ) serverName,\n" +
                "      (\n" +
                "        SELECT s.id\n" +
                "        FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo=o.orderNo INNER JOIN t_server_type s ON s.id=o.orderType\n" +
                "        WHERE os.doctorId=dp.doctorId AND o.patientId=dp.patientId\n" +
                "        ORDER BY payTime DESC LIMIT 0,1\n" +
                "      ) serverId,\n" +
                "      (\n" +
                "        SELECT o.paymentStatus\n" +
                "        FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo=o.orderNo INNER JOIN t_server_type s ON s.id=o.orderType\n" +
                "        WHERE os.doctorId=dp.doctorId AND o.patientId=dp.patientId\n" +
                "        ORDER BY payTime DESC LIMIT 0,1\n" +
                "      ) paymentStatus,\n" +
                "      (\n" +
                "        SELECT o.endTime\n" +
                "        FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo=o.orderNo INNER JOIN t_server_type s ON s.id=o.orderType\n" +
                "        WHERE os.doctorId=dp.doctorId AND o.patientId=dp.patientId\n" +
                "        ORDER BY payTime DESC LIMIT 0,1\n" +
                "      ) endTime,\n" +
                "      (\n" +
                "        SELECT os.syZxCount\n" +
                "        FROM t_order o INNER JOIN t_order_detail_server os ON os.orderNo=o.orderNo INNER JOIN t_server_type s ON s.id=o.orderType\n" +
                "        WHERE os.doctorId=dp.doctorId AND o.patientId=dp.patientId\n" +
                "        ORDER BY payTime DESC LIMIT 0,1\n" +
                "      ) syZxCount,\n" +
                "      CASE\n" +
                "        WHEN pd.nexus='本人' THEN m.photo ELSE ''\n" +
                "      END imgUrl,\n" +
                "      pd.`name` patientName,pd.id patientId,d.doctorId,pd.age,pd.phone,\n" +
                "      IF(0=pd.sex,'男','女') sex,\n" +
                "      2 type\n" +
                "    FROM\n" +
                "      t_doctor_patient dp\n" +
                "      INNER JOIN t_doctor d ON dp.doctorId=d.doctorId\n" +
                "      INNER JOIN t_patient_data pd ON pd.id=dp.patientId\n" +
                "      INNER JOIN t_member m ON m.id=pd.memberId\n" +
                "    WHERE\n" +
                "      d.doctorId=? AND d.docStatus='10' AND d.docIsOn='1' AND dp.isDelete=0\n" +
                "  ) f LEFT join\n" +
                "  (\n" +
                "    SELECT\n" +
                "      a.patient_id,a.msg_time,\n" +
                "      CASE\n" +
                "          WHEN UNIX_TIMESTAMP(now())-a.msg_time>(7*24*60*60) THEN from_unixtime(a.msg_time,'%Y/%m/%d %H:%i')\n" +
                "          WHEN UNIX_TIMESTAMP(now())-a.msg_time>(24*60*60) THEN from_unixtime(a.msg_time,'%m/%d %H:%i')\n" +
                "          WHEN TO_DAYS(FROM_UNIXTIME(a.msg_time))=TO_DAYS(NOW())-1 THEN CONCAT('昨天 ',FROM_UNIXTIME(a.msg_time,'%H:%i'))\n" +
                "          ELSE from_unixtime(a.msg_time,'%H:%i')\n" +
                "      END AS last_time,\n" +
                "      CASE a.msg_type\n" +
                "          WHEN 1 THEN b.content WHEN 2 THEN '[图片]' WHEN 3 THEN '[语音]' WHEN 5 THEN '[地址]' WHEN 6 THEN '[症状描述填写]' WHEN 7 THEN '[症状描述填写完成]'\n" +
                "          WHEN 8 THEN '[问诊单填写]' WHEN 9 THEN '[问诊单填写完成]' WHEN 10 THEN '[复诊单填写]' WHEN 11 THEN '[复诊单填写完成]' WHEN 12 THEN '[调理方案]'\n" +
                "          WHEN 13 THEN '[调理方案已付款]' WHEN 14 THEN '[赠送提问]' WHEN 15 THEN '[补缴挂号费]' WHEN 16 THEN '[挂号费已补缴]' WHEN 17 THEN '[结束问诊]'\n" +
                "          WHEN 18 THEN '[消息撤回]' WHEN 19 THEN '[文章推荐]' WHEN 20 THEN '[商品推荐]' WHEN 21 THEN '[坐诊信息]' WHEN 22 THEN '[帮忙认证]' WHEN 23 THEN '[服务购买]'\n" +
                "          WHEN 24 THEN '[问诊将于10分钟后结束]' WHEN 25 THEN '[已到复诊时间]' WHEN 26 THEN '[调理方案作废]' WHEN 27 THEN '[用药说明]'\n" +
                "          WHEN 28 THEN '[接通电话成功]' WHEN 29 THEN '[接通电话失败]' WHEN 30 THEN '[新增就诊人抄方]' ELSE '您有一条新消息'\n" +
                "      END AS last_msg\n" +
                "    FROM\n" +
                "      (\n" +
                "        SELECT max(t.msg_time) AS msg_time,t.patient_id,t.msg_type,id\n" +
                "        FROM\n" +
                "          (\n" +
                "            (SELECT to_account AS patient_id, msgTime AS msg_time, msgType AS msg_type, id FROM t_msg WHERE from_account=?)\n" +
                "            UNION ALL\n" +
                "            (SELECT from_account AS patient_id, msgTime AS msg_time, msgType AS msg_type, id FROM t_msg WHERE to_account=?)\n" +
                "          ) t\n" +
                "        GROUP BY t.patient_id\n" +
                "      ) a INNER JOIN t_msg_content b ON b.msgId=a.id\n" +
                "      ORDER BY a.msg_time DESC\n" +
                "  ) g on f.patientId=g.patient_id\n" +
                "ORDER BY g.msg_time desc \n" +
                "limit ?,10";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new Object[]{doctorId, doctorId, doctorId, startIndex});

        for (Map<String, Object> map : maps) {
            if (null == map.get("msgTime")) {
                map.put("msgTime", 0);
            }
        }

        return maps;
    }
}