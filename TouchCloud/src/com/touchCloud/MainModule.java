package com.touchCloud;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonException;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.google.gson.Gson;
import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.UserDao;
import com.touchCloud.pojo.Devices;
import com.touchCloud.pojo.Sensors;
import com.touchCloud.pojo.User;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

/**
 * @author james
 *
 */
@IocBy(type=ComboIocProvider.class, args={
		"*js","conf.js",
		"*anno", "com.touchCloud"})
@IocBean
@Modules(scanPackage = true)
public class MainModule extends CloudModule{
	
	private static Logger log = Logger.getLogger(MainModule.class.getName());
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private DevicesDao deviceDao;
	
	@At("/v1.0/user/?")
	@GET
	public void getUser(String userKey) {
		Result result = new Result();
		if(StringUtils.isEmpty(userKey)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = userDao.getUserById(userKey);
		
		if(user == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		user.setPassword("");
		
		renderJson(Json.toJson(user), Mvcs.getResp());
	}
	/*
	 * 登录
	 */
	@At("/v1.0/user/login")
	@POST
	public void login() {
		HttpServletRequest req = Mvcs.getReq();
		String mobile = req.getParameter("mobile");
		String pwd = req.getParameter("password");
		
		Result result = new Result();
		if(StringUtils.isEmpty(mobile) && StringUtils.isEmpty(pwd)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = new User();
		user.setMobile(mobile);
		user.setPassword(pwd);
		
		User findUser = userDao.getUserByMobileAndPwd(user);
		
		JSONObject json = new JSONObject();

		if(findUser == null) {
			json.put("errorCode", Constants.NO_DATA);
			json.put("msg", Constants.NO_DATA_MSG);
			renderJson(json.toString(), Mvcs.getResp());
			return;
		}
		
		json.put("userkey", findUser.getUserKey());
		renderJson(json.toString(), Mvcs.getResp());
	}
	
	/*
	 * 新增
	 */
	@At("/v1.0/user/?")
	@POST
	public void save(String user) {
		Result result = new Result();
		System.out.println(Mvcs.getReq().getParameter("test"));
		try {
			User newUser = Json.fromJson(User.class, user);

			if(StringUtils.isEmpty(newUser.getMobile()) || StringUtils.isEmpty(newUser.getPassword())) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			if(userDao.checkduplicateUser(newUser.getMobile())) {
				result.setErrorCode(Constants.DUPLICATE_CODE);
				result.setMsg(Constants.DUPLICATE_USER);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			newUser.setCreateDate(new Date());
			newUser.setUserKey(UUID.randomUUID().toString());
			
			userDao.save(newUser);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "save user exception data :" + user, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	/*
	 * 更新
	 */
	@At("/v1.0/user/?")
	@PUT
	public void update(String userString) {
		Result result = new Result();

		try {
			User newUser = Json.fromJson(User.class, userString);
			
			if(StringUtils.isEmpty(newUser.getMobile()) && StringUtils.isEmpty(newUser.getPassword())) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			User user = userDao.getUserById(newUser.getUserKey());
			
			if(user == null) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			User mUser = userDao.getUserByMobile(newUser.getMobile());
			
			if(!mUser.getUserKey().equals(user.getUserKey())) {
				if(mUser.getMobile().equals(newUser.getPassword())) {
					result.setErrorCode(Constants.DUPLICATE_CODE);
					result.setMsg(Constants.DUPLICATE_USER);
					renderJson(Json.toJson(result), Mvcs.getResp());
					return;
				}
			}
			
			if(newUser.getName() != null)
				user.setName(newUser.getName());
			if(newUser.getPassword() != null)
				user.setPassword(newUser.getPassword());
			if(newUser.getMobile() != null)
				user.setMobile(newUser.getMobile());
			if(newUser.getEmail() != null)	
				user.setEmail(newUser.getEmail());
			user.setSex(newUser.getSex());
			if(newUser.getAddr() != null)
				user.setAddr(newUser.getAddr());
			if(newUser.getSignature() != null)
				user.setSignature(newUser.getSignature());
			
			userDao.update(user);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "update user exception data :" + userString, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	/*
	 * 删除
	 */
	@At("/v1.0/user/?")
	@DELETE
	public void delete(String json) {
		Result result = new Result();
		try {
			
			User newUser = Json.fromJson(User.class, json);
			
			if(StringUtils.isEmpty(newUser.getUserKey()) || StringUtils.isEmpty(newUser.getPassword()) || StringUtils.isEmpty(newUser.getMobile())) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			User findUser = userDao.getUserByMobileAndPwd(newUser);
			
			if(findUser == null || !findUser.getUserKey().equals(newUser.getUserKey())) {
				result.setErrorCode(Constants.NO_DATA);
				result.setMsg(Constants.NO_DATA_MSG);
				renderJson(json.toString(), Mvcs.getResp());
				return;
			}
			
			userDao.delete(findUser);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "user delete data:" + json, e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/user/bind")
	@POST
	public void bind() {
		HttpServletRequest req = Mvcs.getReq();
		String userKey = req.getParameter("userKey");
		String[] deviceIds = req.getParameterValues("deviceId");
		
		Result result = new Result();
		if(StringUtils.isEmpty(userKey) || deviceIds.length == 0) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = userDao.getUserById(userKey);
		
		if(user == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		for(String deviceIdStr : deviceIds) {
			Devices device = deviceDao.getById(Integer.parseInt(deviceIdStr));
			
			device.setUserKey(userKey);
			deviceDao.update(device);
		}
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/servicesList/?")
	@POST
	public void getServices(String userKey) {
		Map<String,String> addr = new HashMap<String,String>();
		
		HttpServletRequest request = Mvcs.getReq();

		String basePath = request.getScheme()+"://" +request.getServerName()+":"+request.getServerPort()+ request.getContextPath()+ "/";
		basePath += "v1.0";
		
		addr.put("user", basePath + "/user/");
		addr.put("bindUser", basePath + "/user/bind");
		addr.put("login", basePath + "/user/login");
		
		addr.put("device", basePath + "/device/");
		addr.put("bindDevice", basePath + "/device/bind");
		
		addr.put("sensor", basePath + "/sensor/");
		addr.put("bindSensor", basePath + "/sensor/bind");
		
		renderJson(Json.toJson(addr), Mvcs.getResp());
	}
	
	public static void main(String[] args) throws Exception {
		User u = new User();
		u.setUserKey("2c6429be-2de8-4d4d-abac-37d2ec57c461");
		u.setMobile("zxcasds");
		u.setPassword("tessssss");
		System.out.println(new Gson().toJson(u));
		
		Devices d = new Devices();
		d.setTitle("asd");
		System.out.println(new Gson().toJson(d));
		
		Sensors s = new Sensors();
		s.setTitle("zzzzzzzzzz");
		System.out.println(new Gson().toJson(s));
		
	}
}
