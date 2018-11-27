
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

var app = angular.module("inquiryApp", ['ngSanitize','ionic']);

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

app.filter('numFilter', function($sce) { //可以注入依赖
	return function(text) {
		if (!text) {
			return "";
		}
		return text.toFixed(2);
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


var onConnNotify = function(resp) {
    var info;
    switch (resp.ErrorCode) {
        case webim.CONNECTION_STATUS.ON:
            webim.Log.warn('建立连接成功: ' + resp.ErrorInfo);
            break;
        case webim.CONNECTION_STATUS.OFF:
            info = '连接已断开，无法收到新消息，请检查下你的网络是否正常: ' + resp.ErrorInfo;
            // openAlert(info);
            webim.Log.warn(info);
            break;
        case webim.CONNECTION_STATUS.RECONNECT:
            info = '连接状态恢复正常: ' + resp.ErrorInfo;
            // openAlert(info);
            webim.Log.warn(info);
            break;
        default:
            webim.Log.error('未知连接状态: =' + resp.ErrorInfo);
            break;
    }
};

var onKickedEventCall=function(obj){
	console.log(obj);
};

//监听事件

var onMsgNotify=function(newMsgList){
	console.log(newMsgList);
    var sess, newMsg, selSess;
    //获取所有聊天会话
    //var sessMap = webim.MsgStore.sessMap();
     
    for (var j in newMsgList) {//遍历新消息
        newMsg = newMsgList[j];
        if (newMsg.getSession().id() == selToID) {//为当前聊天对象的消息
            selSess = newMsg.getSession();
            //在聊天窗体中新增一条消息
            var elems=newMsg.getElems();
            var time=newMsg.getTime();
            var from_account=newMsg.getFromAccount();
            var count=newMsg.getElems().length;
            var html='';
            //for(var i in newMsg.getElems()){
            	 var elem = elems[0];
            	 var type = elem.getType();//获取元素类型
            	 var content=elem.getContent();
                 switch (type) {
                	case webim.MSG_ELEMENT_TYPE.TEXT:
                		convertTextMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId);
                	break;
                	case webim.MSG_ELEMENT_TYPE.IMAGE:
                		convertImageMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId);
                	break;
                	case webim.MSG_ELEMENT_TYPE.CUSTOM:
                       convertCustomMsgToHtml(time,from_account,content);
                    break;
                	case webim.MSG_ELEMENT_TYPE.SOUND:
                		convertSoundMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId);
                     break;
                }
            //}
            //$(".msgflow").append(html);
           //此处插入
            //addMsg(newMsg);
        }
    }
    //消息已读上报，以及设置会话自动已读标记
    webim.setAutoRead(selSess, true, true);
};

var onMsgNotifyHis=function(newMsgList,flag){
    var sess, newMsg, selSess;
     
    for (var j in newMsgList) {//遍历新消息
        newMsg = newMsgList[j];
        if (newMsg.getSession().id() == selToID) {//为当前聊天对象的消息
            selSess = newMsg.getSession();
            //在聊天窗体中新增一条消息
            var elems=newMsg.getElems();
            var time=newMsg.getTime();
            var from_account=newMsg.getFromAccount();
            var count=newMsg.getElems().length;
            var html='';
            	 var elem = elems[0];
            	 var type = elem.getType();//获取元素类型
            	 var content=elem.getContent();
                 switch (type) {
                	case webim.MSG_ELEMENT_TYPE.TEXT:
                		convertTextMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId,flag);
                	break;
                	case webim.MSG_ELEMENT_TYPE.IMAGE:
                		convertImageMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId,flag);
                	break;
                	case webim.MSG_ELEMENT_TYPE.CUSTOM:
                       convertCustomMsgToHtml(time,from_account,content,flag);
                    break;
                	case webim.MSG_ELEMENT_TYPE.SOUND:
                		convertSoundMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId,flag);
                     break;
                }
        }
    }
};

var listeners = {
	    "onConnNotify": onConnNotify //监听连接状态回调变化事件,必填
	    ,"onMsgNotify":onMsgNotify
	    ,"onKickedEventCall" : onKickedEventCall//被其他登录实例踢下线
};
var options={
			"isLogOn":false	
};

var showMsg=function(newMsgList,flag){
    var sess, newMsg, selSess;
    //获取所有聊天会话
    //var sessMap = webim.MsgStore.sessMap();
     
    for (var j=newMsgList.length-1;j>=0;j--) {//遍历新消息
        newMsg = newMsgList[j];
        console.log(newMsg);
        if (newMsg.getSession().id() == selToID) {//为当前聊天对象的消息
            selSess = newMsg.getSession();
            //在聊天窗体中新增一条消息
            var elems=newMsg.getElems();
            var time=newMsg.getTime();
            var from_account=newMsg.getFromAccount();
            var count=newMsg.getElems().length;
            var html='';
            //for(var i in newMsg.getElems()){
            	 var elem = elems[0];
            	 var type = elem.getType();//获取元素类型
            	 var content=elem.getContent();
                 switch (type) {
                	case webim.MSG_ELEMENT_TYPE.TEXT:
                		convertTextMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId,flag);
                	break;
                	case webim.MSG_ELEMENT_TYPE.IMAGE:
                		convertImageMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId,flag);
                	break;
                	case webim.MSG_ELEMENT_TYPE.CUSTOM:
                       convertCustomMsgToHtml(time,from_account,content,flag);
                    break;
                	case webim.MSG_ELEMENT_TYPE.SOUND:
                		convertSoundMsgToHtml(time,from_account,content,eval('(' +elems[1].getContent().getData()+ ')').msgId,flag);
                     break;
                }
        }
    }
};

var selToID=null;
var identifier=null;
var loginInfo=null;
var curPlayAudio = null;

var ionicScrollDelegate;//ionic滚动时要用
var scope,http;
var subType = webim.C2C_MSG_SUB_TYPE.COMMON;
var selType = webim.SESSION_TYPE.C2C;

