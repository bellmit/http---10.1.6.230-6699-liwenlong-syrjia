package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.OneToMany;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 用户收藏
 */
@Table(name="t_user_keep")
public class UserKeep implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id(increment = false, UUID = true)
	private String id;
	
	/**
	 * 用户Id
	 */
	@Column
	private String openid;
	
	/**
	 * 医生id
	 */
	@Column
	private String doctorId;
	
	/**
	 * 关注人ID
	 */
	@Column
	private String memberId;
	
	/**
	 * 被关注ID
	 */
	@Column String goodsId;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 类型
	 */
	@Column
	private String type; //0-医生 1-话题

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
