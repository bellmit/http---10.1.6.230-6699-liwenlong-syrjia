
var app = angular.module("billApp", ['angular-loading-bar']);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);


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

app.filter('break', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		var good=text.goods.split(",");
		if(text.orderTypes==4){
			content='<h4>子订单号：'+text.orderNo+'</h4><div class="settle_goods"><div class="settle_left"><img src="../../img/tlfa.png"><p>调理方案</p></div><div class="settle_right">'
				+'<p>￥'+good[4]+'</p>'
				+'<span>×1</span></div></div><p class="settle_status">'+text.payState+'</p>';
		}else if(text.orderTypes==2){
			content='<h4>子订单号：'+text.orderNo+'</h4><div class="settle_goods"><div class="settle_left"><img src="'+good[1]+'"><p>'+good[5]+'</p></div><div class="settle_right">'
				+'<p>￥'+good[4]+'</p>'
				+'<span>×1</span></div></div><p class="settle_status">'+text.payState+'</p>';
		}else if(text.orderTypes==12){
            content='<h4>子订单号：'+text.orderNo+'</h4><div class="settle_goods"><div class="settle_left"><img src="'+good[1]+'"><p>锦旗收益</p></div><div class="settle_right">'
                +'<p>￥'+good[4]+'</p>'
                +'<span>×1</span></div></div><p class="settle_status">'+text.payState+'</p>';
        }else{
			var goods=text.goods.split("&");
			for(var i=0;i<goods.length;i++){
				if(i==0){
					content+='<h4>子订单号：'+text.orderNo+'</h4>';
				}
				good=goods[i].split(",");
				content+='<div class="settle_goods"><div class="settle_left"><img src="'+good[4]+'"><p>'+good[1]+'</p></div><div class="settle_right">'
				+'<p>￥'+good[2]+'</p>'
				+'<span>×'+good[3]+'</span></div></div><p class="settle_status">'+text.payState+'</p>';
			}
		}
		return content;
	};
});

app.filter('encrypt', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = '';
		var len=text.length;
		var start=text.substring(0,5);
		var end=text.substring(len-4,len);
		content=start+" **** **** "+end;
		return content;
	};
});

