package com.touchCloud;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

public class CloudModule {
	
	private static Logger log = Logger.getLogger(CloudModule.class.getName());
	
	public void renderJson(String data, HttpServletResponse respo) {
		try {
			respo.setCharacterEncoding("UTF-8");
			respo.setHeader("Content-type", "text/json;charset=UTF-8");
			respo.getWriter().write(data);
		} catch (IOException e) {
			log.log(Level.SEVERE, "render json exception param:" + data, e);
		}
	}
}
