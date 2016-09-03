package com.touchCloud;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.adaptor.PairAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import com.touchCloud.dao.DataPointDao;
import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.SensorsDao;
import com.touchCloud.pojo.DataPoint;
import com.touchCloud.pojo.Page;
import com.touchCloud.pojo.Sensors;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

@IocBean
public class DatapointModule extends CloudModule{
	
	@Inject
	private DevicesDao deviceDao;
	
	@Inject
	private SensorsDao sensorsDao;
	
	@Inject
	private DataPointDao dpDao;
	
	@At("/v1.0/dataPoint/modify")
	@AdaptBy(type=PairAdaptor.class)
	public void update(String json) {
		Result result = new Result();
		
		try {
			DataPoint dp = Json.fromJson(DataPoint.class, json);
			if(dp == null || dp.getId() == 0 || dp.getDeviceId() == 0 || dp.getSensorId() == 0) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			DataPoint fdp = dpDao.getById(dp.getId());
			
			if(fdp == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			dpDao.update(fdp);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			e.printStackTrace();
		}
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/dataPoint/create")
	@AdaptBy(type=PairAdaptor.class)
	public void save(@Param("sensorId") int sensorId, @Param("timestamp") Date timestamp,@Param("type") String type,
			@Param("value") String value) {
		Result result = new Result();
		
		try {
			if(timestamp == null || sensorId == 0 || checkIsEmpty(type,value)) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			Sensors s = sensorsDao.getById(sensorId);
			
			if(s == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			//@wang data dulipcate
			DataPoint dp = new DataPoint();
			dp.setSensorId(sensorId);
			dp.setTimestamp(timestamp);
			dp.setValue(value);
			dp.setType(type);
			dp.setDeviceId(s.getDeviceId());
			dp.setSensorId(sensorId);
			
			dpDao.save(dp);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			e.printStackTrace();
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/dataPoint/check")
	@AdaptBy(type=PairAdaptor.class)
	public void getById(@Param("dataPointId") int id) {
		DataPoint dp = dpDao.getById(id);
		
		Result result = new Result();
		if(dp == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		renderJson(Json.toJson(dp), Mvcs.getResp());
	}
	
	@At("/v1.0/dataPoint/delete")
	@AdaptBy(type=PairAdaptor.class)
	public void delete(@Param("dataPointId") int id){
		Result result = new Result();
		try {
			
			DataPoint dp = dpDao.getById(id);
			
			if(dp == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			dpDao.delete(dp);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			e.printStackTrace();
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/dataPoint/getHistory")
	public void getHistory() {
		HttpServletRequest req = Mvcs.getReq();
		
		String deviceId = req.getParameter("deviceId");
		String sensorId = req.getParameter("sensorId");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		String interval = req.getParameter("interval");
		String pageNo = req.getParameter("pageNo");
		
		Result result = new Result();
		
		if(checkIsEmpty(start, end,interval, pageNo)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			return;
		}
		
		Page page = new Page();
		page.setDeviceId(Integer.parseInt(deviceId));
		page.setSensorId(Integer.parseInt(sensorId));
		page.setStart(start);
		page.setEnd(end);
		page.setInterval(interval);
		page.setPageNo(Integer.parseInt(pageNo));
		
		List<DataPoint> histoty = dpDao.getHistoty(page);
		
		renderJson(Json.toJson(histoty), Mvcs.getResp());
	}
	
	public boolean checkIsEmpty(String ...str) {
		for(String s : str) {
			if(StringUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}
}
