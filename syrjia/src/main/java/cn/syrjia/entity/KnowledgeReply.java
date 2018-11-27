package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_knowledge_reply")
public class KnowledgeReply implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment = false, UUID = true)
	private String id;

	/**
	 * 文章id
	 */
	private String knowledgeId;

	/**
	 * 用户id
	 */
	private String memberId;

	/**
	 * 状态
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	private Integer createTime;

	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 电话
	 */
	@Join(id="memberId",joinType="inner",otherId="id",table="t_member")
	private String photo;
	
	/**
	 * 真实姓名
	 */
	@Join(id="memberId",joinType="inner",otherId="id",table="t_member")
	private String realname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
}
