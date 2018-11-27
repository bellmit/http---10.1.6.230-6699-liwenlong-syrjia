var app = angular.module("nowInquiryApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.filter('subName', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		if(text.length>10){
			return text.substring(0,7)+"...";
		}else{
			return text;
		}
	};
});

app.filter('splitImgs',
		function($sce) { // 可以注入依赖
			return function(text) {
				var str = text;
				 var re=/src="[^"]+/g;
		        var arr=str.match(re),re_arr=[];
		        if(arr!=null&&arr.length>0){
		        for(var i=0,len=arr.length;i<len;i++){
		            re_arr.push(arr[i].substr(5));
		        };
		        var content="";
		        
		        	content+='<div class="know_hot_left">'+
					'	<p>'+text.substr(0,20)+'......</p>'+
					'</div>'+
					'<div class="know_hot_img">'+
					'	<img style="width:124px;hight:68px;" src="'+re_arr[1]+'">'+
					'</div>';
		        }else{
		        	content = text.substr(0,40)+'......';
		        }
				
				return content;
			};
		});
app.filter('splitLabelNames',
		function($sce) { // 可以注入依赖
	return function(text) {
		var arr = text.split(",");
		var re_arr=[];
		if(arr!=null&&arr.length>0){
			var content="";
			for(var i=0,len=arr.length;i<len;i++){
				re_arr.push(arr[i]);
				content+='<span>'+re_arr[i]+'</span>';
			};
		}else{
			content='<span>'+arr+'</span>';
		}
		
		return content;
	};
});
app.filter('splitContent',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = text.substr(0,75)+'......';
		
		return content;
	};
});
app.filter('splitImgAndVideo',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = text.substr(0,50)+'......';
		
		return content;
	};
});
app.filter('splitHosName',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = "";
		if(text.length>8){
			content = text.substr(0,8)+'...';
		}else{
			content = text;
		}
		return content;
	};
});
var goodsName;
app.controller('nowInquiryCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	

	
	var page = 0;
	var row = 20000;
	mui.init({
		pullRefresh : {
			container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
			up : {
				height : 50, // 可选.默认50.触发上拉加载拖动距离
				auto : true, // 可选,默认false.自动上拉加载一次
				contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
				contentnomore : '<p class="commo_none"><span>没有更多订单</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
				callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
					var _this = this;
					page++;
					$http.post(basePath + 'doctorOrder/queryNowInquiry.action', {
						page : page,
						row : row
					}, postCfg)
						.then(function(response) {
							if (response.data && response.data.respCode == 1001) {
								if (response.data.data.length == 0) {
									_this.endPullupToRefresh(true);
								} else {
									if (null != $scope.resultlist) {
										for (var i = 0; i < response.data.data.length; i++) {
											$scope.resultlist.push(response.data.data[i]);
										}
									} else {
										$scope.resultlist = response.data.data;
									}
									if (response.data.data.length < row) {
										_this.endPullupToRefresh(true);
									} else {
										_this.endPullupToRefresh(false);
									}
								}
							} else {
								_this.endPullupToRefresh(true);
							}
								if(null==$scope.resultlist||$scope.resultlist.length==0){
									$(".commo_none").hide();
									$(".notAvailable").show();
								}
								console.log($scope.resultlist);
						});
				}
			}
		}
	});
	
	$scope.toIm=function(doctorId,patientId){
		window.location.href="../im/inquiry.html?identifier="+patientId+"&selToID="+doctorId;
	}
	
	
	$scope.linkhistory=function(){
			window.location.href="casehistory_list.html";
	};
	
	$scope.pay=function(event,recordOrderNo,mainOrderNo){
		event.stopPropagation();
		if(mainOrderNo){
			window.location.href = "../hospital/look_scheme.html?orderNo=" +mainOrderNo
		}else{
			window.location.href = "../hospital/look_scheme.html?orderNo=" +recordOrderNo
		}
	};
	
	$scope.evaluate=function(event,orderNo,orderType){
		event.stopPropagation();
		if(orderNo){
			window.location.href = "../hospital/evaluate_doctor.html?orderNo=" + orderNo;
		}
	};
	
	$scope.logistics=function(event,orderNo){
		event.stopPropagation();
		if(orderNo){
			window.location.href = "../logistics/logistics.html?orderNo=" + orderNo;
		}
	};
	
} ]);

mui('body').on('tap','div',function(event) {
		event.stopPropagation();
		this.click();
});
