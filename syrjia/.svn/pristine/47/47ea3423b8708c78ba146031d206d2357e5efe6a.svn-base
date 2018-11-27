var app = angular.module("downloadApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.controller('downloadCon', [ '$scope', '$location', '$http', '$sce', '$filter','$interval',
		function($scope, $location, $http, $sce, $filter,$interval) {
	 $scope.down=function(){
		 if(isWeiXin()){
			 $(".sy_href").show();
		 }else{
			 if(isiOS){
				 window.location.href ='https://itunes.apple.com/cn/app/id1393888624?mt=8';
			 }else{
				 $http.post(basePath + 'app/queryAppByLast.action', {
						"port" :2
					}, postCfg).then(function(response) {
						if (response.data && response.data.respCode == 1001) {
							window.location.href =response.data.data.src;
						} else {
							openAlert(response.data.respMsg);
						}
					});
				//window.location.href =basePath + 'app/downApp.action?port=2';
			 }
		 }
	 }
} ]);
