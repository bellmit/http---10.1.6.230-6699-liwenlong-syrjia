package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.OneToMany;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong 
 * 科室数据字典表
 */
@Table(name = "t_department")
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id(increment = false)
	private String departId;
	
	/**
	 * 部门名称
	 */
	@Column
	private String departName;
	
	/**
	 * 部门分类
	 */
	@Column
	private Integer departSort;
	
	/**
	 * 部门方式
	 */
	@Column
	private String departType;
	
	/**
	 * 部门简称
	 */
	@Column
	private String departShort;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 状态
	 */
	@Column(value="1")
	private Integer state;
	
	/**
	 * 图像地址
	 */
	@Column
	private String iconUrl;
	
	/**
	 * 病症列表
	 */
	@OneToMany(correlationField="departId",correlationOtherField="departId")
	private List<IllnessOrDiscomfortClass> illClasslist;
	
	/**
	 * 病症
	 */
	private String illClass;
	
	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
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

	public List<IllnessOrDiscomfortClass> getIllClasslist() {
		return illClasslist;
	}

	public void setIllClasslist(List<IllnessOrDiscomfortClass> illClasslist) {
		this.illClasslist = illClasslist;
	}

	public Integer getDepartSort() {
		return departSort;
	}

	public void setDepartSort(Integer departSort) {
		this.departSort = departSort;
	}

	public String getDepartShort() {
		return departShort;
	}

	public void setDepartShort(String departShort) {
		this.departShort = departShort;
	}

	public String getIllClass() {
		return illClass;
	}

	public void setIllClass(String illClass) {
		this.illClass = illClass;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}
}
