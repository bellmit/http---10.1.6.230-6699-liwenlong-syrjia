package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_send_model")
public class SendMode implements Serializable{

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
	 * 标题
	 */
	@Column
	private String title;
	/**
	 * 内容
	 */
	@Column
	private String content;
	/**
	 * 手机唯一识别码
	 */
	@Column
	private String type;
	/**
	 * 
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
	 * 关联用户Id
	 */
	private String relevantUserId;
	
	/**
	 * 是否已读
	 */
	private Integer read;
	
	/**
	 * 应用版本信息
	 */
	@Column
	private String version;
	/**
	 * 应用版本信息
	 */
	@Column
	private String creationTime;
	
	@Column
	private String data;
	
	@Column
	private Integer isShow;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getRelevantUserId() {
		return relevantUserId;
	}
	public void setRelevantUserId(String relevantUserId) {
		this.relevantUserId = relevantUserId;
	}
	public Integer getRead() {
		return read;
	}
	public void setRead(Integer read) {
		this.read = read;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
}
