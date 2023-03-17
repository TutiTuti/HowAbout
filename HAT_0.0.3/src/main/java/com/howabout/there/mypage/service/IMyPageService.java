package com.howabout.there.mypage.service;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.howabout.there.mypage.dto.UserDto;
import com.howabout.there.sign.vo.UserVo;

public interface IMyPageService {
	
	//회원정보 수정. 
	public Map userUpdate(UserVo uservo, String usserNick);
	
	//비밀번호 확인
	public int pwCheck(ArrayList<JSONObject> json) throws ParseException ;
	
	//회원탈퇴
	public int withdrawal(ArrayList<JSONObject> json) throws ParseException;
	
	//내정보 가지고 오기
	public UserDto userListUp(String userId);
}
