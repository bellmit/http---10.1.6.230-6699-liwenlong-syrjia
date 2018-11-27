package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_user_clock_record")
public class UserClockRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	private Integer id;

	/**
	 * 用户id
	 */
	@Column
	private String userid;

	/**
	 * openid
	 */
	@Column
	private String openid;

	/**
	 * 状态
	 */
	@Column
	private Integer state;

	/**
	 * 工作时间
	 */
	@Column
	private String workTime;

	/**
	 * 关闭时间
	 */
	private Integer clockTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public Integer getClockTime() {
		return clockTime;
	}

	public void setClockTime(Integer clockTime) {
		this.clockTime = clockTime;
	}

}
