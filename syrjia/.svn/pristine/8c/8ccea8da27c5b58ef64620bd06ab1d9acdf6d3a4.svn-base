var app = angular.module("lineBelowApp",
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
var cDate = getQueryString(window.location.href, "date");
app
		.filter(
				'splitZzData',
				function($sce) { // 可以注入依赖
					return function(text) {
						if (!text) {
							return "";
						}
						var content = '';
						if (text.indexOf("@") > -1) {
							var arr = text.split("@");
							content += '<div class="line_time"><h3 class="line_title"><span class="left_icon"></span>'
									+ arr[0] + '</h3>';
							if (arr[1].indexOf(",") > -1) {
								var zzarry = arr[1].split(",");
								for ( var i = 0; i < zzarry.length; i++) {
									var iarr = zzarry[i].split(";");
									if (iarr[2] == 1) {
										content += '<div class="line_address">'
												+ '<h3>'
												+ iarr[4]
												+ '<span>剩余0/'
												+ iarr[3]
												+ '</span> <i>¥'
												+ parseInt(iarr[1])
												+ '/次</i>'
												+ '</h3><p><span>'
												+ iarr[5]
												+ '</span><a ng-click="tobuyLine(\''
												+ arr[0]
												+ '\');">预约</a></p></div>';
									} else {
										content += '<div class="line_address">'
												+ '<h3>' + iarr[4] + '<i>¥'
												+ parseInt(iarr[1]) + '/次</i>'
												+ '</h3><p>' + iarr[5]
												+ '</p></div>';
									}
								}
							} else {
								var iarr = arr[1].split(";");
								if (iarr[2] == 1) {
									content += '<div class="line_address">'
											+ '<h3>'
											+ iarr[4]
											+ '<span>剩余0/'
											+ iarr[3]
											+ '</span> <i>¥'
											+ parseInt(iarr[1])
											+ '/次</i>'
											+ '</h3><p><span>'
											+ iarr[5]
											+ '</span><a ng-click="tobuyLine(\''
											+ arr[0] + '\');">预约</a></p></div>';
								} else {
									content += '<div class="line_address">'
											+ '<h3>' + iarr[4] + '<i>¥'
											+ parseInt(iarr[1]) + '/次</i>' + '</h3><p>'
											+ iarr[5] + '</p></div>';
								}
							}
							content += '</div>';
						}
						return content;
					};
				});
var isss = true;
app
		.filter(
				'splitDate',
				function($sce) { // 可以注入依赖
					return function(text) {
						if (!text) {
							return "";
						}
						// 当前日期
						var myDate = new Date;
						var yy = myDate.getFullYear();
						var mm = myDate.getMonth() + 1;
						var dd = myDate.getDate();
						var nowdate = yy + '-' + (mm < 10 ? '0' + mm : mm)
								+ '-' + (dd < 10 ? '0' + dd : dd);
						var content = '';
						var atr = text.split("@");
						if (!isEmpty(cDate)) {
							if (nowdate == cDate&&isss) {
								content += '<li class="line_today" _num="'+atr[2]+'" id="'
										+ atr[0]
										+ '"  ng-click="clickThis($event,\''
										+ atr[0] + '\');" ><span>' + atr[1]
										+ '</span><p>今天</p></li>';
								isss = false;
							} else {
								/*if (nowdate == atr[0]) {
									alert(1);
									content += '<li  id="'
											+ atr[0]
											+ '"  ng-click="clickThis($event,\''
											+ atr[0] + '\');" ><span>' + atr[1]
											+ '</span><p>今天</p></li>';
								} else {*/
									if (cDate == atr[0]
											&& (atr[2] == null || atr[2] == ' ' || atr[2] == "")) {
										content += '<li class="line_today" _num="'+atr[2]+'" id="'
										+ atr[0]
										+ '" ng-click="clickThis($event,\''
												+ atr[0]
												+ '\');" ><span>'
												+ atr[1]
												+ '</span><p>休</p></li>';
									} else {
										if (atr[2] == null || atr[2] == ' '
												|| atr[2] == "") {
											content += '<li _num="'+atr[2]+'" id="'
												+ atr[0]
											+ '" ng-click="clickThis($event,\''
													+ atr[0]
													+ '\');" ><span>'
													+ atr[1]
													+ '</span><p>休</p></li>';
										} else {
											if (atr[2].indexOf("1") > -1) {
												content += '<li _num="'+atr[2]+'" class="line_order" id="'
													+ atr[0]
												+ '"  ng-click="clickThis($event,\''
														+ atr[0]
														+ '\');" ><span>'
														+ atr[1]
														+ '</span><p>可预约</p></li>';
											} else {
												content += '<li _num="'+atr[2]+'" class="line_out" id="'
													+ atr[0]
												+ '"  ng-click="clickThis($event,\''
														+ atr[0]
														+ '\');" ><span>'
														+ atr[1]
														+ '</span><p>出诊</p></li>';
											}
										}
									}
							}
						} else {
							if (nowdate == atr[0]) {
								content += '<li _num="'+atr[2]+'" class="line_today" id="'
									+ atr[0]
								+ '" ng-click="clickThis($event,\''
										+ atr[0]
										+ '\');" ><span>'
										+ atr[1]
										+ '</span><p>今天</p></li>';
							} else {
								if (atr[2] == null || atr[2] == ' '
										|| atr[2] == "") {
									content += '<li _num="'+atr[2]+'" id="'
										+ atr[0]
									+ '" ng-click="clickThis($event,\''
											+ atr[0]
											+ '\');" ><span>'
											+ atr[1]
											+ '</span><p>休</p></li>';
								} else {
									if (atr[2].indexOf("1") > -1) {
										content += '<li _num="'+atr[2]+'" class="line_order" id="'
										+ atr[0]
										+ '"  ng-click="clickThis($event,\''
												+ atr[0]
												+ '\');" ><span>'
												+ atr[1]
												+ '</span><p>可预约</p></li>';
									} else {
										content += '<li _num="'+atr[2]+'" class="line_out" id="'
										+ atr[0]
										+ '"  ng-click="clickThis($event,\''
												+ atr[0]
												+ '\');" ><span>'
												+ atr[1]
												+ '</span><p>出诊</p></li>';
									}
								}
							}
						}
						return content;
					};
				});
var doctorId;
var oobj;
app.controller('lineBelowCon', [
		'$scope',
		'$location',
		'$http',
		'$sce',
		'$filter',
		function($scope, $location, $http, $sce, $filter) {
			oobj = $scope;
			var date = $location.search().date;
			doctorId = $location.search().id;
			if (isEmpty($location.search().date)) {
				// 当前日期
				var myDate = new Date;
				var yy = myDate.getFullYear();
				var mm = myDate.getMonth() + 1;
				var dd = myDate.getDate();
				date = yy + '年' + mm + '月' + dd + '日';
				$scope.clickDate = yy + '-' + mm + '-' + dd;
			} else {
				$scope.clickDate = date;
				var searchDate = date.split("-");
				date = searchDate[0] + '年' + Number(searchDate[1]) + '月'
						+ Number(searchDate[2]) + '日';
			}
			/* $(".line_date").find("h3").html(date); */
			$scope.selectDate = date;

			// 星期
			var date = new Date();
			var week = [ '日', '一', '二', '三', '四', '五', '六' ];
			var warr = [];
			for ( var i = 0; i <= 6; i++) {
				if (i == 0) {
					showWeek = week[date.getDay()];
				} else {
					date.setDate(date.getDate() + 1);
					showWeek = week[date.getDay()];
				}
				warr.push(showWeek);
			}
			;
			$scope.week = warr;

			if (!isEmpty($location.search().id)) {
				$http.post(basePath + 'doctor/queryZzDataCountByDocId.action',
						{
							doctorId : $location.search().id
						}, postCfg).then(function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.dates = response.data.data;
					}
				});

				$http.post(basePath + 'doctor/queryDoctorZzList.action', {
					doctorId : $location.search().id,
					zzDate : $location.search().date
				}, postCfg).then(function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.zzDatas = response.data.data;
					}
				});
			}

			$scope.clickThis = function(event, zzDate) {
				event.stopPropagation();
				// 当前日期
				var myDate = new Date;
				var yy = myDate.getFullYear();
				var mm = myDate.getMonth() + 1;
				var dd = myDate.getDate();
				var nowdate = yy + '-' + (mm < 10 ? '0' + mm : mm)
						+ '-' + (dd < 10 ? '0' + dd : dd);
				var searchDate = zzDate.split("-");
				date = searchDate[0] + '年' + Number(searchDate[1]) + '月'
						+ Number(searchDate[2]) + '日';
				$scope.selectDate = date;
				// line_today
				if(zzDate!=nowdate&&$("#"+nowdate).hasClass("line_today")&&$("#"+nowdate).attr("_num")!=' '){
					$("#"+nowdate).addClass("line_out");
				}
				$(".line_day").find("li").removeClass("selectDay").removeClass("line_today");
				$(event.target).addClass("selectDay");
				$http.post(basePath + 'doctor/queryDoctorZzList.action', {
					doctorId : $location.search().id,
					zzDate : zzDate
				}, postCfg).then(function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.zzDatas = response.data.data;
					}
				});
			};

		} ]);

mui('body').on(
		'tap',
		'li,a',
		function(event) {
			if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))
					&& !isWindow) {
				event.stopPropagation();
				this.click();
			}
		});
