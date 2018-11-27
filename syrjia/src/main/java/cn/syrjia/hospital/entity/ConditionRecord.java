package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * 调理单记录表
 */
@Table(name = "t_conditioning_record")
public class ConditionRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id(UUID = true, increment = false)
	private String id;

	/**
	 * 记录的id
	 */
	@Column
	private String recordId;

	/**
	 * 处方名称
	 */
	@Column
	private String recipeName;

	/**
	 * 访问时间
	 */
	@Column
	private Integer visitTime;

	/**
	 * 禁忌
	 */
	@Column
	private String taboo;

	/**
	 * 警告
	 */
	@Column
	private String waring;
	
	/**
	 * 调理单
	 */
	@Column
	private Integer conditionOrder;

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
	 * 是否隐藏克数 0-否 1-是
	 */
	@Column
	private Integer isHideGram;

	/**
	 * 单价(单付价格)
	 */
	@Column
	private Double unitPrice;

	/**
	 * 总剂量
	 */
	@Column
	private Integer dose;

	/**
	 * 一日几次
	 */
	@Column
	private Integer useCount;

	/**
	 * 外用/口服
	 */
	@Column
	private String outOrIn;

	/**
	 * 剂型选择
	 */
	@Column
	private Integer agentType;

	/**
	 * 加工费
	 */
	@Column
	private Double jgPrice;

	/**
	 * 药费总计
	 */
	@Column
	private Double drugPrice;

	/**
	 * 警告类型 0 正常 1 药量超标 2 药物相克 3 都有
	 */
	@Column
	private Integer waringType;
	
	/**
	 *  0-无 1-打粉 3-膏方 加工服务
	 */
	@Column
	private Integer jgServerType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public Integer getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Integer visitTime) {
		this.visitTime = visitTime;
	}

	public String getTaboo() {
		return taboo;
	}

	public void setTaboo(String taboo) {
		this.taboo = taboo;
	}

	public String getWaring() {
		return waring;
	}

	public void setWaring(String waring) {
		this.waring = waring;
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

	public Integer getIsHideGram() {
		return isHideGram;
	}

	public void setIsHideGram(Integer isHideGram) {
		this.isHideGram = isHideGram;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	public String getOutOrIn() {
		return outOrIn;
	}

	public void setOutOrIn(String outOrIn) {
		this.outOrIn = outOrIn;
	}

	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}

	public Double getJgPrice() {
		return jgPrice;
	}

	public void setJgPrice(Double jgPrice) {
		this.jgPrice = jgPrice;
	}

	public Double getDrugPrice() {
		return drugPrice;
	}

	public void setDrugPrice(Double drugPrice) {
		this.drugPrice = drugPrice;
	}

	public Integer getWaringType() {
		return waringType;
	}

	public void setWaringType(Integer waringType) {
		this.waringType = waringType;
	}

	public Integer getJgServerType() {
		return jgServerType;
	}

	public void setJgServerType(Integer jgServerType) {
		this.jgServerType = jgServerType;
	}

	public Integer getConditionOrder() {
		return conditionOrder;
	}

	public void setConditionOrder(Integer conditionOrder) {
		this.conditionOrder = conditionOrder;
	}

}
