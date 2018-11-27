package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

@Table(name="t_score_goods")
public class ScoreGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id(increment = false, UUID = true)
	private String id;
	/**
	 * 商品名称
	 */
	@Column
	private String name;
	
	/**
	 * 供应商ID
	 */
	@Column
	private String supplierId;
	
	/**
	 * 供应商
	 */
	@Join(id="supplierId",otherId="id",table="t_supplier",otherName="name",joinType="left")
	private String supplier;
	
	/**
	 * 标识
	 */
	@Join(id="identificationId",otherId="id",table="t_score_goods_identification",otherName="name",joinType="left")
	private String identification;
	
	/**
	 * 商品类型
	 */
	@Column
	private String goodsTypeId;
	
	/**
	 * 商品类型
	 */
	@Join(id="goodsTypeId",otherId="id",table="t_score_goods_type",otherName="name")
	private String goodsType;
	
	/**
	 * 商品价格
	 */
	@Column
	private Double price;


	/**
	 * 优惠价格
	 */
	@Column
	private Double newPrice;
	
	/**
	 * 商品描述
	 */
	@Column
	private String description;
	
	/**
	 * 商品图片
	 */
	@Column
	private String picture;
	
	/**
	 * 图片物理地址
	 */
	private String pictureRiskPath;
	
	/**
	 * 状态 1启用 2禁用 3删除
	 */
	@Column
	private Integer state;
	
	/**
	 * 标识ID
	 */
	private String identificationId;
	
	/**
	 * 编辑器
	 */
	private String remark;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 有效期 天数
	 */
	@Column(isNull=true)
	private Integer validityTime;
	
	/**
	 * 是否兑换积分
	 */
	private Integer isIntegral;
	
	/**
	 * 积分
	 */
	@Column(isNull=true)
	private Integer integral;
	
	/**
	 * 是否发货 1需要 0不需要 
	 */
	private Integer isShipping;
	
	/**
	 * 是否推荐
	 */
	private Integer isRecommend;
	
	/**
	 * 排序
	 */
	private Integer rank;
	
	@Join(id="id",joinType="left",otherId="goodsId",table="t_score_goods_activity",otherName="price")
	private Double activityPrice;

	/**
	 * 库存
	 */
	private Integer stock;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public Double getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(String goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPictureRiskPath() {
		return pictureRiskPath;
	}

	public void setPictureRiskPath(String pictureRiskPath) {
		this.pictureRiskPath = pictureRiskPath;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getIdentificationId() {
		return identificationId;
	}

	public void setIdentificationId(String identificationId) {
		this.identificationId = identificationId;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(Double activityPrice) {
		this.activityPrice = activityPrice;
	}

	public Integer getIsIntegral() {
		return isIntegral;
	}

	public void setIsIntegral(Integer isIntegral) {
		this.isIntegral = isIntegral;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getIsShipping() {
		return isShipping;
	}

	public void setIsShipping(Integer isShipping) {
		this.isShipping = isShipping;
	}

	public Integer getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(Integer validityTime) {
		this.validityTime = validityTime;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
}
