var app = angular.module("searchKnowApp", [ 'ngSanitize', 'angular-loading-bar','ngCookies']);

app.filter('splitName',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content="";
		if(text.length>15){
			content = text.substring(0,15)+"...";
		}else{
			content = text;
		}
		return content;
	};
});
app.controller('searchKnowCon', [ '$scope', '$location', '$http', '$sce', '$filter','$cookieStore', function($scope, $location, $http, $sce, $filter,$cookieStore) {

	var keySearch=$cookieStore.get("knowKeySerach");
	if(keySearch){
		$scope.keySearch=keySearch;
	}
	$(".search_import input").focus();
	$(".search_import input").addClass("search_input");
	
	$http.post(basePath + 'knowledgeCircle/queryCommonSearch.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001&&response.data.data.length>0) {
						$scope.commonSearchs=response.data.data;
				}
	});
	
	$scope.search=function(e){
		var name = $("#searchName").val();
		var keycode = window.event?e.keyCode:e.which;
		if(keycode==13){
			if(!isEmpty(name)){
				var keySearch=$cookieStore.get("knowKeySerach");
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
				$cookieStore.put("knowKeySerach",keySearch);
				window.location.href="know_search.html?name="+name;
			}
		}
	};
	
	
	$scope.checkSearch=function(name){
		if(name){
			var keySearch=$cookieStore.get("knowKeySerach");
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
			$cookieStore.put("knowKeySerach",keySearch);
			window.location.href="know_search.html?name="+name;
		}
	}
	
	$scope.checkSearchDoc=function(id,name){
		if(name){
			var keySearch=$cookieStore.get("knowKeySerach");
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
			$cookieStore.put("knowKeySerach",keySearch);
			window.location.href="know_search.html?doctorId="+id;
		}
	};
	
	$scope.cancel=function(){
		window.location.href="knowledge_circle.html";
	};
	
	$scope.deleteSearch=function(){
		$cookieStore.remove("knowKeySerach");
		$scope.keySearch=null;
	};
	
	$(".search_import input").on("focus", function() {
		$(this).addClass("search_input");
	});

	
//	$(".search_import input").on("blur", function() {
//		if ($(this).val("")) {
//			$(this).removeClass("search_input");
//			$(".search_con").show();
//			$(".search_keyword").hide();
//		} else {
//			$(".search_con").hide();
//			$(".search_keyword").show();
//		}
//	});
} ]);

mui('body').on('tap', 'li,a', function(event) {
	if ((this.getAttribute("ng-click")||this.getAttribute("onclick"))&&!isWindow) {
		event.stopPropagation();
		this.click();
	}
});