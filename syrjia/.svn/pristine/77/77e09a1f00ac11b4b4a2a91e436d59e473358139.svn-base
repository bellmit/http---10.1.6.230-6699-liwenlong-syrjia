package cn.syrjia.sales.service.impl;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.sales.dao.SalesTrainingDao;
import cn.syrjia.sales.entity.SalesPersonalGoal;
import cn.syrjia.sales.entity.SalesTestAnswer;
import cn.syrjia.sales.entity.SalesTestAnswerDetail;
import cn.syrjia.sales.param.*;
import cn.syrjia.sales.service.SalesTrainingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("salesTrainingService")
public class SalesTrainingServiceImpl extends BaseServiceImpl implements SalesTrainingService {

    @Resource(name = "salesTrainingDao")
    private SalesTrainingDao salesTrainingDao;

    /**
     * 查询培训类型
     */
    @Override
    public ReplyData queryTrainingTypeForApp(AppQueryTrainingTypesParam param) {
        return salesTrainingDao.queryTrainingTypeForApp(param);
    }

    /**
     * 查询培训文章
     */
    @Override
    public ReplyData queryTrainingArticleForApp(AppQueryTrainingArticlesParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required.");
        }
        try {
            //查询培训文章
            return this.salesTrainingDao.queryTrainingArticleForApp(param);
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 查询培训文章和练习
     */
    @Override
    public ReplyData queryTrainingArticleAndTestForApp(AppQueryTrainingArticlesParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required.");
        }
        try {
            //查询培训文章和练习
            return this.salesTrainingDao.queryTrainingArticleAndTestForApp(param);
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取培训文章
     */
    @Override
    public ReplyData fetchTrainingArticleForApp(AppFetchTrainingArticleParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        if (param.getArticleID() == null) {
            return new ReplyData("article id required");
        }

        try {
            //设置读取培训文章
            this.salesTrainingDao.setReadTrainingArticleForApp(param);
            //获取培训文章
            Map<String, Object> article = this.salesTrainingDao.fetchTrainingArticleForApp(param);
            ReplyData rd = new ReplyData();
            rd.setData(article);
            return rd;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 查询培训练习
     */
    @Override
    public ReplyData queryTrainingTestForApp(AppQueryTrainingTestParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        try {
            //查询培训练习
            return salesTrainingDao.queryTrainingTestForApp(param);
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取培训练习
     */
    @Override
    public ReplyData fetchTrainingTestForApp(AppFetchTrainingTestParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        if (param.getTestID() == null) {
            return new ReplyData("test id required");
        }

        try {
            //获取培训练习
            Map<String, Object> rtn = salesTrainingDao.fetchTrainingTestForApp(param);
            if (rtn == null) {
                throw new Exception("test paper not found");
            }
            Integer type = (Integer) rtn.get("type");
            //获取培训问题
            List<Map<String, Object>> questions = salesTrainingDao.fetchTrainingQuestionsForApp(param);
            rtn.put("questions", questions);
            //获取培训问题答案
            List<Map<String, Object>> answers = salesTrainingDao.fetchTrainingQuestionAnswersForApp(param);
            int totalScore = 0;
            for (Map<String, Object> question : questions) {
                Object id = question.get("id");
                if (type == 3) {
                    Integer score = (Integer) question.get("score");
                    totalScore += score;
                }
                List<Map<String, Object>> ans = new ArrayList<>();
                question.put("answers", ans);
                for (Map<String, Object> answer : answers) {
                    if (id.equals(answer.get("questionID"))) {
                        ans.add(answer);
                    }
                }
            }
            if (type == 3) {
                rtn.put("totalScore", totalScore);
            }

            //获取培训助理答案
            rtn.put("rightCount", salesTrainingDao.getRightCount(param.getSrId(),param.getTestID()));

            ReplyData replyData = new ReplyData();
            replyData.setData(rtn);
            return replyData;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取培训练习统计
     */
    @Override
    public ReplyData fetchTrainingTestStatistics(AppFetchTrainingTestParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        try {
            //获取培训练习统计
            Map<String, Object> rtn = salesTrainingDao.fetchTrainingTestStatistics(param);
            ReplyData replyData = new ReplyData();
            replyData.setData(rtn);
            return replyData;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取培训练习概况
     */
    @Override
    public ReplyData fetchTrainingTestOverview(AppFetchTrainingTestParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        try {
            //获取培训练习统计
            Map<String, Object> rtn = salesTrainingDao.fetchTrainingTestStatistics(param);
            //获取练习名称
            String testName = salesTrainingDao.getTestName();
            rtn.put("testLabel", testName);
            ReplyData replyData = new ReplyData();
            replyData.setData(rtn);
            return replyData;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取培训助理的答案
     */
    @Override
    public ReplyData fetchTrainingSalesAnswersForApp(AppFetchTrainingAnswerParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        if (param.getTestID() == null) {
            return new ReplyData("test id required");
        }
        try {
            //获取培训助理答案
            List<Map<String, Object>> salesAnswers = salesTrainingDao.fetchTrainingSalesAnswersForApp(param);
            ReplyData replyData = new ReplyData();
            replyData.setData(salesAnswers);
            return replyData;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 获取助理的月统计
     */
    @Override
    public ReplyData getSalesMonthGoal(AppGetSalesMonthGoalParam param) {
        if (param.getSrId() == null) {
            return new ReplyData("srId required");
        }
        if (param.getMonth() == null || param.getMonth().length() != 6) {
            return new ReplyData("month required, eg: 201801");
        }

        try {
            //获取助理月统计
            Map<String, Object> salesMonthGoal = salesTrainingDao.getSalesMonthGoal(param);
            ReplyData rd = new ReplyData();
            rd.setData(salesMonthGoal);
            return rd;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 提交练习答案
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ReplyData commitTestAnswer(AppCommitAnswerParam entity) throws Exception {

        if (entity.getSrId() == null) {
            return new ReplyData("srId required");
        }
        if (entity.getTestID() == null) {
            return new ReplyData("testID required");
        }
        if (entity.getBeginTime() == null) {
            return new ReplyData("beginTime required");
        }
        if (entity.getEndTime() == null) {
            return new ReplyData("endTime required");
        }
        if (entity.getTestAnswerID() == null) {
            return new ReplyData("testAnswerID required");
        }
        if (CollectionUtils.isEmpty(entity.getAnswers())) {
            return new ReplyData("answers required");
        }
        Long id = entity.getTestAnswerID();

        //重复提交校验
        SalesTestAnswer oldAnswer = salesTrainingDao.queryById(SalesTestAnswer.class, id, false);
        if (oldAnswer == null) {
            throw new Exception("test paper not found");
        }
        if (oldAnswer.getState() != 0) {
            throw new Exception("Do not allow duplicate submissions");
        }

        SalesTestAnswer answer = new SalesTestAnswer();
        answer.setId(id);
        answer.setSales(entity.getSrId());
        answer.setTestID(entity.getTestID());
        answer.setState(1);
        answer.setBeginTime(entity.getBeginTime());
        answer.setEndTime(entity.getEndTime());
        //提交练习答案
        Integer i = salesTrainingDao.commitTestAnswer(answer);
        if (i != 1) {
            throw new Exception("test paper not found");
        }
        //删除练习答案详情
        salesTrainingDao.deleteTestAnswerDetails(answer);

        List<SalesTestAnswerDetail> details = entity.getAnswers();
        if (!CollectionUtils.isEmpty(details)) {
            for (SalesTestAnswerDetail detail : details) {
                detail.setTestAnswerID(id);
            }
            //批量增加
            salesTrainingDao.addEntity(details);
        }
        return new ReplyData();
    }

    /**
     * 获取练习的名称
     */
    @Override
    public String getTestName() {
        return salesTrainingDao.getTestName();
    }

    /**
     * 添加助理人统计
     */
    @Override
    public ReplyData addSalesPersonalGoal(SalesPersonalGoal goal) {
        if (goal.getSrId() == null) {
            return new ReplyData("srId required.");
        }
        if (goal.getMonth() == null) {
            return new ReplyData("month required.");
        }
        if (goal.getType() == null) {
            return new ReplyData("type required.");
        }
        if (goal.getType() != 1 && goal.getType() != 2) {
            return new ReplyData("type limit in 1 to 2.");
        }
        if (goal.getQuotaType() == null) {
            return new ReplyData("quotaType required.");
        }
        if (goal.getQuotaType() < 1 || goal.getQuotaType() > 4) {
            return new ReplyData("quotaType limit in 1 to 4.");
        }
        if (goal.getQuota() == null) {
            return new ReplyData("quotaType required.");
        }

        if (StringUtils.isBlank(goal.getBeginDate())) {
            return new ReplyData("beginDate required.");
        }

        if (goal.getType() == 2 && StringUtils.isBlank(goal.getEndDate())) {
            return new ReplyData("endDate required.");
        }

        if (goal.getType() == 1) {
            goal.setEndDate(null);
        }

        try {
            //检查助理人统计
            Integer i = salesTrainingDao.checkSalesPersonalGoal(goal);
            if (i > 0) {
                return new ReplyData("Repeated data.");
            }
            //增加
            Long id = (Long) salesTrainingDao.addEntity(goal);
            //根据id查询单个
            SalesPersonalGoal savedEntity = salesTrainingDao.queryById(SalesPersonalGoal.class, id, null);
            ReplyData rd = new ReplyData();
            rd.setData(savedEntity);
            return rd;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }

    }

    /**
     * 删除助理人统计
     */
    @Override
    public ReplyData deleteSalesPersonalGoal(SalesPersonalGoal goal) {
        if (goal.getId() == null) {
            return new ReplyData("id required");
        }
        try {
            //删除 根据Id
            Integer integer = salesTrainingDao.deleteEntity(SalesPersonalGoal.class, goal.getId());
            if (integer == 0) {
                return new ReplyData("can not found sales personal goal with id: " + goal.getId());
            } else {
                return new ReplyData();
            }
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 更新助理人统计
     */
    @Override
    public ReplyData updateSalesPersonalGoal(SalesPersonalGoal goal) {
        if (goal.getId() == null) {
            return new ReplyData("id required");
        }
        if (goal.getQuota() == null) {
            return new ReplyData("quotaType required.");
        }

        try {
            //更新助理人统计
            Integer i = salesTrainingDao.UpdateSalesPersonalGoal(goal);
            if (i == 0) {
                return new ReplyData("Can not found the sales goal with id:" + goal.getId());
            }
            //根据id查询单个
            SalesPersonalGoal savedEntity = salesTrainingDao.queryById(SalesPersonalGoal.class, goal.getId(), null);
            ReplyData rd = new ReplyData();
            rd.setData(savedEntity);
            return rd;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }

    /**
     * 查询助理人统计
     */
    @Override
    public ReplyData querySalesPersonalGoal(SalesPersonalGoal goal) {
        if (goal.getSrId() == null) {
            return new ReplyData("srId required.");
        }
        if (goal.getMonth() == null) {
            return new ReplyData("month required.");
        }
        try {
            //查询助理人统计
            List<Map<String, Object>> data = salesTrainingDao.querySalesPersonalGoal(goal);
            ReplyData rd = new ReplyData();
            rd.setData(data);
            return rd;
        } catch (Exception e) {
            return new ReplyData(e.getMessage());
        }
    }
}
