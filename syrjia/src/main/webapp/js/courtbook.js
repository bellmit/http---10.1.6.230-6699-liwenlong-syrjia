var verticalArr = [];
var selectCourts = {};
var selectCourts2 = {};
function deleteArr(){
	for(var key in selectCourts){
		delete selectCourts[key];
		}
	for(var key in selectCourts2){
		delete selectCourts2[key];
		}
};
var user = eval('(' + Util.get("user") + ')');
var card = Util.getCard();
//增加选中的样式
function addSelected(){
	for(var key in selectCourts2){
		$('[goodsid='+key+']').addClass('selected');
	}
};
$(window).on("load",function(){
    var touchType = ('createTouch' in document) ? 'tap' : 'click';
    var  uParam = {}, bindCourtArr = [];
    var utils = {
        getUrlParam: function(){
            var s = location.search;
            if(s.length < 2) {
                return;
            }
            s = s.substr(1);
            var arr = s.split('&');
            $.each(arr, function(i,v){
                var n = v.split('=');
                uParam[n[0]] = n[1];
            });
        },
        updateSum: function(){
        	
        	if(user==undefined||user==null||user==""){
    			/*var SignInStr ='<h3>提示</h3><p>您还未登录，请登录以后选择场地</p>'
    			$(".book_hint_top").html(SignInStr);
    			var SignInBtn ='<a onclick="cancel()">取消</a><a onclick="window.location.href=&apos;/TennisCenterInterface/weixin/myself/login.html&apos;">确定</a>'
    			$(".book_hint_btn").html(SignInBtn);
    			$(".book_hint").show();
    			$(this).removeClass("active");
    			$('[data-t='+nowdate+']').addClass('active');*/
        		openConfirm("1","提示","您还未登录，请登录以后选择场地。",function(){
        			window.location.href='/TennisCenterInterface/weixin/myself/login.html';
        		});
    			return;
    		}
        	
            var sum = 0;
            $.each(selectCourts, function(i,v){
                sum += v;
            });
            var cent = '';
            var str = '';
            $('#select_court_info').html('');

            var sortCourtArr = utils.sortBy1(selectCourts2);
            sortCourtArr.sort(utils.sortBy2('sort',false,parseInt));

            sortCourtArr.forEach(function(item){
                var contents = selectCourts2[item.id].split(",");
                cent +='<li><span>'+contents[1] + ' ' + contents[0] +'</span>';
                cent +='<input type="hidden" value="'+contents[2]+'" name="price[]" />';
                cent +='<input type="hidden" value="'+contents[1]+'" name="hour[]" />';
                cent +='<input type="hidden" value="'+contents[0]+'" name="course_name[]" />';
                cent += '<input type="hidden" value="'+contents[3]+'" name="real_time[]" />';
                var checkReturn = utils.bindCourt(item.id,contents);
                if(!utils.checkInGroup(item.id)){
                    var classN = "";
                    classN = contents[0].length <= 6 ? "txtStyle3" : "txtStyle1";
                    str += "<li><span><span>"+contents[0]+"</span>"+contents[3]+"</span><a></a></li>"
                }else{
                    if(checkReturn){
                        str += checkReturn;
                    }
                }
            })

            var total = sum ? sum + "元 " : "";
            $('.J_submit div:nth-of-type(1)').html(total+"提交订单");
            $('.court-tips ul').html(cent);
            $('.book-orderinfo').html(str);

            if($('.book-orderinfo li').size()>0){
            	$(".book-tip").hide();
                $(".book-orderinfo").removeClass('hide');
                $(".book-orderinfo").show();
                $(".book-tip").addClass('hide');
            }else{
                $('.J_submit div:nth-of-type(1)').html("请选择场地");
                $(".book-orderinfo").addClass('hide');
                $(".book-orderinfo").hide();
                $(".book-tip").removeClass('hide');
                $(".book-tip").show();
            }
        },
        checkInGroup:function(id){
            var bInBindGroup = false;
            bindCourtArr.forEach(function(item){
                if(item.group_id.indexOf(id) > -1){
                    bInBindGroup = true;
                }
            })
            return bInBindGroup;
        },
        bindCourt:function(id,contents){
            var returnStr = "";
            bindCourtArr.forEach(function(item){
                if(item.group_id.indexOf(id)>-1){
                    if(item.key == 0){
                        item.key = 1;
                        returnStr = "";
                    }else{
                        item.key = 0;
                        var arr = item.timeLen.split(",");
                        a = '',
                            b = arr[0].split("-")[0],
                            c = arr[0].split("-")[1],
                            d = arr[1].split("-")[0],
                            e = arr[1].split("-")[1];
                        if(parseInt(b) > parseInt(d)){
                            a = d + "-" +c;
                        }else{
                            a = b + "-" +e;
                        }
                        returnStr = "<li><div>"+a+"</div><div><div class='txtStyle2'>"+contents[0]+"</div><p>打包时段</p></div></li>";
                    }
                }
            })
            return returnStr;
        },
        sortBy1:function(data) {
            var arr = [];
            for (var i in data) {
                arr.push({'id':i,'sort':data[i].split(",")[4]});
            }
            return arr;
        },
        sortBy2:function(filed, rev, primer) {
            rev = (rev) ? -1 : 1;
            return function(a, b) {
                a = a[filed];
                b = b[filed];
                if (typeof(primer) != 'undefined') {
                    a = primer(a);
                    b = primer(b);
                }
                if (a <= b) {
                    return rev * -1;
                }
                if (a > b) {
                    return rev * 1;
                }
                return 0;
            }
        }
    };

    var bookIndex = 0;
    var bindDOM = function(){
        $('.book_site_con').on("click", 'li', function(){
        	//判断没有会员卡，只能选当天场地
        	/*var active = $(".active").attr("data-t");
        	var nowdate = Util.getTime();//获取当前日期，格式为yyyy-mm-dd
        	if(active!=nowdate){
    			if(Util.isEmptyObject(card)||card.cardnumber==null||card.cardnumber==""||card.cardnumber==undefined){
    				openConfirm("1","提示","如需提前预定场地，请办理会员卡，是否现在办理？",function(){
            			window.location.href='/TennisCenterInterface/weixin/home/apply_vip.html';
            		});
    				return;
    			}
    		}*/
           /* if(g.bNopay > 0){
                $(".book-noPaySprite").removeClass("hide");
                return;
            }*/
            var el = $(this);
            var curGid = el.attr('goodsid');
            if(el.attr('bookstatus')==3){
            	openAlertMsg("该场地已被禁用");
            	return;
            }
            if(el.hasClass('disable')){
            	var time = el.attr('time');
            	var nowtime = (new Date).getHours();
            	if(time<nowtime){
            		openAlertMsg("该场地已过期");
            	}else{
            		openAlertMsg("该场地已被预订");
            	}
                return;
            }
            if(el.hasClass('lock')){
            	openAlertMsg("场地已被他人锁定，逾期未支付将解除锁定。");
            	return;
            }
            if(el.hasClass('reserve')){
            	var userid = el.attr('userid');
            	var orderno = el.attr('orderno');//订单
            	var price = el.attr('price');//价格
            	var detailid = el.attr('detailid');
            	var bookstatus = el.attr('bookstatus');
            	var contentArr = (el.attr('content')).split(" ");
            	var vid = ($(".book_btnli").find("input").val()).replace(/(^\s*)|(\s*$)/g, "");//场馆id
        		var vname =($(".book_btnli").text()).replace(/(^\s*)|(\s*$)/g, "");//场馆
        		var date  =$(".active").attr("data-t");//日期
        		if(bookstatus==4){
        			location.href=encodeURI("/TennisCenterInterface/weixin/serve/pay_commo.html?orderNo="+orderno);
        		}else{
        			var Str ='<li><span>'+vname+'</span></li>'
    				+'<li><span>订单编号：</span><span>'+orderno+'</span></li>'
    				+'<li><span>场地名称：</span><span>'+contentArr[1]+'</span></li>'
    				+'<li><span>场地时间：</span><span><a>'+date+'</a><a>&nbsp;&nbsp;'+contentArr[0]+'</a> </span></li>'
    				+'<li><span>场地价格：</span><span>'+price+'元</span></li>';
		    		$(".book_layer .book_been_con").html(Str);
		    		var StrButton ='<a onclick="cancel()">取消</a><a onclick="refundOneTip(&apos;'+userid+'&apos;,&apos;'+orderno+'&apos;,&apos;'+detailid+'&apos;,&apos;'+price+'&apos;)">退款</a>'
		    		$(".book_layer .book_been_btn").html(StrButton);
		    		$(".book_layer").show(200);
        		}
        		
            	return;
            }
            //JS判断元素是否在数组内
            function contains(arr, obj) {  
            	var i = arr.length;  
            	while (i--) {  
            		if (arr[i] === obj) {  
            			return true;  
            		}  
            	}  
            	return false;  
            };
            //删除某元素
            function remove(val){
            	var index = selectIdArr.indexOf(val);
                if (index > -1) {
                	selectIdArr.splice(index, 1);
                }
            };
            //我的预定，相邻，不可预定
            $.each(eval($(".reserve")),function(i,data){
            	var falstId =((data.id).split("-"))[0];
            	$.each(eval(data.getAttribute("interval")),function(i,data){
                	if(selectIdArr.indexOf(falstId+"-"+data)<0){
                		selectIdArr.push(falstId+"-"+data)
                    }
                });
            });
            
            //获取选择id，以及不能选的id，进行比较
            var selectid =el.attr('id');//当前选择id
            var intervalStr =el.attr('interval');
            if(contains(selectIdArr,selectid)){
            	openAlertMsg("您选中的时间段不能连续预定");
            	return;
            }else if(selectCourts[curGid]!=undefined){
            	var falstId=(selectid.split("-"))[0];
            	$.each(eval(intervalStr),function(i,data){
                	remove(falstId+"-"+data)
                });
            }else{
            	var falstId=(selectid.split("-"))[0];
                $.each(eval(intervalStr),function(i,data){
                	if(selectIdArr.indexOf(falstId+"-"+data)<0){
                		selectIdArr.push(falstId+"-"+data)
                    }
                });
            }
            
            verticalArr = [];
            //exist in the array, delete it
            if(selectCourts[curGid]!=undefined){
                delete selectCourts[curGid];
                delete selectCourts2[curGid];
                el.addClass('available');
                el.removeClass('selected');
                //最小起订时间限制
                if(minHour>1){
                    var bindId = el.attr('bind_id'); //打包关联ID
                    var col = el.parent();
                    $('li[bind_id='+bindId+']').each(function(){
                        var goodsId = $(this).attr('goodsid');
                        delete selectCourts[goodsId];
                        delete selectCourts2[goodsId];
                        $(this).addClass('available');
                        $(this).removeClass('selected');
                    });
                }else{
                    //打包处理
                    var group_ids = el.attr('group_ids');

                    bindCourtArr = $.grep(bindCourtArr,function(item){
                        return (item.group_id != group_ids);
                    });

                    var group_arr = new Array();
                    if (group_ids != ''){
                        group_arr=group_ids.split(',');
                        for(var i=0;i<group_arr.length;i++){
                            var blid = "#block_"+group_arr[i];
                            if(!$(blid).hasClass('disable')  && !!$(blid)[0]){
                                delete selectCourts[group_arr[i]];
                                delete selectCourts2[group_arr[i]];
                                $(blid).addClass('available');
                                $(blid).removeClass('selected');
                            }
                        }
                    }
                }

                //打包处理
            } else {
                var selectNum=1;
                $.each(selectCourts, function(i,v){
                    selectNum++;
                });
                if (selectNum > timeSurplus){
                	var user = Util.get("user");
                	if(user!=undefined){
                		openAlertMsg("最多可选择"+timeSurplus+"块场地");
                	}else{
                		 openConfirm("1","提示","您还未登录，请登录以后选择场地。",function(){
                 			window.location.href='/TennisCenterInterface/weixin/myself/login.html';
                 		});
                		 return;
                	}
                    //openAlertMsg("您选择的场次数太多啦，请分两次下单结算哦。");
                    return;
                }
                bookIndex++;
                //最小起订时间限制
                if(minHour>1){
                    var currentIndex = el.index();
                    var enableDown = true;
                    var enableUp = true;
                    var col = el.parent();
                    //判断上下时段是否符合选择
                    for(var i=1;i<minHour;i++){
                        if(!col.find('li').eq(currentIndex+i).hasClass('available')) enableDown = false;
                        if(!col.find('li').eq(currentIndex-i).hasClass('available')) enableUp = false;
                    }
                    if(enableDown==false && enableUp==false){
                        openAlertMsg('不足两小时，无法预订');
                        return;
                    }
                    //自动选择相邻的场地
                    var bindId = 'bind_'+curGid;
                    el.attr('bind_id',bindId);//设置打包关联ID
                    for(i=1;i<minHour;i++){
                        var nextInd = enableDown==true ? currentIndex+i : currentIndex-i;//设置方向往上或往下
                        var nextTarget = col.find('li').eq(nextInd)
                        var goodsId = nextTarget.attr('goodsid');
                        selectCourts[goodsId] = parseInt(nextTarget.find("em").html());
                        selectCourts2[goodsId] = nextTarget.attr('course_content')+","+bookIndex;
                        nextTarget.removeClass('available');
                        nextTarget.addClass('selected');
                        nextTarget.attr('bind_id',bindId);
                    }

                }else{
                    //打包处理
                    var group_ids = el.attr('group_ids');
                    var group_arr = new Array();
                    if (group_ids != ''){
                        group_arr=group_ids.split(',');
                        var jsonObj = {group_id:group_ids,key:0};
                        var timeStr = "";
                        var bDisable = false;
                        var bindCourtNum = 0;
                        for(var i=0;i<group_arr.length;i++){
                            var blid = "#block_"+group_arr[i];
                            if(!$(blid).hasClass('disable') && !!$(blid)[0]){
                                selectCourts[group_arr[i]] = parseInt($(blid).find("em").html());
                                selectCourts2[group_arr[i]] = $(blid).attr('course_content')+","+bookIndex;
                                $(blid).removeClass('available');
                                $(blid).addClass('selected');
                                timeStr += (timeStr ? "," : "")+$(blid).attr('course_content').split(",")[3];
                                bindCourtNum++;
                            }else{
                                bDisable = true;
                            }
                        }
                        if(!bDisable || bindCourtNum >= 2){
                            jsonObj.timeLen = timeStr;
                            bindCourtArr.push(jsonObj);
                        }
                    }
                }

                //push it to the array
                selectCourts[curGid] = parseInt(el.find("em").html());
                selectCourts2[curGid] = el.attr('course_content')+","+bookIndex;
                el.removeClass('available');
                el.addClass('selected');
            }

            $(".book-list ul").each(function(i,item){
                $(".book-area li").eq(i).removeClass("active");
                var ul = $(item);
                $("li",ul).each(function(j,val){
                    var li = $(val);
                    if(li.hasClass("selected")){
                        if(!$(".book-area li").eq(i).hasClass("active")){
                            $(".book-area li").eq(i).addClass("active");
                        }
                        verticalArr.push(j);
                    }
                })
            })

            $(".book-time li").removeClass("active");
            for(var k in verticalArr){
                $(".book-time li").eq(verticalArr[k]).addClass("active");
                $(".book-time li").eq(verticalArr[k]+1).addClass("active");
            }
            utils.updateSum();
        });

        $('.day-wrap').on(touchType, '.J_selectDay', function(){
            var el = $(this);
            utils.getCourtData({
                datetime: parseInt(el.attr('time'))
            });
        });

        var submitLock = false;
        $('.J_submit').on("click", function(e){
            if(submitLock) return;

            e.stopPropagation();
            e.preventDefault();
           /* if(g.bNopay > 0){
                $(".book-noPaySprite").removeClass("hide");
                return;
            }*/
            var gids = [];
            $.each(selectCourts, function(i,v){
                gids.push(i);
            });
            if (gids.length > 0){
            	var user = Util.get("user");
            	if(user==undefined){
            		openConfirm("1","提示","您还未登录，请登录以后选择场地。",function(){
            			window.location.href='/TennisCenterInterface/weixin/myself/login.html';
            		});
            		return;
            	}else{
            		submitLock = true;
                    $("#loading").removeClass('hide');
                    //跳转确认页面
                    $('.J_goodsIds').val(gids.join(','));
                    $('.J_payConfirm').submit();
                    
                    var parkId  = decodeURI(Util.GetQueryString("ballcode"));//球类id
                    var parkType  = decodeURI(Util.GetQueryString("balltype"));//球类名称
            		var date  =$(".active").attr("data-t");//日期
            		//var date  = decodeURI(Util.GetQueryString("date"));//日期
            		
            		var vid = ($(".book_btnli").find("input").val()).replace(/(^\s*)|(\s*$)/g, "");//场馆id
            		var vname =($(".book_btnli").text()).replace(/(^\s*)|(\s*$)/g, "");//场馆
            		var fields = [];//场地
            		
            		var sortCourtArr = utils.sortBy1(selectCourts2);
            		sortCourtArr.forEach(function(item){
                        var contents = selectCourts2[item.id].split(",");
                        var field = new Object();
                        field.date= date;
                        field.time= contents[2];
                        field.parkid= contents[1];
                        field.parkname= contents[0];
                        fields.push(field);
                    })
                    if(vid!=""&&date!=""&&parkId!=""){
                    	location.href=encodeURI("/TennisCenterInterface/weixin/home/confirm_order.html?vid="+vid+"&vname="+vname+"&parkId="+parkId+"&parkType="+parkType+"&date="+date+"&fields="+ JSON.stringify(fields));
                    }
                    setTimeout(function(){
                        submitLock = false;
                        $("#loading").addClass('hide');
                    },5000)
            	}
                
            }else{
                openAlertMsg("请选择场次");
            }
        });

        var submitLock = false;
        $('.Q_submit').on("click", function(e){
            if(submitLock) return;
            $(".loding").show();
            e.stopPropagation();
            e.preventDefault();
            /*if(g.bNopay > 0){
                $(".book-noPaySprite").removeClass("hide");
                return;
            }*/
            var gids = [];
            $.each(selectCourts, function(i,v){
                gids.push(i);
            });
            if (gids.length > 0){
                submitLock = true;
                $("#loading").removeClass('hide');
                //跳转确认页面
                $('.J_goodsIds').val(gids.join(','));
                $('.J_payConfirm').submit();
                
        		var times = [];//时间段
        		
        		var sortCourtArr = utils.sortBy1(selectCourts2);
        		Util.clean("selectCourts2");
        		Util.set("selectCourts2", JSON.stringify(selectCourts2));
        		sortCourtArr.forEach(function(item){
                    var field = new Object();
                    var arr= item.id.split(",");
                    field.week= arr[0];
                    field.time= arr[1];
                    
                    times.push(field);
                })
        		//获取用户信息，缓存
				var data = Util.get("user");
			    var user = eval('(' + data + ')');
        		if(user!=undefined&&user!=null&&user!=""){
        			var userid = user.id;
        		}else{
        			var userid="-1";
        		}
        		var startdate=decodeURI(Util.GetQueryString("startdate"));
				var enddate=decodeURI(Util.GetQueryString("enddate"));
				var parkid=decodeURI(Util.GetQueryString("parkid"));
				var parkname=decodeURI(Util.GetQueryString("parkname"));
				var viname=decodeURI(Util.GetQueryString("viname"));
                $.post("/TennisCenterInterface/pmPark/getPreFixedList.action",
                		 {userid:userid,startTime:startdate,endTime:enddate,times:JSON.stringify(times),parkid:parkid,parkname:parkname,page:"1",row:"5"},
                		 function(data){
                			 $(".loding").hide();
                			 Util.clean("siteView");
                			 Util.set("siteView", JSON.stringify(data))
	            			 if(data.respCode=="1001"){
	            				 location.href=encodeURI("/TennisCenterInterface/weixin/myself/add_site.html?viname="+viname+"&parkname="+parkname+"&parkid="+parkid+"&startdate="+startdate+"&enddate="+enddate+"&times="+JSON.stringify(times));
	            			 }else{
	            				 openAlertMsg(data.respMsg);
	            			 }
				 });
                
                setTimeout(function(){
                    submitLock = false;
                    $("#loading").addClass('hide');
                },5000)
            }else{
            	$(".loding").hide();
                openAlertMsg("请选择场次");
            }
        });
        //$('.J_submit2').on(touchType, function(){
        //    var gids = [];
        //    $.each(selectCourts, function(i,v){
        //        gids.push(i);
        //    });
        //    var postData = {
        //        goods_ids: gids.join(',')
        //    }
        //    postData = typeof objMerge == 'function' ? objMerge(postData) : postData
        //    $.ajax({
        //        url: '/order/doconfirm',
        //        type: 'GET',
        //        dataType: 'JSON',
        //        cache: false,
        //        data: postData,
        //        success: function(res){
        //            var json=JSON.parse(res);
        //            if(json && json.code == 1){
        //                //todo: modify the url
        //                var url = '/order/pay?id='+json.data;
        //                // location.href = '/order/pay?id='+json.data;
        //                location.href = typeof urlAddParams == 'function' ? urlAddParams(url) : url;
        //            } else {
        //                utils.showError(res.msg || '支付出错，请稍后再试试');
        //            }
        //        },
        //        error: function(res){
        //            utils.showError(res.msg || '支付出错，请稍后再试试');
        //        }
        //    });
        //});
    };

    utils.getUrlParam();
    bindDOM();

    $('.J_selectDay.active').trigger(touchType);
})
