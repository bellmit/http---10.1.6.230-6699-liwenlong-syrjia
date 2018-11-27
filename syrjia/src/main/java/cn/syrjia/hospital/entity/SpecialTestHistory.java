package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_special_test_history")
public class SpecialTestHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id(increment=false)
	private String id;
	
	/**
	 * testName
	 */
	private String testName;
	
	/**
	 * 标记
	 */
	private String remark;
	
	/**
	 * 另一个名称
	 */
	private String otherName;
	
	/**
	 * 状态
	 */
	private Integer state;
	
	/**
	 * 类型
	 */
	private Integer type;
	
	/**
	 * 用户id
	 */
	private String memberId;
	
	/**
	 * 患者id
	 */
	private String patientId;
	
	/**
	 * 订单id
	 */
	private String orderNo;
	
	/**
	 * 舌头
	 */
	private Integer isTongue;
	
	/**
	 * 表层
	 */
	private Integer isSurface;
	
	/**
	 * 另一个
	 */
	private Integer isOther;
	
	/**
	 * 创建时间
	 */
	private Integer createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getIsTongue() {
		return isTongue;
	}
	public void setIsTongue(Integer isTongue) {
		this.isTongue = isTongue;
	}
	public Integer getIsSurface() {
		return isSurface;
	}
	public void setIsSurface(Integer isSurface) {
		this.isSurface = isSurface;
	}
	public Integer getIsOther() {
		return isOther;
	}
	public void setIsOther(Integer isOther) {
		this.isOther = isOther;
	}
}
