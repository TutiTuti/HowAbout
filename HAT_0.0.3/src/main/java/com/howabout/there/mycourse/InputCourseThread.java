package com.howabout.there.mycourse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.howabout.there.findcourse.JsoupReview;
import com.howabout.there.findcourse.cafeDto;
import com.howabout.there.findcourse.restDto;
import com.howabout.there.findcourse.dao.IFindCourseDao;
import com.howabout.there.mycourse.dao.IMyCourseDao;
import com.howabout.there.mycourse.dto.InputCourseDto;
import com.howabout.there.token.JWTUtil;

public class InputCourseThread extends Thread{
	
	Map r_review;
	Map c_review;
	ArrayList<JSONObject> data;
	String token;
	
	@Autowired
	IFindCourseDao findcourseDao;
	@Autowired
	JWTUtil jwtutil;
	@Autowired
	IMyCourseDao courseDao;
	JsoupReview getImgCrawler = new JsoupReview();
	
	
	public InputCourseThread(Map r_review, Map c_review, ArrayList<JSONObject> data, String token) {
		this.r_review = r_review;
		this.c_review = c_review;
		this.data = data;
		this.token = token;
	}
	
	
	
	@Override
	public void run() {

		InputCourseDto myDto = new InputCourseDto();
		restDto rest = new restDto();
		cafeDto cafe = new cafeDto();
		
		
//		FIND REST REVIEW 
//      	Rest 리뷰가 비어있다면
	   if(r_review==null) {
		   System.out.println("Rest Review Null > Search");
		   try {
			r_review= getImgCrawler.getReview(data.get(0).get("place_url").toString(), 1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		   findcourseDao.inputCrawler(data.get(0).get("place_url").toString(),(String)r_review.get("imgUrl"),(String)r_review.get("storeTime"),
				   (String)r_review.get("starpoint"),(String)r_review.get("review_1"),
				   (String)r_review.get("review_2"),(String)r_review.get("review_3"),
				   Timestamp.valueOf(LocalDateTime.now()));
	   }
//       FIND CAFE REVIEW
	   System.out.println("CAFE URL : "+data.get(1).get("place_url").toString());
//       Cafe 리뷰가 비어있다면
       if(c_review==null) {
    	   System.out.println("Cafe Review Null > Search");
    	   try {
			c_review= getImgCrawler.getReview(data.get(1).get("place_url").toString(), 1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    	   findcourseDao.inputCrawler(data.get(1).get("place_url").toString(),(String)c_review.get("imgUrl"),(String)c_review.get("storeTime"),
    			   (String)c_review.get("starpoint"),(String)c_review.get("review_1"),
    			   (String)c_review.get("review_2"),(String)c_review.get("review_3"),
    			   Timestamp.valueOf(LocalDateTime.now()));
       }
      
   
      //내정보 세팅
      myDto.setWriter(jwtutil.getUserNickFromToken(token));
      myDto.setRest_name(data.get(0).get("place_name").toString());
      myDto.setRest_id(data.get(0).get("id").toString());
      myDto.setCafe_name(data.get(1).get("place_name").toString());
      myDto.setCafe_id(data.get(1).get("id").toString());
      myDto.setC_insert_time(Timestamp.valueOf(LocalDateTime.now()));
      myDto.setC_update_time(Timestamp.valueOf(LocalDateTime.now()));
      myDto.setGender(Integer.valueOf(jwtutil.getUserGenderFromToken(token)));
      myDto.setBirth(jwtutil.getUserBirthFromToken(token));
      myDto.setFlag(0);
      
      //식당정보 세팅   경도=x=lon, 위도=y=lat
      String[] restlocation = data.get(0).get("address_name").toString().split(" ");
      String[] restCat = data.get(0).get("category_name").toString().split(" ");
      rest.setR_name(data.get(0).get("place_name").toString());
      rest.setR_phone(data.get(0).get("phone").toString());
      rest.setR_lon(data.get(0).get("x").toString());
      rest.setR_lat(data.get(0).get("y").toString());
      rest.setR_do(restlocation[0]);
      rest.setR_si(restlocation[1]);
      rest.setR_gu(restlocation[2]);
      rest.setR_dong(restlocation[3]);
      rest.setR_cat(restCat[restCat.length-1]);
      rest.setR_id(data.get(0).get("id").toString());
      rest.setR_url(data.get(0).get("place_url").toString());
      rest.setR_insert_time(Timestamp.valueOf(LocalDateTime.now()));
      rest.setR_update_time(Timestamp.valueOf(LocalDateTime.now()));
      rest.setR_image_url(r_review.get("imgUrl").toString());
      
      //카페정보 세팅   경도=x=lon, 위도=y=lat
      String[] cafeLocation = data.get(1).get("address_name").toString().split(" ");
      String[] cafeCat = data.get(1).get("category_name").toString().split(" ");
      cafe.setC_name(data.get(1).get("place_name").toString());
      cafe.setC_phone(data.get(1).get("phone").toString());
      cafe.setC_lon(data.get(1).get("x").toString());
      cafe.setC_lat(data.get(1).get("y").toString());
      cafe.setC_do(cafeLocation[0]);
      cafe.setC_si(cafeLocation[1]);
      cafe.setC_gu(cafeLocation[2]);
      cafe.setC_dong(cafeLocation[3]);
      cafe.setC_cat(cafeCat[cafeCat.length-1]);
      cafe.setC_id(data.get(1).get("id").toString());
      cafe.setC_url(data.get(1).get("place_url").toString());
      cafe.setC_insert_time(Timestamp.valueOf(LocalDateTime.now()));
      cafe.setC_update_time(Timestamp.valueOf(LocalDateTime.now()));
      cafe.setC_image_url(c_review.get("imgUrl").toString());   
      
      
 
      //식당 정보가 업데이트  
      courseDao.restSave(rest);
      //카페정보가 업데이트
      courseDao.cafeSave(cafe);
     
      courseDao.courseSave(myDto);
       
    
      
	}

}
