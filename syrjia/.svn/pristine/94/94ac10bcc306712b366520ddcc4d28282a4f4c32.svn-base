package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Disable;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * 微信凭证
 */
@Table(name="t_weixin_accesstoken")
public class AccessToken implements Serializable{

	
	@Disable
	private static final long serialVersionUID = 1L;
	
	/**
	 * weixin_account表Id
	 */
	@Id(increment=false)
	private String account_id;
	
	/**
	 * 访问令牌
	 */
	@Column
	private String access_token;
	
	/**
	 * ticket
	 */
	private String jsapiTicket;
	
	/**
	 * 添加时间
	 */
	@Column
	private String addTime;
	
	/**
	 * 凭证有效时间，单位：秒
	 */
	@Column
	private String expires_in;

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
