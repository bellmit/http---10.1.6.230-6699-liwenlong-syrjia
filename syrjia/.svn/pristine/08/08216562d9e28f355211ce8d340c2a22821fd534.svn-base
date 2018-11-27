package cn.syrjia.entity;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;

@Table(name = "t_sys_set")
public class SysSet implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id(increment=false,UUID=true)
	private String id;

	@Column
	private Integer serverNoPayTime;// 服务未付款订单有效时长（分钟）

	@Column
	private Integer serverPayTime;// 服务待付款订单时长（分钟）
	
	@Column
	private Integer serverBeEndDay;// 服务快到期前几天通知用户、管理员
	
	@Column
	private Integer serverBeEndNoticeTime;// 服务快到期前每天几点通知用户、管理员

	@Column
	private Integer serverCanApplyRefund;// 服务可申请退款（天）后

	@Column
	private Integer serverAutoFinish;// 过有效期（天）自动完成

	@Column
	private Integer goodNoPayTime;// 产品未支付订单保留时长(分钟)
	
	@Column
	private Integer goodPayTime;// 产品待支付订单有效时长（分钟）
	
	@Column
	private Integer goodCanApplyRefund;// 产品过（天）可申请退款

	@Column
	private Integer goodAutoFinish;// 产品发货（天）自动完成

	@Column
	private Integer scoreNoPayTime;// 及分类未支付订单保留时长(分钟)

	@Column
	private Integer scorePayTime;// 积分类待支付订单有效时长（分钟）

	@Column
	private Integer scoreCanApplyRefund;// 积分类过（天）可申请退款
	
	@Column
	private Integer scoreAutoFinish;// 积分类发货（天）自动完成

	@Column
	private Integer ghFirstTime;// 挂号初步确认时长（分钟）

	@Column
	private Integer ghPayTime;// 挂号订单待支付订单有效时长（分钟）
	
	@Column
	private Integer ghUserConfirmTime;// 挂号过（小时）可自动确认

	@Column
	private Integer ghCanApplyRefund;// 挂号过（天）可申请退款

	@Column
	private Integer ghNoCanApplyRefund;// 服务开始前（天）不可申请退款

	@Column
	private Integer visitFirstTime;// 患者期望就诊日期（最早） （天）后

	@Column
	private Integer visitMinTime;// 患者期望就诊日期区间最少（天）

	@Column
	private Integer visitMaxTime;// 患者期望就诊日期区间最多（天）

	@Column
	private Integer indeedBeginTime;// 客服即使回复开始时间（点）

	@Column
	private Integer indeedEndTime;// 客服即使回复结束时间（点）

	@Column
	private Integer noIndeedTimeAlert;// 非及时回复时间段内未确认订单每天（点）提醒

	@Column
	private Integer ghAutoFinishTime;// 超过挂号日期（天后）订单未处理，订单自动完成

	@Column
	private Integer ghPartRefundScale;// 协商部分退款，平台扣除( )%服务费

	@Column
	private Double pzServerMoney;// 陪诊服务费

	@Column
	private Double pzPeopleMoney;// 陪诊人员费

	@Column
	private Integer pzFirstTime;// 陪诊初步确认时长（分钟）

	@Column
	private Integer pzPayTime;// 陪诊订单待支付订单有效时长（分钟）

	@Column
	private Integer pzCanApplyRefund;// 陪诊过（天）可申请退款

	@Column
	private Integer pzAutoFinishTime;// 超过陪诊日期（天后）订单未处理，订单自动完成
	
	@Column
	private Integer pzVisitFirstTime;// 陪诊最早多少天后

	@Column
	private Integer pzPartRefundScale;// 协商部分退款，平台扣除( )%服务费

	@Column
	private Integer pzNoCanApplyRefund;// 服务开始前（天）不可申请退款

	@Column
	private Double hzServerMoney;// 会诊服务费

	@Column
	private Double hzMoney;// 会诊费（单学科）

	@Column
	private Integer hzFirstTime;// 会诊申请提交后（天）内回复

	@Column
	private Integer hzPayTime;// 会诊订单待支付订单有效时长（分钟）

	@Column
	private Integer hzCanApplyRefund;// 会诊过（天）可申请退款

	@Column
	private Integer hzFinishTime;// 付款后（天）出具电子报告

	@Column
	private Integer hzPartRefundScale;// 协商部分退款，平台扣除( )%服务费

	@Column
	private Double zyServerMoney;// 住院服务费（元）（意向住院）
	
	@Column
	private Double zyPointServerMoney;// 住院服务费（元）（多点职业住院）

	@Column
	private Integer zyPayTime;// 住院订单待支付订单有效时长（分钟）

	@Column
	private Integer zyCanApplyRefund;// 住院过（天）可申请退款

	@Column
	private Integer zyNoFinishTime;// 付款后（天）未完成，退款給用戶

	@Column
	private Integer zyPartRefundScale;// 协商部分退款，平台扣除( )%服务费
	
	@Column
	private Integer zyUserConfirmTime;// 挂号过（小时）可自动确认

	@Column
	private String ghUserProtocol;// 挂號用戶協議

	@Column
	private String ghIntroduce;// 挂号服务介绍
	
	@Column
	private String ghRemarks;// 挂号告知通知

	@Column
	private String pzUserProtocol;// 陪诊用戶協議

	@Column
	private String pzIntroduce;// 陪诊服务介绍
	
	@Column
	private String pzRemarks;// 陪诊告知通知

	@Column
	private String hzUserProtocol;// 会诊用戶協議

	@Column
	private String hzIntroduce;// 會診服务介绍
	
	@Column
	private String hzRemarks;// 会诊告知通知

	@Column
	private String zyUserProtocol;// 住院用戶協議

	@Column
	private String zyIntroduce;// 住院服务介绍

	@Column
	private String zyRemarks;// 住院告知通知
	
	@Column
	private String ssUserProtocol;// 手术用戶協議

	@Column
	private String ssIntroduce;// 手术服务介绍
	
	@Column
	private Integer clickScore;// 签到积分
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getServerNoPayTime() {
		return serverNoPayTime;
	}

	public void setServerNoPayTime(Integer serverNoPayTime) {
		this.serverNoPayTime = serverNoPayTime;
	}

	public Integer getServerPayTime() {
		return serverPayTime;
	}

	public void setServerPayTime(Integer serverPayTime) {
		this.serverPayTime = serverPayTime;
	}

	public Integer getServerCanApplyRefund() {
		return serverCanApplyRefund;
	}

	public void setServerCanApplyRefund(Integer serverCanApplyRefund) {
		this.serverCanApplyRefund = serverCanApplyRefund;
	}

	public Integer getServerAutoFinish() {
		return serverAutoFinish;
	}

	public void setServerAutoFinish(Integer serverAutoFinish) {
		this.serverAutoFinish = serverAutoFinish;
	}

	public Integer getGoodNoPayTime() {
		return goodNoPayTime;
	}

	public void setGoodNoPayTime(Integer goodNoPayTime) {
		this.goodNoPayTime = goodNoPayTime;
	}

	public Integer getGoodPayTime() {
		return goodPayTime;
	}

	public void setGoodPayTime(Integer goodPayTime) {
		this.goodPayTime = goodPayTime;
	}

	public Integer getGoodCanApplyRefund() {
		return goodCanApplyRefund;
	}

	public void setGoodCanApplyRefund(Integer goodCanApplyRefund) {
		this.goodCanApplyRefund = goodCanApplyRefund;
	}

	public Integer getGoodAutoFinish() {
		return goodAutoFinish;
	}

	public void setGoodAutoFinish(Integer goodAutoFinish) {
		this.goodAutoFinish = goodAutoFinish;
	}

	public Integer getScoreNoPayTime() {
		return scoreNoPayTime;
	}

	public void setScoreNoPayTime(Integer scoreNoPayTime) {
		this.scoreNoPayTime = scoreNoPayTime;
	}

	public Integer getScorePayTime() {
		return scorePayTime;
	}

	public void setScorePayTime(Integer scorePayTime) {
		this.scorePayTime = scorePayTime;
	}

	public Integer getScoreCanApplyRefund() {
		return scoreCanApplyRefund;
	}

	public void setScoreCanApplyRefund(Integer scoreCanApplyRefund) {
		this.scoreCanApplyRefund = scoreCanApplyRefund;
	}

	public Integer getScoreAutoFinish() {
		return scoreAutoFinish;
	}

	public void setScoreAutoFinish(Integer scoreAutoFinish) {
		this.scoreAutoFinish = scoreAutoFinish;
	}

	public Integer getGhFirstTime() {
		return ghFirstTime;
	}

	public void setGhFirstTime(Integer ghFirstTime) {
		this.ghFirstTime = ghFirstTime;
	}

	public Integer getGhPayTime() {
		return ghPayTime;
	}

	public void setGhPayTime(Integer ghPayTime) {
		this.ghPayTime = ghPayTime;
	}

	public Integer getGhCanApplyRefund() {
		return ghCanApplyRefund;
	}

	public void setGhCanApplyRefund(Integer ghCanApplyRefund) {
		this.ghCanApplyRefund = ghCanApplyRefund;
	}

	public Integer getGhNoCanApplyRefund() {
		return ghNoCanApplyRefund;
	}

	public void setGhNoCanApplyRefund(Integer ghNoCanApplyRefund) {
		this.ghNoCanApplyRefund = ghNoCanApplyRefund;
	}

	public Integer getVisitFirstTime() {
		return visitFirstTime;
	}

	public void setVisitFirstTime(Integer visitFirstTime) {
		this.visitFirstTime = visitFirstTime;
	}

	public Integer getVisitMinTime() {
		return visitMinTime;
	}

	public void setVisitMinTime(Integer visitMinTime) {
		this.visitMinTime = visitMinTime;
	}

	public Integer getVisitMaxTime() {
		return visitMaxTime;
	}

	public void setVisitMaxTime(Integer visitMaxTime) {
		this.visitMaxTime = visitMaxTime;
	}

	public Integer getIndeedBeginTime() {
		return indeedBeginTime;
	}

	public void setIndeedBeginTime(Integer indeedBeginTime) {
		this.indeedBeginTime = indeedBeginTime;
	}

	public Integer getIndeedEndTime() {
		return indeedEndTime;
	}

	public void setIndeedEndTime(Integer indeedEndTime) {
		this.indeedEndTime = indeedEndTime;
	}

	public Integer getNoIndeedTimeAlert() {
		return noIndeedTimeAlert;
	}

	public void setNoIndeedTimeAlert(Integer noIndeedTimeAlert) {
		this.noIndeedTimeAlert = noIndeedTimeAlert;
	}

	public Integer getGhAutoFinishTime() {
		return ghAutoFinishTime;
	}

	public void setGhAutoFinishTime(Integer ghAutoFinishTime) {
		this.ghAutoFinishTime = ghAutoFinishTime;
	}

	public Integer getGhPartRefundScale() {
		return ghPartRefundScale;
	}

	public void setGhPartRefundScale(Integer ghPartRefundScale) {
		this.ghPartRefundScale = ghPartRefundScale;
	}

	public Double getPzServerMoney() {
		return pzServerMoney;
	}

	public void setPzServerMoney(Double pzServerMoney) {
		this.pzServerMoney = pzServerMoney;
	}

	public Double getPzPeopleMoney() {
		return pzPeopleMoney;
	}

	public void setPzPeopleMoney(Double pzPeopleMoney) {
		this.pzPeopleMoney = pzPeopleMoney;
	}

	public Integer getPzFirstTime() {
		return pzFirstTime;
	}

	public void setPzFirstTime(Integer pzFirstTime) {
		this.pzFirstTime = pzFirstTime;
	}

	public Integer getPzPayTime() {
		return pzPayTime;
	}

	public void setPzPayTime(Integer pzPayTime) {
		this.pzPayTime = pzPayTime;
	}

	public Integer getPzCanApplyRefund() {
		return pzCanApplyRefund;
	}

	public void setPzCanApplyRefund(Integer pzCanApplyRefund) {
		this.pzCanApplyRefund = pzCanApplyRefund;
	}

	public Integer getPzAutoFinishTime() {
		return pzAutoFinishTime;
	}

	public void setPzAutoFinishTime(Integer pzAutoFinishTime) {
		this.pzAutoFinishTime = pzAutoFinishTime;
	}

	public Integer getPzPartRefundScale() {
		return pzPartRefundScale;
	}

	public void setPzPartRefundScale(Integer pzPartRefundScale) {
		this.pzPartRefundScale = pzPartRefundScale;
	}

	public Integer getPzNoCanApplyRefund() {
		return pzNoCanApplyRefund;
	}

	public void setPzNoCanApplyRefund(Integer pzNoCanApplyRefund) {
		this.pzNoCanApplyRefund = pzNoCanApplyRefund;
	}

	public Double getHzServerMoney() {
		return hzServerMoney;
	}

	public void setHzServerMoney(Double hzServerMoney) {
		this.hzServerMoney = hzServerMoney;
	}

	public Double getHzMoney() {
		return hzMoney;
	}

	public void setHzMoney(Double hzMoney) {
		this.hzMoney = hzMoney;
	}

	public Integer getHzFirstTime() {
		return hzFirstTime;
	}

	public void setHzFirstTime(Integer hzFirstTime) {
		this.hzFirstTime = hzFirstTime;
	}

	public Integer getHzPayTime() {
		return hzPayTime;
	}

	public void setHzPayTime(Integer hzPayTime) {
		this.hzPayTime = hzPayTime;
	}

	public Integer getHzCanApplyRefund() {
		return hzCanApplyRefund;
	}

	public void setHzCanApplyRefund(Integer hzCanApplyRefund) {
		this.hzCanApplyRefund = hzCanApplyRefund;
	}

	public Integer getHzFinishTime() {
		return hzFinishTime;
	}

	public void setHzFinishTime(Integer hzFinishTime) {
		this.hzFinishTime = hzFinishTime;
	}

	public Integer getHzPartRefundScale() {
		return hzPartRefundScale;
	}

	public void setHzPartRefundScale(Integer hzPartRefundScale) {
		this.hzPartRefundScale = hzPartRefundScale;
	}

	public Double getZyServerMoney() {
		return zyServerMoney;
	}

	public void setZyServerMoney(Double zyServerMoney) {
		this.zyServerMoney = zyServerMoney;
	}

	public Integer getZyPayTime() {
		return zyPayTime;
	}

	public void setZyPayTime(Integer zyPayTime) {
		this.zyPayTime = zyPayTime;
	}

	public Integer getZyCanApplyRefund() {
		return zyCanApplyRefund;
	}

	public void setZyCanApplyRefund(Integer zyCanApplyRefund) {
		this.zyCanApplyRefund = zyCanApplyRefund;
	}

	public Integer getZyNoFinishTime() {
		return zyNoFinishTime;
	}

	public void setZyNoFinishTime(Integer zyNoFinishTime) {
		this.zyNoFinishTime = zyNoFinishTime;
	}

	public Integer getZyPartRefundScale() {
		return zyPartRefundScale;
	}

	public void setZyPartRefundScale(Integer zyPartRefundScale) {
		this.zyPartRefundScale = zyPartRefundScale;
	}

	public String getGhUserProtocol() {
		return ghUserProtocol;
	}

	public void setGhUserProtocol(String ghUserProtocol) {
		this.ghUserProtocol = ghUserProtocol;
	}

	public String getGhIntroduce() {
		return ghIntroduce;
	}

	public void setGhIntroduce(String ghIntroduce) {
		this.ghIntroduce = ghIntroduce;
	}

	public String getPzUserProtocol() {
		return pzUserProtocol;
	}

	public void setPzUserProtocol(String pzUserProtocol) {
		this.pzUserProtocol = pzUserProtocol;
	}

	public String getPzIntroduce() {
		return pzIntroduce;
	}

	public void setPzIntroduce(String pzIntroduce) {
		this.pzIntroduce = pzIntroduce;
	}

	public String getHzUserProtocol() {
		return hzUserProtocol;
	}

	public void setHzUserProtocol(String hzUserProtocol) {
		this.hzUserProtocol = hzUserProtocol;
	}

	public String getHzIntroduce() {
		return hzIntroduce;
	}

	public void setHzIntroduce(String hzIntroduce) {
		this.hzIntroduce = hzIntroduce;
	}

	public String getZyUserProtocol() {
		return zyUserProtocol;
	}

	public void setZyUserProtocol(String zyUserProtocol) {
		this.zyUserProtocol = zyUserProtocol;
	}

	public String getZyIntroduce() {
		return zyIntroduce;
	}

	public void setZyIntroduce(String zyIntroduce) {
		this.zyIntroduce = zyIntroduce;
	}

	public String getSsUserProtocol() {
		return ssUserProtocol;
	}

	public void setSsUserProtocol(String ssUserProtocol) {
		this.ssUserProtocol = ssUserProtocol;
	}

	public String getSsIntroduce() {
		return ssIntroduce;
	}

	public void setSsIntroduce(String ssIntroduce) {
		this.ssIntroduce = ssIntroduce;
	}

	public String getGhRemarks() {
		return ghRemarks;
	}

	public void setGhRemarks(String ghRemarks) {
		this.ghRemarks = ghRemarks;
	}

	public String getPzRemarks() {
		return pzRemarks;
	}

	public void setPzRemarks(String pzRemarks) {
		this.pzRemarks = pzRemarks;
	}

	public String getHzRemarks() {
		return hzRemarks;
	}

	public void setHzRemarks(String hzRemarks) {
		this.hzRemarks = hzRemarks;
	}

	public String getZyRemarks() {
		return zyRemarks;
	}

	public void setZyRemarks(String zyRemarks) {
		this.zyRemarks = zyRemarks;
	}

	public Integer getPzVisitFirstTime() {
		return pzVisitFirstTime;
	}

	public void setPzVisitFirstTime(Integer pzVisitFirstTime) {
		this.pzVisitFirstTime = pzVisitFirstTime;
	}

	public Integer getClickScore() {
		return clickScore;
	}

	public void setClickScore(Integer clickScore) {
		this.clickScore = clickScore;
	}

	public Integer getGhUserConfirmTime() {
		return ghUserConfirmTime;
	}

	public void setGhUserConfirmTime(Integer ghUserConfirmTime) {
		this.ghUserConfirmTime = ghUserConfirmTime;
	}

	public Integer getZyUserConfirmTime() {
		return zyUserConfirmTime;
	}

	public void setZyUserConfirmTime(Integer zyUserConfirmTime) {
		this.zyUserConfirmTime = zyUserConfirmTime;
	}

	public Double getZyPointServerMoney() {
		return zyPointServerMoney;
	}

	public void setZyPointServerMoney(Double zyPointServerMoney) {
		this.zyPointServerMoney = zyPointServerMoney;
	}

	public Integer getServerBeEndDay() {
		return serverBeEndDay;
	}

	public void setServerBeEndDay(Integer serverBeEndDay) {
		this.serverBeEndDay = serverBeEndDay;
	}

	public Integer getServerBeEndNoticeTime() {
		return serverBeEndNoticeTime;
	}

	public void setServerBeEndNoticeTime(Integer serverBeEndNoticeTime) {
		this.serverBeEndNoticeTime = serverBeEndNoticeTime;
	}
}
