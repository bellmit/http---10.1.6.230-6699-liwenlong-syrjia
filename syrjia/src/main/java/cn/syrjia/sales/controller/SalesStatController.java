package cn.syrjia.sales.controller;

import cn.syrjia.sales.param.ReplyData;
import cn.syrjia.sales.service.SalesGoalDayService;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/appSalesStat")
public class SalesStatController {

    @Autowired
    private SalesGoalDayService salesGoalDayService;

    @RequestMapping("/index")
    public String index(String salesId, ModelMap modelMap) {
        List<Map<String, String>> salesList = new ArrayList<>();
        salesList.add(salesGoalDayService.getSales(salesId));
        salesList.addAll(salesGoalDayService.findSalesList(salesId));

        List<Map<String, String>> doctorList = salesGoalDayService.findDoctorList(salesId);


        modelMap.put("salesId", salesId);
        modelMap.put("salesList", JSONArray.fromObject(salesList).toString());
        modelMap.put("doctorList", JSONArray.fromObject(doctorList).toString());

        return "salesStat/sales";
    }

    @RequestMapping("/index/findDoctors")
    @ResponseBody
    public List<Map<String, String>> indexFindDoctors(String salesId) {
        return salesGoalDayService.findDoctorList(salesId);
    }

    @RequestMapping("/index/data")
    @ResponseBody
    public Map<String, Object> indexData(String salesId, String doctorId, String day) {
        return salesGoalDayService.queryIndexPageData(salesId, doctorId, day);
    }

    @RequestMapping("/week/index")
    @ResponseBody
    public ReplyData weekIndex(String salesId, Integer type, String yearMonth, String startDay, String endDay) {
        return salesGoalDayService.queryWeekPageData(salesId, type, yearMonth, startDay, endDay);
    }

    @RequestMapping("/day/index")
    @ResponseBody
    public ReplyData dayIndex(String salesId, Integer type, String yearMonth, String startDay, String endDay) {
        return salesGoalDayService.queryDayPageData(salesId, type, yearMonth, startDay, endDay);
    }

    @RequestMapping("/day/set")
    @ResponseBody
    public ReplyData daySet(String salesId, Integer type, String day, String quota, String oldQuota) {
        return salesGoalDayService.setSelfQuota(salesId, type, day, quota, oldQuota);
    }

}
