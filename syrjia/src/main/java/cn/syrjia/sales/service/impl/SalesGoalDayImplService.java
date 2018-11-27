package cn.syrjia.sales.service.impl;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.sales.dao.SalesGoalDayDao;
import cn.syrjia.sales.entity.StatBean;
import cn.syrjia.sales.param.ReplyData;
import cn.syrjia.sales.service.SalesGoalDayService;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("salesGoalDayService")
public class SalesGoalDayImplService extends BaseServiceImpl implements SalesGoalDayService {

    private static final String STAT_RESULT = "stat_result";
    private static final BigDecimal DEFAULT_MONEY = new BigDecimal("0.00");
    private static final int DEFAULT_QUANTITY = 0;

    @Autowired
    private SalesGoalDayDao salesGoalDayDao;

    public Map<String, String> getSales(String salesId) {
        final Map<String, Object> map = salesGoalDayDao.queryBysqlMap("select srId,name from t_sales_represent where srId=?", new Object[]{salesId});
        return new HashMap<String, String>() {{
            put("value", map.get("srId").toString());
            put("text", "本人");
        }};
    }

    public List<Map<String, String>> findSalesList(String salesId) {
        List<Map<String, String>> list = new ArrayList<>();

        String sql = "select a.isDirector, b.id, b.code from t_sales_represent a, t_sales_channel b" +
                " where a.srId=? and a.saleChannelId=b.id;";
        List<Map<String, Object>> maps = salesGoalDayDao.queryBysqlList(sql, new Object[]{salesId});
        if (maps.isEmpty()) return list;

        Map<String, Object> map1 = maps.get(0);
        if ((int) map1.get("isDirector") == 0) return list;

        String saleChannelId = map1.get("id").toString();
        String code = map1.get("code").toString();

        List<String> channelIds = Lists.newArrayList();
        channelIds.add(saleChannelId);
        findAllChannels(code, channelIds);

        sql = "select srId,name from t_sales_represent where srId!=? and saleChannelId in (";
        for (String channelId : channelIds) {
            sql += "'" + channelId + "',";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ")";

        maps = salesGoalDayDao.queryBysqlList(sql, new Object[]{salesId});
        for (final Map<String, Object> map : maps) {
            list.add(new HashMap<String, String>() {{
                put("value", map.get("srId").toString());
                put("text", map.get("name").toString());
            }});
        }

        return list;
    }

    public List<Map<String, String>> findDoctorList(String salesId) {
        String sql = "select a.doctorId,a.docName from t_doctor a,t_follow_history b where b.followId=? AND a.doctorId=b.openId order by a.docName";
        List<Map<String, Object>> maps = salesGoalDayDao.queryBysqlList(sql, new Object[]{salesId});

        List<Map<String, String>> list = new ArrayList<>();
        list.add(new HashMap<String, String>() {{
            put("value", "");
            put("text", "全部");
        }});
        for (final Map<String, Object> map : maps) {
            list.add(new HashMap<String, String>() {{
                put("value", map.get("doctorId").toString());
                put("text", map.get("docName").toString());
            }});
        }

        return list;
    }

    public Map<String, Object> queryIndexPageData(String salesId, String doctorId, String day) {
        Map<String, Object> dataMap = new HashMap<>();

        Map<String, StatBean> daysQuota = this.getMonthQuota(salesId, day.substring(0, 6));
        Map<String, StatBean> daysDone = this.getMonthDone(salesId, day.substring(0, 6), doctorId);

        dataMap.put("daysQuota", daysQuota);
        dataMap.put("daysDone", daysDone);

        int doctorOnlineQuota = 0;
        BigDecimal prescriptionSumQuota = DEFAULT_MONEY;
        int prescriptionQuantityQuota = 0;
        BigDecimal recuperateSumQuota = DEFAULT_MONEY;
        int recuperateQuantityQuota = 0;
        BigDecimal consultationSumQuota = DEFAULT_MONEY;
        int consultationQuantityQuota = 0;
        BigDecimal commoditySumQuota = DEFAULT_MONEY;
        int commodityQuantityQuota = 0;
        for (StatBean statBean : daysQuota.values()) {
            doctorOnlineQuota += statBean.getDoctorOnline();
            prescriptionSumQuota = prescriptionSumQuota.add(statBean.getPrescriptionSum());
            prescriptionQuantityQuota += statBean.getPrescriptionQuantity();
            recuperateSumQuota = recuperateSumQuota.add(statBean.getRecuperateSum());
            recuperateQuantityQuota += statBean.getRecuperateQuantity();
            consultationSumQuota = consultationSumQuota.add(statBean.getConsultationSum());
            consultationQuantityQuota += statBean.getConsultationQuantity();
            commoditySumQuota = commoditySumQuota.add(statBean.getCommoditySum());
            commodityQuantityQuota += statBean.getCommodityQuantity();

            if (statBean.getDay().equalsIgnoreCase(day)) {
                dataMap.put("focusDayQuota", statBean);
            }
        }
        Map<String, String> mapQuota = new HashMap<>();
        mapQuota.put("doctorOnline", doctorOnlineQuota + "");
        mapQuota.put("prescriptionSum", prescriptionSumQuota.doubleValue() + "");
        mapQuota.put("prescriptionQuantity", prescriptionQuantityQuota + "");
        mapQuota.put("recuperateSum", recuperateSumQuota.doubleValue() + "");
        mapQuota.put("recuperateQuantity", recuperateQuantityQuota + "");
        mapQuota.put("consultationSum", consultationSumQuota.doubleValue() + "");
        mapQuota.put("consultationQuantity", consultationQuantityQuota + "");
        mapQuota.put("commoditySum", commoditySumQuota.doubleValue() + "");
        mapQuota.put("commodityQuantity", commodityQuantityQuota + "");
        mapQuota.put("allSum", (prescriptionSumQuota.add(recuperateSumQuota).add(consultationSumQuota).add(commoditySumQuota).doubleValue() + ""));
        mapQuota.put("allQuantity", (prescriptionQuantityQuota + recuperateQuantityQuota + consultationQuantityQuota + commodityQuantityQuota) + "");
        dataMap.put("monthQuota", mapQuota);

        int doctorOnlineDone = 0;
        BigDecimal prescriptionSumDone = DEFAULT_MONEY;
        int prescriptionQuantityDone = 0;
        BigDecimal recuperateSumDone = DEFAULT_MONEY;
        int recuperateQuantityDone = 0;
        BigDecimal consultationSumDone = DEFAULT_MONEY;
        int consultationQuantityDone = 0;
        BigDecimal commoditySumDone = DEFAULT_MONEY;
        int commodityQuantityDone = 0;
        for (StatBean statBean : daysDone.values()) {
            doctorOnlineDone += statBean.getDoctorOnline();
            prescriptionSumDone = prescriptionSumDone.add(statBean.getPrescriptionSum());
            prescriptionQuantityDone += statBean.getPrescriptionQuantity();
            recuperateSumDone = recuperateSumDone.add(statBean.getRecuperateSum());
            recuperateQuantityDone += statBean.getRecuperateQuantity();
            consultationSumDone = consultationSumDone.add(statBean.getConsultationSum());
            consultationQuantityDone += statBean.getConsultationQuantity();
            commoditySumDone = commoditySumDone.add(statBean.getCommoditySum());
            commodityQuantityDone += statBean.getCommodityQuantity();

            if (statBean.getDay().equalsIgnoreCase(day)) {
                dataMap.put("focusDayDone", statBean);
            }
        }
        Map<String, String> mapDone = new HashMap<>();
        mapDone.put("doctorOnline", doctorOnlineDone + "");
        mapDone.put("prescriptionSum", prescriptionSumDone.doubleValue() + "");
        mapDone.put("prescriptionQuantity", prescriptionQuantityDone + "");
        mapDone.put("recuperateSum", recuperateSumDone.doubleValue() + "");
        mapDone.put("recuperateQuantity", recuperateQuantityDone + "");
        mapDone.put("consultationSum", consultationSumDone.doubleValue() + "");
        mapDone.put("consultationQuantity", consultationQuantityDone + "");
        mapDone.put("commoditySum", commoditySumDone.doubleValue() + "");
        mapDone.put("commodityQuantity", commodityQuantityDone + "");
        mapDone.put("allSum", (prescriptionSumDone.add(recuperateSumDone).add(consultationSumDone).add(commoditySumDone)).doubleValue() + "");
        mapDone.put("allQuantity", (prescriptionQuantityDone + recuperateQuantityDone + consultationQuantityDone + commodityQuantityDone) + "");
        dataMap.put("monthDone", mapDone);

        return dataMap;
    }

    public ReplyData queryWeekPageData(String salesId, Integer type, String yearMonth,
                                       String startDay, String endDay) {
        if (StringUtils.isBlank(salesId)) return new ReplyData("助理不能为空");
        if (type == null) return new ReplyData("统计类型不能空");
        if (StringUtils.isBlank(yearMonth)) return new ReplyData("年月不能为空");
        if (StringUtils.isBlank(startDay)) return new ReplyData("开始日期不能为空");
        if (StringUtils.isBlank(endDay)) return new ReplyData("结束日期不能为空");

        Map<String, Object> dataMap = new HashMap<>();

        String monthQuota = this.getMonthQuotaByType(type, salesId, yearMonth);
        dataMap.put("monthQuota", monthQuota);

        String monthDone = this.getMonthDoneByType(type, salesId, yearMonth);
        dataMap.put("monthDone", monthDone);

        DateTime startDateTime = DateTime.parse(startDay, DateTimeFormat.forPattern("yyyyMMdd"));
        DateTime endDateTime = DateTime.parse(endDay, DateTimeFormat.forPattern("yyyyMMdd")).plusDays(1);
        int days = Days.daysBetween(startDateTime, endDateTime).getDays();
        if (days % 7 != 0) return new ReplyData("日期期间不合法");

        int group = days / 7;
        List<Map<String, String>> weekList = new LinkedList<>();
        for (int i = 0; i < group; i++) weekList.add(i, new HashMap<String, String>());

        List<String> list = new ArrayList<>();
        for (int i = 0; i < group; i++) {
            list.add(startDateTime.toString("yyyyMMdd") + "-" + startDateTime.plusDays(6).toString("yyyyMMdd"));
            startDateTime = startDateTime.plusDays(7);
        }

        int i = 0;
        for (String s : list) {
            String[] split = s.split("-");
            String weekQuota = this.getDayPeriodQuotaByType(type, salesId, split[0], split[1]);
            String weekDone = this.getDayPeriodDoneByType(type, salesId, split[0], split[1]);

            Map<String, String> stringStringMap = weekList.get(i++);
            stringStringMap.put("weekQuota", weekQuota);
            stringStringMap.put("weekDone", weekDone);
        }

        dataMap.put("weekData", weekList);

        ReplyData replyData = new ReplyData();
        replyData.setData(dataMap);

        return replyData;
    }

    public ReplyData queryDayPageData(String salesId, Integer type, String yearMonth,
                                      String startDay, String endDay) {
        if (StringUtils.isBlank(salesId)) return new ReplyData("助理不能为空");
        if (type == null) return new ReplyData("统计类型不能为空");
        if (StringUtils.isBlank(yearMonth)) return new ReplyData("年月不能为空");
        if (StringUtils.isBlank(startDay)) return new ReplyData("开始日期不能为空");
        if (StringUtils.isBlank(endDay)) return new ReplyData("结束日期不能为空");

        Map<String, Object> dataMap = new HashMap<>();

        String monthQuota = this.getMonthQuotaByType(type, salesId, yearMonth);
        dataMap.put("monthQuota", monthQuota);

        String monthDone = this.getMonthDoneByType(type, salesId, yearMonth);
        dataMap.put("monthDone", monthDone);

        List<Map<String, String>> dayDataList = new ArrayList<>();
        DateTime startDateTime = DateTime.parse(startDay, DateTimeFormat.forPattern("yyyyMMdd"));
        DateTime endDateTime = DateTime.parse(endDay, DateTimeFormat.forPattern("yyyyMMdd")).plusDays(1);
        int days = Days.daysBetween(startDateTime, endDateTime).getDays();
        if (days != 7) return new ReplyData("日期期间不合法");

        BigDecimal weekQuote = new BigDecimal("0");
        BigDecimal weekDone = new BigDecimal("0");
        for (int i = 0; i < 7; i++) {
            final String dayQuota = this.getDayQuotaByType(type, salesId, startDateTime.plusDays(i).toString("yyyyMMdd"));
            final String dayDone = this.getDayDoneByType(type, salesId, startDateTime.plusDays(i).toString("yyyyMMdd"));
            dayDataList.add(i, new HashMap<String, String>() {{
                put("dayQuota", dayQuota);
                put("dayDone", dayDone);
            }});

            weekQuote = weekQuote.add(new BigDecimal(dayQuota));
            weekDone = weekDone.add(new BigDecimal(dayDone));
        }

        dataMap.put("weekQuota", weekQuote.toString());
        dataMap.put("weekDone", weekDone.toString());

        dataMap.put("dayData", dayDataList);

        ReplyData replyData = new ReplyData();
        replyData.setData(dataMap);

        return replyData;
    }

    public ReplyData setSelfQuota(String salesId, Integer type, String day, String quota, String oldQuota) {
        if (StringUtils.isBlank(salesId)) return new ReplyData("追不能为空");
        if (type == null) return new ReplyData("统计类型不能为空");
        if (StringUtils.isBlank(day)) return new ReplyData("日期不能为空");
        if (quota == null) return new ReplyData("新指标值不能为空");
        if (oldQuota == null) return new ReplyData("旧指标值不能为空");

        String sysMonthQuota = this.getSysMonthQuotaByType(type, salesId, day.substring(0, 6));
        String monthQuota = this.getMonthQuotaByType(type, salesId, day.substring(0, 6));

        if (new BigDecimal(monthQuota).add(
                new BigDecimal(quota).subtract(new BigDecimal(oldQuota)))
                .compareTo(new BigDecimal(sysMonthQuota)) < 0) {
            ReplyData replyData = new ReplyData();
            replyData.setRespCode("3001");
            replyData.setRespMsg("指标值过小");
            return replyData;
        }

        StateTypeEnum stateTypeEnum = StateTypeEnum.getByType(type);
        String colAsName = stateTypeEnum.getValue() + "_self";
        String sql = "update t_sales_goal_day set " + colAsName + "=? where sales_id=? and day=?";
        salesGoalDayDao.update(sql, new Object[]{quota, salesId, day});

        ReplyData replyData = new ReplyData();
        replyData.setRespMsg("success");

        return replyData;
    }

    private Map<String, StatBean> getMonthQuota(String salesId, String yearMonth) {
        Map<String, StatBean> resultMap = new LinkedHashMap<>();

        DateTime dateTime = DateTime.parse(yearMonth + "01", DateTimeFormat.forPattern("yyyyMMdd"));
        int maximumValue = dateTime.dayOfMonth().getMaximumValue();
        for (int i = 0; i < maximumValue; i++) {
            StatBean statBean = new StatBean();
            statBean.setDay(dateTime.plusDays(i).toString("yyyyMMdd"));
            resultMap.put(dateTime.plusDays(i).toString("yyyyMMdd"), statBean);
        }

        String doctor_online_sql = "(IF(doctor_online_self=0,doctor_online,doctor_online_self)) as doctorOnline";
        String prescription_sum_sql = "(IF(prescription_sum_self=0,prescription_sum,prescription_sum_self)) as prescriptionSum";
        String prescription_quantity_sql = "(IF(prescription_quantity_self=0,prescription_quantity,prescription_quantity_self)) as prescriptionQuantity";
        String recuperate_sum_sql = "(IF(recuperate_sum_self=0,graphic_recuperate_sum+phone_recuperate_sum,recuperate_sum_self)) as recuperateSum";
        String recuperate_quantity_sql = "(IF(recuperate_quantity_self=0,graphic_recuperate_quantity+phone_recuperate_quantity,recuperate_quantity_self)) as recuperateQuantity";
        String consultation_sum_sql = "(IF(consultation_sum_self=0,graphic_consultation_sum+phone_consultation_sum,consultation_sum_self)) as consultationSum";
        String consultation_quantity_sql = "(IF(consultation_quantity_self=0,graphic_consultation_quantity+phone_consultation_quantity,consultation_quantity_self)) as consultationQuantity";
        String commodity_sum_sql = "(IF(commodity_sum_self=0,commodity_sum,commodity_sum_self)) as commoditySum";
        String commodity_quantity_sql = "(IF(commodity_quantity_self=0,commodity_quantity,commodity_quantity_self)) as commodityQuantity";
        String columns = doctor_online_sql + "," + prescription_sum_sql + "," + prescription_quantity_sql + ","
                + recuperate_sum_sql + "," + recuperate_quantity_sql + "," + consultation_sum_sql + ","
                + consultation_quantity_sql + "," + commodity_sum_sql + "," + commodity_quantity_sql;

        String sql = "select day," + columns + " from t_sales_goal_day where sales_id=? and day like ? order by day asc";

        List<Map<String, Object>> maps = salesGoalDayDao.queryBysqlList(sql, new Object[]{salesId, yearMonth + "%"});
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setDoctorOnline(map.get("doctorOnline") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("doctorOnline").toString()));
                statBean.setPrescriptionSum(map.get("prescriptionSum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("prescriptionSum").toString()));
                statBean.setPrescriptionQuantity(map.get("prescriptionQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("prescriptionQuantity").toString()));
                statBean.setRecuperateSum(map.get("recuperateSum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("recuperateSum").toString()));
                statBean.setRecuperateQuantity(map.get("recuperateQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("recuperateQuantity").toString()));
                statBean.setConsultationSum(map.get("consultationSum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("consultationSum").toString()));
                statBean.setConsultationQuantity(map.get("consultationQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("consultationQuantity").toString()));
                statBean.setCommoditySum(map.get("commoditySum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("commoditySum").toString()));
                statBean.setCommodityQuantity(map.get("commodityQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("commodityQuantity").toString()));
            }
        }

        return resultMap;
    }

    private Map<String, StatBean> getMonthDone(String salesId, String yearMonth, String doctorId) {
        Map<String, StatBean> resultMap = new LinkedHashMap<>();

        DateTime dateTime = DateTime.parse(yearMonth + "01", DateTimeFormat.forPattern("yyyyMMdd"));
        int maximumValue = dateTime.dayOfMonth().getMaximumValue();
        for (int i = 0; i < maximumValue; i++) {
            StatBean statBean = new StatBean();
            statBean.setDay(dateTime.plusDays(i).toString("yyyyMMdd"));
            resultMap.put(dateTime.plusDays(i).toString("yyyyMMdd"), statBean);
        }

        String sql;
        List<Map<String, Object>> maps;
        Object[] objects;

        sql = "SELECT count(*) as doctorOnline,FROM_UNIXTIME(a.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND FROM_UNIXTIME(a.createTime,'%Y%m')=? GROUP BY FROM_UNIXTIME(a.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, new Object[]{salesId, yearMonth});
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setDoctorOnline(map.get("doctorOnline") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("doctorOnline").toString()));
            }
        }

        sql = "SELECT sum(orderPrice-postage) as prescriptionSum,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setPrescriptionSum(map.get("prescriptionSum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("prescriptionSum").toString()));
            }
        }

        sql = "SELECT count(*) as prescriptionQuantity,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setPrescriptionQuantity(map.get("prescriptionQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("prescriptionQuantity").toString()));
            }
        }

        sql = "SELECT sum(orderPrice-postage) as recuperateSum,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=4 or c.orderType=7) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setRecuperateSum(map.get("recuperateSum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("recuperateSum").toString()));
            }
        }

