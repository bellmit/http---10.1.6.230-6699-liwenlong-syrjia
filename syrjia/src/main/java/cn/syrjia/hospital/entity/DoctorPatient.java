package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="")
public class DoctorPatient implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * id
	 */
	@Id
	private Integer id;
	
	/**
	 * 医生id
	 */
	@Column
	private String doctorId;
	
	/**
	 * 患者id
	 */
	@Column
	private String patientId;
	
	/**
	 * 用户id
	 */
	@Column
	private String memberId;
	
	/**
	 * 拉黑
	 */
	@Column
	private Integer isBlack;
	
	/**
	 * 记录
	 */
	@Column
	private String remarks;
	
	/**
	 * 是否关注
	 */
	@Column
	private Integer isFollow;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getIsBlack() {
		return isBlack;
	}

	public void setIsBlack(Integer isBlack) {
		this.isBlack = isBlack;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}
}
