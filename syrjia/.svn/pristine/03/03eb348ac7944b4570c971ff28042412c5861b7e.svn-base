package cn.syrjia.sales.dao.impl;

import cn.syrjia.common.impl.BaseDaoImpl;
import cn.syrjia.sales.dao.SalesTrainingDao;
import cn.syrjia.sales.entity.SalesPersonalGoal;
import cn.syrjia.sales.entity.SalesTestAnswer;
import cn.syrjia.sales.param.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("salesTrainingDao")
public class SalesTrainingDaoImpl extends BaseDaoImpl implements SalesTrainingDao {

    private Logger logger = LogManager.getLogger(SalesTrainingDaoImpl.class);

    /**
     * 查询页数
     *
     * @param sql
     * @param sqlCount
     * @param param
     * @param sqlParam
     * @param countParam
     * @return
     */
    private ReplyData queryPagination(String sql, String sqlCount, BasePaginationParam param, Object[] sqlParam, Object[] countParam) {
        ReplyData pd = new ReplyData(param);
        List list;
        //自定义sql 查询数量
        Integer count = queryBysqlCount(sqlCount, countParam);
        if (count != 0) {
            //自定义sql 查询列表List>
            list = queryBysqlList(sql + param.pageSql(), sqlParam);
        } else {
            list = new ArrayList<>();
        }
        pd.setData(list);
        pd.getPagination().setTotal(count);
        return pd;
    }

    /**
     * 查询培训类型
     */
    @Override
    public ReplyData queryTrainingTypeForApp(AppQueryTrainingTypesParam param) {
        String sql = "select t.id, t.name, count(tsma.id) as articleCount, count(if(tsma.isRead=1,1,null)) as readCount " +
                "from v_sales_training_type t " +
                "left join t_sales_training_article tsta on (t.id = tsta.typeID and tsta.state = 1) " +
                "left join t_sales_map_article tsma on (tsta.id = tsma.articleID and tsma.salesID = ?) " +
                "where t.state = 1 and t.name!='知识问卷' and t.deleted=0 group by t.id,t.name order by t.priority desc";
        String sqlCount = "SELECT count(*) FROM v_sales_training_type t WHERE t.state = 1";

        return queryPagination(sql, sqlCount, param, new Object[]{param.getSrId()}, null);
    }

    /**
     * 查询培训文章
     */
    @Override
    public ReplyData queryTrainingArticleForApp(AppQueryTrainingArticlesParam param) {

        List<Object> list = new ArrayList<>();
        StringBuilder b = new StringBuilder();
        list.add(param.getSrId());
        if (param.getTypeID() != null) {
            b.append(" and t1.typeID = ?");
            list.add(param.getTypeID());
        }
        if (StringUtils.isNotBlank(param.getTitle())) {
            b.append(" and t1.title like ?");
            list.add("%" + param.getTitle() + "%");
        }
        String addSql = b.toString();

        String sql = "SELECT t1.id, t1.articleID, t1.title, DATE_FORMAT(t1.publishTime,\"%Y-%m-%d %T\") as publishTime, t2.isRead " +
                "FROM t_sales_training_article t1 " +
                "INNER JOIN t_sales_map_article t2 ON (t1.id = t2.articleID) " +
                "where t2.salesID = ? and t1.state = 1" + addSql +
                " order by t2.isRead, t1.publishTime";
        String sqlCount = "SELECT count(*) FROM t_sales_training_article t1 " +
                "INNER JOIN t_sales_map_article t2 ON (t1.id = t2.articleID) " +
                "where t2.salesID = ? and t1.state = 1" + addSql;

        return queryPagination(sql, sqlCount, param, list.toArray(), list.toArray());
    }

