var app = angular.module("classifyApp", [ 'ngSanitize', 'angular-loading-bar', 'ngCookies' ]);

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

app.directive('renderFinish2', function($timeout) { // renderFinish自定义指令
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			if (scope.$last === true) {
				$timeout(function() {
					scope.$emit('ngRepeatFinished2');
				});
			}
		}
	};
});

app.controller('classifyCon', [ '$scope', '$location', '$http', '$sce', '$filter', '$cookieStore', function($scope, $location, $http, $sce, $filter, $cookieStore) {
	$http.post(basePath + 'banner/queryBannerList.action', {
		type : 3,
		port:3
	}, postCfg).then(
		function(response) {
			$scope.banners = response.data.data;
			$scope.$on('ngRepeatFinished', function() {
				setBanner();
			});
		});

	var area = $cookieStore.get("area");
	if (area) {
		$scope.city = area.city;
	} else {
		$scope.city = '全国';
	}


	$http.post(basePath + 'goods/queryGoodsType.action').then(
		function(response) {
			$scope.goodsTypes = response.data.data.goodsTypes;
			$scope.$on('ngRepeatFinished1', function() {
				if($location.search().id){
					$scope.findType($location.search().id);
				}else{
					$scope.findType($(".drug_title li:eq(0)").attr("id"));
				}
			});
		});

	$http.post(basePath + 'queryArea.action').then(
		function(response) {
			if (response.data && response.data.respCode == 1001) {
				window.YDUI_CITYS = response.data.data
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

	$scope.findType = function(id) {
		$(".drug_checked").removeClass("drug_checked");
		$("#" + id).addClass("drug_checked");
		$http.post(basePath + 'goods/queryGoodsTypeByTypeId.action', {
			typeId : id
		}, postCfg).then(
			function(response) {
				var types = [];
				$scope.types=[];
				if (response.data && response.data.respCode == 1001) {
					$.each(response.data.data, function(index, item) {
						var t = item.goods.split("&");
						var ty = {
							"name" : item.typeName
						};
						var typ = [];
						for (var i = 0; i < t.length; i++) {
							var g = t[i].split(",");
							typ.push({
								"id" : g[0],
								"name" : g[1],
								"img" : g[2]
							});
						}
						ty.goods = typ;
						types.push(ty);
						$scope.types = types;
						console.log(types);
					});
				} else {
					openAlertMsg(response.data.respMsg);
				}
			});
	};

	$scope.typeDetail = function(id, name) {
		if (id) {
			window.location.href = "commo_list.html?typeId=" + id + "&showName=" + name;
		}
	}

	$scope.searchClick = function() {
		window.location.href = "search.html";
	}
	
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

mui('body').on('tap', '.m-cityselect a', function(event) {
	this.click();
});


$(function() {
	var drugH = $(window).height() - $(".store_top").height() - $(".drug_banner").height() - $(".store_bot").height();
	$(".drug_con").height(drugH);
});