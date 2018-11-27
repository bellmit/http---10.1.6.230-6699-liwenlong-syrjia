package cn.syrjia.sales.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name="t_feed_back")
public class FeedBack implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 内容
	 */
	@Column
	private String content;
	
	/**
	 * 助理id
	 */
	@Column
	private String srId;
	
	/**
	 * feedId
	 */
	@Column
	private String feedId;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 * 状态
	 */
	@Column
	private Integer state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSrId() {
		return srId;
	}

	public void setSrId(String srId) {
		this.srId = srId;
	}

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
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
}
