package com.ahsiu.apple6msg;

import java.io.Serializable;

public class SMSData  implements Serializable{
private String phoneNum;
	private String content;

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
