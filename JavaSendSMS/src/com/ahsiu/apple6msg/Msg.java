package com.ahsiu.apple6msg;

import java.io.Serializable;

import com.ahsiu.apple6msg.server.Response;

public class Msg implements Serializable {
	private int type;
	private Response response;
	private String data;

	public Msg() {
	}

	public Msg(int type, String msg) {
		this.type = type;
		this.data = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}	 

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response data) {
		this.response = data;
	}
}
