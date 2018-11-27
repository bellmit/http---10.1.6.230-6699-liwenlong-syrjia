var search = location.search.substring(1);
var app = angular.module("addressApp", [ 'ngSanitize', 'angular-loading-bar' ]);
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


app
		.controller(
				'addressCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						function($scope, $location, $http, $sce) {
							$scope.isShowOpera = false;
							if (!isEmpty($location.search().type)) {
								$scope.isShowOpera = true;
							} else {
								$scope.isShowOpera = false;
							}
							$http
									.post(
											basePath
													+ 'shippingAddress/queryShippingAddressList.action')
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													$scope.addresss = response.data.data;
												};
											});
							$scope.edit = function(id, event) {
								event.stopPropagation();
								window.location.href = 'manage_address.html?addrId='
										+ id + "&" + search;
							};

							$scope.add = function() {
								if ($scope.addresss.length > 99) {
									openAlert("最多只能添加100个地址");
								} else {
									window.location.href = 'manage_address.html?'
											+ search;
								}
							};
							$scope.checkAddr = function(id, event) {
								if($location.search().type){
									if ($location.search().type==1) {
										event.stopPropagation();
										//parent.obj.getAddr(id);
										//parent.$("#select_address").parent().hide();
										window.location.href = "../hospital/affirm_purchasing.html?orderNo="+$location.search().orderNo+"&shippingAddressId="+id+"&patientId="+$location.search().patientId;
									}else{
										window.location.href = "../goods/affirm_order.html?ids="+$location.search().ids+"&goodsId="+$location.search().goodsId+"&priceNumId="+$location.search().priceNumId+"&buyCount="+$location.search().buyCount+"&shippingAddressId="+id+"&doctorId="+$location.search().doctorId+"&patientId="+$location.search().patientId;
									}
								}
								//return false;
							};

							$scope.del = function(id, event) {
								event.stopPropagation();
								openConfirm(
										"删除",
										"确认删除吗?",
										function() {
											$http
													.post(
															basePath
																	+ 'shippingAddress/deleteShippingAddress.action',
															{
																shippingAddressId : id
															}, postCfg)
													.then(
															function(response) {
																if (response.data
																		&& response.data.respCode == 1001) {
																	openAlert("删除成功");
																	$
																			.each(
																					$scope.addresss,
																					function(
																							index,
																							item) {
																						if (item.id == id) {
																							$scope.addresss
																									.splice(
																											index,
																											1);
																						}
																					});
																} else {
																	openAlert("删除失败");
																}
															});
										});
							};

							$scope.setDefault = function(address,event) {
								event.stopPropagation();
								if (address.isDefault == true) {
									address.isDefault = false;
								} else {
									$.each($scope.addresss, function(index,
											item) {
										item.isDefault = false;
									});
									address.isDefault = true;
								}
								$http
										.post(
												basePath
														+ "shippingAddress/updateShippingAddress.action",
												{
													id : address.id,
													isDefault : address.isDefault
												}, postCfg)
										.then(
												function(response) {
													if (response.data
															&& response.data.respCode == 1001) {
													} else {
														openAlert("修改失败");
													}
												});
							}
						} ]);

/*mui('body').on('tap','div',function(event) {
	if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))&& !isWindow) {
		event.stopPropagation();
		this.click();
	}
});*/