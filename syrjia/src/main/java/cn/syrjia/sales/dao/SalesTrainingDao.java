package cn.syrjia.sales.dao;

import cn.syrjia.common.BaseDaoInterface;
import cn.syrjia.sales.entity.SalesPersonalGoal;
import cn.syrjia.sales.entity.SalesTestAnswer;
import cn.syrjia.sales.param.*;

import java.util.List;
import java.util.Map;

public interface SalesTrainingDao extends BaseDaoInterface {

    /**
     * 查询培训类型
     *
     * @param param
     * @return
     */
    ReplyData queryTrainingTypeForApp(AppQueryTrainingTypesParam param);

    /**
     * 查询培训文章
     *
     * @param param
     * @return
     */
    ReplyData queryTrainingArticleForApp(AppQueryTrainingArticlesParam param);

    /**
     * 查询培训文章和练习
     *
     * @param param
     * @return
     */
    ReplyData queryTrainingArticleAndTestForApp(AppQueryTrainingArticlesParam param);

    /**
     * 获取培训文章
     *
     * @param param
     * @return
     */
    Map<String, Object> fetchTrainingArticleForApp(AppFetchTrainingArticleParam param);

    /**
     * 设置读取培训文章
     *
     * @param param
     * @return
     */
    Integer setReadTrainingArticleForApp(AppFetchTrainingArticleParam param);

    /**
     * 查询培训练习
     *
     * @param param
     * @return
     */
    ReplyData queryTrainingTestForApp(AppQueryTrainingTestParam param);

    /**
     * 获取培训练习
     *
     * @param param
     * @return
     */
    Map<String, Object> fetchTrainingTestForApp(AppFetchTrainingTestParam param);

    /**
     * 获取培训练习统计
     *
     * @param param
     * @return
     */
    Map<String, Object> fetchTrainingTestStatistics(AppFetchTrainingTestParam param);

    /**
     * 获取培训问题
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> fetchTrainingQuestionsForApp(AppFetchTrainingTestParam param);

    /**
     * 获取培训问题答案
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> fetchTrainingQuestionAnswersForApp(AppFetchTrainingTestParam param);

    /**
     * 获取培训助理答案
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> fetchTrainingSalesAnswersForApp(AppFetchTrainingAnswerParam param);

    /**
     * 获取助理月统计
     *
     * @param param
     * @return
     */
    Map<String, Object> getSalesMonthGoal(AppGetSalesMonthGoalParam param);

    /**
     * 提交练习答案
     *
     * @param entity
     * @return
     */
    Integer commitTestAnswer(SalesTestAnswer entity);

    /**
     * 删除练习答案详情
     *
     * @param entity
     * @return
     */
    Integer deleteTestAnswerDetails(SalesTestAnswer entity);

    /**
     * 获取练习名称
     *
     * @return
     */
    String getTestName();

    /**
     * 查询助理人统计
     *
     * @param goal
     * @return
     */
    List<Map<String, Object>> querySalesPersonalGoal(SalesPersonalGoal goal);

    /**
     * 检查助理人统计
     *
     * @param goal
     * @return
     */
    Integer checkSalesPersonalGoal(SalesPersonalGoal goal);

    /**
     * 更新助理人统计
     *
     * @param goal
     * @return
     */
    Integer UpdateSalesPersonalGoal(SalesPersonalGoal goal);

    /**
     * 获取答对题目数量
     *
     * @param srId   助理ID
     * @param testId 试卷ID
     * @return 答对题目数量
     */
    Integer getRightCount(String srId, Long testId);

}
