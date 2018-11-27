package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Join;
import cn.syrjia.common.annotation.Table;

@Table(name="t_evaluate")
public class Evaluate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键 
	 */
	@Id(increment=false,UUID=true)
	private String id;
	
	/**
	 * 商品Id
	 */
	@Column
	private String goodsId;
	
	/**
	 * 订单号
	 */
	@Column
	private String orderNo;
	
	/**
	 * 会员Id
	 */
	@Column
	private String memberId;
	
	/**
	 * 1-好评 2-中评 3-差评
	 */
	@Column(isNull=true)
	private Integer evaluateLevel;
	
	/**
	 * 产品评价等级
	 */
	@Column(isNull=true)
	private Integer goodsLevel;
	
	/**
	 * 发货速度（等级）
	 */
	@Column(isNull=true)
	private Integer sendLevel;
	
	/**
	 * 服务态度等级
	 */
	@Column(isNull=true)
	private Integer serverLevel;
	
	/**
	 * 评价内容
	 */
	@Column
	private String evaluate_note;
	
	/**
	 * 是否已追加评价（0-否 1-是）
	 */
	@Column(isNull=true)
	private String eva_additional;
	
	/**
	 * 追加评价
	 */
	@Column
	private String additional_note;
	
	/**
	 * 是否匿名评价（0-否 1-是）
	 */
	@Column(isNull=true)
	private String anonymous_eva;
	
	/**
	 * 是否有图片（0-无 1-有）
	 */
	@Column(isNull=true)
	private String hasPic;
	
	/**
	 * 解释
	 */
	@Column
	private String explain;
	
	/**
	 * 创建时间
	 */
	@Column
	private Integer createTime;
	
	/**
	 *1-通过 2-待审核 3-未通过 回复审核状态 默认2
	 */
	@Column
	private Integer replyState;

	/**
	 * 状态
	 */
	@Column
	private Integer state;
	
	/**
	 * 会员名称
	 */
	@Join(table="t_member",joinType="inner",id="memberId",otherId="id",otherName="realname")
	private String realName;
	
	/**
	 * 商品名称
	 */
	@Join(table="t_goods",joinType="left",id="goodsId",otherId="id",otherName="name")
	private String goodsName;
	
	/**
	 * 商品名称
	 */
	@Join(table="t_score_goods",joinType="left",id="goodsId",otherId="id",otherName="name")
	private String scoreGoodsName;
	
	/**
	 * 订单详情ID
	 */
	private String orderDetailId;
	
	/**
	 * 评价模块 1商品评价 2积分评价 3挂號評價 4陪诊评价 5会诊评价 6住院评价
	 */
	private Integer type;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getEvaluateLevel() {
		return evaluateLevel;
	}

	public void setEvaluateLevel(Integer evaluateLevel) {
		this.evaluateLevel = evaluateLevel;
	}

	public Integer getGoodsLevel() {
		return goodsLevel;
	}

	public void setGoodsLevel(Integer goodsLevel) {
		this.goodsLevel = goodsLevel;
	}

	public Integer getSendLevel() {
		return sendLevel;
	}

	public void setSendLevel(Integer sendLevel) {
		this.sendLevel = sendLevel;
	}

	public Integer getServerLevel() {
		return serverLevel;
	}

	public void setServerLevel(Integer serverLevel) {
		this.serverLevel = serverLevel;
	}

	public String getEvaluate_note() {
		return evaluate_note;
	}

	public void setEvaluate_note(String evaluate_note) {
		this.evaluate_note = evaluate_note;
	}

	public String getEva_additional() {
		return eva_additional;
	}

	public void setEva_additional(String eva_additional) {
		this.eva_additional = eva_additional;
	}

	public String getAdditional_note() {
		return additional_note;
	}

	public void setAdditional_note(String additional_note) {
		this.additional_note = additional_note;
	}

	public String getAnonymous_eva() {
		return anonymous_eva;
	}

	public void setAnonymous_eva(String anonymous_eva) {
		this.anonymous_eva = anonymous_eva;
	}

	public String getHasPic() {
		return hasPic;
	}

	public void setHasPic(String hasPic) {
		this.hasPic = hasPic;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getScoreGoodsName() {
		return scoreGoodsName;
	}

	public void setScoreGoodsName(String scoreGoodsName) {
		this.scoreGoodsName = scoreGoodsName;
	}

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getReplyState() {
		return replyState;
	}

	public void setReplyState(Integer replyState) {
		this.replyState = replyState;
	}
}