app.controller('inquiryCon', ['$scope','$location','$http','$sce','$filter','$ionicScrollDelegate',function($scope, $location, $http,$sce,$filter,$ionicScrollDelegate) {
	ionicScrollDelegate=$ionicScrollDelegate;
	selToID=$location.search().selToID;
	identifier=$location.search().identifier;
	scope=$scope;
	http=$http;
	$scope.sce = $sce.trustAsResourceUrl;
	
	//延迟加载信息
	setTimeout(function(){
		
		openAlertMsgLoad("加载中");
		//获取医生信息
		$http.post(basePath + 'im/queryDoctorById.action',{doctorId:$location.search().selToID},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.doctor=response.data.data;
					}
		});
		//获取最后一次订单信息
		$http.post(basePath + 'appDoctor/queryLastOrde.action',{doctorId:$location.search().selToID,patientId:$location.search().identifier},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.lastOrder=response.data.data;
						if(isEmpty($scope.lastOrder.mainOrderNo)&&$scope.lastOrder.serverId==4&&isEmpty($scope.lastOrder.symptomId)){
							//openAlert("您还未填写症状描述，请先去填写",function(){
								window.location.href = "../hospital/case_history.html?orderNo="+$scope.lastOrder.orderNo;
							//});
						}
						//如果有过期时间 建立倒计时
						if($scope.lastOrder.validityTime){
							$scope.second=$scope.lastOrder.validityTime-new Date().getTime()/1000;
							if($scope.second>0){
								time(new Date().getTime()/1000,$scope.lastOrder.validityTime,scope);
							}else{//如果已经过期 制成已完成
								if($scope.lastOrder.paymentStatus==2&&($scope.lastOrder.serverId==4||$scope.lastOrder.serverId==7||$scope.lastOrder.serverId==8||$scope.lastOrder.serverId==22)){
									$http.post(basePath + 'appDoctor/updateOrderByEnd.action',{doctorId:$location.search().selToID,patientId:$location.search().identifier},postCfg).then(
											function(response) {
												if(response.data&&response.data.respCode==1001){
												}
									});
									$scope.lastOrder.paymentStatus=5;
								}
							}
						}
					}
		});
		$scope.picNum=0;
		$http.post(basePath + 'im/queryScan.action',{url:$location.absUrl()},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						wx.config({
							debug : false, // 开启调试模式,调用的所有api的返回值会在客户端openAlert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
							appId : response.data.data.appId, // 必填，公众号的唯一标识
							timestamp : response.data.data.timestamp, // 必填，生成签名的时间戳
							nonceStr : response.data.data.nonceStr, // 必填，生成签名的随机串
							signature : response.data.data.signature,// 必填，签名，见附录1
							jsApiList : [ 'startRecord','stopRecord','onVoiceRecordEnd','uploadVoice']
						// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
						});
					}
		});
		//获取患者信息并登陆
		$http.post(basePath + 'im/queryPatientById.action',{patientId:$location.search().identifier},postCfg).then(
				function(response) {
					if(response.data&&response.data.respCode==1001){
						$scope.patient=response.data.data;
						$http.post(basePath + 'im/querySig.action',{identifier:$location.search().identifier},postCfg).then(
								function(response) {
									if(response.data&&response.data.respCode==1001){
										 //当前用户身份
								        loginInfo = {
								            'sdkAppID': response.data.data.sdkAppid, //用户所属应用id,必填
								            'identifier': $location.search().identifier, //当前用户ID,必须是否字符串类型，必填
								            'accountType': response.data.data.accountType, //用户所属应用帐号类型，必填
								            'userSig': response.data.data.sig, //当前用户身份凭证，必须是字符串类型，必填
								            'identifierNick':$scope.patient.name, //当前用户昵称，不用填写，登录接口会返回用户的昵称，如果没有设置，则返回用户的id
								            'headurl':$scope.patient.photo//当前用户默认头像，选填，如果设置过头像，则可以通过拉取个人资料接口来得到头像信息
								        };
										webim.login(loginInfo,listeners,options,function(){
											$scope.doRefresh();
										});
									}
						});
					}
		});
		
		var page=1;
		$scope.doRefresh=function(){
			getLastC2CHistoryMsgs(function(msg){
				if(null==msg||msg.length==0){
					$scope.stop=true;
					if(!$scope.msgs){
						$scope.msgs=[];
					}
					return;
				}
				if(Array.isArray(msg)){
					msg.sort(compare('time'));
				}else{
					var array=[];
					array.push(msg);
					msg=array;
				}
				
				if(!$scope.msgs){
					$scope.msgs=[];
					onMsgNotifyHis(msg,false);
				}else{
					showMsg(msg,true);
				}
				console.log(msg);
			},function(err){
				console.log(err);
			});
			closeAlertMsgLoad();
			$scope.$broadcast('scroll.refreshComplete'); 
			/*$http.post(basePath + 'im/queryIms.action's,{doctorId:$location.search().selToID,patientId:$location.search().identifier,page:page++,row:10},postCfg).then(
					function(response) {
						if(response.data&&response.data.respCode==1001){
							if(null==response.data.data||response.data.data.length==0){
								$scope.stop=true;
								if(page==2){
									$scope.msgs=[];
								}
							}else{
								
								if($scope.msgs){
									for(var i=response.data.data.length-1;i>0;i--){
										$scope.msgs.unshift(response.data.data[i]);
										if(response.data.data[i].msgType==2){
											setImage(response.data.data[i].id,response.data.data[i].bigPictureUrl,true);
										}
									}
								}else{
									$scope.msgs=response.data.data;
									for(var i=0;i<response.data.data.length;i++){
										if(response.data.data[i].msgType==2){
											setImage(response.data.data[i].id,response.data.data[i].bigPictureUrl,true);
										}
									}
									scrollBottom();
								}
							}
							
							$scope.$broadcast('scroll.refreshComplete'); 
							
							$scope.$on('ngRepeatFinished', function() {
								$(".inquiry_main .inquiry_img").touchTouch();
							});
					}
			});*/
		};
	},500);
	
	$scope.showSend=function(event,val){
		event.stopPropagation();
		$scope.isSendShow=val;
		if(val==1){
			setTimeout(function(){
				$("#inputFocus").focus();
			},200);
		}
	};
	
	$scope.showTextSend=function(event,val){
		event.stopPropagation();
		setTimeout(function(){
			$scope.isSendTexShow=val;
			$scope.$apply();
		},10);
		if(val==1){
			setTimeout(function(){
				$("#textFocus").focus();
			},200);
		}
	};
	
	$scope.equals=function(obj,obj1){
		//var minutes=(new Date(obj*1000)-new Date(obj1*1000))/1000/60;
		//return !angular.equals($filter('date')(obj*1000, 'yyyy-MM-dd HH:mm'),$filter('date')(obj1*1000,'yyyy-MM-dd HH:mm'));
		return obj-obj1>600;
	};
	
	/**
	 * 发送文字
	 */
	$scope.sendMsg=function(type){
		if(isEmpty($scope.content)){
			return false;
		}
		selToID=$location.search().selToID;
		var selSess = new webim.Session(selType, selToID, selToID,"", Math.round(new Date().getTime() / 1000));
		var isSend = true; //是否为自己发送
	    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
		var msg = new webim.Msg(selSess,isSend,seq, -1, -1, loginInfo.identifier, subType, loginInfo.identifierNick);
		msg.addText(new webim.Msg.Elem.Text($scope.content));
		
		var Ext=JSON.stringify({type:0,patientId:loginInfo.identifier});
		
		msg.PushInfo = {
		        "PushFlag": 0,
		        "Desc": $scope.content, //离线推送内容
		        "Ext": Ext, //离线推送透传内容
		        "AndroidInfo": {
		            "Sound": "android.mp3" //离线推送声音文件路径。
		        },
		        "ApnsInfo": {
		            "Sound": "apns.mp3", //离线推送声音文件路径。
		            "BadgeMode": 1
		        }
		};
		var data={"msgId":uuid(32),"serverOrderNo":$scope.lastOrder.orderNo};
		var desc="";
		var ext="";
		var custom_obj = new webim.Msg.Elem.Custom(JSON.stringify(data), desc, ext);
		msg.addCustom(custom_obj);
		msg.PushInfoBoolean = true; //是否开启离线推送push同步
		msg.sending = 1;
		msg.originContent =$scope.content;
		//addMsg(msg);
		$scope.content='';
		webim.sendMsg(msg, function(resp) {
			console.log(resp);
	        //发送成功，把sending清理
	        //$("#id_" + msg.random).find(".spinner").remove();
			$scope.msgs.push({msgType:1,from_account:$scope.patient.id,content:msg.originContent,time:getTime()});
			if(type){
				$scope.isSendShow=2;
			}
			$scope.$apply();
			scrollBottom();
			//successEvent();
	        webim.Tool.setCookie("tmpmsg_" + selToID, '', 0);
	    }, function(err) {
	    	console.log(err);
	        //提示重发
	        //showReSend(msg);
	    });
	};

	
    var posStart = 0;//初始化起点坐标  
    var posEnd = 0;//初始化终点坐标  
    var posMove = 0;//初始化滑动坐标  
    var timeOutEvent,timeOutEvent1;
    var startTime=new Date().getTime();
    var btnElem=$(".voideClick")[0];//获取ID  
    if(btnElem){
    	btnElem.addEventListener("touchstart", function(event) {  
            event.preventDefault();//阻止浏览器默认行为 
            startTime=new Date().getTime();
            timeOutEvent=setTimeout(function(){
            	posStart = 0;  
                posStart = event.touches[0].pageY;//获取起点坐标  
                $(".inquiry_record1").show();
        	    wx.startRecord({
        			success: function(){
        				if(!localStorage.rainAllowRecord || localStorage.rainAllowRecord !== 'true'){
    						localStorage.rainAllowRecord = 'true';
    						wx.stopRecord(); 
    						$(".inquiry_record1").hide();
    					}else{
    						timeOutEvent1=setTimeout(function(){
    							$(".inquiry_record1").hide();
    							wx.stopRecord({
    			    	    		success: function (res) {
    			    	    			var localId = res.localId;
    			    	    			uploadVoice(localId);
    			    	    			$(".inquiry_record1").hide();
    			    	    		}
    			    	    	});
    						},59000);//59秒后自动停止上传
    					}
        			},
        			cancel: function () {
        				$(".inquiry_record1").hide();
        			}
        		});
            },300);
        });  
        btnElem.addEventListener("touchmove", function(event) {  
            event.preventDefault();//阻止浏览器默认行为  
            posMove = 0;  
            posMove = event.targetTouches[0].pageY;//获取滑动实时坐标  
            if(posStart - posMove > 100){  
            	clearTimeout(timeOutEvent);
            	clearTimeout(timeOutEvent1);
            	timeOutEvent = 0;
            	$(".inquiry_record1").hide();
            	$(".inquiry_record2").show();
    	    	//wx.stopRecord();  
            }else{
            	$(".inquiry_record1").show();
            	$(".inquiry_record2").hide();
            }
        });  
        btnElem.addEventListener("touchend", function(event) {  
            event.preventDefault(); 
            posEnd = 0;  
            posEnd = event.changedTouches[0].pageY;//获取终点坐标 
            clearTimeout(timeOutEvent);
        	clearTimeout(timeOutEvent1);
            var intervalTime=new Date().getTime()-startTime;
            if(posStart - posEnd < 100){  
            	if(intervalTime>1300){
            		$(".inquiry_record1").hide();
        	    	wx.stopRecord({
        	    		success: function (res) {
        	    			var localId = res.localId;
        	    			uploadVoice(localId);
        	    		}
        	    	});
            	}else{
            		$(".inquiry_record1").hide();
            		$(".inquiry_record3").show();
            		wx.stopRecord();  
                	setTimeout(function(){
                		$(".inquiry_record3").hide();
                	},500);
            	}
            }else{
            	$(".inquiry_record1").hide();
            	$(".inquiry_record2").hide();
    	    	wx.stopRecord();  
            };  
        });  
    }
    
	wx.onVoiceRecordEnd({
		// 录音时间超过一分钟没有停止的时候会执行 complete 回调
		complete: function (res) {
			$(".inquiry_record1").hide();
			var localId = res.localId;
			uploadVoice(localId);
		}
	});
	//上传语音
	function uploadVoice(localId){
		wx.uploadVoice({
			localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
			isShowProgressTips:0, // 默认为1，显示进度提示
			success: function (res) {
				openAlertMsgLoad("发送中");
				var serverId = res.serverId; // 返回音频的服务器端ID
				$http.post(basePath + 'im/queryMp3.action',{mediaId:serverId},postCfg).then(
						function(response) {
							if(response.data&&response.data.respCode==1001){
								$scope.msgs.push({msgType:3,from_account:$scope.patient.id,content:response.data.data.fwPath,second:response.data.data.second,time:getTime()});
								scrollBottom();
								sendCustom({content:response.data.data.fwPath,second:response.data.data.second,msgType:3});
								closeAlertMsgLoad();
							}
				});
			}
		});
	}
	//发送自定义消息
	function sendCustom(data,type){
		var selSess = new webim.Session(selType, selToID, selToID,"", Math.round(new Date().getTime() / 1000));
		var isSend = true; //是否为自己发送
	    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
		var msg = new webim.Msg(selSess,isSend,seq, -1, -1, loginInfo.identifier, subType, loginInfo.identifierNick);
		
		var Ext=JSON.stringify({type:0,patientId:loginInfo.identifier});
		
		msg.PushInfo = {
		        "PushFlag": 0,
		        "Desc": '语音', //离线推送内容
		        "Ext": Ext, //离线推送透传内容
		        "AndroidInfo": {
		            "Sound": "android.mp3" //离线推送声音文件路径。
		        },
		        "ApnsInfo": {
		            "Sound": "apns.mp3", //离线推送声音文件路径。
		            "BadgeMode": 1
		        }
		};
		var desc="您有一条新消息";
		data.msgId=uuid(32);
		data.serverOrderNo=$scope.lastOrder.orderNo;
		var custom_obj = new webim.Msg.Elem.Custom(JSON.stringify(data), desc,null);
		msg.addCustom(custom_obj);
		msg.PushInfoBoolean = true; //是否开启离线推送push同步
		msg.sending = 1;
		webim.sendMsg(msg, function(resp) {
			scrollBottom();
	        webim.Tool.setCookie("tmpmsg_" + selToID, '', 0);
	        //successEvent();
	    }, function(err) {
	    	console.log(err);
	    });
	}
	
	
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
		$.each($("input[name='tongueUrl']"),function(index,item){
			if($(item).val()){
				$scope.uploadNum++;
				$scope.$apply();
				return false;
			}
		});
		$.each($("input[name='surfaceUrl']"),function(index,item){
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
			openAlert("请填写完整详细问诊");
			return false;
		}else if(index==2&&$scope.uploadNum!=($scope.specialTest.isSurface==1&&$scope.specialTest.isTongue==1?2:1)){
			openAlert("请上传舌照/面照");
			return false;
		}
		$(event.target).parent().parent().next().addClass("symptom_describe");
	};
	
	$scope.prev=function(event){
		$(event.target).parent().parent().removeClass("symptom_describe");
	};
	
	$scope.toGoods=function(id){
		window.location.href = "../goods/commodity_details.html?goodsId="+id+"&doctorId="+$scope.doctor.doctorId+"&patientId="+$scope.patient.id;
	};
	
	$scope.toKnowledge=function(id){
		window.location.href = "../knowledge/article_detail.html?circle="+id;
	};
	
	$scope.toBelow=function(id){
		window.location.href = "../hospital/line_below.html?id="+id;
	};
	
	$scope.toPayment =function(orderNo,content){
		if(content!='0'){
			window.location.href = "../pay/pay.html?orderNo="+orderNo;
		}
	};
	
	$scope.toDoctor =function(){
		window.location.href = "../hospital/doctor_detail.html?id="+$scope.doctor.doctorId;
	};
	
	$scope.be_careful=function(){
		window.location.href = "be_careful.html";
	}
	//查看症状描述
	$scope.searchSymptom=function(orderNo){
		if(orderNo){
			window.location.href = "specialTest.html?orderNo="+orderNo+"&doctorId="+$scope.doctor.doctorId+"&title=症状描述&patientId="+$scope.patient.id;
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
	//查看问诊单/复诊单
	$scope.searchSpecialTest=function(orderNo,id,title){
		
		window.location.href = "specialTest.html?orderNo="+orderNo+"&id="+id+"&doctorId="+$scope.doctor.doctorId+"&title="+title+"&patientId="+$scope.patient.id;
		
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
	
	$scope.tl=function(){
		if($scope.lastOrder.isAccpetAsk!=1){
			openAlert("医生暂不接受问诊！");
			return false;
		}
		if($scope.lastOrder.isOnlineTwGh!=1){
			openAlert("医生暂不接受图文调理！");
			return false;
		}
		window.location.href = "../hospital/affirm_serveorder.html?id="+$scope.doctor.doctorId+"&orderType=4&patientId="+$scope.patient.id;
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
		window.location.href = "../hospital/affirm_serveorder.html?id="+$scope.doctor.doctorId+"&orderType=6&patientId="+$scope.patient.id;
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
			  			var obj=$(".symptom_describe");
			  			obj.removeClass("symptom_describe");
			  			setTimeout(function(){
			  				$(".symptom").hide();
			  			},200);
			  		});
			  		if($scope.specialTest.type==1){
			  			scope.msgs.push({msgType:9,from_account:scope.patient.id,dataId:id,orderNo:data.orderNo,time:getTime()});
			  		}else{
			  			scope.msgs.push({msgType:11,from_account:scope.patient.id,dataId:id,orderNo:data.orderNo,time:getTime()});
			  		}
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
							scope.$apply();
						}
			});
	}
	
	$scope.sendZx=function(){
		var fileLen=0;
		for(var i=0;i<3;i++){
			var uploadFile=$("input[name='zx']")[i];
			var len=$(uploadFile)[0].files.length;
			if(len>0){
				fileLen++;
			}
		}
		if(fileLen==0&&isEmpty($scope.content)){
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
						 	   openAlertMsgLoad("发送中");
						 	    uploadPic(file,$scope.lastOrder.syZxCount);
						 	}
						}
						$("input[name='zx']").prev().attr("src","../../img/indetail_add.png");
						$("input[name='zx']").val('');
						$(".textCount").text(0); 
						$scope.isShowzx=true
					}else{
						openAlert(response.data.respMsg);
					}
		});
	};
	$scope.contentLength=0;
	$scope.contentChange=function(){
		$scope.contentLength=$scope.content.length;
	};
	
	var lastMsgTime = 0;//第一次拉取好友历史消息时，必须传 0
	var msgKey = '';
	var getLastC2CHistoryMsgs = function (cbOk, cbError) {
	    if (selType == webim.SESSION_TYPE.GROUP) {
	       // alert('当前的聊天类型为群聊天，不能进行拉取好友历史消息操作');
	        return;
	    }
	    var options = {
	        'Peer_Account': selToID, //好友帐号
	        'MaxCnt': 15, //拉取消息条数
	        'LastMsgTime': lastMsgTime, //最近的消息时间，即从这个时间点向前拉取历史消息
	        'MsgKey': msgKey
	    };
	    webim.getC2CHistoryMsgs(
	            options,
	            function (resp) {
	                var complete = resp.Complete;//是否还有历史消息可以拉取，1-表示没有，0-表示有
	                var retMsgCount = resp.MsgCount;//返回的消息条数，小于或等于请求的消息条数，小于的时候，说明没有历史消息可拉取了
	                if (resp.MsgList.length == 0) {
	                    //webim.Log.error("没有历史消息了:data=" + JSON.stringify(options));
	                    //return complete;
	                }
	                lastMsgTime=resp.LastMsgTime;
	                msgKey=resp.MsgKey;
	                if (cbOk)
	                    cbOk(resp.MsgList);
	            },
	            cbError
	     );
	};
	
	
}]);
//接受文字消息
function convertTextMsgToHtml(time,from_account,content,msgId,flag){
	var text=content.getText();
	if(flag){
		scope.msgs.unshift({id:msgId,msgType:1,from_account:from_account,content:text,msgTime:time,time:getTime(time)});
	}else{
		scope.msgs.push({id:msgId,msgType:1,from_account:from_account,content:text,msgTime:time,time:getTime(time)});
		scrollBottom();
	}
	scope.$apply();
}
//接受图片消息
function convertImageMsgToHtml(time,from_account,content,msgId,flag){
	 var smallImage = content.getImage(webim.IMAGE_TYPE.SMALL);//小图
	 var bigImage = content.getImage(webim.IMAGE_TYPE.LARGE);//大图
	 var oriImage = content.getImage(webim.IMAGE_TYPE.ORIGIN);//原图
	 if (!bigImage) {
	      bigImage = smallImage;
	 }
	 if (!oriImage) {
	      oriImage = smallImage;
	 }
	 if(flag){
		 scope.msgs.unshift({id:msgId,bigPictureUrl:bigImage.url,originalUrl:oriImage.url,shrinkingMapUrl:smallImage.url,from_account:from_account,height:smallImage.height,time:getTime(time),width:smallImage.width,msgType:2,msgTime:time});
		 scope.$apply();
	 }else{
		 scope.msgs.push({id:msgId,bigPictureUrl:bigImage.url,originalUrl:oriImage.url,shrinkingMapUrl:smallImage.url,from_account:from_account,height:smallImage.height,time:getTime(time),width:smallImage.width,msgType:2,msgTime:time});
		 scope.$apply();
		 setTimeout(function(){
			 $("#"+msgId).find(".chat_photo")[0].onload = function () {
				 scrollBottom();
			 };
		 },100);
	 }
	setImage(msgId,bigImage.url,true);
 	/* if (i <= count - 2) {
           var customMsgElem = elems[i + 1];//获取保存图片名称的自定义消息elem
           var imgName = customMsgElem.getContent().getData();//业务可以自定义保存字段，demo中采用data字段保存图片文件名
           html += convertImageMsgToHtml(content, imgName);
             i++;//下标向后移一位
     }else{
           html += convertImageMsgToHtml(content);
     }*/
}
//接受语音消息
function convertSoundMsgToHtml(time,from_account,content,msgId,flag){
	 var second = content.getSecond();//获取语音时长
	 var downUrl = content.getDownUrl();
	 if (webim.BROWSER_INFO.type == 'ie' && parseInt(webim.BROWSER_INFO.ver) <= 8) {
		 return '[这是一条语音消息]demo暂不支持ie8(含)以下浏览器播放语音,语音URL:' + downUrl;
	 }else{
		// scope.msgs.push({id:msgId,content:downUrl,second:second,time:getTime(),msgType:3});
		// scope.$apply();
		// scrollBottom();
		 //$("body").append('<audio id="uuid_'+content.getUUID()+'" src="' + downUrl + '" controls="controls" onplay="onChangePlayAudio(this)" preload="none"></audio>');
		/*setTimeout(function(){
			 getMp3(msgId,second);
		},2000);*/
		 if(flag){
			 scope.msgs.unshift({id:msgId,from_account:from_account,content:downUrl,second:second,time:getTime(time),msgType:3,msgTime:time});
			 scope.$apply();
		 }else{
			 scope.msgs.push({id:msgId,from_account:from_account,content:downUrl,second:second,time:getTime(time),msgType:3,msgTime:time});
			 scope.$apply();
			 scrollBottom();
		 }
	 }
}

