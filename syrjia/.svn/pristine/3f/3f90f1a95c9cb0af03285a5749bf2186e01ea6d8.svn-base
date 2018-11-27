
	$(".cart_check").on("click", function() {
		$(this).toggleClass("cart_checked");
	});


var app = angular.module("collectApp", [ 'ngSanitize', 'angular-loading-bar','infinite-scroll' ]);

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

app.directive('renderFinish1', function($timeout) { // renderFinish自定义指令
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			if (scope.$last === true) {
				$timeout(function() {
					scope.$emit('ngRepeatFinished1');
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
			}, function(html) {
				ele.html(html);
				$compile(ele.contents())(scope);
			});
		}
	};
});

app.filter('splitIllClass', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		if (text.indexOf(",")) {
			var arr = text.split(",");
			for ( var i = 0; i < arr.length; i++) {
				if (i < 5) {
					content += '<li>' + arr[i] + '</li>';
				} else {
					content += '<li>...</li>';
					break;
				}
			}
		} else {
			content += '<li>' + arr[i] + '</li>';
		}
		return content;
	};
});

app.filter('splitMoney',function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}			
		var content = '';					
		if (text.indexOf(";")) {			
			var arr = text.split(";");
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] != null && arr[i] != 0.00
						&& arr[i] != ' ' && i < 4) {
					if (i < 2) {
						content = '¥' + arr[i] + '/次';
					} else {
						content = '¥' + arr[i] + '/'
								+ arr[i + 4] + '次';
					}
					break;
				} else {
					content = '¥/次';
				}
			}
		} else {
			content = '¥/次';
			}
			return content;
		};
	});
