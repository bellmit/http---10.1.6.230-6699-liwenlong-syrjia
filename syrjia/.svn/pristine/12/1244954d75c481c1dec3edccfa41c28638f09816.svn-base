package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Date;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


@Table(name = "h_weixinuser_coupon")
public class WeiXinUserCoupon implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 用户Id
	 */
	@Column
	private String openId;
	
	/**
	 * 购物券Id
	 */
	@Column
	private Integer couponId;
	
	/**
	 * 是否过期 0已过期 1未过期
	 */
	@Column
	private Integer isOverdue;
	
	/**
	 * 优惠券名称
	 */
	@Column
	private String couponName;
	
	/**
	 * 优惠券金额
	 */
	@Column
	private BigDecimal couponPrice;
	
	/**
	 * 使用金额
	 */
	@Column
	private BigDecimal useMoney;
	
	/**
	 * 说明
	 */
	@Column
	private String remarks;
	
	/**
	 * 状态 0未使用 1已使用 3已失效
	 */
	@Column
	private Integer state;
	
	/**
	 * 开始时间
	 */
	@Date(symbol="<=",field="startTime")
	private String startTime;
	
	/**
	 * 过期时间
	 */
	@Column
	private Integer overdueTime;
	
	/**
	 * 类型0医生服务 1药品
	 */
	@Column
	private Integer type;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Integer getIsOverdue() {
		return isOverdue;
	}

	public void setIsOverdue(Integer isOverdue) {
		this.isOverdue = isOverdue;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(Integer overdueTime) {
		this.overdueTime = overdueTime;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public BigDecimal getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(BigDecimal couponPrice) {
		this.couponPrice = couponPrice;
	}

	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
