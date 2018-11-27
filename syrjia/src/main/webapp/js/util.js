﻿var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; // android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
var isWindow = u.indexOf("Windows") > -1 || u.indexOf("Window") > -1|| u.indexOf("windows") > -1;
var allIndex;
var isSubject=true;
var basePath="http://"+window.location.host+"/syrjia/";
var postCfg = {
	    headers: { 'Content-Type':'application/x-www-form-urlencoded;charset=utf-8;' },
	    transformRequest: function (data) {
	        return $.param(data);
	    }
	};

/*document.write('<link href="//cdn.bootcss.com/angular-loading-bar/0.9.0/loading-bar.min.css" rel="stylesheet">');
*/document.write('<script src="/syrjia/js/loading-bar.min.js"></script>');
document.write('<script src="/syrjia/js/exif.js"></script>');

window.onpageshow=function(e){
    if(e.persisted) {
        window.location.reload(); 
    }
};


//判断是否微信登陆
function isWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == 'micromessenger') {
		return true;
	} else {
		return false;
	}
}
function isEmpty(str){
	str=str+'';
	if(null==str||''==str.trim()||"null"==str.trim()||typeof(str) == "undefined"||str == "undefined"||str.indexOf('请选择')==0){
		return true;
	}else{
		 return false;
	}
}

function isPhone(phone){
	return !(/^1[3|4|5|6|7|8|9][0-9]{9}$/.test(phone));
}

function isCardNo(card){  
   return !(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(card));
}

function isPositiveInteger(s){//是否为正整数
    var re = /^[0-9]+$/ ;
    return re.test(s)
}


// 询问弹框
function openConfirmBtn(title, content,btnValue,callback,callback1) {
	closeAlertMsgLoad();
	mui.confirm(content,title, btnValue, function(e) {
		if (e.index == 0) {
			if (callback)
				callback();// 回调
		}else if(index==1){
			if (callback1)
				callback1();// 回调
		}
	},'div');
}

// 询问弹框
function openConfirm(title, content, callback) {
	closeAlertMsgLoad();
	mui.confirm(content,title, [ '确定', '取消' ], function(e) {
		if (e.index == 0) {
			if (callback)
				callback();// 回调
		}
	},'div');
}


// 提醒弹框
function openAlert(content,callback) {
	closeAlertMsgLoad();
	mui.alert(content, '提示', function() {
		if (callback)
			callback();// 回调
	},'div');
}

var mask;
function openAlertMsgLoad(content){
	closeAlertMsgLoad();
	// var div='<div class="loding"><div class="loding_img"><span><img
	// src="'+basePath+'img/loading.gif"></span></div><p>'+content+'</p></div>';
	var div='<div class="wx_loading" id="wxloading" style=""><div class="wx_loading_inner"><i class="wx_loading_icon"></i>'+content+'</div></div>';
	$("body").append(div);
	mask = mui.createMask();// callback为用户点击蒙版时自动执行的回调；
	mask.show();// 显示遮罩
}

function closeAlertMsgLoad(){
	$(".wx_loading").remove();
	if(mask){
		mask.close();// 显示遮罩
	}
}



// 提醒弹框
function openAlertMsg(content) {
	closeAlertMsgLoad();
	mui.toast(content,{ duration:'long', type:'div' });// long(3500ms),short(2000ms)
}

// 时间戳转日期
function getLocalTime(nS) {   
	  return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
} 
// 时间戳转日期
function   formatDate(nS)   {    
	 var now=new  Date(parseInt(nS) * 1000);
    var   year=now.getFullYear();     
    var   month=now.getMonth()+1;     
    var   date=now.getDate();     
    var   hour=now.getHours();     
    var   minute=now.getMinutes();     
    var   second=now.getSeconds();     
    return   year+"-"+isDouble(month)+"-"+isDouble(date)+"   "+isDouble(hour)+":"+isDouble(minute)+":"+isDouble(second);     
}  

