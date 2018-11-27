package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_doctor_conditioning_detail")
public class DoctorConditioningDetail implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id(increment=false)
	private String id;
	
	/**
	 * 调理方id
	 */
	private String conditioningId;
	
	/**
	 * 药id
	 */
	private String drugId;
	
	/**
	 * 重量
	 */
	private Double weight;
	
	/**
	 * 选项数
	 */
	private Integer optionNum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getConditioningId() {
		return conditioningId;
	}
	public void setConditioningId(String conditioningId) {
		this.conditioningId = conditioningId;
	}
	
	public String getDrugId() {
		return drugId;
	}
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Integer getOptionNum() {
		return optionNum;
	}
	public void setOptionNum(Integer optionNum) {
		this.optionNum = optionNum;
	}
}
