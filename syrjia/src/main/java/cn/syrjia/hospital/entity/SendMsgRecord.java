package cn.syrjia.hospital.entity;

import cn.syrjia.common.annotation.Table;


@Table(name="t_sendmsg_record")
public class SendMsgRecord {

	/**
	 * 主键
	 */
	private Integer id;
	
	/**
	 * openid
	 */
	private String openId;
	
	/**
	 * 医生的openid
	 */
	private String docOpenId;
	
	/**
	 * 发送时间
	 */
	private Integer sendTime;
	
	/**
	 * 单位类型
	 */
	private Integer unitype;
	
	public SendMsgRecord() {
		super();
	}
	
	public SendMsgRecord( String openId, String docOpenId,Integer unitype) {
		super();
		this.openId = openId;
		this.docOpenId = docOpenId;
		this.unitype = unitype;
	}
	public SendMsgRecord( String openId, String docOpenId,Integer sendTime,Integer unitype) {
		super();
		this.openId = openId;
		this.docOpenId = docOpenId;
		this.sendTime = sendTime;
		this.unitype = unitype;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getDocOpenId() {
		return docOpenId;
	}

	public void setDocOpenId(String docOpenId) {
		this.docOpenId = docOpenId;
	}

	public Integer getSendTime() {
		return sendTime;
	}

	public void setSendTime(Integer sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getUnitype() {
		return unitype;
	}

	public void setUnitype(Integer unitype) {
		this.unitype = unitype;
	}
	
}
