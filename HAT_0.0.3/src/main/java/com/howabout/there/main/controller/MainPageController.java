package com.howabout.there.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {
	
	
	@GetMapping("/")
	public String mainPage() {
		return "main";
	}
	@GetMapping("/mainPage")
	public String main() {
		return "main";
	}
}
