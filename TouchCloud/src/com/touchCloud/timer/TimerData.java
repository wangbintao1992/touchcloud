package com.touchCloud.timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.Session;

public class TimerData {
	
	private Session wSession;
	
	public TimerData(Session wSession) {
		super();
		this.wSession = wSession;
	}

	public Thread t;
	
	public void start() {
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				TimerTask task = new TimerTask() {  
		            @Override  
		            public void run() {  
		            	
		            }  
		        };  
		        Timer timer = new Timer();  
		        long delay = 0;  
		        long intevalPeriod = 1 * 1000;  
		        timer.scheduleAtFixedRate(task, delay, intevalPeriod);  
			}
		});
		
		t.start();
	}
	
	public void shutdown() {
		t.interrupt();
	}

	public Session getwSession() {
		return wSession;
	}

	public void setwSession(Session wSession) {
		this.wSession = wSession;
	}
}