    /**
     * 查询培训文章和练习
     */
    @Override
    public ReplyData queryTrainingArticleAndTestForApp(AppQueryTrainingArticlesParam param) {

        List<Object> list = new ArrayList<>();
        StringBuilder b = new StringBuilder();
        list.add(param.getSrId());
        list.add(param.getSrId());
        if (StringUtils.isNotBlank(param.getTitle())) {
            b.append(" and tt.title like ?");
            list.add("%" + param.getTitle() + "%");
        }
        String addSql = b.toString();

        String sql = "select * from ( " +
                "SELECT 1 as type, t1.id, t1.articleID as `No`, t1.title, DATE_FORMAT(t1.publishTime,\"%Y-%m-%d %T\") as publishTime, " +
                "null as beginTime, null as endTime, t2.isRead as state " +
                "FROM t_sales_training_article t1 " +
                "INNER JOIN t_sales_map_article t2 ON (t1.id = t2.articleID) " +
                "where t2.salesID = ? and t1.state = 1 " +
                "union all " +
                "SELECT t.type, t.id, t.testID  as `No`, t.name as title, DATE_FORMAT(t.publishTime,\"%Y-%m-%d %T\") as publishTime, " +
                "DATE_FORMAT(t.beginTime,\"%Y-%m-%d %T\") as beginTime, DATE_FORMAT(t.endTime,\"%Y-%m-%d %T\") as endTime, t1.state " +
                "FROM t_sales_test t " +
                "INNER JOIN t_sales_test_answer t1 ON (t.id = t1.testID) WHERE t1.sales = ? and t.state = 1 " +
                ") tt where 1 = 1 " + addSql + "order by tt.publishTime";

        String sqlCount = "select count(*) from ( " +
                "SELECT 1 as type, t1.id, t1.articleID as `No`, t1.title, DATE_FORMAT(t1.publishTime,\"%Y-%m-%d %T\") as publishTime, t2.isRead as state " +
                "FROM t_sales_training_article t1 " +
                "INNER JOIN t_sales_map_article t2 ON (t1.id = t2.articleID) " +
                "where t2.salesID = ? and t1.state = 1 " +
                "union all " +
                "SELECT t.type, t.id, t.testID  as `No`, t.name as title, DATE_FORMAT(t.publishTime,\"%Y-%m-%d %T\") as publishTime, t1.state " +
                "FROM t_sales_test t " +
                "INNER JOIN t_sales_test_answer t1 ON (t.id = t1.testID) WHERE t1.sales = ? and t.state = 1 " +
                ") tt where 1 = 1 " + addSql;

        return queryPagination(sql, sqlCount, param, list.toArray(), list.toArray());
    }

    /**
     * 获取培训文章
     */
    @Override
    public Map<String, Object> fetchTrainingArticleForApp(AppFetchTrainingArticleParam param) {
        String sql = "select t1.id, t1.title, DATE_FORMAT(t1.publishTime,\"%Y-%m-%d %T\") as publishTime, t1.content, t1.sharable " +
                "from t_sales_training_article t1 " +
                "INNER JOIN t_sales_map_article t2 ON (t1.id = t2.articleID) " +
                "where t1.state = 1 and t2.salesID = ? and t1.id = ?";

        return queryBysqlMap(sql, new Object[]{param.getSrId(), param.getArticleID()});
    }

    /**
     * 设置读取培训文章
     */
    @Override
    public Integer setReadTrainingArticleForApp(AppFetchTrainingArticleParam param) {
        String sql = "update t_sales_map_article set isRead = 1 where salesID = ? and articleID = ?";
        return update(sql, new Object[]{param.getSrId(), param.getArticleID()});
    }

