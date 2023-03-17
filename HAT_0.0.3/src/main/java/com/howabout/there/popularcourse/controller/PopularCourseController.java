package com.howabout.there.popularcourse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PopularCourseController {
	
	
	@GetMapping("/popularCourse")
	public String asdsad() {
		return "ingi";
	}
}
