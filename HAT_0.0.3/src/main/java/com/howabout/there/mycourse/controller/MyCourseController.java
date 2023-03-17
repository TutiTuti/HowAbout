package com.howabout.there.mycourse.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MyCourseController {
	@GetMapping("/test/kakao")
	public String kakaoapi() {
		return "kakaotest";
	}
	
	
	
	@GetMapping("/myCourse/myCourseSite")
	public String myCourse() {
		return "myFavoriteCourse";
	}
}
