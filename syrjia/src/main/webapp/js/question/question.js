var app = angular
		.module("answerApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
var costomId = null;
var costomName = null;
var costomUrl = null;
var costomType = null;
app.controller('answerCon', [ '$scope', '$location', '$http', '$sce',
		'$filter',
		function($scope, $location, $http, $sce, $filter, $cookieStore) {
			costomId = $location.search().costomId;
			costomName = $location.search().costomName;
			costomUrl = $location.search().costomUrl;
			costomType = $location.search().costomType;
			$http.post(basePath + 'answer/queryAnswerTypeList.action', {
				id : $location.search().id
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.answers=response.data.data;
				} else {
					openAlert(response.data.respMsg);
				}
			});
			
			$http.post(basePath + 'querySysSet.action').then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.serverPhone=response.data.data.serverPhone;
				} else {
					//openAlert(response.data.respMsg);
				}
			});
			
			if(isWeiXin()){
				$scope.isShow=true;
			}
			
			$scope.toWapChat=function(){
				window.location.href="http://kefu.syrjia.com:8001/chat/Weixin/Chat?costomId="+costomId+"&costomName="+costomName+"&costomUrl="+costomUrl;
			};
			
			$scope.detail=function(event,answer){
				event.stopPropagation() ;
				$(".faq_maintop").html(answer);
				$(".faq_layer").show();
				$(".faq_main").show();
			};

		} ]);
var conH = $(window).height() - $(".faq_title").height() - $(".faq_btn").height();
$(".faq_con").height(conH);