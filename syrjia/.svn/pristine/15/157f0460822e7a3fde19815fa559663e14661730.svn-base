var app = angular.module("caseHistoryApp", [ 'ngSanitize',
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

app.filter('subPhone', function($sce) { // 可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		var content = text.substr(0, 3) + '****'
				+ text.substr(text.length - 4, text.length);
		return content;
	};
});

app.controller('symptomCon', [ '$scope', '$location', '$http', '$sce',
		'$filter', function($scope, $location, $http, $sce, $filter) {
		$scope.doctorId=$location.search().doctorId;
		$scope.patientId=$location.search().patientId;
		$http.post(basePath + 'im/queryDoctorById.action',{doctorId:$location.search().doctorId},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.doctor=response.data.data;
						console.log($scope.doctor);
					}
		});
		
		$http.post(basePath + 'im/queryPatientById.action',{patientId:$location.search().patientId},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.patient=response.data.data;
					}
		});
		
		$http.post(basePath + 'im/queryHistoryIms.action',{doctorId:$location.search().doctorId,patientId:$location.search().patientId},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.msgs=response.data.data;
						$scope.$on('ngRepeatFinished', function() {
							$(".inquiry_main .inquiry_img").touchTouch();
						});
					}
		});
		
		$scope.equals=function(obj,obj1){
			return obj-obj1>600;
		};
		
		$scope.prev=function(event){
			$(event.target).parent().parent().removeClass("symptom_describe");
		};
		
		$scope.toGoods=function(id){
			window.location.href = "../goods/commodity_details.html?goodsId="+id+"&doctorId="+$scope.doctor.doctorId;
		};
		
		$scope.toKnowledge=function(id){
			window.location.href = "../knowledge/article_detail.html?circle="+id;
		};
		
		$scope.toBelow=function(id){
			window.location.href = "../hospital/line_below.html?id="+id;
		};
		
		$scope.toPayment =function(orderNo){
			window.location.href = "../pay/pay.html?orderNo="+orderNo;
		};
		
		$scope.toPayConditioning=function(orderNo){
			window.location.href = "../hospital/look_scheme.html?orderNo="+orderNo;
		};
		
		$scope.toDoctor =function(){
			window.location.href = "../hospital/doctor_detail.html?id="+$scope.doctor.doctorId;
		};
		
		$scope.be_careful=function(){
			window.location.href = "../im/be_careful.html";
		}
		
		$scope.searchSymptom=function(orderNo){
			if(orderNo){
				window.location.href = "../im/specialTest.html?orderNo="+orderNo+"&doctorId="+$scope.doctor.doctorId+"&title=症状描述&patientId="+$scope.patient.id+"&type=1";
				/*$http.post(basePath + 'im/querySymptom.action',{orderNo:orderNo},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								$scope.symptom=response.data.data;
								$("#symptom_describe").show();
								setTimeout(function(){
									$("#symptom_describe").addClass("symptom_describe");
								},200);
							}
				});*/
			}
		};
		
		$scope.specialTest={};
		$scope.specialTest.testOption=[{}];
		$scope.specialTest.isTongue=1;
		$scope.specialTest.isSurface=1;
		$scope.specialTest.isOther=1;
		
		$scope.searchSpecialTest=function(orderNo,id,title){
			
			window.location.href = "../im/specialTest.html?orderNo="+orderNo+"&id="+id+"&doctorId="+$scope.doctor.doctorId+"&title="+title+"&patientId="+$scope.patient.id+"&type=1";
			
			/*$scope.checkNum=0;
			$scope.uploadNum=0;
			if(id){
				$scope.specialTest.isTongue=2;
				$scope.specialTest.isSurface=2;
				$scope.specialTest.isOther=2;
				$scope.specialTest.tongue=[];
				$scope.specialTest.surface=[];
				$scope.specialTest.other=[];
				$scope.specialTest.state=2;
				openAlertMsgLoad("加载中");
				$http.post(basePath + 'im/querySpecialTestDetailHistory.action',{testId:id},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								$scope.specialTest=response.data.data;
								$scope.specialTest.orderNo=orderNo;
								console.log($scope.specialTest);
								$("#specialTest").show();
								setTimeout(function(){
									$(".indetail_first,.indetail_second,.indetail_third").height($(window).height());
									$(".indetail_first,.indetail_second,.indetail_third").scrollTop(0);
									$("#specialTest").addClass("symptom_describe");
								},200);
								$scope.getCheckNum();
							}
							closeAlertMsgLoad();
				});
			}*/
		};
		
		$scope.checkRedio=function(obj,arr){
			if($scope.specialTest.state==1){
				return false;
			}
			$.each(arr,function(index,item){
				item.checked=2;
			});
			obj.checked=1;
			$scope.getCheckNum();
		};
		
		$scope.checkCheckBox=function(obj){
			if($scope.specialTest.state==1){
				return false;
			}
			obj.checked==1?obj.checked=2:obj.checked=1;
			$scope.getCheckNum();
		};
		
		$scope.getCheckNum=function(){
			var checkNum=0;
			$.each($scope.specialTest.testOption,function(index,item){
				$.each(item.options,function(ind,it){
					if(it.checked==1){
						checkNum++;
						return false;
					}else if(item.optionType==3&&!isEmpty(it.optionName)){
						checkNum++;
						return false;
					}
				});
			});
			$scope.checkNum=checkNum;
		};
		
		$scope.getUploadNum=function(){
			$scope.uploadNum=0;
			$.each($("input[name='tongue']"),function(index,item){
				if($(item).val()){
					$scope.uploadNum++;
					$scope.$apply();
					return false;
				}
			});
			$.each($("input[name='surface']"),function(index,item){
				if($(item).val()){
					$scope.uploadNum++;
					$scope.$apply();
					return false;
				}
			});
		};
		
		$scope.next=function(event,index){
			if($scope.specialTest.state==1){
				$(event.target).parent().parent().next().addClass("symptom_describe");
				return false;
			}
			if(index==1&&$scope.checkNum!=$scope.specialTest.testOption.length){
				//openAlert("请填写完整详细问诊");
				//return false;
			}else if(index==2&&$scope.uploadNum!=($scope.specialTest.isSurface==1&&$scope.specialTest.isTongue==1?2:1)){
				//openAlert("请上传舌照/面照");
				//return false;
			}
			$(event.target).parent().parent().next().addClass("symptom_describe");
		};
		
		
		$scope.tl=function(){
			if($scope.lastOrder.isAccpetAsk!=1){
				openAlert("医生暂不接受问诊！");
				return false;
			}
			if($scope.lastOrder.isOnlineTwGh!=1){
				openAlert("医生暂不接受图文调理！");
				return false;
			}
			window.location.href = "../hospital/affirm_serveorder.html?id="+$scope.doctor.doctorId+"&orderType=4";
		}
		
		$scope.zx=function(){
			if($scope.lastOrder.isAccpetAsk!=1){
				openAlert("医生暂不接受问诊！");
				return false;
			}
			if($scope.lastOrder.isOnlineTwZx!=1){
				openAlert("医生暂不接受图文咨询！");
				return false;
			}
			window.location.href = "../hospital/affirm_serveorder.html?id="+$scope.doctor.doctorId+"&orderType=6";
		}
		$scope.toCasehistory=function(){
			window.location.href = "../hospital/casehistory_list.html";
		}
		
		$scope.save=function(){
			$scope.uploadOtherNum=0;
			$.each($("input[name='other']"),function(index,item){
				if($(item).val()){
					$scope.uploadOtherNum++;
					return false;
				}
			});
			if($scope.uploadOtherNum==0&&$scope.specialTest.isOther==1){
				openAlert("请上传其他资料");
				return false;
			}
			  openAlertMsgLoad("提交中");
			  var form = new FormData();
			  $scope.specialTest.memberId=$scope.patient.memberId;
			  $scope.specialTest.patientId=$scope.patient.id;
			  $scope.specialTest.doctorId=$scope.doctor.doctorId;
			  form.append("test",JSON.stringify($scope.specialTest)); 
			  $.each($("input[name='other']"),function(index,item){
					if($(item).val()){
						 form.append("otherFile",$(item)[0].files[0]);       
					}
			 });
			  $.each($("input[name='tongue']"),function(index,item){
					if($(item).val()){
						 form.append("tongueFile",$(item)[0].files[0]);       
					}
			 });
			  $.each($("input[name='surface']"),function(index,item){
					if($(item).val()){
						 form.append("surfaceFile",$(item)[0].files[0]);       
					}
			 });
			  // 文件对象
			  // XMLHttpRequest 对象
			  var xhr = new XMLHttpRequest();
			  xhr.open("post", basePath + 'im/addSpecialTestHistory.action',true);
			  xhr.onreadystatechange = function () {
				    if (xhr.readyState === 4) {
				        if (xhr.status === 200) {
				        } else {
				        	openAlert('上传失败');
				        }
				    }
				}; 
				 xhr.onload = function (data) {
					var responseUrl = this.responseText;
					var json = eval('(' + responseUrl + ')'); 
					if(json&&json.respCode==1001){
						closeAlertMsgLoad();
				  		var id=json.data.result;
				  		openAlert('提交成功!',function(){
				  			var obj=$(".symptom_describe");
				  			obj.removeClass("symptom_describe");
				  			setTimeout(function(){
				  				$(".symptom").hide();
				  			},200);
				  			pushHistory();
				  		});
				  	 	scope.msgs.push({msgType:9,from_account:scope.patient.id,dataId:id,orderNo:data.orderNo,time:getTime()});
						scope.$apply();
						scrollBottom();
					}else{
						openAlert(json.respMsg); 
					}
			  };
			 xhr.send(form);  
		};
		
		
		$scope.toPayConditioning=function(orderNo){
			window.location.href = "../hospital/look_scheme.html?orderNo="+orderNo;
		};
		
		function successEvent(){
				http.post(basePath + 'im/addSyZxCount.action',{patientId:scope.patient.id,doctorId:scope.doctor.doctorId,count:-1},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								scope.lastOrder.syZxCount=parseInt(response.data.data.result);
							}
				});
		}
		
		$scope.sendZx=function(){
			var fileLen=0;
			for(var i=0;i<3;i++){
				var uploadFile=$("input[name='zx']")[i];
				var len=$(uploadFile)[0].files.length;
				fileLen++;
			}
			if(fileLen==0||isEmpty($scope.content)){
				return false;
			}
			$http.post(basePath + 'im/addSyZxCount.action',{patientId:scope.patient.id,doctorId:scope.doctor.doctorId,count:-1},postCfg).then(
					function(response) {
						if(response.data&&response.data.respCode==1001){
							$scope.lastOrder.syZxCount=parseInt(response.data.data.result);
							$scope.sendMsg($scope.lastOrder.syZxCount);
							for(var i=0;i<3;i++){
								var uploadFile=$("input[name='zx']")[i];
								var len=$(uploadFile)[0].files.length;
							 	for(var j=0;j<len;j++){
							 		var file = $(uploadFile)[0].files[j];
							 	    var fileSize = file.size;
							 	    //先检查图片类型和大小
							 	    if (!checkPic(uploadFile, fileSize)) {
							 	        return;
							 	    }
							 	    uploadPic(file,$scope.lastOrder.syZxCount);
							 	}
							}
							$("input[name='zx']").prev().attr("src","../../img/indetail_add.png");
							$("input[name='zx']").val('');
							$(".textCount").text(0); 
						}else{
							openAlert(response.data.respMsg);
						}
			});
		};
		
}]);


