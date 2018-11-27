package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 直播信息表
 * @author lwl
 *
 */
@Table(name="t_live")
public class Live implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 直播编号
	 */
	@Id(increment=false)
	private String liveNo;
	
	/**
	 * 直播室名称
	 */
	@Column
	private String name;
	
	/**
	 * 推流APPName
	 */
	@Column
	private String appName;
	
	/**
	 * 推流streamName
	 */
	@Column
	private String streamName;

	/**
	 * 直播开始时间
	 */
	@Column
	private String startTime;
	
	/**
	 * 直播结束时间
	 */
	@Column
	private String endTime;
	
	/**
	 * 直播邀请码
	 */
	@Column
	private String inviteCode;
	
	/**
	 * 直播信息描述
	 */
	@Column
	private String remarks;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 创建人
	 */
	@Column
	private String createUserId;

	public String getLiveNo() {
		return liveNo;
	}

	public void setLiveNo(String liveNo) {
		this.liveNo = liveNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
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

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
}
