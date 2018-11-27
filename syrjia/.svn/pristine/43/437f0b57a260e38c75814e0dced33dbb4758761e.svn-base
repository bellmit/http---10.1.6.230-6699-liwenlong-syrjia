var app = angular.module("cardApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app.controller('cardCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {

	$http.post(basePath + 'appDoctor/queryBankHistorys.action', {
		doctorId : $location.search().id
	}, postCfg).then(function(response) {
		if (response.data && response.data.respCode == 1001) {
			$scope.cards=response.data.data;
			console.log($scope.cards);
		}
	});
	
	$scope.add=function(){
		window.location.href = "binding_card.html?doctorId="+$location.search().id;
	};
	
	$scope.edit=function(id){
		window.location.href = "binding_card.html?doctorId="+$location.search().id+"&id="+id;
	};
} ]);

mui('body').on('tap', 'li', function(event) {
	if (this.getAttribute("ng-click")||this.getAttribute("onclick")) {
		event.stopPropagation();
		this.click();
	}
});