var curPlayAudio = null;

function payAudio(obj){
	var playAudio=$(obj).find("audio")[0];
	var playAudioParent=$(obj).find("a");
	$(".inquiry_playerleft").removeClass("inquiry_playerleft");
	if (curPlayAudio) {
        if (curPlayAudio != playAudio) {
        	curPlayAudio.pause();
        	curPlayAudio = playAudio;
            curPlayAudio.currentTime = 0;
            curPlayAudio.play();
            playAudioParent.addClass("inquiry_playerleft");
            curPlayAudio.onended = function() {
            	 playAudioParent.removeClass("inquiry_playerleft");
            };
        }else{
        	if(curPlayAudio.paused){
        		curPlayAudio.currentTime = 0;
            	curPlayAudio.play();
            	 playAudioParent.addClass("inquiry_playerleft");
            	 curPlayAudio.onended = function() {
                	 playAudioParent.removeClass("inquiry_playerleft");
                };
        	}else{
        		 curPlayAudio.pause();
        		 playAudioParent.removeClass("inquiry_playerleft");
        	}
        }
    } else {
        curPlayAudio = playAudio;
        curPlayAudio.play();
        playAudioParent.addClass("inquiry_playerleft");
        curPlayAudio.onended = function() {
        	 playAudioParent.removeClass("inquiry_playerleft");
        };
    }
}

