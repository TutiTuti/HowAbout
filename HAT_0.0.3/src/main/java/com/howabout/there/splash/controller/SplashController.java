package com.howabout.there.splash.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howabout.there.mypage.service.MyPageService;
import com.howabout.there.sign.vo.UserVo;
import com.howabout.there.splash.service.SplashService;

@RestController
public class SplashController {

	@Autowired
	MyPageService myPageService;
	
	@Autowired
	SplashService splash;
	
	@PostMapping("/splash/autoLogin")
	public Map refillToken(HttpServletRequest request) {
		System.out.println("SPLASH CONTROLLER");
		String token = request.getHeader("Authorization").substring(7);
		System.out.println(token);
		Map newToken  = splash.getToken(token);
		System.out.println("NEW TOKEN : "+ newToken.get("jwt"));
		return newToken;
	}
	
	@GetMapping("/splash/test")
	public Map rerr() {
		UserVo uservo = new UserVo();
		uservo.setU_nick("TTTAATT");
		uservo.setU_id("test1");
		uservo.setBirth("1998-02-20");
		uservo.setU_pw("abcde");
		uservo.setGender(1);
		Map user = myPageService.userUpdate(uservo, "Tutic");
		
		return user;
	}
	
	
}
