package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 医生邀请类
 * @author miaow
 *
 */

@Table(name="t_doctor_doctor")
public class DoctorInvitation  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String uuid;
	
	/**
	 * 推荐医生id
	 */
	@Column
	private String oldDoctorId;
	
	/**
	 * 新医生id
	 */
	@Column
	private String newDoctorId;
	
	/**
	 * 推荐时间
	 */
	@Column
	private Integer creatTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOldDoctorID() {
		return oldDoctorId;
	}



	public String getOldDoctorId() {
		return oldDoctorId;
	}

	public void setOldDoctorId(String oldDoctorId) {
		this.oldDoctorId = oldDoctorId;
	}

	public String getNewDoctorId() {
		return newDoctorId;
	}

	public void setNewDoctorId(String newDoctorId) {
		this.newDoctorId = newDoctorId;
	}

	public Integer getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Integer creatTime) {
		this.creatTime = creatTime;
	}
	
	
}
