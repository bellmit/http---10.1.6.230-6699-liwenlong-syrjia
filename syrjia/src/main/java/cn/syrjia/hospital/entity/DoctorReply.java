package cn.syrjia.hospital.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;



/**
 * 
 * @author liwenlong
 *
 * 2016-11-2
 */
@Table(name="h_doctor_order_reply")
public class DoctorReply implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 	父ID
	 */
	@Column
	private String pid;
	
	
	/**
	 * 用户ID
	 */
	@Column
	private String openId;
	
	/**
	 * 医生id
	 */
	@Column
	private String doctorId;
	
	/**
	 * 	订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 	内容
	 */
	@Column
	private String content;
	
	/**
	 * 	类型 0医生 1问诊人员
	 */
	@Column
	private Integer type;
	
	/**
	 * 	状态 0正常 1删除
	 */
	@Column
	private Integer state;
	
	/**
	 * 	时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 真实姓名
	 */
	@Join(id="openId",table="h_weixin_user",otherId="openId",joinType="left")
	private String realname;
	
	/**
	 * 头像地址
	 */
	@Join(id="openId",table="h_weixin_user",otherId="openId",joinType="left")
	private String headimgurl;
	
	/**
	 * 医生名称
	 */
	@Join(id="doctorId",table="h_doctor",otherId="doctorId",joinType="left")
	private String docName;
	
	/**
	 * 医生地址
	 */
	@Join(id="doctorId",table="h_doctor",otherId="doctorId",joinType="left")
	private String docUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
}