app.filter('splitJzLists',function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		var _docid = "";
		if (text.indexOf(",")) {
			var arr = text.split(",");
			for ( var i = 0; i < arr.length; i++) {
				var detailArr = arr[i].split(";");
				if (detailArr[0] != null && detailArr[0] != ' '
						&& detailArr[0] != "") {
					_docid = detailArr[5];
					if (detailArr[4] == 1) {// 问诊服务中
						content += '<li ng-click="toJzDetail($event,'+detailArr[0]+');"><span><i>'
								+ formatDateYYMMDD(detailArr[1])
								+ '</i>'
								+ detailArr[2]
								+ '</span> <span>'
								+ detailArr[3]
								+ '</span> <span class="mine_ing">进行中</span> </li>';
					} else {
						content += '<li ng-click="toJzDetail($event,'+detailArr[0]+');"><span><i>'
								+ formatDateYYMMDD(detailArr[1])
								+ '</i>'
								+ detailArr[2]
								+ '</span> <span>'
								+ detailArr[3]
								+ '</span> <span>已结束</span> </li>';
					}
				}
			}
			if (arr.length > 3) {
				$("#seeMore_" + _docid).show();
			}
		}
		return content;
	};
});
app.filter('splitLabelNames',
		function($sce) { // 可以注入依赖
	return function(text) {
		var arr = text.split(",");
		var re_arr=[];
		if(arr!=null&&arr.length>0){
			var content="";
			for(var i=0,len=arr.length;i<len;i++){
				re_arr.push(arr[i]);
				content+='<span>'+re_arr[i]+'</span>';
			};
		}else{
			content='<span>'+arr+'</span>';
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
app.filter('splitHosName',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = "";
		if(text!=null&&text.length>8){
			content = text.substr(0,8)+'...';
		}else{
			content = text;
		}
		return content;
	};
});
app.controller('collectCon',['$scope','$location','$http','$sce','$filter',
    function($scope, $location, $http, $sce, $filter) {$scope.liClass = 1;$scope.bottom = 1;
	//商品详情
	$scope.goodsDetail=function(goodsId){
		if(goodsId){
			window.location.href="../goods/commodity_details.html?goodsId="+goodsId;
		}
		
	};		
	//医生详情
	$scope.doctorDetail = function(event,doctorId) {
		event.stopPropagation();
		window.location.href = "../hospital/doctor_detail.html?id="+doctorId;
	};
	window.addEventListener("swipeleft",function(event){
		event.stopPropagation();
		$scope.liClass=$scope.liClass+1>4?4:$scope.liClass+1;
		$scope.$apply();
	});
	window.addEventListener("swiperight",function(event){
		event.stopPropagation();
		$scope.liClass=$scope.liClass-1<1?1:$scope.liClass-1;
		$scope.$apply();
	});
	/**
	 * 查看详情
	 */
	$scope.detail=function(id){
		if(id){
			window.location.href="../knowledge/article_detail.html?circle="+id;
		}
	};
	$scope.Foll=function(event,id,num){
		//openAlertMsgLoad1("请等待");
		event.stopPropagation();
		if($(event.target).hasClass("know_followed")){
			$(event.target).removeClass("know_followed");
			$(event.target).html("+关注");
		}else{
			$(event.target).addClass("know_followed");
			$(event.target).html("已关注");
		}
		$http.post(basePath + 'knowledgeCircle/updateFollow.action',{id:id},postCfg).then(
			function(response) {
				if(response.data.respCode==1001){
					if(response.data.data.result==2){
						//openAlertMsg("已取消");
					}else{
						//openAlertMsg("关注成功");
					}
				}
		});
	};
							
	/**
	 * 收藏
	 */
	$scope.collect=function(event,id,knowledge){
		//openAlertMsgLoad1("请等待");
		event.stopPropagation();
		$(event.target).toggleClass("know_collected");
		$http.post(basePath + 'knowledgeCircle/knowledgeCollect.action',{id:id},postCfg).then(
				function(response) {
					if (response.data && response.data.respCode == 1001) {
						if(response.data.data.result==2){
							//openAlertMsg("取消收藏");
							knowledge.collectNum--;
							$scope.$broadcast('shanchuSC',id);
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
			//openAlertMsgLoad1("请等待");
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
		//分类别模糊搜索
		$scope.myFunc=function(e){
			var keycode = window.event?e.keyCode:e.which;
			if(keycode==13){
				if($scope.liClass==1){
					$scope.$broadcast('changeJZ', $scope.search);
				}else if($scope.liClass==2){
					$scope.$broadcast('changeGZ', $scope.search);
				}else if($scope.liClass==3){
					$scope.$broadcast('changeWZ', $scope.search);
				}else if($scope.liClass==4){
					$scope.$broadcast('changeSC', $scope.search);
				};
			};
		};
		//取消搜索功能
		$scope.cancel=function(){
			if($scope.liClass==1){
				$scope.$broadcast('changeJZ');
			}else if($scope.liClass==2){
				$scope.$broadcast('changeGZ');
			}else if($scope.liClass==3){
				$scope.$broadcast('changeWZ');
			}else if($scope.liClass==4){
				$scope.$broadcast('changeSC');
			};
		};
		//刷新数据
		$scope.updateRM=function(num){
			$scope.liClass=num;
			$scope.myFunc(num);
		};
							
}]);
//就诊医生
var page1=0,page2=0,page3=0,page4=0,row=5;
app.controller('myJZ', function($scope, $http,$sce) {
	$scope.loadMore1=function(obj){
		$scope.jzDis=true;
		page1++;
		var _this=this;
		$http.post(basePath + 'centerCollect/queryDoctorJZ.action', {
		  page : page1,row : row,name:$scope.jzname,},
		    postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.doclist) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.doclist.push(response.data.data[i]);
					}
				} else {
					$scope.doclist = response.data.data;
				}
				if(response.data.data.length<row){
					if(page1==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						$("#know_more_jz").show();
						$("."+obj).find(".loadBottom").remove();
						//loadOver1(obj,1);
						//$("."+obj).find(".loadBottom").remove();
					}
					$scope.jzDis=true;
				}else{
					$scope.jzDis=false;
				}
			}else{
				//loadOver1(obj,1);
				$("."+obj).find(".loadBottom").remove();
				$scope.jzDis=true;
			}
			});
		};
		
	$scope.$on('changeJZ', function(event, data) {
		page1=0;
		$(".jz").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".jz").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.doclist=null;
		$scope.jzname=data;
		$scope.loadMore1("jz");
	});  
});
//关注医生
app.controller('myGZ', function($scope, $http,$sce) {
	$scope.loadMore2=function(obj){
		$scope.gzDis=true;
		page2++;
		var _this=this;
		$http.post(basePath + 'centerCollect/queryDoctorGZ.action', {
			page : page2,row : row,name:$scope.gzname},
			postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.docs) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.docs.push(response.data.data[i]);
					}
				} else {
					$scope.docs = response.data.data;
					if($scope.docs.length==0){
						$("#GZ").show();
					}else{
						$("#GZ").hide();
					}
				}
				if(response.data.data.length<row){
					if(page2==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						//loadOver1(obj,2);
						$("#know_more_gz").show();
						$("."+obj).find(".loadBottom").remove();
					}
					$scope.gzDis=true;
				}else{
					$scope.gzDis=false;
				}
			}
			});
		};
		
	$scope.$on('changeGZ', function(event, data) {
		page2=0;
		$(".gz").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".gz").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.docs=null;
		$scope.gzname=data;
		$scope.loadMore2("gz");
	});  
});
//收藏文章
app.controller('myWZ', function($scope, $http,$sce) {
	$scope.loadMore3=function(obj){
		$scope.wzDis=true;
		page3++;
		var _this=this;
		$http.post(basePath + 'centerCollect/queryCollectArticle.action', {
			page : page3,
			row : row,
			name:$scope.wzname
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.knowledges) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.knowledges.push(response.data.data[i]);
					}
				} else {
					$scope.knowledges = response.data.data;
					if($scope.knowledges.length==0){
						$("#scWZ").show();
					}else{
						$("#scWZ").hide();
					}
				}
				if(response.data.data.length<row){
					if(page3==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						//loadOver1(obj,3);
						$("#know_more_sc").show();
						$("."+obj).find(".loadBottom").remove();
					}
					$scope.wzDis=true;
				}else{
					$scope.wzDis=false;
				}
			}else{
				//loadOver1(obj,3);
				$("."+obj).find(".loadBottom").remove();
				$scope.wzDis=true;
			}
			});
		};
		
	$scope.$on('changeWZ', function(event, data) {
		page3=0;
		$(".wz").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".wz").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.knowledges=null;
		
		$scope.wzname=data;
		$scope.loadMore3("wz");
	});  
	//当在收藏栏目点击取消收藏的时候清除这一列内容
	$scope.$on('shanchuSC', function(event, data) {
		for ( var int = 0; int < $scope.knowledges.length; int++) {
			if($scope.knowledges[int].id==data){
				$scope.knowledges.splice(int,1);
				if($scope.knowledges==0){
					$("#scWZ").show();
					$("#know_more_sc").hide();
				}
			}
		}
	}); 
});
//收藏商品
app.controller('mySC', function($scope, $http,$sce) {
	$scope.loadMore4=function(obj){
		$scope.scDis=true;
		page4++;
		var _this=this;
		$http.post(basePath + 'centerCollect/queryCollectGoods.action', {
			page : page4,
			row : row,
			name:$scope.scname
		}, postCfg).then(function(response) {
			if (response.data && response.data.respCode == 1001) {
				if ($scope.goods) {
					for(var i=0;i<response.data.data.length;i++){
						$scope.goods.push(response.data.data[i]);
					}
				} else {
					$scope.goods = response.data.data;
				}
				if(response.data.data.length<row){
					if(page4==1&&response.data.data.length==0){
						$("."+obj).find(".loadBottom").remove();
					}else{
						//loadOver1(obj,4);
						$("#know_more_sp").show();
						$("."+obj).find(".loadBottom").remove();
					}
					$scope.scDis=true;
				}else{
					$scope.scDis=false;
				}
			}else{
				//loadOver1(obj,4);
				$("."+obj).find(".loadBottom").remove();
				$scope.scDis=true;
			}
			});
		};
		
	$scope.$on('changeSC', function(event, data) {
		page4=0;
		$(".sc").find(".mui-pull-bottom-pocket.mui-block.mui-visibility").remove();
		$(".sc").append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div><div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
		$scope.goods=null;
		$scope.scname=data;
		$scope.loadMore4("sc");
	});  
});

