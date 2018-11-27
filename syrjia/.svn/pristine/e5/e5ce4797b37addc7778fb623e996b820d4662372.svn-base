var app = angular.module("payApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
var evaOrderNo,orderType,doctorId,patientId;
app.controller('payCon', [ '$scope', '$location', '$http', '$sce', '$filter',
		function($scope, $location, $http, $sce, $filter) {
				if(isWeiXin()){
				}else{
					window.location.href=basePath +"alipay/alipayWxPay.action?orderNo="+$location.search().orderNo;
				}
		} ]);
