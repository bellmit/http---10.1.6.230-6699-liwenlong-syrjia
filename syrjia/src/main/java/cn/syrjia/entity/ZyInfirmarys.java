package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_zy_infirmarys")
public class ZyInfirmarys implements Serializable {

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
	 * 医院id
	 */
	private String infirmaryId;
	
	/**
	 * 医院名称
	 */
	private String infirmaryName;

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

	public String getInfirmaryId() {
		return infirmaryId;
	}

	public void setInfirmaryId(String infirmaryId) {
		this.infirmaryId = infirmaryId;
	}

	public String getInfirmaryName() {
		return infirmaryName;
	}

	public void setInfirmaryName(String infirmaryName) {
		this.infirmaryName = infirmaryName;
	}

}
