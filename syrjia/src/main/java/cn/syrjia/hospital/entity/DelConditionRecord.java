package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author fanwei 
 * 调理单记录表
 */
@Table(name = "h_del_conditioning_record")
public class DelConditionRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@Id(UUID=true,increment=false)
	private String id;

	/**
	 * 记录id
	 */
	@Column
	private Integer recordId;

	/**
	 * 处方名称
	 */
	@Column
	private String recipeName;
	
	/**
	 * 影响
	 */
	@Column
	private String effect;
	
	/**
	 * 方法
	 */
	@Column
	private String doMethod;
	
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
	 * 单价
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
	 * 外用/口服 0口服1外用
	 */
	@Column
	private Integer outOrIn;

	/**
	 *  剂型选择
	 */
	@Column
	private String agentType;
	
	/**
	 * 排序
	 */
	@Column(isSort=true,sortType="asc")
	private Integer descent;
	/**
	 * 药品类别
	 */
	@Column
	private String drugType;

	/**
	 * 代理价格
	 */
	@Column
	private Double agentPrice;

	/**
	 * 警告类型 0 正常 1 药量超标 2 药物相克 3 都有
	 */
	@Column
	private Integer waringType;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	
	public String getDrugType() {
		return drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getDoMethod() {
		return doMethod;
	}

	public void setDoMethod(String doMethod) {
		this.doMethod = doMethod;
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
	
	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getOutOrIn() {
		return outOrIn;
	}

	public void setOutOrIn(Integer outOrIn) {
		this.outOrIn = outOrIn;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public Integer getWaringType() {
		return waringType;
	}

	public void setWaringType(Integer waringType) {
		this.waringType = waringType;
	}

	public Double getAgentPrice() {
		return agentPrice;
	}

	public void setAgentPrice(Double agentPrice) {
		this.agentPrice = agentPrice;
	}

	public Integer getDescent() {
		return descent;
	}

	public void setDescent(Integer descent) {
		this.descent = descent;
	}
	
}