    /**
     * 查询培训练习
     */
    @Override
    public ReplyData queryTrainingTestForApp(AppQueryTrainingTestParam param) {

        List<Object> list = new ArrayList<>();
        StringBuilder b = new StringBuilder();
        list.add(param.getSrId());
        if (param.getType() != null) {
            b.append(" and t.type = ?");
            list.add(param.getType());
        }
        String addSql = b.toString();

        String sql = "SELECT t.id, t.type, t.testID, t.name, t.duration, " +
                "DATE_FORMAT(t.publishTime,\"%Y-%m-%d %T\") as publishTime, " +
                "DATE_FORMAT(t.beginTime,\"%Y-%m-%d %T\") as beginTime, " +
                "DATE_FORMAT(t.endTime,\"%Y-%m-%d %T\") as endTime, t1.state " +
                "FROM t_sales_test t " +
                "INNER JOIN t_sales_test_answer t1 ON (t.id = t1.testID) WHERE t1.sales = ?" + addSql +
                " and deleted=0 order by t.publishTime";

        String sqlCount = "SELECT count(*) FROM t_sales_test t " +
                "INNER JOIN t_sales_test_answer t1 ON (t.id = t1.testID) WHERE t1.sales = ?" + addSql;

        return queryPagination(sql, sqlCount, param, list.toArray(), list.toArray());
    }

    /**
     * 获取培训练习
     */
    @Override
    public Map<String, Object> fetchTrainingTestForApp(AppFetchTrainingTestParam param) {
        String sql = "SELECT t.id, t.type, t1.id as testAnswerID, t.testID, t.name, t.description, t.duration, " +
                "DATE_FORMAT(t.publishTime,\"%Y-%m-%d %T\") as publishTime, " +
                "DATE_FORMAT(t.beginTime,\"%Y-%m-%d %T\") as beginTime, " +
                "DATE_FORMAT(t.endTime,\"%Y-%m-%d %T\") as endTime, t1.state, " +
                "DATE_FORMAT(t1.beginTime,\"%Y-%m-%d %T\") as salesBeginTime, " +
                "DATE_FORMAT(t1.endTime,\"%Y-%m-%d %T\") as salesEndTime, t1.score " +
                "FROM t_sales_test t INNER JOIN t_sales_test_answer t1 ON (t.id = t1.testID) where t1.sales = ? and t1.testID = ?";

        return queryBysqlMap(sql, new Object[]{param.getSrId(), param.getTestID()});
    }

    /**
     * 获取培训练习统计
     */
    @Override
    public Map<String, Object> fetchTrainingTestStatistics(AppFetchTrainingTestParam param) {
        String sql = "select count(*) as total, (count(*) - count(if(t1.state=1,1,null))) as wdTotal " +
                "from t_sales_test t " +
                "LEFT JOIN t_sales_test_answer t1 on (t.id = t1.testID and t1.sales = ?)" +
                "where t.published = 1 and t.state = 1";

        return queryBysqlMap(sql, new Object[]{param.getSrId()});
    }

    /**
     * 获取培训问题
     */
    @Override
    public List<Map<String, Object>> fetchTrainingQuestionsForApp(AppFetchTrainingTestParam param) {
        String sql = "SELECT id, testID, type, question, score, orderNum, required, jump, tip " +
                "FROM t_sales_test_question WHERE testID = ? order by orderNum";
        return queryBysqlList(sql, new Object[]{param.getTestID()});
    }

    /**
     * 获取培训问题答案
     */
    @Override
    public List<Map<String, Object>> fetchTrainingQuestionAnswersForApp(AppFetchTrainingTestParam param) {
        String sql = "SELECT t.id, t.questionID, t.content, t.orderNum, t.isRight " +
                "FROM t_sales_test_question_answer t " +
                "INNER JOIN t_sales_test_question t1 ON (t.questionID = t1.id) " +
                "WHERE t1.testID = ? order by t1.orderNum, t.orderNum";
        return queryBysqlList(sql, new Object[]{param.getTestID()});
    }

    /**
     * 获取培训助理答案
     */
    @Override
    public List<Map<String, Object>> fetchTrainingSalesAnswersForApp(AppFetchTrainingAnswerParam param) {
        String sql = "SELECT t.id, t.testAnswerID, t.questionID, t.answer, t.score " +
                "FROM t_sales_test_answer_detail t " +
                "INNER JOIN t_sales_test_answer t1 on (t.testAnswerID = t1.id) " +
                "WHERE t1.sales = ? and t1.testID = ?";
        return queryBysqlList(sql, new Object[]{param.getSrId(), param.getTestID()});
    }

