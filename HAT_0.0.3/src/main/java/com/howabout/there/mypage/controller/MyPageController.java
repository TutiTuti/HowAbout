package com.howabout.there.mypage.controller;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.howabout.there.mypage.dto.UserDto;
import com.howabout.there.mypage.service.MyPageService;

@Controller
public class MyPageController {
	
	@Autowired
	MyPageService myPageService;
	
	//이건 무슨 홈페이지 ?/
	@GetMapping("/myCourse/myCourse/myCourse")
	public String testtest() {
		return "login";
	}
	
	
	
	
	// 회원탈퇴 홈페이지
	@GetMapping("/myPage/withdrawal/webwithdrawal")
	public String webWithDrawalSite() {
		return "withdrawal";
	}
	
	// 마이페이지 홈페이지
	@GetMapping("/myPage/myInfo/webData")
	public String tokenWebMyInfo() {
		return "myInfo";
	}
	// 정보변경 홈페이지 
	@GetMapping("/myPage/myInfo/webDatasetting")
	public String tokenWebMyInfoSetting() {
		return "myInfoSetting";
	}
	
	
	
	
	
		
	
}
