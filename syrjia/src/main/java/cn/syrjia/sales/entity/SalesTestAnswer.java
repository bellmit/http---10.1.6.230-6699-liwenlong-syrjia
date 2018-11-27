package cn.syrjia.sales.entity;


import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Table(name = "t_sales_test_answer")
public class SalesTestAnswer implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	private Long id;

	/**
	 * testID
	 */
	@Column
	private Long testID;
	
	/**
	 * 渠道码
	 */
	@Column
	private String channelCode;
	
	/**
	 * 助理
	 */
	@Column
	private String sales;
	
	/**
	 * 开始时间
	 */
	@Column
	private Date beginTime;
	
	/**
	 * 结束时间
	 */
	@Column
	private Date endTime;
	
	/**
	 * realScore
	 */
	@Column
	private Integer realScore;
	
	/**
	 * 分数
	 */
	@Column
	private Integer score;
	
	/**
	 * 分数
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建时间
	 */
	@Column
	private Timestamp createTime;

	/**
	 * 答案详情
	 */
	private List<SalesTestAnswerDetail> answerDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestID() {
		return testID;
	}

	public void setTestID(Long testID) {
		this.testID = testID;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
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

	public Integer getRealScore() {
		return realScore;
	}

	public void setRealScore(Integer realScore) {
		this.realScore = realScore;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<SalesTestAnswerDetail> getAnswerDetails() {
		return answerDetails;
	}

	public void setAnswerDetails(List<SalesTestAnswerDetail> answerDetails) {
		this.answerDetails = answerDetails;
	}
}
