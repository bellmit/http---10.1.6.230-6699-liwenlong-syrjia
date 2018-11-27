package cn.syrjia.sales.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

import java.io.Serializable;

@Table(name = "t_sales_test_answer_detail")
public class SalesTestAnswerDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	private Long id;

	/**
	 * testAnswerID
	 */
	@Column
	private Long testAnswerID;
	
	/**
	 * 问题id
	 */
	@Column
	private Long questionID;
	
	/**
	 * 答案
	 */
	@Column
	private String answer;
	
	/**
	 * 分数
	 */
	@Column
	private Integer score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestAnswerID() {
		return testAnswerID;
	}

	public void setTestAnswerID(Long testAnswerID) {
		this.testAnswerID = testAnswerID;
	}

	public Long getQuestionID() {
		return questionID;
	}

	public void setQuestionID(Long questionID) {
		this.questionID = questionID;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
