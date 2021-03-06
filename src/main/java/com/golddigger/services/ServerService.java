package com.golddigger.services;

import com.golddigger.server.GameServer;

public abstract class ServerService extends Service {
	protected GameServer server;
	
	public ServerService(int priority){
		super(priority);
	}
	
	public void setServer(GameServer server){
		this.server = server;
	}
}
