var app = angular.module("doctorApp", [ 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

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

app.filter('break', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var ill=text.split(",");
		var len=0;
		if(ill.length>5){
			len=5;
		}else{
			len=ill.length;
		}
		var content='';
		for(var i=0;i<len;i++){
			content+='<li>'+ill[i]+'</li>';
		}
		if(ill.length>5){
			content+='<li>······</li>'
		}
		return content;
	};
});

app.controller('doctorCon', [ '$scope', '$location', '$http',
		function($scope, $location, $http) {
			$http.post(basePath + 'doctor/queryDoctorByCard.action', {
				doctorId : $location.search().doctorId
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					console.log(response.data.data);
					$scope.doctor=response.data.data;
					$scope.qrCodeUrl = $scope.doctor.qrCodeUrl;
					if(isEmpty($scope.qrCodeUrl)){
						$scope.qrCodeUrl = "../../img/qrcode_gz.jpg";
					}
					//$scope.keepId=$scope.doctor.keepId;
					$scope.keepId=null;
				} else {
					//openAlertMsg("医生信息不存在或设置隐身");
				}
			});
			
			$scope.close=function(){
				$(".subscribeLayer").hide();
				$(".subscribeQrcode").hide();
			};
			
			$scope.keep = function() {
				$(".subscribeLayer").show();
				$(".subscribeQrcode").show();
			}
				/*if($scope.keepId){
					return false;
				}
				openAlertMsgLoad("提交中");
				$http
						.post(
								basePath
										+ 'keep/addKeep.action',
								{
									goodsId : $location
											.search().doctorId,
									type : 3
								}, postCfg)
						.then(
								function(response) {
									closeAlertMsgLoad();
									if (response.data
											&& response.data.respCode == 1001) {
										if (!isNaN(response.data.data.result)) {
											$scope.keepId = null;
										} else {
											$scope.keepId = response.data.data.result;
											console.log($scope.keepId);
										}
									} else {
										openAlertMsg(response.data.respMsg);
									}
								});
			};*/
		} ]);