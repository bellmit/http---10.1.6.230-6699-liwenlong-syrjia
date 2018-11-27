package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * app广告表
 * @author Administrator
 *
 */
@Table(name="t_advertising")
public class Advertising implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	private String id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 点击跳转地址
	 */
	private String url;
	
	/**
	 * 图片访问地址
	 */
	private String imageUrl;
	
	/**
	 * 图片物理路径
	 */
	private String riskPath;
	
	/**
	 * 广告秒数
	 */
	private Integer seconds;
	
	/**
	 * 多少秒之后可跳过
	 */
	private Integer skipSeconds;
	
	/**
	 * 状态 1启用 2禁用 3删除
	 */
	private Integer state;
	
	/**
	 * 1医生端 2助理端
	 */
	private Integer port;
	
	/**
	 * 1知识圈文章 2电商活动 3电商商品 4医生主页 5链接
	 */
	private Integer linkType;
	
	/**
	 * 数据
	 */
	private String data;
	
	/**
	 * 创建时间
	 */
	private Integer createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getRiskPath() {
		return riskPath;
	}
	public void setRiskPath(String riskPath) {
		this.riskPath = riskPath;
	}
	public Integer getSeconds() {
		return seconds;
	}
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}
	public Integer getSkipSeconds() {
		return skipSeconds;
	}
	public void setSkipSeconds(Integer skipSeconds) {
		this.skipSeconds = skipSeconds;
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
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getLinkType() {
		return linkType;
	}
	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
