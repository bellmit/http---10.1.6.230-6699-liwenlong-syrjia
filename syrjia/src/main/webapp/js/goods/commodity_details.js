var app = angular.module("goodsApp", ['ngSanitize','angular-loading-bar','ngCookies']);

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

app.filter('realName',
		function($sce) { // 可以注入依赖
	return function(text) {
		var content = '';
		if(text.length==1){
			return text;
		}else if(text.length==2){
			return text.substring(0,1)+"*"
		}else if(text.length>2){
			return text.substring(0,1)+"**"+text.substring(text.length-1,text.length)
		}
		return content;
	};
});

var pricenum,scope,http,goodsId;
app.controller('goodsCon', ['$scope','$location','$http','$sce','$filter','$cookieStore',function($scope, $location, $http,$sce,$filter,$cookieStore) {
	goodsId=$location.search().goodsId;
	scope=$scope;
	http=$http;
	//查询商品详情
	$http.post(basePath + 'goods/queryGoodsById.action',{goodsIds:goodsId,memberId:$location.search().memberId},postCfg).then(
		function(response) {
			
			$scope.isShow=true;
			if (response.data && response.data.respCode == 1001) {
				$scope.urls = response.data.data.banner;
				$scope.picture = response.data.data.picture;
				$scope.originalPrice = response.data.data.originalPrice;
				$scope.activityPrice = response.data.data.activityPrice;
				$scope.supplierCity = response.data.data.city;
				$scope.supplierId = response.data.data.supplierId;
				$scope.supplierAreas = response.data.data.areas;
				$.each(response.data.data.activity,function(index,item){
					if(!isEmpty(item.activityNum)){
						$scope.activityNum=item.activityNum;
					}
				});
				$scope.activitys = response.data.data.activity;
				$scope.name = response.data.data.name;
				$scope.isProprietary = response.data.data.isProprietary;
				$scope.description = response.data.data.description;
				$scope.promises = response.data.data.promise;
				$scope.keepId = response.data.data.keepId;
				pricenum = response.data.data.pricenum;
				
				$scope.stock=response.data.data.stock;
				
				$scope.remark=$sce.trustAsHtml(response.data.data.remark);
				var specifications=[];
				$.each(response.data.data.specifications,function(index,item){
					var detail={name:item.specificationsName};
					var specificationsDetail=item.specificationsDetail.split("@");
					var value=[];
					$.each(specificationsDetail,function(i,it){
						var specification=it.split(",");
						value.push({id:specification[0],name:specification[1]});
						
					});
					detail.value=value;
					specifications.push(detail);
				});
				$scope.specifications=specifications;
				$scope.$on('ngRepeatFinished', function() {
					setBanner();
				});
				
				$scope.$on('ngRepeatFinished1', function() {
					$(".commo_up").find(".commo_option:eq(0)").find("li").each(function(index,it){
						var id=$(it).attr("id");
						return setDisable(id);
					});
				});
				//微信分享时用
				$http.post(basePath + 'im/queryScan.action',{url:$location.absUrl()},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								wx.config({
									debug : false, // 开启调试模式,调用的所有api的返回值会在客户端openAlert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
									appId : response.data.data.appId, // 必填，公众号的唯一标识
									timestamp : response.data.data.timestamp, // 必填，生成签名的时间戳
									nonceStr : response.data.data.nonceStr, // 必填，生成签名的随机串
									signature : response.data.data.signature,// 必填，签名，见附录1
									jsApiList : [ 'onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone']
								// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
								});
								
								wx.ready (function () {  
									 // 微信分享的数据  
								    var shareData = {  
								        "imgUrl" :$scope.picture,    // 分享显示的缩略图地址  
								        "link" :$location.absUrl(),    // 分享地址  
								        "desc" :$scope.description,   // 分享描述  
								        "title" :$scope.name,   // 分享标题  
								        success : function () {    
								              //alert("分享成功"); 
								        }   
								        };
								       wx.onMenuShareTimeline(shareData); 
								       wx.onMenuShareAppMessage (shareData);
								       wx.onMenuShareQQ(shareData);
								       wx.onMenuShareWeibo(shareData);
								       wx.onMenuShareQZone(shareData);
								});
								
							}
				});
				
			}else{
				//openAlertMsg1(response.data.respMsg);
			}
	});
	//查询评价列表
	$http.post(basePath + 'evaluate/queryEvaluateList.action',{goodsId:goodsId,memberId:$location.search().memberId,page:1,row:2,type:1},postCfg).then(function(response) {
		if(response.data&&response.data.respCode==1001){
			$scope.evals=response.data.data;
		}else{
			openAlertMsg1(response.data.respMsg);
		}
	});
	//查询好评率
	$http.post(basePath + 'evaluate/queryEvaluateRate.action',{goodsId:goodsId,memberId:$location.search().memberId},postCfg).then(function(response) {
		if(response.data&&response.data.respCode==1001){
			$scope.rate=response.data.data.result;
		}else{
			openAlertMsg1(response.data.respMsg);
		}
	});
	
	var area=$cookieStore.get("area");
	//查询地区
	$http.post(basePath + 'queryArea.action').then(
			function(res) {
				if(area){
					$scope.province=area.province;
					$scope.city=area.city;
					$scope.area=area.area;
					$http.post(basePath + 'goodsShopCart/queryPostageByCityName.action',{goodsId:goodsId,city:$scope.city},postCfg).then(
							function(response) {
								if(response.data&&response.data.respCode==1001){
									$scope.postage=response.data.data.result;
								}else{
									openAlertMsg1(response.data.respMsg);
								}
					});
				}else{
					var geolocation = new BMap.Geolocation();  
					// 创建地理编码实例
					var myGeo = new BMap.Geocoder();      
					geolocation.getCurrentPosition(function(r){  
						if(this.getStatus() == BMAP_STATUS_SUCCESS){  
							var pt = r.point;   
							// 根据坐标得到地址描述
							myGeo.getLocation(pt, function(result){      
								if (result){      
									var addComp = result.addressComponents;  
									$scope.province=addComp.province;
									$scope.city=addComp.city;
									$scope.area=addComp.district;
									$http.post(basePath + 'goodsShopCart/queryPostageByCityName.action',{goodsId:goodsId,city:$scope.city},postCfg).then(
											function(response) {
												if(response.data&&response.data.respCode==1001){
													$scope.postage=response.data.data.result;
												}else{
													//openAlertMsg1(response.data.respMsg);
												}
									});
								}      
							});   
						}  
					});
				}
				
				window.YDUI_CITYS = res.data.data;
				var $target = $('#J_Address');

				$target.citySelect();

				$target.on('click', function(event) {
					event.stopPropagation();
					$target.citySelect('open');
				});

				$target.on('done.ydui.cityselect', function(ret) {
					$scope.province = ret.provance;
					$scope.city = ret.city;
					$scope.area = ret.area;
					$scope.$apply();
					$http.post(basePath + 'goodsShopCart/queryPostageByCityName.action',{goodsId:goodsId,city:$scope.city},postCfg).then(
							function(response) {
								if(response.data&&response.data.respCode==1001){
									$scope.postage=response.data.data.result;
								}else{
									openAlertMsg1(response.data.respMsg);
								}
					});
				});
	});
	//查询会员信息
	$http.post(basePath + 'centerCollect/queryMemberById.action').then(
			function(response) {
				$scope.id = response.data.data.member.id;
				$scope.photo = response.data.data.member.photo;
				$scope.realname = response.data.data.member.realname;
			});
	//客服
	$scope.toKF=function(){
		window.location.href="http://kefu.syrjia.com:8001/chat/Weixin/Chat?costomId="+$scope.id+"&costomName="+$scope.realname+"&costomUrl="+$scope.photo+"&costomType=0"+"&goodsPrice="+$scope.originalPrice+"&goodsReqUrl="+$location.absUrl()+"&goodsName="+$scope.name+"&goodsSpec="+$scope.specifications+"&goodsImg="+$scope.picture+"&supplierId="+$scope.supplierId;
	};
	//收藏
	$scope.keep=function(){
		openAlertMsgLoad1("提交中");
		$http.post(basePath + 'keep/addKeep.action',{goodsId:goodsId,memberId:$location.search().memberId},postCfg).then(function(response) {
			closeAlertMsgLoad1();
			if(response.data&&response.data.respCode==1001){
				if(!isNaN(response.data.data.result)){
					$scope.keepId=null;
					 $(".collect_cancel").show();
	                 $(".collect_success").hide();
	                 setTimeout(function(){
	                     $(".collect_cancel").hide();
	                 },1500);
				}else{
					 $(".collect_success").show();
	                 $(".collect_cancel").hide();
	                 setTimeout(function(){
	                     $(".collect_success").hide();
	                 },1500);
					$scope.keepId=response.data.data;
				}
			}else{
				openAlertMsg1(response.data.respMsg);
			}
		});
	};
	
	// 点击选择规格
	$scope.checkspec = function() {
		if($scope.stock<=0){
			return false;
		}
		$("#commodity_checked").show();
		$(".checked_up").removeClass("commo_checked").addClass(
				"commodity_checked").show();
		$(".commo_auto").addClass("commo_body");
	};

	// 点击关闭选择规格弹框
	$scope.checkmain = function() {
		$("#commodity_checked").hide();
		$(".checked_up").removeClass("commodity_checked")
				.addClass("commo_checked");
		$(".checked_up").hide(100);
		$(".commo_auto").removeClass("commo_body");
	};
	//选择规格参数
	$scope.checkSpec=function(obj){
		if($(obj.target).hasClass("ccc")||$(obj.target).hasClass("commo_checkopt")){
			return false;
		}
		$(obj.target).addClass("commo_checkopt").siblings("li").removeClass("commo_checkopt");
		var key=[];
		var val=[];
		var id=$(obj.target).attr("id");
		$(".commo_checkopt").each(function(index,item){
			key.push($(item).attr("id"));
			val.push($(item).html());
		});
		var p=pricenum[key.join("-")];
		if(p){
			if(p.stock>0){
				$scope.checkId=p.id;
				$scope.checkStock=p.stock;
				$scope.checkPrice=p.price;
				$scope.checkNum=$scope.checkNum>p.stock?p.stock:$scope.checkNum;
				$scope.checkPicture=(isEmpty(p.picture)?$scope.picture:p.picture);
				$scope.check=val.join(" ");
			}else{
				$(".commo_checkopt").removeClass("commo_checkopt");
				for(var q in pricenum){
					var ids=q.split("-");
					if($.inArray(id,ids)>=0){
						if(pricenum[q].stock>0){
							key=[];
							val=[];
							for(var i=0;i<ids.length;i++){
								$("#"+ids[i]).addClass("commo_checkopt");
								key.push(ids[i]);
								val.push($("#"+ids[i]).html());
							}
							p=pricenum[q];
							$scope.checkId=p.id;
							$scope.checkStock=p.stock;
							$scope.checkPrice=p.price;
							$scope.checkNum=$scope.checkNum>p.stock?p.stock:$scope.checkNum;
							if($scope.checkNum>=p.stock){
								$(".fkleefr").addClass("dadada");
							}
							$scope.checkPicture=(isEmpty(p.picture)?$scope.picture:p.picture);
							$scope.check=val.join(" ");
							break;
						}
					}
				}
			}
			// setDisable(id);
		}else{
			// setDisable(id);
		}
		$(".ccc").removeClass("ccc");
		var ccc=[];
		for(var k=0;k<key.length;k++){
			for(var q in pricenum){
				var ids=q.split("-");
				if($.inArray(key[k],ids)>=0){
					if(pricenum[q].stock<=0){
						for(var i=0;i<ids.length;i++){
							if(key[k]!=ids[i]){
								ccc.push(ids[i]);
							}
						}
					}
				}
			}
		}
		
		for(var i=0;i<ccc.length;i++){
			for(var q in pricenum){
				var ids=q.split("-");
				if($.inArray(ccc[i],ids)>=0){
					if(pricenum[q].stock>0){
						//delete ccc[i];
					}else{
						$("#"+ccc[i]).addClass("ccc");
					}
				}
			}
		}
		
		for(var q in pricenum){
			if(pricenum[q].stock>0){
				var ids=q.split("-");
				var j=0;
				for(var i=0;i<ids.length;i++){
					if($("#"+ids[i]).hasClass("ccc")){
						j++;
					}
				}
				if(j==ids.length){
					for(var i=0;i<ids.length;i++){
						$("#"+ids[0]).removeClass("ccc");
					}
				}
			}
		}
};
	//减数量
	$scope.subtract=function(){
		$scope.checkNum=parseInt($scope.checkNum) - 1<=0?1:parseInt($scope.checkNum) - 1;
		$(".fkleefr").removeClass("dadada");
	};
	//加数量
	$scope.add=function(){
		var num=$scope.activityNum>$scope.checkStock?$scope.checkStock:$scope.activityNum;
		if(!num){
			num=$scope.checkStock;
		}
		$scope.checkNum=parseInt($scope.checkNum) + 1>num?num:parseInt($scope.checkNum) + 1;
		if($scope.checkNum>=num){
			$(".fkleefr").addClass("dadada");
		}else{
			$(".fkleefr").removeClass("dadada");
		}
	};
	//添加购物车
	$scope.addShopCart=function(){
		openAlertMsgLoad1("提交中");
		$http.post(basePath + 'goodsShopCart/addGoodsShopCart.action',{buyCount:$scope.checkNum,goodsId:goodsId,priceNumId:$scope.checkId,memberId:$location.search().memberId},postCfg).then(function(response) {
			closeAlertMsgLoad1();
			if(response.data&&response.data.respCode==1001){
				$(".goods_join").show();
		           setTimeout(function join(){
		             $(".goods_join").hide();
		        },2000);
			}else{
				openAlertMsg1(response.data.respMsg);
			}
		});
	};
	//去支付
	$scope.pay=function(){
		if(isEmpty($location.search().doctorId)){
			window.location.href = "affirm_order.html?goodsId=" + goodsId + "&priceNumId=" + $scope.checkId+"&buyCount="+$scope.checkNum;
		}else{
			window.location.href = "affirm_order.html?goodsId=" + goodsId + "&priceNumId=" + $scope.checkId+"&buyCount="+$scope.checkNum+"&doctorId="+$location.search().doctorId+"&patientId="+$location.search().patientId;
		}
	};
	//评价列表
	$scope.queryEvals=function(){
		window.location.href = "../evaluate/evaluate_list.html?goodsId=" + goodsId;
	};
	//跳转购物车
	$scope.toCart=function(){
		window.location.href = "shopping_cart.html";
	};
	
}]);

