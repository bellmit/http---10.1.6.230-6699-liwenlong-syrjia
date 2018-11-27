var app = angular.module("salesCircleApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.directive('compileHtml', function($sce, $compile) {
	return {
		restrict : 'A',
		replace : true,
		link : function(scope, ele, attrs) {
			scope.$watch(function() {
				return scope.$eval(attrs.ngBind);
			},
				function(html) {
					ele.html(html);
					$compile(ele.contents())(scope);
				});
		}
	};
});

var srId;
var id;
app.controller('salesCircleCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	if ($location.search().id) {
		srId = $location.search().srId;
		id = $location.search().id;
		var data = {'srId':srId, 'articleID' :id};
		$http.post(basePath + 'appSalesTraining/fetchTrainingArticle.action',data).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.content=response.data.data.content;
						$scope.title=response.data.data.title;
					}
		});
	}else{
		openAlertMsg("参数错误");
	}
	
} ]);
