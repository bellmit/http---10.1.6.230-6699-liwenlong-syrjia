package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 拍照抓药实体
 * @author lwl
 */
@Table(name="t_photo_medical_record")
public class PhotoMedicalRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 用户id
	 */
	@Column
	private String memberId;
	
	/**
	 * 图片地址
	 */
	@Column
	private String imgUrl;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	/**
	 * 数量
	 */
	@Column
	private Integer num;
	
	/**
	 * 摘要
	 */
	@Column
	private String remarks;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 歌剧院用户id
	 */
	@Column
	private String operaUserId;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOperaUserId() {
		return operaUserId;
	}

	public void setOperaUserId(String operaUserId) {
		this.operaUserId = operaUserId;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	} 
}
