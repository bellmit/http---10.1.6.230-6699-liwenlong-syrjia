var app = angular.module("doctorSearchApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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

app.filter('splitMoney', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		if(text.indexOf(";")){
			var arr = text.split(";");
			for (var i = 0; i < arr.length; i++) {
				if(arr[i]!=null&&arr[i]!=' '&&i<4){
					if(i<2){
						content = '¥'+arr[i]+'/次';
					}else{
						content = '¥'+arr[i]+'/'+arr[i+4]+'次';
					}
					break;
				}else{
					content = '¥/次';
				}
			}
		}else{
			content = '¥/次';
		}
		if(content == '¥/次'){
			return '';
		}
		return content;
	};
});


app.filter('splitIllClass', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		if(text.indexOf(",")){
			var arr = text.split(",");
			for (var i = 0; i < arr.length; i++) {
				if(i<5){
					content += '<li>'+arr[i]+'</li>';
				}else{
					content += '<li>...</li>';
					break;
				}
			}
		}else{
			content += '<li>'+arr[i]+'</li>';
		}
		return content;
	};
});

var searchInput;
var illClassId;
app.controller('doctorSearchCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	searchInput = $location.search().name;
	illClassId = $location.search().id;
	if (searchInput) {
		$("#search").attr("placeholder", searchInput);
		if(isEmpty($location.search().id)){
			$scope.searchName=searchInput;
		}
	}else{
		$("#search").attr("placeholder", "搜索病症、医生、医院");
	}	
	$http.post(basePath + 'queryArea.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
					window.YDUI_CITYS = response.data.data;
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
						$(this).val($scope.province+" "+$scope.city+" "+$scope.area);
						$(this).find("span").addClass("doctor_result");
						$(this).find("input").val(ret.city+","+ret.area);
						$(".doctor_con").css("overflow", "auto");
						$(".m-cityselect").css("webkitTransform",
								"translate(0px,100%)");
						page = 0;
						$scope.doctors = null;
						mui('#pullrefresh').pullRefresh().refresh(true);
						mui('#pullrefresh').pullRefresh().pullupLoading();
						mui('#pullrefresh').pullRefresh().scrollTo(0, 0);
					});
				}
			});
	
	$http.post(basePath + 'doctor/queryPositions.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.positions=response.data.data;
				}
	});
	
	$http.post(basePath + 'doctor/queryDeparts.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.departs=response.data.data;
				}
			});

	
	$scope.checkNum=1;
	var page = 0;
	var row = 20;
	mui.init({
		pullRefresh : {
			container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
			up : {
				height : 50, // 可选.默认50.触发上拉加载拖动距离
				auto : true, // 可选,默认false.自动上拉加载一次
				contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
				contentnomore : '<p class="commo_none"><span>没有更多医生</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
				callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
					var _this = this;
					page++;
					$http.post(basePath + 'doctor/queryDoctorList.action', {
						page : page,
						row : row,
						docName : $location.search().name,
						illClassId : $location.search().id,
						departId : $scope.departId,
						searchSort : $scope.searchSort,
						docPositionId : $scope.positionId,
						area:$scope.area,
						city:$scope.city,
						province:$scope.province
					}, postCfg)
						.then(function(response) {
							if (response.data && response.data.respCode == 1001) {
								if (response.data.data.length == 0) {
									_this.endPullupToRefresh(true);
									$(".commo_none").show();
								} else {
									if (null != $scope.doctors) {
										for (var i = 0; i < response.data.data.length; i++) {
											$scope.doctors.push(response.data.data[i]);
										}
									} else {
										$scope.doctors = response.data.data;
									}
									if (response.data.data.length < row) {
										_this.endPullupToRefresh(true);
									} else {
										_this.endPullupToRefresh(false);
									}
								}
							} else {
								_this.endPullupToRefresh(true);
							}
						});
				}
			}
		}
	});
	
	$scope.detail=function(id){
		if(id){
			window.location.href="commodity_details.html?goodsId="+id;
		}
	};
	
	$scope.checkSearch=function(event,num,_sign,id){
		if(num==-1){
			if(_sign=='position'){
				$scope.positionId=null;
			}else if(_sign=='depart'){
				$scope.departId=null;
			}else{
				$scope.searchSort=null;
			}
		}else{
			if(_sign=='position'){
				$scope.positionId=id;
			} else if(_sign=='depart'){
				$scope.departId=id;
			} else{
				$scope.searchSort=id;
			}
		}
		event.stopPropagation();
		$("#"+id).addClass("doctor_checkon").siblings("p")
				.removeClass("doctor_checkon");
		$("#"+id).parent().parent().hide();
		var resultHtml = $("#"+id).find("i").html();
		$("#"+id).parent().parent().prev().addClass(
				"doctor_result").html(resultHtml);
		$(".doctor_layer").removeClass("doctor_show").css(
				"webkitTransform", "translate(0px,100%)");
		$(".doctor_con").css("overflow", "auto");
		page = 0;
		$scope.doctors = null;
		mui('#pullrefresh').pullRefresh().refresh(true);
		mui('#pullrefresh').pullRefresh().pullupLoading();
		mui('#pullrefresh').pullRefresh().scrollTo(0, 0);
	};
	
	$scope.searchClick=function(){
		window.location.href="search.html";
	};
	
	$scope.doctorDetail = function(event,doctorId,docName) {
		event.stopPropagation();
		window.location.href = "doctor_detail.html?id="+doctorId;
	};

} ]);

mui('body').on('tap', 'li,a,p', function(event) {
	event.stopPropagation();
	this.click();
});

mui('body').on('tap', '.m-cityselect a', function(event) {
	event.stopPropagation();
	this.click();
});
mui('body').on('tap', '.doctor_layer.doctor_show', function(event) {
	event.stopPropagation();
	this.click();
});