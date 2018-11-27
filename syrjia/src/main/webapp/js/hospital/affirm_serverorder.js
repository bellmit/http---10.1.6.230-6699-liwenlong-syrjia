var app = angular.module("serverOrderApp", [ 'ngSanitize',
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
var obj;
app
		.controller(
				'serverOrderCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							openAlertMsgLoad("加载中");
							obj = $scope;
							if ($location.search().id
									&& $location.search().orderType) {
								$scope.orderType=$location.search().orderType;
								$http
										.post(
												basePath
														+ 'doctorOrder/queryConfirmDocServerData.action',
												{
													"doctorId" : $location
															.search().id,
													"orderType" : $location
															.search().orderType
												}, postCfg)
										.then(
												function(response) {
													closeAlertMsgLoad();
													if (response.data
															&& response.data.respCode == 1001) {
														console.log(response.data.data);
														$scope.doctorId = response.data.data.docId;
														$scope.docUrl = response.data.data.docUrl;
														$scope.docName = response.data.data.docName;
														$scope.infirmaryName = response.data.data.infirmaryName;
														$scope.positionName = response.data.data.positionName;
														$scope.serverName = response.data.data.serverName;
														$scope.serverCount = '';
														if ($location.search().orderType == 4
																|| $location
																		.search().orderType == 5) {
															$scope.price = response.data.data.fisrtTwGhMoney;
														} else if ($location
																.search().orderType == 6) {
															$scope.price = response.data.data.twZxMoney;
															$scope.serverCount = response.data.data.twZxCount;
														} else if ($location
																.search().orderType == 7
																|| $location
																		.search().orderType == 9) {
															$scope.price = response.data.data.fisrtPhoneGhMoney;
														} else if ($location
																.search().orderType == 8) {
															$scope.price = response.data.data.phoneZxMoney;
															$scope.serverCount = response.data.data.phoneZxCount;
														}
													} else {
														openAlertMsg(response.data.respMsg);
													}
												});
							} else {
								closeAlertMsgLoad();
								openAlert("参数错误", function() {
									window.history.back();
								});
							}
							$scope.checkAddr = function(event) {
								/*event.stopPropagation();
								$('html,body').scrollTop(0);
								$("body").css("overflow","hidden");
								if ($("#person_manage").length > 0) {
									$("#person_manage").parent().show();
									openAlertMsgLoad("加载中");
								}*/
								window.location.href = '../person/seedoctor_person.html?doctorId='+$location
								.search().id+"&orderType="+$location.search().orderType+"&type=1";
								return false;
							};

							$scope.getAddr = function(addrId) {
								closeAlertMsgLoad();
								$("body").css("overflow","auto");
								$http
										.post(
												basePath
														+ 'patientData/queryPatientDataById.action',
												{
													id : addrId
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
														$scope.patientId = response.data.data.id;
														$scope.patientName = response.data.data.name;
														if (response.data.data.sex == 0) {
															$scope.patientSex = '男';
														} else {
															$scope.patientSex = '女';
														}
														$scope.patientAge = response.data.data.age;
														$scope.patientPhone = response.data.data.phone
																.substring(0, 3)
																+ "****"
																+ response.data.data.phone
																		.substring(
																				response.data.data.phone.length - 4,
																				response.data.data.phone.length);
														$scope.addr = true;
													}
												});
							};
							
							if(!isEmpty($location.search().patientId)){
								$scope.getAddr($location.search().patientId);
							}else{
								$http
								.post(
										basePath
												+ 'patientData/queryPatientByDefault.action')
								.then(
										function(response) {
											if (response.data.data
													&& response.data.respCode == 1001) {
												$scope.patientId = response.data.data.id;
												$scope.patientName = response.data.data.name;
												if (response.data.data.sex == 0) {
													$scope.patientSex = '男';
												} else {
													$scope.patientSex = '女';
												}
												$scope.patientAge = response.data.data.age;
												$scope.patientPhone = response.data.data.phone
														.substring(0, 3)
														+ "****"
														+ response.data.data.phone
																.substring(
																		response.data.data.phone.length - 4,
																		response.data.data.phone.length);
												$scope.addr = true;
											} else {
												$scope.addr = false;
											}
										});
							}
							
							
							$scope.isSubmit = true;
							$scope.checkPatientOrder = function() {
								closeAlertMsgLoad();
								$http
										.post(
												basePath
														+ 'doctorOrder/checkNoFinishOrderByPatientId.action',
												{
													patientId : $scope.patientId,
													doctorId : $location.search().id,
													orderType : $location
															.search().orderType
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
														if (response.data.data.result == 0) {
															$scope.isSubmit = true;
														} else {
															openAlert(
																	"您正处于服务中，前往查看",
																	function() {
																		window.location.href = '../im/inquiry.html?identifier='+ $scope.patientId+"&selToID="+$location
																		.search().id;
																	});
															$scope.isSubmit = false;
														}
													}
												});
							};

							$scope.addOrder = function() {
								if (isEmpty($location.search().id)
										|| isEmpty($location.search().orderType)||isEmpty($scope.doctorId)) {
									openAlert("医生参数有误");
									return true;
								}
								if (isEmpty($scope.patientId)) {
									openAlert("请选择就诊人");
									return true;
								}
								if (!$scope.isSubmit) {
									return true;
								}
								if ($location.search().id) {
									$http
											.post(
													basePath
															+ 'doctorOrder/checkNoFinishOrderByPatientId.action',
													{
														patientId : $scope.patientId,
														doctorId : $location.search().id,
														orderType : $location
																.search().orderType
													}, postCfg)
											.then(
													function(response) {
														if (response.data
																&& response.data.respCode == 1001) {
															if (response.data.data.result == 0) {
																/*openConfirm(
																		"保存",
																		"确认提交订单吗？",
																		function() {*/
																			$scope.isSubmit = false;
																			openAlertMsgLoad("提交中");
																			$scope.isSubmit = true;
																			$http
																					.post(
																							basePath
																									+ 'doctorOrder/addServerOrder.action',
																							{
																								doctorId : $location
																										.search().id,
																								orderType : $location
																										.search().orderType,
																								orderWay : 1,
																								patientId : $scope.patientId
																							},
																							postCfg)
																					.then(
																							function(
																									response) {
																								closeAlertMsgLoad();
																								if (response.data
																										&& response.data.respCode == 1001) {
																									if(response.data.data.paystatus==2){
																										window.location.href = '../pay/pay_success.html?orderNo='
																											+ response.data.data.orderNo+"&doctorId="+response.data.data.doctorId+"&patientId="+response.data.data.patientId+"&orderType="+$location.search().orderType;
																									}else{
																										window.location.href = '../pay/pay.html?orderNo='
																											+ response.data.data.orderNo;
																									}
																								} else {
																									openAlertMsg(response.data.respMsg);
																								}
																							});
																		//});
															} else {
																openAlert(
																		"您正处于服务中，前往查看",
																		function() {
																			window.location.href = '../im/inquiry.html?identifier='+ $scope.patientId+"&selToID="+$location
																			.search().id;
																		});
															}
														}
													});
								} else {
									closeAlertMsgLoad();
									openAlert("医生参数有误");
								}
							};

						} ]);
