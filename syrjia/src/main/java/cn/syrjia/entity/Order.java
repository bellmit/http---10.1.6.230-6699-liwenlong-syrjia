package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 订单
 * 
 * @pdOid 61ba88d1-eb08-4b1e-9621-1b00a14ebcc5
 */
@Table(name = "t_order")
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单号
	 * 
	 * @pdOid 2f454b91-00ae-452b-afe8-a02f90e7b846
	 */
	@Id(increment = false)
	private String orderNo;
	/**
	 * 订单日期
	 * 
	 * @pdOid cbce079c-29eb-4363-892a-fc8cb2f7a522
	 */
	private Integer createTime;
	/**
	 * 订单价格
	 * 
	 * @pdOid e17ab700-2ad6-443e-a1a0-829407399ce9
	 */
	private Double orderPrice;
	
	/**
	 * 订单实际支付价格
	 * 
	 * @pdOid e17ab700-2ad6-443e-a1a0-829407399ce9
	 */
	private Double receiptsPrice;
	
	/**
	 * 商品价格
	 */
	private Double goodsPrice;
	
	/**
	 * 运费
	 */
	private Double freight;
	
	/**
	 * 订单状态 1-未发货 2-已发货 3-退货中 4已退货 5-交易成功 6 退货成功
	 * 
	 * @pdOid faea8248-b763-4c25-ab7d-91be1b82044a
	 */
	private Integer orderStatus;
	/**
	 * 支付状态 1-等待付款 2-已付款 3-退款中 4-已退款（交易关闭） 5-交易成功 6-订单作废  7-部分退款
	 * 
	 * @pdOid 3fe7967c-04e0-4428-85bd-0f14236418c1
	 */
	private Integer paymentStatus;
	/**
	 * 订单备注
	 * 
	 * @pdOid 731b7c66-d6d6-4d25-9f5d-12ac9a1842c9
	 */
	private String orderNote;
	/**
	 * 交易流水号
	 * 
	 * @pdOid e313e623-acf9-44d7-a8fa-a72526194873
	 */
	private String tradeNo;
	/**
	 * 订单支付时间
	 * 
	 * @pdOid 038fba2f-9911-4996-a695-61fb747720e7
	 */
	private Integer payTime;
	/**
	 * 会员ID
	 * 
	 * @pdOid 58be85b4-f613-4f08-a84a-65eea434b75e
	 */
	private String memberId;
	/**
	 * 状态字段state ， 默认值1 , 1在用 2删除
	 * 
	 * @pdOid 8dc0d74e-fbb4-4fc9-bd2d-2e2f3f8ee964
	 */
	private Integer state;
	/**
	 * 支付微信号
	 * 
	 * @pdOid 56c48522-0802-4d1f-a7b2-972c2f0a74b3
	 */
	private String payWxNo;
	/**
	 * 订单类型 默认1商品
	 * 
	 * @pdOid 631bdbe6-f73b-444c-8d90-2600b3804868
	 */
	private Integer orderType;
	/**
	 * 支付方式 1 支付宝 2 微信 3-APP微信支付
	 * 
	 * @pdOid 712b6487-4a00-459d-b9f5-f27174b3726f
	 */
	private Integer payWay;
	
	/**
	 * 订单生成方式
	 */
	private Integer orderWay;
	/**
	 * 退款原因
	 * 
	 * @pdOid b1ca21cc-52a4-4d0a-ba29-8c138fbcfd32
	 */
	private String refundNote;
	/**
	 * 退款失败原因
	 * 
	 * @pdOid e023a0b6-7b51-4183-a3c9-b60226ceb340
	 */
	private String refundFailNote;

	/**
	 * 收货人姓名
	 */
	private String consignee;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 地区
	 */
	private String area;

	/**
	 * 详细地址
	 */
	private String detailedAddress;

	/**
	 * 类型
	 */
	private Integer type;
	
	/**
	 * 调理记录ID
	 */
	private String recordId;
	
	/**
	 * 拆分前订单号
	 */
	private String sourceOrderNo;
	
	/**
	 * 备用字段1
	 * 
	 * @pdOid bae54b57-99b3-4b5c-8f0b-59d5870f79b6
	 */
	private String rsrvStr1;
	/**
	 * 备用字段2
	 * 
	 * @pdOid a4c1292a-32bd-48c3-9c3f-727d55773a58
	 */
	private String rsrvStr2;

	/**
	 * 备用字段3
	 * 
	 * @pdOid 5a66c251-4002-4b3d-8b30-698af99d91d6
	 */
	private String rsrvStr3;
	
	/**
	 * 就诊人ID
	 */
	private String patientId;
	
	/**
	 * 医生ID
	 */
	private String doctorId;
	
	/**
	 * 主订单号
	 */
	@Column
	private String mainOrderNo;
	
	/**
	 * 是否老的服务
	 */
	@Column
	private Integer isOldServer;
	
	/**
	 * 岗位年龄
	 */
	private Double postage;
	
	/**
	 * 结束时间
	 */
	private Integer endTime;
	
	/**
	 * 所属医珍堂ID
	 */
	private String yztId;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getPayTime() {
		return payTime;
	}

	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPayWxNo() {
		return payWxNo;
	}

	public void setPayWxNo(String payWxNo) {
		this.payWxNo = payWxNo;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public String getRefundNote() {
		return refundNote;
	}

	public void setRefundNote(String refundNote) {
		this.refundNote = refundNote;
	}

	public String getRefundFailNote() {
		return refundFailNote;
	}

	public void setRefundFailNote(String refundFailNote) {
		this.refundFailNote = refundFailNote;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

	public String getRsrvStr1() {
		return rsrvStr1;
	}

	public void setRsrvStr1(String rsrvStr1) {
		this.rsrvStr1 = rsrvStr1;
	}

	public String getRsrvStr2() {
		return rsrvStr2;
	}

	public void setRsrvStr2(String rsrvStr2) {
		this.rsrvStr2 = rsrvStr2;
	}

	public String getRsrvStr3() {
		return rsrvStr3;
	}

	public void setRsrvStr3(String rsrvStr3) {
		this.rsrvStr3 = rsrvStr3;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Double getReceiptsPrice() {
		return receiptsPrice;
	}

	public void setReceiptsPrice(Double receiptsPrice) {
		this.receiptsPrice = receiptsPrice;
	}

	public Integer getOrderWay() {
		return orderWay;
	}

	public void setOrderWay(Integer orderWay) {
		this.orderWay = orderWay;
	}

	public String getSourceOrderNo() {
		return sourceOrderNo;
	}

	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getMainOrderNo() {
		return mainOrderNo;
	}

	public void setMainOrderNo(String mainOrderNo) {
		this.mainOrderNo = mainOrderNo;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getPostage() {
		return postage;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public Integer getIsOldServer() {
		return isOldServer;
	}

	public void setIsOldServer(Integer isOldServer) {
		this.isOldServer = isOldServer;
	}

	public String getYztId() {
		return yztId;
	}

	public void setYztId(String yztId) {
		this.yztId = yztId;
	}
}