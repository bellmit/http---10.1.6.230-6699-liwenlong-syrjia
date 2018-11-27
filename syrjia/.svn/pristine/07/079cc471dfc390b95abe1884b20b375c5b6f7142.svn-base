var app = angular.module("importCodeApp",
		[ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app
		.controller(
				'importCodeCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {

							$scope.phon = $location.search().phone;
							$scope.codePhon = $location.search().phone.substr(
									0, 3)
									+ "****"
									+ $location.search().phone.substr(7, 11);

							if ($location.search().type == 3) {
								$scope.isOk = 'ok';
							} else {
								// 获取短信验证码
								var validCode = true;
								var time = 180;
								var $code = $(".verify_write a");
								$code.removeClass("import_again");
								$http
										.post(
												basePath
														+ 'member/getPhoneCode.action',
												{
													loginname : $location
															.search().phone,
													type : '4'
												}, postCfg)
										.then(
												function(response) {
													console.log(response);
													if (response.data.respCode == 1001) {

													} else {
														openAlert(response.data.respMsg);
													}
												});
								if (validCode) {
									validCode = false;
									var t = setInterval(function() {
										time--;
										$code.html(time + "s");
										if (time == 0) {
											clearInterval(t);
											$code.addClass("import_again");
											$code.html("重新获取");
											validCode = true;
										}
									}, 1000);
								}
								$scope.isOk = 'no';
							}

							$scope.bindIsOk = function() {
								if (isEmpty($("#inputCode").val().trim())) {
									openAlert("请输入验证码");
									return false;
								}
								$http
										.post(
												basePath
														+ 'member/bindPhone.action',
												{
													phone : $location.search().phone,
													loginname : $location
															.search().phone,
													code : $("#inputCode")
															.val().trim(),
													type : 4
												}, postCfg)
										.then(
												function(response) {
													console.log(response);
													if (response.data.respCode == 1001) {
														window.location.href = "/syrjia/weixin/myself/center_index.html";
													} else {
														openAlert(response.data.respMsg);
													}
												});
							};

							$scope.toNext = function() {
								if (isEmpty($("#inputCode").val().trim())) {
									openAlert("请输入验证码");
									return false;
								}
								$http
										.post(
												basePath
														+ 'member/bindPhone.action',
												{
													phone : $location.search().phone,
													loginname : $location
															.search().phone,
													code : $("#inputCode")
															.val().trim(),
													type : 4
												}, postCfg)
										.then(
												function(response) {
													console.log(response);
													if (response.data.respCode == 1001) {
														window.location.href = "/syrjia/weixin/myself/affirm_change.html?phon="
																+ $location
																		.search().phone;
													} else {
														openAlert(response.data.respMsg);
													}
												});
							};

							$scope.getCode = function() {
								if ($("#timeA").text().indexOf('重新') > -1) {
									$http
											.post(
													basePath
															+ 'member/getPhoneCode.action',
													{
														loginname : $location
																.search().phone,
														type : '4'
													}, postCfg)
											.then(
													function(response) {
														console.log(response);
														if (response.data.respCode == 1001) {

														} else {
															openAlert(response.data.respMsg);
														}
													});
								}
								;

							};

						} ]);
