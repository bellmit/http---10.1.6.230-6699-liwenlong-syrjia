package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_send_notice_log")
public class SendNoticeLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 * 主键
	 */
	@Id
	private Integer id;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 用户id
	 */
	@Column
	private String memberId;
	
	/**
	 * 支付状态
	 */
	@Column
	private Integer paystatus;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createtime;
	
	/**
	 * 状态
	 */
	@Column
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
