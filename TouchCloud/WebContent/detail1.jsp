<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setAttribute("id", request.getParameter("param")); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <script type="text/javascript" src="/TouchCloud/js/jquery-2.1.1.min.js"></script>
    <link rel="stylesheet" href="/TouchCloud/js/bootstrap.min.css">
    <script type="text/javascript" src="/TouchCloud/js/highcharts.js"></script>
<title>Insert title here</title>
<style type="text/css">
*{
	margin: 0;
	padding: 0;
}
.b{

}
.uinfo{
	height:150px;
}
.ucontent{
	height:50px;
	line-height: 50px;
}
.one{
	border-left: 2px #03B1ED solid;
}
.three{
	border-left: 2px #03B1ED solid;
	border-right: 2px #03B1ED solid;
}
.text1{
	margin-top:20px;
	text-align: center;
	border-bottom: 2px #A6AAE1 solid;
	text-align: center;
}
.sensor{
	margin-top:20px;
	background-color: #BEE6E8;
	height: 150px;
	border: 2px #A6AAE1 solid;
}
.sdetial{
	border:1px white solid;
	height: 146px;
	text-align: center;
}
.d{
	height:300px;
	border: 1px #A6AAE1 solid;
}
.p1{
	color: white;
	font-size: 20px;
	margin-top:10px;
	margin-bottom: 0px;
}
.p2{
	color: red;
	font-size: 25px;
	margin-bottom: 0px;
}
.p3{
	color: white;
	font-size: 20px;
	margin-bottom: 0px;
}
.p4{
	color: white;
	font-size: 20px;
	margin-bottom: 0px;
}
</style>
</head>
<body>
	<div class="container b">
		<div class="row ">
			<div class="col-md-2 b uinfo one">
				<span id="u2"></span>
			</div>
			<div class="col-md-7 b uinfo two">
				<div class="col-md-12 ucontent b" id=""><span>Device name:</span><span id="d1"></span></div>
				<div class="col-md-12 ucontent b" id=""><span>Device title:</span><span id="d2"></span></div>
				<div class="col-md-12 ucontent b" id=""><span>User signature:</span><span id="u1"></span></div>
			</div>
			<div class="col-md-3 b three uinfo">
				asdasd
			</div>
		</div>
		<div class="row text1">
			read-time 实时数据区
		</div>
		<div class="row sensor">
			<div class="col-md-12" id="sArr">
				<div class="col-md-2 sdetial">
				<p class="p1"><span>溶解氧</span></p>
				<p class="p2"><span>asd</span></p>
				<p class="p3"><span>mg/l</span></p>
				<p class="p4"><span>2014/1/2/1</span></p>
				</div>
			</div>
		</div>
		</div>
		<div class="row text1">
			histort-data 历史数据区
		</div>
		<div class="row" id="dArr">
			<dir class="d col-md-6" id="h1">

			</dir>
		</div>
	</div>
</body>
<script type="text/javascript">
	var id = "${requestScope.id}";
	var userKey = "${sessionScope.userKey}";
	$(document).ready(function(){
		$.get("/TouchCloud/v1.0/user/check?userKey=" + userKey,function(data) {
			if(data == "" || data.errorCode == "0001") {
				window.location.href = "/TouchCloud/login.jsp";
				return;
			}
			$("#u1").html(data.signature);
			$("#u2").html(data.name);
		});
		
		var device;
		
		$.get("/TouchCloud/v1.0/device/check?deviceId=" + id,function(data) {
			if(data == "" || data.errorCode == "0001") {
				window.location.href = "/TouchCloud/login.jsp";
				return;
			}
			
			device = data;
			
			$("#d1").html(data.deviceName);
			$("#d2").html(data.title);
		});
		
		var sids;
		$.get("/TouchCloud/v1.0/sensor/getSensors?deviceId=" + id,function(data) {
			sids = data;
		});

		setInterval(function(){
			$.get("/TouchCloud/getData?sensorId=" + sids,function(data){
				var arr = JSON.parse(data);
				for(var i = 0; i < arr.length; i ++) {
					$("#sArr").append("<div class='col-md-2 sdetial' ><p class='p1'>" + 
							"<span>" + arr[i].title + "</span></p><p class='p2'><span>" + arr[i].value + "</span></p><p class='p3'>" + 
							"<span>"+ arr[i].unit + "</span></p><p class='p4'><span>" + arr[i].time + "</span></p></div>");
				}
			});
		}, 3000)
		 Highcharts.setOptions({
	            global: {
	                useUTC: false
	            }
	        });
		$('#h1').highcharts({
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function () {
                        // set up the updating of the chart each second
                        var series = this.series[0];
                        setInterval(function () {
                           $.get("/TouchCloud/getChartData?sensorId=" + tmpSensorArr[0],function(data){
                        	    //var x = Date.parse(new Date(data.time));
                        	    //x = x / 1000;
                   				var x = (new Date()).getTime();
                        	    var y = parseInt(data.value);
                   				series.addPoint([x, y], true, true);
                   			});
                        }, 3000);
                    }
                }
            },
            title: {
                text: 'Live random data'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 30,
                minRange: 1000
            },
            yAxis: {
                title: {
                    text: 'Value'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function () {
                	console.log(this.x);
                	console.log(Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x));
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: 'sensor data point',
                data: (function () {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
                    for (i = -5; i <= 0; i += 1) {
                        data.push({
                            x: time + i * 1000,
                            y: 0
                        });
                    }
                    return data;
                }())
            }]
        });
	});
</script>
</html>