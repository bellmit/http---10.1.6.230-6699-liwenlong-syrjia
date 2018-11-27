package cn.syrjia.sales.param;

import cn.syrjia.sales.entity.SalesTestAnswerDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class AppCommitAnswerParam extends BaseParam {

    private static final long serialVersionUID = 1L;

    public Long testID;
    public Long testAnswerID;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date endTime;

    public List<SalesTestAnswerDetail> answers;

    public Long getTestID() {
        return testID;
    }

    public void setTestID(Long testID) {
        this.testID = testID;
    }

    public Long getTestAnswerID() {
        return testAnswerID;
    }

    public void setTestAnswerID(Long testAnswerID) {
        this.testAnswerID = testAnswerID;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<SalesTestAnswerDetail> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SalesTestAnswerDetail> answers) {
        this.answers = answers;
    }
}
