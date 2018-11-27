var app = angular.module("orderApp", ['ngSanitize','angular-loading-bar']);

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
var obj;
app.controller('orderCon', ['$scope','$location','$http','$sce','$filter',function($scope, $location, $http,$sce,$filter) {
	obj=$scope;
	if(!isEmpty($location.search().ids)){//如果是购物车购买
		$http.post(basePath + 'goodsShopCart/queryShopCartById.action',{"id":$location.search().ids},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.goods=response.data.data;
						if(null==$scope.goods||$scope.goods.length==0){
							
						}else{
							$scope.$on('ngRepeatFinished', function() {
								$scope.calculatePrice();
							});
						}
					}else{
						openAlertMsg(response.data.respMsg);
					}
			});
	}
	if(!isEmpty($location.search().goodsId)&&!isEmpty($location.search().priceNumId)){//如果是商品详情直接购买
		$http.post(basePath + 'goodsShopCart/queryGoodsById.action',{"goodsId":$location.search().goodsId,"priceNumId":$location.search().priceNumId,"buyCount":$location.search().buyCount},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.goods=response.data.data;
						$scope.$on('ngRepeatFinished', function() {
							$scope.calculatePrice();
						});
					}else{
						openAlertMsg(response.data.respMsg);
					}
			});
	}
	
	//计算价格
	$scope.calculatePrice=function(){
		var activitys=[];
			var sales=0.0;
			var prices=0.0;
			$.each($scope.goods,function(i,it){
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
						var flag=false;
						$.each(activitys,function(inde,activity){
							if(activity.id==ite.id){
								flag=true;
							}
						});
						if(!flag){
							activitys.push(ite);
						}
					}
				});
				sales+=sale;
				prices+=price;
			});
			$.each(activitys,function(inde,activity){
				var sale=0.0;
				var price=0.0;
				var activityPrices=0.0;
				$.each($scope.goods,function(i,it){
					$.each(it.activity,function(ind,ite){
						if(ite.id==activity.id){
							price += parseInt(it.buyCount)*parseFloat(it.price);
							activityPrices += it.activityPrices;
						}
					});
				});
				var json=JsonSort(activity.activityDetail,"activityPrice");
				$.each(json,function(i,activityDetail){
					if(!$scope.mallActivity){
						$scope.mallActivity="满"+activityDetail.activityPrice+(activity.activityType==1?"折":"减")+activityDetail.activityFold;
					}
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
			$scope.sales=sales;
			$scope.prices=parseFloat(prices);
	};
	
	//选择收货地址
	$scope.checkAddr = function(event) {
		event.stopPropagation();
//		if ($("#select_address").length > 0) {
//			$("#select_address").parent().show();
//			openAlertMsgLoad("加载中");
//		}
//		return false;
		window.location.href = "../shippingaddress/select_address.html?type=2&ids="+$location.search().ids+"&goodsId="+$location.search().goodsId+"&priceNumId="+$location.search().priceNumId+"&buyCount="+$location.search().buyCount+"&doctorId="+$location.search().doctorId+"&patientId="+$location.search().patientId;
	};
	
	//获取默认收货地址
	$http.post(basePath+ 'shippingAddress/queryShippingAddressByDefault.action')
	.then(
			function(response) {
				if (response.data&&response.data.respCode==1001) {
					$scope.shippingAddressId = response.data.data.id;
					$scope.consignee = response.data.data.consignee;
					$scope.shippingAddressPhone = response.data.data.phone;
					$scope.detailedAddress =response.data.data.detailedAddress;
					$scope.province =response.data.data.province;
					$scope.city =response.data.data.city;
					$scope.area =response.data.data.area;
					$scope.addr = true;
					$scope.getPostage();
				} else {
					$scope.addr = false;
				}
			});
	
	//$scope.getAddr = function(addrId) {
		//closeAlertMsgLoad();
	//查询已选择的收货地址
	if($location.search().shippingAddressId){
		$http
				.post(
						basePath
								+ 'shippingAddress/queryShippingAddressById.action',
						{
							shippingAddressId : $location.search().shippingAddressId
						}, postCfg)
				.then(
						function(response) {
							if (response.data&&response.data.respCode==1001) {
								$scope.shippingAddressId = response.data.data.id;
								$scope.consignee = response.data.data.consignee;
								$scope.shippingAddressPhone = response.data.data.phone;
								$scope.detailedAddress = response.data.data.detailedAddress;
								$scope.province =response.data.data.province;
								$scope.city =response.data.data.city;
								$scope.area =response.data.data.area;
								$scope.addr = true;
								$scope.getPostage();
							}
						});
	};
	
	//计算运费
	$scope.getPostage=function(){
		if($scope.city){
			if(!isEmpty($location.search().ids)){
				$http.post(basePath + 'goodsShopCart/queryPostageByCartIds.action',{"city":$scope.city,ids:$location.search().ids},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								$scope.postage=parseFloat(response.data.data.result);
								//$scope.prices=parseFloat($scope.prices)+parseFloat($scope.postage);
							}else{
								//openAlertMsg(response.data.respMsg);
							}
					});
			}else if(!isEmpty($location.search().goodsId)&&!isEmpty($location.search().priceNumId)){
				$http.post(basePath + 'goodsShopCart/queryPostageByCityName.action',{goodsId:$location.search().goodsId,city:$scope.city,buyCount:$location.search().buyCount},postCfg).then(
					function(response) {
						if(response.data&&response.data.respCode==1001){
							$scope.postage=parseFloat(response.data.data.result);
						}else{
							//openAlertMsg(response.data.respMsg);
						}
				});
			}
		}
	};
	
	//确认订单
	$scope.addOrder=function(){
		var arr;
		if($location.search().ids){
			arr=$location.search().ids.split(",");
		}
		if((null==arr||arr.length==0)&&!$location.search().goodsId){
			openAlert("您还没选择商品");
			return true;
		}
		if($scope.goods&&isEmpty($scope.shippingAddressId)){
			openAlert("请选择收货地址");
			return true;
		}
		$scope.isSubmit=true;
		if(!$scope.isSubmit){
			return true;
		}
		$scope.isSubmit=false;
		openAlertMsgLoad("提交中");
		$scope.isSubmit=true;
		if(!isEmpty($location.search().goodsId)){
			$http.post(basePath + 'goodsOrder/addGoodsOrderByGoods.action',{priceNumId:$location.search().priceNumId,buyCount:$location.search().buyCount,goodsId:$location.search().goodsId,shippingAddressId:$scope.shippingAddressId,doctorId:$location.search().doctorId,patientId:$location.search().patientId},postCfg).then(function(response) {
				if(response.data&&response.data.respCode==1001){
					window.location.href='../pay/pay.html?orderNo='+response.data.data.result;
				}else{
					openAlertMsg(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
		}else{
			$http.post(basePath + 'goodsOrder/addGoodsOrder.action',{shopCarts:$location.search().ids,shippingAddressId:$scope.shippingAddressId},postCfg).then(function(response) {
				if(response.data&&response.data.respCode==1001){
					window.location.href='../pay/pay.html?orderNo='+response.data.data.result;
				}else{
					openAlertMsg(response.data.respMsg);
				}
				closeAlertMsgLoad();
			});
		}
	};
	
}]);

//json排序
function JsonSort(json,key){
	if(null==json){
		return null;
	}
    for(var j=1,jl=json.length;j < jl;j	++){
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
//json排序
function JsonSortAsc(json,key){
	if(null==json){
		return null;
	}
    for(var j=1,jl=json.length;j < jl;j++){
        var temp = json[j],
            val  = temp[key],
            i    = j-1;
        while(i >=0 && json[i][key]>val){
            json[i+1] = json[i];
            i = i-1;    
        }
        json[i+1] = temp;
        
    }
    return json;

}