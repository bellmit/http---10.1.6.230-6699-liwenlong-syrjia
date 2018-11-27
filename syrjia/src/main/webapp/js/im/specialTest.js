var app = angular.module("inquiryApp", ['ngSanitize']);

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

var scope;
app.controller('inquiryCon', ['$scope','$location','$http','$sce','$filter',function($scope, $location, $http,$sce,$filter) {
	
	scope=$scope;
	
	$http.post(basePath + 'im/queryDoctorById.action',{doctorId:$location.search().doctorId},postCfg).then(
			function(response) {
				if(response.data&&response.data.respCode==1001){
					$scope.doctor=response.data.data;
				}
	});
	
	$http.post(basePath + 'im/queryPatientById.action',{patientId:$location.search().patientId},postCfg).then(
			function(response) {
				if(response.data&&response.data.respCode==1001){
					$scope.patient=response.data.data;
				}
	});
	
	$scope.type=$location.search().type;
	
	$scope.title=$location.search().title;
	
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
		if($("input[name='tongueUrl']").length>0){
			$.each($("input[name='tongueUrl']"),function(index,item){
				if($(item).val()){
					$scope.uploadNum++;
					$scope.$apply();
					return false;
				}
			});
		}else if($scope.specialTest.tongue.length>0){
			$scope.uploadNum++;
		}
		if($("input[name='surfaceUrl']").length>0){
			$.each($("input[name='surfaceUrl']"),function(index,item){
				if($(item).val()){
					$scope.uploadNum++;
					$scope.$apply();
					return false;
				}
			});
		}else if($scope.specialTest.surface.length>0){
			$scope.uploadNum++;
		}
	};
	
	$scope.next=function(event,index){
		if($scope.specialTest.state==1){
			$(event.target).parent().parent().next().addClass("symptom_describe");
			return false;
		}
		if(index==1&&$scope.checkNum!=$scope.specialTest.testOption.length){
			openAlert("请填写完整详细问诊");
			return false;
		}else if(index==2&&$scope.uploadNum!=($scope.specialTest.isSurface==1&&$scope.specialTest.isTongue==1?2:1)){
			//openAlert("请上传舌照/面照");
			//return false;
		}
		$(event.target).parent().parent().next().addClass("symptom_describe");
	};
	
	$scope.prev=function(event){
		$(event.target).parent().parent().removeClass("symptom_describe");
	};
	
	$scope.toDoctor =function(){
		window.location.href = "../hospital/doctor_detail.html?id="+$scope.doctor.doctorId;
	};
	
	
	$scope.searchSymptom=function(orderNo){
		if(orderNo){
			$http.post(basePath + 'im/querySymptom.action',{orderNo:orderNo},postCfg).then(
					function(response) {
						if(response.data&&response.data.respCode==1001){
							$scope.symptom=response.data.data;
							$("#symptom_describe").show();
							setTimeout(function(){
								$("#symptom_describe").addClass("symptom_describe");
							},200);
						}
			});
		}
	};
	$scope.specialTest={};
	$scope.specialTest.testOption=[{}];
	$scope.specialTest.isTongue=1;
	$scope.specialTest.isSurface=1;
	$scope.specialTest.isOther=1;
	
	$scope.searchSpecialTest=function(orderNo,id){
		$scope.checkNum=0;
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
							$("#specialTest").show();
							setTimeout(function(){
								$("#specialTest,.indetail_first,.indetail_second,.indetail_third").height($(window).height());
								$(".indetail_first,.indetail_second,.indetail_third").scrollTop(0);
								$("#specialTest").addClass("symptom_describe");
							},200);
							$scope.getCheckNum();
							$scope.getUploadNum();
						}
						closeAlertMsgLoad();
			});
		}
	};
	
	$scope.id=$location.search().id;
	
	if($scope.id){
		$scope.searchSpecialTest($location.search().orderNo,$location.search().id);
	}else{
		$scope.searchSymptom($location.search().orderNo);
	}
	
	
	$scope.toCasehistory=function(){
		window.location.href = "../hospital/casehistory_list.html";
	}
	
	$scope.save=function(){
		if($scope.specialTest.testOption.length>0&&$scope.checkNum!=$scope.specialTest.testOption.length){
			openAlert("请填写完整详细问诊");
			return false;
		}
		
//		$scope.uploadOtherNum=0;
//		$.each($("input[name='otherUrl']"),function(index,item){
//			if($(item).val()){
//				$scope.uploadOtherNum++;
//				return false;
//			}
//		});
//		
//		if(($scope.specialTest.isSurface==1||$scope.specialTest.isTongue==1)&&$scope.uploadNum!=($scope.specialTest.isSurface==1&&$scope.specialTest.isTongue==1?2:1)){
//			openAlert("请上传舌照/面照");
//			return false;
//		}
//		
//		if($scope.uploadOtherNum==0&&$scope.specialTest.isOther==1){
//			openAlert("请上传其他资料");
//			return false;
//		}
		  openAlertMsgLoad("提交中");
		  var form = new FormData();
		  $scope.specialTest.memberId=$scope.patient.memberId;
		  $scope.specialTest.patientId=$scope.patient.id;
		  $scope.specialTest.doctorId=$scope.doctor.doctorId;
		  form.append("test",JSON.stringify($scope.specialTest)); 
		  $.each($("input[name='otherUrl']"),function(index,item){
				if($(item).val()){
					 form.append("otherUrl",$(item).val());       
				}
		 });
		  $.each($("input[name='tongueUrl']"),function(index,item){
				if($(item).val()){
					 form.append("tongueUrl",$(item).val());         
				}
		 });
		  $.each($("input[name='surfaceUrl']"),function(index,item){
				if($(item).val()){
					 form.append("surfaceUrl",$(item).val());         
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
			  			if($location.search().type){
			  				window.location.href = "../hospital/im_history.html?patientId="+$scope.patient.id+"&doctorId="+$scope.doctor.doctorId;
			  			}else{
			  				window.location.href = "inquiry.html?identifier="+$scope.patient.id+"&selToID="+$scope.doctor.doctorId;
			  			}
			  		});
				}else{
					openAlert(json.respMsg); 
				}
		  };
		 xhr.send(form);  
	};
	
}]);

