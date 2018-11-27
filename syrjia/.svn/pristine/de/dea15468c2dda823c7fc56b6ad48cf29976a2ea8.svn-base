var app = angular.module("searchApp", [ 'ngSanitize', 'angular-loading-bar','ngCookies']);

app.controller('searchCon', [ '$scope', '$location', '$http', '$sce', '$filter','$cookieStore', function($scope, $location, $http, $sce, $filter,$cookieStore) {

	//查询搜索历史
	$http.post(basePath + 'goods/queryHistorySearch.action',{type:2},postCfg).then(
			function(response) {
				if (response.data && response.data.respCode == 1001&&response.data.data.length>0) {
					$scope.keySearch=response.data.data;
				}
	});
	
	$(".search_import input").focus();
	$(".search_import input").addClass("search_input");
	
	//查询常用搜索
	$http.post(basePath + 'goods/queryCommonSearch.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001&&response.data.data.length>0) {
						$scope.commonSearchs=response.data.data;
				}
	});
	
	
	$scope.myFunc=function(){
		/*if(!isEmpty($scope.searchGoods)){
			$http.post(basePath + 'goods/queryGoodsByName.action',{name:$scope.searchGoods},postCfg).then(
					function(response) {
						console.log(response.data);
						if (response.data && response.data.respCode == 1001) {
								if(response.data.data&&response.data.data.length>0){
									$(".search_con").hide();
									$(".search_keyword").show();
								}
								$scope.lists=response.data.data;
						}
			});
		}else{
			$(".search_con").show();
			$(".search_keyword").hide();
		}*/
	}
	//添加搜索历史
	$scope.checkSearch=function(name){
		if(!isEmpty(name)){
			/*var keySearch=$cookieStore.get("goodsKeySerach");
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
			$cookieStore.put("goodsKeySerach",keySearch);*/
			$http.post(basePath + 'goods/addHistorySearch.action',{type:2,name:name},postCfg).then(
					function(response) {
						if (response.data && response.data.respCode == 1001) {
						}
			});
			window.location.href="commo_list.html?name="+name;
		}
	};
	//搜索
	$scope.search=function(e){
		var name=isEmpty($scope.searchGoods)?'':$scope.searchGoods;
		var keycode = window.event?e.keyCode:e.which;
		if(keycode==13){
			//if(name){
				/*var keySearch=$cookieStore.get("goodsKeySerach");
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
				if(!isEmpty($scope.searchGoods)){
					$cookieStore.put("goodsKeySerach",keySearch);
				}*/
				if(!isEmpty(name)){
					$http.post(basePath + 'goods/addHistorySearch.action',{type:2,name:name},postCfg).then(
							function(response) {
								if (response.data && response.data.respCode == 1001) {
								}
					});
				}
				window.location.href="commo_list.html?name="+name;
			//}
		}
	};

	$("#searchName").blur(function(){
		$scope.search();
	});
	//清空搜素历史
	$scope.deleteSearch=function(){
		//$cookieStore.remove("goodsKeySerach");
		$http.post(basePath + 'goods/deleteHistorySearch.action',{type:2},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.keySearch=null;
					}
		});
	}
	
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