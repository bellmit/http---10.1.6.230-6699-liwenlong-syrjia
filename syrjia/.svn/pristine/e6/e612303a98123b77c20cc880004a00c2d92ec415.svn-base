var app = angular.module("commoApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
		var arr = text.split(",");
		var content = '';
		for (var i = 0; i < arr.length; i++) {
			content += '<span>' + arr[i] + '</span>';
		}
		return content;
	};
});

var goodsName;
app.controller('commoCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	goodsName = $location.search().name;
	var showName = $location.search().showName;
	if(!showName){
		showName=goodsName;
	}
	$("#search").attr("placeholder", showName);
	if (goodsName) {
		$scope.searchName=goodsName;
	}
	//查询服务
	$http.post(basePath + 'goods/queryService.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.services=response.data.data.services;
					$scope.brands=response.data.data.brands;
				}else{
					openAlertMsg(response.data.respMsg);
				}
	});

	$scope.service=[];
	$scope.brand=[];
	
	$scope.checkNum=1;
	var page = 0;
	var row = 20;
	mui.init({
		pullRefresh : {
			container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
			up : {
				height : 50, // 可选.默认50.触发上拉加载拖动距离
				auto : true, // 可选,默认false.自动上拉加载一次
				contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
				contentnomore : '<p class="commo_none"><span>没有更多商品</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
				callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
					var _this = this;
					page++;
					$http.post(basePath + 'goods/queryGoods.action', {
						page : page,
						row : row,
						goodsTypeId : $location.search().typeId,
						isRecommend : $location.search().isRecommend,
						name : $scope.searchName,
						order : $scope.order,
						serviceId : $scope.service.join(","),
						brandId : $scope.brand.join(","),
						lowPrice : $scope.lowPrice,
						highPrice : $scope.highPrice
					}, postCfg)
						.then(function(response) {
							if (response.data && response.data.respCode == 1001) {
								$("#search").attr("placeholder", (isEmpty(showName)?'':showName) + " 共" + response.data.data.total + "个结果");
								if (response.data.data.data.length == 0) {
									_this.endPullupToRefresh(true);
									$(".commo_none").show();
									//$(".mui-pull-caption-nomore").hide();
								} else {
									if (null != $scope.goods) {
										for (var i = 0; i < response.data.data.data.length; i++) {
											$scope.goods.push(response.data.data.data[i]);
										}
									} else {
										$scope.goods = response.data.data.data;
									}
									if (response.data.data.data.length < row) {
										_this.endPullupToRefresh(true);
									//_this.disablePullupToRefresh();
									} else {
										_this.endPullupToRefresh(false);
									}
								}
							} else {
								_this.endPullupToRefresh(true);
							//_this.disablePullupToRefresh();
							}
						});
				}
			}
		}
	});
	
	$(".commo_layer").on("click",function(){
		$(".commo_con").css("overflow","auto");
		$(".commo_screen").removeClass("commo_screen_easein")
		$(".commo_btn").removeClass("commo_btn_easein");
		$(".commo_layer").hide();
		$(".commo_screen").hide(100);
		$scope.checkScreening();
	});

	$(".commo_confirm").on("click",function(){
		$(".commo_con").css("overflow","auto");
		$(".commo_screen").removeClass("commo_screen_easein")
		$(".commo_btn").removeClass("commo_btn_easein");
		$(".commo_layer").hide();
		$(".commo_screen").hide(100);
	});
	
	$scope.detail=function(id){
		if(id){
			window.location.href="commodity_details.html?goodsId="+id;
		}
	}

	$scope.search = function(e) {
		var keycode = window.event ? e.keyCode : e.which;
		if (keycode == 13) {
			page = 0;
			$scope.goods = null;
			mui('#pullrefresh').pullRefresh().refresh(true);
			mui('#pullrefresh').pullRefresh().pullupLoading();
			mui('#pullrefresh').pullRefresh().scrollTo(0, 0);
		}
	};
	
	$scope.checkScreening=function(){
		page = 0;
		$scope.goods = null;
		mui('#pullrefresh').pullRefresh().refresh(true);
		mui('#pullrefresh').pullRefresh().pullupLoading();
		mui('#pullrefresh').pullRefresh().scrollTo(0, 0);
	}
	
	$scope.rest=function(){
		$(".check").removeClass("check");
		$scope.brand=[];
		$scope.service=[];
		$scope.lowPrice=null;
		$scope.highPrice=null;
	}
	
	$scope.screening=function(){
		$(".commo_con").css("overflow","hidden");
		$(".commo_layer").show();
		$(".commo_screen").show().addClass("commo_screen_easein");
		$(".commo_btn").addClass("commo_btn_easein");
	};
	
	$scope.checkService=function(id){
		if($("#"+id).hasClass("check")){
			$("#"+id).removeClass("check");
			$scope.service.splice($.inArray(id,$scope.service), 1);
		}else{
			$("#"+id).addClass("check");
			$scope.service.push(id);
		}
	};
	
	$scope.checkBrand=function(id){
		if($("#"+id).hasClass("check")){
			$("#"+id).removeClass("check");
			$scope.brand.splice($.inArray(id,$scope.brand), 1);
		}else{
			$("#"+id).addClass("check");
			$scope.brand.push(id);
		}
	};
	
	var orderSort;
	$scope.check=function(index){
		if($scope.checkNum!=index){
			orderSort=null;
		}
		$scope.checkNum=index;
		if(index==3){
			$(".commo_title li span").removeClass("downRed");
			$(".commo_title li span").removeClass("upRed");
			if(orderSort){
				if(orderSort=="desc"){
					orderSort="asc";
					$scope.order="pasc";
					$(".commo_title li span").addClass("downRed");
				}else{
					orderSort="desc";
					$scope.order="pdesc";
					$(".commo_title li span").addClass("upRed");
				}
			}else{
				$(".commo_title li span").addClass("downRed");
				orderSort="asc";
				$scope.order="pasc";
			}
		}else if(index==2){
			$scope.order="sdesc";
			$(".downRed").removeClass("downRed");
			$(".upRed").removeClass("downRed");
		}else if(index==1){
			orderSort=null;
			$scope.order=null;
			$(".downRed").removeClass("downRed");
			$(".upRed").removeClass("downRed");
		}
		page = 0;
		$scope.goods = null;
		mui('#pullrefresh').pullRefresh().refresh(true);
		mui('#pullrefresh').pullRefresh().pullupLoading();
		mui('#pullrefresh').pullRefresh().scrollTo(0, 0);
	};
	
	$scope.searchClick=function(){
		window.location.href="search.html";
	}

} ]);

mui('body').on('tap', 'li,a', function(event) {
	if ((this.getAttribute("ng-click")||this.getAttribute("onclick"))&&!isWindow) {
		event.stopPropagation();
		this.click();
	}
});