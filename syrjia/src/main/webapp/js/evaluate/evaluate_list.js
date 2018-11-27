var app = angular
		.module("evaluateApp", [ 'ngSanitize', 'angular-loading-bar','infinite-scroll' ]);

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
			}, function(html) {
				ele.html(html);
				$compile(ele.contents())(scope);
			});
		}
	};
});

app.filter('break', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var arr = text.split(",");
		var content = '';
		for ( var i = 0; i < arr.length; i++) {
			content += '<li>'+arr[i]+'</li>';
		}
		return content;
	};
});

app.filter('breakPic', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var arr = text.split(",");
		var content = '';
		for ( var i = 0; i < arr.length; i++) {
			content += '<span> <img src="'+arr[i]+'" ng-click="bigPic($event.target)"> </span>';
		}
		return content;
	};
});

app.filter('realName',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = '';
		if(text.length==1){
			return text;
		}else if(text.length==2){
			return text.substring(0,1)+"*"
		}else if(text.length>2){
			return text.substring(0,1)+"**"+text.substring(text.length-1,text.length)
		}
		return content;
	};
});


var row=10;
var page1=0;
var page2=0;
var page3=0;
var page4=0;
var page5=0;
var goodsId;
app.controller('evaluateCon', [ '$scope', '$location', '$http', '$sce',
		'$filter', function($scope, $location, $http, $sce, $filter) {
	goodsId=$location.search().goodsId;
	//查询好评差评率
	$http.post(basePath + 'evaluate/queryEvaluateNum.action',{goodsId:goodsId},postCfg).then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
						$scope.rate=response.data.data.rate;
						$scope.cpNum=response.data.data.cp;
						$scope.hpNum=response.data.data.hp;
						$scope.zpNum=response.data.data.zp;
						$scope.allNum=response.data.data.qb;
				}
	});
	//评价tab左滑
	window.addEventListener("swipeleft",function(event){
		event.stopPropagation();
		$scope.check=$scope.check+1>4?4:$scope.check+1;
		$scope.$apply();
	});
	//评价tab右滑
	window.addEventListener("swiperight",function(event){
		event.stopPropagation();
		$scope.check=$scope.check-1<1?1:$scope.check-1;
		$scope.$apply();
	});
	//点击查看大图
	$scope.bigPic=function(target){
		var pic=target.getAttribute("src");
		$(".layer_con").find("img").attr("src",pic);
		$(".evaluate_main").css("overflow","hidden");
		$(".layer_con").show();
	};
	//关闭大图
	$scope.closeLayer=function(){
		$(".layer_con").hide();
		$(".evaluate_main").css("overflow", "auto");
	};
	
} ]);
//所有评论
app.controller('evalAll', function($scope, $http,$sce) {
	$scope.loadMore1=function(obj){
		$scope.orderAllDis=true;
		page1++;
		var _this=this;
		$http.post(basePath + 'evaluate/queryEvaluateList.action', {
			page : page1,
			row : row,
			goodsId:goodsId,
			type:1
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.evals) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.evals.push(response.data.data[i]);
					}
				} else {
					$scope.evals = response.data.data;
				}
				if(response.data.data.length<row){
					loadOver(obj);
					$scope.evalAllDis=true;
				}else{
					$scope.evalAllDis=false;
				}
			}else{
				loadOver(obj);
				$scope.evalAllDis=true;
			}
			});
		};
});
//好评
app.controller('hp', function($scope, $http) {
	$scope.loadMore2=function(obj){
		$scope.evalHPDis=true;
		page2++;
		var _this=this;
		$http.post(basePath + 'evaluate/queryEvaluateList.action', {
			page : page2,
			row : row,
			goodsId:goodsId,
			type:1,
			level:1
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.evals) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.evals.push(response.data.data[i]);
					}
				} else {
					$scope.evals = response.data.data;
				}
				if(response.data.data.length<row){
					loadOver(obj);
					$scope.evalHPDis=true;
				}else{
					$scope.evalHPDis=false;
				}
			}else{
				loadOver(obj);
				$scope.evalHPDis=true;
			}
			});
		};
});
//中评
app.controller('zp', function($scope, $http) {
	$scope.loadMore3=function(obj){
		$scope.evalZPDis=true;
		page3++;
		var _this=this;
		$http.post(basePath + 'evaluate/queryEvaluateList.action', {
			page : page3,
			row : row,
			goodsId:goodsId,
			type:1,
			level:2
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.evals) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.evals.push(response.data.data[i]);
					}
				} else {
					$scope.evals = response.data.data;
				}
				if(response.data.data.length<row){
					loadOver(obj);
					$scope.evalZPDis=true;
				}else{
					$scope.evalZPDis=false;
				}
			}else{
				loadOver(obj);
				$scope.evalZPDis=true;
			}
			});
		};
});
//差评
app.controller('cp', function($scope, $http) {
	$scope.loadMore4=function(obj){
		$scope.evalCPDis=true;
		page4++;
		var _this=this;
		$http.post(basePath + 'evaluate/queryEvaluateList.action', {
			page : page4,
			row : row,
			goodsId:goodsId,
			type:1,
			level:3
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.evals) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.evals.push(response.data.data[i]);
					}
				} else {
					$scope.evals = response.data.data;
				}
				if(response.data.data.length<row){
					loadOver(obj);
					$scope.evalCPDis=true;
				}else{
					$scope.evalCPDis=false;
				}
			}else{
				loadOver(obj);
				$scope.evalCPDis=true;
			}
			});
		};
});
$(".scroll").height($(window).height()-70-$(".order_title").height()-10);