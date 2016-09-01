package com.touchCloud;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonException;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;

import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.SensorsDao;
import com.touchCloud.pojo.Devices;
import com.touchCloud.pojo.Sensors;
import com.touchCloud.pojo.User;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

/**
 * @author james
 * 设备
 */
@IocBean
public class DeviceModule extends CloudModule{
	
	@Inject
	private DevicesDao devicesDao;
	
	@Inject
	private SensorsDao sensorsDao;
	
	Logger log = Logger.getLogger(DeviceModule.class.getName());
	
	@At("/v1.0/device/?")
	@POST
	public void save(String json) {
		Result result = new Result();
		
		try {
			Devices d = Json.fromJson(Devices.class, json);
			
			if(StringUtils.isEmpty(d.getTitle())) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			devicesDao.save(d);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (JsonException e) {
			log.log(Level.SEVERE, "save device exception data :" + json, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/device/?")
	@GET
	public void getById(int deviceId) {
		Devices d = devicesDao.getById(deviceId);
		
		if(d != null) {
			renderJson(Json.toJson(d), Mvcs.getResp());
			return;
		}
		
		Result result = new Result();
		result.setErrorCode(Constants.NO_DATA);
		result.setMsg(Constants.NO_DATA_MSG);
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;
	}
	
	@At("/v1.0/device")
	@GET
	public void getAllDevice() {
		renderJson(Json.toJson(devicesDao.getAllDevices()), Mvcs.getResp());
	}
	
	@At("/v1.0/device/?")
	@PUT
	public void update(String json) {
		Result result = new Result();
		try {
			Devices d = Json.fromJson(Devices.class, json);
			
			if(StringUtils.isEmpty(d.getTitle()) || d.getDeviceId() == 0) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			Devices device = devicesDao.getById(d.getDeviceId());
			
			if(device == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			device.setTitle(d.getTitle());
			
			if(d.getAbout() != null)
				device.setAbout(d.getAbout());
			if(d.getLat() != 0.0)
				device.setLat(d.getLat());
			if(d.getLng() != 0.0) 
				device.setLng(d.getLng());
			
			devicesDao.update(device);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "update device exception data :" + json, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/device/?")
	@DELETE
	public void delete(int deviceId) {

		Result result = new Result();
		if(deviceId == 0) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Devices device = new Devices();
		device.setDeviceId(deviceId);
		
		//digui
		devicesDao.delete(device);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/device/bind")
	@POST
	public void bind() {
		HttpServletRequest req = Mvcs.getReq();
		String[] sensorIds = req.getParameterValues("sensorId");
		String deviceId = req.getParameter("deviceId");
		
		Result result = new Result();
		if(sensorIds.length == 0 || StringUtils.isEmpty(deviceId)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Devices device = devicesDao.getById(Integer.parseInt(deviceId));
		
		if(device == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		for(String sId : sensorIds) {
			Sensors sensor = sensorsDao.getById(Integer.parseInt(sId));
			
			sensor.setDeviceId(device.getDeviceId());
			sensorsDao.update(sensor);
		}
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
}