$(function() {

	$(".history_con").on("click",".histroy_toph", function() {
		$(this).find("i").toggleClass("history_show");
		$(this).next().slideToggle("slow");
	});

	$("#symptom_describe").height($(window).height());
	$(".history_con").height($(window).height()-$(".inquiry_top").height());
	

	function native2ascii(value) {
		var nativecode = value.split("");
		var len = 0;
		for ( var i = 0; i < nativecode.length; i++) {
			var code = Number(nativecode[i].charCodeAt(0));
			/*
			 * if (code > 127) { len += 2; } else {
			 */
			len++;
			// }
		}
		return len;
	}

	$(".symptom_con").on("keyup", ".TextArea1", function() {
		var maxLength = 200;
		var targetLength = native2ascii($(this).val());
		var leftLength = maxLength - targetLength;
		$(this).parent().find("span").text(leftLength);
	});

});

function pushHistory() {
	var state = {
		title : "title",
		url : "#"
	};
	window.history.pushState(state, "title", "#");
}

pushHistory();
window.addEventListener("popstate", function(e) {
	if ($(".symptom:not(:hidden)").length > 0) {
		var obj=$(".symptom_describe");
		obj.removeClass("symptom_describe");
		setTimeout(function(){
			$(".symptom").hide();
		},200);
		pushHistory();
	}else if($("#galleryOverlay:not(:hidden)").length > 0){
		$(".placeholder").click();
		pushHistory();
	} else {
		window.history.back();
	}
}, false);