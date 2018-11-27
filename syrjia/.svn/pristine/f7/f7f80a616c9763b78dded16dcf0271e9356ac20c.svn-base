package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong 
 * 标签类
 */
@Table(name="h_label")
public class Label implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@Id(increment = false)
	private String id;
	
	/**
	 * 病症类别id
	 */
	@Column
	private String illClassId;
	
	/**
	 * 标签名称
	 */
	@Column
	private String labelName;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 疾病分类名称
	 */
	@Join(table="h_Illness_class",joinType="inner",id="illClassId",otherId="illClassId",otherName="illClassName")
	private String illClassName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIllClassId() {
		return illClassId;
	}

	public void setIllClassId(String illClassId) {
		this.illClassId = illClassId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
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

	public String getIllClassName() {
		return illClassName;
	}

	public void setIllClassName(String illClassName) {
		this.illClassName = illClassName;
	}
}
