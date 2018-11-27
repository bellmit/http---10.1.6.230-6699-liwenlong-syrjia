var app = angular.module("doctorApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.controller('doctorCon', [ '$scope', '$location', '$http', '$sce', '$filter','$interval',
		function($scope, $location, $http, $sce, $filter,$interval) {
	
	$scope.paracont = "获取验证码";  
    $scope.paraclass = "but_null";  
    $scope.paraevent = true;  
    var second = 59,  
    timePromise = undefined;  
		$scope.send = function(){
			if(timePromise){
				return;
			};
			if(isPhone($scope.phone)){
				openAlert("请正确输入手机号");
				return;
			}
			openAlertMsgLoad("发送中");
			$http.post(basePath + 'appDoctor/getPhoneCode.action', {
				"phone" : $scope.phone,
				"type" :6,
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					openAlert('验证码已发送，若有延迟请等待片刻');
					 timePromise = $interval(function(){  
				          if(second<=0){  
				            $interval.cancel(timePromise);  
				            timePromise = undefined;  
				  
				            second = 59;  
				            $scope.paracont = "重发验证码";  
				            $scope.paraclass = "but_null";  
				            $scope.paraevent = true;  
				          }else{  
				            $scope.paracont = second + "s";  
				            $scope.paraclass = "registernot but_null";  
				            second--;  
				             
				          }  
				      },1000,100);  
				}else if(response.data && response.data.respCode == 1030){
					openAlert("该手机号已注册，赶紧去下载客户端吧！",function(){
						window.location.href = "download_doctor.html";
					});
				} else {
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
     };
     /**
      * 医生邀请跳转页面
      */
     $scope.docSend = function(){
			if(timePromise){
				return;
			};
			if(isPhone($scope.phone)){
				openAlert("请正确输入手机号");
				return;
			}
			openAlertMsgLoad("发送中");
			$http.post(basePath + 'appDoctor/getPhoneCode.action', {
				"phone" : $scope.phone,
				"type" :6,
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					openAlert('验证码已发送，若有延迟请等待片刻');
					 timePromise = $interval(function(){  
				          if(second<=0){  
				            $interval.cancel(timePromise);  
				            timePromise = undefined;  
				  
				            second = 59;  
				            $scope.paracont = "重发验证码";  
				            $scope.paraclass = "but_null";  
				            $scope.paraevent = true;  
				          }else{  
				            $scope.paracont = second + "s";  
				            $scope.paraclass = "registernot but_null";  
				            second--;  
				             
				          }  
				      },1000,100);  
				}else if(response.data && response.data.respCode == 1030){
					openAlert("该手机号已注册，赶紧去下载客户端吧！",function(){
						window.location.href = "downloadGuide.html";
					});
				} else {
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
  };
     
     $scope.register=function(){
    	 $scope.docName=$scope.docName.replace(/\s+/g,"");
    	 if(isEmpty($scope.docName)){
    		 openAlert("请输入14位以内中文字符");
    		 return false;
    	 }
    	 
    	 var pattern = /^[A-Za-z\u4e00-\u9fa5]+$/gi;
    	 if (!pattern.test($scope.docName)){
    		 openAlert("请输入14个字符中文或英文");
    		 return false;
    	 }
    	 
    	 if(isPhone($scope.phone)){
			openAlert("请正确输入手机号");
			return;
		 }
    	 if(isEmpty($scope.code)||!isPositiveInteger($scope.code)){
    		 openAlert("请输入验证码");
    		 return false;
    	 }
    	 openAlertMsgLoad("提交中");
    	 $http.post(basePath + 'appDoctor/register.action', {
				"phone" : $scope.phone,
				"code" :$scope.code,
				"docName" :$scope.docName,
				"srId":$location.search().srId
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					openAlert("注册成功",function(){
						window.location.href = "download_doctor.html";
					});
				}else if(response.data && response.data.respCode == 1030){
					openAlert("该手机号已注册，赶紧去下载客户端吧！",function(){
						window.location.href = "download_doctor.html";
					});
				} else {
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
     };
     
     /**
      * 医生邀请医生跳转到新的下载页
      */
     $scope.doctorRegister=function(){
    	 $scope.docName=$scope.docName.replace(/\s+/g,"");
    	 if(isEmpty($scope.docName)){
    		 openAlert("请输入14位以内中文字符");
    		 return false;
    	 }
    	 
    	 var pattern = /^[A-Za-z\u4e00-\u9fa5]+$/gi;
    	 if (!pattern.test($scope.docName)){
    		 openAlert("请输入14个字符中文或英文");
    		 return false;
    	 }
    	 
    	 if(isPhone($scope.phone)){
			openAlert("请正确输入手机号");
			return;
		 }
    	 if(isEmpty($scope.code)||!isPositiveInteger($scope.code)){
    		 openAlert("请输入验证码");
    		 return false;
    	 }
    	 openAlertMsgLoad("提交中");
    	 $http.post(basePath + 'appDoctor/register.action', {
				"phone" : $scope.phone,
				"code" :$scope.code,
				"docName" :$scope.docName,
				"srId":$location.search().srId
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					openAlert("注册成功",function(){
						window.location.href = "downloadGuide.html";
					});
				}else if(response.data && response.data.respCode == 1030){
					openAlert("该手机号已注册，赶紧去下载客户端吧！",function(){
						window.location.href = "downloadGuide.html";
					});
				} else {
					openAlert(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
     };
     
} ]);

var registerH = $(window).height();
$(".register_main").height(registerH);