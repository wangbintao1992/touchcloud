package com.touchCloud;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.adaptor.PairAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.PUT;
import org.nutz.mvc.annotation.Param;

import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.SensorsDao;
import com.touchCloud.pojo.Devices;
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
	private DevicesDao deviceDao;
	
	@Inject
	private SensorsDao sensorsDao;
	
	@At("/v1.0/sensor/create")
	@AdaptBy(type=PairAdaptor.class)
	public void save(@Param("unit") String unit,@Param("title") String title, @Param("type") String type,@Param("type") int deviceId) {
		Result result = new Result();
		HttpServletRequest req = Mvcs.getReq();
		String about = req.getParameter("about");
		try {
			if(StringUtils.isEmpty(unit) || deviceId == 0 || StringUtils.isEmpty(title) || StringUtils.isEmpty(type)) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			Devices d = deviceDao.getById(deviceId);
			
			if(d == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			Sensors s = new Sensors();
			s.setUserKey(d.getUserKey());
			s.setAbout(about);
			s.setTitle(title);
			s.setUnit(unit);
			s.setType(Integer.parseInt(type));
			s.setDeviceId(deviceId);
			
			sensorsDao.save(s);
			renderJson(Json.toJson(s.getSensorId()), Mvcs.getResp());
			return;
		} catch (Exception e) {
			log.log(Level.SEVERE, "save Sensor exception data :", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/check")
	@AdaptBy(type=PairAdaptor.class)
	public void getById(@Param("sensorId")int sensorId) {
		//@wang last_updat last_data last_data_gen
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
	
	@At("/v1.0/sensor/enumerate")
	@GET
	public void getAll(@Param("deviceId") int deviceId) {
		//@wang last_updat last_data last_data_gen
		renderJson(Json.toJson(sensorsDao.getSensorByDeviceId(deviceId)), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/modify")
	@AdaptBy(type=PairAdaptor.class)
	public void update(@Param("userKey")String userKey,@Param("title")String title,@Param("sensorId") int sensorId,@Param("type") String type) {
		Result result = new Result();
		HttpServletRequest req = Mvcs.getReq();
		String about = req.getParameter("about");
		String newDeviceId = req.getParameter("newDeviceId");
		String unit = req.getParameter("unit");
		try {
			if(StringUtils.isEmpty(userKey) || StringUtils.isEmpty(title) || StringUtils.isEmpty(type) || sensorId == 0) {
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
			
			if(StringUtils.isNotEmpty(newDeviceId)) {
				sensors.setDeviceId(Integer.parseInt(newDeviceId));
			}
			
			sensors.setTitle(title);
			sensors.setType(Integer.parseInt(type));
			sensors.setAbout(about);
			if(StringUtils.isEmpty(unit)) {
				sensors.setUnit(unit);
			}
			sensorsDao.update(sensors);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "update Sensor exception data :", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/delete")
	@AdaptBy(type=PairAdaptor.class)
	public void delete(@Param("sensorId")int sensorId, @Param("userKey") String userKey) {
		
		Result result = new Result();
		if(sensorId == 0 || StringUtils.isEmpty(userKey)) {
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
		
		sensorsDao.delete(sensors);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/sensor/getSensors")
	@AdaptBy(type=PairAdaptor.class)
	public void getSensors(@Param("deviceId")int deviceId){
		List<Sensors> sensors = sensorsDao.getSensorByDeviceId(deviceId);
		
		int[] ids = new int[sensors.size()];
		for(int i = 0; i < sensors.size(); i ++) {
			ids[i] = sensors.get(i).getSensorId();
		}
		
		renderJson(Json.toJson(ids), Mvcs.getResp());
	}
	
	@At("/detail")
	public void dispathcer(@Param("param")int deviceId) {
		try {
			List<Sensors> sensors = sensorsDao.getSensorByDeviceId(deviceId);
			
			int size = sensors.size();
			
			String url = "/detail" + size + ".jsp";
			HttpServletRequest req = Mvcs.getReq();
			
			String ids = "";
			
			for(int i = 0; i < size; i ++) {
				
				ids += sensors.get(i).getSensorId();
				
				if(size != i) {
					ids += ",";
				}
			}
			
			req.setAttribute("tmpSensorId", ids);
			req.getRequestDispatcher(url).forward(req, Mvcs.getResp());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
