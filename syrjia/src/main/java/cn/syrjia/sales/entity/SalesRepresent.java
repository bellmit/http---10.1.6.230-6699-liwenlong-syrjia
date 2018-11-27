package cn.syrjia.sales.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * @author liwenlong
 * 销售代表表
 */
@Table(name = "t_sales_represent")
public class SalesRepresent implements Serializable {

	public String getLocalImgUrl() {
		return localImgUrl;
	}

	public void setLocalImgUrl(String localImgUrl) {
		this.localImgUrl = localImgUrl;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getIsDirector() {
		return isDirector;
	}

	public void setIsDirector(String isDirector) {
		this.isDirector = isDirector;
	}

	public String getSaleChannelId() {
		return saleChannelId;
	}

	public void setSaleChannelId(String saleChannelId) {
		this.saleChannelId = saleChannelId;
	}

	public String getLocalQrUrl() {
		return localQrUrl;
	}

	public void setLocalQrUrl(String localQrUrl) {
		this.localQrUrl = localQrUrl;
	}

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id(increment = false)
	private String srId;
	
	/**
	 * 销售代表名称
	 */
	@Column
	private String name;
	
	/**
	 * 销售代表所属公司
	 */
	@Column
	private String company;
	
	/**
	 * 销售代表所属办事处
	 */
	@Column
	private String office;
	
	/**
	 * 销售代表电话
	 */
	@Column
	private String phone;
	
	/**
	 * 省级ID
	 */
	@Column
	private String proviceId;
	
	/**
	 * 市级Id
	 */
	@Column
	private String cityId;
	
	/**
	 * 县区级Id
	 */
	@Column
	private String countyId;
	
	/**
	 * 详细地址
	 */
	@Column
	private String address;
	
	
	/**
	 * 销售代表微信号
	 */
	@Column
	private String salesWeChatNo;
	
	/**
	 * 销售代表性别 0-男 1-女
	 */
	@Column
	private Integer sex;
	
	/**
	 * 父级ID(只能是上医仁家ID根ID)
	 */
	@Column
	private String pid;
	
	/**
	 * 角色类别
	 */
	@Column
	private Integer type;
	
	/**
	 * 0-在用 1-禁用 2-删除 3-正在审核 4-锁定 5-离职
	 */
	@Column
	private Integer state;
	
	/**
	 * 创建人
	 */
	@Column
	private String createUser;
	
	/**
	 * 创建日期
	 */
	@Column
	private Integer createTime;
	
	@Column
	private String userId;
	
	@Column
	private String openid;
	
	/**
	 *  厚得馆 0-否 1-是 
	 */
	@Column
	private Integer isHdg;
	
	/**
	 *  药品 0-否 1-是 
	 */
	@Column
	private Integer isDrug;
	
	/**
	 *  服务 0-否 1-是 
	 */
	@Column
	private Integer isServer;
	
	/**
	 *  活动 0-否 1-是 
	 */
	@Column
	private Integer isActivity;
	
	/**
	 *  就诊 0-否 1-是 
	 */
	@Column
	private Integer isVisit;
	
	/**
	 *  业务内容
	 */
	@Column
	private String businessContent;
	
	/**
	 * 创建方式 0-后台创建 1-扫码创建 默认0
	 */
	@Column
	private Integer isLocalCreate;
	
	/**
	 * 头像
	 */
	@Column
	private String imgUrl;
	
	@Column
	private String localImgUrl;
	
	/**
	 * 显示状态 0-显示 1-隐藏
	 */
	@Column
	private Integer showState;
	
	/**
	 *  是否默认 0-否 1-是 
	 */
	@Column
	private Integer isDefault;
	
	/**
	 *  是否自己设置密码 0-否 1-是 
	 */
	@Column
	private Integer isSelfSet;
	
	/**
	 * quota
	 */
	@Column
	private String qrCodeUrl;

	/**
	 * isDirector
	 */
	@Column
	private String isDirector;
	
	/**
	 * 助理渠道id
	 */
	@Column
	private String saleChannelId;
	
	/**
	 * 本地地址
	 */
	@Column
	private String localQrUrl;
	
	/**
	 *  登录密码
	 */
	@Column
	private String salesPassword;

	public String getSrId() {
		return srId;
	}

	public void setSrId(String srId) {
		this.srId = srId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSalesWeChatNo() {
		return salesWeChatNo;
	}

	public void setSalesWeChatNo(String salesWeChatNo) {
		this.salesWeChatNo = salesWeChatNo;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProviceId() {
		return proviceId;
	}

	public void setProviceId(String proviceId) {
		this.proviceId = proviceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsHdg() {
		return isHdg;
	}

	public void setIsHdg(Integer isHdg) {
		this.isHdg = isHdg;
	}

	public Integer getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(Integer isDrug) {
		this.isDrug = isDrug;
	}

	public Integer getIsServer() {
		return isServer;
	}

	public void setIsServer(Integer isServer) {
		this.isServer = isServer;
	}

	public Integer getIsActivity() {
		return isActivity;
	}

	public void setIsActivity(Integer isActivity) {
		this.isActivity = isActivity;
	}

	public Integer getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(Integer isVisit) {
		this.isVisit = isVisit;
	}

	public String getBusinessContent() {
		return businessContent;
	}

	public void setBusinessContent(String businessContent) {
		this.businessContent = businessContent;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getIsLocalCreate() {
		return isLocalCreate;
	}

	public void setIsLocalCreate(Integer isLocalCreate) {
		this.isLocalCreate = isLocalCreate;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getShowState() {
		return showState;
	}

	public void setShowState(Integer showState) {
		this.showState = showState;
	}

	public Integer getIsSelfSet() {
		return isSelfSet;
	}

	public void setIsSelfSet(Integer isSelfSet) {
		this.isSelfSet = isSelfSet;
	}

	public String getSalesPassword() {
		return salesPassword;
	}

	public void setSalesPassword(String salesPassword) {
		this.salesPassword = salesPassword;
	}

}
