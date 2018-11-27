package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Date;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 日志
 */
@Table(name="t_log")
public class Log implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Id
	private Integer id;
	
	/**
	 * 模块
	 */
	@Column
	private String modular;
	
	/**
	 * 内容
	 */
	@Column
	private String content;
	
	/**
	 * 操作人
	 */
	@Column
	private String userId;
	
	/**
	 * 操作IP
	 */
	@Column
	private String ip;
	
	/**
	 * 操作类型
	 */
	@Column
	private String type;
	
	
	/**
	 * 状态 0成功 1失败
	 */
	@Column
	private Integer state;
	
	/**
	 * 操作人
	 */
	@Join(table="t_member",id="userId",otherId="id",otherName="realName")
	private String userName;
	
	/**
	 * 操作时间
	 */
	@Column
	private String creatTime;
	
	/**
	 * 详情
	 */
	@Column
	private String details;
	
	/**
	 * 开始时间 用于查询
	 */
	@Date(field="creatTime",symbol=">=")
	private String startTime;
	
	/**
	 * 结束时间 用于查询
	 */
	@Date(field="creatTime",symbol="<=")
	private String endTime;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModular() {
		return modular;
	}

	public void setModular(String modular) {
		this.modular = modular;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreatTime() {
		return creatTime;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", modular=" + modular + ", content="
				+ content + ", userId=" + userId + ", ip=" + ip + ", type="
				+ type + ", state=" + state + ", userName=" + userName
				+ ", creatTime=" + creatTime + "]";
	}
}
