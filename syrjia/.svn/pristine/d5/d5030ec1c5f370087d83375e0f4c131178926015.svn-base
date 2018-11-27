var app = angular.module("messageApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
var cope,http,circle;
app.controller('messageCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	var page=0;
	var row=5;
	mui.init({
		pullRefresh : {
			container :'#message_main',// 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
			up : {
				height : 50,// 可选.默认50.触发上拉加载拖动距离
				auto : true,// 可选,默认false.自动上拉加载一次
				contentrefresh : "正在加载...",// 可选，正在加载状态时，上拉加载控件上显示的标题内容
				contentnomore : '没有更多数据',// 可选，请求完毕若没有更多数据时显示的提醒内容；
				callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
					var _this=this;
					page++;
					$http.post(basePath + 'knowledgeCircle/queryMessageList.action',{page:page,row:row},postCfg)
							.then(function(response) {
								if (response.data&&response.data.respCode==1001) {
									if(null!=$scope.messlist){
										for(var i=0;i<response.data.data.length;i++){
											$scope.messlist.push(response.data.data[i]);
										}
									}else{
										$scope.messlist=response.data.data;
									}
									if(response.data.data.length<row){
										_this.endPullupToRefresh(true);
										_this.disablePullupToRefresh();
									}else{
										_this.endPullupToRefresh(false);
									}
								}else{
									_this.endPullupToRefresh(true);
									_this.disablePullupToRefresh();
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
	//input获取焦点
	$scope.inputFocus=function(){
		$(this).focus();
	};
	
	$scope.check=function(index){
		if(index!=4){
			page = 0;
			mui('#pullrefresh').pullRefresh().refresh(true);
			mui('#pullrefresh').pullRefresh().pullupLoading();
			mui('#pullrefresh').pullRefresh().scrollTo(0, 0);
		}else{
				$http.post(basePath + 'knowledgeCircle/queryClassList.action').then(
						function(response) {
							if (response.data && response.data.respCode == 1001) {
								classType=response.data.data;
								var str=""
								for(var i=0;i<classType.length;i++){
									str+='<li>'+
									'	    					<div class="know_classify_con">'+
									'	    						<h3>'+
									'	    							<span class="left_icon"></span>'+
									'	    							<span class="know_classify_title">'+classType[i].typeName+'</span>'+
									'	    							<i>'+classType[i].num+'</i>'+
									'	    						</h3>'+
									'	    						<div class="know_classify_img">'+
									'	    							<img src="'+classType[i].imgUrl+'">'+
									'	    						</div>'+
									'	    					</div>'+
									'	    				</li>';
								}
								$("#classType").empty().append(str);
								//$scope.brands=response.data.data.brands;
							}
				});
			}
		
		
	};
	
	$scope.collect=function(id){
		$http.post(basePath + 'knowledgeCircle/knowledgeCollect.action',{id:id},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.knowledges=response.data.data;
						//$scope.brands=response.data.data.brands;
					}
		});
	};
	
	$scope.praise=function(id){
		$http.post(basePath + 'knowledgeCircle/knowledgePraise.action',{id:id},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						$scope.knowledges=response.data.data;
						//$scope.brands=response.data.data.brands;
					}
				});
	};
	
	$scope.replyContent=function(id){
		if(id){
			window.location.href="message_list.html?id="+id;
		}
	};
	$scope.reply=function(messId ,name){
		$("#replyContent").show();
		$("#content").empty().attr("placeholder","回复 "+name+":");
		$("#messId").val(messId);
		$("#content").focus();
	};
	$scope.comment=function(){
		var comment =  $("#content").val().trim();
		var messId = $("#messId").val();
		if(comment==""){
			openAlert("回复不能为空!");
			return false;
		}
	$http.post(basePath + 'knowledgeCircle/addReplyContent.action',{grankId:messId,content:comment,messId:messId},postCfg).then(
			function(response) {
				$("#content").empty().val('');
			if (response.data && response.data.respCode == 1001) {
				if (response.data.data.result==1) {
					openAlert("回复成功!");
					window.location.href="message_list.html?num="+ Date.parse(new Date());;
				}else{
					openAlert("回复失败!");
				}
			}else if(response.data && response.data.respCode == 1089){
				openAlert("无法回复自己的评论!");
			}else if(response.data && response.data.respCode == 1125){
				openAlert("你已被禁言,请确认该账号是否正常!");
			}
		});
	};
} ]);
mui('body').on('tap','div,a,button,input',function(event) {
	if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))&& !isWindow) {
		event.stopPropagation();
		this.click();
	}
});
