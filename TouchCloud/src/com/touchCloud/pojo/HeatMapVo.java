package com.touchCloud.pojo;

public class HeatMapVo {
	
	private double lng;
	private double lat;
	private int count;
	private int deviceId;
	
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public HeatMapVo() {
		super();
	}
	public HeatMapVo(double lat, double lng, int count) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.count = count;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
