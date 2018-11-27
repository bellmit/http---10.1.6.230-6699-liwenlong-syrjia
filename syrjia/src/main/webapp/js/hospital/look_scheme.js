function onBridgeReady() {
	    WeixinJSBridge.call('hideOptionMenu');
}
 
if (typeof WeixinJSBridge == "undefined") {
    if (document.addEventListener) {
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    } else if (document.attachEvent) {
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
} else {
    onBridgeReady();
}

var app = angular.module("orderApp", [ 'ngSanitize', 'angular-loading-bar' ]);

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
		var arr = text.split(",");
		var content='';
		for(var i=0;i<arr.length;i++){
			content+='<span>'+arr[i]+'</span>';
		}
		return content;
	};
});

app.filter('num', function($sce) { // 可以注入依赖
	return function(n) {
		 	if(!/(^[1-9]\d*$)/){  
		        return '';  
		    }  
		 	n=n+'';
		    var uppercase='千百亿千百十万千百十个';  
		    var nLength=n.length;  
		    var newStr='';  
		    if(uppercase.length-nLength<0){  
		        return '';  
		    }  
		    uppercase=uppercase.substr(uppercase.length-nLength);
		    for(var i=0;i<nLength;i++){  
		        newStr +='一二三四五六七八九'.charAt(n[i]-1)+uppercase.charAt(i);
		    };  
		    newStr=newStr.substr(0,newStr.length-1);  
		    return newStr;  
	};
});

