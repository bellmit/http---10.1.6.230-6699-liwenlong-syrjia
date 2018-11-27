
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String finaPackage = (String) session.getAttribute("finaPackage");
	String orderNo = (String) session.getAttribute("orderNo");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>微信支付</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<meta http-equiv="description" content="This is my page">
<script type="text/javascript"
	src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.json-2.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>

<script type="text/javascript">
			
			function callpay(){
				//判断微信版本号
				var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i);
				if( !wechatInfo ) {
					alert("此支付仅支持微信") ;
					window.location.href="<%=basePath%>order/toDetailList.action?orderNo=${sessionScope.orderNo}";
					return;
				} else if ( wechatInfo[1] < "5.0" ) {
					alert("本活动仅支持微信5.0以上版本") ;
					window.location.href="<%=basePath%>order/toDetailList.action?orderNo=${sessionScope.orderNo}";
					return;
				}else{
					if (typeof WeixinJSBridge == "undefined"){
					    if (document.addEventListener){
					        document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
					    }else if (document.attachEvent){
					        document.attachEvent('WeixinJSBridgeReady', jsApiCall);
					        document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
					    }
					} else{
					    jsApiCall();
					}
				}
			}
			var test=${sessionScope.finaPackage};
              		//alert(JSON.stringify(test));
                //调用微信JS api 支付
			function jsApiCall() {
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest',
					test,//json串
					function (res) {
						if(res.err_msg=="get_brand_wcpay_request:ok"){
							alert('恭喜您，支付成功!');
							window.location.href="<%=basePath%>order/toDetailList.action?orderNo=${orderNo}";
						}else{
							alert('支付失败');
							window.location.href="<%=basePath%>order/toDetailList.action?orderNo=${sessionScope.orderNo}";
							return;
						}
					});
				}
			callpay();
		</script>
</head>

<body>

	<!-- <div style="text-align:center;size:30px;">
			
			<input type="button" style="width:200px;height:80px;" value="微信支付" onclick="callpay()">
		</div> -->
	<div class="top">
		<a href="javascript:void(0);" onclick="history.go(-1);" class="back"></a>
		<h2>支付中</h2>
	</div>
	<div class="abc1">
		<div class="main1">
			<p style="text-align: center; font-size:20px; margin-top: 20%;">支付中……</p>
		</div>

	</div>

</body>
</html>
