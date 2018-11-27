var _selectDoctorId=null;
$(function() {
	_selectDoctorId = getQueryString(window.location.href,"doctorId");
});

var app = angular.module("personManageApp", [ 'ngSanitize',
		'angular-loading-bar' ]);

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

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app
		.controller(
				'personManageCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							// 性别默认选项
							$scope.sex = "0";
							$scope.isBjVisit = "0";
							if ($location.search().personId) {
								$http
										.post(
												basePath
														+ 'patientData/queryPatientDataById.action',
												{
													id : $location.search().personId
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
														$scope.nexuinputhidden = response.data.data.nexus;
														$scope.patientname = response.data.data.name;
														$scope.sex = response.data.data.sex;
														$scope.age = response.data.data.age;
														$scope.phone = response.data.data.phone;
														$scope.isDefaultPer = response.data.data.isDefaultPer;
													}
													;
												});
							}
							document.querySelector('#sex').addEventListener(
									"tap",
									function() {
										var roadPick = new mui.PopPicker({
											test : "请选择性别"
										});// 获取弹出列表组建，假如是二联则在括号里面加入{layer:2}
										roadPick.setData([ {// 设置弹出列表的信息，假如是二联，还需要一个children属性
											text : "男"
										}, {
											text : "女"
										} ]);
										roadPick.show(function(item) {// 弹出列表并在里面写业务代码
											var itemCallback = roadPick
													.getSelectedItems();
											if (itemCallback[0].text == '男') {
												$scope.sex = 0;
											} else {
												$scope.sex = 1;
											}
											$('#sex .suc-sex').html(
													itemCallback[0].text);
										});
									});
							$http
									.post(
											basePath
													+ 'patientData/queryPatientNexus.action')
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													console
															.log(response.data.data);
													document
															.querySelector(
																	'#MM')
															.addEventListener(
																	"tap",
																	function() {
																		var roadPick = new mui.PopPicker(
																				{
																					test : "请选择关系"
																				});
																		roadPick
																				.setData(response.data.data);
																		roadPick
																				.show(function(
																						item) {
																					var itemCallback = roadPick
																							.getSelectedItems();
																					$scope.nexuinputhidden = itemCallback[0].text;
																					$(
																							'#MM .suc-msg')
																							.html(
																									itemCallback[0].text);
																				});
																	});

												}
												;
												myscroll = new IScroll(
														".person_con", {
															mouseWheel : true,
															click : true
														});
											});
							$scope.checkName = function(){
								var reg=/^[\u2E80-\u9FFF]+$/;//Unicode编码中的汉字范围
								if(reg.text($scope.patientname)){
									$scope.patientname="";
								}
							};
							
							// 点击选中
							$scope.liClic = function(id, name) {
								$scope.add_back = name;
								// 隐藏Input 赋值
								$scope.nexuinputhidden = name;
							};
							$scope.bjVisit = function(index) {
								$scope.isBjVisit = index;
							};
							
							$scope.getFouse=function(event){
								$(event.target).find("input").focus();
							};

							// 保存
							$scope.save = function() {
								$("input").blur();
								if (isEmpty($scope.patientname)) {
									openAlertMsg("请填就诊人姓名");
								} else if (isPhone($scope.phone)) {
									openAlertMsg("请正确填写联系方式");
								} else if (!($scope.age<999&&$scope.age>0)) {
									openAlertMsg("请正确填写年龄");
								} else if (isEmpty($scope.sex)) {
									openAlertMsg("请选择就诊人性别");
								} else if (isEmpty($scope.nexuinputhidden)) {
									openAlertMsg("请选择和您的关系");
								} else {
									var isDefaultPer = 0;
									if ($scope.isDefaultPer) {
										isDefaultPer = $scope.isDefaultPer;
									}
									var data = {
										name : $scope.patientname,
										phone : $scope.phone,
										sex : $scope.sex,
										nexus : $scope.nexuinputhidden,
										age : $scope.age,
										isDefault : false,
										isDefaultPer : isDefaultPer,
										id : $location.search().personId == undefined
												|| $location.search().personId == ''
												|| $location.search().personId == 'undefined' ? ''
												: $location.search().personId
									};
									$scope.toUrl = 'patientData/addPatientData.action';
									$scope.str = '新增成功';
									if ($location.search().personId != undefined
											&& $location.search().personId != ''
											&& $location.search().personId != 'undefined') {
										$scope.toUrl = "patientData/updatePatientData.action";
										$scope.str = '编辑成功';
									}
									openAlertMsgLoad("加载中...");
									$http
											.post(basePath + $scope.toUrl,
													data, postCfg)
											.then(
													function(response) {
														closeAlertMsgLoad();
														if (response.data
																&& response.data.respCode == 1001) {
															openAlert(
																	$scope.str,
																	function() {
																		/*window.location.href = 'seedoctor_person.html?type='
																				+ $location
																						.search().type
																				+ '&doctorId='
																				+ _selectDoctorId;*/
																		window.history.go(-1); 
																		//location.reload(); 
																	});
														} else {
															openAlertMsg("操作失败");
														}
													});
								}
							};
						} ]);
/*if(parent.$(document)){
	document.querySelector('body').addEventListener('touchend', function(e) {
		if (e.target.className != 'input') {
			document.querySelector('input').blur();
			var h = parent.$(document).height() - $(window).height();
			parent.$(document).scrollTop(h);
		}
	});
}*/