function setImagePreview(obj,len,delLen){
	 var filepath=$(obj).val();
	 if(isEmpty(filepath)){
		 return false;
	 }
	 var name=$(obj).attr("name");
	 var extStart=filepath.lastIndexOf(".");
	 var ext=filepath.substring(extStart,filepath.length).toUpperCase();
	 if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
		openAlert("图片限于bmp,png,gif,jpeg,jpg格式");
		$(obj).val('');
		return false;
	 }
	var f = $(obj)[0].files[0];
	var src = window.URL.createObjectURL(f);
	if($(obj).parents("ul").find("li").length<=len){
		$(obj).parents("ul").append('<li><div class="photo_photo"><img src="../../img/indetail_add.png"><input type="file" name="'+name+'" accept="image/*" onchange="setImagePreview(this,'+len+','+delLen+')"><input type="tetx" name="'+name+'Url"></div><p>点击添加</p></li>');
	}
	//}else{
		$(obj).parent().after('<a delLen="'+delLen+'" len="'+len+'" inputName="'+name+'" class="indetail_delete"></a>');
		$(obj).parents('li').find('p').html('点击修改');
	//}
	imgPreviewUpload(obj,null,function(data){
		$(obj).next().val(data.urllist[0].serverUrl);
		$(obj).val('');
		$(obj).prev()[0].src = data.urllist[0].serverUrl;
		scope.getUploadNum();
	});
}
mui('body').on('tap', '.indetail_delete', function(event) {
	event.stopPropagation();
	var _this=this;
	openConfirm("删除","确认删除吗？",function(){
		//var len=$(_this).parents("ul").find("li").length;
		var delLen=$(_this).attr("delLen");
		var len1=$(_this).attr("len");
		var inputName=$(_this).attr("inputName");
		var len=0;
		$("input[name='"+inputName+"']").each(function(index,item){
			if($(item).val()==''){
				len++;
			}
		});
		if(len<=delLen){
			$(_this).parents("ul").append('<li><div class="photo_photo"><img src="../../img/indetail_add.png"><input type="file" name="'+inputName+'" accept="image/*" onchange="setImagePreview(this,'+len1+','+delLen+')"><input type="tetx" name="'+inputName+'Url"></div><p>点击添加</p></li>');
		}
		$(_this).parent().remove();
		scope.getUploadNum();
	});
});
