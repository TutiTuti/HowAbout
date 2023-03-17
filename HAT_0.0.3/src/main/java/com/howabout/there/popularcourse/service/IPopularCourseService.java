package com.howabout.there.popularcourse.service;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.howabout.there.popularcourse.dto.PopularCourseDto;

public interface IPopularCourseService {
	
	//카테고리 별로 인기코스 반환
	public ArrayList<PopularCourseDto> setCourseCat(ArrayList<JSONObject> setCourseInfo, String token);
	
	//DB내부 Do 리스트 반환
	public ArrayList<String> setDoList();
	
	//DB내부 Do기준 Si 리스트 반환
	public ArrayList<String> setSiList(String choiceDo);
	
}
