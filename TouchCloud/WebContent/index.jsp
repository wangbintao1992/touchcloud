<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style>
.btn1{
	position: fixed;
	top: 100px;
	right: 50px;
}
.btn2{
	position: fixed;
	top: 135px;
	right: 50px;
}
.btn3{
	position: fixed;
	top: 170px;
	right: 50px;
}
.btn4{
	position: fixed;
	top: 205px;
	right: 50px;
}
.btn5{
	position: fixed;
	top: 240px;
	right: 50px;
}
</style>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=NHlwpVgqvW91kETp9zyvQqi0nEfesEcC"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script>
    <script type="text/javascript" src="/TouchCloud/js/jquery-2.1.1.min.js"></script>
    <link rel="stylesheet" href="/TouchCloud/js/bootstrap.min.css">
    <title></title>
    <style type="text/css">
		ul,li{list-style: none;margin:0;padding:0;float:left;}
		html{height:100%}
		body{height:100%;margin:0px;padding:0px;font-family:"微软雅黑";}
		#container{height:100%;width:100%;}
		#r-result{width:100%;}
    </style>	
</head>
<body>
	<div id="container"></div>
	<button name="btn" value="1" class="btn1 btn btn-primary">水&nbsp;&nbsp;&nbsp;质</button>
	<button name="btn" value="2" class="btn2 btn btn-success">大&nbsp;&nbsp;&nbsp;气</button>
	<button name="btn" value="3" class="btn3 btn btn-info">土&nbsp;&nbsp;&nbsp;壤</button>
	<button name="btn" value="4" class="btn4 btn btn-warning">全&nbsp;&nbsp;&nbsp;部</button>
	<button name="btn" value="" id="md" class="btn5 btn btn-danger">麻点图</button>
	<input type="hidden" id="userKey" value="${sessionScope.userKey}"/>
</body>
</html>
<script type="text/javascript" src="/TouchCloud/js/my.js">
</script>