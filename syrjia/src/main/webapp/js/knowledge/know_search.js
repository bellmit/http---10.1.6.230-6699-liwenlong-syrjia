var app = angular.module("searchListApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.filter('splitContent',
		function($sce) { // 可以注入依赖
	return function(text) {
		if(text==null||text==''){
			return '';
		}else{
			index = text.indexOf('>');
			while (index>0){
				var startIndex = text.indexOf('<');
				var endIndex = text.indexOf('>');
				var tem = text.substring(startIndex,endIndex+1);
				text = text.replace(tem,'');
				index = text.indexOf('>');
		    }
			text.replace('&nbsp','');
			
			var content = text.substr(0,75)+'......';
			return content;
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

app.filter('break', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var arr = text.split(",");
		var content = '';
		for (var i = 0; i < arr.length; i++) {
			content += '<span>' + arr[i] + '</span>';
		}
		return content;
	};
});
app.filter('splitLabelNames',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content="";
		if(text){
			var arr = text.split(",");
			var re_arr=[];
			if(arr!=null&&arr.length>0){
				for(var i=0,len=arr.length;i<len;i++){
					if(arr[i]){
						re_arr.push(arr[i]);
						content+='<span>'+re_arr[i]+'</span>';
					}
				};
			}else{
				content='<span>'+arr+'</span>';
			}
		}
		
		return content;
	};
});
var cope,http,circle;
app.controller('searchListCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	var name=$location.search().name;
	if(name == "undefined"){
		name="";
	}
	var type=$location.search().type;
	$scope.type=type;
	$scope.name=name
	if(type){
		name='';
	}
	/*$("#searchName").val(name);*/
	id=$location.search().id;
	if(id == "undefined"){
		id="";
	}
	scope=$scope;
	http=$http;
	/*$http.post(basePath + 'knowledgeCircle/queryKnowledgeListByName.action',{name:name},postCfg).then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.knowledges=[];
					for(var i=0;i<response.data.data.length;i++){
						if(response.data.data[i].typesearch==1){
							$scope.knowledges.push(response.data.data[i]);
						}
					}
				}
			});
	*/
	var page=0;
	var row=10;
	mui.init({
		pullRefresh : {
			container :'#konw_search',// 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
			up : {
				height : 10,// 可选.默认50.触发上拉加载拖动距离
				auto : true,// 可选,默认false.自动上拉加载一次
				contentrefresh : "正在加载...",// 可选，正在加载状态时，上拉加载控件上显示的标题内容
				contentnomore : '没有更多文章',// 可选，请求完毕若没有更多数据时显示的提醒内容；
				callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
					var _this=this;
					page++;
					$http.post(basePath + 'knowledgeCircle/queryKnowledgeListByName.action',{page:page,row:row,name:name,id:id},postCfg)
							.then(function(response) {
								if (response.data&&response.data.respCode==1001) {
								/*	if(type){
										for ( var int = 0; int < response.data.data.length; int++) {
											if(response.data.data[int].typesearch==2){
												$scope.queryAll=1;
												response.data.data.splice(int);
											}
										}
									}*/
									if($scope.knowledges!=null){
										for(var i=0;i<response.data.data.length;i++){
											if(response.data.data[i].typesearch==1){
												$scope.knowledges.push(response.data.data[i]);
											}
										}
									}else{
										$scope.knowledges=[];
										for(var i=0;i<response.data.data.length;i++){
											if(response.data.data[i].typesearch==1){
												$scope.knowledges.push(response.data.data[i]);
											}
										}
									}
									if(type!=1){
										//热门
										if($scope.hotKnowledges!=null){
											for(var i=0;i<response.data.data.length;i++){
												if(response.data.data[i].typesearch==2){
													$scope.hotKnowledges.push(response.data.data[i]);
												}
											}
										}else{
											$scope.hotKnowledges=[];
											for(var i=0;i<response.data.data.length;i++){
												if(response.data.data[i].typesearch==2){
													$scope.hotKnowledges.push(response.data.data[i]);
												}
											}
										}
									}
									
									if(response.data.data.length<row||(type==1&&response.data.data.length>0&&response.data.data[response.data.data.length-1].typesearch==2)){
										_this.endPullupToRefresh(true);
										//_this.disablePullupToRefresh();
									}else{
										_this.endPullupToRefresh(false);
									}
								}else{
									_this.endPullupToRefresh(true);
									//_this.disablePullupToRefresh();
								}
						});
				}
			}
		}
	});
	
	/**
	 * 查看详情
	 */
	$scope.detail=function(id){
		if(id){
			window.location.href="article_detail.html?circle="+id;
		}
	};
	
	$scope.cancel=function(){
		window.location.href="knowledge_circle.html";
	};
	
	/**
	 * 收藏
	 */
	$scope.collect=function(event,id,knowledge){
		event.stopPropagation();
		$(event.target).toggleClass("know_collected");
		$http.post(basePath + 'knowledgeCircle/knowledgeCollect.action',{id:id,type:2},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						if(response.data.data.result==2){
							//openAlertMsg("取消收藏");
							knowledge.collectNum--;
						}else{
							//openAlertMsg("已收藏");
							knowledge.collectNum++;
						}
					}
		});
	};
	/**
	 * 点赞
	 */
	$scope.praise=function(event,id,knowledge){
		event.stopPropagation();
		$(event.target).toggleClass("know_praised");
		$http.post(basePath + 'knowledgeCircle/knowledgePraise.action',{id:id},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						if (response.data.data.result == 2) {
							//openAlertMsg("已取消");
							knowledge.pointNum--;
						} else {
							//openAlertMsg("已赞");
							knowledge.pointNum++;
						}
					}
				});
	};
	
	$scope.checkSearchDoc=function(){
		window.location.href="search.html";
	};
	
	
} ]);


		mui('body').on('tap','span,p,img,h3',function(event) {
			if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))&& !isWindow) {
				event.stopPropagation();
				this.click();
			}
		});