<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="format-detection" content="telephone=no">
	<meta http-equiv="Cache-Control" content="no-cache">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
  	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="0">
<title>微信服务</title>
<style type="text/css">
*{margin: 0; padding: 0;}
</style>
<script type="text/javascript"
	src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
</head>
<body>
	 <iframe src="${toUrl }" frameborder="0" width="100%" id="iframepage"  marginheight="0" marginwidth="0" onload="changeFrameHeight()"></iframe>
</body>
<script type="text/javascript">
	 function changeFrameHeight(){
        var ifm= document.getElementById("iframepage");
        ifm.height=document.documentElement.clientHeight;
    }
    window.onresize=function(){
        changeFrameHeight();
    }
</script>
</html>