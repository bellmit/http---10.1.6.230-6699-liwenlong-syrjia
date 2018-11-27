package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 职称表
 * @author liwenlong
 *
 * 2017-1-17
 */
@Table(name="h_technical_title")
public class TechnicalTitle implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 职称ID
	 */
	@Id(increment=false)
	private String titleId;
	
	/**
	 * 职称名称
	 */
	@Column
	private String tecName;
	
	/**
	 * 职称描述
	 */
	@Column
	private String tecDes;
	
	/**
	 * 职称排序
	 */
	@Column
	private Integer sort;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTecName() {
		return tecName;
	}

	public void setTecName(String tecName) {
		this.tecName = tecName;
	}

	public String getTecDes() {
		return tecDes;
	}

	public void setTecDes(String tecDes) {
		this.tecDes = tecDes;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	
}
