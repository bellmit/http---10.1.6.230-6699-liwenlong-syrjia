var app = angular.module("evaBannerApp",
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

app.filter('float', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		return parseFloat(text);
	};
});
var paramarr = [];
var obj, http;
var orderNo;
$(".cart_check").removeClass("cart_checked");
app
		.controller(
				'evaBannerCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							obj = $scope;
							http = $http;
							$scope.buyCount = 0;
							$scope.totalPrice = 0.0;
							orderNo = $location.search().orderNo;
							$scope.buyNum = 1;
							$http
									.post(
											basePath
													+ 'doctorOrder/queryMyEvaBanners.action')
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													$scope.evaBanners = response.data.data;
												} else {
													openAlert(response.data.respMsg);
												}
											});

							$scope.changePulse = function(event, sign, id,
									price) {
								event.stopPropagation();
								if (sign == 'jia') {
									$scope.buyNum++;
									if ($("#check_" + id).hasClass("mind_buy")) {
										$scope.buyCount++;
										$scope.totalPrice += price;
									}
								} else {
									if ($scope.buyNum > 1) {
										$scope.buyNum--;
										if ($("#check_" + id).hasClass(
												"mind_buy")) {
											$scope.buyCount--;
											$scope.totalPrice -= price;
										}
									}
								}
							};

							$scope.checkAll = function(event) {
								$(event.target).find(".cart_check")
										.toggleClass("cart_checked");
								if ($(event.target).find(".cart_check")
										.hasClass("cart_checked")) {
									$(".mind_select").find("span").addClass(
											"mind_buy");
									var xhPrice = $(".mind_con").find(".type1")
											.attr("price")
											* $scope.buyNum;
									var jqTotal = 0.0;
									$(".notype1")
											.each(
													function(i, d) {
														jqTotal += Math
																.round(parseFloat($(
																		this)
																		.attr(
																				"price")) * 100) / 100;
													});
									$scope.totalPrice = Math
											.round(parseFloat(xhPrice) * 100)
											/ 100
											+ Math
													.round(parseFloat(jqTotal) * 100)
											/ 100;
									$scope.buyCount = $(".mind_con").find(
											".notype1").length
											+ $scope.buyNum;
								} else {
									$(".mind_select").find("span").removeClass(
											"mind_buy");
									$scope.buyCount = 0;
									$scope.totalPrice = 0.0;
								}
							};

							$scope.checkDiv = function(event, price, id, type) {
								$(event.target).toggleClass("mind_buy");
								var flag = $(event.target).hasClass("mind_buy");
								if (flag) {
									if (type == 1) {
										$scope.buyCount = $scope.buyCount
												+ $scope.buyNum;
										$scope.totalPrice += $scope.buyNum
												* price;
									} else {
										$scope.buyCount++;
										$scope.totalPrice += price;
									}
								} else {
									$(".next_sum p").find("span").removeClass(
											"cart_checked");
									if (type == 1) {
										$scope.buyCount = $scope.buyCount
												- $scope.buyNum;
										$scope.totalPrice -= $scope.buyNum
												* price;
									} else {
										$scope.buyCount--;
										$scope.totalPrice -= price;
									}

								}
							};

							$scope.addJqOrder = function() {
								paramarr = [];
								var json;
								$(".mind_con .mind_buy").each(
										function(i, d) {
											var _type = $(this).find("i").attr(
													"_type");
											var id = $(this).find("i").attr(
													"id");
											if (_type == 1) {
												json = {
													"id" : id,
													"buyCount" : $scope.buyNum
												};
												paramarr.push(json);
											} else {
												json = {
													"id" : id,
													"buyCount" : 1
												};
												paramarr.push(json);
											}
										});
								console.log(paramarr);
								if (paramarr.length <= 0) {
									openAlert("请至少选择一个");
									return false;
								}
								openConfirm(
										"保存",
										"确认提交订单吗？",
										function() {
											openAlertMsgLoad("提交中");
											$http
													.post(
															basePath
																	+ 'doctorOrder/addJqOrder.action',
															{
																"paramarr" : JSON
																		.stringify(paramarr)
															}, postCfg)
													.then(
															function(response) {
																closeAlertMsgLoad();
																if (response.data
																		&& response.data.respCode == 1001) {
																	window.location.href = '../pay/pay.html?orderNo='
																			+ response.data.data.result
																			+ "&evaOrderNo="
																			+ $location
																					.search().orderNo;
																} else {
																	openAlert(response.data.respMsg);
																}
															});
										});
							};

						} ]);

mui('body').on('tap', 'li,a,span,div,button,input', function(event) {
	event.stopPropagation();
	this.click();
});