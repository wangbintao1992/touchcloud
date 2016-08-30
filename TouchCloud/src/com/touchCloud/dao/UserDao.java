package com.touchCloud.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.touchCloud.pojo.User;

@IocBean
public class UserDao {
	
	@Inject
	private Dao dao;
	
	public void save(User user) {
		dao.insert(user);
	}
	
	public void update(User user) {
		dao.update(user);
	}
	
	public User getUserByMobileAndPwd(User user) {
		Cnd cnd = Cnd.where("mobile", " = ", user.getMobile()).and("password", " = ", user.getPassword());
		List<User> query = dao.query(User.class, cnd);
		
		if(query != null && query.size() == 1) {
			
			return query.get(0);
		}
		
		return null;
	}
	
	public User getUserById(int userId) {
		return dao.fetch(User.class, userId);
	}
	
	public void delete(User user) {
		dao.delete(user);
	}
}
