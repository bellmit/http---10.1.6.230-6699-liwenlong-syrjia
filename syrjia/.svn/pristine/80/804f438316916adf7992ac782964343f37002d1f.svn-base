var g = {};

window.onload = function() {
	// touchType = ('createTouch' in document) ? 'tap' : 'click';

	if (location.href.indexOf("book_time") > -1) {
		 g.initScroll();
		
		g.scroll = new TouchScroll({
	        id: 'wrapper',
	        onscroll: function(){
	            g.l = $(".touchscrollelement").position().left;
	            g.t = $(".touchscrollelement").position().top;
	            g.areaObj.css("left",g.l);
	            g.timeObj.css("top",g.t);
	        }
	    })
	}
	
	if (location.href.indexOf("dianping") > -1) {
		$(".header .left,.header .right").addClass("hide");
	}

	g.urlMsg = getURLInformation();
	$(".date-wrap a").each(function(i, item) {
		if ($(item).attr("data-t") == g.urlMsg.t) {
			$("li", item).addClass('active')
			return false;
		}
	})
	if ($(".date-wrap li.active").parent("a").index() > 3) {
		$(".data-change").toggleClass("expend");

	}
	if ($(".date-wrap li").size() == 7) {
		$(".data-change").css(
				"background",
				"none");
	} else if ($(".date-wrap li")
			.size() <= 4) {
		$(".data-change").addClass(
				"hide");
	}

	var loadInterval = setInterval(function() {
		if ($(".loading").attr("data-lock") == "1") {
			$(".loading").addClass("hide").css("background-color",
					"transparent");
			$(".main").removeClass("hide");
			clearInterval(loadInterval);
		}
	}, 500);

	var scrollInterval = setInterval(function() {
		var touchH = $(".touchscrollwrapper").height();
		if (touchH == 0) {
			g.scroll.resize();
		} else {
			// setViewPos();
			clearInterval(scrollInterval);
		}
	}, 100);

	$(".data-change").on("click", function() {
		if ($(".date-wrap").find("li").size() > 4) {
			var _this = $(this);
			_this.toggleClass("expend");
			if ($(".date-wrap li").size() < 7) {
				if ($(".data-change").hasClass('expend')) {
					$(".data-change").addClass("bg");
				} else {
					$(".data-change").removeClass("bg");
				}
			}
		}
	})

	if (minHour >= 2) {
		showHeadTip();
	}

	g.bNopay = parseInt($(".main").attr("data-due"));
}

g.remToPx = function(rem) {
	var win = $(window).width();
	return 100 / 375 * win * rem;
}

//初始化场次时间样式
g.initScroll = function(){
 var oList = $(".book-list"),
     aUl = $("ul",oList),
     ul = aUl.eq(0),
     wid = (g.remToPx(0.5)+1) * aUl.size();
 oList.css({"width":wid});
 g.areaObj = $(".book-area ul");
 g.areaObj.css("width",wid);
 g.timeObj = $(".book-time ul");
 $(".touchscrollelement").css("width",wid);
}

// 显示toast
function showToast(errMsg) {
	$(".toast .toast-msg").text(errMsg);
	$(".toast").removeClass('hide');
	setTimeout(function() {
		$(".toast").animate({
			"opacity" : 0
		}, 300, function() {
			$(this).css("opacity", 1).addClass("hide");
		})
	}, 1000);
}

// 显示头部提示
function showHeadTip() {
	$(".book-headTip").animate({
		"top" : "0"
	}, 800, function() {
		setTimeout(function() {
			$(".book-headTip").animate({
				"top" : "-0.6rem"
			}, 800)
		}, 2000);
	});
}

// 可订场次显示位置调整
// function setViewPos(){
// var l = 0,t = 0,obj,
// aUl = $(".book-list ul");
// rows = aUl.eq(0).find("li").size(),
// cols = aUl.size();
// $(".book-list li").each(function(i,item){
// var li = $(item);
// if(!li.hasClass("disable")){
// l = li.parent("ul").index();
// return false;
// }
// })
// for (var i = 0; i < rows; i++) {
// for (var j = 0; j < cols; j++) {
// obj = $('.book-list ul').eq(j).find("li").eq(i);
// if(!obj.hasClass("disable")){
// t = i;
// var ll = -(g.remToPx(0.5)+1) * l;
// var tt = -(g.remToPx(0.3)+1) * t;
// $(".touchscrollelement").css({"left":ll,"top":tt});
// g.areaObj.css("left",ll);
// g.timeObj.css("top",tt);
// return;
// }
// }
// };
// }

// 调用取消订单接口
var cancelLock = false;

$('#book-Cancel').on("click", function() {
	var order_id = $(this).attr('order_id');
	if (cancelLock) {
		return;
	}
	cancelLock = true;
	var postData = {
		id : order_id,
	}
	postData = typeof objMerge == 'function' ? objMerge(postData) : postData
	$.ajax({
		url : '/order/Cancel',
		type : 'GET',
		dataType : 'JSON',
		cache : false,
		data : postData,
		success : function(res) {
			cancelLock = false;
			var res = JSON.parse(res);
			if (res && res.code == 1) {
				showToast("订单已取消");
				setTimeout(function() {
					location.reload();
				}, 500);
			} else {
				showToast(res.msg);
				setTimeout(function() {
					location.reload();
				}, 500);
			}
			$(".book-noPaySprite").addClass("hide");
		},
		error : function(res) {
			cancelLock = false;
			showToast('网络出错，请稍后再试');
		}
	});
});

// 判断是否可以支付
var hrefLock = false;
$('#book-href')
		.on(
				"click",
				function() {
					var order_id = $(this).attr('order_id');
					if (hrefLock) {
						return;
					}
					hrefLock = true;
					var postData = {
						id : order_id,
					}
					postData = typeof objMerge == 'function' ? objMerge(postData)
							: postData
					$
							.ajax({
								url : '/order/beforepay',
								type : 'GET',
								dataType : 'JSON',
								cache : false,
								data : postData,
								success : function(res) {
									hrefLock = false;
									var res = JSON.parse(res);
									if (res && res.code == 1) {
										var url = $('#book-href').attr(
												"data-href");
										// location.href =
										// $('#book-href').attr("data-href");
										location.href = typeof urlAddParams == 'function' ? urlAddParams(url)
												: url;
									} else {
										showToast(res.data);
										setTimeout(function() {
											location.reload();
										}, 1000);
									}
									$(".book-noPaySprite").addClass("hide");
								},
								error : function(res) {
									hrefLock = false;
									showToast('网络出错，请稍后再试');
								}
							});
				});

function getURLInformation() {
	var urlMsg = {}; // 定义一个空对象urlMsg
	if (window.location.href.split('#')[0].split('?')[1]) {
		var urlSearch = window.location.href.split('#')[0].split('?')[1]
				.split('&');
	}
	if (urlSearch) {
		for ( var i = 0; i < urlSearch.length; i++) {
			urlMsg[urlSearch[i].split('=')[0]] = urlSearch[i].split('=')[1]
					|| "";
		}
	}
	return urlMsg;
}