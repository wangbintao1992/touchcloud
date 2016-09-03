package com.touchCloud.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.touchCloud.pojo.Devices;

@IocBean
public class DevicesDao {
	
	@Inject
	private Dao dao;
	
	public List<Devices> getDevicesByUserKey(String userKey) {
		return dao.query(Devices.class, Cnd.where("userKey", " = ", userKey));
	}
	
	public List<Devices> getAllDevices() {
		return dao.query(Devices.class, Cnd.where("1", " = ", "1"));
	}
	
	public Devices getById(int deviceId) {
		return dao.fetch(Devices.class, deviceId);
	}
	
	
	public void save(Devices devices) {
		dao.insert(devices);
	}
	
	public void delete(Devices devices) {
		dao.delete(devices);
	}
	
	public void update(Devices devices) {
		dao.update(devices);
	}
}
