var app = angular.module("doctorNoticeApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app.controller('doctorNoticeCon', [ '$scope', '$location', '$http', '$sce', '$filter',
		function($scope, $location, $http, $sce, $filter) {
			var page = 0;
			var row = 20;
			mui.init({
				pullRefresh : {
					container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
					up : {
						height : 50, // 可选.默认50.触发上拉加载拖动距离
						auto : true, // 可选,默认false.自动上拉加载一次
						contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
						contentnomore : '<p class="commo_none"><span>没有更多公告</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
						callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
							var _this = this;
							page++;
							$http.post(basePath + 'appDoctor/queryNoticeList.action', {
								page : page,
								row : row,
								state : 1,
								doctorId :$location.search().doctorId
							}, postCfg)
								.then(function(response) {
									if (response.data && response.data.respCode == 1001) {
										if (response.data.data.length == 0) {
											_this.endPullupToRefresh(true);
										} else {
											if (null != $scope.notice) {
												for (var i = 0; i < response.data.data.length; i++) {
													$scope.notice.push(response.data.data.data[i]);
												}
											} else {
												$scope.notice = response.data.data;
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
									console.log($scope.notice);
								});
						}
					}
				}
			});
			
			
		} ]);
