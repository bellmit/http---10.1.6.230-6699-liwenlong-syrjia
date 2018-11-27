var search = location.search.substring(1);
$(function() {
	// 设为默认就诊人

	/*$(".address_default").on(
			"click",
			function() {
				console.log(this);
				console.log($(this));
				$(this).find("span").removeClass("address_set");
				$(this).find("i").html("默认地址");
				$(this).parent().parent().siblings().find(".address_bth").find(
						".address_default").find("span")
						.addClass("address_set");
				$(this).parent().parent().siblings().find(".address_bth").find(
						".address_default").find("i").html("设为默认地址");

			});*/

	// 删除就诊人
	$(".address_delete").on("click", function() {
		$(this).parent().parent().parent().remove();
	});

});


var app = angular.module("personApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.filter('subName',
		function($sce) { // 可以注入依赖
			return function(text) {
				if (!text) {
					return "";
				}
				if(text.length>8){
					return text.substring(0,8)+"...";
				}else{
					return text;
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
				'personCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							openAlertMsgLoad("加载数据中");
							$scope.isShowOpera = false;
							if(!isEmpty($location.search().doctorId)||!isEmpty($location.search().type)){
								$scope.isShowOpera = true;
							}else{
								$scope.isShowOpera = false;
							}
							$http
									.post(
											basePath
													+ 'patientData/queryPatientDataList.action')
									.then(
											function(response) {
												closeAlertMsgLoad();
												if (response.data
														&& response.data.respCode == 1001) {
													console.log(response.data);
													$scope.addresss = response.data.data;
													$scope.typeslen = $scope.addresss.length;
													// 选中哪个患者
													if (null == $location
															.search().id
															&& $location
																	.search().id != undefined) {
														$scope.message_con = 0;
														$scope.index = 0;
													} else {
														$scope.message_con = $location
																.search().id;
														$scope.index = $location
																.search().id;
													}
												}
												;
											});
							
							$scope.addPerson = function(id, event) {
								if(isEmpty(id)){
									$http
									.post(
											basePath
															+ 'patientData/queryPatientCount.action')
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													if(response.data.data.result<100){
														window.location.href = '/syrjia/weixin/person/person_manage.html?personId='+id+"&"+ search+"&type="+$location.search().type+"&doctorId="+$location.search().doctorId;
													}else{
														openAlertMsg("最多可添加100个就诊人");
													}
												}else {
													openAlertMsg(response.data.respMsg);
												}
											});
								}else{
									window.location.href = '/syrjia/weixin/person/person_manage.html?personId='+id+"&"+ search+"&type="+$location.search().type;
								}
							};
							$scope.checkAddr = function(id,event) {
								event.stopPropagation();
								if (!isEmpty($location.search().type)) {
									event.stopPropagation();
									if(!isEmpty($location.search().doctorId)){
										window.location.href = '../hospital/affirm_serveorder.html?id='+$location.search().doctorId+"&orderType="+$location.search().orderType+"&patientId="+id;
									}else if($location.search().type==2){
										window.location.href = '../hospital/affirm_purchasing.html?orderNo='+$location.search().orderNo+"&shippingAddressId="+$location.search().shippingAddressId+"&patientId="+id;
									}else{
										if(parent.obj.getPatient){
											parent.obj.getPatient(id);
										}else{
											parent.obj.getAddr(id);
										}
										parent.$("#person_manage").parent().hide();
									}
									return false;
								}else if(!isEmpty($location.search().doctorId)){
									openConfirm("提示","确认提交抄方申请？",function(){
									$http
									.post(
											basePath
															+ 'doctorOrder/addCfOrder.action',
											{
												patientId : id,
												doctorId:$location.search().doctorId
											}, postCfg)
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													closeAlertMsgLoad;
													openAlert(
															"已提交抄方申请至医生",
															function() {
																window.location.href = '/syrjia/weixin/hospital/now_inquiry.html';
															});
												} else {
													closeAlertMsgLoad;
													openAlertMsg(response.data.respMsg);
												}
											});
									});
								}
							};
							// 删除
							$scope.del = function(id) {
								event.stopPropagation();
								openConfirm("删除","确认删除？",function(){
									openAlertMsgLoad("加载中...");
									$http
											.post(
													basePath
															+ 'patientData/deletePatientData.action',
													{
														id : id
													}, postCfg)
											.then(
													function(response) {
														if (response.data
																&& response.data.respCode == 1001) {
															closeAlertMsgLoad;
															openAlert(
																	"删除成功",
																	function() {
																		history
																				.go(0);
																	});
														} else {
															closeAlertMsgLoad;
															openAlertMsg("删除失败");
														}
													});
								});
							};
							// 默认就诊人
							$scope.def = function(id,event) {
								event.stopPropagation();
								openAlertMsgLoad("加载中...");
								$http
										.post(
												basePath
														+ 'patientData/defPatient.action',
												{
													id : id
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
																	$(event.target).parent().find("span").removeClass("address_set");
																	$(event.target).parent().find("i").html("默认就诊人");
																	$(event.target).parent().parent().parent().siblings().find(".address_bth").find(
																			".address_default").find("span")
																			.addClass("address_set");
																	$(event.target).parent().parent().parent().siblings().find(".address_bth").find(
																			".address_default").find("i").html("设为默认就诊人");
													} else {
														openAlertMsg("操作失败");
													}
													closeAlertMsgLoad();
												});
							};
						} ]);

/*mui('body').on('tap','div,button,a,span',function(event) {
	if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))&& !isWindow) {
		event.stopPropagation();
		this.click();
	}
});*/