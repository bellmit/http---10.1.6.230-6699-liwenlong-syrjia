var app = angular.module("centerApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app.controller('centerCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	
	$http.post(basePath + 'centerCollect/queryMemberById.action').then(
			function(response) {
				console.log(response.data);
				if(response.data.data.member.phone!=null&&response.data.data.member.phone!=""){
					$scope.isBind = 4;
					$scope.phone = response.data.data.member.phone;
					$scope.id = response.data.data.member.id;
				}else{
					$scope.isBind = 3;
					$scope.id = response.data.data.member.id;
				}
				$scope.photo = response.data.data.member.photo;
				$scope.realname = response.data.data.member.realname;
				
				$scope.dfk = response.data.data.dfk;
				$scope.dpj = response.data.data.dpj;
				$scope.dsh = response.data.data.dsh;
			});
	
	$scope.bindPhone=function(url){
		window.location.href=url+"?phone="+$scope.phone;
	};
	$scope.nowServer=function(){
		window.location.href='/syrjia/weixin/hospital/now_inquiry.html';
	};
	$scope.myCaseList=function(){
		window.location.href='/syrjia/weixin/hospital/casehistory_list.html';
	};
	$scope.myDoctors=function(){
		window.location.href='/syrjia/weixin/hospital/mine_doctor.html';
	};
	$scope.toCollect=function(){
		window.location.href='/syrjia/weixin/myself/mine_collect.html';
	};
	$scope.toPerson=function(){
		window.location.href="/syrjia/weixin/person/seedoctor_person.html";
	};
	$scope.toAddress=function(){
		window.location.href="/syrjia/weixin/shippingaddress/select_address.html?id="+$scope.id;
	};
	$scope.toAllOrder=function(check){
		window.location.href="/syrjia/weixin/order/allOrder.html?check="+check;
	};
	$scope.callKf=function(){
		window.location.href="/syrjia/weixin/serverOnline/service.html?costomId="+$scope.id+"&costomName="+$scope.realname+"&costomUrl="+$scope.photo+"&costomType=0";
	};

} ]);

mui('body').on('tap', 'li,a', function(event) {
	if (this.getAttribute("ng-click")||this.getAttribute("onclick")) {
		event.stopPropagation();
		this.click();
	}
});