function getMp3(time,msgId,second){
	 http.post(basePath + 'im/queryMp3BySqe.action',{id:msgId},postCfg).then(
				function(response) {
					console.log(second);
					if(response.data&&response.data.respCode==1001){
						 scope.msgs.push({id:msgId,content:response.data.data.content,second:second,time:getTime(time),msgType:3,msgTime:time});
						 scrollBottom();
					}else{
						setTimeout(function(){
							getMp3(msgId,second);
						},1000);
					}
	 });
}
//接受自定义消息
function convertCustomMsgToHtml(time,from_account,content,flag){
	var data=eval('(' +content.getData()+ ')');
    var msgType=data.msgType;
    var json;
    if(msgType==3){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,second:data.second,time:getTime(time),msgTime:time};
    }
    if(msgType==7){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==8){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,dataId:data.dataId,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==9){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,dataId:data.dataId,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==10){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,dataId:data.dataId,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==11){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,dataId:data.dataId,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==12){
    	if(data.content&&data.content!=0){
    		json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    	}
    }
    if(msgType==13){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==14){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    	if(isEmpty(flag)){
    		scope.lastOrder.syZxCount=parseInt(scope.lastOrder.syZxCount)+parseInt(data.content);
        	scope.lastOrder.zxCount=parseInt(scope.lastOrder.zxCount)+parseInt(data.content);
    	}
    }
    if(msgType==15){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    }
    if(msgType==16){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,orderNo:data.orderNo,time:getTime(time),msgTime:time};
    	if(isEmpty(flag)){
    		http.post(basePath + 'appDoctor/queryLastOrde.action',{doctorId:selToID,patientId:identifier},postCfg).then(
    				function(response) {
    					if(response.data&&response.data.respCode==1001){
    						scope.lastOrder=response.data.data;
    						/*if(isEmpty($scope.lastOrder.mainOrderNo)&&$scope.lastOrder.serverId==4&&isEmpty($scope.lastOrder.symptomId)){
    							//openAlert("您还未填写症状描述，请先去填写",function(){
    								window.location.href = "../hospital/case_history.html?orderNo="+$scope.lastOrder.orderNo;
    							//});
    						}*/
    						
    						if(scope.lastOrder.validityTime){
    							scope.second=scope.lastOrder.validityTime-new Date().getTime()/1000;
    							if(scope.second>0){
    								time(new Date().getTime()/1000,scope.lastOrder.validityTime,scope);
    							}
    						}
    					}
    		});
    	}
    }
    if(msgType==17){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    	if(isEmpty(flag)){
    		scope.lastOrder.paymentStatus=5;
    	}
    }
    if(msgType==18){
    	$.each(scope.msgs,function(index,item){
    		if(item.id==data.msgId){
    			item.msgType=18;
    			return false;
    		}
    	});
    }
    if(msgType==19){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,bigPictureUrl:data.bigPictureUrl,height:data.height,originalUrl:data.originalUrl,dataId:data.dataId,shrinkingMapUrl:data.shrinkingMapUrl,width:data.width,time:getTime(time),msgTime:time};
    }
    if(msgType==20){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,bigPictureUrl:data.bigPictureUrl,dataId:data.dataId,shrinkingMapUrl:data.shrinkingMapUrl,time:getTime(time),msgTime:time};
    }
    if(msgType==21){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,dataId:data.dataId,time:getTime(time),msgTime:time};
    }
    if(msgType==23){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,type:data.type,time:getTime(time),msgTime:time};
    }
    if(msgType==24){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    }
    if(msgType==25){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    }
    if(msgType==26){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    }
    if(msgType==27){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,time:getTime(time),msgTime:time};
    }
    if(msgType==28){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    }
    if(msgType==29){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    }
    if(msgType==30){
    	json={id:data.msgId,msgType:msgType,from_account:from_account,content:data.content,time:getTime(time),msgTime:time};
    }
    if(msgType==31){
    	$.each(scope.msgs,function(index,item){
    		if(item.id==data.dataId){
    			item.msgType=18;
    		}
    	});
    	scope.$apply();
    	return;
    }
    if(flag){
		 scope.msgs.unshift(json);
		 scope.$apply();
	 }else{
		 scope.msgs.push(json);
		 scope.$apply();
		 scrollBottom();
	 }
}

