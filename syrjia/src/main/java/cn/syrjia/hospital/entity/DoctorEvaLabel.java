package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 评价标签表
 */
@Table(name = "h_doctor_evaLabel")
public class DoctorEvaLabel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 评论标签id
	 */
	@Id
	private Integer evaLabelId;
	
	/**
	 * 标签名称（20字以内）
	 */
	@Column
	private String evaLabelName;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public Integer getEvaLabelId() {
		return evaLabelId;
	}

	public void setEvaLabelId(Integer evaLabelId) {
		this.evaLabelId = evaLabelId;
	}

	public String getEvaLabelName() {
		return evaLabelName;
	}

	public void setEvaLabelName(String evaLabelName) {
		this.evaLabelName = evaLabelName;
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
