package com.howabout.there.findcourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class kakaoApi {

	//kakaoAPI이용 rest,cafe ListUp
	public ArrayList<JSONObject> listUp(ArrayList<JSONObject> inputData, String category) throws ParseException{
		
		// input data parse
		JSONObject obj = (JSONObject) inputData.get(0);
		String lon = (String) obj.get("x");
		String lat = (String) obj.get("y");
		String radius = (String) obj.get("radius");
	    
		String URL = "https://dapi.kakao.com/v2/local/search/category.json";
		String appkey = "KakaoAK 66be8808cdde00fd86beab9d744bdffc";
		String paramData = "category_group_code=" + category + "&x=" + lon + "&y=" + lat + "&radius=" + radius+ "&size=10";

//		http 요청 시 url 주소와 파라미터 데이터를 결합하기 위한 변수 선언
		String totalUrl = "";
		if (paramData != null && paramData.length() > 0 && !paramData.equals("") && !paramData.contains("null")) {
			totalUrl = URL.trim().toString() + "?" + paramData.trim().toString();
		} else {
			totalUrl = URL.trim().toString();
		}

//		http 통신을 위한 객체 선언
		URL url = null;
		HttpURLConnection connect = null;

//		http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
		String responseData = "";
		BufferedReader br = null;
		StringBuffer sb = null;

//		메소드 호출 결과값을 반환하기 위한 변수
		String returnData = "";
		ArrayList<JSONObject> data = new ArrayList<>();

		try {
//			파라미터로 들어론 url을 사용해 connection
			url = new URL(totalUrl);
			connect = (HttpURLConnection) url.openConnection();

//			http 요청에 필요한 타입 정의 실시
			connect.setRequestMethod("GET");
//			connect.setRequestProperty("content-type", "application/json");
			connect.setRequestProperty("Authorization", appkey);

//			http 요청
			connect.connect();

//			http 요청 후 응답받은 데이터 버퍼에 쌓기
			br = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
			sb = new StringBuffer();
			while ((responseData = br.readLine()) != null) {
				sb.append(responseData); // StringBuffer에 응답받은 데이터 순적적으로 저장
			}

//			메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
			returnData = sb.toString();

			JSONParser json = new JSONParser();
			JSONObject jobj = (JSONObject) json.parse(sb.toString());
			JSONArray jarray = (JSONArray) jobj.get("documents");

			// 반환값
			data.add(jobj);

			// http 요청 응답 코드 확인 실시
			String responseCode = String.valueOf(connect.getResponseCode());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	
	public ArrayList<String> getLocationInfo(String location_name, String num)throws ParseException{
		String query = null;
		try {
			query = URLEncoder.encode(location_name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String IMGURL = "https://dapi.kakao.com/v2/search/image?sort=accuracy&page=1&size="+num+"&query="+query;
		String appkey = "KakaoAK 66be8808cdde00fd86beab9d744bdffc";
		
//		http 통신을 위한 객체 선언
		URL url = null;
		HttpURLConnection connect = null;

//		http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
		String responseData = "";
		BufferedReader imgbr = null;
		StringBuffer imgsb = null;

//		메소드 호출 결과값을 반환하기 위한 변수
		String returnData = "";
		ArrayList<JSONObject> data = new ArrayList<>();
		JSONArray jarray=null;
		try {
//			파라미터로 들어론 url을 사용해 connection
			url = new URL(IMGURL);
			connect = (HttpURLConnection) url.openConnection();

//			http 요청에 필요한 타입 정의 실시
			connect.setRequestMethod("GET");
//			connect.setRequestProperty("content-type", "application/json");
			connect.setRequestProperty("Authorization", appkey);

//			http 요청
			connect.connect();

//			http 요청 후 응답받은 데이터 버퍼에 쌓기
			imgbr = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
			imgsb = new StringBuffer();
			while ((responseData = imgbr.readLine()) != null) {
				imgsb.append(responseData); // StringBuffer에 응답받은 데이터 순적적으로 저장
			}

//			메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
			returnData = imgsb.toString();

			JSONParser json = new JSONParser();
			JSONObject jobj = (JSONObject) json.parse(imgsb.toString());
			jarray = (JSONArray) jobj.get("documents");
			
			// 반환값
		

			// http 요청 응답 코드 확인 실시
			String responseCode = String.valueOf(connect.getResponseCode());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (imgbr != null) {
					imgbr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<String> returnImgValue = new ArrayList<>();
		for(int i=0; i< jarray.size(); i++) {
			JSONObject getJson = (JSONObject) jarray.get(i);
			returnImgValue.add(getJson.get("image_url").toString());
		}
		
		return returnImgValue;
	}
	
	
	
	
	
}

