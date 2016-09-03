package com.touchCloud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.adaptor.PairAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.google.gson.Gson;
import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.UserDao;
import com.touchCloud.pojo.Devices;
import com.touchCloud.pojo.Sensors;
import com.touchCloud.pojo.User;
import com.touchCloud.vo.Constants;
import com.touchCloud.vo.Result;

import net.sf.json.JSONObject;

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
	
	@At("/v1.0/user/check")
	@AdaptBy(type=PairAdaptor.class)
	public void getUser(@Param("userKey") String userKey) {
		Result result = new Result();
		
		if(StringUtils.isEmpty(userKey)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		//password
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
	@AdaptBy(type=PairAdaptor.class)
	public void login(@Param("mobile") String mobile, @Param("password") String pwd) {
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
	@At("/v1.0/user/create")
	@AdaptBy(type=PairAdaptor.class)
	public void save(@Param("name") String name, @Param("sex") int sex, @Param("password") String password,
			@Param("mobile") String mobile) {
		HttpServletRequest req = Mvcs.getReq();
		
		String email = req.getParameter("email");
		String addr = req.getParameter("addr");
		String signature = req.getParameter("signature");
		
		Result result = new Result();
		try {

			if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)) {
				result.setErrorCode(Constants.PARAM_ERROR);
				result.setMsg(Constants.PARAM_MSG);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			
			if(userDao.checkduplicateUser(mobile)) {
				result.setErrorCode(Constants.DUPLICATE_CODE);
				result.setMsg(Constants.DUPLICATE_USER);
				renderJson(Json.toJson(result), Mvcs.getResp());
				return;
			}
			User u = new User();
			u.setAddr(addr);
			u.setName(name);
			u.setPassword(password);
			u.setMobile(mobile);
			u.setSex(sex);
			u.setEmail(email);
			u.setSignature(signature);
			
			u.setCreateDate(new Date());
			u.setUserKey(UUID.randomUUID().toString());
			
			userDao.save(u);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "save user exception data :", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	/*
	 * 更新
	 */
	@At("/v1.0/user/modify")
	@AdaptBy(type=PairAdaptor.class)
	public void update(@Param("userKey") String userKey) {
		HttpServletRequest req = Mvcs.getReq();
		String email = req.getParameter("email");
		String name = req.getParameter("name");
		String addr = req.getParameter("addr");
		String signature = req.getParameter("signature");
		String sex = req.getParameter("sex");
		
		Result result = new Result();

		try {
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
			
			if (!name.equals("")) {
				user.setName(name);
			}
			if (!email.equals("")) {
				user.setEmail(email);
			}
			if (!addr.equals("")) {
				user.setAddr(addr);
			}
			if (!signature.equals("")) {
				user.setSignature(signature);
			}
			if (!sex.equals("")) {
				user.setSex(Integer.parseInt(sex));
			}

			userDao.update(user);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "update user exception data :", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	/*
	 * 删除
	 */
	@At("/v1.0/user/delete")
	@AdaptBy(type=PairAdaptor.class)
	public void delete(@Param("userKey") String userKey) {
		Result result = new Result();
		try {
			
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
			
			userDao.delete(user);
			
			result.setErrorCode(Constants.SUCCESS_CODE);
			result.setMsg(Constants.SUCCESS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "user delete data:", e);
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
		}
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	@At("/v1.0/user/bind")
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
	
	@At("/v1.0/servicesList")
	public void getServices() {
		Map<String,String> addr = new HashMap<String,String>();
		
		HttpServletRequest request = Mvcs.getReq();

		String basePath = request.getScheme()+"://" +request.getServerName()+":"+request.getServerPort()+ request.getContextPath()+ "/";
		basePath += "v1.0";
		
		addr.put("createUser", basePath + "/user/create");
		addr.put("modifyUser", basePath + "/user/modify");
		addr.put("delteUser", basePath + "/user/delteUser");
		addr.put("bindUser", basePath + "/user/bind");
		addr.put("login", basePath + "/user/login");
		
		addr.put("createDevice", basePath + "/device/create");
		addr.put("modifyDevice", basePath + "/device/modify");
		addr.put("deleteDevice", basePath + "/device/delete");
		addr.put("bindDevice", basePath + "/device/bind");
		
		addr.put("createSensor", basePath + "/sensor/create");
		addr.put("modifySensor", basePath + "/sensor/modify");
		addr.put("deleteSensor", basePath + "/sensor/delete");
		addr.put("checkSensor", basePath + "/sensor/check");
		
		addr.put("createDataPonit", basePath + "/dataPoint/create");
		addr.put("modifyDataPonit", basePath + "/dataPoint/modify");
		addr.put("deleteDataPonit", basePath + "/dataPoint/delete");
		addr.put("checkDataPonit", basePath + "/dataPoint/check");
		addr.put("history", basePath + "/dataPoint/getHistory");
		
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