// 时间戳转日期
function   formatDateYYYYMMDDHM(nS)   {    
	 var now=new  Date(parseInt(nS) * 1000);
    var   year=now.getFullYear();     
    var   month=now.getMonth()+1;     
    var   date=now.getDate();     
    var   hour=now.getHours();     
    var   minute=now.getMinutes();     
    var   second=now.getSeconds();     
    return   year+"-"+isDouble(month)+"-"+isDouble(date)+"   "+isDouble(hour)+":"+isDouble(minute);     
} 

// 获取当前日期
function   formatDateMMDDW()   { 
	var show_day=new Array('周日','周一','周二','周三','周四','周五','周六'); 
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate(); 
	  var day1=date.getDay(); 
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	return month + '月' + day+'日 '+' '+show_day[day1];    
} 

// 获取当前日期 年月日时分
function   formatDateYYMMDDHHMM()   { 
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate(); 
	  var day1=date.getDay(); 
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	return year+'-'+month + '-' + day+' '+hour+':'+minute;    
}

// 获取当前日期 年月日时分
function   formatDateYYMMDD()   { 
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate(); 
	return year+'-'+isDouble(month) + '-' + isDouble(day);    
}

// 时间戳转日期
function   formatDateYYYYMMDD(nS)   {    
	 var now=new  Date(parseInt(nS) * 1000);
    var   year=now.getFullYear();     
    var   month=now.getMonth()+1;     
    var   date=now.getDate();     
    var   hour=now.getHours();     
    var   minute=now.getMinutes();     
    var   second=now.getSeconds();     
    return   year+"-"+isDouble(month)+"-"+isDouble(date);     
} 


// 时间戳转日期
function   formatDateMMDD(nS)   {    
	 var now=new  Date(parseInt(nS) * 1000);
 var   year=now.getFullYear();     
 var   month=now.getMonth()+1;     
 var   date=now.getDate();     
 var   hour=now.getHours();     
 var   minute=now.getMinutes();     
 var   second=now.getSeconds();     
 return   isDouble(month)+"-"+isDouble(date);     
} 

// 时间戳转日期
function   formatDateSimple(nS)   {    
	 var now=new  Date(parseInt(nS) * 1000);
    var   month=now.getMonth()+1;     
    var   date=now.getDate();     
    var   hour=now.getHours();     
    var   minute=now.getMinutes();     
    return  isDouble(month)+"-"+isDouble(date)+"   "+isDouble(hour)+":"+isDouble(minute);     
}

function addDate(date,days){
	var d=new Date(date);
	d.setDate(d.getDate()+parseInt(days));
	var month=d.getMonth()+1;
	var day = d.getDate();
	if(month<10){
		month = "0"+month;
	}
	if(day<10){
		day = "0"+day;
	}
	var val = d.getFullYear()+"-"+month+"-"+day;
	return val;
}

