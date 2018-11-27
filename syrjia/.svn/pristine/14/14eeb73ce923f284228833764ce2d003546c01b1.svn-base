package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 收货地址信息
 * 
 * @pdOid 69dc7fa9-fa92-43e4-9ba7-349ede5ee8ad
 */
@Table(name = "t_shipping_address")
public class ShippingAddress implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键Id
	 * 
	 * @pdOid f1ccd860-178e-492f-b73f-6482310ba0c5
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 收货人姓名
	 * 
	 * @pdOid 2ebff016-cc84-49c8-8279-42442d2e8155
	 */
	private String consignee;
	/**
	 * 收货人联系电话
	 * 
	 * @pdOid e3bcd95a-c3cf-438c-aeb3-611c2e3b6d1a
	 */
	private String phone;
	/**
	 * 省
	 * 
	 * @pdOid 11d95f40-5b16-4ed4-81c7-eb62bc7dc3a2
	 */
	private String province;
	/**
	 * 城市
	 * 
	 * @pdOid 4cc032a2-da97-4bca-aa75-6877c35ffb8a
	 */
	private String city;
	/**
	 * 地区
	 * 
	 * @pdOid a54863de-6092-49b0-8381-883ddc5c5041
	 */
	private String area;
	/**
	 * 详细地址
	 * 
	 * @pdOid 7f8002d3-9b9c-468a-8c4b-b66360c17c38
	 */
	private String detailedAddress;
	/**
	 * 创建时间
	 * 
	 * @pdOid ff6149d4-cf86-4f8e-8eb2-dfbfd3cad91b
	 */
	private Integer createTime;
	/**
	 * 状态 默认1 1正常 2删除 3默认地址
	 * 
	 * @pdOid 80a60b38-9b81-4b87-a5a3-f19cf9485c26
	 */
	private Integer state;
	
	private String memberId;//会员id
	
	/**
	 * 是否默认地址
	 */
	private Boolean isDefault;
	
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}