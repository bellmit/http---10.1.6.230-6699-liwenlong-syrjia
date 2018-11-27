package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_order_activity")
public class OrderActivity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 活动id
	 */
	private String activityId;
	
	/**
	 * 订单详情id
	 */
	private String orderDetialId;
	
	/**
	 * 商品id
	 */
	private String goodsId;
	
	/**
	 * 活动类型
	 */
	private Integer activityType;
	
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getOrderDetialId() {
		return orderDetialId;
	}

	public void setOrderDetialId(String orderDetialId) {
		this.orderDetialId = orderDetialId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
}
