package cn.syrjia.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 推送消息 ClassName: MmPush
 * 
 * @Description: TODO
 */
@Table(name = "t_mm_push")
public class Push {

	@Id(increment=false,UUID=true)
	private String id;// id

	@Column
	private String title;// 标题

	@Column
	private String comment;// 内容

	@Column
	private String userid;// 用户id

	@Column
	private String token;// token

	@Column
	private Integer createime;// 创建时间

	@Column
	private Integer deleteflag;// 禁用标识

	@Column
	private String type;// 推送类型

	@Column
	private Integer pushType;// 0-APP 1-微信

	/**
	 * openid
	 */
	@Column
	private String openid;
	
	/**
	 * 推送结果
	 */
	@Column
	private String pushResult;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getCreateime() {
		return createime;
	}

	public void setCreateime(Integer createime) {
		this.createime = createime;
	}

	public Integer getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPushResult() {
		return pushResult;
	}

	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}
}
