$("#searchName").focus();
setTimeout(function(){
    $("#searchName").trigger("click").focus();
},200);

var app = angular.module("searchDoctorApp", [ 'ngSanitize', 'angular-loading-bar','ngCookies']);

app.controller('searchDoctorCon', [ '$scope', '$location', '$http', '$sce', '$filter','$cookieStore', function($scope, $location, $http, $sce, $filter,$cookieStore) {

	var keySearch=null;
	
	$http.post(basePath + 'goods/queryHistorySearch.action',{type:1},postCfg).then(
			function(response) {
				if (response.data && response.data.respCode == 1001&&response.data.data.length>0) {
					$scope.keySearch=response.data.data;
				}
	});
	
	setTimeout(function(){
		$(".search_import input").trigger("click").focus();
	},100);
	$(".search_import input").addClass("search_input");
	
	$http.post(basePath + 'doctor/queryIllClassByNum.action',{num:9},postCfg).then(
			function(response) {
				if (response.data && response.data.respCode == 1001&&response.data.data.length>0) {
						$scope.commonSearchs=response.data.data;
				}
	});
	
	
	$scope.myFunc=function(){
		if(!isEmpty($scope.searchDoctors)){
			/*$http.post(basePath + 'doctor/queryDoctorByKey.action',{name:$scope.searchDoctors},postCfg).then(
					function(response) {
						console.log(response.data);
						if (response.data && response.data.respCode == 1001) {
								if(response.data.data&&response.data.data.length>0){
									$(".search_con").hide();
									$(".search_keyword").show();
								}
								$scope.lists=response.data.data;
						}
			});*/
		}else{
			$(".search_con").show();
			$(".search_keyword").hide();
		}
		
	};
	
	$scope.checkSearch=function(name,id){
		if(!isEmpty(name)){
			/*var keySearch=$cookieStore.get("doctorKeySerach");
			if(keySearch){
				if($.inArray(name, keySearch)>=0){
					keySearch.splice($.inArray(name, keySearch),1);
				}else{
					if(keySearch.length>=10){
						keySearch.splice(0,1);
					}
				}
				keySearch.splice(0, 0,name);  
			}else{
				keySearch=[];
				keySearch.push(name);
			}
			$cookieStore.put("doctorKeySerach",keySearch);*/
			$http.post(basePath + 'goods/addHistorySearch.action',{type:1,name:name},postCfg).then(
					function(response) {
						if (response.data && response.data.respCode == 1001) {
						}
			});
			window.location.href="doctor_search.html?name="+name+"&id="+id;
		}
	};
	
	$scope.search=function(e){
		var name=isEmpty($scope.searchDoctors)?'':$scope.searchDoctors;
		var keycode = window.event?e.keyCode:e.which;
		if(keycode==13){
			//if(name){
				/*var keySearch=$cookieStore.get("doctorKeySerach");
				if(keySearch){
					if($.inArray(name, keySearch)>=0){
						keySearch.splice($.inArray(name, keySearch),1);
					}else{
						if(keySearch.length>=10){
							keySearch.splice(0,1);
						}
					}
					keySearch.splice(0, 0,name);  
				}else{
					keySearch=[];
					keySearch.push(name);
				}
				if(!isEmpty($scope.searchDoctors)){
					$cookieStore.put("doctorKeySerach",keySearch);
				}*/
				if(!isEmpty(name)){
					$http.post(basePath + 'goods/addHistorySearch.action',{type:1,name:name},postCfg).then(
							function(response) {
								if (response.data && response.data.respCode == 1001) {
								}
					});
				}
				window.location.href="doctor_search.html?name="+name;
			//}
		}
	};
	
	$("#searchName").blur(function(){
		$scope.search();
	});
	
	
	
	$scope.deleteSearch=function(){
		$http.post(basePath + 'goods/deleteHistorySearch.action',{type:1},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.keySearch=null;
					}
		});
		//$cookieStore.remove("doctorKeySerach");
	};
	
	$(".search_import input").on("focus", function() {
		$(this).addClass("search_input");
	});

	$(".search_import input").on("blur", function() {
		if ($(this).val("")) {
			$(this).removeClass("search_input");
			$(".search_con").show();
			$(".search_keyword").hide();
		} else {
			$(".search_con").hide();
			$(".search_keyword").show();
		}
	});
} ]);

mui('body').on('tap', 'li,a', function(event) {
	if ((this.getAttribute("ng-click")||this.getAttribute("onclick"))&&!isWindow) {
		event.stopPropagation();
		this.click();
	}
});