var windowsUrl = window.location.href;
var windowsFlag=windowsUrl.indexOf("activity/activity")>-1
||windowsUrl.indexOf("goods/commodity_details")>-1
//||windowsUrl.indexOf("knowledge/knowledge_circle")>-1
||windowsUrl.indexOf("myself/doctor_card")>-1
||windowsUrl.indexOf("register/aid_card")>-1
||windowsUrl.indexOf("hospital/doctor_detail")>-1
||windowsUrl.indexOf("hospital/line_below.html")>-1
//||windowsUrl.indexOf("hospital/look_scheme")>-1
//||windowsUrl.indexOf("knowledge/article_detail")>-1
||windowsUrl.indexOf("goods/store_index")>-1
||windowsUrl.indexOf("order/queryPayOrderDetail")>-1
||windowsUrl.indexOf("wx/wxJsApiPay")>-1
//||windowsUrl.indexOf("pay/pay")>0
||windowsUrl.indexOf("question/faq")>-1
||windowsUrl.indexOf("myself/user_agreement")>-1
||windowsUrl.indexOf("myself/bill")>-1
||windowsUrl.indexOf("settle_accounts")>-1
||windowsUrl.indexOf("bill_detail.html")>-1
||windowsUrl.indexOf("logistics/logistics")>-1
||windowsUrl.indexOf("register/download_assistant")>-1
||windowsUrl.indexOf("register/download_doctor")>-1
//||windowsUrl.indexOf("goods/commodity_details")>-1
||windowsUrl.indexOf("myself/binding_card")>-1
||windowsUrl.indexOf("myself/history_card")>-1
||windowsUrl.indexOf("im/be_careful")>-1
||windowsUrl.indexOf("myself/doctor_card")>-1
||windowsUrl.indexOf("ppDoctor/queryFollowBySrId")>-1
||windowsUrl.indexOf("serverOnline/service")>-1;
if((isWeiXin()||!windowsFlag)&&windowsUrl.indexOf("register/doctor_register")==-1&&windowsUrl.indexOf("register/aid_card")==-1&&windowsUrl.indexOf("register/downloadGuide")==-1&&windowsUrl.indexOf("register/doctor_register_v2")==-1&&windowsUrl.indexOf("register/download_doctor")==-1&&windowsUrl.indexOf("register/download_assistant")==-1&&windowsUrl.indexOf("leadPhone/leadPhone")==-1&&windowsUrl.indexOf("knowledge/article_detail")==-1&&windowsUrl.indexOf("myself/doctor_card")==-1&&windowsUrl.indexOf("toAPP/toAPP")==-1&&windowsUrl.indexOf("pay/pay")==-1){
	getUser();
}
function getUser(){
	$.ajax({
	    type: "post",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    url: basePath+"member/getMember.action?openId="+getCookie("openId"),
	    async: true,
	    crossDomain: true,
	    success: function (data) {
	    	if(data.respCode!=1001){
				var content='<div class="layer subscribeLayer"></div><div class="public_twocode subscribeQrcode"><a></a><p class="public_top">关注上医仁家平台</p><p class="public_title public_bot public_ichrscan">长按识别二维码</p><div class="public_code"><span> <img src="../../img/qrcode_gz.jpg"> </span></div><p class="public_title">为了您更好的享受上医仁家的服务，如实时的接受调理、购物等通知，请您关注上医仁家平台。</p></div>';
				$("body").append(content);
			}else {
				if(data.data.state==2){
					var content = '<div class="hint_main"><div class="hint_con"><img src="../../img/hint.png"><div class="hint_text"><h3>您的账号异常</h3><p>请联系客服处理</p></div></div></div>';
					$("body").append(content);
				}
			}
	    }
	});
	/*$.post(basePath+"member/getMember.action",function(data){
		if(data.respCode!=1001){
			var content='<div class="layer subscribeLayer"></div><div class="public_twocode subscribeQrcode"><a></a><p class="public_top">关注上医仁家平台</p><p class="public_title public_bot public_ichrscan">长按识别二维码</p><div class="public_code"><span> <img src="../../img/qrcode_gz.jpg"> </span></div><p class="public_title">为了您更好的享受上医仁家的服务，如实时的接受调理、购物等通知，请您关注上医仁家平台。</p></div>';
			$("body").append(content);
		}else {
			if(data.data.state==2){
				var content = '<div class="hint_main"><div class="hint_con"><img src="../../img/hint.png"><div class="hint_text"><h3>您的账号异常</h3><p>请联系客服处理</p></div></div></div>';
				$("body").append(content);
			}
		}
	});*/
}

function getCookie(name){
	var strcookie = document.cookie;//获取cookie字符串
	var arrcookie = strcookie.split("; ");//分割
	//遍历匹配
	for ( var i = 0; i < arrcookie.length; i++) {
		var arr = arrcookie[i].split("=");
		if (arr[0] == name){
			return arr[1];
		}
	}
	return "";
}

function isDouble(str){
	if(str<10){
		return "0"+str;
	}
	else{
		return str;
	}
}

