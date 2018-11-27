package cn.syrjia.entity;

import java.io.Serializable;
import java.util.Date;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_knowledge")
public class Knowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键Id
	 * 
	 */
	@Id
	private String id;
	/**
	 * 活动名称
	 * 
	 */
	private String name;
	/**
	 * 活动内容
	 * 
	 */
	private String remark;
	
	/**
	 * 图片访问地址
	 * 
	 * @pdOid 25d11e95-5468-4edf-a7e6-7c486f330954
	 */
	private String imageUrl;
	/**
	 * 图片物理路径
	 * 
	 * @pdOid 97432c69-9804-4ae4-9db3-3722d6efb338
	 */
	private String riskPath;
	
	/**
	 * 图片物理路径
	 * 
	 */
	private String videoPath;
	
	/**
	 * 描述
	 */
	private String describes;
	
	/**
	 * 创建时间
	 * 
	 */
	private Integer createTime;
	/**
	 * 状态 默认2 1启用 2禁用 3删除
	 * 
	 */
	private Integer state;
	/**
	 * 排序
	 * 
	 */
	private Integer rank;
	/**
	 * 备注时间（如手术时间）
	 * 
	 */
	private Date operationTime;
	
	/**
	 * 是否视频
	 */
	private Integer isVideo;
	
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
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
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public String getDescribes() {
		return describes;
	}
	public void setDescribes(String describes) {
		this.describes = describes;
	}
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	public Integer getIsVideo() {
		return isVideo;
	}
	public void setIsVideo(Integer isVideo) {
		this.isVideo = isVideo;
	}
}
