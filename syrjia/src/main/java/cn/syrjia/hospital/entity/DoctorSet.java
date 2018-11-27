package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author lwl
 * 2018-03-19
 */
@Table(name="t_doctor_set")
public class DoctorSet implements Serializable {

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
	 * 0-否 1-是 医生开启隐身设置后，只有医生的患者可以看到该医生 默认0
	 */
	@Column
	private Integer isHide;

	/**
	 * 是否锁定患者 0-否 1-是 默认0
	 */
	@Column
	private Integer isLockPatient;

	/**
	 * 免打扰设置 0-关 1-开  默认0
	 */
	@Column
	private Integer isDisturb;
	
	/**
	 * 免打扰开始时间
	 */
	@Column
	private String disturbStartTime;
	
	/**
	 * 免打扰结束时间
	 */
	@Column
	private String disturbEndTime;
	
	/**
	 * 线上问诊（图文） 0-否 1-是 默认0
	 */
	@Column
	private Integer isOnlineTwGh;

	/**
	 * 线上问诊（图文） 初诊挂号费(图文)
	 */
	@Column
	private Double fisrtTwGhMoney;

	/**
	 * 线上问诊（图文） 复诊挂号费（图文）
	 */
	@Column
	private Double repeatTwGhMoney;

	/**
	 * 单日线上调理接单数（图文） 默认999
	 */
	@Column
	private Integer acceptTwOrderCount;

	/**
	 * 线上问诊电话 0-关 1-开 默认0
	 */
	@Column
	private Integer isOnlinePhoneGh;
	
	/**
	 * 初诊挂号费(电话)
	 */
	@Column
	private Double fisrtPhoneGhMoney;
	
	/**
	 * 复诊挂号费（电话）
	 */
	@Column
	private Double repeatPhoneGhMoney;

	/**
	 * 单日线上调理接单数（电话）默认999
	 */
	@Column
	private Integer acceptPhoneOrderCount;
	
	/**
	 * 图文咨询（图文） 0-关 1-开 默认0
	 */
	@Column
	private Integer isOnlineTwZx;
	
	/**
	 * 图文咨询费
	 */
	@Column
	private Double twZxMoney;

	/**
	 * 可咨询次数
	 */
	@Column
	private Integer twZxCount;

	/**
	 * 诊前咨询免费次数
	 */
	@Column
	private Integer twZqZxCount;

	/**
	 * 诊后咨询免费次数 默认1
	 */
	@Column
	private Integer twZhZxCount;

	/**
	 * 咨询免费时限(小时) 默认24
	 */
	@Column
	private Integer twZxTime;

	/**
	 * 电话咨询（电话） 0-关 1-开 默认0
	 */
	@Column
	private Integer isOnlinePhoneZx;
	
	/**
	 * acceptPhoneZxOrderCount
	 */
	@Column
	private Integer acceptPhoneZxOrderCount;
	
	/**
	 * acceptTwZxOrderCount
	 */
	@Column
	private Integer acceptTwZxOrderCount;
	
	/**
	 * phoneGhServerTime
	 */
	@Column
	private Double phoneGhServerTime;
	
	/**
	 * 电话咨询费
	 */
	@Column
	private Double phoneZxMoney;

	/**
	 * 可咨询次数
	 */
	@Column
	private Integer phoneZxCount;

	/**
	 * 电话咨询时长
	 */
	@Column
	private Integer phoneZxTime;
	
	/**
	 * 是否开始隐私密码 0-否 1-是 默认0
	 */
	@Column
	private Integer isSecret;
	
	/**
	 * 隐私密码
	 */
	@Column
	private String secretPassword;
	
	/**
	 * 是否开始手势密码 0-否 1-是 默认0
	 */
	@Column
	private Integer isGesture;
	
	/**
	 * 手势密码
	 */
	@Column
	private String gesturePassword;
	
	/**
	 * 是否隐藏克数 0-否 1-是-是 默认0
	 */
	@Column
	private Integer isHideGram;
	
