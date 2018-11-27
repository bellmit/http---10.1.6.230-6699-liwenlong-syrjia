package cn.syrjia.entity;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;
@Table(name="t_mm_opinion")
public class MmOpinion implements Serializable{
	@Disable
	private static final long serialVersionUID = 1L;
	//主键
	@Id
	private Integer id;//主键

	/**
	 * 用户id
	 */
	@Column
	private Integer userid ;
	
	/**
	 * 用户名称
	 */
	@Column
	private String username ;
	
	/**
	 * 创建时间
	 */
	@Column
	private String createime ;
	
	/**
	 * fdtime
	 */
	@Column
	private String fdtime ;
	
	/**
	 * 状态
	 */
	@Column
	private String status ;
	
	/**
	 * 删除状态
	 */
	@Column
	private String deleteflag ;
	
	/**
	 * 电话
	 */
	@Column
	private String phone ;
	
	/**
	 * 意见
	 */
	@Column
	private String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCreateime() {
		return createime;
	}
	public void setCreateime(String createime) {
		this.createime = createime;
	}
	public String getFdtime() {
		return fdtime;
	}
	public void setFdtime(String fdtime) {
		this.fdtime = fdtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeleteflag() {
		return deleteflag;
	}
	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
