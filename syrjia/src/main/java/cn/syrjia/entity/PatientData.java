package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_patient_data")
public class PatientData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false,UUID=true)
	private String id; 
	
	/**
	 * 会员
	 */
	@Column
	private String memberId;//会员ID
	
	/**
	 * 姓名
	 */
	@Column
	private String name;//或者姓名
	
	/**
	 * 电话
	 */
	@Column
	private String phone;//联系方式
	
	/**
	 * 省
	 */
	@Column
	private String province;//省
	
	@Column
	private String city;//市
	
	@Column
	private String area;//区
	
	@Column
	private String detailedAddress;//详细地址
	
	@Column
	private Integer createTime;//创建时间
	
	@Column
	private Integer sex;//0-男 1-女
	
	@Column
	private String state;//状态 默认1 1正常 2删除 3默认地址
	
	@Column
	private String idCardNo;//身份证号
	
	@Column
	private String visitNo;//就诊卡号
	
	@Column
	private String carteVitalNo;//医保卡号
	
	@Column
	private String nexus;//关系
	
	@Column
	private Integer isBjVisit;//是否为北京市医保 0-否 1-是 默认0
	
	@Column
	private Integer age; //年龄
	
	@Column
	private Integer isDefaultPer;// 是否是默认就诊人  0 不是 1 是
	
	@Column
	private String nameShort; //年龄

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getIsDefaultPer() {
		return isDefaultPer;
	}

	public void setIsDefaultPer(Integer isDefaultPer) {
		this.isDefaultPer = isDefaultPer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getNameShort() {
		return nameShort;
	}

	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getCarteVitalNo() {
		return carteVitalNo;
	}

	public void setCarteVitalNo(String carteVitalNo) {
		this.carteVitalNo = carteVitalNo;
	}

	public String getNexus() {
		return nexus;
	}

	public void setNexus(String nexus) {
		this.nexus = nexus;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getIsBjVisit() {
		return isBjVisit;
	}

	public void setIsBjVisit(Integer isBjVisit) {
		this.isBjVisit = isBjVisit;
	}

}
