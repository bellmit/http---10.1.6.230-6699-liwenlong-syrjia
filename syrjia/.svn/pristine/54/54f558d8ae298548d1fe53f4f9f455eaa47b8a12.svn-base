package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 
 * @author liwenlong 图片表
 */
@Table(name = "t_piclib")
public class Piclib implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false)
	private String picId; // 自增ID，主键

	@Column
	private String goodId; // 商品ID
	
	@Column
	private Integer adviceId; // 反馈ID

	@Column
	private String picAddr; // 图片物理地址
	
	@Column
	private String picPathUrl; // 图片访问地址

	@Column(value="10")
	private String status; // 状态 10-在用 30-删除

	@Column
	private Integer statusDate; // 生成时间

	@Column
	private String rsrvStr1; // 备用字段1

	@Column
	private String rsrvStr2; // 备用字段2

	@Column
	private String rsrvStr3; // 备用字段3
	
	@Column
	private Integer picType;//图片标识 1-住院单 2-影像及生化类图片 3-诊断资料 4-诊断证明 5-诊断报告 6-生化类 7-病理报告

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getPicAddr() {
		return picAddr;
	}

	public void setPicAddr(String picAddr) {
		this.picAddr = picAddr;
	}

	public String getPicPathUrl() {
		return picPathUrl;
	}

	public void setPicPathUrl(String picPathUrl) {
		this.picPathUrl = picPathUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Integer statusDate) {
		this.statusDate = statusDate;
	}

	public String getRsrvStr1() {
		return rsrvStr1;
	}

	public void setRsrvStr1(String rsrvStr1) {
		this.rsrvStr1 = rsrvStr1;
	}

	public String getRsrvStr2() {
		return rsrvStr2;
	}

	public void setRsrvStr2(String rsrvStr2) {
		this.rsrvStr2 = rsrvStr2;
	}

	public String getRsrvStr3() {
		return rsrvStr3;
	}

	public void setRsrvStr3(String rsrvStr3) {
		this.rsrvStr3 = rsrvStr3;
	}

	public Integer getAdviceId() {
		return adviceId;
	}

	public void setAdviceId(Integer adviceId) {
		this.adviceId = adviceId;
	}

	public Integer getPicType() {
		return picType;
	}

	public void setPicType(Integer picType) {
		this.picType = picType;
	}

}
