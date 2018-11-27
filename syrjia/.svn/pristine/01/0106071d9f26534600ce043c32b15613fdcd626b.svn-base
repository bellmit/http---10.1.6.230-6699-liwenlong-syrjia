package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * 会员信息
 * ClassName: Member 
 * @Description: TODO
 * @date 2017-11-30
 */
@Table(name="t_member")
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false)
	private String id; 
	
	@Column
	private String loginname; //用户登录名
	
	@Column
	private String phone; //用户登录名
	
	@Column
	private String password;//密码
	
	
	@Column(isNull=true)
	private String openid;// 微信openid
	
	@Column
	private String createuserid;//创建人id
	
	@Column
	private String headicon;// 微信头像信息
	
	@Column
	private String photo;//微信头像地址
	
	@Column
	private String realname;// 真实姓名
	
	@Column
	private Integer sex; //性别 1为男，2为女
	
	@Column
	private String idcard;//身份证号
	
	@Column
	private String birthday;//出生日期
	
	@Column
	private String address;//地址
	
	@Column
	private Integer createtime;//创建时间
	
	@Column
	private Integer state;//状态 0正常 1禁用 2删除
	
	@Column
	private String addWay;//wx-微信端 app-APP端  sys-后台创建

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCreateuserid() {
		return createuserid;
	}

	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}

	public String getHeadicon() {
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
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

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getAddWay() {
		return addWay;
	}

	public void setAddWay(String addWay) {
		this.addWay = addWay;
	}
}
