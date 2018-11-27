var app = angular.module("knowledgeApp", [ 'ngSanitize', 'angular-loading-bar','infinite-scroll' ]);

app.config([ '$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);
//渲染banner图指令，调用父controller方法
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

app.filter('splitLabelNames',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content="";
		if(text){
			var arr = text.split(",");
			var re_arr=[];
			if(arr!=null&&arr.length>0){
				for(var i=0,len=arr.length;i<len;i++){
					re_arr.push(arr[i]);
					content+='<span>'+re_arr[i]+'</span>';
				};
			}else{
				content='<span>'+arr+'</span>';
			}
		}
		return content;
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
app.filter('splitImgAndVideo',
		function($sce) { // 可以注入依赖
	return function(text) {
		if(text==null||text==''){
			return '';
		}else{
			var content = text.substr(0,50)+'......';
			return content;
		}
	};
});
app.filter('splitHosName',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = "";
		if(text!=null&&text!=""){
			if(text.length>8){
				content = text.substr(0,8)+'...';
			}else{
				content = text;
			}
		}
		return content;
	};
});

app.controller('knowledgeCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	scope=$scope;
	http=$http;
	var active = $(".know_title ul .know_active").attr("rel");
	$http.post(basePath + 'banner/queryBannerList.action', {
		type : 5,
		port:3
	}, postCfg).then(function(response) {
		if (response.data && response.data.respCode == 1001) {
			$scope.bannerlist = response.data.data;
			$scope.$on('ngRepeatFinished', function() {
				setBanner();
			});
			if($scope.bannerlist==null||$scope.bannerlist.length==0){
				var H = $(window).height() - $(".index_banner").height() - $(".know_search").height() - $(".know_title").height();
				$(".scroll").height(H);
			}
		}else{
			//openAlertMsg(response.data.respMsg);
		}
	});
	//获取未读评论的个数
	$http.post(basePath + 'knowledgeCircle/queryUnreadNum.action').then(
	function(response) {
		if (response.data && response.data.respCode == 1001) {
			$scope.unread=response.data.data.result;
		}
	});
	
	$scope.checkNum=$location.search().checkNum;
	if(isEmpty($scope.checkNum)){
		$scope.checkNum=1;
	}
	
	/**
	 * 查看更多文章的参数切换
	 */
	$scope.upCheckNum=function(){
		//$scope.checkNum=2;
		window.location.href = "knowledge_circle.html?checkNum=2";
	};

	/**
	 * 定义调用子controller对应的方法
	 */
	//点击关注后刷新热门列表
	$scope.shuaxinRM=function(){
		$scope.$broadcast('changeRM');
	};
	//取消关注修改数据
	$scope.changeRMGZ=function(id){
		$scope.$broadcast('changeRMGZ',id);
	};
	
	//点击关注后刷新关注列表
	$scope.shuaxinGZ=function(){
		$scope.$broadcast('changeGZ');
	};
	//点击收藏后刷新收藏列表
	$scope.shuaxinSC=function(){
		$scope.$broadcast('changeSC');
	};
	
	//当在收藏栏目点击取消收藏的时候清除这一列内容
	$scope.shanchuSC=function(id){
		$scope.$broadcast('shanchuSC',id);
	};
	//当在关注栏目点击取消关注的时候清除这一列内容
	$scope.shanchuGZ=function(id){
		$scope.$broadcast('shanchuGZ',id);
	};
	
	$scope.updateData=function(num){
		/*$scope.checkNum = num;
		if(num==2){
			$scope.shuaxinRM();
		}
		if(num==3){
			$scope.shuaxinSC();
		}*/
		window.location.href = "knowledge_circle.html?checkNum=" + num;
	};
	
	/**
	 * 查看详情
	 */
	$scope.detail=function(id){
		if(id){
			window.location.href="article_detail.html?circle="+id;
		}
	};
	//医生详情
	$scope.doctorDetail = function(doctorId) {
		window.location.href = "../hospital/doctor_detail.html?id="+doctorId;
	};
	/**
	 * 关注
	 */
	$scope.Foll=function(event,id,num){
		event.stopPropagation();
	/*	if($(event.target).hasClass("know_followed")){
			$(event.target).removeClass("know_followed");
			$(event.target).html("+关注");
		}else{
			$(event.target).addClass("know_followed");
			$(event.target).html("已关注");
		}*/
		$http.post(basePath + 'knowledgeCircle/updateFollow.action',{id:id},postCfg).then(
				function(response) {
					if(response.data.respCode==1001){
						if(response.data.data.result==2){
							openAlertMsg("已取消");
							//$scope.shanchuGZ(id);
							$scope.shuaxinGZ();
						}else{
							//刷新关注内容
							$scope.shuaxinGZ();
							openAlertMsg("已关注");
						}
						$scope.changeRMGZ(id);
					}
				});
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
							openAlertMsg("已取消");
							knowledge.collectNum--;
							$scope.shanchuSC(id);
						}else if(response.data.data.result==1){
							openAlertMsg("已收藏");
							knowledge.collectNum++;
							//刷新收藏内容
							$scope.shuaxinSC();
						}
					}
		});
	};
	/**
	 * 点赞
	 */
	$scope.praise=function(event,id,knowledge){
		event.stopPropagation();
		event.preventDefault(); 
		
		$(event.target).toggleClass("know_praised");
		$http.post(basePath + 'knowledgeCircle/knowledgePraise.action',{id:id},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						console.log(knowledge.pointNum);
						if (response.data.data.result == 2) {
							openAlertMsg("已取消");
							knowledge.pointNum--;
						} else if(response.data.data.result == 1){
							openAlertMsg("已点赞");
							knowledge.pointNum++;
						}
					}
				});
	};
	
	$scope.search=function(){
			window.location.href="search.html";
	};
	$scope.replyContent=function(){
			window.location.href="message_list.html";
	};
	/**
	 * 分类跳转
	 */
	$scope.claslink=function(id,name){
		if(id){
			window.location.href="know_search.html?id="+id+"&name="+name+"&type=1";
		}
	};
	
	$scope.toBanerDetail=function(obj){
		if(obj.linkType){
			switch (obj.linkType) {
			case 1:
				window.location.href="../knowledge/article_detail.html?circle="+obj.data;
				break;
			case 2:
				window.location.href="../activity/activity.html?id="+obj.data;
				break;
			case 3:
				window.location.href="../goods/commodity_details.html?goodsId="+obj.data;
				break;
			case 4:
				window.location.href="../hospital/doctor_detail.html?id="+obj.data;
				break;
			case 5:
				window.location.href=obj.url;
				break;
			}
		}
	};
} ]);
var page1=0,page2=0,page3=0,page4=0,row=5;
app.controller('gz', function($scope, $http,$sce) {
	$scope.loadMore1=function(obj){
		$scope.gzDis=true;
		page1++;
		var _this=this;
		$http.post(basePath + 'knowledgeCircle/queryKnowledgeList.action', {
			page : page1,
			row : row,
			type:1
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.knowledges) {
					for(var i=0;i<response.data.data.knowledgelist.length;i++){
						$scope.knowledges.push(response.data.data.knowledgelist[i]);
					}
				} else {
					$scope.knowledges = response.data.data.knowledgelist;
					if($scope.knowledges.length==0){
						$("#gzDoctorWZ").show();
						$("#know_more_gz").hide();
					}else{
						$("#gzDoctorWZ").hide();
					}
				}
				if(response.data.data.knowledgelist.length<row){
					if(page1==1&&response.data.data.knowledgelist.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						//loadOverIndex(obj,1);
						$("."+obj).find(".loadBottom").remove();
						$("#know_more_gz").show();
					}
					$scope.gzDis=true;
				}else{
					$scope.gzDis=false;
				}
			}else{
				$("."+obj).find(".loadBottom").remove();
				$("#know_more_gz").show();
				//loadOverIndex(obj,1);
				$scope.gzDis=true;
			}
			});
		};
		
		$scope.$on('changeGZ', function(event, data) {
			page1=0;
			$scope.gzDis=null;
			$scope.knowledges=null;
			$scope.loadMore1("gz");
		});  
		
		//当在关注栏目点击取消关注的时候清除这一列内容
		$scope.$on('shanchuGZ', function(event, data) {
			for ( var int = 0; int < $scope.knowledges.length; int++) {
				if($scope.knowledges[int].doctorId==data){
					$scope.knowledges.splice(int,1);
					if($scope.knowledges==0){
						$("#gzDoctorWZ").show();
						$("#know_more_gz").hide();
					}
					
				}
			}
		}); 
});

