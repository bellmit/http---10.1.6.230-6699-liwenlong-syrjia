package cn.syrjia.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;
@Table(name="t_code")
public class Tcode {
	@Disable
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	private Integer id;//id
	
	/**
	 * 电话
	 */
	@Column
	private String phone;
	
	/**
	 * code
	 */
	@Column
	private String code;
	
	/**
	 * 创建时间
	 */
	@Column
	private String createTime;
	
	/**
	 * 类型
	 */
	@Column
	private Integer type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
