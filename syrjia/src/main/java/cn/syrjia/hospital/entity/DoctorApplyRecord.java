package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 医生申请审核记录表
 */
@Table(name = "t_doctor_apply_record")
public class DoctorApplyRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 申请id
	 */
	@Id(increment=false,UUID=true)
	private String applyId;
	
	/**
	 * 会员ID
	 */
	@Column
	private String openid;
	
	/**
	 * 证件审核不通过理由
	 */
	@Column
	private String recordPicFailNote;
	
	/**
	 * 医生姓名
	 */
	@Column
	private String docName;
	
	/**
	 * 医院Id
	 */
	@Column
	private String hospitalId;
	
	/**
	 * 医院名称
	 */
	@Column
	private String hosName;
	
	/**
	 * 医院所在省
	 */
	@Column
	private String hosProvice;
	
	/**
	 * 医院所在市
	 */
	@Column
	private String hosCity;
	
	/**
	 * 医院所在县区
	 */
	@Column
	private String hosCounty;
	
	/**
	 * 医院详细地址
	 */
	@Column
	private String hosAddr;
	
	/**
	 * 医馆所属科室ID
	 */
	@Column
	private String departId;
	
	/**
	 * 医院所属科室ID
	 */
	@Column
	private String infirDepartId;
	
	/**
	 * 职位
	 */
	@Column
	private String docPosition;
	
	/**
	 * 费用价格
	 */
	@Column
	private Double docPrice;
	
	/**
	 * 开始时间
	 */
	@Column
	private String startTime;

	/**
	 * 结束时间
	 */
	@Column
	private String endTime;
	
	/**
	 * 医生头像url
	 */
	@Column
	private String docUrl;
	
	/**
	 * 医生头像url本地
	 */
	@Column
	private String docLocalUrl;
	
	/**
	 * 医生身份证正面url
	 */
	@Column
	private String idCardFaceUrl;
	
	/**
	 * 医生身份证反面url
	 */
	@Column
	private String idCardBackUrl;
	
	/**
	 * 医生执业注册证url（第二三页）
	 */
	@Column
	private String pracEoSUrl;
	
	/**
	 * 医生执业注册证url（第四页）
	 */
	@Column
	private String pracFourUrl;
	
	/**
	 * 医生医师资格证url(2/3页)
	 */
	@Column
	private String docPhyUrl;
	
	/**
	 * 医生医师资格证url（第四页）
	 */
	@Column
	private String docPhyFourUrl;
	
	/**
	 * 医生职称证书url
	 */
	@Column
	private String docProfessUrl;
	
	/**
	 * 医生职称证书urlTwo
	 */
	@Column
	private String docProfessUrlTwo;
	
	/**
	 * 医生性别 0-男 1-女
	 */
	@Column
	private String docSex;
	
	/**
	 * 申请用户id
	 */
	@Column
	private String applyUserId;
	
	/**
	 * 医生电话
	 */
	@Column
	private String docPhone;

	/**
	 * 医生个人推荐语
	 */
	@Column
	private String docSignature;
	
	/**
	 * 医生描述
	 */
	@Column
	private String docDesc;
	
	/**
	 * 医生状态 10-在用 30-删除
	 */
	@Column
	private String docStatus;
	
	/**
	 * 医生申请状态 0-申请中 1-通过 2-失败
	 */
	@Column
	private String docIsOn;
	
	/**
	 * 创建时间
	 */
	@Column(isSort=true)
	private Integer createTime;
	
	/**
	 * 医生简介
	 */
	@Column
	private String docAbstract;
	
	/**
	 * 医生公告
	 */
	@Column
	private String docNotice;
	
	/**
	 * 失败原因
	 */
	@Column
	private String recordFailNote;
	
	/**
	 * 身份证号
	 */
	@Column
	private String idCardNo;
	
	/**
	 * 是否助理提交 0-否 1-是
	 */
	@Column
	private Integer saleTj;
	
	/**
	 * 是否接受专业问诊 0-否 1-是
	 */
	@Column
	private Integer isAcceptAsk;
	
	/**
	 * 创建方式 0-线下申请 1-扫码
	 */
	@Column
	private Integer createWay;
	
	/**
	 * 0-待审核 1-已审核 2-审核失败
	 */
	@Column
	private String basicIsOn;
	
	/**
	 * 医院id
	 */
	@Column
	private String infirmaryId;
	
	/**
	 * 0-待审核 1-已审核 2-审核失败
	 */
	@Column
	private String picIsOn;
	
	/**
	 * 病症id
	 */
	private String illIds;
	
	/**
	 * infirDepartIds
	 */
	private String infirDepartIds;
	
	/**
	 * 医生职位id
	 */
	@Column
	private String docPositionId;
	

	/**
	 * 原医助id
	 */
	@Column
	private String oldSalesId;
	
	/**
	 * 现医助id
	 */
	@Column
	private String salesId;
	
	/**
	 * 默认医珍堂ID
	 */
	@Column
	private String defaultYztId;
	
	/**
	 *  自定义医珍堂ID
	 */
	@Column
	private String customYztId;

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getHosProvice() {
		return hosProvice;
	}

	public void setHosProvice(String hosProvice) {
		this.hosProvice = hosProvice;
	}

	public String getHosCity() {
		return hosCity;
	}

	public void setHosCity(String hosCity) {
		this.hosCity = hosCity;
	}

	public String getHosCounty() {
		return hosCounty;
	}

	public void setHosCounty(String hosCounty) {
		this.hosCounty = hosCounty;
	}

	public String getHosAddr() {
		return hosAddr;
	}

	public void setHosAddr(String hosAddr) {
		this.hosAddr = hosAddr;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getDocPosition() {
		return docPosition;
	}

	public void setDocPosition(String docPosition) {
		this.docPosition = docPosition;
	}

	public Double getDocPrice() {
		return docPrice;
	}

	public void setDocPrice(Double docPrice) {
		this.docPrice = docPrice;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getDocSex() {
		return docSex;
	}

	public void setDocSex(String docSex) {
		this.docSex = docSex;
	}

	public String getDocPhone() {
		return docPhone;
	}

	public void setDocPhone(String docPhone) {
		this.docPhone = docPhone;
	}

	public String getDocSignature() {
		return docSignature;
	}

	public void setDocSignature(String docSignature) {
		this.docSignature = docSignature;
	}

	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	public String getDocIsOn() {
		return docIsOn;
	}

	public void setDocIsOn(String docIsOn) {
		this.docIsOn = docIsOn;
	}
	
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getDocAbstract() {
		return docAbstract;
	}

	public void setDocAbstract(String docAbstract) {
		this.docAbstract = docAbstract;
	}

	public String getDocNotice() {
		return docNotice;
	}

	public void setDocNotice(String docNotice) {
		this.docNotice = docNotice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getRecordFailNote() {
		return recordFailNote;
	}

	public void setRecordFailNote(String recordFailNote) {
		this.recordFailNote = recordFailNote;
	}

	public String getIdCardFaceUrl() {
		return idCardFaceUrl;
	}

	public void setIdCardFaceUrl(String idCardFaceUrl) {
		this.idCardFaceUrl = idCardFaceUrl;
	}

	public String getIdCardBackUrl() {
		return idCardBackUrl;
	}

	public void setIdCardBackUrl(String idCardBackUrl) {
		this.idCardBackUrl = idCardBackUrl;
	}

	public String getPracEoSUrl() {
		return pracEoSUrl;
	}

	public void setPracEoSUrl(String pracEoSUrl) {
		this.pracEoSUrl = pracEoSUrl;
	}

	public String getPracFourUrl() {
		return pracFourUrl;
	}

	public void setPracFourUrl(String pracFourUrl) {
		this.pracFourUrl = pracFourUrl;
	}

	public String getDocPhyUrl() {
		return docPhyUrl;
	}

	public void setDocPhyUrl(String docPhyUrl) {
		this.docPhyUrl = docPhyUrl;
	}

	public String getDocPhyFourUrl() {
		return docPhyFourUrl;
	}

	public void setDocPhyFourUrl(String docPhyFourUrl) {
		this.docPhyFourUrl = docPhyFourUrl;
	}

	public String getDocProfessUrl() {
		return docProfessUrl;
	}

	public void setDocProfessUrl(String docProfessUrl) {
		this.docProfessUrl = docProfessUrl;
	}

	public String getDocLocalUrl() {
		return docLocalUrl;
	}

	public void setDocLocalUrl(String docLocalUrl) {
		this.docLocalUrl = docLocalUrl;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Integer getIsAcceptAsk() {
		return isAcceptAsk;
	}

	public void setIsAcceptAsk(Integer isAcceptAsk) {
		this.isAcceptAsk = isAcceptAsk;
	}

	public Integer getCreateWay() {
		return createWay;
	}

	public void setCreateWay(Integer createWay) {
		this.createWay = createWay;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getInfirDepartId() {
		return infirDepartId;
	}

	public void setInfirDepartId(String infirDepartId) {
		this.infirDepartId = infirDepartId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDocPositionId() {
		return docPositionId;
	}

	public void setDocPositionId(String docPositionId) {
		this.docPositionId = docPositionId;
	}

	public String getBasicIsOn() {
		return basicIsOn;
	}

	public void setBasicIsOn(String basicIsOn) {
		this.basicIsOn = basicIsOn;
	}

	public String getPicIsOn() {
		return picIsOn;
	}

	public void setPicIsOn(String picIsOn) {
		this.picIsOn = picIsOn;
	}

	public String getInfirmaryId() {
		return infirmaryId;
	}

	public void setInfirmaryId(String infirmaryId) {
		this.infirmaryId = infirmaryId;
	}

	public String getIllIds() {
		return illIds;
	}

	public void setIllIds(String illIds) {
		this.illIds = illIds;
	}

	public String getInfirDepartIds() {
		return infirDepartIds;
	}

	public void setInfirDepartIds(String infirDepartIds) {
		this.infirDepartIds = infirDepartIds;
	}

	public String getDocProfessUrlTwo() {
		return docProfessUrlTwo;
	}

	public void setDocProfessUrlTwo(String docProfessUrlTwo) {
		this.docProfessUrlTwo = docProfessUrlTwo;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Integer getSaleTj() {
		return saleTj;
	}

	public void setSaleTj(Integer saleTj) {
		this.saleTj = saleTj;
	}

	public String getRecordPicFailNote() {
		return recordPicFailNote;
	}

	public void setRecordPicFailNote(String recordPicFailNote) {
		this.recordPicFailNote = recordPicFailNote;
	}

	public String getOldSalesId() {
		return oldSalesId;
	}

	public void setOldSalesId(String oldSalesId) {
		this.oldSalesId = oldSalesId;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getDefaultYztId() {
		return defaultYztId;
	}

	public void setDefaultYztId(String defaultYztId) {
		this.defaultYztId = defaultYztId;
	}

	public String getCustomYztId() {
		return customYztId;
	}

	public void setCustomYztId(String customYztId) {
		this.customYztId = customYztId;
	}
}
