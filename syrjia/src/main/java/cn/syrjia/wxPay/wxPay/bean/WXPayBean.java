package cn.syrjia.wxPay.wxPay.bean;

import java.io.Serializable;

import cn.syrjia.common.annotation.Column;
import cn.syrjia.common.annotation.Id;
import cn.syrjia.common.annotation.Table;



/**
 * 
 * @author liwenlong
 * 产品价格表
 */
@Table(name = "t_wxpay")
public class WXPayBean implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id(increment = false)
	private String wxid;
	
	@Column
	private String appId;// 微信公众号身份的唯一标识 必填
	
	@Column
	private String MchId;// 商户Id 必填
	
	@Column
	private String deviceInfo;// 设备号 不必填
	
	@Column
	private String nonceStr;// 随机字符串 必填
	
	@Column
	private String sign;// 签名 必填
	
	@Column
	private String body;// 商品描述 必填
	
	@Column
	private String attach;// 附加数据 不必填
	
	@Column
	private String outTradeNo;// 商品订单号 必填
	
	@Column
	private Integer totalFee;// 总金额 必填 单位为分，不能带小数点 必填
	
	@Column
	private String spbillCreateIp;// 终端 必填
	
	@Column
	private String timeStart;// 交易起始时间 格式为yyyyMMddHHmmss 不必填
	
	@Column
	private String timeExpire;// 交易结束时间 不必填
	
	@Column
	private String goodsTag;// 商品标记 不必填
	
	@Column
	private String notifyUrl;// 通知地址 必填
	
	@Column
	private String tradeType;// 交易类型 必填
	
	@Column
	private String openid;// 用户标识 必填
	
	@Column
	private String returnCode;// 返回状态码 必填
	
	@Column
	private String returnMsg;// 返回信息 不必填
	
	@Column
	private String resultCode;// 业务结果 必填
	
	@Column
	private String errCode;// 错误代码 不必填
	
	@Column
	private String errCodeDes;// 错误代码描述 不必填
	
	@Column
	private String prepayId;// 预支付ID 必填
	
	@Column
	private String codeUrl;// 二维码链接 不必填 trade_type为native时有返回
	
	/**
	 * 参数总和
	 */
	@Column
	private String paramTotal;
	
	@Column
	private String str1;// 冗余
	
	@Column
	private String str2;// 冗余
	
	@Column
	private String str3;// 冗余
	
	/**
	 * 工作时间
	 */
	@Column
	private Integer operdate;
	
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMchId() {
		return MchId;
	}
	public void setMchId(String mchId) {
		MchId = mchId;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeExpire() {
		return timeExpire;
	}
	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}
	public String getGoodsTag() {
		return goodsTag;
	}
	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
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
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getParamTotal() {
		return paramTotal;
	}
	public void setParamTotal(String paramTotal) {
		this.paramTotal = paramTotal;
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	public String getStr3() {
		return str3;
	}
	public void setStr3(String str3) {
		this.str3 = str3;
	}
	public Integer getOperdate() {
		return operdate;
	}
	public void setOperdate(Integer operdate) {
		this.operdate = operdate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
