package com.howabout.there.popularcourse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class SetCourse {
	String genderQuery;
	String ageQuery;
	String location_doQuery;
	String location_siQuery;
	
	// gender, age, do, si 쿼리문을 작성
	public void setInfo(ArrayList<JSONObject> data) {
		JSONObject jsonData = data.get(0);
		setGender(jsonData.get("gender").toString());
		setAge(jsonData.get("age").toString());
		setLocation_do(jsonData.get("location_do").toString());
		setLocation_si(jsonData.get("location_si").toString());
		System.out.println("TEST 01 : " + jsonData.get("gender").toString());
		System.out.println("TEST 02 : " + jsonData.get("age").toString());
		System.out.println("TEST 03 : " + jsonData.get("location_do").toString());
		System.out.println("TEST 04 : " + jsonData.get("location_si").toString());
		System.out.println("TEST 1 : " + genderQuery);
		System.out.println("TEST 2 : " + ageQuery);
		System.out.println("TEST 3 : " + location_doQuery);
		System.out.println("TEST 4 : " + location_siQuery);
	}
	// gender query 작성
	public void setGender(String gender) {
		//gender : 모두 , 남자, 여자
		String returnGender;
		if(gender.equals("전체")) {
			returnGender="";
		}else if(gender.equals("남성")) {
			returnGender="AND gender=1";
		}else {
			returnGender="AND gender=0";
		}
		genderQuery = returnGender;
	}
	// age query 작성
	public void setAge(String age) {
		//age : 전체, 10대, 20대, 30대, 40대, 50대이상
		String returnAge = null;
		//현재시간
		LocalDate now = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        // 포맷 적용
        int formatedNow = Integer.parseInt(now.format(formatter));
		switch (age) {
			case "전체":{
				returnAge = ""; break;
			}
			case "10대": {
				returnAge= "AND YEAR(birth) >= "+(formatedNow-20); break;
			}
			case "20대":{
				returnAge= "AND YEAR(birth) < "+(formatedNow-20)+" AND YEAR(birth) >= "+(formatedNow-30); break;
			}
			case "30대":{
				returnAge= "AND YEAR(birth) < "+(formatedNow-30)+" AND YEAR(birth) >= "+(formatedNow-40); break;
			}
			case "40대":{
				returnAge= "AND YEAR(birth) < "+(formatedNow-40)+" AND YEAR(birth) >= "+(formatedNow-50); break;
			}
			case "50대이상":{
				returnAge= "AND YEAR(birth) < "+(formatedNow-50); break;
			}
		}
		ageQuery = returnAge;
	}
	
	// do query 작성
	public void setLocation_do(String location_do) {
		//location_do : 전체 , ex>충남,서울,
		String returnDo;
		if(location_do.equals("전체")){
			returnDo = "";
		}else {
			returnDo = "AND r_do = \""+location_do+"\"";
		}
		location_doQuery=returnDo;
	}
	// si query 작성
	public void setLocation_si(String location_si) {
		//location_si : 전체, ex>천안시,아산시,마포구
		String returnsi;
		if(location_si.equals("전체")){
			returnsi = "";
		}else {
			returnsi = "AND r_si = \""+location_si+"\"";
		}
		location_siQuery=returnsi;
	}
	
	 public String getAge (){return ageQuery ; }
	 public String getGender ( ){return genderQuery ; }
	 public String getLocation_do ( ){return location_doQuery ; }
	 public String getLocation_si ( ){return location_siQuery ; }
	
	
}
