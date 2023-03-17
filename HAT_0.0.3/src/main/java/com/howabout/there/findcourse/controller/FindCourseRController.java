package com.howabout.there.findcourse.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.howabout.there.findcourse.JsoupReview;
import com.howabout.there.findcourse.service.FindCourseService;

@RestController
public class FindCourseRController {

   
   @Autowired
   FindCourseService courseService;

   // 식당 리스트 업
   @PostMapping("/findCourse/rest")
   public ArrayList<JSONObject> rest_list(@RequestBody ArrayList<JSONObject> inputData) throws ParseException {
	  System.out.println("/findCourse/rest : 식당 리스트 업");
      String category = "FD6"; // 음식점 카테고리
      ArrayList<JSONObject> result = courseService.listUp(inputData, category);
      return result;
   }
   
   // 카페 시스트 업
   @PostMapping("/findCourse/cafe")
   public ArrayList<JSONObject> cafe_list(@RequestBody ArrayList<JSONObject> inputData) throws ParseException {
      System.out.println("/fincCourse/cafe : 카페 리스트 업");
      String category = "CE7"; // 카페 카테고리
      ArrayList<JSONObject> result = courseService.listUp(inputData, category);
      return result;
      }
    

   //장소정보 이미지 경로 + 사용자 리뷰를 불러와 줌
   @PostMapping("/findCourse/getLocationInfo")
   public Map getLocationInfo(@RequestBody Map locationInfo) throws Exception {
	  System.out.println("/findCourse/getLocationInfo : 장소 리뷰 가지고 오기");
      Map returnList = courseService.getLocationList(locationInfo);
      return returnList;
   }
   
   
   

      
}