app.controller('rm', function($scope, $http,$sce) {
	$scope.loadMore2=function(obj){
		$scope.rmDis=true;
		page2++;
		var _this=this;
		$http.post(basePath + 'knowledgeCircle/queryKnowledgeList.action', {
			page : page2,
			row : row,
			type:2
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.knowledges) {
					for(var i=0;i<response.data.data.knowledgelist.length;i++){
						$scope.knowledges.push(response.data.data.knowledgelist[i]);
					}
				} else {
					$scope.knowledges = response.data.data.knowledgelist;
					if($scope.knowledges.length==0){
						$("#rmWZ").show();
					}else{
						$("#rmWZ").hide();
					}
				}
				if(response.data.data.knowledgelist.length<row){
					if(page2==1&&response.data.data.knowledgelist.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						$("."+obj).find(".loadBottom").remove();
						$("#know_more_rm").show();
					}
					$scope.rmDis=true;
				}else{
					$scope.rmDis=false;
				}
			}else{
				$("."+obj).find(".loadBottom").remove();
				$("#know_more_rm").show();
				$scope.rmDis=true;
			}
			});
		};
		$scope.$on('changeRM', function(event, data) {
			page2=0;
			$scope.gzDis=null;
			$scope.knowledges=null;
			$scope.loadMore2("rm");
		});  
		$scope.$on('changeRMGZ', function(event, data) {
			for ( var int = 0; int < $scope.knowledges.length; int++) {
				if($scope.knowledges[int].doctorId==data){
					if($scope.knowledges[int].follow==1){
						$scope.knowledges[int].follow=2;
					}else{
						$scope.knowledges[int].follow=1;
					}
				}
			}
		});  
		
});
app.controller('sc', function($scope, $http,$sce) {
	$scope.loadMore3=function(obj){
		$scope.scDis=true;
		page3++;
		var _this=this;
		$http.post(basePath + 'knowledgeCircle/queryKnowledgeList.action', {
			page : page3,
			row : row,
			type:3
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.knowledges) {
					for(var i=0;i<response.data.data.knowledgelist.length;i++){
						$scope.knowledges.push(response.data.data.knowledgelist[i]);
					}
				} else {
					$scope.knowledges = response.data.data.knowledgelist;
					if($scope.knowledges.length==0){
						$("#scWZ").show();
						$("#know_more_sc").hide();
					}else{
						$("#scWZ").hide();
					}
				}
				if(response.data.data.knowledgelist.length<row){
					if(page3==1&&response.data.data.knowledgelist.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						//loadOverIndex(obj,3);
						$("."+obj).find(".loadBottom").remove();
						$("#know_more_sc").show();
					}
					$scope.scDis=true;
				}else{
					$scope.scDis=false;
				}
			}else{
				//loadOverIndex(obj,3);
				$("."+obj).find(".loadBottom").remove();
				$("#know_more_sc").show();
				$scope.scDis=true;
			}
		});
	};
	$scope.$on('changeSC', function(event, data) {
		page3=0;
		$scope.gzDis=null;
		$scope.knowledges=null;
		$scope.loadMore3("sc");
	}); 
	//当在收藏栏目点击取消收藏的时候清除这一列内容
	$scope.$on('shanchuSC', function(event, data) {
		for ( var int = 0; int < $scope.knowledges.length; int++) {
			if($scope.knowledges[int].id==data){
				$scope.knowledges.splice(int,1);
				if($scope.knowledges.length==0){
					$("#scWZ").show();
					$("#know_more_sc").hide();
				}
			}
		}
	}); 
	
});
app.controller('fl', function($scope, $http,$sce) {
	$scope.loadMore4=function(obj){
		$scope.flDis=true;
		page4++;
		var _this=this;
		$http.post(basePath + 'knowledgeCircle/queryClassList.action', {
			page : page4,
			row : row,
			type : 3
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.classlist) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.classlist.push(response.data.data[i]);
					}
				} else {
					$scope.classlist = response.data.data;
				}
				if(response.data.data.length<row){
					if(page4==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						loadOverIndex(obj,4);
					}
					$scope.flDis=true;
				}else{
					$scope.flDis=false;
				}
			}else{
				loadOverIndex(obj,4);
				$scope.flDis=true;
			}
		});
	};
});

