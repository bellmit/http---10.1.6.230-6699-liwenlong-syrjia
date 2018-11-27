var app = angular.module("caseHistoryListApp", [ 'ngSanitize',
                                                 'angular-loading-bar' ]);

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
		return content;
	};
});

var obj;
$(".mine_none").hide();
app
.controller(
		'caseHistoryListCon',
		[
		 '$scope',
		 '$location',
		 '$http',
		 '$sce',
		 '$filter',
		 function($scope, $location, $http, $sce, $filter) {
			 obj = $scope;
			 if ($location.search().id) {
				 $scope.patientId = $location.search().id;
			 }
			 $http
			 .post(
					 basePath
					 + 'doctor/queryMedicalRecordsCons.action')
					 .then(
							 function(response) {
								 if (response.data
										 && response.data.respCode == 1001) {
									 $scope.serverTypes = response.data.data.serverTypes;
									 $scope.patients = response.data.data.patients;
									 $scope
									 .$on(
											 'ngRepeatFinished',
											 function() {
												 if ($location
														 .search().id) {
													 $("#" + $location
															 .search().id).addClass("doctor_checkon")
															 .siblings("p").removeClass(
															 "doctor_checkon");
													 var resultHtml = $(
															 "#"
															 + $location
															 .search().id)
															 .find(
															 "i")
															 .html();
													 $(
															 "#"
															 + $location
															 .search().id)
															 .parent()
															 .parent()
															 .prev()
															 .addClass(
																	 "doctor_result")
																	 .html(
																			 resultHtml);
												 }
											 });
								 }
							 });
			 var page = 0;
			 var row = 20;
			 mui
			 .init({
				 pullRefresh : {
					 container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
					 up : {
						 height : 50, // 可选.默认50.触发上拉加载拖动距离
						 auto : true, // 可选,默认false.自动上拉加载一次
						 contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
						 contentnomore : '<div class="commo_none"><span>没有更多档案</span></div>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
						 callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
							 var _this = this;
							 page++;
							 $http
							 .post(
									 basePath
									 + 'doctor/queryMedicalRecords.action',
									 {
										 "page" : page,
										 "row" : row,
										 "orderType" : $scope.orderType,
										 "patientId" : $scope.patientId
									 }, postCfg)
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
														 if (null != $scope.records) {
															 for ( var i = 0; i < response.data.data.length; i++) {
																 $scope.records
																 .push(response.data.data[i]);
															 }
														 } else {
															 $scope.records = response.data.data;
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
												 if(null==$scope.records||$scope.records.length==0){
														$(".commo_none").hide();
														$(".notAvailable").show();
												 }else{
													 $(".commo_none").show();
														$(".notAvailable").hide();
												 }
											 });
						 }
					 }
				 }
			 });

			 $scope.recordDetail = function(event, doctorId,patientId) {
				 event.stopPropagation();
				 window.location.href = "../hospital/im_history.html?doctorId="+doctorId+"&patientId="+patientId;
			 };

			 $scope.checkSearch = function(event,num, _sign, id) {
				 event.stopPropagation();
				 if (num == -1) {
					 if (_sign == 'type') {
						 $scope.orderType = null;
					 } else {
						 $scope.patientId = null;
					 }
				 } else {
					 if (_sign == 'type') {
						 $scope.orderType = id;
						 id = "server_" + id;
					 } else {
						 $scope.patientId = id;
					 }
				 }
				 $("#" + id).parent().parent().css("display","none");
				 $("#" + id).addClass("doctor_checkon").siblings("p").removeClass("doctor_checkon");
				 var resultHtml = $("#" + id).find("i").html();
				 $("#" + id).parent().parent().prev().addClass("doctor_result").html(resultHtml);
				 $(".doctor_layer").removeClass("doctor_show");
				 $(".case_con").css("overflow", "auto");
				 page = 0;
				 $scope.records = null;
				 mui('#pullrefresh').pullRefresh().refresh(true);
				 mui('#pullrefresh').pullRefresh()
				 .pullupLoading();
				 mui('#pullrefresh').pullRefresh()
				 .scrollTo(0, 0);
			 };

		 } ]);
mui('body').on(
		'tap',
		'.case_list li,p',
		function(event) {
			if ((this.getAttribute("ng-click") || this
					.getAttribute("onclick"))
					&& !isWindow) {
				event.stopPropagation();
				this.click();
			} else if (this.getAttribute("href")) {
				window.location.href = this.getAttribute("href");
			}
		});
mui('body').on('tap','.doctor_layer',
		function(event) {
	$(".doctor_layer").removeClass("doctor_show");
});
