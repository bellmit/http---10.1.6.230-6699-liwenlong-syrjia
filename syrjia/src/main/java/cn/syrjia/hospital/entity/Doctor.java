package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author liwenlong 
 * 医生表
 */
@Table(name = "t_doctor")
public class Doctor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 医生ID（主键）
	 */
	@Id(increment=false)
	private String doctorId;
	
	/**
	 * 所属医院ID
	 */
	@Column
	private String infirmaryId;
	
	/**
	 * 所属医馆ID
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
	 * 返给医生费用
	 */
	@Column
	private Double rebatePrice;
	
	/**
	 * 医生头像url
	 */
	@Column
	private String docUrl;
	
	/**
	 * 医生头像本地url
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
	 * 医生姓名
	 */
	@Column
	private String docName;
	
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
	 * 医生性别 0-男 1-女
	 */
	@Column
	private String docSex;
	
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
	 * 医生启禁用状态 0-禁用 1-启用
	 */
	@Column
	private String docIsOn;
	
	/**
	 * 是否推荐  0-否 1-是
	 */
	@Column
	private String isRecommended;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 是否可用优惠券 0-可以 1-不可
	 */
	@Column
	private String isUseCoupon;
	
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
	 * 是否后台管理员新增 0-是 1-否（线下申请）
	 */
	@Column
	private String isLocalDoc;
	
	/**
	 * 身份证号
	 */
	@Column
	private String idCardNo;
	
	/**
	 * 系统id
	 */
	private String systemId;
	
	/**
	 * openId
	 */
	@Column
	private String openid;
	
	/**
	 * 二维码路径
	 */
	@Column
	private String qrCodeUrl;
	
	/**
	 * 关注奖
	 */
	@Column
	private Double followPrice;
	
	/**
	 * 提成比例
	 */
	@Column
	private Double deductScale;
	
	/**
	 * 是否线上调理医生 0-否 1-是
	 */
	@Column
	private Integer isOnLine;
	
	/**
	 * 职称ID
	 */
	@Column
	private String docPositionId;
	
	/**
	 * 每天接诊数量
	 */
	@Column
	private Integer askCount;

	/**
	 * 是否开启隐私保护
	 */
	@Column
	private Integer isProtected;

	/**
	 * 是否开启锁定患者
	 */
	@Column
	private Integer isLockUser;
	
	/**
	 * 是否接受
	 */
	private Integer isAccpetAsk;

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
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

	public String getDocLocalUrl() {
		return docLocalUrl;
	}

	public void setDocLocalUrl(String docLocalUrl) {
		this.docLocalUrl = docLocalUrl;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsUseCoupon() {
		return isUseCoupon;
	}

	public void setIsUseCoupon(String isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}

	public String getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(String isRecommended) {
		this.isRecommended = isRecommended;
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

	public String getIsLocalDoc() {
		return isLocalDoc;
	}

	public void setIsLocalDoc(String isLocalDoc) {
		this.isLocalDoc = isLocalDoc;
	}

	public Double getRebatePrice() {
		return rebatePrice;
	}

	public void setRebatePrice(Double rebatePrice) {
		this.rebatePrice = rebatePrice;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public Double getFollowPrice() {
		return followPrice;
	}

	public void setFollowPrice(Double followPrice) {
		this.followPrice = followPrice;
	}

	public Double getDeductScale() {
		return deductScale;
	}

	public void setDeductScale(Double deductScale) {
		this.deductScale = deductScale;
	}

	public Integer getIsOnLine() {
		return isOnLine;
	}

	public void setIsOnLine(Integer isOnLine) {
		this.isOnLine = isOnLine;
	}

	public String getDocPositionId() {
		return docPositionId;
	}

	public void setDocPositionId(String docPositionId) {
		this.docPositionId = docPositionId;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getInfirmaryId() {
		return infirmaryId;
	}

	public void setInfirmaryId(String infirmaryId) {
		this.infirmaryId = infirmaryId;
	}

	public String getInfirDepartId() {
		return infirDepartId;
	}

	public void setInfirDepartId(String infirDepartId) {
		this.infirDepartId = infirDepartId;
	}

	public Integer getAskCount() {
		return askCount;
	}

	public void setAskCount(Integer askCount) {
		this.askCount = askCount;
	}

	public Integer getIsProtected() {
		return isProtected;
	}

	public void setIsProtected(Integer isProtected) {
		this.isProtected = isProtected;
	}

	public Integer getIsLockUser() {
		return isLockUser;
	}

	public void setIsLockUser(Integer isLockUser) {
		this.isLockUser = isLockUser;
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

	public Integer getIsAccpetAsk() {
		return isAccpetAsk;
	}

	public void setIsAccpetAsk(Integer isAccpetAsk) {
		this.isAccpetAsk = isAccpetAsk;
	}

	public String getDocProfessUrlTwo() {
		return docProfessUrlTwo;
	}

	public void setDocProfessUrlTwo(String docProfessUrlTwo) {
		this.docProfessUrlTwo = docProfessUrlTwo;
	}
	
}
