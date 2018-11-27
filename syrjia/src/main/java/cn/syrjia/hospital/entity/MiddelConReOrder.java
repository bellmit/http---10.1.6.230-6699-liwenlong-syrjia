package cn.syrjia.hospital.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author
 * 调理纪录和调理方关联表
 */
@Table(name="h_middel_con_order")
public class MiddelConReOrder {

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
	 * 调理方id
	 */
	@Column
	private String conditionId;

	/**
	 * recordId
	 */
	@Column
	private Integer recordId;
	
	

	public MiddelConReOrder() {
		super();
	}

	public MiddelConReOrder(String conditionId) {
		super();
		this.conditionId = conditionId;
	}

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

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	
}
