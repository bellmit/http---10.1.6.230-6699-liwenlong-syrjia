package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_patient_nexus")
public class PatientNexus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 关系名称
	 */
	@Column
	private String nexusName;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getNexusName() {
		return nexusName;
	}

	public void setNexusName(String nexusName) {
		this.nexusName = nexusName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
}
