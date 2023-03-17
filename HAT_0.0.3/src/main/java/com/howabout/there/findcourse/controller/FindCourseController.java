
package com.howabout.there.findcourse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FindCourseController {

	@GetMapping("/findCourse/webCourse")
	public String getMap() {
		return "findCourse";
	}
}
