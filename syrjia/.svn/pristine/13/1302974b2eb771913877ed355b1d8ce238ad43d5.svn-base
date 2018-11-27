package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_follow_history")
public class FollowHistory implements Serializable{

	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 关注id
	 */
	private String followId;
	
	/**
	 * 关注时间
	 */
	private Integer followTime;
	
	/**
	 * 结束时间
	 */
	private Integer endTime;
	
	/**
	 * 类型
	 */
	private Integer type;
	
	/**
	 * 状态
	 */
	private Integer state;
	
	/**
	 * openId
	 */
	private String openId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFollowId() {
		return followId;
	}

	public void setFollowId(String followId) {
		this.followId = followId;
	}

	public Integer getFollowTime() {
		return followTime;
	}

	public void setFollowTime(Integer followTime) {
		this.followTime = followTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
