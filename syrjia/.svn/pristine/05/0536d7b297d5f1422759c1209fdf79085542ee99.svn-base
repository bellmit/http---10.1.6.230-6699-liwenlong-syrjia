package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author liwenlong 调理记录表
 */
@Table(name = "t_recipe_record")
public class RecipeRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false)
	private String recordId;

	/**
	 * 医生id
	 */
	@Column
	private String doctorId;

	/**
	 * 订单号
	 */
	@Column
	private String orderNo;

	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	/**
	 * 药品总价
	 */
	@Column
	private Double price;

	/**
	 * 是否有
	 */
	@Column
	private Integer isHas;

	/**
	 * 建议id
	 */
	@Column
	private String adviseId;

	/**
	 * 建议
	 */
	@Column
	private String advise;

	/**
	 * 建议名称
	 */
	@Column
	private String adviseName;
	/**
	 * 订单状态 0作废 1正常
	 */
	@Column
	private Integer state;

	/**
	 * 诊断
	 */
	@Column
	private String otherDia;

	/**
	 * wzPrice
	 */
	@Column
	private Double wzPrice;

	/**
	 * 访问时间
	 */
	@Column
	private Integer visitTime;

	/**
	 * 摘要
	 */
	@Column
	private String remarks;

	//是否患者可见 1-是 0-否
	@Column
	private Integer isSendUser;
	
	//加工费基础费用
	@Column
	private Double jgBasicPrice;
	
	//起算付数
	@Column
	private Integer jgBasicDose;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getIsHas() {
		return isHas;
	}

	public void setIsHas(Integer isHas) {
		this.isHas = isHas;
	}

	public String getAdviseId() {
		return adviseId;
	}

	public void setAdviseId(String adviseId) {
		this.adviseId = adviseId;
	}

	public String getAdvise() {
		return advise;
	}

	public void setAdvise(String advise) {
		this.advise = advise;
	}

	public String getAdviseName() {
		return adviseName;
	}

	public void setAdviseName(String adviseName) {
		this.adviseName = adviseName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getOtherDia() {
		return otherDia;
	}

	public void setOtherDia(String otherDia) {
		this.otherDia = otherDia;
	}

	public Double getWzPrice() {
		return wzPrice;
	}

	public void setWzPrice(Double wzPrice) {
		this.wzPrice = wzPrice;
	}

	public Integer getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Integer visitTime) {
		this.visitTime = visitTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getIsSendUser() {
		return isSendUser;
	}

	public void setIsSendUser(Integer isSendUser) {
		this.isSendUser = isSendUser;
	}

	public Double getJgBasicPrice() {
		return jgBasicPrice;
	}

	public void setJgBasicPrice(Double jgBasicPrice) {
		this.jgBasicPrice = jgBasicPrice;
	}

	public Integer getJgBasicDose() {
		return jgBasicDose;
	}

	public void setJgBasicDose(Integer jgBasicDose) {
		this.jgBasicDose = jgBasicDose;
	}

}
