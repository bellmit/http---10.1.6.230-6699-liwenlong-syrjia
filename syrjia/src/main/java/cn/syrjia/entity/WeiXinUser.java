package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 微信会员
 * 
 * @pdOid 3bb60eb5-6027-411d-b773-5afe14ab6a6b
 */
@Table(name = "t_weixin_user")
public class WeiXinUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * openId
	 * 
	 * @pdOid 2e1476a5-828e-4af8-97dd-fc6416c813b4
	 */
	@Id(increment=false)
	private String openId;
	/**
	 * 真实姓名
	 * 
	 * @pdOid 6354221c-f1d8-4230-99a5-25d3704e294e
	 */
	private String realName;
	/**
	 * 昵称
	 * 
	 * @pdOid 43c6b709-1feb-4d32-8a7c-120ec1454b0f
	 */
	private String nickName;
	/**
	 * 性别(值为1时是男性，值为2时是女性，值为0时是未知)
	 * 
	 * @pdOid 2ded0385-c7c4-4f27-9a02-6949b3cfa496
	 */
	private Integer sex;
	/**
	 * 手机号
	 * 
	 * @pdOid 7203ce58-29ce-4a9c-ab8d-8a1a85bad206
	 */
	private String phone;
	/**
	 * 血型
	 * 
	 * @pdOid a0915fda-d064-48bd-9b95-a9fdc66d7db8
	 */
	private String blood;
	/**
	 * 身高（cm）
	 * 
	 * @pdOid 23ac54e7-ceca-49dc-9418-674752cf2a62
	 */
	private Integer height;
	/**
	 * 体重（KG）
	 * 
	 * @pdOid 605f3ec8-4b35-4525-b05e-fe5adfcda320
	 */
	private Integer weight;
	/**
	 * 生日
	 * 
	 * @pdOid df96dc2d-69c2-4b60-8ed9-c2190bbcd152
	 */
	private String birthday;
	/**
	 * 地址
	 * 
	 * @pdOid 17393b72-6d8c-405e-ad8b-4870620d61fd
	 */
	private String address;
	/**
	 * 城市
	 * 
	 * @pdOid e001c82f-e404-4ddd-9d49-a9a049d97697
	 */
	private String city;
	/**
	 * 国家
	 * 
	 * @pdOid 0fb8733c-6d51-4b1f-8d5b-6f85868af105
	 */
	private String country;
	/**
	 * 用户所在省份
	 * 
	 * @pdOid 8edd3998-1149-435f-a5d8-1e9505f8ee9b
	 */
	private String province;
	/**
	 * 用户的语言，简体中文为zh_CN
	 * 
	 * @pdOid 4db4cf15-3b55-491c-a342-5dec37d46c74
	 */
	private String language;
	
	/**
	 * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	 */
	private String remark;
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像
	 * 
	 * @pdOid 90e10f73-3c97-4684-99a4-ee277a95087c
	 */
	private String headImgUrl;
	/**
	 * 头像物理路径
	 * 
	 * @pdOid 9ba27c8a-7e2d-445b-bf68-38751c3c468e
	 */
	private String riskPath;
	/**
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	 * 
	 * @pdOid 9e85cf1b-ee9f-48be-97ef-420a23ec52e4
	 */
	private Integer subscribe_time;
	/**
	 * 身份证号
	 * 
	 * @pdOid c5650cad-5fd5-44ec-b494-177b2579b61a
	 */
	private String idCard;
	/**
	 * 用户状态
	 * 
	 * @pdOid f5d54a1c-674a-4c71-ae0e-d1e11c2a004d
	 */
	private Integer userState;
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 * 
	 * @pdOid ee7bd8de-bb7b-477d-a9db-d7c3c3531671
	 */
	private String unionid;
	/**
	 * 用户所在的分组ID
	 * 
	 * @pdOid ae0a3bb9-057d-47bf-b929-5afe8a8ec33d
	 */
	private Integer groupid;
	/**
	 * 会员角色1 医生 2分销商
	 * 
	 * @pdOid a3f973a6-ef63-4a60-938a-9ef3eb021aba
	 */
	private Integer type;
	/**
	 * 状态 0关注 1未关注
	 * 
	 * @pdOid 7984f7de-8423-40d4-8b92-1ced6f60503e
	 */
	private Integer state;
	/**
	 * 备用字段1
	 * 
	 * @pdOid c072044b-77a8-4d37-a18b-17b8a038ab76
	 */
	private String rsrvStr1;
	/**
	 * 备用字段2
	 * 
	 * @pdOid 85f61a08-83eb-4895-ab58-e586f05b4d4e
	 */
	private String rsrvStr2;
	/**
	 * 备用字段3
	 * 
	 * @pdOid 185d69d7-5d66-460e-8b34-5930ca5b012b
	 */
	private String rsrvStr3;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getRiskPath() {
		return riskPath;
	}

	public void setRiskPath(String riskPath) {
		this.riskPath = riskPath;
	}

	public Integer getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(Integer subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}