 package com.howabout.there.popularcourse.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.howabout.there.findcourse.dto.testDto;
import com.howabout.there.popularcourse.dto.PopularCourseDto;
import com.howabout.there.popularcourse.service.PopularCourseService;



@RestController
public class PopularCourseRController {
	
	@Autowired
	PopularCourseService popularService;
	
	//카테고리 별로 리스트 반환
	@PostMapping("/popularCourse/getCatCourse")
	public ArrayList<PopularCourseDto> getCatCourse(HttpServletRequest request ,@RequestBody ArrayList<JSONObject> setCatData){
		String token = "NO";
//		String token = request.getHeader("Authorization").indexOf("null")>0? null:request.getHeader("Authorization");
		if(request.getHeader("Authorization")!=null) {
			token=request.getHeader("Authorization").substring(7);
		}
		ArrayList<PopularCourseDto> returnquery = popularService.setCourseCat(setCatData, token);
		return returnquery;
	}
	
	//DB내부 Do 반환
	@GetMapping("/popularCourse/getDo")
	public ArrayList<String> getDo(){
		ArrayList<String> returnDo = popularService.setDoList();
		return returnDo;
	}
	
	//DB내부 Do를 기준으로 Si 반환
	@PostMapping("/popularCourse/getSi")
	public ArrayList<String> getSi(@RequestBody String choiceDo){
		ArrayList<String> returnDo = popularService.setSiList(choiceDo);
		return returnDo;
	}
	
	
	

}