function dateToTimestamp(str,flag){
	if(null==str||""==str){
		return null;
	}
	var timestamp=null;
	try{
		if(flag){
			str=str.trim()+" 23:59:59";
		}
		timestamp = Date.parse(new Date(str.trim()));
		timestamp = timestamp / 1000;
	}catch (e) {
		return null;
	}
	return timestamp;
}

function strToTimestamp(str){
	var timestamp=null;
	try{
		str=str.trim()+":59";
		timestamp = Date.parse(new Date(str.trim()));
		timestamp = timestamp / 1000;
	}catch (e) {
		return null;
	}
	return timestamp;
}

      
function formatJiage(jiage){
	if(jiage==null||jiage==''){
		return 0;
	}else{
		var jiage0 = jiage.toString().split(".")[0];
		var jiage1 = jiage.toString().split(".")[1];
		if(jiage1.length==1){
			if(jiage1==0){
				return jiage0;
			}else{
				return jiage;
			}
		}else{
			return jiage;
		}
	}
}




// 上传图片
function uploadImage(fileId,src,param,callback,isAsyn){
	openAlertMsgLoad("上传中");
	var fileObj = document.getElementById(fileId).files; // 获取文件对象
	 if(null==fileObj){
		 closeAll();
		 openAlertMsg('请选择文件'); 
		 return false;
	 }
	 var filepath=$("#"+fileId).val();
	  var extStart=filepath.lastIndexOf(".");
	  var ext=filepath.substring(extStart,filepath.length).toUpperCase();
	  if(ext!=".JPEG"&&ext!=".JPG"&&ext!=".GIF"&&ext!=".AI"&&ext!=".PDG"&&ext!=".PNG"){
		  closeAll();
		  openAlertMsg("请选择正确的图片格式!");
		  return false;
	  }
  var FileController = src;                    // 接收上传文件的后台地址
  // FormData 对象
  var form = new FormData();
  if(null!=param){
	    var jsonObj = param;// eval('(' + param + ')');
	 // 传回ID报错
	    for(var item in jsonObj){  
	        form.append(item,jsonObj[item]); 
	    }  
  }
  for(var i=0;i<fileObj.length;i++){
		 form.append("multipartFile", fileObj[i]);                           // 文件对象
	 }
  // 文件对象
  // XMLHttpRequest 对象
  var xhr = new XMLHttpRequest();
  if(isAsyn==null){
	  isAsyn=true;
  }
  xhr.open("post", FileController, isAsyn);
  xhr.onreadystatechange = function () {
	    if (xhr.readyState === 4) {
	        if (xhr.status === 200) {
	        } else {
	        	openAlertMsg('上传失败!图片大小应小于5M');
	        }
	    }
	}; 
	 xhr.onload = function (data) {
		var responseUrl = this.responseText;
		var json = eval('(' + responseUrl + ')'); 
  	if(null==this.responseText){
  		openAlertMsg('上传失败!'); 
  	}else{
  		closeAlertMsgLoad();
  		// openAlertMsg('上传成功!');
  		if(callback){
  			callback(json);
  		}
  	}
  	
  };
  xhr.send(form);  
};

