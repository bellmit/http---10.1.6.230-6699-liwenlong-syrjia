if (isAndroid) {
	/*
	 * var androidVersion = parseFloat(userAgent.slice(index + 8));
	 * alert(androidVersion); if (androidVersion < 8) {
	 * $(".mediFile").attr("capture", "camera"); }else{
	 * $(".mediFile").removeAttr("capture"); }
	 */
} else if (isiOS) {
	$(".mediFile").removeAttr("capture");
}
app.factory('HttpInterceptor', [ '$q', HttpInterceptor ]);
function HttpInterceptor($q) {
	return {
		request : function(config) {
			/*config.headers.timestamp=new Date().getTime();
			config.headers._token='abc';
			config.headers.sign=getSign(config.data,config.headers.timestamp,config.headers._token);
			console.log(config);*/
			return config;
		},
		requestError : function(err) {
			return $q.reject(err);
		},
		response : function(res) {
			return res;
		},
		responseError : function(err) {
			if (-1 == err.status) {
				// location.reload();
			} else {
				// return $q.reject(err);
			}
		}
	};
}

function getSign(params,timestamp,token) {
    if (typeof params == "string") {
        return paramsStrSort(params,timestamp,token);
    } else if (typeof params == "object") {
        var arr = [];
        for (var i in params) {
            arr.push((i + "=" + params[i]));
        }
        return paramsStrSort(arr.join(("&")),timestamp,token);
    }
}

function paramsStrSort(paramsStr,timestamp,token) {
    var url = paramsStr;
    var urlStr = url.split("&").sort().join("&");
    var newUrl = urlStr + '&timestamp=' + parseInt(timestamp/1000)+ '&_token=' + token;
    return SparkMD5.hash(newUrl);
}

if (app.requires.indexOf("angular-loading-bar") >= 0) {
	app.config([ 'cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
		cfpLoadingBarProvider.includeSpinner = false;
	} ]);
}
app.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push(HttpInterceptor);
} ]);

app.directive('body', function($timeout) { // renderFinish自定义指令
	return {
		restrict : 'E',
		link : function(scope, element, attr) {
			scope.Toptitle = "上医仁家";
		}
	};
});

app.directive('stringToNumber', function() {
	return {
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			ngModel.$parsers.push(function(value) {
				return '' + value;
			});
			ngModel.$formatters.push(function(value) {
				return parseInt(value);
			});
		}
	};
});

app.controller('topCon', function($scope, $location, $http, $sce, $filter) {
	$http.post(basePath + 'goodsShopCart/queryShopCartNum.action').then(
			function(response) {
				if (response.data && response.data.respCode == 1001) {
					if (response.data.data > 0) {
						if ($(".goods-cart1").find("span").length > 0) {
							$(".goods-cart1").find("span").html(
									response.data.data);
						} else {
							$(".goods-cart1").append(
									'<span>' + response.data.data + '</span>');
						}
					} else {
						$(".goods-cart1").find("span").remove();
					}
				}
			});
});

function share(){
	$("body").append('<div class="sy_href"><img src="../../img/sy_share2.png"></div>');
}
if (typeof mui!="undefined") {
	mui('body').on('tap', '.sy_href', function(event) {
		event.stopPropagation();
		$(".sy_href").remove();
	});
}else{
	$('body').on('click', '.sy_href', function(event) {
		event.stopPropagation();
		$(".sy_href").remove();
	});
}



if (typeof mui!="undefined") {
	mui('body').on(
			'tap',
			'a',
			function(event) {
				if ((this.getAttribute("ng-click") || this
						.getAttribute("onclick"))
						&& !isWindow) {
					event.stopPropagation();
					this.click();
				} else if (this.getAttribute("href")) {
					window.location.href = this.getAttribute("href");
				}
			});
	mui('body')
			.on(
					'tap',
					'.goods-cart1,.goods-cart',
					function(event) {
						event.stopPropagation();
						window.location.href = '/HXTravelInterface/weixin/goods/shoppingcartlist.html';
					});
	mui('body').on('tap', '.layer', function(event) {
		event.stopPropagation();
		$(this).addClass("nav_layer");
	});
}

$("body").on("click", ".nav_logo", function(event) {
	event.stopPropagation();
	if ($(".layer").hasClass("nav_layer")) {
		$(".layer").removeClass("nav_layer");
	} else {
		$(".layer").addClass("nav_layer");
	}
});

window.onload = function() {
	document.querySelector('body').addEventListener(
			'touchend',
			function(e) {
				if (e.target.tagName.toLowerCase() != 'input'
						&& e.target.tagName.toLowerCase() != 'textarea') {
					$('input,textarea').blur();
				}
			});
	setTimeout(function() {
		if ($(".nav_logo").length > 0) {
			$(".nav_logo").draggable({
				containment : ".index_body",
				scroll : false
			});
		}
	}, 500);
};
$('input,textarea').on('click', function() {
	var str = navigator.userAgent.toLowerCase();
	var ver = str.match(/cpu iphone os (.*?) like mac os/);
	if (ver && !ver[1].replace(/_/g, ".") >= 11.1) {
		var target = this;
		setTimeout(function() {
			target.scrollIntoView(true);
		}, 100);
	}
});


$(".clBidImg").on("click", ".showImgs", function() {
	openAlertMsgLoad("加载中...");
	var showUrl = $(this).attr("src");
	$(".bigImg").attr("src", showUrl);
	$(".imgBig").show();
	closeAlertMsgLoad();
});

$(".imgBig").click(function() {
	$(".imgBig").hide();
	$(".bigImg").attr("src", "");
});

var Util = {};


//设置缓存
Util.set=function(key,value){
	localStorage[key]=value;
}

//获取缓存
Util.get=function(key){
	return localStorage[key];
}

//删除缓存
Util.clean=function(key){
	//localStorage.clear(key);
	 localStorage.removeItem(key);
}

//设置缓存，value为JSON对象
Util.setJSON=function(key){
	var json = localStorage[key];
	if(json==undefined){
		return null;
	}
	return JSON.parse(json);
}
