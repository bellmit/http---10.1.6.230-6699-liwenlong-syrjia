package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.OneToMany;
import cn.syrjia.common.annotation.Table;


@Table(name="h_doctor_evaluate")
public class DoctorEvaluate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键 自增
	 */
	@Id
	private Integer evaluateId;
	
	/**
	 * 医生Id
	 */
	@Column
	private String doctorId;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 会员Id
	 */
	@Column
	private String openid;
	
	/**
	 * 评价等级
	 */
	@Column(isNull=true)
	private Integer evaluateLevel;
	
	/**
	 * 评价内容
	 */
	@Column
	private String evaluate_note;
	
	/**
	 * 解释
	 */
	@Column
	private String explain;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * evaStaus
	 */
	@Column
	private String evaStaus;
	
	/**
	 * 会员名称
	 */
	@Join(table="h_weixin_user",joinType="inner",id="openid",otherId="openid",otherName="phone")
	private String phone;
	
	/**
	 * 会员手机号
	 */
	@Join(table="h_weixin_user",joinType="inner",id="openid",otherId="openid",otherName="nickname")
	private String nickname;
	
	/**
	 * 医生名称
	 */
	@Join(table="h_doctor",joinType="inner",id="doctorId",otherId="doctorId",otherName="docName")
	private String docName;
	
	/**
	 * 医生评论标签记录
	 */
	@OneToMany(correlationField="evaluateId",correlationOtherField="evaluateId")
	private List<DocEvaLabelNote> docEvaLabelNotes;

	public Integer getEvaluateId() {
		return evaluateId;
	}

	public void setEvaluateId(Integer evaluateId) {
		this.evaluateId = evaluateId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getEvaluateLevel() {
		return evaluateLevel;
	}

	public void setEvaluateLevel(Integer evaluateLevel) {
		this.evaluateLevel = evaluateLevel;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getEvaluate_note() {
		return evaluate_note;
	}

	public void setEvaluate_note(String evaluate_note) {
		this.evaluate_note = evaluate_note;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getEvaStaus() {
		return evaStaus;
	}

	public void setEvaStaus(String evaStaus) {
		this.evaStaus = evaStaus;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<DocEvaLabelNote> getDocEvaLabelNotes() {
		return docEvaLabelNotes;
	}

	public void setDocEvaLabelNotes(List<DocEvaLabelNote> docEvaLabelNotes) {
		this.docEvaLabelNotes = docEvaLabelNotes;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
