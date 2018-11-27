package cn.syrjia.sales.service;

import cn.syrjia.common.BaseServiceInterface;
import cn.syrjia.sales.entity.SalesPersonalGoal;
import cn.syrjia.sales.param.*;

import java.util.Map;

public interface SalesTrainingService extends BaseServiceInterface {

	/**
	 * 查询培训类型
	 * @param param
	 * @return
	 */
    ReplyData queryTrainingTypeForApp(AppQueryTrainingTypesParam param);

    /**
     * 查询培训文章
     * @param param
     * @return
     */
    ReplyData queryTrainingArticleForApp(AppQueryTrainingArticlesParam param);

    /**
     * 查询培训文章和练习
     * @param param
     * @return
     */
    ReplyData queryTrainingArticleAndTestForApp(AppQueryTrainingArticlesParam param);

    /**
     * 获取培训文章
     * @param param
     * @return
     */
    ReplyData fetchTrainingArticleForApp(AppFetchTrainingArticleParam param);

    /**
     * 查询培训练习
     * @param param
     * @return
     */
    ReplyData queryTrainingTestForApp(AppQueryTrainingTestParam param);

    /**
     * 获取培训练习
     * @param param
     * @return
     */
    ReplyData fetchTrainingTestForApp(AppFetchTrainingTestParam param);

    /**
     * 获取培训练习统计
     * @param param
     * @return
     */
    ReplyData fetchTrainingTestStatistics(AppFetchTrainingTestParam param);

    /**
     * 获取培训练习概况
     * @param param
     * @return
     */
    ReplyData fetchTrainingTestOverview(AppFetchTrainingTestParam param);

    /**
     * 获取培训助理的答案
     * @param param
     * @return
     */
    ReplyData fetchTrainingSalesAnswersForApp(AppFetchTrainingAnswerParam param);

    /**
     * 获取助理的月统计
     * @param param
     * @return
     */
    ReplyData getSalesMonthGoal(AppGetSalesMonthGoalParam param);

    /**
     * 提交练习答案
     * @param entity
     * @return
     * @throws Exception
     */
    ReplyData commitTestAnswer(AppCommitAnswerParam entity) throws Exception;

    /**
     * 获取练习的名称
     * @return
     */
    String getTestName();

    /**
     * 添加助理人统计
     * @param goal
     * @return
     */
    ReplyData addSalesPersonalGoal(SalesPersonalGoal goal);

    /**
     * 删除助理人统计
     * @param goal
     * @return
     */
    ReplyData deleteSalesPersonalGoal(SalesPersonalGoal goal);

    /**
     * 更新助理人统计
     * @param goal
     * @return
     */
    ReplyData updateSalesPersonalGoal(SalesPersonalGoal goal);

    /**
     * 查询助理人统计
     * @param goal
     * @return
     */
    ReplyData querySalesPersonalGoal(SalesPersonalGoal goal);
}