var obj;
app.controller('billCon',['$scope','$location','$http',function($scope, $location, $http) {
		$http.post(basePath + 'appDoctor/querySettlementTotal.action',{doctorId:$location.search().id},postCfg).then(
				function(response) {
					if (response.data&&response.data.respCode==1001) {
						$scope.total = response.data.data;
						if($scope.total.type==2){
							$http.post(basePath + 'appDoctor/queryFollowBySrId.action',{srId:$location.search().id},postCfg).then(
									function(response) {
										 if(response.data.data){
								        	   response.data.data.unshift({"value":'',text:'全部'});
								         }
									//选医生
								    document.querySelector('#settle_doctor').addEventListener("tap", function() {
								           var roadPick = new mui.PopPicker({test:'选医生'});
								           roadPick.setData(response.data.data);
								           roadPick.show(function(item) {
								               var itemCallback=roadPick.getSelectedItems();
								               $('#settle_doctor .settle_doctor').html(itemCallback[0].text);
								               $scope.checkDoctorId=itemCallback[0].value;
								           });
								    });
							});
						}
					}
		});
		obj=$scope;
		$scope.startTime=null;
		$scope.endTime=null;
		
		var page = 0;
		var row = 20;
		var month=[];
		mui.init({
			pullRefresh : {
				container : '#pullrefresh', // 待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
				up : {
					height : 50, // 可选.默认50.触发上拉加载拖动距离
					auto : true, // 可选,默认false.自动上拉加载一次
					contentrefresh : "正在加载...", // 可选，正在加载状态时，上拉加载控件上显示的标题内容
/*					contentnomore : '<p class="commo_none"><span>没有更多数据</span></p>', // 可选，请求完毕若没有更多数据时显示的提醒内容；
*/					callback : function() { // 必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
						var _this = this;
						page++;
						$http.post(basePath + 'appDoctor/querySettlement.action', {
							page : page,
							row : row,
							doctorId : $location.search().id,
							checkDoctorId :$scope.checkDoctorId,
							startTime :$scope.startTime,
							endTime :$scope.endTime,
							settlementType:$scope.settlementType
						}, postCfg)
							.then(function(response) {
								console.log(response.data.data);
								if (response.data && response.data.respCode == 1001) {
									if (response.data.data.length == 0) {
										_this.endPullupToRefresh(true);
									} else {
										if (null != $scope.orders) {
											for(var i=0;i<response.data.data.length;i++){
												if($.inArray(response.data.data[i].date,month)==-1){
													month.push(response.data.data[i].date);
													$scope.orders.push({month:response.data.data[i].date,monthPrice:response.data.data[i].monthPrice,type:1});
												}
												$scope.orders.push(response.data.data[i]);
												
											}
										} else {
											$scope.orders=[];
											
											for(var i=0;i<response.data.data.length;i++){
												if($.inArray(response.data.data[i].date,month)==-1){
													month.push(response.data.data[i].date);
													$scope.orders.push({month:response.data.data[i].date,monthPrice:response.data.data[i].monthPrice,type:1});
												}
												$scope.orders.push(response.data.data[i]);
												
											}
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
								console.log($scope.orders);
							});
					}
				}
			}
		});
		
		$scope.monthBill=function(){
			//window.location.href = "settle_accounts.html";
		};
		
		$scope.historyCard=function(){
			window.location.href = "history_card.html?id="+$location.search().id;
		}
		
		$(".settle_month ul li h3").on("click",function(){
				$(this).find(".settle_slow").find("i").toggleClass("settle_hide");
				$(this).next().slideToggle("slow");
		});
		
		document.querySelector('#settle_type').addEventListener("tap", function() {
            var roadPick = new mui.PopPicker({test:'选类型'});
            roadPick.setData([{
            	value:'',
                text: "全部"
            },{
            	value:1,
                text: "商品引流收益"
            }, {
            	value:2,
                text: "商品推广收益"
            }, {
            	value:3,
                text: "调理服务收益"
            }, {
            	value:4,
                text: "调理引流收益"
            }, {
            	value:5,
                text: "图文调理收益"
            }, {
            	value:7,
                text: "图文咨询收益"
            }, {
            	value:8,
                text: "电话调理收益"
            }, {
            	value:9,
                text: "电话咨询收益"
            }, {
            	value:11,
                text: "锦旗收益"
            }
            ]);
            roadPick.show(function(item) {
                var itemCallback=roadPick.getSelectedItems();
                $('#settle_type .settle_type').html(itemCallback[0].text);
                $scope.settlementType=itemCallback[0].value;
            });
    	});
    	//选类型  end

		$scope.search=function(){
			page=0;
			$scope.orders=null;
			 month=[];
			mui('#pullrefresh').pullRefresh().refresh(true);
			mui('#pullrefresh').pullRefresh().pullupLoading();
			mui('#pullrefresh').pullRefresh().scrollTo(0,0);
			
			/*$http.post(basePath + 'appDoctor/querySettlementTotal.action',{doctorId:$location.search().id,checkDoctorId :$scope.checkDoctorId,startTime :$scope.startTime,endTime :$scope.endTime,settlementType:$scope.settlementType},postCfg).then(
					function(response) {
						if (response.data&&response.data.respCode==1001) {
							$scope.total = response.data.data;
						}
			});*/
		};

		(function($) {
		    var startTime = $('#startTime');
		    startTime.each(function(i, btn) {
		        btn.addEventListener('tap', function() {
		             var optionsJson = this.getAttribute('data-options') || '{}';
		             var options = JSON.parse(optionsJson);
		             var id = this.getAttribute('id');
		             var picker = new $.DtPicker(options);
		             picker.show(function(rs) {
		            	if($scope.endTime&&compareDate(rs.text,$scope.endTime)){
	        				openAlert("开始时间不能大于结束时间");
	        				picker.dispose();
	        				return false;
	        			}
		            	$scope.startTime=rs.text;
		            	$scope.$apply();
		                picker.dispose();
		              });
		          }, false);
		      });
		        
		       var  endTime = $('#endTime');
		       endTime.each(function(i, btn) {
		        	btn.addEventListener('tap', function() {
		        		var optionsJson = this.getAttribute('data-options') || '{}';
		        		var options = JSON.parse(optionsJson);
		        		var id = this.getAttribute('id');
		        		var picker = new $.DtPicker(options);
		        		picker.show(function(rs) {
		        			if($scope.startTime&&!compareDate(rs.text,$scope.startTime)){
		        				openAlert("结束时间不能小于开始时间");
		        				picker.dispose();
		        				return false;
		        			}
		        			$scope.endTime=rs.text;
		        			$scope.$apply();
		        			picker.dispose();
		        		});
		        	}, false);
		        });
		})(mui);
}]);

function compareDate(DateOne, DateTwo) {
	  var OneMonth = DateOne.substring(5, DateOne.lastIndexOf("-"));
	  var OneDay = DateOne.substring(DateOne.length, DateOne.lastIndexOf("-") + 1);
	  var OneYear = DateOne.substring(0, DateOne.indexOf("-"));
	  var TwoMonth = DateTwo.substring(5, DateTwo.lastIndexOf("-"));
	  var TwoDay = DateTwo.substring(DateTwo.length, DateTwo.lastIndexOf("-") + 1);
	  var TwoYear = DateTwo.substring(0, DateTwo.indexOf("-"));
	  if (Date.parse(OneMonth + "/" + OneDay + "/" + OneYear) >= Date.parse(TwoMonth + "/" + TwoDay + "/" + TwoYear)) {
	    return true;
	  } else {
	    return false;
	  }
	}

mui('body').on('tap','li',function(event) {
	$(this).click();
});

mui('body').on('tap','.settle_binding',function(event) {
	$(this).click();
});

mui('body').on('tap','.settle_inquire p',function(event) {
	$(this).addClass("settle_checked");
	$(this).siblings().removeClass("settle_checked");
	var html = $(this).html();
	var value = $(this).attr("value");
	$(".settle_classify").find("i").html(html).addClass("settle_result");
	$(this).parent().hide();
	obj.settlementType=value;
	obj.$apply();
});