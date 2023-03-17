package com.howabout.there.sign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.howabout.there.sign.service.mailSendService;



@Controller
public class SignInController {
	
	@Autowired
	mailSendService mail;
	
	
	@RequestMapping("/login/signIn")	
	public String loginPage() {
		return "login";
	}
	
	
	@GetMapping("/login/findId")
	public String findId() {
		return "findId";
	}
	
	@GetMapping("/login/findPw")
	public String findPw() {
		return "findPw";
	}
}
