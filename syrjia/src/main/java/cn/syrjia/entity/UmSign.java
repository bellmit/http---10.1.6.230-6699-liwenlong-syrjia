package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;
@Table(name="t_um_sign")
public class UmSign implements Serializable{
	
	@Disable
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment=false)
	private Integer id;//主键ID
	
	/**
	 * 用户id
	 */
	@Column
	private Integer userid;
	
	/**
	 * 签名时间
	 */
	@Column
	private String signdate;
	
	/**
	 * 签名时间
	 */
	@Column
	private String signtime;
	
	/**
	 * 经度
	 */
	@Column
	private String longitude;
	
	/**
	 * 维度
	 */
	@Column
	private String latitude;
	
	/**
	 * 地址
	 */
	@Column
	private String address;
	
	/**
	 * wifimac
	 */
	@Column
	private String wifimac;
	
	/**
	 * 删除标志
	 */
	@Column
	private String deleteflag;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	public String getSigntime() {
		return signtime;
	}
	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWifimac() {
		return wifimac;
	}
	public void setWifimac(String wifimac) {
		this.wifimac = wifimac;
	}
	public String getDeleteflag() {
		return deleteflag;
	}
	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}
	
	
	
	
	
	

}