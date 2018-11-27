var app = angular.module("orderApp", [ 'ngSanitize', 'angular-loading-bar','infinite-scroll']);

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
			content += '<div class="order_goods" ng-click="detail(\''+goods[0]+'\')"><img src="'+goods[4]+'"><div class="order_detail">'
					+'<h3><span>'+goods[1]+'</span> <span>¥'+goods[2]+'</span></h3>'
					+'<p>'+goods[5]+'</p><p class="order_num">&nbsp<span>x '+goods[3]+'</span></p></div></div>';
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

app.filter('length', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		return text.split("&").length;
	};
});
var page1=0,page2=0,page3=0,page4=0,row=20;
app.controller('orderCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	
	$scope.check=$location.search().check;
	if(isEmpty($scope.check)){
		$scope.check=1;
	}
	
	/*window.addEventListener("swipeleft",function(event){
		event.stopPropagation();
		$scope.check=$scope.check+1>4?4:$scope.check+1;
		$scope.$apply();
	});
	window.addEventListener("swiperight",function(event){
		event.stopPropagation();
		$scope.check=$scope.check-1<1?1:$scope.check-1;
		$scope.$apply();
	});*/
	$scope.detail=function(event,id){
		//event.stopPropagation();
		//window.location.href = "../goods/commodity_details.html?goodsId=" + id;
	};
	
	$scope.checkTab=function(index){
		window.location.href = "my_order.html?check=" + index;
	}

	$scope.pay=function(event,orderNo){
		event.stopPropagation();
		window.location.href = "../pay/pay.html?orderNo=" + orderNo;
	};
	
	$scope.search=function(e){
		var keycode = window.event?e.keyCode:e.which;
		if(keycode==13){
			$scope.queryOrderByOrderNo();
		};
	};
	
	$scope.queryOrderByOrderNo=function(){
		if($scope.check==1){
			 $scope.$broadcast('changeAll', $scope.orderNoSearch);
		}else if($scope.check==2){
			$scope.$broadcast('changeDFK', $scope.orderNoSearch);
		}else if($scope.check==3){
			$scope.$broadcast('changeDSH', $scope.orderNoSearch);
		}else if($scope.check==4){
			$scope.$broadcast('changeYWC', $scope.orderNoSearch);
		}
	};
	
	$scope.refurbishOrderByOrderNo=function(){
		if($scope.check==1){
			 $scope.$broadcast('changeDFK', $scope.orderNoSearch);
			 $scope.$broadcast('changeDSH', $scope.orderNoSearch);
			 $scope.$broadcast('changeYWC', $scope.orderNoSearch);
		}else if($scope.check==2){
			 $scope.$broadcast('changeAll', $scope.orderNoSearch);
			 $scope.$broadcast('changeDSH', $scope.orderNoSearch);
			 $scope.$broadcast('changeYWC', $scope.orderNoSearch);
		}else if($scope.check==3){
			 $scope.$broadcast('changeAll', $scope.orderNoSearch);
			 $scope.$broadcast('changeDFK', $scope.orderNoSearch);
			 $scope.$broadcast('changeYWC', $scope.orderNoSearch);
		}else if($scope.check==4){
			 $scope.$broadcast('changeAll', $scope.orderNoSearch);
			 $scope.$broadcast('changeDFK', $scope.orderNoSearch);
			 $scope.$broadcast('changeDSH', $scope.orderNoSearch);
		}
	};
	
	$scope.del=function(event,orderNo){
		event.stopPropagation();
		if(orderNo){
			openConfirm("提示","确认删除此订单吗？",function(){
				openAlertMsgLoad("提交中");
				$http.post(basePath + 'order/deleteOrder.action', {state:2,orderNo:orderNo},postCfg).then(function(response){
					if (response.data && response.data.respCode == 1001) {
						$(event.target).parents(".order_con").remove();
						openAlert('删除成功');
						$scope.refurbishOrderByOrderNo();
					}else{
						openAlert(response.data.respMsg);
					}
					closeAlertMsgLoad();
				});
			});
		}
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
					$scope.refurbishOrderByOrderNo();
				}else{
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
		});
	};
	
	$scope.orderDetail=function(event,orderNo){
		event.stopPropagation();
		if(orderNo){
			window.location.href = "order_detail.html?orderNo=" + orderNo;
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
					$scope.refurbishOrderByOrderNo();
				}else{
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
		});
	};
	
	$scope.evaluate=function(event,orderNo){
		event.stopPropagation();
		if(orderNo){
			window.location.href = "../evaluate/evaluate.html?orderNo=" + orderNo;
		}
	};
	
	$scope.logistics=function(event,orderNo){
		event.stopPropagation();
		if(orderNo){
			window.location.href = "../logistics/logistics.html?orderNo=" + orderNo;
		}
	};
	
	$scope.callCostom=function(event,order){
		event.stopPropagation();
		window.location.href="http://kefu.syrjia.com:8001/chat/Weixin/Chat?orderNo="+order.orderNo+"&detailNo="+order.orderNo+"&orderPrice="+(parseFloat(order.receiptsPrice)+parseFloat(order.postage))+"&orderDate="+order.time+"&orderType="+order.orderType+"&serverType="+order.orderType+"&TrackStatus="+order.orderStatus+"&TradeStatus="+order.paymentStatus+"&orderStatus="+order.orderStatus+"&costomId="+order.memberId+"&costomName="+order.realname+"&costomUrl="+order.headicon+"&costomType=0";
	};
	
} ]);


