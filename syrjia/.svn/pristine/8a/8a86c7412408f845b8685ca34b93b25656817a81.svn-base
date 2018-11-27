$(function() {

	$(".histroy_toph").on("click", function() {
		$(this).find("i").toggleClass("history_show");
		$(this).next().slideToggle("slow");
	});

	$("#symptom_describe").height($(window).height());
	
	// 查看症状描述
	$(".history_look").on("click", function() {
		$("#symptom_describe").show();
		$("#symptom_describe").addClass("symptom_describe");
		$("#symptom_describe").removeClass("symptom_symptom");
	});

	function native2ascii(value) {
		var nativecode = value.split("");
		var len = 0;
		for ( var i = 0; i < nativecode.length; i++) {
			var code = Number(nativecode[i].charCodeAt(0));
			/*
			 * if (code > 127) { len += 2; } else {
			 */
			len++;
			// }
		}
		return len;
	}

	/*$(".symptom_con").on("keyup", ".TextArea1", function() {
		var maxLength = 200;
		var targetLength = native2ascii($(this).val());
		var leftLength = maxLength - targetLength;
		$(this).parent().find("span").text(leftLength);
	});*/

});

var app = angular.module("caseHistoryApp", [ 'ngSanitize',
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

var orderNo;
app
		.controller(
				'symptomCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							$scope.buttonName = "开始";
							if ($location.search().orderNo) {
								orderNo = $location.search().orderNo;
								$http
										.post(
												basePath
														+ 'doctorOrder/queryOrderSymptomByOrderNo.action',
												{
													orderNo : $location
															.search().orderNo
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
														$scope.docUrl = response.data.data.docUrl;
														$scope.doctorId = response.data.data.doctorId;
														$scope.docName = response.data.data.docName;
														$scope.positionName = response.data.data.positionName;
														$scope.infirmaryName = response.data.data.infirmaryName;
														$scope.orderPatientId = response.data.data.orderPatientId;
														$scope.patientName = response.data.data.patientName;
														$scope.patientSex = response.data.data.patientSex;
														$scope.age = response.data.data.age;
														$scope.patientPhone = response.data.data.patientPhone;
														$scope.paymentStatus = response.data.data.paymentStatus;
														$scope.symptomDescribe = response.data.data.symptomDescribe;
														$scope.oldsymptomDescribe = response.data.data.symptomDescribe;
														$scope.orderNo = response.data.data.orderNo;
														$scope.otherDescribe = response.data.data.otherDescribe;
														$scope.pulse = response.data.data.pulse;
														if ($scope.pulse == null) {
															$scope.pulse = 60;
														}
														if (!isEmpty($scope.oldsymptomDescribe)) {
															$(".TextArea1")
																	.attr(
																			"readonly",
																			"readonly");
														}

													}
												});

							} else {
								openAlert(
										"参数错误",
										function() {
											window.location.href = "../myself/center_index.html";
										});
							}

							$scope.doctorDetail = function(event) {
								event.stopPropagation();
								window.location.href = "doctor_detail.html?id="
										+ $scope.doctorId;
							};

							$scope.casehistoryList = function() {
								window.location.href = "casehistory_list.html?id="
										+ $scope.orderPatientId;
							};

							$scope.changePulse = function(event, sign) {
								event.stopPropagation();
								if ($scope.paymentStatus == 2
										&& isEmpty($scope.oldsymptomDescribe)) {
									if (sign == 'jia') {
										$scope.pulse++;
									} else {
										if ($scope.pulse > 1) {
											$scope.pulse--;
										}
									}
								}
							};

							$scope.showTimer = function(event) {
								event.stopPropagation();
								if (isEmpty($scope.oldsymptomDescribe)) {
									$("#symptom_describe").hide();
									$("#count_down").height($(window).height());
									var djsHeight = $(window).height()
											- $(".inquiry_top").height()
											- $(".symptom_patient").height();
									$(".count_con").height(djsHeight);
									$("#count_down").show();
								}
							};

							$scope.closeTimer = function(event) {
								event.stopPropagation();
								$("#symptom_describe").show();
								$("#count_down").hide();
								clearInterval(interval);
								$scope.buttonName = "开始";
								$("#pulseTime").html('00:<span>60</span>');
							};

							$scope.startDjs = function(event, id) {
								event.stopPropagation();
								if ($scope.buttonName == '开始') {
									djs(59, id);
									$scope.buttonName = "结束";
								} else {
									clearInterval(interval);
									$scope.buttonName = "开始";
									$("#pulseTime").html('00:<span>60</span>');
								}
							};
							var flag=2;
							$scope.addOrderSymptom = function() {
								if(flag==1){
									return false;
								}
								if (isEmpty($scope.symptomDescribe)) {
									openAlertMsg("病症描述不能为空");
								} else if (isEmpty($scope.otherDescribe)) {
									openAlertMsg("请输入其他描述");
								} else if (isEmpty($scope.pulse)
										|| isNaN($scope.pulse)) {
									openAlertMsg("脉搏数不能为空且只能为数字");
								} else {
									flag=1;
									openAlertMsgLoad1("提交中");
									$http
											.post(
													basePath
															+ 'doctorOrder/addOrderSymptom.action',
													{
														orderNo : orderNo,
														symptomDescribe : $scope.symptomDescribe,
														otherDescribe : $scope.otherDescribe,
														pulse : $scope.pulse,
														doctorId : $scope.doctorId
													}, postCfg)
											.then(
													function(response) {
														closeAlertMsgLoad1();
														if (response.data
																&& response.data.respCode == 1001) {
															openAlert(
																	"填写完成",
																	function() {
																		/*$(
																				"#symptom_describe")
																				.hide();
																		$(
																				".history_sub")
																				.show();*/
																		window.location.href = '../im/inquiry.html?identifier='+ $scope.orderPatientId+"&selToID="+$scope.doctorId;
																	});
														} else {
															openAlertMsg(response.data.respMsg);
														}
														flag=2;
													});
								}
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

mui('body').on('tap', 'li,a,span,div,h3,textarea,button,input',
		function(event) {
			event.stopPropagation();
			this.click();
		});
