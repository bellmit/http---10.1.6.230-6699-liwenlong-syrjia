package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_bind_bank")
public class BindBank implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 *  id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 银行id
	 */
	@Column
	private String bankId;
	
	/**
	 * 银行名称
	 */
	@Column
	private String bankName;
	
	/**
	 * 银行码
	 */
	@Column
	private String bankCode;
	
	/**
	 * 银行时间
	 */
	@Column
	private Integer bankTime;
	
	/**
	 * 医生id
	 */
	@Column
	private String doctorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getBankTime() {
		return bankTime;
	}

	public void setBankTime(Integer bankTime) {
		this.bankTime = bankTime;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
}
