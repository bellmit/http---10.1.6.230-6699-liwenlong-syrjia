package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_login_log")
public class LoginLog implements Serializable{

	@Disable
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@Id(increment=false)
	private String id;
	
	/**
	 * 用户id
	 */
	@Column
	private String userId;
	/**
	 * 用户id
	 */
	@Column
	private String loginName;
	/**
	 * 手机唯一识别码
	 */
	@Column
	private String token;
	/**
	 * 手机类型 苹果、安卓
	 */
	@Column
	private String ostype;
	/**
	 * 手机型号  小米、华为等
	 */
	@Column
	private String model;
	/**
	 * 应用版本信息
	 */
	@Column
	private String version;
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	/**
	 * 状态 默认1  1登陆 2退出
	 */
	@Column
	private Integer type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getOstype() {
		return ostype;
	}
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
