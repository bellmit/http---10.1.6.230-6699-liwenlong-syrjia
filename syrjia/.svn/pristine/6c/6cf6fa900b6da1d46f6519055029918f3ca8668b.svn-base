package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_doctor_notice")
public class DoctorNotice implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 医生ID
	 */
	@Column
	private String doctorId;
	
	/**
	 * 公告标题
	 */
	@Column
	private String title;
	
	/**
	 * 公告内容
	 */
	@Column
	private String content;
	
	/**
	 * 公告状态 0-草稿 1-启用（审核通过） 2-禁用 3-删除 4-待审核 5-审核不通过 默认4
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createtime;
	
	/**
	 * 审核人
	 */
	@Column
	private String auditUserId;
	
	/**
	 * 审核时间
	 */
	@Column
	private String auditTime;
	
	/**
	 * 是否群发0-否 1-是 默认0 （每月只可群发一次）
	 */
	@Column
	private Integer isMass;
	
	/**
	 * 创建方式
	 */
	private String createWay;
	
	/**
	 * 开始时间
	 */
	private Integer startTime;
	
	/**
	 * 结束时间
	 */
	private Integer endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getIsMass() {
		return isMass;
	}

	public void setIsMass(Integer isMass) {
		this.isMass = isMass;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public String getCreateWay() {
		return createWay;
	}

	public void setCreateWay(String createWay) {
		this.createWay = createWay;
	}
 
}
