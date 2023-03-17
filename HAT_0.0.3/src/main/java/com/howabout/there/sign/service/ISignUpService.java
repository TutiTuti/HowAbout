package com.howabout.there.sign.service;

import java.util.Map;

import org.json.simple.parser.ParseException;

import com.howabout.there.sign.dto.SignUpDto;

public interface ISignUpService {
	
	//DB아이디 등록 
	public int signUp(SignUpDto signData) throws ParseException;
	
	//ID중복 체크 
	public int idCheck(Map signData) throws ParseException;

	//NICK중복 체크 
	public int nickCheck(Map signData) throws ParseException;
	
	//EMAIL중복 체크 
	public int emailCheck(Map signData) throws ParseException;
	
	
	public int emailCheckAuth(Map emailData);
	
	public int authCheck(Map authCheck);
}
