var app = angular.module("indexApp", [ 'ngSanitize', 'angular-loading-bar',
		'ngCookies' ]);

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

app
		.filter(
				'break',
				function($sce) { // 可以注入依赖
					return function(text) {
						if (!text) {
							return "";
						}
						var arr = text.split(",");
						var content = '';
						if (arr.length == 1) {
							content='<div class="store_actione"><img src="'+ arr[0]+'"></div>';
						} else if (arr.length == 2) {
							content = ' <ul class="store_actitwo"><li><span><img src="'+ arr[0]+'"></span></li><li><span><img src="'+ arr[1]+'"></span></li></ul>';
						} else if (arr.length == 3) {
							content = '<div class="store_activity"><div class="store_acctvimg"><img src="'
									+ arr[0]
									+ '" ></div><div class="store_activright"><img src="'
									+ arr[1]
									+ '"><img src="'
									+ arr[2]
									+ '"></div></div>';
						} else if (arr.length == 4) {
							content='<ul class="store_actifour"><li><img src="'+arr[0]+'"></li><li><img src="'+arr[1]+'"></li><li><img src="'+arr[2]+'"></li><li><img src="'+arr[3]+'"></li></ul>';
						}
						return content;
					};
				});

app.filter('len', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return 0;
		}
		var arr = text.split(",");
		alert(arr.length);
		return arr.length;
	};
});

app.controller('indexCon', [
		'$scope',
		'$location',
		'$http',
		'$sce',
		'$filter',
		'$cookieStore',
		function($scope, $location, $http, $sce, $filter, $cookieStore) {
			
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
							        "imgUrl" :'https://mobile.syrjia.com/syrjia/img/logo.png',    // 分享显示的缩略图地址  
							        "link" :$location.absUrl(),    // 分享地址  
							        "desc" :"养生佳品，健康好物，精挑严选，品质保障",   // 分享描述  
							        "title" :'上医仁家商城',   // 分享标题  
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
			
			
			
			$http.post(basePath + 'banner/queryBannerList.action', {
				type : 2,
				port:3
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.banners = response.data.data;
					$scope.$on('ngRepeatFinished', function() {
						setBanner();
					});
				}else{
					openAlertMsg(response.data.respMsg);
				}
			});
			var area = $cookieStore.get("area");
			if (area) {
				$scope.city = area.city;
			} else {
				$scope.city = '全国';
			}

			$http.post(basePath + 'queryArea.action').then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					window.YDUI_CITYS = response.data.data;
					var $target = $('#J_Address');

					$target.citySelect();

					$target.on('click', function(event) {
						event.stopPropagation();
						$target.citySelect('open');
					});

					$target.on('done.ydui.cityselect', function(ret) {
						$scope.city = ret.city;
						$cookieStore.put("area", {
							"province" : ret.provance,
							"city" : ret.city,
							"area" : ret.area
						});
						$(this).val(ret.city);
					});
					if (!area) {
						var geolocation = new BMap.Geolocation();
						// 创建地理编码实例
						var myGeo = new BMap.Geocoder();
						geolocation.getCurrentPosition(function(r) {
							if (this.getStatus() == BMAP_STATUS_SUCCESS) {
								var pt = r.point;
								// 根据坐标得到地址描述
								myGeo.getLocation(pt, function(result) {
									if (result) {
										var addComp = result.addressComponents;
										console.log(addComp);
										$scope.city = addComp.city;
										$cookieStore.put("area", {
											"province" : addComp.province,
											"city" : addComp.city,
											"area" : addComp.district
										});
									}
								});
							}

						});
					}
				}
			});

			$scope.searchClick = function() {
				window.location.href = "search.html";
			};

			$http.post(basePath + 'goods/queryGoodsType.action', {
				page : 1,
				row : 4
			}, postCfg).then(function(response) {
				$scope.goodsTypes = response.data.data.goodsTypes;
				$scope.menus = response.data.data.menu;
				console.log(response.data);
			});

			$http.post(basePath + 'goods/queryMallActivity.action').then(
					function(response) {
						$scope.activitys = response.data.data;
					});

			$scope.toGoodsType = function(id) {
				if (id) {
					window.location.href = "drug_classify.html?id=" + id;
				}
			};

			$scope.activityDetail=function(id){
				if (id) {
					window.location.href = "../activity/activity.html?id=" + id;
				}
			};
			
			$scope.toMenu=function(url){
				if (url) {
					window.location.href = "../../"+url;
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
			}
			
		} ]);

function setBanner() {
	var time = 60; // 进度条时间，以秒为单位，越小越快
	var $progressBar, $bar, $elem, isPause, tick, percentTime;
	$('.owl-carousel').owlCarousel({
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
}

mui('body').on('tap', '.m-cityselect a', function(event) {
	this.click();
});

$(function() {
	var drugH = $(window).height() - $(".store_top").height()
			- $(".drug_banner").height() - $(".store_bot").height();
	$(".drug_con").height(drugH);
});