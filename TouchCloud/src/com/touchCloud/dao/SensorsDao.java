package com.touchCloud.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.touchCloud.pojo.Sensors;

@IocBean
public class SensorsDao {
	
	@Inject
	private Dao dao;
	
	public Sensors getById(int sensorsId) {
		return dao.fetch(Sensors.class, sensorsId);
	}
	
	public List<Sensors> getSensorByDeviceId(int deviceId) {
		return dao.query(Sensors.class, Cnd.where("deviceId","=",deviceId));
	}
	
	public List<Sensors> getAllSensors() {
		return dao.query(Sensors.class, Cnd.where("1","=","1"));
	}
	
	public void update(Sensors sensors) {
		dao.update(sensors);
	}
	
	public void save(Sensors sensors) {
		dao.insert(sensors);
	}
	
	public void delete(Sensors sensors) {
		dao.delete(sensors);
	}
}
