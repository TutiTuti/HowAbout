package com.howabout.there.findcourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class JsoupReview {
   
   public Map getReview(String locationUrl, int num) throws InterruptedException {
         
         String url = locationUrl;
         int getLocationData = num;
         
         
         Map returnList = new HashMap<>();
         String id = "webdriver.chrome.driver";
         String path = "C:\\googledriver\\chromedriver_win32\\chromedriver.exe";
         
            //크롬 드라이버 사용하기 위해 로딩
         System.setProperty(id, path);
         
         //크롬 브라우저를 열어줄 때 사용할 옵션들 설정할 수 있는 객체 만들기
         ChromeOptions options = new ChromeOptions();
         options.addArguments("--headless");
         options.addArguments("--no-sandbox");
         //아까 설정해준 옵션들을 기준으로 크롬 드라이버 실행하기
         
         WebDriver driver = new ChromeDriver(options);
         //WebDriverWait driverWait = new WebDriverWait(driver, setTime);
         //실행된 드라이버로 주어진 url 접속시키기
         driver.get(url);
         //실행할 땐, 자바어플리케이션으로 실행하기
         

         //url이 실행이 아래 코드보다 느리게 실행될 수도 있으니까 Thread 써주기
         WebElement imgClass = null;
         try {
            System.out.println("FIRST CATCH @@@@@@@@@@@@@@@@");
            imgClass = driver.findElement(By.className("cont_photo"));
         }catch(Exception  e){
            imgClass=null;
         }
         int count = 7;
         while (imgClass == null ) {
            Thread.sleep(100);
            try {
               System.out.println(count+" CATCH @@@@@@@@@@@@@@@@@@@@@@");
               imgClass = driver.findElement(By.className("cont_photo"));
            }catch(Exception  e){
               imgClass=null;
            }
            if(count ==0)break;
            count--;
         }
         

         String[] imgparse = null;
         try {
            WebElement imgClassss = imgClass.findElement(By.tagName("ul")).findElement(By.className("link_photo"));
            
            String imgUrl = imgClassss.getAttribute("style");
            imgparse = imgUrl.split("\"");
            
         }catch(Exception a) {
            imgparse = new String[] {"실패","https://k.kakaocdn.net/dn/XAvW8/btqEdjBckh2/EosBInJh04aYw0kD2QsaF0/img.png"};
         }
         returnList.put("imgUrl", imgparse[1]);
         
   
         //리뷰 , 별점 , 오픈시간 
         if(getLocationData==1) {
            
            //오픈시간 구하기
            try {
               WebElement dataClass = driver.findElement(By.className("openhour_wrap"));
               List<WebElement> officeHour = new ArrayList<WebElement>();
               officeHour.add(dataClass.findElement(By.tagName("ul")));//).findElements(By.className("list_operation"));
               for(WebElement li : officeHour) {
                  //상품 li에서 txt_comment라는 클래스를 가지고 있는 요소 안의 span 태그 요소 찾아서 그 안에 적힌 텍스트 긁어오기
                  List<WebElement> elemLi = li.findElements(By.className("txt_operation"));
                  for(WebElement elem : elemLi) {
                     String officeTime = elem.getText();
                     returnList.put("storeTime", officeTime);
                  }
               }
            }catch(Exception b) {
            	returnList.put("storeTime", " ");
            }
            
            
   
            //가게 점수 구하기
            try {
               WebElement starClass = driver.findElement(By.className("ahead_info")).findElement(By.className("num_rate"));
               String aaa = starClass.getText();
               returnList.put("starpoint", aaa);
            }catch(Exception c) {
               returnList.put("starpoint", "평점이 없습니다.");
            }
            

            
            //REVIEW 가지고 오기
            
            try {
               WebElement reviewDiv = driver.findElement(By.className("evaluation_review"));
               
               //카카오 div 안에서 ul을 태그를 찾은후 comment_info라는 클래스를 가지고 Review들을 받기( 리뷰 li 등 )
               List<WebElement> reviewClass = reviewDiv.findElement(By.tagName("ul")).findElements(By.className("comment_info"));
               int i=1;
               for(WebElement li : reviewClass) {
            	   
                  //productList에서 li태그들 하나씩 가져오면서 반복 돌기
                  //상품 li에서 title 이라는 클래스를 가지고 있는 요소를 찾아서 그 안에 적힌 텍스트 긁어오기
                  
                  //상품 li에서 txt_comment라는 클래스를 가지고 있는 요소 안의 span 태그 요소 찾아서 그 안에 적힌 텍스트 긁어오기
                  String reviewText = li.findElement(By.className("txt_comment")).findElement(By.tagName("span")).getText();
                  
                  returnList.put("review_"+i, reviewText);
                  i++;
               }
            }catch(Exception d) {
            	returnList.put("review_1", "리뷰없음");
            	returnList.put("review_2", "리뷰없음");
            	returnList.put("review_3", "리뷰없음");
            }
         }
      return returnList; 
   }
}