app.controller('orderAll', function($scope, $http,$sce) {
	$scope.loadMore1=function(obj){
		$scope.orderAllDis=true;
		page1++;
		var _this=this;
		$http.post(basePath + 'goodsOrder/queryOrderList.action', {
			page : page1,
			row : row,
			orderNo:$scope.orderNo
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.orders) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.orders.push(response.data.data[i]);
					}
				} else {
					$scope.orders = response.data.data;
				}
				if(response.data.data.length<row){
					if(page1==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						loadOver(obj);
					}
					$scope.orderAllDis=true;
				}else{
					$scope.orderAllDis=false;
				}
			}else{
				loadOver(obj);
				$scope.orderAllDis=true;
			}
			});
		};
		
	$scope.$on('changeAll', function(event, data) {
		page1=0;
		$(".all").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".all").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.orderNo=data;
		$scope.orders=null;
		$scope.loadMore1("all");
	});  
});

app.controller('dfk', function($scope, $http) {
	$scope.loadMore2=function(obj){
		$scope.orderDfkDis=true;
		page2++;
		var _this=this;
		$http.post(basePath + 'goodsOrder/queryOrderList.action', {
			page : page2,
			row : row,
			paymentStatus : 1,
			orderNo:$scope.orderNo
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.orders) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.orders.push(response.data.data[i]);
					}
				} else {
					$scope.orders = response.data.data;
				}
				if(response.data.data.length<row){
					if(page1==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						loadOver(obj);
					}
					$scope.orderDfkDis=true;
				}else{
					$scope.orderDfkDis=false;
				}
			}else{
				loadOver(obj);
				$scope.orderDfkDis=true;
			}
			});
		};
	$scope.$on('changeDFK', function(event, data) {
		page2=0;
		$(".dfk").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".dfk").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.orderNo=data;
		$scope.orders=null;
		$scope.loadMore2("dfk");
	});  
});

app.controller('dsh', function($scope, $http) {
	$scope.loadMore3=function(obj){
		$scope.orderDSHDis=true;
		page3++;
		var _this=this;
		$http.post(basePath + 'goodsOrder/queryOrderList.action', {
			page : page3,
			row : row,
			paymentStatus : 2,
			orderStatus : 12,
			orderNo:$scope.orderNo
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.orders) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.orders.push(response.data.data[i]);
					}
				} else {
					$scope.orders = response.data.data;
				}
				if(response.data.data.length<row){
					if(page1==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						loadOver(obj);
					}
					$scope.orderDSHDis=true;
				}else{
					$scope.orderDSHDis=false;
				}
			}else{
				loadOver(obj);
				$scope.orderDSHDis=true;
			}
			});
		};
	$scope.$on('changeDSH', function(event, data) {
		page3=0;
		$(".dsh").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".dsh").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.orderNo=data;
		$scope.orders=null;
		$scope.loadMore3("dsh");
	});  
});

app.controller('ywc', function($scope, $http) {
	$scope.loadMore4=function(obj){
		$scope.orderYWCDis=true;
		page4++;
		var _this=this;
		$http.post(basePath + 'goodsOrder/queryOrderList.action', {
			page : page4,
			row : row,
			//orderStatus : 5,
			paymentStatus : 5,
			orderNo:$scope.orderNo
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.orders) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.orders.push(response.data.data[i]);
					}
				} else {
					$scope.orders = response.data.data;
				}
				if(response.data.data.length<row){
					if(page1==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						loadOver(obj);
					}
					$scope.orderYWCDis=true;
				}else{
					$scope.orderYWCDis=false;
				}
			}else{
				loadOver(obj);
				$scope.orderYWCDis=true;
			}
			});
		};
	$scope.$on('changeYWC', function(event, data) {
		page4=0;
		$(".ywc").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".ywc").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.orderNo=data;
		$scope.orders=null;
		$scope.loadMore4("ywc");
	});  
});

$(".scroll").height($(window).height()-70-$(".order_title").height()-10);