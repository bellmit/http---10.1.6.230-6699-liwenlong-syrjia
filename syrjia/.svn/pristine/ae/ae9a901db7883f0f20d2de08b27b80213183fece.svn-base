var app = angular.module("orderApp", [ 'ngSanitize', 'angular-loading-bar']);

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

app.directive('compileHtml', function($sce, $compile) {
	return {
		restrict : 'A',
		replace : true,
		link : function(scope, ele, attrs) {
			scope.$watch(function() {
				return scope.$eval(attrs.ngBind);
			},
				function(html) {
					ele.html(html);
					$compile(ele.contents())(scope);
				});
		}
	};
});

app.filter('break', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var arr = text.split("&");
		var content = '';
		for (var i = 0; i < arr.length; i++) {
			var goods=arr[i].split(",");
			content += '<div class="order_goods" ng-click="detail($event,\''+goods[0]+'\')"><img src="'+goods[4]+'"><div class="order_detail">'
					+'<h3><span>'+goods[1]+'</span> <span>¥'+goods[2]+'</span></h3>'
					+'<p>'+goods[5]+'</p><p class="order_num">&nbsp <span>x '+goods[3]+'</span></p></div></div>';
		}
		return content;
	};
});

app.filter('float', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		return parseFloat(text);
	};
});
app.controller('orderCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {

	$scope.pay=function(orderNo,mainOrderNo){
		window.location.href = "../pay/pay.html?orderNo=" +(isEmpty(mainOrderNo)?orderNo:mainOrderNo);
	};
	
	$http.post(basePath + 'goodsOrder/queryOrderByOrderNo.action', {orderNo:$location.search().orderNo},postCfg).then(function(response){
		if (response.data && response.data.respCode == 1001) {
			
			$scope.order=response.data.data;
			if($scope.order.paymentStatus==1){
				$http.post(basePath + 'member/queryUserAgree.action').then(function(response){
					if (response.data && response.data.respCode == 1001) {
						if(parseInt(response.data.data.isPayWaitTime)==1){
							var second=parseInt(response.data.data.payWaitTime)*60*60;
							second=$scope.order.createTime+second;
							time(new Date().getTime()/1000,second,$scope);
						}
					}else{
						openAlert(response.data.respMsg);
					}
				});
				$scope.order.payState='待付款';
			}
			
			if($scope.order.state==0){
				$scope.order.payState='已作废';
				$scope.order.paymentStatus=6;
			}
		}else{
			openAlert(response.data.respMsg);
		}
	});
	
	$scope.del=function(event,orderNo){
		event.stopPropagation();
		if(orderNo){
			openConfirm("提示","确认删除此订单吗？",function(){
				openAlertMsgLoad("提交中");
				$http.post(basePath + 'order/deleteOrder.action', {state:2,orderNo:orderNo},postCfg).then(function(response){
					if (response.data && response.data.respCode == 1001) {
						openAlert('删除成功',function(){
							mui.back();
						});
					}else{
						openAlert(response.data.respMsg);
					}
					closeAlertMsgLoad();
				});
			});
		}
	};
	
	$scope.cancel=function(event,order){
		event.stopPropagation();
		openConfirm("提示","您确认要取消订单吗？",function(){
			openAlertMsgLoad("提交中");
			$http.post(basePath + 'order/cancelOrder.action', {paymentStatus:6,orderNo:order.orderNo},postCfg).then(function(response){
				if (response.data && response.data.respCode == 1001) {
					order.paymentStatus=6;
					order.payState='已取消';
					openAlert('取消成功');
				}else{
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
		});
	};
	
	$scope.success=function(event,order){
		event.stopPropagation();
		openConfirm("提示","您确定已收到货品？",function(){
			openAlertMsgLoad("提交中");
			$http.post(basePath + 'order/successOrder.action', {paymentStatus:5,orderStatus:5,orderNo:order.orderNo},postCfg).then(function(response){
				if (response.data && response.data.respCode == 1001) {
					order.paymentStatus=5;
					order.orderStatus=5;
					order.payState='已完成';
					openAlert('操作成功');
				}else{
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
		});
	};
	
	$scope.evaluate=function(orderNo){
		if(orderNo){
			window.location.href = "../evaluate/evaluate.html?orderNo=" + orderNo;
		}
	};
	
	$scope.logistics=function(orderNo){
		if(orderNo){
			window.location.href = "../logistics/logistics.html?orderNo=" + orderNo;
		}
	};
	
	$scope.detail=function(event,id){
		event.stopPropagation();
		window.location.href = "../goods/commodity_details.html?goodsId=" + id;
	};
	
	$scope.callCostom=function(event,order){
		event.stopPropagation();
		window.location.href="http://kefu.syrjia.com:8001/chat/Weixin/Chat?orderNo="+order.orderNo+"&detailNo="+order.orderNo+"&orderPrice="+(parseFloat(order.receiptsPrice)+parseFloat(order.postage))+"&orderDate="+order.time+"&orderType="+order.orderType+"&serverType="+order.orderType+"&TrackStatus="+order.orderStatus+"&TradeStatus="+order.paymentStatus+"&orderStatus="+order.orderStatus+"&costomId="+order.memberId+"&costomName="+order.realname+"&costomUrl="+order.headicon+"&costomType=0";
	};
	
} ]);

function time(startTime,endTime,obj){
	 var subTime=endTime-startTime;
	 if(subTime>0){
		 var interval;
		 var minutes=parseInt(subTime/3600);
		 var seconds=parseInt(subTime%3600);
		 seconds=parseInt(seconds/60);
	     obj.dayNum= "剩余"+isDouble(minutes)+'小时'+isDouble(seconds)+"分自动关闭";
	     interval = setInterval(function() {
	    	 minutes=parseInt(subTime/3600);
			 seconds=parseInt(subTime%3600);
			 seconds=parseInt(seconds/60);
	         obj.dayNum= "剩余"+isDouble(minutes)+'小时'+isDouble(seconds)+"分自动关闭";
	         if (subTime<=0) {
	             clearInterval(interval);
	             obj.dayNum= "订单已自动关闭";
	             obj.order.paymentStatus=6;
	             return;
	         }
	         subTime-=60;
	         obj.second-=60;
	         obj.$apply();
	     }, 60000);
	}
} 