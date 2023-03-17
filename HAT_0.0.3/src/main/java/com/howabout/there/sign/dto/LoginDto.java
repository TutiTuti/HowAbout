package com.howabout.there.sign.dto;

public class LoginDto {
	
	
	private int success; 
	private String msg;			//SUCCESS // 아이디가존재하지않거나 //비번이 틀렸다. 
	private String token;
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	
	
	

}
