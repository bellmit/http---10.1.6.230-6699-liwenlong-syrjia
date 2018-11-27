package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

/**
 * banner图片
 * 
 * @pdOid ab5fde98-2870-4b23-9042-93e69424e7e3
 */
@Table(name = "t_banner")
public class Banner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键Id
	 * 
	 * @pdOid dd6aae02-9009-4d98-b12b-03e16f1ed15e
	 */
	@Id
	private String id;
	/**
	 * 图片访问路径
	 * 
	 * @pdOid f3464933-fef9-4846-b417-58d791a11410
	 */
	private String imageUrl;
	/**
	 * 图片物理路径
	 * 
	 * @pdOid b98d37e0-1d61-4dda-baa0-091bb48a617a
	 */
	private String riskPath;
	/**
	 * banner医生权限
	 * 
	 * @pdOid 187cbdae-4d17-4286-a35e-c57ca4aae79f
	 */
	private int docRingt;

	/**
	 * banner标题
	 * 
	 * @pdOid 187cbdae-4d17-4286-a35e-c57ca4aae79f
	 */
	private String title;
	/**
	 * banner内容
	 * 
	 * @pdOid 3d4c5adb-a4f2-4e50-820c-7445b0bad078
	 */
	private String content;
	/**
	 * 创建时间
	 * 
	 * @pdOid 754ccad9-ed72-4c4a-aa38-fc31e42fe953
	 */
	private Integer createTime;
	/**
	 * 状态 默认1 1启用 2禁用 3删除
	 * 
	 * @pdOid c9211da6-33a4-4f80-b79a-ea81944f68f8
	 */
	private Integer state;
	
	/**
	 * 1主页 2商城
	 */
	private Integer type;
	
	/**
	 * 1知识圈文章 2电商活动 3电商商品 4医生主页 5链接
	 */
	private Integer linkType;
	
	/**
	 * 数据
	 */
	private Integer data;
	
	/**
	 * 端口
	 */
	private Integer port;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}

	public Integer getData() {
		return data;
	}

	public void setData(Integer data) {
		this.data = data;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	public int getDocRingt() {
		return docRingt;
	}

	public void setDocRingt(int docRingt) {
		this.docRingt = docRingt;
	}

}