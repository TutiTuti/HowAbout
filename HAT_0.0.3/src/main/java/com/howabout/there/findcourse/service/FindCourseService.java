package com.howabout.there.findcourse.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.howabout.there.findcourse.JsoupReview;
import com.howabout.there.findcourse.cafeDto;
import com.howabout.there.findcourse.kakaoApi;
import com.howabout.there.findcourse.myCourseDto;
import com.howabout.there.findcourse.restDto;
import com.howabout.there.findcourse.dao.IFindCourseDao;
import com.howabout.there.mypage.dto.UserDto;
import com.howabout.there.mypage.service.MyPageService;

@Service
public class FindCourseService implements IFindCourseService{

   @Autowired
   IFindCourseDao courseDao;
   
   @Autowired
   MyPageService myPageService;
   
   // 장소 정보 크롤링 or DB에서 꺼내기
   public Map getLocationList(Map locationName) throws ParseException, Exception{
	   
      String place_url = locationName.get("place_url").toString();
      
      Map returnList  = new HashMap<>();
      try {
    	  System.out.println("TTTTTTTTTTTTTTCCCCCCCCCCCCCC");
         returnList = courseDao.getReview(place_url);
      }catch(Exception e){
    	  System.out.println("22222222222222222222222222222222222222"+ returnList);
      }

      if(returnList==null) {
    	  JsoupReview jsoupCrawler = new JsoupReview();
         returnList= jsoupCrawler.getReview(place_url, 1);
         courseDao.inputCrawler(place_url,(String)returnList.get("imgUrl"),(String)returnList.get("storeTime"),(String)returnList.get("starpoint"),(String)returnList.get("review_1"),(String)returnList.get("review_2"),(String)returnList.get("review_3"),Timestamp.valueOf(LocalDateTime.now()));
      }
      
      return returnList;
   }
   
   // 카카오API사용 카페, 식당 리스트업   
   public ArrayList<JSONObject> listUp(ArrayList<JSONObject> inputData, String category) throws ParseException{
      kakaoApi kakao = new kakaoApi();
      ArrayList<JSONObject> returnData = kakao.listUp(inputData, category);
      return returnData;
   }
   
}
   
   
