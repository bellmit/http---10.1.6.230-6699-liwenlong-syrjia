package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong 
 * 中间表表
 */
@Table(name = "t_middle_util")
public class MiddleUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@Id
	private Integer id;
	
	/**
	 * 医馆Id
	 */
	@Column
	private String hospitalId;
	
	/**
	 * 科室Id
	 */
	@Column
	private String departId;
	
	/**
	 * 医生Id
	 */
	@Column
	private String doctorId;
	
	/**
	 * 疾病分类Id
	 */
	@Column
	private String illClassId;
	
	/**
	 * 标签Id
	 */
	@Column
	private String labelId;
	
	/**
	 * 身体系统Id
	 */
	@Column
	private String systemId;
	
	/**
	 * 医院Id
	 */
	@Column
	private String infirmaryId;
	
	/**
	 * type 0-医馆和科室关联  1-医生和疾病分类关联   2- 医生和标签关联  4-医生和身体系统关联
	 */
	@Column
	private String type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getIllClassId() {
		return illClassId;
	}

	public void setIllClassId(String illClassId) {
		this.illClassId = illClassId;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getInfirmaryId() {
		return infirmaryId;
	}

	public void setInfirmaryId(String infirmaryId) {
		this.infirmaryId = infirmaryId;
	}
	

}
