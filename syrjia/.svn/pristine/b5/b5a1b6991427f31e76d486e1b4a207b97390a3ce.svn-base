package cn.syrjia.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 *
 * 2016-6-21
 */
@Table(name="t_sm_userrole")
public class UserRole implements Serializable{


	private static final long serialVersionUID = 1L;


	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	/**
	 * 用户id
	 */
	@Column
	private Integer userid;
	
	/**
	 * 角色id
	 */
	@Column
	private Integer roleid;


}
