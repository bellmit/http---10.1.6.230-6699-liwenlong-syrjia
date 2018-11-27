package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_eva_banner")
public class EvaBanner implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 图片路径
	 */
	private String imgUrl;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 价格
	 */
	private Double price;
	
	/**
	 * 排序
	 */
	private Integer sort;
	
	/**
	 * 状态
	 */
	private Integer state;
	
	/**
	 * 创建时间
	 */
	private Integer createTime;
	
	/**
	 * 类型
	 */
	private Integer type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
