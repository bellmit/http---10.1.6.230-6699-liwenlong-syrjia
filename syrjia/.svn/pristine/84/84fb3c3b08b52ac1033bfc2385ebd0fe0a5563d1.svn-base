package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Date;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_score_consume")
public class ScoreConsume implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false)
	private Integer id; 
	
	@Column
	private String userid;//用户id
	
	@Column
	private String orderNo;//订单号
	
	/**
	 * 订单详情ID
	 */
	@Column
	private String orderDetailId;
	
	@Column
	private Double consumeScore;//消费积分
	
	@Column
	private Integer createtime;//创建时间
	
	@Column
	private Double surplusScore;//剩余积分
	
	/**
	 * 开始时间
	 */
	@Date(field="createtime",symbol=">=")
	private Integer startTime;
	
	/**
	 * 结束时间
	 */
	@Date(field="createtime",symbol="<=")
	private Integer endTime;

	
	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public Double getConsumeScore() {
		return consumeScore;
	}

	public void setConsumeScore(Double consumeScore) {
		this.consumeScore = consumeScore;
	}

	public Double getSurplusScore() {
		return surplusScore;
	}

	public void setSurplusScore(Double surplusScore) {
		this.surplusScore = surplusScore;
	}

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
}
