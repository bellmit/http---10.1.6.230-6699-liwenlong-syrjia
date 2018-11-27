package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;
/**
 * 我的锦旗实体
 * @author lwl
 *
 */
@Table(name="t_my_eva_banner")
public class MyEvaBanner implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 用户id
	 */
	@Column
	private String memberId;
	
	/**
	 * bannerId
	 */
	@Column
	private String evaBannerId;
	
	/**
	 * 价格
	 */
	@Column
	private Double price;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * evaId
	 */
	@Column
	private String evaId;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	/**
	 * 类型
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getEvaBannerId() {
		return evaBannerId;
	}

	public void setEvaBannerId(String evaBannerId) {
		this.evaBannerId = evaBannerId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getEvaId() {
		return evaId;
	}

	public void setEvaId(String evaId) {
		this.evaId = evaId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	
}
