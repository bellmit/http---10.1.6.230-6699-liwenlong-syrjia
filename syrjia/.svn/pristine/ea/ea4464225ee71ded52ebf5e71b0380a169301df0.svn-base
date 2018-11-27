
var app = angular.module("addressApp", ['angular-loading-bar']);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

app.controller('addressCon',['$scope','$location','$http',function($scope, $location, $http) {
	if(null!=$location.search().addrId){
		$http.post(basePath + 'shippingAddress/queryShippingAddressById.action',{shippingAddressId:$location.search().addrId},postCfg).then(
				function(response) {
					if (response.data&&response.data.respCode==1001) {
						$scope.id = response.data.data.id;
						$scope.consignee = response.data.data.consignee;
						$scope.phone = response.data.data.phone;
						$scope.detailedAddress = response.data.data.detailedAddress;
						$scope.isDefault = response.data.data.isDefault;
						$scope.province = response.data.data.province;
						$scope.city = response.data.data.city;
						$scope.area = response.data.data.area;
						$("#J_Address").val($scope.province+" "+$scope.city+" "+$scope.area);
					}
					;
				});
	}
	$(".manage_con").height($(window).height()-72);
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
						$(this).val($scope.province+" "+$scope.city+" "+$scope.area)
					});
				}
			});
	
	$scope.chooseDefault=function(){
		$("input").blur();
		$scope.isDefault=!$scope.isDefault;
	};
	
	 document.querySelector('body').addEventListener('touchend', function(e) {  
         if(e.target.className != 'input') {  
             document.querySelector('input').blur();  
         }  
     });  
	
	$scope.save=function(){
		$("input").blur();
		if(isEmpty($scope.consignee)){
			openAlert("请填写收货人姓名");
		}else if(isPhone($scope.phone)){
			openAlert("请正确填写联系方式");
		}else if(isEmpty($scope.city)){
			openAlert("请选择所在地区");
		}else if(isEmpty($scope.detailedAddress)){
			openAlert("请填写详细地址");
		}else{
			var data={id:$scope.id,consignee:$scope.consignee,detailedAddress:$scope.detailedAddress,phone:$scope.phone,province:$scope.province,city:$scope.city,area:$scope.area,isDefault:$scope.isDefault};
			
			var url="";
			
			if(isEmpty($scope.id)){
				url='shippingAddress/addShippingAddress.action';
			}else{
				url='shippingAddress/updateShippingAddress.action';
			}
			$http.post(basePath + url,data,postCfg).then(
					function(response) {
					if(isEmpty($scope.id)){
						if(response.data&&response.data.respCode==1001){
							openAlert("新增成功",function(){
								 window.history.go(-1);
								//window.location.href='select_address.html?id='+$location.search().id+"&type="+$location.search().type+"&orderNote="+$location.search().orderNote;
							});
						}else{
							openAlert("新增失败");
						}
					}else{
						if(response.data&&response.data.respCode==1001){
							openAlert("修改成功",function(){
								//window.location.href='select_address.html?id='+$location.search().id+"&type="+$location.search().type+"&orderNote="+$location.search().orderNote;
								 window.history.go(-1);
							});
						}else{
							openAlert("修改失败");
						}
					}
			});
		}
	};
}]);

mui('body').on('tap', '.m-cityselect a', function(event) {
	this.click();
});