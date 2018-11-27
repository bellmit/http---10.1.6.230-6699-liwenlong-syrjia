package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_error")
public class RefundError implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误日志id
	 */
	@Id(increment=false)
	private String errorlogId;       /*ID*/
	
	/**
	 * 拒绝退款原因
	 */
	@Column
	private String errorlogNote; 
	
	/**
	 * 错误日志时间
	 */
	@Column
	private Integer errorlogDate;   
	
	@Column
	private String errorType; /*1-驳回理由 0-取消申请理由 2-申请退款理由*/
	
	/**
	 * rsrvStr1
	 */
	private String rsrvStr1;   
	
	/**
	 * rsrvStr2
	 */
	private String rsrvStr2;
	
	/**
	 * rsrvStr3
	 */
	private String rsrvStr3;

	public String getErrorlogId() {
		return errorlogId;
	}

	public void setErrorlogId(String errorlogId) {
		this.errorlogId = errorlogId;
	}

	public String getErrorlogNote() {
		return errorlogNote;
	}

	public void setErrorlogNote(String errorlogNote) {
		this.errorlogNote = errorlogNote;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getErrorlogDate() {
		return errorlogDate;
	}

	public void setErrorlogDate(Integer errorlogDate) {
		this.errorlogDate = errorlogDate;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
}
