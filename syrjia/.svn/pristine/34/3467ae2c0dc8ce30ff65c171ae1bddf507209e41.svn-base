package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author liwenlong 
 * 疾病或不适分类
 */
@Table(name = "t_Illness_class")
public class IllnessOrDiscomfortClass implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false)
	private String illClassId;

	/**
	 * 分类名称
	 */
	@Column
	private String illClassName;
	
	/**
	 * 分类简拼
	 */
	@Column
	private String illClassShort;
	
	/**
	 * 所属科室
	 */
	@Column
	private String departId;

	/**
	 * 病因病机
	 */
	@Column
	private String illClassDesc;

	/**
	 * 分类状态
	 */
	@Column(value = {"10"})
	private String illClassStatus;

	/**
	 * 分类创建日期
	 */
	@Column
	private Integer createDate;

	/**
	 * 启禁用 0-禁用 1-启用
	 */
	@Column(value = { "1" })
	private String illClassIsOn;

	/**
	 * 分类图标（手机图标）
	 */
	@Column
	private String illClassUrl; // 图片访问地址

	/**
	 * 分类本地地址（手机图标）
	 */
	@Column
	private String illClassLocalUrl; // 图片本地地址
	
	/**
	 * 分类图标（展示图）
	 */
	@Column
	private String illClassShowUrl; // 图片访问地址

	/**
	 * 分类本地地址（展示图）
	 */
	@Column
	private String illClassShowLocalUrl; // 图片本地地址

	/**
	 * 排序
	 */
	@Column
	private Integer isSort;

	/**
	 * 字符串
	 */
	@Column
	private String rsrvStr1;

	/**
	 * 字符串
	 */
	@Column
	private String rsrvStr2;

	/**
	 * 字符串
	 */
	@Column
	private String rsrvStr3;
	
	/*@OneToMany(correlationField="illClassId",correlationOtherField="illClassId")
	private List<MiddleUtil> middlist;*/

	public String getIllClassId() {
		return illClassId;
	}

	public void setIllClassId(String illClassId) {
		this.illClassId = illClassId;
	}

	public String getIllClassName() {
		return illClassName;
	}

	public void setIllClassName(String illClassName) {
		this.illClassName = illClassName;
	}

	public String getIllClassDesc() {
		return illClassDesc;
	}

	public void setIllClassDesc(String illClassDesc) {
		this.illClassDesc = illClassDesc;
	}

	public String getIllClassStatus() {
		return illClassStatus;
	}

	public void setIllClassStatus(String illClassStatus) {
		this.illClassStatus = illClassStatus;
	}

	public Integer getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Integer createDate) {
		this.createDate = createDate;
	}

	public String getIllClassIsOn() {
		return illClassIsOn;
	}

	public void setIllClassIsOn(String illClassIsOn) {
		this.illClassIsOn = illClassIsOn;
	}

	public String getIllClassUrl() {
		return illClassUrl;
	}

	public void setIllClassUrl(String illClassUrl) {
		this.illClassUrl = illClassUrl;
	}

	public String getIllClassLocalUrl() {
		return illClassLocalUrl;
	}

	public void setIllClassLocalUrl(String illClassLocalUrl) {
		this.illClassLocalUrl = illClassLocalUrl;
	}

	public Integer getIsSort() {
		return isSort;
	}

	public void setIsSort(Integer isSort) {
		this.isSort = isSort;
	}

	public String getRsrvStr1() {
		return rsrvStr1;
	}

	public void setRsrvStr1(String rsrvStr1) {
		this.rsrvStr1 = rsrvStr1;
	}

	public String getRsrvStr2() {
		return rsrvStr2;
	}

	public void setRsrvStr2(String rsrvStr2) {
		this.rsrvStr2 = rsrvStr2;
	}

	public String getRsrvStr3() {
		return rsrvStr3;
	}

	public void setRsrvStr3(String rsrvStr3) {
		this.rsrvStr3 = rsrvStr3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIllClassShowUrl() {
		return illClassShowUrl;
	}

	public void setIllClassShowUrl(String illClassShowUrl) {
		this.illClassShowUrl = illClassShowUrl;
	}

	public String getIllClassShowLocalUrl() {
		return illClassShowLocalUrl;
	}

	public void setIllClassShowLocalUrl(String illClassShowLocalUrl) {
		this.illClassShowLocalUrl = illClassShowLocalUrl;
	}

	/*public List<MiddleUtil> getMiddlist() {
		return middlist;
	}

	public void setMiddlist(List<MiddleUtil> middlist) {
		this.middlist = middlist;
	}*/

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getIllClassShort() {
		return illClassShort;
	}

	public void setIllClassShort(String illClassShort) {
		this.illClassShort = illClassShort;
	}
}
