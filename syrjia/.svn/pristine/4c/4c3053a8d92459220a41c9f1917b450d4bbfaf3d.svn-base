var Util = {};

/*$(document).ready(function(){ 
	$.ajax({  
        type : "post",  
         url : basePath+"queryWeiXinConfig.action",  
         async : false,  
         success : function(data){  
        	 alert("初始化方法进入"+JSON.stringify(data)); 
	         if(data.respCode=="1001"){
	        	 Util.set("loginkey",JSON.stringify(data));
			     Util.set("user",JSON.stringify(data.datas.user));
	         }else{
	        	 openConfirm("1","提示","您还没有登陆，请登陆后查看信息。",function(){
	        		 window.location.href='/TennisCenterInterface/weixin/myself/login.html';
	        	 });
	         }
         }  
    }); 
});*/ 

//设置缓存
Util.set=function(key,value){
	localStorage[key]=value;
}

//获取缓存
Util.get=function(key){
	return localStorage[key];
}

//删除缓存
Util.clean=function(key){
	//localStorage.clear(key);
	 localStorage.removeItem(key);
}

//设置缓存，value为JSON对象
Util.setJSON=function(key){
	var json = localStorage[key];
	if(json==undefined){
		return null;
	}
	return JSON.parse(json);
}
/**
 * 截取get url参数
 * @param name
 * @returns
 */
Util.GetQueryString=function(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)
    	 return  r[2]; 
     return null;
};

Util.TimeData=function(time){//获取时间，下拉框，带小时
	if(time<7){
		time=7;
	}
	var arr = new Array();
	var childrenArr = new Array();
	
	for ( var int = 1; int < 3; int++) {
		var obj = new Object();
		var a ="value";
		var b ="text";
		obj[a] =int;
		obj[b] =int+"小时";
		childrenArr.push(obj);
	}
	for ( var int = time; int < 23; int++) {
		var obj = new Object();
		var a ="value";
		var b ="text";
		var c ="children";
		obj[a] =int;
		obj[b] =int+":00";
		obj[c] =childrenArr;
		arr.push(obj);
	}
	return arr;
};
Util.getTime=function(){//获取当前日期，格式为yyyy-mm-dd
	 var date = new Date();
     var year = date.getFullYear();
     var month = date.getMonth() + 1;
     var day = date.getDate();
     var nowdate = "";
     nowdate += year + "-";  
     if (month >= 10) {  
     	nowdate += month + "-";  
     }  
     else {  
     	nowdate += "0" + month + "-";  
     }  
     if (day >= 10) {  
     	nowdate += day;  
     }  
     else {  
     	nowdate += "0" + day;  
     }
     return nowdate;
};
Util.addOneDay=function(date,int){//给固定日期加  int 天
	var day =new Date(date);
	var show_day=new Array('周日','周一','周二','周三','周四','周五','周六'); 
	day.setDate(day.getDate()+int);
	var day1=day.getDay(); 
	return day+","+show_day[day1]; 
};
Util.getWeek=function(date){//给固定日期换成周几
	var day =new Date(date);
	var show_day=new Array('周日','周一','周二','周三','周四','周五','周六'); 
	var day1=day.getDay(); 
	return show_day[day1]; 
};
Util.getMonthDay=function(date){//给固定日期换成月 日
	var day =new Date(date);
	var   month= day.getMonth()+1;
	var   date= day.getDate();  
	return month+"-"+date; 
};

//判断对象是否为空，true为空，false不为空
Util.isEmptyObject=function(obj){
       for(var key in obj){
            return false
       };
       return true
};
//获取7天日期
Util.getDaySeven=function(){
	var show_day=new Array('周日','周一','周二','周三','周四','周五','周六'); 
	var arr = new Array();
	for ( var i = 0; i < 8; i++) {
		var date = new Date();
		date.setDate(date.getDate()+i);
		var obj = new Object();
		var a ="value";
		var b ="text";
		var month =date.getMonth()+1;
		if(month<10){
			day="0"+day;
		}
		var day =date.getDate();
		if(day<10){
			day="0"+day;
		}
		obj[a] =date.getFullYear()+"-"+month+"-"+day;
		if(i==0){
			obj[b] ="("+month+"月"+day+"日)今天";
		}else if(i==1){
			obj[b] ="("+month+"月"+day+"日)明天";
		}else if(i==2){
			obj[b] ="("+month+"月"+day+"日)后天";
		}else{
			obj[b] ="("+month+"月"+day+"日)"+show_day[date.getDay()];
		}
		arr.push(obj)
	}
	
	return arr;
	
};







