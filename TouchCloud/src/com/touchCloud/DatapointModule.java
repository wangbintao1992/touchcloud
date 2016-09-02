package com.touchCloud;


import java.util.List;

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

import com.touchCloud.dao.DataPointDao;
import com.touchCloud.pojo.DataPoint;
import com.touchCloud.pojo.Page;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

@IocBean
public class DatapointModule extends CloudModule{
	
	@Inject
	private DataPointDao dpDao;
	
	@At("/v1.0/dataPoint/?")
	@PUT
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
	
	@At("/v1.0/dataPoint/?")
	@POST
	public void save(String json) {
		Result result = new Result();
		
		try {
			DataPoint dp = Json.fromJson(DataPoint.class, json);
			if(dp == null || dp.getDeviceId() == 0 || dp.getSensorId() == 0) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
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
	
	@At("/v1.0/dataPoint/?")
	@GET
	public void getById(int id) {
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
	
	@At("/v1.0/delete/?")
	@DELETE
	public void delete(int dpId){
		Result result = new Result();
		try {
			
			DataPoint dp = dpDao.getById(dpId);
			
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