function uploadImages(obj){
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
	$(obj).prev()[0].src = src;
	scope.picNum+=1;
	scope.$apply();
}

function deletePhoto(obj){
	$(obj).next().attr("src","../../img/indetail_add.png");
	$(obj).next().next().val("");
	scope.picNum-=1;
	scope.$apply();
}


function words_deal(){ 
	var curLength=$(".TextArea1").val().length; 
	if(curLength>200) { 
			var num=$(".TextArea1").val().substr(0,200); 
			$(".TextArea1").val(num); 
		} 
	else{ 
			$(".textCount").text(0+$(".TextArea1").val().length); 
		} 
} 

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
		$(obj).parents("ul").append('<li><div class="photo_photo"><img src="../../img/indetail_add.png"><input type="file" name="'+name+'" onchange="setImagePreview(this,'+len+','+delLen+')"><input type="tetx" name="'+name+'Url"></div><p>点击添加</p></li>');
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

$("#specialTest").on("click",".indetail_delete",function(event){
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
			$(_this).parents("ul").append('<li><div class="photo_photo"><img src="../../img/indetail_add.png"><input type="file" name="'+inputName+'" onchange="setImagePreview(this,'+len1+','+delLen+')"><input type="tetx" name="'+inputName+'Url"></div><p>点击添加</p></li>');
		}
		$(_this).parent().remove();
		scope.getUploadNum();
	});
});


