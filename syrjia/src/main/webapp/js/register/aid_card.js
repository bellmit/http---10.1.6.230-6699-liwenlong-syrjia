var app = angular.module("cardApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.controller('cardCon', [ '$scope', '$location', '$http', '$sce', '$filter',
		function($scope, $location, $http, $sce, $filter) {
			$http.post(basePath + 'appSales/queryOneSales.action', {
				"srId" : $location.search().srId
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.sales=response.data.data;
				} else {
					openAlert(response.data.respMsg, function() {
						mui.back();
					});
				}
			});

} ]);
