package cn.syrjia.entity;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 订单发票信息
 * ClassName: OmOrderInvoice 
 * @Description: TODO
 * @author 王昭阳
 * @date 2017-6-6
 */
@Table(name="t_om_orderinvoice")
public class OmOrderInvoice {

	/**
	 * 主键
	 */
	@Id
	private Integer id; //id
	
	/**
	 * 订单编号
	 */
	@Column
	private String orderNo;  //订单编号
	@Column
	private String title;  //发票抬头
	@Column
	private Double money;  //发票金额
	@Column
	private String invoicetype;  //发票类型
	@Column
	private String mobile;   //收票人手机
	@Column
	private String taxno; //税号
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getInvoicetype() {
		return invoicetype;
	}
	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTaxno() {
		return taxno;
	}
	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}
	
	
}
