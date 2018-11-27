package cn.syrjia.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 医疗服务订单详情
 * 
 * @pdOid 34ab9f68-6ccb-49ae-ae01-fa326b5f92ca
 */
@Table(name = "t_medica_orderl_detail")
public class MedicalOrderDetial implements Serializable{


	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 * 
	 * @pdOid 70b05467-6032-430a-9cfc-6e5372b26314
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 订单号
	 * 
	 * @pdOid 81345270-fae9-421b-b703-6fb8ca826841
	 */
	@Column
	private String orderNo;
	/**
	 * 医院ID
	 * 
	 * @pdOid 059a36eb-170f-46c2-acfb-455657cb83f3
	 */
	@Column
	private String infirmaryId;
	
	/**
	 * 科室id
	 */
	@Column
	private String departId;
	
	/**
	 * 科室类型
	 */
	@Column
	private String departType;
	
	/**
	 * 病症类型id
	 */
	@Column
	private String illClassId;
	
	/**
	 * 职位id
	 */
	@Column
	private String positionId;
	/**
	 * 服务费
	 * 
	 * @pdOid 76d402b1-c9d0-4f78-9e0f-72708fcfde88
	 */
	@Column
	private Double serverPrice;
	
	/**
	 * 人员费、挂号费
	 */
	@Column
	private Double money;
	
	/**
	 * 服务费实际支付
	 */
	@Column
	private Double serverOriginalPrice;
	
	/**
	 * 备注
	 * 
	 * @pdOid 772a7dec-b397-47c5-969a-c0e96dd421ed
	 */
	@Column
	private String remarks;
	
	/**
	 * 创建时间
	 * 
	 * @pdOid acb7a3f0-2003-4985-9f83-d8e3a0f01af1
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 确认时间
	 * 
	 * @pdOid acb7a3f0-2003-4985-9f83-d8e3a0f01af1
	 */
	@Column
	private Integer confirmTime;

	/**
	 * 支付状态
	 */
	@Column
	private Integer paymentStatus;
	
	/**
	 * 访问地址
	 */
	@Column
	private String accessAddress;
	
	/**
	 * 物理地址
	 */
	@Column
	private String physicalAddress;
	
	/**
	 * 挂号区间开始时间
	 */
	@Column
	private String ghStartTime;
	
	/**
	 * 挂号区间结束时间
	 */
	@Column
	private String ghEndTime;
	
	/**
	 * 上午、下午
	 */
	@Column
	private String ghDayTime;

	/**
	 * 日期确认来源 0-系统 1-用户
	 */
	@Column
	private Integer confirmGhDateType;
	
	/**
	 * 日期
	 */
	@Column
	private String bookDate;
	
	/**
	 * 时间
	 */
	@Column
	private String bookTime;
	
	/**
	 * 名称
	 */
	@Column
	private String name;
	
	/**
	 * 性别
	 */
	@Column
	private String sex;
	
	/**
	 * 电话
	 */
	@Column
	private String phone;//患者联系方式
	
	/**
	 * 紧急电话
	 */
	@Column
	private String urgentPhone;//紧急联系方式
	
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
	private String selectInfirmaryName;//选择医院
	
	@Column
	private String illName;//疾病名称
	
	@Column
	private String ghDate;//挂号日期
	
	/**
	 * 图片列表
	 */
	private List<Piclib> piclist;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getInfirmaryId() {
		return infirmaryId;
	}

	public void setInfirmaryId(String infirmaryId) {
		this.infirmaryId = infirmaryId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getIllClassId() {
		return illClassId;
	}

	public void setIllClassId(String illClassId) {
		this.illClassId = illClassId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public Double getServerPrice() {
		return serverPrice;
	}

	public void setServerPrice(Double serverPrice) {
		this.serverPrice = serverPrice;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getServerOriginalPrice() {
		return serverOriginalPrice;
	}

	public void setServerOriginalPrice(Double serverOriginalPrice) {
		this.serverOriginalPrice = serverOriginalPrice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getAccessAddress() {
		return accessAddress;
	}

	public void setAccessAddress(String accessAddress) {
		this.accessAddress = accessAddress;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public String getGhStartTime() {
		return ghStartTime;
	}

	public void setGhStartTime(String ghStartTime) {
		this.ghStartTime = ghStartTime;
	}

	public String getGhEndTime() {
		return ghEndTime;
	}

	public void setGhEndTime(String ghEndTime) {
		this.ghEndTime = ghEndTime;
	}

	public String getGhDayTime() {
		return ghDayTime;
	}

	public void setGhDayTime(String ghDayTime) {
		this.ghDayTime = ghDayTime;
	}

	public String getBookDate() {
		return bookDate;
	}

	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getGhDate() {
		return ghDate;
	}

	public void setGhDate(String ghDate) {
		this.ghDate = ghDate;
	}

	public String getUrgentPhone() {
		return urgentPhone;
	}

	public void setUrgentPhone(String urgentPhone) {
		this.urgentPhone = urgentPhone;
	}

	public List<Piclib> getPiclist() {
		return piclist;
	}

	public void setPiclist(List<Piclib> piclist) {
		this.piclist = piclist;
	}

	public Integer getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Integer confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getSelectInfirmaryName() {
		return selectInfirmaryName;
	}

	public void setSelectInfirmaryName(String selectInfirmaryName) {
		this.selectInfirmaryName = selectInfirmaryName;
	}

	public String getIllName() {
		return illName;
	}

	public void setIllName(String illName) {
		this.illName = illName;
	}

	public Integer getIsBjVisit() {
		return isBjVisit;
	}

	public void setIsBjVisit(Integer isBjVisit) {
		this.isBjVisit = isBjVisit;
	}

	public Integer getConfirmGhDateType() {
		return confirmGhDateType;
	}

	public void setConfirmGhDateType(Integer confirmGhDateType) {
		this.confirmGhDateType = confirmGhDateType;
	}

}