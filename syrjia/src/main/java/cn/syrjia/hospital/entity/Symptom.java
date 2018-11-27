package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.OneToMany;
import cn.syrjia.common.annotation.Table;

/**
 * 症状表
 */
@Table(name="t_order_symptom")
public class Symptom implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 内容	
	 */
	@Column
	private String content;
	
	/**
	 * 访问地址
	 */
	@Column
	private String visitAddr;
	
	/**
	 * 物理地址
	 */
	@Column
	private String physicsAddr;
	/**
	 * 原物理路径
	 */
	private String oldPhysicsAddr;
	/**
	 * 语音访问地址
	 */
	@Column
	private String voiceAddr;
	
	/**
	 * 类型0 主要症状描述 1脉搏 2舌照 3 面照
	 */
	@Column
	private Integer type;
	
	
	/**
	 * 题目选项
	 */
	/*@OneToMany(correlationField="id",correlationOtherField="symptomId")
	private List<SubjectHistoryTitle> subjectHistoryTitles;*/
	
	/**
	 *插入时间
	 */
	@Column
	private Integer createTime;

	/**
	 * 名称
	 */
	@Column
	private String name;

	/**
	 * 年龄
	 */
	@Column
	private Integer age;

	/**
	 * 性别
	 */
	@Column
	private Integer sex;
	
	/**
	 * 电话
	 */
	@Column
	private String phone;
	
	/**
	 * voiceLen
	 */
	@Column
	private String voiceLen;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getVisitAddr() {
		return visitAddr;
	}

	public void setVisitAddr(String visitAddr) {
		this.visitAddr = visitAddr;
	}

	public String getPhysicsAddr() {
		return physicsAddr;
	}

	public void setPhysicsAddr(String physicsAddr) {
		this.physicsAddr = physicsAddr;
	}
	

	public String getOldPhysicsAddr() {
		return oldPhysicsAddr;
	}

	public void setOldPhysicsAddr(String oldPhysicsAddr) {
		this.oldPhysicsAddr = oldPhysicsAddr;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getVoiceAddr() {
		return voiceAddr;
	}

	public void setVoiceAddr(String voiceAddr) {
		this.voiceAddr = voiceAddr;
	}

	public String getVoiceLen() {
		return voiceLen;
	}

	public void setVoiceLen(String voiceLen) {
		this.voiceLen = voiceLen;
	}

/*	public List<SubjectHistoryTitle> getSubjectHistoryTitles() {
		return subjectHistoryTitles;
	}

	public void setSubjectHistoryTitles(
			List<SubjectHistoryTitle> subjectHistoryTitles) {
		this.subjectHistoryTitles = subjectHistoryTitles;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	/*@Override
	public String toString() {
		return "Symptom [id=" + id + ", orderNo=" + orderNo + ", content="
				+ content + ", visitAddr=" + visitAddr + ", physicsAddr="
				+ physicsAddr + ", oldPhysicsAddr=" + oldPhysicsAddr
				+ ", voiceAddr=" + voiceAddr + ", type=" + type
				+ ", subjectHistoryTitles=" + subjectHistoryTitles
				+ ", createTime=" + createTime + ", name=" + name + ", age="
				+ age + ", sex=" + sex + ", voiceLen=" + voiceLen
				+ ", getId()=" + getId() + ", getOrderNo()=" + getOrderNo()
				+ ", getContent()=" + getContent() + ", getVisitAddr()="
				+ getVisitAddr() + ", getPhysicsAddr()=" + getPhysicsAddr()
				+ ", getOldPhysicsAddr()=" + getOldPhysicsAddr()
				+ ", getType()=" + getType() + ", getCreateTime()="
				+ getCreateTime() + ", getVoiceAddr()=" + getVoiceAddr()
				+ ", getVoiceLen()=" + getVoiceLen()
				+ ", getSubjectHistoryTitles()=" + getSubjectHistoryTitles()
				+ ", getName()=" + getName() + ", getAge()=" + getAge()
				+ ", getSex()=" + getSex() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}*/
	
	
}
