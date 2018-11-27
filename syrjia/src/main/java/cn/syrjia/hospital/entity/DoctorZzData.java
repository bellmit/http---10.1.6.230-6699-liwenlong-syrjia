package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_doctor_zz_data")
public class DoctorZzData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	@Column
	private String doctorId;
	
	/**
	 * 时间
	 */
	@Column
	private String zzDate;
	
	/**
	 * 状态
	 */
	@Column
	private Integer zzState;
	
	/**
	 * 医院
	 */
	@Column
	private String infirmaryId;
	
	/**
	 * 价格
	 */
	@Column
	private Double price;
	
	/**
	 * 数量
	 */
	@Column
	private Integer num;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 副本id
	 */
	@Column
	private String repeatId;
	
	/**
	 * isHis
	 */
	@Column
	private Integer isHis;
	
	/**
	 * repeatWay
	 */
	@Column
	private Integer repeatWay;
	
	/**
	 * isYzt
	 */
	@Column
	private Integer isYzt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getZzDate() {
		return zzDate;
	}

	public void setZzDate(String zzDate) {
		this.zzDate = zzDate;
	}

	public Integer getZzState() {
		return zzState;
	}

	public void setZzState(Integer zzState) {
		this.zzState = zzState;
	}

	public String getInfirmaryId() {
		return infirmaryId;
	}

	public void setInfirmaryId(String infirmaryId) {
		this.infirmaryId = infirmaryId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getRepeatId() {
		return repeatId;
	}

	public void setRepeatId(String repeatId) {
		this.repeatId = repeatId;
	}

	public Integer getIsHis() {
		return isHis;
	}

	public void setIsHis(Integer isHis) {
		this.isHis = isHis;
	}

	public Integer getRepeatWay() {
		return repeatWay;
	}

	public void setRepeatWay(Integer repeatWay) {
		this.repeatWay = repeatWay;
	}

	public Integer getIsYzt() {
		return isYzt;
	}

	public void setIsYzt(Integer isYzt) {
		this.isYzt = isYzt;
	}
}
