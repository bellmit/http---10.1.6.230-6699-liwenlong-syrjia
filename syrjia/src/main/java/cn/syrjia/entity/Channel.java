package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

@Table(name="t_channel")
public class Channel implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(UUID=true,increment=false)
	private String id;

	/**
	 * 渠道名称
	 */
	@Column
	private String channelName;
	
	/**
	 * 父id
	 */
	@Column
	private String pid;
	
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
	 * 创建用户
	 */
	@Column
	private String createUser;
	
	/**
	 * qrUrl
	 */
	@Column
	private String qrUrl;
	
	/**
	 * 本地地址
	 */
	@Column
	private String localQrUrl;
	
	/**
	 * 用户id
	 */
	@Column
	private String userId;
	
	/**
	 * 医生id
	 */
	@Column
	private String docId;
	
	/**
	 * -1-根节点  0-销售代表  1-分销商 2-医生
	 */
	@Column
	private Integer type;
	
	/**
	 *  厚得馆 0-否 1-是 
	 */
	@Column
	private Integer isHdg;
	
	/**
	 *  药品 0-否 1-是 
	 */
	@Column
	private Integer isDrug;
	
	/**
	 *  服务 0-否 1-是 
	 */
	@Column
	private Integer isServer;
	
	/**
	 *  活动 0-否 1-是 
	 */
	@Column
	private Integer isActivity;
	
	/**
	 *  就诊 0-否 1-是 
	 */
	@Column
	private Integer isVisit;
	
	/**
	 *  是否默认 0-否 1-是 
	 */
	@Column
	private Integer isDefault;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLocalQrUrl() {
		return localQrUrl;
	}

	public void setLocalQrUrl(String localQrUrl) {
		this.localQrUrl = localQrUrl;
	}

	public Integer getIsHdg() {
		return isHdg;
	}

	public void setIsHdg(Integer isHdg) {
		this.isHdg = isHdg;
	}

	public Integer getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(Integer isDrug) {
		this.isDrug = isDrug;
	}

	public Integer getIsServer() {
		return isServer;
	}

	public void setIsServer(Integer isServer) {
		this.isServer = isServer;
	}

	public Integer getIsActivity() {
		return isActivity;
	}

	public void setIsActivity(Integer isActivity) {
		this.isActivity = isActivity;
	}

	public Integer getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(Integer isVisit) {
		this.isVisit = isVisit;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

}
