var app = angular
		.module("activityApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);
/**
 * 循环完成后回调
 */
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

/**
 * 拆分字符串
 */
app.filter('arr', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		return text.split(",");
	};
});

app.controller('activityCon', [ '$scope', '$location', '$http', '$sce',
		'$filter',
		function($scope, $location, $http, $sce, $filter, $cookieStore) {
			/**
			 * 查询活动信息
			 */
			$http.post(basePath + 'goods/queryMallActivityDetail.action', {
				id : $location.search().id
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					//$scope.banners = response.data.data.picture.split(",");
					$scope.banners = response.data.data.picture;
					$scope.remark =$sce.trustAsHtml(response.data.data.remark);
					$scope.goods = response.data.data.goods;
					/*$scope.$on('ngRepeatFinished', function() {
						setBanner();
					});*/
				} else {
					openAlert(response.data.respMsg);
				}
			});
			
			/**
			 * 跳转到商品详情页面
			 */
			$scope.toGoods=function(id){
				if(id){
					window.location.href = "../goods/commodity_details.html?goodsId=" + id;
				}
			}

		} ]);

/**
 * banner轮播
 */
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