        sql = "SELECT count(*) as recuperateQuantity,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=4 or c.orderType=7) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setRecuperateQuantity(map.get("recuperateQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("recuperateQuantity").toString()));
            }
        }

        sql = "SELECT sum(orderPrice-postage) as consultationSum,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=6 or c.orderType=8) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setConsultationSum(map.get("consultationSum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("consultationSum").toString()));
            }
        }

        sql = "SELECT count(*) as consultationQuantity,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=6 or c.orderType=8) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setConsultationQuantity(map.get("consultationQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("consultationQuantity").toString()));
            }
        }

        sql = "SELECT sum(orderPrice-postage) as commoditySum,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=1) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setCommoditySum(map.get("commoditySum") == null ?
                        DEFAULT_MONEY : new BigDecimal(map.get("commoditySum").toString()));
            }
        }

        sql = "SELECT count(*) as commodityQuantity,FROM_UNIXTIME(c.createTime,'%Y%m%d') as day FROM t_doctor a,t_follow_history b,t_order c " +
                "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                "AND (c.orderType=1) " +
                "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (StringUtils.isNotBlank(doctorId)) {
            sql += " AND b.openId=?";
            objects = new Object[]{salesId, yearMonth, doctorId};
        } else {
            objects = new Object[]{salesId, yearMonth};
        }
        sql += " GROUP BY FROM_UNIXTIME(c.createTime,'%Y%m%d')";
        maps = salesGoalDayDao.queryBysqlList(sql, objects);
        for (Map<String, Object> map : maps) {
            Object day = map.get("day");
            if (resultMap.containsKey(day.toString())) {
                StatBean statBean = resultMap.get(day.toString());
                statBean.setCommodityQuantity(map.get("commodityQuantity") == null ?
                        DEFAULT_QUANTITY : Integer.valueOf(map.get("commodityQuantity").toString()));
            }
        }

        return resultMap;
    }

    /**
     * 根据类型查询月指标
     *
     * @param type      类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId   助理id
     * @param yearMonth 月份。eg：201810
     * @return 月指标
     */
    private String getMonthQuotaByType(Integer type, String salesId, String yearMonth) {
        String doctor_online_col = "sum(IF(doctor_online_self=0,doctor_online,doctor_online_self))";
        String prescription_sum_col = "sum(IF(prescription_sum_self=0,prescription_sum,prescription_sum_self))";
        String prescription_quantity_col = "sum(IF(prescription_quantity_self=0,prescription_quantity,prescription_quantity_self))";
        String recuperate_sum_col = "sum(IF(recuperate_sum_self=0,graphic_recuperate_sum+phone_recuperate_sum,recuperate_sum_self))";
        String recuperate_quantity_col = "sum(IF(recuperate_quantity_self=0,graphic_recuperate_quantity+phone_recuperate_quantity,recuperate_quantity_self))";
        String consultation_sum_col = "sum(IF(consultation_sum_self=0,graphic_consultation_sum+phone_consultation_sum,consultation_sum_self))";
        String consultation_quantity_col = "sum(IF(consultation_quantity_self=0,graphic_consultation_quantity+phone_consultation_quantity,consultation_quantity_self))";
        String commodity_sum_col = "sum(IF(commodity_sum_self=0,commodity_sum,commodity_sum_self))";
        String commodity_quantity_col = "sum(IF(commodity_quantity_self=0,commodity_quantity,commodity_quantity_self))";
        String column = "1";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType())) column = doctor_online_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType())) column = prescription_sum_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType())) column = prescription_quantity_col;
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType())) column = recuperate_sum_col;
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType())) column = recuperate_quantity_col;
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType())) column = consultation_sum_col;
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType())) column = consultation_quantity_col;
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType())) column = commodity_sum_col;
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType())) column = commodity_quantity_col;
        String sql = "select " + column + " as " + STAT_RESULT + " from t_sales_goal_day where sales_id=? and day like ?";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, yearMonth + "%"});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * 根据类型查询月完成量
     *
     * @param type      类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId   助理id
     * @param yearMonth 月份。eg：201810
     * @return 月完成量
     */
    private String getMonthDoneByType(Integer type, String salesId, String yearMonth) {
        String sql = "";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND FROM_UNIXTIME(a.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=4 or c.orderType=7) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=4 or c.orderType=7) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=6 or c.orderType=8) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1 or c.orderType=8) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m')=?";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, yearMonth});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * 根据类型查询天期间段指标
     *
     * @param type     类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId  助理id
     * @param startDay 开始天。eg：20181001
     * @param endDay   结束天。eg：20181007
     * @return 天期间段指标
     */
    private String getDayPeriodQuotaByType(Integer type, String salesId, String startDay, String endDay) {
        String doctor_online_col = "sum(IF(doctor_online_self=0,doctor_online,doctor_online_self))";
        String prescription_sum_col = "sum(IF(prescription_sum_self=0,prescription_sum,prescription_sum_self))";
        String prescription_quantity_col = "sum(IF(prescription_quantity_self=0,prescription_quantity,prescription_quantity_self))";
        String recuperate_sum_col = "sum(IF(recuperate_sum_self=0,graphic_recuperate_sum+phone_recuperate_sum,recuperate_sum_self))";
        String recuperate_quantity_col = "sum(IF(recuperate_quantity_self=0,graphic_recuperate_quantity+phone_recuperate_quantity,recuperate_quantity_self))";
        String consultation_sum_col = "sum(IF(consultation_sum_self=0,graphic_consultation_sum+phone_consultation_sum,consultation_sum_self))";
        String consultation_quantity_col = "sum(IF(consultation_quantity_self=0,graphic_consultation_quantity+phone_consultation_quantity,consultation_quantity_self))";
        String commodity_sum_col = "sum(IF(commodity_sum_self=0,commodity_sum,commodity_sum_self))";
        String commodity_quantity_col = "sum(IF(commodity_quantity_self=0,commodity_quantity,commodity_quantity_self))";
        String column = "1";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType())) column = doctor_online_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType())) column = prescription_sum_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType())) column = prescription_quantity_col;
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType())) column = recuperate_sum_col;
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType())) column = recuperate_quantity_col;
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType())) column = consultation_sum_col;
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType())) column = consultation_quantity_col;
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType())) column = commodity_sum_col;
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType())) column = commodity_quantity_col;
        String sql = "select " + column + " as " + STAT_RESULT + " from t_sales_goal_day where sales_id=? and day>=? and day<=? order by day asc";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, startDay, endDay});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * 根据类型查询天期间段完成量
     *
     * @param type     类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId  助理id
     * @param startDay 开始天。eg：20181001
     * @param endDay   结束天。eg：20181007
     * @return 天期间段完成量
     */
    private String getDayPeriodDoneByType(Integer type, String salesId, String startDay, String endDay) {
        String sql = "";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND FROM_UNIXTIME(a.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(a.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=4 or c.orderType=7) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=4 or c.orderType=7) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=6 or c.orderType=8) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=6 or c.orderType=8) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')>=? and FROM_UNIXTIME(c.createTime,'%Y%m%d')<=?";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, startDay, endDay});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * 根据类型查询天指标
     *
     * @param type    类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId 助理id
     * @param day     天。eg：20181001
     * @return 天完成量
     */
    private String getDayQuotaByType(Integer type, String salesId, String day) {
        String doctor_online_col = "(IF(doctor_online_self=0,doctor_online,doctor_online_self))";
        String prescription_sum_col = "(IF(prescription_sum_self=0,prescription_sum,prescription_sum_self))";
        String prescription_quantity_col = "sum(IF(prescription_quantity_self=0,prescription_quantity,prescription_quantity_self))";
        String recuperate_sum_col = "(IF(recuperate_sum_self=0,graphic_recuperate_sum+phone_recuperate_sum,recuperate_sum_self))";
        String recuperate_quantity_col = "(IF(recuperate_quantity_self=0,graphic_recuperate_quantity+phone_recuperate_quantity,recuperate_quantity_self))";
        String consultation_sum_col = "(IF(consultation_sum_self=0,graphic_consultation_sum+phone_consultation_sum,consultation_sum_self))";
        String consultation_quantity_col = "(IF(consultation_quantity_self=0,graphic_consultation_quantity+phone_consultation_quantity,consultation_quantity_self))";
        String commodity_sum_col = "(IF(commodity_sum_self=0,commodity_sum,commodity_sum_self))";
        String commodity_quantity_col = "(IF(commodity_quantity_self=0,commodity_quantity,commodity_quantity_self))";
        String column = "1";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType())) column = doctor_online_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType())) column = prescription_sum_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType())) column = prescription_quantity_col;
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType())) column = recuperate_sum_col;
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType())) column = recuperate_quantity_col;
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType())) column = consultation_sum_col;
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType())) column = consultation_quantity_col;
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType())) column = commodity_sum_col;
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType())) column = commodity_quantity_col;
        String sql = "select " + column + " as " + STAT_RESULT + " from t_sales_goal_day where sales_id=? and day=?";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, day});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * 根据类型查询天完成量
     *
     * @param type    类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId 助理id
     * @param day     天。eg：20181001
     * @return 天完成量
     */
    private String getDayDoneByType(Integer type, String salesId, String day) {
        String sql = "";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND FROM_UNIXTIME(a.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=10 or c.orderType=13 or c.orderType=14 or c.orderType=15 or c.orderType=16 or c.orderType=17 or c.orderType=18) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=4 or c.orderType=7) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=4 or c.orderType=7) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=6 or c.orderType=8) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=6 or c.orderType=8) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType()))
            sql = "SELECT sum(orderPrice-postage) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType()))
            sql = "SELECT count(*) as " + STAT_RESULT + " FROM t_doctor a,t_follow_history b,t_order c " +
                    "WHERE b.followId=? AND a.doctorId=b.openId AND a.doctorId=c.doctorId AND (c.paymentStatus=2 or c.paymentStatus=5) " +
                    "AND (c.orderType=1) " +
                    "AND FROM_UNIXTIME(c.createTime,'%Y%m%d')=?";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, day});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * 根据类型查询系统月指标
     *
     * @param type      类型。0=医生上线数量；1=药单金额；2=药单成单量；3=挂号金额；4=挂号成单量；5=咨询金额；6=咨询成单量；7=商品金额；8=商品成单量
     * @param salesId   助理id
     * @param yearMonth 年月。eg：201810
     * @return 系统月指标
     */
    private String getSysMonthQuotaByType(Integer type, String salesId, String yearMonth) {
        String doctor_online_col = "sum(doctor_online)";
        String prescription_sum_col = "sum(prescription_sum)";
        String prescription_quantity_col = "sum(prescription_quantity)";
        String recuperate_sum_col = "sum(graphic_recuperate_sum+phone_recuperate_sum)";
        String recuperate_quantity_col = "sum(graphic_recuperate_quantity+phone_recuperate_quantity)";
        String consultation_sum_col = "sum(graphic_consultation_sum+phone_consultation_sum)";
        String consultation_quantity_col = "sum(graphic_consultation_quantity+phone_consultation_quantity)";
        String commodity_sum_col = "sum(commodity_sum)";
        String commodity_quantity_col = "sum(commodity_quantity)";
        String column = "1";
        if (type.equals(StateTypeEnum.DOCTOR_ON_LINE.getType())) column = doctor_online_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_SUM.getType())) column = prescription_sum_col;
        if (type.equals(StateTypeEnum.PRESCRIPTION_QUANTITY.getType())) column = prescription_quantity_col;
        if (type.equals(StateTypeEnum.RECUPERATE_SUM.getType())) column = recuperate_sum_col;
        if (type.equals(StateTypeEnum.RECUPERATE_QUANTITY.getType())) column = recuperate_quantity_col;
        if (type.equals(StateTypeEnum.CONSULTATION_SUM.getType())) column = consultation_sum_col;
        if (type.equals(StateTypeEnum.CONSULTATION_QUANTITY.getType())) column = consultation_quantity_col;
        if (type.equals(StateTypeEnum.COMMODITY_SUM.getType())) column = commodity_sum_col;
        if (type.equals(StateTypeEnum.COMMODITY_QUANTITY.getType())) column = commodity_quantity_col;
        String sql = "select " + column + " as " + STAT_RESULT + " from t_sales_goal_day where sales_id=? and day like ?";

        Map<String, Object> map = salesGoalDayDao.queryBysqlMap(sql, new Object[]{salesId, yearMonth + "%"});
        if (map == null) return "0";
        Object o = map.get(STAT_RESULT);
        return (o == null) ? "0" : o.toString();
    }

    private void findAllChannels(String pCode, List<String> channelIds) {
        String sql = "select id, code from t_sales_channel where pidCode=?";
        List<Map<String, Object>> maps = salesGoalDayDao.queryBysqlList(sql, new Object[]{pCode});

        for (Map<String, Object> map : maps) {
            channelIds.add(map.get("id").toString());
            String code = map.get("code").toString();
            findAllChannels(code, channelIds);
        }
    }
}
