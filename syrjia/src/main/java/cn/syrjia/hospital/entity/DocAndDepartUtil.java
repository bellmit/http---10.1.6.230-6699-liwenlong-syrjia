package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong 
 * 科室医生关联中间表
 */
@Table(name = "t_doc_depart_util")
public class DocAndDepartUtil implements Serializable {
	
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
	 * 部门id
	 */
	@Column
	private String departId;

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

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}
}
