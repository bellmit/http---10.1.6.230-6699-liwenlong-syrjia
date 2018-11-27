package cn.syrjia.wxPay.wxPayReceive.bean;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;


/**
 * 
 * @author liwenlong
 * 微信异步回调信息记录表
 */
@Table(name = "t_wxpay_reci")
public class WXPayHttpReciBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id(increment = false)
	private String WxreId;
	@Column
	private String returnCode;// 返回状态码
	@Column
	private String returnMsg;// 返回信息
	@Column
	private String appId;// 公众号ID
	@Column
	private String mchId;// 商户号
	@Column
	private String deviceInfo;// 设备号
	@Column
	private String nonceStr;// 随机字符串
	@Column
	private String sign;// 签名
	@Column
	private String resultCode;// 业务结果
	@Column
	private String errCode;// 错误代码
	@Column
	private String errCodeDes;// 结果信息描述
	@Column
	private String openId;// 用户标识
	@Column
	private String isSubscribe;// 是否关注公众账号
	@Column
	private String tradeType;// 交易类型
	@Column
	private String bankType;// 付款银行
	@Column
	private Integer totalFee;// 总金额
	@Column
	private Integer couponFee;// 现金券金额
	@Column
	private String feeType;// 货币种类
	@Column
	private String transactionId;// 微信支付订单号
	@Column
	private String outTradeNo;// 商户订单号
	@Column
	private String attach;// 商家数据包
	@Column
	private String timeEnd;// 支付完成时间
	@Column
	private String paramTotal;
	@Column
	private String status;// 状态
	@Column
	private String payType;// 购物，充值

	public String getWxreId() {
		return WxreId;
	}

	public void setWxreId(String wxreId) {
		WxreId = wxreId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Integer couponFee) {
		this.couponFee = couponFee;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getParamTotal() {
		return paramTotal;
	}

	public void setParamTotal(String paramTotal) {
		this.paramTotal = paramTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
