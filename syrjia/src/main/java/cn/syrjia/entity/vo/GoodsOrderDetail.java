package cn.syrjia.entity.vo;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 订单详情
 * 
 * @pdOid 34ab9f68-6ccb-49ae-ae01-fa326b5f92ca
 */
@Table(name = "t_order_detail")
public class GoodsOrderDetail implements Serializable{


	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 * 
	 * @pdOid 70b05467-6032-430a-9cfc-6e5372b26314
	 */
	@Id(increment=false,UUID=true)
	private String id;
	/**
	 * 订单号
	 * 
	 * @pdOid 81345270-fae9-421b-b703-6fb8ca826841
	 */
	private String orderNo;
	/**
	 * 商品ID
	 * 
	 * @pdOid 059a36eb-170f-46c2-acfb-455657cb83f3
	 */
	private String goodsId;
	
	/**
	 * 商品价格数id
	 */
	private String goodsPriceNumId;
	/**
	 * 商品单价
	 * 
	 * @pdOid 76d402b1-c9d0-4f78-9e0f-72708fcfde88
	 */
	private Double goodsPrice;
	/**
	 * 商品数量
	 * 
	 * @pdOid 8fb53ff2-4526-47f9-8ffd-811df5b65e71
	 */
	private Integer goodsNum;
	/**
	 * 商品原价（单价）
	 * 
	 * @pdOid 522d1083-c0d8-4c10-b735-5e310a0bff96
	 */
	private Double goodsOriginalPrice;
	/**
	 * 备注
	 * 
	 * @pdOid 772a7dec-b397-47c5-969a-c0e96dd421ed
	 */
	private String remarks;
	/**
	 * 创建时间
	 * 
	 * @pdOid acb7a3f0-2003-4985-9f83-d8e3a0f01af1
	 */
	private Integer createTime;

	/**
	 * 商品总价
	 * 
	 * @pdOid c9c9e4ab-4775-446e-94d2-d815aad1fb23
	 */
	public Double goodsTotal;
	
	/**
	 * 支付状态
	 */
	private Integer paymentStatus;
	
	/**
	 * 1商品
	 */
	private Integer type;
	
	/**
	 * 医生推荐商品存入医生ID
	 */
	private String doctorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Double getGoodsOriginalPrice() {
		return goodsOriginalPrice;
	}

	public void setGoodsOriginalPrice(Double goodsOriginalPrice) {
		this.goodsOriginalPrice = goodsOriginalPrice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Double getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(Double goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getGoodsPriceNumId() {
		return goodsPriceNumId;
	}

	public void setGoodsPriceNumId(String goodsPriceNumId) {
		this.goodsPriceNumId = goodsPriceNumId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
}