package cn.syrjia.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 菜单功能
 */
@Table(name="h_menu")
public class Menu implements Serializable{

	@Disable
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	private Integer id;
	
	/**
	 * 菜单名称
	 */
	@Column
	private String name;
	
	/**
	 * 访问地址
	 */
	@Column
	private String url;
	
	/**
	 * 上级id
	 */
	@Column
	private Integer pid;
	
	/**
	 * 备注
	 */
	@Column
	private String remarks;
	
	/**
	 * 0根菜单 1二级菜单 2功能
	 */
	@Column
	private Integer type;
	
	/**
	 * 权限编码
	 */
	@Column
	private String code;
	
	/**
	 * 0启用 1禁用
	 */
	@Column
	private Integer state;
	
	/**
	 * 样式
	 */
	@Column
	private String style;
	
	
	/**
	 * 排序
	 */
	@Column(name="_sort")
	private Integer sort;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
