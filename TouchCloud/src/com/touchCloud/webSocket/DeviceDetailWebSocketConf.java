package com.touchCloud.webSocket;

import javax.websocket.server.ServerEndpointConfig;

import org.nutz.mvc.Mvcs;

public class DeviceDetailWebSocketConf extends ServerEndpointConfig.Configurator{
	
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return Mvcs.ctx().getDefaultIoc().get(endpointClass);
    }
}
