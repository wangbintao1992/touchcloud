$(document).ready(function() {
		var map = new BMap.Map("container");          // 创建地图实例
		var mapType1 = new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});
		var mapType2 = new BMap.MapTypeControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT});
		var overView = new BMap.OverviewMapControl();
		map.addControl(mapType1);          //2D图，卫星图
		map.addControl(mapType2);          //左上角，默认地图控件
		map.setCurrentCity("北京");        //由于有3D图，需要设置城市哦
		map.addControl(overView);     
	    var point = new BMap.Point(116.418261, 39.921984);
	    map.centerAndZoom(point, 15);             // 初始化地图，设置中心点坐标和地图级别
	    map.enableScrollWheelZoom(); // 允许滚轮缩放
	  	
	    heatmapOverlay = new BMapLib.HeatmapOverlay({"radius":20});
		heatmapOverlay.show();
		map.addOverlay(heatmapOverlay);
		
		map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
		var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
		var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角
		map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
		map.addControl(top_right_navigation);    
		
		  // 添加定位控件
		  var geolocationControl = new BMap.GeolocationControl();
		  geolocationControl.addEventListener("locationSuccess", function(e){
		    // 定位成功事件
		    var address = '';
		    address += e.addressComponent.province;
		    address += e.addressComponent.city;
		    address += e.addressComponent.district;
		    address += e.addressComponent.street;
		    address += e.addressComponent.streetNumber;
		    alert("当前定位地址为：" + address);
		  });
		  geolocationControl.addEventListener("locationError",function(e){
		    // 定位失败事件
		    alert(e.message);
		  });
		  map.addControl(geolocationControl);
		var userKey = $("#userKey").val();
		
		var points;
		
		$("#water").bind("click",function() {
			$.get("/TouchCloud/v1.0/device/heatMap?userKey=" + userKey,function(data){
				if(data == "") {
					alert("no data");
					return;
				}
				
				points =[
				             {"lng":116.418261,"lat":39.921984,"count":50},
				             {"lng":116.423332,"lat":39.916532,"count":51},
				             {"lng":116.419787,"lat":39.930658,"count":15},
				             {"lng":116.418455,"lat":39.920921,"count":40},
				             {"lng":116.418843,"lat":39.915516,"count":100},
				             {"lng":116.42546,"lat":39.918503,"count":6},
				             {"lng":116.423289,"lat":39.919989,"count":18},
				             {"lng":116.418162,"lat":39.915051,"count":80},
				             {"lng":116.422039,"lat":39.91782,"count":11},
				             {"lng":116.41387,"lat":39.917253,"count":7},
				             {"lng":116.41773,"lat":39.919426,"count":42},
				             {"lng":116.421107,"lat":39.916445,"count":4}];

				points = data;
				heatmapOverlay.setDataSet({data:points,max:100});
			});
		});
}); 