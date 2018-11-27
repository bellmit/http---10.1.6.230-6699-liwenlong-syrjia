var __ctx="";


var aryCss__=["/TennisCenterInterface/css/owl.carousel.css",
              "/TennisCenterInterface/css/owl.theme.css",
              "/TennisCenterInterface/css/style.css",
              "/TennisCenterInterface/layer/need/layer.css"];

var aryJs__=["/TennisCenterInterface/js/angular.min.js",
             "/TennisCenterInterface/js/owl.carousel.js",
             "/TennisCenterInterface/js/touch.js",
             "/TennisCenterInterface/js/zepto.js",
             "/TennisCenterInterface/layer/layer.js",
             "/TennisCenterInterface/js/util.js",
             "/TennisCenterInterface/js/cookie.js",
             "/TennisCenterInterface/js/WxUtil.js",
             "/TennisCenterInterface/js/courtbook.js",
             "/TennisCenterInterface/assets/js/amazeui.min.js"];


/**
 * js引入时导入必须的css样式。
 */
for(var i=0;i<aryCss__.length;i++){
	var str="<link rel=\"stylesheet\" href=\""+__ctx + aryCss__[i] +"\">";
	document.write(str);
}

/**
 * js引入时导入必须的js文件。
 */
for(var i=0;i<aryJs__.length;i++){
	var str="<script src=\""+__ctx + aryJs__[i] +"\"></script>";
	document.write(str);
}

/**
 * 外部导入的js文件。
 */
function importJs(aryJs){
	for(var i=0;i<aryJs.length;i++){
		var str="<script src=\""+__ctx + aryJs[i] +"\"></script>";
		document.write(str);
	}
}
/**
 * 外部导入css。
 */
function importCss(aryCss){
	for(var i=0;i<aryCss.length;i++){
		var str="<link rel=\"stylesheet\" href=\""+__ctx + aryCss[i] +"\">";
		document.write(str);
	}
}
