package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;



/**
 * 优惠券
 * @author liwenlong
 *
 * 2016-9-8
 */
@Table(name="h_coupon")
public class Coupon implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@Id
	private Integer id;
	
	/**
	 * 优惠券名称
	 */
	@Column
	private String couponName;
	
	/**
	 * 优惠券价格
	 */
	@Column
	private BigDecimal couponPrice;
	
	/**
	 * 大于多少金额才可用
	 */
	@Column(isNull=true)
	private BigDecimal useMoney;
	
	/**
	 * 有效期 （天数）
	 */
	@Column(isNull=true)
	private Integer validity;
	
	/**
	 * 结束时间    如果不为空 结束时间先有效期到达 购物券也自动过期
	 */
	@Column(isNull=true)
	private String endTime;
	
	/**
	 * 是否注册时赠送
	 */
	@Column(isNull=true)
	private Integer isRegister;
	
	/**
	 * 是否立即赠送
	 */
	@Column(isNull=true)
	private Integer isImmediately;
	
	/**
	 * 赠送数量
	 */
	@Column(isNull=true)
	private Integer giveNum;

	
	/**
	 * 开始金额
	 */
	@Column(isNull=true)
	private BigDecimal startMoney;
	
	/**
	 * 结束金额
	 */
	@Column(isNull=true)
	private BigDecimal endMoney;
	
	/**
	 * 状态 0启用 1禁用
	 */
	@Column
	private Integer state;
	
	/**
	 * 备注
	 */
	@Column(isNull=true)
	private String remarks;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(Integer isRegister) {
		this.isRegister = isRegister;
	}

	public BigDecimal getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(BigDecimal startMoney) {
		this.startMoney = startMoney;
	}

	public BigDecimal getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(BigDecimal endMoney) {
		this.endMoney = endMoney;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public Integer getGiveNum() {
		return giveNum;
	}

	public void setGiveNum(Integer giveNum) {
		this.giveNum = giveNum;
	}

	public Integer getIsImmediately() {
		return isImmediately;
	}

	public void setIsImmediately(Integer isImmediately) {
		this.isImmediately = isImmediately;
	}
	
}