	/**
	 * 是否系统默认 0-是 1-医生设置
	 */
	@Column
	private Integer isSystem;
	
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

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}

	public Integer getIsLockPatient() {
		return isLockPatient;
	}

	public void setIsLockPatient(Integer isLockPatient) {
		this.isLockPatient = isLockPatient;
	}

	public Integer getIsDisturb() {
		return isDisturb;
	}

	public void setIsDisturb(Integer isDisturb) {
		this.isDisturb = isDisturb;
	}

	public String getDisturbStartTime() {
		return disturbStartTime;
	}

	public void setDisturbStartTime(String disturbStartTime) {
		this.disturbStartTime = disturbStartTime;
	}

	public String getDisturbEndTime() {
		return disturbEndTime;
	}

	public void setDisturbEndTime(String disturbEndTime) {
		this.disturbEndTime = disturbEndTime;
	}

	public Integer getIsOnlineTwGh() {
		return isOnlineTwGh;
	}

	public void setIsOnlineTwGh(Integer isOnlineTwGh) {
		this.isOnlineTwGh = isOnlineTwGh;
	}

	public Double getFisrtTwGhMoney() {
		return fisrtTwGhMoney;
	}

	public void setFisrtTwGhMoney(Double fisrtTwGhMoney) {
		this.fisrtTwGhMoney = fisrtTwGhMoney;
	}

	public Double getRepeatTwGhMoney() {
		return repeatTwGhMoney;
	}

	public void setRepeatTwGhMoney(Double repeatTwGhMoney) {
		this.repeatTwGhMoney = repeatTwGhMoney;
	}

	public Integer getAcceptTwOrderCount() {
		return acceptTwOrderCount;
	}

	public void setAcceptTwOrderCount(Integer acceptTwOrderCount) {
		this.acceptTwOrderCount = acceptTwOrderCount;
	}

	public Integer getIsOnlinePhoneGh() {
		return isOnlinePhoneGh;
	}

	public void setIsOnlinePhoneGh(Integer isOnlinePhoneGh) {
		this.isOnlinePhoneGh = isOnlinePhoneGh;
	}

	public Double getFisrtPhoneGhMoney() {
		return fisrtPhoneGhMoney;
	}

	public void setFisrtPhoneGhMoney(Double fisrtPhoneGhMoney) {
		this.fisrtPhoneGhMoney = fisrtPhoneGhMoney;
	}

	public Double getRepeatPhoneGhMoney() {
		return repeatPhoneGhMoney;
	}

	public void setRepeatPhoneGhMoney(Double repeatPhoneGhMoney) {
		this.repeatPhoneGhMoney = repeatPhoneGhMoney;
	}

	public Integer getIsOnlineTwZx() {
		return isOnlineTwZx;
	}

	public void setIsOnlineTwZx(Integer isOnlineTwZx) {
		this.isOnlineTwZx = isOnlineTwZx;
	}

	public Double getTwZxMoney() {
		return twZxMoney;
	}

	public void setTwZxMoney(Double twZxMoney) {
		this.twZxMoney = twZxMoney;
	}

	public Integer getTwZxCount() {
		return twZxCount;
	}

	public void setTwZxCount(Integer twZxCount) {
		this.twZxCount = twZxCount;
	}

	public Integer getTwZqZxCount() {
		return twZqZxCount;
	}

	public void setTwZqZxCount(Integer twZqZxCount) {
		this.twZqZxCount = twZqZxCount;
	}

	public Integer getTwZhZxCount() {
		return twZhZxCount;
	}

	public void setTwZhZxCount(Integer twZhZxCount) {
		this.twZhZxCount = twZhZxCount;
	}

	public Integer getTwZxTime() {
		return twZxTime;
	}

	public void setTwZxTime(Integer twZxTime) {
		this.twZxTime = twZxTime;
	}

	public Integer getIsOnlinePhoneZx() {
		return isOnlinePhoneZx;
	}

	public void setIsOnlinePhoneZx(Integer isOnlinePhoneZx) {
		this.isOnlinePhoneZx = isOnlinePhoneZx;
	}

	public Double getPhoneZxMoney() {
		return phoneZxMoney;
	}

	public void setPhoneZxMoney(Double phoneZxMoney) {
		this.phoneZxMoney = phoneZxMoney;
	}

	public Integer getPhoneZxCount() {
		return phoneZxCount;
	}

	public void setPhoneZxCount(Integer phoneZxCount) {
		this.phoneZxCount = phoneZxCount;
	}

	public Integer getPhoneZxTime() {
		return phoneZxTime;
	}

	public void setPhoneZxTime(Integer phoneZxTime) {
		this.phoneZxTime = phoneZxTime;
	}

	public Integer getAcceptPhoneOrderCount() {
		return acceptPhoneOrderCount;
	}

	public void setAcceptPhoneOrderCount(Integer acceptPhoneOrderCount) {
		this.acceptPhoneOrderCount = acceptPhoneOrderCount;
	}

	public Integer getIsSecret() {
		return isSecret;
	}

	public void setIsSecret(Integer isSecret) {
		this.isSecret = isSecret;
	}

	public String getSecretPassword() {
		return secretPassword;
	}

	public void setSecretPassword(String secretPassword) {
		this.secretPassword = secretPassword;
	}

	public Integer getIsGesture() {
		return isGesture;
	}

	public void setIsGesture(Integer isGesture) {
		this.isGesture = isGesture;
	}

	public String getGesturePassword() {
		return gesturePassword;
	}

	public void setGesturePassword(String gesturePassword) {
		this.gesturePassword = gesturePassword;
	}

	public Integer getIsHideGram() {
		return isHideGram;
	}

	public void setIsHideGram(Integer isHideGram) {
		this.isHideGram = isHideGram;
	}

	public Integer getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}

	public Integer getAcceptPhoneZxOrderCount() {
		return acceptPhoneZxOrderCount;
	}

	public void setAcceptPhoneZxOrderCount(Integer acceptPhoneZxOrderCount) {
		this.acceptPhoneZxOrderCount = acceptPhoneZxOrderCount;
	}

	public Integer getAcceptTwZxOrderCount() {
		return acceptTwZxOrderCount;
	}

	public void setAcceptTwZxOrderCount(Integer acceptTwZxOrderCount) {
		this.acceptTwZxOrderCount = acceptTwZxOrderCount;
	}

	public Double getPhoneGhServerTime() {
		return phoneGhServerTime;
	}

	public void setPhoneGhServerTime(Double phoneGhServerTime) {
		this.phoneGhServerTime = phoneGhServerTime;
	}
}
