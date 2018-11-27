var app = angular.module("mineDoctorApp",
		[ 'ngSanitize', 'angular-loading-bar' ]);

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

app.directive('renderFinish1', function($timeout) { // renderFinish自定义指令
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			if (scope.$last === true) {
				$timeout(function() {
					scope.$emit('ngRepeatFinished1');
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

app.filter('splitIllClass', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		if (text.indexOf(",")) {
			var arr = text.split(",");
			for ( var i = 0; i < arr.length; i++) {
				if (i < 5) {
					content += '<li>' + arr[i] + '</li>';
				} else {
					content += '<li>...</li>';
					break;
				}
			}
		} else {
			content += '<li>' + arr[i] + '</li>';
		}
		return content;
	};
});

app
		.filter('splitMoney',
				function($sce) { // 可以注入依赖
					return function(text) {
						if (!text) {
							return "";
						}
						var content = '';
						if (text.indexOf(";")) {
							var arr = text.split(";");
							for ( var i = 0; i < arr.length; i++) {
								if (arr[i] != null && arr[i] != 0.00
										&& arr[i] != ' ' && i < 4) {
									if (i < 2) {
										content = '¥' + arr[i] + '/次';
									} else {
										content = '¥' + arr[i] + '/'
												+ arr[i + 4] + '次';
									}
									break;
								} else {
									content = '¥/次';
								}
							}
						} else {
							content = '¥/次';
						}
						if(content=='¥/次'){
							return '';
						}
						return content;
					};
				});

app
		.filter(
				'splitJzLists',
				function($sce) { // 可以注入依赖
					return function(text) {
						if (!text) {
							return "";
						}
						var content = '';
						var _docid = "";
						if (text.indexOf(",")) {
							var arr = text.split(",");
							for ( var i = 0; i < arr.length; i++) {
								var detailArr = arr[i].split(";");
								if (detailArr[0] != null && detailArr[0] != ' '
										&& detailArr[0] != "") {
									_docid = detailArr[6];
									if (detailArr[5] == 5) {// 问诊结束
										content += '<li ng-click="toJzDetail($event,\''+_docid+'\',\''+detailArr[7]+'\');"><span><i>'
										+ formatDateYYYYMMDD(detailArr[1])
										+ '</i>'
										+ detailArr[2]
										+ '</span> <span>'
										+ detailArr[3]
										+ '</span> <span>已结束</span> </li>';
									} else {
										content += '<li ng-click="toJzDetail($event,\''+_docid+'\',\''+detailArr[7]+'\');"><span><i>'
										+ formatDateYYYYMMDD(detailArr[1])
										+ '</i>'
										+ detailArr[2]
										+ '</span> <span>'
										+ detailArr[3]
										+ '</span> <span class="mine_ing">进行中</span> </li>';
									}
								}
							}
							if (arr.length > 3) {
								$("#seeMore_" + _docid).show();
							}
						}
						return content;
					};
				});
var obj;
//$(".mine_none").hide();
app
		.controller(
				'mineDoctorCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							obj = $scope;
							var page = 0;
							var row = 20;
							$scope.jzDoctors = [];
							$scope.gzDoctors = [];
							mui
									.init({
										pullRefresh : {
											container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
											up : {
												height : 50, // 可选.默认50.触发上拉加载拖动距离
												auto : true, // 可选,默认false.自动上拉加载一次
												contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
												contentnomore : '<p class="commo_none"><span>没有更多医生</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
												callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
													var _this = this;
													page++;
													$http
															.post(
																	basePath
																			+ 'doctor/queryMyDoctors.action',
																	{
																		"page" : page,
																		"row" : row
																	}, postCfg)
															.then(
																	function(
																			response) {
																		if (response.data
																				&& response.data.respCode == 1001) {
																			if (response.data.data.data.length == 0) {
																				_this
																						.endPullupToRefresh(true);
																				if($scope.jzDoctors.length==0&&$scope.gzDoctors.length==0){
																					$(".mine_none").show();
																				}else{
																					$(".commo_none")
																					.show();
																				}
																			} else {
																				$scope.jzCount = response.data.data.jzCount;
																				$scope.gzCount = response.data.data.gzCount;
																				/*$(
																						"._gzDoctors")
																						.hide();*/
																				if ($scope.jzDoctors != null
																						&& $scope.jzDoctors.length > 0) {
																					for ( var i = 0; i < response.data.data.data.length; i++) {
																						if (response.data.data.data[i].type = 1) {
																							$scope.jzDoctors
																									.push(response.data.data.data[i]);
																						} else {
																							$(
																									"._gzDoctors")
																									.show();
																							$scope.gzDoctors
																									.push(response.data.data.data[i]);
																						}
																					}
																				} else {
																					for ( var i = 0; i < response.data.data.data.length; i++) {
																						if (response.data.data.data[i].type == 1) {
																							$scope.jzDoctors
																									.push(response.data.data.data[i]);
																						} else {
																							$(
																									"._gzDoctors")
																									.show();
																							$scope.gzDoctors
																									.push(response.data.data.data[i]);
																						}
																					}
																				}
																				if (response.data.data.data.length < row) {
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
																		
																		if((null==$scope.gzDoctors||$scope.gzDoctors.length==0)&&(null==$scope.gzCount||$scope.gzCount.length==0)){
																			$(".commo_none").hide();
																			$(".notAvailable").show();
																			$('#pullrefresh').remove();
																		}
																	});
												}
											}
										}
									});

							$scope.doctorDetail = function(event,doctorId,docName) {
								event.stopPropagation();
								window.location.href = "doctor_detail.html?id="+doctorId;
							};
							
							$scope.toJzDetail = function(event,doctroId,patientId) {
								event.stopPropagation();
								//window.location.href = "im_history.html?doctorId="+doctroId+"&patientId="+patientId;
								window.location.href = "../im/inquiry.html?selToID="+doctroId+"&identifier="+patientId;
							};

						} ]);

mui('body').on('tap', 'div,li', function(event) {
	if (this.getAttribute("ng-click") || this.getAttribute("onclick")) {
		this.click();
	}
});

mui('body').on('tap', '.mine_more', function(event) {
		this.click();
});
