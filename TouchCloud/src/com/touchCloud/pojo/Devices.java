package com.touchCloud.pojo;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author james
 * 设备表
 */
@Table("t_devices")
public class Devices {
	@Id
	@Column("device_id")
	private int deviceId;
	@Column("title")
	private String title;
	@Column("about")
	private String about;
	@Column("lat")
	private double lat;
	@Column("lng")
	private double lng;
	@Column("user_key") 
	private String userKey;
	@Column("type")
	private String type;
	@Column("count")
	private int count;
	@Column("name")
	private String deviceName;
	/*@Many(field = "sensorId", target = Sensors.class)
	private List<Sensors> sersors;*/
	
	
	public int getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
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

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/*public List<Sensors> getSersors() {
		return sersors;
	}

	public void setSersors(List<Sensors> sersors) {
		this.sersors = sersors;
	}*/
	
	
}
