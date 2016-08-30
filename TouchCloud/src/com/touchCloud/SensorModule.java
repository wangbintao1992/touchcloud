package com.touchCloud;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
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
	
	@At("/v1.0/sensor")
	@POST
	public void save() {
		HttpServletRequest req = Mvcs.getReq();
		String title = req.getParameter("title");
		String type = req.getParameter("type");
		String about = req.getParameter("about");
		
		Result result = new Result();
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(type)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Sensors sensor = new Sensors();
		sensor.setAbout(about);
		sensor.setTitle(title);
		sensor.setType(Integer.parseInt(type));
		
		sensorsDao.save(sensor);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/?")
	@POST
	public void getById(int sensorId) {
		Object data = null;
		if(sensorId == 0) {
			
			data = sensorsDao.getAllSensors();
		}
		else {
			
			data = sensorsDao.getById(sensorId);
		}
		
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
	
	@At("/v1.0/sensor/?")
	@PUT
	public void update(int sensorId) {
		HttpServletRequest req = Mvcs.getReq();
		String title = req.getParameter("title");
		String type = req.getParameter("type");
		String about = req.getParameter("about");
		
		Result result = new Result();
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(type)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Sensors sensors = sensorsDao.getById(sensorId);
		
		if(sensors == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		sensors.setTitle(title);
		sensors.setType(Integer.parseInt(type));
		
		if(about != null)
			sensors.setAbout(about);
		
		sensorsDao.update(sensors);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
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
		return;
	}
}
