package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 症状描述表
 * @author lwl
 */
@Table(name="t_order_symptom")
public class OrderSymptom implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 症状描述	
	 */
	@Column
	private String symptomDescribe;
	
	/**
	 * 其他描述
	 */
	@Column
	private String otherDescribe;
	
	/**
	 * 脉搏每分钟
	 */
	@Column
	private String pulse;
	
	/**
	 * 状态 0-未填写 1-已填写
	 */
	@Column
	private Integer state;
	/**
	 * 语音访问地址
	 */
	@Column
	private String patientId;
	
	/**
	 *插入时间
	 */
	@Column
	private Integer createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSymptomDescribe() {
		return symptomDescribe;
	}

	public void setSymptomDescribe(String symptomDescribe) {
		this.symptomDescribe = symptomDescribe;
	}

	public String getOtherDescribe() {
		return otherDescribe;
	}

	public void setOtherDescribe(String otherDescribe) {
		this.otherDescribe = otherDescribe;
	}

	public String getPulse() {
		return pulse;
	}

	public void setPulse(String pulse) {
		this.pulse = pulse;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

}