function compare(property){
    return function(a,b){
        var value1 = a[property];
        var value2 = b[property];
        return value1 - value2;
    }
}

function scrollBottom(){//滚动到底部
	setTimeout(function(){
		ionicScrollDelegate._instances[0].scrollBottom();
	},100);
}

function getTime(time){
	var date=new Date();
	var oldDate=new Date(time*1000);
	if(date.getFullYear()==oldDate.getFullYear()&&date.getMonth()==oldDate.getMonth()&&date.getDate()==oldDate.getDate()){
		return (oldDate.getHours()>=10?oldDate.getHours():"0"+oldDate.getHours())+":"+(oldDate.getMinutes()>=10?oldDate.getMinutes():"0"+oldDate.getMinutes());
	}else if(date.getFullYear()==oldDate.getFullYear()&&date.getMonth()==oldDate.getMonth()&&date.getDate()==(oldDate.getDate()-1)){
		return "昨天 "+(oldDate.getHours()>=10?oldDate.getHours():"0"+oldDate.getHours())+":"+(oldDate.getMinutes()>=10?oldDate.getMinutes():"0"+oldDate.getMinutes());
	}else if(date.getFullYear()==oldDate.getFullYear()){
		return formatDateSimple(time);
	}else{
		return formatDateYYYYMMDDHM(time);
	}
}

