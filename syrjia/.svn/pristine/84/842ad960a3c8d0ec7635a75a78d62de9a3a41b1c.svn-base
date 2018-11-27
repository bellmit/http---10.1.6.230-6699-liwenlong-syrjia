var app = angular.module("shoppingCartApp", ['ngSanitize','angular-loading-bar']);

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
app.controller('shoppingCartCon', ['$scope','$location','$http','$sce','$filter',function($scope, $location, $http,$sce,$filter) {
	//查询购物车
	$http.post(basePath + 'goodsShopCart/queryShopCart.action').then(
		function(response) {
			if(response.data&&response.data.respCode==1001){
				$scope.goods=[];
				$.each(response.data.data,function(index,item){
					var i=item.goods.length;
					while(i--){
						if(item.goods[i]&&item.goods[i].stock<=0){
							$scope.goods.push(item.goods[i]);
							item.goods.splice(i,1);
						}
					}
					if(item.goods.length==0){
						response.data.data.splice(index,1);
					}
				});
				$scope.shopCarts=response.data.data;
				
				$scope.$on('ngRepeatFinished', function() {
					$scope.calculatePrice();
				});
			}else{
				openAlertMsg(response.data.respMsg);
			}
	});
	$scope.total=0.00;
	//减数量
	$scope.subtract=function(goodsId,supplierId,id,pricenumId){
		$.each($scope.shopCarts,function(index,item){
			if(item.id==supplierId){
				$.each(item.goods,function(i,it){
					if(it.id==id){
						if(parseInt(it.buyCount) - 1>0){
							openAlertMsgLoad("保存中");
							$http.post(basePath + 'goodsShopCart/updateGoodsShopCartBuyCount.action',{goodsId:goodsId,id:id,priceNumId:pricenumId,buyCount:-1},postCfg).then(
								function(response) {
									closeAlertMsgLoad();
									if(response.data&&response.data.respCode==1001){
										it.buyCount=parseInt(it.buyCount) - 1<=0?1:parseInt(it.buyCount) - 1;
										$scope.calculatePrice();
										if(it.check&&it.check==1){
											$scope.calculateCheckPrice();
										}
									}else{
										openAlertMsg(response.data.respMsg);
									}
							});
						}
					}
				});
			}
		});
	};
	//加数量
	$scope.add=function(goodsId,supplierId,id,pricenumId){
		$.each($scope.shopCarts,function(index,item){
			if(item.id==supplierId){
				$.each(item.goods,function(i,it){
					if(it.id==id){
						var num=0;
						$.each(it.activity,function(ind,ite){
							num=isEmpty(ite.activityNum)?num:ite.activityNum;
						});
						num=num!=0&&it.stock>num?num:it.stock;
						if(parseInt(it.buyCount) + 1>num){
							openAlertMsg('数量超出范围');
						}else{
							openAlertMsgLoad("保存中");
							$http.post(basePath + 'goodsShopCart/updateGoodsShopCartBuyCount.action',{goodsId:goodsId,id:id,priceNumId:pricenumId,buyCount:1},postCfg).then(
								function(response) {
									closeAlertMsgLoad();
									if(response.data&&response.data.respCode==1001){
										it.buyCount=parseInt(it.buyCount) + 1;
										$scope.calculatePrice();
										if(it.check&&it.check==1){
											$scope.calculateCheckPrice();
										}
									}else{
										openAlertMsg(response.data.respMsg);
									}
							});
						}
					}
				});
			}
		});
	};
	//删除购物车商品
	$scope.deleteShopCart=function(id,type){
		var shopCartIds=[];
		shopCartIds.push(id);
		openConfirm("删除","确认删除此商品？",function(){
			$http.post(basePath + 'goodsShopCart/deleteGoodsShopCart.action',{shopCartIds:shopCartIds.join(",")},postCfg).then(
					function(response) {
						closeAlertMsgLoad();
						if(response.data&&response.data.respCode==1001){
							if(type){
								$.each($scope.goods,function(i,it){
									if(it.id==id){
										$scope.goods.splice(index, 1);
									}
								});
							}else{
								$.each($scope.shopCarts,function(index,item){
									$.each(item.goods,function(i,it){
										if(it.id==id){
											if(item.goods.length==1){
												$scope.shopCarts.splice(index, 1);
											}else{
												item.goods.splice(i, 1);
											}
											$scope.checkBox(item,it);
											$scope.calculatePrice();
											if(it.check&&it.check==1){
												$scope.calculateCheckPrice();
											}
										}
									});
								});
							}
						}else{
							openAlertMsg(response.data.respMsg);
						}
				});
		});
	};
	//计算价格
	$scope.calculatePrice=function(){
		var activitys=[];
		$.each($scope.shopCarts,function(index,item){
			var sales=0.0;
			var prices=0.0;
			$.each(item.goods,function(i,it){
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
				$.each(item.goods,function(i,it){
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
			item.sales=sales;
			item.prices=prices;
		});
	};
	
	$scope.calculateCheckPrice=function(){
		var activitys=[];
		var sales=0.0;
		var prices=0.0;
		$.each($scope.shopCarts,function(index,item){
			$.each(item.goods,function(i,it){
				if(null==it.check||it.check!=1){
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
				$.each(item.goods,function(i,it){
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
		});
		$scope.total=prices;
		$scope.totalSale=sales;
	};
	
	
	$scope.checkBox=function(shopCart,good){
		if(good.check&&good.check==1){
			good.check=null;
		}else{
			good.check=1;
		}
		var q=0;
		$.each(shopCart.goods,function(i,it){
			if(it.check==1){
				q++;
			}
		});
		if(q==shopCart.goods.length){
			shopCart.check=1;
		}else{
			shopCart.check=null;
		}
		var g=0;
		var k=0;
		$.each($scope.shopCarts,function(index,item){
			$.each(item.goods,function(i,it){
				g++;
				if(it.check&it.check==1){
					k++;
				}
			});
		});
		if(g==k){
			$scope.check=1;
		}else{
			$scope.check=null;
		}
		$scope.calculateCheckPrice();
	};
	
	$scope.checkSupplier=function(shopCart){
		if(shopCart.check&&shopCart.check==1){
			shopCart.check=null;
		}else{
			shopCart.check=1;
		}
		$.each(shopCart.goods,function(i,it){
			if(shopCart.check&&shopCart.check==1){
				it.check=1;
			}else{
				it.check=null;
			}
		});
		
		var g=0;
		var k=0;
		$.each($scope.shopCarts,function(index,item){
			$.each(item.goods,function(i,it){
				g++;
				if(it.check&it.check==1){
					k++;
				}
			});
		});
		if(g==k){
			$scope.check=1;
		}else{
			$scope.check=null;
		}
		$scope.calculateCheckPrice();
	};
	
	$scope.checkAll=function(){
		if($scope.check&&$scope.check==1){
			$scope.check=null;
		}else{
			$scope.check=1;
		}
		$.each($scope.shopCarts,function(index,item){
			if($scope.check&&$scope.check==1){
				item.check=1;
			}else{
				item.check=null;
			}
			$.each(item.goods,function(i,it){
				if($scope.check&&$scope.check==1){
					it.check=1;
				}else{
					it.check=null;
				}
			});
		});
		$scope.calculateCheckPrice();
	};
	
	$scope.toPay=function(){
		var ids=[];
		$.each($scope.shopCarts,function(index,item){
			$.each(item.goods,function(i,it){
				if(it.check&&it.check==1){
					ids.push(it.id);
				}
			});
		});
		if(ids.length==0){
			openAlertMsg('请选择商品');
		}else{
			window.location.href = "affirm_order.html?ids="+ids.join(",");
		}
	};
	
	$scope.kf=function(){
		$.post(basePath+"member/getMember.action",function(data){
			if(data.respCode==1001){
				console.log(data);
				window.location.href = "../serverOnline/service.html?costomId="+data.data.id+"&costomName="+data.data.realname+"&costomUrl="+data.data.photo+"&costomType=0";
			}
		});
	};
	
}]);


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

function JsonSortAsc(json,key){
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
