var app = angular.module("cardApp", [ 'ngSanitize', 'angular-loading-bar' ]);

app.config([ '$locationProvider', function($locationProvider) {
	// $locationProvider.html5Mode(true);
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
} ]);

var roadPick;
app.controller('cardCon', [ '$scope', '$location', '$http', '$sce', '$filter', function($scope, $location, $http, $sce, $filter) {
	
	if(!isEmpty($location.search().id)){
		$http.post(basePath + 'appDoctor/queryBindBankById.action',{id:$location.search().id},postCfg).then(
				function(response) {
					$scope.card=response.data.data;
					console.log($scope.card);
					$http.post(basePath + 'appDoctor/queryBankList.action').then(
							function(response) {
								//选择开户行
								for(var i=0;i<response.data.data.length;i++){
									if(response.data.data[i].value==$scope.card.bankId){
										$('#bank .bank').html(response.data.data[i].text);
									}
								}
								roadPick = new mui.PopPicker({test:'请选择开户行'});//获取弹出列表组建，假如是二联则在括号里面加入{layer:2}
								roadPick.setData(response.data.data);
								roadPick.pickers[0].setSelectedValue($scope.card.bankId);
								document.querySelector('#bank').addEventListener("tap", function() {
								    roadPick.show(function(item) {//弹出列表并在里面写业务代码
								        var itemCallback=roadPick.getSelectedItems();
								        $scope.card.bankId=itemCallback[0].value;
								        $('#bank .bank').html(itemCallback[0].text);
								    });
								});
					});
		});
	}else{
		$scope.card={};
		$http.post(basePath + 'appDoctor/queryBankList.action').then(
				function(response) {
					console.log(response.data.data);
					//选择开户行
					document.querySelector('#bank').addEventListener("tap", function() {
					    roadPick = new mui.PopPicker({test:'请选择开户行'});//获取弹出列表组建，假如是二联则在括号里面加入{layer:2}
					    roadPick.setData(response.data.data);
					    roadPick.show(function(item) {//弹出列表并在里面写业务代码
					        var itemCallback=roadPick.getSelectedItems();
					        $scope.card.bankId=itemCallback[0].value;
					        $('#bank .bank').html(itemCallback[0].text);
					    });
					});
		});
	}
	
	$scope.history=function(){
		window.location.href='history_card.html?id='+$location.search().doctorId;
	};
	
	$scope.save=function(){
		if(isEmpty($scope.card.bankId)){
			openAlert("请选择开户行")
			return false;
		}
		if(isEmpty($scope.card.bankName)){
			openAlert("请填写支行名称")
			return false;
		}
		if(isEmpty($scope.card.bankCode)){
			openAlert("请填写银行卡号")
			return false;
		}
		 var pattern = /^([1-9]{1})(\d{14}|\d{15}|\d{16}|\d{16}|\d{17}|\d{18}|\d{19})$/;
         if(!pattern.test($scope.card.bankCode)){  
        	 openAlert("请正确填写银行卡号");
 			return false;
         }
		openAlertMsgLoad("提交中");
		$http.post(basePath + 'appDoctor/editBindBank.action',{id:$scope.card.id,bankId:$scope.card.bankId,bankName:$scope.card.bankName,bankCode:$scope.card.bankCode,doctorId:$location.search().doctorId},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						openAlert("提交成功",function(){
							if(isEmpty($scope.card.id)){
								$scope.card.id=response.data.data.result;
							}
							window.location.href = "bill.html?id="+$location.search().doctorId;
						});
					}else{
						openAlertMsg(response.data.respMsg);
					}
					closeAlertMsgLoad();
		});
	};

} ]);