function onBridgeReady() {
	    WeixinJSBridge.call('hideOptionMenu');
}
 
if (typeof WeixinJSBridge == "undefined") {
    if (document.addEventListener) {
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    } else if (document.attachEvent) {
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
} else {
    onBridgeReady();
}


var app = angular.module("orderApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

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

app.filter('break', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var arr = text.split(",");
		var content='';
		for(var i=0;i<arr.length;i++){
			content+='<span>'+arr[i]+'</span>';
		}
		return content;
	};
});
var obj;
app.controller('orderCon', [ '$scope', '$location', '$http', '$sce', '$filter',
		function($scope, $location, $http, $sce, $filter) {
			obj=$scope;
			$http.post(basePath + 'appDoctor/queryRecordOrdeNo.action', {
				orderNo : $location.search().orderNo
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.orders = response.data.data;
					console.log($scope.orders);
					
					//if($scope.orders.serverType==13){
						if(!isEmpty($location.search().patientId)){
							$scope.getPatient($location.search().patientId);
						}else if(!isEmpty($scope.orders.recordOrder.patientId)){
							$scope.getPatient($scope.orders.recordOrder.patientId);
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
					//}
					
					
					if(parseInt($scope.orders.drugRecordPrice.receiptsPrice)>=100){
						$scope.drugPostage=0;
					}else{
						if($scope.city=="北京市"){
							$scope.drugPostage=13;
						}else{
							$scope.drugPostage=23;
						}
					}
					$http.post(basePath + 'im/queryDoctorById.action', {
						doctorId : $scope.orders.recordOrder.doctorId
					}, postCfg).then(function(response) {
						if (response.data && response.data.respCode == 1001) {
							$scope.doctor = response.data.data;
						}
					});
				} else {
					openAlert(response.data.respMsg,function(){
						mui.back();
					});
				}
			});
			if(isEmpty($location.search().shippingAddressId)){
				openAlertMsgLoad1("加载中");
				$http.post(basePath+ 'shippingAddress/queryShippingAddressByDefault.action')
				.then(
					function(response) {
						if (response.data&&response.data.respCode==1001) {
							$scope.shippingAddressId = response.data.data.id;
							$scope.consignee = response.data.data.consignee;
							$scope.shippingAddressPhone = response.data.data.phone;
							$scope.detailedAddress =response.data.data.detailedAddress;
							$scope.province =response.data.data.province;
							$scope.city =response.data.data.city;
							$scope.area =response.data.data.area;
							$scope.addr = true;
							$scope.getPostage();
						} else {
							$scope.addr = false;
						}
						closeAlertMsgLoad1();
					});
			} else{
				openAlertMsgLoad1("加载中");
				$http
						.post(
								basePath
										+ 'shippingAddress/queryShippingAddressById.action',
								{
									shippingAddressId : $location.search().shippingAddressId
								}, postCfg)
						.then(
								function(response) {
									if (response.data&&response.data.respCode==1001) {
										$scope.shippingAddressId = response.data.data.id;
										$scope.consignee = response.data.data.consignee;
										$scope.shippingAddressPhone = response.data.data.phone;
										$scope.detailedAddress = response.data.data.detailedAddress;
										$scope.province =response.data.data.province;
										$scope.city =response.data.data.city;
										$scope.area =response.data.data.area;
										$scope.addr = true;
										$scope.getPostage();
									}
									closeAlertMsgLoad1();
								});
			};
			
			$scope.checkPatient = function(event) {
				event.stopPropagation();
				/*if ($("#person_manage").length > 0&&(isEmpty($scope.orders.recordOrder.patientId)||$scope.orders.serverType==13)) {
					$("#person_manage").parent().show();
					openAlertMsgLoad("加载中");
				}*/
				if (isEmpty($scope.patientId)||isEmpty($scope.orders.recordOrder.patientId)||$scope.orders.serverType==13) {
					window.location.href = "../person/seedoctor_person.html?type=2&orderNo="+$location.search().orderNo+"&shippingAddressId="+$scope.shippingAddressId;
				}
				return false;
			};
	

	$scope.getPatient = function(addrId) {
		closeAlertMsgLoad();
		openAlertMsgLoad1("加载中");
		$http
				.post(
						basePath
								+ 'patientData/queryPatientDataById.action',
						{
							id : addrId
						}, postCfg)
				.then(
						function(response) {
							closeAlertMsgLoad1();
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
			
			$scope.getPostage=function(){
					$http.post(basePath + 'goodsShopCart/queryPostageByMainOrderNo.action',{orderNo:$location.search().orderNo,city:$scope.city},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								$scope.postage=parseFloat(response.data.data.result);
								if(parseInt($scope.orders.drugRecordPrice.receiptsPrice)>=100){
									$scope.drugPostage=0;
								}else{
									if($scope.city=="北京市"){
										$scope.drugPostage=13;
									}else{
										$scope.drugPostage=23;
									}
								}
							}else{
								//openAlertMsg(response.data.respMsg);
							}
					});
			};
			
			$scope.checkAddr = function(event) {
				event.stopPropagation();
//				if ($("#select_address").length > 0) {
//					$("#select_address").parent().show();
//					openAlertMsgLoad("加载中");
//				}
				window.location.href = "../shippingaddress/select_address.html?type=1&orderNo="+$location.search().orderNo+"&patientId="+$scope.patientId;
				//return false;
			};
			
			$scope.pay=function(){
				if(isEmpty($scope.patientId)){
					openAlert("请选择患者");
					return false;
				}
				if(isEmpty($scope.shippingAddressId)){
					openAlert("请选择收货地址");
					return false;
				}
				openAlertMsgLoad("提交中");
				var patientId=isEmpty($location.search().patientId)?$scope.patientId:$location.search().patientId;
				var shippingAddressId=isEmpty($location.search().shippingAddressId)?$scope.shippingAddressId:$location.search().shippingAddressId;
				$http.post(basePath + 'appDoctor/updateRecordOrdeNo.action',{orderNo:$location.search().orderNo,patientId:patientId,shippingAddressId:shippingAddressId},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								window.location.href = "../pay/pay.html?orderNo="+$location.search().orderNo;
							}else{
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