function loadOver1(obj,num){
	$("."+obj).find(".loadBottom").remove();
	var content = "";
	if(num==1){
		content='<div class="know_nomore"><span ng-if="bottom<5" class="ng-scope">没有更多文章</span></div><a class="know_btn">查看更多热门文章</a>';
	}else if(num==2){
		content='<div class="know_nomore"><span ng-if="bottom<5" class="ng-scope">没有更多文章</span></div><a class="know_btn">查看更多热门文章</a>';
	}else if(num==3){
		content='<div class="know_nomore"><span ng-if="bottom<5" class="ng-scope">没有更多文章</span></div><a class="know_btn">查看更多热门文章</a>';
	}else if(num==4){
		content='<div class="know_nomore"><span ng-if="bottom<5" class="ng-scope">没有更多文章</span></div><a class="know_btn">查看更多热门文章</a>';
	}
	setTimeout(function(){
		$("."+obj).append(content);
	},100);
}

mui('body').on('tap', '.mine_more', function(event) {
	if ($(this).find("i").hasClass("mine_up")) {
		$(this).find("i").removeClass("mine_up");
		$(this).find("span").html("查看更多");
		$(this).prev().removeClass("mine_ul");
	} else {
		$(this).find("i").addClass("mine_up");
		$(this).find("span").html("收起");
		$(this).prev().addClass("mine_ul");
	}
});
 
$(".scroll").height($(window).height()-70-$(".search_top").height()-10);