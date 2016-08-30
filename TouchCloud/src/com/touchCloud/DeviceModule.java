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
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;

import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.SensorsDao;
import com.touchCloud.pojo.Devices;
import com.touchCloud.pojo.Sensors;
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
	
	@At("/v1.0/device")
	@POST
	public void save() {
		HttpServletRequest req = Mvcs.getReq();
		String title = req.getParameter("title");
		String about = req.getParameter("about");
		String lat = req.getParameter("lat");
		String lng = req.getParameter("lng");
		
		Result result = new Result();
		if(StringUtils.isEmpty(title)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Devices device = new Devices();
		device.setTitle(title);
		device.setAbout(about);
		device.setLat(StringUtils.isEmpty(lat) == true ? 0.0 : Double.parseDouble(lat));
		device.setLng(StringUtils.isEmpty(lng) == true ? 0.0 : Double.parseDouble(lng));
		
		devicesDao.save(device);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/device/?")
	@GET
	public void getById(int deviceId) {
		Object data = null;
		if(deviceId == 0) {
			
			data = devicesDao.getAllDevices();
		}
		else {
			
			data = devicesDao.getById(deviceId);
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
	
	@At("/v1.0/device/?")
	@PUT
	public void update(int deviceId) {
		HttpServletRequest req = Mvcs.getReq();
		String title = req.getParameter("title");
		String about = req.getParameter("about");
		String lat = req.getParameter("lat");
		String lng = req.getParameter("lng");
		
		Result result = new Result();
		if(StringUtils.isEmpty(title)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Devices device = devicesDao.getById(deviceId);
		
		if(device == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		device.setTitle(title);
		
		if(about != null)
			device.setAbout(about);
		if(lat != null)
			device.setLat(Double.parseDouble(lat));
		if(lng != null) 
			device.setLng(Double.parseDouble(lng));
		
		devicesDao.update(device);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
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
		
		devicesDao.delete(device);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;
	}
	
	@At("/v1.0/user/bind")
	public void bind() {
		HttpServletRequest req = Mvcs.getReq();
		String sensorId = req.getParameter("sensorId");
		String deviceId = req.getParameter("deviceId");
		
		Result result = new Result();
		if(StringUtils.isEmpty(sensorId) || StringUtils.isEmpty(deviceId)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Sensors sensor = sensorsDao.getById(Integer.parseInt(sensorId));
		Devices device = devicesDao.getById(Integer.parseInt(deviceId));
		
		if(sensor == null || device == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		sensor.setDeviceId(device.getDeviceId());
		sensorsDao.update(sensor);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;
	}
}
