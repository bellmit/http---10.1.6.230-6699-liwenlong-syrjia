var app = angular.module("discomApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.filter('splitIllClass', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		if(text.indexOf(",")){
			var arr = text.split(",");
			for (var i = 0; i < arr.length; i++) {
				var illArr = arr[i].split(";");
				content += '<li ng-click="illDetail(\''+illArr[1]+'\',\''+illArr[0]+'\')">'+illArr[0]+'</li>';
			}
		}else{
			var illArr = text.split(";");
			content += '<li ng-click="illDetail(\''+illArr[1]+'\',\''+illArr[0]+'\')">'+illArr[0]+'</li>';
		}
		var leftH = $(window).height() - $(".search_top").height();
		$(".more_con ul").height(leftH - 30);
		return content;
	};
});

app.controller('discomCon', [
		'$scope',
		'$location',
		'$http',
		'$sce',
		'$filter',
		function($scope, $location, $http, $sce, $filter) {
			openAlertMsgLoad("数据加载中");
			$scope.typeslen = 0;
			$scope.errnote = "";
			$http.post(basePath + 'doctor/queryAllIllClass.action').then(
					function(response) {
						closeAlertMsgLoad();
						$scope.departs = response.data.data;
						$scope.typeslen = response.data.data.length;
						/*
						 * if ($scope.typeslen > 0) { $(".depart_con").show(); }
						 * else { $scope.errnote = "您选择的科室暂无疾病信息";
						 * $(".check_hint").show(); }
						 */
					});

			$scope.showIndex = function(index) {
				$scope._ggin = index;
			};
			
			$scope.searchClick = function() {
				window.location.href = "search.html";
			};
			
			$scope.illDetail = function(id, name) {
				if (id) {
					window.location.href = "disease_detail.html?id=" + id + "&name=" + name;
				}
			};

		} ]);
