package com.touchCloud;

import java.util.Date;
import java.util.UUID;
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
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.touchCloud.dao.DevicesDao;
import com.touchCloud.dao.UserDao;
import com.touchCloud.pojo.Devices;
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
	
	@At("/v1.0/user/?")
	@GET
	public void getUser(int userId) {
		Result result = new Result();
		if(userId == 0) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = userDao.getUserById(userId);
		
		if(user == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
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
		
		json.put("userId", findUser.getUserId());
		json.put("userkey", findUser.getUserKey());
		renderJson(json.toString(), Mvcs.getResp());
	}
	
	/*
	 * 新增
	 */
	@At("/v1.0/user")
	@POST
	public void save() {
		HttpServletRequest req = Mvcs.getReq();
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String mobile = req.getParameter("mobile");
		String email = req.getParameter("email");
		String sex = req.getParameter("sex");
		String addr = req.getParameter("addr");
		String signature = req.getParameter("signature");
		String icon = req.getParameter("icon");
		
		Result result = new Result();
		//@mobile 重复
		if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setEmail(email);
		user.setSex(sex != null ? Integer.parseInt(sex) : 1);
		user.setAddr(addr);
		user.setSignature(signature);
		user.setIcon(icon);
		user.setCreateDate(new Date());
		user.setUserKey(UUID.randomUUID().toString());
		
		userDao.save(user);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
	}
	
	/*
	 * 更新
	 */
	@At("/v1.0/user/?")
	@POST
	public void update(int userId) {
		HttpServletRequest req = Mvcs.getReq();
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String mobile = req.getParameter("mobile");
		String email = req.getParameter("email");
		String sex = req.getParameter("sex");
		String addr = req.getParameter("addr");
		String signature = req.getParameter("signature");
		String icon = req.getParameter("icon");
		
		Result result = new Result();
		if(StringUtils.isEmpty(mobile) && StringUtils.isEmpty(password) && (userId != 0)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = userDao.getUserById(userId);
		
		JSONObject json = new JSONObject();
		if(user == null) {
			json.put("errorCode", Constants.NO_DATA);
			json.put("msg", Constants.NO_DATA_MSG);
			renderJson(json.toString(), Mvcs.getResp());
			return;
		}
		
		if(name != null)
			user.setName(name);
		if(password != null)
			user.setPassword(password);
		if(mobile != null)
			user.setMobile(mobile);
		if(email != null)	
			user.setEmail(email);
		if(sex != null)	
			user.setSex(Integer.parseInt(sex));
		if(addr != null)
			user.setAddr(addr);
		if(signature != null)
			user.setSignature(signature);
		if(icon != null)
			user.setIcon(icon);
		
		userDao.update(user);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;
	}
	
	/*
	 * 删除
	 */
	@At("/v1.0/user/*")
	@DELETE
	public void delete(String userKey,String mobile, String password) {
		HttpServletRequest req = Mvcs.getReq();
		System.out.println(userKey + " : " + mobile + " : " + password );
		/*String mobile = req.getParameter("mobile");
		String password = req.getParameter("password");*/
		
	/*	
		Result result = new Result();
		if(StringUtils.isEmpty(userKey) || StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = new User();
		user.setMobile(mobile);
		user.setPassword(password);
		
		User findUser = userDao.getUserByMobileAndPwd(user);
		
		JSONObject json = new JSONObject();

		if(findUser == null || !findUser.getUserKey().equals(userKey)) {
			json.put("errorCode", Constants.NO_DATA);
			json.put("msg", Constants.NO_DATA_MSG);
			renderJson(json.toString(), Mvcs.getResp());
			return;
		}
		
		userDao.delete(findUser);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;*/
	}
	
	@At("/v1.0/user/bind")
	public void bind() {
		HttpServletRequest req = Mvcs.getReq();
		String userId = req.getParameter("userId");
		String deviceId = req.getParameter("deviceId");
		
		Result result = new Result();
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(deviceId)) {
			result.setErrorCode(Constants.PARAM_ERROR);
			result.setMsg(Constants.PARAM_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		User user = userDao.getUserById(Integer.parseInt(userId));
		Devices device = deviceDao.getById(Integer.parseInt(deviceId));
		
		if(user == null || device == null) {
			result.setErrorCode(Constants.NO_DATA);
			result.setMsg(Constants.NO_DATA_MSG);
			renderJson(Json.toJson(result), Mvcs.getResp());
			return;
		}
		
		device.setUserId(user.getUserId());
		deviceDao.update(device);
		
		result.setErrorCode(Constants.SUCCESS_CODE);
		result.setMsg(Constants.SUCCESS);
		
		renderJson(Json.toJson(result), Mvcs.getResp());
		return;
	}
}
