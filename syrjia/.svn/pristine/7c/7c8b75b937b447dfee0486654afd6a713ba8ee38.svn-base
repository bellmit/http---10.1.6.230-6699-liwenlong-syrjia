$(function() {
	// 获取短信验证码
	var validCode = false;
});

var app = angular.module("affirmChangeApp", [ 'ngSanitize',
		'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app
		.controller(
				'affirmChangeCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							$scope.isTs = $location.search().isTs;
							if(!isEmpty($scope.isTs)){
								$http.post(basePath+ 'member/getMember.action')
								.then(function(response) {
											if (response.data.respCode == 1001) {
													if(response.data.data.phone){
														window.location.href = "/syrjia/weixin/myself/change_phonenum.html?phone="+response.data.data.phone;
													}
											}
											
								});
							}
							
							$scope.phon = $location.search().phone;
							$scope.bindIsOk = function() {
								if(isPhone($scope.inputPhon)||isEmpty($scope.code)){
									openAlertMsg("请正确输入手机号或验证码");
								}else{
									$http
									.post(
											basePath
													+ 'member/bindPhone.action',
											{
												phone : $scope.inputPhon,
												loginname : isEmpty($location
														.search().phone)?null:$location
																.search().phone,
												code : $scope.code,
												type:5
											}, postCfg)
									.then(
											function(response) {
												if (response.data.respCode == 1001) {
													openAlert("绑定成功",function(){
														if (document.referrer == ''||document.referrer.indexOf("import_code")>-1) {
															window.location.href = "/syrjia/weixin/myself/center_index.html";
														}else{
															mui.back();
														}
													});
												} else {
													openAlertMsg(response.data.respMsg);
												}
											});
								}
							};

							$scope.getCode = function() {
								if ($("#timeA").text().indexOf('重新') > -1
										|| $("#timeA").text().indexOf('获取') > -1) {
									if (!isPhone($scope.inputPhon)) {
										openAlertMsgLoad("发送中");
										$http
												.post(
														basePath
																+ 'member/getPhoneCode.action',
														{
															loginname : $scope.inputPhon,
															type : '5'
														}, postCfg)
												.then(
														function(response) {
															if (response.data.respCode == 1001) {
																var time = 180;
																var $code = $(".verify_write a");
																$code.removeClass("import_again");
																var validCode = true;
																if (validCode) {
																	validCode = false;
																	var t = setInterval(function() {
																		time--;
																		$code.html(time + "s");
																		if (time == 0) {
																			clearInterval(t);
																			$code.addClass("import_again");
																			$code.html("重新验证码");
																			validCode = true;
																		}
																	}, 1000);
																};
															} else {
																openAlertMsg(response.data.respMsg);
															}
															closeAlertMsgLoad();
														});
									} else {
										openAlertMsg("请正确输入手机号");
									}
								};

							};

						} ]);
