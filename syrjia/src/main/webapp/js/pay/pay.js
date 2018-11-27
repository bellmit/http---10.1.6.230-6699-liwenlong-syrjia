var app = angular.module("payApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]); 

app.directive('renderFinish', function($timeout) { // renderFinish自定义指令
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			if (scope.$last === true) {
				$timeout(function() {
					scope.$emit('ngRepeatFinished');
				});
			}
		}
	};
});
var evaOrderNo,orderType,doctorId,patientId,orderNo,memberId,payMemberId;
app.controller('payCon', [ '$scope', '$location', '$http', '$sce', '$filter',
		function($scope, $location, $http, $sce, $filter) {
			evaOrderNo = $location.search().evaOrderNo;
			orderNo = $location.search().orderNo;
			var linkurl = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx00a647491b70a8fa&redirect_uri='+$location.absUrl()+'&response_type=code&scope=snsapi_base&state=1#wechat_redirect';
			$http.post(basePath + 'order/queryPayOrderDetail.action', {
				"orderNo" : $location.search().orderNo
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.price = response.data.data.price;
					$scope.orderType = response.data.data.orderType;
					orderType=$scope.orderType;
					doctorId=response.data.data.doctorId;
					patientId=response.data.data.patientId;
					memberId=response.data.data.memberId;
					payMemberId=response.data.data.payMemberId;
					
					$http.post(basePath + 'im/queryScan.action',{url:$location.absUrl()},postCfg).then(
							function(response) {
								if(response.data&&response.data.respCode==1001){
									wx.config({
										debug : false, // 开启调试模式,调用的所有api的返回值会在客户端openAlert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
										appId : response.data.data.appId, // 必填，公众号的唯一标识
										timestamp : response.data.data.timestamp, // 必填，生成签名的时间戳
										nonceStr : response.data.data.nonceStr, // 必填，生成签名的随机串
										signature : response.data.data.signature,// 必填，签名，见附录1
										jsApiList : [ 'onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone']
									// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
									});
									
									wx.ready (function () {  
										 // 微信分享的数据  
									    var shareData = {  
									        "imgUrl" :'https://mobile.syrjia.com/syrjia/img/logo.png',    // 分享显示的缩略图地址  
									        "link" :linkurl,    // 分享地址  
									        "desc" :"上医仁家商品请你代付",   // 分享描述  
									        "title" :'他人代付',   // 分享标题  
									        success : function () {    
									              //alert("分享成功"); 
									        }   
									        };
									       wx.onMenuShareTimeline(shareData); 
									       wx.onMenuShareAppMessage (shareData);
									       wx.onMenuShareQQ(shareData);
									       wx.onMenuShareWeibo(shareData);
									       wx.onMenuShareQZone(shareData);
									});
									
								}
					});
					
				} else {
					openAlert(response.data.respMsg, function() {
						mui.back();
					});
				}
			});

			$scope.pay = function(event) {
				event.stopPropagation();
				if ($location.search().orderNo) {
					$http.post(basePath + 'order/queryRecordOrderByOrderNo.action', {
						orderNo : $location.search().orderNo
					}, postCfg).then(function(response) {
						if (response.data.respCode == 1001) {
							if($scope.check==1){
								openAlertMsgLoad("提交中");
								$http.post(basePath + 'wx/wxJsApiPay.action', {
									orderNo : $location.search().orderNo
								}, postCfg).then(function(response) {
									if (response.data.respCode == 1001) {
										callpay(response.data.data);
									} else {
										openAlert(response.data.respMsg);
									}
									closeAlertMsgLoad();
								});
							}else if($scope.check==2){
								if(isWeiXin()){
									window.location.href="pay_hint.html?orderNo="+$location.search().orderNo;
								}else{
									window.location.href=basePath +"alipay/alipayWxPay.action?orderNo="+$location.search().orderNo;
								}
							}else{
								$("body").append('<div class="sy_href"><img src="../../img/sy_share2.png"></div>');
							}
						} else {
							openAlert(response.data.respMsg);
						}
					});
				}
				return false;
			};
		} ]);
mui('body').on('tap', '.sy_href', function(event) {
	event.stopPropagation();
	$(".sy_href").remove();
});
function callpay(data) {
	// 判断微信版本号
	var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i);
	if (!wechatInfo) {
		openAlert("此支付仅支持微信");
		return false;
	} else if (wechatInfo[1] < "5.0") {
		openAlert("本活动仅支持微信5.0以上版本");
		return false;
	} else {
		if (typeof WeixinJSBridge == "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', jsApiCall,
						false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', jsApiCall);
				document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
			}
		} else {
			jsApiCall(data);
		}
	}
}
// 调用微信JS api 支付
function jsApiCall(data) {
	var pack = eval('(' + data.finaPackage + ')');
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId" : pack.appId, // 公众号名称，由商户传入
		"timeStamp" : pack.timeStamp, // 时间戳，自1970年以来的秒数
		"nonceStr" : pack.nonceStr, // 随机串
		"package" : pack.package,
		"signType" : pack.signType, // 微信签名方式：
		"paySign" : pack.paySign
	// 微信签名
	}, function(res) {
		if (res.err_msg == "get_brand_wcpay_request:ok") {// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回
			
			if(payMemberId==memberId){
				if (orderType == 1) {
					openAlert("支付成功", function() {
						window.location.href = basePath
								+ "weixin/order/order_detail.html?orderNo="
								+ orderNo;
					});
				} else if (orderType == 4) {
					openAlert("支付成功", function() {
						window.location.href = basePath
								+ "weixin/hospital/case_history.html?orderNo="
								+ orderNo;
					});
				}else if(orderType == 12){
					openAlert("支付成功", function() {
						//window.history.go(-2);
						window.location.href = basePath
						+ "weixin/hospital/evaluate_doctor.html?orderNo="+evaOrderNo;
						
					});
				}else if(orderType == 18){
					openAlert("支付成功", function() {
						window.location.href = basePath
								+ "weixin/hospital/look_scheme.html?orderNo="+orderNo;
					});
				}else{
					openAlert("支付成功", function() {
						window.location.href = basePath
								+ "weixin/im/inquiry.html?identifier="+patientId+"&selToID="+doctorId;
					});
				}
			}else{
				openAlert("支付成功",function(){
					window.location.href = basePath
					+ "weixin/hospital/hospital_index.html";
				});
			}
			
		} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
			openAlert("支付被取消");
		} else {
			openAlert("支付失败");
		}
		closeAlertMsgLoad();
	});
}