    /**
     * 获取助理月统计
     */
    @Override
    public Map<String, Object> getSalesMonthGoal(AppGetSalesMonthGoalParam param) {
        String sql = "select id, channel, month, doctorOnline, " +
                "prescriptionSum, prescriptionQuantity, graphicConsultationSum, graphicConsultationQuantity, " +
                "phoneConsultationSum, phoneConsultationQuantity, graphicRecuperateSum, graphicRecuperateQuantity, " +
                "phoneRecuperateSum, phoneRecuperateQuantity, commoditySum, commodityQuantity " +
                "from t_sales_goal where channel = ? and month = ?";
        return this.queryBysqlMap(sql, new Object[]{param.getSrId(), param.getMonth()});
    }

    /**
     * 提交练习答案
     */
    @Override
    public Integer commitTestAnswer(SalesTestAnswer entity) {
        Integer i = null;
        String sql = "update t_sales_test_answer set state = ?, beginTime = ?, endTime = ? where sales = ? and testID = ?";
        try {
            if (entity.getId() != null)
                i = super.update(sql, new Object[]{entity.getState(), entity.getBeginTime(), entity.getEndTime(), entity.getSales(), entity.getTestID()});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 删除练习答案详情
     */
    @Override
    public Integer deleteTestAnswerDetails(SalesTestAnswer entity) {
        Integer i = null;
        String sql = "delete t from t_sales_test_answer_detail t inner join t_sales_test_answer t1 on (t.testAnswerID = t1.id) where t1.sales = ? and t1.testID = ?";
        try {
            if (entity.getId() != null)
                i = super.update(sql, new Object[]{entity.getSales(), entity.getTestID()});
        } catch (DataAccessException e) {
            logger.warn(e);
        }
        return i;
    }

    /**
     * 获取练习名称
     */
    @Override
    public String getTestName() {
        String sql = "select `value` from t_system_setting where `key` = 'test_name'";
        try {
            String rtn = jdbcTemplate.queryForObject(sql, null, String.class);
            return rtn;
        } catch (DataAccessException e) {
            throw (e);
        }
    }

    /**
     * 查询助理人统计
     */
    @Override
    public List<Map<String, Object>> querySalesPersonalGoal(SalesPersonalGoal goal) {
        String sql = "select id, srId, month, type, quotaType, quota, beginDate, endDate, updateTime " +
                "from t_sales_training_personal_goal where srId = ? and month = ? order by beginDate, updateTime desc";

        List<Map<String, Object>> rtn = queryBysqlList(sql, new Object[]{goal.getSrId(), goal.getMonth()});
        return rtn;
    }

    /**
     * 检查助理人统计
     */
    @Override
    public Integer checkSalesPersonalGoal(SalesPersonalGoal goal) {
        String sql = "select count(*) from t_sales_training_personal_goal where srId = ? and month = ? and type = ? and quotaType = ? and beginDate = ?";
        return queryBysqlCount(sql, new Object[]{goal.getSrId(), goal.getMonth(), goal.getType(), goal.getQuotaType(), goal.getBeginDate()});
    }

    /**
     * 更新助理人统计
     */
    @Override
    public Integer UpdateSalesPersonalGoal(SalesPersonalGoal goal) {
        String sql = "update t_sales_training_personal_goal set quota = ? where id = ?";
        return update(sql, new Object[]{goal.getQuota(), goal.getId()});
    }

    /**
     * 获取答对题目数量
     *
     * @param srId   助理ID
     * @param testId 试卷ID
     * @return 答对题目数量
     */
    @Override
    public Integer getRightCount(String srId, Long testId) {
        String sql = "SELECT count(*)  as rightCount FROM t_sales_test_answer_detail t INNER JOIN t_sales_test_answer t1 ON (t.testAnswerID=t1.id) WHERE t1.sales=? AND t1.testID=? AND t.score>0";
        return jdbcTemplate.queryForObject(sql, new Object[]{srId, testId}, Long.class).intValue();
    }

}
