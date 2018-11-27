package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 模糊挂号详情
 * 
 * @pdOid 34ab9f68-6ccb-49ae-ae01-fa326b5f92ca
 */
@Table(name = "t_vague_gh")
public class VagueGh implements Serializable{


	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 * 
	 * @pdOid 70b05467-6032-430a-9cfc-6e5372b26314
	 */
	@Id
	private Integer id;
	
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
	 * 1-未处理 2-已处理 3-已删除
	 */
	@Column
	private Integer state;
	
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
	
	@Column
	private String phone;//患者联系方式
	
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
	private String memberId;//用户ID
	
	@Column
	private String operaUserid;//操作人ID
	
	@Column
	private String cancleNote;//作废理由
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
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
	 * 职称id
	 */
	@Column
	private String positionId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getOperaUserid() {
		return operaUserid;
	}

	public void setOperaUserid(String operaUserid) {
		this.operaUserid = operaUserid;
	}

	public String getCancleNote() {
		return cancleNote;
	}

	public void setCancleNote(String cancleNote) {
		this.cancleNote = cancleNote;
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

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public Integer getIsBjVisit() {
		return isBjVisit;
	}

	public void setIsBjVisit(Integer isBjVisit) {
		this.isBjVisit = isBjVisit;
	}
	
}