// 上传图片
function uploadImageIm(basePath,blob,param,callback){
	var fd = new FormData();
    if(param){
    	 var jsonObj = param;
    	for(var item in jsonObj){  
    		fd.append(item,jsonObj[item]); 
	    } 
    }
    fd.append("imgFile", blob);
    var xhr = new XMLHttpRequest();
    xhr.addEventListener("load",callback, false);
    xhr.open("POST", basePath+"imImage.action");
    xhr.send(fd);
};
var sendData = new Array;;
function imgPreview(fileId,param,callback,isAsyn){
	var fileTag = document.getElementById(fileId);
	var file = fileTag.files[0];
	fileType = file.type;  
    var fileLen = document.getElementById(fileId).files.length;
    if(fileLen!=null&&fileLen>3){
    	openAlertMsg('每次最多可选3张');  
    }else{
    	 if(/image\/\w+/.test(fileType)){  
    	    // if(file.size>2048){
    	        	openAlertMsgLoad("上传中");
    	        	
    	        	
    	        	var orient;
    	        	 
    	             EXIF.getData(file, function () {
    	                   orient = EXIF.getTag(this, 'Orientation');
    	             });
    	             
    	        	sendData = [];
    	        	for(var f=0;f<fileLen;f++){
    	        		var files = document.getElementById(fileId).files;
    	                file = files[f];
    	                fileType = file.type;  
    	        	    var reader = new FileReader();
    	        	 
    	        	    reader.readAsDataURL(files[f]);
    	        	    var blob = window.URL.createObjectURL(files[f]);// 转换为二进制blob文件
    	        	    var img=new Image();
    	        	    img.src=blob;
    	        	    img.onload = function(){
    	        	        var cvs = document.createElement('canvas');
    	        	        var ctx = cvs.getContext("2d");
    	        	        var scale = 1;
    	        	        var fileKb =  parseInt(file.size/1024);
	                       /* if(this.width > 1000 || this.height > 1000){  // 1000只是示例，可以根据具体的要求去设定
	                            if(this.width > this.height){    
	                                //scale = 500 / this.width;  
	                            	scale = this.height / this.width;
	                            }else{    
	                                //scale = 500 / this.height;  
	                            	scale = this.width / this.height;
	                            }    
	                        } */
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
                        	 // 计算等比缩小后图片宽高
    	                    if (orient == 6) {
	                    		cvs.width = this.height;
    	                    	cvs.height = this.width;
    	                        ctx.rotate(90*Math.PI/180);
    	                        ctx.drawImage(this, 0, -this.height);
	    	                } else {
	    	                	cvs.width = this.width;    
	    	                    cvs.height = this.height;    
	    	                	ctx.drawImage(this,0,0,this.width,this.height,0,0,cvs.width, cvs.height);
	    	                }
    	        	        //ctx.drawImage(this, 0, 0,cvs.width, cvs.height);
    	        	        var newImageData = cvs.toDataURL("image/jpeg",scale);
    	        	        var _sendData = newImageData.replace("data:"+fileType+";base64,",'');
    	        	        // input
							// file不能用于上传base64，所以要再加一个input，同时把当前file的input赋值空，防止原图片也上传
    	        	        sendData.push(newImageData);
        	        	    if(sendData.length==fileLen){
                            	uploadThum(fileId,sendData,param,callback);
                            }
    	        	    };
    	        	}
    	    	
				/*
				 * }else{
				 * uploadImage(fileId,basePath+"uploadImages.action",param,callback,isAsyn); }
				 */
    	    }else{  
    	    	closeAlertMsgLoad();
    	    	openAlertMsg('请选择图片格式文件');  
    	    } 
    }
}

