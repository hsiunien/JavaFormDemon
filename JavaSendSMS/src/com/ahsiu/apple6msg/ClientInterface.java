package com.ahsiu.apple6msg;

import com.ahsiu.apple6msg.server.moduls.ClientModul;

public interface ClientInterface {
	public void  clientConnected(ClientModul clientModul);
	
	public void read(Msg msg);

	public void write(Msg msg);
}
