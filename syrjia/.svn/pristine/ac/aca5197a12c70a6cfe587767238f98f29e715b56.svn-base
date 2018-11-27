var app = angular.module("evaluateDoctorApp", [ 'ngSanitize',
		'angular-loading-bar' ]);

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
var labelarr = [];
var labelIdarr = [];
var jqArr = [];
var obj, http;
app.controller('evaluateDoctorCon', [
		'$scope',
		'$location',
		'$http',
		'$sce',
		'$filter',
		function($scope, $location, $http, $sce, $filter) {
			obj = $scope;
			http = $http;
			$http.post(basePath + 'evaluate/queryEvalabels.action', {
				type : 2
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.evaLables = response.data.data;
					$scope.$on('ngRepeatFinished', function() {
						//获取缓存
						var evaDoctor = eval('(' + Util.get("evaDoctor") + ')');
						if (evaDoctor != null && evaDoctor != "" && evaDoctor != undefined) {
							console.log(evaDoctor);
							$scope.evaluateLevel = evaDoctor.evaluateLevel;
							$scope.evaluate_note = evaDoctor.evaluate_note;
							labelIdarr = evaDoctor.labelIds;
							var labelids =  evaDoctor.labelIds;
							if(labelids!=null){
								$.each(labelids,function(i,d){
									$("#"+d).addClass("evadoc_check");
								});
							}
						}
					});
				} else {
					openAlert(response.data.respMsg);
				}
			});
			$scope.myEvaBannersOrders =[];
			$http.post(basePath + 'doctorOrder/queryMyEvaBanners.action', {
				orderNo : $location.search().orderNo
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.myEvaBanners = response.data.data;
					
				} else {
					openAlert(response.data.respMsg);
				}
			});

			$http.post(basePath + 'doctorOrder/queryDoctorOrderDetail.action',
					{
						orderNo : $location.search().orderNo
					}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.orderNo = response.data.data.orderNo;
					$scope.orderDetailId = response.data.data.orderDetailId;
					$scope.docUrl = response.data.data.docUrl;
					$scope.doctorId = response.data.data.doctorId;
					$scope.docName = response.data.data.docName;
					$scope.positionName = response.data.data.positionName;
					$scope.infirmaryName = response.data.data.infirmaryName;
					if(response.data.data.evalId){
						if (document.referrer == '') {
							openAlert("该服务已评价，去医生主页？", function() {
								window.location.href='doctor_detail.html?id='+$scope.doctorId;
							});
						}else{
							window.location.href = basePath
							+ "weixin/order/order_detail2.html?orderNo="
							+ $scope.orderNo;
						};
					};
				} else {
					openAlert(response.data.respMsg);
				}
			});

			$scope.delJp = function(id) {
				openConfirm("取消", "确认取消赠送吗？", function() {
					$("#"+id).removeClass("evadoc_pitch");
					$("#del_"+id).remove();
					$("#img_"+id).addClass("evaImg");
					$("#count_"+id).remove();
				});

			};
			$(".evadoc_mind").on("click",".evadoc_classify",function(){
				if(!$(this).hasClass("evadoc_pitch")){
					//设置缓存
					var _Object = new Object();
					_Object.evaluateLevel = $scope.evaluateLevel;
					_Object.evaluate_note = $scope.evaluate_note;
					_Object.labelIds = labelIdarr;
					_Object.orderNo = $location.search().orderNo;
			    	Util.set("evaDoctor",JSON.stringify(_Object));
					window.location.href = "to_mind.html?orderNo="+ $location.search().orderNo;
				}
			});

			$scope.checkLabel = function(event, name,id) {
				var flag = $(event.target).hasClass("evadoc_check");
				if (flag) {
					$(event.target).removeClass("evadoc_check");
					labelarr.splice($.inArray(name, labelarr), 1);
					labelIdarr.splice($.inArray(id, labelIdarr), 1);
				} else {
					$(event.target).addClass("evadoc_check");
					labelarr.push(name);
					labelIdarr.push(id);
				};
			};

			$scope.save = function() {
				jqArr = [];
				$(".evadoc_pitch").each(function(i,d){
					var evaBanObj = new Object();
					var _type = $(this).attr("_type");
					//if(_type==1){
						evaBanObj.id=$(this).attr("id");
						evaBanObj.count=$(this).attr("_count");
					/*}else{
						evaBanObj.id=$(this).attr("id");
						evaBanObj.count=1;
					}*/
					jqArr.push(evaBanObj);
				});
				labelarr = [];
				$(".evadoc_impress").find(".evadoc_check").each(function(i,d){
					labelarr.push($(this).attr("_name"));
				});
				if (isEmpty($scope.evaluateLevel)) {
					openAlertMsg("请选择评分");
					return false;
				}
				/*if (isEmpty($scope.evaluate_note)) {
					openAlertMsg("请填写评价内容");
					return false;
				}*/
				openConfirm("保存", "确认提交医生评价吗？", function() {
					openAlertMsgLoad("提交中");
					$http.post(basePath + 'doctorOrder/addEvaluate.action', {
						"goodsId": $scope.doctorId,
						"orderNo" : $location.search().orderNo,
						"orderDetailId" : $scope.orderDetailId,
						"evaluateLevel" : $scope.evaluateLevel,
						"evaluate_note" : $scope.evaluate_note,
						"labels" : JSON.stringify(labelarr),
						"jqArr":JSON.stringify(jqArr),
						"myEvaBannersOrders":JSON.stringify($scope.myEvaBannersOrders)
					}, postCfg).then(function(response) {
						closeAlertMsgLoad();
						if (response.data && response.data.respCode == 1001) {
							openAlert("评价成功", function() {
								//删除缓存
								Util.clean("evaDoctor");
								if (document.referrer == '') {
									window.location.href='doctor_detail.html?id='+$scope.doctorId;
								}else{
									window.location.href = basePath
									+ "weixin/order/order_detail2.html?orderNo="
									+ $location.search().orderNo;
								}
							});
						} else {
							openAlert(response.data.respMsg);
						}
					});
				});
			};

		} ]);

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
	// fd.append(item,jsonObj[item]);

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
			if (json && json.respCode == 1001) {
				var content = '';
				$.each(json.data, function(index, item) {
					obj.orders[i].choosePicture.push(item.picId);
					content += '<li><div class="evalua_add"><img src="'
							+ item.url
							+ '"></div><a onclick="delPicture(this,\'' + id
							+ '\',\'' + item.picId + '\')"></a></li>';
				});
				$(event).parents("li").before(content);
			}
			closeAlertMsgLoad();
		}

	};
	xhr.send(fd);
};

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

mui('body').on('tap', 'span', function(event) {
	// &&!isWindow
	if ((this.getAttribute("ng-click") || this.getAttribute("onclick"))) {
		event.stopPropagation();
		this.click();
	}
});