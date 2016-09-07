package com.touchCloud;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonException;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.adaptor.PairAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.SensorsDao;
import com.touchCloud.dao.UserDao;
import com.touchCloud.pojo.Devices;
import com.touchCloud.pojo.HeatMapVo;
import com.touchCloud.pojo.Sensors;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

/**
 * @author james
 * 设备
 */
@IocBean
public class DeviceModule extends CloudModule{
	
	private UserDao userDao;
	
	@Inject
	private DevicesDao devicesDao;
	
	@Inject
	private SensorsDao sensorsDao;
	
	Logger log = Logger.getLogger(DeviceModule.class.getName());
	
	@At("/v1.0/device/create")
	@AdaptBy(type=PairAdaptor.class)
	public void save(@Param("title") String title) {
		Result result = new Result();
		HttpServletRequest req = Mvcs.getReq();
		
		String about = req.getParameter("about");
		String lat = req.getParameter("lat");
		String lng = req.getParameter("lng");
		String userKey = req.getParameter("userKey");
		try {
			
			if(StringUtils.isEmpty(title)) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			if(userDao.getUserById(userKey) == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			Devices d = new Devices();
			d.setAbout(about);
			d.setUserKey(userKey);
			d.setTitle(title);
			d.setLat(lat == null ? 0.0 : Double.parseDouble(lat));
			d.setLng(lng == null ? 0.0 : Double.parseDouble(lng));
			devicesDao.save(d);
			
			renderJson(d.getDeviceId() + "", Mvcs.getResp());
			return;
		} catch (JsonException e) {
			log.log(Level.SEVERE, "save device exception data :", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/device/check")
	@AdaptBy(type=PairAdaptor.class)
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
	
	@At("/v1.0/device/enumerate")
	public void getAllDevice(@Param("userKey") String userKey) {
		Result result = new Result();
		if(StringUtils.isEmpty(userKey)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		renderJson(Json.toJson(devicesDao.getDevicesByUserKey(userKey)), Mvcs.getResp());
	}
	
	@At("/v1.0/device/modify")
	@AdaptBy(type=PairAdaptor.class)
	public void update(@Param("title") String title,@Param("deviceId") int deviceId, @Param("userKey") String userKey) {
		Result result = new Result();
		HttpServletRequest req = Mvcs.getReq();
		
		String about = req.getParameter("about");
		String lat = req.getParameter("lat");
		String lng = req.getParameter("lng");
		String newUserKey = req.getParameter("newUserKey");
		try {
			if(deviceId == 0 || StringUtils.isEmpty(userKey)) {
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
			device.setAbout(about);
			device.setLat(lat == null ? 0.0 : Double.parseDouble(lat));
			device.setLng(lng == null ? 0.0 : Double.parseDouble(lng));
			
			if(StringUtils.isNotEmpty(newUserKey)) {
				
				if(userDao.getUserById(newUserKey) == null) {
					result.setErrorCode(Constants.PARAM_ERROR);
					result.setMsg(Constants.PARAM_MSG);
					return;
				}
				
				device.setUserKey(newUserKey);
			}
			
			devicesDao.update(device);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "update device exception data :", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/device/delete")
	@AdaptBy(type=PairAdaptor.class)
	public void delete(@Param("deviceId") int deviceId, @Param("userKey") String userkey) {

		Result result = new Result();
		if(deviceId == 0 || StringUtils.isEmpty(userkey)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		Devices d = devicesDao.getById(deviceId);
		
		if(d == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		devicesDao.delete(d);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(d.getUserKey(), Mvcs.getResp());
	}
	
	@At("/v1.0/device/bind")
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
	
	@At("/v1.0/device/heatMap")
	public void getHeadtMap(@Param("userKey") String userKey) {
		Result result = new Result();
		if(StringUtils.isEmpty(userKey)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		List<Devices> data = devicesDao.getDevicesByUserKey(userKey);
		
		List<HeatMapVo> map = new ArrayList<HeatMapVo>();
		
		for(Devices d : data) {
			HeatMapVo heatMap = new HeatMapVo();
			heatMap.setLat(d.getLat());
			heatMap.setLng(d.getLng());
			heatMap.setCount(50);
		
			map.add(heatMap);
		}
		
		renderJson(Json.toJson(map), Mvcs.getResp());
	}
}
