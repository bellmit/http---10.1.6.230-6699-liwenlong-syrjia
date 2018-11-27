package cn.syrjia.entity;
import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

@Table(name="t_order_type22")
public class copySquare implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键 
	 */
	@Id(increment=false,UUID=true)
	private String id;
	

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 图片1
	 */
	@Column
	private String img1;
	
	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1 = img1;
	}
	/**
	 * 图片2
	 */
	@Column
	private String img2;
	
	public String getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}
	/**
	 * 图片3
	 */
	@Column
	private String img3;
	
	public String getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3 = img3;
	}
	/**
	 * 创建时间
	 */
	@Column
	private Integer created;

	public Integer getCreated() {
		return created;
	}

	public void setCreated(Integer created) {
		this.created = created;
	}
	/**
	 * 就诊人性别
	 */
	@Column
	private Integer sex;

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 就诊人姓名
	 */
	@Column
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 剂型选择
	 */
	@Column
	private Integer agentType;

	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}
	/**
	 * 处方数
	 */
	@Column
	private Integer anagraphCount;

	public Integer getAnagraphCount() {
		return anagraphCount;
	}

	public void setAnagraphCount(Integer anagraphCount) {
		this.anagraphCount = anagraphCount;
	}
	/**
	 * 备注
	 */
	@Column
	private String snote;

	public String getSnote() {
		return snote;
	}

	public void setSnote(String snote) {
		this.snote = snote;
	}
	/**
	 * 电话
	 */
	@Column
	private String tel;

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * 关系
	 */
	@Column
	private String nexus;

	public String getNexus() {
		return nexus;
	}

	public void setNexus(String nexus) {
		this.nexus = nexus;
	}
	
}
