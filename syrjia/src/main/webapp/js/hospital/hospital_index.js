$(".clinic_con").height($(window).height());
var app = angular
		.module("hospitalApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
								if (arr[i] != null
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
var obj;
app
		.controller(
				'hospitalCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							obj = $scope;
							$scope.Toptitle = "找医生";
							$http.post(
									basePath + 'banner/queryBannerList.action',
									{
										port:3,
										type : 1
									}, postCfg).then(function(response) {
								$scope.banners = response.data.data;
								$scope.$on('ngRepeatFinished', function() {
									setBanner();
								});
							});

							$http
									.post(
											basePath
													+ 'doctor/queryIllClassByNum.action',
											{
												num : 7
											}, postCfg)
									.then(function(response) {
										$scope.indexIlls = response.data.data;
									});

							var page = 0;
							var row = 10;
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
																			+ 'doctor/queryDoctorList.action',
																	{
																		"isRecommended" : 1,
																		"page" : page,
																		"row" : row
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
																				if (null != $scope.tjDoctors) {
																					for ( var i = 0; i < response.data.data.length; i++) {
																						$scope.tjDoctors
																								.push(response.data.data[i]);
																					}
																				} else {
																					$scope.tjDoctors = response.data.data;
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
									});

							$scope.illDetail = function(id, name) {
								if (id) {
									window.location.href = "disease_detail.html?id="
											+ id + "&name=" + name;
								}
							};

							$scope.toAllIll = function() {
								window.location.href = "more_discom.html";
							};

							$scope.toHospital = function() {
								window.location.href = "hospital_index.html";
							};

							$scope.toKnowledge = function() {
								window.location.href = "../knowledge/knowledge_circle.html";
							};

							$scope.toStore = function() {
								window.location.href = "../goods/store_index.html";
							};
							
							$scope.toPersonCenter = function() {
								window.location.href='/syrjia/weixin/myself/center_index.html';
							};

							$scope.toMyDoctors = function() {
								window.location.href = "mine_doctor.html";
							};

							$scope.searchClick = function() {
								window.location.href="search.html";
							};
							
							$scope.doctorDetail = function(event,doctorId,docName) {
								event.stopPropagation();
								window.location.href = "doctor_detail.html?id="+doctorId;
							};

							$scope.showClinicLayer = function(event) {
								event.stopPropagation();
								if ($(".clinic_layer").is(':hidden')) {
									$(".clinic_con").css("overflow", "hidden");
									$(".clinic_layer").show();
									$("#clinic_btn").show();
								} else {
									$(".clinic_con").css("overflow", "auto");
									$("#clinic_btn").hide();
									$(".clinic_layer").hide();
								}
							};
							
							$scope.toBanerDetail=function(obj){
								if(obj.linkType){
									switch (obj.linkType) {
									case 1:
										window.location.href="../knowledge/article_detail.html?circle="+obj.data;
										break;
									case 2:
										window.location.href="../activity/activity.html?id="+obj.data;
										break;
									case 3:
										window.location.href="../goods/commodity_details.html?goodsId="+obj.data;
										break;
									case 4:
										window.location.href="../hospital/doctor_detail.html?id="+obj.data;
										break;
									case 5:
										window.location.href=obj.url;
										break;
									}
								}
							}

						} ]);

function setBanner() {
	// 轮播图
	var time = 60; // 进度条时间，以秒为单位，越小越快
	var $progressBar, $bar, $elem, isPause, tick, percentTime;
	$('#owl-carousel').owlCarousel({
		slideSpeed : 500,
		paginationSpeed : 500,
		singleItem : true,
		afterInit : progressBar,
		afterMove : moved,
		startDragging : pauseOnDragging
	});
	function progressBar(elem) {
		$elem = elem;
		buildProgressBar();
		start();
	}
	function buildProgressBar() {
		$progressBar = $('<div>', {
			id : 'progressBar'
		});
		$bar = $('<div>', {
			id : 'bar'
		});
		$progressBar.append($bar).insertAfter($elem.children().eq(0));
	}
	function start() {
		percentTime = 0;
		isPause = false;
		tick = setInterval(interval, 10);
	}
	function interval() {
		if (isPause === false) {
			percentTime += 1 / time;
			$bar.css({
				width : percentTime + '%'
			});
			if (percentTime >= 100) {
				$elem.trigger('owl.next');
			}
		}
	}
	function pauseOnDragging() {
		isPause = true;
	}
	function moved() {
		clearTimeout(tick);
		start();
	}
	$elem.on('mouseover', function() {
		isPause = true;
	});
	$elem.on('mouseout', function() {
		isPause = false;
	});
	/*
	 * var topHeight =
	 * $(".hospital_search").height()+$(".clinic_mine").height()+$(".clinic_classify").height()*2+$(".banner0").height();
	 * var martonTop = (topHeight/16)+0.5;
	 * $("#pullrefresh").css("marginTop",martonTop+"rem");
	 */
}
mui('body').on('tap', 'li,p', function(event) {
	if (this.getAttribute("ng-click") || this.getAttribute("onclick")) {
		event.stopPropagation();
		this.click();
	}
});
