package com.touchCloud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;

import com.touchCloud.dao.SensorsDao;
import com.touchCloud.pojo.Sensors;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

/**
 * @author james
 * 传感器
 */
@IocBean
public class SensorModule extends CloudModule{
	
	Logger log = Logger.getLogger(SensorModule.class.getName());
	
	@Inject
	private SensorsDao sensorsDao;
	
	@At("/v1.0/sensor/?")
	@POST
	public void save(String json) {
		Result result = new Result();
		
		try {
			Sensors s = Json.fromJson(Sensors.class, json);
			
			if(s == null || StringUtils.isEmpty(s.getTitle())) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			sensorsDao.save(s);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "save Sensor exception data :" + json, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/?")
	@GET
	public void getById(int sensorId) {
		Sensors data = sensorsDao.getById(sensorId);
		
		if(data != null) {
			renderJson(Json.toJson(data), Mvcs.getResp());
			return;
		}
		
		Result result = new Result();
		result.setErrorCode(Constants.NO_DATA);
		result.setMsg(Constants.NO_DATA_MSG);
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;
	}
	
	@At("/v1.0/sensor")
	@GET
	public void getAll() {
		renderJson(Json.toJson(sensorsDao.getAllSensors()), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/?")
	@PUT
	public void update(String json) {
		Result result = new Result();
		
		try {
			Sensors s = Json.fromJson(Sensors.class, json);
			
			if(StringUtils.isEmpty(s.getTitle())) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			Sensors sensors = sensorsDao.getById(s.getSensorId());
			
			if(sensors == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			sensors.setTitle(s.getTitle());
			sensors.setType(s.getType());
			
			if(s.getAbout() != null)
				sensors.setAbout(s.getAbout());
			
			sensorsDao.update(sensors);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "update Sensor exception data :" + json, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/?")
	@DELETE
	public void delete(int sensorId) {
		
		Result result = new Result();
		if(sensorId == 0) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Sensors sensor = new Sensors();
		sensor.setSensorId(sensorId);
		
		sensorsDao.delete(sensor);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
}