/**
 * 图片
 * @param uploadFile
 */
function fileOnChange(uploadFile){
	 if (!window.File || !window.FileList || !window.FileReader) {
	        openAlert("您的浏览器不支持File Api");
	        return;
	    }
	 	var len=uploadFile.files.length;
	 	if(len>0){
	 		openAlertMsgLoad("发送中");
	 		 
		 	for(var i=0;i<len;i++){
		 		var file = uploadFile.files[i];
		 	    var fileSize = file.size;
		 	    //先检查图片类型和大小
		 	    if (!checkPic(uploadFile, fileSize)) {
		 	        return;
		 	    }
		 	    uploadPic(file);
		 	}
	 	}
}

//检查文件类型和大小
function checkPic(obj, fileSize) {
    var picExts = 'jpg|jpeg|png|bmp|gif|webp';
    var photoExt = obj.value.substr(obj.value.lastIndexOf(".") + 1).toLowerCase();//获得文件后缀名
    var pos = picExts.indexOf(photoExt);
    if (pos < 0) {
        openAlert("您选中的文件不是图片，请重新选择");
        return false;
    }
    fileSize = Math.round(fileSize / 1024 * 100) / 100; //单位为KB
    if (fileSize > 30 * 1024) {
        openAlert("您选择的图片大小超过限制(最大为30M)，请重新选择");
        return false;
    }
    return true;
}

