
var app = angular.module("changePhonApp",
		[ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app
		.controller(
				'changePhonCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							$scope.phon = $location.search().phone;

							$scope.bindPhone=function(url){
								window.location.href=url+"?phone="+$scope.phon+"&type=4";
							};

						} ]);

mui('body').on('tap', 'li,a', function(event) {
	if (this.getAttribute("ng-click") || this.getAttribute("onclick")) {
		event.stopPropagation();
		this.click();
	}
});