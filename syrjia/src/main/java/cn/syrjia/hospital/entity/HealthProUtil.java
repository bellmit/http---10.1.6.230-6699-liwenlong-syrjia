package cn.syrjia.hospital.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


@Table(name="h_healthPro_util")
public class HealthProUtil {

	@Id(increment=true)
	private Integer id;
	
	/**
	 * healthProUtilName
	 */
	@Column
	private String healthProUtilName;

	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 状态
	 */
	@Column(value={"0","1"})
	private Integer state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getHealthProUtilName() {
		return healthProUtilName;
	}

	public void setHealthProUtilName(String healthProUtilName) {
		this.healthProUtilName = healthProUtilName;
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
