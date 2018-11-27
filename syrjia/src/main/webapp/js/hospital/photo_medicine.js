var app = angular.module("phoneMedicineApp", [ 'ngSanitize',
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

app.filter('subPhone', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = text.substr(0, 3) + '****'
				+ text.substr(text.length - 4, text.length);
		return content;
	};
});

var isCanUpload;
var isBindPhone = true;
app
		.controller(
				'phoneMedicineCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							$scope.buyNum = 1;

							$http
							.post(
									basePath
											+ 'member/checkMemberIsBindPhone.action')
							.then(
									function(
											response) {
										if (response.data
												&& response.data.respCode == 1001) {
												if (response.data.data.result == 0) {
													isBindPhone = false;
													openAlert(
															"您还未绑定安全手机号，请先绑定",
															function() {
																window.location.href = "../myself/affirm_change.html";
															});
												} else {
													isBindPhone = true;
												}
											}
										});
							
							$http
									.post(
											basePath
													+ 'doctorOrder/checkPhotoMedicalCount.action')
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													isCanUpload = false;
												} else {
													isCanUpload = true;
												}
											});
							

							$scope.toIndex = function(event) {
								event.stopPropagation();
								window.location.href = "hospital_index.html";
							};

							$scope.changePulse = function(event, sign) {
								event.stopPropagation();
								if (sign == 'jia') {
									$scope.buyNum++;
								} else {
									if ($scope.buyNum > 1) {
										$scope.buyNum--;
									}
								}
							};

							$scope.addPhotoMedical = function() {
								$http
								.post(
										basePath
												+ 'member/checkMemberIsBindPhone.action')
								.then(
										function(
												response) {
											if (response.data
													&& response.data.respCode == 1001) {
												if (response.data.data.result == 0) {
													openAlert(
															"您还未绑定安全手机号，请先绑定",
															function() {
																window.location.href = "../myself/affirm_change.html";
															});
												} else {
													if (isEmpty($(
															"#imgUrl")
															.val())) {
														openAlertMsg("药方图片不能为空");
													} else if (isEmpty($scope.buyNum)
															|| isNaN($scope.buyNum)) {
														openAlertMsg("抓药副不能为空且只能为数字");
													} else {
														openConfirm(
																"保存",
																"确认提交抓药申请吗？",
																function() {
																	openAlertMsgLoad1("提交中");
																	$http
																			.post(
																					basePath
																							+ 'doctorOrder/addPhotoMedical.action',
																					{
																						num : $scope.buyNum,
																						imgUrls : $(
																								"#imgUrl")
																								.val()
																					},
																					postCfg)
																			.then(
																					function(
																							response) {
																						closeAlertMsgLoad1();
																						if (response.data
																								&& response.data.respCode == 1001) {
																							openAlert(
																									"您的拍方抓药申请已发送至平台，请耐心等候",
																									function() {
																										window.location.href = "hospital_index.html";
																									});
																						} else {
																							openAlertMsg(response.data.respMsg);
																						}
																					});
																});
													}
												}
											} else {
												openAlertMsg(response.data.respMsg);
											}
										});
							};

						} ]);

function openAlertMsgLoad1(content) {
	closeAlertMsgLoad1();
	var div = '<div class="mask-black mask"></div><div class="wx_loading" id="wxloading" style=""><div class="wx_loading_inner"><i class="wx_loading_icon"></i>'
			+ content + '</div></div>';
	$("body").append(div);
}

function closeAlertMsgLoad1() {
	$(".mask").remove();
	$("#wxloading").remove();
}

function checkPicture(event) {
	if(isBindPhone){
		if (isCanUpload) {
			var id = $(event).attr("id");
			imgPreview(id, null, function(data) {
				$("#imgUrl").val(data.urllist[0].serverUrl);
				$("#photoUrl").attr("src", data.urllist[0].serverUrl);
			});
		} else {
			openAlertMsg('已有拍方抓药申请未处理，不可重复申请');
		}
	}else{
		$http
		.post(
				basePath
						+ 'member/checkMemberIsBindPhone.action')
		.then(
				function(
						response) {
					if (response.data
							&& response.data.respCode == 1001) {
							if (response.data.data.result == 0) {
								isBindPhone = false;
								openAlert(
										"您还未绑定安全手机号，请先绑定",
										function() {
											window.location.href = "../myself/affirm_change.html";
										});
							} else {
								isBindPhone = true;
								if (isCanUpload) {
									var id = $(event).attr("id");
									imgPreview(id, null, function(data) {
										$("#imgUrl").val(data.urllist[0].serverUrl);
										$("#photoUrl").attr("src", data.urllist[0].serverUrl);
									});
								} else {
									openAlertMsg('已有拍方抓药申请未处理，不可重复申请');
								}
							}
						}
					});
		
	}
};
