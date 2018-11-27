package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_shopcart")
public class ShopCart implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 会员ID
	 */
	private String memberId;
	
	/**
	 * 商品ID
	 */
	private String goodsId;
	
	/**
	 * 购买数量
	 */
	private Integer buyCount;
	
	/**
	 * 购物车类型 1手动加入购物车 2自动加入不显示
	 */
	private Integer type;
	
	/**
	 * 价格数id
	 */
	private String priceNumId;
	
	/**
	 * 创建时间
	 */
	private Integer createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getPriceNumId() {
		return priceNumId;
	}

	public void setPriceNumId(String priceNumId) {
		this.priceNumId = priceNumId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
