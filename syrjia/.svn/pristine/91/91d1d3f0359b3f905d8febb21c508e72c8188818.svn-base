package cn.syrjia.sales.controller;

import cn.syrjia.sales.entity.SalesPersonalGoal;
import cn.syrjia.sales.param.*;
import cn.syrjia.sales.service.SalesTrainingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("appSalesTraining")
public class SalesTrainingController {

    @Resource(name="salesTrainingService")
    private SalesTrainingService salesTrainingService;

    /**
     * 获取培训文章类型列表
     * @param param
     * @return
     */
    @RequestMapping("/queryTrainingType")
    @ResponseBody
    public ReplyData queryTrainingTypeForApp(@RequestBody AppQueryTrainingTypesParam param) {
        return salesTrainingService.queryTrainingTypeForApp(param);
    }

    /**
     * 查询销售培训文章
     * @param param
     * @return
     */
    @RequestMapping("/queryTrainingArticle")
    @ResponseBody
    public ReplyData queryTrainingArticleForApp(@RequestBody AppQueryTrainingArticlesParam param){
        return salesTrainingService.queryTrainingArticleForApp(param);
    }

    /**
     * 查询销售培训文章和试卷
     * @param param
     * @return
     */
    @RequestMapping("/queryTrainingArticleAndTest")
    @ResponseBody
    public ReplyData queryTrainingArticleAndTestForApp(@RequestBody AppQueryTrainingArticlesParam param){
        return salesTrainingService.queryTrainingArticleAndTestForApp(param);
    }

    /**
     * 获取培训文章详细信息
     * @param param
     * @return
     */
    @RequestMapping("/fetchTrainingArticle")
    @ResponseBody
    public ReplyData fetchTrainingArticleForApp(@RequestBody AppFetchTrainingArticleParam param){
        return salesTrainingService.fetchTrainingArticleForApp(param);
    }

    /**
     * 查询试卷/问卷
     * @param param
     * @return
     */
    @RequestMapping("/queryTestPaper")
    @ResponseBody
    public ReplyData queryTrainingTestForApp(@RequestBody AppQueryTrainingTestParam param){
        return salesTrainingService.queryTrainingTestForApp(param);
    }

    /**
     * 获取试卷/问卷详情
     * @param param
     * @return
     */
    @RequestMapping("/fetchTestPaper")
    @ResponseBody
    public ReplyData fetchTrainingTestForApp(@RequestBody AppFetchTrainingTestParam param){
        return salesTrainingService.fetchTrainingTestForApp(param);
    }

    /**
     * 获取调查问卷统计信息
     * @param param
     * @return
     */
    @RequestMapping("/fetchTrainingTestStatistics")
    @ResponseBody
    public ReplyData fetchTrainingTestStatistics(@RequestBody AppFetchTrainingTestParam param){
        return salesTrainingService.fetchTrainingTestStatistics(param);
    }

    /**
     * 获取试卷/问卷sales的答案
     * @param param
     * @return
     */
    @RequestMapping("/fetchTestPaperAnswer")
    @ResponseBody
    public ReplyData fetchTrainingSalesAnswersForApp(@RequestBody AppFetchTrainingAnswerParam param){
        return salesTrainingService.fetchTrainingSalesAnswersForApp(param);
    }

    /**
     * 获取sales的一个月的销售目标
     * @param param
     * @return
     */
    @RequestMapping("/fetchSalesMonthGoal")
    @ResponseBody
    public ReplyData fetchSalesMonthGoal(@RequestBody AppGetSalesMonthGoalParam param){
        return salesTrainingService.getSalesMonthGoal(param);
    }

    /**
     * 提交试卷, 提交sales答案
     * 只提交回答的试题，多选题答案"A,B,C"形式提交 A,B,C 对应答案 1,2,3
     * @param answer
     * @return 保存后的答案
     */
    @RequestMapping("/submitTestPaper")
    @ResponseBody
    public Object submitTestPaper(@RequestBody AppCommitAnswerParam answer) {
        try{
            return salesTrainingService.commitTestAnswer(answer);
        }catch (Exception e){
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取test name
     * @return testname
     */
    @RequestMapping("/getTestName")
    @ResponseBody
    public Object getTestName() {
        try{
            String testName = salesTrainingService.getTestName();
            ReplyData rd = new ReplyData();
            rd.setData(testName);
            return rd;
        }catch (Exception e){
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取调查问卷统计信息
     * @param param
     * @return
     */
    @RequestMapping("/fetchTrainingTestOverview")
    @ResponseBody
    public ReplyData fetchTrainingTestOverview(@RequestBody AppFetchTrainingTestParam param){
        return salesTrainingService.fetchTrainingTestOverview(param);
    }

    /**
     * 添加助理人目标
     * @param param
     * @return
     */
    @RequestMapping("/addSalesPersonalGoal")
    @ResponseBody
    public ReplyData addSalesPersonalGoal(@RequestBody SalesPersonalGoal param){
        return salesTrainingService.addSalesPersonalGoal(param);
    }

    /**
     * 更新助理人目标
     * @param param
     * @return
     */
    @RequestMapping("/updateSalesPersonalGoal")
    @ResponseBody
    public ReplyData updateSalesPersonalGoal(@RequestBody SalesPersonalGoal param){
        return salesTrainingService.updateSalesPersonalGoal(param);
    }

    /**
     * 删除助理人目标
     * @param param
     * @return
     */
    @RequestMapping("/deleteSalesPersonalGoal")
    @ResponseBody
    public ReplyData deleteSalesPersonalGoal(@RequestBody SalesPersonalGoal param){
        return salesTrainingService.deleteSalesPersonalGoal(param);
    }

    /**
     * 查询助理人目标
     * @param param
     * @return
     */
    @RequestMapping("/querySalesPersonalGoal")
    @ResponseBody
    public ReplyData querySalesPersonalGoal(@RequestBody SalesPersonalGoal param){
        return salesTrainingService.querySalesPersonalGoal(param);
    }

}
