$(function() {
	/*var marTop = $(".evaluate_title").height();
	$("#pullrefresh").css("marginTop", (marTop / 16) + "rem");

	var H = $(window).height();
	$(".evaluate_main").height(H);*/

});

var app = angular.module("doctorEvaApp",
		[ 'ngSanitize', 'angular-loading-bar','infinite-scroll' ]);

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

app.filter('splitLables', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		var illlength;
		if (text.indexOf(",")) {
			var arr = text.split(",");
			for ( var i = 0; i < arr.length; i++) {
				content += '<li>' + arr[i] + '</li>';
			}
		} else {
			content += '<li>' + arr[i] + '</li>';
		}
		return content;
	};
});

app.filter('splitEvaImgs', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		if (text.indexOf(",") > -1) {
			var arr = text.split(",");
			for ( var i = 0; i < arr.length; i++) {
				var iarr = arr[i].split(";");
				
				if(iarr[0]>1){
					content += '<span class="clinic_flower"><img ng-click="showBigImg($event.target);" src="' + iarr[1] + '"><i>X'+iarr[0]+'</i></span>';
				}else{
					content += '<span><img ng-click="showBigImg($event.target);" src="' + iarr[1] + '"></span>';
				}
			}
		} else {
			var arr = text.split(";");
			content += '<span><img ng-click="showBigImg($event.target);" src="' + arr[1] + '"> </span>';
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
var doctorId;
app
		.controller(
				'doctorEvaCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							$scope.showBig = 0;
							$scope.illShow = 0;
							$scope.evaTab = 1;
							
							doctorId = $location.search().id;
							if (isEmpty($location.search().id)) {
								openAlertMsg("医生信息不存在或设置隐身");
							} else {
								/*$http
										.post(
												basePath
														+ 'doctor/queryDoctorEvaCensus.action',
												{
													doctorId : $location
															.search().id
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
														$scope.totalEvaCnt = response.data.data.totalEvaCnt;
														$scope.goodEvaCnt = response.data.data.goodEvaCnt;
														$scope.middleEvaCnt = response.data.data.middleEvaCnt;
														$scope.lowEvaCnt = response.data.data.lowEvaCnt;
														$scope.goodEvaRate = response.data.data.goodEvaRate;
													} else {
														openAlertMsg("医生信息不存在或设置隐身");
													}
												});*/
								$http
								.post(
										basePath
												+ 'evaluate/queryEvaluateNum.action',
										{
											goodsId : $location
													.search().id
										}, postCfg)
								.then(
										function(response) {
											if (response.data
													&& response.data.respCode == 1001) {
												$scope.totalEvaCnt = response.data.data.qb;
												$scope.goodEvaCnt = response.data.data.hp;
												$scope.middleEvaCnt = response.data.data.zp;
												$scope.lowEvaCnt = response.data.data.cp;
												$scope.goodEvaRate = response.data.data.rate;
											} else {
												openAlertMsg("医生信息不存在或设置隐身");
											}
										});
								
								
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

								/*mui
										.init({
											pullRefresh : {
												container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
												up : {
													height : 50, // 可选.默认50.触发上拉加载拖动距离
													auto : true, // 可选,默认false.自动上拉加载一次
													contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
													contentnomore : '<p class="commo_none"><span>没有更多评价</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
													callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
														var _this = this;
														page++;
														$http
																.post(
																		basePath
																				+ 'doctor/queryDoctorEvaList.action',
																		{
																			page : page,
																			row : row,
																			doctorId : $location
																					.search().id,
																			_sign : $scope._sign
																		},
																		postCfg)
																.then(
																		function(
																				response) {
																			if (response.data
																					&& response.data.respCode == 1001) {
																				if (response.data.data.length == 0) {
																					_this
																							.endPullupToRefresh(true);
																					$(
																							".commo_none")
																							.show();
																				} else {
																					if (null != $scope.docEvas) {
																						for ( var i = 0; i < response.data.data.length; i++) {
																							$scope.docEvas
																									.push(response.data.data[i]);
																						}
																					} else {
																						$scope.docEvas = response.data.data;
																					}
																					if (response.data.data.length < row) {
																						_this
																								.endPullupToRefresh(true);
																					} else {
																						_this
																								.endPullupToRefresh(false);
																					}
																				}
																			} else {
																				_this
																						.endPullupToRefresh(true);
																			}
																		});
													}
												}
											}
										});*/
							}

							/*$scope.searchDocEva = function(event, _sign, num) {
								event.stopPropagation();
								$scope._sign = _sign;
								$scope.evaTab = num;
								page = 0;
								$scope.docEvas = null;
								mui('#pullrefresh').pullRefresh().refresh(true);
								mui('#pullrefresh').pullRefresh()
										.pullupLoading();
								mui('#pullrefresh').pullRefresh()
										.scrollTo(0, 0);
							};*/

							/**
							 * 查看大图
							 */
							
							$scope.showBigImg=function(target){
								var pic=target.getAttribute("src");
								$(".layer_con").find("img").attr("src",pic);
								$(".evaluate_main").css("overflow","hidden");
								$(".layer_con").show();
							};
							
							$scope.hideBigImg=function(){
								$(".layer_con").hide();
								$(".evaluate_main").css("overflow", "auto");
							};
							
							myscroll=new IScroll(".scroll",{click:true,eventPassthrough:true});

						} ]);


app.controller('evalAll', function($scope, $http,$sce) {
	$scope.loadMore1=function(obj){
		$scope.evalAllDis=true;
		page1++;
		var _this=this;
		$http.post(basePath + 'doctor/queryDoctorEvaList.action', {
			page : page1,
			row : row,
			doctorId:doctorId,
			_sign:"all"
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.docEvas) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.docEvas.push(response.data.data[i]);
					}
				} else {
					$scope.docEvas = response.data.data;
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

app.controller('hp', function($scope, $http) {
	$scope.loadMore2=function(obj){
		$scope.evalHPDis=true;
		page2++;
		var _this=this;
		$http.post(basePath + 'doctor/queryDoctorEvaList.action', {
			page : page2,
			row : row,
			doctorId:doctorId,
			_sign:"good"
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.docEvas) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.docEvas.push(response.data.data[i]);
					}
				} else {
					$scope.docEvas = response.data.data;
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
app.controller('zp', function($scope, $http) {
	$scope.loadMore3=function(obj){
		$scope.evalZPDis=true;
		page3++;
		var _this=this;
		$http.post(basePath + 'doctor/queryDoctorEvaList.action', {
			page : page3,
			row : row,
			doctorId:doctorId,
			_sign:"middle",
			level:2
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.docEvas) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.docEvas.push(response.data.data[i]);
					}
				} else {
					$scope.docEvas = response.data.data;
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
app.controller('cp', function($scope, $http) {
	$scope.loadMore4=function(obj){
		$scope.evalCPDis=true;
		page4++;
		var _this=this;
		$http.post(basePath + 'doctor/queryDoctorEvaList.action', {
			page : page4,
			row : row,
			doctorId:doctorId,
			_sign:"low",
			level:3
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.docEvas) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.docEvas.push(response.data.data[i]);
					}
				} else {
					$scope.docEvas = response.data.data;
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


mui('body').on('tap', 'li,a,img,div', function(event) {
	// &&!isWindow
	if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))) {
		event.stopPropagation();
		this.click();
	}
});

$(".scroll").height($(window).height()-30-$(".evaluate_title").height()-10);