app.controller('orderCon', [ '$scope', '$location', '$http', '$sce', '$filter',
		function($scope, $location, $http, $sce, $filter) {

			$http.post(basePath + 'appDoctor/queryRecord.action', {
				orderNo : $location.search().orderNo
			}, postCfg).then(function(response) {
				if (response.data && response.data.respCode == 1001) {
					$scope.orders = response.data.data;
					$scope.operaName = $scope.orders.operaName;
					if($scope.orders.visitTime&&$scope.orders.visitTime!=0){
						$scope.visitDate=new Date().setDate(new Date($scope.orders.createTime*1000).getDate()+$scope.orders.visitTime);
					}
					$scope.doctorGoods=[];
					$scope.mallGoods=[];
					$scope.doctorTotal=0;
					$scope.mallTotal=0;
					$.each($scope.orders.goods,function(index,item){
						if(item.isDoctor==1){
							$scope.doctorGoods.push(item);
						}else{
							$scope.mallGoods.push(item);
						}
					});
					$scope.calculatePrice($scope.doctorGoods,1);
					$scope.calculatePrice($scope.mallGoods,2);
					$scope.calculatePrice1($scope.doctorGoods,1);
					$scope.calculatePrice1($scope.mallGoods,2);
					console.log($scope.orders);
					$http.post(basePath + 'im/queryDoctorById.action', {
						doctorId : $scope.orders.doctorId
					}, postCfg).then(function(response) {
						if (response.data && response.data.respCode == 1001) {
							$scope.doctor = response.data.data;
						}
					});
				} else {
					openAlert(response.data.respMsg,function(){
						mui.back();
					});
				}
			});
			
			$scope.calculatePrice=function(obj,total){
					var activitys=[];
					var sales=0.0;
					var prices=0.0;
					$.each(obj,function(i,it){
						var sale=0.0;
						var price = parseInt(it.buyCount)*parseFloat(it.price);
						it.activitySales=sale;
						it.activityPrices=price;
						$.each(it.activity,function(ind,ite){
							if(ite.type==1){
								var json=JsonSort(ite.activityDetail,"activityPrice");
								$.each(json,function(inde,activityDetail){
									if(price>=parseFloat(activityDetail.activityPrice)){
										if(ite.activityType==1){
											sale=price-parseFloat(ite.activityFold)*price;
											price=parseFloat(ite.activityFold)*price;
										}else if(ite.activityType==2){
											sale=parseFloat(activityDetail.activityFold);
											price=price-parseFloat(activityDetail.activityFold);
										}
										it.activitySales=sale;
										it.activityPrices=price;
										return false;
									}
								});
							}else{
								activitys.push(ite);
							}
						});
						sales+=sale;
						prices+=price;
					});
					$.each(activitys,function(inde,activity){
						var sale=0.0;
						var price=0.0;
						var activityPrices=0.0;
						$.each(obj,function(i,it){
							$.each(it.activity,function(ind,ite){
								if(ite.id==activity.id){
									price += parseInt(it.buyCount)*parseFloat(it.price);
									activityPrices += it.activityPrices;
								}
							});
						});
						var json=JsonSort(activity.activityDetail,"activityPrice");
						$.each(json,function(i,activityDetail){
							if(price>=parseFloat(activityDetail.activityPrice)){
								if(activity.activityType==1){
									sale=activityPrices-(parseFloat(activityDetail.activityFold/10)*activityPrices);
								}else if(activity.activityType==2){
									sale=parseFloat(activityDetail.activityFold);
								}
								return false;
							}
						});
						sales+=sale;
						prices=prices-sale;
					});
					//obj.sales=sales;
					if(total==1){
						$scope.doctorTotal=prices;
					}else{
						$scope.mallTotal=prices;
					}
			};
			
			$scope.calculatePrice1=function(obj,total){
				var activitys=[];
				var sales=0.0;
				var prices=0.0;
				$.each(obj,function(i,it){
					if(it.check!=1){
						return true;
					}
					var sale=0.0;
					var price = parseInt(it.buyCount)*parseFloat(it.price);
					it.activitySales=sale;
					it.activityPrices=price;
					$.each(it.activity,function(ind,ite){
						if(ite.type==1){
							var json=JsonSort(ite.activityDetail,"activityPrice");
							$.each(json,function(inde,activityDetail){
								if(price>=parseFloat(activityDetail.activityPrice)){
									if(ite.activityType==1){
										sale=price-parseFloat(ite.activityFold)*price;
										price=parseFloat(ite.activityFold)*price;
									}else if(ite.activityType==2){
										sale=parseFloat(activityDetail.activityFold);
										price=price-parseFloat(activityDetail.activityFold);
									}
									it.activitySales=sale;
									it.activityPrices=price;
									return false;
								}
							});
						}else{
							activitys.push(ite);
						}
					});
					sales+=sale;
					prices+=price;
				});
				$.each(activitys,function(inde,activity){
					var sale=0.0;
					var price=0.0;
					var activityPrices=0.0;
					$.each(obj,function(i,it){
						$.each(it.activity,function(ind,ite){
							if(ite.id==activity.id){
								price += parseInt(it.buyCount)*parseFloat(it.price);
								activityPrices += it.activityPrices;
							}
						});
					});
					var json=JsonSort(activity.activityDetail,"activityPrice");
					$.each(json,function(i,activityDetail){
						if(price>=parseFloat(activityDetail.activityPrice)){
							if(activity.activityType==1){
								sale=activityPrices-(parseFloat(activityDetail.activityFold/10)*activityPrices);
							}else if(activity.activityType==2){
								sale=parseFloat(activityDetail.activityFold);
							}
							return false;
						}
					});
					sales+=sale;
					prices=prices-sale;
				});
				//obj.sales=sales;
				if(total==1){
					$scope.doctorMoney=prices;
				}else{
					$scope.mallMoney=prices;
				}
		};
			
			$scope.subtract=function(obj,goodsId,priceNumId,type){
						$.each(obj,function(i,it){
							if(it.priceNumId==priceNumId){
								if(parseInt(it.buyCount) - 1>0){
									it.buyCount=parseInt(it.buyCount) - 1<=0?1:parseInt(it.buyCount) - 1;
									$scope.calculatePrice(obj,type);
									if(it.check==1){
										$scope.calculatePrice1(obj,type);
									}
								}
							}
						});
				
			};
			$scope.add=function(obj,goodsId,priceNumId,type){
						$.each(obj,function(i,it){
							if(it.priceNumId==priceNumId){
								var num=0;
								$.each(it.activity,function(ind,ite){
									num=isEmpty(ite.activityNum)?num:ite.activityNum;
								});
								num=num!=0&&it.stock>num?num:it.stock;
								if(parseInt(it.buyCount) + 1>num){
									openAlertMsg('数量超出范围');
								}else{
									it.buyCount=parseInt(it.buyCount) + 1;
									$scope.calculatePrice(obj,type);
									if(it.check==1){
										$scope.calculatePrice1(obj,type);
									}
								}
							}
						});
					
			};
			
			$scope.more=function(event){
				var _this=$(event.target);
				if(_this.find("i").hasClass("mine_up")){
					_this.find("i").removeClass("mine_up");
					_this.find("span").html("查看更多");
					_this.prev().removeClass("mine_ul");
				}else{
					_this.find("i").addClass("mine_up");
					_this.find("span").html("收起");
					_this.prev().addClass("mine_ul");
				}
			}
			
			$scope.back=function(){
				if (document.referrer == '') {
					window.location.href = 'now_inquiry.html';
				}else{
					mui.back();
				}
			};
			
			$scope.pay=function(){
				if($scope.orders.orderState==0){
					openAlert("调理方已作废");
					return false;
				}
				if($scope.orders.state==0){
					openAlert("医生正在修改调理方案\n请稍后查收新方案");
					return false;
				}
				if($scope.orders.paymentStatus!=1){
					openAlert("订单已支付或已取消");
					return false;
				}
				
				var arr=[];
				$.each($scope.doctorGoods,function(index,item){
					if(item.check==1){
						arr.push({'goodsId':item.goodsId,'priceNumId':item.priceNumId,'buyCount':item.buyCount,'doctorId':$scope.doctor.doctorId});
					}
				});
				
				$.each($scope.mallGoods,function(index,item){
					if(item.check==1){
						arr.push({'goodsId':item.goodsId,'priceNumId':item.priceNumId,'buyCount':item.buyCount,'doctorId':null});
					}
				});
				if(arr.length==0){
					arr=null;
				}
				$http.post(basePath + 'appDoctor/addRecordOrdeNo.action', {
					orderNo : $location.search().orderNo,
					goods:JSON.stringify(arr)
				}, postCfg).then(function(response) {
					if (response.data && response.data.respCode == 1001) {
						window.location.href = "affirm_purchasing.html?orderNo="+$scope.orders.mainOrderNo;
					} else {
						openAlert(response.data.respMsg);
					}
				});
			};
			
			$scope.cartCheck=function(goods,obj,type){
				goods.check==1?goods.check=2:goods.check=1;
				$scope.calculatePrice1(obj,type);
			}
			
		} ]);

function JsonSort(json,key){
    for(var j=1,jl=json.length;j < jl;j++){
        var temp = json[j],
            val  = temp[key],
            i    = j-1;
        while(i >=0 && json[i][key]<val){
            json[i+1] = json[i];
            i = i-1;    
        }
        json[i+1] = temp;
        
    }
    return json;

}