function uploadPic(file,syZxCount){
    var businessType;//业务类型，1-发群图片，2-向好友发图片
    if (selType == webim.SESSION_TYPE.C2C) {//向好友发图片
        businessType = webim.UPLOAD_PIC_BUSSINESS_TYPE.C2C_MSG;
    }
    /*var fileType = file.type
    var image = new Image(), canvas = document.createElement("canvas"),ctx = canvas.getContext('2d');
    var reader = new FileReader();
    reader.onload = function() {
    	 image.src =reader.result;
    };
    
    var orient;
  	 
    EXIF.getData(file, function () {
         orient = EXIF.getTag(this, 'Orientation');
    });
    
    image.onload = function() {
//        var w =600,
//            h =600;
        var scale = 1;
        var fileKb =  parseInt(file.size/1024);
        if(fileKb>=200&&fileKb<1000){
        	if(this.width > this.height){    
             	scale = this.height / this.width;
             }else{    
             	scale = this.width / this.height;
             }    
        }else if(fileKb>=1000){
        	if(this.width > this.height){    
             	scale =1-(this.height / this.width);
             }else{    
             	scale =1-(this.width / this.height);
             } 
        }
        canvas.width = this.width;    
        canvas.height = this.height;     // 计算等比缩小后图片宽高
        if (orient == 6) {
        	canvas.width = this.height;
        	canvas.height = this.width;
            ctx.rotate(90*Math.PI/180);
            ctx.drawImage(this, 0, -this.height);
        } else {
        	ctx.drawImage(this,0,0,this.width,this.height,0,0,canvas.width, canvas.height);
        }
        //ctx.drawImage(this, 0, 0,cvs.width, cvs.height);
        var newImageData = canvas.toDataURL("image/jpeg",scale);
        var _sendData = newImageData.replace("data:"+fileType+";base64,",'');
        var data=_sendData;
        data = window.atob(data);
        var ia = new Uint8Array(data.length);
        for (var i = 0; i < data.length; i++) {
            ia[i] = data.charCodeAt(i);
        };
        var blob = new Blob([ia]);*/
        var opt = {
                'file': file, //图片对象
                //'onProgressCallBack': onProgressCallBack, //上传图片进度条回调函数
                //'abortButton': document.getElementById('upd_abort'), //停止上传图片按钮
                'To_Account': selToID, //接收者
                'businessType': businessType//业务类型
            };
            //上传图片
            webim.uploadPic(opt,
                function (resp) {
                    //上传成功发送图片
                    sendPic(resp,file.name,syZxCount);
                    //$('#upload_pic_dialog').modal('hide');
                    closeAlertMsgLoad();
                },
                function (err) {
                    console.log(err.ErrorInfo);
                    closeAlertMsgLoad();
                }
        );
    //};
    //reader.readAsDataURL(file);
}

