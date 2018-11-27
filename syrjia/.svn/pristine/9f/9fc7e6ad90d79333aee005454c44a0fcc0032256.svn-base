package cn.syrjia.sales.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;
/**
 * 
 * @author lwl
 * 2018-03-19
 */
@Table(name="t_sales_set")
public class SalesSet implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 助理ID
	 */
	@Column
	private String srId;
	
	/**
	 * 0-否 1-是 医生开启隐身设置后，只有医生的患者可以看到该医生 默认0
	 */
	@Column
	private Integer isHide;

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

	public String getSrId() {
		return srId;
	}

	public void setSrId(String srId) {
		this.srId = srId;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
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

	public Integer getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}
}
