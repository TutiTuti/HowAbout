package com.howabout.there.mycourse.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howabout.there.findcourse.JsoupReview;
import com.howabout.there.findcourse.cafeDto;
import com.howabout.there.findcourse.myCourseDto;
import com.howabout.there.findcourse.restDto;
import com.howabout.there.findcourse.dao.IFindCourseDao;
import com.howabout.there.mycourse.InputCourseThread;
import com.howabout.there.mycourse.dao.IMyCourseDao;
import com.howabout.there.mycourse.dto.InputCourseDto;
import com.howabout.there.mycourse.dto.MyCourseDto;
import com.howabout.there.token.JWTUtil;

@Service
public class MyCourseService implements IMyCourseService {

   @Autowired
   IMyCourseDao courseDao;
   //코스가 있는지 찾기위해 Autowired
   @Autowired
   IFindCourseDao findcourseDao;
   
   @Autowired
   JWTUtil jwtutil;
   
   // 내코스 가지고 오기
   public ArrayList<MyCourseDto> getMyCourse (String myInfo)throws ParseException{
      ArrayList<MyCourseDto> myDataCourse = courseDao.getMyCourse(myInfo);
      return myDataCourse;
   }
   
   
      // 내코스 찜하기
   public int courseDibs(Map dibsData) {
         boolean courseSave=false;
         boolean booleanDibs=false;;
         
         //사용자가 선택한 코스가 DB에존재하지 않는 다면
         if(courseDao.exisCheck((String)dibsData.get("u_nick"),(String)dibsData.get("r_id"),(String)dibsData.get("c_id"))==0) {
            // 식당 카페가 일치하는 코스 정보를 한개 가져온다
            InputCourseDto course =  courseDao.findOneCourse((String)dibsData.get("r_id"),(String)dibsData.get("c_id"));
//            System.out.println("COURSE 찍어보기   "+course.getCafe_id());
            course.setWriter((String)dibsData.get("u_nick"));
            course.setC_insert_time(Timestamp.valueOf(LocalDateTime.now()));
            course.setC_update_time(Timestamp.valueOf(LocalDateTime.now()));
            course.setFlag(1);
            // DB에 내코스 저장
            courseSave = courseDao.oneCourseSave(course);
         //코스가 존재한다
         }else {
            // 현재 저장된 코스의 찜 상태를 확인한다.
            int flagType = courseDao.checkFlag((String)dibsData.get("u_nick"),(String)dibsData.get("r_id"),(String)dibsData.get("c_id"));
            if(flagType==1) {
               booleanDibs = courseDao.courseDibs((String)dibsData.get("u_nick"),(String)dibsData.get("r_id"),(String)dibsData.get("c_id"), 0,Timestamp.valueOf(LocalDateTime.now()));
            }else {
               booleanDibs = courseDao.courseDibs((String)dibsData.get("u_nick"),(String)dibsData.get("r_id"),(String)dibsData.get("c_id"), 1,Timestamp.valueOf(LocalDateTime.now()));
            }  
         } 
         //결과가 잘 처리됬는지 확인
         if(booleanDibs == true || courseSave == true ) {
            return 1;
         }else {
            return 0;
         }
      }
      
      // 연결 코스 저장 
   public Map saveCourse(ArrayList<JSONObject> data, String token) throws ParseException, Exception{
         
         Map returnMap = new HashMap<>(); 
         InputCourseDto myDto = new InputCourseDto();
         restDto rest = new restDto();
         cafeDto cafe = new cafeDto();
         JsoupReview getImgCrawler = new JsoupReview();
         String r_image_url = "";
         String c_image_url = "";
         Map r_review = new HashMap<>();
         Map c_review = new HashMap();
         
         
         //코스가 존재 하나?
         if(courseDao.exisCheck(jwtutil.getUserNickFromToken(token),data.get(0).get("id").toString(),data.get(1).get("id").toString())==0) {

                        
            r_review = findcourseDao.getReview(data.get(0).get("place_url").toString());
            c_review = findcourseDao.getReview(data.get(1).get("place_url").toString());
            //식당 카페 리뷰가 없다면 각 id만 테이블에 우선 저장
            if(courseDao.restCheck(data.get(0).get("id").toString())!=1) {               
               courseDao.inputRest(data.get(0).get("id").toString());
            }
            if(courseDao.cafeCheck(data.get(1).get("place_url").toString())!=1) {               
               courseDao.inputCafe(data.get(1).get("id").toString());
            }
            
            
//             식당 카페 table이 존재 한다면 먼저 코스 ID들과 writer, flag를 우선 저장
            int countRest = courseDao.restCheck(data.get(0).get("id").toString());
            int countCafe = courseDao.cafeCheck(data.get(1).get("id").toString());
            if(countRest==1 && countCafe==1) {
               courseDao.inputCourse(jwtutil.getUserNickFromToken(token),data.get(0).get("id").toString(),data.get(1).get("id").toString());
            }
            
//            //Thread 실험 1 
//            InputCourseThread t = new InputCourseThread(r_review, c_review, data, token);
//            t.run();
//            
            
            
//            FIND REST REVIEW 
            System.out.println("REST URL : "+data.get(0).get("place_url").toString());
//               Rest 리뷰가 비어있다면
            if(r_review==null) {
               System.out.println("Rest Review Null > Search");
               r_review= getImgCrawler.getReview(data.get(0).get("place_url").toString(), 1); 
               findcourseDao.inputCrawler(data.get(0).get("place_url").toString(),(String)r_review.get("imgUrl"),(String)r_review.get("storeTime"),
                     (String)r_review.get("starpoint"),(String)r_review.get("review_1"),
                     (String)r_review.get("review_2"),(String)r_review.get("review_3"),
                     Timestamp.valueOf(LocalDateTime.now()));
            }
//             FIND CAFE REVIEW
            System.out.println("CAFE URL : "+data.get(1).get("place_url").toString());
//             Cafe 리뷰가 비어있다면
             if(c_review==null) {
                System.out.println("Cafe Review Null > Search");
                c_review= getImgCrawler.getReview(data.get(1).get("place_url").toString(), 1);   
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
           
//            boolean resultSave = courseDao.courseSave(myDto);
            //코스가 저장됬으면
            if(courseDao.courseSave(myDto)==true) {
               returnMap.put("r_id", data.get(0).get("id").toString()) ;
               returnMap.put("c_id", data.get(1).get("id").toString()) ;
               returnMap.put("flag", (int)0);
            }else {
               returnMap.put("r_id", data.get(0).get("id").toString()) ;
                 returnMap.put("c_id", data.get(1).get("id").toString()) ; 
               returnMap.put("flag", (int)2);
            }
            
         }else {
               returnMap.put("r_id", data.get(0).get("id").toString()) ;
              returnMap.put("c_id", data.get(1).get("id").toString()) ;  
              returnMap.put("flag", (int)1);
         }


         return returnMap; 
      }   
}