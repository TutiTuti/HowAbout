package com.howabout.there.mycourse.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.howabout.there.mycourse.dto.MyCourseDto;
import com.howabout.there.mycourse.service.MyCourseService;
import com.howabout.there.token.JWTUtil;

import ch.qos.logback.classic.pattern.Util;

@RestController
public class MyCourseRController {
	
	@Autowired
	MyCourseService myCourseService;
	@Autowired
	JWTUtil jwtutil;
	
	// 내코스 리스트 업
	@PostMapping("/myCourse/myCourse")
		public ArrayList<MyCourseDto> getMyData(HttpServletRequest request) throws ParseException {
			System.out.println("/myCourse/myCourse : 내코스 리스트 업");
			String userNick = jwtutil.getUserNickFromToken(request.getHeader("Authorization").substring(7));
			ArrayList<MyCourseDto> result = myCourseService.getMyCourse(userNick);
			return result;
		}

	
	 //완성된 코스를 restaurant, cafe, course TABLE에 저장
	   @PostMapping("/myCourse/saveCourse")
	   public Map saveCourse(HttpServletRequest request, @RequestBody ArrayList<JSONObject> coursedata) throws Exception {
		   System.out.println("/myCourse/saveCourse : Table 임시 저장 ");
		   String tokenkey = request.getHeader("Authorization").substring(7);
		   Map answer = myCourseService.saveCourse(coursedata, tokenkey);
		   return answer;
	   }
	   
	   // 내코스 찜하기 
	   @PostMapping("/myCourse/courseDibs")
	   public int courseDibs(HttpServletRequest request, @RequestBody Map dibsData) {
		   System.out.println("/myCourse/courseDibs : 내코스 찜 ");
		   String token = request.getHeader("Authorization").substring(7);
		   dibsData.put("u_nick",jwtutil.getUserNickFromToken(token));
		   int SuccessDibs = myCourseService.courseDibs(dibsData);
		   return SuccessDibs;
	   }

}


