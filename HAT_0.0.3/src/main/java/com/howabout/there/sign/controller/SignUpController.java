package com.howabout.there.sign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.howabout.there.sign.service.SignUpService;

@Controller
@RequestMapping("/login/signUp")
public class SignUpController {
	
	@Autowired
	SignUpService signService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String signUpForm() {
		return "signup";	
	}
}
  