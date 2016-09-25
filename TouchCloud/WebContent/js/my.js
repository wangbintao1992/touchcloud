$(document).ready(function() {
		var map = new BMap.Map("container");          // 创建地图实例
		var mapType1 = new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});
		var mapType2 = new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_RIGHT});
		var overView = new BMap.OverviewMapControl();
		map.addControl(mapType1);          //2D图，卫星图
		map.addControl(mapType2);          //左上角，默认地图控件
		map.setCurrentCity("北京");        //由于有3D图，需要设置城市哦
		map.addControl(overView);     
	    var point = new BMap.Point(116.418261, 39.921984);
	    map.centerAndZoom(point, 15);             // 初始化地图，设置中心点坐标和地图级别
	    map.enableScrollWheelZoom(); // 允许滚轮缩放
	  	
		map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
		var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
		map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
		
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
		
		var points = null;
		
		$("button[name='btn']").each(function(){
			$(this).bind("click",function(){
				map.clearOverlays();
				
				var type = $(this).val();
				$.get("/TouchCloud/v1.0/device/heatMap?userKey=" + userKey + "&type=" + type,function(data){
					if(data == "") {
						alert("no data");
						return;
					}
					
					points = data;
					heatmapOverlay = new BMapLib.HeatmapOverlay({"radius":20});
					map.addOverlay(heatmapOverlay);
					heatmapOverlay.setDataSet({data:points,max:100});
					heatmapOverlay.show();
				});
			});
		}); 
		
		$("#md").bind("click",function() {
			map.clearOverlays();
			
			for(x in points) {
				var marker = new BMap.Marker(new BMap.Point(points[x].lng, points[x].lat)); // 创建点
				marker.id = points[x].deviceId;
				marker.addEventListener("click",attribute);
				map.addOverlay(marker);    //增加点
			}
		});
		
		function attribute(e){
			var p = e.target;
			window.location.href = "/TouchCloud/detail?param=" + p.id;
		}
}); 