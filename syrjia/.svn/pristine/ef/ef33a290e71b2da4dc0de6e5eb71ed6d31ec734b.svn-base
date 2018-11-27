package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 退款审核流程记录表
 * @author lwl
 *
 */
@Table(name="t_refund_audit_record")
public class RefundAuditRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 记录id
	 */
	@Column
	private Integer recordId;

	/**
	 * 订单号
	 */
	@Column
	private String orderNo;

	/**
	 * 父id
	 */
	@Column
	private Integer pid;

	/**
	 * 摘要
	 */
	@Column
	private String remarks;

	/**
	 * 审计时间
	 */
	@Column
	private Integer auditTime;

	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	/**
	 * 审计用户的id
	 */
	@Column
	private String auditUserId;

	/**
	 * 流id
	 */
	@Column
	private Integer streamId;
	
	@Column
	private Integer state;//1-未处理 2-已同意 3-已驳回

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Integer auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public Integer getStreamId() {
		return streamId;
	}

	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}
