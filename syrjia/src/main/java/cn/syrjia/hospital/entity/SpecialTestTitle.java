package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_special_test_title")
public class SpecialTestTitle implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment=false)
	private String id;
	
	/**
	 * specialTestId
	 */
	private String specialTestId;
	
	/**
	 * qid
	 */
	private Integer qid;
	
	/**
	 * 标题名称
	 */
	private String titleName;
	
	/**
	 * 关系父id
	 */
	private String relationPid;
	
	/**
	 * 选项类型
	 */
	private Integer optionType;
	
	/**
	 * 强制的
	 */
	private Integer isMandatory;
	
	/**
	 * 创建时间
	 */
	private Integer createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecialTestId() {
		return specialTestId;
	}

	public void setSpecialTestId(String specialTestId) {
		this.specialTestId = specialTestId;
	}

	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getRelationPid() {
		return relationPid;
	}

	public void setRelationPid(String relationPid) {
		this.relationPid = relationPid;
	}

	public Integer getOptionType() {
		return optionType;
	}

	public void setOptionType(Integer optionType) {
		this.optionType = optionType;
	}

	public Integer getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Integer isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
}
