package cn.syrjia.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author  
 * 意见反馈表
 */
@Table(name = "t_member_advise")
public class UserAdvise implements Serializable {
	@Disable
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	private Integer adviceId;
	
	@Column
	private String comment; //意见内容 
	
	/**
	 * openid
	 */
	@Column
	private String openid; 
	
	/**
	 * 用户id
	 */
	@Column
	private String userid; 
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime; 
	
	/**
	 * 0-未处理 1-已处理 2-已删除
	 */
	@Column
	private Integer state;
	
	/**
	 * 电话
	 */
	@Column
	private String phone;
	
	@Column
	private String replyContent; //回复内容
	
	/**
	 * 图片列表
	 */
	private List<Piclib> piclist;

	public Integer getAdviceId() {
		return adviceId;
	}

	public void setAdviceId(Integer adviceId) {
		this.adviceId = adviceId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public List<Piclib> getPiclist() {
		return piclist;
	}

	public void setPiclist(List<Piclib> piclist) {
		this.piclist = piclist;
	}
	
}
