package com.howabout.there.findcourse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;

import com.howabout.there.findcourse.dto.testDto;

public interface IFindCourseService {

//	//코스 찜하기 ( 회원 전용 ) 
//	public int courseDibs(Map mydata);

//	//코스 저장 
//	public Map saveCourse(@RequestBody ArrayList<JSONObject> courseData) throws ParseException, Exception;
	
	//코스 리스트 주기 
	public ArrayList<JSONObject> listUp(ArrayList<JSONObject> inputData, String category) throws ParseException;
	
	//코스 리뷰 
	public Map getLocationList(Map locationName) throws ParseException, Exception;
}
