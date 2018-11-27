(function($){
	var basePath="http://"+window.location.host+"/HXTravelInterface/";

	 /* document.addEventListener('touchstart',touch,false);
	  document.addEventListener('touchmove',touch,false);
	  document.addEventListener('touchend',touch,false);*/
	
	var store = localStorage;
	var tj;
	
	//初始化
	function init(){
		
		requestTongJi();//发送统计数据
		
		tj=new tongji();//初始化對象
		
		getNameAndId(tj);
		
		getIp(tj);//获取IP
		
		getSessionId(tj);//获取IP
		
		getVersion(tj);//获取版本
	}

	function tongji(){
		this.url=window.location.href;//地址
		this.title=document.title;//标题
		this.startTime=new Date().getTime();//开始时间
		this.endTime;//结束时间
		this.sessionId;//结束时间
		this.duration;//时长（毫秒）
		this.isDown=0;//是否到底
		this.ip;//ip
		this.country;//国家
		this.province;//省
		this.city;//城市
		this.isp;//运营商
		this.accessName;//访问名称
		this.accessId;//访问Id
		this.author;//作者
		this.statisticsPosition;//点击坐标
		this.version;//ios或者android
		this.screen;//分辨率
		this.clickNum=0;
		this.createTime=parseInt(new Date().getTime()/1000);
		this.type;//类型 1 测一测试题 2医生
	}
	
	function position(x,y){
		this.x=x;
		this.y=y;
	}
	
	//页面关闭事件
	window.onbeforeunload = function(){
		tj.endTime=new Date().getTime();
		tj.duration=tj.endTime-tj.startTime;
		var tjHistory=eval('(' + localStorage.getItem('tongji') + ')');
		if(null==tjHistory){
			tjHistory=new Array();
			store.setItem("tongjiTime",new Date().getTime()/1000);
		}
		tjHistory.push(tj);
		store.setItem("tongji",JSON.stringify(tjHistory));
	};
	
	  //var clickPosition=new Array();
	/*  function touch (event){
	        var event = event || window.event;
	         
	        var oInp = document.getElementById("inp");
	 
	        switch(event.type){
	            case "touchstart":
	            	var xx = event.touches[0].clientX || 0; 
	        		var yy = event.touches[0].clientY || 0; 
	        		//clickPosition.push(new position(parseInt(xx),parseInt(yy)));
	        		//tj.statisticsPosition=clickPosition;
	                break;
	            case "touchend":
	            	tj.clickNum=tj.clickNum+1;
	                break;
	            case "touchmove":
	                break;
	        };
	         
	    }*/
	
	//页面滚动事件
	$(window).scroll(function() {
		var scrollTop = $(this).scrollTop();
		var scrollHeight = $(document).height();
		var windowHeight = document.body.clientHeight;
		if(scrollTop + windowHeight >= scrollHeight){
			tj.isDown=1;
		};
	 });
	
	
	// 绑定全局Complete事件
	$(document).ajaxComplete( function(event, jqXHR, options){
		if(document.body.clientHeight==$(document).height()){
			tj.isDown=1;
		}else{
			tj.isDown=0;
		}
	});	
	
	
	function getIp(obj){
		$.getScript("http://pv.sohu.com/cityjson",function(){  //加载test.js,成功后，并执行回调函数
			if(returnCitySN.cname.indexOf("市")>0&&returnCitySN.cname.indexOf("省")>0){
				obj.province=returnCitySN.cname.substring(0,returnCitySN.cname.indexOf("省")+1);
				obj.city=returnCitySN.cname.substring(returnCitySN.cname.indexOf("省")+1);
			}else{
				obj.province=returnCitySN.cname;
			}
			obj.ip=returnCitySN.cip; 
		});
	}
	
	function getSessionId(obj){
		$.post(basePath+"statistics/querySessionid.action",function(data){
			if(null!=data&&""!=data){
				obj.sessionId=data;
			};
		});
	}
	
	function requestTongJi(){
		var tongjiTime=store.getItem('tongjiTime');
		var now=new Date().getTime()/1000;
		var duration=(now-tongjiTime)/60;
		var t=eval('(' + store.getItem('tongji') + ')');
		if(duration>=30&&t){
			$.post(basePath+"statistics/addStatistics.action",{"statistics":JSON.stringify(t)},function(data){
				if(null!=data&&""!=data){
					store.removeItem("tongjiTime");
					store.removeItem("tongji");
				};
			});
		};
	}
	
	function isContains(str, substr) {
	    return str.indexOf(substr) >= 0;
	}
	
	function getQueryString(name) { 
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
		var r = window.location.search.substr(1).match(reg); 
		if (r != null) return unescape(r[2]); return null; 
	} 
	
	function getVersion(obj){
		var u = navigator.userAgent, app = navigator.appVersion;
	    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //g
	    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	    if (isAndroid) {
	    	obj.version="android";
	    }
	    if (isIOS) {
	    	obj.version="ios";
	    }
	    obj.screen=window.screen.width+"×"+window.screen.height;
	}
	
	init();
	
	function getNameAndId(obj){
		var url=window.location.href;
		if(isContains(url,"index.html")){//主页
			obj.accessName="主页";
			obj.type=1;
		}else if(isContains(url,"myself_mine_index.html")){//个人中心
			obj.accessName="个人中心";
			obj.type=2;
		}else if(isContains(url,"sign.html")){//签到
			obj.accessName='签到';
			obj.type=3;
		}else if(isContains(url,"convert_record.html")){//积分详情
			obj.accessName='积分详情';
			obj.type=4;
		}else if(isContains(url,"myself_mine_info.html")){//个人信息
			obj.accessName="个人信息";
			obj.type=5;
		}else if(isContains(url,"orderall.html")){//全部订单
			obj.accessName='全部订单';
			obj.type=6;
		}else if(isContains(url,"order_details.html")){//订单详情
			obj.accessName='订单详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=7;
		}else if(isContains(url,"evaluate.html")){//商品评价
			obj.accessName='商品评价';
			obj.accessId=getQueryString("orderNo");
			obj.type=8;
		}else if(isContains(url,"apply_refund.html")){//退款申请
			obj.accessName='退款申请';
			obj.accessId=getQueryString("orderNo");
			obj.type=9;
		}else if(isContains(url,"refund.html")){//退款详情
			obj.accessName='退款详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=10;
		}else if(isContains(url,"banner_detail.html")){//banner详情
			obj.accessName='banner详情';
			obj.accessId=getQueryString("id");
			obj.type=11;
		}else if(isContains(url,"evaluate_list.html")){//评价列表
			obj.accessName='评价列表';
			obj.accessId=getQueryString("goodsId");
			obj.type=12;
		}else if(isContains(url,"all_classify.html")){//商品分类
			obj.accessName='商品分类';
			obj.type=13;
		}else if(isContains(url,"classify_list.html")){//商品搜索
			obj.accessName='商品搜索';
			obj.accessId=getQueryString("goodsName");
			obj.type=14;
		}else if(isContains(url,"goods_detail.html")){//商品详情
			obj.accessName='商品详情';
			obj.accessId=getQueryString("id");
			obj.type=15;
		}else if(isContains(url,"mall.html")){//商城首页
			obj.accessName='商城首页';
			obj.type=16;
		}else if(isContains(url,"shoppingcartlist.html")){//购物车
			obj.accessName='购物车';
			obj.type=17;
		}else if(isContains(url,"confirm_order.html")){//商品确认订单
			obj.accessName='商品确认订单';
			obj.accessId=null==getQueryString("goodsId")?getQueryString("id"):getQueryString("goodsId");
			obj.type=18;
		}else if(isContains(url,"coupons_center.html")){//卡券中心
			obj.accessName='卡券中心';
			obj.type=19;
		}else if(isContains(url,"service_details.html")){//卡券详情
			obj.accessName='卡券详情';
			obj.accessId=getQueryString("detailId");
			obj.type=20;
		}else if(isContains(url,"payment.html")){//订单支付
			obj.accessName='订单支付';
			obj.accessId=getQueryString("orderNo");
			obj.type=21;
		}else if(isContains(url,"travel.html")){//摩范出行
			obj.accessName='摩范出行';
			obj.type=22;
		}else if(isContains(url,"my_collection.html")){//我的收藏
			obj.accessName='我的收藏';
			obj.type=23;
		}else if(isContains(url,"knowledge_list.html")){//知识库列表
			obj.accessName='知识库列表';
			obj.type=24;
		}else if(isContains(url,"knowledge_detail.html")){//知识库详情
			obj.accessName='知识库详情';
			obj.accessId=getQueryString("id");
			obj.type=25;
		}else if(isContains(url,"live_validate.html")){//直播验证
			obj.accessName='直播验证';
			obj.type=26;
		}else if(isContains(url,"live_broadcast.html")){//观看直播
			obj.accessName='观看直播';
			obj.accessId="appName="+getQueryString("appName")+"&streamName="+getQueryString("streamName");
			obj.type=27;
		}else if(isContains(url,"medical_evaluate.html")){//服务评价
			obj.accessName='服务评价';
			obj.accessId=getQueryString("orderNo");
			obj.type=28;
		}else if(isContains(url,"medical_Info.html")){//服务详情
			if(getQueryString("type")==4){
				obj.accessName='预约挂号';
				obj.type=29;
			}else if(getQueryString("type")==6){
				obj.accessName='预约会诊';
				obj.type=30;
			}else if(getQueryString("type")==5){
				obj.accessName='陪诊服务';
				obj.type=31;
			}else if(getQueryString("type")==7){
				obj.accessName='预约住院';
				obj.type=32;
			}else{
				obj.accessName='预约手术';
				obj.type=33;
			}
			obj.accessId=getQueryString("type");
		}else if(isContains(url,"medical_service.html")){//服务列表
			obj.accessName='服务列表';
			obj.type=34;
		}else if(isContains(url,"select_depart.html")){//科室列表
			obj.accessName='科室列表';
			obj.accessId=getQueryString("infirmaryId");
			obj.type=35;
		}else if(isContains(url,"select_illness.html")){//疾病列表
			obj.accessName='疾病列表';
			obj.accessId=getQueryString("departId");
			obj.type=36;
		}else if(isContains(url,"select_infirmary.html")){//医院列表
			obj.accessName='医院列表';
			obj.type=37;
		}else if(isContains(url,"select_position.html")){//医生列表
			obj.accessName='医生列表';
			obj.type=38;
		}else if(isContains(url,"upload_hospital.html")){//住院单
			obj.accessName='住院单';
			obj.type=39;
		}else if(isContains(url,"upload_image.html")){//影像及生化资料
			obj.accessName='影像及生化资料';
			obj.type=40;
		}else if(isContains(url,"confirm_con_order.html")){//预约会诊
			obj.accessName='预约会诊';
			obj.type=41;
		}else if(isContains(url,"confirm_escort_order.html")){//预约陪诊
			obj.accessName='预约陪诊';
			obj.type=42;
		}else if(isContains(url,"confirm_escort_orderDetail.html")){//预约陪诊详情
			obj.accessName='预约陪诊详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=43;
		}else if(isContains(url,"confirm_escort_orderSuccess.html")){//预约陪诊成功
			obj.accessName='预约陪诊成功';
			obj.accessId=getQueryString("orderNo");
			obj.type=44;
		}else if(isContains(url,"confirm_gh_detail.html")){//预约挂号详情
			obj.accessName='预约挂号详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=45;
		}else if(isContains(url,"confirm_hospital_order.html")){//预约住院
			obj.accessName='预约住院';
			obj.type=46;
		}else if(isContains(url,"confirm_hz_detail.html")){//预约会诊详情
			obj.accessName='预约会诊详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=47;
		}else if(isContains(url,"confirm_medical_order.html")){//预约挂号
			obj.accessName='预约挂号';
			obj.type=48;
		}else if(isContains(url,"confirm_zy_detail.html")){//预约住院详情
			obj.accessName='预约住院详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=49;
		}else if(isContains(url,"consultation_edit.html")){//编辑会诊资料
			obj.accessName='编辑会诊资料';
			obj.accessId=getQueryString("orderNo");
			obj.type=50;
		}else if(isContains(url,"hospital_edit.html")){//编辑住院资料
			obj.accessName='编辑住院资料';
			obj.accessId=getQueryString("orderNo");
			obj.type=51;
		}else if(isContains(url,"forget_password.html")){//忘记密码
			obj.accessName='忘记密码';
			obj.type=52;
		}else if(isContains(url,"login.html")){//登陆页面
			obj.accessName='登陆页面';
			obj.type=53;
		}else if(isContains(url,"modify_password.html")){//修改密码
			obj.accessName='修改密码';
			obj.type=54;
		}else if(isContains(url,"register.html")){//注册
			obj.accessName='注册';
			obj.type=55;
		}else if(isContains(url,"user_agreement.html")){//用户协议
			obj.accessName='用户协议';
			obj.type=56;
		}else if(isContains(url,"patient_add.html")){//添加患者资料
			obj.accessName='添加患者资料';
			obj.type=57;
		}else if(isContains(url,"patient_edit.html")){//编辑患者资料
			obj.accessName='编辑患者资料';
			obj.accessId=getQueryString("addrId");
			obj.type=58;
		}else if(isContains(url,"patient_message.html")){//患者资料列表
			obj.accessName='患者资料列表';
			obj.type=59;
		}else if(isContains(url,"convert_record.html")){//积分列表
			obj.accessName='积分列表';
			obj.type=60;
		}else if(isContains(url,"convert_rule.html")){//积分兑换规则
			obj.accessName='积分兑换规则';
			obj.type=61;
		}else if(isContains(url,"exchange_detail.html")){//积分兑换详情
			obj.accessName='积分兑换详情';
			obj.accessId=getQueryString("orderNo");
			obj.type=62;
		}else if(isContains(url,"exchange_record.html")){//积分兑换记录
			obj.accessName='积分兑换记录';
			obj.type=63;
		}else if(isContains(url,"exchange_succeed.html")){//积分兑换成功
			obj.accessName='积分兑换成功';
			obj.accessId=getQueryString("orderNo");
			obj.type=64;
		}else if(isContains(url,"integral_store.html")){//积分商城
			obj.accessName='积分商城';
			obj.type=65;
		}else if(isContains(url,"present_detail.html")){//积分商品详情
			obj.accessName='积分商品详情';
			obj.accessId=getQueryString("id");
			obj.type=66;
		}else if(isContains(url,"select_address.html")){//收货地址列表
			obj.accessName='收货地址列表';
			obj.type=67;
		}else if(isContains(url,"shippingaddress.html")){//编辑收货地址
			obj.accessName='编辑收货地址';
			obj.accessId=getQueryString("addrId");
			obj.type=68;
		}else{
			obj.accessName=$("header").find("h3").html();
			obj.type=69;
		}
	};
	
})(jQuery);