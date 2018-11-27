package cn.syrjia.sales.service;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.sales.param.ReplyData;

import java.util.List;
import java.util.Map;

public interface SalesGoalDayService extends BaseServiceInterface {

    Map<String, String> getSales(String salesId);

    List<Map<String, String>> findSalesList(String salesId);

    List<Map<String, String>> findDoctorList(String salesId);

    Map<String, Object> queryIndexPageData(String salesId, String doctorId, String day);

    ReplyData queryWeekPageData(String salesId, Integer type, String yearMonth,
                                String startDay, String endDay);

    ReplyData queryDayPageData(String salesId, Integer type, String yearMonth,
                               String startDay, String endDay);

    ReplyData setSelfQuota(String salesId, Integer type, String day, String quota, String oldQuota);

}