function imgPreviewUpload(obj,param,callback,isAsyn){
	var file = obj.files[0];
	fileType = file.type;  
    var fileLen = obj.files.length;
    if(fileLen!=null&&fileLen>3){
    	openAlertMsg('每次最多可选3张');  
    }else{
    	 if(/image\/\w+/.test(fileType)){  
    	    // if(file.size>2048){
    	        	openAlertMsgLoad("上传中");
    	        	sendData = [];
    	        	for(var f=0;f<fileLen;f++){
    	        		var files = obj.files;
    	                file = files[f];
    	                
    	                var orient;
       	        	 
	       	             EXIF.getData(file, function () {
	       	                   orient = EXIF.getTag(this, 'Orientation');
	       	             });
    	                
    	                fileType = file.type;  
    	        	    var reader = new FileReader();
    	        	 
    	        	    reader.readAsDataURL(files[f]);
    	        	    var blob = window.URL.createObjectURL(files[f]);// 转换为二进制blob文件
    	        	    var img=new Image();
    	        	    img.src=blob;
    	        	    img.onload = function(){
    	        	        var cvs = document.createElement('canvas');
    	        	        var ctx = cvs.getContext("2d");
    	        	        var scale = 1;
    	        	        var fileKb =  parseInt(file.size/1024);
	                       /* if(this.width > 1000 || this.height > 1000){  // 1000只是示例，可以根据具体的要求去设定
	                            if(this.width > this.height){    
	                                //scale = 500 / this.width;  
	                            	scale = this.height / this.width;
	                            }else{    
	                                //scale = 500 / this.height;  
	                            	scale = this.width / this.height;
	                            }    
	                        } */
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
                        	cvs.width = this.width;    
    	                    cvs.height = this.height;     // 计算等比缩小后图片宽高
    	                    if (orient == 6) {
    	                        ctx.save();
    	                        ctx.translate(this.width / 2, this.height / 2);
    	                        ctx.rotate(90 * Math.PI / 180);
	    	                	ctx.drawImage(this,0-this.height/2,0-this.width/2,this.height,this.width);
    	                        ctx.restore();
	    	                } else {
	    	                	ctx.drawImage(this,0,0,this.width,this.height,0,0,cvs.width, cvs.height);
	    	                }
    	                    //ctx.drawImage(this, 0, 0,cvs.width, cvs.height);
    	        	        var newImageData = cvs.toDataURL("image/jpeg",scale);
    	        	        var _sendData = newImageData.replace("data:"+fileType+";base64,",'');
    	        	        // input
							// file不能用于上传base64，所以要再加一个input，同时把当前file的input赋值空，防止原图片也上传
    	        	        sendData.push(newImageData);
        	        	    if(sendData.length==fileLen){
                            	uploadThum(null,sendData,param,callback);
                            }
    	        	    };
    	        	}
    	    	
				/*
				 * }else{
				 * uploadImage(fileId,basePath+"uploadImages.action",param,callback,isAsyn); }
				 */
    	    }else{  
    	    	closeAlertMsgLoad();
    	    	openAlertMsg('请选择图片格式文件');  
    	    } 
    }
}

function uploadThum(fileId,sendData,param,callback){
	var _locaHref = window.location.href;
	$.post('https://file.syrjia.com/FileService/thumUpload',{imgFiles:sendData},function(data){
    	if(data!=null&&data.urllist.length>0){  
        	closeAlertMsgLoad();
        	if(_locaHref.indexOf("myself_mine_info")==-1||_locaHref.indexOf("consultation_edit")==-1||_locaHref.indexOf("hospital_edit")==-1||_locaHref.indexOf("inquiry")==-1){
        		openAlertMsg('上传成功'); 
        	}
      		if(callback){
      			callback(data);
      		}
        }else{ 
        	openAlertMsg('上传失败');  
        }  
	});
	/*
	 * var fd = new FormData(); console.log(sendData.length);
	 * fd.append("imgFiles",sendData); var xhr = new XMLHttpRequest();
	 * xhr.addEventListener("load",callback, false);
	 * xhr.addEventListener("error",callback, false); xhr.open("POST",
	 * basePath+"thumUpload.action"); xhr.send(fd);
	 */
}
/**
 * 截取get url参数
 * 
 * @param name
 * @returns
 */
