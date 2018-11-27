package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

@Table(name="t_goods_type")
public class GoodsType implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 咖啡馆
	 */
	@Column
	private String supplierId;

	/**
	 * 咖啡馆名称
	 */
	@Join(id="supplierId",otherId="id",table="t_supplier",otherName="name",joinType="left")
	private String supplierName;
	
	/**
	 * 类型名称
	 */
	@Column
	private String name;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 通用商品分类Id 即为本表id
	 */
	private String generalId;
	
	/**
	 * 状态 1启用2禁用3删除
	 */
	@Column
	private Integer state;
	
	/**
	 * 排序
	 */
	private Integer rank;

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

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getGeneralId() {
		return generalId;
	}

	public void setGeneralId(String generalId) {
		this.generalId = generalId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
