package com.howabout.there.mycourse.service;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;

import com.howabout.there.mycourse.dto.MyCourseDto;

public interface IMyCourseService {
	
	// 내코스 가져오기 ( 회원 전용 )
	public ArrayList<MyCourseDto> getMyCourse (String myInfo)throws ParseException;;
	
	// 코스 찜하기 ( 회원 전용 ) 
	public int courseDibs(Map mydata);

	// 코스 저장 ( 회원 전용 ) 
	public Map saveCourse(@RequestBody ArrayList<JSONObject> courseData, String token) throws ParseException, Exception;

	
}
