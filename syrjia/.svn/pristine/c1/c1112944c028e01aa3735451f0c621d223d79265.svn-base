var app = angular.module("logisticsApp", ['ngSanitize','angular-loading-bar']);

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
app.controller('logisticsCon', ['$scope','$location','$http','$sce','$filter',function($scope, $location, $http,$sce,$filter) {
	obj=$scope;
		$http.post(basePath + 'order/queryLogistics.action',{"orderNo":$location.search().orderNo},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.logistics=response.data.data;
						$scope.logisiticsList=response.data.data.logisiticsList;
					}else{
						//openAlertMsg(response.data.respMsg);
					}
					$scope.time=new Date().getTime();
			});
}]);
