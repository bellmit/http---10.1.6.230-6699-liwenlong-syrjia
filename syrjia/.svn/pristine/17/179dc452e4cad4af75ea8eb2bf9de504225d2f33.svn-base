package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong 
 * 身体系统字典表
 */
@Table(name = "h_bodySystem_diction")
public class BodySystemDiction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 *  系统id
	 */
	@Id(increment=false)
	private String systemId;
	
	/**
	 * 系统名称
	 */
	@Column
	private String systemName;
	
	/**
	 * 启禁用状态 0-禁用 1-启用
	 */
	@Column
	private String systemIsOn;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemIsOn() {
		return systemIsOn;
	}

	public void setSystemIsOn(String systemIsOn) {
		this.systemIsOn = systemIsOn;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
