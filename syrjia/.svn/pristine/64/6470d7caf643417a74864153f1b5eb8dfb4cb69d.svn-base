package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_refundapply_record")
public class RefundApplyRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
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
	private String userid;
	
	/**
	 * 钱
	 */
	@Column
	private Double money;
	
	/**
	 * 状态
	 */
	@Column
	private Integer status;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createtime;
	
	/**
	 * 删除标志
	 */
	@Column
	private Integer deleteflag;

	/**
	 * 申请原因
	 */
	@Column
	private String applyReason;
	
	/**
	 * 退款原因
	 */
	@Column
	private String refundReason;

	/**
	 * 详情id
	 */
	@Column
	private String detailId;
	
	@Column
	private Integer applyWay; //0-后台申请 1-移动端申请

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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public Integer getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public Integer getApplyWay() {
		return applyWay;
	}

	public void setApplyWay(Integer applyWay) {
		this.applyWay = applyWay;
	}

}
