package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 疑难答疑
 * 
 * */
@Table(name = "t_answer")
public class Answer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 * 
	 * @Id private String id; /** 标题
	 * 
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 问题
	 */
	private String title;
	/**
	 * 回答
	 * 
	 * @pdOid 9b167e10-d7cd-420d-b66a-9d45b4cdaaaf
	 */
	private String answer;
	
	/**
	 * 答案类型
	 */
	private String answerType;
	
	/**
	 * 状态 默认1 1启用 2禁用 3删除
	 * 
	 * @pdOid e8531fee-d935-40d1-a646-a65501b04d35
	 */
	private Integer state;
	/**
	 * 排序
	 * 
	 * @pdOid f1b7f328-64e4-49bf-aa02-1bde2db3a1b2
	 */
	private Integer rank;
	
	/**
	 * 创建时间
	 */
	private Integer createTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
}