function loadOverIndex(obj,num){
	var bottom = "";
	if(num==1){
		bottom = '<div class="know_nomore">'+
				'	<span>没有更多文章</span>'+
				'	</div>'+
				'	<a class="know_btn" ng-click="upCheckNum()">查看更多热门文章</a>';
		$("."+obj).find(".loadBottom").remove();
		setTimeout(function(){
			$("."+obj).append(bottom);
		},100);
	}
	if(num==2){
		bottom = '<div class="know_nomore">'+
				'	<span>没有更多文章</span>'+
				'	</div>';
		$("."+obj).find(".loadBottom").remove();
		setTimeout(function(){
			$("."+obj).append(bottom);
		},100);
	}
	if(num==3){
		bottom = '<div class="know_nomore">'+
				'	<span>没有更多收藏文章</span>'+
				'	</div>';
		$("."+obj).find(".loadBottom").remove();
		setTimeout(function(){
			$("."+obj).append(bottom);
		},100);
	}
	if(num==4){
		bottom = '<div class="know_nomore">'+
				'	<span>没有更多文章分类</span>'+
				'	</div>';
		$("."+obj).find(".loadBottom").remove();
		setTimeout(function(){
			$("."+obj).append(bottom);
		},100);
	}
}

function setBanner() {
	// 轮播图
	var time = 60; // 进度条时间，以秒为单位，越小越快
	var $progressBar,
		$bar,
		$elem,
		isPause,
		tick,
		percentTime;
	$('#owl-carousel').owlCarousel({
		slideSpeed : 500,
		paginationSpeed : 500,
		singleItem : true,
		afterInit : progressBar,
		afterMove : moved,
		startDragging : pauseOnDragging
	});
	function progressBar(elem) {
		$elem = elem;
		buildProgressBar();
		start();
	}
	function buildProgressBar() {
		$progressBar = $('<div>', {
			id : 'progressBar'
		});
		$bar = $('<div>', {
			id : 'bar'
		});
		$progressBar.append($bar).insertAfter($elem.children().eq(0));
	}
	function start() {
		percentTime = 0;
		isPause = false;
		tick = setInterval(interval, 10);
	}
	function interval() {
		if (isPause === false) {
			percentTime += 1 / time;
			$bar.css({
				width : percentTime + '%'
			});
			if (percentTime >= 100) {
				$elem.trigger('owl.next');
			}
		}
	}
	function pauseOnDragging() {
		isPause = true;
	}
	function moved() {
		clearTimeout(tick);
		start();
	}
	$elem.on('mouseover', function() {
		isPause = true;
	});
	$elem.on('mouseout', function() {
		isPause = false;
	});
	
	var H = $(window).height() - $(".index_banner").height() - $(".know_search").height() - $(".know_title").height();
	$(".scroll").height(H);
}
