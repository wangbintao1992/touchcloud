package com.touchCloud.webSocket;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.nutz.ioc.loader.annotation.IocBean;

import com.touchCloud.timer.TimerData;

@ServerEndpoint(value = "/data", configurator = DeviceDetailWebSocketConf.class)
@IocBean
public class WebsocketTask extends Endpoint{
	
	public ConcurrentHashMap<String, TimerData> map = new ConcurrentHashMap<String,TimerData>();
	
	@Override
	public void onOpen(Session session, EndpointConfig arg1) {
		
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		
	}
	
	@OnMessage
    public void onMessage(Session session,String message){
		TimerData timerData = map.get(message);
		
		if(timerData == null) {
			map.put(message, new TimerData(session));
		}
	}
	
	//test
	public static void main(String[] args) {
		TimerTask task = new TimerTask() {  
            @Override  
            public void run() {  
                System.out.println("Hello !!!");  
            }  
        };  
        Timer timer = new Timer();  
        long delay = 0;  
        long intevalPeriod = 1 * 1000;  
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);  
	}
}