function getQueryString(url,name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = url.substr(url.indexOf("?")+1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

// 动态加载js脚本文件
function loadScript(url) {
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = url;
    document.body.appendChild(script);
}

// 原预览功能
function imgLocalPreview(fileId,showId){
	var fileTag = document.getElementById(fileId);
	var file = fileTag.files[0];
    var fileReader = new FileReader();
    fileReader.onloadend = function () {
        if (fileReader.readyState == fileReader.DONE) {
            document.getElementById(showId).setAttribute('src', fileReader.result);
        }
    };
    fileReader.readAsDataURL(file);
}

$(function(){
	var oHeight = $(window).height();     // 获取当前窗口的高度
	$(window).resize(function () {
	  if ($(window).height() >= oHeight) {
		$(".bottomHiSh").prev().css("margin-bottom","4rem");
	    $(".bottomHiSh").show();
	  } else {
		$(".bottomHiSh").prev().css("margin-bottom","1rem");
	    $(".bottomHiSh").hide();
	  }
	});
});

function loadOver(obj){
	$("."+obj).find(".loadBottom").remove();
	setTimeout(function(){
		$("."+obj).append('<div class="mui-pull-bottom-pocket mui-block mui-visibility"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-hidden"></div><div class="mui-pull-caption mui-pull-caption-nomore">没有更多数据</div></div></div>');
	},100);
}


function uuid(len, radix) {
	  var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
	  var uuid = [], i;
	  radix = radix || chars.length;
	 
	  if (len) {
	   // Compact form
	   for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
	  } else {
	   // rfc4122, version 4 form
	   var r;
	 
	   // rfc4122 requires these characters
	   uuid[8] = uuid[13] = uuid[18] = uuid[23] = '';
	   uuid[14] = '4';
	 
	   // Fill in random data. At i==19 set the high bits of clock sequence as
	   // per rfc4122, sec. 4.1.5
	   for (i = 0; i < 36; i++) {
	    if (!uuid[i]) {
	     r = 0 | Math.random()*16;
	     uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
	    }
	   }
	  }
	 
	  return uuid.join('');
	}


function diy_time(time1,time2){
    time1 = Date.parse(new Date(time1));
    time2 = Date.parse(new Date(time2));
    return Math.abs(parseInt((time2 - time1)/1000/3600/24));
}


// 获取7天日期
function getDaySeven(visitTime, minTime, maxTime, sign) {
	var show_day = new Array('周日', '周一', '周二', '周三', '周四', '周五', '周六');
	var arr = new Array();
	if (sign == 'star') {
		for ( var i = 0; i < maxTime; i++) {
			var date = new Date();
			date.setDate(date.getDate() + i+visitTime);
			var obj = new Object();
			var a = "value";
			var b = "text";
			var month = date.getMonth() + 1;
			var vMonth = month;
			if (month < 10) {
				vMonth = "0" + vMonth;
			}
			var day = date.getDate();
			if (day < 10) {
				day = "0" + day;
			}
			if(month==2&&day>=15&&day<=21){
				console.log(date.getFullYear() + "-" + vMonth + "-" + day);
			}else{
				obj[a] = date.getFullYear() + "-" + vMonth + "-" + day;
				obj[b] =  date.getFullYear() + "年" + month + "月" + day + "日"
						+"(" + show_day[date.getDay()]+")";
				arr.push(obj);
			}
			
		}
	}else{
		for ( var i = 0; i < maxTime; i++) {
			var date = new Date();
			date.setDate(date.getDate() + i+minTime+visitTime);
			var obj = new Object();
			var a = "value";
			var b = "text";
			var month = date.getMonth() + 1;
			var vMonth = month;
			if (month < 10) {
				vMonth = "0" + vMonth;
			}
			var day = date.getDate();
			if (day < 10) {
				day = "0" + day;
			}
			obj[a] = date.getFullYear() + "-" + vMonth + "-" + day;
			obj[b] = date.getFullYear() + "年" + month + "月" + day + "日"
					+  "("+show_day[date.getDay()]+")";
			arr.push(obj);
		}
	}
	return arr;

};

var interval;
function djs(waitTime,id) {
	interval = setInterval(function() {
		var m =  Math.floor(waitTime/60%60);
		var s =  Math.floor(waitTime%60);
		m = m < 10 ? "0" + m : m;
		s = s < 10 ? "0" + s : s;
		$("#"+id).html(m + '</span>:<span>' + s + '</span>');
		if (m == 0 && s == 0) {
			clearInterval(interval);
			return;
		}
		waitTime--;
	}, 1000);
}


