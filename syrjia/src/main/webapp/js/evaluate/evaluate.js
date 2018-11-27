var app = angular
		.module("evaluateApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
			}, function(html) {
				ele.html(html);
				$compile(ele.contents())(scope);
			});
		}
	};
});

app.filter('break', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var arr = text.split("&");
		var content = '';
		for ( var i = 0; i < arr.length; i++) {
			var goods = arr[i].split(",");
			content += '<div class="order_goods" ng-click="detail(\''
					+ goods[0] + '\')"><img src="' + goods[4]
					+ '"><div class="order_detail">' + '<h3><span>' + goods[1]
					+ '</span> <span>¥' + goods[2] + '</span></h3>' + '<p>'
					+ goods[5] + '</p><p class="order_num">主任医师 <span>x '
					+ goods[3] + '</span></p></div></div>';
		}
		return content;
	};
});

app.filter('float', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		return parseFloat(text);
	};
});

var obj, http;
app
		.controller(
				'evaluateCon',
				[
						'$scope',
						'$location',
						'$http',
						'$sce',
						'$filter',
						function($scope, $location, $http, $sce, $filter) {
							obj = $scope;
							http = $http;
							//查询评论标签
							$http
									.post(
											basePath
													+ 'evaluate/queryEvalabels.action',
											{
												type : 1
											}, postCfg)
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													$scope.evaluates = response.data.data;
												} else {
													openAlert(response.data.respMsg);
												}
											});
							//查询订单详情
							$http
									.post(
											basePath
													+ 'goodsOrder/queryOrderDetailByOrderNo.action',
											{
												orderNo : $location.search().orderNo
											}, postCfg)
									.then(
											function(response) {
												if (response.data
														&& response.data.respCode == 1001) {
													$scope.orders = response.data.data;
												} else {
													openAlert(response.data.respMsg);
												}
											});
							//选择标签
							$scope.checkLabel = function(event, name, order) {
								var flag = $(event.target).hasClass("ccc");
								if (null == order.checkLabel) {
									order.checkLabel = [];
								}
								if (flag) {
									$(event.target).removeClass("ccc");
									order.checkLabel.splice($.inArray(name,
											order.checkLabel), 1);
								} else {
									$(event.target).addClass("ccc");
									order.checkLabel.push(name);
								}
								console.log($scope.orders);
							};
							//提交评论
							$scope.save = function() {
								var arr = [];
								var flag=0;
								$
										.each(
												$scope.orders,
												function(index, item) {
													if (isEmpty(item.evaluateLevel)) {
														flag=1;
														return false;
													}
													if (isEmpty(item.evaluate_note)) {
														flag=2;
														return false;
													}
													var json = {
														"goodsId" : item.goodsId,
														"orderDetailId" : item.orderDetailId,
														"evaluateLevel" : item.evaluateLevel,
														"evaluate_note" : item.evaluate_note,
														"checkLabel" : null == item.checkLabel ? null
																: item.checkLabel
																		.join(","),
														"choosePicture" : null == item.choosePicture ? null
																: item.choosePicture
																		.join(",")
													};
													arr.push(json);
												});
								if(flag==1){
									openAlert("请选择评分");
									return false;
								}
								if(flag==2){
									openAlert("请填写评价内容");
									return false;
								}
								if(arr.length!=$scope.orders.length){
									openAlert("请填写完整");
									return false;
								}
								openConfirm(
										"保存",
										"确认提交商品评价吗？",
										function() {
											$http
													.post(
															basePath
																	+ 'evaluate/addEvaluate.action',
															{
																orderNo : $location
																		.search().orderNo,
																eval:JSON.stringify(arr)
															}, postCfg)
													.then(
															function(response) {
																if (response.data
																		&& response.data.respCode == 1001) {
																	openAlert("保存成功",function(){
																		mui.back();
																	})
																} else {
																	openAlert(response.data.respMsg);
																}
															});
										});
							};

						} ]);
//上次图片
function checkPicture(event) {
	var id = $(event).attr("id");
	var content = '';
	var i;
	$.each(obj.orders, function(index, item) {
		if (item.orderDetailId == id) {
			i = index;
			return false;
		}
	});
	if (obj.orders[i].choosePicture == null) {
		obj.orders[i].choosePicture = [];
	}
	var chooseLen = $(event)[0].files.length;
	if (obj.orders[i].choosePicture.length + chooseLen > 9) {
		openAlert('最多上传9张图片');
	}
	openAlertMsgLoad("上传中");
	var fd = new FormData();
	//fd.append(item,jsonObj[item]); 
	
	for ( var j = 0; j < chooseLen; j++) {
		fd.append('multipartFiles', $(event)[0].files[j]);
	}

	var xhr = new XMLHttpRequest();
	xhr.open("post", basePath + "evaluate/uploadEvaPics.action", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
			} else {
				openAlertMsg('上传失败!');
				closeAlertMsgLoad();
			}
		}
	};
	xhr.onload = function(data) {
		var responseUrl = this.responseText;
		var json = eval('(' + responseUrl + ')');
		if (null == this.responseText) {
			openAlertMsg('上传失败!');
		} else {
			if (json&& json.respCode == 1001) {
				var content='';
				$.each(json.data,function(index,item){
					 obj.orders[i].choosePicture.push(item.picId);
					 content += '<li><div class="evalua_add"><img src="' + item.url + '"></div><a onclick="delPicture(this,\'' + obj.orders[i].orderNo + '\',\'' + item.picId + '\')"></a></li>';
				});
				$(event).parents("li").before(content);
			}
			closeAlertMsgLoad();
		}

	};
	xhr.send(fd);
};
//删除图片
function delPicture(event, orderNo, src) {
	$.each(obj.orders, function(index, item) {
		if (item.orderNo == orderNo) {
			item.choosePicture.splice($.inArray(src, item.choosePicture), 1);
			$(event).parents("li").remove();
			return false;
		}
	});
	console.log(obj.orders);
}