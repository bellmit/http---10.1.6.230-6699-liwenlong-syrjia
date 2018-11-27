package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author liwenlong
 * 角色转换申请审核记录表
 */
@Table(name = "t_audit_record")
public class AuditRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	private Integer id;
	
	/**
	 * 会员ID
	 */
	@Column
	private String openid;
	
	/**
	 * 创建时间
	 */
	@Column(isSort=true)
	private Integer createTime;
	
	/**
	 * 父级ID
	 */
	@Column
	private String pid;
	
	/**
	 * 之前角色ID
	 */
	@Column
	private String lastId;
	
	/**
	 * 申请角色ID
	 */
	@Column
	private String auditId;
	
	/**
	 * 申请角色类别
	 */
	@Column
	private Integer type;
	
	/**
	 * 之前角色类别
	 */
	@Column
	private Integer lastType;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLastType() {
		return lastType;
	}

	public void setLastType(Integer lastType) {
		this.lastType = lastType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