function sendPic(images,imgName,syZxCount) {
    var selSess = new webim.Session(selType, selToID, selToID,loginInfo.headurl, Math.round(new Date().getTime() / 1000));
    var msg = new webim.Msg(selSess, true, -1, -1, -1, loginInfo.identifier, 0, loginInfo.identifierNick);
    var images_obj = new webim.Msg.Elem.Images(images.File_UUID);
    for (var i in images.URL_INFO) {
        var img = images.URL_INFO[i];
        var newImg;
        var type;
        switch (img.PIC_TYPE) {
            case 1://原图
                type = 1;//原图
                break;
            case 2://小图（缩略图）
                type = 3;//小图
                break;
            case 4://大图
                type = 2;//大图
                break;
        }
        newImg = new webim.Msg.Elem.Images.Image(type, img.PIC_Size, img.PIC_Width, img.PIC_Height, img.DownUrl);
        images_obj.addImage(newImg);
    }
    msg.addImage(images_obj);
    var data={msgId:uuid(32),syZxCount:syZxCount,serverOrderNo:scope.lastOrder.orderNo};//通过自定义消息中的data字段保存图片名称
    var custom_obj = new webim.Msg.Elem.Custom(JSON.stringify(data), '', '');
    msg.addCustom(custom_obj);
    
    var Ext=JSON.stringify({type:0,patientId:loginInfo.identifier});
	
    msg.PushInfo = {
	        "PushFlag": 0,
	        "Desc": '图片', //离线推送内容
	        "Ext": Ext, //离线推送透传内容
	        "AndroidInfo": {
	            "Sound": "android.mp3" //离线推送声音文件路径。
	        },
	        "ApnsInfo": {
	            "Sound": "apns.mp3", //离线推送声音文件路径。
	            "BadgeMode": 1
	        }
	};
    //调用发送图片消息接口
    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) {//私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            //addMsg(msg);
        	var smallImage = msg.getElems()[0].getContent().getImage(webim.IMAGE_TYPE.SMALL);//小图
            var bigImage = msg.getElems()[0].getContent().getImage(webim.IMAGE_TYPE.LARGE);//大图
            var oriImage = msg.getElems()[0].getContent().getImage(webim.IMAGE_TYPE.ORIGIN);//原图
            if (!bigImage) {
                bigImage = smallImage;
            }
            if (!oriImage) {
                oriImage = smallImage;
            }
            var id=uuid(32);
        	scope.msgs.push({id:id,bigPictureUrl:bigImage.url,originalUrl:oriImage.url,shrinkingMapUrl:smallImage.url,from_account:scope.patient.id,height:smallImage.height,time:getTime(),width:smallImage.width,msgType:2});
        	scope.$apply();
        	$("#"+id).find(".chat_photo")[0].onload = function () {
        		scrollBottom();
            }
        	setImage(id,bigImage.url,true);
        	//successEvent();
        }
    }, function (err) {
        console.log(err.ErrorInfo);
    });
}


//*图片end*//
function payAudio(obj){
	var playAudio=$(obj).find("audio")[0];
	var audioSrc = playAudio.src;
	if(audioSrc.indexOf("https") < 0 && audioSrc.indexOf("uploadFiles") < 0) {
	payAudioChild(obj);
	    var fileName = playAudio.getAttribute("cid");
        http.post(basePath + 'im/changeToMp3.action',{url:audioSrc, fileName: fileName},postCfg).then(
            function(response) {
                if(response.data&&response.data.respCode==1001){
                    playAudio.setAttribute("src", response.data.data.result);
                    payAudioChild(obj);
                }
            }
        );
    } else {
        payAudioChild(obj);
    }
}

function payAudioChild(obj) {
var playAudio=$(obj).find("audio")[0];
    var playAudioParent=$(obj).find("a");
    $(".inquiry_playerleft").removeClass("inquiry_playerleft");
    if (curPlayAudio) {
        if (curPlayAudio != playAudio) {
            curPlayAudio.pause();
            curPlayAudio = playAudio;
curPlayAudio.load();
            curPlayAudio.currentTime = 0;
            curPlayAudio.play();
            playAudioParent.addClass("inquiry_playerleft");
            curPlayAudio.onended = function() {
                playAudioParent.removeClass("inquiry_playerleft");
            };
        }else{
            if(curPlayAudio.paused){
            curPlayAudio.pause();
            curPlayAudio = playAudio;
curPlayAudio.load();
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

/*end*/



function onChangePlayAudio(playAudio) {
    if (curPlayAudio) {
        if (curPlayAudio != playAudio) {
            curPlayAudio.currentTime = 0;
            curPlayAudio.pause();
            curPlayAudio = playAudio;
        }
    } else {
        curPlayAudio = playAudio;
    }
}



function setImage(id,url,flag){//有新图片时 增加 查看大图时使用
	if(isEmpty(url)){
		return false;
	}
	setTimeout(function(){
		$(".inquiry_main .inquiry_img").touchTouch();
	},1000);
}

$(".inquiry_chat").on("click",".chat_photo",function(){//点击查看大图
	var _this=$(this);
	var checkId=_this.attr("msgId");
	$("."+checkId).trigger("click");
});
//倒计时
function time(startTime,endTime,obj){
	 var subTime=endTime-startTime;
	 if(subTime>0){
		 var interval;
		 var minutes=parseInt(subTime/3600);
		 var seconds=parseInt(subTime%3600);
		 seconds=parseInt(seconds/60);
	     obj.dayNum= isDouble(minutes)+'小时'+isDouble(seconds)+"分";
	     interval = setInterval(function() {
	    	 minutes=parseInt(subTime/3600);
			 seconds=parseInt(subTime%3600);
			 seconds=parseInt(seconds/60);
	         obj.dayNum= isDouble(minutes)+'小时'+isDouble(seconds)+"分";
	         if (subTime<=0) {
	             clearInterval(interval);
	             obj.lastOrder.paymentStatus=5;
	             return;
	         }
	         subTime-=60;
	         obj.second-=60;
	         obj.$apply();
	     }, 60000);
	}
}