function setDisable(id){
	var ccc=[];
	var flag=false;
	for(var key in pricenum){
		var ids=key.split("-");
		if($.inArray(id,ids)>=0){
			if(pricenum[key].stock>0){
				for(var i=0;i<ids.length;i++){
				/*
				 * if($("#"+ids[i]).hasClass("ccc")){
				 * $("#"+ids[i]).removeClass("ccc"); }else{
				 */
						$("#"+ids[i]).click();
						flag=true;
					// }
					continue;
				}
				if(flag){
					return false;
				};
			}/*
				 * else{ for(var i=0;i<ids.length;i++){ if(id!=ids[i]){
				 * ccc.push(ids[i]); } } }
				 */
		}
	}
}

function setBanner() {
	// 轮播图
	var time = 6; // 进度条时间，以秒为单位，越小越快
	var $progressBar, $bar, $elem, isPause, tick, percentTime;
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
				$elem.trigger('owl.next')
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
}
		$(function() {
			// 获取页面高
			$(".commo_auto").height($(window).height());
		});
		
		function openAlertMsgLoad1 (content){
			closeAlertMsgLoad1();
			var div='<div class="mask-black mask"></div><div class="wx_loading" id="wxloading" style=""><div class="wx_loading_inner"><i class="wx_loading_icon"></i>'+content+'</div></div>';
			$("body").append(div);
		}
		
		function closeAlertMsgLoad1(){
			$(".mask").remove();
			$("#wxloading").remove();
		}
		
		function openAlertMsg1(content){
			var div='<div class="mui-toast-container mui-active"><div class="mui-toast-message">'+content+'</div></div>';
			$("body").append(div);
			setTimeout(function(){
				$(".mui-toast-container.mui-active").remove();
			},2000);
		};
