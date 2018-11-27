package cn.syrjia.entity;

import java.io.Serializable;
import java.util.List;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


@Table(name="t_send_tempmsg")
public class SendTempMsg implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	private Integer id;
	
	/**
	 * 目标
	 */
	@Column
	private String touser;
	
	/**
	 * 模板
	 */
	@Column
	private String template_id;
	
	/**
	 * 地址
	 */
	@Column
	private String url;
	
	/**
	 * 数据
	 */
	@Column
	private String data;
	
	/**
	 * 数据列表
	 */
	private List<TemplateData> datalist;
	
	/**
	 * 错误信息
	 */
	@Column
	private String errmsg;
	
	/**
	 * backupStr
	 */
	@Column
	private String backupStr;
	
	/**
	 * backupStr1
	 */
	@Column
	private String backupStr1;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getBackupStr() {
		return backupStr;
	}

	public void setBackupStr(String backupStr) {
		this.backupStr = backupStr;
	}

	public String getBackupStr1() {
		return backupStr1;
	}

	public void setBackupStr1(String backupStr1) {
		this.backupStr1 = backupStr1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<TemplateData> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<TemplateData> datalist) {
		this.datalist = datalist;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
