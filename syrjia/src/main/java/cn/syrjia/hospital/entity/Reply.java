package cn.syrjia.hospital.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;



/**
 * 评论表
 * @author ASUS
 *
 */
@Table(name="t_konw_eva_reply")
public class Reply implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@Id
	private String id;
	
	/**
	 * 文章或者商品ID
	 */
	@Column
	private String knowId;
	
	/**
	 * 评论人ID
	 */
	@Column
	private String replyId;
	
	/**
	 * 回复人ID
	 */
	@Column
	private String returnId;
	
	/**
	 * 父级ID 
	 */
	private String pid;
	
	/**
	 * 回复内容
	 */
	private String content;
	
	/**
	 * 1文章回复 2商品回复
	 */
	private Integer type;
	
	/**
	 * 1未读 2已读
	 */
	private Integer sign_;
	
	/**
	 * 回复内容
	 */
	private String recontent;
	
	/**
	 * 状态
	 */
	private Integer state;
	
	/**
	 * 排序
	 */
	private Integer sort;
	
	/**
	 * grankId
	 */
	private String grankId;
	
	public Integer getState() {
		return state;
	}

	public Integer getSort() {
		return sort;
	}

	public String getGrankId() {
		return grankId;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setGrankId(String grankId) {
		this.grankId = grankId;
	}

	

	public Integer getSign_() {
		return sign_;
	}

	public void setSign_(Integer sign_) {
		this.sign_ = sign_;
	}

	public String getRecontent() {
		return recontent;
	}

	
	public void setRecontent(String recontent) {
		this.recontent = recontent;
	}

	/**
	 * 创建时间
	 */
	private Integer createTime;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public String getKnowId() {
		return knowId;
	}

	public String getReplyId() {
		return replyId;
	}

	public String getReturnId() {
		return returnId;
	}

	public String getPid() {
		return pid;
	}

	public String getContent() {
		return content;
	}

	public Integer getType() {
		return type;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKnowId(String knowId) {
		this.knowId = knowId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	
	
}
