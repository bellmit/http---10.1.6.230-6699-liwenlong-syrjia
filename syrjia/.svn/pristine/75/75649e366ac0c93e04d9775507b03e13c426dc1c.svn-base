var app = angular.module("verifyPhoneApp", [ 'ngSanitize',
		'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app.controller(
				'verifyPhoneCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {

							$scope.phon = $location.search().phone;

							$scope.codeType = $location.search().type;

							$scope.getCode = function() {
								
								if ($scope.codeType == 4) {
									$scope.paramType = 4;
								} else {
									$scope.paramType = 3;
								}
								$http.post(basePath+ 'member/getPhoneCode.action',{loginname : $location.search().phone,type : '4'}, postCfg).then(
												function(response) {
													console.log(response);
													if (response.data.respCode == 1001) {
														window.location.href = "/syrjia/weixin/myself/import_code.html?phone="
																+ $location.search().phone
																+ "&type="
																+ $scope.paramType;
													} else {
														openAlert(response.data.respMsg);
													}
								});
							};

						} ]);

mui('body').on('tap', 'li,a', function(event) {
	if (this.getAttribute("ng-click") || this.getAttribute("onclick")) {
		event.stopPropagation();
		this.click();
	}
});